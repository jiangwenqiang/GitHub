package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhouzhou
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class CouponTypeInfo implements Serializable {
	
	// 商品编码
	String vgnoName;
	// 卷类型 
	String couponType;
	// 多少数量满足一张卷
	BigDecimal couponCount;
	// 这类商品能兑换的卷的张数
	int coupon = 0;
	// 对应这类卷商品打折的数量
	int VgnoCount = 0;
	// 这件商品在单中的数量
	int VgnoGross = 0;
	//是否打折 false NO ture YES
	boolean type = false;
	
	String exception;
	
	public void setVgnoName(String vgnoName){
		this.vgnoName = vgnoName;
		}
	public String getVgnoName(){
		return vgnoName;
		}
	
	public void setCouponType(String couponType){
		this.couponType = couponType;
		}
	public String getCounponType(){
		return couponType;
		}
	
	public void setCouponCount(BigDecimal couponCount){
		this.couponCount = couponCount;
		}
	public BigDecimal getCounponCount(){
		return couponCount;
		}
	
	public void setCoupon(int coupon){
		this.coupon = coupon;
		}
	public int getCoupon(){
		return coupon;
		}
	
	public void setException(String Exception){
		this.exception = Exception;
		}
	public String getException(){
		return exception;
		}
	
	public void setVgnoCount(int VgnoCount){
		this.VgnoCount = VgnoCount;
		}
	public int getVgnoCount(){
		return VgnoCount;
		}
	
	public void setVgnoGross(int vgnoGross){
		this.VgnoGross = this.VgnoGross + vgnoGross;
		}
	public int getVgnogross(){
		return VgnoGross;
		}
	
	public void setDiscount(boolean type){
		this.type = type;
		
		}
	public boolean getDiscount(){
		return type;
		}
	}
	
	

