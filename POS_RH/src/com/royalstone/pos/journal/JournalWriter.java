/*
 * Created on 2004-6-4
 *
 */
package com.royalstone.pos.journal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Date;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;
import com.royalstone.pos.util.FileUtil;
import com.royalstone.pos.util.Formatter;
import com.royalstone.pos.util.Response;
import com.royalstone.pos.util.WorkTurn;

/**
 * JournalWriter �������̨������ˮ. �乤����������:
 * �򿪱�����ˮ���ݵ�XML�ĵ�,�������е���ˮ�ڵ㹹�� PosJournal ����.
 * ���Ӻ�̨��DispatchServlet, ָ����ʵ���� com.royalstone.pos.web.command.PosJournalCommand.
 * ����PosJournal ���󵽺�̨,��PosJournalCommand ����д�����ݿ���.
 * @author Mengluoyi
 *
 */

public class JournalWriter {

	public static void main(String[] args) {
		JournalWriter w = new JournalWriter("localhost", 9090);
		w.writeJournal("001.xml");
	}

	/**
	 * Constructs a JournalWriter.
	 * @param server	��̨������IP��ַ.
	 * @param port		����˿ں�.
	 */
	public JournalWriter(String server, int port) {
		try {
			servlet =
				new URL(
					"http://" + server + ":" + port + "/pos41/DispatchServlet");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��������ļ�file �е���ˮд���̨������. ���д��̨�ɹ�,����ˮ�ļ��ƶ����԰����������Ŀ¼��.
	 * ÿ����һ�ʽ��׵���ˮ,������JournalLogList��������¼.
	 * @param file	���XML��ˮ���ļ�.
	 */
	public boolean writeJournal(final String file) {
		JournalLog journalLog = JournalLogList.getInstance().find(file);

		if (journalLog == null) {
			System.out.println(file + " not in JournalLog!");
			journalLog = new JournalLog();
			journalLog.setJournalName(file);
			journalLog.setStatus(JournalLog.CREATE);
			JournalLogList.getInstance().addLog(journalLog);
			JournalLogList.dump();
		}

		if (journalLog.getStatus() == JournalLog.UPLOADED) {
			System.out.println(file + " in JournalLog is Uploaded!");
			try {
				backupJournalFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		journalLog.setStatus(JournalLog.BEGIN_UPLOAD);
		JournalLogList.dump();

		System.out.println("Begin writeJournal(" + file + ")... ");
		Element root = getRootElement(file);

		if (root == null) {
			System.out.println("root is null!");
			return true;
		}

		PosJournal journal = new PosJournal(root);

		Response result = null;
		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.PosJournalCommand";
			params[1] = journal;

			MarshalledValue mvI = new MarshalledValue(params);
			System.out.println("Invoke! Begin upload journal... ");

			journalLog.setStatus(JournalLog.UPLOADING);
			JournalLogList.dump();
			System.out.println("dump journal... ");

			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			System.out.println("if mvO is null?" + (mvO == null));
			if (mvO != null) {
				results = mvO.getValues();
			}

			System.out.println("if results is null?" + (results == null));
			if (results != null)
				System.out.println("if results.length=" + results.length);

			if (results != null && results.length > 0) {
				result = (Response) results[0];
				System.out.println(result);

				System.out.println("if result is null?" + (result == null));
				if (result != null)
					System.out.println("if result.succeed=" + result.succeed());

				if (result != null && result.succeed()) {

					try {
						backupJournalFile(file);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					journalLog.setStatus(JournalLog.UPLOADED);
					journalLog.setUploadTime(Formatter.getDateFile(new Date()));
					JournalLogList.dump();

					return true;
				}
			}
			System.out.println("Finish upload journal... ");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		journalLog.setStatus(JournalLog.UPLOAD_FAIL);
		JournalLogList.dump();

		System.out.println("End writeJournal... ");

		return false;
	}

	/**
	 * ������ˮ�ļ�.
	 * @param file		��ˮ�ļ���.
	 * @throws Exception	
	 */
	private void backupJournalFile(String file) throws Exception {

		PosContext context = PosContext.getInstance();
		WorkTurn workTurn = context.getWorkTurn();

		String dirName;
		if (workTurn == null) {
			dirName =
				"journal"
					+ File.separator
					+ Formatter.getDate(new Date());
				//TODO ���ݸ��� by fire  2005_5_11  "#unknown";
					
			System.out.println("workTurn is null");
		} else {
			dirName = "journal" + File.separator + workTurn.toString();
		}

		File dir = new File(dirName);

		if (!dir.exists()) {
			System.out.println(dir.getName() + " not exists");
			dir.mkdir();
		}

		String srcfile = "journal" + File.separator + file;
		String destfile = dirName + File.separator + file;

		System.out.println("srcfile=" + srcfile);
		System.out.println("destfile=" + destfile);

		FileChannel in = new FileInputStream(srcfile).getChannel();
		FileChannel out = new FileOutputStream(destfile).getChannel();
		long transbytes = in.transferTo(0, in.size(), out);

		System.out.println("transbytes=" + transbytes);

		in.close();
		out.close();

		System.out.println("Begin delete file \"" + srcfile + "\"");
		boolean delresult = (new File(srcfile)).delete();
		System.out.println(
			"Delete file \"" + srcfile + "\" return " + delresult);

	}

	/**
	 * ����XML��ˮ�ļ�,ȡ����ڵ�.
	 * @param file	XML��ˮ�ļ���.
	 * @return		���ڵ�.
	 */
	public static Element getRootElement(String file) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream("journal" + File.separator + file);
			Document doc = (new SAXBuilder()).build(fin);
			Element root = doc.getRootElement();
			try {
				fin.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return root;
		} catch (Exception e) {
			e.printStackTrace();
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (!FileUtil.journalFileError(file)) {
				System.exit(1);
			}
		}
		return null;
	}

	/**
	 * <code>servlet</code>	URL for servlet.
	 */
	private URL servlet;
	/**
	 * <code>conn</code>	http connection to servlet.
	 */
	private HttpURLConnection conn;

}
