/*
 * 创建日期 2008-4-9
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.royalstone.pos.journal;


import java.io.FileInputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: yaopoqing
 * Date: 2005-5-26
 * Time: 10:46:18
 * To change this template use Options | File Templates.
 */

// 兑换积分使用类
public class ChangeCard4Reprint {
    private String cardNo;
	private String cardPoint;
    private String plan;

	public ChangeCard4Reprint(){

	}
    public String getCardNo() {
        return cardNo;
    }

    public String getCardPoint() {
        return cardPoint;
    }

    public String getPlan() {
        return plan;
    }

    public static ChangeCard4Reprint getInstance(String file){
    	ChangeCard4Reprint mc4r=null;
        try{
			Document doc = (new SAXBuilder()).build( new FileInputStream(file) );
			Element root = doc.getRootElement();
			Element sheet = root.getChild("sheet");
			Element memberCard = sheet.getChild("Change_card");
			if(memberCard==null)
                return null;
            else
                mc4r=new ChangeCard4Reprint();
				Element carNo = memberCard.getChild("cardno");
				Element cardPoint = memberCard.getChild("cardPoint");
				Element plan = memberCard.getChild("plan");
					mc4r.setCardNo(carNo.getTextTrim());
					mc4r.setCardPoint(cardPoint.getTextTrim());
                    mc4r.setPlan(plan.getTextTrim());
              return mc4r;
			}

			 catch ( Exception e ){
				e.printStackTrace();
			}
        return null;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setCardPoint(String cardPoint) {
        this.cardPoint = cardPoint;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

	public static void main(String[] args) {
		MemberCard4Reprint test = MemberCard4Reprint.getInstance("reprint/reprintsheet.xml");
		System.out.println("test.getCardno():  "+test.getCardNo());
		System.out.println("test.getMemberLevel():  "+test.getMemberLevel());
        System.out.println("test.getCurrentPoint():  "+test.getCurrentPoint());
        System.out.println("test.getTotalPoint():  "+test.getTotalPoint());
	}
}
