package com.royalstone.pos.web.command.realtime;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.royalstone.pos.favor.Disc4Dept;
import com.royalstone.pos.favor.Disc4Goods;
import com.royalstone.pos.favor.Disc4Member;
import com.royalstone.pos.favor.Disc4MemberDept;
import com.royalstone.pos.favor.DiscCriteria;
import com.royalstone.pos.favor.DiscTime;
import com.royalstone.pos.favor.Prom4Member;
import com.royalstone.pos.favor.Promotion;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，负责查询商品的普通促销信息 
 * @author liangxinbiao
 */
public class GetDiscCriteriaCommand implements ICommand {

	private String errorMsg1;
	private String errorMsg2;

	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {

		if (values != null
			&& values.length == 3
			&& (values[1] instanceof String)) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[3];
				result[0] =
					getDiscCriteria(
						con,
						(String) values[1],
						(String) values[2]);
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
	 * 根据商品编码和商品的促销类型查询出商品的普通促销信息
	 * @param connection 到数据库的连接
	 * @param code 商品编码
	 * @param ptype 促销类型
	 * @return 普通促销信息
	 */
	public DiscCriteria getDiscCriteria(
		Connection connection,
		String code,
		String ptype) {

		DiscCriteria result = null;

		try {
			result = getDiscCriteriaWithException(connection, code, ptype);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "数据库查询错误!";
			errorMsg2 = e.getMessage();
		}

		return result;
	}

	/**
	 * 根据商品编码在表distitem_vgno里查询商品的单品折扣
	 * @param connection 到数据库的连接
	 * @param code 商品编码
	 * @return 普通促销信息
	 * @throws SQLException
	 */
	public DiscCriteria getDisc4Goods(Connection connection, String code)
		throws SQLException {

		DiscCriteria result = null;

		String sql =
			" SELECT vgno, distrate1, distrate2, distrate3, "
				+ " min_amount, med_amount, max_amount, "
				+ " starttime, endtime "
				+ " FROM distitem_vgno where vgno=?; ";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, code);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			String vgno = Formatter.mytrim(rs.getString("vgno"));
			int distrate1 = rs.getInt("distrate1");
			int distrate2 = rs.getInt("distrate2");
			int distrate3 = rs.getInt("distrate3");
			int min_amount = rs.getInt("min_amount");
			int med_amount = rs.getInt("med_amount");
			int max_amount = rs.getInt("max_amount");

			Date starttime = rs.getDate("starttime");
			Date endtime = rs.getDate("endtime");

			GregorianCalendar g_start = new GregorianCalendar();
			g_start.setTime(starttime);

			GregorianCalendar g_end = new GregorianCalendar();
			g_end.setTime(endtime);

			result =
				new Disc4Goods(
					vgno,
					distrate1,
					min_amount,
					distrate2,
					med_amount,
					distrate3,
					max_amount,
					g_start,
					g_end);
		}

		return result;

	}

	/**
	 * 根据商品编码在表distitem_dept和price_lst里查询商品的整类折扣
	 * @param connection 到数据库的连接
	 * @param code 商品编码
	 * @return 普通促销信息
	 * @throws SQLException
	 */
	public DiscCriteria getDisc4Dept(Connection connection, String code)
		throws SQLException {

		DiscCriteria result = null;

		String sql =
			" SELECT distitem_dept.deptno, distrate1, distrate2, distrate3, "
				+ " min_amount, med_amount, max_amount, "
				+ " starttime, endtime "
				+ " FROM distitem_dept,price_lst where price_lst.vgno=? and price_lst.deptno=distitem_dept.deptno; ";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, code);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			String deptno = Formatter.mytrim(rs.getString("deptno"));
			int distrate1 = rs.getInt("distrate1");
			int distrate2 = rs.getInt("distrate2");
			int distrate3 = rs.getInt("distrate3");
			int min_amount = rs.getInt("min_amount");
			int med_amount = rs.getInt("med_amount");
			int max_amount = rs.getInt("max_amount");

			Date starttime = rs.getDate("starttime");
			Date endtime = rs.getDate("endtime");

			GregorianCalendar g_start = new GregorianCalendar();
			g_start.setTime(starttime);

			GregorianCalendar g_end = new GregorianCalendar();
			g_end.setTime(endtime);

			result =
				new Disc4Dept(
					deptno,
					distrate1,
					min_amount,
					distrate2,
					med_amount,
					distrate3,
					max_amount,
					g_start,
					g_end);
		}

		return result;
	}
    public DiscCriteria getDisc4MemberDept(Connection connection, String code)
        throws SQLException {

        DiscCriteria result = null;

        String sql =
            " SELECT DeptID, DiscLevel, DiscRate,"
					+ " StartTime, EndTime "
					+ " FROM discDeptMember,price_lst where price_lst.vgno=? and price_lst.deptno=CONVERT(char(15),discDeptMember.DeptID); ";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, code);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
				int 	deptID 	   = rs.getInt( "DeptID" );
				int		discLevel  = rs.getInt( "DiscLevel" );
				int		discRate  = rs.getInt( "DiscRate" );
				Date	starttime  = rs.getDate( "StartTime" );
				Date	endtime    = rs.getDate( "EndTime" );

				GregorianCalendar g_start = new GregorianCalendar();
				g_start.setTime(starttime);

				GregorianCalendar g_end = new GregorianCalendar();
				g_end.setTime(endtime);

            result =
                new Disc4MemberDept( deptID, discLevel, discRate,g_start, g_end );
        }

        return result;

    }

	/**
	 * 根据商品编码从表promdisc_vip里查询出会员折扣
	 * @param connection 到数据库的连接
	 * @param code 商品编码
	 * @return 普通促销信息
	 * @throws SQLException
	 */
	public DiscCriteria getDisc4Member(Connection connection, String code)
		throws SQLException {

		DiscCriteria result = null;

		String sql =
			" SELECT vgno, promlevel, promdisc, "
				+ " startdate, enddate, starttime, endtime "
				+ " FROM promdisc_vip where vgno=?; ";

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, code);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			String vgno = Formatter.mytrim(rs.getString("vgno"));
			int promlevel = rs.getInt("promlevel");
			int promdisc = rs.getInt("promdisc");

			Date startdate = rs.getDate("startdate");
			Date enddate = rs.getDate("enddate");
			Date starttime = rs.getDate("starttime");
			Date endtime = rs.getDate("endtime");

			GregorianCalendar g_start = new GregorianCalendar();
			g_start.setTime(starttime);

			GregorianCalendar g_end = new GregorianCalendar();
			g_end.setTime(endtime);

			result = new Disc4Member(vgno, promdisc, promlevel, g_start, g_end);
		}

		return result;
	}


	/**
	 * 根据商品编码从表promotion里查询出商品的单品促销信息
	 * @param connection 到数据库的连接
	 * @param code 商品编码
	 * @return 普通促销信息
	 * @throws SQLException
	 */
	public DiscCriteria getPromotion(Connection connection, String code)
		throws SQLException {

		DiscCriteria result = null;

		String sql =
			" SELECT vgno, promtype, promprice, "
				+ " startdate, enddate, starttime, endtime "
				+ "FROM promotion where vgno=?; ";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, code);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			String vgno = Formatter.mytrim(rs.getString("vgno"));
			String promtype = Formatter.mytrim(rs.getString("promtype"));
			double promprice = rs.getDouble("promprice");

			Date startdate = rs.getDate("startdate");
			Date enddate = rs.getDate("enddate");
			Date starttime = rs.getDate("starttime");
			Date endtime = rs.getDate("endtime");

			GregorianCalendar g_start = new GregorianCalendar();
			g_start.setTime(starttime);

			GregorianCalendar g_end = new GregorianCalendar();
			g_end.setTime(endtime);

			result =
				new Promotion(vgno, (int) (100 * promprice), g_start, g_end);
		}

		return result;
	}

	/**
	 * 根据商品编码从表promotion_vip里查询出商品的会员价
	 * @param connection 到数据库的连接
	 * @param code 商品编码
	 * @return 普通促销信息
	 * @throws SQLException
	 */
	public DiscCriteria getProm4Member(Connection connection, String code)
		throws SQLException {

		DiscCriteria result = null;

		String sql =
			" SELECT vgno, promlevel, promprice, "
				+ " startdate, enddate, starttime, endtime "
				+ " FROM promotion_vip where vgno=?; ";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, code);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			String vgno = Formatter.mytrim(rs.getString("vgno"));
			int promlevel = rs.getInt("promlevel");
			double promprice = rs.getDouble("promprice");

			Date startdate = rs.getDate("startdate");
			Date enddate = rs.getDate("enddate");
			Date starttime = rs.getDate("starttime");
			Date endtime = rs.getDate("endtime");

			GregorianCalendar g_start = new GregorianCalendar();
			g_start.setTime(starttime);

			GregorianCalendar g_end = new GregorianCalendar();
			g_end.setTime(endtime);

			result =
				new Prom4Member(
					vgno,
					(int) (100 * promprice),
					promlevel,
					g_start,
					g_end);
		}

		return result;
	}
	
	/**
	 * 根据商品编码从表promotion里查询出商品的单品促销信息
	 * @param connection 到数据库的连接
	 * @param code 商品编码
	 * @return 普通促销信息
	 * @throws SQLException
	 */
	public DiscCriteria getDISCTIME(Connection connection, String code)
		throws SQLException {

		DiscCriteria result = null;
		SimpleDateFormat fmt1=new SimpleDateFormat("HHmmss");

		String sql =
			" SELECT vgno,time1,time2,time3,time4,time5,time6, "
				+ " distrate1,distrate2,distrate3,distrate4,distrate5,distrate6 "
				+ "FROM vgnodiscount where vgno=? and convert(char(8),getdate(),112) between convert(char(8),startdate,112) and convert(char(8),enddate,112); ";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, code);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			String vgno = Formatter.mytrim(rs.getString("vgno"));
			//int time1 = Integer.parseInt(fmt1.format(Date.valueOf(rs.getString("time1"))));
			int time1 = Integer.parseInt(fmt1.format(rs.getTime("time1")));
			int time2 = Integer.parseInt(fmt1.format(rs.getTime("time2")));
			int time3 = Integer.parseInt(fmt1.format(rs.getTime("time3")));
			int time4 = Integer.parseInt(fmt1.format(rs.getTime("time4")));
			int time5 = Integer.parseInt(fmt1.format(rs.getTime("time5")));
			int time6 = Integer.parseInt(fmt1.format(rs.getTime("time6")));
			
			int distrate1 = rs.getInt("distrate1");
			int distrate2 = rs.getInt("distrate2");
			int distrate3 = rs.getInt("distrate3");
			int distrate4 = rs.getInt("distrate4");
			int distrate5 = rs.getInt("distrate5");
			int distrate6 = rs.getInt("distrate6");

			result =
				new DiscTime(vgno,time1,distrate1,time2,distrate2,time3,distrate3,
						time4,distrate4,time5,distrate5,time6,distrate6);
		}

		return result;
	}


	public DiscCriteria getDiscCriteriaWithException(
		Connection connection,
		String code,
		String ptype)
		throws SQLException {
		DiscCriteria result = null;
		if (ptype.equals(DiscCriteria.DISC4GOODS)) {
			result = getDisc4Goods(connection, code);
		} else if (ptype.equals(DiscCriteria.DISC4DEPT)) {
			result = getDisc4Dept(connection, code);
		} else if (ptype.equals(DiscCriteria.DISC4MEMBER)) {
			result = getDisc4Member(connection, code);
		} else if (ptype.equals(DiscCriteria.PROMOTION)) {
			result = getPromotion(connection, code);
		} else if (ptype.equals(DiscCriteria.PROM4MEMBER)) {
			result = getProm4Member(connection, code);
        } else if (ptype.equals(DiscCriteria.DISC4MEMBERDEPT)){
			result = getDisc4MemberDept(connection, code);
		} else if (ptype.equals(DiscCriteria.DISCTIME)){//增加时点折扣
			result = getDISCTIME(connection, code);
		}
		return result;
	}
}
