/*
 * �������� 2005-12-12
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author zhouzhou
 * 
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class CardCouponInfoList implements Serializable {

	private String headShopID="";

	private String Exception;

	public CardCouponInfoList() {

		lst = new Vector();

	}

	public boolean add(CardCouponInfoNew cardCouponInfoNew) {

		lst.add(cardCouponInfoNew);

		return true;

	}

	public int size() {

		return lst.size();

	}

	public void setException(String Exception) {
		this.Exception = Exception;
	}

	public String getException() {
		return Exception;
	}

	public CardCouponInfoNew getInfo(int i) {

		return (CardCouponInfoNew) lst.get(i);

	}

	Vector lst;

	public String getHeadShopID() {
		return headShopID;
	}

	public void setHeadShopID(String headShopID) {
		this.headShopID = headShopID;
	}

}