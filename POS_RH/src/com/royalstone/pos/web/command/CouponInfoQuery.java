package com.royalstone.pos.web.command;

import com.royalstone.pos.coupon.CouponLargess;

/**
 * ����˴��룬������ѯ���˿�����Ϣ
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
	 * ���ݿ��ź�����(�ӿ���������)��ѯ���˿�����Ϣ
	 * 
	 * @param cardNo
	 *            ���˿���
	 * @return ���˿���ѯֵ����
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