package com.royalstone.pos.util;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * ��Jar�ļ���ʽ����ȡ�汾��
 * @deprecated
 * @author liangxinbiao
 */
public class JarVersion {

	/**
	 * ��Jar�ļ���ʽ����ȡ�汾��
	 * @param jarName Jar�ļ����ļ���
	 * @param attrName �汾�õ�����
	 * @return �汾��
	 */
	public static String getVersion(String jarName, String attrName) {
		try {
			JarFile jarFile = new JarFile(jarName);
			Manifest mf = jarFile.getManifest();
			Attributes attrs = mf.getMainAttributes();
			return attrs.getValue(attrName);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return "";
	}

	/**
	 * ������,������
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("POS-Version="+JarVersion.getVersion("posv41.jar","POS-Version"));
		System.out.println("Loader-Version="+JarVersion.getVersion("loader.jar","Loader-Version"));
		System.out.println("Driver-Version="+JarVersion.getVersion("drv/driver.zip","Driver-Version"));
	}
}
