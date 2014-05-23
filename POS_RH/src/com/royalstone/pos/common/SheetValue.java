package com.royalstone.pos.common;

import java.io.Serializable;

import com.royalstone.pos.util.PosConfig;
import com.royalstone.pos.util.Value;

/**
   @version 1.0 2004.05.20
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

/**
 * ���۹�����,POS������Ҫ������Ʒ��ֵ��˿�֧��������ϼ���Ӧ��/Ӧ������. 
 * Ϊ�˽������������������SheetValue.
 * ����ҵ��,����֧�����������ļ����н�Ϊ���ӵĹ涨:
 * ������ô�����֧�����ֽ�( ��ȯ�������� ).
 * 
 * @author Mengluoyi
 */
public class SheetValue implements Serializable
{
	/**
	 * constructs an instance of SheetValue, all value set to 0.
	 */
	public SheetValue()
	{
		value_total  = 0;
		value_paid   = 0;
		cash_paid    = 0;
		value_unpaid = 0;
		disc_old     = 0;
		disc_current = 0;
	}

	/**
	 * @param v		an instance of SheetValue. ��һ�� SheetValue ʵ��������һ��ʵ��.
	 */
	public SheetValue( SheetValue v )
	{
		value_total  = v.value_total;
		value_paid   = v.value_paid;
		cash_paid    = v.cash_paid;
		value_unpaid = v.value_unpaid;
		disc_old     = v.disc_old;
		disc_current = v.disc_current;
	}

	/**	���ý��.
	 * @param total		�ܽ��.
	 * @param paid		��֧��.
	 * @param cash		�Ѹ��ֽ�.
	 * @param disc		�ۿ۽��.
	 */
	public void setValue( int total, int paid, int cash, int disc )
	{
		
		//zhouzhou add 20091104
        String talRound=PosConfig.getInstance().getString("TALROUND_SCHEMA");
        if("ON".equals(talRound))
             total=(int)Math.floor((double)total/10)*10;
        //END  ���������ֱ��ȥ��
		//int handledTotal=(int)Math.ceil((double)total/10)*10;
		value_total  = total;
        //value_total=handledTotal;
		value_paid   = paid;
		cash_paid    = cash;
		//value_unpaid = handledTotal - paid;
		value_unpaid = value_total - paid;
		disc_old     = disc_current;
		disc_current = disc;
        //disc_current = (int)Math.floor((double)disc/10)*10;
	}
	
	/**
	 * @param disc	�趨�ۿ۽��.
	 */
	public void setDisc(int disc){
		disc_old = 0;
		disc_current = disc;
	}

	/**
	 * @return	��Ʒ����ܺ�(�۳���Ʒ-������Ʒ-�˻���Ʒ).
	 */
	public int getValueTotal()
	{
		return value_total;
	}

	/**
	 * @return	��֧�����.
	 */
	public int getValuePaid()
	{
		return value_paid;
	}

	/**
	 * @return	δ֧�����.
	 */
	public int getValueUnPaid()
	{
		return value_unpaid;
	}

	/**	���� "��֧�����".
	 * ���Ȳ鿴 value_unpaid, ���Ϊ��,�����˿�֧������,�� value_topay = value_unpaid.
	 * ���value_unpaid Ϊ��, �����˿�֧������ѳ���Ӧ֧�����, ����"���겻�ó����Ѹ��ֽ�"�Ĺ涨����������.
	 * @return	��֧�����. �����ֵΪ��,�����ֵΪ�˿ͻ���Ҫ��֧���Ľ��,�����ֵΪ��,�����ֵΪӦ�һظ��˿͵Ľ��.
	 */
	public int getValueToPay()
	{
		if( value_unpaid >= 0 ) return value_unpaid;
		else if( value_total >0 ) {
			int	value_change = 0 - value_unpaid;
			if( cash_paid < value_change ) value_change = cash_paid;
			return (0-value_change);
		}
		else{
			return (value_total - cash_paid);
		}
	}

	/**
	 * @return	�ۿ��ܶ�.
	 */
	public int getDiscTotal()
	{
		return disc_current;
	}

	/**
	 * @return	�ۿ۽��ı仯ֵ(��Ϊ��,Ҳ��Ϊ��). 
	 * POS���������۹�����Ҫ���ϵؼ���ӦΪ�˿��ṩ���ۿ�. ��Ʒ�۳������ʹ�ۿ�����,���������˻�����ʹ�ۿ��ջ�.
	 * ����,�ۿ۱仯����Ϊ��,Ҳ����Ϊ��.
	 */
	public int getDiscDelta()
	{
		return (disc_current-disc_old);
	}

	/**
	 * @return	a string summarizing SheetValue info, for debug use.
	 */
	public String toString()
	{
		return ( "Total: "    + new Value( getValueTotal() )
			+ " Paid: "   + new Value( getValuePaid()  )
			+ " UnPaid: " + new Value( getValueUnPaid() )
			+ " ToBePaid: " + new Value( getValueToPay() )
			+ "DiscDelta: " + new Value( getDiscDelta()) );
	}

	/**
	 * <code>value_total</code>	��Ʒ���ܽ��.
	 */
	private int value_total;
	/**
	 * <code>value_paid</code>	��֧�����.
	 */
	private int value_paid;
	/**
	 * <code>value_unpaid</code>	δ֧�����.
	 */
	private int value_unpaid;
	/**
	 * <code>disc_current</code>	��ǰ�ۿ۽��.
	 */
	private int disc_current;
	/**
	 * <code>disc_old</code>	ԭ�ۿ۽��.
	 */
	private int disc_old;
	/**
	 * <code>cash_paid</code>	��֧�����ֽ�(�Է�Ϊ��λ).
	 */
	private int cash_paid;
}
