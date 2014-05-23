package com.royalstone.pos.invoke;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.util.Response;

/*
 * �������� 2004-5-24
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */

/**
 * @author Mengluoyi
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class CommandInvoker 
{
	public CommandInvoker( String host, int port )
	{
		this.host = host;
		this.port = port;
	}

	public Response invoke( String command, Object obj ) 
	{
		Response result;

		try {
			URL servlet;
			HttpURLConnection conn;

			servlet = new URL ( "http", host, port, DISPATCHER );
			conn = (HttpURLConnection) servlet.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("ContentType", REQUEST_CONTENT_TYPE );
			conn.setRequestMethod("POST");

			Object[] params = new Object[2];
			params[0] = command;
			params[1] = obj;
			MarshalledValue mvI = new MarshalledValue(params);

			ObjectOutputStream oos = new ObjectOutputStream(conn.getOutputStream());
			oos.writeObject(mvI);
			oos.flush();

			ObjectInputStream  ois = new ObjectInputStream(conn.getInputStream());

			MarshalledValue mvO = (MarshalledValue) ois.readObject();
			if( mvO != null ){
				Object[] results = mvO.getValues();
				if (results != null && results.length > 0) result = (Response) results[0];
				else result = new Response( -1, "ԭ�����Ĵ���");
			} else {
				result = new Response( -1, "�����������ϵʧ��");
			}
			
			ois.close();
			oos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = new Response( -1, "���̨��������ϵʧ�ܣ�" );
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			result = new Response( -1, "���̨��������ϵʧ�ܣ�" );
		} 

		return result;
	}
	
	private static final String DISPATCHER = "/pos41/DispatchServlet";
	private static final String REQUEST_CONTENT_TYPE = "application/x-java-serialized-object";

	private String host;
	private int port;
}
