/*
 * 创建日期 2010-5-26
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.royalstone.pos.web.command;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.royalstone.pos.card.SHCardPayVO;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，调用相应的Ejb完成储值卡的支付
 * @deprecated
 * @author liangxinbiao
 */
public class SHCardPayRaServletCommand implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 2
			&& (values[1] instanceof SHCardPayVO)) {
			try {
				SHCardPayVO payVO = (SHCardPayVO) values[1];

				String result = null;

				result = pay(payVO);

				Object[] results = new Object[1];

				results[0] = result;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

    private String pay(SHCardPayVO cardpay){
       String result = null;
	   Connection conn = null;
        Statement state = null;
        ResultSet rs = null;


		conn = DBConnection.getConnection("java:comp/env/dbcard");

		if (cardpay.getCardno() == null
			|| cardpay.getPassword() == null
			|| cardpay.getCardno().trim().equals("")
			|| cardpay.getPassword().trim().equals("")) {
			result = "卡号或密码为空";
			return result;
		}
		try {
			state = conn.createStatement();
			  rs =
				state.executeQuery(
					"select detail,mode,secrety,ifnewcard,memberid from guest "
						+ "where cardno = '"
						+ cardpay.getCardno().trim()
						+ "'");
		if (rs.next()) {
//             if(isPass(cardpay.getCardno().trim(),rs.getString("secrety"),rs.getInt("ifnewcard"),cardpay.getPassword().trim())){
				java.math.BigDecimal rsDetail = rs.getBigDecimal("detail");
				rsDetail.setScale(5, BigDecimal.ROUND_HALF_UP);
				java.math.BigDecimal payvalue =
					new java.math.BigDecimal(cardpay.getPayvalue());
				payvalue.setScale(5, BigDecimal.ROUND_HALF_UP);

				String mode = (rs.getString("mode")).trim();
				if (!mode.equals("1")) {
					switch (mode.charAt(0)) {
						case '2' :
							result = "未到帐卡";
							break;
						case 'r' :
							result = "已回收卡";
							break;
						case 'm' :
							result = "一般挂失卡";
							break;
						case 'l' :
							result = "严重挂失卡";
							break;
						case 'f' :
							result = "冻结";
							break;
						case 'e' :
							result = "已换卡";
							break;
						case 'q' :
							result = "退卡";
							break;
						default :
							result = "其他错误";
							break;
					}
					DBConnection.closeAll(rs, state, conn);
					return "该卡" + result;
				}
				try {
					conn.setAutoCommit(false);
					DBConnection.closeAll(rs, state, null);
					state = conn.createStatement();
					String sql = null;
					rsDetail = rsDetail.subtract(payvalue);
					sql =
						"update guest set paymoney=paymoney+"
							+ cardpay.getPayvalue()
							+ ",detail=detail+"
							+ Double.parseDouble(cardpay.getPayvalue())
							+ ",times=times+1,lastusedate=getdate(),"
							+ "lastshopid='"
							+ cardpay.getShopid()
							+ "',lastposid='"
							+ cardpay.getPosid()
							+ "', LastCashierID='"
							+ cardpay.getCashierid()
							+ "' where cardNo='"
							+ cardpay.getCardno()
							+ "'";
					state.executeUpdate(sql);
					sql =
						"exec InsertCardAcc '"
							+ cardpay.getCardno()
							+ "','"
							+ cardpay.getShopid()
							+ "',-1,"
							+ cardpay.getPayvalue()
							+ ",0,'',557101,'储值消费充值'";
					state.executeUpdate(sql);
					conn.commit();
					result = "1";
					conn.setAutoCommit(true);
				} catch (SQLException ex) {
					ex.printStackTrace();
					conn.rollback();
					DBConnection.closeAll(rs, state, conn);
					return "出现错误,事务回滚";
				}

             }else{
         	    result = "无此卡号";
				DBConnection.closeAll(rs, null, null);
				return result;
        }
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
		return result;
    }
      private boolean isPass(String cardNO,String dbPass,int isNew,String password){
       if(isNew!=1){
         if(password.equals("0"))
            password="";
          return password.equals(dbPass.trim());
       }
       long k;
       int a;
       String s;
       k=123456789;
        for(int i=0;i<cardNO.length();i++){
            a=(int)cardNO.charAt(i)%13+1;
            k=(k * a)% 9999999 + 1;
        }
        k=k%98989898+99;

        for(int i=0;i<dbPass.length();i++){
           a=(int)dbPass.charAt(i)%17+1;
           k=(k%9876543+1)*a;
        }
       s=Long.toString(k+100000000);
       s=s.substring(s.length()-8,(s.length()-8)+6);
       s= s + verify(s);

        return s.equals(password);
    }
   private char verify(String s){
       int odd=0,env=0;
       int I;
        I=0;
       for(;I<s.length();){
        odd = odd +(int)s.charAt(I) - (int)'0';
        I = I + 2;
       }
        I=1;
       for(;I<s.length();){
           env = env + (int)s.charAt(I) -(int) '0';
        I = I + 2;
       }
       I = (env + odd * 3)%10;
       return (char)(I + (int)'0');
   }
}

