package com.royalstone.pos.common;

import java.io.Serializable;

import org.jdom.Element;

public class ProductProperty implements Serializable {

		/**
		 * <code>id</code> 生产属性id
		 */
		private String id;

		/**
		 * <code>name</code> 生产属性名称
		 */
		private String name;

		/**
		 * <code>price</code> 商品价格
		 */
		private double price;

		/**
		 * <code>flag</code> 是否有效
		 */
		private int flag;

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}
		
		public ProductProperty() {
			super();
		}
		
		public ProductProperty(String id, String name, double price, int flag) {
			super();
			this.id = id;
			this.name = name;
			this.price = price;
			this.flag = flag;
		}

		public ProductProperty(Element e) {
			// TODO 自动生成构造函数存根
			this.id = e.getChildTextTrim("id");
			this.name = e.getChildTextTrim("name");
			this.price = Double.parseDouble(e.getChildTextTrim("price"));
			this.flag = Integer.parseInt(e.getChildTextTrim("flag"));
		}

		public Element toElement() {
			Element e = new Element("prop");			
			e.addContent(new Element("id").addContent(getId()));
			e.addContent(new Element("name").addContent(getName()));
			e.addContent(new Element("price").addContent(""+getPrice()));
			e.addContent(new Element("flag").addContent(""+getFlag()));
			return e;
		}
}
