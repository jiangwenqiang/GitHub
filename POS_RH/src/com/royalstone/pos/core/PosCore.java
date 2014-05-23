package com.royalstone.pos.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.card.ICCardProcess;
import com.royalstone.pos.card.LoanCardDisc;
import com.royalstone.pos.card.LoanCardProcess;
import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.common.AccurateList;
import com.royalstone.pos.common.CashBasket;
import com.royalstone.pos.common.CashBox;
import com.royalstone.pos.common.CouponListLargess;
import com.royalstone.pos.common.Goods;
import com.royalstone.pos.common.GoodsCombList;
import com.royalstone.pos.common.GoodsCutList;
import com.royalstone.pos.common.GoodsExt;
import com.royalstone.pos.common.GoodsExtList;
import com.royalstone.pos.common.GoodsList;
import com.royalstone.pos.common.GoodsProduct;
import com.royalstone.pos.common.GoodsProductList;
import com.royalstone.pos.common.Payment;
import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.common.ProductProperty;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.common.SheetBrief;
import com.royalstone.pos.common.SheetValue;
import com.royalstone.pos.complex.DiscComplex;
import com.royalstone.pos.complex.DiscComplexList;
import com.royalstone.pos.coupon.CouponException;
import com.royalstone.pos.coupon.CouponLargess;
import com.royalstone.pos.coupon.CouponLargessList;
import com.royalstone.pos.coupon.CouponMgr;
import com.royalstone.pos.coupon.CouponMgrImpl;
import com.royalstone.pos.coupon.CouponSale;
import com.royalstone.pos.coupon.CouponSaleList;
import com.royalstone.pos.coupon.CouponTypeList;
import com.royalstone.pos.favor.BulkFavor;
import com.royalstone.pos.favor.BulkPriceList;
import com.royalstone.pos.favor.DiscCriteria;
import com.royalstone.pos.favor.DiscPrice;
import com.royalstone.pos.favor.DiscRate;
import com.royalstone.pos.favor.Discount;
import com.royalstone.pos.favor.DiscountList;
import com.royalstone.pos.gui.DialogConfirm;
import com.royalstone.pos.gui.PriceInput;
import com.royalstone.pos.gui.ProductSelect;
import com.royalstone.pos.invoke.realtime.RealTimeException;
import com.royalstone.pos.io.PosDevOut;
import com.royalstone.pos.io.PosInputGoods;
import com.royalstone.pos.io.PosInputPayment;
import com.royalstone.pos.journal.JournalLog;
import com.royalstone.pos.journal.JournalLogList;
import com.royalstone.pos.journal.JournalManager;
import com.royalstone.pos.journal.JournalWriter;
import com.royalstone.pos.journal.LogManager;
import com.royalstone.pos.journal.LogWriter;
import com.royalstone.pos.managment.ClerkAdm;
import com.royalstone.pos.notify.GoodsUpdate;
import com.royalstone.pos.notify.UnitOfWork;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.Day;
import com.royalstone.pos.util.Exchange;
import com.royalstone.pos.util.ExchangeList;
import com.royalstone.pos.util.FileUtil;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.util.InvalidDataException;
import com.royalstone.pos.util.PosConfig;

/**
 * POS ϵͳ��Ϊ������: shell, core, IO... PosCore ʵ��POS��core
 * 
 * @version 1.0 2004.05.14
 * @author Mengluoyi, Royalstone Co., Ltd.
 */

public class PosCore {

	/**
	 * ��ʼ��PosCore�����ݽṹ: exch_lst, goods_lst, goodsext_lst, sheet_lst... exch_lst :
	 * goods_lst : goodsext_lst : sheet_lst :
	 */
	public PosCore() {
		exch_lst = new ExchangeList();
		goods_lst = new GoodsList();
		prop_lst = new GoodsProductList();
		goodsext_lst = new GoodsExtList();
		favor_lst = new DiscComplexList();
		discount_lst = new DiscountList();
		bulk_lst = new BulkPriceList();
		sheet = new PosSheet();
		context = PosContext.getInstance();
		accurateList = new AccurateList();
		goodsCombList = new GoodsCombList();
		goodsCutList = new GoodsCutList();
		couponListLargess = new CouponListLargess();
		couponLargessList = new CouponLargessList();
		int m = PosConfig.getInstance().getInteger("HANGMAX");
		System.out.println("Max Held sheet: " + m);
		if (m < 1 || m > 100)
			m = 10;
		MAX_SHEETS = m + 1;
		sheet_lst = new String[MAX_SHEETS];
		for (int i = 0; i < MAX_SHEETS; i++)
			sheet_lst[i] = "work" + File.separator + "sheet#" + i;
		last_sold = null;
	}

	/**
	 * @throws InvalidDataException
	 */
	public void init() throws InvalidDataException {

		context = PosContext.getInstance();
		accurateList.fromXMLFile("promo/accurate.xml");
		goodsCombList.fromXMLFile("promo/pricecomb.xml");
		goodsCutList.fromXMLFile("promo/pricecut.xml");
		System.err.println("Init Exchange List ...");
		exch_lst.fromXML("pos.xml");

		System.err.println("Set Currency rate for RMB");
		context.setCurrency("RMB", 1.0);
		String isFast = PosConfig.getInstance().getString("ISFASTLOAD");
		// --------------
		if ("ON".equals(isFast)) {
			System.err.println("Init Price List ...");
			goods_lst.fromXMLFile("price.xml");
			prop_lst.fromXMLFile("property.xml");
		}
		
		// -----------------------
		System.err.println("Init PriceExt List ...");
		goodsext_lst.fromXMLFile("promo/priceExt.xml");

		System.err.println("Init Discount List ...");
		discount_lst.load("promo/discount.lst");

		System.err.println("Init Favor List ...");
		favor_lst.load("promo/favor.lst");

		// System.err.println("Init Bulk List ...");
		// bulk_lst.load("promo/bulkprice.lst");

		createSheetFile();
		loadSheet(sheetFile());
		setHeldCount();

		last_sold = null;

		if (new File(FILE4BASKET).exists()) {
			try {
				cashbasket = CashBasket.load(FILE4BASKET);
			} catch (Exception ex) {
				ex.printStackTrace();

				FileUtil.fileError(FILE4BASKET);
				cashbasket = new CashBasket();

			}

		} else {
			cashbasket = new CashBasket();
		}

		cashbasket.setExchange(exch_lst);
		PosConfig config = PosConfig.getNewInstance();
		int cash_limit = config.getInteger("CASH_LIMIT");
		if (cash_limit < 1)
			cash_limit = 5000;
		cashbasket.setCashLimit(cash_limit * 100);
	}

	public void goodsUpdate(ArrayList goodsUpdateList) throws RealTimeException {

		UnitOfWork.getInstance().begin();

		for (int i = 0; i < goodsUpdateList.size(); i++) {

			GoodsUpdate goodsUpdate = (GoodsUpdate) goodsUpdateList.get(i);
			String goodsNo = goodsUpdate.getGoodsNo();

			goods_lst.update(goodsNo, goodsUpdate.getGoods());
			goodsext_lst.update(goodsNo, goodsUpdate.getGoodsExtList());
			discount_lst.update(goodsNo, goodsUpdate.getDiscCriteria());
			favor_lst.update(goodsNo, goodsUpdate.getComplexList());
			bulk_lst.update(goodsNo, goodsUpdate.getBulkPrice());
		}

		UnitOfWork.getInstance().commit(goods_lst, goodsext_lst, discount_lst,
				favor_lst, bulk_lst);

	}

	/**
	 * POSǰ̨�����˳�ǰ�Ĺ���:ɾ�������ļ�(sheet#1, sheet#2, sheet#3, ...).
	 * 
	 */
	public void exit() {
		for (int i = 0; i < MAX_SHEETS; i++) {
			(new File(sheet_lst[i])).delete();
		}
	}

	/**
	 * @param input
	 * @param baseprice
	 *            ������ĵ���.
	 * @return
	 * @throws RealTimeException
	 * @throws IOException
	 * @throws CouponException
	 */
	public Sale sell(PosInputGoods input, int baseprice, MemberCard memberCard)
			throws RealTimeException, IOException, CouponException {

		PosConfig config = PosConfig.getInstance();
		int qty_top = config.getInteger("MAXAMOUNT");
		int value_top = config.getInteger("MAXCASH") * 100;
		int sheetlen_top = config.getInteger("MAXITEM");
		int value_top_line = config.getInteger("MAXVALUE") * 100;
		long value_cents = input.getCents();
		int qty = input.getQty();
		String colorsize = input.getColorSize();
		Sale sale_rec;

		/*
		 * if (sheet.getValue().getValueTotal() > value_top) {
		 * context.setWarning("�۳���Ʒ����ѳ�������,�뼰ʱ����,�����������!"); return null; }
		 */
		// if(input.getCode().length()!=18&&this.getPosSheet().isCouponSale){
		// context.setWarning("��ȯʱ������������Ʒ,�����������!");
		// return null;
		// }
		if (sheet.getSaleLen() >= sheetlen_top) {
			context.setWarning("�˵��ѳ���,�뼰ʱ����,�����������!");
			return null;
		}

		try {
			Goods g;
			// Look for goods info in goods_lst by scanned code.
			if (input.getCode() == "999999") {
				g = new Goods("999999", "111111", "Ԥ������Ʒ", "000000", "", "", 0,
						0, 1, "n");
			} else {
				g = goods_lst.find(input.getCode());
			}
			// --------
			// ȯ����
			if (g == null && input.getCode().length() == 18) {
				// ���pos�������״̬��������ȯ
				if (this.getPosSheet().isCouponEncash) {
					context.setWarning("�������״̬,������ȯ,�����������!");
					return null;
				}

				if (qty > 1) {
					context.setWarning("���ܶ���ͬһ��ȯ,�����������!");
					return null;
				}

				// if(pos.core.getPosSheet().getSaleLen()!=0&&!pos.core.getPosSheet().isCouponSale){
				// context.setWarning("����Ʒʱȯ��ʱ������ȯ,�����������!");
				// return null;
				// }

				CouponMgr couponMgr = null;
				try {
					couponMgr = new CouponMgrImpl();
				} catch (Exception e) {
					context.setWarning("ȯ�����ô���,�����������!");
					return null;
				}
				// if(input.getCode().length()!=18){
				// context.setWarning("ȯ�Ŵ���,�����������!");
				// return null;
				// }

				String cardNo = input.getCode().substring(0, 10);
				String secrety = input.getCode().substring(10, 18);
				/**
				 * ���������ż��
				 */
				CouponSale queryVO = couponMgr.getCouponSale(cardNo, secrety,
						context.getStoreid());
				input.setOrgCode(cardNo);
				String pType = "n";
				if (queryVO.getDiscountValue() != null
						&& queryVO.getDiscountValue().doubleValue() != 0)
					pType = "p";
				if (queryVO != null && queryVO.getExceptionInfo() == null) {
					g = new Goods("000000", cardNo, cardNo + secrety + "ȯ",
							secrety, "", "��", (int) queryVO.getPrice()
									.doubleValue() * 100, 0, 1, pType);
					if (queryVO.getDiscountValue() != null)
						g.setDiscountValue((int) (Math.ceil(Double
								.parseDouble(queryVO.getDiscountValue()
										.toString()) * 100)));
				}
				if (queryVO != null && queryVO.getExceptionInfo() != null) {
					context.setWarning(queryVO.getExceptionInfo() + ",�����������!");
					return null;
				}
				if (queryVO == null) {
					context.setWarning("��ѯȯʱ�������,�����������!");
					return null;
				}
				queryVO.setCouponID(cardNo);
				queryVO.setCouponPass(secrety);
				queryVO.setCashierID(context.getCashierid());
				queryVO.setShopID(context.getStoreid());
				queryVO.setPosID(context.getPosid());
				// queryVO.setUpdateType("1"); //����״̬λ
				if (!sheet.couponSales.add(queryVO)) {
					context.setWarning("��ȯ�ظ�����,�����������!");
					return null;
				} else if (sheet.couponSales.getUpdateType().equals("0"))
					sheet.couponSales.setUpdateType("sale"); // sale ״̬�� 4
																// ��Ϊ1
				// ��encash ״̬�� 1
				// ��Ϊf
				this.getPosSheet().isCouponSale = true;
			}

			// ����������Ʒ
			// if (g == null&&PosContext.getInstance().isOnLine()) {
			if (g == null) {
				Goods goodsCut = goodsCutList.findCut(input.getCode());
				// GoodsCut goodsCut=
				// RealTime.getInstance().findGoodsCut(input.getCode());
				if (goodsCut != null)
					g = goodsCut;
			}
			// ����������Ʒ
			if (g == null) {
				// Goods goodsCut = goodsCutList.find(input.getCode());
				Goods goodsComb = goodsCombList.find(input.getCode());
				if (goodsComb != null)
					g = goodsComb;
			}
			if (g == null) {
				GoodsExt goodsExt = goodsext_lst.find(input.getCode());
				if (goodsExt != null) {
					g = goodsExt;
					qty = input.getQty() * goodsExt.getPknum();
				}
			}

			if (baseprice != 0) {
				g.setPrice(baseprice);
			}
			if (g != null) {
				// ����0�ۼ���Ʒ
				if (g != null && g.getPrice() == 0) {
					// ����������Ʒ
					if (g.getVgno().equals("999999")) {
						double alterPrice = 0;
						PriceInput priceInput = new PriceInput("������Ԥ�������:");
						priceInput.show();
						try {
							while (!priceInput.isFinish())
								Thread.sleep(500);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						String inputCode = priceInput.getInputcode();
						if (!inputCode.equals("")) {
							String Storeid = context.getStoreid();
							String Buffer = null;
							StringBuffer Str = new StringBuffer();
							for (int i = 0; i < Storeid.length(); i++) {
								char aa = Storeid.charAt(i);
								Buffer = String.valueOf(aa);
								if (Buffer.equals("1") || Buffer.equals("2")
										|| Buffer.equals("3")
										|| Buffer.equals("4")
										|| Buffer.equals("5")
										|| Buffer.equals("6")
										|| Buffer.equals("7")
										|| Buffer.equals("8")
										|| Buffer.equals("9")
										|| Buffer.equals("0")) {
									Str.append(Buffer);
								}
							}
							String Posid = context.getPosid();
							for (int i = 0; i < Posid.length(); i++) {
								char aa = Posid.charAt(i);
								Buffer = String.valueOf(aa);
								if (Buffer.equals("1") || Buffer.equals("2")
										|| Buffer.equals("3")
										|| Buffer.equals("4")
										|| Buffer.equals("5")
										|| Buffer.equals("6")
										|| Buffer.equals("7")
										|| Buffer.equals("8")
										|| Buffer.equals("9")
										|| Buffer.equals("0")) {
									Str.append(Buffer);
								}
							}
							String Ss = Str.toString();
							context.setOrderidOld(Ss
									+ Integer.toString(context.getOrder_id()));
							alterPrice = Double.parseDouble(inputCode);
							g.setPrice((int) (alterPrice * 100));
						} else {
							context.setWarning("Ԥ��������Ϊ�㣬��������˳�");
							return null;
						}
					} else {
						double alterPrice = 0;
						PriceInput priceInput = new PriceInput("��������Ʒ�۸�:");
						priceInput.show();
						try {
							while (!priceInput.isFinish())
								Thread.sleep(500);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						String inputCode = priceInput.getInputcode();
						if (inputCode != null) {
							if (inputCode.equals(""))
								inputCode = "0";
							alterPrice = Double.parseDouble(inputCode);
							g.setPrice((int) (alterPrice * 100));
						}
					}
				}

			}

			if (g == null) {
				context.setWarning("�۸�����޴���Ʒ,�����������!");
				return null;
			}

			if (qty > qty_top * g.getX()) {
				context.setWarning("��Ʒ��������,�����������!");
				return null;
			}

			if (g.getVgno().equals("999999")) {
				long v = (long) Math.round((long) g.getPrice() * (long) qty
						* 100 / (long) g.getX() + 50) / 100;

				if (v == 0 || v > Moner_line) {
					context.setWarning("�˱ʶ���������,�����������!");
					return null;
				}

			} else {
				long v = (long) Math.round((long) g.getPrice() * (long) qty
						* 100 / (long) g.getX() + 50) / 100;
				if (v < 0 || v > value_top_line) {
					context.setWarning("�˱���Ʒ�����󣬰����������");
					return null;
				}
				if (v + sheet.getValue().getValueTotal() > value_top) {
					context.setWarning("�۳���Ʒ����ѳ�������,�뼰ʱ����,�����������!");
					return null;
				}
				if (pos.core.getPosSheet().isCouponEncash
						&& (v + sheet.getValue().getValueTotal()) > pos.core
								.getPosSheet().getCouponValue()) {
					context.setWarning("������Ʒ����ѳ�������,�뼰ʱ����,�����������!");
					return null;
				}
			}

			if (g.getX() == 1 && g.getDeptid() != null
					&& !g.getDeptid().equals("040201")
					&& input.getQty() * 1000 != input.getMilliVolume()) {
				context.setWarning("����Ʒ��������Ϊ����,�����������!");
				return null;
			}

			if (qty <= 0) {
				context.setWarning("��Ʒ��������Ϊ��,�����������!");
				return null;
			}

			// �����ӡ;
			if (sheet.getSaleLen() == 0 && sheet.getMemberCard() == null
					&& sheet.getCouponNO().equals("")
					&& !g.getVgno().equals("999999")) {
				PosDevOut.getInstance().displayHeader(context);
			}

			if (sheet.getSaleLen() == 0 && sheet.getMemberCard() == null
					&& sheet.getCouponNO().equals("")
					&& g.getVgno().equals("999999")) {
				PosDevOut.getInstance().displayHead(context);
			}

			if (!context.getCounp()) {
				clearDiscount(g);
			}

			Sale sale;
			sale = new Sale(g, qty, Sale.SALE);

			sale.setOriginalCode(input.getOrgCode());
			sale.setWaiter(context.getWaiterid());
			sale.setAuthorizer(context.getAuthorizerid());
			sale.setPlaceno(context.getPlaceno());
			sale.setColorSize(input.getColorSize());
			if (input.getGoodsType() == Goods.WEIGHT_VALUE) {

				if (sale.getDiscValue() == 0) {
					sale.setStdValue(input.getCents());
					sale.setFactValue(input.getCents());
				} else {
					sale.setFactValue(input.getCents());
					sale.setStdValue(input.getCents() + sale.getDiscValue());
				}

			}

			// if(input.getGoodsType() == Goods.WEIGHT){ ����ذ����ף��������ۿ�
			if (input.getDeptid() != null
					&& input.getDeptid().equals(Goods.LOADOMETER)) {
				sale.setFactValue(input.getCents());
				sale.setStdValue(input.getCents());
			}
			if (this.getPosSheet().getCouponNO() != null
					&& !this.getPosSheet().getCouponNO().equals("")) {

				sale.getGoods().setType("n");

			}

			if (context.getCounp()) {
				for (int i = 0; i < couponLargessList.size(); i++) {
					CouponLargess couponLargess = couponLargessList.get(i);
					if (sale.getGoods().getVgno().equals("000000")) {
						// if (sale.getGoods().getPrice() == (int) (Math
						// .rint(couponLargess.getPrice().doubleValue() * 100)))
						if (couponLargess.getPresenttype().equals(
								sale.getGoods().getBarcode().substring(0, 4))) {
							if (couponLargess.dumpVgnoPrice(sale.getGoods()
									.getPrice())) {
								sale.setFactValue(0);
								sale.setStdValue(0);
								sale.getGoods().setPrice(0);
								sale.setDiscType(couponLargessList
										.getLargessType());
								sale_rec = sheet.sell(sale);
								// couponLargessList.removeCouponSale(couponLargess.getPrice());

								sheet.updateValue();
								dump();
								last_sold = sale_rec;

								String filename = "largessinfo"
										+ File.separator + ".xml";
								File file = new File(filename);
								file.delete();

								performCouponLargessList(couponLargessList);

								return sale_rec;
							}
						}
					} else {
						CouponTypeList coupon = getVgnoCouponType(sale
								.getGoods().getVgno());
						if (!coupon.getException().equals("")) {
							context.setWarning(coupon.getException());
							return null;
						}
						for (int c = 0; c < coupon.size(); c++) {
							String couponType;
							couponType = coupon.getCouponType(c);
							if (couponLargess.getPresenttype().equals(
									couponType)) {
								if (couponLargess.dumpVgnoPrice((int) sale
										.getFactValue())) {
									sale.setFactValue(0);
									sale.setStdValue(0);
									sale.getGoods().setPrice(0);
									sale.setDiscType(couponLargessList
											.getLargessType());
									sale_rec = sheet.sell(sale);
									// couponLargessList
									// .removeCouponSale(couponLargess
									// .getPrice());

									sheet.updateValue();
									dump();
									last_sold = sale_rec;

									String filename = "largessinfo"
											+ File.separator + ".xml";
									File file = new File(filename);
									file.delete();

									performCouponLargessList(couponLargessList);

									return sale_rec;
								}
							}
						}

					}
				}
				context.setWarning("������Ʒ���������");
				if (g.getVgno().equals("000000")) {
					sheet.couponSales.removeCouponSale(g.getBarcode());
				}

				return null;

			}

			// ��ȯ��Ʒ
			if (context.getLargessCoupon() == 1) {
				if (confirm("�Ƿ���ȯ��Ʒ?")) {
					DiscPrice discprice = new DiscPrice(Discount.LARGESSC, 0);
					sale.setDiscValue(discprice);
				}
			}

			sale_rec = sheet.sell(sale);

			// �������������������������������������
			// �������۲��ۼ�
			if (!context.getGrouplargess()) {
				// ������ʱ���ۼ�
				if (!context.getCounp()) {
					performDiscount(g);
				}
			}

			sheet.updateValue();

			dump();

			last_sold = sale_rec;
			return sale_rec;

		} catch (RealTimeException ex) {
			ex.printStackTrace();
			loadSheet(sheetFile());
			throw ex;
		}
	}

	// ��ȡ��Ʒ�ľ�����
	private CouponTypeList getVgnoCouponType(String vgno) {
		ClerkAdm am = new ClerkAdm(context.getServerip(), context.getPort());
		CouponTypeList couponType = am.getCouponType(vgno);
		return couponType;
	}

	// liangxinbiao 2005-12-10 begin
	private void performComplexPromProm() {

		if (sheet.getMemberCard() != null) {

			MemberCard mc = pos.core.getPosSheet().getMemberCard();

			// if (mc.getDiscount() != 0 && mc.getDiscount() != 100
			// && mc.getPromDiscount() != 0 && mc.getPromDiscount() != 100) {

			if (mc.getDiscount() != 0 && mc.getPromDiscount() != 100) {

				for (int i = 0; i < sheet.getSaleLen(); i++) {

					Sale s = sheet.getSale(i);

					Goods g = s.getGoods();

					if ((g.getPType().equals(DiscCriteria.DISCCOMPLEX) || g
							.getPType().equals(DiscCriteria.BUYANDGIVE))
							&& s.getType() != Sale.WITHDRAW) {

						long valueAfterDisc = 0;

						long priceAfterComplexDisc = Math
								.round((s.getPrice() - (double) (s
										.getDiscValue())
										/ s.getQtyDisc()) / 10) * 10;

						long priceAfterVipDisc = Math
								.round((double) (s.getStdPrice() * (100 - mc
										.getDiscount())) / 1000) * 10;

						long priceAfterComplexVipDisc = Math
								.round((double) (priceAfterComplexDisc * (100 - mc
										.getPromDiscount())) / 1000) * 10;

						if (priceAfterVipDisc * s.getQty() <= priceAfterComplexVipDisc
								* s.getQtyDisc()
								+ priceAfterVipDisc
								* (s.getQty() - s.getQtyDisc())) {

							valueAfterDisc = priceAfterVipDisc * s.getQty();

							s.setDiscType(Discount.VIPPROM);

						} else {

							valueAfterDisc = priceAfterComplexVipDisc
									* s.getQtyDisc() + priceAfterVipDisc
									* (s.getQty() - s.getQtyDisc());

							s.setDiscType(Discount.VIPPROMPROM);

						}

						s.setFactValue(valueAfterDisc);

						s.setDiscValue(s.getStdValue() - s.getFactValue());

					}

				}

			}

		}

	}

	private void clearComplexDiscount() {

		for (int i = 0; i < sheet.getSaleLen(); i++) {

			Sale s = sheet.getSale(i);

			Goods g = s.getGoods();

			if ((g.getPType().equals(DiscCriteria.DISCCOMPLEX) || g.getPType()
					.equals(DiscCriteria.BUYANDGIVE))
					&& s.getType() != Sale.WITHDRAW) {

				sheet.clearDiscount(g);

			}

		}

	}

	public void performCouponLargessList(CouponLargessList couponLargessList) {
		// ͬ������
		synchronized (pos.Lock) {
			try {
				String filename = "largessinfo" + File.separator + "2006RH.xml";
				FileOutputStream fs = new FileOutputStream(filename);
				Document doc = new Document(couponLargessList.toElement());
				XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
				outputter.output(doc, fs);
				fs.flush();
				fs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public String performCouponPay(CouponSaleList couponSales) {
		synchronized (pos.Lock) {

			SimpleDateFormat sdfDateTime = new SimpleDateFormat(
					"yyyyMMddHHmmssSSS");
			try {

				String filename = "couponrever" + File.separator
						+ sdfDateTime.format(new Date()) + ".xml";
				FileOutputStream fs = new FileOutputStream(filename);
				Document doc = new Document(couponSales.toElement());
				XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
				outputter.output(doc, fs);
				fs.flush();
				fs.close();

				CouponMgr couponMgr = new CouponMgrImpl();
				for (int i = 0; i < couponSales.size(); i++) {
					couponSales.get(i).setShopID(context.getStoreid());
					couponSales.get(i).setPosID(context.getPosid());
					couponSales.get(i).setCashierID(context.getCashierid());
				}
				String result = couponMgr.saleAndEncashCoupons(couponSales);
				// String result=null;
				if (result != null && result.equals("1")) {
					File file = new File(filename);
					file.delete();
					// this.sheet.couponSales=new CouponSaleList();
					return "1";
				} else {
					if (result == null) {
						context.setWarning("ʹ�ñ�ȯʱ��������,�����������!");
						System.out.println("���±�ȯ��Ϣʱ�������,�����������!");
						return "0";
					} else {
						context.setWarning("ʹ�ñ�ȯʱ" + result + ",�����������!");
						System.out.println("���±�ȯ��Ϣʱ" + result + ",�����������!");
						return "0";
					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return "0";
			}
		}
	}

	/**
	 * ����ȯ�������Լ����֡�
	 * 
	 * @param couponSales
	 * @return
	 */
	public String performCouponSale(CouponSaleList couponSales) {
		synchronized (pos.Lock) {

			SimpleDateFormat sdfDateTime = new SimpleDateFormat(
					"yyyyMMddHHmmssSSS");
			try {
				if (couponSales.getUpdateType().equals("0")) {
					context.setWarning("ȯ���۳���,�����������!");
					return "0";

				}
				// if(couponSales.getUpdateType().equals("sale"))
				if (sheet.isCouponSale)
					couponSales.setUpdateType("4"); // ��ȯ�ڿ�����ǰ��״̬�� 4
				// else if(couponSales.getUpdateType().equals("encash"))
				if (sheet.isCouponEncash)
					couponSales.setUpdateType("1"); // ��ȯ�ڿ�����ǰ��״̬�� 4

				String filename = "couponrever" + File.separator
						+ sdfDateTime.format(new Date()) + ".xml";
				FileOutputStream fs = new FileOutputStream(filename);
				Document doc = new Document(couponSales.toElement());
				XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
				outputter.output(doc, fs);
				fs.flush();
				fs.close();

				// if(couponSales.getUpdateType().equals("4"))
				if (sheet.isCouponSale)
					couponSales.setUpdateType("1"); // ��ȯ�ڿ�����ǰ��״̬�� 4
				// else if(couponSales.getUpdateType().equals("1")){
				if (sheet.isCouponEncash) {
					couponSales.setUpdateType("f"); // ��ȯ�ڿɶһ�ǰ��״̬�� 1
					couponSales.get(0).setSaleValue(
							(double) sheet.getValue().getValueTotal() / 100);
				}

				CouponMgr couponMgr = new CouponMgrImpl();
				for (int i = 0; i < couponSales.size(); i++) {
					couponSales.get(i).setShopID(context.getStoreid());
					couponSales.get(i).setPosID(context.getPosid());
					couponSales.get(i).setCashierID(context.getCashierid());
				}
				String result = couponMgr.saleAndEncashCoupons(couponSales);
				// String result=null;
				if (result != null && result.equals("1")) {
					File file = new File(filename);
					file.delete();
					// this.sheet.couponSales=new CouponSaleList();
					return "1";
				} else {
					if (result == null) {
						context.setWarning("ʹ�ñ�ȯʱ��������,�����������!");
						System.out.println("���±�ȯ��Ϣʱ�������,�����������!");
						return "0";
					} else {
						context.setWarning("ʹ�ñ�ȯʱ" + result + ",�����������!");
						System.out.println("���±�ȯ��Ϣʱ" + result + ",�����������!");
						return "0";
					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return "0";
			}
		}
	}

	public boolean checkLoanCardCanConsume(ArrayList deptList, Goods g) {

		DecimalFormat df = new DecimalFormat("000000");
		for (int i = 0; i < deptList.size(); i++) {
			deptList.set(i, df.format(Integer
					.parseInt((String) deptList.get(i))));
		}

		String strDepid = g.getDeptid();
		String strBigGroup = "";
		String strMidGroup = "";

		if (strDepid != null && strDepid.length() == 6) {
			strBigGroup = df.format(Integer.parseInt(strDepid.substring(0, 2)));
			strMidGroup = df.format(Integer.parseInt(strDepid.substring(1, 4)));
		}

		if (deptList.contains("000000")) {
			return true;
		}

		if (!deptList.contains(strBigGroup)) {
			if (!deptList.contains(strMidGroup)) {
				if (!deptList.contains(g.getDeptid())) {
					return false;
				}
			}
		}

		return true;

	}

	public void performLoanDisc(ArrayList discs, Sale sale) {

		DecimalFormat df = new DecimalFormat("000000");

		String strDepid = sale.getDeptid();
		String strBigGroup = "";
		String strMidGroup = "";

		if (strDepid != null && strDepid.length() == 6) {
			strBigGroup = df.format(Integer.parseInt(strDepid.substring(0, 2)));
			strMidGroup = df.format(Integer.parseInt(strDepid.substring(1, 4)));
		}

		for (int i = 0; i < discs.size(); i++) {

			LoanCardDisc disc = (LoanCardDisc) discs.get(i);

			if ((disc.getItemType() == LoanCardDisc.SINGLE_ITEMTYPE && df
					.format(disc.getItemID()).equals(sale.getGoods().getVgno()))
					|| (disc.getItemType() == LoanCardDisc.SMALLDEPT_ITEMTYPE && df
							.format(disc.getItemID()).equals(strDepid))
					|| (disc.getItemType() == LoanCardDisc.MIDDEPT_ITEMTYPE && df
							.format(disc.getItemID()).equals(strMidGroup))
					|| (disc.getItemType() == LoanCardDisc.BIGDEPT_ITEMTYPE && df
							.format(disc.getItemID()).equals(strBigGroup))
					|| (df.format(disc.getItemID()).equals("000000"))) {

				switch (disc.getDiscType()) {
				case LoanCardDisc.DISC_PRICE:
					long newPrice = sale.getPrice()
							- (disc.getDiscCount()
									.multiply(new BigDecimal(100)).longValue());
					DiscPrice discPrice = new DiscPrice(Discount.LOANDISC,
							newPrice);
					sale.setDiscount(discPrice);
					break;

				case LoanCardDisc.DISC_RATE:
					DiscRate discRate = new DiscRate(Discount.LOANDISC, disc
							.getDiscCount().intValue());
					sale.setDiscount(discRate);
					break;
				}

				break;

			}

		}

	}

	/**
	 * @param g
	 * @return
	 * @throws RealTimeException
	 */
	private ArrayList getMatchDiscComplex(Goods g) throws RealTimeException {
		return sheet.getMatchDiscComplex(favor_lst, g);
	}

	/**
	 * @param input
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RealTimeException
	 */
	public Sale findprice(PosInputGoods input) throws FileNotFoundException,
			IOException, RealTimeException {
		PosConfig config = PosConfig.getInstance();
		int qty_top = config.getInteger("MAXAMOUNT");
		int value_top = config.getInteger("MAXCASH") * 100;
		int sheetlen_top = config.getInteger("MAXITEM");
		long value_cents = input.getCents();
		int qty = input.getQty();
		String colorsize = input.getColorSize();
		Sale sale_rec;

		// Look for goods info in goods_lst by scanned code.
		try {
			Goods g = goods_lst.find(input.getCode());
			if (g == null) {
				GoodsExt goodsExt = goodsext_lst.find(input.getCode());
				if (goodsExt != null) {
					g = goodsExt;
					qty = input.getQty() * goodsExt.getPknum();
				}
			}

			if (g == null) {
				context.setWarning("�۸�����޴���Ʒ,�����������!");
				return null;
			}

			if (qty > qty_top * g.getX()) {
				context.setWarning("��Ʒ��������,�����������!");
				return null;
			}

			int v = g.getPrice() * qty / g.getX();

			if (v < 0 || v > value_top) {
				context.setWarning("��Ʒ������,�����������!");
				return null;
			}

			if (g.getX() == 1
					&& input.getQty() * 1000 != input.getMilliVolume()) {
				context.setWarning("����Ʒ��������Ϊ����,�����������!");
				return null;
			}

			if (qty <= 0) {
				context.setWarning("��Ʒ��������Ϊ��,�����������!");
				return null;
			}

			Sale sale = new Sale(g, qty, Sale.SALE);
			sale.setOriginalCode(input.getOrgCode());
			sale.setWaiter(context.getWaiterid());
			sale.setAuthorizer(context.getAuthorizerid());
			sale.setPlaceno(context.getPlaceno());
			sale.setColorSize(input.getColorSize());
			if (input.getGoodsType() == Goods.WEIGHT_VALUE) {
				sale.setStdValue(input.getCents());
				sale.setFactValue(input.getCents());
			}
			sale_rec = sheet.sell(sale);

			clearDiscount(g);
			performDiscount(g);

			sheet.updateValue();

			return sale_rec;
		} catch (RealTimeException ex) {
			ex.printStackTrace();
			loadSheet(sheetFile());
			throw ex;
		}
	}

	/**
	 * @param disc_type
	 *            �ۿ�����
	 * @param name
	 *            ��Ʒ���ƻ��߹��ʿ�����
	 * @param value
	 *            ���ۼ�ֵ
	 */
	public void sell(int disc_type, String name, long value) {
		Goods g = new Goods(name);
		Sale sale = new Sale(disc_type, g, value);
		// System.out.println("NEW ������SALE type Ϊ��"+ (char) disc_type );
		sheet.falsesell(sale);
	}

	/**
	 * @return Sale ���ۼ�¼
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RealTimeException
	 */
	public Sale quick_correct() throws FileNotFoundException, IOException,
			RealTimeException {

		// �˴�����ֱ�ӵ��� posFrame ����,Ӧ���޸�.
		int qcorrectrow = pos.posFrame.quickcorrectrow();
		// TODO ��ȡ�������һ�е����
		int lastrow = pos.posFrame.getLastrow();
		System.out.println("qcorrectrow:" + qcorrectrow + "...lastrow:"
				+ lastrow);
		if (qcorrectrow != lastrow) {
			context.setWarning("��Ч����,ֻ��ʹ�ø�������,�����������!");
			return null;
		}
		System.out.println("����:qcorrect" + qcorrectrow);
		last_sold = sheet.getSale(qcorrectrow);

		if (last_sold.getType() == Sale.ENCASH) {
			context.setWarning("��Ч����,�����������!");
			return null;
		}

		// zhouzhou add �һ���Ʒ��������
		if (last_sold.getDiscType() == Discount.Change) {
			context.setWarning("�һ���Ʒ������Ч,�����������!");
			return null;
		}

		Goods g = last_sold.getGoods();
		// ----------------------------
		if (g.getVgno().equals("000000")) {
			sheet.couponSales.removeCouponSale(g.getBarcode());
		}

		// ------------------------------

		System.out.println(last_sold.getquickcorrect());
		if (last_sold.getquickcorrect() != 'y'
				&& (last_sold.getType() == 's' || last_sold.getType() == 'r' || last_sold
						.getType() == 'l')) {
			pos.posFrame.quickcorrectchangerow();
			last_sold.setquickcorrect();
		} else if (last_sold.getquickcorrect() == 'y'
				&& (last_sold.getType() == 's' || last_sold.getType() == 'r' || last_sold
						.getType() == 'l')) {
			// writelog("����", "1", 0);
			// context.setWarning("����Ʒ�ѽ��м���,�����������!");
			writelog("����", "1", 0);
			context.setWarning("����Ʒ�ѽ��и���,�����������!");
			return null;
		} else {
			// writelog("����", "1", 0);
			writelog("����", "1", 0);
			context.setWarning("��Ч����,�����������!");
			return null;
		}

		if (last_sold == null) {
			// writelog("����", "1", 0);
			writelog("����", "1", 0);
			context.setWarning("��Ч����,�����������!");
			return null;
		}

		int sold = sheet.getSoldQty(last_sold.getGoods().getVgno());
		if (sold < last_sold.getQty()) {
			// writelog("����", "1", 0);
			writelog("����", "1", 0);
			context.setWarning("��Ч����,�����������!");
			return null;
		}

		if (last_sold.getType() != Sale.WITHDRAW) {
			clearDiscount(g);
		}
		Sale sale = new Sale(g, -last_sold.getQty(), Sale.QUICKCORRECT);
		sale.setOriginalCode(last_sold.getOrgCode());
		sale.setColorSize(last_sold.getColorSize());
		sale.setPlaceno(context.getPlaceno());
		sale.setAuthorizer(context.getAuthorizerid());
		sale.setStdValue(-last_sold.getStdValue());
		sale.setFactValue(-last_sold.getFactValue());

		if (last_sold.getDiscType() == Discount.ALTPRICE) {
			sale.setDiscValue(new DiscPrice(Discount.ALTPRICE, -(last_sold
					.getStdValue() - last_sold.getDiscValue())));
		}
		if (last_sold.getDiscType() == Discount.SINGLE) {
			sale.setDiscValue(new DiscPrice(Discount.SINGLE, -(last_sold
					.getStdValue() - last_sold.getDiscValue())));
		}
		if (last_sold.getDiscType() == Discount.TOTAL) {
			int percent = 0;
			percent = (int) Math.rint(100 - last_sold.getFactValue() * 100.0
					/ last_sold.getStdValue());
			DiscRate discrate = new DiscRate(Discount.TOTAL, percent);
			sale.setDiscount(discrate);
		}
		if (last_sold.getDiscType() == Discount.MONEY) {
			long itemvalue = 0;
			itemvalue = last_sold.getFactValue();
			DiscPrice discprice = new DiscPrice(Discount.MONEY, -itemvalue);
			sale.setDiscValue(discprice);
		}
		if (last_sold.getDiscType() == Discount.LOANDISC) {
			sale.setDiscValue(new DiscPrice(Discount.LOANDISC, -(last_sold
					.getStdValue() - last_sold.getDiscValue())));
		}
		if (last_sold.getDiscType() == Discount.LARGESSC) {
			sale.setDiscValue(new DiscPrice(Discount.LARGESSC, -(last_sold
					.getStdValue() - last_sold.getDiscValue())));
		}

		sale.setquickcorrect();
		Sale s = sheet.correct(sale);
		if (last_sold.getType() != Sale.WITHDRAW) {
			performDiscount(g);
		}

		sheet.updateValue();
		dump();

		/* add by lichao 8/24/2004 */
		try {

			int value = (int) sale.getDiscValue();
			int value1 = sheet.getValue().getDiscDelta();
			if (value1 != 0) {
				if (last_sold.getDiscType() == Discount.ALTPRICE) {
					String name = "���";
					sell(Sale.AlTPRICE, name, value);
				} else if (last_sold.getDiscType() == Discount.SINGLE) {
					String name = "�����ۿ�";
					sell(Sale.SINGLEDISC, name, value);
				} else if (last_sold.getDiscType() == Discount.TOTAL) {
					String name = "�ܶ��ۿ�";
					sell(Sale.TOTALDISC, name, value);
				} else if (last_sold.getDiscType() == Discount.MONEY) {
					String name = "����ۿ�";
					sell(Sale.MONEYDISC, name, value);
				} else if (last_sold.getDiscType() == Discount.LOANDISC) {
					String name = "���ʿ��ۿ�";
					sell(Sale.LOANDISC, name, value);
				} else {
					String name = new Discount(s.getDiscType()).getTypeName();
					sell(Sale.AUTODISC, name, value1);
				}
			}

			dump();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* end */

		// writelog("����", "0", 0);
		writelog("����", "1", 0);
		last_sold = null;
		return s;
	}

	/**
	 * ��������
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RealTimeException
	 */
	public Sale correct() throws FileNotFoundException, IOException,
			RealTimeException {

		// �˴�����ֱ�ӵ��� posFrame ����,Ӧ���޸�.
		int qcorrectrow = pos.posFrame.quickcorrectrow();
		System.out.println("����:qcorrect" + qcorrectrow);
		last_sold = sheet.getSale(qcorrectrow);

		if (last_sold.getType() == Sale.ENCASH) {
			context.setWarning("��Ч����,�����������!");
			return null;
		}

		// ----------------------------
		Goods g = last_sold.getGoods();
		// zhouzhou add �һ���Ʒ���ܸ���
		if (last_sold.getDiscType() == Discount.Change) {
			context.setWarning("�һ���Ʒ��Ч����,�����������!");
			return null;
		}

		if (g.getVgno().equals("000000"))
			sheet.couponSales.removeCouponSale(g.getBarcode());
		// ------------------------------

		System.out.println(last_sold.getquickcorrect());
		if (last_sold.getquickcorrect() != 'y'
				&& (last_sold.getType() == 's' || last_sold.getType() == 'r')) {
			pos.posFrame.quickcorrectchangerow();
			last_sold.setquickcorrect();
		} else if (last_sold.getquickcorrect() == 'y'
				&& (last_sold.getType() == 's' || last_sold.getType() == 'r')) {
			// writelog("����", "1", 0);
			// context.setWarning("����Ʒ�ѽ��м���,�����������!");
			writelog("����", "1", 0);
			context.setWarning("����Ʒ�ѽ��и���,�����������!");
			return null;
		} else {
			// writelog("����", "1", 0);
			writelog("����", "1", 0);
			context.setWarning("��Ч����,�����������!");
			return null;
		}

		if (last_sold == null) {
			// writelog("����", "1", 0);
			writelog("����", "1", 0);
			context.setWarning("��Ч����,�����������!");
			return null;
		}

		int sold = sheet.getSoldQty(last_sold.getGoods().getVgno());
		if (sold < last_sold.getQty()) {
			// writelog("����", "1", 0);
			writelog("����", "1", 0);
			context.setWarning("��Ч����,�����������!");
			return null;
		}

		if (last_sold.getType() != Sale.WITHDRAW) {
			clearDiscount(g);
		}
		Sale sale = new Sale(g, -last_sold.getQty(), Sale.QUICKCORRECT);
		sale.setOriginalCode(last_sold.getOrgCode());
		sale.setColorSize(last_sold.getColorSize());
		sale.setPlaceno(context.getPlaceno());
		sale.setAuthorizer(context.getAuthorizerid());
		sale.setStdValue(-last_sold.getStdValue());
		sale.setFactValue(-last_sold.getFactValue());

		if (last_sold.getDiscType() == Discount.ALTPRICE) {
			sale.setDiscValue(new DiscPrice(Discount.ALTPRICE, -(last_sold
					.getStdValue() - last_sold.getDiscValue())));
		}
		if (last_sold.getDiscType() == Discount.SINGLE) {
			sale.setDiscValue(new DiscPrice(Discount.SINGLE, -(last_sold
					.getStdValue() - last_sold.getDiscValue())));
		}
		if (last_sold.getDiscType() == Discount.TOTAL) {
			int percent = 0;
			percent = (int) Math.rint(100 - last_sold.getFactValue() * 100.0
					/ last_sold.getStdValue());
			DiscRate discrate = new DiscRate(Discount.TOTAL, percent);
			sale.setDiscount(discrate);
		}
		if (last_sold.getDiscType() == Discount.MONEY) {
			long itemvalue = 0;
			itemvalue = last_sold.getFactValue();
			DiscPrice discprice = new DiscPrice(Discount.MONEY, -itemvalue);
			sale.setDiscValue(discprice);
		}
		if (last_sold.getDiscType() == Discount.LOANDISC) {
			sale.setDiscValue(new DiscPrice(Discount.LOANDISC, -(last_sold
					.getStdValue() - last_sold.getDiscValue())));
		}
		if (last_sold.getDiscType() == Discount.LARGESSC) {
			sale.setDiscValue(new DiscPrice(Discount.LARGESSC, -(last_sold
					.getStdValue() - last_sold.getDiscValue())));
		}

		sale.setquickcorrect();
		Sale s = sheet.correct(sale);
		if (last_sold.getType() != Sale.WITHDRAW) {
			performDiscount(g);
		}

		sheet.updateValue();
		dump();

		/* add by lichao 8/24/2004 */
		try {

			int value = (int) sale.getDiscValue();
			int value1 = sheet.getValue().getDiscDelta();
			if (value1 != 0) {
				if (last_sold.getDiscType() == Discount.ALTPRICE) {
					String name = "���";
					sell(Sale.AlTPRICE, name, value);
				} else if (last_sold.getDiscType() == Discount.SINGLE) {
					String name = "�����ۿ�";
					sell(Sale.SINGLEDISC, name, value);
				} else if (last_sold.getDiscType() == Discount.TOTAL) {
					String name = "�ܶ��ۿ�";
					sell(Sale.TOTALDISC, name, value);
				} else if (last_sold.getDiscType() == Discount.MONEY) {
					String name = "����ۿ�";
					sell(Sale.MONEYDISC, name, value);
				} else if (last_sold.getDiscType() == Discount.LOANDISC) {
					String name = "���ʿ��ۿ�";
					sell(Sale.LOANDISC, name, value);
				} else {
					String name = new Discount(s.getDiscType()).getTypeName();
					sell(Sale.AUTODISC, name, value1);
				}
			}

			dump();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* end */

		// writelog("����", "0", 0);
		writelog("����", "1", 0);
		last_sold = null;
		return s;
	}

	public Sale property() {
		int row = pos.posFrame.quickcorrectrow();

		System.out.println("ѡ�����:" + row);
		last_sold = sheet.getSale(row);
		//�������Ʒ��������Ʒ
		if (last_sold.isProductGood()) {
			GoodsProduct gp = null;
			try {
				gp = findGoodsPruduct(last_sold.getVgno());
			} catch (RealTimeException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			
			if (gp != null) {
				ProductSelect ui = new ProductSelect(gp,last_sold.getProerties());
				ui.show();
				if (ui.getConfrim() == ProductSelect.PS_ENTER)
				{
					ProductProperty pp = gp.getProperty(ui.getHoldid());
					if (pp != null) {
						last_sold.addGoodProperty(pp);
					}
				}
				else if(ui.getConfrim() == ProductSelect.PS_CLEAR)
				{
					last_sold.clearGoodProperty();
				}
			}
		}
		return last_sold;
	}

	/**
	 * ����ۿ�����. Ϊ��֧�ֶ����ۿ�,POS���ۿ��㷨ʮ�ָ���. ��"�۳���Ʒ�嵥"�б仯ʱ,��Ҫ�Ȱѹ˿������ܵ��ۿ�����,�����¼���Ӧ���ܵ��ۿ�.
	 * 
	 * @param g
	 * @throws RealTimeException
	 */
	private void clearDiscount(Goods g) throws RealTimeException {

		if ((g.getPType().equals(DiscCriteria.DISCCOMPLEX))
				|| (g.getPType().equals(DiscCriteria.BUYANDGIVE))) {
			SaleList saleList = sheet.getSalelst();
			for (int i = 0; i < saleList.size(); i++) {
				if (saleList.get(i).getType() != Sale.WITHDRAW
						&& (saleList.get(i).getGoods().getPType().equals(
								DiscCriteria.DISCCOMPLEX) || saleList.get(i)
								.getGoods().getPType().equals(
										DiscCriteria.BUYANDGIVE))
						&& !saleList.get(i).getVgno().equals("000000")) {
					saleList.get(i).clearFavor();
				}
			}
		} else {
			sheet.clearDiscount(g);
		}
	}

	/**
	 * @param g
	 * @throws RealTimeException
	 */
	private void performDiscount(Goods g) throws RealTimeException {

		if (context.getLargessCoupon() == 1) {
		} else if (g.getPType().equals(DiscCriteria.BULKPRICE)) {
			sheet.clearDiscount(g);
			int qty_favored = 0;
			int qty_goods = sheet.getSoldQty(g);
			for (int i = 6; i >= 1 && qty_goods > 0; i--) {
				qty_goods -= sheet.getQtyDisc(g);
				BulkFavor favor_total = bulk_lst.getBulkFavor(g, qty_goods);
				sheet.consumeBulkFavorDesc(g, favor_total);
			}
			// ���Ĺ� �������������
		} else if (g.getPType().equals(DiscCriteria.DISCCOMPLEX)
				|| g.getPType().equals(DiscCriteria.BUYANDGIVE)) {

			clearComplexDiscount();

			ArrayList complexGoods = getComplexGoods();
			ArrayList discList = new ArrayList();

			for (int i = 0; i < complexGoods.size(); i++) {
				discList
						.addAll(getMatchDiscComplex((Goods) complexGoods.get(i)));
			}

			// Object[] discAry =
			// orderComplexByLevel(splitDiscComplex(filterDiscComplex(discList)));

			Object[] discAry = orderComplexByLevel(filterDiscComplex(discList));

			for (int i = 0; i < discAry.length; i++) {
				DiscComplex disc = (DiscComplex) discAry[i];
				disc.computeFavorAfter(sheet.getSalelst());
				if (sheet.getSalelst().caculateFavorAfter(disc) > 0) {
					sheet.getSalelst().consumeFavorAfter(disc);
				}
			}

			performComplexPromProm();

		} else {
			int qty = sheet.getSoldQty(g);

			if (discount_lst.matches(g, qty, 0)) {
				Discount disc = null;
				// if(sheet.getMemberCard()!=null)
				// disc = discount_lst.getDiscount(g, qty,
				// sheet.getMemberLevel());
				// else
				disc = discount_lst.getDiscount(g, qty, -1);
				if (disc instanceof DiscRate) {
					System.out.println("DiscRate FOUND!");
					DiscRate r = (DiscRate) disc;
					sheet.setGoodsDisc(g, r);
				}

				if (disc instanceof DiscPrice) {
					System.out.println("DiscPrice FOUND!");
					DiscPrice p = (DiscPrice) disc;
					sheet.setGoodsDisc(g, p);
				}

			}

			else {
				if (g.getVgno().equals("000000") && g.getDiscountValue() != 0) {
					System.out.println("��ȯ����!");
					DiscPrice p = new DiscPrice(g.getPType().charAt(0), g
							.getDiscountValue());
					sheet.setGoodsDisc(g, p);

				}

				else if (sheet.getMemberCard() != null) {
					// û�д�����Ʒֱ�Ӱ�VIP�ۿ۴���
					MemberCard mc = pos.core.getPosSheet().getMemberCard();
					// if (mc.getDiscount() != 0 && mc.getDiscount() != 100
					// && mc.getPromDiscount() != 0
					// && mc.getPromDiscount() != 100) {
					if (mc.getDiscount() != 0 && mc.getDiscount() != 100
							&& mc.getPromDiscount() != 100) {
						DiscRate r = new DiscRate(Discount.VIPPROM, mc
								.getDiscount());
						sheet.setGoodsDisc(g, r);
					}
				}

				else {
					sheet.clearDiscount(g);
				}

			}

		}
	}

	private ArrayList filterDiscComplex(ArrayList list) {

		ArrayList result = new ArrayList();

		for (int i = 0; i < list.size(); i++) {
			DiscComplex disc = (DiscComplex) list.get(i);
			boolean isIn = false;
			for (int j = 0; j < result.size(); j++) {
				DiscComplex disc2 = (DiscComplex) result.get(j);
				if (disc.getGroupID().equals(disc2.getGroupID())) {
					isIn = true;
				}
			}
			if (!isIn) {
				result.add(disc);
			}
		}

		return result;
	}

	private ArrayList splitDiscComplex(ArrayList list) {

		ArrayList result = new ArrayList();

		for (int i = 0; i < list.size(); i++) {
			DiscComplex disc = (DiscComplex) list.get(i);
			result.addAll(Arrays.asList(disc.split()));
			// result.add(disc);
		}

		return result;
	}

	/**
	 * @return
	 */
	private ArrayList getComplexGoods() {
		ArrayList result = new ArrayList();
		ArrayList vgNoList = new ArrayList();
		SaleList saleList = sheet.getSalelst();
		for (int i = 0; i < saleList.size(); i++) {
			if (saleList.get(i).getType() != Sale.WITHDRAW
					&& ((saleList.get(i).getGoods().getPType()
							.equals(DiscCriteria.DISCCOMPLEX)) || (saleList
							.get(i).getGoods().getPType()
							.equals(DiscCriteria.BUYANDGIVE)))
					&& !vgNoList.contains(saleList.get(i).getGoods().getVgno())) {
				vgNoList.add(saleList.get(i).getGoods().getVgno());
				result.add(saleList.get(i).getGoods());
			}
		}
		return result;
	}

	/**
	 * �������� ��ҵ�����,ϵͳ�ڽ�����Ʒ��������ʱ��Ҫ�����¹���:
	 * ��¼��������.����д����ˮ���������ݲ����޸�,������ˮ�в���һ����¼������Ϊ�ļ�¼. ��Ʒ�ĸ����������ô������۳�������.
	 * ��Ʒ���ۿ�ͨ�����۳������й�.����,�������������,��Ҫ���¼��������Ʒ���ۿ�.
	 * 
	 * @param input
	 *            ����������Ʒ����(��Ʒ����,��Ʒ����)
	 * @return Ϊ�������������ɵļ�¼
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Sale correct(PosInputGoods input) throws FileNotFoundException,
			IOException {
		// Look for the sold items in sale_lst by code.
		// if sold number less than corrected number requested, refuse.
		// if sold number greater, correct it .
		SaleList salelst = getPosSheet().getSalelst();
		int sold = sheet.getSoldQty(input.getCode());

		if (sold < input.getQty()) {
			context.setWarning("�����������ô������۳�����,�����������!");
			return null;
		}

		try {
			Goods g = goods_lst.find(input.getCode());
			GoodsExt goodsExt = null;
			if (g == null) {
				goodsExt = goodsext_lst.find(input.getCode());
				if (goodsExt != null) {
					g = goodsExt;

				}
			}
			if (g == null) {
				context.setWarning("�Ҳ���Ҫ��������Ʒ,�����������!");
				return null;
			} else if (g.getX() == 1
					&& input.getQty() * 1000 != input.getMilliVolume()) {
				context.setWarning("����Ʒ��������Ϊ����,�����������!");
				return null;
			} else {
				if (goodsExt != null) {
					input.setQty(input.getQty() * goodsExt.getPknum());
				}

				clearDiscount(g);
				Sale correctSale = salelst.getItemBCode(input.getCode());

				Sale sale = new Sale(g, -input.getQty(), Sale.CORRECT);
				sale.setOriginalCode(input.getOrgCode());
				sale.setColorSize(input.getColorSize());
				sale.setPlaceno(context.getPlaceno());
				sale.setAuthorizer(context.getAuthorizerid());

				if (input.getGoodsType() == Goods.WEIGHT_VALUE) {
					sale.setStdValue(-input.getCents());
					sale.setFactValue(-input.getCents());
				}

				long cordisc = correctSale.getDiscValue();
				long inqty = input.getQty();
				long totalqty = correctSale.getQty();
				long stdprice = correctSale.getStdPrice();
				long stdvalue = correctSale.getStdValue();
				long factvalue = correctSale.getFactValue();

				if (correctSale.getDiscType() == Discount.ALTPRICE) {
					sale
							.setDiscValue(new DiscPrice(Discount.ALTPRICE,
									-(correctSale.getStdPrice()
											* input.getQty() - (int) Math
											.rint(correctSale.getDiscValue()
													* 1.0 * input.getQty()
													/ correctSale.getQty()))));
				}
				if (correctSale.getDiscType() == Discount.SINGLE) {
					sale
							.setDiscValue(new DiscPrice(Discount.SINGLE,
									-(correctSale.getStdPrice()
											* input.getQty() - (int) Math
											.rint(correctSale.getDiscValue()
													* 1.0 * input.getQty()
													/ correctSale.getQty()))));
					sheet.setGoodsDisc(g,
							new DiscPrice(Discount.SINGLE, -(correctSale
									.getStdPrice()
									* input.getQty() - (int) Math
									.rint(correctSale.getDiscValue() * 1.0
											* input.getQty()
											/ correctSale.getQty()))));
				}
				if (correctSale.getDiscType() == Discount.TOTAL) {
					int percent = 0;
					percent = (int) Math.rint(100 - factvalue * 100.0
							/ stdvalue);
					DiscRate discrate = new DiscRate(Discount.TOTAL, percent);
					sale.setDiscount(discrate);
					sheet.setGoodsDisc(g, discrate);
				}
				if (correctSale.getDiscType() == Discount.MONEY) {
					long itemvalue = 0;
					itemvalue = (int) Math.rint(factvalue * 1.0 * inqty
							/ totalqty);
					DiscPrice discprice = new DiscPrice(Discount.MONEY,
							-itemvalue);
					sale.setDiscValue(discprice);
					sheet.setGoodsDisc(g, discprice);
				}

				Sale s = sheet.correct(sale);
				performDiscount(g);

				sheet.updateValue();
				dump();
				last_sold = null;
				return s;
			}
		} catch (RealTimeException ex) {

		}

		return null;
	}

	/**
	 * @param source
	 * @return
	 */
	private Object[] orderComplexByLevel(ArrayList source) {

		Object[] dest = source.toArray();
		for (int i = 0; i < dest.length - 1; i++) {
			for (int j = i + 1; j < dest.length; j++) {
				if (((DiscComplex) dest[i]).getLevel() < ((DiscComplex) dest[j])
						.getLevel()) {
					Object disc = dest[i];
					dest[i] = dest[j];
					dest[j] = disc;
				}
			}
		}
		return dest;
	}

	/**
	 * @param input
	 * @param baseprice
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RealTimeException
	 */
	public Sale withdraw(PosInputGoods input, int baseprice)
			throws FileNotFoundException, IOException, RealTimeException {
		PosConfig config = PosConfig.getInstance();
		Goods g = goods_lst.find(input.getCode());
		int qty_top = config.getInteger("MAXAMOUNT");
		int value_top = config.getInteger("MAXCASH") * 100;
		int sheetlen_top = config.getInteger("MAXITEM");
		int value_top_line = config.getInteger("MAXVALUE") * 100;
		int qty = input.getQty();
		/*
		 * //�˻�ʱ�����ж�,������ÿ����Ʒ����������޶�passed if (
		 * Math.abs(sheet.getValue().getValueTotal()) > value_top) {
		 * context.setWarning("�˻���Ʒ����ѳ�������,�뼰ʱ����,�����������!"); return null; }
		 */
		// �˻�ʱ�����ж�,������ÿ����Ʒ���������������
		if (sheet.getSaleLen() >= sheetlen_top) {
			context.setWarning("���˻����ѳ���,�뼰ʱ����,�����������!");
			return null;
		}

		/*
		 * if(baseprice != 0){ g.setPrice(baseprice); }
		 */
		if (g == null) {
			Goods goodsCut = goodsCutList.findCut(input.getCode());
			// GoodsCut goodsCut=
			// RealTime.getInstance().findGoodsCut(input.getCode());
			if (goodsCut != null)
				g = goodsCut;
		}
		// ����������Ʒ
		if (g == null) {
			// Goods goodsCut = goodsCutList.find(input.getCode());
			Goods goodsComb = goodsCombList.find(input.getCode());
			if (goodsComb != null)
				g = goodsComb;
		}

		GoodsExt goodsExt = null;
		if (g == null) {
			goodsExt = goodsext_lst.find(input.getCode());
			if (goodsExt != null) {
				g = goodsExt;
			}
		}

		if (g == null) {
			context.setWarning("��Ʒ�۸�����Ҳ������������Ʒ����,�����������!");
			return null;
		}
		// ɾ����Ʒ���˻�
		else if (g.getX() == 1
				&& input.getQty() * 1000 != input.getMilliVolume()) {
			context.setWarning("����Ʒ��������Ϊ����,�����������!");
			return null;
		} else {
			// �˻�ʱ�����ж�,������ÿ����Ʒ�����������ÿ����Ʒ����

			if (qty > qty_top * g.getX()) {
				context.setWarning("��Ʒ��������,�����������!");
				return null;
			}
			// �˻�ʱ�����ж�,������ÿ����Ʒ���۽��������ÿ�����۽��
			// int v = g.getPrice() * qty / g.getX();
			long v = (long) Math.round((long) g.getPrice() * (long) qty * 100
					/ (long) g.getX() + 50) / 100;

			// System.out.println("1 ��Ʒ�Ľ����v �� "+v);
			// System.out.println("1 ��Ʒ���������value_top�� "+value_top);
			// System.out.println("���ڴ˵��ܶ�Ϊsheet.getValue().getValueTotal() ��
			// "+sheet.getValue().getValueTotal());

			if (v < 0 || v > value_top_line) {
				context.setWarning("�˻���Ʒ������,�����������!");
				return null;
			}
			// System.out.println("�����Ӵ˱ʵ��ܶ�Ϊ��
			// "+(v-sheet.getValue().getValueTotal()));
			// if(Math.abs(v)+sheet.getValue().getValueTotal()>value_top){
			if (v - sheet.getValue().getValueTotal() > value_top) {
				context.setWarning("�˻���Ʒ����ѳ�������,�뼰ʱ����,�����������!");
				return null;
			}

			if (goodsExt != null) {
				input.setQty(input.getQty() * goodsExt.getPknum());
			}

			Sale sale = new Sale(g, -input.getQty(), Sale.WITHDRAW);
			sale.setAuthorizer(context.getAuthorizerid());
			sale.setPlaceno(context.getPlaceno());
			sale.setColorSize(input.getColorSize());
			sale.setOriginalCode(input.getOrgCode());

			if (input.getDeptid() != null
					&& input.getDeptid().equals(Goods.LOADOMETER)) {
				sale.setFactValue(-input.getCents());
				sale.setStdValue(-input.getCents());
			}

			sheet.withdraw(sale);
			sheet.updateValue();
			dump();
			last_sold = null;
			return sale;
		}
	}

	private boolean confirm(String s) {
		DialogConfirm confirm = new DialogConfirm();
		confirm.setMessage(s);
		confirm.show();

		return (confirm.isConfirm());
	}

	/**
	 * @param input
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Payment pay(PosInputPayment input) throws FileNotFoundException,
			IOException, RealTimeException {
		PosConfig config = PosConfig.getInstance();
		// ��ȡ����޶�
		int value_top = config.getInteger("MAXCASH") * 100;
		Payment p;
		// ���λ�ñ��֣����ӣ��۸�
		String curr_code = context.getCurrenCode();
		double rate = context.getCurrenRate();
		int value = input.getCents();
		int value_equiv = (int) Math.rint(value * rate);
		String media_no = input.getMediaNumber();
		// System.out.println("Media_no" + media_no);
		if (value_equiv > value_top) {
			PosContext.getInstance().setWarning("֧������ѳ���ϵͳ����,�����������!");
			return null;
		}

		if (input.getType() == Payment.CARDLOAN) {
			LoanCardProcess lcp = input.getLoanCardProcess();
			if (lcp == null) {
				PosContext.getInstance().setWarning("��Ч����,�����������!");
				return null;
			}

			if (!lcp.performPay()) {
				if (lcp.getExceptionInfo() != null) {
					PosContext.getInstance().setWarning(lcp.getExceptionInfo());
				} else {
					PosContext.getInstance().setWarning("��Ч����,�����������!");
				}
				return null;
			}
		}

		if (input.getType() == Payment.ICCARD) {
			ICCardProcess icp = input.getICCardProcess();
			if (icp == null) {
				PosContext.getInstance().setWarning("��Ч����,�����������!");
				return null;
			}
			if (!icp.performPay()) {
				if (icp.getExceptionInfo() != null) {
					PosContext.getInstance().setWarning(icp.getExceptionInfo());
				} else {
					PosContext.getInstance().setWarning("��Ч����,�����������!");
				}
				return null;
			}
		}

		// add by lichao 2004/08/02
		if (input.getType() == 'R') {
			if (value_equiv > getValue().getValueTotal()) {
				System.out.println("���п�֧�����ܴ���Ӧ�ս��" + "\n���п���" + value_equiv
						+ "\nӦ��Ϊ:" + getValue().getValueTotal());
				setWarning("���п�֧�����ܴ���Ӧ�ս��,�����������!");
				return null;
			}
		}
		if (sheet.getShopCard() != null && "".equals(media_no))
			media_no = sheet.getShopCard().getCardNO();
		if (sheet.getCouponNO() != null && !sheet.getCouponNO().equals("")) {
			if (sheet.getCouponValue() > 0
					&& getValue().getValueTotal() > sheet.getCouponValue()) {
				setWarning("Ӧ�ս��ܴ�������һ�ȯ����ֵ,�����������!");
				return null;
			}
			// media_no=sheet.getCouponNO();
			// input.setType('Q');
			// value=sheet.getValue().getValueToPay();
			// value_equiv=(int) Math.rint(value * rate);
		}

		// try {
		// // ��ȯ�һ���ʱ�򣬲��ܻ��֡�
		// if (sheet.getMemberCard() != null && input.getCents() != 0 &&
		// sheet.getCardChange() == null)
		// {
		// sheet.handlePoint();
		// //���֧��������ȯ��Ҫ��Ӧ��ȥ����
		// if(input.getType()== Payment.couponPay)
		// sheet.couponPoint((long)value_equiv);
		// }
		// } catch (RealTimeException e) {
		// e.printStackTrace();
		// this.setWarning(e.getMessage());
		// sheet.setPaymentList(new PaymentList());
		// throw (e);
		// } catch (IOException e) {
		// e.printStackTrace();
		// PosContext.getInstance().setWarning("��������������ʧ�ܣ���������������Ի�ȡ��!");
		// sheet.setPaymentList(new PaymentList());
		// return null;
		// }

		// ����ȯ������
		if (sheet.couponSales.size() > 0) {
			if (!"1".equals(performCouponSale(sheet.couponSales))) {
				return null;
			}
			// if(sheet.couponSales.getUpdateType().equals("f")){
			if (sheet.isCouponEncash) {
				input.setType('Q');
				value = sheet.getValue().getValueTotal()
						- sheet.couponSales.getTotalValue();
				value_equiv = (int) Math.rint(value * rate);
				for (int i = 0; i < sheet.couponSales.size(); i++) {
					int couponValue = (int) Math.rint(sheet.couponSales.get(i)
							.getPrice().doubleValue() * 100);
					int equiv = (int) Math.rint(couponValue * rate);
					Payment ps = new Payment(Payment.PAY, input.getType(),
							curr_code, couponValue, equiv, sheet.couponSales
									.get(i).getCouponID());
					sheet.pay(ps);
					cashbasket.put(input.getType(), curr_code, couponValue);

				}
			}
		}

		// ����ȯ������
		if (sheet.couponPay.size() > 0) {
			if (!"1".equals(performCouponPay(sheet.couponPay))) {
				sheet.couponPay = new CouponSaleList();
				return null;
			}
			// ɾ��
			sheet.couponPay = new CouponSaleList();
		}

		// ------------------------

		// ����һ��֧�����񣨣�
		p = new Payment(Payment.PAY, input.getType(), curr_code, value,
				value_equiv, media_no);
		// ���۵�
		sheet.pay(p);
		// װ����
		cashbasket.put(input.getType(), curr_code, value);

		// -------------------------
		sheet.updateValue();
		dump();
		sheet.setCouponNO("");
		sheet.setCouponValue(0);
		// this.getPosSheet().isCouponSale=false;
		// this.getPosSheet().isCouponEncash=false;
		last_sold = null;
		return p;
	}

	/**
	 * @param input
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Payment cashin(PosInputPayment input) throws FileNotFoundException,
			IOException {
		String curr_code = context.getCurrenCode();
		double rate = context.getCurrenRate();
		int value_top = PosConfig.getInstance().getInteger("CASH_LIMIT") * 100;
		int value = input.getCents();
		int value_equiv = (int) (value * rate);

		if (value_equiv > value_top) {
			PosContext.getInstance().setWarning("������Ǯ��������,�����������!");
			return null;
		}

		Payment p = new Payment(Payment.CASHIN, input.getType(), curr_code,
				value, value_equiv);
		sheet.addPayment(p);
		cashbasket.put(input.getType(), curr_code, value);
		dump();
		writeJournal();
		last_sold = null;
		return p;
	}

	/**
	 * @param input
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Payment cashoutRa(PosInputPayment input, String cardno)
			throws FileNotFoundException, IOException {
		String curr_code = context.getCurrenCode();
		double rate = context.getCurrenRate();
		int value = input.getCents();
		int box_value = 0;
		int value_equiv = (int) (value * rate);

		// ԭʼ֧����Ϣ
		Payment p = new Payment(Payment.RaCard, input.getType(), curr_code,
				value, value_equiv, input.getMediaNumber());
		// ��ֵ����
		Payment p1 = new Payment(Payment.RaCard, Payment.CARDSHOP, "RMB", 0, 0,
				cardno);

		sheet.addPayment(p);
		sheet.addPayment(p1);
		cashbasket.put(input.getType(), curr_code, value);
		dump();
		writeJournal();
		last_sold = null;
		return p;
	}

	/**
	 * @param input
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Payment cashout(PosInputPayment input) throws FileNotFoundException,
			IOException {
		String curr_code = context.getCurrenCode();
		double rate = context.getCurrenRate();
		int value = input.getCents();
		int box_value = 0;
		int value_equiv = (int) (value * rate);

		CashBox box = cashbasket.getBox(input.getType(), curr_code);
		if (box != null)
			box_value = box.getValue();
		if (box == null || box_value < value) {
			PosContext.getInstance().setWarning("�����ڵĽ���,���������Ƿ���ȷ,�����������!");
			return null;
		}

		Payment p = new Payment(Payment.CASHOUT, input.getType(), curr_code,
				-value, -value_equiv);
		sheet.addPayment(p);
		cashbasket.put(input.getType(), curr_code, -value);
		dump();
		writeJournal();
		last_sold = null;
		return p;
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void dump() throws FileNotFoundException, IOException {
		sheet.dump(sheetFile());
		context.setWorkDate(new Day());
		context.dump();
		cashbasket.dump(FILE4BASKET);
	}

	/**
	 * �ѵ�ǰ��"�������۵�"�е�����д�뵽����Ӳ�̵��ļ�(��Ϊfname)��.
	 * 
	 * @param fname
	 *            ���sheet������ļ�����.
	 */
	public void dumpSheet(String fname) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(fname));
			out.writeObject(sheet);
			out.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}

	/**
	 * ����Ϊfname ���ļ���ȡ��sheet ����.
	 * 
	 * @param fname
	 */
	public void loadSheet(String fname) {
		ObjectInputStream in = null;
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(fname);
			in = new ObjectInputStream(fin);
			sheet = (PosSheet) in.readObject();
			in.close();
		} catch (java.io.FileNotFoundException e) {
			System.out.println("WARNING: " + fname + " Not Found.");
			sheet = new PosSheet();
		} catch (Exception e) {
			System.out.println("WARNING: " + e);
			try {
				if (fin != null) {
					fin.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			FileUtil.fileError(fname);
			sheet = new PosSheet();

		}
		last_sold = null;
	}

	/**
	 * @param type
	 * @return
	 */
	public Payment closeSheetWithoutMoney(int type) {
		int value_unpaid = sheet.getValue().getValueUnPaid();
		Payment payment = new Payment(type, type, value_unpaid);
		sheet.updateValue();
		sheet.pay(payment);
		sheet.setTrainFlag(9);
		writeJournal();
		last_sold = null;
		return payment;
	}

	/**
	 * 
	 */
	public void closeSheet(PosInputPayment input) {
		// �õ�������
		int value_topay = sheet.getValue().getValueToPay();
		if (value_topay < 0) {
			// CHANGE ����
			sheet.pay(Payment.CHANGE, value_topay);
			cashbasket.put(Payment.CASH, "RMB", value_topay);
		}

		int value_unpaid = sheet.getValue().getValueUnPaid();
		if (value_unpaid != 0)
			sheet.pay(Payment.PSEUDO, value_unpaid);
		if (context.isTraining()) {
			sheet.setTrainFlag(1);
		}
		// writeJournal();
		// д�뱾��Ӳ��
		writeJournal();
		sheet.couponSales = new CouponSaleList();
		this.getPosSheet().isCouponSale = false;
		this.getPosSheet().isCouponEncash = false;
		last_sold = null;
	}

	// ���Ԥ����д�뱾��Ӳ��
	public void closeSheets(PosInputPayment input) {
		// �õ�������o
		int value_topay = sheet.getValue().getValueToPay();
		if (value_topay < 0) {
			// CHANGE ����
			sheet.pay(Payment.CHANGE, value_topay);
			cashbasket.put(Payment.CASH, "RMB", value_topay);
		}

		int value_unpaid = sheet.getValue().getValueUnPaid();
		if (value_unpaid != 0)
			sheet.pay(Payment.PSEUDO, value_unpaid);
		if (context.isTraining()) {
			sheet.setTrainFlag(1);
		}
		// writeJournal();
		// д�뱾��Ӳ��
		writeJournal(input);
		sheet.couponSales = new CouponSaleList();
		this.getPosSheet().isCouponSale = false;
		this.getPosSheet().isCouponEncash = false;
		last_sold = null;
	}

	/**
	 * 
	 */
	public void deleteSheet() throws Exception {

		// ------------------------------
		sheet.setCouponNO("");
		sheet.couponSales = new CouponSaleList();
		sheet.setAsDeleted();

		writeJournal();
		last_sold = null;

	}

	public void deleteOrder() throws Exception {

		// ------------------------------
		sheet.setCouponNO("");
		sheet.couponSales = new CouponSaleList();
		sheet.setAsDeleted();
		writeJournal(true);
		last_sold = null;

	}

	/**
	 * 
	 */
	public void deleteFindresult() {
		try {
			sheet.setAsDeleted();
		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use
			// Options | File Templates.
		}
		last_sold = null;
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void openSheet() throws FileNotFoundException, IOException {
		sheet = new PosSheet();
		context.incrSheetid();
		context.setOrderidOld("0");
		// ---����������ˮ��������������ˮ��������������������ˮ����-------
		int resetSheetCount = PosConfig.getInstance().getInteger(
				"RESETSHEET_COUNT");
		if (resetSheetCount == 0)
			resetSheetCount = 50000;
		else if (context.getSheetid() > resetSheetCount)
			context.setSheetid(1);
		// ------------------------------------
		dump();
		last_sold = null;
	}

	public void openOrder() throws FileNotFoundException, IOException {
		sheet = new PosSheet();
		context.incrSheetid();
		context.incrOrder_id();
		context.setOrderidOld("0");
		// ---����������ˮ��������������ˮ��������������������ˮ����-------
		int resetSheetCount = PosConfig.getInstance().getInteger(
				"RESETSHEET_COUNT");
		if (resetSheetCount == 0)
			resetSheetCount = 50000;
		else if (context.getSheetid() > resetSheetCount)
			context.setSheetid(1);
		// ------------------------------------
		dump();
		last_sold = null;
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void findpriceSheet() throws FileNotFoundException, IOException {
		sheet = new PosSheet();
		dump();
		last_sold = null;
	}

	/**
	 * @return
	 */
	public SheetValue getValue() {
		return sheet.getValue();
	}

	/**
	 * @return
	 */
	public int getValue4Indicator() {
		return sheet.getValue4Indicator();
	}

	/**
	 * @return
	 */
	public int getCount4Indicator() {
		return sheet.getCount4Indicator();
	}

	/**
	 * @return
	 */
	public boolean isSheetEmpty() {
		return sheet.isEmpty();
	}

	/**
	 * @param i
	 * @return
	 */
	public Sale getSale(int i) {
		return sheet.getSale(i);
	}

	/**
	 * @param i
	 * @return
	 */
	public Sale getFalseSale(int i) {
		return sheet.getFalseSale(i);
	}

	/**
	 * @param i
	 * @return
	 */
	public Payment getPayment(int i) {
		return sheet.getPayment(i);
	}

	/**
	 * @return
	 */
	public int getSaleLen() {
		return sheet.getSaleLen();
	}

	/**
	 * @return
	 */
	public int getFalseSaleLen() {
		return sheet.getFalseSaleLen();
	}

	/**
	 * @return
	 */
	public int getPayLen() {
		return sheet.getPayLen();
	}

	/**
	 * @return
	 */
	public PaymentList getPaylst() {
		return sheet.getPaylst();
	}

	/**
	 * @param code
	 */
	public void setCurrency(String code) {
		Exchange e = exch_lst.find(code);
		if (e == null)
			return;
		context.setCurrency(code, e.getRate());
	}

	/**
	 * 
	 */
	public void writeJournal(PosInputPayment input) {
		final String jfile = context.getName4Journal();
		Element root = new Element("journal");
		// root.addContent(new SheetElement(sheet));

		// д���ļ�
		root.addContent(new SheetElement(sheet, input));
		root.addContent(new SheetElement(context, 1));
		FileOutputStream owriter;
		try {
			owriter = new FileOutputStream("journal" + File.separator + jfile);
			XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
			outputter.setTextTrim(true);
			outputter.output(new Document(root), owriter);
			owriter.close();

			JournalLog journalLog = new JournalLog();
			journalLog.setJournalName(jfile);
			journalLog.setCreateTime(Formatter.getDateFile(new Date()));
			journalLog.setStatus(JournalLog.CREATE);
			JournalLogList.getInstance().addLog(journalLog);
			JournalLogList.dump();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* add by lichao */
		try {

			PosContext con = PosContext.getInstance();
			String srcfile = "journal" + File.separator + con.getName4Journal();
			File dir = new File("reprint/");
			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdir();
			}
			String destfile = "reprint/reprintsheet.xml";

			FileChannel infile = new FileInputStream(srcfile).getChannel();
			FileChannel outfile = new FileOutputStream(destfile).getChannel();
			infile.transferTo(0, infile.size(), outfile);
			infile.close();
			outfile.close();
		} catch (java.io.EOFException e) {
			System.out.println("EOF!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		pos.activeUploader();

	}

	public void writeJournal(boolean input) {
		final String jfile = context.getName4Journal();
		Element root = new Element("journal");
		// root.addContent(new SheetElement(sheet));

		// д���ļ�
		root.addContent(new SheetElement(sheet, input));
		root.addContent(new SheetElement(context, 2));
		FileOutputStream owriter;
		try {
			owriter = new FileOutputStream("journal" + File.separator + jfile);
			XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
			outputter.setTextTrim(true);
			outputter.output(new Document(root), owriter);
			owriter.close();

			JournalLog journalLog = new JournalLog();
			journalLog.setJournalName(jfile);
			journalLog.setCreateTime(Formatter.getDateFile(new Date()));
			journalLog.setStatus(JournalLog.CREATE);
			JournalLogList.getInstance().addLog(journalLog);
			JournalLogList.dump();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* add by lichao */
		try {

			PosContext con = PosContext.getInstance();
			String srcfile = "journal" + File.separator + con.getName4Journal();
			File dir = new File("reprint/");
			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdir();
			}
			String destfile = "reprint/reprintsheet.xml";

			FileChannel infile = new FileInputStream(srcfile).getChannel();
			FileChannel outfile = new FileOutputStream(destfile).getChannel();
			infile.transferTo(0, infile.size(), outfile);
			infile.close();
			outfile.close();
		} catch (java.io.EOFException e) {
			System.out.println("EOF!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		pos.activeUploader();

	}

	public void writeJournal() {
		final String jfile = context.getName4Journal();
		Element root = new Element("journal");
		root.addContent(new SheetElement(sheet));
		root.addContent(new SheetElement(context));

		FileOutputStream owriter;
		try {
			owriter = new FileOutputStream("journal" + File.separator + jfile);
			XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
			outputter.setTextTrim(true);
			outputter.output(new Document(root), owriter);
			owriter.close();

			JournalLog journalLog = new JournalLog();
			journalLog.setJournalName(jfile);
			journalLog.setCreateTime(Formatter.getDateFile(new Date()));
			journalLog.setStatus(JournalLog.CREATE);
			JournalLogList.getInstance().addLog(journalLog);
			JournalLogList.dump();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			PosContext con = PosContext.getInstance();
			String srcfile = "journal" + File.separator + con.getName4Journal();
			File dir = new File("reprint/");
			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdir();
			}
			String destfile = "reprint/reprintsheet.xml";

			FileChannel infile = new FileInputStream(srcfile).getChannel();
			FileChannel outfile = new FileOutputStream(destfile).getChannel();
			infile.transferTo(0, infile.size(), outfile);
			infile.close();
			outfile.close();
		} catch (java.io.EOFException e) {
			System.out.println("EOF!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (context.isOnLine()) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					String ip = context.getServerip();
					int port = context.getPort();
					synchronized (pos.uploadLock) {
						JournalWriter writer = new JournalWriter(ip, port);
						writer.writeJournal(jfile);
						JournalManager journalManager = new JournalManager();
						journalManager.upload();
					}
				}
			});
			t.start();
		}

	}

	/**
	 * @param action
	 * @param flag
	 * @param cashierid
	 */
	public void writelog(String action, String flag, int cashierid) {
		final String logfile = context.getNameLog();
		Element root = new Element("log");
		if (cashierid == 0) {
			root.addContent(new SheetElement(context, action, flag));
		} else {
			root.addContent(new SheetElement(context, action, flag, cashierid));
		}

		FileOutputStream owriter;
		try {
			owriter = new FileOutputStream("poslog" + File.separator + logfile);
			XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
			outputter.setTextTrim(true);
			outputter.output(new Document(root), owriter);
			owriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param action
	 * @param flag
	 * @param cashierid
	 */
	public void writelogexit(String action, String flag, int cashierid) {
		final String logfile = context.getNameLog();
		Element root = new Element("log");
		if (cashierid == 0) {
			root.addContent(new SheetElement(context, action, flag));
		} else {
			root.addContent(new SheetElement(context, action, flag, cashierid));
		}

		FileOutputStream owriter;
		try {
			owriter = new FileOutputStream("poslog" + File.separator + logfile);
			XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
			outputter.setTextTrim(true);
			outputter.output(new Document(root), owriter);
			owriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread t = new Thread(new Runnable() {
			public void run() {
				String ip = context.getServerip();
				int port = context.getPort();
				LogWriter log = new LogWriter(ip, port);
				log.writeLog(logfile);
				LogManager logManager = new LogManager();
				logManager.upload();
			}

		});

		t.start();

	}

	/**
	 * ȡ��ǰ�����ļ�������.
	 * 
	 * @return ��ǰ����ʹ�õĹ����ļ�.
	 */
	public String sheetFile() {
		return context.sheetFile();
	}

	/**
	 * @return SheetBrief ����. �ڸ�������, ����ȫ���ѹ����POS������������.
	 */
	public SheetBrief[] getSheetBrief() {
		SheetBrief[] briefs = new SheetBrief[sheet_lst.length];
		for (int i = 0; i < sheet_lst.length; i++) {
			String file = sheet_lst[i];
			if (!file.equals(this.sheetFile())) {
				PosSheet t = PosSheet.load(file);
				briefs[i] = new SheetBrief(t);
			}
		}
		return briefs;
	}

	/**
	 * �趨"��ǰ�ҵ���". POS��Ҫ��ʱ��ʾ"��ǰ�ҵ���". ��ϵͳ��,sheet_lst �б�����POS����ʹ�õ����й����ļ���.
	 * ÿ�����ҵ������,ϵͳ���sheet_lst �����е��ļ����м��,���¼������ǰ�ҵ���,���޸�context�еĹҵ���.
	 */
	private void setHeldCount() {
		int count = 0;
		for (int i = 0; i < sheet_lst.length; i++) {
			String file = sheet_lst[i];
			if (!file.equals(sheetFile())) {
				PosSheet t = PosSheet.load(file);
				if (t != null && !t.isEmpty())
					count++;
			}
		}
		context.setHeldCount(count);
	}

	/**
	 * @return
	 */
	public int holdSheet() {
		boolean done = false;
		String new_file = "";
		int n = -1; // �˱�����¼����Ĺ����ļ���sheet_lst �е�λ��.

		for (int i = 0; i < MAX_SHEETS; i++) {
			if (!PosSheet.isSheetInFile(sheet_lst[i])) {
				new_file = sheet_lst[i];
				context.setSheetFile(new_file);
				done = true;
				n = i;
				break;
			}
		}
		setHeldCount();

		if (done) {
			sheet = new PosSheet();
			sheet.dump(new_file);
			return n;
		} else {
			return -1;
		}
	}

	/**
	 * ��ҵ�����.
	 * 
	 * @param n
	 *            ��Ҫ��"���"������ļ����.
	 */
	public void unholdSheet(int n) {
		String new_file = sheet_lst[n]; // ���ݹҵ����ȡ���ҵ��ļ���.
		context.setSheetFile(new_file); // �޸ĵ�ǰ�����ļ���.
		sheet = PosSheet.load(new_file); // ���ļ��б����sheet����װ�뵱ǰsheet��.
		setHeldCount(); // ��������"��ǰ�ҵ�����".
	}

	/**
	 * 
	 */
	public void unholdFirst() {
		for (int i = 0; i < sheet_lst.length; i++) {
			String file = sheet_lst[i];
			if (!file.equals(sheetFile()) && PosSheet.isSheetInFile(file))
				unholdSheet(i);
		}
	}

	/**
	 * @param warning
	 */
	public void setWarning(String warning) {
		context.setWarning(warning);
	}

	/**
	 * @param id
	 */
	public void setCashierid(String id) {
		context.setCashierid(id);
	}

	/**
	 * 
	 */
	private void createSheetFile() {
		for (int i = 0; i < sheet_lst.length; i++)
			if (!(new File(sheet_lst[i])).exists()) {
				PosSheet t = new PosSheet();
				t.dump(sheet_lst[i]);
			}
	}

	/**
	 * 
	 */
	public void updateValue() {
		sheet.updateValue();
	}

	/**
	 * ����
	 */
	private void computeFavor() {
		sheet.computeFavor(favor_lst);
	}

	/**
	 * 
	 */
	private void consumeFavor() {
		sheet.consumeFavor(favor_lst);
	}

	/**
	 * @param disc
	 */
	private void consumeFavor(DiscComplex disc) {
		sheet.consumeFavor(disc);
	}

	/**
	 * @return
	 */
	public PosSheet getPosSheet() {
		return sheet;
	}

	/**
	 * @return
	 */
	public PosContext getPosContext() {
		return context;
	}

	/**
	 * @return
	 */
	public GoodsList getGoodList() {
		return goods_lst;
	}

	/**
	 * @return
	 */
	public Sale getLastSale() {
		return last_sold;
	}

	/**
	 * �ж���Ʒ�Ƿ���Ҫ¼�벹����Ϣ.�÷�����Ҫ�����Ʒ.��Ʒ����ʱ,Ҫ��¼��������Ϣ:�͵���. �жϷ���: ����Ʒ���ϱ��в�ѯ��Ʒ��deptid,
	 * Ȼ��鿴���ñ��Indicator �����Ƿ��������Ʒ��deptid. �������,��Ҫ��¼�벹����Ϣ,����Ҫ��¼��.
	 * 
	 * @param code
	 *            ��Ʒ����.
	 * @return true Ҫ��¼�벹����Ϣ; <br/>false ��Ҫ��¼�벹����Ϣ;
	 * @throws RealTimeException
	 */
	public boolean requireDetail(String code) throws RealTimeException {
		Goods g = goods_lst.find(code);
		PosConfig config = PosConfig.getInstance();
		if (g != null && config.isIndicatorDept(g.getDeptid()))
			return true;
		return false;
	}

	/**
	 * @return
	 */
	public boolean exceedCashLimit() {
		return cashbasket.exceedCashLimit();
	}

	/**
	 * @return
	 */
	public boolean exceedCashMaxLimit() {
		return cashbasket.exceedCashMaxLimit();
	}

	/**
	 * @return
	 */
	public int CompareLimit() {
		return cashbasket.CompareLimit();
	}

	/**
	 * 
	 */
	public void resetCashBasket() {
		cashbasket.reset();
	}

	/**
	 * @return
	 */
	public CashBasket getCashBasket() {
		return cashbasket;
	}

	/**
	 * @param code
	 * @return
	 * @throws RealTimeException
	 */
	public Goods findGoods(String code) throws RealTimeException {
		return goods_lst.find(code);
	}

	public GoodsProduct findGoodsPruduct(String code) throws RealTimeException {
		return prop_lst.find(code);
	}

	/**
	 * @param cardno
	 * @return
	 * @throws RealTimeException
	 */
	public int getLoanCardDiscCount(String cardno) throws RealTimeException {
		return goods_lst.getLoanCardDiscCount(cardno);
	}

	/**
	 * @param code
	 * @return
	 * @throws RealTimeException
	 */
	public GoodsExt findGoodsExt(String code) throws RealTimeException {
		return goodsext_lst.find(code);
	}

	/**
	 * <code>MAX_SHEETS</code> POS������֧�ֵ�"���۹�����"��������.
	 */
	final public int MAX_SHEETS;

	/**
	 * <code>FILE4BASKET</code> ���Ǯ����Ϣ���ļ���.
	 */
	final private String FILE4BASKET = "work" + File.separator + "basket.xml";

	/**
	 * <code>exch_lst</code> ���ʱ�.
	 */
	private ExchangeList exch_lst;

	/**
	 * <code>goods_lst</code> ��Ʒ���ϱ�.
	 */
	private GoodsList goods_lst;

	private GoodsProductList prop_lst;

	/**
	 * <code>goodsext_lst</code> ��Ʒ��չ���ϱ�.
	 */
	private GoodsExtList goodsext_lst;

	/**
	 * <code>discount_lst</code> ��Ʒ�ۿ۱�.
	 */
	private DiscountList discount_lst;

	/**
	 * <code>favor_lst</code> ����ۿ۱�.
	 */
	private DiscComplexList favor_lst;

	/**
	 * <code>bulk_lst</code>
	 */
	private BulkPriceList bulk_lst;

	/**
	 * <code>context</code> POS��������������Ϣ.
	 */
	private PosContext context;

	/**
	 * <code>cashbasket</code> Ǯ����Ϣ.
	 */
	private CashBasket cashbasket;

	/**
	 * <code>sheet</code> ��ǰ����"��".
	 */
	private PosSheet sheet;

	/**
	 * <code>sheet_lst</code> ��sheet��Ӧ�Ĺ����ļ����嵥.
	 */
	private String[] sheet_lst;

	/**
	 * <code>last_sold</code> ���һ�ʽ������۵������ۼ�¼.
	 */
	private Sale last_sold = null;

	// ��Ʒ�۸�ӳ���
	public static HashMap priceListMap = new HashMap();

	public static HashMap barcodeMap = new HashMap();

	public AccurateList accurateList;

	public GoodsCombList goodsCombList;

	public GoodsCutList goodsCutList;

	public CouponListLargess couponListLargess;

	// �����Ʒ
	public CouponLargessList couponLargessList;

	// Ԥ�����������
	int Moner_line = 15000000;

	public PosSheet getSheet() {
		return sheet;
	}

	public void setSheet(PosSheet sheet) {
		this.sheet = sheet;
	}

}