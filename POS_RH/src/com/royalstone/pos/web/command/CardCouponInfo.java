package com.royalstone.pos.web.command;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.royalstone.pos.coupon.CardCouponInfoList;
import com.royalstone.pos.coupon.CardCouponInfoNew;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，用来查询挂账卡的信息
 * 
 * @author liangxinbiao
 */
public class CardCouponInfo implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 3) {

			String host = (String) values[1];

			String salesID = (String) values[2];

			Object[] results = new Object[1];

			results[0] = query(host, salesID);

			return results;
		}

		return null;
	}

	/**
	 * 根据卡号和密码(子卡不需密码)查询挂账卡的信息
	 * 
	 * @param cardNo
	 *            挂账卡号
	 * @return 挂账卡查询值对象
	 */
	private String query(String host, String salesID) {

		CardCouponInfoList cardCouponInfoList = null;
		String set = null;

		URL servlet;
		HttpURLConnection conn;

		try {

			servlet = new URL("http://" + host + "/pos41/DispatchServlet");

			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.CardCouponInfoServletCommand";
			params[1] = salesID;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();

			}

			if (results != null && results.length > 0) {
				cardCouponInfoList = (CardCouponInfoList) results[0];
				if (insertInfo(cardCouponInfoList, salesID, cardCouponInfoList
						.getHeadShopID())) {
					return "";
				}
				return "出错";
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return set;
	}

	private boolean insertInfo(CardCouponInfoList cardCouponInfoList,
			String salesID, String headShopID) {

		Connection conn = null;
		PreparedStatement pstate = null;

		conn = DBConnection.getConnection("java:comp/env/dbpos");

		try {

			conn.setAutoCommit(false);

			pstate = conn.prepareStatement("delete from CouponDiscount");

			pstate.executeUpdate();

			pstate.close();

			pstate = conn
					.prepareStatement("insert into CouponDiscount (shopid,typeid,begindate,enddate,beginno,endno,discount,"
							+

							"psflag,presenttype,psmode,presentamount,price,sheetid,note) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

			for (int i = 0; i < cardCouponInfoList.size(); i++) {

				CardCouponInfoNew cardCouponInfoNew = (CardCouponInfoNew) cardCouponInfoList
						.getInfo(i);

				pstate.setString(1, cardCouponInfoNew.getShopID());
				pstate.setString(2, cardCouponInfoNew.getTypeID());
				pstate.setString(3, cardCouponInfoNew.getBeginDate());
				pstate.setString(4, cardCouponInfoNew.getEndDate());
				pstate.setInt(5, cardCouponInfoNew.getBeginNO());
				pstate.setInt(6, cardCouponInfoNew.getEndNo());
				pstate.setObject(7, cardCouponInfoNew.getDiscount());
				pstate.setObject(8, cardCouponInfoNew.getPsFlag());
				pstate.setString(9, cardCouponInfoNew.getPresenttype());
				pstate.setInt(10, cardCouponInfoNew.getPsMode());
				pstate.setInt(11, cardCouponInfoNew.getPresentamount());
				pstate.setObject(12, cardCouponInfoNew.getPrice());
				pstate.setString(13, cardCouponInfoNew.getSheetID());
				pstate.setString(14, cardCouponInfoNew.getNote());

				pstate.addBatch();

			}

			pstate.executeBatch();

			pstate.close();

			pstate = conn.prepareStatement("delete from sales");

			pstate.executeUpdate();

			pstate.close();

			pstate = conn
					.prepareStatement("insert into sales (SalesID,HeadShopID) values (?,?) ");

			pstate.setString(1, salesID);

			pstate.setString(2, headShopID);

			pstate.executeUpdate();

			conn.commit();

			return true;

		} catch (SQLException se) {
			se.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return false;

		} finally {

			DBConnection.closeAll(null, pstate, conn);

		}

	}
}

