package com.royalstone.pos.web.command;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.coupon.CouponSale;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;

/**
 * ����˴��룬������ѯ���˿�����Ϣ
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
	 * ���ݿ��ź�����(�ӿ���������)��ѯ���˿�����Ϣ
	 * @param cardNo ���˿���
	 * @return ���˿���ѯֵ����
	 */
	private CouponSale query(String cardNo,String secrety,String storeid,String host) {

        CouponSale cardquery = new CouponSale();


        URL servlet;
        HttpURLConnection conn;
        try {
		servlet =new URL("http://"+ host+ "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
			cardquery.setExceptionInfo("���ӿ�����������,�����������");
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
		    cardquery.setExceptionInfo("��ѯȯ����������,�����������");
            return cardquery;
		}
         return cardquery;
	}

}
