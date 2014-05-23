package com.royalstone.pos.common;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

public class GoodsProduct implements Serializable {

	private String vgno = null;
	private Vector prop = null; 
	
	public GoodsProduct(String vgno, Vector prop) {
		super();
		this.vgno = vgno;
		this.prop = prop;
	}

	public GoodsProduct(Element e) 
	{
		this.vgno = e.getChildTextTrim("vgno");
		List list = e.getChildren("prop");
		if(prop == null) prop =  new Vector();
		for(int i = 0;i < list.size();++i)
		{
			prop.add(new ProductProperty((Element)list.get(i)));
		}
	}

	public GoodsProduct(String vgno) {
		// TODO 自动生成构造函数存根
		this.vgno = vgno;
		this.prop = new Vector();
	}
	
	public void addProperty(ProductProperty pp)
	{
		if(this.prop != null)
		this.prop.add(pp);
	}

	/**
	 * <code>vgno</code> 商品编码
	*/
	
	public String getVgno() {
		return vgno;
	}

	public void setVgno(String vgno) {
		this.vgno = vgno;
	}

	public Vector getProp() {
		return prop;
	}

	public void setProp(Vector prop) {
		this.prop = prop;
	}
	
	public String getPropInfo(String id)
	{
		ProductProperty pp = null;
		for(int i = 0;i <prop.size();++i)
		{
			pp = (ProductProperty)prop.elementAt(i);
			if(pp.getId().equals(id))
			{
				return /*pp.getId()+ */pp.getName();
			} 
		}
		return null;
	}
	
	public ProductProperty getProperty(String id)
	{
		ProductProperty pp = null;
		for(int i = 0;i <prop.size();++i)
		{
			pp = (ProductProperty)prop.elementAt(i);
			if(pp.getId().equals(id))
			{
				return pp;
			} 
		}
		return null;
	}

	public Element toElement() {
		// TODO 自动生成方法存根
		Element es = new Element("props");
		Element e = null;
		ProductProperty pp = null;
		es.addContent(new Element("vgno").addContent(getVgno()));
		for(int i = 0; i< prop.size(); ++i)
		{
			pp = (ProductProperty)prop.elementAt(i);
			if(pp != null) e = pp.toElement();
			if(e != null) es.addContent(e);
			e = null;
		}
		return es;
	}
	
	public void fromElement(Element e) {
		List list = null;
		try {
			this.vgno = e.getChildTextTrim("vgno");
			list = e.getChildren("props");
			for (int i = 0; i < list.size(); i++)
				this.prop.add(new ProductProperty((Element) list.get(i)));

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
