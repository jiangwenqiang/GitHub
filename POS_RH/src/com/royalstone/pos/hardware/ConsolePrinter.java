package com.royalstone.pos.hardware;

/**
 * �������ڿ���̨�������ģ���ӡ��
 * @author liangxinbiao
 */
public class ConsolePrinter implements IPrinter{

	/**
	 * @see com.royalstone.pos.hardware.IPrinter#println(java.lang.String)
	 */
	public void println(String value) {
		System.out.println(value);
	}

	/**
	 * @see com.royalstone.pos.hardware.IPrinter#feed(int)
	 */
	public void feed(int line) {
		for(int i=0;i<line;i++){
			System.out.println("");
		}
	}

	/**
	 * @see com.royalstone.pos.hardware.IPrinter#cut()
	 */
	public void cut() {
	}

}
