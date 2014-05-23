package com.royalstone.pos.common;

import java.io.Serializable;

import com.royalstone.pos.util.Currency;
import com.royalstone.pos.util.Day;
import com.royalstone.pos.util.PosTime;

/**	Payment ģ��˿͵�֧����Ϊ,��������Ϊ.
   @version 1.0 2004.05.12
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

final public class Payment implements Serializable
{
	/**
	   @param v value paid.
	 */
	public Payment( int v )
	{
		value = v;
		type   = CASH;
		reason = PAY;
		curren_code = Currency.RMB;
	}

	public Payment( int r, int v )
	{
		reason = r;
		value  = v;
		value_equiv = v;
		type   = CASH;
		curren_code = Currency.RMB;
		cardno = "" ;
	}

	public Payment( int reason, int type, int value )
	{
		this.reason = reason;
		this.type 	= type; 
		this.value_equiv = this.value  = value;
		this.curren_code = Currency.RMB;
		this.cardno 	 = "";
	}

	public Payment( int reason, int type, String curren_code, int value, int value_equiv )
	{
		this.reason = reason;
		this.type 	= type; 
		this.value  = value;
		this.value_equiv = value_equiv;
		this.curren_code = curren_code;
	}


	/**
	   @param r payment reason.
	   @param t payment type.
	   @param curren_code currency code.
	   @param v value in the currency paid.
	   @param cardno card number, cheque number.
	 */
	public Payment( int reason, int type, String curren_code, int value, int value_equiv, String cardno )
	{
		this.reason = reason;
		this.type = type; 
		this.value  = value;
		this.value_equiv = value_equiv;
		this.curren_code =  curren_code ;
		this.cardno = cardno;
	}

	public Payment( int reason, int type, String curren_code, int value, int value_equiv, String cardno,
			Day sysdate, PosTime systime )
	{
		this.reason = reason;
		this.type = type; 
		this.value  = value;
		this.value_equiv = value_equiv;
		this.curren_code = new String ( curren_code );
		this.cardno = new String(cardno);
		this.sysdate = sysdate;
		this.systime = systime;
	}
	
	public Payment( int reason, int type, String curren_code, int value, int value_equiv, String cardno,
			int trainflag, Day sysdate, PosTime systime )
	{
		this.reason = reason;
		this.type = type; 
		this.value  = value;
		this.value_equiv = value_equiv;
		this.curren_code = new String ( curren_code );
		this.cardno = new String(cardno);
		this.trainflag = trainflag;
		this.sysdate = sysdate;
		this.systime = systime;
	}




	public Day getSysDate()			{	return sysdate; }
	public PosTime getSysTime() 	{	return systime; }
	public int getReason()			{	return reason; 	}
	public int getType()			{	return type; 	}
	public String getCurrenCode()	{ 	return curren_code; }
	public int getValue()			{	return value; 	}
	public int getValueEquiv() 		{ 	return value_equiv; }
	public String getCardno() 		{ 	return cardno; 	}
	public String getReasonCode()	{	return "" + (char) reason; 	}
	
	public String getTypeCode()		
	{
		int t = ( type == CARDLOAN )? CARDSHOP : type; 
			return "" + (char) t;	
	}


	public static String getTypeName( int t )
	{
		if( t == CASH       ) return "�ֽ�";
		if( t == CHEQUE     ) return "֧Ʊ";
		if( t == VOUCHER    ) return "����ȯ";
		//if( t == CARDSHOP   ) return "�����";���������������ܣ�������������
		if( t == CARDSHOP   ) return "��ֵ��";
		if( t == CARDMEMBER ) return "��Ա��";
		if( t == CARDBANK   ) return "���п�";
		if( t == CARDLOAN   ) return "���ʿ�";
		if( t == FLEE   	) return "�ӳ�";
		if( t == SAMPLE  	) return "�������";
		if( t == OILTEST   	) return "�ͻ�����";
		if( t == ICCARD   	) return "IC��";
        if( t == Coupon  	) return "ȯ";
        if (t == BRINGFORWARD ) return "�����";
        if (t == PlanPoint ) return "���ֶһ�";
        if (t == couponPay ) return "ȯ֧��";
        if (t == RaCard)     return "����ֵ";
        if (t == CARDRKSHOP) return "�ٻ���";
		return "UNDEFINED";
	}

	public static String getReasonName( int r )
	{
		if( r == PAY    ) return "֧��";
		if( r == CHANGE ) return "����";
		if( r == PSEUDO ) return "����";
		if( r == FLEE   	) return "�ӳ�";
		if( r == SAMPLE  	) return "�������";
		if( r == OILTEST   	) return "�ͻ�����";
		return "UNDEFINED";
	}

	public void setAsTraining()
	{
		trainflag = 1;
	}
	
	public void setAsDeleted()
	{
		trainflag = 2;
	}

	public void setTrainFlag( int flag )
	{
		trainflag = flag;
	}

	public int getTrainFlag()
	{
		return trainflag;
	}
	
	public String toString()
	{
		return "{ " + getReasonName(reason) + ", "+ getTypeName(type) + ", " + curren_code 
			+ " $" + value + "; " + value_equiv + ", " + cardno + ", " 
			+ sysdate + ", " + systime + " }";
	}

	/**
	 * <code>CASH</code>	�ֽ�
	 */
/*
paytype         O   �����
paytype        	C	�ֽ�           
paytype        	D	����ȯ         
paytype        	K	֧Ʊ           
paytype        	L	���ʿ�         
paytype        	R	���п�         
paytype        	V	�����         
pay_reason     	R	����           

*/
	/**
	 * <code>BRINGFORWARD</code>  ����� 
	 */
	final public static int BRINGFORWARD = 'O';
	
	
	final public static int	CASH		= 'C';
	/**
	 * <code>CHEQUE</code>	֧Ʊ
	 */
	final public static int	CHEQUE		= 'K';
	/**
	 * <code>VOUCHER</code>	��ȯ
	 */
	final public static int	VOUCHER		= 'D';
	/**
	 * <code>CARDSHOP</code>��ֵ��
	 */
	final public static int	CARDSHOP	= 'V';
	/**
	 * <code>CARDSHOP</code>�ٻ���
	 */
	final public static int	CARDRKSHOP	= 'W';
	/**
	 * <code>CARDMEMBER</code>��Ա��
	 */
	final public static int	CARDMEMBER	= 'M';
	/**
	 * <code>CARDBANK</code>���п�
	 */
	final public static int	CARDBANK	= 'R';
	/**
	 * <code>CARDLOAN</code>���ʿ�
	 */
	final public static int	CARDLOAN	= 'L';
	
	/**
	 * <code>FLEE</code>	�ӳ�
	 */
	final public static int	FLEE		= 'F';
	/**
	 * <code>SAMPLE</code>	����
	 */
	final public static int	SAMPLE		= 'S';
	/**
	 * <code>OILTEST</code>	�ͻ�����
	 */
	final public static int	OILTEST		= 'T';

	/**
	 * <code>PAY</code>		�˿�֧��
	 */
	final public static int PAY 		= 'P';
	/**
	 * <code>CHANGE</code>	����
	 */
	final public static int CHANGE 		= 'R';
	/**
	 * <code>PSEUDO</code>	
	 */
	final public static int PSEUDO		= 'U';
	/**
	 * <code>CASHIN</code>	���
	 */
	final public static int CASHIN 		= 'I';
	/**
	 * <code>CASHOUT</code>	����
	 */
	final public static int CASHOUT 	= 'D';
	//��
    final public static int Coupon 	= 'Q';
 	//���ֶһ�
    final public static int PlanPoint 	= '8';
 	/**
 	 * <code>VOUCHER</code>	�±�ȯ ֧��
 	 */
 	final public static int	couponPay		= 'E';
	/**
	 * <code>ICCARD</code>	IC��֧��
	 */
	final public static int ICCARD='A';
	//��ֵ����ֵ
	final public static int RaCard='X';
	
	
	private int trainflag = 0;
	private int reason;
	private int type;
	private String curren_code = Currency.RMB;
	private int value, value_equiv;
	private String cardno = "";
	
	private PosTime systime = new PosTime();
	private Day	sysdate = new Day();
}
