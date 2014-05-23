package com.royalstone.pos.web.command.realtime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.royalstone.pos.common.GoodsProduct;
import com.royalstone.pos.common.ProductProperty;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

public class LookupGoodsProductCommand implements ICommand {

	private String errorMsg1;

	private String errorMsg2;

	public Object[] excute(Object[] values) {
		// TODO 自动生成方法存根
		System.out.println("LookupGoodsProductCommand executed!");

		if (values != null && values.length == 2
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

	private GoodsProduct lookup(Connection con, String code) {

		GoodsProduct result = null;

		try {
			result = lookupWithException(con, code);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "数据库查询错误!";
			errorMsg2 = e.getMessage();
		}

		// TODO 自动生成方法存根
		return result;
	}

	private GoodsProduct lookupWithException(Connection con, String code) throws SQLException {
		GoodsProduct result = null;
		
		System.out.println("GoodsProductlookupWithException executed!");

		PreparedStatement pstmt = con
				.prepareStatement("SELECT id,name,price,flag FROM GoodsProductType where vgno = ?;");

		pstmt.setString(1,code);

		ResultSet rs = pstmt.executeQuery();

		Vector v = null;
		ProductProperty prop = null;
		boolean iscreate = true;
		while (rs.next()) {
			prop = new ProductProperty();
			prop.setId(Formatter.mytrim(rs.getString("id")));
			prop.setName(Formatter.mytrim(rs.getString("name")));
			prop.setPrice(rs.getDouble("price"));
			prop.setFlag(rs.getInt("flag"));
			if(iscreate && prop != null)
			{
				System.out.println("createVector");
				
				v = new Vector();
				iscreate = false;
			}
	
			if(v != null && prop != null )
			{
				System.out.println("addVector");
				v.add(prop);
			}
		}
		rs.close();
		if(v != null)
			result = new GoodsProduct(code,v);
		return result;
	}
}
