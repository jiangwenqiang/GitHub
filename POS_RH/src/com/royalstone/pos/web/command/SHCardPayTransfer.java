package com.royalstone.pos.web.command;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.card.SHCardPayVO;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;

/**
 * ����˴��룬������Ӧ��Ejb��ɴ�ֵ����֧��
 * @deprecated
 * @author liangxinbiao
 */
public class SHCardPayTransfer implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 3
			&& (values[1] instanceof SHCardPayVO)
			&& ((values[2] instanceof String) || (values[2] == null))) {
			try {

                SHCardPayVO payVO = (SHCardPayVO) values[1];
				String host = (String) values[2];
				String result = null;

				result = pay(payVO,host);

				Object[] results = new Object[1];

				results[0] = result;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}
  private String pay(SHCardPayVO payVo,String host){
      String result = null;


        URL servlet;
        HttpURLConnection conn;
        try {
		servlet =new URL("http://"+ host+ "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
			result="���ӿ�����������,�����������";
            return result;
		}

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.SHCardPayServletCommand";
			params[1] = payVo;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (String) results[0];
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
  }
}
