/*
 * Created on 2004-6-3
 */
package com.royalstone.pos.favor;

import java.util.GregorianCalendar;

import com.royalstone.pos.common.Goods;

/**	Promotion ʵ�ְ��۸���Ļ�Ա����, �����Ա��������Ҫ�����������Ӧ���Ż�. ����Ϊ��ͬ����Ļ�Ա���岻ͬ���Żݼ۸�.
 * @author Mengluoyi
 */

public class Prom4Member extends DiscCriteria 
{
	/**
	 * @param vgno		��Ʒ����
	 * @param price		��Ʒ�����ۼ�
	 * @param cust_level	�������ܴ����۵Ŀͻ�����
	 */
	public Prom4Member(String vgno, int price, int cust_level) 
	{
		super();
		this.vgno = vgno;
		this.price = price;
		this.cust_level = cust_level;
	}

	/**
	 * @param vgno		��Ʒ����
	 * @param price		��Ʒ�����ۼ�
	 * @param cust_level	�������ܴ����۵Ŀͻ�����
	 * @param start		��Ч������ʱ��
	 * @param end		��ֹ������ʱ��
	 */
	public Prom4Member(String vgno, int price, int cust_level, GregorianCalendar start, GregorianCalendar end) 
	{
		super(start, end);
		this.vgno = vgno;
		this.price = price;
		this.cust_level = cust_level;
	}

	/**	���ݴ���Ĳ��������Ƿ�������ܴ����ۿ�. �˺���ʵ�ָ����еĳ��󷽷�. 
	 * �Ƿ�������ܴ���ȡ�������¼���:
	 * 1) ʱ��������Ƿ�����Ч����
	 * 2) ��Ʒ���ۿ������Ƿ�ƥ��
	 * 3) �ͻ������Ƿ�ƥ��
	 * @see com.royalstone.pos.favor.DiscCriteria#matches
	 */
	public boolean matches(Goods goods, int qty, int cust_level) {
		return (
			super.valid()
				&& (goods.getPType() != null && goods.getPType().equals(DiscCriteria.PROM4MEMBER))
				&& this.vgno.equals(goods.getVgno())
				&& this.cust_level == cust_level);
	}

	/**	���ݴ���Ĳ���������Ʒ�������ܵ��ۿ�. �˺���ʵ�ָ����еĳ��󷽷�. 
	 * �Ƿ�������ܴ���ȡ�������¼���:
	 * 1) ʱ��������Ƿ�����Ч����
	 * 2) ��Ʒ���ۿ������Ƿ�ƥ��
	 * 3) �ͻ������Ƿ�ƥ��
	 * @see com.royalstone.pos.favor.DiscCriteria#getDiscount
	 */
	public Discount getDiscount(Goods goods, int qty, int cust_level) {
		if (matches(goods, qty, cust_level))
			return new DiscPrice(Discount.MEMBERPRICE, price);
		return new Discount(Discount.NONE);
	}
	
	public boolean matches(String goodsNo) {
		return vgno.equals(goodsNo);
	}


	/**
	 * for debug use.
	 */
	public String toString() {
		return "{ " + vgno + ", " + price + ", " + cust_level + " }";
	}

	/**
	 * <code>vgno</code>		��Ʒ����
	 */
	private String vgno;
	
	/**
	 * <code>cust_level</code>	�������ܴ����۵Ŀͻ�����
	 */
	private int cust_level;
	
	/**
	 * <code>price</code>		��Ʒ�Ĵ�����(�Է�Ϊ��λ)
	 */
	private int price;
}