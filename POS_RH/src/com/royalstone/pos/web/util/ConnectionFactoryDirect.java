package com.royalstone.pos.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ���ӹ�����ʵ�֣�����������ֱ��͸��JDBCȡ�õģ�
 * ֻҪ������Ӧ�÷����������һЩ��Ҫ�������ݿ��ģ��
 * @author liangxinbiao
 */
public class ConnectionFactoryDirect implements IConnectionFactory {

	private DataSource[] datasources =
		{
			new DataSource(
				"java:comp/env/dbcard",
				"com.microsoft.jdbc.sqlserver.SQLServerDriver",
				"jdbc:microsoft:sqlserver://172.16.16.108:1433;DatabaseName=MyshopSHCard",
				"sa",
				"sa"),
			new DataSource(
				"java:comp/env/dbpos",
				"com.microsoft.jdbc.sqlserver.SQLServerDriver",
				"jdbc:microsoft:sqlserver://172.16.7.197:1433;SelectMethod=Cursor;databasename=ApplePOS",
				"sa",
				"sa")};

	/**
	 * @see com.royalstone.pos.web.util.IConnectionFactory#getConnection(java.lang.String)
	 */
	public Connection getConnection(String datasrc) {

		for (int i = 0; i < datasources.length; i++) {
			if (datasources[i].getDatasourceName() != null
				&& datasources[i].getDatasourceName().equals(datasrc)) {
				try {
					Class.forName(datasources[i].getDriverName());
					Connection conn =
						DriverManager.getConnection(
							datasources[i].url,
							datasources[i].userName,
							datasources[i].password);
					return conn;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * ����Դ�������й�Ҫ���ӵ����ݿ����Ϣ��������ַ��JNDI����URL��
	 * @author liangxinbiao
	 */
	private class DataSource {

		private String datasourceName;
		private String driverName;
		private String url;
		private String userName;
		private String password;

		public DataSource(
			String datasourceName,
			String driverName,
			String url,
			String userName,
			String password) {
			this.datasourceName = datasourceName;
			this.driverName = driverName;
			this.url = url;
			this.userName = userName;
			this.password = password;
		}

		/**
		 * @return JNDI��
		 */
		public String getDatasourceName() {
			return datasourceName;
		}

		/**
		 * @return ������������
		 */
		public String getDriverName() {
			return driverName;
		}

		/**
		 * @return �������ݿ�����
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * @return �������ݿ��JDBC URL
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @return �������ݿ���û���
		 */
		public String getUserName() {
			return userName;
		}

	}

}
