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
import com.royalstone.pos.gui.ShoppingValueCardConfirm;
import com.royalstone.pos.io.PosInputPayment;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.util.PosConfig;
import com.royalstone.pos.util.Value;

/**
 * ��ֵ�������̣���ҵ�񷽷����á�
 * ��װ�˰�����ˢ������ѯ���û�ȷ�ϡ�֧���ȵĶ���
 * @deprecated
 * @author liangxinbiao
 */
public class SHValueCardProcess {
    private SimpleDateFormat sdfDateTime =
		new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private String AddTotal = "0";
	private String exceptionInfo;
    private int isView=1;
    private SHCardPayVO payVO;
    private IShoppingCard shoppingCard;
    private SHCardQueryVO queryVO;
    private PosInputPayment p;
	private String cardNo = null;
	private String secrety = null;
    
 
	public SHValueCardProcess(PosInputPayment p) {
      this.p=p;
	}
	
	public String getCardNo(){
		return cardNo;
		}
	
	
	private boolean confirm(String s) {
		DialogConfirm confirm = new DialogConfirm();
		confirm.setMessage(s);
		confirm.show();

		return (confirm.isConfirm());
	}

	public int process() {
		
		if (confirm("IC��")){
			//IC��
			ICCardR330 ic = new ICCardR330();
			if(ic.process())
			 {  
				cardNo = ic.getCardNo();
				secrety = ic.getPass();
			 }
			else
			 {
				exceptionInfo = ic.getExceptionInfo();
				return -1;
			 }

		
	 }
	else{
		if (confirm("������")){
			
			MSRInput msrInput = new MSRInput();
			msrInput.show();

			try {
				while (!msrInput.isFinish())
					Thread.sleep(500);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if(!msrInput.isConfirm())return -1;
			
			String inputCode = msrInput.getInputcode();
			if (inputCode == null && inputCode.equals("")) {
				exceptionInfo = "���Ŵ���!";
				return -1;
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
			exceptionInfo = "ȡ����ҵ��";
			return -1;
		 	}
	  }
		
//        DialogInfo notice = new DialogInfo();
//        notice.setMessage(cardNo);
//        notice.show();
		
		shoppingCard = SHCardFactory.createInstance();
	    queryVO = shoppingCard.query(cardNo, secrety);

		if (queryVO != null && queryVO.getExceptioninfo() == null) {
			try {
			  PosConfig config=PosConfig.getInstance();
              String cardvalue=config.getString("CARDVALUE");
              double iVg=0.0;
              
              //ʵ����
              String tenderAmount;
              tenderAmount =
					(new Value(p.getCents())
						.toString());
              
                try {
                   iVg=Double.parseDouble(cardvalue);
                } catch (NumberFormatException e) {
                	}
               if(iVg<Double.parseDouble(queryVO.getDetail())+Double.parseDouble(tenderAmount)){
                   DialogInfo notice = new DialogInfo();
                   notice.setMessage("�˴�ֵ���ﵽ��ֵ����޶�");
                   notice.show();
                   return -1;
               }


//				if (Double
//					.parseDouble(
//						(new Value(pos.core.getValue().getValueToPay())
//							.toString()))
//					> Double.parseDouble(queryVO.getDetail())) {
//					tenderAmount = queryVO.getDetail();
//				} else {
//					tenderAmount =
//						(new Value(pos.core.getValue().getValueToPay())
//							.toString());
//				}

				ShoppingValueCardConfirm shoppingValueCardConfirm =
					new ShoppingValueCardConfirm();
				shoppingValueCardConfirm.setCardNo(cardNo);
				//ʵ����
				shoppingValueCardConfirm.setTenderAmount(tenderAmount);
				shoppingValueCardConfirm.setCardAmount(
					Formatter.toMoney(queryVO.getDetail()));
				String cardBalance =
					Formatter.toMoney(
						Double.toString(
							Double.parseDouble(queryVO.getDetail())
								+Double.parseDouble(tenderAmount)));
				shoppingValueCardConfirm.setBalance(cardBalance);
				
				//��ֵ��ɺ���
				AddTotal=cardBalance;

                //-------------------------------
                queryVO.setDetail(cardBalance);
                queryVO.setCardNO(cardNo);
                //�Ƿ��ѯ
                if(this.isView==0)
                	shoppingValueCardConfirm.setEnterButton(false);
                shoppingValueCardConfirm.show();

				try {
					while (!shoppingValueCardConfirm.isFinish())
						Thread.sleep(500);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (shoppingValueCardConfirm.confirm()) {
					
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

                    return performPay(tenderAmount);
                }

				if(shoppingValueCardConfirm.confirm())
					return 0;
				else
				     return -1;
			} catch (Exception ex) {
				exceptionInfo = "��Ч����,�����������!";
			}
		}
		if (exceptionInfo == null) {
			if (queryVO != null && queryVO.getExceptioninfo() != null) {
				exceptionInfo = queryVO.getExceptioninfo() + "!";
			} else {
				if (queryVO == null)
					exceptionInfo = "�������,�����������!";
			}
		}
		return -1;
	}

    public int performPay(String tenderAmount) {
        synchronized (pos.Lock) {
            try {
            	String result=null;
                result = shoppingCard.RaPay(payVO);
              
                if (result != null && result.equals("1")) {
                    pos.core.getPosSheet().setShopCard(queryVO);
                    return 0;
                } else {
                    if(result==null){
                    	exceptionInfo="��ֵ�쳣�����ʵ!";
                    	 pos.core.getPosSheet().setShopCard(queryVO);
                    	return 2;
                    	}else{
                        exceptionInfo = result + ",�����������!";
                        return -1;
                    	}
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                exceptionInfo = "��Ч����,�����������!";
                return -1;
            }
        }
    }


	public String getAddTotal() {
		return AddTotal;
	}

	/**
	 * @return
	 */
	public String getExceptionInfo() {
		return exceptionInfo;
	}

}
