/*
 * �������� 2004-6-25
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.common;


/**
 * @author liangxinbiao
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class GoodsCut extends Goods {

	private int qty;
    private int curqty;

	public GoodsCut(
		String Vgno,
		String Barcode,
		String GoodsName,
        String UnitName,
		String Spec,
		int GoodsPrice,
		int x4Price,
		int qty,
        int curqty) {
		super(
			Vgno,
			Barcode,
			GoodsName,
			"",
			Spec,
			UnitName,
			GoodsPrice,
			0,
			x4Price,"n");
		this.qty = qty;
        this.curqty=curqty;
	}


    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getCurqty() {
        return curqty;
    }

    public void setCurqty(int curqty) {
        this.curqty = curqty;
    }


}
