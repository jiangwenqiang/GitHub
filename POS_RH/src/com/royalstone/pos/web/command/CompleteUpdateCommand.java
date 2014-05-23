package com.royalstone.pos.web.command;

import com.royalstone.pos.util.UpdateCheck;

/**
 * ��ĳһPOS����ɳ������غ������,�Է�ֹ���ڶ�������ʱ�����س���
 * @author liangxinbiao
 */
public class CompleteUpdateCommand implements ICommand {

	private String posid;

	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {
		if (values != null && values.length == 2) {
			posid = (String) values[1];
			updateCheck();
			Object[] result = new Object[1];
			result[0] = "";
			return result;
		}
		return null;
	}

	/**
	 * ��POS����posupdate.properties�����һ��
	 */
	private void updateCheck() {
		UpdateCheck updateCheck = UpdateCheck.getInstance();
		updateCheck.addPOS("../download/posupdate.properties", posid);
	}

}
