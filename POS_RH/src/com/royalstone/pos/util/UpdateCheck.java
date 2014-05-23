package com.royalstone.pos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * �����Ҫ���õ���,
 * ��������������Ƿ����ļ����ºͽ��Լ��Ƿ���¼�¼����
 * @author liangxinbiao
 */
public class UpdateCheck {

	public static UpdateCheck instance;

	private UpdateCheck() {
	}

	/**
	 * @return ����UpdateCheck�ĵ�ʵ��
	 */
	public static UpdateCheck getInstance() {
		if (instance == null) {
			instance = new UpdateCheck();
		}
		return instance;
	}

	/**
	 * @param filename �ļ����¿����ļ����ļ���
	 * @param posid POS����
	 * @return �����Ƿ��и���
	 */
	public boolean hadUpdate(String filename, String posid) {
		try {
			Properties posList = new Properties();
			posList.load(new FileInputStream(filename));
			return posList.get(posid) == null ? false : true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * ���Ѹ��¹���POS���Ļ���д���ļ����¿����ļ���
	 * @param filename �ļ����¿����ļ����ļ���
	 * @param posid POS����
	 */
	public synchronized void addPOS(String filename, String posid) {
		try {
			Properties posList = new Properties();
			File file = new File(filename);
			if (!file.exists())
				file.createNewFile();
			posList.load(new FileInputStream(filename));
			if (posList.get(posid) == null) {
				posList.put(posid, "");
				posList.store(new FileOutputStream(filename), "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ������,������
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		UpdateCheck updateCheck = new UpdateCheck();
		System.out.println(
			updateCheck.hadUpdate("posupdate.properties", "P007"));
		updateCheck.addPOS("posupdate.properties", "P007");

	}
}
