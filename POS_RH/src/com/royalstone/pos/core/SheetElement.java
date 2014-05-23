
package com.royalstone.pos.core;
import java.text.DecimalFormat;

import org.jdom.Element;

import com.royalstone.pos.card.CardChange;
import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.common.Payment;
import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.common.ProductProperty;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.coupon.CouponSaleList;
import com.royalstone.pos.io.PosInputPayment;
import com.royalstone.pos.shell.pos;

/**
   @version 1.0 2004.05.14
   @author  Mengluoyi, Royalstone Co., Ltd.
 */

class SheetElement extends Element
{
	
	/* 输入商品对像
	 * 输出
	 * True 结算单是使用提货单
	 * false 结算单是是初始预订单
	 * */
//	public boolean isNewOrder ( Sale sale){
//		if (sale.getVgno().equals("999999") && sale.getFactValue() < 0){
//			return true;
//			}
//			return false;
//		}
	
	// 为提货，预订单状态写入本地文件
	public SheetElement ( PosContext con, int x)
	{
		super( "context" );
				int a = x;
				if ( a == 1){
					this.addChild( "storeid", con.getStoreid() );
					this.addChild( "posid",   con.getPosid() );
					this.addChild( "cashierid", con.getCashierid() );
					this.addChild("sheetid", "" + con.getSheetid());
					this.addChild( "orderid", "" + con.getOrderidOld());
					this.addChild("orderlstid","" + con.getOrder_id());
					this.addChild( "mode", con.getModeCode() );
					this.addChild( "workdate", "" + con.getWorkDate() );
				}
				else {
					int b = 0;
					this.addChild( "storeid", con.getStoreid() );
					this.addChild( "posid",   con.getPosid() );
					this.addChild( "cashierid", con.getCashierid() );
					this.addChild( "sheetid", "" + con.getSheetid());
					this.addChild( "orderid", "" + b);
					this.addChild( "orderlstid", "" + con.getOrder_id());
					this.addChild( "mode", con.getModeCode() );
					this.addChild( "workdate", "" + con.getWorkDate() );
					
					}

	}
	
	public SheetElement ( PosContext con)
	{
		super( "context" );
		int x = 0;
		this.addChild( "storeid", con.getStoreid() );
		this.addChild( "posid",   con.getPosid() );
		this.addChild( "cashierid", con.getCashierid() );
		this.addChild( "sheetid", "" + con.getSheetid() );
	
		this.addChild("orderid", "" +  x);
		this.addChild("orderlstid", "" + x);
		this.addChild( "mode", con.getModeCode() );
		this.addChild( "workdate", "" + con.getWorkDate() );
	//TODO  沧州富达 by fire  2005_5_11	

	}
	public SheetElement ( PosContext con, String action, String flag )
	{
		super( "log_lst" );
		this.addChild( "shop_id", con.getStoreid() );
		this.addChild( "pos_id",   con.getPosid() );
		this.addChild( "edate", "" + con.getLWorkDate() );
		this.addChild( "etime", "" + con.getLPosTime() );
		this.addChild( "action", action );
		this.addChild( "resultflag", flag );
		this.addChild( "cashier_id", con.getCashierid() );
	}
	
	public SheetElement ( PosContext con, String action, String flag, int cashierid )
	{
		super( "log_lst" );
		this.addChild( "shop_id", con.getStoreid() );
		this.addChild( "pos_id",   con.getPosid() );
		this.addChild( "edate", "" + con.getLWorkDate() );
		this.addChild( "etime", "" + con.getLPosTime() );
		this.addChild( "action", action );
		this.addChild( "resultflag", flag );
		DecimalFormat df = new DecimalFormat( "0000" );
		String cashier_id = df.format(cashierid); 
		this.addChild( "cashier_id", cashier_id );
	}
	/**
	 * @param sheet PosSheet实例，记录了销售、支付、上下文等信息
	 * @param input 输入的支付信息，主要用来在写XML文件时候记录挂帐卡的信息：卡号，卡余额，以备重打上一单所用
	 * */
	public SheetElement ( PosSheet sheet,PosInputPayment input )
	{
		super( "sheet" );
		Element elm_sale = new Element ( "salelist" );
		for( int i = 0; i< sheet.getFalseSaleLen(); i++ ){
			if( sheet.getFalseSale(i).getType() != Sale.AlTPRICE && sheet.getFalseSale(i).getType() != Sale.AUTODISC
					&& sheet.getFalseSale(i).getType() != Sale.MONEYDISC && sheet.getFalseSale(i).getType() != Sale.SINGLEDISC
					&& sheet.getFalseSale(i).getType() != Sale.TOTAL && sheet.getFalseSale(i).getType() != Sale.TOTALDISC
					&& sheet.getFalseSale(i).getType() != Sale.LOANCARD && sheet.getFalseSale(i).getType() != Sale.LOANDISC && sheet.getFalseSale(i).getType()!=Sale.ENCASH
					&& sheet.getFalseSale(i).getType()!=Sale.Change){// zhouzhou add
			    Element e = new SheetElement ( sheet.getFalseSale(i) );
			    elm_sale.addContent( e );	
			}else if( sheet.getFalseSale(i).getType() == Sale.AlTPRICE || sheet.getFalseSale(i).getType() == Sale.AUTODISC
					|| sheet.getFalseSale(i).getType() == Sale.MONEYDISC || sheet.getFalseSale(i).getType() == Sale.SINGLEDISC
					|| sheet.getFalseSale(i).getType() == Sale.TOTAL || sheet.getFalseSale(i).getType() == Sale.TOTALDISC
					|| sheet.getFalseSale(i).getType() == Sale.LOANCARD || sheet.getFalseSale(i).getType() == Sale.LOANDISC || sheet.getFalseSale(i).getType()==Sale.ENCASH
					|| sheet.getFalseSale(i).getType()==Sale.Change){// zhouzhou add
				Element ee = new SheetElement ( sheet.getFalseSale(i), "extendsale" );
				elm_sale.addContent( ee );	
			}	
		}

		Element elm_pay = new Element ( "paymentlist" );
		for( int i = 0; i< sheet.getPayLen(); i++ ){
			Element e = new SheetElement ( sheet.getPayment(i) ,input);
			elm_pay.addContent( e );
		}
		this.addContent( elm_sale );
		this.addContent( elm_pay  );

		MemberCard card = sheet.getMemberCard();
		if( card != null ) this.addContent( new SheetElement ( card ) );
		
		CardChange cardchange = sheet.getCardChange();
		if(cardchange != null) this.addContent( new SheetElement(cardchange));

        CouponSaleList csl=sheet.couponSales;
        for(int i=0;i<csl.size();i++){
          this.addContent( new SheetElement ( csl.get(i).getCouponID()) );
        }
//        String couponNo=sheet.getCouponNO();
//        if(couponNo!=null&&!couponNo.equals(""))this.addContent( new SheetElement ( couponNo) );

	}

	public SheetElement ( PosSheet sheet )
		{
			super( "sheet" );
			Element elm_sale = new Element ( "salelist" );
			for( int i = 0; i< sheet.getFalseSaleLen(); i++ ){
				if( sheet.getFalseSale(i).getType() != Sale.AlTPRICE && sheet.getFalseSale(i).getType() != Sale.AUTODISC
						&& sheet.getFalseSale(i).getType() != Sale.MONEYDISC && sheet.getFalseSale(i).getType() != Sale.SINGLEDISC
						&& sheet.getFalseSale(i).getType() != Sale.TOTAL && sheet.getFalseSale(i).getType() != Sale.TOTALDISC
						&& sheet.getFalseSale(i).getType() != Sale.LOANCARD && sheet.getFalseSale(i).getType() != Sale.LOANDISC && sheet.getFalseSale(i).getType()!=Sale.ENCASH
						&& sheet.getFalseSale(i).getType()!=Sale.Change){
					Element e = new SheetElement ( sheet.getFalseSale(i) );
					elm_sale.addContent( e );	
				}else if( sheet.getFalseSale(i).getType() == Sale.AlTPRICE || sheet.getFalseSale(i).getType() == Sale.AUTODISC
						|| sheet.getFalseSale(i).getType() == Sale.MONEYDISC || sheet.getFalseSale(i).getType() == Sale.SINGLEDISC
						|| sheet.getFalseSale(i).getType() == Sale.TOTAL || sheet.getFalseSale(i).getType() == Sale.TOTALDISC
						|| sheet.getFalseSale(i).getType() == Sale.LOANCARD || sheet.getFalseSale(i).getType() == Sale.LOANDISC || sheet.getFalseSale(i).getType()==Sale.ENCASH
						|| sheet.getFalseSale(i).getType()==Sale.Change){
					Element ee = new SheetElement ( sheet.getFalseSale(i), "extendsale" );
					elm_sale.addContent( ee );	
				}	
			}

			Element elm_pay = new Element ( "paymentlist" );
			for( int i = 0; i< sheet.getPayLen(); i++ ){
				Element e = new SheetElement ( sheet.getPayment(i));
				elm_pay.addContent( e );
			}
			this.addContent( elm_sale );
			this.addContent( elm_pay  );

			MemberCard card = sheet.getMemberCard();
			if( card != null ) this.addContent( new SheetElement ( card ) );
			
			CardChange cardchange = sheet.getCardChange();
			if(cardchange != null) this.addContent( new SheetElement(cardchange));
		}
	
	//提货单取消
	public SheetElement (PosSheet sheet, boolean input )
	{
		super( "sheet" );

		Element elm_sale = new Element ( "salelist" );
		for( int i = 0; i< sheet.getFalseSaleLen(); i++ ){
			if( sheet.getFalseSale(i).getType() != Sale.AlTPRICE && sheet.getFalseSale(i).getType() != Sale.AUTODISC
					&& sheet.getFalseSale(i).getType() != Sale.MONEYDISC && sheet.getFalseSale(i).getType() != Sale.SINGLEDISC
					&& sheet.getFalseSale(i).getType() != Sale.TOTAL && sheet.getFalseSale(i).getType() != Sale.TOTALDISC
					&& sheet.getFalseSale(i).getType() != Sale.LOANCARD && sheet.getFalseSale(i).getType() != Sale.LOANDISC 
					&& sheet.getFalseSale(i).getType() != Sale.Change){// zhouzhou add 
				Element e = new SheetElement ( sheet.getFalseSale(i) );
				elm_sale.addContent( e );	
			}else if( sheet.getFalseSale(i).getType() == Sale.AlTPRICE || sheet.getFalseSale(i).getType() == Sale.AUTODISC
					|| sheet.getFalseSale(i).getType() == Sale.MONEYDISC || sheet.getFalseSale(i).getType() == Sale.SINGLEDISC
					|| sheet.getFalseSale(i).getType() == Sale.TOTAL || sheet.getFalseSale(i).getType() == Sale.TOTALDISC
					|| sheet.getFalseSale(i).getType() == Sale.LOANCARD || sheet.getFalseSale(i).getType() == Sale.LOANDISC 
					|| sheet.getFalseSale(i).getType() == Sale.Change ){// zhouzhou add
				Element ee = new SheetElement ( sheet.getFalseSale(i), "extendsale" );
				elm_sale.addContent( ee );	
			}	
		}

		Element elm_pay = new Element ( "paymentlist" );
//		for( int i = 11; i< sheet.getPayLen(); i++ ){
//			Element e = new SheetElement ( sheet.getPayment(i));
//			elm_pay.addContent( e );
//		}
		this.addContent( elm_sale );
		this.addContent( elm_pay  );

		MemberCard card = sheet.getMemberCard();
		if( card != null ) this.addContent( new SheetElement ( card ) );
		
		CardChange cardchange = sheet.getCardChange();
		if(cardchange != null) this.addContent( new SheetElement(cardchange));
	}
	
	public SheetElement ( CardChange card )
	{
		super( "Change_card" );
		this.addChild( "cardno", card.getCardno() );	// 卡号
        this.addChild( "cardPoint", "" + card.getCardPoint() );		// 兑换减积分
		this.addChild( "plan",   card.getPlan());					// 方案号
	}

	public SheetElement ( MemberCard card )
	{
		super( "member_card" );
		this.addChild( "cardno", card.getCardNo() );
        this.addChild( "memberlevel", "" + card.getLevelName() );
		this.addChild( "totalpoint",   card.getTotalPoint().toString());
        this.addChild( "currentpoint", "" + card.getCurrentPoint().toString());
	}
    public SheetElement(String rodeValue){
        super( "coupon" );
        this.addChild("couponNO",rodeValue);
    }

	public SheetElement ( SaleList lst )
	{
		super( "salelist" );
		for( int i = 0; i< lst.size(); i++ ){
			Element e = new SheetElement ( lst.get(i) );
			this.addContent( e );
		}
	}

	public SheetElement ( PaymentList lst )
	{
		super( "paymentlist" );
		for( int i = 0; i< lst.size(); i++ ){
			Element e = new SheetElement ( lst.get(i) );
			this.addContent( e );
		}
	}
	/**
	 * @param p 支付信息，其中没有挂帐卡的余额信息
	 * */
	public SheetElement ( Payment p )
	{
		super( "payment" );

		addChild( "reason", p.getReasonCode() );
		addChild( "type", p.getTypeCode() );
		addChild( "currency", p.getCurrenCode() );
		addChild( "value",       "" + p.getValue() );
		addChild( "value_equiv", "" + p.getValueEquiv() );
		addChild( "cardno", p.getCardno() );
		addChild( "trainflag", "" + p.getTrainFlag() );
		addChild( "sysdate", p.getSysDate().toString() );
		addChild( "systime", p.getSysTime().toString() );
	}
	/**
	 * @param p 支付信息，其中没有挂帐卡的余额信息
	 * @param input 输入的支付信息，主要用来在写XML文件时候记录挂帐卡的信息：卡号，卡余额，以备重打上一单所用
	 * */
	public SheetElement ( Payment p,PosInputPayment input)
	{
		super( "payment" );

		addChild( "reason", p.getReasonCode() );
		addChild( "type", p.getTypeCode() );
		addChild( "currency", p.getCurrenCode() );
		addChild( "value",       "" + p.getValue() );
		addChild( "value_equiv", "" + p.getValueEquiv() );
		addChild( "cardno", p.getCardno() );
		addChild( "trainflag", "" + p.getTrainFlag() );
		addChild( "sysdate", p.getSysDate().toString() );
		addChild( "systime", p.getSysTime().toString() );
		//addChild( "cardResult", input.getExtraData());
        String cardRestult="";
        if(pos.core.getPosSheet().getShopCard()!=null)
            cardRestult=pos.core.getPosSheet().getShopCard().getDetail();

        addChild( "cardResult", cardRestult);
	}

	public SheetElement ( Sale sale )
	{
		super( "sale" );
		this.addContent( sale.getGoods().toElement());
		this.addContent(sale.prodpToElement());
		addChild( "vgno", sale.getVgno() );
		addChild( "barcode", sale.getBarcode() );
		addChild( "orgcode", sale.getOrgCode() );
		addChild( "deptid",  sale.getDeptid()  );
		addChild( "qty",  "" + sale.getQty() );
		addChild( "type", sale.getTypeCode() );
		addChild( "disctype", sale.getDiscCode() );
		addChild( "waiter", sale.getWaiter() );
		addChild( "authorizer", sale.getAuthorizer() );
		addChild( "placeno", sale.getPlaceno() );
		addChild( "colorsize", sale.getColorSize() );
		addChild( "stdprice",  "" + sale.getStdPrice()  );
		addChild( "x",  "" + sale.getGoods().getX()  );
		addChild( "itemvalue", "" + sale.getStdValue() );
		addChild( "discvalue", "" + sale.getDiscValue() );
		addChild( "factvalue", "" + sale.getFactValue() );
		addChild( "trainflag", "" + sale.getTrainFlag() );
		addChild( "sysdate", sale.getSysDate().toString() );
		addChild( "systime", sale.getSysTime().toString());
	}
	
	public SheetElement ( Sale sale, String flag )
	{
		super( "extendsale" );
		this.addContent( sale.getGoods().toElement() );
		this.addContent(sale.prodpToElement());
		addChild( "vgno", sale.getVgno() );
		addChild( "barcode", sale.getBarcode() );
		addChild( "orgcode", sale.getOrgCode() );
		addChild( "deptid",  sale.getDeptid()  );
		addChild( "qty",  "" + sale.getQty() );
		addChild( "type", sale.getTypeCode() );
		addChild( "disctype", sale.getDiscCode() );
		addChild( "waiter", sale.getWaiter() );
		addChild( "authorizer", sale.getAuthorizer() );
		addChild( "placeno", sale.getPlaceno() );
		addChild( "colorsize", sale.getColorSize() );
		addChild( "stdprice",  "" + sale.getStdPrice()  );
		addChild( "x",  "" + sale.getGoods().getX()  );
		addChild( "itemvalue", "" + sale.getStdValue() );
		addChild( "discvalue", "" + sale.getDiscValue() );
		addChild( "factvalue", "" + sale.getFactValue() );
		addChild( "trainflag", "" + sale.getTrainFlag() );
		addChild( "sysdate", sale.getSysDate().toString() );
		addChild( "systime", sale.getSysTime().toString() );
	}
	
	private void addChild( String name, String str )
	{
		Element e = new Element( name );
		e.addContent( str );
		this.addContent( e );
	}
}
