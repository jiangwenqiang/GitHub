package com.royalstone.pos.favor;


/**
   @version 1.0 2004.05.25
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

/**	��ҵ����,�����ۿ۰������ķ�ʽ�ɷ�������:���ٷֵ㶨����ۿ�,�Ͱ������۸�����ۿ�.
 * DiscRate ��ʾ���ٷֱȶ�����ۿ�, DiscPrice ��ʾ�������۶�����ۿ�.
 * @author Mengluoyi
 */
public class DiscRate extends Discount
{
	/**
	 * @param disc_type		�ۿ�����
	 * @param disc_point	�ۿ۵���
	 */
	public DiscRate( int disc_type, int disc_point )
	{
		super( disc_type );
		point = disc_point;
	}

	/**
	 * @return		�ۿ۵���
	 */
	public int getPoint()
	{
		return point;
	}
	
	/**
	 * for debug use.
	 */
	public String toString()
	{
		return "Disc Point: " + point + "% ";
	}

	/**
	 * <code>point</code>	�ۿ۵���. ȱʡֵΪ0.
	 */
	private int point = 0;
}
