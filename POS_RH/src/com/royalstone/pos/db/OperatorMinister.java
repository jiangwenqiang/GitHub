/*
 * Created on 2004-6-15
 */
package com.royalstone.pos.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.royalstone.pos.common.Operator;
import com.royalstone.pos.coupon.CouponTypeInfo;
import com.royalstone.pos.coupon.CouponTypeInfoList;
import com.royalstone.pos.coupon.CouponTypeList;
import com.royalstone.pos.journal.DataSource;

/**
 * @author Mengluoyi
 */
public class OperatorMinister {

	public static void main(String[] args) {
		DataSource datasrc = new DataSource( "127.0.0.0", 1433, "MyShoppos" );
		Connection con = datasrc.open( "sa", "sa" );
		OperatorMinister m = new OperatorMinister( con );
		try {
			int op = m.getListNO( "123" );
			System.out.println( "Operator: " + op);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param connection	Database connection.
	 */
	public OperatorMinister( Connection connection )
	{
		this.connection = connection;
	}

	/**
	 * @param operatorid:
	 * @param connection:
	 * @return	null if not found; a Operator obj whose id is operatorid.
	 */
	public Operator getOperator( String operatorid ) throws SQLException
	{
		Operator op;
		PreparedStatement pstmt = connection.prepareStatement(
			" SELECT clerk_id, name, passwd, levelid, max_disc FROM clerk_lst WHERE clerk_id = ?; "  );
                 //   " SELECT clerk_id, name, passwd, levelid FROM clerk_lst WHERE clerk_id = ? "  );
		pstmt.setString( 1, operatorid );

		ResultSet rs = pstmt.executeQuery();
		if( ! rs.next() ) return null;
		else {
			String clerk_id = rs.getString( "clerk_id" );
			String name 	= rs.getString( "name" );
			String passwd 	= rs.getString( "passwd" );
			int levelid 	= rs.getInt( "levelid" );
			int max_disc    =rs.getInt("max_disc");
			PreparedStatement stmt_power = connection.prepareStatement(
				" SELECT power_id FROM power_lst WHERE levelid = " + levelid  );

			ResultSet rs_power = stmt_power.executeQuery();

			Vector v = new Vector();
			while( rs_power.next() ) v.add( new Integer( rs_power.getInt("power_id") ) );
			int[] powers = new int[ v.size() ];
			for ( int i=0; i<v.size(); i++ ) powers[i] = ((Integer) v.get(i)).intValue();

			rs_power.close();

			//op = new Operator( clerk_id, passwd, name, powers );
                        op = new Operator( clerk_id, passwd, name,max_disc ,powers );
		}
		rs.close();
		return op;
	}

	public void alterOperator( Operator op ) throws SQLException
	{
		String sql = " UPDATE clerk_lst SET passwd = ? WHERE clerk_id = ?; ";
		PreparedStatement pstmt = connection.prepareStatement( sql );

		pstmt.setString( 1, op.getPinEncrypted() );
		pstmt.setString( 2, op.getId() );

		pstmt.executeUpdate();
	}

      public int getListNO(String posID)throws SQLException{
       Operator op;
        ResultSet rs = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                " SELECT listno FROM pos_lst WHERE pos_id = ?; "  );
            pstmt.setString( 1, posID );

            rs = pstmt.executeQuery();
            if( ! rs.next() ) return -1;
            else {
                return rs.getInt("listno");
                }
        } catch (SQLException e) {
           throw(e);
        } finally {
            if(rs!=null)
                try {
                    rs.close();
                } catch (SQLException e) {}
        }


    }
      // 获取预销售中的佘额
      public Operator getMoney(int lading_id) throws SQLException{
      	ResultSet rs = null;
      	Operator op;
      	try {
      		PreparedStatement pstmt = connection.prepareStatement(
      				"SELECT end_m FROM hand_order WHERE orderid = ?;");
      		pstmt.setInt(1,lading_id);
      		
      		 rs = pstmt.executeQuery();
      		if ( !rs.next()){
      			String M = "-1";
      			op = new Operator(M, false);
      			return op;
      			}
      		else{
      			double money = rs.getDouble("end_m");
      			String MP = String.valueOf(money);
      			op = new Operator( MP , true );
      			return op;
      			}
      		}
      	catch(SQLException e){
      		throw(e);
      		}
      	}
      // ------------------------------
      
      
      // 获取数量折扣促销资料
      public CouponTypeInfoList getLargess(HashSet set) throws SQLException{
      	ResultSet rs = null;
      	ResultSet ros = null;
      	CouponTypeInfoList couponTypeInfoList = new CouponTypeInfoList();
     
      	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
        String today=sdf.format(new Date());
      	Operator op;
      	String typeid = null;     	
      	try {
      		Iterator iter=set.iterator(); 
      		while(iter.hasNext()){
      			String vgno=(String)iter.next();
      			CouponTypeInfo couponTypeInfo = new CouponTypeInfo();
      		PreparedStatement psmtt = connection.prepareStatement("select a.Qty , a.typeid From GoodsCouponProm a, GoodsCouponPromItem b where b.GoodsID = ? and a.typeid = b.typeid and startdate <= ? and enddate >= ? and a.couponflag = '1'");
      		psmtt.setString(1,vgno);
      		psmtt.setString(2,today);
      		psmtt.setString(3,today);
      		rs = psmtt.executeQuery();
      		if ( !rs.next()){
      			rs.close();
      			
      			PreparedStatement psmt = connection.prepareStatement("select a.Qty , a.typeid From GoodsCouponProm a, GoodsCouponPromItem b where b.GoodsID = ? and a.typeid = b.typeid and startdate <= ? and enddate >= ? and a.couponflag = '0'");
      			psmt.setString(1,vgno);
          		psmt.setString(2,today);
          		psmt.setString(3,today);
          		rs = psmt.executeQuery();
          		if ( !rs.next()){
          			couponTypeInfo.setVgnoName(vgno);
          			couponTypeInfo.setException("没有卷内容");
          			couponTypeInfoList.add(couponTypeInfo);
          		}else{
          			couponTypeInfo.setVgnoName(vgno);
          			couponTypeInfo.setCouponType(rs.getString("typeid"));
          			couponTypeInfo.setCouponCount(rs.getBigDecimal("Qty"));
              		couponTypeInfoList.add(couponTypeInfo);
          			}
          		
      			}
      		else{
      			couponTypeInfo.setVgnoName(vgno);
      			couponTypeInfo.setCouponType(rs.getString("typeid"));
      			couponTypeInfo.setCouponCount(rs.getBigDecimal("Qty"));
          		couponTypeInfoList.add(couponTypeInfo);
      			}
      		}
      		rs.close();
      		return couponTypeInfoList;
      	}
      	catch(SQLException e){
      		throw(e);
      		}
      	}
      // ------------------------------
      
      // 获取碰销信息
      public CouponTypeInfoList getLargessL(HashSet set) throws SQLException{
      	ResultSet rs = null;
      	ResultSet ros = null;
      	CouponTypeInfoList couponTypeInfoList = new CouponTypeInfoList();
     
      	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
        String today=sdf.format(new Date());
      	Operator op;
      	String typeid = null;     	
      	try {
      		Iterator iter=set.iterator(); 
      		while(iter.hasNext()){
      			String vgno=(String)iter.next();
      			CouponTypeInfo couponTypeInfo = new CouponTypeInfo();
      		PreparedStatement psmtt = connection.prepareStatement("select a.Qty , a.typeid From GoodsCouponProm a, GoodsCouponPromItem b where b.GoodsID = ? and a.typeid = b.typeid and startdate <= ? and enddate >= ? and a.couponflag = '0'");
      		psmtt.setString(1,vgno);
      		psmtt.setString(2,today);
      		psmtt.setString(3,today);
      		rs = psmtt.executeQuery();
      		if ( !rs.next()){
      			rs.close();
      			
      			PreparedStatement psmttl = connection.prepareStatement("select a.Qty , a.typeid From GoodsCouponProm a, GoodsCouponPromItem b where b.GoodsID = ? and a.typeid = b.typeid and startdate <= ? and enddate >= ? and a.couponflag = '1'");
          		psmttl.setString(1,vgno);
          		psmttl.setString(2,today);
          		psmttl.setString(3,today);
          		rs = psmttl.executeQuery();
          		if ( !rs.next() ){
          			couponTypeInfo.setVgnoName(vgno);
          			couponTypeInfo.setException("没有卷内容");
          			couponTypeInfoList.add(couponTypeInfo);
          			}
          		else{
          			couponTypeInfo.setVgnoName(vgno);
          			couponTypeInfo.setCouponType(rs.getString("typeid"));
          			couponTypeInfo.setCouponCount(rs.getBigDecimal("Qty"));
              		couponTypeInfoList.add(couponTypeInfo);
          			}
      			}
      		else{
      			couponTypeInfo.setVgnoName(vgno);
      			couponTypeInfo.setCouponType(rs.getString("typeid"));
      			couponTypeInfo.setCouponCount(rs.getBigDecimal("Qty"));
          		couponTypeInfoList.add(couponTypeInfo);
      			}
      		}
      		rs.close();
      		return couponTypeInfoList;
      	}
      	catch(SQLException e){
      		throw(e);
      		}
      	}

      // 获取促销资料
      public CouponTypeList getCouponType(String vgno){
      	ResultSet rs = null;
      	CouponTypeList couponType = new CouponTypeList();
      	Operator op;    	
      	try {
      		PreparedStatement psmtt = connection.prepareStatement("select a.Qty , a.typeid  from GoodsCouponProm a, GoodsCouponPromItem b where b.GoodsID = ? and a.typeid = b.typeid  ");
      		psmtt.setString(1,vgno);
      		rs = psmtt.executeQuery();
      		while (rs.next()){
      			String coupon =  rs.getString("typeid");
      			couponType.add(coupon);
      			}
      		if (couponType.size() == 0){
      			rs.close();
      			couponType.setException("没有找到赠送商或者网络错误");
      			return couponType;
      			}
      		rs.close();
      		return couponType;
      	}
      	catch(SQLException e){
      		return couponType;
      		}
      	}
      
      // ------------------------------
      public Operator getOrderid(String posid) throws SQLException{
      	ResultSet rs = null;
     	Operator op;
      	try {
     		PreparedStatement pstmt = connection.prepareStatement(
     				"SELECT listno FROM order_lst WHERE posid = ?;"
     				);
      		pstmt.setString(1,posid);
      		
      		 rs = pstmt.executeQuery();
      		if ( !rs.next()){
      			int id = 0;
      			op = new Operator (id);
      			return op;
      			}
      		else{
     			int oid = rs.getInt("listno");
     			op = new Operator( oid );
     			return op;
    			}
     		}
     	catch(SQLException e){
     		throw(e);
     		}
     	}
      
      public Operator setOrderid(String posid) throws SQLException{
      	ResultSet rs = null;
     	Operator op;
      	try {
     		PreparedStatement pstmt = connection.prepareStatement(
     				"INSERT INTO order_lst (listno, posid) VALUES (?,?);"
     				);
      		pstmt.setInt(1,1);
      		pstmt.setString(2,posid);
      		pstmt.addBatch();
      		pstmt.executeBatch();
      		op = new Operator(0);
      		return op;
     		}
     	catch(SQLException e){
     		throw(e);
     		}
     	}
	private Connection connection;
}
