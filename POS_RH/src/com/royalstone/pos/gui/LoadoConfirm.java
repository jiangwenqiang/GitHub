/*
 *POS Version 4 Product
 *��ͨϵͳ�������޹�˾
 *@author HuangXuean
 *@version 1.0
 */
package com.royalstone.pos.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.royalstone.pos.shell.pos;

/**
 * �޸ĵذ����ȷ�϶Ի��� 
 */
public class LoadoConfirm extends JDialog {
	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JPanel jPanel4 = new JPanel();
	JPanel jPanel5 = new JPanel();
	JPanel jPanel6 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JButton btnCancel = new JButton();
	JButton btnEnter = new JButton();
	JLabel lblMessage = new JLabel();
	private boolean isConfirm = false;

	public LoadoConfirm() {
		super(pos.posFrame);
		try {
			jbInit();
			setSize(350, 160);
			this.setModal(true);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(
				(int) ((screenSize.getWidth() - 350) / 2),
				(int) ((screenSize.getHeight() - 160) / 2));

			addKeyAndContainerListenerRecursively(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * JBuilder�Զ����ɵĳ�ʼ�����淽��
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		jPanel1.setLayout(borderLayout1);
		jPanel6.setPreferredSize(new Dimension(10, 30));
		jPanel5.setPreferredSize(new Dimension(30, 10));
		jPanel3.setPreferredSize(new Dimension(30, 10));
		jPanel2.setPreferredSize(new Dimension(10, 50));
		btnCancel.setFont(new java.awt.Font("Dialog", 0, 13));
		btnCancel.setText("ȡ    ��");
		btnCancel.addActionListener(
			new DdutyEndConfirm_btnCancel_actionAdapter(this));
		btnEnter.setFont(new java.awt.Font("Dialog", 0, 13));
		btnEnter.setText("ȷ    ��");
		btnEnter.addActionListener(
			new DdutyEndConfirm_btnEnter_actionAdapter(this));
		lblMessage.setFont(new java.awt.Font("Dialog", 0, 16));
		lblMessage.setRequestFocusEnabled(true);
		lblMessage.setText("�Ƿ��޸ĵذ���");
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(jPanel6, BorderLayout.NORTH);
		jPanel1.add(jPanel5, BorderLayout.WEST);
		jPanel1.add(jPanel4, BorderLayout.CENTER);
		jPanel4.add(lblMessage, null);
		jPanel1.add(jPanel3, BorderLayout.EAST);
		jPanel1.add(jPanel2, BorderLayout.SOUTH);
		jPanel2.add(btnEnter, null);
		jPanel2.add(btnCancel, null);
	}

	private PosKeyboard keyListener = new PosKeyboard();
	
	/**
	 * 
	 * @param c
	 */
	private void addKeyAndContainerListenerRecursively(Component c) {
		c.removeKeyListener(keyListener);
		c.addKeyListener(keyListener);
		if (c instanceof Container) {
			Container cont = (Container) c;
			Component[] children = cont.getComponents();
			for (int i = 0; i < children.length; i++) {
				addKeyAndContainerListenerRecursively(children[i]);
			}
		}
	}

	/**
	 * 
	 * @param c
	 */
	private void removeKeyAndContainerListenerRecursively(Component c) {
		c.removeKeyListener(keyListener);
		if (c instanceof Container) {
			Container cont = (Container) c;
			Component[] children = cont.getComponents();
			for (int i = 0; i < children.length; i++) {
				removeKeyAndContainerListenerRecursively(children[i]);
			}
		}
	}

	/**
	 * ������̶���
	 */
	private class PosKeyboard implements KeyListener {

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
				case KeyEvent.VK_ESCAPE :
					isConfirm = false;
					dispose();
					break;
				case KeyEvent.VK_ENTER :
					if(btnEnter.hasFocus()){
						isConfirm = true;	
					}
					dispose();
					break;
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}

	/**
	 * 
	 * @return �����û��Ƿ���ȷ����
	 */
	public boolean isConfirm() {
		return isConfirm;
	}

	/**
	 * 
	 * @param e
	 */
	void btnEnter_actionPerformed(ActionEvent e) {
		isConfirm = true;
		dispose();
	}

	/**
	 * 
	 * @param e
	 */
	void btnCancel_actionPerformed(ActionEvent e) {
		isConfirm = false;
		dispose();
	}

	/**
	 * 
	 * @param value ��ʾ��Ϣ
	 */
	public void setMessage(String value) {
		lblMessage.setText(value);
	}

}

class DdutyEndConfirm_btnEnter_actionAdapter
	implements java.awt.event.ActionListener {
	LoadoConfirm adaptee;

	public DdutyEndConfirm_btnEnter_actionAdapter(LoadoConfirm adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.btnEnter_actionPerformed(e);
	}
}

class DdutyEndConfirm_btnCancel_actionAdapter
	implements java.awt.event.ActionListener {
		LoadoConfirm adaptee;

	public DdutyEndConfirm_btnCancel_actionAdapter(LoadoConfirm adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.btnCancel_actionPerformed(e);
	}
}

