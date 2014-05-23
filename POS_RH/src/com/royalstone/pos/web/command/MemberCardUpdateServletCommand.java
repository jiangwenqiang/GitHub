package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.royalstone.pos.card.MemberCardUpdate;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，调用相应的Ejb完成储值卡的支付
 * @deprecated
 * @author liangxinbiao
 */
public class MemberCardUpdateServletCommand implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 2
			&& (values[1] instanceof MemberCardUpdate)) {
			try {
				MemberCardUpdate updateVO = (MemberCardUpdate) values[1];

				String result = null;
				
				Object[] results = new Object[1];

				result = pay(updateVO);

//				Object[] results = new Object[1];

				results[0] = result;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

    private String pay(MemberCardUpdate cardUpdate){
       String result = null;
	   Connection conn = null;
        Statement state = null;
        ResultSet rs = null;


		conn = DBConnection.getConnection("java:comp/env/dbcard");

		if (cardUpdate.getCardno() == null
			|| cardUpdate.getCardno().trim().equals("")) {
			result = "卡号或密码为空";
			return result;
		}
		try {
			state = conn.createStatement();
			rs =
				state.executeQuery(
					"select mode,detail from guest "
						+ "where cardno = '"
						+ cardUpdate.getCardno().trim()
						+ "'");
			if (rs.next()) {

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
					sql =
						"insert into guestpurch0(cardno,paymoney,detail,shopid,reqtime,cdseq,branchno,Listno,cashierno,stat,point) values('"
							+ cardUpdate.getCardno()
							+ "',"
							+ cardUpdate.getPayvalue()
							+ ",'0','"
							+ cardUpdate.getShopid()
							+ "','"
							+ cardUpdate.getTime()
							+ "','"
							+ cardUpdate.getCdseq()
							+ "','"
							+ cardUpdate.getPosid()
							+ "','"
							+ cardUpdate.getListNo()		// 增加流水号
							+ "','"
							+ cardUpdate.getCashierid()
							+ "','0',"
                            +cardUpdate.getCourrentPoint()+")";
					state.executeUpdate(sql);
					sql =
						"update guest set paymoney=paymoney+"
							+ cardUpdate.getPayvalue()
							+ ",times=times+1,lastusedate=getdate(),"
							+ "lastshopid='"
							+ cardUpdate.getShopid()
							+ "',lastposid='"
							+ cardUpdate.getPosid()
							+ "', LastCashierID='"
							+ cardUpdate.getCashierid()
                            + "',Point='"
							+ cardUpdate.getPoint()
							+ "' where cardNo='"
							+ cardUpdate.getCardno()
							+ "'";
					state.executeUpdate(sql);
					sql =
						"exec InsertCardAcc '"
							+ cardUpdate.getCardno()
							+ "','"
							+ cardUpdate.getShopid()
							+ "',-1,"
							+ cardUpdate.getPayvalue()
							+ ","+cardUpdate.getPoint()+",'',559001,'会员卡消费'";
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
			} else {
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

}
