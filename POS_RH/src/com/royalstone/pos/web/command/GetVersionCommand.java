package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.royalstone.pos.util.POSVersionVO;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，取得数据库里记录的各模块的版本号
 * @deprecated
 * @author liangxinbiao
 */
public class GetVersionCommand implements ICommand {

	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {

		if (values != null && values.length == 1) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[1];
				result[0] = fetchVersion(con);
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	public POSVersionVO fetchVersion(Connection con) {
		POSVersionVO version = new POSVersionVO();
		try {
			PreparedStatement pstmt =
				con.prepareStatement(
					"select progver from prog_lst where progname=?");
			pstmt.setString(1, "posv41.jar");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				version.setPosVersion(rs.getString("progver"));
			}

			pstmt.setString(1, "loader.jar");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				version.setLoaderVersion(rs.getString("progver"));
			}

			pstmt.setString(1, "driver.zip");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				version.setDriverVersion(rs.getString("progver"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return version;
	}

}
