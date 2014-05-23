/*
 * Created on 2004-6-24
 */
package com.royalstone.pos.util;

/**
 * ���ڴ����û��˳�����.
 * @author Mengluoyi
 *
 */
public class UserExitException extends Exception 
{

	public UserExitException() {
		super();
	}

	/**
	 * @param message	˵����Ϣ.
	 */
	public UserExitException(String message) {
		super(message);
	}

	/**
	 * @param cause		�˳�ԭ�����.
	 */
	public UserExitException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message	˵����Ϣ.
	 * @param cause		�˳�ԭ�����.
	 */
	public UserExitException(String message, Throwable cause) {
		super(message, cause);
	}
}
