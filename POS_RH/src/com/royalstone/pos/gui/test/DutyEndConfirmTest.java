/*
 * �������� 2004-6-20
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.gui.test;

import com.royalstone.pos.gui.DialogConfirm;

/**
 * @author liangxinbiao
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class DutyEndConfirmTest {

	public static void main(String[] args) {
		DialogConfirm dutyEndConfirm=new DialogConfirm();
		dutyEndConfirm.setMessage( "��������������ȷʵҪ�������" );
		dutyEndConfirm.show();
		
		System.out.println(dutyEndConfirm.isConfirm());
	}
}
