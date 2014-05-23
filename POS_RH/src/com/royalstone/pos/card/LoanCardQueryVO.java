package com.royalstone.pos.card;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ���˿���ѯֵ�����Ǵӷ��������صĹ��˿���Ϣ
 * @author liangxinbiao
 */
public class LoanCardQueryVO implements Serializable {

	private String cardNo;
	private String subcardNo;
	private String detail;
	private ArrayList shopIDs;
	private ArrayList deptIDs;
	private String maxOilQtyPerTrans;
	private String oilGoodsID;
	private String custName;
	private String carID;
	private String cardType;
	private String credit;
	private String mainBalance;
	private ArrayList discs;

	private String exceptioninfo;

	public LoanCardQueryVO() {
		
	}
	
	
	/**
	 * @return ���
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * 
	 * @param detail ���
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return ������Ϣ
	 */
	public String getExceptioninfo() {
		return exceptioninfo;
	}
	
	/**
	 * @param exceptioninfo ������Ϣ
	 */
	public void setExceptioninfo(String exceptioninfo) {
		this.exceptioninfo = exceptioninfo;
	}

	/**
	 * @return ����
	 */
	public String getCardNo() {
		return cardNo;
	}

	/**
	 * @return ��������ƷС����б�
	 */
	public ArrayList getDeptIDs() {
		return deptIDs;
	}


	/**
	 * @return �����ѵ�ĵ���б�
	 */
	public ArrayList getShopIDs() {
		return shopIDs;
	}

	/**
	 * @return �ӿ���
	 */
	public String getSubcardNo() {
		return subcardNo;
	}

	/**
	 * @param string �ӿ���
	 */
	public void setCardNo(String string) {
		cardNo = string;
	}

	/**
	 * @param list ��������ƷС����б�
	 */
	public void setDeptIDs(ArrayList list) {
		deptIDs = list;
	}


	/**
	 * @param list �����ѵ�ĵ���б�
	 */
	public void setShopIDs(ArrayList list) {
		shopIDs = list;
	}

	/**
	 * @param string �ӿ���
	 */
	public void setSubcardNo(String string) {
		subcardNo = string;
	}

	/**
	 * @return һ�����������Ʒ��
	 */
	public String getMaxOilQtyPerTrans() {
		return maxOilQtyPerTrans;
	}

	/**
	 * @return �ο���Ʒ����Ʒ����
	 */
	public String getOilGoodsID() {
		return oilGoodsID;
	}

	/**
	 * @param string һ�����������Ʒ��
	 */
	public void setMaxOilQtyPerTrans(String string) {
		maxOilQtyPerTrans = string;
	}

	/**
	 * @param string �ο���Ʒ����Ʒ����
	 */
	public void setOilGoodsID(String string) {
		oilGoodsID = string;
	}

	/**
	 * @return ���ƺ�
	 */
	public String getCarID() {
		return carID;
	}

	/**
	 * @return �ͻ�����
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * @param string ���ƺ�
	 */
	public void setCarID(String string) {
		carID = string;
	}

	/**
	 * @param string �ͻ�����
	 */
	public void setCustName(String string) {
		custName = string;
	}

	/**
	 * @return ������
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * @param string ������
	 */
	public void setCardType(String string) {
		cardType = string;
	}

	/**
	 * @return ���ö��
	 */
	public String getCredit() {
		return credit;
	}

	/**
	 * @param string ���ö��
	 */
	public void setCredit(String string) {
		credit = string;
	}

	/**
	 * @return �������
	 */
	public String getMainBalance() {
		return mainBalance;
	}

	/**
	 * @param string �������
	 */
	public void setMainBalance(String string) {
		mainBalance = string;
	}

	/**
	 * @return
	 */
	public ArrayList getDiscs() {
		return discs;
	}

	/**
	 * @param list
	 */
	public void setDiscs(ArrayList list) {
		discs = list;
	}

}
