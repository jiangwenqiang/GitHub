/*
 * 创建日期 2004-6-25
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.royalstone.pos.web;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.common.GoodsExtList;
import com.royalstone.pos.db.PosMinister;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class PriceTableExt extends HttpServlet {

	private static String RESPONSE_CONTENT_TYPE = "text/html";

	public void doPost(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		OutputStream out = response.getOutputStream();

		Connection con = null;
		Context ctx = null;
		DataSource ds = null;

		try {
			response.setContentType(RESPONSE_CONTENT_TYPE);

			ctx = new InitialContext();
			if (ctx != null)
				ds = (DataSource) ctx.lookup("java:comp/env/dbpos");
			if (ds != null)
				con = ds.getConnection();

			if (con != null) {
				GoodsExtList glst = PosMinister.getGoodsExtList(con);
				XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
				outputter.setTextTrim(true);
				outputter.output(new Document(glst.toElement()), out);
			}

			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeAll(null, null, con);
		}
	}
}
