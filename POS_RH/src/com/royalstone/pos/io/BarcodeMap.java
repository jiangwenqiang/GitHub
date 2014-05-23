package com.royalstone.pos.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.royalstone.pos.common.Goods;

/**
 * ����ת����
 * Ϊ�˴���ĳЩ������Ʒ,POSϵͳ�����˼�����������:������,�����,���ؽ����.���������һ��������Ʒ����,һ�������ʾ��Ʒ����������.
 * POS���������Ʒ�����,������Ҫ���з���,���Ƿ���������.
 * �������������,�������Ӧ�Ĺ����������ȡ����Ʒ����������,�ٲ�ѯ��Ʒ�۸�����Ƶ���Ϣ.
 * POS����ʹ��BarcodeFormat ģ��һ����������"�任��", �� BarcodeMap ʵ�ֶ�����ļ��ͱ任. 
 * BarcodeMap �а����ж�� BarcodeFormat.
 * �ϲ�ģ����Ҫʹ��BarcodeMap������������ɱ任: matches, convert. 
 * @version 1.0 2004.05.11
 * @author  Mengluoyi, Royalstone Co, Ltd.
 */

public class BarcodeMap 
{
	/**
	 * constructor.
	 */
	public BarcodeMap ( )
	{
		map = new Vector();
	}

	/**	�˷�����һ���ı��ļ���װ�����������ʽ����.
	 * @param ���������ʽ�����ļ�.
	 */
	public void load ( String mapfile )
	{
		System.err.println( "Load BarcodeMap from " + mapfile + " ... " );
		BufferedReader in;
		String s;

		try {
			in = new BufferedReader ( new FileReader ( mapfile ) );
			while ( ( s = in.readLine() ) != null )	add ( s );
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println( "Load failed. File not found." );
		} catch (IOException e) {
			System.err.println( "Load failed. IOException." );
		}
		System.err.println( "BarcodeMap Loaded." );
	}

	/**	����XML�����ļ�������任����г�ʼ��.
	 * @param file	 �������������붨����Ϣ��POS�����ļ�.
	 */
	public void fromXML( String file )
	{
		try{
			Document  doc    = (new SAXBuilder()).build(new FileInputStream( file ) );
			Element   root   = doc.getRootElement();
			Element   config = root.getChild( "barcodemap" );

			List parms = config.getChildren( "barcodeformat" );

			for (int i = 0; i < parms.size(); i++)
			{
				Element parm_item = (Element) parms.get(i);
				String  bformat   = parm_item.getTextTrim();
                System.out.println ( bformat );
				this.add( bformat );
			}
		}
		catch ( Exception e ){
			e.printStackTrace();
		}
	}

	/**	������任�������һ���������붨��.
	 * @param sformat	�������붨���ִ�.
	 */
	public void add ( String sformat )
	{
		map.add( new BarcodeFormat( sformat ) );
	}

	/**	������任�������һ���������붨��.
	 * @param bformat	��������任��.
	 */
	public void add ( BarcodeFormat bformat )
	{
		map.add ( bformat );
	}

	/**	�����Ʒ��code �Ƿ����������붨����ƥ��.
	 * @param code
	 * @return	true	code����������;<br/> false	code������������;
	 */
	public boolean matches ( String code )
	{
		for( int i=0; i < map.size(); i++ ){
			BarcodeFormat bformat = ( BarcodeFormat ) map.get(i);
			if ( bformat.matches( code ) ) return true;
		}
		return false;
	}

	/**	�˷�����ԭʼ������������н����ͱ任,���ɹ淶��������(��У��λ)����Ʒ��������.
	 * @param code	ԭʼ��������.
	 * @return	��У��λ�Ĺ淶����,��Ʒ��������.
	 * @see	PosInputGoods
	 */
	public PosInputGoods convert ( String code )
	{
		for( int i=0; i < map.size(); i++ ){
			BarcodeFormat bformat = ( BarcodeFormat ) map.get(i);
			if ( bformat.matches( code ) ) return bformat.convert( code );
		}
		return new PosInputGoods( code, 1 );
	}

	/**	ȡ���������ʽ��ĳ���.
	 * @return	���������ʽ��ĳ���.
	 */
	public int size ()
	{
		return map.size();
	}

	/**
	 * <code>map</code><br/>�˱����д�����������ʽ����.
	 */
	private Vector map;
}

/**	BarcodeFormat ����ģ��"��������任��". �䷽��convert����ɶ�ԭʼ������Ľ�����任.
 * POSϵͳ��,���������ʽ���������������ĸ�ʽ: 22xxxxxwwwwwc. 
 * ����,��ǰ������ֲ���(22)Ϊ����ͷ��,��ԭʼ������ƥ���ʽ���������; 
 * �ڶ�����(xxxxx)Ϊ��Ʒ����,��������(wwwww)Ϊ��������, ���Ĳ���ΪУ��λ. 
 * @author Mengluoyi
 */
class BarcodeFormat
{
	/**
	 * @param fmt	������Ʒ��Ķ����ִ�. ��:22xxxxxwwwwwc.
	 */
	public BarcodeFormat ( String fmt )
	{
		format = fmt;
		head   = getHead( fmt );
	}

	/**	�ж�ԭʼ����Ʒ���Ƿ����ʽ����ƥ��.
	 * �ж������ж�: �����볤���Ƿ����ʽ�������; ������ͷ���Ƿ����ʽ����ͷ���ִ�ƥ��.
	 * @param code
	 * @return
	 */
	public boolean matches( String code )
	{
		return  ( code.length() == format.length() )
			&& code.startsWith( head ) ;
	}

	/**	��ԭʼ��������Ʒ�����任.
	 * @param code	ԭʼ����Ʒ������.
	 * @return		���������ͱ任�����Ʒ����,��Ʒ��������Ʒ���.
	 */
	public PosInputGoods convert ( String code )
	{
		PosInputGoods g;
		String stdcode  = "";
		String sw = "";
		String sm = "";
		int    weight, money;
		int    gt;

		if ( !matches( code ) ) g =  new PosInputGoods ( code );
		else {
			for ( int i=0; i<format.length(); i++ ){
				char cfmt, c;
				cfmt = format.charAt(i);
				c  = code.charAt(i);
				if( ( cfmt >= '0' && cfmt <= '9' ) || cfmt == 'x' ) stdcode += c;
				if( cfmt == 'w' || cfmt == 'm' ) stdcode += '0';
				if( cfmt == 'w' ) sw += c;
				if( cfmt == 'm' ) sm += c;
			}

			stdcode += getChkChar( stdcode );
			try
			{
				weight = Integer.parseInt ( sw );
			}
			catch (NumberFormatException e)
			{
				weight = 0;
			}

			try
			{
				money = Integer.parseInt ( sm );
			}
			catch (NumberFormatException e)
			{
				money = 0;
			}

			if( weight > 0 && money == 0 ) gt = Goods.WEIGHT;
			else if( weight == 0 && money > 0 ) gt = Goods.VALUE;
			else gt = Goods.WEIGHT_VALUE;
			g = new PosInputGoods ( stdcode, weight, money, gt, code );

		}
		return g;
	}

	/**	�������λ.
	 * @param rawcode	�޼������Ʒ����.
	 * @return			����λ.
	 */
	public static char getChkChar ( String rawcode )
	{
		int len = rawcode.length();
		int sum_odd  = 0;
		int sum_even = 0;
		int sum_last;
		int chk;

		for ( int i = len - 2; i >= 0; i -= 2 ) sum_even += rawcode.charAt( i ) - '0';
		for ( int i = len - 1; i >= 0; i -= 2 ) sum_odd  += rawcode.charAt( i ) - '0';

		sum_last = ( sum_even + sum_odd * 3 ) % 10;
		if ( sum_last > 0 ) chk = 10 - sum_last;
		else chk = sum_last;
		return (char) ( chk + '0' );
	}

	/**	���������ʽ�����ͷ���ִ�.
	 * @param fmt	�����ʽ����.
	 * @return		��ʽ�����ͷ���ִ�.
	 */
	private String getHead ( String fmt )
	{
		String h = "";

		for( int i=0; i< fmt.length() ; i++ ){
		    char ch;
			ch = fmt.charAt( i );
			if ( ch < '0' || ch > '9' )break;
			h += ch;
		}
		return h;
	}

	/**
	 * Comment for <code>head</code><br/>
	 * ������/������ͷ��. ����, 22xxxxxwwwwwc ��ͷ��Ϊ22.
	 */
	private String head;
	
	/**
	 * <code>format</code><br/>
	 * �����ĳ�����/�����Ķ����ִ�. ��, 22xxxxxwwwwwc.
	 */
	private String format;
}
