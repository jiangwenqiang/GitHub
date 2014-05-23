package com.royalstone.pos.gui;

import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.common.Payment;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.common.SheetValue;

/*
 * �������� 2004-5-20
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */

/**
 * ������Ľӿڣ���PosDevOut���������������ʾ
 * @author liangxinbiao
 */
public interface MainUI {
	
	/**
	 * @param value ����Ա��
	 */
	public abstract void setCashier(String value);
	/**
	 * @param value ����ʱ��
	 */
	public abstract void setDatetime(String value);
	
	/**
	 * @param value ��������
	 */
	public abstract void setWorkDay(String value);

	/**
	 * @param value ��κ�
	 */
	public abstract void setDutyNo(String value);
	/**
	 * @param value �ҵ���
	 */
	public abstract void setHoldNo(String value);
	
	/**
	 * @param value ӪҵԱ��
	 */
	public abstract void setWaiterNo(String value);
	
	/**
	 * @param value ������
	 */
	public abstract void setInputField(String value);
	
	/**
	 * @param value ���տ�
	 */
	public abstract void setPaid(String value);
	
	/**
	 * @param value POS����
	 */
	public abstract void setPosNo(String value);
	
	/**
	 * @param value ��ʾ��Ϣ
	 */
	public abstract void setPrompt(String value);
	
	/**
	 * @param value �Ŵ��������Ʒ����
	 */
	public abstract void setSpCode(String value);
	
	/**
	 * @param value �Ŵ��������Ʒ����
	 */
	public abstract void setSpName(String value);
	
	/**
	 * @param value �Ŵ��������Ʒ����
	 */
	public abstract void setSpPrice(String value);
	
	/**
	 * @param value �Ŵ��������Ʒ����
	 */
	public abstract void setSpQuantity(String value);
	
	/**
	 * @param value Ӧ�տ�
	 */
	public abstract void setTotal(String value);
	
	/**
	 * @param value ���ݺ�
	 */
	public abstract void setTransNo(String value);
	
	/**
	 * @param value ���տ�
	 */
	public abstract void setUnPaid(String value);

	/**
	 * 
	 * @param s ���ۼ�¼
	 */
	public abstract void display(Sale s);

	/**
	 * 
	 * @param p ֧����¼
	 */
	public abstract void display(Payment p);

	/**
	 * ���������
	 */
	public abstract void clear();
	
	/**
	 * 
	 * @param value ����״̬���������ѻ���
	 */
	public abstract void setConnStatus(String value);
	
	/**
	 * 
	 * @param value ״̬�����ۡ��˻��ȣ�
	 */
	public abstract void setStatus(String value);
	
	/**
	 * 
	 * @param value 
	 */
	public abstract void setStep(int value);
	
	/**
	 * 
	 * @param value ���յ���ʾ�����ա��һأ�
	 */
	public abstract void setUnPaidLabel(String value);
	
	/**
	 * 
	 * @param s 
	 */
	public abstract void displayDiscount(Sale s);
	
	/**
	 * @param s
	 * @param sheet
	 */
	public abstract void displayDiscount4correct(Sale s, SheetValue sheet);

	/**
	 * ��ʾ�ϼ�
	 * @param v
	 * @return
	 */
	public abstract int disptotal(SheetValue v);
	
	/**
	 * ��ʾ�ۿ�
	 * @param s ���ۼ�¼�������ۿ���Ϣ
	 */
	public abstract void displayprom(Sale s);
	
	/**
	 * ��ʾ���˿���Ϣ
	 * @param memberCard ���˿���Ϣ
	 * @return
	 */
	public abstract int dispMemberCard(MemberCard memberCard);

    public  int dispCoupon(String couponNO);
    
    public int dispCollectivity(String collectivity);
}
