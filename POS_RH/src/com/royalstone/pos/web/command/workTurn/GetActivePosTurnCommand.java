package com.royalstone.pos.web.command.workTurn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.royalstone.pos.data.PosTurn;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */

public class GetActivePosTurnCommand implements ICommand {

	private String errorMsg1;
	private String errorMsg2;

	public Object[] excute(Object[] values) {
		if (values != null && values.length == 2) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[3];
				result[0] = getActivePosTurn(con, (String) values[1]);
				result[1] = errorMsg1;
				result[2] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private PosTurn getActivePosTurn(Connection connection, String posid) {
		PosTurn result = null;
		try {
			String sql =
				"select * from pos_turn where stat=0 and pos_id=? order by workdate desc,shiftid desc ";
			PreparedStatement pstate = connection.prepareStatement(sql);
			pstate.setString(1, posid);

			ResultSet rs = pstate.executeQuery();

			if (rs.next()) {

				result = new PosTurn();
				result.setPosid(rs.getString("pos_id"));
				result.setWorkdate(rs.getDate("workdate"));
				result.setShiftID(rs.getInt("shiftid"));
				result.setStartTime(rs.getDate("start_time"));
				result.setEndTime(rs.getDate("end_time"));
				result.setStat(rs.getInt("stat"));

				if (rs.getInt("IsStartOffLine") == 1) {
					result.setStartOffLine(true);
				} else {
					result.setStartOffLine(false);
				}

				if (rs.getInt("IsEndOffLine") == 1) {
					result.setEndOffLine(true);
				} else {
					result.setEndOffLine(false);
				}

			}

		} catch (SQLException e) {

			e.printStackTrace();
			errorMsg1 = "Êý¾Ý¿â²Ù×÷´íÎó!";
			errorMsg2 = e.getMessage();

		}

		return result;
	}
}