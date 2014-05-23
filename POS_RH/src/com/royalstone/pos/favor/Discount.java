package com.royalstone.pos.favor;

/**
   @version 1.0 2004.05.25
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

/**		POS����֧�ֶ����ۿ�. Discount Ϊ���־����ۿ۵ĸ���.
 * NOTE: ���´���ĺ����д���ȷ : 
 * @author Mengluoyi
 */
public class Discount
{
	/**
	 * @param t		�ۿ����ʹ���
	 */
	public Discount( int t )
	{
		type = t;
	}

	/**
	 * @return		�ۿ����ʹ���
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * @return		�ۿ���������
	 */
	public String getTypeName()
	{
		if( type == NONE ) 			return "�ۿ۸���";
		if( type == QUANTITY ) 		return "�����ۿ�";
		if( type == DEPT ) 			return "С���ۿ�";
		if( type == PERIOD ) 		return "ʱ���ۿ�";
		if( type == PROMOTION ) 	return "����";
		if( type == MEMBERPRICE ) 	return "��Ա����";
		if( type == MEMBERDISC ) 	return "��Ա�ۿ�";
		if( type == MANUAL ) 		return "�ֹ��ۿ�";
		if( type == ALTPRICE ) 		return "���";
		if( type == COMPLEX ) 		return "��ϴ���";
		if( type == BULK ) 			return "��������";
		if( type == SINGLE ) 		return "�����ۿ�";
		if( type == TOTAL ) 		return "�ܶ��ۿ�";
		if( type == MONEY ) 		return "����ۿ�";
		if( type == LOANDISC )      return "���ʿ��ۿ�";
        if( type == MEMBERDEPT )    return "��Ա�����ۿ�";
        if( type == VIPPROM )       return "VIP�ۿ�";
        if( type == VIPPROMPROM )   return "VIP������";
        if( type == BUYGIVE)        return "����";
        if(	type == LARGESS)		return "��Ʒ����";
        if( type == Change)			return "�һ�";
        if( type == LARGESSC)       return "��ȯ";
		
		return "����";
	}

	/**
	 * <code>NONE</code>	���ۿ�
	 */
	final public static int NONE       = 'n';
	/**l
	 * <code>QUANTITY</code>	�����ۿ�
	 */
	final public static int QUANTITY   = 'v';
	/**
	 * <code>DEPT</code>		С���ۿ�
	 */
	final public static int DEPT       = 'd';
	/**
	 * <code>PERIOD</code>		ʱ���ۿ�
	 */
	final public static int PERIOD     = 't';
	/**
	 * <code>PROMOTION</code>	����
	 */
	final public static int PROMOTION  = 'p';
	/**
	 * <code>MEMBERPRICE</code>	��Ա����
	 */
	final public static int MEMBERPRICE= 'h';
	/**
	 * <code>MEMBERDISC</code>	��Ա�ۿ�
	 */
	final public static int MEMBERDISC = 'k';
	/**
	 * <code>MANUAL</code>		�ֹ��ۿ�
	 */
	final public static int MANUAL     = 'Z';
	/**
	 * <code>ALTPRICE</code>	���
	 */
	final public static int ALTPRICE   = 'c';
	/**
	 * <code>COMPLEX</code>		��ϴ���
	 */
	final public static int COMPLEX    = 's';
	/**
	 * <code>BULK</code>		��������
	 */
	final public static int BULK       = 'B';
	/**
	 * <code>SINGLE</code>		�����ۿ�
	 */
	final public static int SINGLE     = 'i';
	/**
	 * <code>TOTAL</code>		�ܶ��ۿ�
	 */
	final public static int TOTAL      = 'T';
	/**
	 * <code>MONEY</code>		����ۿ�
	 */
	final public static int MONEY      = 'm';
    /**
     * <code>LOANDISC</code>	���ʿ��ۿ�
     */
    final public static int LOANDISC   = 'o';
    //��Ա�����ۿ�
    final public static int MEMBERDEPT   = 'M';
    //VIP�ۿ�
    final public static int VIPPROM   = 'V';
    //VIP������
    final public static int VIPPROMPROM   = 'I';

    final public static int BUYGIVE   = 'l';
    // ��Ʒ�����ۿ�
    final public static int LARGESS = 'A';
    // ����
    final public static int  LARGESSL = 'B';
    // �һ�
    final public static int Change='E';
    // ��ȯ
    final public static int LARGESSC = 'F';


	/**
	 * <code>type</code>�ۿ�����
	 */
	private int type = NONE;
}
