package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.royalstone.pos.common.Operator;
import com.royalstone.pos.common.RequestAlterPin;
import com.royalstone.pos.db.OperatorMinister;
import com.royalstone.pos.util.Response;
import com.royalstone.pos.web.util.DBConnection;


/**
 * @author root
 *
 */
public class AlterPinCommand implements ICommand 
{

	public Object[] excute(Object[] values) {
		System.out.println( "alterPinCommand executed!" );

		Connection con    = null;
		Response response = null;
		Context ctx       = null;
		DataSource ds     = null;
		RequestAlterPin req  = null;
			
		Object[] results = new Object[1];
		if ( values != null && values.length >1 ) {
			req = (RequestAlterPin) values[1];
			System.out.println( req );
			String posid = req.getPosid();
			String cashierid = req.getCashierid();
			String pin_old = req.getPinOld();
			String pin_new = req.getPinNew();

			try {
				ctx = new InitialContext();
				ds = (DataSource)ctx.lookup( "java:comp/env/dbpos" );
				con = ds.getConnection();
				OperatorMinister minister = new OperatorMinister( con );
				Operator op = minister.getOperator( cashierid );
				if( op == null ) response = new Response( -1, "失败！" );
				if( op != null && !op.checkPlainPin( pin_old ) ) 
					response = new Response( -1, "密码不正确" );
				if( op != null && op.checkPlainPin( pin_old ) ) {
					op.setPlainPin( pin_new );
					minister.alterOperator( op );
					response = new Response( 0, "成功", op );
				} 
				
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



