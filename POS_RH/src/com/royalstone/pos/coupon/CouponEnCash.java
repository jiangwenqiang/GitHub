package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fire
 * Date: 2005-6-23
 */
public class CouponEnCash implements Serializable {
    private String couponID;

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


    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public List getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List goodsList) {
        this.goodsList = goodsList;
    }

    private String couponPass;
    private String shopID;
    private String posID;
    private String cashierID;
    private String mode;
    private String exceptionInfo;
    private List goodsList;
    private List goodsQty;
    private String minFlag;
    private String type;
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinFlag() {
        return minFlag;
    }

    public void setMinFlag(String minFlag) {
        this.minFlag = minFlag;
    }

    public List getGoodsQty() {
        return goodsQty;
    }

    public void setGoodsQty(List goodsQty) {
        this.goodsQty = goodsQty;
    }
}
