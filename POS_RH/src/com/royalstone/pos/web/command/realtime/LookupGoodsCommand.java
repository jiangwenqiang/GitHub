package com.royalstone.pos.web.command.realtime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.royalstone.pos.common.Goods;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ����˴��룬�����ѯ��Ʒ��Ϣ
 * @author liangxinbiao
 */
public class LookupGoodsCommand implements ICommand {

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
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[3];
				result[0] = lookup(con, (String) values[1]);
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
	 * ������Ʒ���������ӱ�price_lst���ѯ����Ʒ��Ϣ
	 * @param connection �����ݿ������
	 * @param code ��Ʒ���������
	 * @return ��Ʒ��Ϣ
	 */
	public Goods lookup(Connection connection, String code) {

		Goods result = null;

		try {
			result = lookupWithException(connection, code);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "���ݿ��ѯ����!";
			errorMsg2 = e.getMessage();
		}

		return result;
	}

	public Goods lookupWithException(Connection connection, String code)
		throws SQLException {
			
		Goods result = null;

		PreparedStatement pstmt =
			connection.prepareStatement(
				" SELECT vgno, goodsno, gname, deptno, spec, uname,  v_type, p_type, price, x ,producttype  FROM price_lst where goodsno=? or vgno=?; ");

		pstmt.setString(1, code);
		pstmt.setString(2, code);

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			String vgno = Formatter.mytrim(rs.getString("vgno"));
			String goodsno = Formatter.mytrim(rs.getString("goodsno"));
			String gname = Formatter.mytrim(rs.getString("gname"));
			String deptno = Formatter.mytrim(rs.getString("deptno"));
			String spec = Formatter.mytrim(rs.getString("spec"));
			String uname = Formatter.mytrim(rs.getString("uname"));
			String v_type = Formatter.mytrim(rs.getString("v_type"));
			String p_type = Formatter.mytrim(rs.getString("p_type"));
			double price = rs.getDouble("price");
			int x = rs.getInt("x");
			int prodtype = rs.getInt("producttype");
			
			int goods_type = Integer.parseInt(v_type);
			int i_price = (int) Math.rint(price * 100);
			if (spec == null)
				spec = "";
			if (uname == null)
				uname = "";

			result =
				new Goods(
					vgno,
					goodsno,
					gname,
					deptno,
					spec,
					uname,
					i_price,
					goods_type,
					x,
					p_type,
					prodtype);

		}
		rs.close();
		
		return result;
	}

}
