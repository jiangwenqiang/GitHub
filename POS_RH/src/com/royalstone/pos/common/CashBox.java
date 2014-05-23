/*
 * Created on 2004-6-23
 */
package com.royalstone.pos.common;

import org.jdom.Element;

import com.royalstone.pos.util.InvalidDataException;
import com.royalstone.pos.util.Value;


/**	POSϵͳ�ڹ���������,Ҫ���ϵؼ�����������ڵ��ʽ����.����ֽ𳬹�һ��������,����Ҫ�������.
 * Ϊ��ʵ�ָù���,POS����������������: CashBasket, CashBox.
 * CashBasket ����װǮ�ĳ���,���а������CashBox. ÿ��CashBox ��װ��"Ǯ"����ͬһ����ͬһ�ұ�.
 * �������������ڵ��ֽ�,ϵͳҪ����������:"������"��"Ӳ����". 
 * ����ֽ𳬹�"������",ϵͳ�������ʾ,���Կɼ�������; �������"Ӳ����",��ֻ�������������.
 * CashBasket �ṩ���³�Ա���������ж��ֽ��Ƿ񳬳�����: exceedCashLimit, exceedCashMaxLimit.
 * POS�����ڹ���������,��һ��XML�ļ����������������Ϣ.����,CashBasket/CashBox ���ṩ����XML��ת���ķ���.
 * @author Mengluoyi
 */
public class CashBox {

	/**
	 * @param type			"Ǯ"������
	 * @param curren_code	�ұ�
	 * @param value			���(�Է�Ϊ��λ)
	 */
	public CashBox( int type, String curren_code, int value )
	{
		this.type 			= type;
		this.curren_code 	= curren_code;
		this.value 			= value;
	}
	
	/**
	 * @param elm	����"Ǯ����"��Ϣ��XML�ڵ�
	 * @throws InvalidDataException
	 */
	public CashBox( Element elm ) throws InvalidDataException
	{
		try{
			this.type 			= elm.getChild( "type" ).getTextTrim().charAt(0);
			this.curren_code 	= elm.getChild( "curren_code" ).getTextTrim();
			this.value 			= Integer.parseInt( elm.getChild( "value" ).getTextTrim() ); 
		} catch ( NullPointerException e ){
			throw new InvalidDataException( "CashBox" );
		}
	}
	
	/**
	 * @return	"Ǯ"������
	 */
	public int getType()
	{
		return type;
	}
	
	/**	�˺�����Ҫ������XML�ڵ�ʱʹ��.
	 * @return	"Ǯ"��������
	 */
	public String getTypeCode()
	{
		return "" + (char) type;
	}
	
	/**	
	 * @return	���
	 */
	public int getValue()
	{
		return value;
	}
	
	/**
	 * @return	���ִ���: RMB,HKD,USD.
	 */
	public String getCurrenCode()
	{
		return curren_code;
	}
	
	/**	�˺�����"Ǯ��"�����ӽ��Ϊvalue ��Ǯ.
	 * @param value	���ӵĽ��
	 */
	public void addValue( int value )
	{
		this.value += value;
	}
	
	/**
	 * for debug use.
	 */
	public String toString()
	{
		return "{ " + Payment.getTypeName(type) + ": " + curren_code + " " + ( new Value (value) ).toString() + " }";	
	}
	
	/**
	 * @return	XML�ڵ�.
	 */
	public Element toElement()
	{
		Element elm = new Element( "cashbox" );
		elm.addContent( ( new Element( "type" ).addContent( getTypeCode() )));
		elm.addContent( ( new Element( "curren_code" ).addContent( curren_code )));
		elm.addContent( ( new Element( "value" ).addContent( "" + value )));
		return elm;
	}
	
	/**
	 * <code>type</code>	�˱�����ʾǮ������װ��"Ǯ"������: �ֽ�,��ȯ,��ֵ��.
	 */
	private int type;
	
	/**
	 * <code>curren_code</code>	�ұ�: RMB, HKD, USD.
	 */
	private String curren_code;
	
	/**
	 * <code>value</code>	���(�Է�Ϊ��λ).
	 */
	private int value;
}
