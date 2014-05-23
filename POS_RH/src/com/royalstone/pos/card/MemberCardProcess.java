package com.royalstone.pos.card;

import java.text.SimpleDateFormat;

import com.royalstone.pos.gui.MSRInput;


/**
 * ���˿������̣���ҵ�񷽷����á�
 * ��װ�˰�����ˢ������ѯ���û�ȷ�ϡ�֧���ȵĶ���
 * @author liangxinbiao
 */
public class MemberCardProcess {

	private SimpleDateFormat sdfDateTime =
		new SimpleDateFormat("yyyyMMddHHmmssSSS");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmssSSS");

	private String payTotal = "0";
	private String exceptionInfo;
	private String cardNo;
    private MemberCardMgr memberCardMgr;
    private MemberCard  memberCard;
	private boolean isConfirm = false;

	public MemberCardProcess() {

	}

	/**
	 * ��һ����ʼʱˢ��
	 * @return ����
	 */
	public MemberCard readLoanCardNum() {
		MSRInput msrInput = new MSRInput("��ˢ��:","loan");

		msrInput.show();

		try {
			while (!msrInput.isFinish())
				Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String inputCode = msrInput.getInputcode();

		if (!msrInput.isConfirm()) {
			isConfirm = false;
			return null;
		} else {
			isConfirm = true;
		}

		if (inputCode == null && inputCode.equals("")) {
			exceptionInfo = "���Ŵ���,�����������!";
			return null;
		}

		String cardNo = null;
		String secrety = null;
        //-------------------------------------
        int equalStr=inputCode.indexOf("=");
        if(equalStr==0)
            inputCode=inputCode.substring(1,inputCode.length());
        //--------------------------------------------------------
		String cardValue[] = inputCode.split("=");
		if (cardValue.length != 2) {
			cardNo = cardValue[0];
			secrety = "0";
		} else {
			cardNo = cardValue[0];
			secrety = cardValue[1];
		}
 //------------------------
        int firstIndex=cardValue[0].indexOf(";");
              firstIndex=firstIndex+1;
		if (cardValue.length != 2) {
			cardNo = cardValue[0].substring(firstIndex,cardValue[0].length());
			secrety = "0";
		} else {
			cardNo = cardValue[0].substring(firstIndex,cardValue[0].length());
             int lastIndex=cardValue[1].indexOf("?");
             if(lastIndex==-1)
              lastIndex=cardValue[1].length()-1;
			secrety = cardValue[1].substring(0,lastIndex);
		}
//-------------------------------------------------------
		try {
			memberCardMgr = MemberCardMgrFactory.createInstance();
		} catch (Exception e) {
			e.printStackTrace();
			this.exceptionInfo="POS�������������ô���,�����������!";
		}
		MemberCard cardInfo=null;
		try {
			cardInfo = memberCardMgr.query(cardNo);
		} catch (Exception e) {
			e.printStackTrace();
			this.exceptionInfo="�������,�����������!";
		}
		
		if (exceptionInfo == null&&cardInfo == null) {
			exceptionInfo = "�˿�������,�����������!!";
	    }
        if(cardInfo!=null&&cardInfo.getExceptionInfo()!=null)
             exceptionInfo = cardInfo.getExceptionInfo();
		return cardInfo;
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
