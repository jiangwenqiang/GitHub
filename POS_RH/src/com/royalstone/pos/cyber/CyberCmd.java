/*
 * �������� 2004-5-25
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.cyber;

import java.sql.Connection;

import org.jdom.Element;

/**
 * @author liangxinbiao
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public interface CyberCmd {
	
	public Element excute(Element xinput);
	public void setDBConnection( Connection con);

}
