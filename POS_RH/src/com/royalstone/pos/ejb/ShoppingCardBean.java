package com.royalstone.pos.ejb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import com.royalstone.pos.card.SHCardPayVO;
import com.royalstone.pos.card.SHCardQueryVO;
import com.royalstone.pos.ejb.util.DBConnection;

/**
 * 储值卡的EJB的Bean实现
 * @deprecated 
 * @author liangxinbiao
 */
public class ShoppingCardBean implements SessionBean {
	SessionContext sessionContext;

	private static int BIGDECIMAL_SCALE = 5;

	private Connection conn = null;
	private Statement state = null;
	private ResultSet rs = null;
	private SHCardQueryVO cardquery = null;

	private String mytrim(String str) {
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}
	}

	public void ejbCreate() throws CreateException {
		try {
			conn = DBConnection.getConnection("jdbc/SHCard");
		} catch (NamingException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void ejbRemove() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	public SHCardQueryVO query(String CardNo, String secrety) {
		try {
			if (conn.isClosed()) {
				conn = DBConnection.getConnection("jdbc/SHCard");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		cardquery = new SHCardQueryVO();
		String mode = null;
		if (CardNo == null
			|| secrety == null
			|| CardNo.trim().equals("")
			|| secrety.trim().equals("")) {
			cardquery.setExceptioninfo("卡号或密码为空");
			return cardquery;
		}
		try {
			conn.setAutoCommit(true);
			state = conn.createStatement();
			rs =
				state.executeQuery(
					"select count(*) as counts from guest where cardno = '"
						+ CardNo.trim()
						+ "'");
			rs.next();
			if (rs.getInt("counts") == 0) {
				cardquery.setExceptioninfo("无此卡号");
				DBConnection.closeAll(rs, null, null);
				return cardquery;
			}
			rs =
				state.executeQuery(
					"select detail,mode,secrety,ifnewcard,memberid from guest "
						+ "where cardno = '"
						+ CardNo.trim()
						+ "' and secrety = '"
						+ secrety.trim()
						+ "'");
			if (rs.next()) {
				mode = (rs.getString("mode")).trim();
				if (!mode.equals("1")) {
					switch (mode.charAt(0)) {
						case '2' :
							cardquery.setExceptioninfo("未到帐卡");
							break;
						case 'r' :
							cardquery.setExceptioninfo("已回收卡");
							break;
						case 'm' :
							cardquery.setExceptioninfo("一般挂失卡");
							break;
						case 'l' :
							cardquery.setExceptioninfo("严重挂失卡");
							break;
						case 'f' :
							cardquery.setExceptioninfo("冻结");
							break;
						case 'e' :
							cardquery.setExceptioninfo("已换卡");
							break;
						case 'q' :
							cardquery.setExceptioninfo("退卡");
							break;
						default :
							cardquery.setExceptioninfo("其他错误");
							break;
					}
					DBConnection.closeAll(rs, state, conn);
					return cardquery;
				}
				cardquery.setDetail(rs.getString("detail"));
				cardquery.setIfnewcard(rs.getString("ifnewcard"));
				cardquery.setMemberid(rs.getString("memberid"));
				DBConnection.closeAll(rs, state, conn);
			} else {
				cardquery.setExceptioninfo("密码有误");
				DBConnection.closeAll(rs, state, conn);
				return cardquery;
			}
		} catch (SQLException se) {
			cardquery = new SHCardQueryVO();
			cardquery.setExceptioninfo("数据库操作有误");

			return cardquery;
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
		return cardquery;
	}

	public String pay(SHCardPayVO cardpay) {
		String result = null;
		try {
			if (conn.isClosed()) {
				conn = DBConnection.getConnection("jdbc/SHCard");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
					"select count(*) as counts from guest where cardno = '"
						+ cardpay.getCardno().trim()
						+ "'");
			rs.next();
			if (rs.getInt("counts") == 0) {
				result = "无此卡号";
				DBConnection.closeAll(rs, null, null);
				return result;
			}
			DBConnection.closeAll(rs, null, null);
			rs =
				state.executeQuery(
					"select mode,detail from guest "
						+ "where cardno = '"
						+ cardpay.getCardno().trim()
						+ "' and secrety = '"
						+ cardpay.getPassword().trim()
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
							+ ",detail=detail-"
							+ cardpay.getPayvalue()
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
					conn.setAutoCommit(true);
				} catch (SQLException ex) {
					ex.printStackTrace();
					conn.rollback();
					DBConnection.closeAll(rs, state, conn);
					return "出现错误,事务回滚";
				}
			} else {
				result = "密码有误";
				DBConnection.closeAll(rs, state, conn);
				return result;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
		return result;
	}

	public String autoRever(SHCardPayVO cardpay) {

		try {
			if (conn.isClosed()) {
				conn = DBConnection.getConnection("jdbc/SHCard");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String cardno = cardpay.getCardno();
		String shopID = cardpay.getShopid();
		String cashierID = cardpay.getCashierid();
		String posID = cardpay.getPosid();
		String time = cardpay.getTime();
		String pay_value = cardpay.getPayvalue();
		BigDecimal _pay_value = new BigDecimal(pay_value);
		_pay_value.setScale(BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
		String passwd = cardpay.getPassword();
		String cdseq = cardpay.getCdseq();

		String sql = "";
		String sub_sql =
			"CardNo='"
				+ cardno
				+ "' and ShopID='"
				+ shopID
				+ "' and BranchNo='"
				+ posID
				+ "' and "
				+ "CashierNo='"
				+ cashierID
				+ "' and ReqTime='"
				+ time
				+ "' and CDSeq="
				+ cdseq
				+ " and "
				+ "Convert(char(8),PurchDateTime,112)=Convert(char(8),getdate(),112) and stat=0";
		int updateCount = 0;

		try {
			state = conn.createStatement();
			rs =
				state.executeQuery(
					"select count(*) from GuestPurch0 where " + sub_sql);
			rs.next();

			if (rs.getInt(1) == 0) {
				return "1";
			}

			if (rs != null)
				rs.close();
			rs =
				state.executeQuery(
					"select Detail,PayMoney from GuestPurch0 where " + sub_sql);
			rs.next();

			String detail = mytrim(rs.getString(1));
			BigDecimal _detail = rs.getBigDecimal(1);
			String paymoney = mytrim(rs.getString(2));
			BigDecimal _paymoney = rs.getBigDecimal(2);

			sql =
				"update Guest set Detail=Detail+"
					+ paymoney
					+ ",PayMoney=PayMoney-"
					+ paymoney
					+ ",times=times-1 where CardNo='"
					+ cardno
					+ "'";

			conn.setAutoCommit(false);

			updateCount = state.executeUpdate(sql);

			sql = "update GuestPurch0 set Stat='1' where " + sub_sql;

			updateCount = state.executeUpdate(sql);

			sql =
				"insert into GuestPurch0(CardNo,PayMoney,Detail,ShopID,ReqTime,CDSeq,BranchNo,CashierNo,Stat,Point) values('"
					+ cardno
					+ "',"
					+ paymoney
					+ ","
					+ detail
					+ ",'"
					+ shopID
					+ "','"
					+ time
					+ "','"
					+ cdseq
					+ "','"
					+ posID
					+ "','"
					+ cashierID
					+ "','3',0)";

			updateCount = state.executeUpdate(sql);

			conn.commit();

			return "1";

		} catch (Exception ex) {
			try {
				if (!conn.getAutoCommit())
					conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "储值卡交易冲正错误! ";
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
	}
}
