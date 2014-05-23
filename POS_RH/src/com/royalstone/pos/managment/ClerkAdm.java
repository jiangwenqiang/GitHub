/*
 * Created on 2004-6-4
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.royalstone.pos.managment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import com.royalstone.pos.common.RequestAlterPin;
import com.royalstone.pos.common.RequestLogon;
import com.royalstone.pos.coupon.CouponTypeInfoList;
import com.royalstone.pos.coupon.CouponTypeList;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;
import com.royalstone.pos.util.Response;

/**
 * @author root
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ClerkAdm 
{
	public ClerkAdm( String host, int port ) 
	{
		this.host = host;
		this.port = port;
	}

	public Response getClerk( String posid, String cashierid, String plainpin )
	{
		RequestLogon req = new RequestLogon( posid, cashierid, plainpin );
		Response result = null;
		try {
			servlet = new URL ( "http", host, port, "/pos41/DispatchServlet" );
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.ClerkCommand";
			params[1] = req;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) result = (Response) results[0];
		}
		catch ( IOException e ) {
			e.printStackTrace();
			result = new Response( -1, "ERROR: 与后台服务器连接失败。" );
		}
		
		return result;
	}
	
	public Response getOrderid( String posid)
	{
		RequestLogon req = new RequestLogon( posid);
		Response result = null;
		try {
			servlet = new URL ( "http", host, port, "/pos41/DispatchServlet" );
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.orderidCommand";
			params[1] = req;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
				
			}

			if (results != null && results.length > 0) result = (Response) results[0];
		}
		catch ( IOException e ) {
			e.printStackTrace();
			result = new Response( -1, "ERROR: 与后台服务器连接失败。" );
		}
		return result;
	}

	// 获取商品与卷的对应队列信息
	public CouponTypeInfoList getVgnoCouponType(HashSet set , String largessType) throws IOException{
		CouponTypeInfoList couponTypeInfoList = null;
		Response results = null;
		try {
			servlet = new URL ( "http", host, port, "/pos41/DispatchServlet" );
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] = "com.royalstone.pos.web.command.LargessCount";
			params[1] = set;
			params[2] = largessType;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] result = null;

			if (mvO != null) {
				result = mvO.getValues();
			}

			if (result != null && result.length > 0) 
				couponTypeInfoList = (CouponTypeInfoList) result[0];
		}
		catch ( IOException e ) {
			e.printStackTrace();
			throw e;
		}
		return couponTypeInfoList;
		}

	public Response alterPin( String posid, String cashierid, String pin_old, String pin_new )
	{
		RequestAlterPin req = new RequestAlterPin( posid, cashierid, pin_old, pin_new );
		Response result = null;
		try {
			servlet = new URL ( "http", host, port, "/pos41/DispatchServlet" );
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.AlterPinCommand";
			params[1] = req;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) result = (Response) results[0];
		}
		catch ( IOException e ) {
			e.printStackTrace();
			result = new Response( -1, "ERROR: 与后台连接失败。" );
		}
		
		return result;
	}
	
	public Response  getEnd_m( int Order_id )
	{
		RequestLogon req = new RequestLogon(Order_id);
		Response result = null;
		try {
			servlet = new URL ( "http", host, port, "/pos41/DispatchServlet" );
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.orderCommand";
			params[1] = req;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) result = (Response) results[0];
		}
		catch ( IOException e ) {
			e.printStackTrace();
			result = new Response( -1, "ERROR: 与后台服务器连接失败。" );
		}
		
		return result;
	}
	
	public CouponTypeList getCouponType(String vgno)  {
		
		Response results = null;
		CouponTypeList coupon = new CouponTypeList();
		try {
			servlet = new URL ( "http", host, port, "/pos41/DispatchServlet" );
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.largessCoupon";
			params[1] = vgno;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] result = null;

			if (mvO != null) {
				result = mvO.getValues();
			}

			if (result != null && result.length > 0) 
				 coupon = (CouponTypeList) result[0];
		}
		catch ( IOException e ) {
			e.printStackTrace();
			return coupon;
		}
		return coupon;
		
		
		}
	
	private URL servlet;
	private HttpURLConnection conn;
	private String host;
	private int port;
}
