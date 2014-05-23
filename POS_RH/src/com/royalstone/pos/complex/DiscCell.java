/*
 * Created on 2004-6-2
 */
package com.royalstone.pos.complex;

import java.io.Serializable;

import com.royalstone.pos.common.Goods;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.core.SaleList;
import com.royalstone.pos.favor.DiscCriteria;

/**
 * @author Mengluoyi
 */
public class DiscCell implements Serializable {

	/**
	 * @param discpool	��ϴ��������еĿɻ�����ƷС��
	 */
	public DiscCell(DiscPool discpool) {
		pool = discpool;
	}

	/**	������� DiscPool ����ϲ���DiscCell ��pool ������.
	 * @param discpool	An instance of DiscPool
	 */
	public void mergePool(DiscPool discpool) {
		if (pool.isSamePool(discpool))
			pool = DiscPool.merge(pool, discpool);
	}

	/**	�ж���Ʒ�����Ƿ��ڵ�Ч��ƷС����
	 * @param vgno	��Ʒ����
	 * @return	true	��������ڵ�Ч��ƷС����;<br/>
	 * 			false	���ڵ�ЧС����.
	 */
	public boolean hasVgno(String vgno) {
		return pool.hasVgno(vgno);
	}

	/**	�жϴ���ĵ�Ч��Ʒ���Ƿ��DiscCell �ĵ�ЧС������ͬһ����Ч��.
	 * @param discpool
	 * @return
	 */
	public boolean isSamePool(DiscPool discpool) {
		return this.pool.isSamePool(discpool);
	}

	/**
	 * @param slist
	 */
	public void clearinSaleList(SaleList slist) {
		for (int i = 0; i < slist.size(); i++) {
			Sale sale = slist.get(i);
			if (sale.getType() != Sale.WITHDRAW
				&& pool.hasVgno(sale.getVgno())
				&& (sale.getGoods().getPType() != null
					&& sale.getGoods().getPType().equals(
						DiscCriteria.DISCCOMPLEX) || sale.getGoods().getPType().equals(DiscCriteria.BUYANDGIVE))) {
				sale.clearFavor();
			}
		}
	}

	/**	��SaleList �������ɨ��,�����Ч��Ʒ���ڵ���Ʒ,���ۼ����۳�����.
	 * @param slist	��Ʒ���ۼ�¼
	 */
	public void checkinSaleList(SaleList slist) {
		for (int i = 0; i < slist.size(); i++)
			checkinSale(slist.get(i));
	}

	/**	�ú����������ۼ�¼���м��. ������ϲ�����ϴ���������,���ۼ����۳�����.
	 * �Ƿ���ϵ��ж�����:
	 * 1) ��¼���Ͳ���Ϊ�˻�;
	 * 2) ��Ʒ���ۿ�����Ϊ��ϴ���;
	 * 3) ��Ʒ�����ڴ���С���嵥��.
	 * @param sale	��Ʒ���ۼ�¼
	 */
	private void checkinSale(Sale sale) {
		if (sale.getType() != Sale.WITHDRAW
			&& pool.hasVgno(sale.getVgno())
			&& (sale.getGoods().getPType() != null
				&& sale.getGoods().getPType().equals(DiscCriteria.DISCCOMPLEX)||sale.getGoods().getPType().equals(DiscCriteria.BUYANDGIVE)))
			qty_sold += sale.getQty() / sale.getGoods().getX();
	}

	/**
	 * @param slist
	 */
	public void checkinSaleListAfter(SaleList slist) {
		for (int i = 0; i < slist.size(); i++)
			checkinSaleAfter(slist.get(i));
	}

	/**
	 * @param sale
	 */
	private void checkinSaleAfter(Sale sale) {
		if (sale.getType() != Sale.WITHDRAW
			&& pool.hasVgno(sale.getVgno())
			&& (sale.getGoods().getPType() != null
				&& sale.getGoods().getPType().equals(DiscCriteria.DISCCOMPLEX)||sale.getGoods().getPType().equals(DiscCriteria.BUYANDGIVE)))
			qty_sold += (sale.getQty() - sale.getQtyDisc())
				/ sale.getGoods().getX();
	}

	/**
	 * @param sale
	 * @return
	 */
	public boolean match(Sale sale) {
		if (sale.getType() != Sale.WITHDRAW
			&& pool.hasVgno(sale.getVgno())
			&& (sale.getGoods().getPType() != null
				&& sale.getGoods().getPType().equals(DiscCriteria.DISCCOMPLEX)||sale.getGoods().getPType().equals(DiscCriteria.BUYANDGIVE))) {
			return true;
		}
		return false;
	}

	/**
	 * @param g
	 * @return
	 */
	public boolean match(Goods g) {
		if (pool.hasVgno(g.getVgno())) {
			return true;
		}
		return false;
	}

	public boolean match(String goodsNo) {
		if (pool.hasVgno(goodsNo)) {
			return true;
		}
		return false;
	}

	/**	����DiscCell�����������������Ʒ�Ĵ������۶�.
	 * @return	��DiscCell ��,�������ܴ����ۼ۵���Ʒ���մ����۸����Ľ��.
	 */
	public int getPromValue() {
		return pool.getPromPrice() * baskets_favored;
	}

	/**
	 * @return	�۳���Ʒ�������ĵ�Ч��Ʒ����
	 */
	public int getSoldBaskets() {
		return (pool.getMinQty() == 0) ? 0 : (qty_sold / pool.getMinQty());
	}

	/**
	 * @param baskets_favored	���ܴ����Żݼ۸��С����
	 */
	public void setFavor(int baskets_favored) {
		this.baskets_favored = baskets_favored;
		qty_favored = baskets_favored * pool.getMinQty();
	}

	/**
	 * 
	 */
	public void clear() {
		qty_sold = 0;
		qty_favored = 0;
	}

	/**
	 * @return
	 */
	private DiscPool getPool() {
		return pool;
	}

	/**
	 * @return
	 */
	public int getFavorQty() {
		return qty_favored;
	}

	/**
	 * @return
	 */
	public int getFavorValue() {
		return pool.getPromPrice() * baskets_favored;
	}

	/**
	 * for debug use.
	 */
	public String toString() {
		return pool.toString();
	}

	public DiscCell[] split() {

		DiscCell[] result = null;

		if (pool != null) {
			DiscPool[] pools = pool.split();
			result = new DiscCell[pools.length];
			for (int i = 0; i < pools.length; i++) {
				result[i] = new DiscCell(pools[i]);
			}
		}

		return result;
	}

	/**
	 * <code>pool</code>		������ƷС��
	 */
	private DiscPool pool;

	/**
	 * <code>qty_sold</code>	����С������۳�������
	 */
	private int qty_sold = 0;

	/**
	 * <code>qty_favored</code>	�������ܴ����ؼ۵�С������Ʒ������
	 */
	private int qty_favored = 0;

	/**
	 * <code>baskets_favored</code>	�������ܴ����ؼ۵���ƷС�������
	 */
	private int baskets_favored = 0;
}
