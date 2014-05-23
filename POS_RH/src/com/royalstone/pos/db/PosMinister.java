/*
 * Created on 2004-6-6
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.royalstone.pos.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.common.Accurate;
import com.royalstone.pos.common.AccurateList;
import com.royalstone.pos.common.Goods;
import com.royalstone.pos.common.GoodsCombList;
import com.royalstone.pos.common.GoodsCutList;
import com.royalstone.pos.common.GoodsExt;
import com.royalstone.pos.common.GoodsExtList;
import com.royalstone.pos.common.GoodsList;
import com.royalstone.pos.common.GoodsProduct;
import com.royalstone.pos.common.GoodsProductList;
import com.royalstone.pos.common.Operator;
import com.royalstone.pos.common.OperatorList;
import com.royalstone.pos.common.ProductProperty;
import com.royalstone.pos.io.PosKeyMap;
import com.royalstone.pos.journal.DataSource;
import com.royalstone.pos.util.Exchange;
import com.royalstone.pos.util.ExchangeList;
import com.royalstone.pos.util.PosConfig;

/**
 * @author root
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PosMinister {

	public PosMinister() {
	}

	// 测试主入口
	public static void main(String[] args) {
		DataSource datasrc = new DataSource("172.16.7.163", 1433, "ApplePos");
		Connection con = datasrc.open("sa", "sa");

		Element root = getElementIni(con, "P001");
		try {
			FileOutputStream owriter = new FileOutputStream("property.NEW.xml");

			XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
			outputter.setTextTrim(true);
			outputter.output(new Document(root), owriter);
			owriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static GoodsList getGoodsList() {
		return null;
	}

	public static AccurateList getAccurateList(Connection connection) {
		System.err.println("getAccuratesList ...");
		AccurateList list = new AccurateList();
		ResultSet rs = null;
		try {
			PreparedStatement pstmt = connection
					.prepareStatement(" SELECT cardlevelid, deptid, accurate FROM accurate; ");

			rs = pstmt.executeQuery();
			while (rs.next()) {
				int cardLevelID = rs.getInt("cardlevelid");
				int deptID = rs.getInt("deptid");
				String accurate = rs.getString("accurate");
				list.add(new Accurate(cardLevelID, deptID, accurate));
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
		}
	}

	public static GoodsList getGoodsList(Connection connection) {
		System.err.println("getGoodsList ...");
		GoodsList list = new GoodsList();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement(" SELECT vgno, goodsno, gname, deptno, spec, uname,  v_type, p_type, price, x,producttype FROM price_lst; ");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String vgno = rs.getString("vgno");
				String goodsno = rs.getString("goodsno");
				String gname = rs.getString("gname");
				String deptno = rs.getString("deptno");
				String spec = rs.getString("spec");
				String uname = rs.getString("uname");
				String v_type = rs.getString("v_type");
				String p_type = rs.getString("p_type");
				double price = rs.getDouble("price");
				int x = rs.getInt("x");
				int goods_type = Integer.parseInt(v_type);
				int i_price = (int) Math.rint(price * 100);
				if (spec == null)
					spec = "";
				if (uname == null)
					uname = "";
				int prodtype = rs.getInt("producttype");
				list.add(new Goods(vgno, goodsno, gname, deptno, spec, uname,
						i_price, goods_type, x, p_type,prodtype));
			}
			rs.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static GoodsProductList getPropertyList(Connection connection) {
		System.err.println("getPropertyList ...");
		
		GoodsProductList list = new GoodsProductList();
		GoodsProduct gp =  null;
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT vgno,id,name,price,flag FROM GoodsProductType order by vgno;");
			
			ResultSet rs = pstmt.executeQuery();
			String vgno = null;
			String id = null;
			String name = null;
			double price = 0;
			int flag = 0;
			
			String cur_vgno = "";
			while (rs.next()) {
				vgno = rs.getString("vgno");
				id = rs.getString("id");
				name = rs.getString("name");
				price = Double.parseDouble(rs.getString("price"));
				flag = Integer.parseInt(rs.getString("flag"));
				
				if(cur_vgno != null && vgno != null )
				{
//					if(cur_vgno.equals(vgno))
//					{
//						gp.addProperty(new ProductProperty(id, name, price, flag));	
//					}
//					else
//					{
//						if(gp != null) list.add(gp);
//						//新一个商品的生产属性
//						gp = new GoodsProduct(vgno);
//						gp.addProperty(new ProductProperty(id, name, price, flag));
//					}
					if(!cur_vgno.equals(vgno))
					{
						if(gp != null) list.add(gp);
						//新一个商品的生产属性
						gp = new GoodsProduct(vgno);
					}
					gp.addProperty(new ProductProperty(id, name, price, flag));					
				}
				else continue;
				
				if(vgno != null) cur_vgno = vgno;
				
			}
			rs.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static GoodsCombList getGoodsCombList(Connection connection) {
		System.err.println("getGoodsCombList ...");
		GoodsCombList list = new GoodsCombList();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement(" SELECT vgno, goodsno, gname, uname, spec, price, x FROM goodscombine; ");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String vgno = rs.getString("vgno");
				String goodsno = rs.getString("goodsno");
				String gname = rs.getString("gname");
				String deptno = "";
				String spec = rs.getString("spec");
				String uname = rs.getString("uname");
				String v_type = "";
				String p_type = "n";
				double price = rs.getDouble("price");
				int x = rs.getInt("x");
				int goods_type = 0;
				int i_price = (int) Math.rint(price * 100);
				if (spec == null)
					spec = "";
				if (uname == null)
					uname = "";
				list.add(new Goods(vgno, goodsno, gname, deptno, spec, uname,
						i_price, goods_type, x, p_type));
			}
			rs.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static GoodsCutList getGoodsCutList(Connection connection) {
		System.err.println("getGoodsCombList ...");
		GoodsCutList list = new GoodsCutList();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT vgno, goodsno, gname, uname, spec, price, x FROM goodscut; ");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String vgno = rs.getString("vgno");
				String goodsno = rs.getString("goodsno");
				String gname = rs.getString("gname");
				String deptno = "";
				String spec = rs.getString("spec");
				String uname = rs.getString("uname");
				String v_type = "";
				String p_type = "n";
				double price = rs.getDouble("price");
				int x = rs.getInt("x");
				int goods_type = 0;
				int i_price = (int) Math.rint(price * 100);
				if (spec == null)
					spec = "";
				if (uname == null)
					uname = "";

				list.add(new Goods(vgno, goodsno, gname, deptno, spec, uname,
						i_price, goods_type, x, p_type));
			}
			rs.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//TODO
	public static GoodsList getGoodsList(Connection connection, String maxCode,
			int selectCount) {

		GoodsList list = new GoodsList();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement(" SELECT top "
							+ Integer.toString(selectCount)
							+ " vgno, goodsno, gname, deptno, spec, uname,  v_type, p_type, price, x FROM price_lst where vgno>'"
							+ maxCode + "' order by vgno; ");

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String vgno = rs.getString("vgno");
				String goodsno = rs.getString("goodsno");
				String gname = rs.getString("gname");
				String deptno = rs.getString("deptno");
				String spec = rs.getString("spec");
				String uname = rs.getString("uname");
				String v_type = rs.getString("v_type");
				String p_type = rs.getString("p_type");
				double price = rs.getDouble("price");
				int x = rs.getInt("x");
				int goods_type = Integer.parseInt(v_type);
				int i_price = (int) Math.rint(price * 100);
				if (spec == null)
					spec = "";
				if (uname == null)
					uname = "";

				list.add(new Goods(vgno, goodsno, gname, deptno, spec, uname,
						i_price, goods_type, x, p_type));
			}
			rs.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static GoodsExtList getGoodsExtList(Connection connection) {
		System.err.println("getGoodsExtList ...");
		GoodsExtList list = new GoodsExtList();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT price_lst.vgno, goodsext6.goodsno, price_lst.gname, price_lst.deptno, price_lst.spec, price_lst.uname, "
							+ "price_lst.v_type, price_lst.p_type,price_lst.price,price_lst.x,pknum "
							+ "FROM price_lst,goodsext6 where goodsext6.vgno=price_lst.vgno");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String vgno = rs.getString("vgno");
				String goodsno = rs.getString("goodsno");
				String gname = rs.getString("gname");
				String deptno = rs.getString("deptno");
				String spec = rs.getString("spec");
				String uname = rs.getString("uname");
				String v_type = rs.getString("v_type");
				String p_type = rs.getString("p_type");
				double price = rs.getDouble("price");
				int x = rs.getInt("x");
				int goods_type = Integer.parseInt(v_type);
				int i_price = (int) Math.rint(price * 100);
				if (spec == null)
					spec = "";
				if (uname == null)
					uname = "";
				int pknum = rs.getInt("pknum");

				list.add(new GoodsExt(vgno, goodsno, gname, deptno, spec,
						uname, i_price, goods_type, x, p_type, pknum));
			}
			rs.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static OperatorList getOperatorList(Connection connection) {
		System.err.println("getOperatorList ...");
		OperatorList list = new OperatorList();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement(" SELECT clerk_id, name, passwd, levelid FROM clerk_lst; ");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String clerk_id = rs.getString("clerk_id");
				String name = rs.getString("name");
				String passwd = rs.getString("passwd");
				int levelid = rs.getInt("levelid");

				PreparedStatement stmt_power = connection
						.prepareStatement(" SELECT power_id FROM power_lst WHERE levelid = "
								+ levelid);

				ResultSet rs_power = stmt_power.executeQuery();

				Vector v = new Vector();
				while (rs_power.next())
					v.add(new Integer(rs_power.getInt("power_id")));
				int[] powers = new int[v.size()];
				for (int i = 0; i < v.size(); i++)
					powers[i] = ((Integer) v.get(i)).intValue();

				rs_power.close();

				Operator op = new Operator(clerk_id, passwd, name, powers);
				list.add(op);
			}
			rs.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param operatorid:
	 * @param connection:
	 * @return	null if not found; a Operator obj whose id is operatorid. 
	 */
	public static Operator getOperator(Connection connection, String operatorid) {
		System.err.println("getOperator ...");
		Operator op;
		try {
			PreparedStatement pstmt = connection
					.prepareStatement(" SELECT clerk_id, name, passwd, levelid FROM clerk_lst WHERE clerk_id = ?; ");
			pstmt.setString(1, operatorid);

			ResultSet rs = pstmt.executeQuery();
			if (!rs.next())
				return null;
			else {
				String clerk_id = rs.getString("clerk_id");
				String name = rs.getString("name");
				String passwd = rs.getString("passwd");
				int levelid = rs.getInt("levelid");

				PreparedStatement stmt_power = connection
						.prepareStatement(" SELECT power_id FROM power_lst WHERE levelid = "
								+ levelid);

				ResultSet rs_power = stmt_power.executeQuery();

				Vector v = new Vector();
				while (rs_power.next())
					v.add(new Integer(rs_power.getInt("power_id")));
				int[] powers = new int[v.size()];
				for (int i = 0; i < v.size(); i++)
					powers[i] = ((Integer) v.get(i)).intValue();

				rs_power.close();

				op = new Operator(clerk_id, passwd, name, powers);
			}
			rs.close();
			return op;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PosKeyMap getKeyMap(Connection connection, String posid) {
		System.err.println("getKeyMap ...");
		PosKeyMap map = new PosKeyMap();
		try {

			String sql = " SELECT key_value, keymap_value FROM keybd_cfg k JOIN pos_lst p "
					+ " ON (k.keybd_type=p.keybd_type) "
					+ " WHERE pos_id = ? ;";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, posid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int key_value = rs.getInt("key_value");
				int keymap_value = rs.getInt("keymap_value");
				map.setFunction(key_value, keymap_value);
			}
			rs.close();
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ExchangeList getExchangeList(Connection connection) {
		System.err.println("getExchangeList ...");
		String sql = " SELECT curren_code, rate FROM rate_lst; ";
		ExchangeList list = new ExchangeList();
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String curren_code = rs.getString("curren_code");
				double rate = rs.getDouble("rate");
				list.add(new Exchange(curren_code, rate));
			}
			rs.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Element getCardType(Connection connection) {
		System.err.println("getCardType ...");
		Element card = new Element("card");
		String sql = " SELECT bank_id, cardtype, bank,cardnote FROM cardnote order by cardtype; ";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String bank_id = rs.getString("bank_id");
				String cardtype = rs.getString("cardtype");
				String bank = rs.getString("bank");
				String note = rs.getString("cardnote");
				Element card_type = new Element("card_type");
				card_type
						.addContent(new Element("bank_id").addContent(bank_id));
				card_type.addContent(new Element("cardtype")
						.addContent(cardtype));
				card_type.addContent(new Element("bank").addContent(bank));
				card_type.addContent(new Element("cardnote").addContent(note));
				card.addContent(card_type);
			}
			rs.close();
			return card;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PosConfig getConfig(String posid) {
		return null;
	}

	public static Element getElementCodeMap(Connection connection) {
		System.err.println("getElementCodeMap ...");
		String sql = " SELECT plu_format FROM weight_code; ";
		Element elm = new Element("barcodemap");
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String plu_format = rs.getString("plu_format");
				elm.addContent(new Element("barcodeformat")
						.addContent(plu_format));
			}
			rs.close();
			return elm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Element getElementHotKey(Connection connection, String posid) {
		System.err.println("getElementHotKey ...");
		String sql = " SELECT key_value, plu FROM hotkey_cfg k JOIN pos_lst p "
				+ " ON (k.keybd_type=p.keybd_type) " + " WHERE pos_id = ? ;";

		Element elm_map = new Element("hotkeymap");
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, posid);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int key_value = rs.getInt("key_value");
				String plu = rs.getString("plu");
				Element elm = new Element("hotkey");
				elm.addContent(new Element("value").addContent("" + key_value));
				elm.addContent(new Element("code").addContent(plu));
				elm_map.addContent(elm);
			}
			rs.close();
			return elm_map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Element getElementHotKey_hxa(Connection connection,
			String posid) {
		System.err.println("getElementHotKey ...");
		String sql = " SELECT key_value, plu, flag FROM hotkey_cfg k JOIN pos_lst p "
				+ " ON (k.keybd_type=p.keybd_type) " + " WHERE pos_id = ? ;";

		Element elm_map = new Element("hotkeymap");
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, posid);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int key_value = rs.getInt("key_value");
				String plu = rs.getString("plu");
				int flag = rs.getInt("flag");
				Element elm = new Element("hotkey");
				elm.addContent(new Element("value").addContent("" + key_value));
				elm.addContent(new Element("code").addContent(plu));
				elm.addContent(new Element("flag").addContent("" + flag));
				elm_map.addContent(elm);
			}
			rs.close();
			return elm_map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Element getElementIndicator(Connection connection) {
		System.err.println("Fetch deptid for oil ...");
		String sql = " SELECT deptid FROM oildept; ";
		Element indicator = new Element("indicator");
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int deptid = rs.getInt("deptid");
				String deptno = (new DecimalFormat("000000")).format(deptid);
				indicator.addContent(new Element("deptid").addContent(deptno));
			}
			rs.close();
			return indicator;
		} catch (Exception e) {
			System.err.println("ERROR: failed when selecting oildept.");
			e.printStackTrace();
			return null;
		}
	}

	public static Element getElementParms(Connection connection, String posid) {
		System.err.println("getElementParms ...");
		String sql = " SELECT para, val FROM pos_ini i "
				+ " JOIN pos_lst p ON (i.posgroup=p.posgroup) "
				+ " WHERE p.pos_id = ? ; ";
		Element config = new Element("config");
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, posid);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String para = rs.getString("para");
				String val = rs.getString("val");
				Element parm = new Element("parameter");
				parm.addContent(new Element("name").addContent(para));
				parm.addContent(new Element("value").addContent(val));
				config.addContent(parm);
			}
			rs.close();
			return config;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Element getElementBasic(Connection connection, String posid) {
		System.err.println("getElementBasic ...");
		String sql = " SELECT placeno,posgroup FROM pos_lst WHERE pos_id = ?; ";
		Element basic = new Element("basic");
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, posid);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String placeno = rs.getString("placeno");
				String posgroup = rs.getString("posgroup");
				basic.addContent(new Element("posid").addContent(posid));
				basic.addContent(new Element("placeno").addContent(placeno));
				basic.addContent(new Element("posgroup").addContent(posgroup));
			}
			rs.close();
			return basic;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Element getElementIni(Connection connection, String posid) {
		Element elm_pos = new Element("pos");
		elm_pos.addContent(getElementBasic(connection, posid));

		PosKeyMap map = getKeyMap(connection, posid);
		elm_pos.addContent(map.toElement());

		elm_pos.addContent(getElementHotKey(connection, posid));
		//TODO 沧州富达 by fire  2005_5_11

		ExchangeList ex_lst = getExchangeList(connection);
		elm_pos.addContent(ex_lst.toElement());

		Element elm_codemap = getElementCodeMap(connection);
		elm_pos.addContent(elm_codemap);

		elm_pos.addContent(getElementParms(connection, posid));
		//TODO 沧州富达 by fire  2005_5_11

		return elm_pos;
	}
}
