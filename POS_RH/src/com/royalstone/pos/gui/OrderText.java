/*
 * �������� 2005-10-21
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.royalstone.pos.gui;

/**
 * @author zhouzhou
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.VerticalFlowLayout;
import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.common.Payment;
import com.royalstone.pos.common.PosFunction;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.common.SheetValue;
import com.royalstone.pos.io.PosInput;
import com.royalstone.pos.io.PosKeyMap;
import com.royalstone.pos.shell.pos;

/**
 * Ԥ�����Ի��� 
 * @author zhouzhou
 */
// ���հ���
public class OrderText extends JDialog implements MainUI {
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JPanel jPanel4 = new JPanel();
	JPanel jPanel5 = new JPanel();
	JPanel jPanel6 = new JPanel();
	VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	JPanel jPanel7 = new JPanel();
	JPanel jPanel8 = new JPanel();
	JTextField MoneyDuom = new TheTextField();
	JLabel jLabel1 = new JLabel();

	private int step = 0;

	private PosKeyMap kmap;

	public OrderText() {
		super(pos.posFrame);
		try {
			jbInit();
			setTitle("Ԥ����");
			setSize(350, 200);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(
				(int) ((screenSize.getWidth() - 350) / 2),
				(int) ((screenSize.getHeight() - 200) / 2));

			addKeyAndContainerListenerRecursively(this);

			kmap = PosKeyMap.getInstance();

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					cancel();
				}
			});

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
		jPanel3.setPreferredSize(new Dimension(10, 30));
		jPanel4.setPreferredSize(new Dimension(30, 10));
		jPanel5.setPreferredSize(new Dimension(30, 10));
		jPanel6.setPreferredSize(new Dimension(10, 30));
		jPanel2.setLayout(verticalFlowLayout1);
		MoneyDuom.setBackground(Color.white);
		MoneyDuom.setFont(new java.awt.Font("Dialog", 0, 16));
		MoneyDuom.setPreferredSize(new Dimension(100, 30));
		MoneyDuom.setEditable(true);
		MoneyDuom.setText("");
		jLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabel1.setPreferredSize(new Dimension(70, 23));
		jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel1.setText("��  ��:");
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(jPanel2, BorderLayout.CENTER);
		jPanel2.add(jPanel8, null);
		jPanel8.add(jLabel1, null);
		jPanel8.add(MoneyDuom, null);
		jPanel2.add(jPanel7, null);
		jPanel1.add(jPanel3, BorderLayout.SOUTH);
		jPanel1.add(jPanel4, BorderLayout.WEST);
		jPanel1.add(jPanel5, BorderLayout.EAST);
		jPanel1.add(jPanel6, BorderLayout.NORTH);
	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setCashier(java.lang.String)
	 */
	public void setCashier(String value) {

	}
	 public int dispCollectivity(String collectivity){
	 	return 1;
	 	}
	/**
	 * @see com.royalstone.pos.gui.MainUI#setDatetime(java.lang.String)
	 */
	public void setDatetime(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setDutyNo(java.lang.String)
	 */
	public void setDutyNo(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setHoldNo(java.lang.String)
	 */
	public void setHoldNo(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setInputField(java.lang.String)
	 */
	public void setWaiterNo(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setInputField(java.lang.String)
	 */
	public void setInputField(String value) {
		switch (step) {
			case 0 :
				MoneyDuom.setEditable(true);
				MoneyDuom.setText(value);
				MoneyDuom.setCaretPosition(value.length());
				break;
			case 1 :
				StringBuffer strb = new StringBuffer();
				for (int i = 0; i < value.length(); i++) {
					strb.append("*");
				}
				MoneyDuom.setEditable(false);
				break;
		}

	}
	
	private class TheTextField extends JTextField {

		/**
		 * �������м��������Ĭ�϶���
		 * @see javax.swing.JComponent#processKeyBinding(javax.swing.KeyStroke, java.awt.event.KeyEvent, int, boolean)
		 */
		protected boolean processKeyBinding(
			KeyStroke ks,
			KeyEvent e,
			int condition,
			boolean pressed) {
			return false;
		}
	}

	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setPaid(java.lang.String)
	 */
	public void setPaid(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setPosNo(java.lang.String)
	 */
	public void setPosNo(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setPrompt(java.lang.String)
	 */
	public void setPrompt(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setSpCode(java.lang.String)
	 */
	public void setSpCode(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setSpName(java.lang.String)
	 */
	public void setSpName(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setSpPrice(java.lang.String)
	 */
	public void setSpPrice(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setSpQuantity(java.lang.String)
	 */
	public void setSpQuantity(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setTotal(java.lang.String)
	 */
	public void setTotal(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setTransNo(java.lang.String)
	 */
	public void setTransNo(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setUnPaid(java.lang.String)
	 */
	public void setUnPaid(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#display(com.royalstone.pos.common.Sale)
	 */
	public void display(Sale s) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#display(com.royalstone.pos.common.Payment)
	 */
	public void display(Payment p) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#clear()
	 */
	public void clear() {
		MoneyDuom.setText("");
	}

	/**
	 * @see com.royalstone.pos.gui.MainUI#setConnStatus(java.lang.String)
	 */
	public void setConnStatus(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setStatus(java.lang.String)
	 */
	public void setStatus(String value) {

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setStep(int)
	 */
	public void setStep(int value) {
		step = value;
	}

	private PosKeyboard keyListener = new PosKeyboard();
	private OutputStream posOutputStream = pos.posOutStream;

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
	 * @author liangxinbiao
	 */
	private class PosKeyboard implements KeyListener {

		public void keyPressed(KeyEvent e) {

			if (posOutputStream != null) {

				try {
					int keyCode = e.getKeyCode();
					switch (keyCode) {
						case KeyEvent.VK_ESCAPE :
							keyCode =
								kmap.getKeyValue(
									new PosInput(PosFunction.CANCEL));
							posOutputStream.write(keyCode);
							posOutputStream.flush();
							dispose();
							break;
						default :
							posOutputStream.write(keyCode);
							posOutputStream.flush();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}


	/**
	 *  �û���ȡ�����Ĵ�����
	 */
	private void cancel() {
		if (posOutputStream != null) {
			try {
				int keyCode =
					kmap.getKeyValue(new PosInput(PosFunction.CANCEL));
				posOutputStream.write(keyCode);
				posOutputStream.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#setUnPaidLabel(java.lang.String)
	 */
	public void setUnPaidLabel(String value) {
	}

	/**
	 * @see com.royalstone.pos.gui.MainUI#displayDiscount(com.royalstone.pos.common.Sale)
	 */
	public void displayDiscount(Sale s) {
	}

	/**
	 * @see com.royalstone.pos.gui.MainUI#displayDiscount4correct(com.royalstone.pos.common.Sale, com.royalstone.pos.common.SheetValue)
	 */
	public void displayDiscount4correct(Sale s, SheetValue sheet){
	}

	/**
	 * @see com.royalstone.pos.gui.MainUI#disptotal()
	 */
	public int disptotal(SheetValue v) {
		return 1;
	}
	
	/**
	 * @see com.royalstone.pos.gui.MainUI#displayprom(com.royalstone.pos.common.Sale)
	 */
	public void displayprom(Sale s) {
	}

	/* ���� Javadoc��
	 * @see com.royalstone.pos.gui.MainUI#dispMemberCardHeader(com.royalstone.pos.card.LoanCardQueryVO)
	 */
	public int dispMemberCard(MemberCard memberCard) {
		// TODO �Զ����ɷ������
		return 0;
	}

    public int dispCoupon(String couponNO) {
        return 0;
    }

    /* ���� Javadoc��
	 * @see com.royalstone.pos.gui.MainUI#setWorkDay(java.lang.String)
	 */
	public void setWorkDay(String value) {
		// TODO �Զ����ɷ������
		
	}
	
}
