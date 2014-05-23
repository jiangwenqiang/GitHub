package com.royalstone.pos.util;

import java.io.Serializable;

/**
 * ��װ�˴���POS��������ģ��İ汾��
 * @deprecated
 * @author liangxinbiao
 */
public class POSVersionVO implements Serializable {
	
	private String posVersion;
	private String loaderVersion;
	private String driverVersion;
	
	

	/**
	 * @return ��������Driver.zip�İ汾��
	 */
	public String getDriverVersion() {
		return driverVersion;
	}

	/**
	 * @return loader.jar�İ汾��
	 */
	public String getLoaderVersion() {
		return loaderVersion;
	}

	/**
	 * @return pos.jar�İ汾��
	 */
	public String getPosVersion() {
		return posVersion;
	}

	/**
	 * @param string ��������Driver.zip�İ汾��
	 */
	public void setDriverVersion(String string) {
		driverVersion = string;
	}

	/**
	 * @param string loader.jar�İ汾��
	 */
	public void setLoaderVersion(String string) {
		loaderVersion = string;
	}

	/**
	 * @param string pos.jar�İ汾��
	 */
	public void setPosVersion(String string) {
		posVersion = string;
	}

}
