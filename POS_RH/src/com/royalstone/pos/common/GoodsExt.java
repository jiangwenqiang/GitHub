/*
 * �������� 2004-6-25
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.common;

import org.jdom.Element;

/**
 * @author liangxinbiao
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class GoodsExt extends Goods {

	private int pknum;

	public GoodsExt(
		String Vgno,
		String Barcode,
		String GoodsName,
		String Deptid,
		String Spec,
		String UnitName,
		int GoodsPrice,
		int gtype,
		int x4Price,
		String ptype,
		int pknum) {
		super(
			Vgno,
			Barcode,
			GoodsName,
			Deptid,
			Spec,
			UnitName,
			GoodsPrice,
			gtype,
			x4Price,ptype);
		this.pknum = pknum;
	}

	public GoodsExt(Element elm) {
		super(elm);
		try {
			pknum = Integer.parseInt(elm.getChildTextTrim("pknum"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	public Element toElement() {
		Element e = super.toElement();
		e.addContent(new Element("pknum").addContent("" + pknum));
		return e;
	}

	public int getPknum() {
		return pknum;
	}

	public void setPknum(int i) {
		pknum = i;
	}

}
