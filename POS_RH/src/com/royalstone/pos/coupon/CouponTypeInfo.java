package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhouzhou
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class CouponTypeInfo implements Serializable {
	
	// ��Ʒ����
	String vgnoName;
	// ������ 
	String couponType;
	// ������������һ�ž�
	BigDecimal couponCount;
	// ������Ʒ�ܶһ��ľ������
	int coupon = 0;
	// ��Ӧ�������Ʒ���۵�����
	int VgnoCount = 0;
	// �����Ʒ�ڵ��е�����
	int VgnoGross = 0;
	//�Ƿ���� false NO ture YES
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
	
	

