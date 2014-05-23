package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.royalstone.pos.card.MemberCard;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ����˴��룬������ѯ���˿�����Ϣ
 * @author liangxinbiao
 */
public class MemberCardQueryCommand implements ICommand {

	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {
		if (values.length == 2
			&& (values[1] instanceof String)) {
			Object[] results = new Object[1];
			results[0] = query((String) values[1]);
			return results;
		}

		return null;
	}

	/**
	 * ���ݿ��ź�����(�ӿ���������)��ѯ���˿�����Ϣ
	 * @param CardNo ���˿���
	 * @return ���˿���ѯֵ����
	 */
	private MemberCard query(String CardNo) {

		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		conn = DBConnection.getConnection("java:comp/env/dbcard");

		MemberCard cardquery = new MemberCard();
		String mode = null;
		if (CardNo == null
			|| CardNo.trim().equals("")) {
			cardquery.setExceptionInfo("���Ż�����Ϊ��");
			return cardquery;
		}
		try {
			conn.setAutoCommit(true);
			state = conn.createStatement();
			
				rs =
					state.executeQuery(
						"select CardNO,Mode,MemberID,memberlevel,Point,Level.Name,Level.Discount,Level.PromDiscount from guest,Level where guest.memberlevel=Level.LevelID and cardno = '"
							+ CardNo.trim()
							+ "' ");
				//rs.next();
				if (!rs.next()) {
					cardquery.setExceptionInfo("�˿��Ų�����,�밴���������");
					DBConnection.closeAll(rs, null, null);
					return cardquery;
				} else { //����
					mode = (rs.getString("Mode")).trim();
					if (!mode.equals("1")) {
					switch (mode.charAt(0)) {
						case '2' :
							cardquery.setExceptionInfo("δ���ʿ�");
							break;
						case 'r' :
							cardquery.setExceptionInfo("�ѻ��տ�");
							break;
						case 'm' :
							cardquery.setExceptionInfo("һ���ʧ��");
							break;
						case 'l' :
							cardquery.setExceptionInfo("���ع�ʧ��");
							break;
						case 'f' :
							cardquery.setExceptionInfo("����");
							break;
						case 'e' :
							cardquery.setExceptionInfo("�ѻ���");
							break;
						case 'q' :
							cardquery.setExceptionInfo("�˿�");
							break;
						default :
							cardquery.setExceptionInfo("��������");
							break;
							}
							DBConnection.closeAll(rs, state, conn);
							return cardquery;
						}

						cardquery.setCardNo(CardNo);
						cardquery.setMemberLevel(rs.getInt("memberlevel"));
                        cardquery.setLevelName(rs.getString("Name"));
                        cardquery.setDiscount(100-rs.getInt("Discount"));
                        cardquery.setPromDiscount(100-rs.getInt("PromDiscount"));
						cardquery.setTotalPoint(rs.getBigDecimal("Point"));
				
				}
			
		} catch (SQLException se) {
			se.printStackTrace();
			cardquery = new MemberCard();
			cardquery.setExceptionInfo("���ݿ��������,�밴���������");
			return cardquery;
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
		return cardquery;
	}


}
