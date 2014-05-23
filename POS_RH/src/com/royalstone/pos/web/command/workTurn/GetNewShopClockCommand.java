package com.royalstone.pos.web.command.workTurn;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.royalstone.pos.data.ShopClock;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */
public class GetNewShopClockCommand implements ICommand {

	private String errorMsg1;
	private String errorMsg2;

	public Object[] excute(Object[] values) {
		if (values != null
			&& values.length == 2
			) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[3];
				result[0] = getNewShopClock(con, (ShopClock) values[1]);
				result[1] = errorMsg1;
				result[2] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private ShopClock getNewShopClock(
		Connection connection,
		ShopClock shopClock) {
		try {
			String sql = "select workdate,shiftid from shopclock";
			Statement state = connection.createStatement();
			ResultSet rs = state.executeQuery(sql);
			if (rs.next()) {
				Date workDate = rs.getDate("workdate");
				int shiftID = rs.getInt("shiftid");
				if (shopClock == null) {
					return new ShopClock(workDate, shiftID);
				}
				if (workDate.after(shopClock.getWorkDate())) {
					return new ShopClock(workDate, shiftID);
				} else {
					if (workDate.compareTo(shopClock.getWorkDate()) == 0
						&& shiftID > shopClock.getShiftID()) {
						return new ShopClock(workDate, shiftID);
					}
				}
			} else {
				errorMsg1 = "数据库数据不一致!";
				errorMsg2 = "数据库数据不一致!";
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "数据库操作错误!";
			errorMsg2 = e.getMessage();
		}
		return null;
	}

}
