package com.royalstone.pos.ticket;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;

import org.jdom.Element;
import org.jdom.JDOMException;

import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.hardware.IPrinter;
import com.royalstone.pos.hardware.NetPrinter;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.util.Value;

public class ProductTickt extends PosTicket
{
	private int feedline = 0;
	private double paperHeight = 155;
	private double paperWidth = 105;
	
	public final static String spliter = "&&";
	
	public ProductTickt(String filename,NetPrinter net_printer,IPrinter printer) throws JDOMException, FileNotFoundException {
		super(filename, printer);
		
		String feedlineStr = root.getAttributeValue("feedline");
		if(feedlineStr != null) feedline = Integer.parseInt(feedlineStr);
		
		String strHeight = root.getAttributeValue("pheight");
		String strpWidth = root.getAttributeValue("pwidth");
		if(strpWidth != null) paperWidth = Double.parseDouble(strpWidth);
		if(strHeight != null) paperHeight = Double.parseDouble(strHeight);	
		
		this.net_printer =  net_printer;
		this.net_printer.setPageSize(getPaperWidth(), getPaperHeight());
	}
	
	private NetPrinter net_printer;
	
	public HashMap getHeader(PosContext context,int index,int total) {
		HashMap paras = new HashMap();
		paras.put("${ShopID}", context.getStoreid());
		paras.put("${PosID}", context.getPosid());
		//createrandomnum randomnum = new createrandomnum();
		//String num = Integer.toString(randomnum.getrandomnum());
		paras.put(
			"${SheetID}",
			Integer.toString(context.getSheetid()));
		paras.put("${Cashier}", context.getCashierid());
		paras.put("${Date}", Formatter.getDate(new Date()));
		paras.put("${Time}", Formatter.getTime(new Date()));
		paras.put("${DateTime}", Formatter.getDate(new Date()) + " " + Formatter.getTime(new Date()));
		paras.put("${OrderID}", Integer.toString(context.getOrderidOld()));
		paras.put("${PageNo}",String.valueOf(index)+"/"+String.valueOf(total));
		return paras;
	}
	
	public HashMap getSale(Sale s) 
	{
		HashMap paras = new HashMap();
		paras.put("${GoodsNo}",s.getVgno());
		paras.put("${GoodsName}", s.getName());
		paras.put("${Barcode}", s.getVgno());
		paras.put("${Quantity}", s.getQtyStr());
		paras.put("${Price}", (new Value(s.getStdPrice())).toString());
		paras.put("${Product}", s.getProerties());
		paras.put(
			"${Amount}",
			(new Value(s.getStdValue())).toString());
		return paras;
	}
	
	public boolean print(PosContext c, Sale s,int index,int total) 
	{
		parseHeader(getHeader(c, index, total));
		parseSale(getSale(s));
		printer.println(margin(format("", seperator, ticketWidth, LEFT)));
		net_printer.printpage();
		net_printer.clearPrintpage();
		return true;
	}

	public void parseHeader(HashMap params) 
	{
		printTrainingFlag();
		this.params = params;
		Element table = root.getChild("Header").getChild("table");
		parseTable(table);
		table = root.getChild("Info").getChild("table");
		parseTable(table);
		table = root.getChild("Content").getChild("Title").getChild("table");
		parseTable(table);
	}	
	
	protected String parseTable(Element table)
	{
		String s = super.parseTable(table);
		if(s != null) net_printer.addPrintline(s);
		return s;
	}
	
	public NetPrinter getNet_printer() {
		return net_printer;
	}
	public void setNet_printer(NetPrinter net_printer) {
		this.net_printer = net_printer;
	}

	public void feed() {
		this.printer.feed(feedline);
	}
	
	public double getPaperHeight() {
		return paperHeight;
	}

	public void setPaperHeight(int paperHeight) {
		this.paperHeight = paperHeight;
	}

	public double getPaperWidth() {
		return paperWidth;
	}

	public void setPaperWidth(int paperWidth) {
		this.paperWidth = paperWidth;
	}
}
