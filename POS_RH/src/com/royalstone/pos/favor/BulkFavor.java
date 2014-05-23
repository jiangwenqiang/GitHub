package com.royalstone.pos.favor;


/**
   @version 1.0 2004.05.25
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

/**	����BulkFavor,��Ϊ�˽�������Żݽ��ķ�̯����. BulkFavor������Ҫ��: �Żݽ��,�����Żݵ���Ʒ����.
 * BulkFavor ����Ҫ�ĳ�Ա������shareFavor. ���øú����Ľ��,����ԭʼ��BulkFavor һ��Ϊ��, 
 * ���ص�BulkFavor �����Ӧ������Ϊqty ����ƷӦ����Ĳ���,ʣ�²���Ϊ����̯���Żݽ��.
 * @author Mengluoyi
 */
public class BulkFavor extends Discount
{
	/**
	 * @param qty_favored		�����Żݽ�����Ʒ����
	 * @param value_favored		�Żݽ��
	 */
	public BulkFavor( int qty_favored, int value_favored )
	{
		super( Discount.BULK );
		this.favor_value 	= value_favored;
		this.favor_qty 		= qty_favored;
	}

	/**
	 * @return	�Żݽ��
	 */
	public int getValue()
	{
		return favor_value;
	}
	
	/**
	 * @return	�����Żݵ���Ʒ����
	 */
	public int getQty()
	{
		return favor_qty;
	}
	
	/**
	 * @return	�����Żݵ���Ʒ����
	 */
	public int getQtyFavored()
	{
		return favor_qty;
	}
	
	/**	�ú������������ۿ۵ķ�̯����.
	 * ���������ۿ�,����Ҫ�ѷֲ��ڶ�����¼�е���Ʒ���ۼ�¼����Ʒ������,
	 * Ȼ�����ۿ۱�������������ܵ��Żݽ��,
	 * �ٰ��Żݽ���̯���������Ʒ���ۼ�¼��.
	 * ����shareFavor ����;���ǰ��Żݽ���̯������Ϊqty ����Ʒ��¼��.
	 * ҵ����,Ҫ���̯�������"һ�ֲ���". 
	 * ����, ������Ʒ��¼,������Ϊ1,���Żݽ��Ϊ100,���̯���ӦΪ:33,33,34.
	 * 
	 * @param qty	�����Żݽ�����Ʒ����
	 * @return		���this �������Żݶ�Ϊ0, ����һ���Ż�Ϊ0�� BulkFavor����;<br/>
	 * ���qty ����this �����е��Ż�����,���ֵ�ȫ���Żݽ��, this �����Ϊ0;<br/>
	 * ���qty С��this �����е��Ż�����,�򷵻ذ�����������Ż�, ͬʱ,this �����е��Ż�����Ӧ�Ŀۼ�.
	 */
	public BulkFavor shareFavor( int qty )
	{
		int q = 0;
		int v = 0;
		
		if( favor_qty <= 0 || qty <= 0 ) return new BulkFavor( 0, 0 );
		if( qty >= favor_qty ){
			q = favor_qty;
			v = favor_value;
			favor_qty = 0;
			favor_value = 0;
			return new BulkFavor( q, v );
		} else {
			q = qty;
			v = favor_value*qty / favor_qty;
			favor_qty -= qty;
			favor_value -= v;
			return new BulkFavor(q, v); 
		}
	}
	
	/**
	 * for debug use.
	 */
	public String toString()
	{
		return "" + "value_favored: " + favor_value + "; qty_favored: " + favor_qty;
	}

	/**
	 * <code>favor_value</code>	�Żݽ��
	 */
	private int favor_value;
	
	/**
	 * <code>favor_qty</code>	�Ż�����
	 */
	private int favor_qty;
}
