/*
 * �������� 2005-10-24
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.royalstone.pos.util;

/**
 * @author zhouzhou
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */

// ������Ʒ��Ϣ��  // δ��  ��ɹ����ݻ�
public class vgno_t {
	String barcode = "00000";  
	String deptid = "1111111";
	int discountValue = 0;
	String name = "Ԥ������Ʒ";	//��Ʒ����
	int oiltiscCount = 0;
	int price = 0;				//��Ʒ�۸�
	String spec;
	int type = 0;				
	String unit = "";
	String vgno = "999999";
	int x = 1;
	
 public String getBarcode(){
 	return barcode;
 	}
 
 public void setBarcode(String barcode){
 	this.barcode = barcode;
 	}
 
 public String deptid(){
 	return deptid;
 	}
 
 public void setDeptid(String deptid){
 	this.deptid = deptid;
 	}
 
 public int getDiscountValue(){
 	return discountValue;
 	} 
 
 public void setDiscountValue(int discountValue){
 	this.discountValue = discountValue;
 	}
 
 
 
}
