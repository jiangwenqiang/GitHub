package com.royalstone.pos.hardware;

/**
 * СƱ��ӡ��Ӳ����Ľӿ�
 * @author liangxinbiao
 */
public interface IPrinter {
	
	/**
	 * ��ӡһ����Ϣ
	 * @param value
	 */
	public abstract void println(String value);
	
	/**
	 * ��ֽ������
	 * @param line
	 */
	public abstract void feed(int line);
	
	/**
	 * ��ֽ 
	 */
	public abstract void cut();
	
}