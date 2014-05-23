package com.royalstone.pos.services;

/**
 * Ǯ���JNI����������,��JavaPos��Services�����
 * @author liangxinbiao
 */
public class RSCashDrawer {

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
	 * Ǯ��ĳ�ʼ��
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ��
	 */
	public native static int drawerInit();
	
	/**
	 * ȡ��Ǯ���״̬
	 * @return ״̬ 0 �ر� 1 �� 
	 */
	public native static int drawerChk();
	
	/**
	 * ��Ǯ��
	 * @return �Ƿ�ɹ� 0 �ɹ� ���� ʧ�� 
	 */
	public native static int drawerOpen();
	
	/**
	 * @deprecated 
	 */
	public native static int drawerClose();

	/**
	 * ������,������
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("length="+args.length);
//		driverInit("RSCashDrawr41_Wincor");
//		if(args.length>0){
//			paramInit("64", null, null, null, null, null, null, null, null, null);
//		}else{
//			paramInit("128", null, null, null, null, null, null, null, null, null);
//		}
		driverInit("drv/NCRCashDrawer");
		//paramInit("COM1","9600","8","0","0",null,null,null,null,null);

		drawerInit();
		System.out.println("Before Open....");
		showCashDrawerStatus();
		drawerOpen();
		Thread.sleep(1000);
		System.out.println("After Open....");
		showCashDrawerStatus();
		drawerClose();
	}

	/**
	 * ��ʾǮ���״̬,������ 
	 */
	public static void showCashDrawerStatus() {
		if (drawerChk() == 1) {
			System.out.println("CashDrawer Open");
		} else {
			System.out.println("CashDrawer Close");
		}

	}
}
