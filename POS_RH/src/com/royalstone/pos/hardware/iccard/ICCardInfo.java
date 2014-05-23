package com.royalstone.pos.hardware.iccard;

import java.io.IOException;
import java.text.DecimalFormat;

import com.royalstone.pos.hardware.iccard.util.FromSigned;

/**
 * ��װ��IC���Ķ�д,�ø��ײ����RC500��ִ��������IC����д
 * @author marco
 */
public class ICCardInfo {
	private static final DecimalFormat F00000 = new DecimalFormat("00000");
	private int yyyyMMdd;
	private int xxxxx;
	private int maxCredit;
	private int credit;
	private byte[] s1b1;
	
	
	/**
	 * ��RC500������IC������Ϣ
	 * @param rc500
	 * @return
	 * @throws IOException
	 */
	public static ICCardInfo load(RC500 rc500) throws IOException {
		try{
			prepare(rc500);	
		}catch(ConfigRCException e){
			throw e;
		}
		
		final ICCardInfo result = new ICCardInfo();
		final byte[] s1b0 = RC500.extractData(rc500.readBlock(4));
		result.yyyyMMdd = FromSigned.toInt(s1b0[7], s1b0[8]);
		result.xxxxx = FromSigned.toInt(s1b0[9], s1b0[10]);
		result.s1b1 = RC500.extractData(rc500.readBlock(5));
		result.maxCredit = FromSigned.toInt(result.s1b1[1], result.s1b1[2]);
		result.credit = FromSigned.toInt(result.s1b1[3], result.s1b1[4]);
		return result;
	}
	
	/**
	 * ��IC���������빤��״̬
	 * @param rc500
	 * @throws IOException
	 */
	private static void prepare(RC500 rc500) throws IOException {
		
		rc500.configRC();

		if(rc500.loadKeyOfSector(1, RC500.getKeyOfSector())!=0){
			throw new IOException();
		}
		
		if(rc500.request()!=0)throw new ConfigRCException();
		if(rc500.anticoll()!=0)throw new IOException();
		if(rc500.selectCard()!=0)throw new IOException();
		if(rc500.authenticateSector(1)!=0)throw new IOException();
		
	}
	
	/**
	 * 
	 * @param rc500
	 * @throws IOException
	 */
	public void save(RC500 rc500) throws IOException {
		this.s1b1[3] = FromSigned.lowByte(this.credit);
		this.s1b1[4] = FromSigned.highByte(this.credit);
		updateCheckSum(this.s1b1);
		prepare(rc500);
		rc500.writeBlock(5, this.s1b1);
	}
	
	/**
	 * дУ���
	 * @param bytes
	 */
	private static void updateCheckSum(byte[] bytes) {
		byte checkSum = bytes[0];
		for (int i = 1; i < bytes.length - 1; i++) {
			checkSum = (byte) (checkSum ^ bytes[i]);
		}
		bytes[bytes.length - 1] = checkSum;
	}
	
	/**
	 * 
	 * @return ����
	 */
	public String getSerialNumber() {
		return F00000.format(this.yyyyMMdd) + F00000.format(this.xxxxx);
	}
	
	/**
	 * 
	 * @return ������ö��
	 */
	public int getMaxCredit() {
		return this.maxCredit;
	}
	
	/**
	 * 
	 * @return ���ö��
	 */
	public int getCredit() {
		return credit;
	}
	
	/**
	 * 
	 * @param credit ���ö��
	 */
	public void setCredit(int credit) {
		this.credit = credit;
	}
	
	
	public String toString() {
		return "Serial#: " + this.getSerialNumber() + " max credit: "
				+ this.getMaxCredit() + " credit: " + this.credit;
	}
}