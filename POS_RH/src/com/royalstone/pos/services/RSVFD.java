package com.royalstone.pos.services;

/**
 * �˿���ʾ����JNI����������,��JavaPos��Services�����
 * @author liangxinbiao
 */
public class RSVFD {
	
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
	 * ���Եĳ�ʼ��
	 * @param param
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int vfdInit(char[] param);
	
	/**
	 * ��ʾ�ַ���
	 * @param str �ַ�����Byte����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int vfdPrint(byte[] str);
	
	/**
	 * ����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int vfdClear();
	
	/**
	 * ��ʾ��ӭ����
	 * @param str ��ӭ�ַ�����Byte����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int vfdWelcome(byte[] str);
	
	/**
	 * ��ʾС��
	 * @param str С���ַ�����Byte����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int vfdPrintSubTotal(byte[] str);

	/**
	 * ��ʾ�ϼ�
	 * @param str �ϼ��ַ�����Byte����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int vfdPrintTotal(byte[] str);
	
	/**
	 * ��ʾ�һ�
	 * @param str �һ��ַ�����Byte����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int vfdPrintReturn(byte[] str);
	
	/**
	 * ��ʾ��Ʒ��
	 * @param str ��Ʒ���ַ�����Byte����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int vfdPrintGoods(byte[] str);
	
	/**
	 * ��ʾ���
	 * @param str ����ַ�����Byte����
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int vfdPrintAmtPr(byte[] str);

	/**
	 * ������,������
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		driverInit("RSVFD41_BA63G");
		//paramInit("COM3","9600","8","0","0",null,null,null,null,null);
		paramInit("COM6","9600","8","0","0",null,null,null,null,null);
		vfdInit(null);
		vfdClear();
		vfdPrint("88.88".getBytes());
          Thread.sleep(10000);
		vfdWelcome("��ӭ����!!".getBytes());
		 Thread.sleep(10000);
		vfdPrintReturn("����12.34".getBytes());
		   Thread.sleep(10000);
		vfdPrintTotal("-4444".getBytes());
		   Thread.sleep(10000);
		vfdPrintAmtPr("555.55".getBytes());
	       Thread.sleep(10000);
	}
}
