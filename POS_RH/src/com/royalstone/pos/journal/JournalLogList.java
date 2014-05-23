package com.royalstone.pos.journal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.royalstone.pos.util.FileUtil;
import com.royalstone.pos.util.Formatter;

/**
 * ��ˮ��־���б�,ÿ����һ����ˮ���Ὣ����һ����ˮ��־,
 * Ȼ������������ˮ��־�б���,��д���ļ���.
 * @author liangxinbiao
 */

public class JournalLogList implements Serializable {

	private static JournalLogList instance;

	private static String logfile =
		"journalLog" + File.separator + "journalLog.dat";

	private Vector list;
	private HashMap map;

	private JournalLogList() {
		list = new Vector();
		map = new HashMap();
	}

	/**
	 * @return ȡ����ˮ��־�б�ĵ�ʵ��
	 */
	public synchronized static JournalLogList getInstance() {
		if (instance == null) {
			load();
		}
		return instance;
	}

	/**
	 * ������ˮ��������ˮ��־�б����ҳ���ˮ��־,
	 * �Ҳ����Ļ����� null
	 * @param journalName ��ˮ����
	 * @return ��ˮ��־
	 */
	public synchronized JournalLog find(String journalName) {
		return (JournalLog) map.get(journalName);
	}

	/**
	 * ����ˮ��־�ӵ���ˮ��־�б���
	 * @param log ��ˮ��־
	 */
	public synchronized void addLog(JournalLog log) {
		if (log != null) {
			list.add(log);
			map.put(log.getJournalName(), log);
		}
	}

	/**
	 * ����ˮ��־�б�д���ļ��� 
	 */
	public synchronized static void dump() {
		if (instance != null) {
			try {
				ObjectOutputStream out =
					new ObjectOutputStream(new FileOutputStream(logfile));
				out.writeObject(instance);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����ˮ��־�б���ļ����ص��ڴ��� 
	 */
	public synchronized static void load() {
		FileInputStream fin = null;
		ObjectInputStream in = null;
		try {
			fin = new FileInputStream(logfile);
			in = new ObjectInputStream(fin);
			instance = (JournalLogList) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			instance = new JournalLogList();
			JournalLogList.dump();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (fin != null) {
					fin.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			FileUtil.fileError(logfile);
			instance = new JournalLogList();
		}
	}

	/**
	 * ����ˮ��־�б��ļ���������,��ԭ���ļ������������ʱ�� 
	 */
	public synchronized static void rename() {
		if (instance != null) {
			for (int i = 0; i < instance.list.size(); i++) {
				JournalLog journalLog = (JournalLog) instance.list.elementAt(i);
				if (journalLog.getStatus() != JournalLog.UPLOADED) {
					return;
				}
			}
			File file = new File(logfile);
			file.renameTo(
				new File(
					"journalLog"
						+ File.separator
						+ "journalLog"
						+ Formatter.getDateFile(new Date())
						+ ".dat"));
		}
	}

	/**
	 * @return ��ˮ��־�б����û�ϴ��ɹ�����ˮ����
	 */
	public synchronized static int getUnuploadCount() {
		int count = 0;
		if (instance != null) {
			for (int i = 0; i < instance.list.size(); i++) {
				JournalLog journalLog = (JournalLog) instance.list.elementAt(i);
				if (journalLog.getStatus() != JournalLog.UPLOADED) {
					count++;
				}
			}
		}
		return count;
	}

	public Vector getLogList() {
		return list;
	}
}
