package com.royalstone.pos.web.command;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.royalstone.pos.coupon.CouponSale;
import com.royalstone.pos.coupon.CouponSaleList;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，调用相应的Ejb完成储值卡的支付
 * @deprecated
 * @author liangxinbiao
 */
public class CouponSaleServletCommand implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 2
			&& (values[1] instanceof CouponSaleList)) {
			try {
				CouponSaleList couponSales = (CouponSaleList) values[1];

				String result = null;

				result = pay(couponSales);

				Object[] results = new Object[1];

				results[0] = result;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}
  /**
   * 同时处理多张券的销售 以及兑现
   * @param cardpays
   * @return
   */
    private String pay(CouponSaleList cardpays){
       String result = null;
	   Connection conn = null;
        Statement state = null;
        ResultSet rs = null;


		conn = DBConnection.getConnection("java:comp/env/dbcard");
      if(cardpays==null||cardpays.size()<0){
          result="券号为空";
          return result;
      }
      String  couponUpdateType=cardpays.getUpdateType();
      
		try {
         conn.setAutoCommit(false);
      for(int i=0;i<cardpays.size();i++){
//      	  String UpdateType=couponUpdateType;
          CouponSale cardpay=   cardpays.get(i);
        if (cardpay.getCouponID() == null
			|| cardpay.getCouponPass() == null
			|| cardpay.getCouponID().trim().equals("")
			|| cardpay.getCouponPass().trim().equals("")) {
			result = "券号或密码为空";
			return result;
		}
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        String dateTime = date.format(new Date());
 //       if(cardpay.getMode().equals("p"))
 //       	UpdateType="f";
        
			state = conn.createStatement();

			  rs =
				state.executeQuery(
					"select CouponID,vrowid,CPwd,Mode from Coupon "
						+ "where CouponID = '"
						+ cardpay.getCouponID().trim()
						+ "'");
		if (rs.next()) {

             if(isPass(cardpay.getCouponID().trim(),rs.getString("vrowid").trim(),rs.getString("CPwd"),cardpay.getCouponPass().trim())){

				String mode = (rs.getString("mode")).trim();
				if (!mode.equals("1")&&!mode.equals("4")) {

					switch (mode.charAt(0)) {
						case '2' :
							result = "未到帐不能销售或兑换";
							break;
                        case '3' :
							result = "不能在pos机销售或兑换";
							break;
						case 'f' :
							result = "已兑换,不能重复销售或兑换";
							break;
                        case 'd' :
							result = "已作废,不能销售或兑换";
							break;
						default :
							result = "其他错误,不能销售或兑换";
							break;
					}

					DBConnection.closeAll(rs, state, conn);
				}

				try {

					DBConnection.closeAll(rs, state, null);
					state = conn.createStatement();
					String sql = null;

                    if(cardpay.getDiscountValue()==null)
                       sql =
						"update Coupon set "
							+ "Mode='"
//							+ UpdateType
							+ couponUpdateType
							+ "',MInFlag='"+cardpay.getMinFlag()
                            + "',ConsumeDate=getdate() "
                            + ", UseShopID='"+cardpay.getShopID().trim()
                            + "',POSID='"+cardpay.getPosID().trim()
                            + "',CashierID='"+cardpay.getCashierID()
                            + "',salevalue='"+cardpay.getSaleValue()
							+ "',EndDate= DATEADD(Month," + cardpay.getValidValue() + ",'" + dateTime + "')"
                            + " where CouponID='"
							+ cardpay.getCouponID().trim()
							+ "'";
                    else
                      sql =
						"update Coupon set "
							+ "Mode='"
//							+ UpdateType
							+ couponUpdateType
							+ "',MInFlag='"+cardpay.getMinFlag()
                            + "',ConsumeDate=getdate() "
                            + ",UseShopID='"+cardpay.getShopID().trim()
                            + "',POSID='"+cardpay.getPosID().trim()
                            + "',CashierID='"+cardpay.getCashierID()
                            + "',Discountvalue=CpnValue-'"+cardpay.getDiscountValue().toString()
                            + "',salevalue='"+cardpay.getSaleValue()
							+ "',EndDate= DATEADD(Month," + cardpay.getValidValue() + ",'" + dateTime + "')"
                            + " where CouponID='"
							+ cardpay.getCouponID().trim()
							+ "'";

					state.executeUpdate(sql);

				} catch (SQLException ex) {
					ex.printStackTrace();
					DBConnection.closeAll(rs, state, conn);
					return "出现错误,事务回滚";
				}
			} else {
				result = "密码有误,此券可能是假券";
				DBConnection.closeAll(rs, state, conn);
				return result;
			}
             }else{
         	    result = "无此券号，可能是假券";
				DBConnection.closeAll(rs, null, null);
				return result;
        }
            }
            conn.commit();
           result = "1";
		} catch (SQLException se) {
           try {
				if (!conn.getAutoCommit())
					conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
                return "数据库回滚错误，需要紧急处理";
			}
            se.printStackTrace();
            return "出现错误,事务回滚";

		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
		return result;
    }
      private boolean isPass(String cardNO,String vrowID,String dbPass,String password){

        String id= vrowID +cardNO;
        String s="";
        long k = 123456789;

		for( int i=0; i < id.length(); i++){
			long	a = ( (int)id.charAt(i) ) % 13 + 1;
			k = ( k * a ) % 9999999 + 1;
		}

		k = k % 98989898 + 99;
		for(int i=0; i<  password.length(); i++){
			long	a = ( (int)password.charAt(i) ) % 17 + 1;
			k = ( k % 9876543 + 1 ) * a;
		}

		DecimalFormat df = new DecimalFormat( "00000000" );
		s=df.format( k % 100000000 );

        return s.equals(dbPass.trim());
    }

}
