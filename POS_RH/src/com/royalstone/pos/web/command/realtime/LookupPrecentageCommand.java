package com.royalstone.pos.web.command.realtime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ����˴��룬�����ѯ��Ʒ��Ϣ
 * @author liangxinbiao
 */
public class LookupPrecentageCommand implements ICommand {

	private String errorMsg1;
	private String errorMsg2;

	/**
	 * @see ICommand#excute(Object[])
	 */
	public Object[] excute(Object[] values) {

		if (values != null
			&& values.length == 4
			&& (values[1] instanceof String)&& (values[2] instanceof String)) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[3];
				result[0] = lookup(con, (String) values[1],(String) values[2],(String) values[3]);
				result[1] = errorMsg1;
				result[2] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

/**
 *
 * @param connection
 * @param cardLevelID
 * @param deptID
 * @param flag 
 * @return
 */
	public String lookup(Connection connection, String cardLevelID,String deptID,String flag) {

		String result ="0";

		try {
			result = lookupWithException(connection, cardLevelID,deptID,flag);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "���ݿ��ѯ����!";
			errorMsg2 = e.getMessage();
		}

		return result;
	}
	// zhouzhou add һ��FLAG 0 ȡ�������� 1 ȡ��������
	public String lookupWithException(Connection connection,String cardLevelID,String deptID,String flag)
		throws SQLException {
			
		String result = null;
		PreparedStatement pstmt =  null;
		
		/**
		 * ��һ��Ʒ��ȡ���ֱ���
		 * */
		pstmt = 
			connection.prepareStatement("declare @Result int,@Rate char(12) exec @Result=Getaccurate ?, ?, @Rate out select @Rate as result ; ");
		
		 int level=-1;
	        try {
	            level=Integer.parseInt(cardLevelID);
	        } catch (NumberFormatException e) {}
//	        int dept=-1;
	         try {
//	            dept=Integer.parseInt(deptID);
	        } catch (NumberFormatException e) {
	        	}    
	        pstmt.setString(1,deptID);
	        pstmt.setInt(2,level);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("Result");
			}
			rs.close();
	    	return result;


//		if (flag.equals("0")) 
//		 pstmt =
//			connection.prepareStatement(
//				"SELECT accurate FROM accurate where cardlevelid=? and deptid=? ; ");
//		else 
//		 pstmt =
//			connection.prepareStatement(
//					"SELECT Promaccurate FROM accurate where cardlevelid=? and deptid=? ; ");
		
//        int level=-1;
 //       try {
  //          level=Integer.parseInt(cardLevelID);
   //     } catch (NumberFormatException e) {}
//        int dept=-1;
//         try {
//            dept=Integer.parseInt(deptID);
//        } catch (NumberFormatException e) {}
//        pstmt.setInt(1,level);
//        pstmt.setInt(2,dept);

//		ResultSet rs = pstmt.executeQuery();
//		if (rs.next()) {
//			if (flag.equals("0")) 
//			result = rs.getString("accurate");
//			else 
//			result = rs.getString("Promaccurate");	
//		}
//		rs.close();
 //   	return result;
	}

}
