package com.royalstone.pos.util;

import java.io.File;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * ��װ��һЩ�ļ�����������ʵ����
 * @author liangxinbiao
 */
public class FileUtil {

	/**
	 * �ļ�����
	 * @param fnew ���ļ���
	 * @param fcur ��ǰ�ļ���
	 */
	public static void rename(String fnew, String fcur) {
		File curFile = new File(fcur);
		curFile.renameTo(new File(fnew));
	}

	/**
	 * �ڵ�ǰ�ļ����Ļ����ϼ�������ʱ������׺
	 * @param fcur
	 */
	public static void bakup(String fcur) {
		File curFile = new File(fcur);
		curFile.renameTo(
			new File(fcur + "." + Formatter.getDateFile(new Date())));
	}

	public static boolean bakup(String fcur, String fnew) {
		File curFile = new File(fcur);
		return curFile.renameTo(new File(fnew));
	}

	public static void fileError(String fname) {
		String fbak = fname + "." + Formatter.getDateFile(new Date());

		if (bakup(fname, fbak)) {
			JOptionPane.showMessageDialog(
				null,
				"�ļ�" + fname + "��,�ѱ���Ϊ" + fbak + ".");
			System.out.println("�ļ�" + fname + "��,�ѱ���Ϊ" + fbak + ".");
		} else {
			JOptionPane.showMessageDialog(
				null,
				"�ļ�" + fname + "��,����Ϊ" + fbak + "ʧ��.");
			System.out.println("�ļ�" + fname + "��,����Ϊ" + fbak + "ʧ��.");
		}
	}
	
	public static boolean journalFileError(String fname) {

		String dirName="journal"+File.separator+Formatter.getDate(new Date())+"#bad";
		File dir = new File(dirName);
		if(!dir.exists()){
			dir.mkdir();						
		}
		
		String fbak = dirName+File.separator+fname + "." + Formatter.getDateFile(new Date());
		fname="journal"+File.separator+fname;

		if (bakup(fname, fbak)) {
			JOptionPane.showMessageDialog(
				null,
				"�ļ�" + fname + "��,�ѱ���Ϊ" + fbak + ".");
			System.out.println("�ļ�" + fname + "��,�ѱ���Ϊ" + fbak + ".");
			return true;
		} else {
			JOptionPane.showMessageDialog(
				null,
				"�ļ�" + fname + "��,����Ϊ" + fbak + "ʧ��.");
			System.out.println("�ļ�" + fname + "��,����Ϊ" + fbak + "ʧ��.");
			return false;
		}
	}

}
