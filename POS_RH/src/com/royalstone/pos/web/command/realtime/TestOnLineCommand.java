package com.royalstone.pos.web.command.realtime;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ����˴��룬����Ӧ�÷������Ƿ�����ݿ��������������������POS���Ƿ�������״̬������
 * @author liangxinbiao
 */
public class TestOnLineCommand implements ICommand {

	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {
		if (values != null
			&& values.length == 1) {
				Connection con = null;
				try {
					con = DBConnection.getConnection("java:comp/env/dbpos");
					Statement stmt=con.createStatement();
					stmt.execute("select 1");

					Object[] result = new Object[1];
					result[0] = "1";
					return result;
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBConnection.closeAll(null, null, con);
				}
		}
		return null;
	}
}
