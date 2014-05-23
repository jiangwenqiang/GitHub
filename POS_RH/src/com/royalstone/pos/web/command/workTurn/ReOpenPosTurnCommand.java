package com.royalstone.pos.web.command.workTurn;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.royalstone.pos.data.PosTurn;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */

public class ReOpenPosTurnCommand implements ICommand {

	private String errorMsg1;
	private String errorMsg2;

	public Object[] excute(Object[] values) {
		if (values != null && values.length == 2) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[2];
				resetStat(con, (PosTurn) values[1]);
				result[0] = errorMsg1;
				result[1] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private void resetStat(Connection connection, PosTurn turn) {
		try {
			String sql =
				"update pos_turn set stat=0 where pos_id=? and workdate=? and shiftid=?";
			PreparedStatement state = connection.prepareStatement(sql);
			state.setString(1, turn.getPosid());
			state.setDate(2, new Date(turn.getWorkdate().getTime()));
			state.setInt(3, turn.getShiftID());
			state.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "Êý¾Ý¿â²Ù×÷´íÎó!";
			errorMsg2 = e.getMessage();
		}
	}

}
