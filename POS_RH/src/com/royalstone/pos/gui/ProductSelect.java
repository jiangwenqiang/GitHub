/*
 * 创建日期 2014-04-12
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.royalstone.pos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.royalstone.pos.common.GoodsProduct;
import com.royalstone.pos.common.ProductProperty;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.getCardType;

public class ProductSelect extends JDialog {
	
	public final static String PS_CLEAR = "CLEAR";
	public final static String PS_ESC = "ESC";
	public final static String PS_ENTER = "ENTER";
	
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JButton btnCancel = new JButton();
	JButton btnEnter = new JButton();
	JLabel  lblproperties =  new JLabel();

	private TheTableModel theTableModel;
	private TheTable theTable;
	private String bconfirm = null;
	private String holdid;
	private EnterButtonAction enterBtnAction = new EnterButtonAction();
	private CancelButtonAction cancelBtnAction = new CancelButtonAction();

	BorderLayout borderLayout2 = new BorderLayout();
	
	Vector prodprop = null;
	
	public ProductSelect(){
		
	}

	public ProductSelect(GoodsProduct gp,String properties) {
		super(pos.posFrame);
		try {
			if(properties != null) lblproperties.setText("已选: "+properties);
			
			this.setTitle("加工属性");
			setModal(true);
			setSize(580, 300);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(
				(int) ((screenSize.getWidth() - 580) / 2),
				(int) ((screenSize.getHeight() - 300) / 2));
			
			jbInit();

			theTableModel = new TheTableModel();
			theTable = new TheTable(theTableModel);

			theTable.getTableHeader().setFont(
				new java.awt.Font("Dialog", 0, 20));
			theTable.setFont(new java.awt.Font("Dialog", 0, 20));
			theTable.setRowHeight(30);
			theTable.setDefaultRenderer(
				JLabel.class,
				new JLabelTableCellRenderer());
				
			TableColumn col = theTable.getColumnModel().getColumn(0);
			col.setPreferredWidth(60);

			col = theTable.getColumnModel().getColumn(1);
			col.setPreferredWidth(260);

			//col = theTable.getColumnModel().getColumn(2);
			//col.setPreferredWidth(100);
			

			JScrollPane scrollPane1 = new JScrollPane(theTable);
			scrollPane1.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			jPanel2.add(scrollPane1, BorderLayout.CENTER);

			btnEnter.addActionListener(enterBtnAction);
			btnCancel.addActionListener(cancelBtnAction);


			prodprop = gp.getProp();
			for (int i=0;i<prodprop.size();i++)
			{
				ProductProperty pp=(ProductProperty)prodprop.get(i);
				ArrayList column = new ArrayList();
				column.add(Integer.toString((theTable.getRowCount() + 1)));
				column.add(pp.getName());
				column.add((pp.getPrice())+ "");
				column.add(pp.getId());
				theTableModel.addRow(column);
			}

			if (theTable.getRowCount() > 0){
				theTable.changeSelection(0, 1, false, false);
			}
				
			addKeyAndContainerListenerRecursively(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void jbInit() throws Exception {
		jPanel1.setLayout(borderLayout1);
		lblproperties.setFont(new java.awt.Font("Dialog", 0, 16));
		lblproperties.setForeground(Color.red);
		lblproperties.setPreferredSize(new Dimension(200, 33));
		btnCancel.setFont(new java.awt.Font("Dialog", 0, 16));
		btnCancel.setPreferredSize(new Dimension(100, 33));
		btnCancel.setText("取消");
		btnEnter.setFont(new java.awt.Font("Dialog", 0, 16));
		btnEnter.setPreferredSize(new Dimension(100, 33));
		btnEnter.setText("确定");
		jPanel2.setLayout(borderLayout2);
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(jPanel2, BorderLayout.CENTER);
		jPanel1.add(jPanel3, BorderLayout.SOUTH);
		jPanel3.add(lblproperties, null);
		jPanel3.add(btnEnter, null);
		jPanel3.add(btnCancel, null);
	}

	public void makeRowVisible(JTable table, int visibleRow) {
		if (table.getColumnCount() == 0)
			return;

		if (visibleRow < 0 || visibleRow >= table.getRowCount()) {
			return;
		}

		Rectangle visible = table.getVisibleRect();
		Rectangle cell = table.getCellRect(visibleRow, 0, true);

		if (cell.y < visible.y) {
			visible.y = cell.y;
			table.scrollRectToVisible(visible);
		} else if (cell.y + cell.height > visible.y + visible.height) {
			visible.y = cell.y + cell.height - visible.height;
			table.scrollRectToVisible(visible);
		}
	}
	public TheTableModel getTheTableModel()
	{
		return theTableModel;
	}
	public TheTable getTheTable()
	{
		return theTable;
	}
	private class TheTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2768646246439099623L;
		private ArrayList data = new ArrayList();
		private String columnNames[] = { "序号", "种类"};

		public Class getColumnClass(int col) {
			if (col == 2) {
				return JLabel.class;
			}
			return Object.class;
		}

		public int getColumnCount() {
			return 2;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return data.size();
		}

		public Object getValueAt(int row, int column) {
			if (row >= data.size())
				return null;

			ArrayList columns = (ArrayList) data.get(row);
			if (column >= columns.size())
				return null;

			return columns.get(column);
		}

		public void clear() {
			data = new ArrayList();
			fireTableDataChanged();
		}

		public void addRow(ArrayList columns) {
			data.add(columns);
			fireTableDataChanged();
			//makeRowVisible(theTable, theTable.getRowCount() - 1);
		}

	}

	private class JLabelTableCellRenderer implements TableCellRenderer {
		private JLabel label = new JLabel();

		public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int col) {
			TableColumn tableColumn = table.getColumnModel().getColumn(col);
			label.setOpaque(true);
			label.setFont(table.getFont());
			label.setSize(tableColumn.getWidth(), table.getRowHeight());
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setText(value.toString());
			if (isSelected) {
				label.setBackground(table.getSelectionBackground());
			} else {
				label.setBackground(table.getBackground());
			}
			return label;
		}
	}

	private class EnterButtonAction implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			holdid=(String)theTableModel.getValueAt(theTable.getSelectedRow(),3);   
			bconfirm = PS_ENTER;
			dispose();
		}

	}

	private class CancelButtonAction implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			bconfirm = PS_ESC;
			dispose();
		}

	}

	private class TheTable extends JTable {
		public TheTable(TableModel tm) {
			super(tm);
			this.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		}

		protected boolean processKeyBinding(
			KeyStroke ks,
			KeyEvent e,
			int condition,
			boolean pressed) {

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				return false;
			}

			return super.processKeyBinding(ks, e, condition, pressed);
		}

	}

	public String getHoldid() {
		return holdid;
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
			switch (e.getKeyCode()) {
				case KeyEvent.VK_0 :
				case KeyEvent.VK_1 :
				case KeyEvent.VK_2 :
				case KeyEvent.VK_3 :
				case KeyEvent.VK_4 :
				case KeyEvent.VK_5 :
				case KeyEvent.VK_6 :
				case KeyEvent.VK_7 :
				case KeyEvent.VK_8 :
				case KeyEvent.VK_9 :
					int selRow = 0;
					if (theTable.getRowCount() < 10) {
						selRow = Integer.parseInt("" + e.getKeyChar()) - 1;
					} else {
						selRow =
							Integer.parseInt("" + e.getKeyChar() + "0") - 1;
						if(selRow==-1)selRow=0;
							
					}
					if (selRow >= 0 && selRow < theTable.getRowCount()) {
						theTable.changeSelection(selRow, 1, false, false);
						makeRowVisible(theTable, selRow);
					}
					break;
				case KeyEvent.VK_ENTER :		
					holdid  = (String)theTableModel.getValueAt(theTable.getSelectedRow(),3);
					bconfirm = PS_ENTER;
					dispose();
					break;
				case KeyEvent.VK_ESCAPE :
					bconfirm = PS_ESC;
					dispose();
					break;
				case KeyEvent.VK_SPACE:
					bconfirm = PS_CLEAR;
					dispose();
					break;
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}
	
	public String getConfrim(){
		return bconfirm;
	}
	
	public static void main(String[] args){

		Vector as = new Vector();
		ProductProperty data = new ProductProperty();
		data.setId("1");
		data.setName("少糖");
		data.setPrice(1.1);
		data.setFlag(0);
		as.add(data);
		
		ProductProperty data1 = new ProductProperty();
		data1.setId("2");
		data1.setName("少糖多冰");
		data1.setPrice(1.2);
		data1.setFlag(1);
		as.add(data1);
		
		ProductProperty data2 = new ProductProperty();
		data2.setId("3");
		data2.setName("少糖少冰");
		data2.setPrice(1.2);
		data2.setFlag(0);		
		as.add(data2);
		
		ProductProperty data6 = new ProductProperty();
		data6.setId("4");
		data6.setName("常温");
		data6.setPrice(1.2);
		data6.setFlag(0);
		as.add(data6);
		
		ProductProperty data3 = new ProductProperty();
		data3.setId("5");
		data3.setName("加热");
		data3.setPrice(1.2);
		data3.setFlag(0);
		as.add(data3);
		
		ProductProperty data4 = new ProductProperty();
		data4.setId("6");
		data4.setName("少糖加热");
		data4.setPrice(1.2);
		data4.setFlag(0);
		as.add(data4);
		
		ProductProperty data5 = new ProductProperty();
		data5.setId("7");
		data5.setName("多糖加热");
		data5.setPrice(1.2);
		data5.setFlag(0);
		as.add(data5);
		
		GoodsProduct gp = new GoodsProduct("2", as);
		
		
		ProductSelect ui = new ProductSelect(gp,"");
		ui.show();
		if(ui.getConfrim() == PS_ENTER)
		{
			System.out.println(ui.getHoldid());
		}
	}

}
