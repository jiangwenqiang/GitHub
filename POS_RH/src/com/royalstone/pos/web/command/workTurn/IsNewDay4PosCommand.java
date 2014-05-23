package com.royalstone.pos.web.command.workTurn;

import java.sql.Connection;
import java.sql.SQLException;

import com.royalstone.pos.db.WorkTurnMinister;
import com.royalstone.pos.util.WorkTurn;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */
public class IsNewDay4PosCommand implements ICommand {

	private String errorMsg1;
	private String errorMsg2;

	public Object[] excute(Object[] values) {
		if (values != null && values.length == 3) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[3];
				result[0] =
					isNewDay4Pos(con, (String) values[1], (WorkTurn) values[2]);
				result[1] = errorMsg1;
				result[2] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private Boolean isNewDay4Pos(
		Connection connection,
		String posid,
		WorkTurn turn) {
		boolean result = false;
		try {
			WorkTurnMinister workTurnMinister =
				new WorkTurnMinister(connection);
			if(workTurnMinister.isNewDay4Pos(posid, turn)){
				workTurnMinister.resetPosListno(posid);
				result=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "Êý¾Ý¿â²Ù×÷´íÎó!";
			errorMsg2 = e.getMessage();
		}
		return new Boolean(result);
	}

}
