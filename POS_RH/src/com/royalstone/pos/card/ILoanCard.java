package com.royalstone.pos.card;

/**
 * ���˿��ӿ�
 * @author liangxinbiao
 */
public interface ILoanCard {
	
	/**
	 * ���˿���ѯ 
	 * @param cardNo
	 * @param secrety
	 * @return
	 */
	public abstract LoanCardQueryVO query(String cardNo, String secrety);
	
	/**
	 * ���˿�֧��
	 * @param cp
	 * @return
	 */
	public abstract String pay(LoanCardPayVO cp);
	
	/**
	 * ���˿��Զ�����
	 * @param cp
	 * @return
	 */
	public abstract String autoRever(LoanCardPayVO cp);
}