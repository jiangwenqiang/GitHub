/*
 * �������� 2008-3-16
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.royalstone.pos.card;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.royalstone.pos.gui.MSRInput;


/**
 * �����ֶһ�...
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
	 * ���뷽���ţ������������Ʒ���/����
	 * @return ����
	 */
	public CardChange readLoanCardNum(String cardno,String money,String ShopID,String PosID,String listno,String flag) {
		MSRInput msrInput = new MSRInput("�����뷽����:","loan");

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
			exceptionInfo = "�������������";
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
			this.exceptionInfo="POS�������������ô���,�����������!";
		}
		
		  
		  CardChange cardchange = new CardChange(cardno,ShopID,PosID,listno,money,plan,flag);
		  
           try {
              result= memberCardMgr.updateCardChange(cardchange);
           } catch (IOException e) {
           	   exceptionInfo = "�����쳣�������������";
               return result;
           }
           if(result!=null&&!result.getInfoFlag().equals("0")){
           	exceptionInfo = "�һ�ʧ�ܣ������������!";
           	if(result.equals("1"))
           	 exceptionInfo = "�����Ų����ڣ������������!";
        	if(result.equals("2"))
              	 exceptionInfo = "��Ա�����ֲ����������������!";
        	if(result.equals("3"))
             	 exceptionInfo = "��̨���˷������󣬰����������!";
        	if(result.equals("4"))
            	 exceptionInfo = "�����������Ϊ0�������������!";
        	if(result.equals("5"))
            	 exceptionInfo = "������������Ϊ0�������������!";
        	   return null;
           }
           if(result==null){
           		exceptionInfo = "�����쳣�������������";
           		return result;
           }
           
           return result;
		
	}

	/**
	 * @return ���ش�����ʾ
	 */
	public String getExceptionInfo() {
		return exceptionInfo;
	}



	/**
	 * @return ����
	 */
	public String getCardNo() {
		return cardNo;
	}


}

