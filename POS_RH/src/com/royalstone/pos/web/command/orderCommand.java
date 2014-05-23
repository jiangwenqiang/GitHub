/*
 * 创建日期 2005-10-24
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.royalstone.pos.web.command;

/**
 * @author zhouzhou
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
	import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.royalstone.pos.common.Operator;
import com.royalstone.pos.common.RequestLogon;
import com.royalstone.pos.db.OperatorMinister;
import com.royalstone.pos.util.Response;
import com.royalstone.pos.web.util.DBConnection;


	/**
	 * @author root
	 *
	 */
	public class orderCommand implements ICommand 
	{

		public Object[] excute(Object[] values) {
			System.out.println( "ClerkCommand executed!" );

			Connection con    = null;
			Response response = null;
			Context ctx       = null;
			DataSource ds     = null;
			RequestLogon req  = null;
				
			Object[] results = new Object[1];
			if ( values != null && values.length >1 ) {
				req = (RequestLogon) values[1];
				int id = req.getOrder_id();


				try {
					ctx = new InitialContext();
					ds = (DataSource)ctx.lookup( "java:comp/env/dbpos" );
					con = ds.getConnection();
					OperatorMinister minister = new OperatorMinister( con );
					
					Operator money=minister.getMoney(id);
	                response = new Response(money);
		
					
				} catch (NamingException e) {
					e.printStackTrace();
					response = new Response( -1, "数据库连接失败，登录未成功." );
				} catch (SQLException e) {
					e.printStackTrace();
					response = new Response( -1, "数据库连接失败，登录未成功." );
				}finally{
					DBConnection.closeAll(null,null,con);
				}  
			}
			results[0] = response;
			return results;
		}
	}
