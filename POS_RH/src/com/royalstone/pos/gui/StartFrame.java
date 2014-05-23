/*
 * �������� 2004-6-14
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * ��������
 * @author liangxinbiao
 */
public class StartFrame extends JFrame {

	private StartPanel startPanel = new StartPanel();

	public StartFrame() {

		setSize(500, 300);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(
			(int) ((screenSize.getWidth() - 500) / 2),
			(int) ((screenSize.getHeight() - 300) / 2));
		this.getContentPane().add(startPanel, BorderLayout.CENTER);
		this.setUndecorated(true);

	}

}
