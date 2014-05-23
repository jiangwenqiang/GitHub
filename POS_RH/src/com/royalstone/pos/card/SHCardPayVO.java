package com.royalstone.pos.card;

import java.io.Serializable;

import org.jdom.Element;


/**
 * ��ֵ��֧����ֵ����Ҫ������������������Ҫ����֧���Ĵ�ֵ����һЩ��Ϣ����Ҫ֧���Ľ��ȡ�

 * @author liangxinbiao
 */
public class SHCardPayVO implements Serializable {

	private String cardno;
	private String shopid;
	private String cashierid;
	private String time;
	private String payvalue;
	private String password;
	private String cdseq;
	private String posid;
	private String AccFlag;  //��չ�˻�  1 �ٻ��� 
	private String Payflag;  //���ѱ�־ 

	public SHCardPayVO() {
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	
	public String getAccFlag(){
		return AccFlag;
		}
	
	public void setAccFlag(String accflag){
		this.AccFlag=accflag;
		}
	
	public String getpayFlag(){
		return Payflag;
		}
	
	public void setpayFlag(String payflag){
		this.Payflag=payflag;
		}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	public String getCashierid() {
		return cashierid;
	}

	public void setCashierid(String cashierid) {
		this.cashierid = cashierid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPayvalue() {
		return payvalue;
	}

	public void setPayvalue(String payvalue) {
		this.payvalue = payvalue;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCdseq() {
		return cdseq;
	}

	public void setCdseq(String cdseq) {
		this.cdseq = cdseq;
	}

	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

    public Element toElement() {
		Element g = new Element("SHCardPayVO");
		g.addContent(new Element("cardno").addContent(cardno));
		g.addContent(new Element("shopid").addContent(shopid));
		g.addContent(new Element("cashierid").addContent(cashierid));
		g.addContent(new Element("time").addContent(time));
		g.addContent(new Element("payvalue").addContent(payvalue));
		g.addContent(new Element("password").addContent(password));
		g.addContent(new Element("cdseq").addContent(cdseq));
		g.addContent(new Element("posid").addContent(posid));
  	    return g;
	}

    	/**
	 * @param elm ��XML��������LoanCardPayVO
	 */
	public SHCardPayVO(Element elm) {
		try {
			cardno = elm.getChildTextTrim("cardno");
			shopid = elm.getChildTextTrim("shopid");
			cashierid = elm.getChildTextTrim("cashierid");
			time = elm.getChildTextTrim("time");
			payvalue = elm.getChildTextTrim("payvalue");
			password = elm.getChildTextTrim("password");
			cdseq = elm.getChildTextTrim("cdseq");
			posid = elm.getChildTextTrim("posid");
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}


}
