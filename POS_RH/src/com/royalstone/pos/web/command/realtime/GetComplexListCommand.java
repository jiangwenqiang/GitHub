package com.royalstone.pos.web.command.realtime;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;

import com.royalstone.pos.complex.DiscComplex;
import com.royalstone.pos.complex.DiscComplexList;
import com.royalstone.pos.complex.DiscPool;
import com.royalstone.pos.util.Day;
import com.royalstone.pos.util.PosTime;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，负责查询商品的组合促销信息
 * @author liangxinbiao
 */
public class GetComplexListCommand implements ICommand {

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
				result[0] = getComplexList(con, (String) values[1]);
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
	 * 根据商品编码从表GoodsCombineProm,GoodsCombinePromItem里查询出它所参与的组合促销信息
	 * @param connection 到数据库的连接
	 * @param code 商品编码
	 * @return 组合促销信息
	 */
	public DiscComplexList getComplexList(Connection connection, String code) {

		DiscComplexList disc_list = null;

		try {
			disc_list = getComplexListWithException(connection, code);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "数据库查询错误!";
			errorMsg2 = e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg1 = "参数错误!";
			errorMsg2 = e.getMessage();
		}

		return disc_list;
	}

	public DiscComplexList getComplexListWithException(
		Connection connection,
		String code)
		throws SQLException {

		String sql =
			"SELECT distinct CombineProm.schemaNo, Name, CombineProm.Price, StartDate, EndDate, StartTime, EndTime "
				+ "FROM CombineProm,CombinePromItem "
				+ "where CombineProm.schemaNo=CombinePromItem.schemaNo "
				+ "and vgno = ? and convert(char(8),getdate(),112)  between convert(char(8),startdate,112) and convert(char(8),enddate,112) "
				+ "and convert(char(8),getdate(),108) between convert(char(8),starttime,108) and convert(char(8),endtime,108) ; ";

		DiscComplexList disc_list = new DiscComplexList();
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1,Integer.parseInt(code.trim()));
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			int schemaNo = rs.getInt("schemaNo");
			String Name = rs.getString("Name");
			double Price = rs.getDouble("Price");
			Date startDate = rs.getDate("startDate");
			Date EndDate = rs.getDate("EndDate");
			Time StartTime = rs.getTime("StartTime");
			Time EndTime = rs.getTime("EndTime");
			int level = 1;

			DiscComplex disc =
				new DiscComplex(
					"" + schemaNo,
					Name,
					(int) Math.rint(100 * Price),
					new Day(startDate),
					new Day(EndDate),
					new PosTime(StartTime),
					new PosTime(EndTime),
					level);

			String sql_item =
				" SELECT groupno, vgno, Qty, PromValue "
					+ " FROM CombinePromItem WHERE schemaNo = ? ; ";

			PreparedStatement pstmt = connection.prepareStatement(sql_item);
			pstmt.setInt(1, schemaNo);
			ResultSet rs_item = pstmt.executeQuery();

			while (rs_item.next()) {
				int Link_Goodsid = rs_item.getInt("groupno");
				int Goodsid = rs_item.getInt("vgno");
				double Qty = rs_item.getDouble("Qty");
				double PromValue = rs_item.getDouble("PromValue");

				DecimalFormat df = new DecimalFormat("000000");
				DiscPool pool =
					new DiscPool(
						df.format(Link_Goodsid),
						df.format(Goodsid),
						(int) Math.rint(Qty),
						(int) Math.rint(100 * PromValue));

				disc.addPool(pool);
			}
			rs_item.close();
			disc_list.add(disc);
		}
		rs.close();

		return disc_list;
	}
}
