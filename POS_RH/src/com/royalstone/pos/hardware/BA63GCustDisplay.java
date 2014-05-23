package com.royalstone.pos.hardware;

import jpos.JposException;
import jpos.LineDisplay;

import com.royalstone.pos.common.Sale;
import com.royalstone.pos.util.Value;

/**
 * BA63G��������Ӳ���㣨��JavaPos��׼��Control�ϵĲ㣩������ʾ����
 * @author liangxinbiao
 */
public class BA63GCustDisplay implements ICustDisplay {

	private LineDisplay control;

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#init()
	 */
	public void init(LineDisplay c) {
		this.control = c;
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printText(java.lang.String)
	 */
	public void printText(String value) {
		try {
			control.displayText(value, 0);
		} catch (JposException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#clear()
	 */
	public void clear() {
		try {
			control.clearText();
		} catch (JposException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#welcome()
	 */
	public void welcome() {
		printText("��ӭ����");
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printSubTotal(java.lang.String)
	 */
	public void printSubTotal(String value) {
		printText("С�ƣ�" + value);
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printTotal(java.lang.String)
	 */
	public void printTotal(String value) {
		printText("�ϼƣ�" + value);
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printReturn(java.lang.String)
	 */
	public void printReturn(String value) {
		printText("�һأ�" + value);
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printGoods(java.lang.String)
	 */
	public void printGoods(Sale s) {
		printText(
			s.getName()
				+ " "
				+ (new Value(s.getStdPrice())).toString()
				+ "x"
				+ s.getQtyStr());
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printAmtPr(java.lang.String)
	 */
	public void printAmtPr(String value) {
		printText("��" + value);
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printPayment(java.lang.String)
	 */
	public void printPayment(String value) {
		printText("�տ" + value);

	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printSubTotal(com.royalstone.pos.util.Value)
	 */
	public void printSubTotal(Value value) {
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printTotal(com.royalstone.pos.util.Value)
	 */
	public void printTotal(Value value) {
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printReturn(com.royalstone.pos.util.Value)
	 */
	public void printReturn(Value value) {
	}

	/**
	 * @see com.royalstone.pos.hardware.ICustDisplay#printPayment(com.royalstone.pos.util.Value)
	 */
	public void printPayment(Value value) {
	}

}
