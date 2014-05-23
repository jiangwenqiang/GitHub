package com.royalstone.pos.util;
import java.text.DecimalFormat;

/**
 * Volume ��Ҫ��Ϊ������Ʒ�ͳ�����Ʒ���������������. 
 * ��Ʒ��������������������������Ҫ��: 
 * ��������ʱ����Ϊ��λ; ϵͳ�д洢�������Ժ���Ϊ��λ; 
 * ��ʾ����������Ϊ��λ,���Զ��ŷָ�; ���ɵ�XML�ڵ�������Ϊ��λ����û�ж��ŷָ�.
 * Volume ������������Ҫ����ṩһϵ�еķ���,ʹ�ó�����Խ��з����ת��. 
   @version 1.0 2004.05.27
   @author  Mengluoyi, Royalstone Co., Ltd.
 */


public class Volume
{
	public Volume()
	{
		millivolume = 0;
	}
	
	/**
	 * @param milli		�Ժ���Ϊ��λ������.
	 */
	public Volume( int milli )
	{
		millivolume = milli;
	}

	public Volume( Volume v )
	{
		millivolume = v.millivolume;
	}
	
	/**
	 * @return �Ժ���Ϊ��λ������.
	 */
	public int getMilliVolume()
	{
		return millivolume;
	}
	
	/**
	 * @return ����Ϊ��λ������(float ��).
	 */
	public double getVolume()
	{
		return (double)millivolume / 1000.0;
	}
	
	public void setVolume( double volume )
	{
		millivolume = (int) ( volume * 1000 );
	}

	/**
	 * Volume ����ļӷ�����.
	 * @param b A instance of Volume.
	 * @return  A Volume obj which equals this minus b.
	 */
	public Volume add( Volume b )
	{
		return new Volume ( millivolume + b.millivolume );
	}

	/**
	 * Volume ����ļ�������.
	 */
	public Volume minus( Volume b )
	{
		return new Volume ( millivolume - b.millivolume );
	}

	/**
	 * Volume ����ĳ˷�.
	 * @param n 
	 * @return ( this * n ).
	 */
	public Volume multiply( int n )
	{
		return new Volume ( millivolume * n );
	}

	/**
	 * Volume ����ĳ���.
	 * @param n ������.
	 * @return
	 */
	public Volume divide( int n )
	{
		return new Volume ( millivolume / n );
	}

	public int  compareTo( Volume b )
	{
		return ( millivolume - b.millivolume);
	}

	public boolean equals( Volume v )
	{
		return ( millivolume == v.millivolume );
	}
	
	/**
	 * ת������ʾ��ʽ
	 * @return XML�ڵ���ʹ�õ��ִ�(����Ϊ��λ,�ж��ŷָ�).
	 */
	public String toString()
	{
		DecimalFormat df = new DecimalFormat( ",##0.000" );
		return df.format( (double) millivolume / 1000.0 );
	}

	/**
	 * ת��������Ϊ��λ���ִ�.
	 * @return XML�ڵ���ʹ�õ��ִ�(����Ϊ��λ,�޶��ŷָ�).
	 */
	public String toVolumeStr()
	{
		DecimalFormat df = new DecimalFormat( "0.000" );
		return df.format( (double) millivolume / 1000.0 );
	}
	
	/**
	 * ȡ��Ʒ�����е���ͷ(��������).
	 * @return ��Ʒ�����в���һ���Ĳ���(�Ժ���Ϊ��λ)
	 */
	public int oddment()
	{
		return ( millivolume % 1000 );
	}

	private int millivolume = 0;
}
