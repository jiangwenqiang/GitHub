/*
 * 创建日期 2008-3-16
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.royalstone.pos.card;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.royalstone.pos.gui.MSRInput;


/**
 * 卡积分兑换...
 * @author zhouzhou
 */
public class MemberCardChange {

	private SimpleDateFormat sdfDateTime =
		new SimpleDateFormat("yyyyMMddHHmmssSSS");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmssSSS");

	private String payTotal = "0";
	private String exceptionInfo;
	private String cardNo;
    private MemberCardMgr memberCardMgr;
    private MemberCard  memberCard;
	private boolean isConfirm = false;

	public MemberCardChange() {

	}

	/**
	 * 输入方案号，代入参数：商品金额/卡号
	 * @return 卡号
	 */
	public CardChange readLoanCardNum(String cardno,String money,String ShopID,String PosID,String listno,String flag) {
		MSRInput msrInput = new MSRInput("请输入方案号:","loan");

		msrInput.show();
		
		CardChange result=null;

		try {
			while (!msrInput.isFinish())
				Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String inputCode = msrInput.getInputcode();

		if (!msrInput.isConfirm()) {
			isConfirm = false;
			return result;
		} else {
			isConfirm = true;
		}

		if (inputCode == null && inputCode.equals("")) {
			exceptionInfo = "方案号输入错误！";
			return result;
		}

		String plan = null;
        //-------------------------------------
        int equalStr=inputCode.indexOf("=");
        if(equalStr==0)
            inputCode=inputCode.substring(1,inputCode.length());
        //--------------------------------------------------------
		String cardValue[] = inputCode.split("=");
		if (cardValue.length != 2) {
			plan = cardValue[0];
		} else {
			plan = cardValue[0];
		}
		try {
			String info = null;
			memberCardMgr = MemberCardMgrFactory.createInstance();
		} catch (Exception e) {
			e.printStackTrace();
			this.exceptionInfo="POS服务器参数配置错误,按清除键继续!";
		}
		
		  
		  CardChange cardchange = new CardChange(cardno,ShopID,PosID,listno,money,plan,flag);
		  
           try {
              result= memberCardMgr.updateCardChange(cardchange);
           } catch (IOException e) {
           	   exceptionInfo = "网络异常，按清除键继续";
               return result;
           }
           if(result!=null&&!result.getInfoFlag().equals("0")){
           	exceptionInfo = "兑换失败，按清除键继续!";
           	if(result.equals("1"))
           	 exceptionInfo = "方案号不存在，按清除键继续!";
        	if(result.equals("2"))
              	 exceptionInfo = "会员卡积分不够，按清除键继续!";
        	if(result.equals("3"))
             	 exceptionInfo = "后台记账发生错误，按清除键继续!";
        	if(result.equals("4"))
            	 exceptionInfo = "返利方案金额为0，按清除键继续!";
        	if(result.equals("5"))
            	 exceptionInfo = "返利方案积分为0，按清除键继续!";
        	   return null;
           }
           if(result==null){
           		exceptionInfo = "网络异常，按清除键继续";
           		return result;
           }
           
           return result;
		
	}

	/**
	 * @return 返回错误提示
	 */
	public String getExceptionInfo() {
		return exceptionInfo;
	}



	/**
	 * @return 卡号
	 */
	public String getCardNo() {
		return cardNo;
	}


}

