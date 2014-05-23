package com.royalstone.pos.keymap;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.royalstone.pos.gui.test.KeyboardTest;

/**
 * �Բ��ɱ�̼���ʵ�м���ӳ��(�� A�� ӳ��� B��)
 * ��ʵ�����Ǹ����̹���,������׽�ײ�����м����¼�,
 * Ȼ��������ý�ԭ���ļ������뻻�����Ӧ�ļ�ֵ
 * @author liangxinbiao
 */
public class AllKeyEventListener {

	/**
	 * @param file �����ļ���
	 */
	
//	String Awt;
	
	public AllKeyEventListener(String file) {

		try {
//			this.Awt = file;
//			if (Awt == null)
//			{
//				System.out.println("Awt File = null");
//				}
			TheListener listener = new TheListener(file);
			Toolkit.getDefaultToolkit().addAWTEventListener(
				listener,
				AWTEvent.KEY_EVENT_MASK);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	/**
	 * ���̼����� 
	 * @author liangxinbiao
	 */
	private class TheListener implements AWTEventListener {

		WinKeyMap keyMap;

		public TheListener(String file) throws Exception {
			keyMap = new WinKeyMap(file);
		}

		public void eventDispatched(AWTEvent event) {
			if ((event instanceof KeyEvent)
				&& !(event instanceof TheKeyEvent)) {
				KeyEvent keyEvent = (KeyEvent) event;
				ArrayList maps = keyMap.getMap(keyEvent.getKeyCode());
				if (maps != null) {
					for (int i = 0; i < maps.size(); i++, i++) {
						keyEvent.consume();
						EventQueue queue =
							Toolkit.getDefaultToolkit().getSystemEventQueue();

						queue.postEvent(
							new TheKeyEvent(
								keyEvent.getComponent(),
								keyEvent.getID(),
								keyEvent.getWhen(),
								keyEvent.getModifiers(),
								((Integer) maps.get(i)).intValue(),
								((String) maps.get(i + 1)).charAt(0)));
					}
				}

			}
		}
	}

	/**
	 * ����ӳ���ļ����¼� 
	 * @author liangxinbiao
	 */
	private class TheKeyEvent extends KeyEvent {
		public TheKeyEvent(
			Component source,
			int id,
			long when,
			int modifiers,
			int keyCode,
			char keyChar) {
			super(source, id, when, modifiers, keyCode, keyChar);
		}
	}

	/**
	 * ������,������
	 * @param args
	 */
	public static void main(String[] args) {
		AllKeyEventListener allKeyEventListener =
			new AllKeyEventListener("winkeymap.xml");
		KeyboardTest keyboardTest = new KeyboardTest();
		keyboardTest.show();
	}
}
