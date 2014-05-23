/*
 * Created on 2004-6-3
 *
 */
package com.royalstone.pos.favor;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.royalstone.pos.common.Goods;

/**	Promotion 实现按价格定义的促销, 不认是否会员都可以享受.
 * @author Mengluoyi
 */
public class DiscTime extends DiscCriteria 
{
	/**
	 * @param vgno		商品编码
	 * @param price		商品促销售价
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
	 * @param vgno		商品编码
	 * @param price		商品促销售价
	 * @param start		生效日期与时间
	 * @param end		截止日期与时间
	 */
	public DiscTime(String vgno, int price, GregorianCalendar start, GregorianCalendar end) 
	{
		super(start, end);
		this.vgno = vgno;
	}

	/**	根据传入的参数决定是否可以享受促销折扣. 此函数实现父类中的抽象方法. 
	 * cust_level(客户级别)被忽略.
	 * @see com.royalstone.pos.favor.DiscCriteria#matches
	 */
	public boolean matches(Goods goods, int qty, int cust_level) 
	{
		// cust_level is ignored when searching for promotion price.
		return ( super.valid()
				&& (goods.getPType() != null && goods.getPType().equals(DiscCriteria.DISCTIME))
				&& vgno.equals(goods.getVgno()) );
	}

	/**	根据传入的参数决定商品可以享受的折扣. 此函数实现父类中的抽象方法. 
	 * cust_level(客户级别)被忽略.
	 * @see com.royalstone.pos.favor.DiscCriteria#getDiscount
	 * 
	 */
	public Discount getDiscount(Goods goods, int qty, int cust_level) 
	{
		int time;
		
		//获取时间点
		SimpleDateFormat fmt1=new SimpleDateFormat("HHmmss");
		time=Integer.parseInt(fmt1.format(new java.util.Date()));
		
		if (!matches(goods, qty, cust_level)) return new Discount(Discount.NONE);
		//判断
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
	 * <code>vgno</code>	商品编码
	 */
	private String vgno;
	
	/**
	 * <code>price</code>	商品折扣
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
