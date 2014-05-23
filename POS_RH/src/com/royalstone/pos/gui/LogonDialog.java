/*
 * �������� 2004-5-30
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.OutputStream;

import javax.swing.JFrame;

/**
 * ��½����
 * @author liangxinbiao
 */
public class LogonDialog extends JFrame {

	private LogonPanel logonPanel;

	/**
	 * 
	 * @param output
	 */
	public LogonDialog(OutputStream output) {
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		logonPanel = new LogonPanel(output);
		this.getContentPane().add(logonPanel, BorderLayout.CENTER);
			
		this.setUndecorated(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		this.addWindowFocusListener(new WindowFocusListener(){

			public void windowGainedFocus(WindowEvent e) {
				logonPanel.focusComponent();
			}

			public void windowLostFocus(WindowEvent e) {
								
			}});

	}	
	
	/**
	 * 
	 * @return ��½���
	 */
	public LogonPanel getLogonPanel(){
		return logonPanel;
	}
	
	
	
}
