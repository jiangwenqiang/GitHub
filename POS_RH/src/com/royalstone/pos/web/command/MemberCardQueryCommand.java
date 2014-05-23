package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，用来查询挂账卡的信息
 * @author liangxinbiao
 */
public class MemberCardQueryCommand implements ICommand {

	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {
		if (values.length == 2
			&& (values[1] instanceof String)) {
			Object[] results = new Object[1];
			results[0] = query((String) values[1]);
			return results;
		}

		return null;
	}

	/**
	 * 根据卡号和密码(子卡不需密码)查询挂账卡的信息
	 * @param CardNo 挂账卡号
	 * @return 挂账卡查询值对象
	 */
	private MemberCard query(String CardNo) {

		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		conn = DBConnection.getConnection("java:comp/env/dbcard");

		MemberCard cardquery = new MemberCard();
		String mode = null;
		if (CardNo == null
			|| CardNo.trim().equals("")) {
			cardquery.setExceptionInfo("卡号或密码为空");
			return cardquery;
		}
		try {
			conn.setAutoCommit(true);
			state = conn.createStatement();
			
				rs =
					state.executeQuery(
						"select CardNO,Mode,MemberID,memberlevel,Point,Level.Name,Level.Discount,Level.PromDiscount from guest,Level where guest.memberlevel=Level.LevelID and cardno = '"
							+ CardNo.trim()
							+ "' ");
				//rs.next();
				if (!rs.next()) {
					cardquery.setExceptionInfo("此卡号不存在,请按清除键继续");
					DBConnection.closeAll(rs, null, null);
					return cardquery;
				} else { //主卡
					mode = (rs.getString("Mode")).trim();
					if (!mode.equals("1")) {
					switch (mode.charAt(0)) {
						case '2' :
							cardquery.setExceptionInfo("未到帐卡");
							break;
						case 'r' :
							cardquery.setExceptionInfo("已回收卡");
							break;
						case 'm' :
							cardquery.setExceptionInfo("一般挂失卡");
							break;
						case 'l' :
							cardquery.setExceptionInfo("严重挂失卡");
							break;
						case 'f' :
							cardquery.setExceptionInfo("冻结");
							break;
						case 'e' :
							cardquery.setExceptionInfo("已换卡");
							break;
						case 'q' :
							cardquery.setExceptionInfo("退卡");
							break;
						default :
							cardquery.setExceptionInfo("其他错误");
							break;
							}
							DBConnection.closeAll(rs, state, conn);
							return cardquery;
						}

						cardquery.setCardNo(CardNo);
						cardquery.setMemberLevel(rs.getInt("memberlevel"));
                        cardquery.setLevelName(rs.getString("Name"));
                        cardquery.setDiscount(100-rs.getInt("Discount"));
                        cardquery.setPromDiscount(100-rs.getInt("PromDiscount"));
						cardquery.setTotalPoint(rs.getBigDecimal("Point"));
				
				}
			
		} catch (SQLException se) {
			se.printStackTrace();
			cardquery = new MemberCard();
			cardquery.setExceptionInfo("数据库操作有误,请按清除键继续");
			return cardquery;
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
		return cardquery;
	}


}
