package com.royalstone.pos.coupon;

import java.io.IOException;
/**
 * Created by IntelliJ IDEA.
 * User: fire
 * Date: 2005-6-23
 */
public interface CouponMgr {
    public String sale(CouponSale coupon) throws IOException,CouponException;
    public CouponSale getCouponSale(String couponID,String secrety,String Storeid) throws IOException,CouponException;
    public CouponEnCash query(String couponID,String secrety) throws IOException,CouponException;
    public String autoRever(CouponSaleList couponSales) throws IOException,CouponException;
    public String saleAndEncashCoupons(CouponSaleList couponSales) throws IOException,CouponException;
    public CouponLargess getCouponInfo(String coupon, String count,String sheepID, String largessType) throws IOException,CouponException;
    public void copyCouponInfo(String salesID) throws IOException;
}
