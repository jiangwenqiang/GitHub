package com.royalstone.pos.hardware.iccard.util;

/**
 * ʵ�÷���,���������ֽ������һ����
 * @author macro
 */
public class Slice {
	public static byte[] fromCount(byte[] array, int from, int count) {
		byte[] result = new byte[count];
		for (int i = 0; i < result.length; i++) {
			result[i] = array[from];
		}
		return result;
	}
}