package com.royalstone.pos.invoke;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * �ͻ��˵��÷�������������
 * @author liangxinbiao
 */

public class HttpInvoker {

	private static final String REQUEST_CONTENT_TYPE =
		"application/x-java-serialized-object";

	/**
	 * ����Զ�̷����ķ��������ù��������������쳣������
	 * @param conn ����������HTTP���� 
	 * @param mvI ���ݵ�����������MarshalledValue��װ�Ĳ���
	 * @return ���������صľ���MarshalledValue��װ��ֵ
	 */
	
	/**
	 * ���룺�������� MarshalledValue ����
	 * �����������
	 * ���ܣ��ͻ��˵��÷���������ʶ����
	 */
	public static MarshalledValue invoke(
		HttpURLConnection conn,
		MarshalledValue mvI) throws IOException{

		MarshalledValue result = null;

		try {
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("ContentType", REQUEST_CONTENT_TYPE);
			conn.setRequestMethod("POST");

			OutputStream os = conn.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);

			oos.writeObject(mvI);
			oos.flush();

			InputStream is = conn.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			result = (MarshalledValue) ois.readObject();

			ois.close();
			oos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
            throw(ex);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
            throw new IOException(ex.getMessage());
		}

		return result;
	}

	/**
	 * ����Զ�̷����ķ���,�����ù�������������IO�쳣���׳������ϼ�����������
	 * @param conn ����������HTTP����
	 * @param mvI ���ݵ�����������MarshalledValue��װ�Ĳ���
	 * @return ���������صľ���MarshalledValue��װ��ֵ
	 * @throws IOException ���ù�������������IO�쳣
	 */
	public static MarshalledValue invokeWithException(
		HttpURLConnection conn,
		MarshalledValue mvI)
		throws IOException {

		MarshalledValue result = null;

		try {
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("ContentType", REQUEST_CONTENT_TYPE);
			conn.setRequestMethod("POST");

			OutputStream os = conn.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);

			oos.writeObject(mvI);
			oos.flush();

			InputStream is = conn.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			result = (MarshalledValue) ois.readObject();

			ois.close();
			oos.close();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return result;
	}

}
