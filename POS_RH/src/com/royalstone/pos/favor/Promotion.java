/*
 * Created on 2004-6-3
 *
 */
package com.royalstone.pos.favor;

import java.util.GregorianCalendar;

import com.royalstone.pos.common.Goods;

/**	Promotion ʵ�ְ��۸���Ĵ���, �����Ƿ��Ա����������.
 * @author Mengluoyi
 */
public class Promotion extends DiscCriteria 
{
	/**
	 * @param vgno		��Ʒ����
	 * @param price		��Ʒ�����ۼ�
	 */
	public Promotion(String vgno, int price) 
	{
		super();
		this.vgno = vgno;
		this.price = price;
	}

	/**
	 * @param vgno		��Ʒ����
	 * @param price		��Ʒ�����ۼ�
	 * @param start		��Ч������ʱ��
	 * @param end		��ֹ������ʱ��
	 */
	public Promotion(String vgno, int price, GregorianCalendar start, GregorianCalendar end) 
	{
		super(start, end);
		this.vgno = vgno;
		this.price = price;
	}

	/**	���ݴ���Ĳ��������Ƿ�������ܴ����ۿ�. �˺���ʵ�ָ����еĳ��󷽷�. 
	 * cust_level(�ͻ�����)������.
	 * @see com.royalstone.pos.favor.DiscCriteria#matches
	 */
	public boolean matches(Goods goods, int qty, int cust_level) 
	{
		// cust_level is ignored when searching for promotion price.
		return ( super.valid()
				&& (goods.getPType() != null && goods.getPType().equals(DiscCriteria.PROMOTION))
				&& vgno.equals(goods.getVgno()) );
	}

	/**	���ݴ���Ĳ���������Ʒ�������ܵ��ۿ�. �˺���ʵ�ָ����еĳ��󷽷�. 
	 * cust_level(�ͻ�����)������.
	 * @see com.royalstone.pos.favor.DiscCriteria#getDiscount
	 * 
	 */
	public Discount getDiscount(Goods goods, int qty, int cust_level) 
	{
		if (matches(goods, qty, cust_level)) return new DiscPrice(Discount.PROMOTION, price);
		return new Discount(Discount.NONE);
	}

	/**
	 * for debug use.
	 */
	public String toString() 
	{
		return "{ " + vgno + ", " + price + " }";
	}

	/**
	 * <code>vgno</code>	��Ʒ����
	 */
	private String vgno;
	
	/**
	 * <code>price</code>	��Ʒ�����ۼ�
	 */
	private int price;

	public boolean matches(String goodsNo) {
		return vgno.equals(goodsNo);
	}
	
}
