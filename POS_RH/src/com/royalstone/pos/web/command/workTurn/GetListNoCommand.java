package com.royalstone.pos.web.command.workTurn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */
public class GetListNoCommand implements ICommand {

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
				result[0] = getListNo(con, (String) values[1]);
				result[1] = errorMsg1;
				result[2] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private Integer getListNo(Connection connection, String posid) {
		int listno = 0;
		try {
			String sql = " SELECT listno FROM pos_lst WHERE pos_id = ?; ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, posid);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				listno = rs.getInt("listno");
			return new Integer(listno);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "Êý¾Ý¿â²Ù×÷´íÎó!";
			errorMsg2 = e.getMessage();
		}
		return new Integer(listno);
	}

}
