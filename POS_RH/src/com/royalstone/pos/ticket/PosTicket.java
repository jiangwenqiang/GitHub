package com.royalstone.pos.ticket;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.hardware.ConsolePrinter;
import com.royalstone.pos.hardware.IPrinter;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.util.PosConfig;

/**
 * СƱ��ӡģ��Ľ�����
 * @author liangxinbiao
 */
public class PosTicket {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int CENTER = 2;

	protected int ticketWidth = 0;

	
	private int leftMargin = 0;
	private int rightMargin = 0;

	protected HashMap params = null;
	protected String seperator = "";
	protected Element root = null;
	protected IPrinter printer;

	/**
	 * @param filename СƱ��ӡģ����ļ���
	 * @param printer  �����ӡ��
	 * @throws JDOMException
	 * @throws FileNotFoundException
	 */
	public PosTicket(String filename, IPrinter printer)
		throws JDOMException, FileNotFoundException {

		Document doc = (new SAXBuilder()).build(new FileInputStream(filename));
		root = doc.getRootElement();
		String strWidth = root.getAttributeValue("width");
		String strLeftMargin = root.getAttributeValue("leftMargin");
		String strRightMargin = root.getAttributeValue("rightMargin");
		
		leftMargin = Integer.parseInt(strLeftMargin);
		rightMargin = Integer.parseInt(strRightMargin);
		ticketWidth = Integer.parseInt(strWidth) - leftMargin - rightMargin;
		seperator = root.getAttributeValue("seperator");

		this.printer = printer;

	}

	public void setParams(HashMap params) {
		this.params = params;
	}

	/**
	 * ����СƱͷ
	 * @param params СƱ��ӡģ���ļ��и�������ֵ���������ļ��еĸ�ʽΪ:${������}��
	 */
	public void parseHeader(HashMap params) {

		printTrainingFlag();

		this.params = params;
		Element table = root.getChild("Header").getChild("table");
		parseTable(table);
		table = root.getChild("Info").getChild("table");
		parseTable(table);
		printer.println(margin(format("", seperator, ticketWidth, LEFT)));

		table = root.getChild("Content").getChild("Title").getChild("table");
		parseTable(table);
	}
	
    public void parseClearHeader(HashMap params) {

           printTrainingFlag();

           this.params = params;
           Element table = root.getChild("Header").getChild("table");
           parseTable(table);

           table = root.getChild("Info").getChild("table");
           parseTable(table);

           printer.println(margin(format("", seperator, ticketWidth, LEFT)));

//        table = root.getChild("Content").getChild("Title").getChild("table");
//
//        parseTable(table);

       }

    public void parseHeaderWithoutGoods(HashMap params) {

            printTrainingFlag();

            this.params = params;
            Element table = root.getChild("Header").getChild("table");
            parseTable(table);

            table = root.getChild("ClearTicket").getChild("table");
            parseTable(table);

            printer.println(margin(format("", seperator, ticketWidth, LEFT)));

           // parseTable(table);

        }

	/**
	 * �ش�СƱʱ��СƱͷ����,��СƱͷ�д�ӡ�ش�����
	 * @param params СƱ��ӡģ���ļ��и�������ֵ
	 * @param tag �ش���Ϣ
	 */
	public void reprintparseHeader(HashMap params, String tag) {

		printTrainingFlag();

		this.params = params;
		Element table = root.getChild("Header").getChild("table");
		parseTable(table);

		table = root.getChild("Info").getChild("table");
		parseTable(table);

		printer.println(margin(format("", seperator, ticketWidth, LEFT)));
		printer.println(margin(format(tag, seperator, ticketWidth, CENTER)));
		table = root.getChild("Content").getChild("Title").getChild("table");

		parseTable(table);

	}

	/**
	 * ����СƱ��������Ϣ
	 * @param params СƱ��ӡģ���ļ��и�������ֵ
	 */
	public void parseSale(HashMap params) {

		this.params = params;
		Element table =
			root.getChild("Content").getChild("Sale").getChild("table");
		parseTable(table);

		printTrainingFlagInRandom();

	}

	/**
	 * ����СƱ���ۿ���Ϣ
	 * @param params СƱ��ӡģ���ļ��и�������ֵ
	 */
	public void parseDiscount(HashMap params) {

		this.params = params;
		Element table =
			root.getChild("Content").getChild("Discount").getChild("table");
		parseTable(table);

	}

	/**
	 * ����СƱ�ĺϼ���Ϣ
	 * @param params СƱ��ӡģ���ļ��и�������ֵ
	 */
	public void parseSubtotal(HashMap params) {

		this.params = params;
		Element table =
			root.getChild("Content").getChild("Subtotal").getChild("table");
		parseTable(table);

	}
	
	public void parseCount(HashMap params){
		
		this.params = params;
		Element table = 
			root.getChild("Content").getChild("Count").getChild("table");
		parseTable(table);
		}

	/**
	 * ����СƱ��֧����Ϣ
	 * @param params СƱ��ӡģ���ļ��и�������ֵ
	 */
	public void parsePayment(HashMap params) {

		this.params = params;
		Element table =
			root.getChild("Content").getChild("Payment").getChild("table");

		parseTable(table);

		printTrainingFlagInRandom();

	}
	
	/**
	 * ����СƱ�Ļ�Ա���ֶһ���Ϣ
	 * @param params СƱ��ӡģ���ļ��и�������ֵ
	 */
	public void parsePlan(HashMap params) {
		this.params = params;
		Element table =
			root.getChild("Content").getChild("Plan").getChild("table");

		parseTable(table);
	}

	/**
	 * ����СƱ�Ĺ��˿���Ϣ
	 * @param params СƱ��ӡģ���ļ��и�������ֵ
	 */
	public void parseLoanCard(HashMap params) {
		this.params = params;
		Element table =
			root.getChild("Content").getChild("LoanCard").getChild("table");

		parseTable(table);
	}
   	public void parseMemberCard(HashMap params) {
		this.params = params;
		Element table =
			root.getChild("Content").getChild("MemberCard").getChild("table");

		parseTable(table);
	}
	/**
	 * ����СƱ��Ʊβ��Ϣ
	 * @param params СƱ��ӡģ���ļ��и�������ֵ
	 */
	public void parseTrail(HashMap params) {

		this.params = params;
		Element table =
			root.getChild("Content").getChild("Total").getChild("table");

		parseTable(table);

		printer.println(
			margin(
				format(
					Formatter.getTime(new Date()),
					seperator,
					ticketWidth,
					CENTER)));

		table = root.getChild("Trail").getChild("table");

		parseTable(table);

		printTrainingFlag();

		printer.feed(getFeedLines());
		printer.cut();
	}

	/**
	 * @return ���غ�̨���õ���ֽǰ�Ĵ�ӡ����
	 */
	public int getFeedLines() {
		int feedLines = 2;
		String strFeedLines = PosConfig.getInstance().getString("CUTLINES");
		if (strFeedLines != null) {
			try {
				feedLines = Integer.parseInt(strFeedLines);
				if (feedLines > 10 || feedLines < 2)
					feedLines = 2;
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
				feedLines = 2;
			}
		}
		return feedLines;
	}

	/**
	 * �ش�СƱʱ��СƱβ����
	 * @param params СƱ��ӡģ���ļ��и�������ֵ
	 * @param tag �ش���Ϣ
	 */
	public void reprintparseTrail(
		HashMap params,
		String tag,
		boolean isShowTotalInfo) {

		this.params = params;
		Element table = null;
		if (isShowTotalInfo) {
			table =
				root.getChild("Content").getChild("Total").getChild("table");

			parseTable(table);
		}
		printer.println(margin(format(tag, seperator, ticketWidth, CENTER)));
		printer.println(
			margin(
				format(
					Formatter.getTime(new Date()),
					seperator,
					ticketWidth,
					CENTER)));

		table = root.getChild("Trail").getChild("table");

		parseTable(table);

		printTrainingFlag();

		printer.feed(getFeedLines());
		printer.cut();
	}

	/**
	 * ��ֽ 
	 */
	public void cutPaper() {
		printer.cut();
	}

	public void parseButtom(HashMap params) {

		this.params = params;
		printer.println(
			margin(
				format(
					Formatter.getTime(new Date()),
					seperator,
					ticketWidth,
					CENTER)));

		Element table = root.getChild("Trail").getChild("table");

		parseTable(table);

		printer.feed(getFeedLines());
		printer.cut();

	}

	/**
	 * ���Է���
	 * @deprecated
	 */
	public void parseTicket() {

		parseHeader(null);
		parseSale(null);
		parseDiscount(null);
		parsePayment(null);
		parseTrail(null);

	}

	/**
	 * �������СƱģ��������Html��Table������СƱ�ĸ�ʽ	 
	 * @param table
	 */
	protected String parseTable(Element table) {

		if (table == null)
			return null;

		String strColumnCount = table.getAttributeValue("columns");
		int columnCount = Integer.parseInt(strColumnCount);
		int columnsWidth = ticketWidth / columnCount;
		List rows = table.getChildren("row");
		Iterator rowIter = rows.iterator();

		StringBuffer line = null;
		StringBuffer page =  null;
		while (rowIter.hasNext()) {
			Element row = (Element) rowIter.next();
			String visible = row.getAttributeValue("visible");
			if (visible != null && parseContent(visible).equals("false")) {
				return null;
			}
			List columns = row.getChildren("column");
			Iterator columnIter = columns.iterator();
			line = new StringBuffer();
			int printColumnCount = 0;
			while (columnIter.hasNext()) {
				Element column = (Element) columnIter.next();
				String strSpan = column.getAttributeValue("span");
				String strAlign = column.getAttributeValue("align");
				int align = LEFT;
				if (strAlign == null
					|| strAlign.equals("")
					|| strAlign.equals("left")) {
					align = LEFT;
				} else if (strAlign.equals("center")) {
					align = CENTER;
				} else if (strAlign.equals("right")) {
					align = RIGHT;
				}
				int span = 1;
				if (strSpan != null && !strSpan.equals("")) {
					span = Integer.parseInt(strSpan);
				}
				printColumnCount += span;
				if (printColumnCount <= columnCount) {
					String content = parseContent(column.getText().trim());
					line.append(
						format(content, " ", span * columnsWidth, align));
				}
			}
			if ((line.toString().trim().length()) > 0) {
				printer.println(margin(line.toString()));
				if(page == null)
				{
					page = new StringBuffer();
				}
				page.append(margin(line.toString())+ProductTickt.spliter);
			}
		}
		return page.toString();
	}

	/**
	 * �������ݣ����ô������ĸ�������ֵ�滻����
	 * @param content 
	 * @return
	 */
	private String parseContent(String content) {
		String result = content;
		if (params != null && params.size() > 0) {
			Set keySet = params.keySet();
			Iterator iter = keySet.iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String value = (String) params.get(key);
				if (value == null)
					value = "";
				if (value != null) {
					result = result.replaceAll("\\Q" + key + "\\E", value);
				}
			}
		}
		return result;
	}

	/**
	 * ���̶����ȺͶ��뷽ʽ�ø����ַ������ض����ַ���
	 * @param s 
	 * @param fill 
	 * @param width
	 * @param justify
	 * @return
	 */
	protected String format(String s, String fill, int width, int justify) {

		int i = 0;
		if ((s.getBytes()).length > width) {
			if ((s.getBytes()).length > s.length()) {
				StringBuffer buf = new StringBuffer();
				int count = 0;
				for (int j = 0; j < s.length() - 1; j++) {
					String str = s.substring(j, j + 1);
					if (str.getBytes().length > 1) {
						count += 2;
					} else {
						count++;
					}
					if (count <= width) {
						buf.append(str);
					}
				}
				s = buf.toString();
			} else {
				s = s.substring(0, width - 1);
			}
		}

		StringBuffer tmp = new StringBuffer();

		switch (justify) {

			case LEFT :
				tmp.append(s);
				for (i = 0; i < width - (s.getBytes()).length; i++)
					tmp.append(fill);
				break;
			case RIGHT :
				for (i = 0; i < width - (s.getBytes()).length - 1; i++)
					tmp.append(fill);
				tmp.append(s);
				tmp.append(" ");
				break;

			case CENTER :
				int left = (width - (s.getBytes()).length) / 2;
				int right = left;

				if (((width - (s.getBytes()).length) % 2) > 0)
					right++;

				for (i = 0; i < left; i++)
					tmp.append(fill);
				tmp.append(s);
				for (i = 0; i < right; i++)
					tmp.append(fill);
				break;
			default :
				tmp.append(s);
				for (i = 0; i < width - (s.getBytes()).length; i++)
					tmp.append(fill);
				break;
		}
		return tmp.toString();
	}

	/**
	 * ���ַ������Ҽӿո�
	 * @param s Ҫ������ַ���
	 * @return �������ַ���
	 */
	protected String margin(String s) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < leftMargin; i++) {
			buf.append(" ");
		}
		buf.append(s);
		for (int i = 0; i < rightMargin; i++) {
			buf.append(" ");
		}
		return buf.toString();
	}

	/**
	 * �ڴ�ӡ���ϴ�ӡ�ַ����ӻ��У��ַ�������һ�о������Ҽӷָ��
	 * @param s Ҫ��ӡ���ַ���
	 */
	public void println(String s) {
		printer.println(margin(format(s, seperator, ticketWidth, CENTER)));
	}
	
	public void println(String s,int align,String seperator) {
		printer.println(margin(format(s, seperator, ticketWidth, align)));
	}


	/**
	 * �ڴ�ӡ���ϴ�ӡ�ַ����ӻ��У��ַ�������һ�в��ӷָ��
	 * @param s Ҫ��ӡ���ַ���
	 */
	public void printlnWithoutSeperator(String s) {
		printer.println(margin(format(s, "", ticketWidth, LEFT)));
	}

	/**
	 * ��ӡ��ѵ��� 
	 */
	public void printTrainingFlag() {
		if (PosContext.getInstance().isTraining()) {
			printer.println(margin(format("��ѵСƱ", "-", ticketWidth, CENTER)));
		}
	}

	/**
	 * �������ɵ�������Ƿ���(1,2,5,7)���������Ƿ��ӡ��ѵ��� 
	 */
	public void printTrainingFlagInRandom() {
		long randomNum = Math.round(Math.random() * 10);
		if (randomNum == 2
			|| randomNum == 5
			|| randomNum == 7
			|| randomNum == 1) {
			printTrainingFlag();
		}
	}

	/**
	 * ��������������
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		HashMap params = new HashMap();

		params.put("${Header1}", "����BP������");
		params.put("${ShopID}", "B001");
		params.put("${PosID}", "P001");
		params.put("${SheetID}", "17");
		params.put("${Cashier}", "0001");
		params.put("${Date}", "2004.06.04");
		params.put("${GoodsName}", "AILEG102AԲ��ӡ��T��");
		params.put("${Barcode}", "000496");
		params.put("${Quantity}", "1");
		params.put("${Price}", "25.00");
		params.put("${Amount}", "25.00");
		params.put("${DiscDesc}", "�Ż�:10%");
		params.put("${DiscValue}", "2.50");
		params.put("${PayType}", "�ֽ�");
		params.put("${Currency}", "�����");
		params.put("${PayAmount}", "600.00");
		params.put("${TotalPayAmount}", "600.00");
		params.put("${Change}", "575.00");
		params.put("${ActualPayAmount}", "25.00");
		params.put("${Trail1}", "��ӭ��������BP������-����绰:020-12345678");
		params.put("${Trail2}", "�뱣������СƱ,��Ϊ�˻���ƾ֤");

		;
		PosTicket posTicket =
			new PosTicket("posticket.xml.bak", (new ConsolePrinter()));
		posTicket.parseHeader(params);
		posTicket.parseSale(params);
		posTicket.parseDiscount(params);
		posTicket.parsePayment(params);
		posTicket.parseTrail(params);

	}

	public int getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public int getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}

	public int getTicketWidth() {
		return ticketWidth;
	}

	public void setTicketWidth(int ticketWidth) {
		this.ticketWidth = ticketWidth;
	}
}
