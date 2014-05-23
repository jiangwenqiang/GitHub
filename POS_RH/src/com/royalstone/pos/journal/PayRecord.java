/*
 * Created on 2004-6-4
 *
 */
package com.royalstone.pos.journal;

import java.io.Serializable;

import org.jdom.Element;

import com.royalstone.pos.util.Day;
import com.royalstone.pos.util.PosTime;

/**
 * PayRecord ����֧����¼,����ǰ̨ģ�����̨������ˮ.
 * @author Mengluoyi
 *
 */
public class PayRecord implements Serializable {

	public static void main(String[] args) {
	}

	/**
	 * @param elm			���֧����Ϣ��XML�ڵ�.
	 * @param onlineflag	������־.
	 */
	public PayRecord(Element elm, String onlineflag) {
		trainflag = elm.getChild("trainflag").getTextTrim();
		reason = elm.getChild("reason").getTextTrim();
		type = elm.getChild("type").getTextTrim();
		currency = elm.getChild("currency").getTextTrim();
		value = atoi(elm.getChild("value").getTextTrim());
		value_equiv = atoi(elm.getChild("value_equiv").getTextTrim());
		cardno = elm.getChild("cardno").getTextTrim();
		sysdate = str2day(elm.getChild("sysdate").getTextTrim());
		systime = str2time(elm.getChild("systime").getTextTrim());
		this.onlineflag = onlineflag;

	}

	/**	�÷������ڽ�XML�е������ִ�(yyyy-mm-dd)ת����Day ����.
	 * @param sysdate	�������ڵ��ִ�. ��: 2004-09-28.
	 * @return	����.
	 */
	private static Day str2day(String sysdate) {
		return new Day(
			atoi(sysdate.substring(0, 4)),
			atoi(sysdate.substring(5, 7)),
			atoi(sysdate.substring(8, 10)));
	}

	/**	�÷������ڽ�XML�е�ʱ���ִ�(hh:mm:ss)ת����PosTime ����.
	 * @param systime	����ʱ����ִ�. ��:12:00:00.
	 * @return	PosTime ʱ��.
	 */
	private static PosTime str2time(String systime) {
		return new PosTime(
			atoi(systime.substring(0, 2)),
			atoi(systime.substring(3, 5)),
			atoi(systime.substring(6, 8)));
	}

	/**
	 * @return	��ѵ��־.
	 */
	public String trainflag() {
		return trainflag;
	}
	
	/**
	 * @return	֧��ԭ��(��֧ͨ��,����).
	 */
	public String reason() {
		return reason;
	}
	
	/**
	 * @return	֧����ʽ(�ֽ�,�����,��ȯ).
	 */
	public String type() {
		return type;
	}
	/**
	 * @return	֧������.
	 */
	public String currency() {
		return currency;
	}
	/**
	 * @return	����.
	 */
	public String cardno() {
		return cardno;
	}
	/**
	 * @return	֧�����(��֧�����ұ�ʾ).
	 */
	public int value() {
		return value;
	}
	/**
	 * @return	��Ч֧�����(��λΪ����ҷ�).
	 */
	public int value_equiv() {
		return value_equiv;
	}
	/**
	 * @return	֧������ʱ��ϵͳ����.
	 */
	public Day sysdate() {
		return sysdate;
	}
	/**
	 * @return	֧������ʱ��ϵͳʱ��.
	 */
	public PosTime systime() {
		return systime;
	}
	
	/**	
	 * @param s	�ִ�תΪ������.
	 * @return	�ִ�s ��������ֵ.
	 */
	private static int atoi(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw e;
		}
	}

	/**
	 * <code>trainflag</code>	"��ѵ״̬"��־
	 */
	private String trainflag = "0";
	/**
	 * <code>reason</code>	֧��ԭ��
	 */
	private String reason;
	/**
	 * <code>type</code>	֧����ʽ
	 */
	private String type;
	/**
	 * <code>currency</code>	֧�����ִ���
	 */
	private String currency;
	/**
	 * <code>cardno</code>	����
	 */
	private String cardno;
	/**
	 * <code>value</code>	֧�����(��֧�����������ʾ,��λΪ��)
	 */
	private int value;
	/**
	 * <code>value_equiv</code>	��Ч֧�����(������ұ�ʾ,��λΪ��)
	 */
	private int value_equiv;
	/**
	 * <code>sysdate</code>	ϵͳ����
	 */
	private Day sysdate;
	/**
	 * <code>systime</code>	ϵͳʱ��
	 */
	private PosTime systime;
	/**
	 * <code>onlineflag</code>	������־
	 */
	private String onlineflag;
}

