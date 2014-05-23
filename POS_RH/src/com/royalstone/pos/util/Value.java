package com.royalstone.pos.util;
import java.text.DecimalFormat;

/**
 * Value ���ڱ�ʾ���.Ϊ�˱������"�����",POS�ڲ���long �ͱ�ʾ���,��λΪ��.
 * ��ʾ�ʹ�ӡʱ,��Ҫת��Ϊ�Զ��ŷָ����ִ�,��XML�ĵ�����Ҫת�����޶��ŷָ����ִ�.
 * ʹ�ø�����Է����ʵ��������ص�һϵ�е�ת���ͼ���.
 * 
 * Value �ṩ���� "��/��/��/��" �������Ӧ�ķ���: add, minus, multiply, divide.
 * ����, add/minus ֻ������ Value ����֮�����. multiply/divide �Ĳ���������������. ���������Ʒ����Ʒ�Ľ����Ҫ�����˷���������.
   @version 1.0 2004.05.27
   @author  Mengluoyi, Royalstone Co., Ltd.
 */


public class Value
{

	/**
	 * @param cents	�Է�Ϊ��λ��ʾ�Ľ��.
	 */
	public Value( long cents )
	{
		value = cents;
	}

	/**
	 * @param cents	�Է�Ϊ��λ��ʾ�Ľ��.
	 */
	public Value( int cents )
	{
		value = cents;
	}

	/**
	 * @param v	A instance of Value.
	 */
	public Value( Value v )
	{
		value = v.value;
	}

	/**
	 * @return	�Է�Ϊ��λ��ʾ�Ľ��.
	 */
	public long getCents()
	{
		return value;
	}

	/**
	 * @param b	A instance of Value.
	 * @return	A instance of Value which equals to this Value added to argument b.
	 */
	public Value add( Value b )
	{
		return new Value ( value + b.value );
	}

	/**
	 * @param b	A instance of Value.
	 * @return	A instance of Value which equals to this Value minus argument b.
	 */
	public Value minus( Value b )
	{
		return new Value ( value - b.value );
	}

	/**
	 * @param n	a integer.
	 * @return	A instance of Value which equals to this Value times n.
	 */
	public Value multiply( int n )
	{
		return new Value ( value * n );
	}

	/**
	 * @param n	a integer.
	 * @return	A instance of Value which equals to this Value divided by n.
	 * ����Value �ľ���Ϊ��,divide������ܴ������. ����, 100 ��ʾ1.00Ԫ,����3����Ϊ33(�൱��0.33Ԫ)
	 */
	public Value divide( int n )
	{
		return new Value ( value / n );
	}
	
	/**
	 * @param b A instance of Value.
	 * @return	The value 0 if the argument b is a Value equal to this Value, 
	 * the value less than 0 if argument b is greater than this Value,
	 * the value greater than 0 if argument b is less than this Value.
	 */
	public long  compareTo( Value b )
	{
		return ( value - b.value);
	}

	/**
	 * @param v	A instance of Value.
	 * @return	true is argument v equals to this Value, false if argument v does not equals to this Value.
	 */
	public boolean equals( Value v )
	{
		return ( value == v.value );
	}

	/**
	 * �˷�����Ҫ����������ʾʱʹ�õ��ִ�.
	 * @return	��ʾ�����ִ�, ��ԪΪ��λ, С���������λ, �ж��ŷָ�.
	 */
	public String toString()
	{
		DecimalFormat df = new DecimalFormat( ",##0.00" );
		return df.format( (double) value / 100.0 );
	}

	/**
	 * �˷�����Ҫ��������XML�ĵ��ڵ��ִ�.
	 * @return	��ʾ�����ִ�, ��ԪΪ��λ, С���������λ, �޶��ŷָ�.
	 */
	public String toValStr()
	{
		DecimalFormat df = new DecimalFormat( "0.00" );
		return df.format( (double) value / 100.0 );
	}

	private long value = 0;
}
