package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * User: fire
 * Date: 2005-6-23
 */
public class CardCouponInfoNew implements Serializable{
		
	String shopID;  		//所属机构
	String typeID;			//卷类型编码
	String beginDate;		//有效期开始
	String endDate;			//有效期结束
	int beginNo;			//数量起始段
	int endNo;				//数量结束段
	BigDecimal discount; 	//卷折扣率
	BigDecimal psFlag;		//赠品标志
	String presenttype; 	//赠卷类型
	int psMode;		//赠送方式
	int presentamount;		//赠送数量
	BigDecimal price;			//卷面值
	String sheetID;			//单号
	String	note;			//备注
	
	public void setShopID(String ShopID){
		this.shopID = ShopID;
		}
	public String getShopID(){
		return shopID;
		}
	
	public void setTypeID(String TypeID){
		this.typeID = TypeID;
		}
	public String getTypeID(){
		return typeID;
		}
	
	public void setBeginDate(String BeginDate){
		this.beginDate = BeginDate;
		}
	public String getBeginDate(){
		return beginDate;
		}
	
	public void setEndDate(String EndDate){
		this.endDate = EndDate;
		}
	public String getEndDate(){
		return endDate;
		}
	
	public void setBeginNo(int BeginNO){
		this.beginNo = BeginNO;
		}
	public int getBeginNO(){
		return beginNo;
		}
	
	public void setEndNo(int EndNo){
		this.endNo = EndNo;
		}
	public int getEndNo(){
		return endNo;
		}
	
	public void setDiscount(BigDecimal Discount){
		this.discount = Discount;
		}
	public BigDecimal getDiscount(){
		return discount;
		}
	
	public void setPsFlag(BigDecimal PsFlag){
		this.psFlag = PsFlag;
		}
	public BigDecimal getPsFlag(){
		return psFlag;
		}
	
	public void setPresenttype(String Presenttype){
		this.presenttype = Presenttype;
		}
	public String getPresenttype(){
		return presenttype;
		}
	
	public void setPsMode(int PsMode){
		this.psMode = PsMode;
		}
	public int getPsMode(){
		return psMode;
		}
	
	public void setPresentamount(int Presentamount){
		this.presentamount = Presentamount;
		}
	public int getPresentamount(){
		return presentamount;
		}
	
	public void setPrice(BigDecimal Price){
		this.price = Price;
		}
	public BigDecimal getPrice(){
		return price;
		}
	
	public void setSheetID(String SheetID){
		this.sheetID = SheetID;
		}
	public String getSheetID(){
		return sheetID;
		}
	
	public void setNote(String Note){
		this.note = Note;
		}
	public String getNote(){
		return note;
		}
	
	
	}
