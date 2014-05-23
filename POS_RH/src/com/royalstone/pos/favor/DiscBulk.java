package com.royalstone.pos.favor;


/**	DiscBulk �������������۴���. 
 * �����۵��ص���:ֻ����������������������Ʒ�ǿ��Դ��۵�.
 * ����, ��������Ϊ12ֻ,�˿͹���15ֻ,������12ֻ��������������,������3ֻ������.
 * @version 1.0 2004.05.25
 * @author  Mengluoyi, Royalstone Co., Ltd.
 */

public class DiscBulk extends Discount
{
	/**
	 * @param disc_type		�ۿ�����
	 * @param disc_price	�ۿۼ۸�
	 * @param qty_bulk		�ۿ�����
	 */
	public DiscBulk( int disc_type, int disc_price, int qty_bulk )
	{
		super( disc_type );
		this.price = disc_price;
		this.qty_bulk = qty_bulk;
	}

	/**
	 * @return	������
	 */
	public int getPrice()
	{
		return price;
	}
	
	/**
	 * @return	������
	 */
	public int getBulkPrice()
	{
		return price;
	}
	
	/**
	 * @return	�������������۵���Ʒ����
	 */
	public int getBulkVolume()
	{
		return qty_bulk;
	}
	
	/**
	 * @return	�������������۵���Ʒ����
	 */
	public int getQtyBulk()
	{
		return qty_bulk;
	}
	
	/**
	 * for debug use.
	 */
	public String toString()
	{
		return "Bulk Price: " + price;
	}

	/**
	 * <code>price</code>	������
	 */
	private int price;
	
	/**
	 * <code>qty_bulk</code>	�������������۵���Ʒ����
	 */
	private int qty_bulk;
}
