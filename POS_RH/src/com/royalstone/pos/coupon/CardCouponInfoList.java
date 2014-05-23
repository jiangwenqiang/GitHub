/*
 * 创建日期 2005-12-12
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author zhouzhou
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
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