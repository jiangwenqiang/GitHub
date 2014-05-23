package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * User: fire
 * Date: 2005-6-23
 */
public class CouponInfo implements Serializable{
	
    BigDecimal coun;
    String typeid = null;
    String goodsid = null; 
    boolean CouponInfo = false;
    
    public void setCoun(BigDecimal coun){
    	this.coun = coun;
    	}
    
    public void setTypeID(String typeid){
    	this.typeid= typeid;
    	}
    
    public void setGoodsid(String goodsid){
    	this.goodsid = goodsid;
    	}
    
    public void setCouponInfo(boolean CouponInfo){
    	this.CouponInfo = CouponInfo;
    	}
    
    public BigDecimal getCoun(){
    	return coun;
    	}
    
    public String getTypeID(){
    	return typeid;
    	}
    
    public String getGoodsID(){
    	return goodsid;
    	}
    
    public boolean getCouponInfo(){
    	return CouponInfo;
    	}
}
