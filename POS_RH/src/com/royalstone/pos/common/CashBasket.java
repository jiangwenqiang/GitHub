/*
 * Created on 2004-6-23
 *
 */
package com.royalstone.pos.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.util.ExchangeList;
import com.royalstone.pos.util.InvalidDataException;
import com.royalstone.pos.util.PosConfig;

/**	POSϵͳ�ڹ���������,Ҫ���ϵؼ�����������ڵ��ʽ����.����ֽ𳬹�һ��������,����Ҫ�������.
 * Ϊ��ʵ�ָù���,POS����������������: CashBasket, CashBox.
 * CashBasket ����װǮ�ĳ���,���а������CashBox. ÿ��CashBox ��װ��"Ǯ"����ͬһ����ͬһ�ұ�.
 * �������������ڵ��ֽ�,ϵͳҪ����������:"������"��"Ӳ����". 
 * ����ֽ𳬹�"������",ϵͳ�������ʾ,���Կɼ�������; �������"Ӳ����",��ֻ�������������.
 * CashBasket �ṩ���³�Ա���������ж��ֽ��Ƿ񳬳�����: exceedCashLimit, exceedCashMaxLimit.
 * POS�����ڹ���������,��һ��XML�ļ����������������Ϣ.����,CashBasket/CashBox ���ṩ����XML��ת���ķ���.
 * @author Mengluoyi
 */
public class CashBasket 
{

	/**
	 * Constructor
	 */
	public CashBasket()
	{
		boxes = new Vector();	
	}
	
	/**
	 * @param elm
	 * @throws InvalidDataException
	 */
	public CashBasket( Element elm ) throws InvalidDataException
	{
		try{
			boxes = new Vector();	

			List list = elm.getChildren( "cashbox" );

			for (int i = 0; i < list.size(); i++)
			{
				Element item = (Element) list.get(i);
				boxes.add( new CashBox(item) );
			}
		
			Element elm_exch = elm.getChild( "exchange_list" );
			exch_lst = new ExchangeList( elm_exch );
			String limit_str = elm.getChildTextTrim( "cash_limit" );
			cash_limit = Integer.parseInt( limit_str );
		} catch ( Exception e ){
			throw new InvalidDataException();
		}
	}
	
	/**	�˺���������������Ϣ���浽һ���ļ�file ��.
	 * @param file	��������������Ϣ���ļ�
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void dump( String file ) throws FileNotFoundException, IOException
	{
		XMLOutputter outputter = new XMLOutputter( "  ", true, "GB2312" );
		outputter.output(new Document(this.toElement()),  new FileOutputStream( file ));
	}
	
	/**
	 * @param file
	 * @return
	 * @throws InvalidDataException
	 */
	public static CashBasket load( String file ) throws InvalidDataException
	{
		CashBasket basket = null;
		Document doc;
		try {
			doc = (new SAXBuilder()).build( file );
			Element   root   = doc.getRootElement();
			basket = new CashBasket( root );
		} catch (JDOMException e) {
			throw new InvalidDataException( e );
		}
		return basket;
	}
	
	/**	 �˺������ֽ��������ʼ������.
	 * ֻ���ֽ��������Ҫװ���,֧Ʊ,��ȯ��ֻ��Ҫ���������.
	 */
	public void init()
	{
		put( Payment.CASH, 		"RMB", 0 );
		put( Payment.CASH, 		"HKD", 0 );
		put( Payment.CHEQUE, 	"RMB", 0 );
		put( Payment.VOUCHER, 	"RMB", 0 );
		put( Payment.CARDBANK, 	"RMB", 0 );
		put( Payment.CARDSHOP, 	"RMB", 0 );
		put( Payment.CARDLOAN, 	"RMB", 0 );
		put( Payment.FLEE, 		"RMB", 0 );
		put( Payment.SAMPLE, 	"RMB", 0 );
		put( Payment.OILTEST, 	"RMB", 0 );
		put( Payment.couponPay, "RMB", 0 );
	}
	
	/**
	 * 
	 */
	public void reset()
	{
		boxes = new Vector();	
	}
	
	/**
	 * @return
	 */
	public int size()
	{
		return boxes.size();
	}
	
	/**
	 * @param i
	 * @return
	 */
	public CashBox get(int i)
	{
		return (CashBox) boxes.get(i);
	}
	
	/**
	 * @param type
	 * @param curren_code
	 * @return
	 */
	public CashBox getBox( int type, String curren_code )
	{
		for( int i=0; i<boxes.size(); i++ ){
			CashBox box = ( CashBox ) boxes.get(i);
			if( type == box.getType() && curren_code.equals( box.getCurrenCode() ) ){
				return box;
			}
		}
		return null;
	}
	
	/**	����������һϵ�е�CashBox���. ÿ��CashBox ��ֻ����װͬһ�ұ�ͬһ���͵�"Ǯ". 
	 * ���������з�Ǯʱ, �ҵ��˺��ʵĺ���,�ͷ������������, ����Ҳ������ʵĺ���,���ڳ���������һ������.
	 * @param type
	 * @param curren_code
	 * @param value
	 */
	public void put( int type, String curren_code, int value )
	{
		PosContext context=PosContext.getInstance();
		
		if(!context.isTraining()){

			for( int i=0; i<boxes.size(); i++ ){
				CashBox box = ( CashBox ) boxes.get(i);
				// �ҵ��˺��ʵĺ���,�Ͱ�Ǯ�������������.
				if( type == box.getType() && curren_code.equals( box.getCurrenCode() ) ){
					box.addValue( value );
					return;
				}
			}
		
			//  ����Ҳ������ʵĺ���,���ڳ���������һ������.
			boxes.add( new CashBox( type, curren_code, value ) );
			
		}
		
	}
	
	/**
	 * @return
	 */
	public Element toElement()
	{
		Element elm = new Element( "cashbasket");
		for( int i=0; i<size(); i++ ){
			CashBox box = (CashBox) this.get(i);
			elm.addContent( box.toElement() );	
		}
		elm.addContent( exch_lst.toElement() );
		Element elm_limit = new Element( "cash_limit" ).addContent( "" + cash_limit );
		elm.addContent( elm_limit );
		return elm;
	}
	
	/**	���û��ʱ�
	 * @param list	���ʱ�
	 */
	public void setExchange( ExchangeList list )
	{
		exch_lst = list;
	}
	
	/**
	 * @param limit
	 */
	public void setCashLimit( int limit )
	{
		cash_limit = limit;
	}
	
	/**
	 * @return
	 * @throws InvalidDataException
	 */
	public int getCashTotal() throws InvalidDataException
	{
		int total = 0;
		for( int i=0; i< boxes.size(); i++ ){
			CashBox box = (CashBox) boxes.get(i);
			double rate = exch_lst.getRate( box.getCurrenCode() );
			if( box.getType() == Payment.CASH ) total += box.getValue() * rate;
		}
		return total;
	}
	
	/**
	 * @return
	 */
	public boolean exceedCashLimit()
	{
		try {
			return ( getCashTotal() > cash_limit );
		} catch (InvalidDataException e) {
			return false;
		}
	}
	
	/**
	 * @return
	 */
	public boolean exceedCashMaxLimit()
	{
		try {
			cash_maxlimit = Integer.parseInt(PosConfig.getInstance().getString("CASH_MAXLIMIT")) * 100;
			return ( getCashTotal() > cash_maxlimit );
		} catch (InvalidDataException e) {
			return false;
		}
	}
	

	/**
	 * @return
	 */
	public int CompareLimit(){
		try{
		cash_maxlimit = Integer.parseInt(PosConfig.getInstance().getString("CASH_MAXLIMIT")) * 100;
		if(cash_limit < cash_maxlimit){
			if(cash_limit < getCashTotal() && getCashTotal() < cash_maxlimit ){
				return 1;
			}else if(getCashTotal() > cash_maxlimit){
				return 2;
			}else{
				return 0;
			}
			
		} else{
			if(cash_limit < getCashTotal()){
				return 1;
			}else{
				return 0;
			}
			
		}
		
		}catch(InvalidDataException e){
			return 0;
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	 * <code>boxes</code>		�˱���ģ��װǮ��"����".
	 */
	private Vector boxes = null;
	
	/**
	 * <code>exch_lst</code>	���ʱ�
	 */
	private ExchangeList exch_lst = null;
	
	/**
	 * <code>cash_limit</code>		�������ֽ�����
	 */
	private int cash_limit = 0;
	
	/**
	 * <code>cash_maxlimit</code>	ǿ�����ֽ�����
	 */
	private int cash_maxlimit = 0 ;
}
