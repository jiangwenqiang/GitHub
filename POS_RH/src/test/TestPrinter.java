package test;

/**
 * Created by IntelliJ IDEA.
 * User: fire
 * Date: 2005-8-11
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JPanel;

/**
 * @author liangxinbiao
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class TestPrinter extends JPanel implements Printable {

	//public static final  double INCH = 1.0;

	public static void main(String[] args) {

//		定位默认的打印服务
		PrintService printService =
			PrintServiceLookup.lookupDefaultPrintService();		
		PrinterJob job = PrinterJob.getPrinterJob();
		try {
			job.setPrintService(printService);
		} catch (PrinterException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		
		PageFormat pageFormat = job.defaultPage();
		
	    Paper paper = new Paper();
	    paper.setSize(155, 105);
	    double wideth = paper.getWidth();
	    double height = paper.getHeight();
	    double margin = 1; // half inch
	    paper.setImageableArea(0, 0, wideth - margin * 2, height- margin * 2);
	    //paper.setImageableArea(0,0, wideth, height);
	    pageFormat.setPaper(paper);

		job.setPrintable(new TestPrinter(),pageFormat);


//		if (job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException e) {
				e.printStackTrace();
			}
//		}

	}

	public int print(Graphics g, PageFormat pf, int page)
		throws PrinterException {
		if (page == 0) 
		{
			double x = pf.getImageableX();
			double y = pf.getImageableY();

			Graphics2D g2 = (Graphics2D) g;
		    g2.setFont(new Font("Serif", Font.PLAIN, 14));
		    g2.setPaint(Color.black);
		    g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
		    
		    g2.drawString("商品编码:1234567890", 0, 8);
		    g2.drawString("商品编码:2234567890", 0, 25+2);
		    g2.drawString("商品编码:3234567890", 0, 40+2);
		    g2.drawString("商品编码:4234567890", 0, 55+2);
		    g2.drawString("商品编码:5234567890", 0, 70+2);
		
		    Rectangle2D outline = new Rectangle2D.Double(pf.getImageableX(), pf.getImageableY(), pf
		            .getImageableWidth(), pf.getImageableHeight());
		    g2.draw(outline);
		    
			return (PAGE_EXISTS);
		} else
			return (NO_SUCH_PAGE);
	}

}
