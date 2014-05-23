package com.royalstone.pos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.royalstone.pos.common.PosFunction;
import com.royalstone.pos.io.PosKeyMap;
import com.royalstone.pos.shell.pos;

/**
 * IC卡的刷卡对话框
 * @author liangxinbiao
 */
public class PriceInput extends JDialog {
	private String tips;
        public String type;
        public String price = "";
	JPanel jPanel2 = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
	JPanel jPanel1 = new JPanel();
	JLabel jLabel1 = new JLabel();
	JTextField cardValue = new JTextField();
	JLabel jLabel5 = new JLabel();
	JLabel jLabel6 = new JLabel();
	FlowLayout flowLayout1 = new FlowLayout();

	StringBuffer inputcode = new StringBuffer();

	private volatile boolean isFinish=false;
	private boolean isConfirm=false;

	private PosKeyMap kmap;


	/**
	 *
	 * @return 返回用户是否按下确定按钮
	 */
	public boolean isConfirm(){
		return isConfirm;
	}

	/**
	 * 处理键盘动作
	 * @author liangxinbiao
	 */
	private class CardKeyListener extends KeyAdapter {
          String type;
          public CardKeyListener(){

         }

          public CardKeyListener(String type){
            this.type=type;
           // System.out.println("我现在要执行者"+this.type+"的操作");
          }

		public void keyPressed(java.awt.event.KeyEvent e) {
                      switch (kmap.getFunction(e.getKeyCode()).getKey()) {
                              // case PosFunction.CARDBANK: type="bank";break;
                              // case PosFunction.CARDLOAN: type="loan";break;
				case PosFunction.EXIT:
				case PosFunction.CANCEL:
					if (inputcode.length() > 0) {
						inputcode.delete(0, inputcode.length());
					}
					isFinish=true;
					dispose();
					break;
				case PosFunction.ENTER :
					if (inputcode.length() < 1){
						inputcode.append(price);
						setprice();
						}
					isFinish=true;
					isConfirm=true;
					dispose();
					break;
			
                case PosFunction.BACKSPACE :
                                 if (inputcode.length() > 0) {
                                   inputcode.deleteCharAt(inputcode.length()-1);
                                    cardValue.setText(inputcode.toString());
                                 }
                               break;


				case '0' :
				case '1' :
				case '2' :
				case '3' :
				case '4' :
				case '5' :
				case '6' :
				case '7' :
				case '8' :
				case '9' :
                     inputcode.append((char)kmap.getFunction(e.getKeyCode()).getKey());
                    cardValue.setText(inputcode.toString());
                     break;
				case PosFunction.POINT:
                    if(inputcode.indexOf(".") < 0) {
					inputcode.append(".");
                   cardValue.setText(inputcode.toString());
                    }
                    break;
			}
		}
	}

	/**
	 *
	 *
	 */
	public PriceInput() {
		this("请输入价格：");
	}


	public PriceInput(String tips){
		super(pos.posFrame, pos.posFrame.getTitle(), true);
		try {

			this.tips=tips;
			jbInit();
			setSize(450, 200);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(
				(int) ((screenSize.getWidth() - 450) / 2),
				(int) ((screenSize.getHeight() - 200) / 2));

			PriceInput.CardKeyListener cl = new PriceInput.CardKeyListener();
			cardValue.addKeyListener(cl);


			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					isFinish=true;
				}
			});

			kmap=PosKeyMap.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public PriceInput(String tips,String price,String id){
		super(pos.posFrame, pos.posFrame.getTitle(), false);
		try {

			this.tips=tips;
			jbInit(price,id);
			setSize(450, 200);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(
				(int) ((screenSize.getWidth() - 450) / 2),
				(int) ((screenSize.getHeight() - 200) / 2));

			PriceInput.CardKeyListener cl = new PriceInput.CardKeyListener();
			cardValue.addKeyListener(cl);


			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					isFinish=true;
				}
			});

			kmap=PosKeyMap.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	/**
	 * JBuilder自动生成的初始化界面方法
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		jPanel2.setLayout(gridLayout1);
		gridLayout1.setColumns(2);                             
		gridLayout1.setRows(3);
		jPanel1.setLayout(flowLayout1);
		jLabel1.setFont(new java.awt.Font("Dialog", 0, 16));
		jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel1.setText(tips);
		cardValue.setBackground(Color.white);
		cardValue.setFont(new java.awt.Font("Dialog", 0, 16));
		cardValue.setMinimumSize(new Dimension(6, 22));
		cardValue.setPreferredSize(new Dimension(200, 40));
		cardValue.setEditable(false);
		cardValue.setText("0");
		jLabel6.setText("");
		jLabel5.setAlignmentY((float) 0.5);
		jLabel5.setText("");
		this.getContentPane().add(jPanel2, BorderLayout.CENTER);
		jPanel2.add(jLabel6, null);
		jPanel2.add(jPanel1, null);
		jPanel1.add(jLabel1, null);
		jPanel1.add(cardValue, null);
		jPanel2.add(jLabel5, null);
	}
	
	public void setprice(){
		this.price = "";
		}
	
	private void jbInit(String price,String id) throws Exception {
		this.price = price;
		// 丑陋的作法，不要学我！
		String aa = "    ";
		String dd = "提货单的单据号是：";
		String gg = "提货单的剩余金额：";
		StringBuffer ee = new StringBuffer("                ");
		ee.append(dd);
		ee.append(id);
		ee.append(aa);
		ee.append(gg);
		ee.append(price);
		String cc = String.valueOf(ee);
		// --- 显示在屏幕上。
		jPanel2.setLayout(gridLayout1);
		gridLayout1.setColumns(2);                             
		gridLayout1.setRows(3);
		jPanel1.setLayout(flowLayout1);
		jLabel1.setFont(new java.awt.Font("Dialog", 0, 16));
		jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel1.setText(tips);
		cardValue.setBackground(Color.white);
		cardValue.setFont(new java.awt.Font("Dialog", 0, 16));
		cardValue.setMinimumSize(new Dimension(6, 22));
		cardValue.setPreferredSize(new Dimension(200, 40));
		cardValue.setEditable(false);
		cardValue.setText(price);
		jLabel6.setText(cc);
		jLabel5.setAlignmentY((float) 0.5);
		jLabel5.setText("");
		this.getContentPane().add(jPanel2, BorderLayout.CENTER);
		jPanel2.add(jLabel6, null);
		jPanel2.add(jPanel1, null);
		jPanel1.add(jLabel1, null);
		jPanel1.add(cardValue, null);
		jPanel2.add(jLabel5, null);
		
	}
	

	/**
	 * @return 返回卡号
	 */
	public String getInputcode() {
		return inputcode.toString();
	}
	/**
	 *
	 * @return 用户是否完成刷卡动作
	 */
	public boolean isFinish(){
		return isFinish;
	}

}
