/*
 * �������� 2004-6-18
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.gui.test;

import com.royalstone.pos.gui.OilInput;

/**
 * @author liangxinbiao
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class OilInputTest {

	public static void main(String[] args) {
		OilInput oilInput=new OilInput(System.out);
		oilInput.show();
		//oilInput.setStep(0);
		oilInput.setStep(0);
		oilInput.setAmount("124");
		
	}
}
