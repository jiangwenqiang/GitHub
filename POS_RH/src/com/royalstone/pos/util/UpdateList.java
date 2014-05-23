package com.royalstone.pos.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Ҫ���µ��ļ���Ϣ���б�
 * @author liangxinbiao
 */
public class UpdateList implements Serializable {

	private ArrayList files = new ArrayList();

	/**
	 * @param filename �����б��ļ�����ϸ·��
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 */
	public UpdateList(String filename)
		throws FileNotFoundException, JDOMException {

		Document doc = (new SAXBuilder()).build(new FileInputStream(filename));
		Element root = doc.getRootElement();

		List fileList = root.getChildren("File");

		for (int i = 0; i < fileList.size(); i++) {
			Element file = (Element) fileList.get(i);
			Element srcPath = file.getChild("SrcPath");
			Element destPath = file.getChild("DestPath");
			UpdateFile updateFile =
				new UpdateFile(srcPath.getTextTrim(), destPath.getTextTrim());
			files.add(updateFile);
		}

	}

	/**
	 * @return Ҫ�����ļ�������
	 */
	public int size() {
		return files.size();
	}

	/**
	 * ȡ�ø����ļ���Ϣ
	 * @param index ����
	 * @return �����ļ���Ϣ
	 */
	public UpdateFile get(int index) {
		return (UpdateFile) files.get(index);
	}

	/**
	 * ������,������
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		UpdateList updateList = new UpdateList("posupdate.xml");
		System.out.println("size=" + updateList.size());
		for (int i = 0; i < updateList.size(); i++) {
			UpdateFile updateFile = updateList.get(i);
			System.out.println("src=" + updateFile.getSrcPath());
			System.out.println("dest=" + updateFile.getDestPath());
		}
	}
}
