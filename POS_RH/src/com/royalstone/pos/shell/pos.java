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
 *�ֳ��Ѹ���JDK1.5
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
				JOptionPane.showMessageDialog(null, "POS�����Ѿ����У�");
				System.exit(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "���ش���");
			System.exit(1);
		}
		try {

			System.out.println("javapos start...\n");

			//����ϵͳ�ļ�Ŀ¼
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
			// ����������ϢĿ¼
			prepareDir("largessinfo");

			// ϵͳ�ռ��ļ���д��LOGĿ¼�У�
			redirectOutput();

			// ���ؼ���ӳ���ļ�
			AllKeyEventListener allKeyEventListener = new AllKeyEventListener(
					"winkeymap.xml");

			sh = new PosShell();
			// �����ѻ�����
			sh.synchronizeData();
			// POS����ʼ��
			sh.init();

			//sh.copyCouponInfo(pos.core.getPosContext().getStoreid());

			sh.run();

		} catch (Throwable t) {
			t.printStackTrace();
			generateErrLog();
			JOptionPane.showMessageDialog(null,
					"ϵͳ�������ش���,�뽫�����ϵ�\"RTPOS������Ϣ\"���͵�����֧�ֲ���!");
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
					.getProperty("user.home") + "/����/RTPOS������Ϣ").getBytes(),
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
		//-----����POS��Ӳ������--------------------------
		PosConfig posconfig = PosConfig.getInstance();
		HardWareConfigure hwConfig = new HardWareConfigure(posconfig);
		hwConfig.initHardWareConfig();
		//----------------------------------
		//��ʼ��Ǯ�� ? �ĸ���
		this.cashDrawer = POSCashDrawer.getInstance();

		state = new PosState(PosState.PRELOGON);

		//		Thread t = new Thread(new LoanCardAutoRever());
		//		t.start();

		// �߳� ���˿��Զ����� ��
		Thread t = new Thread(new SHCardAutoRever());
		t.start();

		// �߳� ���˿��Զ����� ��
		Thread coupon = new Thread(new CouponAutoRever());
		coupon.start();

		// �߳� ���ɴ����ռ�
		Thread t_log = new Thread(new LoanLog());
		t_log.start();

		//  ��������
		lock = new Wait4Lock();
		lock.start();
		
		//TODO ����POS�ͻ��˵�ʵʱ���۽����߳� ���ݸ��� by fire 2005_5_11
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

	// ��ȡ�����ŷ���
	private String getForward() {
		PriceInput priceInput = new PriceInput("������������ţ�");
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

	// �ٻ���Ŀ Ԥ����
	private int bringForward() {
		int ID = context.getOrderidOld();
		if (ID != 0) {
			confirm("������һ�ʵ��еڶ���ʹ�������");
			return -1;
		}

		String O_id;
		// ��ȡ��Ļ����
		if ((O_id = getForward()).equals("")) {
			confirm("Ԥ����ʧЧ");
			return -1;
		}
		if (O_id.length() > 12) {
			confirm("Ԥ��������");
			return -1;
		} else {

			// ת��ΪINT��
			int order_id = Integer.parseInt(O_id);
			String order_m = getOrder_m(order_id);
			if (order_m.equals("-1")) {
				confirm("Ԥ���������ڣ����ܽ������ѣ�");
				return -1; //�����״̬
			} else {
				// INT ���String��
				// �޸Ľ��
				String p = getPrice(order_m, O_id);
				if (p == "-1") {
					return -1; //�����״̬
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
		PriceInput priceInput = new PriceInput("������Ԥ������ۣ�", price, id);
		priceInput.show();
		try {
			while (!priceInput.isFinish())
				Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String inputCode = priceInput.getInputcode();
		if (inputCode.equals("")) {
			confirm("����Ϊ��ֵ");
			return "-1";
		}
		if (inputCode.equals("0")) {
			confirm("����Ϊ��ֵ");
			return "-1";
		}
		if (inputCode.equals("0.0")) {
			confirm("����Ϊ��ֵ");
			return "-1";
		}
		double a = Double.parseDouble(price);
		double b = Double.parseDouble(inputCode);
		if (b > a) {
			confirm("�����֧���˳�");
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
			if (!confirm("�����ѻ�����ʧ�ܣ��Ƿ������")) {
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
					// ��½����
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
			// ��ʼԤ����
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

		out.displayState("�������");
		out.clearInputLine();

		do {
			out.prompt("�������,������������������������л����л����ѻ�!");
			boolean reconnect = in.waitReConnect();
			if (reconnect) {
				out.prompt("���ڳ�����������");
				if (RealTime.getInstance().testOnLine()) {
					notice("�����ɹ�!");
					break;
				} else {
					in.clearKey();
					notice("����ʧ��!");
				}
			} else {
				notice("�ɹ�ת��Ϊ�ѻ�������ʽ!");
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
				if (!confirm("���̨����������ʧ�ܣ�����(ȷ��)���ѻ�����(ȡ��)��")) {
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

		out.displayState("���¼");
		out.clear();
		String note = context.getWarning();
		if (note.length() == 0)
			note = "����������Ա��ź�����";
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
		//TODO ���ݸ��� by fire 2005_5_11
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
			System.out.println("��¼�ɹ�......");
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
			core.writelog("��¼", "0", getid);			
			out.print("����Ա:" + op.getId() + " ��¼");
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
				// ״̬�仯
				displaySheet();
				// �������Ͷ���
				displayCoupon();
				

				//                state.setState(PosState.SALE);
			}
			return;
		} else {
			core.writelog("��¼", "1", Integer.parseInt(cashierid));
			context.setWarning(r.getNote());
			state.setState(PosState.PRELOGON);
			logonDialog.getLogonPanel().clear();
		}
	}
	// ��Ʒ��Ϣ���Ƶ���ǰ����
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

	// �д�����������ʣ����
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
			if (confirm("�������ɹ����ӣ���������(ȷ��)���ѻ�����(ȡ��)��")) {
				context.setOnLine(true);
				state.setState(PosState.PRELOGON);
				return;
			}
		}

		initLogonIO();
		out.displayState("���¼");
		out.clear();

		String note = context.getWarning();
		if (note.length() == 0)
			note = "����������Ա��ź�����";

		PosInput inp = in.getInputLogon(note + "(�ѻ�״̬)");
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
		//TODO ���ݸ��� by fire 2005_5_11
		//int work_turn = input.getShiftid();

		OperatorList lst = new OperatorList();
		lst.load("operator.lst");

		Operator op = lst.get(id, pin);
		if (op == null) {
			core.writelog("��¼", "1", 0);
			context.setWarning("�û��������벻��ȷ");
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
			core.writelog("��¼", "0", getid);

			out.print("����Ա:" + op.getId() + " ��¼");
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

	// ����1ΪԤ�����жϵ磻
	// ����0���������¶ϵ磻
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
		
		// ��ӡ��Ա����
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
	 *  out.displayState("������"); PosInput inp =
	 * in.getInputPin("POS��������������������Ա���������"); if (inp == null || !(inp
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
			inp = in.getInputAuthority("��������Ȩ���ܵı�ź�����");
			out.setMainUI(oldMainUI);
			authrization.dispose();

			if (inp == null || !(inp instanceof PosInputLogon)) {
				System.out.println("Canceled!");
				//�޸�
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
			//�޸�
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
		//�����ж��Ƿ���Ҫ��Ȩ
		if (this.needAuth(fun)) {
			if (!getAuthority(author)) {
				context.setWarning("��Ȩʧ��,�����������!");
				//              //�ٻ���ĿԤ���ۣ����Ӳ���
				if (state.getState() == PosState.FUTRUESELL) {
					return false;
				}
				state.setState(PosState.ERROR);
				return false;
			}
		} else if (!cashier.hasPrivilege(fun)) {
			if (!getAuthority(author)) {
				context.setWarning("��Ȩʧ��,�����������!");
				// �ٻ���ĿԤ���ۣ����Ӳ���
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
			context.setWarning("��Ȩʧ��,�����������!");
			state.setState(PosState.ERROR);
		}

		return cashier.hasPrivilege(fun);
	}

	/**
	 * �жϲ���������ʹ�õĹ����Ƿ���Ҫ��Ȩ
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
	 * �жϲ����������Ƿ����ʹ�����ֹ���
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
			System.out.println("����ӪҵԱ���");
			out.dispWaiter("ӪҵԱ:" + waiter_show);
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
	 * �ش���һ������reprintsheet.xml�ļ�������һ���������۽��д�ӡ��ֻ���ش���һ������
	 */
	private void printLastSheet(PosInputPayment payment) {
		prepareDir("reprint");
		boolean fileexist = prepareFile("reprint/reprintsheet.xml");
		if (fileexist == false) {

			core.setWarning("�ش�СƱ�ļ�������,�����������!");
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
	 * ��ӡ�����嵥
	 */
	private void printInvoice() {
		out.printFeed(out.getFeedLines());
		out.cutPaper();
		out.printInvoice();
	}

	/**
	 * �޸����룬ֻ��������״̬�½����޸���Ϊ
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
				core.setWarning("������Ч�����������������ͬ,�����������!");
			if (!pin_new.equals(pin_confirm))
				core.setWarning("������Ч��������������벻һ��,�����������!");
			if (!pin_new.equals(pin_old) && pin_new.equals(pin_confirm)) {
				ClerkAdm adm = new ClerkAdm(server, port);
				Response r = adm.alterPin(posid, cashierid, pin_old, pin_new);
				if (r != null && r.succeed())
					core.setWarning("�����޸ĳɹ�,�����������!");
				else
					core.setWarning("�����޸�ʧ��,�����������!");
			}

			state.setState(PosState.ERROR);
		} else {
			//			core.setWarning("ȡ��");
			//			state.setState(PosState.ERROR);
		}
	}

	private void logoff() {

		int count = context.getHeldCount();
		if (count > 0) {
			context.setWarning("�йҵ������˳�,�����������!");
			core.writelogexit("�˳�", "1", 0);
			state.setState(PosState.ERROR);
		} else if (confirm("�Ƿ��˳�ϵͳ��")) {
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
			core.writelogexit("�˳�", "0", 0);
			System.out.println("�˳��ɹ�......");
			out.print("����Ա:" + context.getCashierid() + " �˳�");
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			context.setWarning("");
			generateLog();
		}

	}

	private void closeWorkTurn() {

		int count = context.getHeldCount();
		if (count > 0) {
			context.setWarning("�йҵ����������,�����������!");
			state.setState(PosState.ERROR);
			return;
		}

		if (context.getShiftid() == 3) {
			if (context.getWorkDate().getGregorian().getTime().compareTo(
					(new Date())) >= 0) {
				context.setWarning("�������ڲ��ܴ���ϵͳ����,�����������!");
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
			confirm.setMessage("��������������ȷʵҪ�������");
		} else {
			confirm = new DialogConfirm(580, 160);
			confirm.setMessage("ϵͳ�����ѻ�״̬�������������ݿ��ܻ�û�ϴ���ȷʵҪ���ѻ������");
		}

		confirm.show();
		if (!confirm.isConfirm())
			return;

		synchronized (pos.uploadLock) {
			JournalManager journalManager = new JournalManager();

			int unloadCount1 = journalManager.getUnuploadCount();
			int unloadCount2 = JournalLogList.getUnuploadCount();

			if (unloadCount1 != unloadCount2) {
				System.out.println("�ѻ���ˮ�ļ���Ŀ����ˮ��־��¼����ˮ����һ��!");
				System.out.println(Formatter.getDateFile(new Date())
						+ " FileCount=" + unloadCount1 + " LogCount="
						+ unloadCount2);
			} else {
				System.out.println(Formatter.getDateFile(new Date())
						+ " FileCount=" + unloadCount1);
			}

			DialogConfirm confirm2 = new DialogConfirm(550, 160);
			while (unloadCount1 > 0) {

				confirm2.setMessage("���� " + unloadCount1
						+ " ���ѻ���ˮû�ϴ������³����ϴ�(ȷ��)��������(ȡ��)��");
				confirm2.setWarning("���������ܻᵼ����Щ�ѻ���ˮ������һ������������У�");
				confirm2.show();

				if (!confirm2.isConfirm()) {
					System.out.println(Formatter.getDateFile(new Date())
							+ " �û�ѡ���ϴ��ѻ���ˮ,�������.");
					break;
				} else {
					System.out.println(Formatter.getDateFile(new Date())
							+ " �û�ѡ������,�����ϴ��ѻ���ˮ.");
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

		// ��ӡ����� ...
		if (PosConfig.getInstance().getString("CLEAR_POS") != null
				&& PosConfig.getInstance().getString("CLEAR_POS").equals("ON")) {
			CashBasket basket = core.getCashBasket();
			out.printFeed(10);
			out.print("�����");
			out.printWithoutSeperator("����Ա:" + context.getCashierid());
			out.printWithoutSeperator("��  ��:"
					+ context.getWorkDate().toString());
			out.printWithoutSeperator("��  ��:"
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

		out.prompt("���ڸ����ѻ����ϡ���");
		pos.reinit();

		DialogInfo notice = new DialogInfo();

		notice.setMessage("���ɹ���");
		notice.show();

		pos.posFrame.exit();
		out.print("���");
		out.print("����Ա:" + context.getCashierid() + " �˳�");
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
				// ��ӡ����� ...
				if (PosConfig.getInstance().getString("CLEAR_POS") != null
						&& PosConfig.getInstance().getString("CLEAR_POS")
								.equals("ON")) {
					CashBasket basket = core.getCashBasket();
					out.printFeed(10);
					out.print("�����");
					out.printWithoutSeperator("����Ա:" + context.getCashierid());
					out.printWithoutSeperator("��  ��:"
							+ context.getWorkDate().toString());
					out.printWithoutSeperator("��  ��:"
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

				notice.setMessage("�ѻ����ɹ���");
				notice.show();

				pos.posFrame.exit();
				out.print("�ѻ����");
				out.print("����Ա:" + context.getCashierid() + " �˳�");
				out.printFeed(out.getFeedLines());
				out.cutPaper();
				state.setState(PosState.PRELOGON);
				context.setWarning("");
				generateLog();
			} else {
				context.setWarning("������ݴ���,�����������!");
				state.setState(PosState.ERROR);
			}
		}

	}

	// ��Ǯ�䷽��
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
		// �������
		out.clear();
		out.displayChange(core.getValue());
		// ���
		context.setLargessCoupon(0);
		//add by lichao
		for (int i = 0; i < core.getPayLen(); i++) {
			Payment p = core.getPayment(i);
			if (p.getType() == 'C' || p.getType() == 'D') {
				//System.out.println("ֻ���ֽ���ߴ���ȯ�Ŵ�Ǯ��");
				cashDrawer.open();
				break;
			}
		}

		for (int i = 0; i < core.getPayLen(); i++) {
			Payment p = core.getPayment(i);
			//�ж��������֧������
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
			// дӲ������
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

		//        System.out.println("�ȽϽ��Ϊ:"+core.CompareLimit());

		if (core.CompareLimit() == 1) {
			DialogInfo notice = new DialogInfo();
			notice.setMessage("�뽫�ֽ���⡣");
			notice.show();
			state.setState(PosState.PRESALE);
		} else if (core.CompareLimit() == 2) {
			DialogInfo notice = new DialogInfo();
			notice.setMessage("�ѳ�����޶�뽫�ֽ���⡣");
			notice.show();
			state.setState(PosState.MAXCASH);
		} else {
			state.setState(PosState.PRESALE);
		}
	}

	private void commitSheets(PosInputPayment input) {
		core.setCurrency("RMB");
		out.display(core.getValue(), context.getExchange());
		// �������
		out.clear();
		out.displayChange(core.getValue());
		//add by lichao
		for (int i = 0; i < core.getPayLen(); i++) {
			Payment p = core.getPayment(i);
			if (p.getType() == 'C' || p.getType() == 'D') {
				//System.out.println("ֻ���ֽ���ߴ���ȯ�Ŵ�Ǯ��");
				cashDrawer.open();
				break;
			}
		}

		//		cashDrawer.open();
//		out.displayTrail(core.getValue());
		out.displayTrail(core.getValue(),core.getPosSheet().isCouponEncash );
		// дӲ������
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

		//System.out.println("�ȽϽ��Ϊ:"+core.CompareLimit());

		if (core.CompareLimit() == 1) {
			DialogInfo notice = new DialogInfo();
			notice.setMessage("�뽫�ֽ���⡣");
			notice.show();
			state.setState(PosState.PRESALE);
		} else if (core.CompareLimit() == 2) {
			DialogInfo notice = new DialogInfo();
			notice.setMessage("�ѳ�����޶�뽫�ֽ���⡣");
			notice.show();
			state.setState(PosState.MAXCASH);
		} else {
			state.setState(PosState.PRESALE);
		}
	}

	private void deletefindresult() {
		out.clear();
		//out.print("ȡ��");
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
	
	//�Ƿ������͵ļ�ֵ
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
		out.print("����ȡ��");
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
		out.print("����ȡ��");
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
			context.setWarning("��Ч����,�����������!");
			return null;
		}

		Payment p = core.closeSheetWithoutMoney(paytype);
		return p;
	}

	private void presale() {
		out.display(context);
		if (context.getGrouplargess()) {
			out.displayState("��������");
		} else {
			out.displayState("����");
		}
		out.prompt("��������Ʒ���������룬�򰴹��ܼ���");
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
		// Ԥ����
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
		
		//IC����ֵ��ֵ
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
				context.setWarning("�������ѻ�״̬���޸����룬�����������!");
				state.setState(PosState.ERROR);
			}
			break;
		case PosFunction.LARGESSCOUPON:
			context.setWarning("δ������Ʒ����ʹ����ȯ���ܣ������������!");
			state.setState(PosState.ERROR);
			break;
		case PosFunction.WAITER:
			showWaiter();
			break;
		case PosFunction.PRINTLASTSHEET:
			//				printLastSheet(inputpayment);
			//				break;
			//�����ش���һ����Ȩ�޿��ƹ���
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
			//����Ǯ�����ܵ�Ȩ��
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
			//				����Ǯ�����ܵ�Ȩ��
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
					//�ڴ������µ��Ǵ�Ǯ������Ǯ��
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
					out.print("�տ���");
					out.printWithoutSeperator("���Ա:" + author.getid());
					out.printWithoutSeperator("����Ա:" + context.getCashierid());
					out.printWithoutSeperator("��  ��:"
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
						notice("�ɹ�ת��Ϊ�ѻ�������ʽ!");
						context.setOnLine(false);
						out.display(context);
						cashier.resetPrivilege();
						context.setAuthorizerid("");
					}
				} catch (UserCancelException e1) {
				}
			} else {
				out.prompt("���ڳ�����������");
				if (RealTime.getInstance().testOnLine()) {
					notice("�����ɹ�!");
					context.setOnLine(true);
					out.display(context);
				} else {
					in.clearKey();
					notice("����ʧ��!");
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
				context.setWarning("����ʱ�����������,�����������!");
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
			//                context.setWarning("��Ч����,�����������!");
			//                state.setState(PosState.ERROR);
			;
		}
	}

	private void printAnyTicket() {
		int ticketNO = 0;
		try {
			ticketNO = Integer.parseInt(this.getInputNo(5, "������СƱ��", "loan"));
		} catch (UserCancelException e) {
			return;
		}
		DecimalFormat df = new DecimalFormat("00000");
		//TODO ���ݸ��� by fire 2005_5_11
		String ticketFile = "journal/" + new Day().toString() + "/"
				+ new Day().toString() + "." + context.getPosid() + "."
				+ df.format(ticketNO) + ".xml";

		boolean fileexist = prepareFile(ticketFile);
		if (fileexist == false) {

			core.setWarning("�ش�����СƱ�ļ�������,�����������!");
			state.setState(PosState.ERROR);
		} else {

			out.printFeed(out.getFeedLines());
			// out.cutPaper();
			out.printAnyTicket(context, ticketFile);
		}

	}
	
	//��ֵ����ֵ
	private void RaCardAuto()
	{
		try {
		if (pos.core.getPosSheet().getSaleLen() != 0) {
			context.setWarning("����Ʒ��ʱ�������ֵ,�����������!");
			state.setState(PosState.ERROR);
			return;
		}
			out.displayState("��ֵ����ֵ");
			out.prompt("�����봢ֵ����ֵ�������ֽ����");

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
						core.writelog("��ֵ", "1", 0);
						state.setState(PosState.ERROR);
					} else {
						String s;
						cashDrawer.open();
						s = (context.getCurrenCode() == "RMB") ? "" : context
								.getCurrenCode();

						s += " " + new Value(pay_info.getValue()).toString();

						notice("�����" + s);
						out.displayHeader(context);
						if(flag==0)
						    out.displayCardRa("��ֵ����ֵ", s,scp.getCardNo(),scp.getAddTotal(),core.getPosSheet().getShopCard());
						else
							out.displayCardRa("***��ֵ����ֵ", s,scp.getCardNo(),scp.getAddTotal(),core.getPosSheet().getShopCard());	
						core.openSheet();
						out.displayWelcome();
						core.writelog("��ֵ����ֵ�ɹ�","1", 0);
						state.setState(PosState.PRESALE);
					}
				} else {
					if (scp.getExceptionInfo() != null) {
						core.writelog("��ֵ����ֵʧ��", "1", 0);
						context.setWarning(scp.getExceptionInfo()+",�����������!");
						state.setState(PosState.ERROR);
//						return cancelInput(scp.getExceptionInfo());
					}
				}
			}else {
				context.setWarning("��Ч����,�����������!");
				state.setState(PosState.ERROR);
			}
		} catch (IOException e) {
			context.setWarning("�����쳣,�����������!");
			state.setState(PosState.ERROR);
		}
	}

	private void encashCoupon() throws IOException, CouponException {

		if (pos.core.getPosSheet().isCouponSale) {
			context.setWarning("��ȯ��ʱ���ܶһ�ȯ,�����������!");
			state.setState(PosState.ERROR);
			return;
		}
		if (pos.core.getPosSheet().getSaleLen() != 0
				&& !pos.core.getPosSheet().isCouponEncash) {
			context.setWarning("����Ʒ��ʱ���ܶһ�ȯ,�����������!");
			state.setState(PosState.ERROR);
			return;
		}

		String cardno = "";
		try {
			cardno = this.getInputNo(18, "������ȯ��", "bank");

		} catch (UserCancelException e) {
			return;
		}

		CouponMgr couponMgr = null;
		try {
			couponMgr = new CouponMgrImpl();
		} catch (Exception e) {
			context.setWarning("ȯ�����ô���,�����������!");
			state.setState(PosState.ERROR);
			return;
		}
		if (cardno.length() != 18) {
			context.setWarning("ȯ�Ŵ���,�����������!");
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
					context.setWarning("��ȯ�ظ�����,�����������!");
					state.setState(PosState.ERROR);
					return;
					//                        }else
					// if(core.getPosSheet().couponSales.getUpdateType().equals("0")){
				} else {
					core.getPosSheet().couponSales.setUpdateType("encash"); //sale
					// ״̬��
					// 4
					// ��Ϊ1
					// ��encash
					// ״̬��
					// 1
					// ��Ϊf
					// 0=����Ӧ��Ʒ 1=��Ӧ��Ʒ
					//if ("0".equals(queryVO.getType())) {
					int couponValue = 0;
					if (queryVO.getPrice() == null
							|| "".equals(queryVO.getPrice()))
						couponValue = 0;
					else
						couponValue = (int) Math.rint(queryVO.getPrice()
								.doubleValue() * 100);
					core.getPosSheet().setCouponValue(couponValue);
					//��ʾ��
					out.getMainUI().dispCoupon(
							queryVO.getCouponID() + queryVO.getCouponPass());
					if (core.getSaleLen() == 0) {
						PosDevOut.getInstance().displayHeader(context);

					}
					Goods g = new Goods("", cardNo, "����ȯ" + cardNo + secrety,
							secrety, "", "��",
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
						context.setWarning("���������ļ�����,�����������!");
						state.setState(PosState.ERROR);
					} catch (RealTimeException e) {
						state.setState(PosState.NETWORKERROR);
					}

				}

				pos.core.getPosSheet().isCouponEncash = true;

			} else {
				context.setWarning("��ȯû�пɶ�����Ʒ,�����������!");
				state.setState(PosState.ERROR);
				}

		}
		if (queryVO != null && queryVO.getExceptionInfo() != null) {
			context.setWarning(queryVO.getExceptionInfo() + ",�����������!");
			state.setState(PosState.ERROR);
		}
		if (queryVO == null) {
			context.setWarning("��ѯ��ȯʱ�������,�����������!");
			state.setState(PosState.ERROR);
		}

	}

	private void printClearTicket() {
		int valueAdd = 0;
		// ��ӡ����� ...
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (PosConfig.getInstance().getString("CLEAR_POS") != null
				&& PosConfig.getInstance().getString("CLEAR_POS").equals("ON")) {
			CashBasket basket = core.getCashBasket();
			//            out.printFeed(3);
			out.print("�����");
			//            out.printWithoutSeperator("���:"+context.getStoreid()+"
			// ����:"+context.getPosid());
			//            out.printWithoutSeperator("����Ա:" + context.getCashierid());
			//            out.printWithoutSeperator(
			//                    "�� ��:" + sdf.format(new Date()));

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
			out.print("���տ��" + (new Value(valueAdd)).toString());
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
			context.setWarning("�������ӡ�����,�����������!");
			state.setState(PosState.ERROR);
		}
	}

	private void maxcash() {
		try {
			out.displayState("����");
			out.prompt("���������������ֽ����");

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
						out.print("�տ���");
						out.printWithoutSeperator("����Ա:"
								+ context.getCashierid());
						out.printWithoutSeperator("��  ��:"
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
					notice("���Ǯ��ȡ�����½�" + s);
					out.displayCash("����", s);
					core.openSheet();
					//// out.displayHeader(context);
					out.displayWelcome();
					if (core.exceedCashMaxLimit()) {
						core.setWarning("�뽫�ֽ����");
						state.setState(PosState.MAXCASH);
					} else {
						state.setState(PosState.PRESALE);
					}
				}
			} else {
				context.setWarning("��Ч����,�����������!");
				state.setState(PosState.MAXCASH);
			}
		} catch (IOException e) {
			state.setState(PosState.ERROR);
		}
	}

	/**
	 * Ԥ��������
	 */
	private void futruesell() {
		Sale s;
		out.display(context);
		out.displayState("Ԥ����");
		out.prompt("����������㣬�򰴹��ܼ�");

		PosInput input = in.getInput();
		// ��ȡ���ܼ�ֵ
		switch (input.key()) {
		case PosFunction.CANCEL:
			JOptionPane.showMessageDialog(null, "��Ԥ����״̬�²�֧�֣��밴ȷ��");
			state.setState(PosState.FUTRUESELL);
			break;
		// �ı�Ԥ�����۸�
		case PosFunction.ALTPRICE:
			orderMoney();
			break;
		case PosFunction.OPENCASHBOX:
			context.setWarning("�ǿյ�״̬�²��ܴ�Ǯ�䣬�����������!");
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
				context.setWarning("֧������������ȡ��,�����������!");
				// �ٻ���ĿԤ���ۣ����Ӳ���
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
							// �ٻ���ĿԤ���ۣ����Ӳ���
							context.setWarning(e.getMessage());
							in.waitCancel(context.getWarning());
							context.setWarning("");
							state.setState(PosState.FUTRUESELL);
							// ----------
						}
						cashier.resetPrivilege();
						context.setAuthorizerid("");
					}
					// �ٻ���ĿԤ���ۣ����Ӳ���
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
			System.out.println("�ҵ� ....");
			hold();
			break;
		case PosFunction.PAYMENT:
			// ������Ʒ��
			sale_rec = null;
			PosInputPayment p = (PosInputPayment) input;
			inputpayment = p; //��֧����Ϣ���ݸ�ȫ�ֱ��������ش�СƱʹ��

			Payment pay_info;
			Payment cardPay = null;
			try {
				//�õ���ǰ����Ա����Ϣ
				if (core.getPosSheet().getMemberCard() != null) {
					PosInputPayment cp = new PosInputPayment(0, 'C', core
							.getPosSheet().getMemberCard().getCardNo());
					cardPay = core.pay(cp);
				}
				pay_info = core.pay(p);

				if (pay_info == null) {
					state.setState(PosState.ERROR);
				} else {

					// ��������ʾ
					out.display(core.getValue(), context.getExchange());

					out.display(pay_info, core.getValue());
					//------------
					if (core.getPosSheet().getShopCard() != null)
						out.display(core.getPosSheet().getShopCard());
					//  out.
					//��ӡ֧����Ϣ��֧�����ͺͽ��
					out.display(core.getPosSheet().getMemberCard()); //��ӡ��Ա����Ϣ�����ź����
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
			context.setWarning("���Ƚ������˳�,�����������!");
			state.setState(PosState.ERROR);
			break;
		default:
			//            	JOptionPane.showMessageDialog(null, "��Ԥ����״̬�²�֧�֣��밴ȷ��");
			state.setState(PosState.FUTRUESELL);
			;
		}
	}

	/**
	 * �������
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

			// �ѽ���ļ۸������ķ�ʽ����PosInputPayment
			PosInputPayment up = new PosInputPayment(ion, Payment.BRINGFORWARD);
			// PosInputPayment p
			inputpayment = up; //��֧����Ϣ���ݸ�ȫ�ֱ��������ش�СƱʹ��

			Payment pay_info;
			Payment cardPay = null;
			try {
				//�õ���ǰ����Ա����Ϣ
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

					// ��������ʾ
					out.display(core.getValue(), context.getExchange());

					out.display(pay_info, core.getValue());
					//------------
					if (core.getPosSheet().getShopCard() != null)
						out.display(core.getPosSheet().getShopCard());
					//out.
					//��ӡ֧����Ϣ��֧�����ͺͽ��
					out.display(core.getPosSheet().getMemberCard()); //��ӡ��Ա����Ϣ�����ź����
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

	// ����Ѱ����Ʒ�����Ʒ��Ӧ��Ĺ�ϵ ��ȡһ�����ض���Ҫ�ж����Ƿ�Ϊ��
	private CouponTypeInfoList largVgno(String largessType) throws IOException {
		HashSet set = new HashSet();
		CouponTypeInfoList couponTypeInfoList = new CouponTypeInfoList();
		// ����Ѱ����Ʒ
		for (int i = 0; i < core.getSaleLen(); i++) {
			if (core.getSale(i).getDiscType() == Discount.NONE
					|| core.getSale(i).getDiscType() == Discount.VIPPROMPROM
					|| core.getSale(i).getDiscType() == Discount.VIPPROM) {
				if (!core.getSale(i).getGoods().getVgno().equals("000000")){
					set.add(core.getSale(i).getGoods().getVgno());
					}
			}
		}
		// �����г���ִ�еķ���
		if (set.size() > 0) {
			// ����һ���������о��Ӧ��Ϣ�Ķ���
			couponTypeInfoList = vgnoTOtome(set,largessType);
			// �Ƴ����пյľ�Ķ�Ӧ��Ϣ��Ӧ�Թ�ϵ����Excetpon��Ϣ�����ڣ�˵������Ч�Ķ�Ӧ��Ϣ
			if (couponTypeInfoList != null) {
				int Count = couponTypeInfoList.size();
				for (int i = 0; i < Count; i++) {
					couponTypeInfoList.rmoveNull();
				}
			}
			// �ٴα�����Ʒ������Ʒ���ֵľ�����Ϣ
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
		int coupon_list = 0; // ��Ʒ���ۣ���ʣ���Դ��ۣ�Ҫ��ʵ���������ۣ���ʵ�����ʱ��Ҫ��ȥ�� 
		int coupon_count = 0; //������Ʒ�����ۺ�ʣ����Ʒ������������
		// ��ȡ�����Ϣ
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
				// ת��һ��������ͣ���þ������
				if (couponTypeInfoList.size() > 0) {
					coupon = couponTypeInfoList.getCouponCounnt(largess.substring(0, 4));
				}
				CouponLargess couponLargess = getLaressInfo(largess.substring(
						0, 4), String.valueOf(loupou + coupon), context
						.getStoreid() , largessType);
				// ����20061129 ���������� �ж�����
				if (couponLargess.getFlag()) {
					// Ϊ��\������������ ����
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
					// �˷����Ľ�������ͬ�����ͺϲ� ����֤
					if (core.couponLargessList.add(couponLargess)) {
						value += performDiscountCoupon(largess.substring(0, 4),
								couponLargess, couponTypeInfoList, coupon,
								false, largessType,true);
					}
				}
					} // ��β��
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
						// Ϊ��\������������ ����
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
//						 �˷����Ľ�������ͬ�����ͺϲ� ����֤
						if (core.couponLargessList.add(couponLargess)) {
							// TUREֵ�������ü���ʵ�ʾ�
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

			String name = "������Ʒ�ۿ�";
			// ���͵�ʱ���ۿ�������A
			if (largessType.equals("0")){
				core.sell(Sale.AlTPRICE, name, value);
				}
			// ������ʱ���ۿ�������B
			if (largessType.equals("1")){
				core.sell(Sale.AlTPRICE, name, value);
				}

			SheetValue tmpSheetValue = new SheetValue();
			
			tmpSheetValue.setValue(value, 0, 0, 0);
			out.display(core.getFalseSale(core.getFalseSaleLen() - 1),
					tmpSheetValue);
			out.display(core.getValue(), context.getExchange());
			core.dump();
			// ����������̵Ķ����ⶨXML�ļ�����ڵ���largessinfoĿ¼��
			core.performCouponLargessList(core.couponLargessList);
			context.setCounp(true);
		}else{
			context.setWarning("û�д�������Ʒ�����������");
			state.setState(PosState.ERROR);
			}
	}

	// ������Ʒ�б�����Ʒ�ж�Ӧ����Ϣ
	private CouponTypeInfoList vgnoTOtome(HashSet set , String largessType) throws IOException {

		ClerkAdm ad = new ClerkAdm(context.getServerip(), context.getPort());
		CouponTypeInfoList response = ad.getVgnoCouponType(set , largessType);

		return response;

	}

	// ��ȡ��������ۿ���Ϣ
	private CouponLargess getLaressInfo(String Largess, String count,
			String sheepID , String largessType) throws IOException, CouponException {
		CouponMgr couponMgr = null;
		try {
			couponMgr = new CouponMgrImpl();
		} catch (Exception e) {
			context.setWarning("ȯ�����ô���,�����������!");
			return null;
		}
		
		CouponLargess queryVO = null;

		try {
			queryVO = couponMgr.getCouponInfo(Largess, count, sheepID, largessType);
		} catch (IOException ex) {
			
//			if (!ex.getMessage().equals("û�д����͵Ĵ���")) {
				throw ex;

			}

//		}
		
		if (queryVO == null){
			queryVO.setExceptionInfo("û�д����͵Ĵ���");
			return queryVO;
			}
		
		if (largessType.equals("1")){
			if (queryVO.getAddPrice() <= 0){
				queryVO.setExceptionInfo("û�д����͵Ĵ���");
				return queryVO;
				}
			}

		return queryVO;
	}
	
	
	// largess ������
	// couponLargess ��Ӧ��������Ϣ
	// couponTypeInfoList ��Ʒ��Ӧ�������б�
	// couponCount ��Ʒ�ܶһ��ľ�����
	// flag���Ƴ���־λ
	private int performDiscountCoupon(String largess,CouponLargess couponLargess, CouponTypeInfoList couponTypeInfoList,int couponCount, boolean Type, String largessType, boolean flag) {
		int Coun = 0;
		salelst = core.getPosSheet().getSalelst();
		int totalValue = salelst.getTotalValue();
		Vector vnums = salelst.getNumsWithoutDisc();
		// ���۽��
		int value = 0;
		// �ܹ����۵���Ʒ����
		int VgnoCount = 0;
		// һ�ž����Ʒ����
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
							// ʹ���ۿۼ�
//							DiscRate discrate;
							DiscPrice discPrice;
							// ����Ϊ0
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
							
							// zhouzhou 20060811 ���ž�һ��Զ����Ʒ�Ľ������
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
								
								// ����Ϊ0
								if (largessType.equals("0")){
									
//								 discrates = new DiscRate(Discount.LARGESS,
//										100 - (int) disc);
								 
//								 zhouzhou 20060811 ���ž�һ��Զ����Ʒ�Ľ������
//								 discPrices = new DiscPrice(Discount.LARGESS, disc);
								 discPrices = new DiscPrice(Discount.LARGESS,onePrice);
								}
								
								else{
//									 discrates = new DiscRate(Discount.LARGESSL,
//											100 - (int) disc);
//									 zhouzhou 20060811 ���ž�һ��Զ����Ʒ�Ľ������	
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
											// 20060815 ֧��һ��Զ�����Ʒ ���һ��һ�������������
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
//											 zhouzhou ֧�־�Զ����Ʒ������������һ����Ʒ�ۿۼ�
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
						//                    String name = "�ܶ��ۿ�";
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
				//�ٻ���ĿԤ���ۣ����Ӳ���
				if (state.getState() == PosState.FUTRUESELL) {
					return 0;
				}
				// ------------------------
				state.setState(PosState.SALE);

				// �ٻ���ĿԤ���ۣ����Ӳ���
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
		out.displayState("��ѯ");
		out.prompt("������Ҫ��ѯ��Ʒ���������룬�򰴹��ܼ���");

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
			out.displayState("��������");
		} else if(context.getLargessCoupon() == 1){
			out.displayState("��ȯ");
		} else {
			out.displayState("����");
		}
		out.prompt("��������Ʒ���������룬�򰴹��ܼ���");

		PosInput input = in.getInput();
		
//		NotifyChangePriceConsumer.getInstance().consume();
		switch (input.key()) {

		case PosFunction.CANCEL:
			break;
		case PosFunction.GROUPLARGESS:
			//context.setGrouplargess(true);
			break;
		case PosFunction.ALTPRICE:
			//���ӱ�۵Ŀ���Ȩ��
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
				confirm("�������ѻ�״̬��ʹ�������");
			}
			break;
			
		// ��ȯ ....  �ٻ�����ҵ�� ���������ϲ�ͬ����Ʒ����ѻ�ȡһ����Ʒ��ȯ��
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
				if(confirm("�Ƿ�ȡ����ȯ?")){
					context.setLargessCoupon(0);
					break;
				}else{
					context.setLargessCoupon(1);
					break;
					}
				}

			// �������ʹ���
		case PosFunction.LARGESS:
			if (confirm(PosFunction.toString(input.key()) + "?")) {
				if (context.getCounp()) {
					confirm("�Բ��𣬲�����ͬһ�ʵ��а�����");
					break;
				}
				if (!context.getGrouplargess()) {
					try {
						// LargessType ����Ϊ0 ������  1 ��Ϊ����
						String largessType = "0";
						largess(largessType);
					} catch (IOException ex) {
						context.setWarning("����ʱ����������ϣ������������!");
						state.setState(PosState.ERROR);
					} catch (CouponException e) {
						e.printStackTrace();
						context.setWarning(e.getMessage());
						state.setState(PosState.ERROR);
					}
				}
			}
			break;
			
			// ������������
		case PosFunction.LARGESSL:
			if (confirm(PosFunction.toString(input.key()) + "?")){
				if (context.getCounp()){
					confirm("�Բ��𣬲�������һ�ʵ��а�����");
					break;
					}
				if (!context.getGrouplargess()){
					try{
						//LargessType ����Ϊ0 ������  1 ��Ϊ����
						String largessType = "1";
						largess(largessType);
						}catch(IOException ex){
							context.setWarning("����ʱ����������ϣ������������");
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
			//���ӵ����ۿ۵Ŀ���Ȩ��
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
			//�����ܶ��ۿ۵Ŀ���Ȩ��
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
			//���ӽ���ۿ۵Ŀ���Ȩ��
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
			context.setWarning("�ǿյ�״̬�²��ܴ�Ǯ�䣬�����������!");
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
				context.setWarning("����ʱ���ܼ���,�밴�����������");
				state.setState(PosState.ERROR);
				}

			break;
			
		case PosFunction.CARDCHANGE:
			if(core.getPosSheet().getMemberCard() != null) {
				if (core.getPosSheet().getCardChange() == null){
			// ������ֶһ�
			if (!context.getCounp()){
				MemberCardChange change = new MemberCardChange();
				String vgno = null;
				//����֮ǰ��֧�����
				String vaiter = String.valueOf(core.getPosSheet().getValue().getValueToPay() / 100.0);
				//������������ѯ�Ƿ�����㹻
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
						
						// ����ɹ�������һ��������Ϣ��������Ϣ����
						sale.setFactPrice(Discount.Change, (int) Math
							.rint(100 * 0));
					}
					core.updateValue();
//					out.displayDiscount(sale_rec, core.getValue());
//					out.display(core.getValue(), context.getExchange());
					cashier.resetPrivilege();
					// ����Ա
					context.setAuthorizerid(core.getPosContext().getCashierid());
					authorizeCashier = null;
					try {
						String name = "�һ�";
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
					context.setWarning("����ʱ����ʹ�û��ֶһ����밴���������!");
					state.setState(PosState.ERROR);
					}
			}else{
				context.setWarning("�Ѿ��һ�һ�Σ��밴���������!");
				state.setState(PosState.ERROR);
				}
			}else{
				context.setWarning("δ��ˢ��Ա�����밴���������!");
				state.setState(PosState.ERROR);
				}
			break;
			
		case PosFunction.CORRECT:
			if(!context.getCounp()){
			try {
				// ���Ӹ�����Ȩ�޿���
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
					context.setWarning("û�и�����Ȩ��,�밴�����������");
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
				context.setWarning("����ʱ���ܸ���,�밴�����������");
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
				context.setWarning("֧������������ȡ��,�����������!");
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
					context.setWarning("��Ա�����ֶһ���������������Ʒ��");
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
			System.out.println("�ҵ� ...");
		if(!context.getCounp()){
			hold();
			}else{
				context.setWarning("����ʱ���ܹҵ�,�밴�����������");
				state.setState(PosState.ERROR);
				}
			break;
			
		
		case PosFunction.PAYMENT:
			if (getPayt()){
				if (getPay()){
			// ������Ʒ��
			sale_rec = null;
			PosInputPayment p = (PosInputPayment) input;
			inputpayment = p; //��֧����Ϣ���ݸ�ȫ�ֱ��������ش�СƱʹ��

			Payment pay_info;
			Payment cardPay = null;
			Payment plan=null;
			try {
				//�õ���ǰ����Ա����Ϣ
//				if (core.getPosSheet().getMemberCard() != null) {
//					PosInputPayment cp = new PosInputPayment(0, 'C', core
//							.getPosSheet().getMemberCard().getCardNo());
//					cardPay = core.pay(cp);
//				}
				pay_info = core.pay(p); 

				if (pay_info == null) {
					state.setState(PosState.ERROR);
				} else {
					// ��������ʾ
					out.display(core.getValue(), context.getExchange());

					out.display(pay_info, core.getValue());
					if (core.getPosSheet().getShopCard() != null)
						out.display(core.getPosSheet().getShopCard());
					if (core.getValue().getValueToPay() <= 0) {

						//�����Ա��������
						try {
								// ��ȯ�һ���ʱ�򣬲��ܻ��֡�
							if (core.getPosSheet().getMemberCard() != null && core.getPosSheet().getCardChange() == null)
							{
								long values=0;
								values = (long)core.getPaylst().getCouponPay();
								//��ȡȯ֧����Ϣ
								core.getPosSheet().handlePoint(values);
							}
						} catch (RealTimeException e) {
							PosContext.getInstance().setWarning("��Ա������ʧ�ܣ���������������Ի�ȡ��!");
							state.setState(PosState.ERROR);
							return;
						} catch (IOException e) {
							PosContext.getInstance().setWarning("��Ա��������ʧ�ܣ���������������Ի�ȡ��!");
							state.setState(PosState.ERROR);
							return;
						}
						//��ӡ֧����Ϣ��֧�����ͺͽ��
						out.display(core.getPosSheet().getMemberCard()); //��ӡ��Ա����Ϣ�����ź����
			
						state.setState(PosState.CLOSESHEET);
						//�õ���ǰ����Ա����Ϣ`������Ϣ���ڽ���� 
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
								context.setWarning("���ֶһ������쳣,�����������!");
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
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			}
			else{
					context.setWarning("����δ������ľ�,�����������!");
					state.setState(PosState.SALE);
			}
				}else 
				{
					context.setWarning("����δ������ϵ���Ʒ,�����������!");
				state.setState(PosState.SALE);
					}
			break;
		case PosFunction.Coupon:
			try {
				encashCoupon();
			} catch (IOException ex) {
				context.setWarning("����ʱ�����������,�����������!");
				state.setState(PosState.ERROR);
			} catch (CouponException e) {
				e.printStackTrace();
				context.setWarning(e.getMessage());
				state.setState(PosState.ERROR);
			}
			break;
		case PosFunction.PROPERTY:
			//����ǰ������Լ�
			core.property();
			break;
		case PosFunction.EXIT:
			context.setWarning("���Ƚ������˳�,�����������!");
			state.setState(PosState.ERROR);
			break;
		default:
			;
		}
	}
	
	/*
	 * ��ӡ��Ʒ����������
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
			//������˻��򲻴�ӡ
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

	
	//�Ƿ��������
	private boolean getPayt(){
		if (getAddPrice()){
			if (confirm(("����δ���͵���Ʒ,ȷ��֧��" + "?"))){
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
	
	// �Ƿ������Ϣ
	private boolean getPay(){
		if (core.getPosSheet().isCouponEncash){
		if (core.getPosSheet().couponSales.size() > 0){
		int value = core.getPosSheet().getValue().getValueTotal()
			- core.getPosSheet().couponSales.getTotalValue();
		if (value  < 0){
		if (confirm(("����δ������ľ�ȷ������" +  "?"))){
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
				 * @param Vgno ��Ʒ���� @param Barcode ��Ʒ���� @param GoodsName ��Ʒ����
				 * @param Deptid ��ƷС�� @param Spec ��� @param UnitName ���۵�λ @param
				 * GoodsPrice ��Ʒ�ۼ� @param gtype ��Ʒ���� @param x4Price �۸����� @param
				 * ptype �ۿ�/��������
				 */
				goods = new Goods("999999", "111111", "Ԥ������Ʒ", "000000", "",
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
					System.out.println("basepriceΪ:" + baseprice);
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
						// �ٻ�Ԥ�������ӶԵ�ǰ״̬�Ķ϶�
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
	 * ��Ȩ����ɵ�����¶Ե�ǰ��Ʒ���б�۴������Դﵽ����Ʒ�ۿ۵Ĺ���
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
					out.displayState("�ļ۸�");
					out.prompt("��������Ʒ�ۼ�");
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
								.setWarning("��۳�����Χ,��ͱ�۵�"
										+ (new Value((int) Math
												.rint(alterPrice * 100)))
												.toString() + "�������������!");

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
						String name = "���";
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
			context.setWarning("��Ч����,�����������!");
			state.setState(PosState.ERROR);
		}
	}

	// �ٻ���Ԥ�����޼۸�
	private void orderMoney() {
		if (sale_rec != null
				&& (sale_rec.getType() == Sale.SALE || sale_rec.getType() == Sale.WITHDRAW)) {
			try {
				if (checkPrivilege(PosFunction.ALTPRICE)) {
					double price_org = ((double) sale_rec.getGoods().getPrice() / 100.0);
					double price_new;
					if (state.getState() == PosState.FUTRUESELL) {
						out.displayState("��Ԥ����");
						out.prompt("������Ԥ�����");
						price_new = in.getDouble(6, 2);
					} else {
						out.displayState("�ļ۸�");
						out.prompt("��������");
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
								.setWarning("�Բ��𣬽�����"
										+ (new Value((int) Math
												.rint(alterPrice * 100)))
												.toString() + "������!");

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
						String name = "���";
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
			context.setWarning("��Ч����,�����������!");
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
						out.displayState("�����ۿ�");
						out.prompt("�������ۿ��ʣ���:�������,����80; ����������,����75");
						disc = in.getDouble(6, 2);
						if (new Double(disc).intValue() == 0) {
							context.setWarning("�ۿ۲���Ϊ��,�����������!");
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
				context.setWarning("��ǰ��Ʒ�Ѿ��ۿ�,���ܽ��е����ۿ�,�����������!");
				state.setState(PosState.ERROR);
			}

		} else {
			context.setWarning("��Ч����,�����������!");
			state.setState(PosState.ERROR);
		}

	}

	/**
	 * ��Ȩ����ɵ�����¶Ե�ǰû���ۿ۵���Ʒ���е����ۿ۴���
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
					out.displayState("�����ۿ�");
					out.prompt("�������ۿ��ʣ���:�������,����80; ����������,����75");
					disc = in.getDouble(6, 2);
					int idisc = new Double(disc).intValue();
					if (idisc > 100) {
						context.setWarning("�����ۿ۲��ܴ���100%,�����������!");
						// �ٻ���ĿԤ���� ���Ӳ���
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
						context.setWarning("���ۿ۳���������ۿۣ�����ۿ�"
								+ (100 - authorizeCashier.getMax_Disc())
								+ "%,�����������!");
						// �ٻ���ĿԤ���ۣ����Ӳ���
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
						String name = "�����ۿ�";
						core.sell(Sale.AlTPRICE, name, value);
						core.dump();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* end */
					// �ٻ���ĿԤ����,���Ӳ���
					if (state.getState() == PosState.FUTRUESELL) {
						state.setState(PosState.FUTRUESELL);
						return;
					}
					// ------------------
					state.setState(PosState.SALE);
				} else {
					// �ٻ���ĿԤ���ۣ����Ӳ���
					if (state.getState() == PosState.FUTRUESELL) {
						in.waitCancel(context.getWarning());
						context.setWarning("");
						return;
					}
					// -----------------------
					state.setState(PosState.ERROR);
				}
			} catch (UserCancelException e) {
				// �ٻ���ĿԤ���ۣ����Ӳ���
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
			//                context.setWarning("��ǰ��Ʒ�Ѿ��ۿ�,���ܽ��е����ۿ�,�����������!");
			//                state.setState(PosState.ERROR);
			//            }

		} else {
			context.setWarning("��Ч����,�����������!");
			// �ٻ���ĿԤ���ۣ����Ӳ���
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
	 * @return boolean��true ��ʾ��ǰ���Ѿ������ۿۣ������ٴβ����κ��ۿ�ҵ�񣬷�֮�����ԡ�
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
	 * ��Ȩ����ɵ�����¶Ե�ǰ��û���ۿ۹�����Ʒ�����ܶ��ۿ۴��� �����ǰ����Ʒ�����й��ۿۣ���ǰ����Ʒ�����ٴβ����ܶ��ۿ�ҵ��
	 * ���򣬶Ե�ǰ��������û���ۿ۵���Ʒ�����ܶ��ۿ�
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
					//                        context.setWarning("��ǰ����Ʒ�Ѿ��ۿ�,���ܽ����ܶ��ۿ�,�����������!");
					//                        state.setState(PosState.ERROR);
					//                        return;
					//                    }
					if (false) {
						return;
					} else {
						out.displayState("�ܶ��ۿ�");
						out.prompt("�������ۿ��ʣ���:�������,����80; ����������,����75");
						disc = in.getDouble(6, 2);

						if (disc > 100) {
							context.setWarning("�ܶ��ۿ۲��ܳ���100%,�����������!");
							// �ٻ���ĿԤ���ۣ����Ӳ���
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
							context.setWarning("�ܶ��ۿ۳�������ۿۣ�����ۿ۵�"
									+ (100 - authorizeCashier.getMax_Disc())
									+ "%,�����������!");
							// �ٻ���ĿԤ���ۣ����Ӳ���
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
						String name = "�ܶ��ۿ�";
						core.sell(Sale.AlTPRICE, name, value);
						core.dump();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* end */
					//�ٻ���ĿԤ���ۣ����Ӳ���
					if (state.getState() == PosState.FUTRUESELL) {
						return;
					}
					// ------------------------
					state.setState(PosState.SALE);
				} else {
					// �ٻ���ĿԤ���ۣ����Ӳ���
					if (state.getState() == PosState.FUTRUESELL) {
						in.waitCancel(context.getWarning());
						context.setWarning("");
						return;
					}
					// -------------------------
					state.setState(PosState.ERROR);
				}
			} catch (UserCancelException e) {
				// �ٻ���ĿԤ���ۣ����Ӳ���
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
			context.setWarning("��Ч����,�����������!");
			// �ٻ���ĿԤ���ۣ����Ӳ���
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
					//                        context.setWarning("��ǰ����Ʒ�Ѿ��ۿ�,���ܽ����ܶ��ۿ�,�����������!");
					//                        state.setState(PosState.ERROR);
					//                        return;
					//                    }
					if (false) {
						return;
					} else {
						out.displayState("�����ܶ��ۿ�");
						out.prompt("�������ۿ��ʣ���:�������,����80; ����������,����75");
						disc = in.getDouble(6, 2);

						if (disc > 100) {
							context.setWarning("�ܶ��ۿ۲��ܳ���100%,�����������!");
							// �ٻ���ĿԤ���ۣ����Ӳ���
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
										.setWarning("�ܶ��ۿ۳�������ۿۣ�����ۿ۵�"
												+ (100 - authorizeCashier
														.getMax_Disc())
												+ "%,�����������!");
								// �ٻ���ĿԤ���ۣ����Ӳ���
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
						String name = "�ܶ��ۿ�";
						core.sell(Sale.AlTPRICE, name, value);
						core.dump();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* end */
					//�ٻ���ĿԤ���ۣ����Ӳ���
					if (state.getState() == PosState.FUTRUESELL) {
						return;
					}
					// ------------------------
					state.setState(PosState.SALE);
				} else {
					// �ٻ���ĿԤ���ۣ����Ӳ���
					if (state.getState() == PosState.FUTRUESELL) {
						in.waitCancel(context.getWarning());
						context.setWarning("");
						return;
					}
					// -------------------------
					state.setState(PosState.ERROR);
				}
			} catch (UserCancelException e) {
				// �ٻ���ĿԤ���ۣ����Ӳ���
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
			context.setWarning("��Ч����,�����������!");
			// �ٻ���ĿԤ���ۣ����Ӳ���
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
						context.setWarning("��ǰ����Ʒ�Ѿ��ۿ�,���ܽ����ܶ��ۿ�,�����������!");
						state.setState(PosState.ERROR);
						return;
					} else {
						out.displayState("�ܶ��ۿ�");
						out.prompt("�������ۿ��ʣ���:�������,����80; ����������,����75");
						disc = in.getDouble(6, 2);
						if (new Double(disc).intValue() == 0) {
							context.setWarning("�ۿ۲���Ϊ��,�����������!");
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
			context.setWarning("��Ч����,�����������!");
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
	 * ��Ȩ����ɵ�����¶Ե�ǰ��û���ۿ۹�����Ʒ���н���ۿ۴��� �����ǰ����Ʒ�����й��ۿۣ���ǰ����Ʒ�����ٴβ������ۿ�ҵ��
	 * ���򣬶Ե�ǰ��������û���ۿ۵���Ʒ���н���ۿ�
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
					//                        context.setWarning("��ǰ����Ʒ�Ѿ��ۿ�,���ܽ��н���ۿ�,�����������!");
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
						out.displayState("����ۿ�");
						out.prompt("�������ۺ���");
						disc = in.getDouble(6, 2);

						if (Math.rint(disc * 100) > unPaid) {
							context.setWarning("�ۿۺ���ܴ���Ӧ�տ�,�����������!");
							// �ٻ���ĿԤ���ۣ����Ӳ���
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

						//û�д��۵���Ʒ����Ҫ���۵Ľ��
						double totalDisc = 0.0;
						totalDisc = total - disc * 100.0;
						//����Ա����ܴ�Ľ��
						double discAvailable = 0.0;
						if (authorizeCashier != null) {
							discAvailable = totalValue
									* authorizeCashier.getMax_Disc() / 100.0;
						}

						if (totalDisc > discAvailable) {
							context.setWarning("�ۿ۽�������ۿ۽��,��ͱ��"
									+ (new Value((int) Math
											.rint((total - discAvailable))))
											.toString() + ",�����������!");
							// �ٻ���ĿԤ���ۣ����Ӳ���
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
							String name = "����ۿ�";
							core.sell(Sale.AlTPRICE, name, value);
							core.dump();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						/* end *///�ٻ���ĿԤ���ۣ����Ӳ���
						if (state.getState() == PosState.FUTRUESELL) {
							state.setState(PosState.FUTRUESELL);
							return;
						}
						// ------------------------------
						state.setState(PosState.SALE);
					}
				} else {
					// �ٻ���ĿԤ���ۣ����Ӳ���
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
				//�ٻ�Ԥ�����ۣ����Ӳ���
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
			context.setWarning("��Ч����,�����������!");
			// �ٻ���ĿԤ���ۣ����Ӳ���
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
						context.setWarning("��ǰ����Ʒ�Ѿ��ۿ�,���ܽ��н���ۿ�,�����������!");
						state.setState(PosState.ERROR);
						return;
					} else {
						int temp = 0;
						out.displayState("����ۿ�");
						out.prompt("�������ۺ���");
						disc = in.getDouble(6, 2);
						if (new Double(disc).intValue() == 0) {
							context.setWarning("�ۿ۲���Ϊ��,�����������!");
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
			context.setWarning("��Ч����,�����������!");
			state.setState(PosState.ERROR);
		}

	}

	/**
	 * @param
	 * @return
	 * @deprecated
	 */

	private void correct() {
		out.displayState("����");
		out.prompt("��������Ʒ���������룬�������.");
		PosInput input = in.getInput();
		switch (input.key()) {
		case PosFunction.CANCEL:
			core.writelog("����", "1", 0);
			state.setState(PosState.SALE);
			break;

		case PosFunction.CORRECT:
			core.writelog("����", "1", 0);
			state.setState(PosState.CORRECT);
			break;

		case PosFunction.WITHDRAW:
			core.writelog("����", "1", 0);
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
							core.writelog("����", "1", 0);
							System.out.println("�����������ɹ�.");
							state.setState(PosState.ERROR);
						} else {
							core.writelog("����", "0", 0);
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
			core.writelog("����", "1", 0);
			out.prompt("��Ч����,�����������!");
			in.waitCancel();
			state.setState(PosState.SALE);
		}
		cashier.resetPrivilege();
		context.setAuthorizerid("");
	}

	private void withdraw() {
		out.displayState("�˻�");
		out.prompt("��������Ʒ���������룬�򰴷�����.");
		PosInput input = in.getInput();
		switch (input.key()) {

		case PosFunction.EXIT:
		case PosFunction.CANCEL:
			core.writelog("�˻�", "1", 0);
			state.setState(state.getOldState());
			break;

		//			case PosFunction.CORRECT :
		//				try {
		//					if (checkPrivilege(PosFunction.CORRECT)) {
		//						core.writelog("�˻�", "1", 0);
		//						state.setState(PosState.CORRECT);
		//					} else {
		//						core.writelog("�˻�", "1", 0);
		//						state.setState(PosState.ERROR);
		//					}
		//				} catch (UserCancelException e2) {
		//					// do nothing
		//				}
		//
		//				break;

		case PosFunction.WITHDRAW:
			core.writelog("�˻�", "1", 0);
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
				 * �ذ��������봦��
				 * 
				 * @param goods
				 *            ������Ʒ
				 * @param goods.getDeptid()
				 *            ��ƷС�࣬����Goods.LOADOMETER��ʾ�ذ�
				 */
				/*
				 * �ٻ� 2005��11��21�� if (goods != null && goods.getDeptid() != null &&
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
							core.writelog("�˻�", "1", 0);
							state.setState(PosState.ERROR);
						} else {
							core.writelog("�˻�", "0", 0);
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
			core.writelog("�˻�", "0", 0);
			out.prompt("��Ч����,�����������!");
			in.waitCancel();
			state.setState(PosState.SALE);
		}
		cashier.resetPrivilege();
		context.setAuthorizerid("");
	}

	public void unhold() {
		int count = context.getHeldCount();
		if (count < 1) {
			core.writelog("��ҵ�", "1", 0);
			context.setWarning("���ܹҿյ�,�����������!");
			state.setState(PosState.ERROR);
			return;
		} else if (count == 1) {
			core.unholdFirst();
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			out.print("��ҵ�: " + Formatter.getTime(new Date()));
			out.displayHeader(context);
			//��ʾ��ҵ���Ա����Ϣ
			if (core.getPosSheet().getMemberCard() != null) {
				out.dispMemberCardHeader(core.getPosSheet().getMemberCard());
			}
			if (core.getPosSheet().getCouponNO() != null
					&& !core.getPosSheet().getCouponNO().equals(""))
				out.dispCouponHeader(core.getPosSheet().getCouponNO());
			displaySheet();
			core.writelog("��ҵ�", "0", 0);
			return;
		}

		HoldList holdList = new HoldList(core.getSheetBrief());
		holdList.show();

		if (holdList.isConfrim()) {
			core.unholdSheet(holdList.getHoldNo());
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			out.print("��ҵ�: " + Formatter.getTime(new Date()));
			out.displayHeader(context);
			//��ʾ��ҵ���Ա����Ϣ
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
			context.setWarning("�һ�ȯʱ������ҵ�,�����������!");
			// ��ҵ��ĿԤ���ۣ����Ӳ���
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
			context.setWarning("��ȯʱ������ҵ�,�����������!");
			// ��ҵ��ĿԤ���ۣ����Ӳ���
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
			core.writelog("�ҵ�", "1", 0);
			context.setWarning("��֧��������ҵ�,�����������!");
			// ��ҵ��ĿԤ���ۣ����Ӳ���
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
			core.writelog("�ҵ�", "1", 0);
			context.setWarning("�ҵ����Ѿ��ﵽ����,�����������!");
			// ��ҵ��ĿԤ���ۣ����Ӳ���
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
			core.writelog("�ҵ�", "0", 0);
			int n = core.holdSheet();
			state.setState(PosState.PRESALE);
			out.clear();
			out.print("�ҵ���δ���: " + Formatter.getTime(new Date()));
			out.printFeed(out.getFeedLines());
			out.cutPaper();
			//// out.displayHeader(context);
		}
	}

	/**
	 * �ϼƹ���
	 * 
	 * @deprecated
	 */
	public void showTotal() {
		try {
			out.disptotal(core.getValue());
			SheetValue v = core.getValue();

			int value = v.getValueTotal();
			String name = "�ϼ�";
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
			out.displayState("���");
			out.prompt("���������������ֽ����");

			PosInput input = in.getInput();
			if (input.key() == PosFunction.CANCEL) {
				core.writelog("���", "1", 0);
				state.setState(PosState.PRESALE);
			} else if (input.key() == PosFunction.PAYMENT
					&& ((PosInputPayment) input).getCents() > 0) {
				PosInputPayment p = (PosInputPayment) input;

				Payment pay_info = core.cashin(p);
				if (pay_info == null) {
					core.writelog("���", "1", 0);
					state.setState(PosState.ERROR);
				} else {
					cashDrawer.open();
					String s = pay_info.getCardno() + " "
							+ new Value(pay_info.getValue()).toString();
					notice("�뽫���½�����Ǯ�䣺" + s);
					out.displayHeader(context);
					out.displayCash("���", s);
					core.openSheet();
					//// out.displayHeader(context);
					out.displayWelcome();
					core.writelog("���", "0", 0);
					state.setState(PosState.PRESALE);
				}
			} else {
				core.writelog("���", "1", 0);
				context.setWarning("��Ч����,�����������!");
				state.setState(PosState.ERROR);
			}
		} catch (IOException e) {
			state.setState(PosState.ERROR);
		}

	}

	public void cashout() {
		try {
			out.displayState("����");
			out.prompt("���������������ֽ����");

			PosInput input = in.getInput();
			if (input.key() == PosFunction.CANCEL) {
				core.writelog("����", "1", 0);
				state.setState(PosState.PRESALE);
			} else if (input.key() == PosFunction.PAYMENT
					&& ((PosInputPayment) input).getCents() > 0) {
				PosInputPayment p = (PosInputPayment) input;
				Payment pay_info = core.cashout(p);
				if (pay_info == null) {
					core.writelog("����", "1", 0);
					state.setState(PosState.ERROR);
				} else {
					String s;
					cashDrawer.open();
					s = (context.getCurrenCode() == "RMB") ? "" : context
							.getCurrenCode();

					s += " " + new Value(-pay_info.getValue()).toString();

					notice("���Ǯ��ȡ�����½�" + s);
					out.displayHeader(context);
					out.displayCash("����", s);
					core.openSheet();
					//  out.displayHeader(context);
					out.displayWelcome();
					core.writelog("����", "0", 0);
					state.setState(PosState.PRESALE);
				}
			} else {
				core.writelog("����", "1", 0);
				context.setWarning("��Ч����,�����������!");
				state.setState(PosState.ERROR);
			}
		} catch (IOException e) {
			state.setState(PosState.ERROR);
		}
	}
	
	
	private String getBankCardNo(int maxlen) throws UserCancelException {

		MSRInput msrInput = new MSRInput("��ˢ���п���","bank");
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

	final public static int PRESALE = 'S'; // ����״̬

	final public static int SALE = 's'; // ����״̬

	final public static int FIND = 'F'; // ��ѯ״̬

	final public static int MAXCASH = 'B'; // ����״̬

	final public static int CORRECT = 'C';

	final public static int WITHDRAW = 'W'; // �˻�״̬

	final public static int DISCOUNT = 'd'; // ���ۿ���

	final public static int DISCTOTAL = 'Y'; // ����״̬

	final public static int DISCMONEY = 'M'; // ����ۿ�

	final public static int LOCK = 'K'; // ����/����

	final public static int ALTPRICE = 'A'; // ���״̬

	final public static int OPENSHEET = 'O';

	final public static int CLOSESHEET = 'T';

	final public static int HOLD = 'H';

	final public static int ERROR = 'E'; // ����״̬

	final public static int EXIT = 'X'; // ����״̬

	final public static int CASHOUT = 'U';

	final public static int CASHIN = 'I';

	final public static int NETWORKERROR = 'N'; // �������״̬

	final public static int FUTRUESELL = 'f'; // Ԥ����״̬

	final public static int BRINGFORWARD = 'r'; //���״̬

	final public static int LARGESS = 'l'; //����״̬

	final public static int GROUPLARGESS = 'G'; //��������״̬

	private int CurrentState;

	private int OldState;
}