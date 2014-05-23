package com.royalstone.pos.favor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import com.royalstone.pos.common.Goods;
import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.invoke.realtime.RealTime;
import com.royalstone.pos.invoke.realtime.RealTimeException;
import com.royalstone.pos.notify.UnitOfWork;
import com.royalstone.pos.util.PosConfig;

/**
   @version 1.0 2004.05.25
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

/**	BulkPriceList ����ʵ����������.
 * @author Mengluoyi
 *
 */
public class BulkPriceList implements Serializable {

	/**
	 * for debug use.
	 *
	public void print()
	{
		for (int i = 0; i < list.size(); i++)
			System.out.println((BulkPrice) list.get(i));
	}
	*/
	/**
	 * Constructor
	 */
	public BulkPriceList() {
		list = new Vector();
	}

	/**
	 * @param goods
	 * @param qty
	 * @param cust_level
	 * @return
	 *
	public boolean matches(Goods goods, int qty, int cust_level)
	{
		for (int i = 0; i < list.size(); i++) {
			DiscCriteria c = (DiscCriteria) list.get(i);
			if (c.matches(goods, qty, cust_level)) return true;
		}
		return false;
	}
	*/
	/**
	 * @param goods
	 * @param qty
	 * @param cust_level
	 * @return
	 *
	public Discount getDiscount(Goods goods, int qty, int cust_level)
	{
		for (int i = 0; i < list.size(); i++) {
			DiscCriteria c = (DiscCriteria) list.get(i);
			if (c.matches(goods, qty, cust_level))
				return c.getDiscount(goods, qty, cust_level);
		}
		return new Discount(Discount.NONE);
	}
	*/
	/**
	 * @param goods
	 * @param qty
	 * @param cust_level
	 * @return
	public BulkPrice getBulkPrice(Goods goods, int qty, int cust_level)
	{
		for (int i = 0; i < list.size(); i++) {
			BulkPrice b = (BulkPrice) list.get(i);
			if (b.matches(goods, qty, cust_level))
				return b;
		}
		return null;
	}
	 */

	/**
	 * @param goods	��Ʒ
	 * @param qty	��Ʒ��������.
	 * @return		���û���ҵ����ʵ������Ż�,�򷵻�Ϊnull.
	 */
	public BulkFavor getBulkFavor(Goods goods, int qty)throws RealTimeException {
        //TODO ���ݸ��� by fire  2005_5_11
         PosConfig config=PosConfig.getInstance();
        String isRealTime=config.getString("ISREALTIME");
		if (PosContext.getInstance().isOnLine()&&"ON".equals(isRealTime)) {
			BulkPrice b = RealTime.getInstance().getBulkPrice(goods.getVgno());
			if (b != null) {
				return performGetBulkFavor(b, goods, qty);
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				BulkPrice b = (BulkPrice) list.get(i);
				BulkFavor f = performGetBulkFavor(b, goods, qty);
				if (f != null)
					return f;
			}
		}
		return null;

	}

	/**
	 * NOTE: BulkPrice �д���6������������۸�.
	 * ����getDiscBulk ��������һ��DiscBulk ����, �ڸö����п��Եõ����������������۸�.
	 * ������Ʒ�Ļ�������,���������������۸���Լ��������Ʒ�����ܵ������Ż����������ܽ��.
	 * @param b	BulkPrice ����, ���������۸�������(��Ϊ6��).
	 * @param goods	��Ʒ
	 * @param qty	��Ʒ�Ļ�������
	 * @return		����Ʒ�����ܵ������Ż�(���������).
	 * @see			BulkFavor, DiscBulk.
	 */
	private BulkFavor performGetBulkFavor(BulkPrice b, Goods goods, int qty) {
		if (goods.getPType().equals(DiscCriteria.BULKPRICE)
			&& b.matches(goods, qty)) {
			DiscBulk disc = b.getDiscBulk(goods, qty);
			if (disc != null) {
				int favor_qty = qty - qty % disc.getBulkVolume();
				int favor_value =
					favor_qty * goods.getPrice() / goods.getX()
						- favor_qty / disc.getBulkVolume() * disc.getBulkPrice();
				return new BulkFavor(favor_qty, favor_value);
			}
		}
		return null;
	}

	/**	�˺���������������Ӽ�¼.
	 * @param criteria	������Ϣ
	 */
	public void add(DiscCriteria criteria) {
		list.add(criteria);
	}

	/**
	 * @return	����������ĳ���.
	 */
	public int size() {
		return list.size();
	}

	/**	�÷�����BulkPriceList ���������д���ļ� file ��.
	 * @param file	�洢BulkPriceList ������ļ�
	 */
	public void dump(String file) {
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file));
			for (int i = 0; i < list.size(); i++) {
				DiscCriteria c = (DiscCriteria) list.get(i);
				out.writeObject(c);
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**	��ָ���ļ���װ����������������.
	 * @param file	�洢�����������ݵ��ļ�
	 */
	public void load(String file) {
		ObjectInputStream in;
		DiscCriteria c;

		System.out.println("Load Bulkprice_list ... ");
		try {
			in = new ObjectInputStream(new FileInputStream(file));
			while (true) {
				c = (DiscCriteria) in.readObject();
				add(c);
			}
		} catch (java.io.EOFException eof) {
			/**
			 * End of file met, all data loaded.
			 */
			System.out.println("Bulkprice_list Loaded.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(String goodsNo, BulkPrice bulkPrice) {

		BulkPrice b = getBulkPrice(goodsNo);
		if (b != null) {
			list.remove(b);
		}
		if (bulkPrice != null) {
			list.add(b);
		}

		if (b == null && bulkPrice == null) {
			UnitOfWork.getInstance().updateBulk(true);
		}

	}

	public BulkPrice getBulkPrice(String goodsNo) {
		for (int i = 0; i < list.size(); i++) {
			BulkPrice b = (BulkPrice) list.get(i);
			if (b.matches(goodsNo))
				return b;
		}
		return null;
	}

	/**
	 * <code>list</code>	�洢�����������ݵ��ڲ��ṹ.
	 */
	private Vector list;
}
