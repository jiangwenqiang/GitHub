package com.royalstone.pos.hardware;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import com.royalstone.pos.ticket.ProductTickt;

public class NetPrinter implements Printable {

	private String printpage;
	private double width;
	private double height;
	private String printername;	
	
	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void printpage(){
		// TODO 自动生成方法存根
		PrintService printService = null;
		
		if(printername.equals("default"))
		{
			printService =PrintServiceLookup.lookupDefaultPrintService();		
		}
		
		PrinterJob job = PrinterJob.getPrinterJob();
		try {
			job.setPrintService(printService);
		} catch (PrinterException e) {
			e.printStackTrace();
		}
		PageFormat pageFormat = job.defaultPage();
	    Paper paper = new Paper();
	    paper.setSize(width, height);
	    paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
	    pageFormat.setPaper(paper);
		job.setPrintable(this,pageFormat);
		try {
			job.print();
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}

	public int print(Graphics g, PageFormat pf, int page) 
	throws PrinterException {
		if (page == 0) 
		{
			Graphics2D g2 = (Graphics2D)g;
			
		    g2.setFont(new Font("Serif", Font.PLAIN, 10));
		    g2.setPaint(Color.black);
		    g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
		    
		    String prints[] = printpage.split(ProductTickt.spliter);
		    int y = 10;
		    
		    for(int i = 0; i < prints.length ; ++ i)
		    {
		    	g2.drawString(prints[i], 0, y);
		    	y+=15;
		    }

		    return (PAGE_EXISTS);
		} else
			return (NO_SUCH_PAGE);
	}

	public String getPrintpage() {
		return printpage;
	}
	public void clearPrintpage()
	{
		this.printpage = null;
	}

	public void setPrintpage(String printpage) {
		this.printpage = printpage;
	}
	public void addPrintline(String line){
		if(this.printpage == null) this.printpage = line;
		else this.printpage += (line);
	}

	public String getPrintername() {
		return printername;
	}

	public void setPrintername(String name) {
		this.printername = name;
	}

	public NetPrinter(String name) {
		super();
		this.printername = name;
	}
	
	public void setPageSize(double width,double height)
	{	
		this.width = width;
		this.height = height;
	}
	
	public static void main(String[] args) 
	{
		NetPrinter netprinter =  new NetPrinter("default");
		netprinter.setPrintpage("操作员:1234&&"+
							"日期:2014-05-14&&"+
							"编码:1234567890&&"+
							"名称:芙蓉月饼&&"+
							"属性:少糖&&");
		netprinter.setPageSize(155, 105);
		netprinter.printpage();
	}
}
