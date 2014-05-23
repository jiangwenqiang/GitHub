package com.royalstone.pos.card;

import java.io.Serializable;

/**
 * 储值卡查询值对象，是从服务器返回的储值卡信息
 *
 * @author liangxinbiao
 */
public class SHCardQueryVO implements Serializable {

	private String memberid;
	private String exceptioninfo;
	private String detail="0.00";
	private String ifnewcard;
    private String cardNO;
    private String accflag="0"; //荣华币标志
    private String RHBDetail="0.00";//荣华币金额
    private String CardDetail="0.00";//储值卡剩余金额
    

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }
    
    public String getAccflag() {
        return accflag;
    }

    public void setAccflag(String accflag) {
        this.accflag = accflag;
    }

	public SHCardQueryVO() {
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getCardDetail() {
		return CardDetail;
	}
	public void setCardDetail(String cardDetail) {
		this.CardDetail = cardDetail;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	public String getIfnewcard() {
		return ifnewcard;
	}
	public void setIfnewcard(String ifnewcard) {
		this.ifnewcard = ifnewcard;
	}
	public String getExceptioninfo() {
		return exceptioninfo;
	}
	public void setExceptioninfo(String exceptioninfo) {
		this.exceptioninfo = exceptioninfo;
	}
	
	public String getRHBDetail(){
		return RHBDetail;
		}

	public void setRHBDetail(String rhbDetail){
		this.RHBDetail = rhbDetail;
		}
}
