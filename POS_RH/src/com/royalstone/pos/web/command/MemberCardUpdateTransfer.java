package com.royalstone.pos.web.command;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.card.MemberCardUpdate;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;

/**
 * 服务端代码，调用相应的Ejb完成储值卡的支付
 * @deprecated
 * @author liangxinbiao
 */
public class MemberCardUpdateTransfer implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 3
			&& (values[1] instanceof MemberCardUpdate)
			&& ((values[2] instanceof String) || (values[2] == null))) {
			try {

                MemberCardUpdate updateVO = (MemberCardUpdate) values[1];
				String host = (String) values[2];
				String result = null;
				Object[] results = new Object[1];
				
				result = pay(updateVO,host);

//				Object[] results = new Object[1];

				results[0] = result;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}
  private String pay(MemberCardUpdate updateVo,String host){
      String result = null;


        URL servlet;
        HttpURLConnection conn;
        try {
		servlet =new URL("http://"+ host + "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
			result="连接卡服务器出错,按清除键继续！";
            return result;
		}

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.MemberCardUpdateServletCommand";
			params[1] = updateVo;

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
