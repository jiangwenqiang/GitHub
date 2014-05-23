package com.royalstone.pos.util;

import java.io.Serializable;

/**
 * ��װҪ���µ��ļ�����Ϣ
 * @author liangxinbiao
 */
public class UpdateFile implements Serializable{

	private String srcPath = "";
	private String destPath = "";

	public UpdateFile(String src, String dest) {
		this.srcPath = src;
		this.destPath = dest;
	}

	/**
	 * @return Ҫ�����ļ���POS���ϵ�·��
	 */
	public String getDestPath() {
		return destPath;
	}

	/**
	 * @return Ҫ�����ļ��ڷ������ϵ�·��
	 */
	public String getSrcPath() {
		return srcPath;
	}

}
