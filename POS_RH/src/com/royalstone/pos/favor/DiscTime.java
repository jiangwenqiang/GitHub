/*
 * Created on 2004-6-3
 *
 */
package com.royalstone.pos.favor;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.royalstone.pos.common.Goods;

/**	Promotion ʵ�ְ��۸���Ĵ���, �����Ƿ��Ա����������.
 * @author Mengluoyi
 */
public class DiscTime extends DiscCriteria 
{
	/**
	 * @param vgno		��Ʒ����
	 * @param price		��Ʒ�����ۼ�
	 */
	public DiscTime(String vgno, 
			int time1,int Discnumber1,
			int time2,int Discnumber2,
			int time3,int Discnumber3,
			int time4,int Discnumber4,
			int time5,int Discnumber5,
			int time6,int Discnumber6
			) 
	{
		super();
		this.vgno = vgno;
		this.time1=time1;
		this.Discnumber1=Discnumber1;
		this.time2=time2;
		this.Discnumber2=Discnumber2;
		this.time3=time3;
		this.Discnumber3=Discnumber3;
		this.time4=time4;
		this.Discnumber4=Discnumber4;
		this.time5=time5;
		this.Discnumber5=Discnumber5;
		this.time6=time6;
		this.Discnumber6=Discnumber6;
	}

	/**
	 * @param vgno		��Ʒ����
	 * @param price		��Ʒ�����ۼ�
	 * @param start		��Ч������ʱ��
	 * @param end		��ֹ������ʱ��
	 */
	public DiscTime(String vgno, int price, GregorianCalendar start, GregorianCalendar end) 
	{
		super(start, end);
		this.vgno = vgno;
	}

	/**	���ݴ���Ĳ��������Ƿ�������ܴ����ۿ�. �˺���ʵ�ָ����еĳ��󷽷�. 
	 * cust_level(�ͻ�����)������.
	 * @see com.royalstone.pos.favor.DiscCriteria#matches
	 */
	public boolean matches(Goods goods, int qty, int cust_level) 
	{
		// cust_level is ignored when searching for promotion price.
		return ( super.valid()
				&& (goods.getPType() != null && goods.getPType().equals(DiscCriteria.DISCTIME))
				&& vgno.equals(goods.getVgno()) );
	}

	/**	���ݴ���Ĳ���������Ʒ�������ܵ��ۿ�. �˺���ʵ�ָ����еĳ��󷽷�. 
	 * cust_level(�ͻ�����)������.
	 * @see com.royalstone.pos.favor.DiscCriteria#getDiscount
	 * 
	 */
	public Discount getDiscount(Goods goods, int qty, int cust_level) 
	{
		int time;
		
		//��ȡʱ���
		SimpleDateFormat fmt1=new SimpleDateFormat("HHmmss");
		time=Integer.parseInt(fmt1.format(new java.util.Date()));
		
		if (!matches(goods, qty, cust_level)) return new Discount(Discount.NONE);
		//�ж�
		if(time>time6) return  new DiscRate(Discount.PERIOD, Discnumber6);
		if(time>time5) return  new DiscRate(Discount.PERIOD, Discnumber5);
		if(time>time4) return  new DiscRate(Discount.PERIOD, Discnumber4);
		if(time>time3) return  new DiscRate(Discount.PERIOD, Discnumber3);
		if(time>time2) return  new DiscRate(Discount.PERIOD, Discnumber2);
		if(time>time1) return  new DiscRate(Discount.PERIOD, Discnumber1);
		
		return new Discount(Discount.NONE);
		
	}

	/**
	 * for debug use.
	 */
	public String toString() 
	{
		return vgno;
		//return "{ " + vgno + ", " + Discnumber + " }";
	}

	/**
	 * <code>vgno</code>	��Ʒ����
	 */
	private String vgno;
	
	/**
	 * <code>price</code>	��Ʒ�ۿ�
	 */
	private int time1, Discnumber1;
	private int time2, Discnumber2;
	private int time3, Discnumber3;
	private int time4, Discnumber4;
	private int time5, Discnumber5;
	private int time6, Discnumber6;

	public boolean matches(String goodsNo) {
		return vgno.equals(goodsNo);
	}
	
}
