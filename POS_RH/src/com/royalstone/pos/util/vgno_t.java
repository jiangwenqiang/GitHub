/*
 * 创建日期 2005-10-24
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.royalstone.pos.util;

/**
 * @author zhouzhou
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

// 特殊商品信息类  // 未完  完成工作暂缓
public class vgno_t {
	String barcode = "00000";  
	String deptid = "1111111";
	int discountValue = 0;
	String name = "预订单商品";	//商品名称
	int oiltiscCount = 0;
	int price = 0;				//商品价格
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
