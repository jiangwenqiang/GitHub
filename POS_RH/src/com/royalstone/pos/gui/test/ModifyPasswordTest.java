/*
 * �������� 2004-5-31
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.gui.test;

import com.royalstone.pos.gui.ModifyPassword;

/**
 * @author liangxinbiao
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class ModifyPasswordTest {

	public static void main(String[] args) {
		ModifyPassword modifyPassword=new ModifyPassword();
		modifyPassword.show();
		
		if(modifyPassword.isConfirm()){
			System.out.println(modifyPassword.getOldPassword());
			System.out.println(modifyPassword.getNewPassword());
			System.out.println(modifyPassword.getConfirmPassword());
		}else{
			System.out.println("Cancel");
		}
	}
}
