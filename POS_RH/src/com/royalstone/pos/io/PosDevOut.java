package com.royalstone.pos.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.card.SHCardQueryVO;
import com.royalstone.pos.common.CashBox;
import com.royalstone.pos.common.Payment;
import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.common.SheetBrief;
import com.royalstone.pos.common.SheetValue;
import com.royalstone.pos.core.PaymentList;
import com.royalstone.pos.core.PosCore;
import com.royalstone.pos.core.PosSheet;
import com.royalstone.pos.core.SaleList;
import com.royalstone.pos.favor.Discount;
import com.royalstone.pos.gui.HoldList;
import com.royalstone.pos.gui.MainUI;
import com.royalstone.pos.hardware.NetPrinter;
import com.royalstone.pos.hardware.POSCustDisplay;
import com.royalstone.pos.hardware.POSPrinter;
import com.royalstone.pos.hardware.POSPrinter1;
import com.royalstone.pos.hardware.POSPrinterEx;
import com.royalstone.pos.journal.ChangeCard4Reprint;
import com.royalstone.pos.journal.LoanCard4Reprint;
import com.royalstone.pos.journal.MemberCard4Reprint;
import com.royalstone.pos.ticket.CreatePosSheet;
import com.royalstone.pos.ticket.PosInvoice;
import com.royalstone.pos.ticket.PosTicket;
import com.royalstone.pos.ticket.ProductTickt;
import com.royalstone.pos.ticket.createrandomnum;
import com.royalstone.pos.util.Exchange;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.util.PosConfig;
import com.royalstone.pos.util.Value;
;


/**
   @version 1.0 2004.05.14
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

public class PosDevOut {

	private StringBuffer inputLine = new StringBuffer();
	private MainUI mainUI;
	private static PosTicket posTicket;
	private static ProductTickt posTicketEx;
	private static PosInvoice posInvoice;
	
	private static POSCustDisplay custDisplay;

	public PosDevOut() {
	}

	public PosDevOut(MainUI mainUI) {
		this.mainUI = mainUI;
	}

	public void setMainUI(MainUI mainUI) {
		this.mainUI = mainUI;
	}

	public MainUI getMainUI() {
		return this.mainUI;
	}

	public void displayAtInputLine(int value) {
		inputLine.append((char) value);
		mainUI.setInputField(inputLine.toString());
	}

	public void clearInputLine() {
		if (inputLine.length() > 0) {
			inputLine.delete(0, inputLine.length());
		}
		mainUI.setInputField(inputLine.toString());
	}

	public void setInputLine(String s) {
		inputLine = new StringBuffer(s);
		mainUI.setInputField(inputLine.toString());

	}

	public void setStep(int i) {
		mainUI.setStep(i);
	}

	public void clear() {
		mainUI.clear();
	}

	public void init() {
		// Initialize GUI compoments here ...
	}

	public static synchronized PosDevOut getInstance() {
		if (out == null) {
			out = new PosDevOut();
			//printer = POSPrinter.getInstance();
			//printer1 = POSPrinter1.getInstance();
			//POSScanner scanner = POSScanner.getInstance();
			custDisplay = POSCustDisplay.getInstance();
			try {
				if (PosContext.getInstance().getPrintType().compareTo("TICKET")
					== 0) {
					printer = POSPrinter.getInstance();
					printerEx = POSPrinterEx.getInstance();
					posTicket = new PosTicket("posticket.xml", printer);
					posTicketEx = new ProductTickt("prodticket.xml",new NetPrinter("default"),printerEx);
				} else {
					printer1 = POSPrinter1.getInstance();
					posInvoice = new PosInvoice("POSInvoice.xml", printer1);
				}
			} catch (Exception ex) {
				posInvoice = null;
				posTicket = null;
				ex.printStackTrace();
			}
		}

		return out;
	}

	public static PosDevOut getInstance(MainUI mainUI) {
		if (out == null)
			out = new PosDevOut(mainUI);
		return out;
	}

	// added on 2004.05.30 by Mengluoyi
	public void displayState(String state_name) {
		mainUI.setStatus(state_name);
	}

	public void display(PosContext con) {
		if(mainUI!=null){
			mainUI.setPosNo(con.getPosid());
			//TODO 屏蔽班次信息  沧州富达 by fire  2005_5_11 
			//mainUI.setDutyNo(Integer.toString(con.getShiftid()));
			mainUI.setCashier(con.getCashierid());
			mainUI.setTransNo(Integer.toString(con.getSheetid()));
			mainUI.setConnStatus(con.getModeStr());
			mainUI.setHoldNo(Integer.toString(con.getHeldCount()));
			//TODO 屏蔽日期信息 沧州富达 by fire  2005_5_11 
			//mainUI.setWorkDay(con.getWorkDate().toString());
		}
	}
	// 提货订单专业显示
//	public void displayes(PosContext con) {
//		if(mainUI!=null){
//			mainUI.setPosNo(con.getPosid());
			//TODO 屏蔽班次信息  沧州富达 by fire  2005_5_11 
			//mainUI.setDutyNo(Integer.toString(con.getShiftid()));
//			mainUI.setCashier(con.getCashierid());
//			mainUI.setTransNo(Integer.toString(con.Order_id()));
//			mainUI.setConnStatus(con.getModeStr());
//			mainUI.setHoldNo(Integer.toString(con.getHeldCount()));
			//TODO 屏蔽日期信息 沧州富达 by fire  2005_5_11 
			//mainUI.setWorkDay(con.getWorkDate().toString());
//		}
//	}
	
	public void displayConnStatus(PosContext con) {
		if(mainUI!=null){
			mainUI.setConnStatus(con.getModeStr());	
		}
		
	}

	/**在mainUI中显示营业员信息
	 * @param waiter_show
	 * @return void
	 * */
	public void dispWaiter(String waiter_show) {
		mainUI.setWaiterNo(waiter_show);

	}

	/**设置营业员信息
	 * @param waiter_show
	 * @return void
	 * */
	public void setWaiter(String waiter_show) {
		PosContext con = PosContext.getInstance();
		con.setWaiterid(waiter_show);
	}

	/**打印销售清单，为深圳真冰娱乐场而开发
	 * @param context
	 * */
	public void printInvoice() {

		PosContext con = PosContext.getInstance();
		int items =
			Integer.parseInt(PosConfig.getInstance().getString("ITEMS"));
		String filename = "reprint/reprintsheet.xml";
		System.out.println("文件名：" + filename);
		CreatePosSheet createpossheet = new CreatePosSheet();
		SaleList falsesalelist =
			createpossheet.CreateSalelst(filename, "falsesale");
		SaleList salelist = createpossheet.CreateSalelst(filename, "sale");
		PaymentList paymentlist = createpossheet.CreatePaylst(filename);
		String storeid = createpossheet.getstoreid(filename);
		String posid = createpossheet.getposid(filename);
		String cashierid = createpossheet.getcashierid(filename);
		String sheetid = createpossheet.getsheetid(filename);
		String workdate = createpossheet.getworkdate(filename);

		PosSheet possheet = new PosSheet();
		possheet.setFalseSaleList(falsesalelist);
		possheet.setSaleList(salelist);
		possheet.setPaymentList(paymentlist);
		possheet.updateValue();
		SheetValue sheetvalue = possheet.getValue();
		int pages = pages(falsesalelist, paymentlist);

		possheet.print();

		for (int k = 0; k < pages; k++) {

			printHeader(
				storeid,
				posid,
				cashierid,
				sheetid,
				workdate,
				Integer.toString(k + 1));

			if ((k + 1) * items >= possheet.getFalseSaleLen()) {
				for (int i = k * items; i < possheet.getFalseSaleLen(); i++) {
					printdisplay(possheet.getFalseSale(i), sheetvalue);
				}

				for (int i = 0;
					i < possheet.getPayLen();
					i++) { //it must be estimated for payment here
					printdisplay(possheet.getPayment(i), sheetvalue);
				}

			} else {
				for (int i = k * items; i < (k + 1) * items; i++) {
					printdisplay(possheet.getFalseSale(i), sheetvalue);
				}
				printTrail(k, pages);
			}

			if (k == pages - 1)
				printTrail(sheetvalue, k, pages);
		}

	}
	
	
	

	private int pages(SaleList salelst, PaymentList paymentlst) {
		int pages = 1;
		PosConfig config = PosConfig.getInstance();
		int items =
			Integer.parseInt(PosConfig.getInstance().getString("ITEMS"));

		int temp = salelst.size() + paymentlst.size();
		if (temp % items == 0) {
			pages = temp / items;
		} else {
			pages = temp / items + 1;
		}

		return pages;
	}

	/**重打上一单小票,called by pos.java
	 * @param context
	 * @return void
	 * */
	public void dispLastPrint(PosContext context) {

		PosContext con = PosContext.getInstance();

		String filename = "reprint/reprintsheet.xml";
		System.out.println("文件名：" + filename);
		CreatePosSheet createpossheet = new CreatePosSheet();
		SaleList falsesalelist =
			createpossheet.CreateSalelst(filename, "falsesale");
		SaleList salelist = createpossheet.CreateSalelst(filename, "sale");
		PaymentList paymentlist = createpossheet.CreatePaylst(filename);
		String storeid = createpossheet.getstoreid(filename);
		String posid = createpossheet.getposid(filename);
		String cashierid = createpossheet.getcashierid(filename);
		String sheetid = createpossheet.getsheetid(filename);
		String workdate = createpossheet.getworkdate(filename);
		String orderid = createpossheet.getorderid(filename);

		PosSheet possheet = new PosSheet();
		possheet.setFalseSaleList(falsesalelist);
		possheet.setSaleList(salelist);
		possheet.setPaymentList(paymentlist);
		possheet.updateValue();
		SheetValue sheetvalue = possheet.getValue();
		
		possheet.print();
		boolean Bring = false;
		int id;
		id = Integer.parseInt(orderid);
		
		// 对预销售与正常销售的判断
		if (possheet.getFalseSale(0).getVgno().equals("999999")){
			reprintHeaders(storeid,posid,cashierid,sheetid,workdate,orderid,"重打小票");
			} 
		else{
			if (!orderid.equals("0")){
				if (!possheet.getFalseSale(0).getVgno().equals("999999")){
				  Bring  = true;
				}
			}
			reprintHeader(storeid, posid, cashierid, sheetid, workdate, "重打小票");
		}
        //-----------------
          MemberCard4Reprint memberCard =MemberCard4Reprint.getInstance("reprint/reprintsheet.xml");
          if(memberCard!=null){
               this.dispMemberCardHeader(memberCard);
          }
          
          // 兑换
          ChangeCard4Reprint changerCard =ChangeCard4Reprint.getInstance("reprint/reprintsheet.xml");

           this.dispCouponHeader();
        int count = 0;
		for (int i = 0; i < possheet.getFalseSaleLen(); i++) {
			reprintdisplay(possheet.getFalseSale(i), sheetvalue);
			count += possheet.getFalseSale(i).getQtyInt();
			}
		//商品总量
		if (needAuth("PRINT_TALCOUNT")){
		 	display(count, true);
		}
		// 打印预单号
		if (Bring){
			display (Integer.parseInt(orderid),false);
			}

		LoanCard4Reprint loancard =
			new LoanCard4Reprint("reprint/reprintsheet.xml");
		boolean isShowTotalInfo = true;
		for (int i = 0; i < possheet.getPayLen(); i++) {
			reprintdisplay(possheet.getPayment(i), sheetvalue);
			if (possheet.getPayment(i).getType() == Payment.CARDLOAN
				|| possheet.getPayment(i).getType() == Payment.CARDSHOP) {
				String cardno = possheet.getPayment(i).getCardno();
				reprintdisplay(loancard);
			}
			if (possheet.getPayment(i).getReason() == Payment.CASHIN
				|| possheet.getPayment(i).getReason() == Payment.CASHOUT  //zhouzhou 
				|| possheet.getPayment(i).getReason() == Payment.RaCard) {
				isShowTotalInfo = false;
				break;
			}
			if (possheet.getPayment(i).getReason() == Payment.FLEE
				|| possheet.getPayment(i).getReason() == Payment.OILTEST
				|| possheet.getPayment(i).getReason() == Payment.SAMPLE) {
					sheetvalue=new SheetValue();
			}
		}
		
		// 积分竞换
		if (changerCard != null){
			this.Plandisplay(changerCard.getPlan(),changerCard.getCardPoint());
			}
			
         if(memberCard!=null){
               this.display(memberCard);
          }
		reprintdisplayTrail(sheetvalue, "重打小票", isShowTotalInfo);

		System.out.println("完成小票重打........");
	}
	
    /**
     * 判断参数配置中是否可以使用这种功能 
     */
    private boolean needAuth(String name){
    	PosConfig config = PosConfig.getInstance();
    	String authFlag = config.getString(name);
    	if (authFlag.equals("ON"))
    		return true;
    	return false;
    	}
    

    public void printAnyTicket(PosContext context,String ticketFile) {

        //PosContext con = PosContext.getInstance();

        String filename = ticketFile;
        System.out.println("文件名：" + filename);
        CreatePosSheet createpossheet = new CreatePosSheet();
        SaleList falsesalelist =
            createpossheet.CreateSalelst(filename, "falsesale");
        SaleList salelist = createpossheet.CreateSalelst(filename, "sale");
        PaymentList paymentlist = createpossheet.CreatePaylst(filename);
        String storeid = createpossheet.getstoreid(filename);
        String posid = createpossheet.getposid(filename);
        String cashierid = createpossheet.getcashierid(filename);
        String sheetid = createpossheet.getsheetid(filename);
        String workdate = createpossheet.getworkdate(filename);
        String orderid = createpossheet.getorderid(filename);

        PosSheet possheet = new PosSheet();
        possheet.setFalseSaleList(falsesalelist);
        possheet.setSaleList(salelist);
        possheet.setPaymentList(paymentlist);
        possheet.updateValue();
        SheetValue sheetvalue = possheet.getValue();

        possheet.print();
 
        boolean Bring = false;
		int id;
		id = Integer.parseInt(orderid);
		
		// 对预销售与正常销售的判断
		if (possheet.getFalseSale(0).getVgno().equals("999999")){
			reprintHeaders(storeid,posid,cashierid,sheetid,workdate,orderid,"重打任意小票");
			} 
		else{
			if (!orderid.equals("0")){
				if (!possheet.getFalseSale(0).getVgno().equals("999999")){
				  Bring  = true;
				}
			}
			reprintHeader(storeid, posid, cashierid, sheetid, workdate, "重打任意小票");
		}
        //-----------------
          MemberCard4Reprint memberCard =MemberCard4Reprint.getInstance(ticketFile);
          if(memberCard!=null){
               this.dispMemberCardHeader(memberCard);
          }

           this.dispCouponHeader();
        int count = 0;
        for (int i = 0; i < possheet.getFalseSaleLen(); i++) {
            reprintdisplay(possheet.getFalseSale(i), sheetvalue);
            count += possheet.getFalseSale(i).getQtyInt();

        }
        
		// 商品总量
        if ( needAuth("PRINT_TALCOUNT") ){
        	display(count, true);
        }
		// 打印预单号
		if (Bring){
			display (Integer.parseInt(orderid),false);
			}


        LoanCard4Reprint loancard =
            new LoanCard4Reprint(ticketFile);
        boolean isShowTotalInfo = true;
        for (int i = 0; i < possheet.getPayLen(); i++) {
            reprintdisplay(possheet.getPayment(i), sheetvalue);
            if (possheet.getPayment(i).getType() == Payment.CARDLOAN
                || possheet.getPayment(i).getType() == Payment.CARDSHOP) {
                String cardno = possheet.getPayment(i).getCardno();
                reprintdisplay(loancard);
            }
            if (possheet.getPayment(i).getReason() == Payment.CASHIN
                || possheet.getPayment(i).getReason() == Payment.CASHOUT //zhouzhou Add
	            || possheet.getPayment(i).getReason() == Payment.RaCard) {
                isShowTotalInfo = false;
                break;
            }
            if (possheet.getPayment(i).getReason() == Payment.FLEE
                || possheet.getPayment(i).getReason() == Payment.OILTEST
                || possheet.getPayment(i).getReason() == Payment.SAMPLE) {
                    sheetvalue=new SheetValue();
            }
        }
         if(memberCard!=null){
               this.display(memberCard);
          }
        reprintdisplayTrail(sheetvalue, "重打任意小票", isShowTotalInfo);

        PosContext con = createpossheet.CreateContext(filename);
        printProdProperty(possheet,con);
        
        System.out.println("完成任意小票重打........");
    }

    /**
     *
     * @param storeid
     * @param posid
     * @param cashierid
     * @param sheetid
     * @param workdate
     * @param pageno
     */
	public void printHeader(
		String storeid,
		String posid,
		String cashierid,
		String sheetid,
		String workdate,
		String pageno) {

		if (posInvoice != null) {
			HashMap params = new HashMap();

			String info = PosConfig.getInstance().getString("HD1_INFO");
			params.put("${Header1}", "");
			info = PosConfig.getInstance().getString("HD2_INFO");
			params.put("${Header2}", "");
			params.put("${ShopID}", storeid);
			params.put("${PosId}", posid);
			params.put("${InvoiceNo}", sheetid);
			params.put("${Cashier}", cashierid);
			params.put("${Date}", workdate);
			String remark = PosConfig.getInstance().getString("REMARK");
			params.put("${Remark}", remark);
			DecimalFormat df = new DecimalFormat("00");
			String pageNo = df.format(Integer.parseInt(pageno));
			params.put("${Page}", pageNo);

			posInvoice.parseHeader(params);
		}
	}

	/**重打小票头
	 * @param storeid,posid,cashierid,sheetid,workdate,tag
	 * @return void
	 * */
	public void reprintHeader(
		String storeid,
		String posid,
		String cashierid,
		String sheetid,
		String workdate,
		String tag) {

		if (posTicket != null) {
			HashMap params = new HashMap();

			String info = PosConfig.getInstance().getString("HD1_INFO");
			params.put("${Header1}", info);
			info = PosConfig.getInstance().getString("HD2_INFO");
			params.put("${Header2}", info);
			params.put("${ShopID}", storeid);
			params.put("${PosID}", posid);
			createrandomnum randomnum = new createrandomnum();
			String num = Integer.toString(randomnum.getrandomnum());
			params.put("${SheetID}", sheetid + num);
			params.put("${Cashier}", cashierid);
			params.put("${Date}", Formatter.getDate(new Date()));

			posTicket.reprintparseHeader(params, tag);
		}
	}
	
	public void reprintHeaders(
			String storeid,
			String posid,
			String cashierid,
			String sheetid,
			String workdate,
			String orderid,
			String tag) {

			if (posTicket != null) {
				HashMap params = new HashMap();

				String info = PosConfig.getInstance().getString("HD1_INFO");
				params.put("${Header1}", info);
				info = PosConfig.getInstance().getString("HD2_INFO");
				params.put("${Header2}", info);
				params.put("${ShopID}", storeid);
				params.put("${PosID}", posid);
				createrandomnum randomnum = new createrandomnum();
				String num = Integer.toString(randomnum.getrandomnum());
				params.put("${SheetID}", sheetid + num);
				params.put("${Cashier}", cashierid);
				params.put("${Date}", Formatter.getDate(new Date()));
				params.put("${OrderID}",orderid);
				
				try {
					posTicket = new PosTicket("postick.xml", printer);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (JDOMException e) {
					e.printStackTrace();
				}

				posTicket.reprintparseHeader(params, tag);
			}
		}

	/**打印销售清单销售记录
	* @param Sale s 销售记录
	* @param SheetValue v 销售价值
	* @return void
	* */
	public void printdisplay(Sale s, SheetValue v) {

		if (posInvoice != null) {
			long discDelta = s.getDiscValue();
			HashMap params = new HashMap();

			if (s.getType() != Sale.TOTAL
				&& s.getType() != Sale.AlTPRICE
				&& s.getType() != Sale.SINGLEDISC
				&& s.getType() != Sale.TOTALDISC
				&& s.getType() != Sale.MONEYDISC
				&& s.getType() != Sale.AUTODISC
				&& s.getType() != Sale.LOANCARD
				&& s.getType() != Sale.LOANDISC
				&& s.getType() != Sale.Change) {
				String prefix = "";
				if (s.getType() == Sale.WITHDRAW) {
					prefix = "退货 ";
				}
				//params.put("${ItemCode}", s.getVgno());
				params.put("${ItemCode}", s.getBarcode());
				params.put("${Description}", prefix + s.getName());
				params.put(
					"${UnitPrice}",
					(new Value(s.getStdPrice())).toString());
				params.put(
					"${Discount}",
					(new Value(discDelta * (-1))).toString());
				params.put("${QTY}", s.getQtyStr());
				params.put(
					"${Amount}",
					(new Value(s.getStdValue())).toString());

				posInvoice.parseSale(params);

				if (s.getType() == Sale.WITHDRAW) {
					params.clear();
					params.put("${DiscDesc}", "");
					PosContext context = PosContext.getInstance();
					if (context.getAuthorizerid() != null
						&& !context.getAuthorizerid().equals("")) {
						params.put(
							"${DiscValue}",
							"退货授权人:" + context.getAuthorizerid());
					} else {
						params.put(
							"${DiscValue}",
							"退货授权人:" + context.getCashierid());
					}
					//				   posInvoice.parseDiscount(params);
				}

			} else if (s.getType() == Sale.LOANCARD) {
				params.clear();
				params.put("${DiscDesc}", "挂帐卡");
				params.put("${DiscValue}", s.getName());
				posInvoice.parseDiscount(params);
			}

		}

	}

	/**重打小票销售记录
	 * @param Sale s 销售记录
	 * @param SheetValue v 销售价值
	 * @return void
	 * */
	public void reprintdisplay(Sale s, SheetValue v) {

		if (posTicket != null) {

			HashMap params = new HashMap();

			if (s.getType() != Sale.TOTAL
				&& s.getType() != Sale.AlTPRICE
				&& s.getType() != Sale.SINGLEDISC
				&& s.getType() != Sale.TOTALDISC
				&& s.getType() != Sale.MONEYDISC
				&& s.getType() != Sale.AUTODISC
				&& s.getType() != Sale.LOANCARD
				&& s.getType() != Sale.LOANDISC
				&& s.getType() != Sale.Change) {
				String prefix = "";
				if (s.getType() == Sale.WITHDRAW) {
					prefix = "退货 ";
				}

				String suffix = "";

				params.put("${GoodsName}", prefix + s.getName() + suffix);

				params.put("${Barcode}", s.getVgno());

				params.put("${Quantity}", s.getQtyStr());
				params.put("${Price}", (new Value(s.getStdPrice())).toString());
				params.put(
					"${Amount}",
					(new Value(s.getStdValue())).toString());

				posTicket.parseSale(params);

				if (s.getType() == Sale.WITHDRAW) {
					params.clear();
					params.put("${DiscDesc}", "");
					PosContext context = PosContext.getInstance();
					if (context.getAuthorizerid() != null
						&& !context.getAuthorizerid().equals("")) {
						params.put(
							"${DiscValue}",
							"退货授权人:" + context.getAuthorizerid());
					} else {
						params.put(
							"${DiscValue}",
							"退货授权人:" + context.getCashierid());
					}
					posTicket.parseDiscount(params);
				}
			} else if (
				s.getType() == Sale.TOTAL
					|| s.getType() == Sale.AlTPRICE
					|| s.getType() == Sale.SINGLEDISC
					|| s.getType() == Sale.TOTALDISC
					|| s.getType() == Sale.MONEYDISC
					|| s.getType() == Sale.AUTODISC
					|| s.getType() == Sale.LOANDISC
					|| s.getType() == Sale.Change) {
				params.clear();
				params.put("${SubtotalDesc}", s.getName());
				if (s.getType() == Sale.TOTAL) {
					params.put(
						"${SubtotalValue}",
						new Value(v.getValueTotal()).toString());
				} else {
					params.put(
						"${SubtotalValue}",
						new Value(s.getStdValue() * (-1)).toString());
				}
				posTicket.parseSubtotal(params);
			} else if (s.getType() == Sale.LOANCARD) {
				params.clear();
				params.put("${DiscDesc}", "挂帐卡");
				params.put("${DiscValue}", s.getName());
				posTicket.parseDiscount(params);
			}

		}

	}

	/**打印支付信息（用于打印销售清单）
	 * @param p支付信息和v整单价值
	 **/
	public void printdisplay(Payment p, SheetValue v) {

		if (posInvoice != null) {
			HashMap params = new HashMap();

			params.put("${PayType}", Payment.getTypeName(p.getType()));
			params.put("${Currency}", p.getCurrenCode());
			params.put("${PayAmount}", (new Value(p.getValue())).toString());

			posInvoice.parsePayment(params);

		}

	}

	/**重新打印支付信息（用于重打小票）
	 * @param p支付信息和v整单价值
	 * */
	public void reprintdisplay(Payment p, SheetValue v) {

		if (posTicket != null) {

			if (p.getReason() == Payment.CASHOUT) {

				HashMap params = new HashMap();

				params.put("${DiscDesc}", "出款");
				params.put("${DiscValue}", new Value(-p.getValue()).toString());

				posTicket.parseDiscount(params);

			} else if (p.getReason() == Payment.CASHIN) {

				HashMap params = new HashMap();

				params.put("${DiscDesc}", "入款");
				params.put("${DiscValue}", new Value(p.getValue()).toString());

				posTicket.parseDiscount(params);

			} else if (p.getReason() == Payment.RaCard) {

				HashMap params = new HashMap();

				params.put("${DiscDesc}", "储值充值");
				params.put("${DiscValue}", new Value(p.getValue()).toString());

				posTicket.parseDiscount(params);

			}else {

				HashMap params = new HashMap();

				params.put("${PayType}", Payment.getTypeName(p.getType()));
				params.put("${Currency}", p.getCurrenCode());
				params.put(
					"${PayAmount}",
					(new Value(p.getValue())).toString());

				posTicket.parsePayment(params);

			}
		}

	}
	
	
	/**重新打印兑换信息
	 * @param pinput 支付信息，从中取卡号和卡余额以供重打显示
	 * */
	public void Plandisplay(String plan,String point) {
		//if (p.getType() == Payment.CARDLOAN || p.getType() == Payment.CARDSHOP) {
		HashMap params = new HashMap();
		
		params.put("${Plan}",  plan);
		params.put("${PlanPoint}", point);
		
		posTicket.parsePlan(params);
		//}

	}

	/**重新打印挂帐卡卡号和余额
	 * @param pinput 支付信息，从中取卡号和卡余额以供重打显示
	 * */
	public void reprintdisplay(LoanCard4Reprint loancard) {
		//if (p.getType() == Payment.CARDLOAN || p.getType() == Payment.CARDSHOP) {
		HashMap params = new HashMap();

		params.put("${loadCardNo}", loancard.getCardno());
		params.put("${loadCardBalance}", loancard.getResultValue());
		posTicket.parseLoanCard(params);
		//}

	}

	/**显示合计信息
	 * @param v
	 * @return
	 * @deprecated
	 * */
	public void disptotal(SheetValue v) {

		if (mainUI.disptotal(v) == 0) {

			if (posTicket != null) {

				HashMap params = new HashMap();

				params.clear();
				params.put("${SubtotalDesc}", "合计");
				params.put(
					"${SubtotalValue}",
					new Value(v.getValueTotal()).toString());
				posTicket.parseSubtotal(params);

			}
		}
	}

   	public void dispCouponHeader(String couponNO) {

		if (mainUI.dispCoupon(couponNO) == 0) {

			if (posTicket != null) {

				HashMap params = new HashMap();

				String name = "券号";
				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", couponNO);
                posTicket.parseDiscount(params);

			}

		}
	}

    public void dispCouponHeader() {
          // String couponNO=CouponSale.getCouponNOReprint("reprint/reprintsheet.xml");
         List couponElementList=null;
         try{
			Document doc = (new SAXBuilder()).build( new FileInputStream("reprint/reprintsheet.xml") );
			Element root = doc.getRootElement();
			Element sheet = root.getChild("sheet");
			//Element couponElement = sheet.getChild("coupon");
            couponElementList=sheet.getChildren("coupon");

//            String couponNo=null;
//			if(couponElement==null)
//                return null;
//            else
//
//				couponNo = couponElement.getChildText("couponNO");
//
//              return couponNo;
			}

			 catch ( Exception e ){
				e.printStackTrace();
			}
//        return null;
            if (posTicket != null&&couponElementList!=null) {

               // HashMap params = new HashMap();
             for(int i=0;i<couponElementList.size();i++){
                 HashMap params = new HashMap();
               Element couponElement=(Element)couponElementList.get(i);
                String couponNo = couponElement.getChildText("couponNO");
               if(couponNo!=null&&!couponNo.equals("")){
                String name = "券号"+i;
                params.clear();
                params.put("${DiscDesc}", name);
                params.put("${DiscValue}", couponNo);
                posTicket.parseDiscount(params);
               }
             }
            }

    }


	public void dispMemberCardHeader(MemberCard query) {

		if (mainUI.dispMemberCard(query) == 0) {

			if (posTicket != null) {

				HashMap params = new HashMap();

				String name = "会员卡";
				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", query.getCardNo());
                posTicket.parseDiscount(params);
                params.clear();
                name = "会员卡级别";
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", query.getLevelName());
				posTicket.parseDiscount(params);

			}

		}
	}

    public void dispMemberCardHeader(MemberCard4Reprint card4Print) {

            if (posTicket != null&&card4Print!=null) {

                HashMap params = new HashMap();

                String name = "会员卡";
                params.clear();
                params.put("${DiscDesc}", name);
                params.put("${DiscValue}", card4Print.getCardNo());
                posTicket.parseDiscount(params);
                params.clear();
                name = "会员卡级别";
                params.put("${DiscDesc}", name);
                params.put("${DiscValue}", card4Print.getMemberLevel());
                posTicket.parseDiscount(params);

            }

    }
    public void display(MemberCard memberCard) {


            if (posTicket != null&&memberCard!=null) {

				HashMap params = new HashMap();
              PosConfig config=PosConfig.getInstance();
           //是否打印会员积分 ON 为打印
            if(config.getString("PRINT_CURPOINT").equals("ON")){

              if(config.getString("PRINT_SUMPOINT").equals("ON")){
				String name = "上日累计积分：";
				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}",Formatter.toMoney(memberCard.getTotalPoint().toString()));
                posTicket.parseDiscount(params);
              }else{
				String name = "累计积分：";
				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", Formatter.toMoney( memberCard.getTotalPoint().add(memberCard.getCurrentPoint()).toString()));
                posTicket.parseDiscount(params);
              }

                params.clear();
               String  name = "此单积分：";
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", Formatter.toMoney(memberCard.getCurrentPoint().toString()));
				posTicket.parseDiscount(params);
				
				// zhouzhou add
                params.clear();
                String  namea = "卡类型：";
 				params.put("${DiscDesc}", namea);
 				params.put("${DiscValue}",memberCard.getLevelName().toString());
 				posTicket.parseDiscount(params);
           }
		}

	}
    
    
    // 计算商品总量
    public void display( int Count, boolean name){
    	
    	HashMap params = new HashMap();
    	
    	String tabname = name ? "商品总量" : "提货单号";
    	

		params.clear();
		params.put("${DiscDesc}", tabname);
		params.put("${DiscValue}",String.valueOf(Count));
        posTicket.parseDiscount(params);
    	
     }

    public void display(SHCardQueryVO shopCard) {


            if (posTicket != null&&shopCard!=null) {

				HashMap params = new HashMap();
				String name = "储值卡号：";
				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}",shopCard.getCardNO());
                posTicket.parseDiscount(params);


                params.clear();
                name = "储值卡余额：";
				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", Formatter.toMoney(shopCard.getCardDetail()));
                posTicket.parseDiscount(params);
                
                params.clear();
                name = "荣华币余额：";
				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", Formatter.toMoney(shopCard.getRHBDetail()));
                posTicket.parseDiscount(params);
                
                params.clear();
                name = "卡总余额：";
				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", Formatter.toMoney(shopCard.getDetail()));
                posTicket.parseDiscount(params);

		}

	}
    public void display(MemberCard4Reprint memberCard) {

//		if (memberCard!=null) {
//
//			HashMap params = new HashMap();
//
//			params.put("${memberCardTotalPoint}", memberCard.getTotalPoint());
//			params.put("${memberCardCurrentPoint}", memberCard.getCurrentPoint());
//
//			posTicket.parseMemberCard(params);
//   }

        if (posTicket != null&&memberCard!=null) {

				HashMap params = new HashMap();
              PosConfig config=PosConfig.getInstance();
          //是否打印会员积分 ON 为打印
           if(config.getString("PRINT_CURPOINT").equals("ON")){
              if(config.getString("PRINT_SUMPOINT").equals("ON")){
				String name = "上日累计积分：";

				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", Formatter.toMoney(memberCard.getTotalPoint()));
                posTicket.parseDiscount(params);
             }else{
				String name = "累计积分：";
                double totalPoint=Double.parseDouble(memberCard.getTotalPoint());
                double currentPoint=Double.parseDouble(memberCard.getCurrentPoint());
				params.clear();
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}",Formatter.toMoney(Double.toString(totalPoint+currentPoint)));
                posTicket.parseDiscount(params);
             }
//             if(config.getString("PRINT_CURPOINT").equals("ON")){
                params.clear();
               String name = "此单积分：";
				params.put("${DiscDesc}", name);
				params.put("${DiscValue}", Formatter.toMoney(memberCard.getCurrentPoint()));
				posTicket.parseDiscount(params);
				
				
				// zhouzhou add
//                params.clear();
//                String  namea = "卡类型：";
// 				params.put("${DiscDesc}", namea);
// 				params.put("${DiscValue}", Formatter.toMoney(memberCard.getLevelName().toString()));
// 				posTicket.parseDiscount(params);
           }
		}



	}

public void displayCouponCount(String CouponCount){
	
	
	}

public void displayEnCash(Sale s) {
	
	mainUI.display(s);
	
	if (posTicket != null) {
	    posTicket.println(s.getName(),PosTicket.LEFT,"");
	}
	
}

	public void display(Sale s, SheetValue v) {

		mainUI.display(s);

		if (posTicket != null) {

			HashMap params = new HashMap();

			if (s.getType() != Sale.TOTAL
				&& s.getType() != Sale.AlTPRICE
				&& s.getType() != Sale.SINGLEDISC
				&& s.getType() != Sale.TOTALDISC
				&& s.getType() != Sale.MONEYDISC
				&& s.getType() != Sale.AUTODISC
				&& s.getType() != Sale.LOANCARD
				&& s.getType() != Sale.LOANDISC
				&& s.getType() != Sale.Change) {
				String prefix = "";
				if (s.getType() == Sale.WITHDRAW) {
					prefix = "退货 ";
				}

				String suffix = "";
//屏蔽油岛号信息  沧州富达 by fire  2005_5_11 				
//				if (PosConfig.getInstance().isIndicatorDept(s.getDeptid())) {
//					if (s.getColorSize() != null
//						&& !s.getColorSize().equals("")) {
//						suffix = " 油岛号:" + s.getColorSize();
//					}
//				}

				params.put("${GoodsName}", prefix + s.getName() + suffix);
				params.put("${Barcode}", s.getVgno());

				params.put("${Quantity}", s.getQtyStr());
				params.put("${Price}", (new Value(s.getStdPrice())).toString());
				params.put(
					"${Amount}",
					(new Value(s.getStdValue())).toString());
				posTicket.parseSale(params);

				if (s.getType() == Sale.WITHDRAW) {
					params.clear();
					params.put("${DiscDesc}", "");
					PosContext context = PosContext.getInstance();
					if (context.getAuthorizerid() != null
						&& !context.getAuthorizerid().equals("")) {
						params.put(
							"${DiscValue}",
							"退货授权人:" + context.getAuthorizerid());
					} else {
						params.put(
							"${DiscValue}",
							"退货授权人:" + context.getCashierid());
					}
					posTicket.parseDiscount(params);
				}

				int discDelta = v.getDiscDelta();

				if (discDelta != 0) {

					params.clear();
					if (s.getDiscType() == Discount.COMPLEX) {
						params.put("${DiscDesc}", s.getFavorName());
					} else {
						params.put(
							"${DiscDesc}",
							(new Discount(s.getDiscType())).getTypeName());
					}
					params.put(
						"${DiscValue}",
						(new Value(discDelta * (-1))).toString());
					posTicket.parseDiscount(params);

				}
			} else if (
				s.getType() == Sale.TOTAL
					|| s.getType() == Sale.AlTPRICE
					|| s.getType() == Sale.SINGLEDISC
					|| s.getType() == Sale.TOTALDISC
					|| s.getType() == Sale.MONEYDISC
					|| s.getType() == Sale.AUTODISC
					|| s.getType() == Sale.LOANCARD
					|| s.getType() == Sale.LOANDISC
					|| s.getType() == Sale.Change) {
				params.clear();
				params.put("${SubtotalDesc}", s.getName());
				if (s.getType() == Sale.TOTAL) {
					params.put(
						"${SubtotalValue}",
						new Value(v.getValueTotal()).toString());
				} else {
					params.put(
						"${SubtotalValue}",
						new Value(v.getValueTotal() * (-1)).toString());
				}
				posTicket.parseSubtotal(params);
			} else if (s.getType() == Sale.LOANCARD) {
				params.clear();
				params.put("${DiscDesc}", "挂帐卡");
				params.put("${DiscValue}", s.getName());
				posTicket.parseDiscount(params);
			}

		}

		if (custDisplay != null) {
			custDisplay.printGoods(s);
			custDisplay.printTotal((new Value(v.getValueUnPaid()).toString()));
		}

	}

	public void displayUnhold(Sale s, SheetValue v) {

		mainUI.display(s);

		if (posTicket != null && s.getType()!=Sale.ENCASH) {

			HashMap params = new HashMap();

			if (s.getType() != Sale.TOTAL
				&& s.getType() != Sale.AlTPRICE
				&& s.getType() != Sale.SINGLEDISC
				&& s.getType() != Sale.TOTALDISC
				&& s.getType() != Sale.MONEYDISC
				&& s.getType() != Sale.AUTODISC
				&& s.getType() != Sale.LOANCARD
				&& s.getType() != Sale.LOANDISC
				&& s.getType() != Sale.Change) {
				String prefix = "";

				String suffix = "";
//屏蔽 油岛号信息 沧州富达 by fire  2005_5_11 				
//				if (PosConfig.getInstance().isIndicatorDept(s.getDeptid())) {
//					if (s.getColorSize() != null
//						&& !s.getColorSize().equals("")) {
//						suffix = " 油岛号:" + s.getColorSize();
//					}
//				}
				params.put("${GoodsName}", prefix + s.getName() + suffix);
				params.put("${Barcode}", s.getVgno());

				params.put("${Quantity}", s.getQtyStr());
				params.put("${Price}", (new Value(s.getStdPrice())).toString());
				params.put(
					"${Amount}",
					(new Value(s.getStdValue())).toString());

				posTicket.parseSale(params);

			} else if (
				s.getType() == Sale.TOTAL
					|| s.getType() == Sale.AlTPRICE
					|| s.getType() == Sale.SINGLEDISC
					|| s.getType() == Sale.TOTALDISC
					|| s.getType() == Sale.MONEYDISC
					|| s.getType() == Sale.AUTODISC
					|| s.getType() == Sale.LOANDISC
					|| s.getType() == Sale.Change) {
				params.clear();
				params.put("${SubtotalDesc}", s.getName());
				if (s.getType() == Sale.TOTAL) {
					params.put(
						"${SubtotalValue}",
						new Value(v.getValueTotal()).toString());
				} else {
					params.put(
						"${SubtotalValue}",
						new Value(s.getStdValue() * (-1)).toString());
				}
				posTicket.parseSubtotal(params);
			} else if (s.getType() == Sale.LOANCARD) {
				params.clear();
				params.put("${DiscDesc}", "挂帐卡");
				params.put("${DiscValue}", s.getName());
				posTicket.parseDiscount(params);
			}

		}

		if (custDisplay != null && s.getType()!=Sale.ENCASH) {
			custDisplay.printGoods(s);
			custDisplay.printTotal((new Value(v.getValueUnPaid()).toString()));
		}

	}

	public void displayDiscount(Sale s, SheetValue v) {

		mainUI.displayDiscount(s);

		if (posTicket != null) {
			int discDelta = v.getDiscDelta();

			if (discDelta != 0) {

				HashMap params = new HashMap();

				params.clear();
				params.put(
					"${DiscDesc}",
					(new Discount(s.getDiscType())).getTypeName());
				params.put(
					"${DiscValue}",
					(new Value(discDelta * (-1))).toString());
				posTicket.parseDiscount(params);

			}
		}
	}

	public void displayprom(Sale s, SheetValue v) {

		mainUI.displayprom(s);
		/*
		if (posTicket != null) {
			int discDelta = v.getDiscDelta();
		
			if (discDelta != 0) {
		
				HashMap params = new HashMap();
		
				params.clear();
				params.put(
					"${DiscDesc}",
				     s.getFavorName());
				params.put(
					"${DiscValue}",
					(new Value(discDelta * (-1))).toString());
				posTicket.parseDiscount(params);
		
			}
		}
		*/
	}

	public void displayDiscount4correct(Sale s, SheetValue v) {

		mainUI.displayDiscount4correct(s, v);

		if (posTicket != null) {
			int discDelta = v.getDiscDelta();

			if (discDelta != 0) {

				HashMap params = new HashMap();

				params.clear();
				params.put(
					"${DiscDesc}",
					(new Discount(s.getDiscType())).getTypeName());
				params.put(
					"${DiscValue}",
					(new Value(discDelta * (-1))).toString());
				posTicket.parseDiscount(params);

			}
		}
	}

	public void displayTotalDiscount(Sale s, SheetValue v) {

		mainUI.displayDiscount(s);

		if (posTicket != null) {
			int discDelta = v.getDiscTotal();

			if (discDelta != 0) {

				HashMap params = new HashMap();

				params.clear();
				params.put(
					"${DiscDesc}",
					(new Discount(s.getDiscType())).getTypeName());
				params.put(
					"${DiscValue}",
					(new Value(discDelta * (-1))).toString());
				posTicket.parseDiscount(params);

			}
		}
	}

	public void display(Payment p, SheetValue v) {
		mainUI.display(p);

		if (posTicket != null) {
			HashMap params = new HashMap();

			params.put("${PayType}", Payment.getTypeName(p.getType()));
			params.put("${Currency}", p.getCurrenCode());
			params.put("${PayAmount}", (new Value(p.getValue())).toString());
			posTicket.parsePayment(params);
		}

		if (custDisplay != null) {
			custDisplay.printPayment(new Value(p.getValue()));
			if (v.getValueUnPaid() > 0) {
				custDisplay.printTotal(new Value(v.getValueUnPaid()));
			}
		}

	}

	public void display(Payment p, PosInputPayment pinput) {

		if (posTicket != null
			&& (p.getType() == Payment.CARDLOAN
				|| p.getType() == Payment.ICCARD)) {

			HashMap params = new HashMap();

			params.put("${loadCardNo}", pinput.getMediaNo());
			params.put("${loadCardBalance}", pinput.getExtraData());

			posTicket.parseLoanCard(params);

		}

	}
	
	public void displayPlan(Payment p) {

		if (posTicket != null
			&& (p.getType() == Payment.PlanPoint)) {

			HashMap params = new HashMap();

			params.put("${Plan}",  String.valueOf(p.getCurrenCode()));
			params.put("${PlanPoint}", p.getCardno());

			posTicket.parsePlan(params);

		}

	}



	public void display(SheetValue v, Exchange e) {

		String code = e.getCode();
		double rate = e.getRate();

		Value disc_delta = new Value(v.getDiscDelta());

		mainUI.setTotal((new Value(v.getValueTotal()).toString()));
		mainUI.setPaid((new Value(v.getValuePaid()).toString()));
		int rmb_topay = v.getValueToPay();

		int money_topay =
			(code.equals("RMB"))
				? rmb_topay
				: (int) Math.rint((double) rmb_topay / rate);

		if (money_topay == 0 && rmb_topay > 0)
			money_topay = 1;

		if (money_topay <= 0) {
			money_topay = -money_topay;
			mainUI.setUnPaidLabel("  找  赎  ");
		} else {
			mainUI.setUnPaidLabel("  待  收  ");
		}

		if (code.equals("RMB"))
			mainUI.setUnPaid((new Value(money_topay)).toString());
		else
			mainUI.setUnPaid(code + " " + (new Value(money_topay)).toString());
	}

	public void display(CashBox box) {
		if (posTicket != null) {
			HashMap params = new HashMap();

			params.put("${PayType}", Payment.getTypeName(box.getType()));
			params.put("${Currency}", box.getCurrenCode());
			params.put("${PayAmount}", (new Value(box.getValue())).toString());

			posTicket.parsePayment(params);
		}
	}

	public void display(SheetBrief briefs[]) {

		HoldList holdList = new HoldList(briefs);
		holdList.show();

	}

	public void prompt(String s) {
		mainUI.setPrompt(s);
	}

	public void displayHeader(PosContext context) {
		printer = POSPrinter.getInstance();
		try {
			posTicket = new PosTicket("posticket.xml", printer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		if (posTicket != null) {
			HashMap params = new HashMap();

			String info = PosConfig.getInstance().getString("HD1_INFO");
			params.put("${Header1}", info);
			info = PosConfig.getInstance().getString("HD2_INFO");
			params.put("${Header2}", info);
			params.put("${ShopID}", context.getStoreid());
			params.put("${PosID}", context.getPosid());
			createrandomnum randomnum = new createrandomnum();
			String num = Integer.toString(randomnum.getrandomnum());
			params.put(
				"${SheetID}",
				Integer.toString(context.getSheetid()) + num);
			params.put("${Cashier}", context.getCashierid());
			params.put("${Date}", Formatter.getDate(new Date()));

			posTicket.parseHeader(params);
		}
	}
	
	// 打印预订单小票头
	public void displayHead(PosContext context) {
		printer = POSPrinter.getInstance();
		try {
			posTicket = new PosTicket("postick.xml", printer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		if (posTicket != null) {
			HashMap params = new HashMap();

			String info = PosConfig.getInstance().getString("HD1_INFO");
			params.put("${Header1}", info);
			info = PosConfig.getInstance().getString("HD2_INFO");
			params.put("${Header2}",info);
			params.put("${ShopID}", context.getStoreid());
			params.put("${PosID}", context.getPosid());
			createrandomnum randomnum = new createrandomnum();
			String num = Integer.toString(randomnum.getrandomnum());
			params.put(
				"${SheetID}",
				Integer.toString(context.getSheetid()) + num);
			params.put("${Cashier}", context.getCashierid());
			params.put("${Date}", Formatter.getDate(new Date()));
			params.put("${OrderID}", Integer.toString(context.getOrderidOld()));

			posTicket.parseHeader(params);
		}
	}
	
	//打印提货卡
	public void displayHeaed(PosContext context) {
		printer = POSPrinter.getInstance();
		try {
			posTicket = new PosTicket("postick.xml", printer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		if (posTicket != null) {
			HashMap params = new HashMap();

			String info = PosConfig.getInstance().getString("HD1_INFO");
			params.put("${Header1}", info);
			info = PosConfig.getInstance().getString("HD2_INFO");
			params.put("${Header2}",info);
			params.put("${ShopID}", context.getStoreid());
			params.put("${PosID}", context.getPosid());
			createrandomnum randomnum = new createrandomnum();
			String num = Integer.toString(randomnum.getrandomnum());
			params.put(
				"${SheetID}",
				Integer.toString(context.getSheetid()) + num);
			params.put("${Cashier}", context.getCashierid());
			params.put("${Date}", Formatter.getDate(new Date()));
			params.put("${OrderID}", Integer.toString(context.getOrderidOld()));

			posTicket.parseHeader(params);
		}
	}
    public void displayClearHeader(PosContext context) {

        if (posTicket != null) {
            HashMap params = new HashMap();

            String info = PosConfig.getInstance().getString("HD1_INFO");
            params.put("${Header1}", info);
            info = PosConfig.getInstance().getString("HD2_INFO");
            params.put("${Header2}", info);
            params.put("${ShopID}", context.getStoreid());
            params.put("${PosID}", context.getPosid());
            createrandomnum randomnum = new createrandomnum();
            String num = Integer.toString(randomnum.getrandomnum());
            params.put(
                "${SheetID}",
                Integer.toString(context.getSheetid()) + num);
            params.put("${Cashier}", context.getCashierid());
            params.put("${Date}", Formatter.getLongDate(new Date()));

            posTicket.parseClearHeader(params);
        }
    }

    public void displayHeaderWithoutGoods(PosContext context) {
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (posTicket != null) {
                HashMap params = new HashMap();

                String info = PosConfig.getInstance().getString("HD1_INFO");
                params.put("${Header1}", info);
                info = PosConfig.getInstance().getString("HD2_INFO");
                params.put("${Header2}", info);
                params.put("${ShopID}", context.getStoreid());
                params.put("${PosID}", context.getPosid());
                params.put("${Cashier}", context.getCashierid());
                params.put("${Date}", sdf.format(new Date()));

                posTicket.parseHeaderWithoutGoods(params);
            }
        }

    // 逻辑isCoupon 假： 非兑现卷 真： 兑现卷
	public void displayTrail(SheetValue sheetValue , boolean isCoupon) {
		if (posTicket != null) {
			PosConfig config = PosConfig.getInstance();

			HashMap params = new HashMap();

			int paid = sheetValue.getValuePaid();
			int topay = sheetValue.getValueToPay();
			int total = sheetValue.getValueTotal();
			
			if (!isCoupon){
				params.put("${TotalPayAmount}", (new Value(paid)).toString());
				params.put("${Change}", (new Value(topay * -1)).toString());
				params.put("${ActualPayAmount}", (new Value(total)).toString());}
			else{
				params.put("${TotalPayAmount}","0" );
				params.put("${Change}","0" );
				params.put("${ActualPayAmount}","0" );
				}

			if (sheetValue.getDiscTotal() > 0) {
				params.put("${ShowDiscountTotal}", "true");
				params.put(
					"${DiscountTotal}",
					(new Value(sheetValue.getDiscTotal())).toString());

			} else {
				params.put("${ShowDiscountTotal}", "false");
			}
			
			

			params.put("${Trail1}", config.getString("TL1_INFO"));
			params.put("${Trail2}", config.getString("TL2_INFO"));
			params.put("${Trail3}", config.getString("TL3_INFO"));
			posTicket.parseTrail(params);
		}

	}

	/**打印销售清单尾
	 *
	 * */
	public void printTrail(SheetValue sheetValue, int page, int pages) {

		if (posInvoice != null) {
			PosConfig config = PosConfig.getInstance();

			HashMap params = new HashMap();

			int paid = sheetValue.getValuePaid();
			int topay = sheetValue.getValueToPay();
			int total = sheetValue.getValueTotal();

			params.put("${TotalPayAmount}", (new Value(paid)).toString());
			params.put("${Change}", (new Value(topay * -1)).toString());
			params.put("${ActualPayAmount}", (new Value(total)).toString());

			if (sheetValue.getDiscTotal() > 0) {
				params.put("${ShowDiscountTotal}", "true");
				params.put(
					"${DiscountTotal}",
					(new Value(sheetValue.getDiscTotal())).toString());

			} else {
				params.put("${ShowDiscountTotal}", "false");
			}

			DecimalFormat df = new DecimalFormat("00");
			String pageNo = df.format(page + 1);
			String allPage = df.format(pages);
			params.put("${Trail1}", "");
			params.put("${Trail2}", "第" + pageNo + "页/总" + allPage + "页");
			posInvoice.parseTrail(params);
		}

	}

	public void printTrail(int page, int pages) {

		if (posInvoice != null) {

			HashMap params = new HashMap();

			DecimalFormat df = new DecimalFormat("00");
			String pageNo = df.format(page + 1);
			String allPage = df.format(pages);
			params.put("${Trail1}", "");
			params.put("${Trail2}", "第" + pageNo + "页/总" + allPage + "页");
			posInvoice.parseCommnet(params);

		}
	}

	public void reprintdisplayTrail(
		SheetValue sheetValue,
		String tag,
		boolean isShowTotalInfo) {

		if (posTicket != null) {
			PosConfig config = PosConfig.getInstance();

			HashMap params = new HashMap();

			if (isShowTotalInfo) {

				int paid = sheetValue.getValuePaid();
				int topay = sheetValue.getValueToPay();
				int total = sheetValue.getValueTotal();

				params.put("${TotalPayAmount}", (new Value(paid)).toString());
				params.put("${Change}", (new Value(topay * -1)).toString());
				params.put("${ActualPayAmount}", (new Value(total)).toString());

				if (sheetValue.getDiscTotal() > 0) {
					params.put("${ShowDiscountTotal}", "true");
					params.put(
						"${DiscountTotal}",
						(new Value(sheetValue.getDiscTotal())).toString());

				} else {
					params.put("${ShowDiscountTotal}", "false");
				}
			}

			params.put("${Trail1}", config.getString("TL1_INFO"));
			params.put("${Trail2}", config.getString("TL2_INFO"));
			params.put("${Trail3}", config.getString("TL3_INFO"));

			posTicket.reprintparseTrail(params, tag, isShowTotalInfo);
		}

	}

	public void displayCash(String desc, String value) {
		if (posTicket != null) {
			PosConfig config = PosConfig.getInstance();
			HashMap params = new HashMap();

			params.put("${DiscDesc}", desc);
			params.put("${DiscValue}", value);
			params.put("${Trail1}", config.getString("TL1_INFO"));
			params.put("${Trail2}", config.getString("TL2_INFO"));
			params.put("${Trail3}", config.getString("TL3_INFO"));

			posTicket.parseDiscount(params);

			posTicket.parseButtom(params);

		}
	}
	
	public void displayCardRa(String desc, String value,String card,String Addpay,SHCardQueryVO shopCard){
		
		if (posTicket != null) {
			PosConfig config = PosConfig.getInstance();
			HashMap params = new HashMap();
			
			String name = "储值卡号：";
			params.clear();
			params.put("${DiscDesc}", desc);
			params.put("${DiscValue}",shopCard.getCardNO());
            posTicket.parseDiscount(params);

            params.clear();
            name = "储值卡余额：";
			params.clear();
			params.put("${DiscDesc}", name);
			params.put("${DiscValue}", Formatter.toMoney(String.valueOf(Double.parseDouble(Addpay))));
            posTicket.parseDiscount(params);
            
            params.clear();
            name = "荣华币余额：";
			params.clear();
			params.put("${DiscDesc}", name);
			params.put("${DiscValue}", Formatter.toMoney(shopCard.getRHBDetail()));
            posTicket.parseDiscount(params);
            
            params.clear();
            name = "卡总余额：";
			params.clear();
			params.put("${DiscDesc}", name);
			params.put("${DiscValue}", Formatter.toMoney(String.valueOf(Double.parseDouble(shopCard.getRHBDetail())+Double.parseDouble(Addpay))));
            posTicket.parseDiscount(params);
			
            params.clear();
//			params.put("${DiscDesc}", desc);
//			params.put("${DiscValue}", value);
//			params.put("${loadCardNo}", card);
//			params.put("${loadCardBalance}", Addpay);
			params.put("${Trail1}", config.getString("TL1_INFO"));
			params.put("${Trail2}", config.getString("TL2_INFO"));
			params.put("${Trail3}", config.getString("TL3_INFO"));

//			posTicket.parseDiscount(params);
//			posTicket.parseLoanCard(params);
			posTicket.parseButtom(params);

		}

		}

	public void displayWelcome() {
		if (custDisplay != null) {
			custDisplay.welcome();
		}
	}

	public void displayChange(SheetValue v) {
		if (custDisplay != null) {
			custDisplay.printReturn(
				((new Value(v.getValueToPay() * -1).toString())));
		}
	}

	public void print(String s) {
		if (posTicket != null) {
			posTicket.println(s);
		}
	}

	public void printWithoutSeperator(String s) {
		if (posTicket != null) {
			posTicket.printlnWithoutSeperator(s);
		}
	}

	public void cutPaper() {
		if (posTicket != null) {
			posTicket.cutPaper();
		}
	}

	public void printFeed(int line) {
		if (printer != null) {
			printer.feed(line);
		}
	}

	public int getFeedLines() {
		if (posTicket != null) {
			return posTicket.getFeedLines();
		}
		return 2;
	}

	public boolean printProductInfo(PosContext c,Sale s,int index,int total)
	{
		if(posTicketEx != null)
		{
			return posTicketEx.print(c,s,index,total);
		}
		return false;	
	}
	
	/*
	 * 打印商品的生产属性
	 */
	
	public void printProdProperty(PosSheet sheet,PosContext context)
	{
		int goods_num =0;
		goods_num = sheet.getSaleLen();
		
		
		Sale s = null;
		int prodnum = 0;
		int i,j,m;
		for(i = 0; i < goods_num; ++i)
		{
			s = sheet.getSale(i);
			//如果是退货则不打印
			if(s.isProductGood() && s.getType() != Sale.WITHDRAW)
			{
				if(s.getQtyInt() > 1)
					prodnum += s.getQtyInt();
				else
					prodnum++;
			}
		}
		
		PosDevOut.setPrinterEx("POSPrinterEx");
		
		for(i = j = 0; i < goods_num;++i)
		{
			s = sheet.getSale(i);
			if(s.isProductGood() && s.getType() != Sale.WITHDRAW)
			{
				for(m = 0;m < s.getQtyInt(); ++m)
				{
					j++;
					out.printProductInfo(context,s,j,prodnum);
				}
			}
		}
		if(j > 0) PosDevOut.printerFeed();
		
		PosDevOut.resetPrinter();	
	}

	private static PosDevOut out = null;
	private static POSPrinter printer;
	private static POSPrinterEx printerEx;
	private static POSPrinter1 printer1;
	/**
	 * @param waiter_show
	 */
	public static void setPrinterEx(String entryName) {
		PosDevOut.printerEx.setPrinter(entryName);
	}

	public static void resetPrinter() {
		PosDevOut.printerEx.resetPrinter();
	}

	public static void printerFeed() {
		PosDevOut.posTicketEx.feed();
	}

}
