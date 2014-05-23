package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.royalstone.pos.coupon.CouponLargess;
import com.royalstone.pos.web.util.DBConnection;


public class CouponInfoQueryServletCommand implements ICommand {

	private String datasource = "java:comp/env/dbcard";

	public Object[] excute(Object[] values) {

		if (values.length == 5 && (values[1] instanceof String)
				&& (values[2] instanceof String)) {
			try {

				String coupon = (String) values[1];
				String count = (String) values[2];
				String sheepID = (String) values[3];
				String largessType = (String) values[4];
				
				Object[] results = new Object[1];
				// 查询赠送
				if (largessType.equals("0")){
					results[0] = query(coupon, count, sheepID);//
					}
				// 查询碰销
				if (largessType.equals("1")){
					results[0] = queryL(coupon, count, sheepID);
					}

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}
	
	// 查询赠送信息
	private CouponLargess query(String coupon, String count, String sheepID) {

		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		conn = DBConnection.getConnection(datasource);

		CouponLargess cardquery = new CouponLargess();
		String mode = null;
		int CountGross;
		if (coupon == null) {
			cardquery.setExceptionInfo("券号为空");
			return cardquery;
		}
		try {
			
			int counts = Integer.parseInt(count);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
			String today = sdf.format(new Date());

			conn.setAutoCommit(true);
			state = conn.createStatement();

			String sql = "select a.TypeID, a.Discount,a.PsMode, a.Presentamount,a.Price, a.Presenttype , a.PsFlag "
					+ "From CouponDiscount a "
					+ "where a.TypeID = '"
					+ coupon
					+ "'and  a.BeginNo <= '"
					+ counts
					+ "'and a.EndNo >= '"
					+ counts
					+ "'and a.EndDate >= '"
					+ today
					+ "'and a.BeginDate <= '"
					+ today
					+ "'and a.ShopID= '"
					+ sheepID
					+ "' and a.PsFlag != 3 ";

			rs = state.executeQuery(sql);

			if (!rs.next()) {
				String sql_l = "select top 1 a.BeginNo, a.TypeID, a.Discount,a.PsMode, a.Presentamount,a.Price, a.Presenttype , a.PsFlag "
					+ "From CouponDiscount a "
					+ "where a.TypeID = '"
					+ coupon
					+ "'and  a.BeginNo <= '"
					+ counts
					+ "'and a.EndDate >= '"
					+ today
					+ "'and a.BeginDate <= '"
					+ today
					+ "'and a.ShopID= '"
					+ sheepID
					+ "' and a.PsFlag != 3 "
					+ " ORDER BY BeginNo DESC ";
				
				rs.close();
				rs = state.executeQuery(sql_l);
				
				// 没有促销数据
				if (!rs.next()){
					cardquery.setExceptionInfo("没有此类型的促销");
					return cardquery;
				}
				
				CountGross = Math.round(counts/rs.getInt("BeginNo"));
				cardquery.setBeginNo(rs.getInt("BeginNo"));
				cardquery.setDiscontNo(CountGross*cardquery.getBeginNo());
				
				// 获取数据
				cardquery.setTypeID(rs.getString("TypeID"));
				cardquery.setDiscount(rs.getBigDecimal("Discount"));
				
				if (rs.getShort("PsMode") == 1) {
//					cardquery.setPresentamount(Integer.parseInt(count)* rs.getInt("Presentamount"));
//					cardquery.setAddPrice((int) (((rs.getBigDecimal("price")).doubleValue() * 100 ) * counts));
					cardquery.setPresentamount(cardquery.getDiscontNo()* rs.getInt("Presentamount"));
					cardquery.setAddPrice((int) (((rs.getBigDecimal("price")).doubleValue() * 100 ) * cardquery.getDiscontNo()));
				} else {
					cardquery.setPresentamount(CountGross * rs.getInt("Presentamount"));
//					cardquery.setPresentamount(rs.getInt("Presentamount"));
//					cardquery.setAddPrice(((int) (rs.getBigDecimal("price")).doubleValue() * 100));
					cardquery.setAddPrice((int) (((rs.getBigDecimal("price")).doubleValue() * 100 ) * CountGross));
				}
				
				cardquery.setCount(rs.getInt("Presentamount"));
				cardquery.setPrice(rs.getBigDecimal("Price"));
				cardquery.setPresenttype(rs.getString("Presenttype"));
				cardquery.setPsMode(rs.getBigDecimal("PsMode"));
				cardquery.setPsFlag(rs.getBigDecimal("PsFlag"));
				cardquery.setFlag(true);
				
				return cardquery;
				
			}
			
			cardquery.setTypeID(rs.getString("TypeID"));
			cardquery.setDiscount(rs.getBigDecimal("Discount"));
			
			if (rs.getShort("PsMode") == 1) {
				cardquery.setPresentamount(Integer.parseInt(count)* rs.getInt("Presentamount"));
				cardquery.setAddPrice((int) (((rs.getBigDecimal("price")).doubleValue() * 100 ) * counts));
			} else {
				cardquery.setPresentamount(rs.getInt("Presentamount"));
				cardquery.setAddPrice(((int) (rs.getBigDecimal("price")).doubleValue() * 100));
			}
			
			cardquery.setCount(rs.getInt("Presentamount"));
			cardquery.setPrice(rs.getBigDecimal("Price"));
			cardquery.setPresenttype(rs.getString("Presenttype"));
			cardquery.setPsMode(rs.getBigDecimal("PsMode"));
			cardquery.setPsFlag(rs.getBigDecimal("PsFlag"));
			cardquery.setFlag(false);
			
			return cardquery;

		} catch (SQLException se) {
			se.printStackTrace();
			cardquery.setExceptionInfo("数据库操作有误");

			return cardquery;
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}

	}
	
	//  查询碰销信息
	private CouponLargess queryL(String coupon, String count, String sheepID) {

		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		conn = DBConnection.getConnection(datasource);

		CouponLargess cardquery = new CouponLargess();
		String mode = null;
		if (coupon == null) {
			cardquery.setExceptionInfo("券号为空");
			
			return cardquery;
		}
		
		try {
			int counts = Integer.parseInt(count);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
			String today = sdf.format(new Date());

			conn.setAutoCommit(true);
			state = conn.createStatement();

			String sql = "select a.TypeID, a.Discount,a.PsMode, a.Presentamount,a.Price, a.Presenttype , a.PsFlag , a.EndNo "
					+ "From CouponDiscount a "
					+ "where a.TypeID = '"
					+ coupon
					+ "'and a.EndDate >= '"
					+ today
					+ "'and a.BeginDate <='"
					+ today
					+ "'and a.ShopID='"
					+ sheepID 
					+ "'and a.PsFlag = 3 ";

			rs = state.executeQuery(sql);

			if (!rs.next()) {
				cardquery.setExceptionInfo("没有此类型的碰销");
				
				return cardquery;

			}
			
			cardquery.setTypeID(rs.getString("TypeID"));
			cardquery.setDiscount(rs.getBigDecimal("Discount"));
			
			cardquery.setEndNo(rs.getInt("EndNo"));
			
			int endNo = cardquery.getEndNO();
			counts = Math.round(counts/endNo);
			
			cardquery.setAddPrice((int) (((rs.getBigDecimal("price")).doubleValue() * 100 ) * counts));
			
			cardquery.setCount(rs.getInt("Presentamount"));
			cardquery.setPrice(rs.getBigDecimal("Price"));
			cardquery.setPresenttype(rs.getString("Presenttype"));
			cardquery.setPsMode(rs.getBigDecimal("PsMode"));
			cardquery.setPsFlag(rs.getBigDecimal("PsFlag"));
			
			return cardquery;

		} catch (SQLException se) {
			se.printStackTrace();
			cardquery.setExceptionInfo("数据库操作有误");

			return cardquery;
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}

	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

}