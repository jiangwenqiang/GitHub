package com.royalstone.pos.services;

/**
 * ��ӡ����JNI����������,��JavaPos��Services�����
 * @author liangxinbiao
 */
public class RSPrinter {
	
	/**
	 * @param driverName ���������DLL������
	 */
	public static void driverInit(String driverName) {
		System.loadLibrary(driverName);
	}

	/**
	 * ��ʼ������
	 * @param portName �˿�����
	 * @param baudRate ������
	 * @param byteSize λ��
	 * @param parity   У��λ
	 * @param stopBits ֹͣλ
	 * @param value1   Ԥ��ֵ1
	 * @param value2   Ԥ��ֵ2
	 * @param value3   Ԥ��ֵ3
	 * @param value4   Ԥ��ֵ4
	 * @param value5   Ԥ��ֵ5
	 */
	public native static void paramInit(
		String portName,
		String baudRate,
		String byteSize,
		String parity,
		String stopBits,
		String value1,
		String value2,
		String value3,
		String value4,
		String value5);

	
	/**
	 * ��ӡ���ĳ�ʼ��
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int printInit();
	
	/**
	 * ��ӡ�ַ���
	 * @param str �ַ�����Byte����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int printStr(byte[] str);
	
	/**
	 * ��ӡ��״̬���
	 * @deprecated
	 */
	public native static int printChk();
	
	/**
	 * ��ֽ
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int printCut();
	
	/**
	 * ��ֽ
	 * @param feedCount ����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int printFeed144(int feedCount);
	
	/**
	 * ������,������
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		driverInit("RSPrinter_Wincor_ND210");
		paramInit("COM1","9600","8","0","0",null,null,null,null,null);
		printStr("This is a Test!!\n".getBytes());
		printStr("����һ������!!\n".getBytes("GB2312"));
		//printFeed144(10);
		//printCut();
	}
	
}
