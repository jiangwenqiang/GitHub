package com.royalstone.pos.web.command.workTurn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 *
 */
public class SetCurOperatorCommand implements ICommand {

	private String errorMsg1;
	private String errorMsg2;

	public Object[] excute(Object[] values) {
		if (values != null && values.length == 3) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[2];
				setCurOperator(con, (String) values[1], (String) values[2]);
				result[0] = errorMsg1;
				result[1] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private void setCurOperator(
		Connection connection,
		String posid,
		String cashierid) {
		try {
			posid = posid.toUpperCase();
			String sql =
				" UPDATE pos_lst SET current_op = ? WHERE pos_id = ?; ";
			PreparedStatement pstmt;

			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, cashierid);
			pstmt.setString(2, posid);
			pstmt.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
			errorMsg1 = "Êý¾Ý¿â²Ù×÷´íÎó!";
			errorMsg2 = e.getMessage();
		}
	}

}
