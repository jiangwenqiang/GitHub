package com.royalstone.pos.hardware;

import jpos.LineDisplay;

import com.royalstone.pos.common.Sale;
import com.royalstone.pos.util.Value;

/**
 * ����Ӳ����Ľӿ�
 * @author liangxinbiao
 */
public interface ICustDisplay {
	
	/**
	 * ��ʼ��
	 * @param c 
	 */
	public  void init(LineDisplay c);
	
	/**
	 * ��ʾ�ַ���
	 * @param value
	 */
	public  void printText(String value);
	
	/**
	 * ���� 
	 */
	public  void clear();
	
	/**
	 * ��ʾ��ӭ��Ϣ 
	 */
	public  void welcome();
	
	/**
	 * ��ʾС�� 
	 * @param value
	 */
	public  void printSubTotal(String value);
	
	/**
	 * ��ʾ�ϼ�
	 * @param value
	 */
	public  void printTotal(String value);
	
	/**
	 * ��ʾ�һ�
	 * @param value
	 */
	public  void printReturn(String value);
	
	/**
	 * ��ʾС��
	 * @deprecated
	 * @param value
	 */
	public  void printSubTotal(Value value);
	
	/**
	 * ��ʾ�ϼ�
	 * @deprecated
	 * @param value
	 */
	public  void printTotal(Value value);
	
	/**
	 * ��ʾ�һ�
	 * @deprecated
	 * @param value
	 */
	public  void printReturn(Value value);
	
	/**
	 * ��ʾ��������Ʒ
	 * @param s
	 */
	public  void printGoods(Sale s);
	
	/**
	 * ��ʾ���
	 * @param value
	 */
	public  void printAmtPr(String value);
	
	/**
	 * ��ʾ֧��
	 * @param value
	 */
	public  void printPayment(String value);
	
	/**
	 * ��ʾ֧��
	 * @deprecated 
	 * @param value
	 */
	public  void printPayment(Value value);

}
