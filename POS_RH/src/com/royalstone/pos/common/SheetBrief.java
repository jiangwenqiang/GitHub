package com.royalstone.pos.common;

import com.royalstone.pos.core.PosSheet;
import com.royalstone.pos.util.PosConfig;
import com.royalstone.pos.util.PosTime;

/**
   @version 1.0 2004.05.24
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

/**
 * POS �������֧�ֶ��Źҵ�. ������Ҳ���ʱ,Ϊ�˱�������Աѡ����ȷ�Ĺҵ�,ϵͳӦ�ѹ���ĵ�����Ҫ������ʾ����.
 * SheetBrief ����Ϊ����ʾ�ѹ��� POS ����������Ҫ����������.
 * ��Ϊ������Ҫ������: 1)ʱ��, 2)�ܽ��, 3)��Ʒ.
 * @author Mengluoyi
 */
public class SheetBrief
{

	/**
	 * Constructs a SheetBrief instance by an instance of PosSheet.
	 * @param sheet		POS������.
	 */
	public SheetBrief ( PosSheet sheet )
	{
		if( sheet != null ){
			sale_len = sheet.getSaleLen();
			if( !sheet.isEmpty() ){
				value_total = sheet.getValue().getValueTotal();
				sheet_time  = sheet.getSale(0).getSysTime();
			}
			//TODO  ���ݸ��� by fire  2005_5_11 
			//getIndicators( sheet );
		}
	}
	
	/**	��PosSheet ��ȡ����־����Ʒ(��Ʒ). 
	 * �Ƿ��־����Ʒ,��Ҫ���������ļ��еĲ���ȷ��.
	 * NOTE: ������sheet ���Ƿ���ڱ�־����Ʒ, indicators �����õ���ʼ��. 
	 * ���sheet Ϊ��,����û�б�־����Ʒ,������ indicators ����Ϊ0.
	 * @param sheet
	 */
	private void getIndicators( PosSheet sheet )
	{
		PosConfig config = PosConfig.getInstance();
		int count = 0;
		if( !sheet.isEmpty() ) for( int i=0; i<sheet.getSaleLen(); i++ ){
			Sale sale = sheet.getSale( i );
			if( config.isIndicatorDept( sale.getGoods().getDeptid() ) ) count++;
		}
		
		indicators = new Sale[ count ];
		for( int i=0, j=0; j<count && i < sheet.getSaleLen(); i++ ){
			Sale sale = sheet.getSale( i );
			if( config.isIndicatorDept( sale.getGoods().getDeptid() ) ) indicators[j++] = sale;
		}
	}


	/**	�÷�������һ������POS�������������ִ�. 
	 * ��ҵ������,����۳���Ʒ���ж����Ʒ,�򷵻�����'*',
	 * �����һ����Ʒ,�򷵻ظ���Ʒ������,���û����Ʒ,�򷵻�һ���հ��ִ�.
	 * @return	����POS������������ִ�.
	 */
	public String toString()
	{
		if( indicators == null ) return " ";
		else if ( indicators.length == 0 ) return " ";
		else if ( indicators.length == 1 ) return (indicators[0].getName() );
		else  return " * * * ";
	}

	/**
	 * @return	POS���������ܽ��.
	 */
	public int getValueTotal()
	{
		return value_total;
	}
	
	/**
	 * @return	POS��������ʱ��.
	 */
	public PosTime getTime()
	{
		return sheet_time; 
	}

	/**
	 * @return	true	POS������Ϊ��<br/>
	 * false	POS�������ǿ�<br/>
	 */
	public boolean isEmpty()
	{
		return ( sale_len == 0 );
	}

	/**
	 * <code>sheet_time</code>POS��������ʱ��.
	 */
	private PosTime sheet_time  = new PosTime();
	
	/**
	 * <code>value_total</code>POS�������ϵ���Ʒ�ܽ��.
	 */
	private int   	value_total = 0;

	/**
	 * <code>indicators</code>POS�������ϵ�������Ʒ(��Ʒ).
	 */
	private Sale[]  indicators  = null;
	
	/**
	 * <code>sale_len</code>POS���������۳���Ʒ�嵥�ĳ���.
	 */
	private int		sale_len    = 0;
}
