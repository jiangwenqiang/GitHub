package com.royalstone.pos.web.command;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.royalstone.pos.common.OperatorList;
import com.royalstone.pos.db.PosMinister;
import com.royalstone.pos.util.Response;
import com.royalstone.pos.web.util.DBConnection;

/**
 * 下载Operator信息
 * @author root
 */
public class OperatorCommand implements ICommand 
{

	public OperatorCommand () 
	{}

	public Object[] excute (Object[] values) {
		Object[] results = new Object[1];
		
		Connection con    = null;
		Response response = null;
		Context ctx       = null;
		DataSource ds     = null;

		if ( values != null  ) {
			try {
				ctx = new InitialContext();
				if( ctx != null ) ds = (DataSource)ctx.lookup( "java:comp/env/dbpos" );
				if( ds  != null ) con = ds.getConnection();  
				
				if( con != null ){
					System.out.println("dbpos connected in OperatorCommand!");   
					OperatorList list = PosMinister.getOperatorList( con );
					response = new Response( 0, "OK", list );
				}else {
					response = new Response( -1, "Database connection failed in OperatorCommand." );
				}

				results[0] = response;
				return results;
			} catch (Exception e) {
				e.printStackTrace();
				results[0] = new Response( -1, "Failed." );
			}finally{
				// 异常处理（？) '改动部份
				try{
					DBConnection.closeAll(null,null,con);
					return results;
					}
				catch (Exception e)
				{
					e.printStackTrace();
					results[0] = new Response(-1, "Failed.");
					}
			}
		}
		return null;
	}
}

