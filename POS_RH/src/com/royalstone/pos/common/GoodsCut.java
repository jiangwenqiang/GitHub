/*
 * 创建日期 2004-6-25
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.royalstone.pos.common;


/**
 * @author liangxinbiao
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
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
