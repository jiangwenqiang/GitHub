/*
 * �������� 2005-12-3
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.royalstone.pos.web.command;

/**
 * @author zhouzhou
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.royalstone.pos.coupon.CouponTypeInfoList;
import com.royalstone.pos.db.OperatorMinister;
import com.royalstone.pos.util.Response;
import com.royalstone.pos.web.util.DBConnection;


/**
 * @author root
 *
 */
public class LargessCount implements ICommand 
{

	public Object[] excute(Object[] values) {
		System.out.println( "ClerkCommand executed!" );

		Connection con    = null;
		Response response = null;
		Context ctx       = null;
		DataSource ds     = null;
		HashSet set;
		String largessType;
		CouponTypeInfoList couponTypeInfoList = null;
			
		Object[] results = new Object[1];
		if ( values != null && values.length >1 ) {
			set = (HashSet) values[1];
			largessType = (String)values[2];

			try {
				ctx = new InitialContext();
				ds = (DataSource)ctx.lookup( "java:comp/env/dbpos" );
				con = ds.getConnection();
				OperatorMinister minister = new OperatorMinister( con );
				//  �����ۿ�
				if (largessType.equals("0")){
					couponTypeInfoList = minister.getLargess(set);}
				// ����
				else{
					couponTypeInfoList = minister.getLargessL(set);
					}
	
				
			} catch (NamingException e) {
				e.printStackTrace();
				response = new Response( -1, "���ݿ�����ʧ�ܣ���¼δ�ɹ�." );
			} catch (SQLException e) {
				e.printStackTrace();
				response = new Response( -1, "���ݿ�����ʧ�ܣ���¼δ�ɹ�." );
			}finally{
				DBConnection.closeAll(null,null,con);
			}  
		}
		results[0] = couponTypeInfoList;
		return results;
	}
}

