package com.royalstone.pos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.borland.jbcl.layout.VerticalFlowLayout;
import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.common.Payment;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.common.SheetValue;
import com.royalstone.pos.io.PosKeyMap;
import com.royalstone.pos.util.PosConfig;

/**
 * 登陆面板
 * @author Liangxinbiao
 * @version 1.0
 */

public class LogonPanel extends JPanel implements MainUI {
	private Image img;

	private PosKeyboard keyListener = new PosKeyboard();
	private OutputStream posOutputStream = null;

	private DumbDocument userNameDumbDoc = new DumbDocument();
	private DumbDocument passwordDumbDoc = new DumbDocument();
	private DumbDocument dutyNoDumbDoc = new DumbDocument();

	private int step = 0;

	private JFrame theParent;
	private PosKeyMap kmap;

	private StringBuffer password = new StringBuffer();
	JPanel jPanel9 = new JPanel();
	GridLayout gridLayout2 = new GridLayout();
	JLabel jLabel8 = new JLabel();
	JTextField lblUserName = new TheTextField();
	JPanel jPanel12 = new JPanel();
	GridLayout gridLayout3 = new GridLayout();
	JPanel jPanel8 = new JPanel();
	JPanel jPanel4 = new JPanel();
	JTextField lblPassword = new TheTextField();
	GridLayout gridLayout4 = new GridLayout();
	JLabel jLabel7 = new JLabel();
	JPanel jPanel13 = new JPanel();
	JPanel jPanel7 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JPanel jPanel1 = new JPanel();
	JPanel jPanel10 = new JPanel();
	JPanel jPanel6 = new JPanel();
	JPanel jPanel14 = new JPanel();
	VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel15 = new JPanel();
	JPanel jPanel5 = new JPanel();
	JPanel jPanel11 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel16 = new JPanel();
	JLabel lblPrompt = new JLabel();
	BorderLayout borderLayout2 = new BorderLayout();
	JPanel jPanel17 = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
  JPanel jPanel18 = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel jPanel19 = new JPanel();
  GridLayout gridLayout5 = new GridLayout();
  JButton jButton_VK7 = new JButton();
  JButton jButton_VK8 = new JButton();
  JButton jButton_VK9 = new JButton();
  JButton jButton_Esc = new JButton();
  JButton jButton_VK4 = new JButton();
  JButton jButton_VK5 = new JButton();
  JButton jButton_VK6 = new JButton();
  JButton jButton_EEsc = new JButton();
  JButton jButton_VK1 = new JButton();
  JButton jButton_VK2 = new JButton();
  GridLayout gridLayout6 = new GridLayout();
  JButton jButton_VK3 = new JButton();
  JButton jButton_VK0 = new JButton();
  JButton jButton_Back = new JButton();
  JButton jButton_BBack = new JButton();
  JButton jButton_Enter = new JButton();
  JButton jButton_EEnter = new JButton();

	/**
	 *
	 * @param f 父窗口
	 */
	public void setTheParent(JFrame f) {
		theParent = f;
	}


	/**
	 *
	 * @param out
	 */
	public LogonPanel(OutputStream out) {
		try {
			this.posOutputStream = out;
			URL url = LogonPanel.class.getResource("/images/background.jpg");
			img = this.getToolkit().createImage(url);
			MediaTracker tracker = new MediaTracker(this);
			tracker.addImage(img, 0);
			tracker.waitForID(0);
			jbInit();
			lblPassword.setDocument(passwordDumbDoc);
			lblUserName.setDocument(userNameDumbDoc);
			addKeyAndContainerListenerRecursively(this);

			kmap = new PosKeyMap();
			kmap.fromXML("pos.xml");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * JBuilder自动生成的初始化界面方法
	 * @throws Exception
	 */
	void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		this.setBackground(new Color(127, 199, 63));
		jPanel9.setLayout(gridLayout2);
		gridLayout2.setColumns(3);
		gridLayout2.setRows(4);
		jLabel8.setText("密码：");
		jLabel8.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel8.setFont(new java.awt.Font("Dialog", 0, 20));
		jLabel8.setPreferredSize(new Dimension(100, 29));
		lblUserName.setText("");
		lblUserName.setBorder(BorderFactory.createLineBorder(Color.black));
		lblUserName.setMinimumSize(new Dimension(4, 31));
		lblUserName.setOpaque(true);
		lblUserName.setPreferredSize(new Dimension(100, 31));
		lblUserName.setEditable(true);
		lblUserName.setFont(new java.awt.Font("Dialog", 0, 20));
		lblUserName.setBackground(Color.white);
		jPanel12.setOpaque(false);
		jPanel8.setOpaque(false);
		jPanel4.setLayout(gridLayout4);
		jPanel4.setBackground(new Color(127, 199, 63));
		jPanel4.setOpaque(false);
		lblPassword.setText("");
		lblPassword.setMinimumSize(new Dimension(4, 22));
		lblPassword.setOpaque(true);
		lblPassword.setPreferredSize(new Dimension(150, 2));
		lblPassword.setEditable(false);
		lblPassword.setDebugGraphicsOptions(0);
		lblPassword.setBorder(BorderFactory.createLineBorder(Color.black));
		lblPassword.setFont(new java.awt.Font("Dialog", 0, 20));
		lblPassword.setBackground(Color.white);
		jLabel7.setText("用户名：");
		jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel7.setFont(new java.awt.Font("Dialog", 0, 20));
		jLabel7.setOpaque(false);
		jPanel13.setOpaque(false);
		jPanel7.setOpaque(false);
		jPanel3.setLayout(gridLayout3);
		jPanel3.setBackground(new Color(127, 199, 63));
		jPanel3.setOpaque(false);
		jPanel1.setOpaque(false);
    jPanel1.setLayout(borderLayout4);
		jPanel10.setOpaque(false);
		jPanel6.setOpaque(false);
		jPanel14.setOpaque(false);
    jPanel14.setLayout(borderLayout5);
		jPanel2.setLayout(verticalFlowLayout1);
		jPanel2.setBackground(new Color(127, 199, 63));
		jPanel2.setOpaque(false);
		jPanel15.setOpaque(false);
		jPanel5.setMinimumSize(new Dimension(10, 10));
		jPanel5.setOpaque(false);
		jPanel11.setOpaque(false);
		jPanel9.setOpaque(false);
		jPanel16.setBorder(BorderFactory.createEtchedBorder());
		jPanel16.setLayout(borderLayout2);
		lblPrompt.setFont(new java.awt.Font("Dialog", 0, 16));
		lblPrompt.setPreferredSize(new Dimension(0, 25));
		lblPrompt.setText("");
		jPanel17.setLayout(gridLayout1);
		jPanel17.setOpaque(false);
		jPanel18.setLayout(gridLayout5);
    jButton_VK7.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK7.setMaximumSize(new Dimension(43, 30));
    jButton_VK7.setPreferredSize(new Dimension(43, 30));
    jButton_VK7.setToolTipText("");
    jButton_VK7.setText("7");
          jButton_VK7.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    gridLayout5.setColumns(3);
    gridLayout5.setRows(2);
    jButton_VK8.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK8.setMaximumSize(new Dimension(43, 30));
    jButton_VK8.setToolTipText("");
    jButton_VK8.setText("8");
        jButton_VK8.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_VK9.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK9.setMaximumSize(new Dimension(43, 30));
    jButton_VK9.setToolTipText("");
    jButton_VK9.setText("9");
        jButton_VK9.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_Esc.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_Esc.setAlignmentY((float) 0.5);
    jButton_Esc.setMaximumSize(new Dimension(43, 30));
    jButton_Esc.setToolTipText("");
    jButton_Esc.setText("退出");
     jButton_Esc.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_VK4.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK4.setMaximumSize(new Dimension(43, 30));
    jButton_VK4.setToolTipText("");
    jButton_VK4.setText("4");
        jButton_VK4.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_VK5.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK5.setMaximumSize(new Dimension(43, 30));
    jButton_VK5.setToolTipText("");
    jButton_VK5.setText("5");
        jButton_VK5.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_VK6.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK6.setMaximumSize(new Dimension(43, 30));
    jButton_VK6.setToolTipText("");
    jButton_VK6.setText("6");
        jButton_VK6.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_EEsc.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_EEsc.setAlignmentY((float) 0.5);
    jButton_EEsc.setMaximumSize(new Dimension(43, 30));
    jButton_EEsc.setToolTipText("");
    jButton_EEsc.setText("出");
        jButton_EEsc.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_VK1.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK1.setMaximumSize(new Dimension(43, 30));
    jButton_VK1.setToolTipText("");
    jButton_VK1.setText("1");
        jButton_VK1.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_VK2.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK2.setMaximumSize(new Dimension(43, 30));
    jButton_VK2.setToolTipText("");
    jButton_VK2.setText("2");
        jButton_VK2.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jPanel19.setLayout(gridLayout6);
    gridLayout6.setColumns(3);
    gridLayout6.setRows(2);
    jButton_VK3.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK3.setMaximumSize(new Dimension(43, 30));
    jButton_VK3.setToolTipText("");
    jButton_VK3.setText("3");
        jButton_VK3.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_VK0.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_VK0.setMaximumSize(new Dimension(43, 30));
    jButton_VK0.setText("0");
        jButton_VK0.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_Back.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_Back.setMaximumSize(new Dimension(43, 30));
    jButton_Back.setToolTipText("");
    jButton_Back.setText("回");
    jButton_BBack.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_BBack.setMaximumSize(new Dimension(43, 30));
    jButton_BBack.setToolTipText("");
    jButton_BBack.setText("退");
        jButton_BBack.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_Enter.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_Enter.setMaximumSize(new Dimension(43, 30));
    jButton_Enter.setToolTipText("");
    jButton_Enter.setText("确认");
        jButton_Enter.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    jButton_EEnter.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_EEnter.setMaximumSize(new Dimension(43, 30));
    jButton_EEnter.setToolTipText("");
    jButton_EEnter.setText("认");
        jButton_EEnter.addMouseListener(new LogonPanel_jButton_mouseAdapter(this));
    this.add(jPanel9, BorderLayout.CENTER);
		jPanel3.add(jLabel7, null);
		jPanel3.add(lblUserName, null);
		jPanel2.add(jPanel3, null);
		jPanel2.add(jPanel4, null);
		jPanel2.add(jPanel17, null);
		jPanel4.add(jLabel8, null);
		jPanel4.add(lblPassword, null);
		jPanel9.add(jPanel15, null);
		jPanel9.add(jPanel5, null);
		jPanel9.add(jPanel11, null);
		this.add(jPanel16, BorderLayout.SOUTH);
		jPanel16.add(lblPrompt, BorderLayout.CENTER);
		jPanel9.add(jPanel12, null);
		jPanel9.add(jPanel8, null);
		jPanel9.add(jPanel13, null);
		jPanel9.add(jPanel7, null);
		jPanel9.add(jPanel2, null);
		jPanel9.add(jPanel1, null);
        if("ON".equals(PosConfig.getInstance().getString("IS_TOUCHSCREEN"))){
              jPanel18.setVisible(true);
              jPanel19.setVisible(true);
        }
        else{
              jPanel18.setVisible(false);
            jPanel19.setVisible(false);
        }
    jPanel1.add(jPanel18,  BorderLayout.CENTER);
    jPanel18.add(jButton_VK7, null);
    jPanel18.add(jButton_VK8, null);
    jPanel18.add(jButton_VK9, null);

    jPanel18.add(jButton_VK4, null);
    jPanel18.add(jButton_VK5, null);
    jPanel18.add(jButton_VK6, null);
  //  jPanel18.add(jButton_EEsc, null);
		jPanel9.add(jPanel10, null);
		jPanel9.add(jPanel6, null);
		jPanel9.add(jPanel14, null);
    jPanel14.add(jPanel19,  BorderLayout.CENTER);
    jPanel19.add(jButton_VK1, null);
    jPanel19.add(jButton_VK2, null);
    jPanel19.add(jButton_VK3, null);

    jPanel19.add(jButton_VK0, null);
   // jPanel19.add(jButton_Back, null);
   // jPanel19.add(jButton_BBack, null);
        jPanel19.add(jButton_Esc, null);
         jPanel19.add(jButton_Enter, null);
   // jPanel19.add(jButton_EEnter, null);

	}

	/**
	 *
	 * @return 用户名
	 */
	public String getUserName() {
		return lblUserName.getText();
	}

	/**
	 *
	 * @return 密码
	 */
	public String getPassword() {
		return this.password.toString();
	}

	/**
	 * 将用户所输入的用户名显示出来
	 * @param userName
	 */
	public void appendUserName(String userName) {
		userNameDumbDoc.setPermit(true);
		lblUserName.setText(lblUserName.getText() + userName);
		userNameDumbDoc.setPermit(false);
	}

	/**
	 * 将用户所输入的密码用“*”显示出来
	 * @param password
	 */
	public void appendPassword(String password) {
		passwordDumbDoc.setPermit(true);
		lblPassword.setText(lblPassword.getText() + '*');
		this.password.append(password);
		passwordDumbDoc.setPermit(false);
	}

	/**
	 * 清除输入
	 */
	public void clearInput() {
		lblUserName.setText("");
		lblPassword.setText("");
		password = new StringBuffer();
	}

	/**
	 *
	 * @param component
	 */
	public void setFocus(int component) {
		this.requestFocus();
	}

	/**
	 * 重画时显示背景图片
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
	}

	/**
	 *
	 * @author liangxinbiao
	 */
	private class DumbDocument extends PlainDocument {
		private boolean permit = false;

		public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
			if (permit)
				super.insertString(offs, str, a);
		}

		protected void insertUpdate(
			AbstractDocument.DefaultDocumentEvent chng,
			AttributeSet a) {
			//if (permit)
			super.insertUpdate(chng, a);
		}

		protected void removeUpdate(
			AbstractDocument.DefaultDocumentEvent chng) {
			//if (permit)
			super.removeUpdate(chng);
		}

		public void setPermit(boolean value) {
			permit = value;
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
	 * @see com.royalstone.pos.gui.MainUI#setInputField(java.lang.String)
	 */
	public void setInputField(String value) {
		switch (step) {
			case 0 :
				userNameDumbDoc.permit = true;
				lblUserName.setText(value);
				userNameDumbDoc.permit = false;
				lblUserName.setCaretPosition(value.length());
				break;
			case 1 :
				StringBuffer strb = new StringBuffer();
				for (int i = 0; i < value.length(); i++) {
					strb.append("*");
				}
				passwordDumbDoc.permit = true;
				lblPassword.setText(strb.toString());
				passwordDumbDoc.permit = false;
				lblPassword.setCaretPosition(value.length());
				break;
			case 2 :
				dutyNoDumbDoc.permit = true;
				//txtDutyNo.setText(value);
				dutyNoDumbDoc.permit = false;
				//txtDutyNo.setCaretPosition(value.length());
				break;
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
	public void setPrompt(final String value) {
		Runnable r=new Runnable(){
			public void run() {
				lblPrompt.setText(value);
			}
		};
		SwingUtilities.invokeLater(r);
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

	/**
	 * 清除所有输入
	 */
	public void clear() {
		lblUserName.setText("");
		lblPassword.setText("");
		lblPrompt.setText("");
		//txtDutyNo.setText("");
		focusComponent();
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
	 * 处理键盘动作
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

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
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
		switch (step) {
			case 0 :
				disableText();
				lblUserName.setEditable(true);
				lblUserName.requestFocus();
				lblUserName.setCaretPosition(lblUserName.getText().length());
				break;
			case 1 :
				disableText();
				lblPassword.setEditable(true);
				lblPassword.requestFocus();
				break;
			case 2 :
				disableText();
				//txtDutyNo.setEditable(true);
				//txtDutyNo.requestFocus();
				break;
		}
	}

	/**
	 *
	 * @param o
	 */
	public void setOutputStream(OutputStream o) {
		this.posOutputStream = o;
	}

	private void disableText() {
		lblUserName.setEditable(false);
		lblPassword.setEditable(false);
		//txtDutyNo.setEditable(false);
	}

	/**
	 *
	 * @author liangxinbiao
	 */
	private class TheTextField extends JTextField {

		/**
		 * 屏蔽所有键盘输入的默认动作
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
	 * 根据现在所处的步骤好(step)将焦点定在相应的输入里
	 */
	public void focusComponent() {
		switch (step) {
			case 0 :
				lblUserName.requestFocus();
				lblUserName.setCaretPosition(lblUserName.getText().length());
				break;
			case 1 :
				lblPassword.requestFocus();
				lblPassword.setCaretPosition(lblPassword.getText().length());
				break;
			case 2 :
				//txtDutyNo.requestFocus();
				//txtDutyNo.setCaretPosition(txtDutyNo.getText().length());
				break;
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

	public void displayDiscount4correct(Sale s, SheetValue sheet){
		}

	/**
	 */
	public int disptotal(SheetValue v) {
		return 1;
	}
	
	 public int dispCollectivity(String collectivity){
	 	return 1;
	 	}

	/**
	 * @see com.royalstone.pos.gui.MainUI#displayprom(com.royalstone.pos.common.Sale)
	 */
	public void displayprom(Sale s) {
	}


	/* （非 Javadoc）
	 * @see com.royalstone.pos.gui.MainUI#dispMemberCardHeader(com.royalstone.pos.card.LoanCardQueryVO)
	 */
	public int dispMemberCard(MemberCard memberCard) {
		// TODO 自动生成方法存根
		return 0;
	}

    public int dispCoupon(String couponNO) {
        return 0;
    }


    /* （非 Javadoc）
	 * @see com.royalstone.pos.gui.MainUI#setWorkDay(java.lang.String)
	 */
	public void setWorkDay(String value) {
		// TODO 自动生成方法存根

	}

  void jButton_mouseClicked(MouseEvent e) {
      int touchKey=0;
      try {
          if(jButton_Esc.equals(e.getSource()))
                 touchKey=27;
          if(jButton_EEsc.equals(e.getSource()))
                 touchKey=27;
          if(jButton_Back.equals(e.getSource()))
              touchKey=8;
          if(jButton_BBack.equals(e.getSource()))
              touchKey=8;
          if(jButton_VK7.equals(e.getSource()))
              touchKey=55;
          if(jButton_VK8.equals(e.getSource()))
              touchKey=56;
          if(jButton_VK9.equals(e.getSource()))
              touchKey=57;
          if(jButton_VK4.equals(e.getSource()))
              touchKey=52;
          if(jButton_VK5.equals(e.getSource()))
              touchKey=53;
          if(jButton_VK6.equals(e.getSource()))
              touchKey=54;
          if(jButton_VK1.equals(e.getSource()))
              touchKey=49;
          if(jButton_VK2.equals(e.getSource()))
              touchKey=50;
          if(jButton_VK3.equals(e.getSource()))
              touchKey=51;
          if(jButton_VK0.equals(e.getSource()))
              touchKey=48;
          if(jButton_Enter.equals(e.getSource()))
              touchKey=10;
          if(jButton_EEnter.equals(e.getSource()))
              touchKey=10;

           posOutputStream.write(touchKey);
           posOutputStream.flush();
      } catch (IOException e1) {
          e1.printStackTrace();
      }
  }


}

class LogonPanel_jButton_mouseAdapter extends java.awt.event.MouseAdapter {
  LogonPanel adaptee;

  LogonPanel_jButton_mouseAdapter(LogonPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.jButton_mouseClicked(e);
  }
}
