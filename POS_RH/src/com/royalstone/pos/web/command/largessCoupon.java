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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.royalstone.pos.coupon.CouponTypeList;
import com.royalstone.pos.db.OperatorMinister;
import com.royalstone.pos.util.Response;
import com.royalstone.pos.web.util.DBConnection;


/**
 * @author root
 *
 */
public class largessCoupon implements ICommand 
{

	public Object[] excute(Object[] values) {
		System.out.println( "ClerkCommand executed!" );

		Connection con    = null;
		Response response = null;
		Context ctx       = null;
		DataSource ds     = null;
		CouponTypeList couponType =  new CouponTypeList();
		String vgno = null;
			
		Object[] results = new Object[1];
		if ( values != null && values.length >1 ) {
			vgno = (String) values[1];

			try {
				ctx = new InitialContext();
				ds = (DataSource)ctx.lookup( "java:comp/env/dbpos" );
				con = ds.getConnection();
				OperatorMinister minister = new OperatorMinister( con );
				
				couponType = minister.getCouponType(vgno);
				
				if (couponType == null){
					couponType.setException("�������");
					}
	
				
			} catch (NamingException e) {
				e.printStackTrace();
				couponType.setException("�������");
			} catch (SQLException e) {
				e.printStackTrace();
				couponType.setException("���ݿ��ѯ����");
			}finally{
				DBConnection.closeAll(null,null,con);
			}  
		}
		results[0] = couponType;
		return results;
	}
}

