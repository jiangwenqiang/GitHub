
package com.royalstone.pos.gui;

/**
 * ����ȷ�Ͻӿ�
 * @deprecated
 * @author Liangxinbiao
 */
public interface CardConfirmUI {
	public abstract void setCardNo(String value);
	public abstract void setTenderAmount(String value);
	public abstract void setCardAmount(String value);
	public abstract void setBalance(String value);
	/**
	 * @return
	 */
	public abstract boolean confirm();
}