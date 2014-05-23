/*
 * 创建日期 2005-12-9
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
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

public class CouponTypeInfoList  implements Serializable {
	
	String exception = "";
	
	public CouponTypeInfoList(){
		lst = new Vector();
		}
	
	public void setException(String Exception){
		this.exception = Exception;
		}
	
	public String getException(){
		return exception;
		}
	
	public boolean add(CouponTypeInfo couponTypeInfo){
		lst.add(couponTypeInfo);
		return true;
		}
	
	public int size(){
		return lst.size();
		}
	
	//清除对应的对象
	public void removeCouponSale(CouponTypeInfo couponTypeInfo) {
		for (int i = 0; i < lst.size(); i++) {
			CouponTypeInfo info = (CouponTypeInfo)lst.get(i);
			if (info.getVgnoName().equals(couponTypeInfo.getVgnoName())){
					lst.remove(info);
					}
				}
	}
	
	public void removeAll(){
		lst.removeAllElements();
		}
	
	// 清除卷
	public void removeCouponType(String coupon){
		for (int i = 0; i < lst.size(); i ++){
			CouponTypeInfo info = (CouponTypeInfo)lst.get(i);
		  if (info.getCounponType().equals(coupon)){
		  		info.setDiscount(true);
//					lst.remove(info);
					}
		}
	}
	
	public String getCouponType(int i){
//		for (int i = 0; i < lst.size(); i ++){
			CouponTypeInfo info = (CouponTypeInfo)lst.get(i);
			return info.getCounponType();
//			}
//		return "";
		}
	// 获取是否打过折
	public boolean getDiscount(int i){
		CouponTypeInfo info = (CouponTypeInfo)lst.get(i);
		return info.getDiscount();
		}
	
	//清除所有没有用处的对象
	public void rmoveNull(){
  			for (int i = 0; i < lst.size(); i ++){
  				CouponTypeInfo infp = (CouponTypeInfo)lst.get(i);
  				if ( infp.getException() != null){
  					for (int A = 0; A < lst.size(); A++){
  						CouponTypeInfo dd = (CouponTypeInfo)lst.get(A);
  						if (dd.vgnoName.equals(infp.getVgnoName())){
  							lst.remove(i);
  							}
  						}
  					}
  			}
  		}
	
	public void add(String vgno, int vgnoCount ){
		int disc = 0;
		for (int i = 0; i < lst.size(); i ++){
			CouponTypeInfo info = (CouponTypeInfo)lst.get(i);
			if (info.getVgnoName().equals(vgno)){
				info.setVgnoGross(vgnoCount);
				}
			}
		}
	// 得到卷数 并且计算能打折的商品数量
	public int getCouponCounnt(String Coupon){
		// 多少数量兑换一张卷
		int counponCount = 0;
		// 这类卷总共有几件商品
		int CountGross = 0;
		// 能兑现的的张数
		int CouponGross = 0;
		
		for (int i = 0; i < lst.size(); i ++){
			CouponTypeInfo coupon = (CouponTypeInfo)lst.get(i);
			if (coupon.getCounponType().equals(Coupon)){
				counponCount = (int)Math.rint(coupon.getCounponCount().doubleValue());
				CountGross += coupon.getVgnogross();
				}
			}
		if (CountGross == 0)
			return CountGross;
		 CountGross = Math.round(CountGross/counponCount);
		return CountGross;
		
		}
	// 得到可以打折的商品总数量
	public int getCouponCounnt(String Coupon, int Count){
		int counposCount = 0 ;
		for (int i = 0; i < lst.size(); i ++){
			CouponTypeInfo coupon = (CouponTypeInfo)lst.get(i);
			if (coupon.getCounponType().equals(Coupon)){
				counposCount = (int)Math.rint(coupon.getCounponCount().doubleValue());
			return	counposCount * Count;
				}
		}
		return 0;
		}
	
	// 得到一张的卷能换算的商品的数量
	public int getVgnoCount(String Coupon){
		int counposCount = 0 ;
		for (int i = 0; i < lst.size(); i ++){
			CouponTypeInfo coupon = (CouponTypeInfo)lst.get(i);
			if (coupon.getCounponType().equals(Coupon)){
				counposCount = (int)Math.rint(coupon.getCounponCount().doubleValue());
			return	counposCount;
				}
		}
		return 0;
		}

	// 获取是否可以打折
	public boolean YNGRR(String Coupon, String Vgno){
		for (int i = 0; i < lst.size(); i++){
			CouponTypeInfo coupon = (CouponTypeInfo)lst.get(i);
			if (coupon.getVgnoName().equals(Vgno) && coupon.getCounponType().equals(Coupon)){
				if (!coupon.getDiscount()){
					return true;
					}
				}
			}
		return false;
		}
	// 得到某件商品的数量
	public int getVgnoCount(String Coupon , String Vgno){
		for (int i = 0; i < lst.size(); i++){
			CouponTypeInfo coupon = (CouponTypeInfo)lst.get(i);
			if (coupon.getVgnoName().equals(Vgno) && coupon.getCounponType().equals(Coupon)){
				return (int)Math.rint(coupon.getCounponCount().doubleValue());
				}
			}
		return 0;
		
		}
	
	Vector lst;
}
