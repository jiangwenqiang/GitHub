/*
 * �������� 2008-4-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.royalstone.pos.card;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 * ��Ա���һ���Ϣ��
 */
public class CardChange implements Serializable{
	private String cardno="";	//����
	private String shopid="";	//���
	private String posid="";	//POS����
	private String listno="";	//��ˮ��
	private String payvalue="";//��Ʒ�ܽ��
	private String cardPoint="";	//�ۼ�����
	private String execinfo="";//������Ϣ
	private String flag="0";	//��־λ 0 �����ѯ�� 1 ����ۼ�����
	private String plan="";	//�������
	private String infoFlag="2";	// �洢���̷��ر�־

	public CardChange() {
	}
	
	
	public CardChange(String cardno,String shopid,String posid,String listno,String payvalue,String plan,String flag){
		this.cardno = cardno;
		this.shopid = shopid;
		this.listno = listno;
		this.payvalue = payvalue;
		this.plan = plan;
		this.flag = flag;
		this.posid = posid;
		}
	
	/**
	 * @return �洢���̷��ر�־
	 */
	public String getInfoFlag() {
		return infoFlag;
	}

	/**
	 * @param �洢���̷��ر�־
	 */
	public void setInfoFlag(String infoFlag) {
		this.infoFlag = infoFlag;
	}
	
	/**
	 * @return �������
	 */
	public String getPlan() {
		return plan;
	}

	/**
	 * @param plan �������
	 */
	public void setPlan(String plan) {
		this.plan = plan;
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
	 * @return ��ˮ��
	 */
	public String getListno() {
		return listno;
	}

	/**
	 * @param cashierid ��ˮ��
	 */
	public void setListno(String listno) {
		this.listno = listno;
	}

	/**
	 * @return ��Ʒ�ܽ��
	 */
	public String getPayvalue() {
		return payvalue;
	}

	/**
	 * @param payvalue ��Ʒ�ܽ��
	 */
	public void setPayvalue(String payvalue) {
		this.payvalue = payvalue;
	}

	/**
	 * @return �ۼ��Ļ���
	 */
	public String getCardPoint() {
		return cardPoint;
	}

	/**
	 * @param �ۼ��Ļ���
	 */
	public void setCardPoint(String cardPoint) {
		this.cardPoint = cardPoint;
	}

	/**
	 * @return ������Ϣ
	 */
	public String getExecinfo() {
		return execinfo;
	}

	/**
	 * @param ������Ϣ
	 */
	public void setExecinfo(String execinfo) {
		this.execinfo = execinfo;
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
	 * @return ��־λ
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param ��־λ
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

}
