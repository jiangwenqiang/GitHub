package com.royalstone.pos.common;

import java.io.Serializable;

import org.jdom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: fire
 * Date: 2005-5-27
 */
/**
 * ��Ա�����ּ���ֵ����
 */
public class Accurate implements Serializable {
    private int cardLevelID;
    private int deptID;

    public int getCardLevelID() {
        return cardLevelID;
    }

    public void setCardLevelID(int cardLevelID) {
        this.cardLevelID = cardLevelID;
    }

    public int getDeptID() {
        return deptID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }

    private String accurate; //���ֱ���
    private String exeptionInfo;//�쳣��Ϣ

    public String getExeptionInfo() {
        return exeptionInfo;
    }

    public void setExeptionInfo(String exeptionInfo) {
        this.exeptionInfo = exeptionInfo;
    }

    public String getAccurate() {
        return accurate;
    }

    public void setAccurate(String accurate) {
        this.accurate = accurate;
    }

    public Element toElement() {
		Element acc = new Element("accurates");
		acc.addContent(new Element("cardLevelID").addContent(Integer.toString(this.cardLevelID)));
		acc.addContent(new Element("deptID").addContent(Integer.toString(this.deptID)));
		acc.addContent(new Element("accurate").addContent(this.accurate));
		return acc;
	}

    public Accurate(Element elm) {
        try {
			this.cardLevelID = Integer.parseInt(elm.getChildTextTrim("cardLevelID"));
			this.deptID =Integer.parseInt(elm.getChildTextTrim("deptID"));
			this.accurate = elm.getChildTextTrim("accurate");
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
    }

    public Accurate(int cardLevelID, int deptID, String accurate) {
        this.cardLevelID = cardLevelID;
        this.deptID = deptID;
        this.accurate = accurate;
    }
}
