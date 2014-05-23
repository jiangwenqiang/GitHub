package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.royalstone.pos.coupon.CardCouponInfoList;
import com.royalstone.pos.coupon.CardCouponInfoNew;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 服务端代码，调用相应的Ejb完成储值卡的查询
 * 
 * @deprecated
 * @author liangxinbiao
 */
public class CardCouponInfoServletCommand implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 2 && (values[0] instanceof String)) {
			try {

				String salesID = (String) values[1];

				Object[] results = new Object[1];

				results[0] = query(salesID);

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
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
	private CardCouponInfoList query(String salesID) {

		CardCouponInfoList cardCouponInfoList = new CardCouponInfoList();
		Connection conn = null;
		Statement state = null;
		ResultSet set = null;
		conn = DBConnection.getConnection("java:comp/env/dbcard");
		String mode = null;
		try {
			conn.setAutoCommit(true);
			state = conn.createStatement();
			String sql = "select * From CouponDiscount";
			set = state.executeQuery(sql);

			while (set.next()) {

				CardCouponInfoNew cardCouponInfoNew = new CardCouponInfoNew();

				cardCouponInfoNew.setShopID(set.getString("ShopID"));
				cardCouponInfoNew.setTypeID(set.getString("TypeID"));
				cardCouponInfoNew.setBeginDate(set.getString("BeginDate"));
				cardCouponInfoNew.setEndDate(set.getString("EndDate"));
				cardCouponInfoNew.setBeginNo(set.getInt("BeginNO"));
				cardCouponInfoNew.setEndNo(set.getInt("EndNo"));
				cardCouponInfoNew.setDiscount(set.getBigDecimal("Discount"));
				cardCouponInfoNew.setPsFlag(set.getBigDecimal("PsFlag"));
				cardCouponInfoNew.setPresenttype(set.getString("Presenttype"));
				cardCouponInfoNew.setPsMode(set.getInt("PsMode"));
				cardCouponInfoNew.setPresentamount(set.getInt("Presentamount"));
				cardCouponInfoNew.setPrice(set.getBigDecimal("Price"));
				cardCouponInfoNew.setSheetID(set.getString("SheetID"));
				cardCouponInfoNew.setNote(set.getString("Note"));
				cardCouponInfoList.add(cardCouponInfoNew);

			}

			set.close();

			ResultSet rs = state
					.executeQuery("select headShopID from sales where salesID = '"
							+ salesID + "'");

			if (rs.next()) {

				cardCouponInfoList.setHeadShopID(rs.getString("headShopID"));

			}

			rs.close();

			return cardCouponInfoList;
		} catch (SQLException se) {
			se.printStackTrace();
			return cardCouponInfoList;
		} finally {
			DBConnection.closeAll(set, state, conn);
		}

	}
}