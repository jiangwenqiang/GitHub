/*
 * 创建日期 2008-3-16
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.royalstone.pos.card.CardChange;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，调用相应的Ejb完成卡兑换
 * @deprecated
 * @author zhouzhou
 */
public class MemberCardChangeUpdateServletCommand implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 2) {
			try {
				CardChange cardChange = (CardChange) values[1];

				CardChange result = null;
				
				Object[] results = new Object[1];

				result = pay(cardChange);

				results[0] = result;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

    private CardChange pay(CardChange cardChange){
    	CardChange result = null;
	    Connection conn = null;
        Statement state = null;
        ResultSet rs = null;
        
        conn = DBConnection.getConnection("java:comp/env/dbcard");
		String sql =
			" declare @retvalue int  declare @cardpoint int "
				+ "exec TL_ProfitToPoint ?,?,?,?,?,?,?,@retvalue output, @cardpoint output  "
				+ "select @retvalue as retvalue, @cardpoint as cardpoint; ";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(cardChange.getFlag()));
			stmt.setString(2,cardChange.getCardno());
			stmt.setString(3, cardChange.getPlan());
			stmt.setString(4,cardChange.getPosid());
			stmt.setString(5, cardChange.getShopid());
			stmt.setString(6,cardChange.getListno());
			stmt.setString(7, cardChange.getPayvalue());
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = cardChange;
				String retvalue = rs.getString("retvalue");
				result.setInfoFlag(retvalue);
				String cardpoint = rs.getString("cardpoint");
				result.setCardPoint(cardpoint);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
		return result;
    }

}
