package com.royalstone.pos.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.royalstone.pos.card.ICCardProcess;
import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.card.MemberCardProcess;
import com.royalstone.pos.card.SHCardProcess;
import com.royalstone.pos.common.Goods;
import com.royalstone.pos.common.Operator;
import com.royalstone.pos.common.Payment;
import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.common.PosFunction;
import com.royalstone.pos.common.Privilege;
import com.royalstone.pos.coupon.CouponEnCash;
import com.royalstone.pos.coupon.CouponException;
import com.royalstone.pos.coupon.CouponMgr;
import com.royalstone.pos.coupon.CouponMgrImpl;
import com.royalstone.pos.coupon.CouponSale;
import com.royalstone.pos.gui.DialogInfo;
import com.royalstone.pos.gui.DispPrice;
import com.royalstone.pos.gui.GetBankCardType;
import com.royalstone.pos.gui.LoadoConfirm;
import com.royalstone.pos.gui.Loadometer;
import com.royalstone.pos.gui.MSRInput;
import com.royalstone.pos.gui.OilInput;
import com.royalstone.pos.hardware.POSCashDrawer;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.PosConfig;
import com.royalstone.pos.util.UserCancelException;
import com.royalstone.pos.util.UserExitException;
import com.royalstone.pos.util.Value;
import com.royalstone.pos.util.Volume;
import com.royalstone.pos.util.getCardType;
//import com.royalstone.pos.gui.BankTypeInput;

/**
   @version 1.0 2004.05.14
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

public class PosDevIn {

	public PosDevIn(InputStream inputStream, PosDevOut out) {
		this.posInputStream = inputStream;
		this.out = out;

		bmap = new BarcodeMap();
		bmap.fromXML("pos.xml");//zhouzhou Add 20091104
		keymap = new int[256];
	}

	public PosDevIn() {
		bmap = new BarcodeMap();
        bmap.fromXML("pos.xml");//zhouzhou Add 20091104
		keymap = new int[256];
	}

	public static PosDevIn getInstance(
		InputStream inputStream,
		PosDevOut out) {
		if (in == null)
			in = new PosDevIn(inputStream, out);
		return in;
	}

	public static PosDevIn getInstance() {
		if (in == null)
			in = new PosDevIn();
		return in;
	}

	public void init() {
		loadKeyMap("pos.xml");
		PosConfig config = PosConfig.getInstance();
		String radix = config.getString("RADIX_TYPE");
		payment_base = 1;
		if (radix != null && radix.equals("ON"))
			payment_base = 100;
	}

	// load from xml doc.
	public void loadKeyMap(String xmldoc) {
		Document doc;
		try {
			doc = (new SAXBuilder()).build(xmldoc);
			Element root = doc.getRootElement();
			Element config = root.getChild("keymap");
			List list = config.getChildren("key_fun");
			for (int i = 0; i < list.size(); i++) {
				Element item = (Element) list.get(i);
				Element elm_value = item.getChild("value");
				Element elm_fun = item.getChild("fun");

				int v = Integer.parseInt(elm_value.getTextTrim());
				int f = Integer.parseInt(elm_fun.getTextTrim());
				keymap[v] = f;
			}

			for (int i = 0; i < hotkeymap.length; i++) {
				hotkeymap[i] = null;
				hotkeylst[i] = null;
			}
			Element elm_hot = root.getChild("hotkeymap");
			List list_hot = null;
			if (elm_hot != null)
				list_hot = elm_hot.getChildren("hotkey");

			if (list_hot != null)
				for (int i = 0; i < list_hot.size(); i++) {
					Element item = (Element) list_hot.get(i);
					Element elm_value = item.getChild("value");
					Element elm_code = item.getChild("code");
					//Element elm_flag = item.getChild("flag");
					int v = Integer.parseInt(elm_value.getTextTrim());
					//int flag = Integer.parseInt(elm_flag.getTextTrim());
                    int flag=1;
					String code = elm_code.getTextTrim();
					hotkeymap[v] = code;
					hotkey = new Hotkey(v, code, flag);
					//System.out.println("Hotkey:"+hotkey.toString());
					hotkeylst[v] = hotkey;
				}

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// reserved func key
		keymap['\n'] = PosFunction.ENTER;
		keymap['\b'] = PosFunction.BACKSPACE;
		keymap['.'] = PosFunction.POINT;
		keymap['='] = '=';
		keymap[';'] = ';';
		for (int i = '0'; i <= '9'; i++)
			keymap[i] = i;
	}

	/**
	   @param cents State the POS system in. POsDevIn whill process that users type according to which state POS is in.
	   @return
	   PosInput	If the user presses a function key.
	   PosInputGoods	If the user input goods info.
	   PosInputPayment	If user input payment info.
	 */

	public PosInput getInput(int cents) {

		return null;
	}

	private String getBankCardNo(int maxlen) throws UserCancelException {

		MSRInput msrInput = new MSRInput("请刷银行卡：","bank");
//msrInput.type="bank";
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

    public String getCouponNo(int maxlen) throws UserCancelException {

        MSRInput msrInput = new MSRInput("请输入券号：","loan");
//msrInput.type="bank";
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


	public PosInput getInput() {
		String buffer = "";
		boolean qty_set = false;
		int c;
		double amount_top =
			(double) PosConfig.getInstance().getInteger("MAXAMOUNT");
		String bank_card_type =
			(String) PosConfig.getInstance().getString("BANK_CARD_TYPE");
		if (amount_top < 1 || amount_top > 1000000)
			amount_top = 10000;
		double amount = 1;
		DecimalFormat df_qty = new DecimalFormat("#.###");

		out.clearInputLine();
		while (true) {
			c = getKey();
			if (keyFunc(c) >= '0' && keyFunc(c) <= '9') {
				buffer += (char) keyFunc(c);
				if (buffer.length() > 18)
					return cancelInput("输入超长,按清除键继续!");
				buffer = normalizeBuffer(buffer);
				out.setInputLine(buffer);

			}else if (keyFunc(c) == PosFunction.BIZERO) {
				buffer += "00";
				if (buffer.length() > 18)
					return cancelInput("输入超长,按清除键继续!");
				buffer = normalizeBuffer(buffer);
				out.setInputLine(buffer);

			} else if (keyFunc(c) == PosFunction.CLEAR) {
				buffer = "";
				out.setInputLine(buffer);

			}else if (keyFunc(c) == PosFunction.CANCEL) {
				return new PosInput(PosFunction.CANCEL);
			} else if (keyFunc(c) == PosFunction.SHIFT) {
				return new PosInput(PosFunction.SHIFT);
			} else if (keyFunc(c) == PosFunction.BACKSPACE) {
				if (buffer.length() > 0)
					buffer = buffer.substring(0, buffer.length() - 1);
				out.setInputLine(buffer);

			} else if (keyFunc(c) == PosFunction.POINT) {
				if (payment_base == 1)
					return cancelInput("无效操作,按清除键继续!");
				else if (buffer.indexOf(".") < 0) {
					buffer += ".";
					out.setInputLine(buffer);
				}

			} else if (keyFunc(c) == PosFunction.QUANTITY) {
				if (buffer.length() == 0 || qty_set)
					return cancelInput("无效操作,按清除键继续!");
				if (buffer.length() > 7)
					return cancelInput("输入数量超长,按清除键继续!");
				amount = atof(buffer);
				if (amount > amount_top)
					return cancelInput("数量超标,按清除键继续!");
				if (amount <= 0)
					return cancelInput("数量不得为零,按清除键继续!");
				qty_set = true;
				buffer = "";
				out.prompt("数量: " + df_qty.format(amount));
				out.clearInputLine();

			} else if (
				keyFunc(c) == PosFunction.ENTER && buffer.length() == 0) {
				// do nothing
			} else if (
				keyFunc(c) == PosFunction.ENTER && buffer.length() > 0) {
				// if code matches weight format, convert it first.
				if (bmap.matches(buffer)) {
					return bmap.convert(buffer);
				} else {
					PosInputGoods inputg = new PosInputGoods(buffer, amount);
					return inputg;
				}

			} else if (keyFunc(c) == PosFunction.CASH) {
				if (buffer.length() == 0) {
//					if (pos.core.getPosSheet().getValue().getValueToPay()
//						>= 0) {
//						return new PosInputPayment(
//							pos.core.getPosSheet().getValue().getValueToPay(),
//							Payment.CASH);
//					} else {
//						return cancelInput("无效操作,按清除键继续!");
//					}
                    return cancelInput("请输入金额再结算,按清除键继续!");
				}
				if (buffer.length() > 8)
					return cancelInput("输入金额过大,按清除键继续!");
				// 转变输入价格
				int cents = (int) Math.rint((atof(buffer) * payment_base));
				// 把结算的价格与结算的方式传给PosInputPayment
				return new PosInputPayment(cents, Payment.CASH);

			} else if (keyFunc(c) == PosFunction.VOUCHER) {
				if (buffer.length() == 0 || pos.core.getPosSheet().getSaleLen()==0)
					return cancelInput("无效操作,按清除键继续!");
				if (buffer.length() > 8)
					return cancelInput("输入金额过大,按清除键继续!");
				int cents = (int) Math.rint((atof(buffer) * payment_base));
				return new PosInputPayment(cents, Payment.VOUCHER);

			} else if (keyFunc(c) == PosFunction.CHEQUE) {
				if (buffer.length() == 0 || pos.core.getPosSheet().getSaleLen()==0)
					return cancelInput("无效操作,按清除键继续!");
				if (buffer.length() > 8)
					return cancelInput("输入金额过大,按清除键继续!");
				if ((int) Math.rint((atof(buffer) * payment_base))
					> pos.core.getPosSheet().getValue().getValueToPay()) {
					return cancelInput("支票支付不能大于应收金额,按清除键继续!");
				}
				try {
					String chequeno = getDigitalString("请输入支票号码:", 18, 0);
					if (chequeno.length() == 0) {
						return cancelInput("支票号码不能为空,按清除键继续!");
					} else {
						int cents =
							(int) Math.rint((atof(buffer) * payment_base));
						return new PosInputPayment(
							cents,
							Payment.CHEQUE,
							chequeno);
					}
				} catch (UserCancelException e) {
					buffer = "";
					out.setInputLine("");
					return new PosInput(PosFunction.CANCEL);
				}

			} else if (keyFunc(c) == PosFunction.CARDBANK) {
//				if (buffer.length() == 0 || pos.core.getPosSheet().getSaleLen()==0)
				if (buffer.length() == 0 )
					return cancelInput("无效操作,按清除键继续!");
				if (buffer.length() > 8)
					return cancelInput("输入金额过大,按清除键继续!");
				try {
					String cardno = null;
					String card_type_no = null;
					String card_type = null;
					if (bank_card_type.equalsIgnoreCase("ON")) {
						GetBankCardType BankCardType = new GetBankCardType();
						BankCardType.show();
						if (BankCardType.isConfrim()) {
							AbstractTableModel theTableModel =
								BankCardType.getTheTableModel();
							JTable theTable = BankCardType.getTheTable();
							card_type =
								((String) theTableModel
									.getValueAt(theTable.getSelectedRow(), 1))
									.toString();
							getCardType getcardtype = getCardType.getInstance();
							card_type = getcardtype.getString(card_type);
							cardno = getBankCardNo(18);
							card_type_no = card_type + cardno;
							//System.out.println("卡号为" + card_type_no);
						} else {
							throw new UserCancelException();
						}
					} else {
						cardno = getBankCardNo(18);
						card_type_no = cardno;
						//System.out.println("卡号为" + cardno);
					}
					int cents = (int) Math.rint((atof(buffer) * payment_base));
					return new PosInputPayment(
						cents,
						Payment.CARDBANK,
						card_type_no);
				} catch (UserCancelException e) {
					buffer = "";
					out.setInputLine("");
					return new PosInput(PosFunction.CANCEL);
				}

			}else if (keyFunc(c) == PosFunction.CARDSHOP) {
                PosConfig config=PosConfig.getInstance();
                String withDraw=config.getString("SHC_WITHDRAW");
               if(pos.core.getPosSheet().getValue().getValueToPay()<0&&"OFF".equals(withDraw))
                    return cancelInput("储值卡不允许退货,按清除键继续!");
				//if (pos.core.getPosSheet().getSaleLen() > 0) {
              	SHCardProcess scp = new SHCardProcess(pos.core.getPosSheet().getSaleLen());
					if (scp.process()){
						if(scp.getpayflag()==0){
						return (
							new PosInputPayment(
								atoi(scp.getPayTotal()),
								Payment.CARDSHOP));
						}else
						{
							return (
									new PosInputPayment(
										atoi(scp.getPayTotal()),
										Payment.CARDRKSHOP));
						}
					} else {
						if (scp.getExceptionInfo() != null) {
							return cancelInput(scp.getExceptionInfo());
						}
					}
//				} else {
//					return cancelInput("储值卡不允许退货,按清除键继续!");
//				}

			} 
			//TODO
			else if (keyFunc(c) == PosFunction.CARDMEMBER) {
				if (pos.core.getPosSheet().getSaleLen() == 0) {
                    //if(pos.posFrame.getState()!='S')
                     PosDevOut.getInstance().displayHeader(PosContext.getInstance());
					if (pos.core.getPosSheet().getMemberCard()== null) {
						MemberCardProcess mcp = new MemberCardProcess();
						MemberCard card = mcp.readLoanCardNum();
						
							if (mcp.getExceptionInfo() != null) {
								return cancelInput(mcp.getExceptionInfo());
							} else if(card!=null){
								pos.core.getPosSheet().setMemberCard(
										card);
								out.dispMemberCardHeader(card);
								
							}
						
					} else {
						out.clear();
						out.printFeed(out.getFeedLines());
						out.cutPaper();
						pos.core.deleteFindresult();
						try {
							pos.core.findpriceSheet();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						PosContext context = PosContext.getInstance();
						out.displayHeader(context);

					}
				}  else {
					// 20080702 增加
					if (pos.core.getPosSheet().getMemberCard()== null) {
						MemberCardProcess mcp = new MemberCardProcess();
						MemberCard card = mcp.readLoanCardNum();
						
							if (mcp.getExceptionInfo() != null) {
								return cancelInput(mcp.getExceptionInfo());
							} else if(card!=null){
								pos.core.getPosSheet().setMemberCard(
										card);
								out.dispMemberCardHeader(card);
								
							}
						
					} 
					else		
					return cancelInput("已存在会员卡,按清除键继续!");
				}

			} 
			 else if (keyFunc(c) == PosFunction.Coupon){
                  return  new PosInput(PosFunction.Coupon);

             }
			 else if (keyFunc(c) == PosFunction.CouponPay){		//券, 支付
					String inport = "";
					try {
						inport = getDigitalString("请输入支付券号码:", 18, 0);
					} catch (UserCancelException e) {
						return cancelInput("券号有误,按清除键继续!");
					}
					CouponMgr couponMgr = new CouponMgrImpl();
					if (inport.length() != 18) {
						return cancelInput("无效券号,按清除键继续!");
					}
					String cardNo = inport.substring(0, 10);
					String secrety = inport.substring(10, 18);
					CouponSale couponPay =new CouponSale();
					try {
						CouponEnCash queryVO = couponMgr.query(cardNo, secrety);
//						queryVO = couponMgr.getCouponSale(cardNo, secrety,PosContext.getInstance().getStoreid());
					if (queryVO != null && queryVO.getExceptionInfo() == null) 
					{
						couponPay.setCouponID(queryVO.getCouponID());
						couponPay.setCouponPass(queryVO.getCouponPass());
//						couponPay.setMode("p");
						couponPay.setMode(queryVO.getMode());
						couponPay.setMinFlag(queryVO.getMinFlag());
						couponPay.setPrice(queryVO.getPrice());
						if (!pos.core.getPosSheet().couponPay.add(couponPay)) {
							return cancelInput("此券重复过机,按清除键继续!");
						}
						pos.core.getPosSheet().couponPay.setUpdateType("f");
						int cents = (int) Math.rint((atof(String.valueOf(queryVO.getPrice())) * payment_base));
						return new PosInputPayment(
								cents,
								Payment.couponPay,
								queryVO.getCouponID());
					}
					else
					{
						if(queryVO==null)
						    return cancelInput("无效券号,按清除键继续!");
						else
							return cancelInput(queryVO.getExceptionInfo());
					}
				}
					 catch (IOException e) {
						 return cancelInput("支付券号异常,按清除键继续!");
						} 
					 catch (CouponException e) {
							 return cancelInput("支付异常"+e.getMessage()+"按清除键继续!");
						}
			 }
			 else if (keyFunc(c) == PosFunction.ICCARD) {
				if (pos.core.getPosSheet().getSaleLen() > 0) {
					ICCardProcess icp = new ICCardProcess();
					if (icp.process()) {
						PosInputPayment p =
							new PosInputPayment(
								atoi(icp.getPayTotal()),
								Payment.ICCARD);
						p.setMediaNo(icp.getCardNo());
						p.setExtraData(icp.getCardBalance());
						p.setICCardProcess(icp);
						return p;
					} else {
						if (icp.getExceptionInfo() != null) {
							return cancelInput(icp.getExceptionInfo());
						}
					}
				} else {
					return cancelInput("无效操作,按清除键继续!");
				}

			} else if(keyFunc(c) == PosFunction.OPENCASHBOX){
                          //假如按下的是打开钱箱键：
                         // System.out.println("你已按下打开钱箱键！");
                          return new PosInput(PosFunction.OPENCASHBOX);
                          //cashDrawer.open();



                        }
			/*else {
				String code = getCode4Hotkey(c);

				if (code != null)
					return new PosInputGoods(code, amount);

				if (buffer.length() == 0)
					return new PosInput(keyFunc(c));
				else
					return cancelInput("无效操作！");
			}*/
			else {
				Hotkey hot = getHotkey(c);
				if (hot != null) {

					String code = hot.getPlus();
					int flag = hot.getFlag();

					switch (flag) {
						case 0 : //银行卡热键
							//deal with bankcard hotkey process...
							break;
						case 1 : //商品热键
							if (code != null)
								return new PosInputGoods(code, amount);
							if (buffer.length() == 0)
								return new PosInput(keyFunc(c));
							else
								return cancelInput("无效操作,按清除键继续!");
						case 2 : //现金热键
							if (code != null)
								return new PosInputPayment(
									Integer.parseInt(code) * 100,
									Payment.CASH);
							else
								return cancelInput("无效操作,按清除键继续!");

						default :
							break;
					}
				} else
					return new PosInput(keyFunc(c));
			}

		}

	}

	public int getbaseprice() {
		DispPrice dispprice = (DispPrice) out.getMainUI();
		double baseprice = 0;
		int cents = 0;
		try {
			dispprice.set();
			baseprice = getDouble(6, 2);
			cents = (int) Math.rint(baseprice * 100);
		} catch (UserCancelException e) {
		}
		return cents;
	}
	/**获取地磅信息输入
	 * @param g
	 * @param init_volume
	 * @return PosInputGoods
	 * */
	public PosInputGoods getLoadometer(
		Goods g,
		Volume init_volume,
		Operator operator) {
		int qty = 0;
		double volume = 0;
		PosInputGoods input_goods = null;
		Privilege priv = new Privilege(operator);

		Loadometer loadometer = (Loadometer) out.getMainUI();
		loadometer.setLoadometerPrice(new Value(g.getPrice()).toString());

		try {
			loadometer.setStep(0);
			//volume = getDouble(init_volume.getVolume(), 5, 3);
			volume = getDouble(0, 5, 3);
			if(g.getX()==1){
				g.setX(1000);
			}
			qty = (int) Math.rint(volume * g.getX());	
			double money = volume * g.getPrice();
			long cents = (int) Math.rint(money);

			loadometer.setQuantity(new Volume(qty).toString());
			loadometer.setAmount(new Value(cents).toString());
			String temp = new Value(cents).toString();

			loadometer.setStep(1);
			this.clearKey();
			volume = getDouble4Amount(money / 100, 6, 2, priv);
			cents = (int) Math.rint(volume * 100);
			if (cents == 0) {
				notice("地磅金额不能为零！");
				return null;
			}
			input_goods = new PosInputGoods(g.getVgno());
			input_goods.setQty(qty);
			input_goods.setMilliVolume(qty);
			input_goods.setCents(cents);
			input_goods.setOrgCode(g.getVgno());
			input_goods.setDeptid(g.getDeptid());
			//input_goods.setGoodsType(Goods.WEIGHT);

		} catch (UserCancelException e) {
			e.printStackTrace();
		} finally {
			operator.resetPrivilege();
			PosContext.getInstance().setAuthorizerid("");
			return input_goods;
		}

	}

	private boolean confirm(String s) {
		LoadoConfirm confirm = new LoadoConfirm();
		confirm.setMessage(s);
		confirm.show();

		return (confirm.isConfirm());
	}

	public PosInputGoods getOilInput(Goods g, Volume init_volume) {
		/*
		double money = 0;
		long cents = 0;
		*/
		int qty = 0;
		double volume = 0;

		PosInputGoods input_goods = null;

		OilInput oilInput = (OilInput) out.getMainUI();
		oilInput.setOilName(g.getName());
		oilInput.setOilPrice(
			new Value(g.getPrice() - g.getOilDisc()).toString());
		oilInput.setOilOriginPrice(new Value(g.getPrice()).toString());	
		oilInput.setOilGunNO("");

		try {
			/*
				out.setStep(0);
				double volume = getDouble(init_volume.getVolume(), 5, 3);
				int qty = (int) Math.rint(volume * g.getX());

				if (qty == 0) {
					oilInput.setStep(1);
					money = getDouble(0, 6, 2);
					cents = (int) Math.rint(money * 100);
				}

				if (cents < 1) {
					cents = (long)qty * g.getPrice() / g.getX();
					oilInput.setAmount(new Value(cents).toString());
					oilInput.setQuantity(new Volume(qty).toString());
				} else {
					volume = (double) cents / (double) g.getPrice();
					qty = (int) Math.rint(volume * g.getX());
					oilInput.setQuantity(new Volume(qty).toString());
				}
				*/
			out.setStep(1);
			//double money = getDouble(init_volume.getVolume(), 6, 2);
			double money = getDouble(0, 6, 2);
			long cents = (int) Math.rint(money * 100);
			if (cents == 0) {
				oilInput.setStep(0);
				volume = getDouble(0, 5, 3);
				qty = (int) Math.rint(volume * g.getX());

			}
			if (qty < 1) {
				volume =
					(double) cents / (double) (g.getPrice() - g.getOilDisc());
				qty = (int) Math.rint(volume * g.getX());
				oilInput.setAmount(new Value(cents).toString());
				oilInput.setQuantity(new Volume(qty).toString());
			} else {
				cents = (long) qty * (g.getPrice() - g.getOilDisc()) / g.getX();
				oilInput.setAmount(new Value(cents).toString());
			}
			int oilisland_maxlimit = 0;
			try {
				oilisland_maxlimit =
					Integer.parseInt(
						PosConfig.getInstance().getString("OILISLAND_MAXNO"));
			} catch (NumberFormatException e) {

			}
			String gun_no;
			do {
				oilInput.setStep(2);
				gun_no = getDigitalString("Gun", 3, 0);
				if (oilisland_maxlimit != 0) {
					if (gun_no.equals("")) {
						break;
					} else if (Integer.parseInt(gun_no) > oilisland_maxlimit) {
						notice("超过最大油岛号");
						oilInput.setOilGunNO("");

					} else {
						break;
					}

				} else {
					break;
				}

			} while (true);

			input_goods = new PosInputGoods(g.getVgno());
			input_goods.setGoodsType(Goods.WEIGHT_VALUE);
			input_goods.setQty(qty);
			input_goods.setMilliVolume(qty);
			input_goods.setCents(cents);
			input_goods.setOrgCode(g.getVgno());
			input_goods.setColorSize(gun_no);
		} catch (UserCancelException e) {
			e.printStackTrace();
		} finally {
			return input_goods;
		}

	}

	private void notice(String note) {
		DialogInfo notice = new DialogInfo();
		notice.setMessage(note);
		notice.show();
	}

	public PosInput getInputLogon(String info) {
		out.setStep(0);

		String s = (info.length() > 0) ? info : "请输入收银员编号: ";
		String id;
		try {
			id = getDigit4Logon(s, 4, 1);
			out.setStep(1);

            /*
            String pin = getDigit4Logon("请输入密码: ", 4, 1);
			out.setStep(2);
			String dutyNo = getDigit4Logon("请输入班次:", 2, 1);
			//TODO 删除登陆界面的班次显示  沧州富达 by fire  2005_5_11 
            if (id.length() == 0
				|| pin.length() == 0
				|| dutyNo.length() == 0) {
				return new PosInput(PosFunction.CANCEL);
			} else {
				return new PosInputLogon(id, pin, Integer.parseInt(dutyNo));
			}
           */

			//TODO 删除登陆界面的班次显示  沧州富达 by fire  2005_5_11 
            String pin = getDigit4Logon("请输入密码: ", 5, 1);
            if (id.length() == 0
				|| pin.length() == 0) {
				return new PosInput(PosFunction.CANCEL);
			} else {
				// TODO   沧州富达 by fire  2005_5_11 
                return new PosInputLogon(id, pin);
			}

		} catch (UserExitException e) {
			e.printStackTrace();
			return new PosInput(PosFunction.EXIT);
		}
	}

	public PosInput getInputAuthority(String info) throws UserCancelException {
		System.out.println(info);
		out.setStep(0);

		String pin = "";

		String s = (info.length() > 0) ? info : "请输入收银员编号: ";
		String id = in.getDigitalString(s, 4, 0);
		if (id.length() != 0) {
			out.setStep(1);
			//pin = in.getDigitalString("请输入密码: ", 4, 0);
            pin = in.getDigitalString("请输入密码: ", 5, 0);
			out.setStep(2);
		}

		if (id.length() == 0 || pin.length() == 0) {
			return new PosInput(PosFunction.CANCEL);
		} else {
			return new PosInputLogon(id, pin);
		}
	}

	public PosInput getInputPin(String info) {
		System.out.println(info);
		String pin = in.getDigit("请输入密码: ", 4);

		if (pin.length() == 0) {
			return new PosInput(PosFunction.CANCEL);
		} else {
			return new PosInputPin(pin);
		}
	}

	public String getDigit(String s, int maxlen) {
		return getDigit(s, maxlen, 1);
	}

	public String getDigit(String s, int maxlen, int minlen) {
		out.clearInputLine();
		PosDevOut.getInstance().prompt(s);
		String buffer = "";
		boolean done = false;
		while (!done) {
			int c = getKey();
			if (keyFunc(c) >= '0' && keyFunc(c) <= '9') {
				buffer += (char) keyFunc(c);
				out.setInputLine(buffer);
			}
			if (keyFunc(c) == PosFunction.BIZERO) {
				buffer += "00";
				out.setInputLine(buffer);
			}

			if (buffer.length() >= maxlen)
				done = true;
			if (buffer.length() >= minlen && keyFunc(c) == PosFunction.ENTER)
				done = true;
			if (keyFunc(c) == PosFunction.CANCEL) {
				buffer = "";
				done = true;
			}
			if (keyFunc(c) == PosFunction.BACKSPACE) {
				if (buffer.length() > 0)
					buffer = buffer.substring(0, buffer.length() - 1);
				out.setInputLine(buffer);
			}
		}
		return buffer;
	}

	public String getDigit4Logon(String s, int maxlen, int minlen)
		throws UserExitException {
		out.clearInputLine();
		PosDevOut.getInstance().prompt(s);
		String buffer = "";
		boolean done = false;
		while (!done) {
			int c = getKey();
			if (keyFunc(c) >= '0' && keyFunc(c) <= '9') {
				buffer += (char) keyFunc(c);
				out.setInputLine(buffer);
			}
			if (keyFunc(c) == PosFunction.BIZERO) {
				buffer += "00";
				out.setInputLine(buffer);
			}

			if (buffer.length() >= maxlen)
				done = true;
			if (buffer.length() >= minlen && keyFunc(c) == PosFunction.ENTER)
				done = true;
			if (keyFunc(c) == PosFunction.CANCEL) {
				buffer = "";
				done = true;
			}
			if (keyFunc(c) == PosFunction.BACKSPACE) {
				if (buffer.length() > 0)
					buffer = buffer.substring(0, buffer.length() - 1);
				out.setInputLine(buffer);
			}
			if (keyFunc(c) == PosFunction.EXIT)
				throw new UserExitException("User exit system ...");
		}
		return buffer;
	}

	public String getDigitalString(String note, int maxlen, int minlen)
		throws UserCancelException {
		out.clearInputLine();
		PosDevOut.getInstance().prompt(note);
		String buffer = "";
		boolean done = false;
		while (!done) {
			int c = getKey();
			if (keyFunc(c) >= '0' && keyFunc(c) <= '9') {
				buffer += (char) keyFunc(c);
				out.setInputLine(buffer);
			}
			if (keyFunc(c) == PosFunction.BIZERO) {
				buffer += "00";
				out.setInputLine(buffer);
			}

			if (keyFunc(c) == PosFunction.BACKSPACE) {
				if (buffer.length() > 0)
					buffer = buffer.substring(0, buffer.length() - 1);
				out.setInputLine(buffer);
			}
			if (buffer.length() >= maxlen)
				done = true;
			if (buffer.length() >= minlen && keyFunc(c) == PosFunction.ENTER)
				done = true;
			if (keyFunc(c) == PosFunction.CANCEL)
				throw new UserCancelException("User cancel ...");
			if (keyFunc(c) == PosFunction.EXIT)
				throw new UserCancelException("User exit ...");
		}
		return buffer;
	}

	public double getDouble(double init_val, int n, int m)
		throws UserCancelException {
//		String buffer = (init_val != 1 && init_val != 0) ? ("" + init_val) : "";

		String buffer = (init_val != 0) ? ("" + init_val) : "";

		boolean done = false;

		out.setInputLine(buffer);
		while (!done) {
			int c = getKey();
			if (keyFunc(c) >= '0' && keyFunc(c) <= '9') {
				buffer += (char) keyFunc(c);
				buffer = normalizeDecimalBuffer(buffer, n, m);
				out.setInputLine(buffer);
			}

			if (keyFunc(c) == PosFunction.BACKSPACE) {
				if (buffer.length() > 0)
					buffer = buffer.substring(0, buffer.length() - 1);
				out.setInputLine(buffer);
			}

			if (keyFunc(c) == PosFunction.POINT && buffer.indexOf(".") < 0) {
				buffer += '.';
				out.setInputLine(buffer);
			}

			if (keyFunc(c) == PosFunction.BIZERO) {
				buffer += "00";
				out.setInputLine(buffer);
			}

			if (keyFunc(c) == PosFunction.ENTER)
				done = true;
			if (keyFunc(c) == PosFunction.CANCEL)
				throw new UserCancelException("User cancel ...");
			if (keyFunc(c) == PosFunction.EXIT)
				throw new UserCancelException("User exit ...");
		}

		return atof(buffer);
	}

	public double getDouble4Amount(
		double init_val,
		int n,
		int m,
		Privilege priv)
		throws UserCancelException {
//		String buffer =
//			(init_val != 1 && init_val != 0) ? (Double.toString(init_val)) : "";

		String buffer =
				(init_val != 0) ? (Double.toString(init_val)) : "";

		boolean done = false;

		out.setInputLine(buffer);
		while (!done) {
			int c = getKey();

			if (keyFunc(c) >= '0'
				&& keyFunc(c) <= '9'
				|| keyFunc(c) == PosFunction.BACKSPACE
				|| keyFunc(c) == PosFunction.POINT
				|| keyFunc(c) == PosFunction.BIZERO) {
				try {

					if (priv.checkPrivilege(PosFunction.ALTVAVLUE)) {
						if (keyFunc(c) >= '0' && keyFunc(c) <= '9') {
							buffer += (char) keyFunc(c);
							buffer = normalizeDecimalBuffer(buffer, n, m);
							out.setInputLine(buffer);
						}

						if (keyFunc(c) == PosFunction.BACKSPACE) {
							if (buffer.length() > 0)
								buffer =
									buffer.substring(0, buffer.length() - 1);
							out.setInputLine(buffer);
						}

						if (keyFunc(c) == PosFunction.POINT
							&& buffer.indexOf(".") < 0) {
							buffer += '.';
							out.setInputLine(buffer);
						}

						if (keyFunc(c) == PosFunction.BIZERO) {
							buffer += "00";
							out.setInputLine(buffer);
						}

					}
				} catch (UserCancelException e1) {
				}

			}

			if (keyFunc(c) == PosFunction.ENTER)
				done = true;
			if (keyFunc(c) == PosFunction.CANCEL)
				throw new UserCancelException("User cancel ...");
			if (keyFunc(c) == PosFunction.EXIT)
				throw new UserCancelException("User exit ...");
		}

		return atof(buffer);
	}

	public double getDouble(int n, int m) throws UserCancelException {
		String buffer = "";

		boolean done = false;

		out.setInputLine(buffer);
		while (!done) {
			int c = getKey();
			if (keyFunc(c) >= '0' && keyFunc(c) <= '9') {
				buffer += (char) keyFunc(c);
				buffer = normalizeDecimalBuffer(buffer, n, m);
				out.setInputLine(buffer);
			}

			if (keyFunc(c) == PosFunction.BACKSPACE) {
				if (buffer.length() > 0)
					buffer = buffer.substring(0, buffer.length() - 1);
				out.setInputLine(buffer);
			}

			if (keyFunc(c) == PosFunction.POINT && buffer.indexOf(".") < 0) {
				buffer += ".";
				out.setInputLine(buffer);
			}

			if (keyFunc(c) == PosFunction.BIZERO) {
				buffer += "00";
				out.setInputLine(buffer);
			}

			if (keyFunc(c) == PosFunction.ENTER)
				done = true;
			if (keyFunc(c) == PosFunction.CANCEL)
				throw new UserCancelException("User cancel ...");
			if (keyFunc(c) == PosFunction.EXIT)
				throw new UserCancelException("User exit ...");
		}

		return atof(buffer);
	}

	private String normalizeBuffer(String buffer) {
		int offset = buffer.indexOf('.');

		if (offset >= 0 && !buffer.endsWith(".")) {
			String a = buffer.substring(0, offset);
			String b = buffer.substring(offset + 1, buffer.length());
			a = "" + atoi(a);
			if (b.length() > 3)
				b = b.substring(0, 3);
			buffer = a + "." + b;
		}
		return buffer;
	}
	private String normalizeDecimalBuffer(String buffer, int n, int m) {
		int offset = buffer.indexOf('.');
		if (offset < 0) {
			if (buffer.length() > n)
				buffer = buffer.substring(0, n);
			int i = atoi(buffer);
			buffer = "" + i;
			return buffer;
		}

		if (offset >= 0 && !buffer.endsWith(".")) {
			String a = buffer.substring(0, offset);
			String b = buffer.substring(offset + 1, buffer.length());
			if (b.length() > m)
				b = b.substring(0, m);
			buffer = "" + atoi(a) + "." + b;
		}
		return buffer;
	}

	// basic method for routine work
	private int atoi(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private double atof(String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private int getKey() {
		int ch = 0;
		try {
			ch = posInputStream.read();
		} catch (java.io.IOException e) {
			ch = 0;
		}
		return ch;
	}

	public void clearKey() {
		try {
			posInputStream.skip(posInputStream.available());
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	private int keyFunc(int c) {
		return keymap[c];
	}

	private String getCode4Hotkey(int key) {
		return hotkeymap[key];
	}

	private Hotkey getHotkey(int key) {
		Hotkey hot = (Hotkey) hotkeylst[key];
		return hot;
	}

	public int waitCancel() {
		int c;
		PosDevOut out = PosDevOut.getInstance();

		out.prompt("无效操作,按清除键继续!");
		do {
			c = getKey();
		} while (
			keyFunc(c) != PosFunction.CANCEL
				&& keyFunc(c) != PosFunction.CLEAR);
		return PosFunction.CANCEL;
	}

	public int waitCancel(String s) {
		int c;
		PosDevOut out = PosDevOut.getInstance();

		out.prompt(s);
		do {
			c = getKey();
		} while (
			keyFunc(c) != PosFunction.CANCEL
				&& keyFunc(c) != PosFunction.CLEAR);
		return PosFunction.CANCEL;
	}

	public boolean waitReConnect() {
		int c;
		do {
			c = getKey();
		} while (
			keyFunc(c) != PosFunction.CANCEL
				&& keyFunc(c) != PosFunction.CLEAR
				&& keyFunc(c) != PosFunction.OFFLINE);
		if (keyFunc(c) == PosFunction.OFFLINE) {
			return false;
		} else {
			return true;
		}
	}

	private PosInput cancelInput(String s) {
		waitCancel(s);
		return new PosInput(PosFunction.CANCEL);
	}

	/**
	 * @param out
	 */
	public void setOut(PosDevOut out) {
		this.out = out;
	}

	/**
	 * @param stream
	 */
	public void setPosInputStream(InputStream stream) {
		posInputStream = stream;
	}

	public void setPaymentBase(int base) {
		payment_base = base;
	}

	/**
	 * @author HuangXuean
	 * 热键包装类
	 * 用来处理不同类型的热键，热键类型由flag字段标识
	 * 0表示银行卡热键
	 * 1表示商品热键
	 * 2表示现金热键
	 * */
	private class Hotkey {
		private String kb_type = null;
		private int key_value;
		private String plus;
		private int flag = 0;

		public Hotkey(int value, String plus, int flag) {
			key_value = value;
			this.plus = plus;
			this.flag = flag;
		}
		public Hotkey(String type, int value, String plus, int flag) {
			kb_type = type;
			key_value = value;
			this.plus = plus;
			this.flag = flag;
		}

		public String getType() {
			return kb_type;
		}
		public int getKeyvalue() {
			return key_value;
		}
		public String getPlus() {
			return plus;
		}
		public int getFlag() {
			return flag;
		}

		public String toString() {
			return "[Hotkey]@,kb_type = "
				+ kb_type
				+ ",key_value = "
				+ key_value
				+ ",plus = "
				+ plus
				+ ",flag = "
				+ flag;
		}
	}

	private static PosDevIn in = null;
	private InputStream posInputStream = null;
	private PosDevOut out = null;
	private int[] keymap;
	private String[] hotkeymap = new String[256];
	private Hotkey[] hotkeylst = new Hotkey[256];
	private BarcodeMap bmap;
	private int payment_base = 1;
	private Hotkey hotkey = null;
        private POSCashDrawer cashDrawer=POSCashDrawer.getInstance();
}
