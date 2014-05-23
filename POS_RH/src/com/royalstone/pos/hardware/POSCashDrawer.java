package com.royalstone.pos.hardware;

import jpos.CashDrawer;
import jpos.JposException;

import com.royalstone.pos.common.PosContext;

/**
 * Ǯ������Ӳ���㣨��JavaPos��׼��Control�ϵĲ�)
 * @author liangxinbiao
 */
public class POSCashDrawer {

	public static final int OPEN = 0;
	public static final int CLOSE = 1;
	public static final int UNKNOWN = 2;

	private static POSCashDrawer instance;

	private CashDrawer control;
	private boolean isOpen = false;

	/**
	 * ˽�й��췽����ֻ�����getInstance()������ȡ��ʵ��
	 * @param c
	 * @param devicename
	 */
	private POSCashDrawer(CashDrawer c, String devicename) {
		try {
			control = c;
			control.open(devicename);
			control.claim(1000);
			control.setDeviceEnabled(true);
			isOpen = true;
		} catch (JposException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ȡ��JavaPos��׼��Control���Ǯ�����
	 * @return
	 */
	public CashDrawer getControl() {
		return control;
	}

	/**
	 * ��Ǯ�� 
	 */
	public void open() {
		try {
			if (isOpen && !PosContext.getInstance().isTraining())
				control.openDrawer();
		} catch (JposException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * �ж�Ǯ�俪��״̬
	 * @return
	 */
	public int isOpen() {
		if (isOpen) {
			try {
				boolean result=control.getDrawerOpened();
				if(result){
					return OPEN;
				}else{
					return CLOSE;
				}
			} catch (JposException ex) {
				ex.printStackTrace();
			}
		}
		return UNKNOWN;
	}

	/**
	 * �Ե�ʵ���ķ�ʽȡ��Ǯ�����
	 * @return
	 */
	public static POSCashDrawer getInstance() {
		if (instance == null) {
			instance =
				new POSCashDrawer(new jpos.CashDrawer(), "POSCashDrawer");
		}
		return instance;
	}

}
