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
 * ˢ���˿��ĶԻ���
 * @author liangxinbiao
 */
public class MSRInput extends JDialog {
	private String tips;
        public String type;
	JPanel jPanel2 = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
	JPanel jPanel1 = new JPanel();
	JLabel jLabel1 = new JLabel();
	public JTextField cardValue = new JTextField();
	JLabel jLabel5 = new JLabel();
	JLabel jLabel6 = new JLabel();
	FlowLayout flowLayout1 = new FlowLayout();

	StringBuffer inputcode = new StringBuffer();

	private volatile boolean isFinish=false;
	private boolean isConfirm=false;

	private PosKeyMap kmap;


	/**
	 *
	 * @return �����û��Ƿ���ȷ����ť
	 */
	public boolean isConfirm(){
		return isConfirm;
	}

	/**
	 * ������̶���
	 * @author liangxinbiao
	 */
	private class CardKeyListener extends KeyAdapter {
          String type;
          public CardKeyListener(){

         }

          public CardKeyListener(String type){
            this.type=type;
           // System.out.println("������Ҫִ����"+this.type+"�Ĳ���");
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
					isFinish=true;
					isConfirm=true;
					dispose();
					break;
                               case PosFunction.BACKSPACE :
                                 if(type.equals("loan")){
                                 if (inputcode.length() > 0) {
                                   inputcode.deleteCharAt(inputcode.length()-1);
                                    cardValue.setText(inputcode.toString());
                                 }
                               }else if(type.equals("bank")){
                                 if (inputcode.length() > 0) {
                                   inputcode.deleteCharAt(inputcode.length()-1);
                                   StringBuffer tmp2 = new StringBuffer();
                                        for (int i = 0; i < inputcode.length(); i++) {
                                                tmp2.append("*");

                                        }
                                    cardValue.setText(tmp2.toString());
                                 }


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
				case '=' :
				case ';' :
					inputcode.append((char)kmap.getFunction(e.getKeyCode()).getKey());
 					StringBuffer tmp = new StringBuffer();
					for (int i = 0; i < inputcode.length(); i++) {
						tmp.append("*");

					}

                                       // System.out.println("input code is   "+inputcode.toString());
                                       // System.out.println("���ſ���������: "+this.type);
                                      // cardValue.setText(inputcode.toString());
                                      // cardValue.setText(tmp.toString());

                                        if(type.equals("bank")){
                                         // System.out.println("bankCard input code is   "+inputcode.toString());
                                          cardValue.setText(tmp.toString());
                                        }else if(type.equals("loan")){
                                         // cardValue.setText(tmp.toString());
                                          cardValue.setText(inputcode.toString());

                                        }

			}
		}
	}

	/**
	 *
	 *
	 */
	public MSRInput() {
		this("��ˢ����","loan");
	}


	public MSRInput(String tips){
		super(pos.posFrame, pos.posFrame.getTitle(), false);
		try {

			this.tips=tips;
			jbInit();
			setSize(450, 200);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(
				(int) ((screenSize.getWidth() - 450) / 2),
				(int) ((screenSize.getHeight() - 200) / 2));

			CardKeyListener cl = new CardKeyListener();
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

        public MSRInput(String tips,String type){

              super(pos.posFrame, pos.posFrame.getTitle(), false);

                try {
                        this.type=type;
                        this.tips=tips;
                        jbInit();
                        setSize(450, 200);
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        this.setLocation(
                                (int) ((screenSize.getWidth() - 450) / 2),
                                (int) ((screenSize.getHeight() - 200) / 2));

                        CardKeyListener cl = new CardKeyListener(this.type);
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
	 * JBuilder�Զ����ɵĳ�ʼ�����淽��
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
		cardValue.setPreferredSize(new Dimension(100, 40));
        cardValue.setEditable(false);
		cardValue.setText("");
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

	/**
	 * @return ���ؿ���
	 */
	public String getInputcode() {
		return inputcode.toString();
	}
	/**
	 *
	 * @return �û��Ƿ����ˢ������
	 */
	public boolean isFinish(){
		return isFinish;
	}

}
