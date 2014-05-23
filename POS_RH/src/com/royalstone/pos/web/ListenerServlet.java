package com.royalstone.pos.web;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */

public class ListenerServlet extends HttpServlet {

	private boolean stop = false;
	private int interval=10000;

	public void init(ServletConfig arg0) throws ServletException {
		super.init(arg0);
		String strInterval=arg0.getInitParameter("interval");
		try{
			interval=Integer.parseInt(strInterval);	
		}catch(NumberFormatException ex){
			interval=10000;
		}
		System.out.println("ListenerServlet Start.......");
		Thread t = new Thread(new DBListener());
		t.start();
	}

	public void destroy() {
		super.destroy();
		stop = true;
		System.out.println("ListenerServlet Stop.......");
	}

	private class DBListener implements Runnable {

		Connection con = null;
		Context ctx = null;
		DataSource ds = null;

		public void run() {
			while (!stop) {
				try {
					try {
						ctx = new InitialContext();
						if (ctx != null)
							ds = (DataSource) ctx.lookup("java:comp/env/dbpos");
						if (ds != null)
							con = ds.getConnection();
//TODO  屏蔽Tomcat实时调价功能	 沧州富达 by fire  2005_5_11 					
//						if (con != null) {
//							NotifySender notifySender = new NotifySender(con);
//							notifySender.send();
//						}
					} catch (NamingException e1) {
						e1.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						DBConnection.closeAll(null, null, con);
					}
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
