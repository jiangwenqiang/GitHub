package com.royalstone.pos.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ʵ����,��Zip��ʽ���ļ���ѹ
 * @author 
 */
public class UnZipFile {

	/**
	 * ��ѹZip��ʽ�ļ�
	 * @param srcFileName Դ�ļ�����ϸ·��
	 * @param destPath Ŀ��·��
	 */
	public void unZip(String srcFileName, String destPath) throws IOException{
		  byte buf[]=new byte[4096];
			try {
				ZipFile zipFile = new ZipFile(srcFileName);
				//������ַ�enume 
				Enumeration enume = zipFile.entries();
				while (enume.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) enume.nextElement();
					if (!entry.isDirectory()) {
						FileOutputStream fos =
							new FileOutputStream(destPath + "/" + entry.getName());
						InputStream is = zipFile.getInputStream(entry);

	                    int len=is.read(buf);
						while (len!=-1) {
							fos.write(buf,0,len);
							len=is.read(buf);
						}
						is.close();
						fos.close();
					} else {
						File file = new File(destPath + "/" + entry.getName());
						file.mkdir();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new IOException(ex.getMessage());
			}
		}

		/**
		 * ������,������
		 * @param args
		 */
		public static void main(String[] args) {
			UnZipFile unZipFile = new UnZipFile();
			try {
				unZipFile.unZip("download/posv41.jar", "download");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
