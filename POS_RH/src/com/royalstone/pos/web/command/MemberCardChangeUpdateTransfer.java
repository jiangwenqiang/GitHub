/*
 * 创建日期 2008-3-16
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */



package com.royalstone.pos.web.command;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.card.CardChange;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;

/**
 * 服务端代码，调用相应的Ejb完成兑换卡
 * @deprecated
 * @author zhouzhou
 */
public class MemberCardChangeUpdateTransfer implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 3) {
			try {
				CardChange cardChange = (CardChange)values[1];
				String host = (String) values[2];
				CardChange result = null;
				Object[] results = new Object[1];
				
				result = pay(cardChange,host);
				
				results[0] = result;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}
  private CardChange pay(CardChange cardChange,String host){
  		CardChange result = null;
        URL servlet;
        HttpURLConnection conn;
        try {
		servlet =new URL("http://"+ host + "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setExecinfo("连接卡服务器出错,按清除键继续！");
            return result;
		}

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.MemberCardChangeUpdateServletCommand";
			params[1] = cardChange;
			

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (CardChange) results[0];
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
  }
}
