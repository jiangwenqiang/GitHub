package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.royalstone.pos.card.LoanCardDisc;
import com.royalstone.pos.card.LoanCardQueryVO;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ����˴��룬������ѯ���˿�����Ϣ
 * @author liangxinbiao
 */
public class LoanCardQueryCommand implements ICommand {

	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {
		if (values.length == 3
			&& (values[1] instanceof String)
			&& (values[2] instanceof String)) {
			Object[] results = new Object[1];
			results[0] = query((String) values[1], (String) values[2]);
			return results;
		}

		return null;
	}

	/**
	 * ���ݿ��ź�����(�ӿ���������)��ѯ���˿�����Ϣ
	 * @param CardNo ���˿���
	 * @param secrety ����
	 * @return ���˿���ѯֵ����
	 */
	private LoanCardQueryVO query(String CardNo, String secrety) {

		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		conn = DBConnection.getConnection("java:comp/env/dbcard");

		LoanCardQueryVO cardquery = new LoanCardQueryVO();
		String mode = null;
		if (CardNo == null
			|| secrety == null
			|| CardNo.trim().equals("")
			|| secrety.trim().equals("")) {
			cardquery.setExceptioninfo("���Ż�����Ϊ��");
			return cardquery;
		}
		try {
			conn.setAutoCommit(true);
			state = conn.createStatement();
			rs =
				state.executeQuery(
					"select count(*) as counts from creditsubcard where creditsubcardno = '"
						+ CardNo.trim()
						+ "'");
			rs.next();
			if (rs.getInt("counts") == 0) {
				rs =
					state.executeQuery(
						"select count(*) as counts from guest where cardno = '"
							+ CardNo.trim()
							+ "'");
				rs.next();
				if (rs.getInt("counts") == 0) {
					cardquery.setExceptioninfo("�޴˿���");
					DBConnection.closeAll(rs, null, null);
					return cardquery;
				} else { //����
					rs =
						state.executeQuery(
							"select cardno,detail,credit,mode from guest "
								+ "where cardno = '"
								+ CardNo.trim()
								+ "' and secrety = '"
								+ secrety.trim()
								+ "'");

					if (rs.next()) {
						mode = (rs.getString("mode")).trim();
						if (!mode.equals("1")) {
							switch (mode.charAt(0)) {
								case '2' :
									cardquery.setExceptioninfo("δ���ʿ�");
									break;
								case 'r' :
									cardquery.setExceptioninfo("�ѻ��տ�");
									break;
								case 'm' :
									cardquery.setExceptioninfo("һ���ʧ��");
									break;
								case 'l' :
									cardquery.setExceptioninfo("���ع�ʧ��");
									break;
								case 'f' :
									cardquery.setExceptioninfo("����");
									break;
								case 'e' :
									cardquery.setExceptioninfo("�ѻ���");
									break;
								case 'q' :
									cardquery.setExceptioninfo("�˿�");
									break;
								default :
									cardquery.setExceptioninfo("��������");
									break;
							}
							DBConnection.closeAll(rs, state, conn);
							return cardquery;
						}

						cardquery.setCardNo(CardNo);
						cardquery.setCardType("����");
						cardquery.setSubcardNo("");
						cardquery.setDetail(
							Integer.toString(
								((int) (rs.getDouble("detail")
									+ rs.getDouble("credit"))
									* 100)));
						cardquery.setCredit(
							Integer.toString(
								(int) (rs.getDouble("credit") * 100)));

						String custName = "";
						ResultSet rs2 =
							state.executeQuery(
								"select Name from creditclient where cardNo='"
									+ mytrim(rs.getString("cardNo"))
									+ "'");
						if (rs2.next()) {
							custName = mytrim(rs2.getString("Name"));
						}
						rs2.close();
						cardquery.setCustName(custName);

						ResultSet rs1 =
							state.executeQuery(
								"select shopId from creditclient_shop where isAllowedUse=1 and cardno='"
									+ CardNo
									+ "'");
						ArrayList shopList = new ArrayList();
						while (rs1.next()) {
							shopList.add(rs1.getString("shopId"));
						}
						cardquery.setShopIDs(shopList);
						rs1.close();
						DBConnection.closeAll(rs, state, conn);
					} else {
						cardquery.setExceptioninfo("��������");
						DBConnection.closeAll(rs, state, conn);
						return cardquery;
					}
				}
			} else { //�ӿ�
				rs =
					state.executeQuery(
						"select cardno,creditsubcardno,balance,credit,status as mode,MaxOilQtyPerTrans,OilTypeID,carID from creditsubcard "
							+ "where (creditsubcardno = '"
							+ CardNo.trim()
							+ "' and cardverifyCode = '"
							+ secrety.trim()
							+ "' and IsNeededVerify=1) or (creditsubcardno ='"
							+ CardNo.trim()
							+ "' and IsNeededVerify=0) ");

				if (rs.next()) {
					mode = (rs.getString("mode")).trim();
					if (!mode.equals("1")) {
						switch (mode.charAt(0)) {
							case '2' :
								cardquery.setExceptioninfo("δ���ʿ�");
								break;
							case 'r' :
								cardquery.setExceptioninfo("�ѻ��տ�");
								break;
							case 'm' :
								cardquery.setExceptioninfo("һ���ʧ��");
								break;
							case 'l' :
								cardquery.setExceptioninfo("���ع�ʧ��");
								break;
							case 'f' :
								cardquery.setExceptioninfo("����");
								break;
							case 'e' :
								cardquery.setExceptioninfo("�ѻ���");
								break;
							case 'q' :
								cardquery.setExceptioninfo("�˿�");
								break;
							default :
								cardquery.setExceptioninfo("��������");
								break;
						}
						DBConnection.closeAll(rs, state, conn);
						return cardquery;
					}

					String mainCardNo = mytrim(rs.getString("cardno"));

					double subBalance =
						rs.getDouble("balance") + rs.getDouble("credit");
					cardquery.setMaxOilQtyPerTrans(
						rs.getString("MaxOilQtyPerTrans"));
					cardquery.setOilGoodsID(rs.getString("OilTypeID"));
					cardquery.setCarID(mytrim(rs.getString("carID")));
					
					
					ResultSet rs6 =
						state.executeQuery(
							"select mode from guest "
								+ "where cardno = '"
								+ mainCardNo.trim()
								+ "' ");

					if (rs6.next()) {
						mode = (rs6.getString("mode")).trim();
						if (!mode.equals("1")) {
							switch (mode.charAt(0)) {
								case '2' :
									cardquery.setExceptioninfo("δ���ʿ�[���˻�]");
									break;
								case 'r' :
									cardquery.setExceptioninfo("�ѻ��տ�[���˻�]");
									break;
								case 'm' :
									cardquery.setExceptioninfo("һ���ʧ��[���˻�]");
									break;
								case 'l' :
									cardquery.setExceptioninfo("���ع�ʧ��[���˻�]");
									break;
								case 'f' :
									cardquery.setExceptioninfo("����[���˻�]");
									break;
								case 'e' :
									cardquery.setExceptioninfo("�ѻ���[���˻�]");
									break;
								case 'q' :
									cardquery.setExceptioninfo("�˿�[���˻�]");
									break;
								default :
									cardquery.setExceptioninfo("��������[���˻�]");
									break;
							}
							DBConnection.closeAll(rs6, state, conn);
							return cardquery;
						}
					}

					ResultSet rs1 =
						state.executeQuery(
							"select shopId from creditclient_shop where isAllowedUse=1 and cardno='"
								+ mainCardNo
								+ "'");
					System.out.println(
						"select shopId from creditclient_shop where isAllowedUse=1 and cardno='"
							+ mainCardNo
							+ "'");
					ArrayList shopList = new ArrayList();
					while (rs1.next()) {
						shopList.add(rs1.getString("shopId"));
					}

					ResultSet rs2 =
						state.executeQuery(
							"select deptID from creditclient_c where isAllowedUse=1 and creditclientsubcardno='"
								+ CardNo
								+ "'");
					ArrayList deptList = new ArrayList();
					while (rs2.next()) {
						deptList.add(rs2.getString("deptID"));
					}

					ResultSet rs3 =
						state.executeQuery(
							"select detail,credit from guest where cardno='"
								+ mainCardNo
								+ "'");

					double mainBalance = 0;
					if (rs3.next()) {
						mainBalance =
							rs3.getDouble("detail") + rs3.getDouble("credit");

						if (subBalance > mainBalance)
							subBalance = mainBalance;

					}

					String custName = "";
					ResultSet rs4 =
						state.executeQuery(
							"select Name from creditclient where cardNo='"
								+ mainCardNo
								+ "'");
					if (rs4.next()) {
						custName = mytrim(rs4.getString("Name"));
					}

					ResultSet rs5 =
						state.executeQuery(
							"select itemType,itemID,discType,discCount from creditClientDisc where cardNo='"
								+ mainCardNo
								+ "' order by itemType desc ,itemID desc ");

					ArrayList discList = new ArrayList();
					while (rs5.next()) {
						LoanCardDisc disc = new LoanCardDisc();
						disc.setItemType(rs5.getInt("itemType"));
						disc.setItemID(rs5.getInt("itemID"));
						disc.setDiscType(rs5.getInt("discType"));
						disc.setDiscCount(rs5.getBigDecimal("discCount"));

						discList.add(disc);
					}

					rs1.close();
					rs2.close();
					rs3.close();
					rs4.close();

					cardquery.setCardNo(mainCardNo);
					cardquery.setDeptIDs(deptList);
					cardquery.setDetail(
						Integer.toString((int) Math.rint(subBalance * 100)));
					cardquery.setShopIDs(shopList);
					cardquery.setSubcardNo(CardNo);
					cardquery.setCustName(custName);
					cardquery.setCardType("�ӿ�");
					cardquery.setMainBalance(
						Integer.toString((int) Math.rint(mainBalance * 100)));
					cardquery.setDiscs(discList);

					DBConnection.closeAll(rs, state, conn);
					return cardquery;
				} else {
					cardquery.setExceptioninfo("��������");
					DBConnection.closeAll(rs, state, conn);
					return cardquery;
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			cardquery = new LoanCardQueryVO();
			cardquery.setExceptioninfo("���ݿ��������");
			return cardquery;
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
		return cardquery;
	}

	private String mytrim(String str) {
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}
	}

}
