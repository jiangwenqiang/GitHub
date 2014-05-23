package com.royalstone.pos.gui;

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

import javax.swing.BorderFactory;
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
 * ��Ʒ¼��Ի���
 * @author liangxinbiao
 */
public class OilInput extends JDialog implements MainUI {
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
	JPanel jPanel9 = new JPanel();
	JTextField txtQty = new TheTextField();
	JLabel jLabel5 = new JLabel();
	JTextField txtAmount = new TheTextField();
	JLabel jLabel6 = new JLabel();
	JTextField txtOilMNO = new TheTextField();
	JLabel jLabel7 = new JLabel();

	private PosKeyboard keyListener = new PosKeyboard();
	private OutputStream posOutputStream = null;
	private int step;

	private PosKeyMap kmap;
  JPanel panOriginPrice = new JPanel();
  JLabel lblPriceName = new JLabel();
  JPanel jPanel10 = new JPanel();
  JLabel lblPrice = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel lblName = new JLabel();
  JPanel jPanel11 = new JPanel();
  JLabel lblOriginPrice = new JLabel();
  JLabel jLabel3 = new JLabel();

	/**
	 *
	 * @param out
	 */
	public OilInput(OutputStream out) {
		super(pos.posFrame);
		posOutputStream = out;
		try {

			kmap = new PosKeyMap();
			kmap.fromXML("pos.xml");

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					try {
						int keyCode =
							kmap.getKeyValue(new PosInput(PosFunction.EXIT));
						posOutputStream.write(keyCode);
						posOutputStream.flush();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});

			jbInit();
			setTitle("��Ʒ¼��");
			setSize(450, 350);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(
				(int) ((screenSize.getWidth() - 450) / 2),
				(int) ((screenSize.getHeight() - 300) / 2));

			panOriginPrice.setVisible(false);

			addKeyAndContainerListenerRecursively(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showOriginPrice(boolean isShow){

		if(isShow){
			panOriginPrice.setVisible(true);
			lblPriceName.setText("�Żݼ�:");
		}else{
			panOriginPrice.setVisible(false);
			lblPriceName.setText("��   ��:");
		}


	}
	 public int dispCollectivity(String collectivity){
	 	return 1;
	 	}

	/**
	 * JBuilder�Զ����ɵĳ�ʼ�����淽��
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		jPanel1.setLayout(borderLayout1);
		jPanel3.setPreferredSize(new Dimension(10, 30));
		jPanel4.setPreferredSize(new Dimension(40, 10));
		jPanel5.setPreferredSize(new Dimension(40, 10));
		jPanel6.setPreferredSize(new Dimension(10, 30));
		jPanel2.setLayout(verticalFlowLayout1);
		jLabel5.setFont(new java.awt.Font("Dialog", 0, 20));
		jLabel5.setToolTipText("");
		jLabel5.setText("��   ��:");
		txtAmount.setBackground(Color.white);
		txtAmount.setFont(new java.awt.Font("Dialog", 0, 20));
		txtAmount.setDoubleBuffered(false);
		txtAmount.setPreferredSize(new Dimension(200, 30));
		txtAmount.setEditable(true);
		txtAmount.setText("");
		txtAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel6.setFont(new java.awt.Font("Dialog", 0, 20));
		jLabel6.setText("��   ��:");
		txtOilMNO.setBackground(Color.white);
		txtOilMNO.setFont(new java.awt.Font("Dialog", 0, 20));
		txtOilMNO.setPreferredSize(new Dimension(200, 30));
		txtOilMNO.setEditable(false);
		txtOilMNO.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOilMNO.setScrollOffset(0);
		jLabel7.setFont(new java.awt.Font("Dialog", 0, 20));
		jLabel7.setToolTipText("");
		jLabel7.setText("�͵���:");
		txtQty.setBackground(Color.white);
		txtQty.setFont(new java.awt.Font("Dialog", 0, 20));
		txtQty.setPreferredSize(new Dimension(200, 30));
		txtQty.setEditable(false);
		txtQty.setText("");
		txtQty.setHorizontalAlignment(SwingConstants.RIGHT);
    lblPriceName.setFont(new java.awt.Font("Dialog", 0, 20));
    lblPriceName.setHorizontalAlignment(SwingConstants.RIGHT);
    lblPriceName.setText("��   ��:");
    lblPrice.setText("");
    lblPrice.setFont(new java.awt.Font("Dialog", 0, 20));
    lblPrice.setBorder(BorderFactory.createLineBorder(Color.black));
    lblPrice.setPreferredSize(new Dimension(200, 30));
    lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
    lblPrice.setText("");
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 20));
    jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2.setText("��Ʒ��:");
    lblName.setFont(new java.awt.Font("Dialog", 0, 20));
    lblName.setBorder(BorderFactory.createLineBorder(Color.black));
    lblName.setPreferredSize(new Dimension(200, 30));
    lblName.setRequestFocusEnabled(true);
    lblName.setText("");
    lblOriginPrice.setFont(new java.awt.Font("Dialog", 0, 20));
    lblOriginPrice.setBorder(BorderFactory.createLineBorder(Color.black));
    lblOriginPrice.setPreferredSize(new Dimension(200, 30));
    lblOriginPrice.setHorizontalAlignment(SwingConstants.RIGHT);
    lblOriginPrice.setText("");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 20));
    jLabel3.setRequestFocusEnabled(true);
    jLabel3.setText("ԭ����:");
    this.getContentPane().add(jPanel1,BorderLayout.CENTER);
		jPanel1.add(jPanel2,BorderLayout.CENTER);
    jPanel10.add(lblPriceName, null);
    jPanel10.add(lblPrice, null);
    jPanel11.add(jLabel2, null);
    jPanel11.add(lblName, null);
    jPanel2.add(jPanel11, null);
    jPanel2.add(jPanel10, null);
    jPanel2.add(panOriginPrice, null);
    panOriginPrice.add(jLabel3, null);
    panOriginPrice.add(lblOriginPrice, null);
		jPanel2.add(jPanel9,null);
		jPanel9.add(jLabel6,null);
		jPanel9.add(txtAmount,null);
		jPanel2.add(jPanel8,null);
		jPanel8.add(jLabel5,null);
		jPanel8.add(txtQty,null);
		jPanel2.add(jPanel7,null);
		jPanel7.add(jLabel7,null);
		jPanel7.add(txtOilMNO,null);

		jPanel1.add(jPanel3, BorderLayout.SOUTH);
		jPanel1.add(jPanel4, BorderLayout.WEST);
		jPanel1.add(jPanel5, BorderLayout.EAST);
		jPanel1.add(jPanel6, BorderLayout.NORTH);
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
					posOutputStream.write(keyCode);
					posOutputStream.flush();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		/* ���� Javadoc��
		 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
		 */
		public void keyReleased(KeyEvent e) {
		}

		/* ���� Javadoc��
		 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
		 */
		public void keyTyped(KeyEvent e) {
		}
	}

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
	 *
	 * @param value ��Ʒ����
	 */
	public void setOilName(String value) {
		lblName.setText(value);
	}

	/**
	 *
	 * @param value ��Ʒ����
	 */
	public void setOilPrice(String value) {
		lblPrice.setText(value);
	}

	public void setOilOriginPrice(String value) {
		lblOriginPrice.setText(value);
	}


	/**
	 *
	 * @param value ��Ʒ����
	 */
	public void setQuantity(String value) {
		txtQty.setText(value);
	}

	/**
	 *
	 * @param value ��Ʒ�ܼ�
	 */
	public void setAmount(String value) {
		txtAmount.setText(value);
	}

	/**
	 *
	 * @param value �͵���
	 */
	public void setOilGunNO(String value) {
		txtOilMNO.setText(value);
	}
	/**
	 * @param value
	 */
	public void setInputField(String value) {
		switch (step) {
			case 0 :
				txtQty.setText(value);
				txtQty.setCaretPosition(value.length());
				break;
			case 1 :
				txtAmount.setText(value);
				txtAmount.setCaretPosition(value.length());
				break;
			case 2 :
				txtOilMNO.setText(value);
				txtOilMNO.setCaretPosition(value.length());
				break;
		}

	}
	/**
	 *
	 */
	public void setStep(int value) {
		step = value;
		switch (step) {
			case 0 :
				disableText();
				txtQty.setEditable(true);
				txtQty.requestFocus();
				txtQty.setCaretPosition(txtQty.getText().length());
				break;
			case 1:
				disableText();
				txtAmount.setEditable(true);
				txtAmount.requestFocus();
				txtAmount.setCaretPosition(txtAmount.getText().length());
				break;
			case 2 :
				disableText();
				txtOilMNO.setEditable(true);
				txtOilMNO.requestFocus();
				txtOilMNO.setCaretPosition(txtOilMNO.getText().length());
				break;
		}
	}

	/**
	 *
	 */
	private void disableText() {
		txtQty.setEditable(false);
		txtAmount.setEditable(false);
		txtOilMNO.setEditable(false);
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
	 * @see com.royalstone.pos.gui.MainUI#setCashier(java.lang.String)
	 */
	public void setCashier(String value) {
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
	public void setWaiterNo(String value) {
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
	 * @deprecated
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
