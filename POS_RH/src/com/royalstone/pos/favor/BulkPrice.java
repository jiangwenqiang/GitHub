/*
 * Created on 2004-6-3
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.royalstone.pos.favor;

import java.util.GregorianCalendar;

import com.royalstone.pos.common.Goods;
import com.royalstone.pos.util.Value;

/**
 * @author Mengluoyi
 * BulkPrice ��ʾ������,�����ݿ��еı�discPrice_vgno���������Ӧ.
 */
public class BulkPrice extends DiscCriteria 
{
	/**
	 * @param vgno			��Ʒ����
	 * @param bulk_price1	�����۸�1
	 * @param qty1			�����۸�1����������
	 * @param bulk_price2	�����۸�2
	 * @param qty2			�����۸�2����������
	 * @param bulk_price3	�����۸�3
	 * @param qty3			�����۸�3����������
	 * @param bulk_price4	�����۸�4
	 * @param qty4			�����۸�4����������
	 * @param bulk_price5	�����۸�5
	 * @param qty5			�����۸�5����������
	 * @param bulk_price6	�����۸�6
	 * @param qty6			�����۸�6����������
	 */
	public BulkPrice( String vgno,
		int bulk_price1, int qty1,
		int bulk_price2, int qty2,
		int bulk_price3, int qty3,
		int bulk_price4, int qty4,
		int bulk_price5, int qty5,
		int bulk_price6, int qty6 ) 
	{
		super();
		this.vgno = vgno;
		this.bulk_price1 = bulk_price1;
		this.qty1 = qty1;
		this.bulk_price2 = bulk_price2;
		this.qty2 = qty2;
		this.bulk_price3 = bulk_price3;
		this.qty3 = qty3;
		this.bulk_price4 = bulk_price4;
		this.qty4 = qty4;
		this.bulk_price5 = bulk_price5;
		this.qty5 = qty5;
		this.bulk_price6 = bulk_price6;
		this.qty6 = qty6;
	}

	/**
	 * @param vgno			��Ʒ����
	 * @param bulk_price1	�����۸�1
	 * @param qty1			�����۸�1����������
	 * @param bulk_price2	�����۸�2
	 * @param qty2			�����۸�2����������
	 * @param bulk_price3	�����۸�3
	 * @param qty3			�����۸�3����������
	 * @param bulk_price4	�����۸�4
	 * @param qty4			�����۸�4����������
	 * @param bulk_price5	�����۸�5
	 * @param qty5			�����۸�5����������
	 * @param bulk_price6	�����۸�6
	 * @param qty6			�����۸�6����������
	 * @param start			��ʼʱ��
	 * @param end			��ֹʱ��
	 */
	public BulkPrice(
		String vgno,
		int bulk_price1,
		int qty1,
		int bulk_price2,
		int qty2,
		int bulk_price3,
		int qty3,
		int bulk_price4,
		int qty4,
		int bulk_price5,
		int qty5,
		int bulk_price6,
		int qty6,
		GregorianCalendar start,
		GregorianCalendar end) 
	{
		super(start, end);
		this.vgno = vgno;
		this.bulk_price1 = bulk_price1;
		this.qty1 = qty1;
		this.bulk_price2 = bulk_price2;
		this.qty2 = qty2;
		this.bulk_price3 = bulk_price3;
		this.qty3 = qty3;
		this.bulk_price4 = bulk_price4;
		this.qty4 = qty4;
		this.bulk_price5 = bulk_price5;
		this.qty5 = qty5;
		this.bulk_price6 = bulk_price6;
		this.qty6 = qty6;
	}

	/**	�˺���������Ʒ,��Ʒ�����ͻ�Ա�����ж��Ƿ���������ۿ�.
	 * @param goods		��Ʒ
	 * @param qty		��Ʒ����
	 * @param cust_level	�ͻ�����
	 * @return	true	ƥ��;<br/>false	��ƥ��;
	 * @see com.royalstone.pos.favor.DiscCriteria#matches(com.royalstone.pos.common.Goods, int, int)
	 */
	public boolean matches(Goods goods, int qty, int cust_level) 
	{
		if (!super.valid() || !vgno.equals(goods.getVgno())) return false;
		if (qty1 > 0 && qty >= qty1) return true;
		if (qty2 > 0 && qty >= qty2) return true;
		if (qty3 > 0 && qty >= qty3) return true;
		if (qty4 > 0 && qty >= qty4) return true;
		if (qty5 > 0 && qty >= qty5) return true;
		if (qty6 > 0 && qty >= qty6) return true;

		return false;
	}
	
	/**	������Ʒ�Ƿ�������������ۿ�.
	 * @param goods	��Ʒ
	 * @param qty	��Ʒ����
	 * @return		true	ƥ��;<br/>false	��ƥ��.
	 */
	public boolean matches(Goods goods, int qty) {
		if (!super.valid()
			|| !vgno.equals(goods.getVgno())
			|| (goods.getPType() == null
				|| !goods.getPType().equals(DiscCriteria.BULKPRICE)))
			return false;
		if (qty1 > 0 && qty >= qty1) return true;
		if (qty2 > 0 && qty >= qty2) return true;
		if (qty3 > 0 && qty >= qty3) return true;
		if (qty4 > 0 && qty >= qty4) return true;
		if (qty5 > 0 && qty >= qty5) return true;
		if (qty6 > 0 && qty >= qty6) return true;

		return false;
	}
	
	public boolean matches(String goodsNo) {
		return vgno.equals(goodsNo);
	}


	/**
	 * @param goods	��Ʒ.
	 * @param qty	��Ʒ������.
	 * @param cust_level
	 * @return	�����Ʒ�����ﵽ������׼,�򷵻�Ϊ�����۸�,��DiscBulk �����ʾ.<br/>
	 * �����Ʒ����δ�ﵽ������׼,����ֵΪnull.
	 */
	public Discount getDiscount(Goods goods, int qty, int cust_level) 
	{
		if (!matches(goods, qty, cust_level))
			return new Discount(Discount.NONE);
		if (qty6 > 0 && qty >= qty6)
			return new DiscBulk(Discount.BULK, bulk_price6, qty6);
		if (qty5 > 0 && qty >= qty5)
			return new DiscBulk(Discount.BULK, bulk_price5, qty5);
		if (qty4 > 0 && qty >= qty4)
			return new DiscBulk(Discount.BULK, bulk_price4, qty4);
		if (qty3 > 0 && qty >= qty3)
			return new DiscBulk(Discount.BULK, bulk_price3, qty3);
		if (qty2 > 0 && qty >= qty2)
			return new DiscBulk(Discount.BULK, bulk_price2, qty2);
		if (qty1 > 0 && qty >= qty1)
			return new DiscBulk(Discount.BULK, bulk_price1, qty1);
		return new Discount(Discount.NONE);
	}

	/**	POSϵͳ�п���Ϊһ����Ʒ����6������������ۼ�. 
	 * ���������Ը�����Ʒ������,��ѯһ����߼���������۸�.
	 * @param goods	��Ʒ.
	 * @param qty	��Ʒ������.
	 * @return	�����Ʒ�����ﵽ������׼,�򷵻�Ϊ�����۸�,��DiscBulk �����ʾ.�����Ʒ����δ�ﵽ������׼,����ֵΪnull.
	 */
	public DiscBulk getDiscBulk(Goods goods, int qty) 
	{
		if (qty6 > 0 && qty >= qty6)
			return new DiscBulk(Discount.BULK, bulk_price6, qty6);
		if (qty5 > 0 && qty >= qty5)
			return new DiscBulk(Discount.BULK, bulk_price5, qty5);
		if (qty4 > 0 && qty >= qty4)
			return new DiscBulk(Discount.BULK, bulk_price4, qty4);
		if (qty3 > 0 && qty >= qty3)
			return new DiscBulk(Discount.BULK, bulk_price3, qty3);
		if (qty2 > 0 && qty >= qty2)
			return new DiscBulk(Discount.BULK, bulk_price2, qty2);
		if (qty1 > 0 && qty >= qty1)
			return new DiscBulk(Discount.BULK, bulk_price1, qty1);
		return null;
	}

	/**
	 * for debug use.
	 */
	public String toString() {
		return "{ " + vgno + " " + new Value(bulk_price1).toString() + "@"
				+ qty1 + "; " + new Value(bulk_price2).toString() + "@" + qty2
				+ "; " + new Value(bulk_price3).toString() + "@" + qty3 + " }";
	}

	private String vgno;
	private int bulk_price1, qty1;
	private int bulk_price2, qty2;
	private int bulk_price3, qty3;
	private int bulk_price4, qty4;
	private int bulk_price5, qty5;
	private int bulk_price6, qty6;
}
