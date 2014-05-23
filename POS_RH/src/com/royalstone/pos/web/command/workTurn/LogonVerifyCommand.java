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

			System.err.println("查找收银机...");
			if (!workTurnMinister.isPosAvailable(posid, cashierid)) {
				String warning = "本店数据库中找不到收银机" + posid + "，请检查配置文件。";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

			System.err.println("查找收银员...");
			// 查找收银员.
			Operator op;
			if ((op = m.getOperator(cashierid)) == null) {
				String warning = "本店数据库中找不到收银员" + cashierid + "，请重新输入正确的收银员号。";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

			String curOp = workTurnMinister.getPower(posid);
			// 检查收银员是否可以在指定的收银机上登录.
			if (!workTurnMinister.checkPower(cashierid, curOp)) {
				String warning = "您无权打开POS机" + posid + "，请重新获得授权。";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

			System.err.println("检查密码...");
			// 检查密码.
			if (!op.checkPlainPin(plainpin)) {
				String warning = "密码不正确";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;

			}

			System.err.println("查看收银员是否已在其他收银机登录...");
			// 查看收银员是否已在其他收银机登录.
			String pos_other = workTurnMinister.getPos4Cashier(cashierid);
			if (pos_other != null && !pos_other.equals(posid)) {
				String warning = "请先从收银台 " + pos_other + " 退出。";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

			// 检查收银机IP地址是否与后台记录的一致.
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
					"您的IP与POS机"
						+ posid
						+ "的IP不符，"
						+ posid
						+ "的IP为："
						+ workTurnMinister.getPosip(posid)
						+ "。";
				errorMsg1 = warning;
				errorMsg2 = warning;
				return;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg1 = "数据库操作错误!";
			errorMsg2 = e.getMessage();
		}

	}
}
