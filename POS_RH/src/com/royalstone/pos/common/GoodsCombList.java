package com.royalstone.pos.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.invoke.realtime.RealTime;
import com.royalstone.pos.invoke.realtime.RealTimeException;
import com.royalstone.pos.notify.UnitOfWork;
import com.royalstone.pos.util.PosConfig;

/**	GoodsList Ϊ��Ʒ�۸��. �ñ��д������Ʒ�ı���,����,�۸�,��Ʒ����,���,��λ.
 * ���Ը�����Ʒ�ı���������ѯ��Ʒ������۸�.
 * ���ڰ汾�����л�����ʵ����Ʒ�۸������,���Ѹ���XML�ĵ�. 
 * ԭ���� load/unload ������ɾ��. 
 * @version 1.0 2004.05.12
 * @author  Mengluoyi
 */

public class GoodsCombList {

	/**
	 * ����һ���յ���Ʒ�۸��.
	 */
	public GoodsCombList() {
		map_vgno = new HashMap();
		map_barcode = new HashMap();
		goodslist = new Vector();
	}

	/**	����Ʒ�۸���������Ʒ�۸����.
	 * @param g
	 */
	public void add(Goods g) {
		map_vgno.put(g.getVgno(), g);
		map_barcode.put(g.getBarcode(), g);
		goodslist.add(g);
	}

	/**	����Ʒ�۸��������XMLԪ��.
	 * @return	XMLԪ��,���б�������Ʒ�۸�������.
	 */
	public Element toElement() {
		Element elm_list = new Element("goodscomblist");

		for (int i = 0; i < goodslist.size(); i++)
			elm_list.addContent(((Goods) goodslist.get(i)).toElement());
		
		//this.maxVgno=((Goods) goodslist.get(goodslist.size()-1)).getVgno();

		return elm_list;
	}

	/**	����Ʒ�۸���XML�ĵ�Ԫ���н����۸�������.
	 * @param root	XML�ĵ��е���Ʒ�۸��Ԫ��.
	 */
	public void fromElement(Element root) {
		List list;
		try {
			System.out.println("node name: " + root.getName());
			list = root.getChildren("goods");

			for (int i = 0; i < list.size(); i++)
				this.add(new Goods((Element) list.get(i)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(String goodsNo, Goods g) {

		Goods goods = (Goods) map_vgno.get(goodsNo);
		if (goods == null) { //new
			if (g != null)
				add(g);
		} else {
			if (g == null) { //delete
				remove(goods);
			} else { //update
				remove(goods);
				add(g);
			}
		}

		UnitOfWork.getInstance().updateGoods(true);

	}

	public void remove(Goods g) {
		goodslist.remove(g);
		map_vgno.remove(g.getVgno());
		map_barcode.remove(g.getBarcode());
	}

	/**	����Ϊfile ���ļ��н�����Ʒ�۸�������.
	 * @param file	���XML��Ʒ�۸����ļ�.
	 */
	public void fromXMLFile(String file) {
		try {
			Document doc = (new SAXBuilder()).build(file);
			fromElement(doc.getRootElement());
		} catch (JDOMException e) {
			// TODO �˴�Ӧ�����⴦��.
			e.printStackTrace();
		}
	}

	public void toXMLFile(String file) {
		try {
			XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
			outputter.setTextTrim(true);
			FileOutputStream out = new FileOutputStream(file);
			outputter.output(new Document(toElement()), out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param  code ��Ʒ������. ����������(ͨ��Ϊ13λ),Ҳ��������Ʒ����(6λ). <br/>
	 * @return a Goods obj which matches the code param, 
	 * null if no matches found.
	 */
	public Goods find(String code) throws RealTimeException {
		//TODO  ���ݸ��� by fire  2005_5_11 
        Goods g;
        PosConfig config=PosConfig.getInstance();
        String isRealTime=config.getString("ISREALTIME");
		if (PosContext.getInstance().isOnLine()&&"ON".equals(isRealTime)) {
 //       	g = RealTime.getInstance().findGoods(code);
        	g = RealTime.getInstance().findGoodsComb(code);
	    } else{

     	g = (Goods) map_barcode.get(code);
		if (g != null)
				return g;
       }
	    return g;
	}



	/**
	 * @return	��Ʒ�۸���ڵĵ�Ʒ��.
	 */
	public int size() {
		return map_vgno.size();
	}

	/**
	 * <code>map_vgno</code>
	 * <br/>map_vgno �ڱ�����vgno ����Ʒ���ϵĶ�Ӧ��ϵ; map_barcode �ڱ�����barcode ����Ʒ���ϵĶ�Ӧ��ϵ.
	 * ����map_vgno/map_barcode, ���Էֱ�ʵ�ְ�vgno/barcode��ѯ��Ʒ�۸�Ĺ���.
	 */
	private HashMap map_vgno, map_barcode;

	/**
	 * <code>goodslist</code>	������Ʒ���϶���,����˳��������Ʒ����( ��Ҫ�� toElement ����ʹ�� ).
	 */
	private Vector goodslist;
	
    private String maxVgno="0";
    public String getMaxVgno(){
        return this.maxVgno;
    }

}
