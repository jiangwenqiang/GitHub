package com.royalstone.pos.card;

/**
 * �����ฺ������ʵ����ILoanCard�ӿڵ���
 * @author liangxinbiao
 */
public class LoanCardFactory {

	private static ILoanCard instance = null;

	public static void setInstance(ILoanCard value) {
		instance = value;
	}

	public static ILoanCard createInstance() {
		if (instance == null) {
			instance = new LoanCard();
		}
		return instance;
	}

}
