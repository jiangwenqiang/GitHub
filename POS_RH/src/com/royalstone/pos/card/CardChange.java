/*
 * 创建日期 2008-4-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.royalstone.pos.card;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 * 会员卡兑换信息类
 */
public class CardChange implements Serializable{
	private String cardno="";	//卡号
	private String shopid="";	//店号
	private String posid="";	//POS机号
	private String listno="";	//流水号
	private String payvalue="";//商品总金额
	private String cardPoint="";	//扣减积分
	private String execinfo="";//出错信息
	private String flag="0";	//标志位 0 代表查询， 1 代表扣减积分
	private String plan="";	//方案编号
	private String infoFlag="2";	// 存储过程返回标志

	public CardChange() {
	}
	
	
	public CardChange(String cardno,String shopid,String posid,String listno,String payvalue,String plan,String flag){
		this.cardno = cardno;
		this.shopid = shopid;
		this.listno = listno;
		this.payvalue = payvalue;
		this.plan = plan;
		this.flag = flag;
		this.posid = posid;
		}
	
	/**
	 * @return 存储过程返回标志
	 */
	public String getInfoFlag() {
		return infoFlag;
	}

	/**
	 * @param 存储过程返回标志
	 */
	public void setInfoFlag(String infoFlag) {
		this.infoFlag = infoFlag;
	}
	
	/**
	 * @return 方案编号
	 */
	public String getPlan() {
		return plan;
	}

	/**
	 * @param plan 方案编号
	 */
	public void setPlan(String plan) {
		this.plan = plan;
	}

	/**
	 * @return 主卡号
	 */
	public String getCardno() {
		return cardno;
	}

	/**
	 * @param cardno 主卡号
	 */
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	/**
	 * @return 店号
	 */
	public String getShopid() {
		return shopid;
	}

	/**
	 * @param shopid 店号
	 */
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	/**
	 * @return 流水号
	 */
	public String getListno() {
		return listno;
	}

	/**
	 * @param cashierid 流水号
	 */
	public void setListno(String listno) {
		this.listno = listno;
	}

	/**
	 * @return 商品总金额
	 */
	public String getPayvalue() {
		return payvalue;
	}

	/**
	 * @param payvalue 商品总金额
	 */
	public void setPayvalue(String payvalue) {
		this.payvalue = payvalue;
	}

	/**
	 * @return 扣减的积分
	 */
	public String getCardPoint() {
		return cardPoint;
	}

	/**
	 * @param 扣减的积分
	 */
	public void setCardPoint(String cardPoint) {
		this.cardPoint = cardPoint;
	}

	/**
	 * @return 错误信息
	 */
	public String getExecinfo() {
		return execinfo;
	}

	/**
	 * @param 错误信息
	 */
	public void setExecinfo(String execinfo) {
		this.execinfo = execinfo;
	}

	/**
	 * @return POS机号
	 */
	public String getPosid() {
		return posid;
	}
	
	
	/**
	 * @param posid POS机号
	 */
	public void setPosid(String posid) {
		this.posid = posid;
	}
	

	/**
	 * @return 标志位
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param 标志位
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

}
