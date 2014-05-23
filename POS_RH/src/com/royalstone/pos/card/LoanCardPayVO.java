package com.royalstone.pos.card;

import java.io.Serializable;

import org.jdom.Element;

/**
 * ���ʿ�֧����ֵ����Ҫ������������������Ҫ����֧���Ĺ��˿���һЩ��Ϣ����Ҫ֧���Ľ��ȡ�
 * @author liangxinbiao
 */
public class LoanCardPayVO implements Serializable {
	private String cardno="";
	private String subcardno="";
	private String shopid="";
	private String cashierid="";
	private String time="";
	private String payvalue="";
	private String password="";
	private String cdseq="";
	private String posid="";
	private String sheetid="";

	public LoanCardPayVO() {
	}

	/**
	 * @return ������
	 */
	public String getCardno() {
		return cardno;
	}

	/**
	 * @param cardno ������
	 */
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	/**
	 * @return ���
	 */
	public String getShopid() {
		return shopid;
	}

	/**
	 * @param shopid ���
	 */
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	/**
	 * @return ����Ա��
	 */
	public String getCashierid() {
		return cashierid;
	}

	/**
	 * @param cashierid ����Ա��
	 */
	public void setCashierid(String cashierid) {
		this.cashierid = cashierid;
	}

	/**
	 * @return ����֧��ʱ��
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time ����֧��ʱ��
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return ����֧�����
	 */
	public String getPayvalue() {
		return payvalue;
	}

	/**
	 * @param payvalue ����֧�����
	 */
	public void setPayvalue(String payvalue) {
		this.payvalue = payvalue;
	}

	/**
	 * @return ���˿�����
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password ���˿�����
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return ����˳���
	 */
	public String getCdseq() {
		return cdseq;
	}

	/**
	 * @param cdseq ����˳���
	 */
	public void setCdseq(String cdseq) {
		this.cdseq = cdseq;
	}

	/**
	 * @return POS����
	 */
	public String getPosid() {
		return posid;
	}
	
	
	/**
	 * @param posid POS����
	 */
	public void setPosid(String posid) {
		this.posid = posid;
	}
	
	/**
	 * @return ���ݺ�
	 */
	public String getSheetid(){
		return sheetid;
	}
	
	/**
	 * @param sheetid ���ݺ� 
	 */
	public void setSheetid(String sheetid){
		this.sheetid = sheetid;
	}

	/**
	 * @return �ӿ���
	 */
	public String getSubcardno() {
		return subcardno;
	}

	/**
	 * @param string �ӿ���
	 */
	public void setSubcardno(String string) {
		subcardno = string;
	}

	/**
	 * @return ����˶����XML�Ķ���
	 */
	public Element toElement() {
		Element g = new Element("CardPayVO");
		g.addContent(new Element("cardno").addContent(cardno));
		g.addContent(new Element("subcardno").addContent(subcardno));
		g.addContent(new Element("shopid").addContent(shopid));
		g.addContent(new Element("cashierid").addContent(cashierid));
		g.addContent(new Element("time").addContent(time));
		g.addContent(new Element("payvalue").addContent(payvalue));
		g.addContent(new Element("password").addContent(password));
		g.addContent(new Element("cdseq").addContent(cdseq));
		g.addContent(new Element("posid").addContent(posid));
		g.addContent(new Element("sheetid").addContent(sheetid));

		return g;
	}

	/**
	 * @param elm ��XML��������LoanCardPayVO
	 */
	public LoanCardPayVO(Element elm) {
		try {
			cardno = elm.getChildTextTrim("cardno");
			subcardno = elm.getChildTextTrim("subcardno");
			shopid = elm.getChildTextTrim("shopid");
			cashierid = elm.getChildTextTrim("cashierid");
			time = elm.getChildTextTrim("time");
			payvalue = elm.getChildTextTrim("payvalue");
			password = elm.getChildTextTrim("password");
			cdseq = elm.getChildTextTrim("cdseq");
			posid = elm.getChildTextTrim("posid");
			sheetid = elm.getChildTextTrim("sheetid");

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

}
