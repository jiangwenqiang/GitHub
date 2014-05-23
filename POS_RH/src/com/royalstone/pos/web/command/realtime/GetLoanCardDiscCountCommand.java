package com.royalstone.pos.web.command.realtime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ����˴��룬ȡ�ù��˿����Żݽ��
 * @author lichao
 */
public class GetLoanCardDiscCountCommand implements ICommand {
	
	private String errorMsg1;
	private String errorMsg2;
	
	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {
		if (values != null
				&& values.length == 2
				&& (values[1] instanceof String)) {

				Connection con = null;

				try {
					con = DBConnection.getConnection("java:comp/env/dbcard");

					Object[] result = new Object[3];
					result[0] = new Integer(getLoanCardDiscCount(con, (String) values[1]));
					result[1] = errorMsg1;
					result[2] = errorMsg2;
					return result;

				} finally {
					DBConnection.closeAll(null, null, con);
				}

			}
		return null;
	}
	
	/**
	 * ���ݹ��˿��Ŵӱ�creditsubcard���ѯ�����˿����Żݽ��
	 * @param connection �����ݿ������
	 * @param CardNo ���˿���
	 * @return �Żݽ��
	 */
	public int getLoanCardDiscCount(Connection connection, String CardNo) {

		int result=0;
		String sql =
			" SELECT disccount*100 disccount "
				+ " FROM creditclient where cardno = "
				+ " ( SELECT cardno from creditsubcard where creditsubcardno = ? ); ";
		try {

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, CardNo);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int disccount = (int) (rs.getDouble("disccount"));
				result = disccount;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "���ݿ��ѯ����!";
			errorMsg2 = e.getMessage();
		}

		return result;
	}

}
