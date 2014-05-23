package com.royalstone.pos.web.command.workTurn;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;

import com.royalstone.pos.common.Operator;
import com.royalstone.pos.db.OperatorMinister;
import com.royalstone.pos.db.WorkTurnMinister;
import com.royalstone.pos.web.command.ICommand;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author liangxinbiao
 */
public class LogonVerifyCommand implements ICommand {
	private String errorMsg1;
	private String errorMsg2;

	public Object[] excute(Object[] values) {
		if (values != null && values.length == 5) {

			Connection con = null;

			try {
				con = DBConnection.getConnection("java:comp/env/dbpos");

				Object[] result = new Object[2];

				String posid = (String) values[1];
				String cashierid = (String) values[2];
				String plainpin = (String) values[3];
				String localip = (String) values[4];
				verify(con, posid, cashierid, plainpin, localip);

				result[0] = errorMsg1;
				result[1] = errorMsg2;
				return result;

			} finally {
				DBConnection.closeAll(null, null, con);
			}

		}
		return null;
	}

	private void verify(
		Connection connection,
		String posid,
		String cashierid,
		String plainpin,
		String localip) {

		try {
			WorkTurnMinister workTurnMinister =
				new WorkTurnMinister(connection);

			OperatorMinister m = new OperatorMinister(connection);

			System.err.println("����������...");
			if (!workTurnMinister.isPosAvailable(posid, cashierid)) {
				String warning = "�������ݿ����Ҳ���������" + posid + "�����������ļ���";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

			System.err.println("��������Ա...");
			// ��������Ա.
			Operator op;
			if ((op = m.getOperator(cashierid)) == null) {
				String warning = "�������ݿ����Ҳ�������Ա" + cashierid + "��������������ȷ������Ա�š�";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

			String curOp = workTurnMinister.getPower(posid);
			// �������Ա�Ƿ������ָ�����������ϵ�¼.
			if (!workTurnMinister.checkPower(cashierid, curOp)) {
				String warning = "����Ȩ��POS��" + posid + "�������»����Ȩ��";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

			System.err.println("�������...");
			// �������.
			if (!op.checkPlainPin(plainpin)) {
				String warning = "���벻��ȷ";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;

			}

			System.err.println("�鿴����Ա�Ƿ�����������������¼...");
			// �鿴����Ա�Ƿ�����������������¼.
			String pos_other = workTurnMinister.getPos4Cashier(cashierid);
			if (pos_other != null && !pos_other.equals(posid)) {
				String warning = "���ȴ�����̨ " + pos_other + " �˳���";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

			// ���������IP��ַ�Ƿ����̨��¼��һ��.
			String serverip = null;
			try {
				InetAddress iadd = InetAddress.getLocalHost();
				serverip = iadd.getHostAddress();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (!(serverip != null && serverip.trim().equals(localip.trim()))
				&& localip.trim().compareTo(
					workTurnMinister.getPosip(posid).trim())
					!= 0) {
				String warning =
					"����IP��POS��"
						+ posid
						+ "��IP������"
						+ posid
						+ "��IPΪ��"
						+ workTurnMinister.getPosip(posid)
						+ "��";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "���ݿ��������!";
			errorMsg2 = e.getMessage();
		}

	}
}
