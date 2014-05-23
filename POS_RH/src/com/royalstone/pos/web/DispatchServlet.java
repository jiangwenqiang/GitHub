package com.royalstone.pos.web;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.royalstone.pos.invoke.MarshalledValue;
import com.royalstone.pos.web.command.ICommand;

/**
 * �����ݷ�Զ�̵��õ�Servlet(�����С����).
 * �ͻ��˵Ĳ�����װ��MarshalledValue���������л���ʽ͸��HTTPЭ�鴫����������DispatchServlet,
 * ��doPost()������,ȡ��MarshalledValue������Ĳ�������Object[],����ĵ�һ��������һ���ַ���
 * ����ֵ��һ��ʵ����ICommand�ӿڵ����ȫ��,DispatchServlet�÷���API�ڷ�����������һ������ʵ��,
 * Ȼ��,�ò����������ʵ����excute()������ɵ��õķַ�.
 * @author liangxinbiao
 */
public class DispatchServlet extends HttpServlet {

	private static String RESPONSE_CONTENT_TYPE =
		"application/x-java-serialized-object";

	public void doPost(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
		try {
			response.setContentType(RESPONSE_CONTENT_TYPE);
			ServletInputStream sis = request.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(sis);
			MarshalledValue mvI = (MarshalledValue) ois.readObject();
			ois.close();

			Object[] params = mvI.getValues();
			if (params != null
				&& params.length > 0
				&& (params[0] instanceof String)) {

				Class clazz = Class.forName((String) params[0]);
				ICommand command = (ICommand) clazz.newInstance();
				Object[] results = command.excute(params);

				ServletOutputStream sos = response.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(sos);

				MarshalledValue mvO = new MarshalledValue(results);
				oos.writeObject(mvO);
				oos.close();
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("<H2>adfdfdfd</H2>");
		String[] parms = request.getParameterValues("posid");
		String id = request.getParameter( "posid" );
		out.println( "Your ID:" + id );
		out.flush();
		out.close();
	}

}
