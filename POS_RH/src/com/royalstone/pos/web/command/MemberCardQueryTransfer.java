package com.royalstone.pos.web.command;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;

/**
 * ����˴��룬������ѯ���˿�����Ϣ
 * @author liangxinbiao
 */
public class MemberCardQueryTransfer implements ICommand {

	public Object[] excute(Object[] values) {
		if (values.length == 3
			&& (values[1] instanceof String)
            && ((values[2] instanceof String) || (values[2]==null))) {
            String host = (String) values[2];
			Object[] results = new Object[1];
			results[0] = query((String) values[1],host);
			return results;
		}

		return null;
	}

	/**
	 * ���ݿ��ź�����(�ӿ���������)��ѯ���˿�����Ϣ
	 * @param CardNo ���˿���
	 * @return ���˿���ѯֵ����
	 */
	private MemberCard query(String CardNo,String host) {

         MemberCard cardquery = new MemberCard();


        URL servlet;
        HttpURLConnection conn;
        try {
		servlet =new URL("http://"+ host+ "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
			cardquery.setExceptionInfo("���ӿ�����������,�������������");
            return cardquery;
		}


		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.MemberCardQueryCommand";
			params[1] = CardNo;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				cardquery = (MemberCard) results[0];
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		    cardquery.setExceptionInfo("��ѯ������������,�������������");
            return cardquery;
		}
         return cardquery;
	}

//    private MemberCard queryLocal(String CarNo){
//        Connection conn = null;
//		Statement state = null;
//		ResultSet rs = null;
//		conn = DBConnection.getConnection("java:comp/env/dbcard");
//
//		MemberCard cardquery = new MemberCard();
//		String mode = null;
//		if (CarNo == null
//			|| CarNo.trim().equals("")) {
//			cardquery.setExceptionInfo("���Ż�����Ϊ��");
//			return cardquery;
//		}
//		try {
//			conn.setAutoCommit(true);
//			state = conn.createStatement();
//
//				rs =
//					state.executeQuery(
//						"select CardNO,Mode,MemberID,memberlevel,Point from guest where cardno = '"
//							+ CarNo.trim()
//							+ "'");
//				//rs.next();
//				if (!rs.next()) {
//					cardquery.setExceptionInfo("�˿��Ų�����,�밴���������");
//					DBConnection.closeAll(rs, null, null);
//					return cardquery;
//				} else { //����
//
//						mode = (rs.getString("Mode")).trim();
//						if (!mode.equals("1")) {
//							switch (mode.charAt(0)) {
//								case '2' :
//									cardquery.setExceptionInfo("δ���ʿ�,�밴���������");
//									break;
//								case 'm' :
//									cardquery.setExceptionInfo("һ���ʧ��,�밴���������");
//									break;
//								default :
//									cardquery.setExceptionInfo("��������,�밴���������");
//									break;
//							}
//							DBConnection.closeAll(rs, state, conn);
//							return cardquery;
//						}
//
//						cardquery.setCardNo(CarNo);
//						cardquery.setMemberLevel(rs.getInt("memberlevel"));
//						cardquery.setTotalPoint(rs.getBigDecimal("Point"));
//
//				}
//
//		} catch (SQLException se) {
//			se.printStackTrace();
//			cardquery = new MemberCard();
//			cardquery.setExceptionInfo("���ݿ��������,�밴���������");
//			return cardquery;
//		} finally {
//			DBConnection.closeAll(rs, state, conn);
//		}
//		return cardquery;
//    }
}
