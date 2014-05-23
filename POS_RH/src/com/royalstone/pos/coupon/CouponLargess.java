package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

import org.jdom.Element;

/**
 * @author zhouzhou
 */

public class CouponLargess implements Serializable{

	    
	    // 定义的 couponLargess 结构
	    private String TypeID;
	    private BigDecimal discount;
	    private BigDecimal psMode;
	    private int presentamount;
	    // 赠送的卷类型
	    private String presenttype;
	    // 出错信息
	    private String exceptionInfo = "";
	    // 单价总额
	    private BigDecimal price;
	    // 数量的基量
	    private int count;
	    // 赠送方式  0 没有，1，赠卷 2， 赠商品 
	    private BigDecimal PsFlag;
	    
	    private int addPrice;
	    
	    // 碰销基数（数量段的结束段）
	    private int endNo;
	    // 处理数量段标志位 
	    private boolean Flag = false;
	    // 处理数量问题起始数量
	    private int BeginNo;
	    // 处理数量问题可折卷数
	    private int DiscontNo;
	
	    
	    public void setDiscontNo(int discontNo){
	    	this.DiscontNo = discontNo;
	    	}
	    public int getDiscontNo(){
	    	return DiscontNo;
	    	}
	    
	    public void setBeginNo(int beginNo){
	    	this.BeginNo = beginNo;
	    	}
	    public int getBeginNo(){
	    	return BeginNo;
	    	}
	    
	    public void setEndNo( int endNo){
	    	this.endNo =  endNo;
	    	}
	    public int getEndNO(){
	    	return  endNo;
	    	}
	    
	    public void setFlag(boolean flag){
	    	this.Flag = flag;
	    	}
	    public boolean getFlag(){
	    	return Flag;
	    	}
	    
	    public void setAddPrice(int addPrice){
	    	
	    	this.addPrice = addPrice;
	    	}
	    
	    public int getAddPrice(){
	    	return addPrice;
	    	}
	    
	    public void setAddPriceL(int addPrice){
	    	this.addPrice = (getAddPrice() + addPrice);
	    	}
	    
	    
	    public void setCount(int count){
	    	this.count = count;
	    	}
	    public int getCount(){
	    	return count;
	    	}
	    
	    public void setExceptionInfo(String exceptionInfo){
	    	this.exceptionInfo = exceptionInfo;
	    	}
	    public String getExceptionInfo(){
	    	return exceptionInfo;
	    	}
	    
	    public void setTypeID(String typeID){
	    	this.TypeID = typeID;
	    	}
	    public String getTypeID(){
	    	return TypeID;
	    	}
	    public void setDiscount(BigDecimal discount){
	    	this.discount = discount;
	    	}
	    
	    public BigDecimal getDiscount(){
	    	return discount;
	    	}
	    public void setPsMode(BigDecimal psMode){
	    	this.psMode = psMode;
	    	}
	    public BigDecimal getPsMode(){
	    	return psMode;
	    	}
	    
	    public void setPsFlag(BigDecimal PsFlag){
	    	this.PsFlag = PsFlag;
	    	}
	    public BigDecimal getPsFlag(){
	    	return PsFlag;
	    	}
	    
	    public void setPresentamount(int presentamount){
	    	this.presentamount = presentamount;
	    	}
	    public int getPresentamount(){
	    	return presentamount;
	    	}
	    
	    public void setPrice(BigDecimal price){
	    	this.price = price;
	    	}
	    public BigDecimal getPrice(){
	    	return price;
	    	}
	    
	    public void setPresenttype(String presenttype){
	    	this.presenttype = presenttype;
	    	}
	    public String getPresenttype(){
	    	return presenttype;
	    }
	    
	    public boolean dumpVgnoPrice(int price){
	    	if ((getAddPrice() - price) >= 0){
	    		setAddPrice(getAddPrice() - price);
	    		return true;
	    		}
	    	return false;
	    	}
	    
	    // 卷数量减掉
	    public boolean dumpLagessCount(){
	    	setPresentamount(getPresentamount() - 1);
	    	if (getPresentamount() < 0)
	    		return false;
	    	return true;
	    	}
	    
	    // 商品数量减掉
	    public boolean dumpLagessCount(int Count){
	    	if (getCount() == Count){
	    		setPresentamount(getPresentamount() - Count);
	    		if (getPresentamount() < 0){
	    			return false;
	    			}
	    		return true;
	    	}
	    	return false;
	    	}
	    
	    public Element toElement() {
	    	
	           Element g = new Element("CouponLargess");
	           g.addContent(new Element("TypeID").addContent(TypeID));
	           g.addContent(new Element("discount").addContent(String.valueOf(discount.doubleValue())));
	           g.addContent(new Element("psMode").addContent(String.valueOf(psMode.doubleValue())));
	           g.addContent(new Element("presentamount").addContent(String.valueOf(presentamount)));
	           g.addContent(new Element("presenttype").addContent(presenttype));
	           g.addContent(new Element("exceptionInfo").addContent(exceptionInfo));
	           g.addContent(new Element("price").addContent(String.valueOf(price.doubleValue())));
	           g.addContent(new Element("count").addContent(String.valueOf(count)));
	           g.addContent(new Element("PsFlag").addContent(String.valueOf(PsFlag.doubleValue())));
	           g.addContent(new Element("addPrice").addContent(String.valueOf(addPrice)));
	           g.addContent(new Element("endNo").addContent(String.valueOf(endNo)));      
	           return g;
	           
	       }
	    
	       public CouponLargess(Element elm) {
	           try {
	           	
	               TypeID = elm.getChildTextTrim("TypeID");
	               discount = new BigDecimal(elm.getChildTextTrim("discount"));
	               psMode = new BigDecimal(elm.getChildTextTrim("psMode"));
	               presentamount = Integer.parseInt(elm.getChildTextTrim("presentamount"));
	               presenttype = elm.getChildTextTrim("presenttype");
	               exceptionInfo = elm.getChildTextTrim("exceptionInfo");
	               price = new BigDecimal(elm.getChildTextTrim("price"));
	               count = Integer.parseInt(elm.getChildTextTrim("count"));
	               PsFlag = new BigDecimal(elm.getChildTextTrim("PsFlag"));
	               addPrice = Integer.parseInt(elm.getChildTextTrim("addPrice"));
	               endNo = Integer.parseInt(elm.getChildTextTrim("endNo"));
	               
	           } catch (Exception e) {
	               e.printStackTrace();
	               throw new IllegalArgumentException();
	           }
	       }
	       
	       public CouponLargess(){
	       	}
	    
	    
	    
	  
	}

