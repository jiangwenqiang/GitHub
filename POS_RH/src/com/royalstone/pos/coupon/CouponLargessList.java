package com.royalstone.pos.coupon;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

public class CouponLargessList implements Serializable {
	
	public CouponLargessList() {
		lst = new Vector();
	}

	public boolean add(CouponLargess couponLargess) {
		if (couponLargess == null) {
			return false;
		}
		if (!couponLargess.getExceptionInfo().equals("")){
			return false;
			}

		if (lst.size() > 0) {
			for (int i = 0; i < lst.size(); i++) {
				CouponLargess Cou = (CouponLargess) lst.get(i);
				if (Cou != null && Cou.getTypeID() != null
						&& Cou.getTypeID().equals(couponLargess.getTypeID())) {
					if (Cou.getTypeID().equals(couponLargess.getTypeID())
							&& Cou.getPresenttype().equals(couponLargess.getPresenttype())){
						return false;
						}
				}
			}
		}
//		lst.add(couponLargess);
		regroupLargess(couponLargess);
		return true;
	}
	
	// 重组信息
	public void regroupLargess(CouponLargess couponLargess){
		if (lst.size() > 0 ){
			for (int i = 0; i < lst.size(); i++){
				CouponLargess Cou = (CouponLargess) lst.get(i);
				if (Cou.getPresenttype().equals(couponLargess.getPresenttype())){
					Cou.setAddPriceL(couponLargess.getAddPrice());
					return;
					}
				}
			lst.add(couponLargess);
			return;
			}
		lst.add(couponLargess);
		return;
		}
	
	// 创建XML文件
	public Element toElement(){
		
        Element elm_list = new Element("CouponLargessList");
		for (int i = 0; i < lst.size(); i++)
			elm_list.addContent(((CouponLargess) lst.get(i)).toElement());
		return elm_list;	
		}

	/*
	 * public int getTotalValue(){ int total=0; for(int i=0;i <lst.size();i++){
	 * int
	 * couponValue=(int)Math.rint(((CouponLargess)lst.get(i)).getPrice().doubleValue()*100);
	 * total=total+couponValue; } return total; }
	 */
	/**
	 * @return
	 */
	public int size() {
		return lst.size();
	}

	public CouponLargess get(int i) {
		return (CouponLargess) lst.get(i);
	}

	public void remove() {

		lst.removeAllElements();

	}
	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	
//	public boolean couponPay(String coupon, int price){
//		for (int i = 0; i < lst.size(); i++){
//			CouponLargess couponLargess = (CouponLargess)lst.get(i);
//			if (couponLargess.getTypeID().equals(coupon)){
//				if (couponLargess.dumpVgnoPrice(price)){
//					return true;
//					}
//				}
//			}
//		return false;
//		
//		}

	public void removeCouponSale(BigDecimal count) {
		for (int i = 0; i < lst.size(); i++) {
			CouponLargess couponLargess = (CouponLargess) lst.get(i);
			if (couponLargess.getPresentamount() < 0) {
				if (couponLargess.getPrice().equals(count)){
					lst.remove(couponLargess);
//				if ((int) Math.rint(couponLargess.getPrice() * 100) == (int) Math.rint(count * 100))
//					lst.remove(couponLargess);
					}
				}
			}
		}
	
	public void setLargessType(char largessType){
		this.largessType = largessType;
		}
	
	public char getLargessType(){
		return largessType;
		}
	
	
    public CouponLargessList fromElement(Element root , CouponLargessList largess){
    	
        List list;
		try {
			list = root.getChildren("CouponLargess");

			for (int i = 0; i < list.size(); i++)
				largess.add(new CouponLargess((Element) list.get(i)));
			return largess;

		} catch (Exception e) {
			e.printStackTrace();
			return largess;
		}
    }

	/**
	 * Comment for <code>lst</code>
	 */
	private Vector lst;

	private String updateType = "0";
	private char largessType = 'n';
}

