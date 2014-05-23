/*
 * �������� 2005-12-9
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
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
	
	//�����Ӧ�Ķ���
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
	
	// �����
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
	// ��ȡ�Ƿ�����
	public boolean getDiscount(int i){
		CouponTypeInfo info = (CouponTypeInfo)lst.get(i);
		return info.getDiscount();
		}
	
	//�������û���ô��Ķ���
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
	// �õ����� ���Ҽ����ܴ��۵���Ʒ����
	public int getCouponCounnt(String Coupon){
		// ���������һ�һ�ž�
		int counponCount = 0;
		// ������ܹ��м�����Ʒ
		int CountGross = 0;
		// �ܶ��ֵĵ�����
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
	// �õ����Դ��۵���Ʒ������
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
	
	// �õ�һ�ŵľ��ܻ������Ʒ������
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

	// ��ȡ�Ƿ���Դ���
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
	// �õ�ĳ����Ʒ������
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
