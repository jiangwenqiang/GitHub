package com.royalstone.pos.shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdom.JDOMException;

import com.royalstone.pos.card.CardChange;
import com.royalstone.pos.card.MemberCardChange;
import com.royalstone.pos.card.MemberCardMgr;
import com.royalstone.pos.card.MemberCardMgrFactory;
import com.royalstone.pos.card.SHCardAutoRever;
import com.royalstone.pos.card.SHCardProcess;
import com.royalstone.pos.card.SHValueCardProcess;
import com.royalstone.pos.common.CashBasket;
import com.royalstone.pos.common.CashBox;
import com.royalstone.pos.common.GetAuthor;
import com.royalstone.pos.common.Goods;
import com.royalstone.pos.common.LoanLog;
import com.royalstone.pos.common.Operator;
import com.royalstone.pos.common.OperatorList;
import com.royalstone.pos.common.Payment;
import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.common.PosFunction;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.common.SheetValue;
import com.royalstone.pos.core.PosCore;
import com.royalstone.pos.core.SaleList;
import com.royalstone.pos.coupon.CouponAutoRever;
import com.royalstone.pos.coupon.CouponEnCash;
import com.royalstone.pos.coupon.CouponException;
import com.royalstone.pos.coupon.CouponLargess;
import com.royalstone.pos.coupon.CouponLargessList;
import com.royalstone.pos.coupon.CouponMgr;
import com.royalstone.pos.coupon.CouponMgrImpl;
import com.royalstone.pos.coupon.CouponSale;
import com.royalstone.pos.coupon.CouponSaleList;
import com.royalstone.pos.coupon.CouponTypeInfoList;
import com.royalstone.pos.coupon.LargessAutoCoupon;
import com.royalstone.pos.data.PosTurn;
import com.royalstone.pos.data.PosTurnList;
import com.royalstone.pos.data.ShopClock;
import com.royalstone.pos.favor.DiscPrice;
import com.royalstone.pos.favor.DiscRate;
import com.royalstone.pos.favor.Discount;
import com.royalstone.pos.gui.Authorization;
import com.royalstone.pos.gui.CashBoxInfo;
import com.royalstone.pos.gui.DialogConfirm;
import com.royalstone.pos.gui.DialogInfo;
import com.royalstone.pos.gui.DispPrice;
import com.royalstone.pos.gui.DispWaiter;
import com.royalstone.pos.gui.HoldList;
import com.royalstone.pos.gui.LogonDialog;
import com.royalstone.pos.gui.MSRInput;
import com.royalstone.pos.gui.MainUI;
import com.royalstone.pos.gui.ModifyPassword;
import com.royalstone.pos.gui.OilInput;
import com.royalstone.pos.gui.PosFrame;
import com.royalstone.pos.gui.PriceInput;
import com.royalstone.pos.gui.StartFrame;
import com.royalstone.pos.gui.UnLock;
import com.royalstone.pos.hardware.POSCashDrawer;
import com.royalstone.pos.hardware.config.HardWareConfigure;
import com.royalstone.pos.invoke.realtime.RealTime;
import com.royalstone.pos.invoke.realtime.RealTimeException;
import com.royalstone.pos.io.PosDevIn;
import com.royalstone.pos.io.PosDevOut;
import com.royalstone.pos.io.PosInput;
import com.royalstone.pos.io.PosInputGoods;
import com.royalstone.pos.io.PosInputLogon;
import com.royalstone.pos.io.PosInputPayment;
import com.royalstone.pos.journal.JournalLogList;
import com.royalstone.pos.journal.JournalManager;
import com.royalstone.pos.journal.JournalUploader;
import com.royalstone.pos.journal.LogManager;
import com.royalstone.pos.keymap.AllKeyEventListener;
import com.royalstone.pos.keymap.Wait4Lock;
import com.royalstone.pos.loader.PosSynchronizer;
import com.royalstone.pos.managment.ClerkAdm;
import com.royalstone.pos.managment.WorkTurnAdm;
import com.royalstone.pos.notify.NotifyChangePriceConsumer;
import com.royalstone.pos.util.Day;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.util.InvalidDataException;
import com.royalstone.pos.util.PosConfig;
import com.royalstone.pos.util.Response;
import com.royalstone.pos.util.UserCancelException;
import com.royalstone.pos.util.Value;
import com.royalstone.pos.util.Volume;
import com.royalstone.pos.workTurn.WorkTurnException;



/**
 *现场已更新JDK1.5
 * */

public class pos {
	public static Object uploadLock = new Object();
	public static Object Lock = new Object();
	public static Object workTurnLock = new Object();
	public static PosFrame posFrame = null;
	public static PosCore core = null;
	public static PipedOutputStream posOutStream = null;
	public static StartFrame startFrame = null;
	public static FileOutputStream logger = null;
	public static PosShell sh = null;

	public static void main(String[] args) {
		FileLock fileLock = null;
		int a = 1;
		try {
			FileOutputStream fos = new FileOutputStream("lock");
			fileLock = fos.getChannel().tryLock();
			if (fileLock == null) {
				JOptionPane.showMessageDialog(null, "POS程序已经运行！");
				System.exit(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "严重错误！");
			System.exit(1);
		}
		try {

			System.out.println("javapos start...\n");

			//创建系统文件目录
			prepareDir("log");
			prepareDir("data");
			prepareDir("work");
			prepareDir("journal");
			prepareDir("journalLog");
			prepareDir("poslog");
			prepareDir("price");
			prepareDir("promo");
			prepareDir("autorever");
			prepareDir("couponrever");
			// 保存赠送信息目录
			prepareDir("largessinfo");

			// 系统日记文件（写入LOG目录中）
			redirectOutput();

			// 加载键盘映射文件
			AllKeyEventListener allKeyEventListener = new AllKeyEventListener(
					"winkeymap.xml");

			sh = new PosShell();
			// 下载脱机数据
			sh.synchronizeData();
			// POS机初始发
			sh.init();

			//sh.copyCouponInfo(pos.core.getPosContext().getStoreid());

			sh.run();

		} catch (Throwable t) {
			t.printStackTrace();
			generateErrLog();
			JOptionPane.showMessageDialog(null,
					"系统发生严重错误,请将桌面上的\"RTPOS错误信息\"发送到技术支持部门!");
			System.exit(3);
		}
	}

	public static void activeUploader() {
		sh.activeUploader();
	}

	public static void setStateBUnlock() {
		sh.setStateBUnlock();
	}

	public static boolean isLock() {
		return sh.getState() == PosState.LOCK;
	}

	private static void redirectOutput() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			File file = new File("log/pos41.log");
			if (file.exists()) {
				FileChannel infile = new FileInputStream("log/pos41.log")
						.getChannel();
				FileChannel outfile = new FileOutputStream("log/pos41_"
						+ sdf.format(new Date()) + "_R.log").getChannel();
				infile.transferTo(0, infile.size(), outfile);
			}

			logger = new FileOutputStream("log/pos41.log");
			System.setOut(new PrintStream(logger));
			System.setErr(new PrintStream(logger));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("ERROR: Connot open log file, exit ...");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean prepareDir(String dirname) {
		File dir = new File(dirname);
		if (dir.exists() && dir.isDirectory())
			return true;
		if (dir.exists() && !dir.isDirectory())
			return false;
		return dir.mkdir();
	}

	public static void reinit() {

		sh.synchronizeData();
		try {
			sh.reinit();
		} catch (InvalidDataException e) {
			e.printStackTrace();
			System.out.println("Reinit ERROR: Invalid data!");
			System.exit(3);
		}
	}

	private static void generateErrLog() {

		try {
			Properties prop = new Properties();

			try {
				prop.load(new FileInputStream("ErrorLog.properties"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			String destPath = prop.getProperty("Path", new String((System
					.getProperty("user.home") + "/桌面/RTPOS错误信息").getBytes(),
					"ISO8859-1"));

			destPath = new String(destPath.getBytes("ISO8859-1"), "GB2312");

			String destDir[] = destPath.split("/");
			String path = null;
			for (int i = 0; i < destDir.length; i++) {
				if (path != null) {
					path = path + "/" + destDir[i];
					prepareDir(path);
				} else {
					path = destDir[i];
				}
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

			FileChannel infile = new FileInputStream("log/pos41.log")
					.getChannel();
			FileChannel outfile = new FileOutputStream(destPath
					+ File.separator + "error" + sdf.format(new Date())
					+ ".log").getChannel();
			infile.transferTo(0, infile.size(), outfile);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}

class PosShell {
	Wait4Lock lock;

	private Thread journalUploader;

	public void activeUploader() {

		if (journalUploader != null) {
			journalUploader.interrupt();
		}

	}

	public void init() throws InvalidDataException {
		System.out.println("javapos init...\n");

		// Init Context
		initContext();
		context.setOnLine(true);

		// Init PosCore
		pos.core = core = new PosCore();
		core.init();
		//-----配置POS的硬件参数--------------------------
		PosConfig posconfig = PosConfig.getInstance();
		HardWareConfigure hwConfig = new HardWareConfigure(posconfig);
		hwConfig.initHardWareConfig();
		//----------------------------------
		//初始化钱箱 ? 改更过
		this.cashDrawer = POSCashDrawer.getInstance();

		state = new PosState(PosState.PRELOGON);

		//		Thread t = new Thread(new LoanCardAutoRever());
		//		t.start();

		// 线程 挂账卡自动冲正 ？
		Thread t = new Thread(new SHCardAutoRever());
		t.start();

		// 线程 挂账卡自动冲正 ？
		Thread coupon = new Thread(new CouponAutoRever());
		coupon.start();

		// 线程 生成贷款日记
		Thread t_log = new Thread(new LoanLog());
		t_log.start();

		//  监听键盘
		lock = new Wait4Lock();
		lock.start();
		
		//TODO 屏蔽POS客户端的实时调价接收线程 沧州富达 by fire 2005_5_11
		//		Thread r = new Thread(new NotifyReceiver());
		//		r.start();

		journalUploader = new Thread(new JournalUploader(context));
		journalUploader.start();

	}

	public void reinit() throws InvalidDataException {
		initContext();
		core.init();
	}

	private void generateLog() {
		if (pos.logger != null) {
			try {
				deleteLog();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				FileChannel infile = new FileInputStream("log/pos41.log")
						.getChannel();
				FileChannel outfile = new FileOutputStream("log/pos41_"
						+ sdf.format(new Date()) + ".log").getChannel();
				infile.transferTo(0, infile.size(), outfile);

				pos.logger = new FileOutputStream("log/pos41.log");
				System.setOut(new PrintStream(pos.logger));
				System.setErr(new PrintStream(pos.logger));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 获取订单号方法
	private String getForward() {
		PriceInput priceInput = new PriceInput("请输入提货单号：");
		priceInput.show();
		try {
			while (!priceInput.isFinish())
				Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String inputCode = priceInput.getInputcode();
		return inputCode;
	}

	// 荣华项目 预订单
	private int bringForward() {
		int ID = context.getOrderidOld();
		if (ID != 0) {
			confirm("不能在一笔单中第二次使用提货卡");
			return -1;
		}

		String O_id;
		// 获取屏幕数字
		if ((O_id = getForward()).equals("")) {
			confirm("预订单失效");
			return -1;
		}
		if (O_id.length() > 12) {
			confirm("预订单过大");
			return -1;
		} else {

			// 转变为INT型
			int order_id = Integer.parseInt(O_id);
			String order_m = getOrder_m(order_id);
			if (order_m.equals("-1")) {
				confirm("预订单不存在，不能进行消费！");
				return -1; //报错的状态
			} else {
				// INT 变成String型
				// 修改金额
				String p = getPrice(order_m, O_id);
				if (p == "-1") {
					return -1; //报错的状态
				}
				context.setOrderidOld(O_id);
				int cents = (int) Math.rint((Double.parseDouble(p) * 100));
				out.display(context.getOrderidOld(), false);
				return cents;
			}
		}
	}

	// -------------------------
	private String getPrice(String price, String id) {
		PriceInput priceInput = new PriceInput("请输入预订单变价：", price, id);
		priceInput.show();
		try {
			while (!priceInput.isFinish())
				Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String inputCode = priceInput.getInputcode();
		if (inputCode.equals("")) {
			confirm("金额不能为空值");
			return "-1";
		}
		if (inputCode.equals("0")) {
			confirm("金额不能为空值");
			return "-1";
		}
		if (inputCode.equals("0.0")) {
			confirm("金额不能为空值");
			return "-1";
		}
		double a = Double.parseDouble(price);
		double b = Double.parseDouble(inputCode);
		if (b > a) {
			confirm("输入金额超支，退出");
			return "-1";
		}
		return inputCode;
	}

	private void deleteLog() {
		File path = new File("log");
		File[] dirList = path.listFiles();
		if (dirList.length > 1500) {
			for (int i = 0; i < dirList.length; i++) {
				if (!dirList[i].getName().equals("pos41.log")) {
					dirList[i].delete();
				}
			}
		}
	}

	public void synchronizeData() {

		System.out.println("load pos.xml, price.xml, operator.lst ... ");
		try {
			PosSynchronizer s = new PosSynchronizer("pos.ini");
			s.synchronize();
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Connot open pos.ini, exit ...");
			System.exit(2);
		} catch (IOException e) {
			System.err.println("ERROR: Connot read pos.ini, exit ...");
			System.exit(2);
		} catch (JDOMException e) {
			e.printStackTrace();
			System.err
					.println("WARNING: Downloading new data failed, go on working with dated data.");
			if (!confirm("下载脱机数据失败，是否继续？")) {
				System.exit(2);
			}
		}
	}

	public int getState() {
		//System.out.println("state.getState() in posShell :
		// "+state.getState());
		return state.getState();
	}

	public PosState get_state() {
		return state;
	}

	private void initContext() {
		if ((new File(PosContext.file4context)).exists()) {
			context = PosContext.load();
			context.fromIni("pos.ini");
			context.fromXML("pos.xml");
			context.setWarning("");
		} else {
			context = PosContext.getInstance();
			context.fromIni("pos.ini");
			context.fromXML("pos.xml");
			context.setWorkDate(new Day());
		}
	}

	public void run() {
		System.out.println("javapos run...");
		while (true) {

			switch (state.getState()) {
			case PosState.PRELOGON:
				if (context.isOnLine()) {
					// 登陆方法
					logon_online();
					
				} else {
					logon();
				}
				break;
			case PosState.PRESALE:
				presale();
				break;
			case PosState.SALE:
				sale();
				break;
			case PosState.FIND:
				find();
				break;
			case PosState.MAXCASH:
				maxcash();
				break;
			// 初始预订单
			case PosState.FUTRUESELL:
				futruesell();
				break;
			case PosState.WITHDRAW:
				withdraw();
				break;
			case PosState.DISCOUNT:
				singleDisc();
				break;
			case PosState.DISCTOTAL:
				totalDisc();
				break;
			case PosState.DISCMONEY:
				moneyDisc();
				break;
			case PosState.ALTPRICE:
				altPrice();
				break;
			case PosState.LOCK:
				UnLock unLock = new UnLock();
				unLock.show();
				//wait_unlock();
				break;
			case PosState.OPENSHEET:
				break;
			case PosState.CLOSESHEET:
				//printProdProperty();
				commitSheet(inputpayment);
				String printtype = PosContext.getInstance().getPrintType();
				if (printtype != null || !printtype.equals("")) {
					if (printtype.compareToIgnoreCase("INVOICE") == 0) {
						printInvoice();
					}
				}
				break;
			case PosState.CASHIN:
				cashin();
				break;
			case PosState.CASHOUT:
				cashout();
				break;
			case PosState.NETWORKERROR:
				networkError();
				break;
			case PosState.ERROR:
				in.waitCancel(context.getWarning());
				context.setWarning("");
				if (state.getOldState() == PosState.FIND) {
					state.setState(PosState.FIND);
				} else {
					if (core.isSheetEmpty())
						state.setState(PosState.PRESALE);
					else
  						state.setState(PosState.SALE);
				}
				break;
			default:
				;
			}
		}
	}

	private void networkError() {

		out.displayState("网络故障");
		out.clearInputLine();

		do {
			out.prompt("网络故障,按清除键尝试联机或按联单机切换键切换到脱机!");
			boolean reconnect = in.waitReConnect();
			if (reconnect) {
				out.prompt("正在尝试联机……");
				if (RealTime.getInstance().testOnLine()) {
					notice("联机成功!");
					break;
				} else {
					in.clearKey();
					notice("联机失败!");
				}
			} else {
				notice("成功转换为脱机工作方式!");
				context.setOnLine(false);
				out.display(context);
				break;
			}
		} while (true);
		context.setWarning("");
		state.setState(state.getOldState());
	}

	/*
	 * Make a requestLogon with ID, PIN, shiftid. Send request to server.
	 * Receive reply from server. Set Work Environment: workdate, shiftid,
	 * sheetid, etc.
	 */
	private void logon_online() {

		do {
			if (!RealTime.getInstance().testOnLine()) {
				if (!confirm("与后台服务器连接失败，重连(确定)或脱机工作(取消)？")) {
					context.setOnLine(false);
					state.setState(PosState.PRELOGON);
					return;
				}
			} else {
				break;
			}
		} while (true);

		initLogonIO();

		synchronized (pos.uploadLock) {
			JournalManager journalManager = new JournalManager();
			journalManager.upload(out);
		}

		out.displayState("请登录");
		out.clear();
		String note = context.getWarning();
		if (note.length() == 0)
			note = "请输入收银员编号和密码";
		PosInput inp = in.getInputLogon(note);
		if (inp != null && inp.key() == PosFunction.EXIT) {
			System.err.println("User exit...");
			core.exit();
			System.exit(0);
		}
		if (inp == null || !(inp instanceof PosInputLogon))
			return;

		String server = context.getServerip();
		int port = context.getPort();
		String posid = context.getPosid();

		PosInputLogon input = (PosInputLogon) inp;

		String cashierid = input.getID();
		String pin = input.getPIN();
		//TODO 沧州富达 by fire 2005_5_11
		//int shiftid = input.getShiftid();
		/**
		 * updated begin by huangxuean 25 Jun 2004
		 */
		String localip = null;
		try {
			InetAddress iadd = InetAddress.getLocalHost();
			localip = iadd.getHostAddress();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		/**
		 * updated end by huangxuean 25 Jun 2004
		 */
		ClerkAdm adm = new ClerkAdm(server, port);
		Response r = adm.getClerk(posid, cashierid, pin);
		Operator op = (Operator) r.getObject();
		
		/*
		if(op == null)
		{ 
			int a[] ={1,2,3};
			op = new Operator("0001","0001","jwq", a);
		}
		*/
		
		if (op != null) {
//			context.updateWorkDay(r.getListNO());
//			context.setWorkDate(new Day());
			context.updateWorkDay();
			System.out.println("登录成功......");
			lock.setState(true);

			ClerkAdm ad = new ClerkAdm(server, port);
			Response rl = ad.getOrderid(posid);
			Operator a = (Operator) rl.getObject();
			context.setOrder_id(a.getorderid() + 1);

			cashier = op;
			core.setCashierid(cashierid);

			logonDialog.dispose();
			context.setCounp(false);

			openPosWindow();
			
			int getid = Integer.parseInt(op.getId());
			core.writelog("登录", "0", getid);			
			out.print("收银员:" + op.getId() + " 登录");
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			

			// check current core and decide which state to go.
			// if current sheet is empty, go to PRESALE, else go to SALE.
			// out.displayHeader(context);
			out.displayWelcome();
			
			
			in.clearKey();
			if (core.isSheetEmpty()) {
				state.setState(PosState.PRESALE);
			} else {
	//			if (core.getPosSheet().getMemberCard() != null)
	//				out
	//						.dispMemberCardHeader(core.getPosSheet()
	//								.getMemberCard());
				//                if
				// (core.getPosSheet().getCouponNO()!=null&&!core.getPosSheet().getCouponNO().equals(""))
				//                    out.dispCouponHeader(core.getPosSheet().getCouponNO());
				// 状态变化
				displaySheet();
				// 复制赠送对象
				displayCoupon();
				

				//                state.setState(PosState.SALE);
			}
			return;
		} else {
			core.writelog("登录", "1", Integer.parseInt(cashierid));
			context.setWarning(r.getNote());
			state.setState(PosState.PRELOGON);
			logonDialog.getLogonPanel().clear();
		}
	}
	// 赠品信息复制到当前对象
	public void displayCoupon(){
		
		CouponLargessList couponLargess = new LargessAutoCoupon().Auto();
		
		if (couponLargess != null){
			for (int i = 0; i < couponLargess.size(); i ++){
				if (couponLargess.get(i).getExceptionInfo().equals("")){
					core.couponLargessList.add(couponLargess.get(i));
					context.setCounp(true);
					}
				}
			}
		}

	public void copyCouponInfo(String salesID) {
		
		CouponMgr couponMgr = new CouponMgrImpl();
		try {
			couponMgr.copyCouponInfo(salesID);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// 中传服务器调用剩余金额
	private String getOrder_m(int Order_id) {
		String se = context.getServerip();
		int po = context.getPort();

		ClerkAdm adm = new ClerkAdm(se, po);
		Response r = adm.getEnd_m(Order_id);
		Operator op = (Operator) r.getObject();
		if (op == null){
			return "-1";
			}else{
				return op.getMoney();
			}
	}

	private void logon() {

		if (RealTime.getInstance().testOnLine()) {
			if (confirm("服务器成功连接，联机工作(确定)或脱机工作(取消)？")) {
				context.setOnLine(true);
				state.setState(PosState.PRELOGON);
				return;
			}
		}

		initLogonIO();
		out.displayState("请登录");
		out.clear();

		String note = context.getWarning();
		if (note.length() == 0)
			note = "请输入收银员编号和密码";

		PosInput inp = in.getInputLogon(note + "(脱机状态)");
		if (inp == null)
			return;
		if (inp.key() == PosFunction.CANCEL)
			return;
		if (inp.key() == PosFunction.EXIT)
			System.exit(0);

		if (!(inp instanceof PosInputLogon)) {
			System.out.println("Canceled!");
			return;
		}

		PosInputLogon input = (PosInputLogon) inp;

		String id = input.getID();
		String pin = input.getPIN();
		//TODO 沧州富达 by fire 2005_5_11
		//int work_turn = input.getShiftid();

		OperatorList lst = new OperatorList();
		lst.load("operator.lst");

		Operator op = lst.get(id, pin);
		if (op == null) {
			core.writelog("登录", "1", 0);
			context.setWarning("用户名或密码不正确");
			state.setState(PosState.PRELOGON);
			logonDialog.getLogonPanel().clear();
		} else {
			//  context.updateWorkDay();
			context.setWorkDate(new Day());
			//-------------
			cashier = op;
			core.setCashierid(id);

			logonDialog.dispose();

			openPosWindow();
			lock.setState(true);

			int getid = Integer.parseInt(op.getId());
			core.writelog("登录", "0", getid);

			out.print("收银员:" + op.getId() + " 登录");
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			in.clearKey();
			if (core.isSheetEmpty()) {
				//// out.displayHeader(context);
				out.displayWelcome();
				state.setState(PosState.PRESALE);
			} else {
				//// out.displayHeader(context);
				out.displayWelcome();
				if (core.getPosSheet().getMemberCard() != null)
					out
							.dispMemberCardHeader(core.getPosSheet()
									.getMemberCard());
				if (core.getPosSheet().getCouponNO() != null
						&& !core.getPosSheet().getCouponNO().equals(""))
					out.dispCouponHeader(core.getPosSheet().getCouponNO());
				displaySheet();
				state.setState(PosState.SALE);
			}

		}
	}

	private void initLogonIO() {
		pos.posOutStream = null;
		PipedInputStream posInputStream = null;

		try {
			pos.posOutStream = new PipedOutputStream();
			posInputStream = new PipedInputStream(pos.posOutStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// init logonDislog ...
		if (logonDialog == null) {
			logonDialog = new LogonDialog(pos.posOutStream);
			if (pos.startFrame != null) {
				pos.startFrame.dispose();
			}
		} else {
			logonDialog.getLogonPanel().setOutputStream(pos.posOutStream);
		}
		logonDialog.show();
		logonDialog.requestFocus();

		// Init PosIO
		out = PosDevOut.getInstance();
		out.setMainUI(logonDialog.getLogonPanel());
		in = PosDevIn.getInstance();
		in.setOut(out);
		in.setPosInputStream(posInputStream);
		in.init();

	}

	private void openPosWindow() {
		pos.posFrame = new PosFrame(pos.posOutStream);
		out.setMainUI(pos.posFrame);
		pos.posFrame.setUndecorated(true);
		pos.posFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		pos.posFrame.show();
	}

	// 返回1为预订单中断电；
	// 返回0正常销售下断电；
	private int stateNew(Sale s) {
		int pr = 0;
		if (s.getVgno().equals("999999")) {
			return pr = 1;
		}
		return pr;
	}

	private void displaySheet() {

		switch (stateNew(core.getFalseSale(0))) {
		case 0:
			PosDevOut.getInstance().displayHeader(context);
			if (core.getPosSheet().getCouponNO() != null
					&& !core.getPosSheet().getCouponNO().equals(""))
				out.dispCouponHeader(core.getPosSheet().getCouponNO());
			state.setState(PosState.SALE);
			break;
		case 1:
			state.setState(PosState.FUTRUESELL);
			PosDevOut.getInstance().displayHead(context);
			break;
		default:
		}
		
		// 打印会员卡号
		if (core.getPosSheet().getMemberCard() != null)
			out
					.dispMemberCardHeader(core.getPosSheet()
							.getMemberCard());


		sale_rec = core.getFalseSale(core.getFalseSaleLen()-1);		// zhouzhou add 
		
		int temp = 0;
		for (int i = 0; i < core.getFalseSaleLen(); i++) {

			out.displayUnhold(core.getFalseSale(i), core.getValue());
		}

		for (int i = 0; i < core.getPayLen(); i++) {
			out.display(core.getPayment(i), core.getValue());
		}
		out.display(core.getValue(), context.getExchange());
	}

	/*
	 * private void wait_unlock() {
	 *  out.displayState("已锁定"); PosInput inp =
	 * in.getInputPin("POS机已锁定，请输入收银员密码解锁。"); if (inp == null || !(inp
	 * instanceof PosInputPin)) return;
	 * 
	 * PosInputPin input = (PosInputPin) inp;
	 * 
	 * String pin = input.getPIN();
	 * 
	 * ClerkAdm adm = new ClerkAdm(context.getServerip(), context.getPort());
	 * Response r = adm.getClerk(context.getPosid(), context.getCashierid(),
	 * pin); Operator op = (Operator) r.getObject(); if (op != null) { if
	 * (core.isSheetEmpty()) state.setState(PosState.PRESALE); else
	 * state.setState(PosState.SALE); } }
	 */
	public void setStateBUnlock() {
		if (core.isSheetEmpty())
			state.setState(PosState.PRESALE);
		else
			state.setState(PosState.SALE);
	}

	private boolean getAuthority(GetAuthor author) throws UserCancelException {
		Authorization authrization = new Authorization();
		MainUI oldMainUI = out.getMainUI();
		out.setMainUI(authrization);
		authrization.show();
		try {

			PosInput inp;
			inp = in.getInputAuthority("请输入授权主管的编号和密码");
			out.setMainUI(oldMainUI);
			authrization.dispose();

			if (inp == null || !(inp instanceof PosInputLogon)) {
				System.out.println("Canceled!");
				//修改
				return false;
			}

			PosInputLogon input = (PosInputLogon) inp;

			String id = input.getID();
			String pin = input.getPIN();

			if (author != null) {
				author.setid(id);
			}

			Operator op = null;

			if (context.isOnLine()) {
				ClerkAdm adm = new ClerkAdm(context.getServerip(), context
						.getPort());
				Response r = adm.getClerk(context.getPosid(), id, pin);

				if (r != null) {
					if (r.retCode() != -1) {
						op = (Operator) r.getObject();
					} else {
						OperatorList lst = new OperatorList();
						lst.load("operator.lst");
						op = lst.get(id, pin);
					}
				}
			} else {
				OperatorList lst = new OperatorList();
				lst.load("operator.lst");
				op = lst.get(id, pin);
			}

			if (op != null) {
				cashier.addPrivilege(op);
				context.setAuthorizerid(op.getId());
				authorizeCashier = op;
			}
			//修改
			else
				return false;

			return true;
		} catch (UserCancelException e) {
			out.setMainUI(oldMainUI);
			authrization.dispose();
			throw new UserCancelException("User Cancel!");
		}

	}

	private boolean checkPrivilege(int fun) throws UserCancelException {

		return checkPrivilege(fun, null);
	}

	private boolean checkPrivilege(int fun, GetAuthor author)
			throws UserCancelException {
		//首先判断是否需要授权
		if (this.needAuth(fun)) {
			if (!getAuthority(author)) {
				context.setWarning("授权失败,按清除键继续!");
				//              //荣华项目预销售，增加部份
				if (state.getState() == PosState.FUTRUESELL) {
					return false;
				}
				state.setState(PosState.ERROR);
				return false;
			}
		} else if (!cashier.hasPrivilege(fun)) {
			if (!getAuthority(author)) {
				context.setWarning("授权失败,按清除键继续!");
				// 荣华项目预销售，增加部份
				if (state.getState() == PosState.FUTRUESELL) {
					return false;
				}
				state.setState(PosState.ERROR);
				return false;
			}
		} else {
			authorizeCashier = cashier;
			if (author != null) {
				author.setid(cashier.getId());
			}
		}

		if (!cashier.hasPrivilege(fun)) {
			context.setWarning("授权失败,按清除键继续!");
			state.setState(PosState.ERROR);
		}

		return cashier.hasPrivilege(fun);
	}

	/**
	 * 判断参数配置中使用的功能是否需要授权
	 * 
	 * @param fun
	 * @return
	 */
	private boolean needAuth(int fun) {
		String authStr = this.getAuthStr(fun);
		PosConfig config = PosConfig.getInstance();
		String authFlag = config.getString(authStr);
		if (authFlag.equals("ON"))
			return true;
		return false;
	}

	/**
	 * 判断参数配置中是否可以使用这种功能
	 */
	public boolean needAuth(String name) {
		PosConfig config = PosConfig.getInstance();
		String authFlag = config.getString(name);
		if (authFlag.equals("ON"))
			return true;
		return false;
	}

	private String getAuthStr(int fun) {
		if (fun == 38)
			return "AUTH_BLANKTRAN";
		if (fun == 41)
			return "AUTH_DiscMoney";
		if (fun == 43)
			return "AUTH_LASTPRINT";
		if (fun == 60)
			return "IF_INCR_AUTH";
		if (fun == 62)
			return "IF_DECR_AUTH";
		if (fun == 67)
			return "AUTH_VOIDITEM";
		if (fun == 70)
			return "AUTH_Return";
		if (fun == 77)
			return "AUTH_AltPrice";
		if (fun == 123)
			return "AUTH_Discount";
		if (fun == 125)
			return "AUTH_DiscTotal";
		return "";
	}

	//-----------------------------------
	private void showWaiter() {
		String waiter_show;

		DispWaiter dispWaiter = new DispWaiter();
		dispWaiter.show();

		if (dispWaiter.isConfirm()) {

			waiter_show = dispWaiter.getShowWaiter();
			System.out.println("输入营业员完成");
			out.dispWaiter("营业员:" + waiter_show);
			out.setWaiter(waiter_show);

			state.setState(PosState.PRESALE);
		}

	}

	public boolean prepareDir(String dirname) {
		File dir = new File(dirname);
		if (dir.exists() && dir.isDirectory())
			return true;
		if (dir.exists() && !dir.isDirectory())
			return false;
		return dir.mkdir();
	}

	public boolean prepareFile(String filename) {
		File file = new File(filename);
		if (file.exists() == true) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 重打上一单，将reprintsheet.xml文件解析成一张虚拟销售进行打印，只能重打上一单销售
	 */
	private void printLastSheet(PosInputPayment payment) {
		prepareDir("reprint");
		boolean fileexist = prepareFile("reprint/reprintsheet.xml");
		if (fileexist == false) {

			core.setWarning("重打小票文件不存在,按清除键继续!");
			state.setState(PosState.ERROR);
		} else {

			out.printFeed(out.getFeedLines());
			out.cutPaper();
			//out.dispLastPrint(context,payment);
			out.dispLastPrint(context);
			//// out.displayHeader(context);
		}
	}

	/**
	 * 打印销售清单
	 */
	private void printInvoice() {
		out.printFeed(out.getFeedLines());
		out.cutPaper();
		out.printInvoice();
	}

	/**
	 * 修改密码，只能在联机状态下进行修改行为
	 */
	private void alterPin() {
		String pin_old, pin_new, pin_confirm;
		String server = context.getServerip();
		int port = context.getPort();
		String posid = context.getPosid();
		String cashierid = context.getCashierid();

		ModifyPassword modifyPassword = new ModifyPassword();
		modifyPassword.show();

		if (modifyPassword.isConfirm()) {
			pin_old = modifyPassword.getOldPassword();
			pin_new = modifyPassword.getNewPassword();
			pin_confirm = modifyPassword.getConfirmPassword();

			if (pin_new.equals(pin_old))
				core.setWarning("输入无效：新密码与旧密码相同,按清除键继续!");
			if (!pin_new.equals(pin_confirm))
				core.setWarning("输入无效：两次输入的密码不一致,按清除键继续!");
			if (!pin_new.equals(pin_old) && pin_new.equals(pin_confirm)) {
				ClerkAdm adm = new ClerkAdm(server, port);
				Response r = adm.alterPin(posid, cashierid, pin_old, pin_new);
				if (r != null && r.succeed())
					core.setWarning("密码修改成功,按清除键继续!");
				else
					core.setWarning("密码修改失败,按清除键继续!");
			}

			state.setState(PosState.ERROR);
		} else {
			//			core.setWarning("取消");
			//			state.setState(PosState.ERROR);
		}
	}

	private void logoff() {

		int count = context.getHeldCount();
		if (count > 0) {
			context.setWarning("有挂单不能退出,按清除键继续!");
			core.writelogexit("退出", "1", 0);
			state.setState(PosState.ERROR);
		} else if (confirm("是否退出系统？")) {
			if (context.isOnLine()) {
				WorkTurnAdm w = new WorkTurnAdm(context);
				Response rep = (Response) w.logoff();

				synchronized (pos.uploadLock) {
					JournalManager journalManager = new JournalManager();
					journalManager.upload(out);
				}
			}

			lock.setState(false);
			pos.posFrame.exit();
			state.setState(PosState.PRELOGON);
			core.writelogexit("退出", "0", 0);
			System.out.println("退出成功......");
			out.print("收银员:" + context.getCashierid() + " 退出");
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			context.setWarning("");
			generateLog();
		}

	}

	private void closeWorkTurn() {

		int count = context.getHeldCount();
		if (count > 0) {
			context.setWarning("有挂单不能作班结,按清除键继续!");
			state.setState(PosState.ERROR);
			return;
		}

		if (context.getShiftid() == 3) {
			if (context.getWorkDate().getGregorian().getTime().compareTo(
					(new Date())) >= 0) {
				context.setWarning("工作日期不能大于系统日期,按清除键继续!");
				state.setState(PosState.ERROR);
				return;
			}
		}

		if (RealTime.getInstance().testOnLine()) {
			context.setOnLine(true);
		} else {
			context.setOnLine(false);
		}
		out.displayConnStatus(context);

		DialogConfirm confirm;
		if (context.isOnLine()) {
			confirm = new DialogConfirm();
			confirm.setMessage("班结后不能再收银。确实要作班结吗？");
		} else {
			confirm = new DialogConfirm(580, 160);
			confirm.setMessage("系统处在脱机状态，本地销售数据可能还没上传。确实要作脱机班结吗？");
		}

		confirm.show();
		if (!confirm.isConfirm())
			return;

		synchronized (pos.uploadLock) {
			JournalManager journalManager = new JournalManager();

			int unloadCount1 = journalManager.getUnuploadCount();
			int unloadCount2 = JournalLogList.getUnuploadCount();

			if (unloadCount1 != unloadCount2) {
				System.out.println("脱机流水文件数目和流水日志记录的流水数不一致!");
				System.out.println(Formatter.getDateFile(new Date())
						+ " FileCount=" + unloadCount1 + " LogCount="
						+ unloadCount2);
			} else {
				System.out.println(Formatter.getDateFile(new Date())
						+ " FileCount=" + unloadCount1);
			}

			DialogConfirm confirm2 = new DialogConfirm(550, 160);
			while (unloadCount1 > 0) {

				confirm2.setMessage("还有 " + unloadCount1
						+ " 个脱机流水没上传，重新尝试上传(确定)或继续班结(取消)？");
				confirm2.setWarning("继续班结可能会导致这些脱机流水算在下一班的销售数据中！");
				confirm2.show();

				if (!confirm2.isConfirm()) {
					System.out.println(Formatter.getDateFile(new Date())
							+ " 用户选择不上传脱机流水,继续班结.");
					break;
				} else {
					System.out.println(Formatter.getDateFile(new Date())
							+ " 用户选择重联,继续上传脱机流水.");
				}

				if (RealTime.getInstance().testOnLine()) {
					context.setOnLine(true);
					journalManager.upload(out);
				} else {
					context.setOnLine(false);
				}

				unloadCount1 = journalManager.getUnuploadCount();
			}

			if (unloadCount1 == 0) {

				System.out.println(Formatter.getDateFile(new Date())
						+ " Rename JournalLogList");
				JournalLogList.rename();
			}

		}

		if (context.isOnLine()) {
			closeWorkTurn_online();
		} else {

			try {
				if (checkPrivilege(PosFunction.OFFLINECLOSEWORKTURN, null)) {
					closeWorkTurn_offline();
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
				} else {
					state.setState(PosState.ERROR);
				}
			} catch (UserCancelException e) {
				e.printStackTrace();
			}

		}
	}

	private void closeWorkTurn_online() {

		synchronized (pos.workTurnLock) {

			com.royalstone.pos.workTurn.WorkTurnAdm w = com.royalstone.pos.workTurn.WorkTurnAdm
					.getInstance();
			try {
				w.closeWorkTurn(context, context.getPosid());
			} catch (WorkTurnException e1) {
				context.setWarning(e1.getMessage());
				state.setState(PosState.ERROR);
				return;
			}

		}

		LogManager logManager = new LogManager();
		logManager.delete();

		// 打印清机单 ...
		if (PosConfig.getInstance().getString("CLEAR_POS") != null
				&& PosConfig.getInstance().getString("CLEAR_POS").equals("ON")) {
			CashBasket basket = core.getCashBasket();
			out.printFeed(10);
			out.print("清机单");
			out.printWithoutSeperator("收银员:" + context.getCashierid());
			out.printWithoutSeperator("日  期:"
					+ context.getWorkDate().toString());
			out.printWithoutSeperator("班  次:"
					+ context.getWorkTurn().getShiftid());
			for (int i = 0; i < basket.size(); i++) {
				CashBox box = basket.get(i);
				System.out.println(box.toString());
				out.display(box);
			}
			out.print("");
			out.printFeed(out.getFeedLines());
			out.cutPaper();
		}

		try {
			core.resetCashBasket();
			core.dump();
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.prompt("正在更新脱机资料……");
		pos.reinit();

		DialogInfo notice = new DialogInfo();

		notice.setMessage("班结成功。");
		notice.show();

		pos.posFrame.exit();
		out.print("班结");
		out.print("收银员:" + context.getCashierid() + " 退出");
		out.printFeed(out.getFeedLines());
		out.cutPaper();
		state.setState(PosState.PRELOGON);
		context.setWarning("");
		generateLog();
	}

	private void closeWorkTurn_offline() {

		synchronized (pos.workTurnLock) {

			PosTurnList turnList = PosTurnList.getInstance();
			ArrayList activeTurnList = turnList.findByState(0);
			if (activeTurnList.size() > 0) {

				PosTurn activeTurn = (PosTurn) activeTurnList.get(0);
				activeTurn.setEndOffLine(true);
				activeTurn.setEndTime(new Date());
				activeTurn.setStat(1);
				turnList.dump();

				ShopClock shopClock = ShopClock.getInstance();
				shopClock.nextTurn(context);

				DialogInfo notice = new DialogInfo();
				// 打印清机单 ...
				if (PosConfig.getInstance().getString("CLEAR_POS") != null
						&& PosConfig.getInstance().getString("CLEAR_POS")
								.equals("ON")) {
					CashBasket basket = core.getCashBasket();
					out.printFeed(10);
					out.print("清机单");
					out.printWithoutSeperator("收银员:" + context.getCashierid());
					out.printWithoutSeperator("日  期:"
							+ context.getWorkDate().toString());
					out.printWithoutSeperator("班  次:"
							+ context.getWorkTurn().getShiftid());
					for (int i = 0; i < basket.size(); i++) {
						CashBox box = basket.get(i);
						System.out.println(box.toString());
						out.display(box);
					}
					out.print("");
					out.printFeed(out.getFeedLines());
					out.cutPaper();
				}

				try {
					core.resetCashBasket();
					core.dump();
				} catch (IOException e) {
					e.printStackTrace();
				}

				notice.setMessage("脱机班结成功。");
				notice.show();

				pos.posFrame.exit();
				out.print("脱机班结");
				out.print("收银员:" + context.getCashierid() + " 退出");
				out.printFeed(out.getFeedLines());
				out.cutPaper();
				state.setState(PosState.PRELOGON);
				context.setWarning("");
				generateLog();
			} else {
				context.setWarning("班次数据错误,按清除键继续!");
				state.setState(PosState.ERROR);
			}
		}

	}

	// 打开钱箱方法
	private void commitSheet(PosInputPayment input) {
		boolean order = false;
		core.setCurrency("RMB");
		if (needAuth("PRINT_TALCOUNT")) {
			int Count = 0;
			for (int i = 0; i < core.getFalseSaleLen(); i++) {
				Count += core.getFalseSale(i).getQtyInt();
			}
			out.display(Count, true);
		}
		out.display(core.getValue(), context.getExchange());
		// 清除界面
		out.clear();
		out.displayChange(core.getValue());
		// 清除
		context.setLargessCoupon(0);
		//add by lichao
		for (int i = 0; i < core.getPayLen(); i++) {
			Payment p = core.getPayment(i);
			if (p.getType() == 'C' || p.getType() == 'D') {
				//System.out.println("只有现金或者代币券才打开钱箱");
				cashDrawer.open();
				break;
			}
		}

		for (int i = 0; i < core.getPayLen(); i++) {
			Payment p = core.getPayment(i);
			//判断提货卡的支付方付
			if (p.getType() == 'O') {
				order = true;
				break;
			}
		}

		//cashDrawer.open();
//		out.displayTrail(core.getValue());
		out.displayTrail(core.getValue(),core.getPosSheet().isCouponEncash);

		if (order) {
			core.closeSheets(input);
		} else {
			// 写硬盘数据
			core.closeSheet(input);
			String filename = "largessinfo" + File.separator+ "2006RH.xml";
			File file = new File(filename);
			file.delete();
			
			core.couponLargessList.remove();
			context.setGrouplargess(false);
			context.setCounp(false);
			context.setGrouplargessT(false);
			for (int i = 0; i < core.couponListLargess.size(); i++) {
				core.couponListLargess.remove(i);
			}
		}
		
		this.printProdProperty(core);
		
		try {
			core.openSheet();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//// out.displayHeader(context);
		out.dispWaiter("");
		out.setWaiter("");
		out.displayWelcome();

		//        System.out.println("比较结果为:"+core.CompareLimit());

		if (core.CompareLimit() == 1) {
			DialogInfo notice = new DialogInfo();
			notice.setMessage("请将现金入库。");
			notice.show();
			state.setState(PosState.PRESALE);
		} else if (core.CompareLimit() == 2) {
			DialogInfo notice = new DialogInfo();
			notice.setMessage("已超最大限额，请将现金入库。");
			notice.show();
			state.setState(PosState.MAXCASH);
		} else {
			state.setState(PosState.PRESALE);
		}
	}

	private void commitSheets(PosInputPayment input) {
		core.setCurrency("RMB");
		out.display(core.getValue(), context.getExchange());
		// 清除界面
		out.clear();
		out.displayChange(core.getValue());
		//add by lichao
		for (int i = 0; i < core.getPayLen(); i++) {
			Payment p = core.getPayment(i);
			if (p.getType() == 'C' || p.getType() == 'D') {
				//System.out.println("只有现金或者代币券才打开钱箱");
				cashDrawer.open();
				break;
			}
		}

		//		cashDrawer.open();
//		out.displayTrail(core.getValue());
		out.displayTrail(core.getValue(),core.getPosSheet().isCouponEncash );
		// 写硬盘数据
		core.closeSheets(input);
		try {
			core.openOrder();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//// out.displayHeader(context);
		out.dispWaiter("");
		out.setWaiter("");
		out.displayWelcome();

		//System.out.println("比较结果为:"+core.CompareLimit());

		if (core.CompareLimit() == 1) {
			DialogInfo notice = new DialogInfo();
			notice.setMessage("请将现金入库。");
			notice.show();
			state.setState(PosState.PRESALE);
		} else if (core.CompareLimit() == 2) {
			DialogInfo notice = new DialogInfo();
			notice.setMessage("已超最大限额，请将现金入库。");
			notice.show();
			state.setState(PosState.MAXCASH);
		} else {
			state.setState(PosState.PRESALE);
		}
	}

	private void deletefindresult() {
		out.clear();
		//out.print("取消");
		out.printFeed(out.getFeedLines());
		out.cutPaper();
		core.deleteFindresult();
		try {
			core.findpriceSheet();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state.setState(PosState.PRESALE);
		//// out.displayHeader(context);
	}
	
	//是否还有赠送的价值
	private boolean getAddPrice(){
		for (int i = 0; i < core.couponLargessList.size(); i++) {
			CouponLargess couponLargess = core.couponLargessList.get(i);
			if (couponLargess.getAddPrice() >= 100){
				return true;
				}
			}
		return false;
		}

	private void deleteSheet() throws Exception {

		try {
			core.deleteSheet();
		} catch (Exception e) {
			throw e;
		}
		out.clear();
		out.print("整单取消");
		out.printFeed(out.getFeedLines());
		out.cutPaper();
		try {
			core.openSheet();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//// out.displayHeader(context);
		out.displayWelcome();
		state.setState(PosState.PRESALE);
	}

	private void deleteOrder() throws Exception {

		try {
			core.deleteOrder();
		} catch (Exception e) {
			throw e;
		}
		out.clear();
		out.print("整单取消");
		out.printFeed(out.getFeedLines());
		out.cutPaper();
		try {
			core.openOrder();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//// out.displayHeader(context);
		out.displayWelcome();
		state.setState(PosState.PRESALE);
	}

	private Payment closeSheetWithoutMoney(int k) {
		int paytype;
		if (k == PosFunction.FLEE)
			paytype = Payment.FLEE;
		else if (k == PosFunction.SAMPLE)
			paytype = Payment.SAMPLE;
		else if (k == PosFunction.OILTEST)
			paytype = Payment.OILTEST;
		else {
			context.setWarning("无效操作,按清除键继续!");
			return null;
		}

		Payment p = core.closeSheetWithoutMoney(paytype);
		return p;
	}

	private void presale() {
		out.display(context);
		if (context.getGrouplargess()) {
			out.displayState("机团销售");
		} else {
			out.displayState("销售");
		}
		out.prompt("请输入商品数量及条码，或按功能键。");
		PosInput input = in.getInput();

		NotifyChangePriceConsumer.getInstance().consume();

		switch (input.key()) 
		{
		
		case PosFunction.FIND:
			this.find();
			break;
		case PosFunction.CANCEL:
			break;
		case PosFunction.GROUPLARGESS:
			if (confirm(PosFunction.toString(input.key()) + "?")) {
				context.setGrouplargess(true);
			}
			break;
		// 预订单
		case PosFunction.FUTRUESELL:
			if (confirm(PosFunction.toString(input.key()) + "?")) {
				state.setState(PosState.FUTRUESELL);
				String buffer = "999999";
				int amount = 1;
				PosInputGoods inputg = new PosInputGoods(buffer, amount);
//				PosDevOut.getInstance().displayHead(context);
				try {
					sellGoods((PosInputGoods) inputg);
				} catch (CouponException e1) {
					e1.printStackTrace();
					context.setWarning(e1.getMessage());
					state.setState(PosState.ERROR);
				}
				break;
			}
			break;
		// -----------------------
		case PosFunction.WITHDRAW:
			try {
				if (checkPrivilege(PosFunction.WITHDRAW))
					state.setState(PosState.WITHDRAW);
				else
					state.setState(PosState.ERROR);
			} catch (UserCancelException e) {
			}

			break;
		case PosFunction.GOODS:
			try {
				sellGoods((PosInputGoods) input);
			} catch (CouponException e2) {
				e2.printStackTrace();
				context.setWarning(e2.getMessage());
				state.setState(PosState.ERROR);
			}
			break;
		
		//IC卡储值充值
	    case PosFunction.CardRA:
	    	RaCardAuto();
	    	break;
		/*
		 * case PosFunction.FIND : state.setState(PosState.FIND); break;
		 */
		case PosFunction.LOCK:
			state.setState(PosState.LOCK);
			//UnLock unLock = new UnLock();
			//unLock.show();
			break;
		case PosFunction.HOLD:
			unhold();
			break;
		case PosFunction.SHIFT:
			// this.closeWorkTurn();
			try {
				if (checkPrivilege(PosFunction.SHIFT))
					printClearTicket();
				else
					state.setState(PosState.ERROR);
			} catch (UserCancelException e) {
				// do nothing.
			}
			// printClearTicket();
			break;
		case PosFunction.EXIT:
			logoff();
			break;
		case PosFunction.NEWPASS:
			if (context.isOnLine()) {
				alterPin();
			} else {
				context.setWarning("不能在脱机状态下修改密码，按清除键继续!");
				state.setState(PosState.ERROR);
			}
			break;
		case PosFunction.LARGESSCOUPON:
			context.setWarning("未销售商品不能使用赠券功能，按清除键继续!");
			state.setState(PosState.ERROR);
			break;
		case PosFunction.WAITER:
			showWaiter();
			break;
		case PosFunction.PRINTLASTSHEET:
			//				printLastSheet(inputpayment);
			//				break;
			//增加重打上一单的权限控制功能
			try {
				if (checkPrivilege(PosFunction.PRINTLASTSHEET))
					printLastSheet(inputpayment);
				else
					state.setState(PosState.ERROR);
			} catch (UserCancelException e) {
			}
			break;
		case PosFunction.CASHIN:
			//				state.setState(PosState.CASHIN);
			//				break;
			//增加钱箱入款功能的权限
			try {
				if (checkPrivilege(PosFunction.CASHIN))
					state.setState(PosState.CASHIN);
				else
					state.setState(PosState.ERROR);
			} catch (UserCancelException e) {
				// do nothing.
			}
			break;
		case PosFunction.CASHOUT:
			//				state.setState(PosState.CASHOUT);
			//				break;
			//				增加钱箱出款功能的权限
			try {
				if (checkPrivilege(PosFunction.CASHOUT))
					state.setState(PosState.CASHOUT);
				else
					state.setState(PosState.ERROR);
			} catch (UserCancelException e) {
				// do nothing.
			}
			break;
		case PosFunction.OPENCASHBOX:
			try {
				GetAuthor author = new GetAuthor();
				if (checkPrivilege(PosFunction.OPENCASHBOX, author)) {
					//在此若按下的是打开钱箱键则打开钱箱
					cashDrawer.open();
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;

				} else {
					state.setState(PosState.ERROR);
				}

			} catch (UserCancelException e) {
				// do nothing.
			}
			break;
		case PosFunction.SHOWCASHBOX:
			try {
				GetAuthor author = new GetAuthor();
				if (checkPrivilege(PosFunction.SHOWCASHBOX, author)) {
					CashBoxInfo cashBoxInfo = new CashBoxInfo();
					CashBasket basket = core.getCashBasket();
					out.printFeed(out.getFeedLines());
					//out.cutPaper();
					//out.printFeed(3);
					out.displayHeader(context);
					out.print("收款检查");
					out.printWithoutSeperator("检查员:" + author.getid());
					out.printWithoutSeperator("收银员:" + context.getCashierid());
					out.printWithoutSeperator("日  期:"
							+ Formatter.getDate(new Date()));
					for (int i = 0; i < basket.size(); i++) {
						CashBox box = basket.get(i);
						out.display(box);
						cashBoxInfo.addCashBox(box);
					}
					out.print(Formatter.getTime(new Date()));
					out.printFeed(out.getFeedLines());
					out.cutPaper();
					//// out.displayHeader(context);
					cashBoxInfo.show();
					cashier.resetPrivilege();
					context.setAuthorizerid("");
				} else {
					state.setState(PosState.ERROR);
				}

			} catch (UserCancelException e) {
				// do nothing.
			}
			break;
		case PosFunction.HKD:
			if (context.getCurrenCode().equals("HKD"))
				core.setCurrency("RMB");
			else
				core.setCurrency("HKD");
			out.display(core.getValue(), context.getExchange());
			break;
		case PosFunction.OFFLINE:
			if (context.isOnLine()) {
				try {
					if (checkPrivilege(PosFunction.OFFLINE)) {
						notice("成功转换为脱机工作方式!");
						context.setOnLine(false);
						out.display(context);
						cashier.resetPrivilege();
						context.setAuthorizerid("");
					}
				} catch (UserCancelException e1) {
				}
			} else {
				out.prompt("正在尝试联机……");
				if (RealTime.getInstance().testOnLine()) {
					notice("联机成功!");
					context.setOnLine(true);
					out.display(context);
				} else {
					in.clearKey();
					notice("联机失败!");
				}
			}
			break;
		case PosFunction.DELETE:
			if (core.getPosSheet().getCouponValue() > 0) {
				out.getMainUI().clear();
				core.getPosSheet().setCouponValue(0);
				core.getPosSheet().setCouponNO("");
				core.getPosSheet().couponSales = new CouponSaleList();
			}
		case PosFunction.PAYMENT:
		case PosFunction.CORRECT:
		case PosFunction.QUICKCORRECT:
			break;
		case PosFunction.Coupon:
			try {
				encashCoupon();
			} catch (IOException ex) {
				ex.printStackTrace();
				context.setWarning("兑现时发生网络故障,按清除键继续!");
				state.setState(PosState.ERROR);
			} catch (CouponException e) {
				e.printStackTrace();
				context.setWarning(e.getMessage());
				state.setState(PosState.ERROR);
			}
			break;
		case PosFunction.ticket:
			printAnyTicket();
			break;
		default:
			//                context.setWarning("无效操作,按清除键继续!");
			//                state.setState(PosState.ERROR);
			;
		}
	}

	private void printAnyTicket() {
		int ticketNO = 0;
		try {
			ticketNO = Integer.parseInt(this.getInputNo(5, "请输入小票号", "loan"));
		} catch (UserCancelException e) {
			return;
		}
		DecimalFormat df = new DecimalFormat("00000");
		//TODO 沧州富达 by fire 2005_5_11
		String ticketFile = "journal/" + new Day().toString() + "/"
				+ new Day().toString() + "." + context.getPosid() + "."
				+ df.format(ticketNO) + ".xml";

		boolean fileexist = prepareFile(ticketFile);
		if (fileexist == false) {

			core.setWarning("重打任意小票文件不存在,按清除键继续!");
			state.setState(PosState.ERROR);
		} else {

			out.printFeed(out.getFeedLines());
			// out.cutPaper();
			out.printAnyTicket(context, ticketFile);
		}

	}
	
	//储值卡充值
	private void RaCardAuto()
	{
		try {
		if (pos.core.getPosSheet().getSaleLen() != 0) {
			context.setWarning("卖商品的时候不允许充值,按清除键继续!");
			state.setState(PosState.ERROR);
			return;
		}
			out.displayState("储值卡充值");
			out.prompt("请输入储值卡充值金额，并按现金键。");

			PosInput input = in.getInput();
			if (input.key() == PosFunction.CANCEL) {
				state.setState(PosState.PRESALE);
			} else if ((input.key() == PosFunction.PAYMENT || input.key() == PosFunction.CARDBANK)
					&& ((PosInputPayment) input).getCents() > 0) {
              	SHValueCardProcess scp = new SHValueCardProcess((PosInputPayment) input);
              	int flag=-1;
              	flag = scp.process();
				if (flag!=-1) {
					PosInputPayment p = (PosInputPayment) input;
//					Payment pay_info = core.cashout(p);
					double rate = context.getCurrenRate();
					int value = p.getCents();
					int value_equiv = (int) (value * rate);
					Payment pay_info = core.cashoutRa(p,scp.getCardNo());
					if (pay_info == null) {
						core.writelog("充值", "1", 0);
						state.setState(PosState.ERROR);
					} else {
						String s;
						cashDrawer.open();
						s = (context.getCurrenCode() == "RMB") ? "" : context
								.getCurrenCode();

						s += " " + new Value(pay_info.getValue()).toString();

						notice("充入金额：" + s);
						out.displayHeader(context);
						if(flag==0)
						    out.displayCardRa("储值卡充值", s,scp.getCardNo(),scp.getAddTotal(),core.getPosSheet().getShopCard());
						else
							out.displayCardRa("***储值卡充值", s,scp.getCardNo(),scp.getAddTotal(),core.getPosSheet().getShopCard());	
						core.openSheet();
						out.displayWelcome();
						core.writelog("储值卡充值成功","1", 0);
						state.setState(PosState.PRESALE);
					}
				} else {
					if (scp.getExceptionInfo() != null) {
						core.writelog("储值卡充值失败", "1", 0);
						context.setWarning(scp.getExceptionInfo()+",按清除键继续!");
						state.setState(PosState.ERROR);
//						return cancelInput(scp.getExceptionInfo());
					}
				}
			}else {
				context.setWarning("无效输入,按清除键继续!");
				state.setState(PosState.ERROR);
			}
		} catch (IOException e) {
			context.setWarning("发生异常,按清除键继续!");
			state.setState(PosState.ERROR);
		}
	}

	private void encashCoupon() throws IOException, CouponException {

		if (pos.core.getPosSheet().isCouponSale) {
			context.setWarning("卖券的时候不能兑换券,按清除键继续!");
			state.setState(PosState.ERROR);
			return;
		}
		if (pos.core.getPosSheet().getSaleLen() != 0
				&& !pos.core.getPosSheet().isCouponEncash) {
			context.setWarning("卖商品的时候不能兑换券,按清除键继续!");
			state.setState(PosState.ERROR);
			return;
		}

		String cardno = "";
		try {
			cardno = this.getInputNo(18, "请输入券号", "bank");

		} catch (UserCancelException e) {
			return;
		}

		CouponMgr couponMgr = null;
		try {
			couponMgr = new CouponMgrImpl();
		} catch (Exception e) {
			context.setWarning("券库设置错误,按清除键继续!");
			state.setState(PosState.ERROR);
			return;
		}
		if (cardno.length() != 18) {
			context.setWarning("券号错误,按清除键继续!");
			state.setState(PosState.ERROR);
			return;
		}

		String cardNo = cardno.substring(0, 10);
		String secrety = cardno.substring(10, 18);
		CouponEnCash queryVO = couponMgr.query(cardNo, secrety);
		if (queryVO != null && queryVO.getExceptionInfo() == null) {
			if ((queryVO.getGoodsList() != null || queryVO.getGoodsList()
					.size() > 0)
					|| "0".equals(queryVO.getType())) {
				CouponSale couponSale = new CouponSale();
				couponSale.setCouponID(queryVO.getCouponID());
				couponSale.setCouponPass(queryVO.getCouponPass());

				couponSale.setMode(queryVO.getMode());
				couponSale.setMinFlag(queryVO.getMinFlag());
				couponSale.setPrice(queryVO.getPrice());
//				couponSale.setUpdateType("f");
				if (!core.getPosSheet().couponSales.add(couponSale)) {
					context.setWarning("此券重复过机,按清除键继续!");
					state.setState(PosState.ERROR);
					return;
					//                        }else
					// if(core.getPosSheet().couponSales.getUpdateType().equals("0")){
				} else {
					core.getPosSheet().couponSales.setUpdateType("encash"); //sale
					// 状态由
					// 4
					// 变为1
					// ，encash
					// 状态由
					// 1
					// 变为f
					// 0=不对应商品 1=对应商品
					//if ("0".equals(queryVO.getType())) {
					int couponValue = 0;
					if (queryVO.getPrice() == null
							|| "".equals(queryVO.getPrice()))
						couponValue = 0;
					else
						couponValue = (int) Math.rint(queryVO.getPrice()
								.doubleValue() * 100);
					core.getPosSheet().setCouponValue(couponValue);
					//显示卷
					out.getMainUI().dispCoupon(
							queryVO.getCouponID() + queryVO.getCouponPass());
					if (core.getSaleLen() == 0) {
						PosDevOut.getInstance().displayHeader(context);

					}
					Goods g = new Goods("", cardNo, "兑现券" + cardNo + secrety,
							secrety, "", "张",
							//				(int)queryVO.getPrice().doubleValue()*100,
							0, 0, 1, "n");
					Sale s = new Sale(g, 1);
					s.setType(Sale.ENCASH);
					core.getPosSheet().sell(s);
					out.displayEnCash(s);
					out.display(core.getValue(), context.getExchange());
					state.setState(PosState.SALE);

					//}

				}

				core.getPosSheet().setCouponNO(cardNo + secrety);

				for (int i = 0; i < queryVO.getGoodsList().size(); i++) {
					Goods tempGood = (Goods) queryVO.getGoodsList().get(i);
					int tempQty = ((BigDecimal) queryVO.getGoodsQty().get(i))
							.intValue();
					PosInputGoods pig = new PosInputGoods(tempGood.getVgno(),
							tempQty);
					//pig.setGoodsType('z');
					try {
						Sale s;
						s = core.sell(pig, 0, null);
						if (s == null) {
							state.setState(PosState.ERROR);
						} else {

							sale_rec = s;
							out.display(s, core.getValue());
							out.display(core.getValue(), context.getExchange());

							state.setState(PosState.SALE);

						}
					} catch (IOException e) {
						context.setWarning("生成销售文件错误,按清除键继续!");
						state.setState(PosState.ERROR);
					} catch (RealTimeException e) {
						state.setState(PosState.NETWORKERROR);
					}

				}

				pos.core.getPosSheet().isCouponEncash = true;

			} else {
				context.setWarning("此券没有可兑现商品,按清除键继续!");
				state.setState(PosState.ERROR);
				}

		}
		if (queryVO != null && queryVO.getExceptionInfo() != null) {
			context.setWarning(queryVO.getExceptionInfo() + ",按清除键继续!");
			state.setState(PosState.ERROR);
		}
		if (queryVO == null) {
			context.setWarning("查询饼券时网络故障,按清除键继续!");
			state.setState(PosState.ERROR);
		}

	}

	private void printClearTicket() {
		int valueAdd = 0;
		// 打印清机单 ...
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (PosConfig.getInstance().getString("CLEAR_POS") != null
				&& PosConfig.getInstance().getString("CLEAR_POS").equals("ON")) {
			CashBasket basket = core.getCashBasket();
			//            out.printFeed(3);
			out.print("清机单");
			//            out.printWithoutSeperator("店号:"+context.getStoreid()+"
			// 机号:"+context.getPosid());
			//            out.printWithoutSeperator("收银员:" + context.getCashierid());
			//            out.printWithoutSeperator(
			//                    "日 期:" + sdf.format(new Date()));

			out.displayClearHeader(context);

			//  out.displayHeaderWithoutGoods(context);
			for (int i = 0; i < basket.size(); i++) {
				CashBox box = basket.get(i);
				System.out.println(box.toString());
				if (box.getType() != Payment.Coupon) {
					out.display(box);
					valueAdd += box.getValue();
				}
			}
			out.print("总收款金额：" + (new Value(valueAdd)).toString());
			out.print("");
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			try {
				core.resetCashBasket();
				core.dump();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			context.setWarning("不允许打印清机条,按清除键继续!");
			state.setState(PosState.ERROR);
		}
	}

	private void maxcash() {
		try {
			out.displayState("出款");
			out.prompt("请输入出款金额，并按现金键。");

			PosInput input = in.getInput();
			if (input.key() == PosFunction.CANCEL) {
				state.setState(PosState.MAXCASH);
			} else if (input.key() == PosFunction.HKD) {
				if (context.getCurrenCode().equals("HKD"))
					core.setCurrency("RMB");
				else {
					core.setCurrency("HKD");
					out.display(core.getValue(), context.getExchange());
				}
			} else if (input.key() == PosFunction.SHOWCASHBOX) {
				try {
					if (checkPrivilege(PosFunction.SHOWCASHBOX)) {
						CashBoxInfo cashBoxInfo = new CashBoxInfo();
						CashBasket basket = core.getCashBasket();
						out.printFeed(10);
						out.displayHeader(context);
						out.print("收款检查");
						out.printWithoutSeperator("收银员:"
								+ context.getCashierid());
						out.printWithoutSeperator("日  期:"
								+ Formatter.getDate(new Date()));
						for (int i = 0; i < basket.size(); i++) {
							CashBox box = basket.get(i);
							out.display(box);
							cashBoxInfo.addCashBox(box);
						}
						out.print(Formatter.getTime(new Date()));
						out.printFeed(out.getFeedLines());
						out.cutPaper();
						//// out.displayHeader(context);
						cashBoxInfo.show();
						cashier.resetPrivilege();
						context.setAuthorizerid("");
					} else {
						state.setState(PosState.ERROR);
					}

				} catch (UserCancelException e) {
					// do nothing.
				}
			} else if (input.key() == PosFunction.PAYMENT
					&& ((PosInputPayment) input).getCents() > 0) {
				PosInputPayment p = (PosInputPayment) input;
				Payment pay_info = core.cashout(p);
				if (pay_info == null) {
					state.setState(PosState.MAXCASH);
				} else {
					String s;
					cashDrawer.open();
					s = (context.getCurrenCode() == "RMB") ? "" : context
							.getCurrenCode();

					s += " " + new Value(-pay_info.getValue()).toString();
					notice("请从钱箱取出以下金额：" + s);
					out.displayCash("出款", s);
					core.openSheet();
					//// out.displayHeader(context);
					out.displayWelcome();
					if (core.exceedCashMaxLimit()) {
						core.setWarning("请将现金入库");
						state.setState(PosState.MAXCASH);
					} else {
						state.setState(PosState.PRESALE);
					}
				}
			} else {
				context.setWarning("无效操作,按清除键继续!");
				state.setState(PosState.MAXCASH);
			}
		} catch (IOException e) {
			state.setState(PosState.ERROR);
		}
	}

	/**
	 * 预订单流程
	 */
	private void futruesell() {
		Sale s;
		out.display(context);
		out.displayState("预订单");
		out.prompt("请输入金额结算，或按功能键");

		PosInput input = in.getInput();
		// 获取功能键值
		switch (input.key()) {
		case PosFunction.CANCEL:
			JOptionPane.showMessageDialog(null, "在预订单状态下不支持，请按确定");
			state.setState(PosState.FUTRUESELL);
			break;
		// 改变预订单价格
		case PosFunction.ALTPRICE:
			orderMoney();
			break;
		case PosFunction.OPENCASHBOX:
			context.setWarning("非空单状态下不能打开钱箱，按清除键继续!");
			state.setState(PosState.ERROR);
			break;
		case PosFunction.QUICKCORRECT:
			try {
				s = core.quick_correct();
				if (s == null) {
					state.setState(PosState.ERROR);
				} else {
					sale_rec = s;
					state.setState(PosState.SALE);
					out.displayUnhold(s, core.getValue());
					out.displayDiscount(s, core.getValue());
					out.display(core.getValue(), context.getExchange());
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (RealTimeException ex) {
				state.setState(PosState.NETWORKERROR);
			}
			break;

		case PosFunction.DELETE:
			if (core.getPosSheet().getPayLen() > 0) {
				context.setWarning("支付后不允许整单取消,按清除键继续!");
				// 荣华项目预销售，增加部份
				if (state.getState() == PosState.FUTRUESELL) {
					in.waitCancel(context.getWarning());
					context.setWarning("");
					state.setState(PosState.FUTRUESELL);
				}
				// -------------------------------
			} else if (confirm(PosFunction.toString(input.key()) + "?"))
				try {
					if (checkPrivilege(input.key())) {
						try {
							deleteOrder();
						} catch (Exception e) {
							// 荣华项目预销售，增加部份
							context.setWarning(e.getMessage());
							in.waitCancel(context.getWarning());
							context.setWarning("");
							state.setState(PosState.FUTRUESELL);
							// ----------
						}
						cashier.resetPrivilege();
						context.setAuthorizerid("");
					}
					// 荣华项目预销售，增加部份
					else {
						in.waitCancel(context.getWarning());
						context.setWarning("");
						state.setState(PosState.FUTRUESELL);
					}
					// -------------------------
				} catch (UserCancelException e4) {
					// do nothing
				}

			break;

		case PosFunction.HKD:
			if (context.getCurrenCode().equals("HKD"))
				core.setCurrency("RMB");
			else
				core.setCurrency("HKD");

			out.display(core.getValue(), context.getExchange());
			break;

		case PosFunction.LOCK:
			state.setState(PosState.LOCK);
			break;

		case PosFunction.HOLD:
			System.out.println("挂单 ....");
			hold();
			break;
		case PosFunction.PAYMENT:
			// 定义商品类
			sale_rec = null;
			PosInputPayment p = (PosInputPayment) input;
			inputpayment = p; //把支付信息传递给全局变量，供重打小票使用

			Payment pay_info;
			Payment cardPay = null;
			try {
				//得到当前单会员卡信息
				if (core.getPosSheet().getMemberCard() != null) {
					PosInputPayment cp = new PosInputPayment(0, 'C', core
							.getPosSheet().getMemberCard().getCardNo());
					cardPay = core.pay(cp);
				}
				pay_info = core.pay(p);

				if (pay_info == null) {
					state.setState(PosState.ERROR);
				} else {

					// 桌面上显示
					out.display(core.getValue(), context.getExchange());

					out.display(pay_info, core.getValue());
					//------------
					if (core.getPosSheet().getShopCard() != null)
						out.display(core.getPosSheet().getShopCard());
					//  out.
					//打印支付信息：支付类型和金额
					out.display(core.getPosSheet().getMemberCard()); //打印会员卡信息：卡号和余额
					if (core.getValue().getValueToPay() <= 0) {
						//state.setState(PosState.CLOSESHEET);
						commitSheets(inputpayment);
						String printtype = PosContext.getInstance()
								.getPrintType();
						if (printtype != null || !printtype.equals("")) {
							if (printtype.compareToIgnoreCase("INVOICE") == 0) {
								printInvoice();
							}
						}
						state.setState(PosState.PRESALE);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RealTimeException e) {
				state.setState(PosState.NETWORKERROR);
			}

			break;

		case PosFunction.EXIT:
			context.setWarning("请先结算再退出,按清除键继续!");
			state.setState(PosState.ERROR);
			break;
		default:
			//            	JOptionPane.showMessageDialog(null, "在预订单状态下不支持，请按确定");
			state.setState(PosState.FUTRUESELL);
			;
		}
	}

	/**
	 * 提货流程
	 */
	private void bringforward() {
		int ion = bringForward();
		if (ion == -1) {
			state.setState(PosState.SALE);

		} else {
			if (ion > core.getValue().getValueToPay()) {
				if (ion > core.getValue().getValueTotal()) {
					ion = core.getValue().getValueTotal()
							- core.getValue().getValuePaid();
				}
			}

			// 把结算的价格与结算的方式传给PosInputPayment
			PosInputPayment up = new PosInputPayment(ion, Payment.BRINGFORWARD);
			// PosInputPayment p
			inputpayment = up; //把支付信息传递给全局变量，供重打小票使用

			Payment pay_info;
			Payment cardPay = null;
			try {
				//得到当前单会员卡信息
				if (core.getPosSheet().getMemberCard() != null) {
					PosInputPayment cp = new PosInputPayment(0, 'C', core
							.getPosSheet().getMemberCard().getCardNo());
					cardPay = core.pay(cp);
				}
				pay_info = core.pay(up);

				if (pay_info == null) {
					state.setState(PosState.ERROR);
				} else {

					//        	PosDevOut.getInstance().displayHeaed(context);

					// 桌面上显示
					out.display(core.getValue(), context.getExchange());

					out.display(pay_info, core.getValue());
					//------------
					if (core.getPosSheet().getShopCard() != null)
						out.display(core.getPosSheet().getShopCard());
					//out.
					//打印支付信息：支付类型和金额
					out.display(core.getPosSheet().getMemberCard()); //打印会员卡信息：卡号和余额
					if (core.getValue().getValueToPay() <= 0) {
						state.setState(PosState.CLOSESHEET);
					} else {
						state.setState(PosState.SALE);
					}

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RealTimeException e) {
				state.setState(PosState.NETWORKERROR);
			}

		}
	}

	// 遍历寻找商品获得商品对应卷的关系 获取一个返回队列要判断它是否为空
	private CouponTypeInfoList largVgno(String largessType) throws IOException {
		HashSet set = new HashSet();
		CouponTypeInfoList couponTypeInfoList = new CouponTypeInfoList();
		// 遍历寻找商品
		for (int i = 0; i < core.getSaleLen(); i++) {
			if (core.getSale(i).getDiscType() == Discount.NONE
					|| core.getSale(i).getDiscType() == Discount.VIPPROMPROM
					|| core.getSale(i).getDiscType() == Discount.VIPPROM) {
				if (!core.getSale(i).getGoods().getVgno().equals("000000")){
					set.add(core.getSale(i).getGoods().getVgno());
					}
			}
		}
		// 队列中承在执行的方法
		if (set.size() > 0) {
			// 返回一个包含所有卷对应信息的队列
			couponTypeInfoList = vgnoTOtome(set,largessType);
			// 移除所有空的卷的对应信息。应对关系是在Excetpon信息不存在，说明是有效的对应信息
			if (couponTypeInfoList != null) {
				int Count = couponTypeInfoList.size();
				for (int i = 0; i < Count; i++) {
					couponTypeInfoList.rmoveNull();
				}
			}
			// 再次遍历商品计算商品兑现的卷内信息
			for (int i = 0; i < core.getSaleLen(); i++) {
				if (core.getSale(i).getDiscType() == Discount.NONE
						|| core.getSale(i).getDiscType() == Discount.VIPPROMPROM
						|| core.getSale(i).getDiscType() == Discount.VIPPROM) {
					if (!core.getSale(i).getGoods().getVgno().equals("000000")){
					couponTypeInfoList.add(
							core.getSale(i).getGoods().getVgno(), core.getSale(
									i).getQty());
					}
				}
			}
			return couponTypeInfoList;
		}
		return couponTypeInfoList;
	}

	private void largess(String largessType) throws IOException, CouponException {
		int value = 0;
		int coupon = 0;
		int coupon_list = 0; // 商品打折，还剩可以打折，要用实体卷继续打折，当实体卷当折时候要减去它 
		int coupon_count = 0; //卷与商品打完折后，剩余商品数与卷继续打折
		// 获取类的信息
		CouponTypeInfoList couponTypeInfoList = largVgno(largessType);
		int loupou = 0;
		for (int i = 0; i < core.getSaleLen(); i++) {
			if (core.getSale(i).getGoods().getVgno().equals("000000")
					&& core.getSale(i).getGoods().getPType().equals("n")) {
				loupou = 0;
				String largess = core.getSale(i).getGoods().getBarcode();
				for (int n = 0; n < core.getSaleLen(); n++) {
					if (core.getSale(n).getGoods().getVgno().equals("000000")
							&& core.getSale(n).getGoods().getPType()
									.equals("n")
							&& core.getSale(n).getGoods().getBarcode()
									.subSequence(0, 4).equals(
											largess.subSequence(0, 4))) {
						loupou++;
					}
				}
				// 转入一个卷的类型，获得卷的张数
				if (couponTypeInfoList.size() > 0) {
					coupon = couponTypeInfoList.getCouponCounnt(largess.substring(0, 4));
				}
				CouponLargess couponLargess = getLaressInfo(largess.substring(
						0, 4), String.valueOf(loupou + coupon), context
						.getStoreid() , largessType);
				// 处理20061129 数量段问题 判断问题
				if (couponLargess.getFlag()) {
					// 为真\是数量段问题 处理
					if (couponLargess != null){
						if (core.couponLargessList.add(couponLargess)){
							if (couponLargess.getDiscontNo() >= coupon){
								coupon_list = couponLargess.getDiscontNo() - coupon;
								if (coupon_list == 0){
										value += performDiscountCoupon(largess.substring(0, 4),
												couponLargess, couponTypeInfoList, couponLargess.getDiscontNo(),
												false, largessType,true);
									} else {
										value += performDiscountCoupon(largess.substring(0, 4),
												couponLargess, couponTypeInfoList, coupon,
												false, largessType,true);
										}
								} else {
									coupon_list = coupon - couponLargess.getDiscontNo();
									if (coupon_list > 0){
										CouponLargess couponLargessA = getLaressInfo(largess.substring(
												0, 4), String.valueOf(coupon_list), context
												.getStoreid() , largessType);
										if (couponLargessA != null && core.couponLargessList.add(couponLargessA)){
											value += performDiscountCoupon(largess.substring(0, 4),
													couponLargess, couponTypeInfoList, couponLargess.getDiscontNo(),
													false, largessType,false);
											value += performDiscountCoupon(largess.substring(0, 4),
													couponLargessA, couponTypeInfoList, coupon_list,
													false, largessType,true);
											} else {
												value += performDiscountCoupon(largess.substring(0, 4),
														couponLargess, couponTypeInfoList, couponLargess.getDiscontNo(),
														false, largessType,true);
												}
										}
									}
							}
						}
					} else {
				if (couponLargess != null) {
					// 此方法改进，送相同卷类型合并 待验证
					if (core.couponLargessList.add(couponLargess)) {
						value += performDiscountCoupon(largess.substring(0, 4),
								couponLargess, couponTypeInfoList, coupon,
								false, largessType,true);
					}
				}
					} // 结尾段
			}
		}
		if (couponTypeInfoList.size() > 0) {
			String coupons;
			for (int i = 0; i < couponTypeInfoList.size(); i++) {
				if (!(coupons = couponTypeInfoList.getCouponType(i)).equals("")) {
					if (!couponTypeInfoList.getDiscount(i)){
					int couponCount = couponTypeInfoList
							.getCouponCounnt(coupons);
					CouponLargess couponLargess = getLaressInfo(coupons, String
							.valueOf(couponCount), context.getStoreid(), largessType);
					if (couponLargess.getFlag()){
						// 为真\是数量段问题 处理
						if (couponLargess != null){
							if (core.couponLargessList.add(couponLargess)){
								if (couponLargess.getDiscontNo() >= couponCount){ 
									coupon_list = couponLargess.getDiscontNo() - couponCount;
									if (coupon_list == 0){
										value += performDiscountCoupon(coupons,
												couponLargess, couponTypeInfoList, couponLargess.getDiscontNo(),
												true, largessType,true);
										} else {
											value += performDiscountCoupon(coupons,
													couponLargess, couponTypeInfoList, couponCount,
													true, largessType,true);
											}
									} else {
										coupon_count = couponCount - couponLargess.getDiscontNo();
										if (coupon_count > 0){
											CouponLargess couponLargessA = getLaressInfo(coupons, String
													.valueOf(coupon_count), context.getStoreid(), largessType);
										if (couponLargessA != null && core.couponLargessList.add(couponLargessA)){
										value += performDiscountCoupon(coupons,
													couponLargess, couponTypeInfoList, couponLargess.getDiscontNo(),
													true, largessType,false);
										value += performDiscountCoupon(coupons,
												couponLargessA, couponTypeInfoList, coupon_count,
												true, largessType,true);
											} else {
												value += performDiscountCoupon(coupons,
														couponLargess, couponTypeInfoList, couponLargess.getDiscontNo(),
														true, largessType,true);
												}
										}
									}
								}
							}
					}else{
					if (couponLargess != null) {
//						 此方法改进，送相同卷类型合并 待验证
						if (core.couponLargessList.add(couponLargess)) {
							// TURE值表明不用计算实际卷
							value += performDiscountCoupon(coupons,
									couponLargess, couponTypeInfoList,
									couponCount, true,largessType,true); 
						}
					}
					
				  }
					
				}
			}
		}
			couponTypeInfoList.removeAll();
		}
		if (core.couponLargessList.size() > 0) {
			
			if (largessType.equals("0")){
				char largess = 'A';
			core.couponLargessList.setLargessType(largess);}
			else{
				char largess = 'B';
				core.couponLargessList.setLargessType(largess);
				}

			String name = "促销商品折扣";
			// 赠送的时候折扣类型是A
			if (largessType.equals("0")){
				core.sell(Sale.AlTPRICE, name, value);
				}
			// 碰销的时候折扣类型是B
			if (largessType.equals("1")){
				core.sell(Sale.AlTPRICE, name, value);
				}

			SheetValue tmpSheetValue = new SheetValue();
			
			tmpSheetValue.setValue(value, 0, 0, 0);
			out.display(core.getFalseSale(core.getFalseSaleLen() - 1),
					tmpSheetValue);
			out.display(core.getValue(), context.getExchange());
			core.dump();
			// 将存放赠送商的对象拟定XML文件存放在当地largessinfo目录中
			core.performCouponLargessList(core.couponLargessList);
			context.setCounp(true);
		}else{
			context.setWarning("没有促销的商品，按清除继续");
			state.setState(PosState.ERROR);
			}
	}

	// 返回商品列表中商品中对应卷信息
	private CouponTypeInfoList vgnoTOtome(HashSet set , String largessType) throws IOException {

		ClerkAdm ad = new ClerkAdm(context.getServerip(), context.getPort());
		CouponTypeInfoList response = ad.getVgnoCouponType(set , largessType);

		return response;

	}

	// 获取促销卷的折扣信息
	private CouponLargess getLaressInfo(String Largess, String count,
			String sheepID , String largessType) throws IOException, CouponException {
		CouponMgr couponMgr = null;
		try {
			couponMgr = new CouponMgrImpl();
		} catch (Exception e) {
			context.setWarning("券库设置错误,按清除键继续!");
			return null;
		}
		
		CouponLargess queryVO = null;

		try {
			queryVO = couponMgr.getCouponInfo(Largess, count, sheepID, largessType);
		} catch (IOException ex) {
			
//			if (!ex.getMessage().equals("没有此类型的促销")) {
				throw ex;

			}

//		}
		
		if (queryVO == null){
			queryVO.setExceptionInfo("没有此类型的促销");
			return queryVO;
			}
		
		if (largessType.equals("1")){
			if (queryVO.getAddPrice() <= 0){
				queryVO.setExceptionInfo("没有此类型的促销");
				return queryVO;
				}
			}

		return queryVO;
	}
	
	
	// largess 卷类型
	// couponLargess 对应卷内型信息
	// couponTypeInfoList 商品对应卷类型列表
	// couponCount 商品能兑换的卷张数
	// flag不移除标志位
	private int performDiscountCoupon(String largess,CouponLargess couponLargess, CouponTypeInfoList couponTypeInfoList,int couponCount, boolean Type, String largessType, boolean flag) {
		int Coun = 0;
		salelst = core.getPosSheet().getSalelst();
		int totalValue = salelst.getTotalValue();
		Vector vnums = salelst.getNumsWithoutDisc();
		// 打折金额
		int value = 0;
		// 能够打折的商品数量
		int VgnoCount = 0;
		// 一张卷的商品数量
		int CouponVgnoCount = 1;
		
		if (couponLargess.getExceptionInfo() == null
				|| couponLargess.getExceptionInfo().equals("")) {

//			int disc = 0;
			
			long disc = 0;

			if (couponLargess.getDiscount() != null) {
//				disc = (int) Math.rint(couponLargess.getDiscount()
//						.doubleValue());
				disc = (long)Math.rint(couponLargess.getDiscount().doubleValue() * 100);

			} else {

				return 0;
			}

			if (salelst != null) {

				try {
					Sale sale = null;

					if (!Type) {
						for (int i = 0; i < vnums.size(); i++) {
							int num = ((Integer) vnums.elementAt(i)).intValue();
							// 使用折扣价
//							DiscRate discrate;
							DiscPrice discPrice;
							// 赠送为0
							if (largessType.equals("0")){
								
//							 discrate = new DiscRate(Discount.LARGESS,
//									100 - (int) disc);
							discPrice  =  new DiscPrice(Discount.LARGESS,
							            disc);
							}
							else{
//								 discrate = new DiscRate(Discount.LARGESSL,
//										100 - (int) disc);
								discPrice  =  new DiscPrice(Discount.LARGESSL,
							            disc);
										}
							
							sale = salelst.get(num);
							if (sale.getGoods().getVgno().equals("000000")
									&& sale.getGoods().getPType().equals("n")
									&& sale.getGoods().getBarcode().substring(
											0, 4).equals(largess)) {
								// 20060808 END
//								sale.setDiscount(discrate);
								sale.setDiscount(discPrice);
							}
						}
					}
					if (couponCount != 0) {
						VgnoCount = couponTypeInfoList.getCouponCounnt(largess,
								couponCount);
						if (VgnoCount != 0) {
							
							CouponVgnoCount = couponTypeInfoList.getVgnoCount(largess);
							
							// zhouzhou 20060811 老婆卷一卷对多个商品的金额折让
							long onePrice = (long)Math.rint(disc / CouponVgnoCount);
							long EndPrice = (long)disc - (onePrice * (CouponVgnoCount-1));
							// zhouzhou 20060811 END
							
							// if
							// (couponTypeInfoList.YNGRR(largess,sale.getGoods().getVgno())){
							for (int A = 0; A < vnums.size(); A++) {
								int numa = ((Integer) vnums.elementAt(A))
										.intValue();
//								DiscRate discrates;
								
								DiscPrice discPrices;
								
								// 赠送为0
								if (largessType.equals("0")){
									
//								 discrates = new DiscRate(Discount.LARGESS,
//										100 - (int) disc);
								 
//								 zhouzhou 20060811 老婆卷一卷对多个商品的金额折让
//								 discPrices = new DiscPrice(Discount.LARGESS, disc);
								 discPrices = new DiscPrice(Discount.LARGESS,onePrice);
								}
								
								else{
//									 discrates = new DiscRate(Discount.LARGESSL,
//											100 - (int) disc);
//									 zhouzhou 20060811 老婆卷一卷对多个商品的金额折让	
//									 discPrices = new DiscPrice(Discount.LARGESSL, disc);
									 discPrices = new DiscPrice(Discount.LARGESSL,onePrice);
											}
								Sale sales = null;
								sales = salelst.get(numa);
								if (couponTypeInfoList.YNGRR(largess, sales
										.getGoods().getVgno())) {
									if (core.getSale(A).getDiscType() == Discount.NONE
											|| core.getSale(A).getDiscType() == Discount.VIPPROMPROM
											|| core.getSale(A).getDiscType() == Discount.VIPPROM
											|| (core.getSale(A).getDiscType() == Discount.LARGESS 
											&& core.getSale(A).getQtyDisc() != core.getSale(A).getQty())) {

										int VgnoCountOne = sales.getQty();
										
										if ((VgnoCount >= VgnoCountOne)) {
											// 20060815 支持一卷对多种商品 如果一卷一件，无需代入差价
											if (onePrice == EndPrice){
										//	sales.setDiscount(discrates);
											sales.setDiscount(discPrices,0);
											}else {
												sales.setDiscount(discPrices,(EndPrice - onePrice));
												}
											
											VgnoCount = VgnoCount
													- VgnoCountOne;
											
										} else if (VgnoCount > 0) {	
											
											if(onePrice == EndPrice){
												sales.setDiscountVgno(discPrices,VgnoCount,0);
												} else {
													sales.setDiscountVgno(discPrices,VgnoCount,(EndPrice - onePrice));
													}
//											sales.setDiscountVgno(discrates,
//													VgnoCount);
//											 zhouzhou 支持卷对多个商品分配金额，引入最后一件商品折扣价
										}
									}
								}
							}
						}
						if (flag){
						couponTypeInfoList.removeCouponType(largess);
						}
					}
					if (flag){
					couponTypeInfoList.removeCouponType(largess);
					}

					core.updateValue();
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
					/* add by lichao 8/24/2004 */
					try {

						
						value = (int) core.getValue().getDiscDelta();
						//SheetValue v = core.getValue();

						//                    int value = (int)core.getValue().getDiscDelta();
						//                    String name = "总额折扣";
						//                    core.sell(Sale.AlTPRICE, name, value);
						//                    
						//                    SheetValue tmpSheetValue=new SheetValue();
						//                    tmpSheetValue.setValue(value,0,0,0);

						//                   out.display(core.getFalseSale(core.getFalseSaleLen()-1),tmpSheetValue);
						//                    out.display(core.getValue(), context.getExchange());

						core.dump();
						return value;

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				/* end */
				//荣华项目预销售，增加部份
				if (state.getState() == PosState.FUTRUESELL) {
					return 0;
				}
				// ------------------------
				state.setState(PosState.SALE);

				// 荣华项目预销售，增加部份
				if (state.getState() == PosState.FUTRUESELL) {
					in.waitCancel(context.getWarning());
					context.setWarning("");
					return 0;
				}
			}
		}
		return 0;
	}

	/**
	 * @deprecated
	 *  
	 */
	private void find() {
		Sale s;
		out.display(context);
		out.displayState("查询");
		out.prompt("请输入要查询商品数量及条码，或按功能键。");

		PosInput input = in.getInput();
		switch (input.key()) {
		case PosFunction.CANCEL:
			// do nothing.
			break;

		case PosFunction.DISCOUNT:
			findpricesingleDisc();

			break;

		case PosFunction.DISCTOTAL:
			findpricetotalDisc();

			break;

		case PosFunction.DISCMONEY:
			findpricemoneyDisc();

			break;

		case PosFunction.GOODS:
			FindGoodsPrice((PosInputGoods) input);
			break;

		case PosFunction.HKD:
			if (context.getCurrenCode().equals("HKD"))
				core.setCurrency("RMB");
			else
				core.setCurrency("HKD");

			out.display(core.getValue(), context.getExchange());
			break;

		case PosFunction.TOTAL:
			showTotal();
			break;

		case PosFunction.EXIT:
			deletefindresult();
			break;
		default:
			;
		}
	}

	private void sale() {
		Sale s;
		out.display(context);
		if (context.getGrouplargess()) {
			out.displayState("机团销售");
		} else if(context.getLargessCoupon() == 1){
			out.displayState("赠券");
		} else {
			out.displayState("销售");
		}
		out.prompt("请输入商品数量及条码，或按功能键。");

		PosInput input = in.getInput();
		
//		NotifyChangePriceConsumer.getInstance().consume();
		switch (input.key()) {

		case PosFunction.CANCEL:
			break;
		case PosFunction.GROUPLARGESS:
			//context.setGrouplargess(true);
			break;
		case PosFunction.ALTPRICE:
			//增加变价的控制权限
			//				try {
			//					if (checkPrivilege(PosFunction.ALTPRICE))
			state.setState(PosState.ALTPRICE);
			//					else
			//						state.setState(PosState.ERROR);
			//				} catch (UserCancelException e3) {
			//					// do nothing
			//				}
			break;
		case PosFunction.BRINGFORWARD:
			if (context.isOnLine()) {
				bringforward();
			} else {
				confirm("不能在脱机状态下使用提货卡");
			}
			break;
			
		// 赠券 ....  荣华促销业务， 买两种以上不同的商品，免费获取一个商品或券！
		case PosFunction.LARGESSCOUPON:
			if(context.getLargessCoupon() == 0){
				if(confirm(PosFunction.toString(input.key())+"?")){
					context.setLargessCoupon(1);
					break;
					}else{
					context.setLargessCoupon(0);
					break;
						}
				}
			else{
				if(confirm("是否取消赠券?")){
					context.setLargessCoupon(0);
					break;
				}else{
					context.setLargessCoupon(1);
					break;
					}
				}

			// 处理赠送代码
		case PosFunction.LARGESS:
			if (confirm(PosFunction.toString(input.key()) + "?")) {
				if (context.getCounp()) {
					confirm("对不起，不能再同一笔单中按赠送");
					break;
				}
				if (!context.getGrouplargess()) {
					try {
						// LargessType 类型为0 是赠送  1 是为碰销
						String largessType = "0";
						largess(largessType);
					} catch (IOException ex) {
						context.setWarning("赠送时发生网络故障，按清除键继续!");
						state.setState(PosState.ERROR);
					} catch (CouponException e) {
						e.printStackTrace();
						context.setWarning(e.getMessage());
						state.setState(PosState.ERROR);
					}
				}
			}
			break;
			
			// 处理碰销代码
		case PosFunction.LARGESSL:
			if (confirm(PosFunction.toString(input.key()) + "?")){
				if (context.getCounp()){
					confirm("对不起，不能在再一笔单中按碰销");
					break;
					}
				if (!context.getGrouplargess()){
					try{
						//LargessType 类型为0 是赠送  1 是为碰销
						String largessType = "1";
						largess(largessType);
						}catch(IOException ex){
							context.setWarning("碰销时发生网络故障，按清除键继续");
							state.setState(PosState.ERROR);
							} catch(CouponException e){
								e.printStackTrace();
								context.setWarning(e.getMessage());
								state.setState(PosState.ERROR);
								}
					}
		}
		break;
		
		case PosFunction.DISCOUNT:
			//增加单项折扣的控制权限
			//                try {
			//                    if (checkPrivilege(PosFunction.DISCOUNT))
			//                        state.setState(PosState.DISCOUNT);
			//                    else
			//                        state.setState(PosState.ERROR);
			//                } catch (UserCancelException e3) {
			//                    // do nothing
			//                }
			state.setState(PosState.DISCOUNT);
			break;
			
		case PosFunction.DISCTOTAL:
			//增加总额折扣的控制权限
			//                try {
			//                    if (checkPrivilege(PosFunction.DISCTOTAL))
			//                        state.setState(PosState.DISCTOTAL);
			//                    else
			//                        state.setState(PosState.ERROR);
			//                } catch (UserCancelException e3) {
			//                    // do nothing
			//                }
			state.setState(PosState.DISCTOTAL);
			break;
		case PosFunction.DISCMONEY:
			//增加金额折扣的控制权限
			//                try {
			//                    if (checkPrivilege(PosFunction.DISCMONEY))
			//                        state.setState(PosState.DISCMONEY);
			//                    else
			//                        state.setState(PosState.ERROR);
			//                } catch (UserCancelException e3) {
			//                    // do nothing
			//                }
			state.setState(PosState.DISCMONEY);
			break;
		case PosFunction.OPENCASHBOX:
			context.setWarning("非空单状态下不能打开钱箱，按清除键继续!");
			state.setState(PosState.ERROR);
			break;
		case PosFunction.QUICKCORRECT:
			if (!context.getCounp()){
			try {
				s = core.quick_correct();
				if (s == null) {
					state.setState(PosState.ERROR);
				} else {
					sale_rec = s;
					state.setState(PosState.SALE);
					out.displayUnhold(s, core.getValue());
					out.displayDiscount(s, core.getValue());
					out.display(core.getValue(), context.getExchange());
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (RealTimeException ex) {
				state.setState(PosState.NETWORKERROR);
			}
			}	else{
				context.setWarning("赠送时不能即更,请按清除键继续！");
				state.setState(PosState.ERROR);
				}

			break;
			
		case PosFunction.CARDCHANGE:
			if(core.getPosSheet().getMemberCard() != null) {
				if (core.getPosSheet().getCardChange() == null){
			// 处理积分兑换
			if (!context.getCounp()){
				MemberCardChange change = new MemberCardChange();
				String vgno = null;
				//计算之前待支付金额
				String vaiter = String.valueOf(core.getPosSheet().getValue().getValueToPay() / 100.0);
				//最后参数代表，查询是否积分足够
				CardChange cardchange = change.readLoanCardNum(core.getPosSheet().getMemberCard().getCardNo()
						,vaiter,core.getPosContext().getStoreid(),core.getPosContext().getPosid(),
						String.valueOf(core.getPosContext().getSheetid()),"0");
				if( cardchange != null ){
					pos.core.getPosSheet().setCardChange(cardchange);
					salelst = core.getPosSheet().getSalelst();
					Vector vnums = salelst.getNumsWithoutDisc();
					Sale sale = null;
					long value = 0;
					
					// int tempValue=0;
					for (int i = 0; i < vnums.size(); i++) {
						int num = ((Integer) vnums.elementAt(i)).intValue();
						sale = salelst.get(num);
						value += sale.getFactValue();
						
						// 如果成功，生成一条打折信息，打折信息是零
						sale.setFactPrice(Discount.Change, (int) Math
							.rint(100 * 0));
					}
					core.updateValue();
//					out.displayDiscount(sale_rec, core.getValue());
//					out.display(core.getValue(), context.getExchange());
					cashier.resetPrivilege();
					// 收银员
					context.setAuthorizerid(core.getPosContext().getCashierid());
					authorizeCashier = null;
					try {
						String name = "兑换";
						core.sell(Sale.Change, name, value);
						out.displayDiscount(sale_rec, core.getValue());
						out.display(core.getValue(), context.getExchange());
						core.dump();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					state.setState(PosState.SALE);
					} else{
						context.setWarning(change.getExceptionInfo());
						state.setState(PosState.ERROR);
					}
				}else{
					context.setWarning("赠送时不能使用积分兑换，请按清除键继续!");
					state.setState(PosState.ERROR);
					}
			}else{
				context.setWarning("已经兑换一次，请按清除键继续!");
				state.setState(PosState.ERROR);
				}
			}else{
				context.setWarning("未先刷会员卡，请按清除键继续!");
				state.setState(PosState.ERROR);
				}
			break;
			
		case PosFunction.CORRECT:
			if(!context.getCounp()){
			try {
				// 增加更正的权限控制
				if (checkPrivilege(PosFunction.CORRECT)) {
					s = core.correct();
					if (s == null) {
						state.setState(PosState.ERROR);
					} else {
						sale_rec = s;
						state.setState(PosState.SALE);
						out.displayUnhold(s, core.getValue());
						out.displayDiscount(s, core.getValue());
						out.display(core.getValue(), context.getExchange());
					}
				} else {
					context.setWarning("没有更正的权限,请按清除键继续！");
					state.setState(PosState.ERROR);
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (RealTimeException ex) {
				state.setState(PosState.NETWORKERROR);
			} catch (UserCancelException e3) {
			}
			}
			else{
				context.setWarning("赠送时不能更正,请按清除键继续！");
				state.setState(PosState.ERROR);
				}
			
			break;

		case PosFunction.WITHDRAW:
			try {
				if (checkPrivilege(PosFunction.WITHDRAW))
					state.setState(PosState.WITHDRAW);
				else
					state.setState(PosState.ERROR);
			} catch (UserCancelException e3) {
				// do nothing
			}

			break;

		case PosFunction.DELETE:
			if (core.getPosSheet().getPayLen() > 0) {
				context.setWarning("支付后不允许整单取消,按清除键继续!");
				state.setState(PosState.ERROR);
			} else if (confirm(PosFunction.toString(input.key()) + "?"))
				try {
					if (checkPrivilege(input.key())) {
						try {
							deleteSheet();
							
							String filename = "largessinfo" + File.separator+ "2006RH.xml";
							File file = new File(filename);
							file.delete();
							core.couponLargessList.remove();
							context.setGrouplargess(false);
							context.setCounp(false);
							context.setGrouplargessT(false);
							context.setLargessCoupon(0);
						} catch (Exception e) {
							context.setWarning(e.getMessage());
							state.setState(PosState.ERROR);
						}
						cashier.resetPrivilege();
						context.setAuthorizerid("");
					}
				} catch (UserCancelException e4) {
				}

			break;

		case PosFunction.GOODS:
			try {
				if(core.getPosSheet().getCardChange() == null){
				sellGoods((PosInputGoods) input);
				}else{
					context.setWarning("会员卡积分兑换过，不能销售商品！");
					state.setState(PosState.ERROR);
					}
			} catch (CouponException e2) {
				e2.printStackTrace();
				context.setWarning(e2.getMessage());
				state.setState(PosState.ERROR);
			}
			break;

		case PosFunction.HKD:
			if (context.getCurrenCode().equals("HKD"))
				core.setCurrency("RMB");
			else
				core.setCurrency("HKD");

			out.display(core.getValue(), context.getExchange());
			break;

		case PosFunction.LOCK:
			state.setState(PosState.LOCK);
			//UnLock unLock = new UnLock();
			//unLock.show();
			break;

		case PosFunction.HOLD:
			System.out.println("挂单 ...");
		if(!context.getCounp()){
			hold();
			}else{
				context.setWarning("赠送时不能挂单,请按清除键继续！");
				state.setState(PosState.ERROR);
				}
			break;
			
		
		case PosFunction.PAYMENT:
			if (getPayt()){
				if (getPay()){
			// 定义商品类
			sale_rec = null;
			PosInputPayment p = (PosInputPayment) input;
			inputpayment = p; //把支付信息传递给全局变量，供重打小票使用

			Payment pay_info;
			Payment cardPay = null;
			Payment plan=null;
			try {
				//得到当前单会员卡信息
//				if (core.getPosSheet().getMemberCard() != null) {
//					PosInputPayment cp = new PosInputPayment(0, 'C', core
//							.getPosSheet().getMemberCard().getCardNo());
//					cardPay = core.pay(cp);
//				}
				pay_info = core.pay(p); 

				if (pay_info == null) {
					state.setState(PosState.ERROR);
				} else {
					// 桌面上显示
					out.display(core.getValue(), context.getExchange());

					out.display(pay_info, core.getValue());
					if (core.getPosSheet().getShopCard() != null)
						out.display(core.getPosSheet().getShopCard());
					if (core.getValue().getValueToPay() <= 0) {

						//处理会员积分问题
						try {
								// 有券兑换的时候，不能积分。
							if (core.getPosSheet().getMemberCard() != null && core.getPosSheet().getCardChange() == null)
							{
								long values=0;
								values = (long)core.getPaylst().getCouponPay();
								//获取券支付信息
								core.getPosSheet().handlePoint(values);
							}
						} catch (RealTimeException e) {
							PosContext.getInstance().setWarning("会员服务器失败，按清除键继续尝试或取消!");
							state.setState(PosState.ERROR);
							return;
						} catch (IOException e) {
							PosContext.getInstance().setWarning("会员更新数据失败，按清除键继续尝试或取消!");
							state.setState(PosState.ERROR);
							return;
						}
						//打印支付信息：支付类型和金额
						out.display(core.getPosSheet().getMemberCard()); //打印会员卡信息：卡号和余额
			
						state.setState(PosState.CLOSESHEET);
						//得到当前单会员卡信息`计算信息放在结算后 
						if (core.getPosSheet().getMemberCard() != null) {
							PosInputPayment cp = new PosInputPayment(0, 'C', core
									.getPosSheet().getMemberCard().getCardNo());
							cardPay = core.pay(cp);
						}

						if (core.getPosSheet().getCardChange() != null){
							MemberCardMgr memberCardMgr = null;
							memberCardMgr = MemberCardMgrFactory.createInstance();
							CardChange result = null;
							core.getPosSheet().getCardChange().setFlag("1");
						    result= memberCardMgr.updateCardChange(core.getPosSheet().getCardChange());
						    if (result == null){
								context.setWarning("积分兑换出现异常,按清除键继续!");
								state.setState(PosState.ERROR);
								return;
							}else{
							    core.getPosSheet().getCardChange().setCardPoint(result.getCardPoint());
							    core.dump();
								out.Plandisplay(core.getPosSheet().getCardChange().getPlan(),core.getPosSheet().getCardChange().getCardPoint());
								}
						}
						
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RealTimeException e) {
				state.setState(PosState.NETWORKERROR);
				} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			}
			else{
					context.setWarning("还有未兑现完的卷,按清除键继续!");
					state.setState(PosState.SALE);
			}
				}else 
				{
					context.setWarning("还有未赠送完毕的商品,按清除键继续!");
				state.setState(PosState.SALE);
					}
			break;
		case PosFunction.Coupon:
			try {
				encashCoupon();
			} catch (IOException ex) {
				context.setWarning("兑现时发生网络故障,按清除键继续!");
				state.setState(PosState.ERROR);
			} catch (CouponException e) {
				e.printStackTrace();
				context.setWarning(e.getMessage());
				state.setState(PosState.ERROR);
			}
			break;
		case PosFunction.PROPERTY:
			//如果是按的属性键
			core.property();
			break;
		case PosFunction.EXIT:
			context.setWarning("请先结算再退出,按清除键继续!");
			state.setState(PosState.ERROR);
			break;
		default:
			;
		}
	}
	
	/*
	 * 打印商品的生产属性
	 */
	public void printProdProperty(PosCore poscore)
	{
		int goods_num =0;
		goods_num = poscore.getSaleLen();

		Sale s = null;
		int prodnum = 0;
		int i,j,m;
		for(i = 0; i < goods_num; ++i)
		{
			s = poscore.getSale(i);
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
			s = poscore.getSale(i);
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

	
	//是否赠送完毕
	private boolean getPayt(){
		if (getAddPrice()){
			if (confirm(("还有未赠送的商品,确定支付" + "?"))){
				return true;
				}else{
			return false;
				}
			}
		return true;
		}
	
	private boolean getPayCoupon(){
		for (int i = 0; i < core.getSaleLen(); i ++){
			if (core.getSale(i).getGoods().getVgno().equals(""))
			{
				return true;
				}
			}
		return false;
		}
	
	// 是否结算信息
	private boolean getPay(){
		if (core.getPosSheet().isCouponEncash){
		if (core.getPosSheet().couponSales.size() > 0){
		int value = core.getPosSheet().getValue().getValueTotal()
			- core.getPosSheet().couponSales.getTotalValue();
		if (value  < 0){
		if (confirm(("还有未兑现完的卷，确定结算" +  "?"))){
			return true;
			}else{
		return false;
			}
		}
		return true;
		}
		return true;
		}
	return true;
	}
	private String getInputNo(int maxlen, String title, String type)
			throws UserCancelException {

		MSRInput msrInput = new MSRInput(title, type);
		//msrInput.type="bank";
		msrInput.show();
		//  msrInput.hasFocus();
		try {
			while (!msrInput.isFinish())
				Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String inputCode = msrInput.getInputcode();

		if (!msrInput.isConfirm()) {
			throw new UserCancelException();
		}

		if (inputCode.length() > maxlen) {
			inputCode = inputCode.substring(0, maxlen - 1);
		}

		return inputCode;
	}

	void sellGoods(PosInputGoods pig) throws CouponException {
		try {

			Goods goods;
			PosInputGoods input_goods = pig;
			if (input_goods.getCode() == "999999") {
				/*
				 * @param Vgno 商品编码 @param Barcode 商品条码 @param GoodsName 商品名称
				 * @param Deptid 商品小类 @param Spec 规格 @param UnitName 销售单位 @param
				 * GoodsPrice 商品售价 @param gtype 商品类型 @param x4Price 价格因子 @param
				 * ptype 折扣/促销类型
				 */
				goods = new Goods("999999", "111111", "预订单商品", "000000", "",
						"", 0, 0, 1, "n");
			} else {
				goods = core.findGoods(pig.getCode());
			}

			Volume init_volume = pig.getVolume();
			int disccount = 0;
			int baseprice = 0;
			PosConfig config = PosConfig.getInstance();

			/* add by lichao 09/07/2004 */
			if (goods != null && goods.getType() == 8
					&& goods.getDeptid() != null
					&& !goods.getDeptid().equals(Goods.LOADOMETER)) {
				String BASE_PRICE = (String) PosConfig.getInstance().getString(
						"PRICE_FLAG");
				if (BASE_PRICE.equals("ON")) {

					DispPrice dispPrice = new DispPrice(pos.posOutStream);
					dispPrice.show();
					MainUI oldMainUI = out.getMainUI();
					out.setMainUI(dispPrice);
					dispPrice.show();
					baseprice = in.getbaseprice();
					System.out.println("baseprice为:" + baseprice);
					goods.setPrice(baseprice);
					out.setMainUI(oldMainUI);
					dispPrice.dispose();
					out.clearInputLine();
					if (!dispPrice.getconfirm()) {
						return;
					}
				}
			}

			Sale s;
			if (input_goods != null)
				try {
					s = core.sell(input_goods, baseprice, pos.core
							.getPosSheet().getMemberCard());
					if (s == null) {
						state.setState(PosState.ERROR);
					} else {
						sale_rec = s;
						out.display(s, core.getValue());
						out.display(core.getValue(), context.getExchange());
						/* add by lichao 08/25/2004 */
						int discDelta = pos.core.getPosSheet().getValue()
								.getDiscDelta();

						if (discDelta != 0) {
							try {
								out.displayprom(s, core.getValue());
								int value = (int) discDelta;
								if (s.getDiscType() == Discount.COMPLEX) {
									String name = s.getFavorName();
									core.sell(Sale.AUTODISC, name, value);
								} else {
									String name = new Discount(s.getDiscType())
											.getTypeName();
									core.sell(Sale.AUTODISC, name, value);
								}

								core.dump();
						} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						/* end */
						// 荣华预销售增加对当前状态的断定
						if (state.getState() == PosState.FUTRUESELL) {
							state.setState(PosState.FUTRUESELL);
						}
						// ----------------------------
						else {
							state.setState(PosState.SALE);
						}
					}

				} catch (RealTimeException ex) {
					state.setState(PosState.NETWORKERROR);
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RealTimeException ex) {
			state.setState(PosState.NETWORKERROR);
		}
	}

	void FindGoodsPrice(PosInputGoods pig) {
		try {
			PosInputGoods input_goods = pig;
			Goods goods = core.findGoods(pig.getCode());
			Volume init_volume = pig.getVolume();
			int disccount = 0;

			PosConfig config = PosConfig.getInstance();

			Sale s;
			if (input_goods != null)
				try {
					s = core.findprice(input_goods);
					if (s == null) {
						state.setState(PosState.ERROR);
					} else {
						sale_rec = s;
						out.display(s, core.getValue());
						out.display(core.getValue(), context.getExchange());
						state.setState(PosState.FIND);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		} catch (RealTimeException ex) {
			state.setState(PosState.NETWORKERROR);
		}

	}

	/**
	 * 在权限许可的情况下对当前商品进行变价处理，可以达到对商品折扣的功用
	 *  
	 */
	private void altPrice() {
		// get last sale record;
		// get Fact price;
		// set Fact Price for last goods;
		// compute sheet value;
		// display sheet value;

		//        if (sale_rec != null
		//                && sale_rec.getDiscValue() == 0
		//                && (sale_rec.getType() == Sale.SALE
		//                || sale_rec.getType() == Sale.WITHDRAW)) {
		if (sale_rec != null
				&& (sale_rec.getType() == Sale.SALE || sale_rec.getType() == Sale.WITHDRAW)) {
			try {
				if (checkPrivilege(PosFunction.ALTPRICE)) {
					double price_org = ((double) sale_rec.getGoods().getPrice() / 100.0);
					double price_new;
					out.displayState("改价格");
					out.prompt("请输入商品售价");
					price_new = in.getDouble(6, 2);

					double deltaValue = 0;
					double alterPrice = price_org;

					if (authorizeCashier != null) {
						deltaValue = (price_org
								* authorizeCashier.getMax_Disc() / 100.0);
						alterPrice = price_org
								* (100 - authorizeCashier.getMax_Disc())
								/ 100.0;
					}

					if ((price_org - price_new) > deltaValue) {

						context
								.setWarning("变价超出范围,最低变价到"
										+ (new Value((int) Math
												.rint(alterPrice * 100)))
												.toString() + "，按清除键继续!");

						state.setState(PosState.ERROR);
						cashier.resetPrivilege();
						context.setAuthorizerid("");
						authorizeCashier = null;

						return;
					}
					sale_rec.setFactPrice(Discount.ALTPRICE, (int) Math
							.rint(100 * price_new));
					core.updateValue();
					out.displayDiscount(sale_rec, core.getValue());
					out.display(core.getValue(), context.getExchange());
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
					/* add by lichao 8/24/2004 */
					try {
						//SheetValue v = core.getValue();

						int value = (int) sale_rec.getDiscValue();
						String name = "变价";
						core.sell(Sale.AlTPRICE, name, value);
						core.dump();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* end */
					state.setState(PosState.SALE);
				} else {
					state.setState(PosState.ERROR);
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
				}
			} catch (UserCancelException e) {
				state.setState(PosState.SALE);
				cashier.resetPrivilege();
				context.setAuthorizerid("");
				authorizeCashier = null;
			}

		} else {
			context.setWarning("无效操作,按清除键继续!");
			state.setState(PosState.ERROR);
		}
	}

	// 荣华，预销售修价格
	private void orderMoney() {
		if (sale_rec != null
				&& (sale_rec.getType() == Sale.SALE || sale_rec.getType() == Sale.WITHDRAW)) {
			try {
				if (checkPrivilege(PosFunction.ALTPRICE)) {
					double price_org = ((double) sale_rec.getGoods().getPrice() / 100.0);
					double price_new;
					if (state.getState() == PosState.FUTRUESELL) {
						out.displayState("改预付款");
						out.prompt("请输入预付金额");
						price_new = in.getDouble(6, 2);
					} else {
						out.displayState("改价格");
						out.prompt("请输入金额");
						price_new = in.getDouble(6, 2);
					}

					double deltaValue = 0;
					double alterPrice = price_org;

					if (authorizeCashier != null) {
						deltaValue = (price_org
								* authorizeCashier.getMax_Disc() / 100.0);
						alterPrice = price_org
								* (100 - authorizeCashier.getMax_Disc())
								/ 100.0;
					}

					if ((price_org - price_new) > deltaValue) {

						context
								.setWarning("对不起，金额过低"
										+ (new Value((int) Math
												.rint(alterPrice * 100)))
												.toString() + "，继续!");

						if (state.getState() == PosState.FUTRUESELL) {
							state.setState(PosState.FUTRUESELL);
						} else {
							state.setState(PosState.BRINGFORWARD);
						}
						cashier.resetPrivilege();
						context.setAuthorizerid("");
						authorizeCashier = null;

						return;
					}
					sale_rec.setFactPrice(Discount.ALTPRICE, (int) Math
							.rint(100 * price_new));
					core.updateValue();
					out.displayDiscount(sale_rec, core.getValue());
					out.display(core.getValue(), context.getExchange());
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
					try {
						int value = (int) sale_rec.getDiscValue();
						String name = "变价";
						core.sell(Sale.AlTPRICE, name, value);
						core.dump();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* end */
					if (state.getState() == PosState.FUTRUESELL) {
						state.setState(PosState.FUTRUESELL);
					} else {
						state.setState(PosState.BRINGFORWARD);
					}
				} else {
					if (state.getState() == PosState.FUTRUESELL) {
						state.setState(PosState.FUTRUESELL);
					} else {
						state.setState(PosState.BRINGFORWARD);
					}
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
				}
			} catch (UserCancelException e) {
				if (state.getState() == PosState.FUTRUESELL) {
					state.setState(PosState.FUTRUESELL);
				} else {
					state.setState(PosState.BRINGFORWARD);
				}
				cashier.resetPrivilege();
				context.setAuthorizerid("");
				authorizeCashier = null;
			}

		} else {
			context.setWarning("无效操作,按清除键继续!");
			if (state.getState() == PosState.FUTRUESELL) {
				state.setState(PosState.FUTRUESELL);
			} else {
				state.setState(PosState.BRINGFORWARD);
			}
		}

	}

	/**
	 * The following codes were updated by huangxuean in July,2004 this method
	 * is used by singleDisc() totalDisc() and moneyDisc() it can make certain
	 * whether there are Disc in this salelst that calls it
	 * 
	 * @param salelst
	 * @return boolean ,false means has not been discounted
	 * @deprecated
	 */
	private boolean isDisc(SaleList salelst) {
		boolean disc = false;
		Sale sale = null;
		for (int i = 0; i < salelst.size(); i++) {
			sale = salelst.get(i);
			if (sale.getDiscType() == Discount.SINGLE
					|| sale.getDiscType() == Discount.TOTAL
					|| sale.getDiscType() == Discount.MONEY)
				return false;
			else
				disc = true;
		}
		return disc;
	}

	private void findpricesingleDisc() {
		salelst = core.getPosSheet().getSalelst();
		int nums = salelst.size();
		if (sale_rec != null && (sale_rec.getType() == Sale.SALE)) {
			if (sale_rec.getDiscValue() == 0) {
				try {
					if (checkPrivilege(PosFunction.DISCOUNT)) {
						double disc;
						out.displayState("单项折扣");
						out.prompt("请输入折扣率，如:若打八折,输入80; 若打七五折,输入75");
						disc = in.getDouble(6, 2);
						if (new Double(disc).intValue() == 0) {
							context.setWarning("折扣不能为空,按清除键继续!");
							state.setState(PosState.ERROR);
							return;
						}
						int idisc = new Double(disc).intValue();
						DiscRate discrate = new DiscRate(Discount.SINGLE,
								100 - idisc);
						sale_rec.setDiscount(discrate);
						core.updateValue();
						out.displayDiscount(sale_rec, core.getValue());
						out.display(core.getValue(), context.getExchange());
						core.getPosSheet().updateValue();
						cashier.resetPrivilege();
						context.setAuthorizerid("");
						state.setState(PosState.FIND);
					} else {
						state.setState(PosState.ERROR);
					}
				} catch (UserCancelException e) {
					state.setState(PosState.FIND);
					cashier.resetPrivilege();
					context.setAuthorizerid("");
				}
			} else {
				context.setWarning("当前商品已经折扣,不能进行单项折扣,按清除键继续!");
				state.setState(PosState.ERROR);
			}

		} else {
			context.setWarning("无效操作,按清除键继续!");
			state.setState(PosState.ERROR);
		}

	}

	/**
	 * 在权限许可的情况下对当前没有折扣的商品进行单项折扣处理
	 *  
	 */
	private void singleDisc() {
		salelst = core.getPosSheet().getSalelst();
		int nums = salelst.size();
		if (sale_rec != null && (sale_rec.getType() == Sale.SALE)) {

			// if (sale_rec.getDiscValue() == 0) {
			try {
				if (checkPrivilege(PosFunction.DISCOUNT)) {
					double disc;
					out.displayState("单项折扣");
					out.prompt("请输入折扣率，如:若打八折,输入80; 若打七五折,输入75");
					disc = in.getDouble(6, 2);
					int idisc = new Double(disc).intValue();
					if (idisc > 100) {
						context.setWarning("单项折扣不能大于100%,按清除键继续!");
						// 荣华项目预销售 增加部份
						if (state.getState() == PosState.FUTRUESELL) {
							in.waitCancel(context.getWarning());
							context.setWarning("");
							return;
						}
						// --------------------------
						state.setState(PosState.ERROR);
						cashier.resetPrivilege();
						context.setAuthorizerid("");
						authorizeCashier = null;
						return;
					}

					if (authorizeCashier != null
							&& (100 - idisc) > authorizeCashier.getMax_Disc()) {
						context.setWarning("此折扣超过的最大折扣，最高折扣"
								+ (100 - authorizeCashier.getMax_Disc())
								+ "%,按清除键继续!");
						// 荣华项目预销售，增加部份
						if (state.getState() == PosState.FUTRUESELL) {
							in.waitCancel(context.getWarning());
							context.setWarning("");
							return;
						}
						// ----------------------------
						state.setState(PosState.ERROR);
						cashier.resetPrivilege();
						context.setAuthorizerid("");
						authorizeCashier = null;
						return;
					}

					DiscRate discrate = new DiscRate(Discount.SINGLE,
							100 - idisc);
					sale_rec.setDiscount(discrate);
					core.updateValue();
					out.displayDiscount(sale_rec, core.getValue());
					out.display(core.getValue(), context.getExchange());
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
					/* add by lichao 8/24/2004 */
					try {
						//SheetValue v = core.getValue();

						int value = (int) sale_rec.getDiscValue();
						String name = "单项折扣";
						core.sell(Sale.AlTPRICE, name, value);
						core.dump();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* end */
					// 荣华项目预销售,增加部份
					if (state.getState() == PosState.FUTRUESELL) {
						state.setState(PosState.FUTRUESELL);
						return;
					}
					// ------------------
					state.setState(PosState.SALE);
				} else {
					// 荣华项目预销售，增加部份
					if (state.getState() == PosState.FUTRUESELL) {
						in.waitCancel(context.getWarning());
						context.setWarning("");
						return;
					}
					// -----------------------
					state.setState(PosState.ERROR);
				}
			} catch (UserCancelException e) {
				// 荣华项目预销售，增加部份
				if (state.getState() == PosState.FUTRUESELL) {
					state.setState(PosState.FUTRUESELL);
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
				}
				// --------------------------
				state.setState(PosState.SALE);
				cashier.resetPrivilege();
				context.setAuthorizerid("");
				authorizeCashier = null;
			}
			//            } else {
			//                context.setWarning("当前商品已经折扣,不能进行单项折扣,按清除键继续!");
			//                state.setState(PosState.ERROR);
			//            }

		} else {
			context.setWarning("无效操作,按清除键继续!");
			// 荣华项目预销售，增加部份
			if (state.getState() == PosState.FUTRUESELL) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			// ---------------------------
			state.setState(PosState.ERROR);
		}
	}

	/**
	 * The following codes were updated by huangxuean in July,2004 this method
	 * is used by totalDisc() specially it can make certain whether there are
	 * Disc in this salelst that calls it
	 * 
	 * @param salelst
	 * @return boolean，true 表示当前单已经进行折扣，不能再次参与任何折扣业务，反之，可以。
	 */
	private boolean isDisc4Total(SaleList salelst) {
		boolean disc = false;
		Sale sale = null;
		for (int i = 0; i < salelst.size(); i++) {
			sale = salelst.get(i);
			if (sale.getDiscValue() == 0) {
				return false;
			} else
				disc = true;
		}
		return disc;
	}

	/**
	 * 在权限许可的情况下对当前单没有折扣过的商品进行总额折扣处理。 如果当前单商品均已有过折扣，则当前单商品不能再次参与总额折扣业务
	 * 否则，对当前单中所有没有折扣的商品进行总额折扣
	 */
	private void totalDisc() {
		salelst = core.getPosSheet().getSalelst();
		int totalValue = salelst.getTotalValue();
		Vector vnums = salelst.getNumsWithoutDisc();

		if (salelst != null) {

			try {
				if (checkPrivilege(PosFunction.DISCTOTAL)) {
					double disc;
					Sale sale = null;
					//                    if (isDisc4Total(salelst)) {
					//                        context.setWarning("当前单商品已经折扣,不能进行总额折扣,按清除键继续!");
					//                        state.setState(PosState.ERROR);
					//                        return;
					//                    }
					if (false) {
						return;
					} else {
						out.displayState("总额折扣");
						out.prompt("请输入折扣率，如:若打八折,输入80; 若打七五折,输入75");
						disc = in.getDouble(6, 2);

						if (disc > 100) {
							context.setWarning("总额折扣不能超过100%,按清除键继续!");
							// 荣华项目预销售，增加部份
							if (state.getState() == PosState.FUTRUESELL) {
								in.waitCancel(context.getWarning());
								context.setWarning("");

								cashier.resetPrivilege();
								context.setAuthorizerid("");
								authorizeCashier = null;
								return;
							}
							// ----------------------
							state.setState(PosState.ERROR);
							cashier.resetPrivilege();
							context.setAuthorizerid("");
							authorizeCashier = null;
							return;
						}

						if (authorizeCashier != null
								&& (100 - disc) > authorizeCashier
										.getMax_Disc()) {
							context.setWarning("总额折扣超过最大折扣，最多折扣到"
									+ (100 - authorizeCashier.getMax_Disc())
									+ "%,按清除键继续!");
							// 荣华项目预销售，增加部份
							if (state.getState() == PosState.FUTRUESELL) {
								in.waitCancel(context.getWarning());
								context.setWarning("");

								cashier.resetPrivilege();
								context.setAuthorizerid("");
								authorizeCashier = null;
								return;
							}
							// -----------------------
							state.setState(PosState.ERROR);
							cashier.resetPrivilege();
							context.setAuthorizerid("");
							authorizeCashier = null;
							return;

						}

						for (int i = 0; i < vnums.size(); i++) {
							int num = ((Integer) vnums.elementAt(i)).intValue();
							DiscRate discrate = new DiscRate(Discount.TOTAL,
									100 - (int) disc);
							sale = salelst.get(num);
							if (!(sale.getType() == Sale.TOTAL
									|| sale.getType() == Sale.AlTPRICE
									|| sale.getType() == Sale.SINGLEDISC
									|| sale.getType() == Sale.TOTALDISC
									|| sale.getType() == Sale.MONEYDISC || sale
									.getType() == Sale.AUTODISC)) {
								sale.setDiscount(discrate);
							}
						}
					}
					core.updateValue();
					out.displayDiscount(sale, core.getValue());
					out.display(core.getValue(), context.getExchange());
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
					/* add by lichao 8/24/2004 */
					try {
						//SheetValue v = core.getValue();

						int value = (int) sale.getDiscValue();
						String name = "总额折扣";
						core.sell(Sale.AlTPRICE, name, value);
						core.dump();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* end */
					//荣华项目预销售，增加部份
					if (state.getState() == PosState.FUTRUESELL) {
						return;
					}
					// ------------------------
					state.setState(PosState.SALE);
				} else {
					// 荣华项目预销售，增加部份
					if (state.getState() == PosState.FUTRUESELL) {
						in.waitCancel(context.getWarning());
						context.setWarning("");
						return;
					}
					// -------------------------
					state.setState(PosState.ERROR);
				}
			} catch (UserCancelException e) {
				// 荣华项目预销售，增加部份
				if (state.getState() == PosState.FUTRUESELL) {
					state.setState(PosState.FUTRUESELL);

					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
					return;
				}
				// -----------------------------
				state.setState(PosState.SALE);
				cashier.resetPrivilege();
				context.setAuthorizerid("");
				authorizeCashier = null;
			}
		} else {
			context.setWarning("无效操作,按清除键继续!");
			// 荣华项目预销售，增加部份
			if (state.getState() == PosState.FUTRUESELL) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			// --------------------------------
			state.setState(PosState.ERROR);
		}
	}

	private void totalDiscs() {
		salelst = core.getPosSheet().getSalelst();
		int totalValue = salelst.getTotalValue();
		Vector vnums = salelst.getNumsWithoutDisc();

		if (salelst != null) {

			try {
				if (checkPrivilege(PosFunction.DISCTOTAL)) {
					double disc;
					Sale sale = null;
					//                    if (isDisc4Total(salelst)) {
					//                        context.setWarning("当前单商品已经折扣,不能进行总额折扣,按清除键继续!");
					//                        state.setState(PosState.ERROR);
					//                        return;
					//                    }
					if (false) {
						return;
					} else {
						out.displayState("机团总额折扣");
						out.prompt("请输入折扣率，如:若打八折,输入80; 若打七五折,输入75");
						disc = in.getDouble(6, 2);

						if (disc > 100) {
							context.setWarning("总额折扣不能超过100%,按清除键继续!");
							// 荣华项目预销售，增加部份
							if (state.getState() == PosState.FUTRUESELL) {
								in.waitCancel(context.getWarning());
								context.setWarning("");

								cashier.resetPrivilege();
								context.setAuthorizerid("");
								authorizeCashier = null;
								return;
							}
							// ----------------------
							state.setState(PosState.ERROR);
							cashier.resetPrivilege();
							context.setAuthorizerid("");
							authorizeCashier = null;
							return;
						}

						if (authorizeCashier != null
								&& (100 - disc) > authorizeCashier
										.getMax_Disc()) {
							if (!context.getGrouplargess()) {
								context
										.setWarning("总额折扣超过最大折扣，最多折扣到"
												+ (100 - authorizeCashier
														.getMax_Disc())
												+ "%,按清除键继续!");
								// 荣华项目预销售，增加部份
								if (state.getState() == PosState.FUTRUESELL) {
									in.waitCancel(context.getWarning());
									context.setWarning("");

									cashier.resetPrivilege();
									context.setAuthorizerid("");
									authorizeCashier = null;
									return;
								}
								// -----------------------
								state.setState(PosState.ERROR);
								cashier.resetPrivilege();
								context.setAuthorizerid("");
								authorizeCashier = null;
								return;
							}
						}

						for (int i = 0; i < vnums.size(); i++) {
							int num = ((Integer) vnums.elementAt(i)).intValue();
							DiscRate discrate = new DiscRate(Discount.TOTAL,
									100 - (int) disc);
							sale = salelst.get(num);
							if (!(sale.getType() == Sale.TOTAL
									|| sale.getType() == Sale.AlTPRICE
									|| sale.getType() == Sale.SINGLEDISC
									|| sale.getType() == Sale.TOTALDISC
									|| sale.getType() == Sale.MONEYDISC || sale
									.getType() == Sale.AUTODISC)) {

								sale.setDiscount(discrate);
							}
						}
					}
					core.updateValue();
					out.displayDiscount(sale, core.getValue());
					out.display(core.getValue(), context.getExchange());
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
					/* add by lichao 8/24/2004 */
					try {
						//SheetValue v = core.getValue();

						int value = (int) sale.getDiscValue();
						String name = "总额折扣";
						core.sell(Sale.AlTPRICE, name, value);
						core.dump();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* end */
					//荣华项目预销售，增加部份
					if (state.getState() == PosState.FUTRUESELL) {
						return;
					}
					// ------------------------
					state.setState(PosState.SALE);
				} else {
					// 荣华项目预销售，增加部份
					if (state.getState() == PosState.FUTRUESELL) {
						in.waitCancel(context.getWarning());
						context.setWarning("");
						return;
					}
					// -------------------------
					state.setState(PosState.ERROR);
				}
			} catch (UserCancelException e) {
				// 荣华项目预销售，增加部份
				if (state.getState() == PosState.FUTRUESELL) {
					state.setState(PosState.FUTRUESELL);

					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
					return;
				}
				// -----------------------------
				state.setState(PosState.SALE);
				cashier.resetPrivilege();
				context.setAuthorizerid("");
				authorizeCashier = null;
			}
		} else {
			context.setWarning("无效操作,按清除键继续!");
			// 荣华项目预销售，增加部份
			if (state.getState() == PosState.FUTRUESELL) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			// --------------------------------
			state.setState(PosState.ERROR);
		}
	}

	private void findpricetotalDisc() {
		salelst = core.getPosSheet().getSalelst();
		int totalValue = salelst.getTotalValue();
		Vector vnums = salelst.getNumsWithoutDisc();

		if (salelst != null && sale_rec != null
				&& (sale_rec.getType() == Sale.SALE)) {

			try {
				if (checkPrivilege(PosFunction.DISCTOTAL)) {
					double disc;
					Sale sale = null;
					if (isDisc4Total(salelst)) {
						context.setWarning("当前单商品已经折扣,不能进行总额折扣,按清除键继续!");
						state.setState(PosState.ERROR);
						return;
					} else {
						out.displayState("总额折扣");
						out.prompt("请输入折扣率，如:若打八折,输入80; 若打七五折,输入75");
						disc = in.getDouble(6, 2);
						if (new Double(disc).intValue() == 0) {
							context.setWarning("折扣不能为空,按清除键继续!");
							state.setState(PosState.ERROR);
							return;
						}
						for (int i = 0; i < vnums.size(); i++) {
							int num = ((Integer) vnums.elementAt(i)).intValue();
							DiscRate discrate = new DiscRate(Discount.TOTAL,
									100 - (int) disc);
							sale = salelst.get(num);
							sale.setDiscount(discrate);
						}
					}
					core.updateValue();
					out.displayDiscount(sale, core.getValue());
					out.display(core.getValue(), context.getExchange());
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					state.setState(PosState.FIND);
				} else {
					state.setState(PosState.ERROR);
				}
			} catch (UserCancelException e) {
				//state.setState(PosState.FIND);
				state.setState(PosState.SALE);
				cashier.resetPrivilege();
				context.setAuthorizerid("");
			}

		} else {
			context.setWarning("无效操作,按清除键继续!");
			state.setState(PosState.ERROR);
		}
	}

	/**
	 * 
	 * @param totalValue
	 * @param discValue
	 * @param vnums
	 * @return
	 * @deprecated
	 */
	private int afterDisc(int totalValue, int discValue, Vector vnums) {
		int itemdisc = 0;
		if ((totalValue - discValue) % vnums.size() == 0) {
			itemdisc = (totalValue - discValue) / vnums.size();
			return itemdisc;
		} else {
			for (int i = 0; i < vnums.size() - 1; i++) {
				itemdisc = Math.round((totalValue - discValue) / vnums.size());
				itemdisc += (totalValue - discValue) % vnums.size();
				return itemdisc;
			}
			return itemdisc;
		}
		//return itemdisc;
	}

	/**
	 * 在权限许可的情况下对当前单没有折扣过的商品进行金额折扣处理。 如果当前单商品均已有过折扣，则当前单商品不能再次参与金额折扣业务
	 * 否则，对当前单中所有没有折扣的商品进行金额折扣
	 * 
	 * @param
	 * @return void
	 */
	private void moneyDisc() {
		salelst = core.getPosSheet().getSalelst();
		int unPaid = core.getPosSheet().getValue().getValueToPay();
		int total = salelst.getValueWithoutDisc();//salelst.getTotalValue();
		int totalValue = salelst.getValueWithoutDisc();
		Vector vnums = salelst.getNumsWithoutDisc();
		int numAll = salelst.size();
		if (salelst != null && sale_rec != null
				&& (sale_rec.getType() == Sale.SALE)) {

			try {
				if (checkPrivilege(PosFunction.DISCMONEY)) {
					double disc;
					Sale sale = null;
					//					if(vnums.size()>0){
					//                    if (isDisc4Total(salelst)) {
					//                        context.setWarning("当前单商品已经折扣,不能进行金额折扣,按清除键继续!");
					//                        state.setState(PosState.ERROR);
					//                        cashier.resetPrivilege();
					//                        context.setAuthorizerid("");
					//                        authorizeCashier = null;
					//                        return;
					//                    }
					if (false) {
						return;
					} else {
						int temp = 0;
						out.displayState("金额折扣");
						out.prompt("请输入折后金额");
						disc = in.getDouble(6, 2);

						if (Math.rint(disc * 100) > unPaid) {
							context.setWarning("折扣后金额不能大于应收款,按清除键继续!");
							// 荣华项目预销售，增加部份
							if (state.getState() == PosState.FUTRUESELL) {
								in.waitCancel(context.getWarning());
								context.setWarning("");

								cashier.resetPrivilege();
								context.setAuthorizerid("");
								authorizeCashier = null;

								return;
							}
							// --------------------------------
							state.setState(PosState.ERROR);

							cashier.resetPrivilege();
							context.setAuthorizerid("");
							authorizeCashier = null;

							return;
						}

						//没有打折的商品的总要打折的金额
						double totalDisc = 0.0;
						totalDisc = total - disc * 100.0;
						//操作员最大能打的金额
						double discAvailable = 0.0;
						if (authorizeCashier != null) {
							discAvailable = totalValue
									* authorizeCashier.getMax_Disc() / 100.0;
						}

						if (totalDisc > discAvailable) {
							context.setWarning("折扣金额过最大折扣金额,最低变价"
									+ (new Value((int) Math
											.rint((total - discAvailable))))
											.toString() + ",按清除键继续!");
							// 荣华项目预销售，增加部份
							if (state.getState() == PosState.FUTRUESELL) {
								in.waitCancel(context.getWarning());
								context.setWarning("");

								cashier.resetPrivilege();
								context.setAuthorizerid("");
								authorizeCashier = null;
								return;
							}
							// ----------------------------------------
							state.setState(PosState.ERROR);

							cashier.resetPrivilege();
							context.setAuthorizerid("");
							authorizeCashier = null;

							return;
						}

						// int tempValue=0;
						for (int i = 0; i < vnums.size() - 1; i++) {
							int num = ((Integer) vnums.elementAt(i)).intValue();
							sale = salelst.get(num);
							long orgvalue = sale.getStdValue();
							//int itemvalue = orgvalue - new
							// Double((orgvalue*1.0/totalValue)*(total-disc*100)).intValue();
							long itemvalue = orgvalue
									- (int) Math
											.rint((orgvalue * 1.0 / totalValue)
													* (total - disc * 100));
							DiscPrice discprice = new DiscPrice(Discount.MONEY,
									itemvalue);
							sale.setDiscValue(discprice);
							//temp += new
							// Double((orgvalue*1.0/totalValue)*(total-disc*100)).intValue();
							temp += (int) Math
									.rint((orgvalue * 1.0 / totalValue)
											* (total - disc * 100));

						}

						if (vnums.size() > 0) {
							int num = ((Integer) vnums
									.elementAt(vnums.size() - 1)).intValue();
							sale = salelst.get(num);
							long orgvalue = sale.getStdValue();
							//int itemvalue = orgvalue
							// -(totalValue-(int)(disc*100)-temp);
							long itemvalue = orgvalue
									- (total - (int) Math.rint((disc * 100)) - temp);
							//int itemvalue = orgvalue -(total-new
							// Double((disc*100)-temp).intValue());

							DiscPrice discprice = new DiscPrice(Discount.MONEY,
									itemvalue);
							sale.setDiscValue(discprice);
						}
						core.updateValue();
						out.displayDiscount(sale, core.getValue());
						out.display(core.getValue(), context.getExchange());
						core.getPosSheet().updateValue();
						cashier.resetPrivilege();
						context.setAuthorizerid("");
						authorizeCashier = null;
						/* add by lichao 8/24/2004 */
						try {
							//SheetValue v = core.getValue();

							int value = (int) sale.getDiscValue();
							String name = "金额折扣";
							core.sell(Sale.AlTPRICE, name, value);
							core.dump();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						/* end *///荣华项目预销售，增加部份
						if (state.getState() == PosState.FUTRUESELL) {
							state.setState(PosState.FUTRUESELL);
							return;
						}
						// ------------------------------
						state.setState(PosState.SALE);
					}
				} else {
					// 荣华项目预销售，增加部份
					if (state.getState() == PosState.FUTRUESELL) {
						state.setState(PosState.FUTRUESELL);
						cashier.resetPrivilege();
						context.setAuthorizerid("");
						authorizeCashier = null;
						return;
					}
					// --------------------------
					state.setState(PosState.ERROR);
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
				}
			} catch (UserCancelException e) {
				//荣华预售销售，增加部份
				if (state.getState() == PosState.FUTRUESELL) {
					state.setState(PosState.FUTRUESELL);
					cashier.resetPrivilege();
					context.setAuthorizerid("");
					authorizeCashier = null;
				}
				// -----------------------------
				state.setState(PosState.SALE);
				cashier.resetPrivilege();
				context.setAuthorizerid("");
				authorizeCashier = null;
			}

		} else {
			context.setWarning("无效操作,按清除键继续!");
			// 荣华项目预销售，增加部份
			if (state.getState() == PosState.FUTRUESELL) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			// -------------------------------
			state.setState(PosState.ERROR);
		}

	}

	/**
	 * @param
	 * @return
	 * @deprecated
	 */
	private void findpricemoneyDisc() {
		salelst = core.getPosSheet().getSalelst();
		int total = salelst.getTotalValue();
		int totalValue = salelst.getValueWithoutDisc();
		Vector vnums = salelst.getNumsWithoutDisc();
		int numAll = salelst.size();
		if (salelst != null && sale_rec != null
				&& (sale_rec.getType() == Sale.SALE)) {
			try {
				if (checkPrivilege(PosFunction.DISCMONEY)) {
					double disc;
					Sale sale = null;
					//					if(vnums.size()>0){
					if (isDisc4Total(salelst)) {
						context.setWarning("当前单商品已经折扣,不能进行金额折扣,按清除键继续!");
						state.setState(PosState.ERROR);
						return;
					} else {
						int temp = 0;
						out.displayState("金额折扣");
						out.prompt("请输入折后金额");
						disc = in.getDouble(6, 2);
						if (new Double(disc).intValue() == 0) {
							context.setWarning("折扣不能为空,按清除键继续!");
							state.setState(PosState.ERROR);
							return;
						}
						for (int i = 0; i < vnums.size() - 1; i++) {
							int num = ((Integer) vnums.elementAt(i)).intValue();
							sale = salelst.get(num);
							long orgvalue = sale.getStdValue();
							//int itemvalue = orgvalue - new
							// Double((orgvalue*1.0/totalValue)*(total-disc*100)).intValue();
							long itemvalue = orgvalue
									- (int) Math
											.rint((orgvalue * 1.0 / totalValue)
													* (total - disc * 100));
							DiscPrice discprice = new DiscPrice(Discount.MONEY,
									itemvalue);
							sale.setDiscValue(discprice);
							temp += new Double((orgvalue * 1.0 / totalValue)
									* (total - disc * 100)).intValue();
						}
						if (vnums.size() > 0) {
							int num = ((Integer) vnums
									.elementAt(vnums.size() - 1)).intValue();
							sale = salelst.get(num);
							long orgvalue = sale.getStdValue();
							//int itemvalue = orgvalue
							// -(totalValue-(int)(disc*100)-temp);
							long itemvalue = orgvalue
									- (total - (int) Math.rint((disc * 100)) - temp);
							//int itemvalue = orgvalue -(total-new
							// Double((disc*100)-temp).intValue());
							DiscPrice discprice = new DiscPrice(Discount.MONEY,
									itemvalue);
							sale.setDiscValue(discprice);
						}
						core.updateValue();
						out.displayDiscount(sale, core.getValue());
						out.display(core.getValue(), context.getExchange());
						cashier.resetPrivilege();
						context.setAuthorizerid("");
						state.setState(PosState.FIND);
					}
				} else {
					state.setState(PosState.ERROR);
				}
			} catch (UserCancelException e) {
				state.setState(PosState.FIND);
				cashier.resetPrivilege();
				context.setAuthorizerid("");
			}
			//		}
		} else {
			context.setWarning("无效操作,按清除键继续!");
			state.setState(PosState.ERROR);
		}

	}

	/**
	 * @param
	 * @return
	 * @deprecated
	 */

	private void correct() {
		out.displayState("更正");
		out.prompt("请输入商品数量及条码，或按清除键.");
		PosInput input = in.getInput();
		switch (input.key()) {
		case PosFunction.CANCEL:
			core.writelog("更正", "1", 0);
			state.setState(PosState.SALE);
			break;

		case PosFunction.CORRECT:
			core.writelog("更正", "1", 0);
			state.setState(PosState.CORRECT);
			break;

		case PosFunction.WITHDRAW:
			core.writelog("更正", "1", 0);
			state.setState(PosState.WITHDRAW);
			break;

		case PosFunction.GOODS:
			try {
				PosInputGoods g = (PosInputGoods) input;
				PosInputGoods input_goods = g;
				int disccount = 0;

				Goods goods = core.findGoods(g.getCode());
				int init_qty = g.getQty();
				Volume init_volume = g.getVolume();
				PosConfig config = PosConfig.getInstance();

				if (goods != null && config.isIndicatorDept(goods.getDeptid())) {
					OilInput oilInput = new OilInput(pos.posOutStream);
					oilInput.show();
					MainUI oldMainUI = out.getMainUI();
					out.setMainUI(oilInput);
					oilInput.show();

					if (pos.core.getPosSheet().getLoanCardQuery() != null
							&& pos.core.getPosSheet().getLoanCardQuery()
									.getSubcardNo() != null) {
						String cardno = pos.core.getPosSheet()
								.getLoanCardQuery().getSubcardNo();
						try {
							disccount = pos.core.getLoanCardDiscCount(cardno);
						} catch (RealTimeException ex) {
							state.setState(PosState.NETWORKERROR);
						}
					}

					goods.setOilDisc(disccount);
					input_goods = in.getOilInput(goods, init_volume);
					out.setMainUI(oldMainUI);
					oilInput.dispose();
					out.clearInputLine();
				}

				Sale s;
				if (input_goods != null)
					try {
						s = core.correct(input_goods);
						if (s == null) {
							core.writelog("更正", "1", 0);
							System.out.println("更正操作不成功.");
							state.setState(PosState.ERROR);
						} else {
							core.writelog("更正", "0", 0);
							sale_rec = s;
							out.display(s, core.getValue());
							out.display(core.getValue(), context.getExchange());
							state.setState(PosState.SALE);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			} catch (RealTimeException ex) {
				state.setState(PosState.NETWORKERROR);
			}

			break;

		default:
			core.writelog("更正", "1", 0);
			out.prompt("无效操作,按清除键继续!");
			in.waitCancel();
			state.setState(PosState.SALE);
		}
		cashier.resetPrivilege();
		context.setAuthorizerid("");
	}

	private void withdraw() {
		out.displayState("退货");
		out.prompt("请输入商品数量及条码，或按放弃键.");
		PosInput input = in.getInput();
		switch (input.key()) {

		case PosFunction.EXIT:
		case PosFunction.CANCEL:
			core.writelog("退货", "1", 0);
			state.setState(state.getOldState());
			break;

		//			case PosFunction.CORRECT :
		//				try {
		//					if (checkPrivilege(PosFunction.CORRECT)) {
		//						core.writelog("退货", "1", 0);
		//						state.setState(PosState.CORRECT);
		//					} else {
		//						core.writelog("退货", "1", 0);
		//						state.setState(PosState.ERROR);
		//					}
		//				} catch (UserCancelException e2) {
		//					// do nothing
		//				}
		//
		//				break;

		case PosFunction.WITHDRAW:
			core.writelog("退货", "1", 0);
			state.setState(PosState.WITHDRAW);
			break;

		case PosFunction.GOODS:
			int baseprice = 0;
			try {
				PosInputGoods g = (PosInputGoods) input;
				Goods goods = core.findGoods(g.getCode());
				if (goods != null && goods.getType() == 8
						&& goods.getDeptid() != null
						&& !goods.getDeptid().equals(Goods.LOADOMETER)) {
					String BASE_PRICE = (String) PosConfig.getInstance()
							.getString("PRICE_FLAG");
					if (BASE_PRICE.equals("ON")) {
						DispPrice dispPrice = new DispPrice(pos.posOutStream);
						dispPrice.show();
						MainUI oldMainUI = out.getMainUI();
						out.setMainUI(dispPrice);
						dispPrice.show();
						baseprice = in.getbaseprice();
						goods.setPrice(baseprice);
						out.setMainUI(oldMainUI);
						dispPrice.dispose();
						out.clearInputLine();
						if (!dispPrice.getconfirm()) {
							return;
						}
					}
				}

				/**
				 * 地磅交易输入处理
				 * 
				 * @param goods
				 *            输入商品
				 * @param goods.getDeptid()
				 *            商品小类，等于Goods.LOADOMETER表示地磅
				 */
				/*
				 * 荣华 2005年11月21日 if (goods != null && goods.getDeptid() != null &&
				 * goods.getDeptid().equals(Goods.LOADOMETER)) {
				 * 
				 * String authorID = context.getAuthorizerid();
				 * 
				 * Loadometer loadometer = new Loadometer(pos.posOutStream);
				 * loadometer.show(); MainUI oldMainUI = out.getMainUI();
				 * out.setMainUI(loadometer); loadometer.show();
				 * 
				 * g = in.getLoadometer(goods, g.getVolume(), cashier);
				 * out.setMainUI(oldMainUI); loadometer.dispose();
				 * out.clearInputLine();
				 * 
				 * context.setAuthorizerid(authorID); }
				 */

				Sale s;
				if (g != null) {
					try {
						sale_rec = s = core.withdraw(g, baseprice);
						if (s == null) {
							core.writelog("退货", "1", 0);
							state.setState(PosState.ERROR);
						} else {
							core.writelog("退货", "0", 0);
							out.display(s, core.getValue());
							out.display(core.getValue(), context.getExchange());
							state.setState(PosState.SALE);
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} catch (RealTimeException ex) {
				state.setState(PosState.NETWORKERROR);
			}
			break;
		/*
		 * case PosFunction.QUICKCORRECT :
		 * 
		 * try { Sale s = core.quick_correct(); if (s == null) {
		 * state.setState(PosState.ERROR); } else { sale_rec = s;
		 * state.setState(PosState.WITHDRAW); out.displayUnhold(s,
		 * core.getValue()); out.displayDiscount(s, core.getValue()); //
		 * if(s.getStdValue()-s.getFactValue()!=0){//if(s.getDiscValue()!=0){ //
		 * System.out.println("core.getValue() is:"+core.getValue()); //
		 * out.displayDiscount(s, core.getValue()); // }
		 * out.display(core.getValue(), context.getExchange()); } } catch
		 * (FileNotFoundException e1) { e1.printStackTrace(); } catch
		 * (IOException e1) { e1.printStackTrace(); } catch (RealTimeException
		 * ex) { state.setState(PosState.NETWORKERROR); } break;
		 */
		case PosFunction.PAYMENT:
			try {
				PosInputPayment p = (PosInputPayment) input;
				Payment pay_info = core.pay(p);
				inputpayment = p;
				if (pay_info == null) {
					state.setState(PosState.ERROR);
				} else {
					out.display(core.getValue(), context.getExchange());
					out.display(pay_info, core.getValue());
					out.display(core.getPosSheet().getMemberCard());
					if (core.getValue().getValueToPay() <= 0) {
						state.setState(PosState.CLOSESHEET);
						out.clear();
						out.displayChange(core.getValue());
						cashDrawer.open();
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RealTimeException e) {
				state.setState(PosState.NETWORKERROR);
			}
			break;

		default:
			core.writelog("退货", "0", 0);
			out.prompt("无效操作,按清除键继续!");
			in.waitCancel();
			state.setState(PosState.SALE);
		}
		cashier.resetPrivilege();
		context.setAuthorizerid("");
	}

	public void unhold() {
		int count = context.getHeldCount();
		if (count < 1) {
			core.writelog("解挂单", "1", 0);
			context.setWarning("不能挂空单,按清除键继续!");
			state.setState(PosState.ERROR);
			return;
		} else if (count == 1) {
			core.unholdFirst();
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			out.print("解挂单: " + Formatter.getTime(new Date()));
			out.displayHeader(context);
			//显示界挂单会员卡信息
			if (core.getPosSheet().getMemberCard() != null) {
				out.dispMemberCardHeader(core.getPosSheet().getMemberCard());
			}
			if (core.getPosSheet().getCouponNO() != null
					&& !core.getPosSheet().getCouponNO().equals(""))
				out.dispCouponHeader(core.getPosSheet().getCouponNO());
			displaySheet();
			core.writelog("解挂单", "0", 0);
			return;
		}

		HoldList holdList = new HoldList(core.getSheetBrief());
		holdList.show();

		if (holdList.isConfrim()) {
			core.unholdSheet(holdList.getHoldNo());
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			out.print("解挂单: " + Formatter.getTime(new Date()));
			out.displayHeader(context);
			//显示界挂单会员卡信息
			if (core.getPosSheet().getMemberCard() != null) {
				out.dispMemberCardHeader(core.getPosSheet().getMemberCard());
			}

			if (core.getPosSheet().getCouponNO() != null
					&& !core.getPosSheet().getCouponNO().equals(""))
				out.dispCouponHeader(core.getPosSheet().getCouponNO());

			displaySheet();
			state.setState(PosState.SALE);
		}

	}

	public void hold() {
		if (core.getPosSheet().isCouponEncash) {
			context.setWarning("兑换券时不允许挂单,按清除键继续!");
			// 荣业项目预销售，增加部份
			if (state.getState() == PosState.FUTRUESELL) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			if (state.getState() == PosState.BRINGFORWARD) {
				in.waitCancel(context.getWaiterid());
				context.setWaiterid("");
				return;
			}
			state.setState(PosState.ERROR);
			return;
		}
		if (core.getPosSheet().isCouponSale) {
			context.setWarning("卖券时不允许挂单,按清除键继续!");
			// 荣业项目预销售，增加部份
			if (state.getState() == PosState.FUTRUESELL) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			if (state.getState() == PosState.BRINGFORWARD) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			state.setState(PosState.ERROR);
			return;
		}

		if (core.getPosSheet().getPayLen() > 0) {
			core.writelog("挂单", "1", 0);
			context.setWarning("已支付不允许挂单,按清除键继续!");
			// 荣业项目预销售，增加部份
			if (state.getState() == PosState.FUTRUESELL) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			if (state.getState() == PosState.BRINGFORWARD) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			state.setState(PosState.ERROR);
			return;
		}

		int count = context.getHeldCount();
		if (count >= core.MAX_SHEETS - 1) {
			core.writelog("挂单", "1", 0);
			context.setWarning("挂单数已经达到上限,按清除键继续!");
			// 荣业项目预销售，增加部份
			if (state.getState() == PosState.FUTRUESELL) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			if (state.getState() == PosState.BRINGFORWARD) {
				in.waitCancel(context.getWarning());
				context.setWarning("");
				return;
			}
			state.setState(PosState.ERROR);
			return;
		} else {
			core.writelog("挂单", "0", 0);
			int n = core.holdSheet();
			state.setState(PosState.PRESALE);
			out.clear();
			out.print("挂单（未付款）: " + Formatter.getTime(new Date()));
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			//// out.displayHeader(context);
		}
	}

	/**
	 * 合计功能
	 * 
	 * @deprecated
	 */
	public void showTotal() {
		try {
			out.disptotal(core.getValue());
			SheetValue v = core.getValue();

			int value = v.getValueTotal();
			String name = "合计";
			core.sell(Sale.TOTAL, name, value);
			core.dump();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cashin() {
		try {
			out.displayState("入款");
			out.prompt("请输入入款金额，并按现金键。");

			PosInput input = in.getInput();
			if (input.key() == PosFunction.CANCEL) {
				core.writelog("入款", "1", 0);
				state.setState(PosState.PRESALE);
			} else if (input.key() == PosFunction.PAYMENT
					&& ((PosInputPayment) input).getCents() > 0) {
				PosInputPayment p = (PosInputPayment) input;

				Payment pay_info = core.cashin(p);
				if (pay_info == null) {
					core.writelog("入款", "1", 0);
					state.setState(PosState.ERROR);
				} else {
					cashDrawer.open();
					String s = pay_info.getCardno() + " "
							+ new Value(pay_info.getValue()).toString();
					notice("请将以下金额放入钱箱：" + s);
					out.displayHeader(context);
					out.displayCash("入款", s);
					core.openSheet();
					//// out.displayHeader(context);
					out.displayWelcome();
					core.writelog("入款", "0", 0);
					state.setState(PosState.PRESALE);
				}
			} else {
				core.writelog("入款", "1", 0);
				context.setWarning("无效输入,按清除键继续!");
				state.setState(PosState.ERROR);
			}
		} catch (IOException e) {
			state.setState(PosState.ERROR);
		}

	}

	public void cashout() {
		try {
			out.displayState("出款");
			out.prompt("请输入出款金额，并按现金键。");

			PosInput input = in.getInput();
			if (input.key() == PosFunction.CANCEL) {
				core.writelog("出款", "1", 0);
				state.setState(PosState.PRESALE);
			} else if (input.key() == PosFunction.PAYMENT
					&& ((PosInputPayment) input).getCents() > 0) {
				PosInputPayment p = (PosInputPayment) input;
				Payment pay_info = core.cashout(p);
				if (pay_info == null) {
					core.writelog("出款", "1", 0);
					state.setState(PosState.ERROR);
				} else {
					String s;
					cashDrawer.open();
					s = (context.getCurrenCode() == "RMB") ? "" : context
							.getCurrenCode();

					s += " " + new Value(-pay_info.getValue()).toString();

					notice("请从钱箱取出以下金额：" + s);
					out.displayHeader(context);
					out.displayCash("出款", s);
					core.openSheet();
					//  out.displayHeader(context);
					out.displayWelcome();
					core.writelog("出款", "0", 0);
					state.setState(PosState.PRESALE);
				}
			} else {
				core.writelog("出款", "1", 0);
				context.setWarning("无效输入,按清除键继续!");
				state.setState(PosState.ERROR);
			}
		} catch (IOException e) {
			state.setState(PosState.ERROR);
		}
	}
	
	
	private String getBankCardNo(int maxlen) throws UserCancelException {

		MSRInput msrInput = new MSRInput("请刷银行卡：","bank");
		msrInput.show();

		try {
			while (!msrInput.isFinish())
				Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String inputCode = msrInput.getInputcode();

		if (!msrInput.isConfirm()) {
			throw new UserCancelException();
		}

		if (inputCode.length() > maxlen) {
			inputCode = inputCode.substring(0, maxlen - 1);
		}

		return inputCode;
	}

	private boolean confirm(String s) {
		DialogConfirm confirm = new DialogConfirm();
		confirm.setMessage(s);
		confirm.show();

		return (confirm.isConfirm());
	}

	private void notice(String note) {
		DialogInfo notice = new DialogInfo();
		notice.setMessage(note);
		notice.show();
	}

	private PosState state = null;

	private PosCore core = null;

	private PosDevIn in = null;

	private PosDevOut out = null;

	private Operator cashier = null;

	private Operator authorizeCashier = null;

	private Sale sale_rec = null;

	private SaleList salelst = null;

	private LogonDialog logonDialog;

	private POSCashDrawer cashDrawer = null;

	private PosContext context;

	private PosInputPayment inputpayment = null;
	//    private CouponListLargess couponListLargess;
	//    private CouponLargessList couponLargessList;
}

/**
 * @version 1.0 2004.05.11
 * @author Mengluoyi, Royalstone Co., Ltd.
 */

final class PosState {
	public PosState(int state) {
		CurrentState = state;
		OldState = state;
	}

	public int getState() {
		return CurrentState;
	}

	public int getOldState() {
		return OldState;
	}

	public void setState(int state) {
		OldState = CurrentState;
		CurrentState = state;
	}

	public int restoreState() {
		CurrentState = OldState;
		return CurrentState;
	}

	public boolean equals(int state) {
		return (CurrentState == state);
	}

	public boolean equals(PosState state) {
		return (CurrentState == state.CurrentState);
	}

	public String toString() {
		if (CurrentState == PRELOGON)
			return "PRELOGON";
		if (CurrentState == PRESALE)
			return "PRESALE";
		if (CurrentState == SALE)
			return "SALE";
		if (CurrentState == MAXCASH)
			return "MAXCASH";
		if (CurrentState == CORRECT)
			return "CORRECT";
		if (CurrentState == WITHDRAW)
			return "WITHDRAW";
		if (CurrentState == DISCOUNT)
			return "DISCOUNT";
		if (CurrentState == DISCTOTAL)
			return "DISCTOTAL";
		if (CurrentState == LOCK)
			return "LOCK";
		if (CurrentState == ALTPRICE)
			return "ALTPRICE";
		if (CurrentState == OPENSHEET)
			return "OPENSHEET";
		if (CurrentState == CLOSESHEET)
			return "CLOSESHEET";
		if (CurrentState == HOLD)
			return "HOLD";
		if (CurrentState == ERROR)
			return "ERROR";
		if (CurrentState == EXIT)
			return "EXIT";
		if (CurrentState == FUTRUESELL)
			return " FUTRUESELL";
		if (CurrentState == BRINGFORWARD)
			return " BRINGFORWARD";
		if (CurrentState == LARGESS)
			return "LARGESS";
		if (CurrentState == GROUPLARGESS)
			return "GROUPLARGESS";
		return "UNDEFINED";
	}

	final public static int PRELOGON = 'L';

	final public static int PRESALE = 'S'; // 销售状态

	final public static int SALE = 's'; // 购买状态

	final public static int FIND = 'F'; // 查询状态

	final public static int MAXCASH = 'B'; // 出款状态

	final public static int CORRECT = 'C';

	final public static int WITHDRAW = 'W'; // 退货状态

	final public static int DISCOUNT = 'd'; // 单价扣项

	final public static int DISCTOTAL = 'Y'; // 打折状态

	final public static int DISCMONEY = 'M'; // 金额折扣

	final public static int LOCK = 'K'; // 加锁/解锁

	final public static int ALTPRICE = 'A'; // 变价状态

	final public static int OPENSHEET = 'O';

	final public static int CLOSESHEET = 'T';

	final public static int HOLD = 'H';

	final public static int ERROR = 'E'; // 出错状态

	final public static int EXIT = 'X'; // 结束状态

	final public static int CASHOUT = 'U';

	final public static int CASHIN = 'I';

	final public static int NETWORKERROR = 'N'; // 网络出错状态

	final public static int FUTRUESELL = 'f'; // 预订单状态

	final public static int BRINGFORWARD = 'r'; //提货状态

	final public static int LARGESS = 'l'; //赠送状态

	final public static int GROUPLARGESS = 'G'; //机团销售状态

	private int CurrentState;

	private int OldState;
}