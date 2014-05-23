package com.royalstone.pos.web.command.workTurn;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.royalstone.pos.data.PosTurn;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */
public class UploadNewPosTurnCommand implements ICommand {

	private String errorMsg1;
	private String errorMsg2;

	public Object[] excute(Object[] values) {
		if (values != null && values.length == 2) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[2];
				saveTurn(con, (PosTurn) values[1]);
				result[0] = errorMsg1;
				result[1] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private void saveTurn(Connection connection, PosTurn turn) {
		try {
			String checkSql =
				"select stat from pos_turn where pos_id=? and workdate=? and shiftid=?";
			PreparedStatement checkStmt = connection.prepareStatement(checkSql);
			checkStmt.setString(1, turn.getPosid());
			checkStmt.setDate(2, new Date(turn.getWorkdate().getTime()));
			checkStmt.setInt(3, turn.getShiftID());
			ResultSet rs = checkStmt.executeQuery();
			if (!rs.next()) {
				String insertSql =
					"insert into pos_turn (pos_id,workdate,shiftid,start_time,end_time,"
						+ "stat,isStartOffLine,isEndOffLine) values "
						+ "(?,?,?,?,?,?,?,?)";

				PreparedStatement insertStmt =
					connection.prepareStatement(insertSql);

				insertStmt.setString(1, turn.getPosid());
				insertStmt.setDate(2, new Date(turn.getWorkdate().getTime()));
				insertStmt.setInt(3, turn.getShiftID());
				insertStmt.setTimestamp(
					4,
					new Timestamp(turn.getStartTime().getTime()));
				if (turn.getEndTime() != null) {
					insertStmt.setTimestamp(
						5,
						new Timestamp(turn.getEndTime().getTime()));
				} else {
					insertStmt.setTimestamp(5, null);
				}
				insertStmt.setInt(6, turn.getStat());
				if (turn.isStartOffLine()) {
					insertStmt.setInt(7, 1);
				} else {
					insertStmt.setInt(7, 0);
				}
				if (turn.isEndOffLine()) {
					insertStmt.setInt(8, 1);
				} else {
					insertStmt.setInt(8, 0);
				}
				insertStmt.execute();
			}else{

				String updateSql =
					"update pos_turn set stat=? where pos_id=? and workdate=? and shiftid=?";
				PreparedStatement updateStmt =
					connection.prepareStatement(updateSql);

				int stat = rs.getInt("stat");
				if (stat != 0) {
					updateStmt.setInt(1, turn.getStat());
					updateStmt.setString(2, turn.getPosid());
					updateStmt.setDate(
						3,
						new Date(turn.getWorkdate().getTime()));
					updateStmt.setInt(4, turn.getShiftID());
					updateStmt.execute();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "Êý¾Ý¿â²Ù×÷´íÎó!";
			errorMsg2 = e.getMessage();
		}

	}

}
