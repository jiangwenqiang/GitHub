package com.royalstone.pos.card;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.PosConfig;

/**
 * 挂账卡实现，它负责和后台服务器通讯，实现挂账卡的查询、支付等功能

 * @author liangxinbiao
 */
public class SHCard implements IShoppingCard {

	private URL servlet;
	private HttpURLConnection conn;

	public SHCard() {
		try {
			servlet =
				new URL(
					"http://"
						+ pos.core.getPosContext().getServerip()
						+ ":"
						+ pos.core.getPosContext().getPort()
						+ "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public SHCard(URL servlet) {
		this.servlet = servlet;
	}

	public SHCardQueryVO query(String cardNo, String secrety) {

		SHCardQueryVO result = null;
		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[4];

			params[0] = "com.royalstone.pos.web.command.SHCardQueryTransfer";
			params[1] = cardNo;
			params[2] = secrety;
			params[3] = PosConfig.getInstance().getString("CARDHOST_IP");
			

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (SHCardQueryVO) results[0];
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}
	
	//储值卡充值
	public String RaPay(SHCardPayVO cp) {

		String result = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] = "com.royalstone.pos.web.command.SHCardPayRaTransfer";
			params[1] = cp;
			params[2] = PosConfig.getInstance().getString("CARDHOST_IP");

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

	public String pay(SHCardPayVO cp) {

		String result = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] = "com.royalstone.pos.web.command.SHCardPayTransfer";
			params[1] = cp;
			params[2] = PosConfig.getInstance().getString("CARDHOST_IP");

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
	
    public String autoRever(SHCardPayVO cp) {

		String result = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] = "com.royalstone.pos.web.command.SHCardAutoReverTransfer";
			params[1] = cp;
			params[2] = PosConfig.getInstance().getString("CARDHOST_IP");

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
