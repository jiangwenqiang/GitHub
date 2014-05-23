package com.royalstone.pos.web.command.workTurn;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.royalstone.pos.data.PosTurn;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */
public class UploadCompletePosTurnCommand implements ICommand {

	private String errorMsg1;
	private String errorMsg2;

	public Object[] excute(Object[] values) {
		if (values != null && values.length == 2) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[2];
				savePosTurn(con, (ArrayList) values[1]);
				result[0] = errorMsg1;
				result[1] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private void savePosTurn(Connection connection, ArrayList turnList) {
		try {
			connection.setAutoCommit(false);
			String checkSql =
				"select stat from pos_turn where pos_id=? and workdate=? and shiftid=?";
			PreparedStatement checkStmt = connection.prepareStatement(checkSql);

			String insertSql =
				"insert into pos_turn (pos_id,workdate,shiftid,start_time,end_time,"
					+ "stat,isStartOffLine,isEndOffLine) values "
					+ "(?,?,?,?,?,?,?,?)";
			PreparedStatement insertStmt =
				connection.prepareStatement(insertSql);

			String updateSql =
				"update pos_turn set stat=?,isEndOffLine=?,end_time=? where pos_id=? and workdate=? and shiftid=?";
			PreparedStatement updateStmt =
				connection.prepareStatement(updateSql);

			String resetSql = "update pos_lst set listno=0 where pos_id=? ";
			PreparedStatement resetStmt =
				connection.prepareStatement(resetSql);
			boolean hasReset=false;

			for (int i = 0; i < turnList.size(); i++) {
				PosTurn turn = (PosTurn) turnList.get(i);
				checkStmt.setString(1, turn.getPosid());
				checkStmt.setDate(2, new Date(turn.getWorkdate().getTime()));
				checkStmt.setInt(3, turn.getShiftID());
				ResultSet rs = checkStmt.executeQuery();
				if (rs.next()) {
					int stat = rs.getInt("stat");
					if (stat != 1) {
						updateStmt.setInt(1, turn.getStat());
						updateStmt.setInt(2, 1);
						updateStmt.setTimestamp(
							3,
							new Timestamp(turn.getEndTime().getTime()));
						updateStmt.setString(4, turn.getPosid());
						updateStmt.setDate(
							5,
							new Date(turn.getWorkdate().getTime()));
						updateStmt.setInt(6, turn.getShiftID());
						updateStmt.execute();
					}
				} else {
					insertStmt.setString(1, turn.getPosid());
					insertStmt.setDate(
						2,
						new Date(turn.getWorkdate().getTime()));
					insertStmt.setInt(3, turn.getShiftID());
					insertStmt.setTimestamp(
						4,
						new Timestamp(turn.getStartTime().getTime()));
					insertStmt.setTimestamp(
						5,
						new Timestamp(turn.getEndTime().getTime()));
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
				}

				if (!hasReset && turn.getShiftID() == 3) {
					resetStmt.setString(1, turn.getPosid());
					resetStmt.execute();
					hasReset=true;
					
				}

				rs.close();
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "Êý¾Ý¿â²Ù×÷´íÎó!";
			errorMsg2 = e.getMessage();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}
