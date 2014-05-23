/*
 * �������� 2004-6-1
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.gui.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;


/**
 * @author liangxinbiao
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class KeyboardTest extends JFrame{
  JPanel jPanel1 = new JPanel();
  JTextArea txtContent = new JTextArea();
  BorderLayout borderLayout1 = new BorderLayout();

  public KeyboardTest() {
    try {
      jbInit();
	  addKeyAndContainerListenerRecursively(this);
	  this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	  this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	  this.setTitle("���̲���");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jPanel1.setLayout(borderLayout1);
    txtContent.setFont(new java.awt.Font("Dialog", 0, 20));
    txtContent.setEditable(false);
    txtContent.setText("");
    this.getContentPane().add(jPanel1,  BorderLayout.CENTER);
    jPanel1.add(txtContent,  BorderLayout.CENTER);
  }

  private PosKeyboard keyListener = new PosKeyboard();

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

  private class PosKeyboard implements KeyListener {

	  public void keyPressed(KeyEvent e) {
	  		txtContent.append("KeyCode="+e.getKeyCode()+"\n");
	  }

	  public void keyReleased(KeyEvent e) {
	  }

	  public void keyTyped(KeyEvent e) {
	  }
  }

  public static void main(String args[]){
		KeyboardTest keyboardTest=new KeyboardTest();
		keyboardTest.show();
  }

}
