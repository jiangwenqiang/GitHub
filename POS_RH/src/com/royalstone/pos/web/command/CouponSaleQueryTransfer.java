package com.royalstone.pos.web.command;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.coupon.CouponSale;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;

/**
 * 服务端代码，用来查询挂账卡的信息
 * @author liangxinbiao
 */
public class CouponSaleQueryTransfer implements ICommand {

	public Object[] excute(Object[] values) {
		if (values.length == 5
			&& (values[1] instanceof String)
			&& (values[2] instanceof String)
			&& ((values[3] instanceof String) || (values[4]==null))){
            String cardNo = (String) values[1];
			String secrety = (String) values[2];
			String storeid = (String) values[3];
			String host = (String) values[4];

			Object[] results = new Object[1];
			results[0] = query(cardNo,secrety,storeid,host);
			return results;
		}

		return null;
	}

	/**
	 * 根据卡号和密码(子卡不需密码)查询挂账卡的信息
	 * @param cardNo 挂账卡号
	 * @return 挂账卡查询值对象
	 */
	private CouponSale query(String cardNo,String secrety,String storeid,String host) {

        CouponSale cardquery = new CouponSale();


        URL servlet;
        HttpURLConnection conn;
        try {
		servlet =new URL("http://"+ host+ "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
			cardquery.setExceptionInfo("连接卡服务器出错,按清除键继续");
            return cardquery;
		}


		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[4];

			params[0] = "com.royalstone.pos.web.command.CouponSaleQueryServletCommand";
			params[1] = cardNo;
            params[2] = secrety;
            params[3] = storeid;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				
				cardquery = (CouponSale) results[0];
				
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		    cardquery.setExceptionInfo("查询券服务器出错,按清除键继续");
            return cardquery;
		}
         return cardquery;
	}

}
