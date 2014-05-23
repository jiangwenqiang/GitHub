/*
 * Created on 2004-6-4
 */
package com.royalstone.pos.journal;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdom.Element;

import com.royalstone.pos.util.Day;
import com.royalstone.pos.util.PosTime;

/**
 * @author Mengluoyi
 */
public class PosJournal implements Serializable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @param root	保存销售流水信息的XML 根节点.
	 */
	public PosJournal(Element root) 
	{
		List list;
		Element elm_list;
		Element elm_context = root.getChild("context");
		storeid = elm_context.getChild("storeid").getTextTrim();
		posid = elm_context.getChild("posid").getTextTrim();
		cashierid = elm_context.getChild("cashierid").getTextTrim();
		sheetid = atoi(elm_context.getChild("sheetid").getTextTrim());
		if(elm_context.getChild("orderid") != null){
			orderid = atoi(elm_context.getChild("orderid").getTextTrim());
		}else{
			orderid = 0;
		}
		if (elm_context.getChild("orderlstid") != null){
			orderlstid = atoi(elm_context.getChild("orderlstid").getTextTrim());
			}else{
				orderlstid = 0;
				}
		//TODO 沧州富达 by fire  2005_5_11
	    //shiftid = atoi(elm_context.getChild("shiftid").getTextTrim());
		workdate = str2day(elm_context.getChild("workdate").getTextTrim());
		if (elm_context.getChild("mode").getTextTrim().equals("ONLINE")) {
			onlineflag = "0";
		} else {
			onlineflag = "1";
		}

		elm_list = root.getChild("sheet").getChild("paymentlist");
		list = elm_list.getChildren("payment");

		pay_list = new PayRecord[list.size()];
		for (int i = 0; i < list.size(); i++)
			pay_list[i] = new PayRecord((Element) list.get(i), onlineflag);

		elm_list = root.getChild("sheet").getChild("salelist");
		list = elm_list.getChildren("sale");
		sale_list = new SaleRecord[list.size()];

		for (int i = 0; i < list.size(); i++)
			sale_list[i] = new SaleRecord((Element) list.get(i));
	}

	/**	保存流水到数据库中.
	 * @param connection	数据库连接
	 * @throws SQLException
	 */
	public void save(Connection connection) throws SQLException 
	{
		try {
			boolean duplicated = isDuplicated(connection);

			connection.setAutoCommit(false);
			
			updSheetid(connection);
			if (!duplicated) writePayLog(connection);
			// 断定单是否是预销售单
			if (isT()){
				if (sale_list[0].vgno().equals("999999")){
					updOrderid(connection);
				}
				// 插入Save_j表 listno 流水号是预订单号
				PreparedStatement st_sale = prepare4Sale(connection, duplicated);
				for (int i = 0; i < sale_list.length; i++) insertSale( st_sale, sale_list[i] );
				st_sale.executeBatch();
				// 把预销售插入order_lst表中
				PreparedStatement st_order = prepare4Order_lst(connection,duplicated);
				for (int i = 0; i < sale_list.length; i++) insertOrder_lst ( st_order, sale_list[i] );
				st_order.executeBatch();
				// 新的预销售表
				if (!isNewOrder(connection)){
					PreparedStatement st_hand = prepare4hand_order(connection,duplicated);
					inserthand_order( st_hand, sale_list[0]);
					st_hand.executeBatch();
				}
				}
			else{
				PreparedStatement st_sale = prepare4Sale(connection, duplicated);
				for (int i = 0; i < sale_list.length; i++) insertSales( st_sale, sale_list[i] );
				st_sale.executeBatch();
			}
			
			int prite = 0;
			PreparedStatement st_pay  = prepare4Payment(connection, duplicated);
			for (int i = 0; i < pay_list.length; i++) {
				if (pay_list[i].type().equals("O")){
					 prite += pay_list[i].value();
					}
				 insertPayment(st_pay, pay_list[i]);
			}
			st_pay.executeBatch();
			
			if (prite != 0 ){
				prepare4Paymenthand(connection,prite);
				}

			connection.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			connection.rollback();
			throw e;
		}
	}

	/**	准备写入销售流水的SQL. 根据参数 duplicated 决定写入哪个表. 
	 * 如果duplicated 为true, 则应将流水写入sale_jrep;
	 * 如果duplicated 为false, 则应将流水写入sale_j;
	 * @param connection	数据库连接
	 * @param duplicated	是否重复流水
	 * @return				准备好的SQL查询对象
	 * @throws SQLException
	 */
	private PreparedStatement prepare4Sale(Connection connection, boolean duplicated) throws SQLException
	{
		String tabname = duplicated ? "sale_jrep" : "sale_j";
		String sql  = 
			"INSERT INTO "
			+ tabname
			+ " ( dt, time, listno,pos_id, cashier_id, waiter_id, "
			+ " vgno, goodsno, placeno, deptno, "
			+ " amount, colorsize, item_value, disc_value, "
			+ " item_type, disc_type, authorizer_id, x, price, use_goodsno, "
			+ " trainflag, flag1, flag2, flag3, "
			+ " v_type, sublistno ) "
			+ " VALUES ( ?, ?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, "
			+ " ?, ?  ) ; " ;
			//TODO 沧州富达 by fire  2005_5_11
			return connection.prepareStatement( sql );
	}
	
	
	private void insertSale(PreparedStatement pstmt, SaleRecord rec) throws SQLException 
	{
		GregorianCalendar dt = workdate.getGregorian();
		GregorianCalendar time = new GregorianCalendar(
				1900, 0, 1,  
				rec.systime().getHours(),
				rec.systime().getMinutes(),
				rec.systime().getSeconds());
		pstmt.setDate(1, new Date(dt.getTimeInMillis())); 		// dt
		pstmt.setTime(2, new Time(time.getTimeInMillis())); 	// time
		pstmt.setInt(3, sheetid); 								// listno
		pstmt.setString(4, posid); 								// pos_id
		pstmt.setString(5, cashierid); 							// cashier_id
		pstmt.setString(6, rec.waiter()); 						// waiter_id
		pstmt.setString(7, rec.vgno()); 						// vgno
		pstmt.setString(8, rec.barcode()); 						// goodsno
		pstmt.setString(9, rec.placeno()); 						// placeno
		pstmt.setString(10, rec.deptid()); 						// deptno
		pstmt.setLong(11, rec.qty()); 							// amount
		pstmt.setString(12, rec.colorsize()); 					// colorsize
		pstmt.setDouble(13, ((double) rec.itemvalue()) / 100.0);
		pstmt.setDouble(14, ((double) rec.discvalue()) / 100.0);
		pstmt.setString(15, rec.type()); 						// item_type
		pstmt.setString(16, rec.disctype());					// disc_type
		pstmt.setString(17, rec.authorizer()); 					// authorizerid
		pstmt.setInt(18, rec.x()); 								// x
		pstmt.setDouble(19, ((double) rec.stdprice()) / 100.0); // price
		pstmt.setString(20, rec.orgcode()); 					// usebarcodeid

		pstmt.setString(21, rec.trainflag()); 					// trainflag
		pstmt.setString(22, "0"); 								// flag1
		pstmt.setString(23, "0"); 								// flag2
		pstmt.setString(24, onlineflag); 						// flag3
		pstmt.setString(25, "0"); 								// v_type
		pstmt.setInt(26, orderid); 									// sublistno
		pstmt.addBatch();
		
	}
	
	/**
	 * @param pstmt
	 * @param rec
	 * @throws SQLException
	 */
	private void insertsale(PreparedStatement pstmt, SaleRecord rec) throws SQLException 
	{
		GregorianCalendar dt = workdate.getGregorian();
		GregorianCalendar time = new GregorianCalendar(
				1900, 0, 1,  
				rec.systime().getHours(),
				rec.systime().getMinutes(),
				rec.systime().getSeconds());
		pstmt.setDate(1, new Date(dt.getTimeInMillis())); 		// dt
		pstmt.setTime(2, new Time(time.getTimeInMillis())); 	// time
		pstmt.setInt(3, orderid); 								// listno
		pstmt.setString(4, posid); 								// pos_id
		pstmt.setString(5, cashierid); 							// cashier_id
		pstmt.setString(6, rec.waiter()); 						// waiter_id
		pstmt.setString(7, rec.vgno()); 						// vgno
		pstmt.setString(8, rec.barcode()); 						// goodsno
		pstmt.setString(9, rec.placeno()); 						// placeno
		pstmt.setString(10, rec.deptid()); 						// deptno
		pstmt.setLong(11, rec.qty()); 							// amount
		pstmt.setString(12, rec.colorsize()); 					// colorsize
		pstmt.setDouble(13, ((double) rec.itemvalue()) / 100.0);
		// item_value
		pstmt.setDouble(14, ((double) rec.discvalue()) / 100.0);
		// disc_value
		pstmt.setString(15, rec.type()); 						// item_type
		pstmt.setString(16, rec.disctype());					// disc_type
		pstmt.setString(17, rec.authorizer()); 					// authorizerid
		pstmt.setInt(18, rec.x()); 								// x
		pstmt.setDouble(19, ((double) rec.stdprice()) / 100.0); // price
		pstmt.setString(20, rec.orgcode()); 					// usebarcodeid

		pstmt.setString(21, rec.trainflag()); 					// trainflag
		pstmt.setString(22, "0"); 								// flag1
		pstmt.setString(23, "0"); 								// flag2
		pstmt.setString(24, onlineflag); 						// flag3
		pstmt.setString(25, "0"); 								// v_type
		pstmt.setInt(26, 1); 									// sublistno
//TODO 沧州富达 by fire  2005_5_11
//		pstmt.setDate(27, new Date(dt.getTimeInMillis()));
//		pstmt.setInt(28, shiftid);
		pstmt.addBatch();
		
	}
	
	// 预订单插入销售流水记录
	private void insertSales(PreparedStatement pstmt, SaleRecord rec) throws SQLException 
	{
		GregorianCalendar dt = workdate.getGregorian();
		GregorianCalendar time = new GregorianCalendar(
				1900, 0, 1,  
				rec.systime().getHours(),
				rec.systime().getMinutes(),
				rec.systime().getSeconds());
		pstmt.setDate(1, new Date(dt.getTimeInMillis())); 		// dt
		pstmt.setTime(2, new Time(time.getTimeInMillis())); 	// time
		pstmt.setInt(3, sheetid); 								// listno
		pstmt.setString(4, posid); 								// pos_id
		pstmt.setString(5, cashierid); 							// cashier_id
		pstmt.setString(6, rec.waiter()); 						// waiter_id
		pstmt.setString(7, rec.vgno()); 						// vgno
		pstmt.setString(8, rec.barcode()); 						// goodsno
		pstmt.setString(9, rec.placeno()); 						// placeno
		pstmt.setString(10, rec.deptid()); 						// deptno
		pstmt.setLong(11, rec.qty()); 							// amount
		pstmt.setString(12, rec.colorsize()); 					// colorsize
		pstmt.setDouble(13, ((double) rec.itemvalue()) / 100.0);
		pstmt.setDouble(14, ((double) rec.discvalue()) / 100.0);
		pstmt.setString(15, rec.type()); 						// item_type
		pstmt.setString(16, rec.disctype());					// disc_type
		pstmt.setString(17, rec.authorizer()); 					// authorizerid
		pstmt.setInt(18, rec.x()); 								// x
		pstmt.setDouble(19, ((double) rec.stdprice()) / 100.0); // price
		pstmt.setString(20, rec.orgcode()); 					// usebarcodeid

		pstmt.setString(21, rec.trainflag()); 					// trainflag
		pstmt.setString(22, "0"); 								// flag1
		pstmt.setString(23, "0"); 								// flag2
		pstmt.setString(24, onlineflag); 						// flag3
		pstmt.setString(25, "0"); 								// v_type
		pstmt.setLong(26, orderid); 									// sublistno
		pstmt.addBatch();
		
	}
	
	private PreparedStatement prepare4Order_lst(Connection connection,boolean duplicated ) throws SQLException
	{
	
		String tabname = duplicated ? "order_jrep" : "order_j";
		String sql  = 
			"INSERT INTO "
			+ tabname
			+ " ( dt, time,orderid, listno, pos_id, cashier_id, waiter_id, "
			+ " vgno, goodsno, placeno, deptno, "
			+ " amount, colorsize, item_value, disc_value, "
			+ " item_type, disc_type, authorizer_id, x, price, use_goodsno, "
			+ " trainflag, flag1, flag2, flag3, "
			+ " v_type ) "
			+ " VALUES ( ?, ?, ?, ?, ?, ?,?,"
			+ " ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, "
			+ " ? ) ; " ;
			//TODO 沧州富达 by fire  2005_5_11
			return connection.prepareStatement( sql );
	}
	
	// 预销售索引表SQL语句
	private PreparedStatement prepare4hand_order(Connection connection,boolean duplicated) throws SQLException
	{
		String tabname = duplicated ? "hand_order_jrep" : "hand_order";
	
		String sql  = 
			"INSERT INTO "
			+ tabname
			+ " ( dt, time,orderid, listno, pos_id,  "
			+ " start_m, end_m,vgno ) "
			+ " VALUES ( ?, ?, ?, ?, ?, "
			+ " ?, ? ,? ) ; " ;
			//TODO 沧州富达 by fire  2005_5_11
			return connection.prepareStatement( sql );
	}
	
	// 预销售索引表数据插入
	private void inserthand_order(PreparedStatement pstmt, SaleRecord rec) throws SQLException 
	{
		
		GregorianCalendar dt = workdate.getGregorian();
		GregorianCalendar time = new GregorianCalendar(
				1900, 0, 1,  
				rec.systime().getHours(),
				rec.systime().getMinutes(),
				rec.systime().getSeconds());
		pstmt.setDate(1, new Date(dt.getTimeInMillis())); 		// dt
		pstmt.setTime(2, new Time(time.getTimeInMillis())); 	// time
		pstmt.setLong(3,orderid);								// Order_id
		pstmt.setInt(4, sheetid); 								// listno
		pstmt.setString(5, posid); 								// pos_id
	
		pstmt.setDouble(6,((double) rec.factvalue()) / 100.0);
		pstmt.setDouble(7,((double) rec.factvalue()) / 100.0);
		
		pstmt.setString(8,rec.vgno());							// vgno			
		pstmt.addBatch();
		
	}
	
	/**
	 * @param pstmt
	 * @param rec
	 * @throws SQLException
	 */
//	 预销售流水表数据插入
	private void insertOrder_lst(PreparedStatement pstmt, SaleRecord rec) throws SQLException 
	{
		GregorianCalendar dt = workdate.getGregorian();
		GregorianCalendar time = new GregorianCalendar(
				1900, 0, 1,  
				rec.systime().getHours(),
				rec.systime().getMinutes(),
				rec.systime().getSeconds());
		pstmt.setDate(1, new Date(dt.getTimeInMillis())); 		// dt
		pstmt.setTime(2, new Time(time.getTimeInMillis())); 	// time
		pstmt.setInt(3,orderid);								// Order_id
		pstmt.setInt(4, sheetid); 								// listno
		pstmt.setString(5, posid); 								// pos_id
		pstmt.setString(6, cashierid); 							// cashier_id
		pstmt.setString(7, rec.waiter()); 						// waiter_id
		pstmt.setString(8, rec.vgno()); 						// vgno
		pstmt.setString(9, rec.barcode()); 						// goodsno
		pstmt.setString(10, rec.placeno()); 						// placeno
		pstmt.setString(11, rec.deptid()); 						// deptno
		pstmt.setLong(12, rec.qty()); 							// amount
		pstmt.setString(13, rec.colorsize()); 					// colorsize
		pstmt.setDouble(14, ((double) rec.itemvalue()) / 100.0);
		pstmt.setDouble(15, ((double) rec.discvalue()) / 100.0);
		pstmt.setString(16, rec.type()); 						// item_type
		pstmt.setString(17, rec.disctype());					// disc_type
		pstmt.setString(18, rec.authorizer()); 					// authorizerid
		pstmt.setInt(19, rec.x()); 								// x
		pstmt.setDouble(20, ((double) rec.stdprice()) / 100.0); // price
		pstmt.setString(21, rec.orgcode()); 					// usebarcodeid

		pstmt.setString(22, rec.trainflag()); 					// trainflag
		pstmt.setString(23, "0"); 								// flag1
		pstmt.setString(24, "0"); 								// flag2
		pstmt.setString(25, onlineflag); 						// flag3
		pstmt.setString(26, "0"); 								// v_type								// sublistno
		pstmt.addBatch();
		
	}
	

	
	// 断定是否是预订单
	// 是，返回true
	// 否，返回false
	private boolean isT(){
		if (orderid == 0){
			return false;
			}
		return true;
		}

	/**	准备写入支付流水的SQL. 根据参数 duplicated 决定写入哪个表. 
	 * 如果duplicated 为true, 则应将流水写入pay_jrep;
	 * 如果duplicated 为false, 则应将流水写入pay_j;

	 * @param connection	数据库连接
	 * @param duplicated	是否重复流水.
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement prepare4Payment( Connection connection, boolean duplicated) throws SQLException
	{
		PreparedStatement pstmt;
		String tabname = (duplicated) ? "pay_jrep" : "pay_j";

		String sql = 
			"INSERT INTO "
			+ tabname
			+ " ( dt, time, listno, sublistno, "
			+ " pos_id, cashier_id, pay_reason, pay_type, "
			+ " curren_code, pay_value, equiv_value, cardno, "
			+ " trainflag, flag3 ) "
			+ " VALUES "
			+ "( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );" ;
      //TODO 沧州富达 by fire  2005_5_11
			pstmt = connection.prepareStatement( sql );
			return pstmt;
	}

	/**
	 * @param pstmt	专为插入支付记录而准备的SQL.
	 * @param rec	支付记录
	 * @throws SQLException
	 */
	private void insertPayment(PreparedStatement pstmt, PayRecord rec) throws SQLException
	{
		GregorianCalendar dt = workdate.getGregorian();
		GregorianCalendar time = new GregorianCalendar(
				1900, 0, 1,
				rec.systime().getHours(),
				rec.systime().getMinutes(),
				rec.systime().getSeconds());
		pstmt.setDate(1, new Date(dt.getTimeInMillis())); // dt
		pstmt.setTime(2, new Time(time.getTimeInMillis())); // time
		pstmt.setInt(3, sheetid); // listno
		pstmt.setInt(4, 0);
		pstmt.setString(5, posid); // posid
		pstmt.setString(6, cashierid); // cashierid
		pstmt.setString(7, rec.reason());
		pstmt.setString(8, rec.type());
		pstmt.setString(9, rec.currency());
		pstmt.setDouble(10, ((double) rec.value()) / 100.0);
		pstmt.setDouble(11, ((double) rec.value_equiv()) / 100.0);
		pstmt.setString(12, rec.cardno());
		pstmt.setString(13, rec.trainflag());
		pstmt.setString(14, onlineflag);
		//TODO 沧州富达 by fire  2005_5_11
		//pstmt.setDate(15, new Date(dt.getTimeInMillis()));
		//pstmt.setInt(16, shiftid);
		pstmt.addBatch();

	}
	
	private void prepare4Paymenthand( Connection connection ,int price) throws SQLException
	{
		PreparedStatement pstmt;

		String sql = " UPDATE hand_order set end_m = end_m + ?  "
			+ " WHERE  orderid = ? ; ";
      //TODO 沧州富达 by fire  2005_5_11
			pstmt = connection.prepareStatement( sql );

			pstmt.setDouble(1, -((double) price) / 100.0);
			pstmt.setInt(2, orderid);
			pstmt.addBatch();
			pstmt.executeBatch();
	}
	

	/**	检查流水是否重复上传的流水
	 * @param connection	数据库连接
	 * @return				true	该笔流水已经写入数据库;<br/> 
	 * 						false.	该笔流水尚未写入数据库.
	 * @throws SQLException
	 */
	public boolean isDuplicated(Connection connection) throws SQLException
	{
		boolean dup = false;
		String sql = " SELECT listno FROM pay_log  "
			+ " WHERE listno = ? AND pos_id = ? AND dt = ? ; ";
		PreparedStatement qstmt = connection.prepareStatement ( sql );

		qstmt.setInt(1, sheetid);
		qstmt.setString(2, posid);
		Date dt = new Date((workdate.getGregorian()).getTimeInMillis());
		qstmt.setDate(3, dt);

		ResultSet rs = qstmt.executeQuery();
		if (rs.next()) dup = true;
		rs.close();

		return dup;
	}
	
	/**
	 * 
	 * @author zhouzhou
	 * @param  connection 数据库连接
	 * @return 	true   该笔流水已经写入数据库
	 *              false  该笔流水尚未写入数据库
	 * @throws		SQLException
	 */
	
	// 返回值
	// true 初始表中存在这个账号
	// false 初始表中不存在这个账号
	public boolean isNewOrder(Connection connection) throws SQLException
	{
		boolean dup = false;
		String sql = " SELECT orderid FROM hand_order  "
			+ " WHERE orderid = ?";
		PreparedStatement qstmt = connection.prepareStatement ( sql );

		// 需要修改
		qstmt.setInt(1, orderid);
		
		ResultSet rs = qstmt.executeQuery();
		if (rs.next()) dup = true;
		rs.close();

		return dup;
	}

	/**	在 pay_log 表中插入一条流水日志信息. 以防止写入重复的流水.
	 * @param connection	数据库连接
	 * @throws SQLException
	 */
	public void writePayLog(Connection connection) throws SQLException
	{
		PreparedStatement pstmt;

		String sql = "INSERT INTO pay_log ( dt, listno, pos_id, flag3 ) "
			+ " VALUES "
			+ "( ?, ?, ?, ? ); ";
		pstmt = connection.prepareStatement( sql );

		Date dt = new Date((workdate.getGregorian()).getTimeInMillis());
		pstmt.setDate(1, dt);
		pstmt.setInt(2, sheetid);
		pstmt.setString(3, posid);
		pstmt.setString(4, onlineflag);
		pstmt.addBatch();
		pstmt.executeBatch();
	}

	/**	更新pos_lst 表中的listno, 记录最后一笔写入数据库的流水号.
	 * @param connection	数据库连接
	 * @throws SQLException
	 */
	public void updSheetid(Connection connection) throws SQLException
	{
		PreparedStatement pstmt;
		String sql = " UPDATE pos_lst set listno = ?  "
					+ " WHERE  pos_id = ? and listno<?; ";
		pstmt = connection.prepareStatement(sql);

		pstmt.setInt(1, sheetid);
		pstmt.setString(2, posid);
		pstmt.setInt(3, sheetid);
		pstmt.addBatch();
		pstmt.executeBatch();
	}
	
	/**
	 *更新 order_lst 表中的listno,记录最后一笔写入数据库的流水号。
	 *@param connection 数据库连接	
	 *@throws SQLException
	 */
	
	public void updOrderid(Connection connection) throws SQLException
	{
		PreparedStatement pstmt;
		String sql = "UPDATE order_lst set listno = ? "
					+ " WHERE posid = ?  and listno< ?; ";
		pstmt = connection.prepareStatement(sql);
		
		pstmt.setInt(1,orderlstid);
		pstmt.setString(2,posid);
		pstmt.setInt(3,orderlstid);
		pstmt.addBatch();
		pstmt.executeBatch();
		}
	

	//////////////////// internal routines ////////////////////
	/**
	 * @param sysdate
	 * @return
	 */
	private static Day str2day(String sysdate) {
		return new Day(
			atoi(sysdate.substring(0, 4)),
			atoi(sysdate.substring(5, 7)),
			atoi(sysdate.substring(8, 10)));
	}

	/**
	 * @param systime
	 * @return
	 */
	private static PosTime str2time(String systime) {
		return new PosTime(
			atoi(systime.substring(0, 2)),
			atoi(systime.substring(3, 5)),
			atoi(systime.substring(6, 8)));
	}

	/**
	 * @param s
	 * @return
	 */
	private static int atoi(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw e;
		}
	}

	/**
	 * Comment for <code>pay_list</code>
	 */
	PayRecord[] pay_list = null;
	/**
	 * Comment for <code>sale_list</code>
	 */
	SaleRecord[] sale_list = null;
	/**
	 * Comment for <code>storeid</code>
	 */
	String storeid;
	/**
	 * Comment for <code>posid</code>
	 */
	String posid;
	/**
	 * Comment for <code>cashierid</code>
	 */
	String cashierid;
	/**
	 * Comment for <code>workdate</code>
	 */
	Day workdate;
	/**
	 * Comment for <code>orderid</code>
	 */
	int orderid;
	/**
	 * Comment for <code>orderlstid</code>
	 */
	int orderlstid;
	/**
	 * Comment for <code>sheetid</code>
	 */
	int sheetid;
	/**
	 * Comment for <code>shiftid</code>	班次号
	 */
	int shiftid;
	/**
	 * <code>onlineflag</code>
	 */
	String onlineflag;
}
