/*
 * Created on 2004-6-13
 */
package com.royalstone.pos.common;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * ǰ̨ģ��ʹ�� RequestLogon �����ͽ�����ʾ����̨ģ��.
 * NOTE: �ڽ��е�¼����ʱ��ϵͳҪ�������Ա�����κŵ���Ҫ��¼��Ӫҵ����. Ӫҵ������ϵͳ������
 * ��һ����������¿��ǣ����ڲ�����Ա��Ӫҵ���ڽ�������ɻ�������κŲ����׻�����
 * ��֮������ϵͳ���ԣ�ȷ��Ӫҵ���ڽ�Ϊ���ס�
 * @author Mengluoyi
 */

public class RequestLogon  implements Serializable
{
	/**
	 * @param posid			POS��ID.
	 * @param cashierid		����ԱID.
	 * @param plainpin		��¼����(����).
	 */
	public RequestLogon( String posid, String cashierid, String plainpin )
	{
		this.posid = posid;
		this.cashierid = cashierid;
		this.plainpin = plainpin;
	}
	
	public RequestLogon(String posid)
	{
		this.posid = posid;
		}
	
	/**
	 * @param posid			POS��ID.
	 * @param cashierid		����ԱID.
	 * @param plainpin		��¼����(����).
	 * @param shiftid		��κ�(1,2,3...).
	 */
	public RequestLogon( String posid, String cashierid, String plainpin, int shiftid )
	{
		this.posid 		= posid;
		this.cashierid 	= cashierid;
		this.plainpin 	= plainpin;
		this.shiftid  	= shiftid;
	}
	//�ٻ� 
	 public RequestLogon(int Order_id)
	 {
	 	this.Order_id = Order_id;
	 	
	 	}
	 
	 // �ٻ�����
	 public RequestLogon(String Largess, String Vgno){
	 	this.Largess = Largess;
	 	this.Vgno = Vgno;
	 	}
	 
	 public String getLargess(){
	 	return Largess;
	 	}
	 
	 public String getVgno(){
	 	return Vgno;
	 	}
	/**
	 * @param posid			POS��ID.
	 * @param cashierid		����ԱID.
	 * @param plainpin		��¼����(����).
	 * @param shiftid		��κ�(1,2,3...).
	 * @param ip			POS��IP��ַ.
	 */
	public RequestLogon( String posid, String cashierid, String plainpin, int shiftid ,String ip)
	{
			this.posid 		= posid;
			this.cashierid 	= cashierid;
			this.plainpin 	= plainpin;
			this.shiftid  	= shiftid;
			this.ip = ip;
	}
	
	/**
	 * @return	������¼�����������ID.
	 */
	public String getPosid()
	{
		return posid;
	}
	
	/**
	 * @return	�����¼������ԱID.
	 */
	public String getCashierid()
	{
		return cashierid;
	}
	
	/**
	 * @return	����Ա����Ŀ���(����).
	 */
	public String getPlainPin()
	{
		return plainpin;
	}
	
	public int getOrder_id()
	{
		return Order_id;
		}
	/**
	 * @return	�����¼�İ�κ�(1,2,3...).
	 */
	public int getShiftid()
	{
		return shiftid;
	}
	
	/**
	 * @return	������¼�����������IP��ַ.
	 */
	public String getIp()
	{
			return ip;
	}
	
	/**
	 * @return	���ܺ�ĵ�¼����. �������ݿ��д�ŵĿ����Ϊ����,��̨������Ҫ���ô˷���������Ա����Ŀ��������ܴ���������Ա�.
	 */
	public String getPinEncrypted()
	{
		long k = 123456789;
		
		for( int i=0; i < cashierid.length(); i++){
			long	a = ( (int)cashierid.charAt(i) ) % 13 + 1;
			k = ( k * a ) % 9999999 + 1;
		}
		
		k = k % 98989898 + 99;
		for(int i=0; i<  plainpin.length(); i++){
			long	a = ( (int)plainpin.charAt(i) ) % 17 + 1;
			k = ( k % 9876543 + 1 ) * a;
		}
		
		DecimalFormat df = new DecimalFormat( "00000000" );
		return df.format( k % 100000000 );
	}
	
	/**
	 * @return a string summarizing the request.
	 */
	public String toString()
	{
		return "Posid: " + posid + "; Cashierid: " + cashierid 
			+ "; pin: " + plainpin + "; shiftid=" + shiftid;
	}

	private String posid 	 = "";
	private String cashierid = "";
	private String plainpin  = "";
	private  String Largess = "";
	private  String Vgno = "";
	private int shiftid 	 = 0;
	private String ip        = "";	
	private int Order_id = 0;
}
