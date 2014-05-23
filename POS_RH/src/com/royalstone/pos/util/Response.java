/*
 * Created on 2004-6-4
 */
package com.royalstone.pos.util;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Response ���ڰѺ�ִ̨�н�����ݻ�ǰ̨. ��Ҫ������:
 * ִ�е����ں�ʱ��. �ɵ��÷��� getDate(), getTime() �õ�.
 * ִ�з��ش���.�ɵ��÷��� retCode() �õ�.
 * ����ִ�н���ļ�Ҫ˵��. �ɵ��÷���getNote() �õ�.
 * ִ�к������صĶ���.�ɵ��÷���getObject() �õ�.
 * @author Mengluoyi
 */
public class Response  implements Serializable
{
	/**
	 * @param ret
	 */
	public Response( int ret ) 
	{
		time = new GregorianCalendar();
		this.ret = ret;
	}
	
	/**
	 * @param ret
	 * @param note
	 */
	public Response( int ret, String note ) 
	{
		time = new GregorianCalendar();
		this.ret = ret;
		this.note = note;
	}
	
	public Response(Object obj)
	{
		this.obj = obj;
		}
	/**
	 * @param ret
	 * @param note
	 * @param obj
	 */
	public Response( int ret, String note, Object obj ) 
	{
		time = new GregorianCalendar();
		this.ret = ret;
		this.note = note;
		this.obj = obj;
	}
	   public Response( int ret, String note, Object obj,int listNO)
	{
		time = new GregorianCalendar();
		this.ret = ret;
		this.note = note;
		this.obj = obj;
        this.listNO=listNO;
	}
	/**
	 * @return
	 */
	public boolean succeed()
	{
		return ( ret == 0 );
	}
	
	/**
	 * @return
	 */
	public int retCode()
	{
		return ret;
	}
	
	/**
	 * @return
	 */
	public String getNote()
	{
		return note;
	}
	

	/**
	 * @return
	 */
	public Day getDate()
	{
		return new Day(time);
	}
	
	/**
	 * @return
	 */
	public PosTime getTime()
	{
		return new PosTime(time);
	}
	
	/**
	 * @return
	 */
	public Object getObject()
	{
		return obj;
	}
      public int getListNO(){
        return this.listNO;
    }

	/**
	 * @return
	 */
	public String toString()
	{
		return getDate().toString() + " " + getTime().toString() + " [" + ret + "] " + note;
	}

	private int ret = 0;
	private String note = "";
	private GregorianCalendar time; 
	private Object obj = null;
    private int listNO=0;
}
