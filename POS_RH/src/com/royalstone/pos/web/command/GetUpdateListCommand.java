package com.royalstone.pos.web.command;

import java.io.FileNotFoundException;

import org.jdom.JDOMException;

import com.royalstone.pos.util.UpdateCheck;
import com.royalstone.pos.util.UpdateList;

/**
 * ����˴��룬ȡ�ó�����µ�ģ���б�
 * @author liangxinbiao
 */
public class GetUpdateListCommand implements ICommand {

	private String posid;

	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {

		if (values != null && values.length == 2) {
			posid = (String) values[1];
			Object[] result = new Object[1];
			result[0] = getUpdateList();
			return result;
		}
		return null;
	}

	/**
	 * ���Ҫ���µ��ļ���Ϣ���б�
	 * ����posupdate.properties���¼���Ѿ����ع���POS����POS����
	 * posupdate.xml���¼��Ҫ���µ��ļ�����Ϣ
	 * @return Ҫ���µ��ļ���Ϣ���б�
	 */
	private UpdateList getUpdateList() {
		try {

			UpdateCheck updateCheck = UpdateCheck.getInstance();
			if (posid != null
				&& !updateCheck.hadUpdate("../download/posupdate.properties", posid)) {
				UpdateList updateList = new UpdateList("../download/posupdate.xml");
				return updateList;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return null;
	}

}
