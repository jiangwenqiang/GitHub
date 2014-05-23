/*
 * Created on 2004-6-16
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.royalstone.pos.util;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.jdom.Element;

/**
 * Workturn ��ҵ���ϵ�"���"�ڼ����ϵ�ʵ��.
 * �����Ҫ���ж�: workdate(Ӫҵ����) ��shiftid(��κ�). shiftid ΪӪҵԱ��εı��, workdate ΪӪҵ����. 
 * ��������ҵ�����ƶ�,Ӫҵ���ڿ�����ϵͳ���ڲ�һ��. 
 * ��ʵ��˵������: ����涨ÿ������,��һ�ཻ��ʱ��Ϊ����7��,����2004��9��1���糿6:30,Ӫҵ���Ϊ2004-08-31#03,
 * 7�㽻�����Ϊ2004-09-01#01.
 *
 * @author Mengluoyi
 */
public class WorkTurn  implements Serializable
{

	public static void main(String[] args) 
	{
		WorkTurn t = new WorkTurn( new Day(), 2 );
		System.out.println( t.toString() );
	}
	
	
	/**
	 * ȱʡ�Ĺ��캯��,ȡ��ǰϵͳ����ΪӪҵ����.��κ�Ϊ0. 
	 */
	public WorkTurn()
	{
		workdate = new Day();
		postime  = new PosTime();
		shiftid  = 0;
	}
	
	/**
	 * @param workdate Ӫҵ����.
	 * @param shiftid  Ӫҵ��κ�.
	 */
	public WorkTurn( Day workdate, int shiftid )
	{
		this.workdate = workdate;
		this.shiftid = shiftid;
	}
	
	/**
	 * @param elm	��XML�ڵ��ʾ�İ����Ϣ.
	 * @throws InvalidDataException
	 */
	public WorkTurn( Element elm ) throws InvalidDataException
	{
		try {
			shiftid = Integer.parseInt( elm.getChild( "shiftid" ).getTextTrim() );
			workdate = new Day( elm.getChild( "workdate" ) );
		} catch (Exception e) {
			throw new InvalidDataException( "WorkTurn" );
		}
	}
	
	/**
	 * get an XML element which represents WorkTurn obj.
	 * @return	��XML�ڵ��ʾ�İ����Ϣ.
	 */
	public Element toElement()
	{
		Element elm = new Element( "workturn" );
		Element d = ( new Element( "workdate" ) ).addContent( workdate.toElement() );
		Element s = ( new Element( "shiftid" ) ).addContent( "" + shiftid );
		elm.addContent( d );
		elm.addContent( s );
		return elm;
	}
	
	/**	ȡ�����Ϣ�е�Ӫҵ����.
	 * @return	Ӫҵ����.
	 */
	public Day getWorkDate()
	{
		return workdate;
	}
	
	/**
	 * @return
	 */
	public String getLWorkDate()
	{
		return workdate.LtoString();
	}
	
	/**
	 * @return
	 */
	public String getLPosTime()
	{
		return postime.LtoString();
	}
	
	/**
	 * ȡ��κ�.
	 * @return	��κ�.
	 */
	public int getShiftid()
	{
		return shiftid;
	}
	
	/**
	 * �ж���������Ƿ���ͬ.
	 * @param t	���.
	 * @return	
	 * true		��ͬ.<br/>
	 * false	��ͬ.
	 */
	public boolean equals( WorkTurn t )
	{
		return ( this.workdate.equals(t.workdate) && this.shiftid == t.shiftid );
	}
	
	/**
	 * @param b
	 * @return
	 */
	public int compareTo( WorkTurn b )
	{
		int diff = this.workdate.daysBetween( b.workdate );
		int shift_diff = this.shiftid - b.shiftid;

		if( diff <0 ) return -1;
		if( diff >0 ) return  1;
		if( diff == 0 && shift_diff <0 ) return -1;
		if( diff == 0 && shift_diff >0 ) return  1;
		return 0;
	}
	
	/**
	 * @return	���ִ���ʾ�İ����Ϣ.��: 2004-09-28#01
	 */
	public String toString()
	{
		DecimalFormat df = new DecimalFormat( "00" );
		return workdate.toString() + "#" + df.format(shiftid); 
	}
	
	public String LtoString()
	{
		DecimalFormat df = new DecimalFormat( "00" );
	    return workdate.LtoString() ; 

	}
	
	private Day workdate;
	private PosTime postime;
	private int shiftid;
}
