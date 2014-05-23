package com.royalstone.pos.web.command.workTurn;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.royalstone.pos.data.PosTurn;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */
public class CheckStatCommand implements ICommand {
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
				result[0] = checkStat(con, (PosTurn) values[1]);
				result[1] = errorMsg1;
				result[2] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private Boolean checkStat(Connection connection, PosTurn turn) {
		try {
			String sql="select stat from pos_turn where pos_id=? and workdate=? and shiftid=?";
			PreparedStatement state = connection.prepareStatement(sql);
			state.setString(1,turn.getPosid());
			state.setDate(2,new Date(turn.getWorkdate().getTime()));
			state.setInt(3,turn.getShiftID());
			ResultSet rs=state.executeQuery();
			if(rs.next()){
				int stat=rs.getInt("stat");
				if(stat==1){
					return new Boolean(true);								
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "Êý¾Ý¿â²Ù×÷´íÎó!";
			errorMsg2 = e.getMessage();
		}
		return new Boolean(false);
	}
}
