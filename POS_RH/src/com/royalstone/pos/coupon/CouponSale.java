package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

import org.jdom.Element;

/**
 * User: fire
 * Date: 2005-6-23
 */
public class CouponSale implements Serializable{

    public String getCouponID() {
        return couponID;
    }

    public void setCouponID(String couponID) {
        this.couponID = couponID;
    }

    public String getCouponPass() {
        return couponPass;
    }

    public void setCouponPass(String couponPass) {
        this.couponPass = couponPass;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getPosID() {
        return posID;
    }

    public void setPosID(String posID) {
        this.posID = posID;
    }

    public String getCashierID() {
        return cashierID;
    }

    public void setCashierID(String cashierID) {
        this.cashierID = cashierID;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }


    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public CouponSale() {
    }


    public Element toElement() {
           Element g = new Element("CouponSale");
           g.addContent(new Element("couponID").addContent(couponID));
           g.addContent(new Element("couponPass").addContent(couponPass));
           g.addContent(new Element("shopID").addContent(shopID));
           g.addContent(new Element("posID").addContent(posID));
           g.addContent(new Element("cashierID").addContent(cashierID));
           g.addContent(new Element("mode").addContent(mode));
           g.addContent(new Element("minFlag").addContent(minFlag));
           g.addContent(new Element("saleValue").addContent(Double.toString(saleValue)));
           g.addContent(new Element("validValue").addContent(String.valueOf(validValue)));
           //g.addContent(new Element("price").addContent(price.toString()));
 //          g.addContent(new Element("UpdateType").addContent(UpdateType));
           return g;
       }

           /**
        * @param elm 用XML对象生成LoanCardPayVO
        */
       public CouponSale(Element elm) {
           try {
               couponID = elm.getChildTextTrim("couponID");
               couponPass = elm.getChildTextTrim("couponPass");
               shopID = elm.getChildTextTrim("shopID");
               posID = elm.getChildTextTrim("posID");
               cashierID = elm.getChildTextTrim("cashierID");
               mode = elm.getChildTextTrim("mode");
               minFlag=elm.getChildTextTrim("minFlag");
               saleValue=Double.parseDouble(elm.getChildTextTrim("saleValue"));
              // price=new BigDecimal(elm.getChildTextTrim("price"));
               validValue = Integer.parseInt(elm.getChildText("validValue"));
//               UpdateType = elm.getChildTextTrim("UpdateType");
           } catch (Exception e) {
               e.printStackTrace();
               throw new IllegalArgumentException();
           }
       }
//    public static String getCouponNOReprint(String file){
//        try{
//			Document doc = (new SAXBuilder()).build( new FileInputStream(file) );
//			Element root = doc.getRootElement();
//			Element sheet = root.getChild("sheet");
//			Element couponElement = sheet.getChild("coupon");
//            String couponNo=null;
//			if(couponElement==null)
//                return null;
//            else
//
//				couponNo = couponElement.getChildText("couponNO");
//
//              return couponNo;
//			}
//
//			 catch ( Exception e ){
//				e.printStackTrace();
//			}
//        return null;
//    }
    private String couponID;
    private String couponPass;
    private String shopID;
    private String posID;
    private String cashierID;
    private String mode;
    private BigDecimal price;
    private BigDecimal discountValue;
    private String minFlag;
    private double saleValue=0;
    // 卷激活后有效期 月数
    private int validValue = 0;
    
//    private String UpdateType;
//    
//    public String getUpdateType()
//    {
//    	return this.UpdateType;
//     }
//    
//    public void setUpdateType(String type)
//    {
//    	this.UpdateType=type;
//     }

    public double getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(double saleValue) {
        this.saleValue = saleValue;
    }
    
    public int getValidValue(){
    	return validValue;
    	}
    
    public void setValidValue(int validValue){
    	this.validValue = validValue;
    	}

    public String getMinFlag() {
        return minFlag;
    }

    public void setMinFlag(String minFlag) {
        this.minFlag = minFlag;
    }

    private String exceptionInfo;
}
