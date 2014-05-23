package com.royalstone.pos.common;

final public class PosFunction
{
	public static String toString( int key )
	{
		// ��ʽ�ĸĶ�
		if (key == RESERVE)		return "RESERVE";
		if (key == ENTER) 		return "ENTER";
		if (key == CANCEL) 		return "CANCEL";
		if (key == QUANTITY) 	return "QUANTITY";
		if (key == RMB) 		return "RMB";
		if (key == HKD) 		return "HKD";
		if (key == USD) 		return "USD";
		if (key == CASH) 		return "CASH";
		if (key == CHEQUE) 		return "CHEQUE";
		if (key == VOUCHER) 	return "VOUCHER";
		if (key == CARDSHOP) 	return "CARDSHOP";
		if (key == CARDMEMBER) 	return "CARDMEMBER";
		if (key == CARDBANK) 	return "CARDBANK";
		if (key == CORRECT) 	return "CORRECT";
		if (key == QUICKCORRECT) 	return "QUICKCORRECT";
		if (key == WITHDRAW) 	return "WITHDRAW";
		if (key == ALTPRICE) 	return "ALTPRICE";
		if (key == DISCOUNT) 	return "DISCOUNT";
		if (key == DISCTOTAL) 	return "DISCTOTAL";
		if (key == DISCMONEY) 	return "DISCMONEY";
		if (key == SUBTOTAL) 	return "SUBTOTAL";
		if (key == TOTAL) 		return "TOTAL";
		if (key == EXIT) 		return "EXIT";
		if (key == NEWPASS) 	return "NEWPASS";
		if (key == LOCK) 		return "LOCK";
		if (key == HOLD) 		return "HOLD";
		if (key == CASHOUT) 	return "CASHOUT";
		if (key == FIND)		return "FIND";
		if (key == CASHIN) 		return "CASHIN";
		if (key == DELETE) 		return "����ɾ��";
		if (key == UP) 			return "UP";
		if (key == DOWN) 		return "DOWN";
		if (key == BACKSPACE) 	return "BACKSPACE";
		if (key == CLEAR) 		return "CLEAR";
		if (key == GOODS) 		return "GOODS";
		if (key == PAYMENT) 	return "PAYMENT";
		if (key == PRICE) 		return "PRICE";
		if (key == PIN) 		return "PIN";
		if (key == LOGON) 		return "LOGON";
		if (key == FLEE)		return "�ӳ�";
		if (key == SAMPLE)		return "�������";
		if (key == OILTEST)		return "�ͻ�����";
		if (key == ALTVAVLUE)	return "�ذ��޸�";
        if (key == OPENCASHBOX)	return "OPENCASHBOX";
        if (key == CardRA)      return "RaCard";
        
        
		if (key == OFFLINECLOSEWORKTURN)    return "OFFLINECLOSEWORKTURN";
		// ����������ֵ Ԥ���������
		// 301 302
		if (key == FUTRUESELL)				return "����Ԥ����";
		if (key == BRINGFORWARD)			return "BringForward";
		
		// ��������.��������.����..
		if (key == LARGESS)					return "ȷ������";
		if (key == GROUPLARGESS)			return "ȷ����������";
		if (key == LARGESSL)				return "ȷ������";
		if (key == LARGESSCOUPON)           return "ȷ����ȯ";
		if (key == PROPERTY)				return "ѡ����������";
		
		return "UNDEFINED";
	}
	
	public final static int	RESERVE		= 100;		
	public final static int	ENTER		= 10;		// ȷ��
	public final static int	CANCEL		= 89;		// ����
	public final static int	QUANTITY	= 66;		// ����
	public final static int	HKD			= 85;			 
	public final static int	RMB			= 86;			
	public final static int	USD			= 87;
	public final static int	CASH		= 71;		// �ֽ�
	public final static int	CHEQUE		= 69;		// ֧Ʊ
	public final static int	VOUCHER		= 68;		// ����ȯ
	public final static int	CARDSHOP	= 74;		// �����
	public final static int	CARDMEMBER	= 34;		// ��Ա��
	public final static int	CARDBANK	= 75;		// ���п�
	public final static int	CARDLOAN	= 83;		// ���˿�//���˿�����ɫ��
	public final static int	CORRECT		= 67;		// ����
	public final static int	QUICKCORRECT 	= 73;	// ����
	public final static int	WITHDRAW	= 70;		// �˻�
	public final static int	ALTPRICE	= 77;		// ���
	public final static int	DISCOUNT	= 123;      // �����ۿ�
	public final static int DISCTOTAL	= 125;		// �ܶ��ۿ�
	public final static int ALTVAVLUE	= 122;		// �ذ��޸�
	public final static int	SUBTOTAL	= 80;		// С��
	public final static int	TOTAL		= 79;		// �ϼ�
	public final static int	EXIT		= 82;		// �˳�
	public final static int	NEWPASS		= 78;		// ������
	public final static int	LOCK		= 84;		// ����/����
	public final static int	HOLD		= 72;		// �ҵ�
	public final static int  FIND       = 91;    	// ��ѯ�۸�
	public final static int	CASHOUT		= 62;		// ����
	public final static int	CASHIN		= 60;		// ���
	public final static int	DELETE		= 38;		// ����ȡ��
	public final static int WAITER		= 40;		// ӪҵԱ����
	public final static int DISCMONEY	= 41;		// ����ۿ�
	public final static int PRINTLASTSHEET = 43;	// �ش���һ��
	public final static int RETPAYMENT	= 63;		// �˿�
	public final static int DELIVERY	= 76;		// �ͻ�
	public final static int S2G			= 81;		// ���й���ת��
	public final static int COLORSIZE	= 83;		// ɫ��
	public final static int GROUP		= 90;		// ����
	public final static int OFFLINE		= 124;		// ��/�����л�
	public final static int PROPERTY	= 94;		// ��Ʒ���Լ�
	// internal functions
	public final static int	BACKSPACE	= 8;		// �˸�
	public final static int	CLEAR		= 88;		// ���
	public final static int SHIFT		= 96;		// ���
	public final static int FLEE		= 97;		// �ӳ�
	public final static int SAMPLE		= 98;		// �������
	public final static int OILTEST		= 99;		// �ͻ�����
	public final static int	POINT		= 46;
	public final static int BIZERO		= 200;		// "00"
	public final static int	UP			= 201;
	public final static int	DOWN		= 202;
	
	// virtual functions
	public final static int	GOODS		= 203;
	public final static int	PAYMENT		= 204;		// ����
	public final static int	PRICE		= 205;
	public final static int	PIN			= 206;
	public final static int	LOGON		= 207;
	public final static int	UNDEFINED	= 255;
    public final static int OPENCASHBOX    =92;   	// ��Ǯ��
	public final static int OFFLINECLOSEWORKTURN     =93;     //�ѻ����
	public final static int SHOWCASHBOX	=126;   // ��ʾǮ��״̬
	public final static int ICCARD			=127;   // IC��֧��
    public final static int Coupon			=128;   // ��ȯ
    public final static int ticket			=129;   // �ش�����СƱ
    // ����Ԥ�������������ֵ
    public final static int FUTRUESELL 	= 301;	// Ԥ����
    public final static int BRINGFORWARD 	= 302;	// �� ��s
    
    public final static int LARGESS = 303; 			// ����
    public final static int GROUPLARGESS = 304; 	// ��������
    public final static int LARGESSL = 305; 		// ����
    public final static int CARDCHANGE = 306;		// ��Ա�����ֶһ�
    public final static int LARGESSCOUPON = 307; 	// ��ȯ
    public final static int CouponPay = 308;		// ȯ֧�� 20090802
    public final static int CardRA =309;			// ��ֵ����ֵ 20100526

}
