package com.royalstone.pos.web.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * ���ӹ�����ʵ�֣�����������͸��Ӧ�÷�����ȡ�õ�
 * @author liangxinbiao
 */
public class ConnectionFactoryContainer implements IConnectionFactory {

	/**
	 * @see com.royalstone.pos.web.util.IConnectionFactory#getConnection(java.lang.String)
	 */
	public Connection getConnection(String datasrc) {
		Connection con = null;
		Context ctx;
		try {
			ctx = new InitialContext();
			DataSource ds = null;
			if (ctx != null) {
				ds = (DataSource) ctx.lookup(datasrc);
				con = ds.getConnection();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

}
