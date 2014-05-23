package com.royalstone.pos.hardware;

import jpos.JposException;
import jpos.Scanner;

import com.royalstone.pos.common.PosFunction;
import com.royalstone.pos.io.PosInput;
import com.royalstone.pos.io.PosKeyMap;
import com.royalstone.pos.services.events.DataListenerAdapter;
import com.royalstone.pos.shell.pos;

/**
 * ɨ������������Ӳ���㣨��JavaPos��׼��Control�ϵĲ�)
 * @author liangxinbiao
 */
public class POSScanner {

	private jpos.Scanner control;
	private byte[] scanData;
	private int scanDataType;
	private boolean enable = true;
	private boolean isOpen = false;
	private PosKeyMap kmap;

	private static POSScanner instance;

	/**
	 * ˽�й��췽����ֻ�����getInstance()������ȡ��ʵ��
	 * @param c
	 * @param devicename
	 */
	private POSScanner(jpos.Scanner c, String devicename) {

		control = c;
		try {
			control.open(devicename);

			c.addDataListener(new DataListenerAdapter() {
				public void dataOccurred(jpos.events.DataEvent e) {

					try {
						scanData = control.getScanData();
						scanDataType = control.getScanDataType();
					} catch (jpos.JposException jpe) {
						jpe.printStackTrace();
					}
					if (enable) {
						process();
					}
				}
			});

			kmap = new PosKeyMap();
			kmap.fromXML("pos.xml");

			c.claim(1000);
			c.setDeviceEnabled(true);
			c.setDataEventEnabled(true);
			c.setDecodeData(true);
			isOpen = true;

		} catch (JposException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  ɨ���������ݵ���ʱ�Ĵ�����
	 */
	public void process() {
		try {
			pos.posOutStream.write(scanData);
			int keyCode = kmap.getKeyValue(new PosInput(PosFunction.CANCEL));
			pos.posOutStream.write(10);
			pos.posOutStream.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *  ����ɨ����Ϊ������
	 */
	public void disable() {
		enable = false;
	}

	/**
	 * ����ɨ����Ϊ����
	 */
	public void enable() {
		enable = true;
	}

	/**
	 * �Ե�ʵ���ķ�ʽȡ��ɨ��������
	 * @return
	 */
	public static POSScanner getInstance() {
		if (instance == null) {
			instance = new POSScanner(new Scanner(), "POSScanner");
		}
		return instance;
	}

}
