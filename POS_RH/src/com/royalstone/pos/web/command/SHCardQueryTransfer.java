package com.royalstone.pos.web.command;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.card.SHCardQueryVO;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;

/**
 * ����˴��룬������ѯ���˿�����Ϣ
 * @author liangxinbiao
 */
public class SHCardQueryTransfer implements ICommand {

	public Object[] excute(Object[] values) {
		if (values.length == 4
			&& (values[1] instanceof String)
			&& (values[2] instanceof String)
			&& ((values[3] instanceof String) || (values[3]==null))){
            String cardNo = (String) values[1];
			String secrety = (String) values[2];
			String host = (String) values[3];

			Object[] results = new Object[1];
			results[0] = query(cardNo,secrety,host);
			return results;
		}

		return null;
	}

	/**
	 * ���ݿ��ź�����(�ӿ���������)��ѯ���˿�����Ϣ
	 * @param cardNo ���˿���
	 * @return ���˿���ѯֵ����
	 */
	private SHCardQueryVO query(String cardNo,String secrety,String host) {

        SHCardQueryVO cardquery = new SHCardQueryVO();


        URL servlet;
        HttpURLConnection conn;
        try {
		servlet =new URL("http://"+ host+ "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
			cardquery.setExceptioninfo("���ӿ�����������,�����������");
            return cardquery;
		}


		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] = "com.royalstone.pos.web.command.SHCardQueryServletCommand";
			params[1] = cardNo;
            params[2] = secrety;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				cardquery = (SHCardQueryVO) results[0];
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		    cardquery.setExceptioninfo("��ѯ������������,�����������");
            return cardquery;
		}
         return cardquery;
	}

}
