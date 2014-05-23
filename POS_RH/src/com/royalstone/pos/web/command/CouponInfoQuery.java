package com.royalstone.pos.web.command;

import com.royalstone.pos.coupon.CouponLargess;

/**
 * 服务端代码，用来查询挂账卡的信息
 * 
 * @author liangxinbiao
 */
public class CouponInfoQuery implements ICommand {

	public Object[] excute(Object[] values) {
		if (values.length == 6 && (values[1] instanceof String)
				&& (values[2] instanceof String)
				&& ((values[3] instanceof String) || (values[3] == null))) {
			
			String coupon = (String) values[1];
			String count = (String) values[2];
			String sheepID = (String) values[3];
			String largessType = (String) values[4];
			String host = (String) values[5];

			Object[] results = new Object[1];
			results[0] = query(coupon, count, sheepID,largessType, host);
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
	private CouponLargess query(String coupon, String count, String sheepID,String largessType, String host) {
		
		CouponLargess cardquery = new CouponLargess();

		CouponInfoQueryServletCommand command = new CouponInfoQueryServletCommand();

		command.setDatasource("java:comp/env/dbpos");

		Object[] params = new Object[5];

		params[0] = "com.royalstone.pos.web.command.CouponInfoQueryServletCommand";
		params[1] = coupon;
		params[2] = count;
		params[3] = sheepID;
		params[4] = largessType;

		Object[] results = command.excute(params);

		if (results != null && results.length > 0) {
			return (CouponLargess) results[0];
		}

		return cardquery;

	}

}