package com.royalstone.pos.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ��װ���й����ڡ����ֵĸ�ʽ���ķ�����ʵ����
 * @author liangxinbiao
 */
public class Formatter {
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
	private static SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfDateFile=new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * ���������ֵ��ַ�����ʽ��Ϊ����λС�����ַ���
	 * @param s Ҫ��ʽ�����ַ���
	 * @return ��ʽ������ַ���
	 */
	public static String toMoney(String s) {

		Double val = new Double(s);

		java.text.DecimalFormat format =
			(java.text.DecimalFormat) java
				.text
				.NumberFormat
				.getCurrencyInstance();
				
		format.applyPattern("#####0.00");

		return (format.format(val.doubleValue()));
	}

	/**
	 * �����ڸ�ʽ��Ϊ�ַ����� ��λ��/��λ��/��λ�� ��λСʱ:��λ�� )
	 * @param date Ҫ��ʽ��������
	 * @return ��ʽ������ַ���
	 */
	public static String getLongDate(Date date) {
		return date!=null?sdf.format(date):"";
	}

	/**
	 * �����ڸ�ʽ��Ϊ�ַ����� ��λ����λ����λ����λСʱ��λ����λ�� )
	 * @param date Ҫ��ʽ��������
	 * @return ��ʽ������ַ���
	 */
	public static String getDateFile(Date date) {
		return date!=null?sdfDateFile.format(date):"";
	}

	/**
	 * �����ڸ�ʽ��Ϊ�ַ����� ��λСʱ:��λ��:��λ�� )
	 * @param date Ҫ��ʽ��������
	 * @return ��ʽ������ַ���
	 */
	public static String getTime(Date date) {
		return date !=null ? sdfTime.format(date):"";
	}
	
	/**
	 * �����ڸ�ʽ��Ϊ�ַ����� ��λ��-��λ��-��λ�� )
	 * @param date Ҫ��ʽ��������
	 * @return ��ʽ������ַ���
	 */
	public static String getDate(Date date){
		return date!=null?sdfDate.format(date):"";
	}
	
	/**
	 * ȥ���ַ�����ǰ��ո� 
	 * @param value Ҫ������ַ���
	 * @return �������ַ���
	 */
	public static String mytrim(String value) {
		String result = value;
		if (value != null) {
			result = value.trim();
		}
		return result;
	}

}
