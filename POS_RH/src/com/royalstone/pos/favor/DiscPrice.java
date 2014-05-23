package com.royalstone.pos.favor;


/**
   @version 1.0 2004.05.25
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

/**	��ҵ����,�����ۿ۰������ķ�ʽ�ɷ�������:���ٷֵ㶨����ۿ�,�Ͱ������۸�����ۿ�.
 * DiscRate ��ʾ���ٷֱȶ�����ۿ�, DiscPrice ��ʾ�������۶�����ۿ�.
 * @author Mengluoyi
 */

public class DiscPrice extends Discount
{
	/**
	 * @param disc_type		�ۿ�����
	 * @param disc_price	�����ۼ�
	 */
	public DiscPrice( int disc_type, long disc_price )
	{
		super( disc_type );
		price = disc_price;
	}

	/**
	 * @return	��Ʒ�ۼ�(������)
	 */
	public long getPrice()
	{
		return price;
	}
	
	/**
	 * for debug use.
	 */
	public String toString()
	{
		return "Disc Price: " + price;
	}

	/**
	 * <code>price</code>	��Ʒ������(�Է�Ϊ��λ)
	 */
	private long price;
}
