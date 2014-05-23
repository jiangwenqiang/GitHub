package com.royalstone.pos.journal;

import java.io.Serializable;

/**
 * ��ˮ��־,�������¼��ˮ��״̬,���½�,�ϴ���.
 * @author liangxinbiao
 */
public class JournalLog implements Serializable{
	
	public final static int CREATE=0;
	public final static int BEGIN_UPLOAD=1;
	public final static int UPLOADING=2;
	public final static int UPLOADED=3;
	public final static int UPLOAD_FAIL=4;
	
	private String journalName;
	private String createTime;
	private String uploadTime;
	private int status;
	

	/**
	 * @return ��ˮ������ʱ��
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @return ��ˮ������
	 */
	public String getJournalName() {
		return journalName;
	}

	/**
	 * @return ��ˮ��״̬
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return ��ˮ���ϴ�ʱ��
	 */
	public String getUploadTime() {
		return uploadTime;
	}

	/**
	 * @param string ��ˮ������ʱ��
	 */
	public void setCreateTime(String string) {
		createTime = string;
	}

	/**
	 * @param string ��ˮ������
	 */
	public void setJournalName(String string) {
		journalName = string;
	}

	/**
	 * @param i ��ˮ��״̬
	 */
	public void setStatus(int i) {
		status = i;
	}

	/**
	 * @param string ��ˮ���ϴ�ʱ��
	 */
	public void setUploadTime(String string) {
		uploadTime = string;
	}

}
