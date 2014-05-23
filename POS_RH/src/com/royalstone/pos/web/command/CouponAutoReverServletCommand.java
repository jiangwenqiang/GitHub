package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.royalstone.pos.coupon.CouponSale;
import com.royalstone.pos.coupon.CouponSaleList;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，调用相应的Ejb完成储值卡的支付
 * @deprecated
 * @author liangxinbiao
 */
public class CouponAutoReverServletCommand implements ICommand {
    private static int BIGDECIMAL_SCALE = 5;
	public Object[] excute(Object[] values) {

		if (values.length == 2
			&& (values[1] instanceof CouponSaleList)) {
			try {
				CouponSaleList couponSales = (CouponSaleList) values[1];

				String result = null;

				result = autoRever(couponSales);

				Object[] results = new Object[1];

				results[0] = result;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

    private String autoRever(CouponSaleList couponSales){
       String result = null;
	   Connection conn = null;
        Statement state = null;
        ResultSet rs = null;


		conn = DBConnection.getConnection("java:comp/env/dbcard");

//		String couponID = couponSales.getCouponID();
//		String couponPass = couponSales.getCouponPass();
//		String shopID = couponSale.getShopID();
//		String posID = couponSale.getPosID();
//		String cashierID = couponSale.getCashierID();
//		String mode = couponSale.getMode();
//
//		String sql = "";
//		String sub_sql ="select count(*) from Coupon where CouponID='"+couponID+"'";
//
//		int updateCount = 0;

		try {
            //------------------
            conn.setAutoCommit(false);
      for(int i=0;i<couponSales.size();i++){
      	
         CouponSale couponSale=   couponSales.get(i);
     		String couponID = couponSale.getCouponID();
		   String couponPass = couponSale.getCouponPass();
		   String shopID = couponSale.getShopID();
		   String posID = couponSale.getPosID();
		   String cashierID = couponSale.getCashierID();
		   String mode = couponSale.getMode();
		   
		 if(mode.equals("p"))
		 	break;

		String sql = "";
		String sub_sql ="select count(*) from Coupon where CouponID='"+couponID+"'";

		int updateCount = 0;


            //------------------
			state = conn.createStatement();
			rs =
				state.executeQuery(sub_sql);
			rs.next();

			if (rs.getInt(1) == 0) {
				return "1";
			}

			if (rs != null)
				rs.close();


//			sql = "update Coupon set UseShopID='"+shopID+"',POSID='"+posID+"',CashierID='"+cashierID+"',Mode='"+mode+"'" +
//                    "  where  CouponID='" +couponID+"'";
       		sql = "update Coupon set Mode='"+mode+"'" +
                    "  where  CouponID='" +couponID+"'";
			updateCount = state.executeUpdate(sql);

             System.out.println("券号:"+couponID+"发生冲正,冲正状态为:"+mode+" --时间:"+new Date().toString());
             System.out.println("店号："+shopID+"--POS机:"+posID+"--收银员:"+cashierID);
              }
             conn.commit();
            return "1";
		} catch (Exception ex) {
			try {
				if (!conn.getAutoCommit())
					conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
                return "数据库回滚错误，需要紧急处理";
			}

			return "饼券交易冲正错误! ";
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
    }

	/**
	 * 清除字符串的前后空格
	 * @param str
	 * @return
	 */
	private String mytrim(String str) {
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}
	}
}
