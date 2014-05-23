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
public class SHCardPayServletCommand implements ICommand {

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
         double rsDetail=0.00;
         double pRKDetail=0.00;
         double RKDetail=0.00;
         double AddDetail=0.00;
         double payvalue=0.00;
         int payflag=0;

 		conn = DBConnection.getConnection("java:comp/env/dbcard");

 		if (cardpay.getCardno() == null
 			|| cardpay.getPassword() == null
 			|| cardpay.getCardno().trim().equals("")
 			|| cardpay.getPassword().trim().equals("")) {
 			result = "卡号或密码为空";
 			return result;
 		}
 		
 		payflag=Integer.valueOf(cardpay.getpayFlag()).intValue();
 		
 		try {
 			state = conn.createStatement();
 		if(payflag==0){
 			  rs =
 				state.executeQuery(
 					"select detail,mode,secrety,ifnewcard,memberid from guest "
 						+ "where cardno = '"
 						+ cardpay.getCardno().trim()
 						+ "'");
 		if(rs.next())
 		 {
 			rsDetail = rs.getBigDecimal("detail").doubleValue();
 			DBConnection.closeAll(rs, state, null);
 		 }   
 		}else{
            rs =
				state.executeQuery(
					"select Detail from GuestExt "
						+ "where AccFlag=0 and cardno = '"
						+ cardpay.getCardno().trim()
						+ "'");    
        if(rs.next())
         {
        	rsDetail = rs.getBigDecimal("detail").doubleValue();
        	DBConnection.closeAll(rs, state, null);
         }
 		}
        payvalue = new java.math.BigDecimal(cardpay.getPayvalue()).doubleValue();

	    if (rsDetail < payvalue) {
					result = "支付额不够";
					DBConnection.closeAll(rs, state, conn);
					return result;
			  }
	    else{
 				try {
 					conn.setAutoCommit(false);
 				    if(payflag==0){
 					DBConnection.closeAll(rs, state, null);
 					state = conn.createStatement();
 					String sql = null;
 					sql =
 						"insert into guestpurch0(cardno,paymoney,detail,shopid,reqtime,cdseq,branchno,cashierno,stat,point) values('"
 							+ cardpay.getCardno()
 							+ "',"
 							+ String.valueOf(payvalue).trim()
 							+ ","
 							+ String.valueOf(rsDetail).trim()
 							+ ",'"
 							+ cardpay.getShopid()
 							+ "','"
 							+ cardpay.getTime()
 							+ "','"
 							+ cardpay.getCdseq()
 							+ "','"
 							+ cardpay.getPosid()
 							+ "','"
 							+ cardpay.getCashierid()
 							+ "','0',0)";
 					state.executeUpdate(sql);
 					sql =
 						"update guest set paymoney=paymoney+"
 							+ String.valueOf(payvalue).trim()
 							+ ",MPayMoney=MPayMoney+"
 							+ cardpay.getPayvalue().trim()
 							+ ",detail=detail+"
 							+ Double.parseDouble(cardpay.getPayvalue())*-1
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
 							+ ",0,'',559001,'储值消费余额减少'";
 					state.executeUpdate(sql);
 				   }
 				   else
 				   {
 					DBConnection.closeAll(rs, state, null);
 					state = conn.createStatement();
 					String sql = null;
 					sql =
 						"update GuestExt set PayMoney=paymoney+"
 							+ String.valueOf(payvalue).trim()
 							+ ",detail=detail+"
 							+ payvalue*-1
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
 						"exec InsertCardExtAcc "
 						    + 0 
							+ ",'"
 							+ cardpay.getCardno()
 							+ "','"
 							+ cardpay.getShopid()
 							+ "',-1,"
							+ String.valueOf(payvalue).trim()
 							+ ",0,994104,'荣华币消费余额减少'";
 					state.executeUpdate(sql);
 					
 				   //如果储值卡金额无,需要刷新支付
 					sql =
 						"update guest set "
 							+ "MPayMoney=MPayMoney+"
 							+  cardpay.getPayvalue().trim()
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
 			   	}
 					conn.commit();
 					result = "1";
 					conn.setAutoCommit(true);
 				} catch (SQLException ex) {
 					ex.printStackTrace();
 					conn.rollback();
 					DBConnection.closeAll(rs, state, conn);
 		//			return "出现错误,事务回滚";
 					return ex.getMessage();
 				}
	      }
 		} catch (SQLException se) {
 			se.printStackTrace();
 		} finally {
 			DBConnection.closeAll(rs, state, conn);
 		}
 		return result;
     }
/*
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
				java.math.BigDecimal rsDetail = rs.getBigDecimal("detail");
				rsDetail.setScale(5, BigDecimal.ROUND_HALF_UP);
				java.math.BigDecimal payvalue =
					new java.math.BigDecimal(cardpay.getPayvalue());
				payvalue.setScale(5, BigDecimal.ROUND_HALF_UP);
				if (rsDetail.compareTo(payvalue) < 0) {
					result = "支付额不够";
					DBConnection.closeAll(rs, state, conn);
					return result;
				}
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
						"insert into guestpurch0(cardno,paymoney,detail,shopid,reqtime,cdseq,branchno,cashierno,stat,point) values('"
							+ cardpay.getCardno()
							+ "',"
							+ cardpay.getPayvalue()
							+ ","
							+ rsDetail.toString().trim()
							+ ",'"
							+ cardpay.getShopid()
							+ "','"
							+ cardpay.getTime()
							+ "','"
							+ cardpay.getCdseq()
							+ "','"
							+ cardpay.getPosid()
							+ "','"
							+ cardpay.getCashierid()
							+ "','0',0)";
					state.executeUpdate(sql);
					sql =
						"update guest set paymoney=paymoney+"
							+ cardpay.getPayvalue()
							+ ",MPayMoney=MPayMoney+"
							+ cardpay.getPayvalue()
							+ ",detail=detail+"
							+ Double.parseDouble(cardpay.getPayvalue())*-1
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
							+ ",0,'',559001,'储值消费余额减少'";
					state.executeUpdate(sql);
					conn.commit();
					result = "1";
                    //测试冲正功能设为null
                    //result =null;
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
    }*/
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
