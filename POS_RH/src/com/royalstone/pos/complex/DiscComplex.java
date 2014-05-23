/*
 * Created on 2004-6-1
 *
 */
package com.royalstone.pos.complex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import com.royalstone.pos.common.Goods;
import com.royalstone.pos.core.SaleList;
import com.royalstone.pos.util.Day;
import com.royalstone.pos.util.PosTime;

/**
 * @author Mengluoyi
 */
public class DiscComplex implements Serializable {
	/**
	 * @param groupid		��Ϸ������
	 * @param name			������Ϸ�������
	 * @param price			�����ۼ�
	 * @param start_date	��Ч����
	 * @param end_date		��ֹ����
	 * @param start_time	��Чʱ��
	 * @param end_time		��ֹʱ��
	 * @param level			���ȼ�
	 */
	public DiscComplex(
		String groupid,
		String name,
		int price,
		Day start_date,
		Day end_date,
		PosTime start_time,
		PosTime end_time,
		int level) {
		this.groupid = groupid;
		this.name = name;
		this.favored_price = price;
		this.start_date = start_date;
		this.end_date = end_date;
		this.start_time = start_time;
		this.end_time = end_time;
		this.level = level;
		cells = new Vector();
	}

	public String getGroupID() {
		return groupid;
	}

	/**
	 * @return		���ȼ�
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return		��Ʒ������Ԫ�嵥�ĳ���
	 */
	public int size() {
		return cells.size();
	}

	/**	ȡָ���±����Ʒ������Ԫ
	 * @param i		�±�
	 * @return		��Ʒ������Ԫ
	 */
	public DiscCell get(int i) {
		return (DiscCell) cells.get(i);
	}

	public Vector getCells() {
		return cells;
	}

	/**
	 * @return	��Ʒ��ϴ����ķ�������
	 */
	public String name() {
		return name;
	}

	/**	�˺����ڴ�����������ӵ�Ч��ƷС��.
	 * ����ڴ��������ĵ�Ч��Ʒ�����ҵ����鳤��ͬ�ĵ�Ч��,�����ϲ�����;����,�ѵ�Ч����ӵ���Ч���б���.
	 * @param pool	��Ч��ƷС��
	 */
	public void addPool(DiscPool pool) {
		for (int i = 0; i < cells.size(); i++) {
			DiscCell cell = (DiscCell) cells.get(i);
			if (cell.isSamePool(pool)) {
				cell.mergePool(pool);
				return;
			}
		}

		DiscCell cell = new DiscCell(pool);
		cells.add(cell);
	}

	/**	�������ۼ�¼�嵥����������ܵ���ϴ����Żݽ��. �����������ڸ���������ԪDiscCell��.
	 * ���� SaleList.consumeFavor ���԰���Щ��ϴ����Żݽ���̯����Ʒ���ۼ�¼.
	 * �㷨Ҫ��:
	 * 1) ������е��Ż�.
	 * 2) ͳ�ƿ�����������Żݵ���Ʒ������.
	 * 3) ������Ʒ�����嵥�����������Ż���Ʒ�����.
	 * 4) ������Ʒ������������ܵ��Żݽ��.
	 * @see SaleList.consumeFavor
	 * @param sale_lst	��Ʒ���ۼ�¼�嵥
	 */
	public void computeFavor(SaleList sale_lst) {
		clear();
		checkinSaleList(sale_lst);
		setFavorBaskets();
		setFavor();
	}

	/**
	 * @param sale_lst
	 */
	public void computeFavorAfter(SaleList sale_lst) {
		clear();
		checkinSaleListAfter(sale_lst);
		setFavorBaskets();
		setFavor();
	}

	/**
	 * @return		�������ܴ����������������
	 */
	public int getFavorBaskets() {
		return favored_baskets;
	}

	/**
	 * @param sale_lst
	 */
	public void clearFavor(SaleList sale_lst) {
		for (int i = 0; i < this.size(); i++)
			this.get(i).clearinSaleList(sale_lst);
	}

	/**
	 * ����Ż�
	 */
	private void clear() {
		for (int i = 0; i < this.size(); i++)
			this.get(i).clear();
	}

	/**
	 * @param sale_lst
	 */
	private void checkinSaleList(SaleList sale_lst) {
		for (int i = 0; i < this.size(); i++)
			this.get(i).checkinSaleList(sale_lst);
	}

	/**
	 * @param g
	 * @return
	 */
	public boolean match(Goods g) {
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).match(g))
				return true;
		}
		return false;
	}

	public boolean match(String goodsNo) {
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).match(goodsNo))
				return true;
		}
		return false;
	}

	/**
	 * @param sale_lst
	 */
	private void checkinSaleListAfter(SaleList sale_lst) {
		for (int i = 0; i < this.size(); i++)
			this.get(i).checkinSaleListAfter(sale_lst);
	}

	/**
	 * �˺����������е���Ʒ��ϵ�Ԫ, �������Ԫ����������С��������Сֵ. ����Сֵ��Ϊ�������Żݵ���Ʒ�����.
	 */
	private void setFavorBaskets() {
		int min = 0;

		if (cells.size() > 0)
			min = this.get(0).getSoldBaskets();

		for (int i = 1; i < this.size(); i++) {
			int n = this.get(i).getSoldBaskets();
			if (n < min)
				min = n;
		}
		favored_baskets = min;
	}

	/**
	 * ����������ϵ�ԪDiscCell �Ŀ��Ż�С����.
	 */
	private void setFavor() {
		for (int i = 0; i < this.size(); i++)
			this.get(i).setFavor(favored_baskets);
	}

	/**
	 * for debug use.
	 */
	public String toString() {
		String s = "### DiscComplex ###\n";
		s += "Groupid	" + groupid + "\n";
		s += "Name		" + name + "\n";
		for (int i = 0; i < size(); i++)
			s += "# " + i + " " + get(i).toString() + "\n";
		s += "######\n";
		return s;
	}

	public DiscComplex[] split() {

		if (cells != null) {

			ArrayList cellsList = new ArrayList();

			if (cells.size() > 1) {

				DiscCell[] cells1 = ((DiscCell) cells.get(0)).split();
				DiscCell[] cells2 = ((DiscCell) cells.get(1)).split();

				for (int i = 0; i < cells1.length; i++) {
					for (int j = 0; j < cells2.length; j++) {
						ArrayList horizonCellsList = new ArrayList();
						horizonCellsList.add(cells1[i]);
						horizonCellsList.add(cells2[j]);
						cellsList.add(horizonCellsList);
					}
				}

				for (int k = 2; k < cells.size(); k++) {

					ArrayList newcellsList = new ArrayList();
					DiscCell[] cells3 = ((DiscCell) cells.get(k)).split();

					for (int i = 0; i < cellsList.size(); i++) {
						for (int j = 0; j < cells3.length; j++) {
							ArrayList horizonCellsList = new ArrayList();
							horizonCellsList.addAll(
								(ArrayList) cellsList.get(i));
							horizonCellsList.add(cells3[j]);
							newcellsList.add(horizonCellsList);
						}
					}

					cellsList = newcellsList;
				}

			} else {

				if(cells.size()>0){

					DiscCell[] cells1 = ((DiscCell) cells.get(0)).split();
					for (int i = 0; i < cells1.length; i++) {
						ArrayList horizonCellsList = new ArrayList();
						horizonCellsList.add(cells1[i]);
						cellsList.add(horizonCellsList);
					}
					
				}
				
			}

			DiscComplex[] result = new DiscComplex[cellsList.size()];

			for (int i = 0; i < cellsList.size(); i++) {

				DiscComplex complex =
					new DiscComplex(
						groupid + "_" + i,
						name,
						favored_price,
						start_date,
						end_date,
						start_time,
						end_time,
						level);

				complex.getCells().addAll((ArrayList) cellsList.get(i));

				result[i] = complex;

			}

			return result;

		}

		return null;

	}

	/**
	 * <code>cells</code>				���������� DiscCell (������Ԫ) ���嵥
	 */
	private Vector cells = null;

	/**
	 * <code>groupid</code>				���������ı��
	 */
	private String groupid;

	/**
	 * <code>name</code>				������������
	 */
	private String name;

	/**
	 * <code>favored_price</code>		�����������ܼ�(�����ۼ�)
	 */
	private int favored_price;

	/**
	 * <code>favored_baskets</code>		����������ϴ�����������Ʒ������
	 */
	private int favored_baskets = 0;

	/**
	 * <code>end_date</code>			��ֹ����
	 */
	/**
	 * <code>start_date</code>			��Ч����
	 */
	private Day start_date, end_date;

	/**
	 * <code>end_time</code>			��ֹʱ��
	 */
	/**
	 * <code>start_time</code>			��Чʱ��
	 */
	private PosTime start_time, end_time;

	/**
	 * <code>level</code>				�������������ȼ�
	 */
	private int level;
}
