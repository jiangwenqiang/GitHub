package com.royalstone.pos.card;

/**
 * �����ฺ������ʵ����IShoppingCard�ӿڵ���

 * @author liangxinbiao
 */
public class SHCardFactory {

	public static IShoppingCard createInstance() {
		return new SHCard();
	}
}
