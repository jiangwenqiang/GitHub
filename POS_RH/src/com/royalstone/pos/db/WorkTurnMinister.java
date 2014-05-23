/*
 * Created on 2004-6-16
 */
package com.royalstone.pos.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.royalstone.pos.common.Operator;
import com.royalstone.pos.common.PosEnv;
import com.royalstone.pos.common.PosRequest;
import com.royalstone.pos.common.RequestLogon;
import com.royalstone.pos.journal.DataSource;
import com.royalstone.pos.util.Day;
import com.royalstone.pos.util.Response;
import com.royalstone.pos.util.WorkTurn;

/**
 * ����: POS4.1 Ҫ��֧�ְ�ι���.
 * �����ι����,��ҵ���������±仯:<br/>
 * 1)��Ҫ�����¿���κ͹رհ�εĹ���.<br/>
 * 2)��¼ʱ��Ҫ���POS���İ���Ƿ�. ���<br/>
 * @author Mengluoyi<br/>
 *
 */
public class WorkTurnMinister {
	public static void main(String[] args) 
	{
		DataSource datasrc = new DataSource( "172.16.7.163", 1433, "ApplePos" );
		Connection con = datasrc.open( "sa", "sa" );
		WorkTurnMinister minister = new WorkTurnMinister( con );
		RequestLogon req = new RequestLogon( "P001", "0001", "0001", 4 );
		Response r = minister.logon( req );
		PosEnv env = (PosEnv) r.getObject();
		System.out.println( r );
		System.out.println( env );
	}

	/**
	 * Constructor.
	 */
	public WorkTurnMinister( Connection connection )
	{
		this.connection = connection;
	}
	
	/**
	 * ��¼������Ҫ�������¼�飺
	 * 1.���û��Ƿ����?
	 * 2.�û������Ƿ���ȷ?
	 * 3.POS�����Ƿ���ȷ?
	 * 	4.�Ƿ������û���ָ�����������Ͽ�ʼһ�����? ��κ��Ƿ���ȷ? 
	 * @param req
	 * @return
	 */
	public Response logon( RequestLogon req )
	{
		String posid  		= req.getPosid();
		String cashierid 	= req.getCashierid();
		String plainpin 	= req.getPlainPin();
		int    shiftid		= req.getShiftid();  
		int	   listno; 
		WorkTurn turn;
		String localip = req.getIp();
		Response response = null;

		// search clerk_lst for operator by id.
		try {
			OperatorMinister m = new OperatorMinister( connection );
			
			System.err.println( "����������..." );
			if( !isPosAvailable( posid, cashierid ) )  {
				String warning = "�������ݿ����Ҳ���������" + posid + "�����������ļ���";
				return new Response ( -1, warning );
			}
			
			System.err.println( "��������Ա..." );
			// ��������Ա.
			Operator op;
			if( ( op = m.getOperator(cashierid) ) == null ){
				String warning = "�������ݿ����Ҳ�������Ա" + cashierid + "��������������ȷ������Ա�š�";
				return new Response ( -1, warning );
			}
			
			String curOp = getPower(posid); 
			// �������Ա�Ƿ������ָ�����������ϵ�¼.
			if(!checkPower(cashierid,curOp)){
				String warning = "����Ȩ��POS��" + posid + "�������»����Ȩ��";
				return new Response ( -1, warning );
			}

			System.err.println( "�������..." );
			// �������.
			if( !op.checkPlainPin( plainpin ) ) 
				return new Response( -1, "���벻��ȷ" );
			
			System.err.println( "�鿴����Ա�Ƿ�����������������¼..." );
			// �鿴����Ա�Ƿ�����������������¼.
			String pos_other = getPos4Cashier( cashierid );
			if( pos_other != null && !pos_other.equals(posid) ) 
				return new Response( -1, "���ȴ�����̨ " + pos_other + " �˳���" );
			
			System.err.println( "�鿴���������..." );
			// �鿴�������Ļ���.
			// ����������ڰ�α����л�İ��,���û���������ԱҪ���¼�İ�κŲ�һ��,��ܾ���¼.
			WorkTurn turn_active = getActivePosTurn( posid );
			if( turn_active != null && turn_active.getShiftid() != shiftid ){
				String warning = "������İ�κŲ���ȷ��������" 
					+ posid + "��δ����ᣬ��ǰ�������Ϊ��" 
					+ turn_active.toString() + "��";
				return 	new Response( -1, warning );
			}
			
			// ���������IP��ַ�Ƿ����̨��¼��һ��.
			//if(!localip.trim().equals(getPosip(posid).trim()) ){
			if(localip.trim().compareTo(getPosip(posid).trim())!=0 ){
				String warning = "����IP��POS��" 
									+ posid +"��IP������" +posid+ "��IPΪ��" 
									+ getPosip(posid) + "��";
				return 	new Response( -1, warning );
		    }

			// ����������л�İ��, Ҫ������һ���κż������Ա����İ�κ��Ƿ���ȷ.
			// ȡ����Ӧ����ˮ��.
			if( turn_active != null && turn_active.getShiftid() == shiftid ){
				listno = getPosListno( posid );
				setCurrentOperator( posid, cashierid );
				return new Response( 0, "OK", new PosEnv(turn_active, listno+1) );
			}
				
			// ����ڰ�α���û�в��ҵ��������Ļ���, ����ҪΪ����������ʼһ���µİ��.
			// ���Ȳ鿴������α�. �˶�����ԱҪ���¼�İ������������Ƿ�һ��. 
			// �����һ��,��ܾ���¼;���һ��,���¿�һ�����.
			if( turn_active == null ){
				System.err.println( "�鿴�������..." );
				WorkTurn turn_new = getShopClock();
				if( turn_new == null ){
					return new Response( -1, "���󣺰�α�Ϊ�ա�" );
				} else  if( turn_new != null && turn_new.getShiftid() != shiftid ){
					String warning = "��κŲ���ȷ����ǰ���Ϊ��" + turn_new.toString() + "��";
					return 	new Response( -1, warning );
				} else if( turn_new != null && isPosTurnClosed( posid, turn_new ) ){
					return new Response( -1, "����Ҫ���¼�İ���Ѿ��رա�" );
				} else {
					boolean isNewDay = false;
					if (isNewDay4Pos(posid, turn_new)) {
						resetPosListno(posid);
						isNewDay = true;
					}
					System.err.println( "��ʼ�µİ��..." );
					openPosTurn( posid, turn_new );
					listno = getPosListno( posid );
					System.err.println( "��ָ������̨�ϵ�¼..." );
					setCurrentOperator( posid, cashierid );
					return new Response(
						0,
						"OK",
						new PosEnv(turn_new, listno + 1, isNewDay));
				}
			} 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Response( -1, "��¼ʧ�ܣ����ݿ��������" );
		}

		return new Response( -1, "��¼ʧ�ܣ�ԭ������" );
	}
	
	/**
	 * ��̨�˳�����ģ��.
	 * @param req	ǰ̨�������˳�����.
	 * @return		����ǰ̨��Ӧ��.
	 */
	public Response logoff( PosRequest req )
	{
		String posid = req.getPosid();
		try {
			resetCurrentOperator( posid );
			return new Response( 0, "��̨���˹�������ɡ�" );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Response( -1, "��̨����ʧ�ܣ��ٴε�¼���������ѡ�" );
		}
	}
	
	/**
	 * �˷������ں�̨��ᴦ��.
	 * @param req	ǰ̨�����İ������.
	 * @return		����ǰ̨��Ӧ��.
	 */
	public Response closeWork( PosRequest req ) 
	{
		WorkTurn posturn, shopturn;
		Response response;
		String posid = req.getPosid();
		
		try {
			shopturn = getShopClock();
			posturn  = getActivePosTurn( posid );
			if( shopturn != null && posturn != null && shopturn.compareTo(posturn) <= 0 ){
				response = new Response( -1, "���δ�������������ᡣ" );
			} else {
				if(posturn!=null){
					closePosTurn( posid );
					response = new Response ( 0, "OK");
				}else{
					response = new Response( -1, "����Ѿ��رա�" );
				}
			}
		} catch (SQLException e) {
			response = new Response( -1, "Fail!" );
			e.printStackTrace();
		}
		return response;
	}
	
	/**	�鿴�������嵥pos_lst ���Ƿ����ָ��ID��������.
	 * @param posid		������ID.
	 * @param cashierid	����ԱID.
	 * @return
	 * @throws SQLException
	 */
	public boolean isPosAvailable( String posid, String cashierid ) throws SQLException
	{
		posid = posid.toUpperCase();
		boolean available;
		String sql = " SELECT pos_id, current_op FROM pos_lst WHERE pos_id = ?; ";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		ResultSet rs 	= pstmt.executeQuery();
		available = rs.next();
		rs.close();
		pstmt.close();
		return available;
	}
	
	/**	��ѯָ��POS�����Ƿ��л�İ�μ�¼.
	 * @param posid		������ID.
	 * @return	true	�л�İ�μ�¼;<br/>
	 * 			false	û�л�İ�μ�¼;
	 * @throws SQLException
	 */
	private boolean isPosTurnActive( String posid ) throws SQLException
	{
		posid = posid.toUpperCase();
		boolean isActive;
		String sql = " SELECT workdate, shiftid FROM pos_turn WHERE stat = 0 AND pos_id = ?;";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		ResultSet rs 	= pstmt.executeQuery();
		isActive = rs.next();
		rs.close();
		return isActive;
	}
	
	/**	ȡ������posid ��"����".
	 * @param posid		������ID.
	 * @return a WorkTurn obj if a active pos_turn found, else return null.
	 * @throws SQLException
	 */
	private WorkTurn getActivePosTurn( String posid ) throws SQLException
	{
		posid = posid.toUpperCase();
		String sql = " SELECT workdate, shiftid FROM pos_turn WHERE stat = 0 AND pos_id = ?;";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		ResultSet rs 	= pstmt.executeQuery();
		if( rs.next() ){
			Date workdate = rs.getDate( "workdate" );
			int shiftid = rs.getInt( "shiftid" );
			return new WorkTurn( new Day(workdate), shiftid );
		}
		return null;
	}
	
	/**	�鿴���turn ��������posid ���Ƿ��Ѿ��ر�.
	 * @param posid		������ID.
	 * @param turn		�������.
	 * @return	true	�Ѿ��ر�;<br/> false	��δ�ر�.
	 * @throws SQLException
	 */
	private boolean isPosTurnClosed( String posid, WorkTurn turn ) throws SQLException
	{
		posid = posid.toUpperCase();
		boolean isClosed = false;
		
		String sql = " SELECT stat FROM pos_turn WHERE pos_id = ? AND workdate = ? AND shiftid = ? ; ";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		pstmt.setDate( 2, new Date( turn.getWorkDate().getGregorian().getTimeInMillis() ) );
		pstmt.setInt( 3, turn.getShiftid() );
		ResultSet rs 	= pstmt.executeQuery();
		isClosed = ( rs.next() && rs.getInt(1) == 1 );
		rs.close();
		
		return isClosed;	
	}
	
	/**	�жϰ��turn �Ա��Ϊposid ���������Ƿ��µ�һ��Ӫҵ��. 
	 * �˷���������Ա��¼ʱʹ��. �������Ӫҵ���л�,����ˮ��listno ��Ҫ����,���������㴦��.
	 * @param posid		������ID.
	 * @param turn		�������.
	 * @return		true	�µ�Ӫҵ��.<br/> false	�����µ�Ӫҵ��.
	 * @throws SQLException
	 */
	public boolean isNewDay4Pos( String posid, WorkTurn turn ) throws SQLException
	{
		posid = posid.toUpperCase();
		boolean isNewDay = false;
		Date workdate = new Date( turn.getWorkDate().getGregorian().getTimeInMillis() ) ;
		String sql = " SELECT pos_id FROM pos_turn WHERE pos_id = ? AND workdate = ?; ";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		pstmt.setDate( 2, workdate );
		ResultSet rs 	= pstmt.executeQuery();
		isNewDay = ! rs.next();
		rs.close();
		return isNewDay;
	}
	
	/** �ر�ָ��POS���ڰ�α�pos_turn �еİ�μ�¼.
	 * @param posid		������ID.
	 * @throws SQLException
	 */
	private void closePosTurn( String posid ) throws SQLException
	{
		String sql = " UPDATE pos_turn SET stat = 1, end_time = getdate(),IsEndOffLine=0"
					+ " WHERE stat = 0 AND pos_id = ?; ";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		pstmt.executeUpdate();
	}
	
	/**	��POS��α�pos_turn �����һ����μ�¼.
	 * @param posid		������ID.
	 * @param turn		�������.
	 * @throws SQLException
	 */
	private void openPosTurn( String posid, WorkTurn turn ) throws SQLException
	{
		String sql = " INSERT INTO pos_turn ( pos_id, workdate, shiftid ) VALUES ( ?, ?, ? ); ";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		pstmt.setDate( 2, new Date( turn.getWorkDate().getGregorian().getTimeInMillis() ) );
		pstmt.setInt( 3, turn.getShiftid() );
		pstmt.executeUpdate();
	}
	
	/**
	 * ��ѯָ���������Ĺ�����ˮ��.
	 * @param posid		������ID.
	 * @return
	 * @throws SQLException
	 */
	private int getPosListno( String posid ) throws SQLException
	{
		String sql = " SELECT listno FROM pos_lst WHERE pos_id = ?; ";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		ResultSet rs 	= pstmt.executeQuery();
		int listno = -1;
		
		if( rs.next() ) listno = rs.getInt( "listno" );
		return listno;
	}

	/**
	 * ��ѯ��������IP��ַ.
	 * @param posid	����ԱID.
	 * @return		IP��ַ.
	 * @throws SQLException
	 * @author huangxuean
	 */
	public String getPosip(String posid) throws SQLException{
		posid = posid.toUpperCase();
		String ip = null;
		String sql = " SELECT ip FROM pos_lst WHERE pos_id = ?";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			ip = rs.getString("ip");
		}
		return ip;
	}
	
	/**
	 * @param posid
	 * @return
	 * @throws SQLException
	 * @author huangxuean
	 */
	public String getPower(String posid){
		posid = posid.toUpperCase();
		String curOp = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			sql = "select * from pos_lst where pos_id = ?";	
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1,posid);
			rs = pstmt.executeQuery();
			while(rs.next()){
				curOp = rs.getString("current_op");
			}

		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}					
		return curOp;
	}
	
	/**
	 * @param cashierid
	 * @param curOp
	 * @return
	 * @author huangxuean
	 */
	public boolean checkPower(String cashierid, String curOp)
	{
		int auth = 0;
		String sql = "select auth from clerk_lst where clerk_id = ?";	
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, cashierid);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				auth = rs.getInt("auth");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return ( ( auth == 1 ) 					// if cashierid has force logon right,
 			|| ( curOp == null ) 				// if current operator is null,
			|| ( curOp.equals(cashierid) ) ) ;	// if current operator is cashierid.
		
	}

	/**
	 * ��ָ������������ˮ�������㴦��.
	 * @param posid		������ID.
	 * @throws SQLException
	 */
	public void resetPosListno( String posid ) throws SQLException
	{
		posid = posid.toUpperCase();
		String sql = " UPDATE pos_lst set listno = 0 WHERE pos_id = ?; ";
		PreparedStatement pstmt = connection.prepareStatement( sql );
		pstmt.setString( 1, posid );
		pstmt.executeUpdate();
	}
	
	/**
	 * �˷������ڲ�ѯ������ͳһ���.
	 * @return	������ͳһ���.
	 * @throws SQLException
	 */
	private WorkTurn getShopClock() throws SQLException
	{
		String sql 		= " SELECT workdate, shiftid FROM ShopClock; ";
		Statement stmt 	= connection.createStatement();
		ResultSet rs 	= stmt.executeQuery( sql );
				
		if( rs.next() ) {
			int shiftid   = rs.getInt( "shiftid" );
			Date workdate = rs.getDate( "workdate" );
			return new WorkTurn( new Day(workdate), shiftid );
		} 

		return null;
	}
	
	/**
	 * ��ѯ����ԱĿǰ�Ѿ���¼����������.
	 * @param cashierid	����ԱID.
	 * @return			����Ա��ǰ���ڹ�����������ID.
	 * @throws SQLException
	 */
	public String getPos4Cashier( String cashierid ) throws SQLException
	{
		String sql = " SELECT pos_id FROM pos_lst WHERE current_op = ? " ;
		PreparedStatement pstmt;
		String posid = null;

		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, cashierid);
		ResultSet rs = pstmt.executeQuery();
		if( rs.next() ) posid = rs.getString( "pos_id" );

		posid = ( posid == null)? posid : posid.toUpperCase();

		return posid;
	}
	
	/**
	 * ��ѯָ��POS�������ڹ���������ԱID.
	 * @param posid ������ID.
	 * @return ����ָ��POS���Ϲ���������ԱID.
	 * @throws SQLException
	 */
	private String getCashier4Pos( String posid ) throws SQLException
	{
		posid = posid.toUpperCase();
		String sql = " SELECT current_op FROM pos_lst WHERE pos_id = ? " ;
		PreparedStatement pstmt;
		String operator = null;

		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, posid);
		ResultSet rs = pstmt.executeQuery();
		if( rs.next() ) operator = rs.getString( "current_op" );

		return posid;
	}
	
	/**
	 * �˷�����pos_lst ����ָ��POS���ĵ�ǰ����ԱID��Ϊָ����cashierid. ��������Ա��¼����.
	 * @param posid ������ID.
	 * @param cashierid	����ԱID.
	 * @throws SQLException ���ִ��SQL�������׳�����.
	 */
	private void setCurrentOperator( String posid, String cashierid ) throws SQLException
	{
		posid = posid.toUpperCase();
		String sql = " UPDATE pos_lst SET current_op = ? WHERE pos_id = ?; " ;
		PreparedStatement pstmt;

		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, cashierid);
		pstmt.setString(2, posid);
		pstmt.executeUpdate();
	}

	/**
	 * �˷�����pos_lst ��������ԱID��ΪNULL. ��������Ա�˳�����.
	 * @param posid ������ID.
	 * @throws SQLException ���ִ��SQL�������׳�����.
	 */
	private void resetCurrentOperator( String posid ) throws SQLException
	{
		posid = posid.toUpperCase();
		String sql = " UPDATE pos_lst SET current_op = NULL WHERE pos_id = ?; " ;
		PreparedStatement pstmt;
	
		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, posid);
		pstmt.executeUpdate();
	}

	private Connection connection;
}

