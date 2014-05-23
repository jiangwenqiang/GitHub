package com.royalstone.pos.card;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.gui.DialogConfirm;
import com.royalstone.pos.gui.DialogInfo;
import com.royalstone.pos.gui.MSRInput;
import com.royalstone.pos.gui.ShoppingCardConfirm;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.util.PosConfig;
import com.royalstone.pos.util.Value;

/**
 * 储值卡主过程，由业务方法调用。
 * 封装了包括从刷卡、查询、用户确认、支付等的动作
 * @deprecated
 * @author liangxinbiao
 */
public class SHCardProcess {
    private SimpleDateFormat sdfDateTime =
		new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private String payTotal = "0";
	private String exceptionInfo;
    private int isView=1;
    private SHCardPayVO payVO;
    private IShoppingCard shoppingCard;
    private SHCardQueryVO queryVO;
    private int payflag=0;  //0 储值卡  //1 荣华币
    
 
	public SHCardProcess(int isView) {
      this.isView=isView;
      File dir = new File("autorever");
	  if (!dir.exists())
			dir.mkdir();
	}
	
	public SHCardProcess() {

		}
	
	
	private boolean confirma(String s) {
		DialogConfirm confirm = new DialogConfirm();
		confirm.setMessage(s);
		confirm.show();
		return (confirm.isConfirm());
	}

	//存在储值卡与荣华币支付.但支付时候是分别.
	public boolean process() {
		String cardNo = null;
		String secrety = null;

		if (confirma("IC卡")){
			//IC卡
			ICCardR330 ic = new ICCardR330();
			if(ic.process())
			 {  
				cardNo = ic.getCardNo();
				secrety = ic.getPass();
			 }
			else
			 {
				exceptionInfo = ic.getExceptionInfo();
				return false;
			 }
	 }
	else{
		if (confirma("磁条卡")){
			
			MSRInput msrInput = new MSRInput();
			msrInput.show();

			try {
				while (!msrInput.isFinish())
					Thread.sleep(500);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if(!msrInput.isConfirm())return false;
			
			String inputCode = msrInput.getInputcode();
			if (inputCode == null && inputCode.equals("")) {
				exceptionInfo = "卡号错误!";
				return false;
			}

			String cardValue[] = inputCode.split("=");
			if (cardValue.length != 2) {
				cardNo = cardValue[0];
				secrety = "0";
			}else{
				cardNo = cardValue[0];
				secrety = cardValue[1];
			}
		 }else{
			exceptionInfo = "取消卡业务";
			return false;
		 	}
	  }
		
		
		shoppingCard = SHCardFactory.createInstance();
	    queryVO = shoppingCard.query(cardNo, secrety);
        //加了荣华币的处理机制
		if (queryVO != null && queryVO.getExceptioninfo() == null) {
			try {
			  PosConfig config=PosConfig.getInstance();
              String vgBottom=config.getString("VGBOTTOM");
              double iVg=0.0;
                try {
                    iVg=Double.parseDouble(vgBottom);
                } catch (NumberFormatException e) {}
               if(iVg>(Double.parseDouble(queryVO.getDetail())+Double.parseDouble(queryVO.getRHBDetail()))){
                  DialogInfo notice = new DialogInfo();
                   notice.setMessage("此储值卡余额已低于限额，请回收！");
                   notice.show();
               }
                String tenderAmount;
                
//				if (Double
//					.parseDouble(
//						(new Value(pos.core.getValue().getValueToPay())
//							.toString()))
//					> (Double.parseDouble(queryVO.getDetail())+Double.parseDouble(queryVO.getRHBDetail()))) {
			//		tenderAmount = queryVO.getDetail();
//					tenderAmount = String.valueOf(Double.parseDouble(queryVO.getDetail())+Double.parseDouble(queryVO.getRHBDetail()));
//				} else {
//					tenderAmount =
//						(new Value(pos.core.getValue().getValueToPay())
//							.toString());
//				}
                
                //需要分别支付
                if(Double.parseDouble(queryVO.getDetail())>0.00)    //储值卡
                {
    				if (Double
					.parseDouble(
						(new Value(pos.core.getValue().getValueToPay())
							.toString()))
					> (Double.parseDouble(queryVO.getDetail()))) {
					tenderAmount = queryVO.getDetail();
				  } else {
					tenderAmount =
						(new Value(pos.core.getValue().getValueToPay())
							.toString());
				    }
                }else if(Double.parseDouble(queryVO.getRHBDetail())>0.00) //荣华币
                {
    				if (Double
    						.parseDouble(
    							(new Value(pos.core.getValue().getValueToPay())
    								.toString()))
    						> (Double.parseDouble(queryVO.getRHBDetail()))) {
    						tenderAmount = queryVO.getRHBDetail();
    					  } else {
    						tenderAmount =
    							(new Value(pos.core.getValue().getValueToPay())
    								.toString());
    				 }
    				payflag=1;
                }else
                {
                	tenderAmount="0.00";
                }
                

				ShoppingCardConfirm shoppingCardConfirm =
					new ShoppingCardConfirm();
				shoppingCardConfirm.setCardNo(cardNo);
				shoppingCardConfirm.setTenderAmount(tenderAmount);
				String cardBalance;
				//加入荣华币的显示
				if(Integer.parseInt(queryVO.getAccflag())!=1)
				{
					shoppingCardConfirm.setCardAmount(
							Formatter.toMoney(queryVO.getDetail()));
					cardBalance =
						Formatter.toMoney(
							Double.toString(
								Double.parseDouble(queryVO.getDetail())
									- Double.parseDouble(tenderAmount)));
					queryVO.setCardDetail(cardBalance);
				}else{
					shoppingCardConfirm.setCardAmount(
							Formatter.toMoney(queryVO.getDetail())+"&"+Formatter.toMoney(queryVO.getRHBDetail())+"("+Formatter.toMoney(String.valueOf((Double.parseDouble(queryVO.getDetail())+Double.parseDouble(queryVO.getRHBDetail()))))+")");
					//余额计算加入荣华币
					cardBalance =
						Formatter.toMoney(
							Double.toString(
								(Double.parseDouble(queryVO.getDetail())+Double.parseDouble(queryVO.getRHBDetail()))
									- Double.parseDouble(tenderAmount)));
					//非荣华币支付
					if(payflag==0)
					   queryVO.setCardDetail(String.valueOf(Double.parseDouble(queryVO.getDetail())-Double.parseDouble(tenderAmount)));
					//荣华币支付
					if(payflag==1)
					   queryVO.setRHBDetail(String.valueOf(Double.parseDouble(queryVO.getRHBDetail())-Double.parseDouble(tenderAmount)));
				
				}

				shoppingCardConfirm.setBalance(cardBalance);
                //-------------------------------
                queryVO.setDetail(cardBalance);
                
                queryVO.setCardNO(cardNo);
                //是否查询
                if(this.isView==0)
                  shoppingCardConfirm.setEnterButton(false);
                
                if(payflag==1)
                   shoppingCardConfirm.setButton("确定(荣华币)");
                else
                   shoppingCardConfirm.setButton("确定(储值卡)");
				shoppingCardConfirm.show();

				try {
					while (!shoppingCardConfirm.isFinish())
						Thread.sleep(500);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (shoppingCardConfirm.confirm()) {
					
					PosContext context=PosContext.getInstance();

					payVO = new SHCardPayVO();
					payVO.setCardno(cardNo);
					payVO.setCashierid(context.getCashierid());
					payVO.setCdseq("0");
					payVO.setPassword(secrety);
					payVO.setPayvalue(String.valueOf(tenderAmount));
					payVO.setPosid(context.getPosid());
					payVO.setShopid(context.getStoreid());
					SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
					payVO.setTime(sdf.format(new Date()));
					if(payflag==1)
					 payVO.setpayFlag("1");
					else
					 payVO.setpayFlag("0");
				
                    return performPay(tenderAmount);
                }
				return shoppingCardConfirm.confirm();
			} catch (Exception ex) {
				exceptionInfo = "无效操作,按清除键继续!";
			}
		}
		if (exceptionInfo == null) {
			if (queryVO != null && queryVO.getExceptioninfo() != null) {
				exceptionInfo = queryVO.getExceptioninfo() + "!";
			} else {
				if (queryVO == null)
					exceptionInfo = "网络故障,按清除键继续!";
			}
		}
		return false;
	}

    public boolean performPay(String tenderAmount) {
        synchronized (pos.Lock) {
            try {
                String filename =
                    "autorever"
                        + File.separator
                        + sdfDateTime.format(new Date())
                        + ".xml";
                FileOutputStream fs = new FileOutputStream(filename);
                Document doc = new Document(payVO.toElement());
                XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
                outputter.output(doc, fs);
                fs.flush();
                fs.close();
                String result = shoppingCard.pay(payVO);
               // String result=null;
                if (result != null && result.equals("1")) {
                    File file = new File(filename);
                    file.delete();
                    payTotal =
							Integer.toString(
								(int) Math.floor(
									Double.parseDouble(tenderAmount) * 100));
                     pos.core.getPosSheet().setShopCard(queryVO);
                    return true;
                } else {
                    if(result==null)
                        result="储值卡使用失败";
                    exceptionInfo = result + ",按清除键继续!";
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                exceptionInfo = "无效操作,按清除键继续!";
                return false;
            }
        }
    }


	public String getPayTotal() {
		return payTotal;
	}
	
	public int getpayflag(){
		return payflag;
		}

	/**
	 * @return
	 */
	public String getExceptionInfo() {
		return exceptionInfo;
	}

}
