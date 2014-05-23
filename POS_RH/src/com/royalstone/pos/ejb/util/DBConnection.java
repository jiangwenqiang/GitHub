package com.royalstone.pos.ejb.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 用来管理EJB的数据库连接
 * @deprecated
 * @author liangxinbiao
 */
public class DBConnection {

	public static Connection getConnection(String datasource)
		throws NamingException, SQLException {
		Connection conn = null;
		try {
			DataSource ds =
				ServiceLocator.getInstance().getDataSource(datasource);
			conn = ds.getConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		return conn;
	}

	public static void closeAll(ResultSet rs, Statement stmt, Connection con) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

}
