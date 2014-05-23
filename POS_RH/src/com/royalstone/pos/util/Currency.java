package com.royalstone.pos.util;

/**
 * Currency ���ڱ�ʾPOSϵͳ�еĻ��Ҵ���. Ŀǰ,POSֻ֧�����ֻ���:�����,�۱�,��Ԫ.
   @version 1.0 2004.05.14
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

final public class Currency
{
	Currency( String c )
	{
		code = c;
	}

	public String getCode()
	{
		return new String(code);
	}

	public String getName()
	{
		return new String( code );
	}

	public String getUnit()
	{
		return new String( "yuan" );
	}

	boolean	equals(Currency c )
	{
		return code.equals( c.getCode() );
	}

	boolean	equals(String currencode )
	{
		return code.equals( currencode );
	}

	/**
	 * <code>RMB</code>
	 * �����<br/>
	 * <code>HKD</code>
	 * �۱�<br/>
	 * <code>USD</code>
	 * ��Ԫ<br/>
	 */
	final public static String	RMB = "RMB";
	final public static String	HKD = "HKD";
	final public static String	USD = "USD";
	private String	code;
}
