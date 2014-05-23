package com.royalstone.pos.data;

import java.io.Serializable;
import java.util.Date;

import com.royalstone.pos.util.Formatter;

/**
 * @author liangxinbiao
 *
 */
public class PosTurn implements Serializable {

	private String posid;
	private Date workdate;
	private int shiftID;
	private Date startTime;
	private Date endTime;
	private int stat;
	private boolean isStartOffLine;
	private boolean isEndOffLine;

	/**
	 * @return ���ʱ��
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @return �Ƿ����ѻ�״̬����ɰ��ģ�0��ʾ������1��ʾ�ѻ���
	 */
	public boolean isEndOffLine() {
		return isEndOffLine;
	}

	/**
	 * @return �Ƿ����ѻ�״̬�¿���ģ�0��ʾ������1��ʾ�ѻ���
	 */
	public boolean isStartOffLine() {
		return isStartOffLine;
	}

	/**
	 * @return POS����
	 */
	public String getPosid() {
		return posid;
	}

	/**
	 * @return ��κ�
	 */
	public int getShiftID() {
		return shiftID;
	}

	/**
	 * @return ����ʱ��
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @return ���״̬��0��ʾ����λ�û����ᣬ1��ʾ������Ѿ���ɣ�
	 */
	public int getStat() {
		return stat;
	}

	/**
	 * @return ��������
	 */
	public Date getWorkdate() {
		return workdate;
	}

	/**
	 * @param string ���ʱ��
	 */
	public void setEndTime(Date value) {
		endTime = value;
	}

	/**
	 * @param string �Ƿ����ѻ�״̬����ɰ��ģ�0��ʾ������1��ʾ�ѻ���
	 */
	public void setEndOffLine(boolean value) {
		isEndOffLine = value;
	}

	/**
	 * @param string �Ƿ����ѻ�״̬�¿���ģ�0��ʾ������1��ʾ�ѻ���
	 */
	public void setStartOffLine(boolean value) {
		isStartOffLine = value;
	}

	/**
	 * @param string POS����
	 */
	public void setPosid(String string) {
		posid = string;
	}

	/**
	 * @param string ��κ�
	 */
	public void setShiftID(int value) {
		shiftID = value;
	}

	/**
	 * @param string ����ʱ��
	 */
	public void setStartTime(Date value) {
		startTime = value;
	}

	/**
	 * @param string ���״̬��0��ʾ����λ�û����ᣬ1��ʾ������Ѿ���ɣ�
	 */
	public void setStat(int value) {
		stat = value;
	}

	/**
	 * @param string ��������
	 */
	public void setWorkdate(Date value) {
		workdate = value;
	}

	public String toString() {
		return "posid="
			+ posid
			+ ",workdate="
			+ Formatter.getDate(workdate)
			+ ",shiftID="
			+ shiftID
			+ ",startTime="
			+ Formatter.getDateFile(startTime)
			+ ",endTime="
			+ Formatter.getDateFile(endTime)
			+ ",stat="
			+ stat
			+ ",isStartOffLine="
			+ isStartOffLine
			+ ",isEndOffLine="
			+ isEndOffLine;
	}

}
