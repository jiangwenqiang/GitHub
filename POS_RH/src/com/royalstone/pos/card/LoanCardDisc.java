package com.royalstone.pos.card;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liangxinbiao
 */
public class LoanCardDisc implements Serializable{
	
	public final static int DISC_PRICE=0;
	public final static int DISC_RATE=1;
	
	public final static int BIGDEPT_ITEMTYPE=1;
	public final static int MIDDEPT_ITEMTYPE=2;
	public final static int SMALLDEPT_ITEMTYPE=3;
	public final static int SINGLE_ITEMTYPE=4;

	/**
	 * �������� 1=���� 2=���� 3=С�� 4=��Ʒ
	 */
	private int itemType;

	/**
	 * ��� ����Ż�����Ʒ����
	 */
	private int itemID;

	/**
	 * 0=�Żݶ�� 1=�ٷֱ�
	 */
	private int discType;

	/**
	 * ���ʿͻ���Ʒ�Żݶ�� �ٷֱȻ����Żݽ��
	 */
	private BigDecimal discCount;

	/**
	 * @return
	 */
	public BigDecimal getDiscCount() {
		return discCount;
	}

	/**
	 * @return
	 */
	public int getDiscType() {
		return discType;
	}

	/**
	 * @return
	 */
	public int getItemID() {
		return itemID;
	}

	/**
	 * @return
	 */
	public int getItemType() {
		return itemType;
	}

	/**
	 * @param decimal
	 */
	public void setDiscCount(BigDecimal decimal) {
		discCount = decimal;
	}

	/**
	 * @param i
	 */
	public void setDiscType(int i) {
		discType = i;
	}

	/**
	 * @param i
	 */
	public void setItemID(int i) {
		itemID = i;
	}

	/**
	 * @param i
	 */
	public void setItemType(int i) {
		itemType = i;
	}

}
