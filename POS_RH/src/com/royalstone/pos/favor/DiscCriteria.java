package com.royalstone.pos.favor;

import java.io.Serializable;
import java.util.GregorianCalendar;

import com.royalstone.pos.common.Goods;

public abstract class DiscCriteria implements Serializable {
	public DiscCriteria() {
		time_start = new GregorianCalendar(1900, 0, 1);
		time_end = new GregorianCalendar(9000, 11, 31);
	}

	public DiscCriteria(GregorianCalendar start, GregorianCalendar end) {
		time_start = start;
		time_end = end;
	}

	public boolean valid() {
		GregorianCalendar now = new GregorianCalendar();
		return true;
		//		return ( time_start.before( now ) && time_end.after( now ) );
	}

	public abstract boolean matches(Goods goods, int qty, int cust_level);
	public abstract Discount getDiscount(Goods goods, int qty, int cust_level);
	public abstract boolean matches(String goodsNo);

	private GregorianCalendar time_start;
	private GregorianCalendar time_end;

	public final static String NORMAL = "n"; //û����
	public final static String PROMOTION = "p"; //��Ʒ����
	public final static String DISC4GOODS = "v"; //��Ʒ�ۿ�
	public final static String DISC4DEPT = "d"; //�����ۿ�
    public final static String DISC4MEMBERDEPT = "M"; //��Ա�����ۿ�
	public final static String BULKPRICE = "b"; //�����ۿ�
	public final static String DISCCOMPLEX = "x"; //��ϴ���
	public final static String DISCTIME = "t"; //ʱ���ۿ�
	public final static String DISC4MEMBER = "k"; //��Ա�ۿ�
	public final static String PROM4MEMBER = "h"; //��Ա��
    public final static String BUYANDGIVE = "l"; //����
    public final static String LARGESS = "A";		//��Ʒ����

}
