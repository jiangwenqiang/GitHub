package com.royalstone.pos.common;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.royalstone.pos.invoke.realtime.RealTime;
import com.royalstone.pos.invoke.realtime.RealTimeException;
import com.royalstone.pos.notify.UnitOfWork;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.PosConfig;

public class GoodsProductList {

	public GoodsProductList() {
		map_vgno = new HashMap();
		productlist =new Vector();
	}
	
	public void add(GoodsProduct gp) {
		map_vgno.put(gp.getVgno(), gp);
		productlist.add(gp);
	}
	
	public void update(String goodsNo, GoodsProduct gp) {

		Goods goods = (Goods) map_vgno.get(goodsNo);
		if (goods == null) { //new
			if (gp != null)
				this.add(gp);
		} else {
			if (gp == null) { //delete
				this.remove(gp);
			} else { //update
				this.remove(gp);
				this.add(gp);
			}
		}

		UnitOfWork.getInstance().updateGoods(true);
	}
	
	private void remove(GoodsProduct gp) {
		map_vgno.remove(gp.getVgno());
	}

	public int size() {
		return map_vgno.size();
	}
	
	public GoodsProduct find(String code) throws RealTimeException {
		PosConfig config = PosConfig.getInstance();
		String isRealTime = config.getString("ISREALTIME");
		String isFast = config.getString("ISFASTLOAD");
		GoodsProduct goodproduct = null;
		if (PosContext.getInstance().isOnLine() && "ON".equals(isRealTime)) {
			goodproduct = RealTime.getInstance().findGoodsProduct(code);
		} else if ("OFF".equals(isFast)) {
			HashMap priceMap = pos.core.priceListMap;
			HashMap barcodeMap = pos.core.barcodeMap;
			String mapCode = (String) barcodeMap.get(code);
			if (mapCode != null)
				code = mapCode;
			if (code.length() != 6)
				return null;
			int i = 0;
			for (; i < priceMap.size(); i++) {

				String tempCode = (String) priceMap.get(Integer.toString(i));
				int iCode = Integer.parseInt(tempCode);
				int findCode = Integer.parseInt(code);
				if (findCode <= iCode)
					break;

			}

			if (i < priceMap.size()) {
				// good = this.findFormXML("price/price" + i + ".xml", code);
			}
		} else {
			goodproduct = (GoodsProduct) map_vgno.get(code);
		}
		return goodproduct;
	}
	
	/**	���������Ա�������XMLԪ��.
	 * @return	XMLԪ��,���б������������Ա������.
	 */
	public Element toElement() {
		Element elm_list = new Element("goodsprod");
		for (int i = 0; i < productlist.size(); i++)
			elm_list.addContent(((GoodsProduct) productlist.get(i)).toElement());
		//this.maxVgno=((Goods) goodslist.get(productlist.size()-1)).getVgno();
		return elm_list;
	}

	public void fromElement(Element root) {
		List list = null;
		try {
			list = root.getChildren("props");
			for (int i = 0; i < list.size(); i++)
				this.add(new GoodsProduct((Element) list.get(i)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fromXMLFile(String file) {
		try {
			Document doc = (new SAXBuilder()).build(file);
			fromElement(doc.getRootElement());
		} catch (JDOMException e) {
			// TODO �˴�Ӧ�����⴦��.
			e.printStackTrace();
		}
	}
	
	/**
	 * <code>propertylist</code>	���������������϶���,����˳��������������( ��Ҫ�� toElement ����ʹ�� ).
	 */
	private Vector productlist;
	
	/**
	 * <code>map_vgno</code> <br/>map_vgno �ڱ�����vgno ����Ʒ���ϵĶ�Ӧ��ϵ; map_barcode
	 * �ڱ�����barcode ����Ʒ���ϵĶ�Ӧ��ϵ. ����map_vgno/map_barcode,
	 * ���Էֱ�ʵ�ְ�vgno/barcode��ѯ��Ʒ�۸�Ĺ���.
	 */
	private HashMap map_vgno;
}
