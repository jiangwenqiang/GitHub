/*
 *POS Version 4 Product
 *��ͨϵͳ�������޹�˾
 *@author HuangXuean
 *@version 1.0
 */
package com.royalstone.pos.hardware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import jpos.JposException;
import jpos.POSPrinterConst;

/**
 * �����嵥��Ϊ������ֳ���ƣ���ӡ������Ӳ���㣨��JavaPos��׼��Control�ϵĲ�)
 * @author HuangXuean
 */
public class POSPrinter1 implements IPrinter {

	private jpos.POSPrinter control;
	private boolean isOpen=false;
	
	private static POSPrinter1 instance;

	/**
	 * ˽�й��췽����ֻ�����getInstance()������ȡ��ʵ��
	 * @param c
	 * @param devicename
	 */
	private POSPrinter1(jpos.POSPrinter c, String devicename) {
		try {
			control = c;
			control.open(devicename);
			control.claim(1000);
			control.setDeviceEnabled(true);
			isOpen=true;
		} catch (JposException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ȡ��JavaPos��׼��Control���POS��ӡ������
	 * @return
	 */
	public jpos.POSPrinter getControl() {
		return control;
	}

	/**
	 * @see com.royalstone.pos.hardware.IPrinter#println(java.lang.String)
	 */
	public void println(String value) {
		try {
			if(isOpen){	
				control.printNormal(POSPrinterConst.PTR_S_JOURNAL, value + "\n");
			}
			
			
			File dir = new File("posprint/");
			if (!dir.exists()) {
				dir.mkdir();
			}

			String srcfile = "posprint" + File.separator + "Print.txt";						
								
			BufferedReader in = new BufferedReader(
				new StringReader(value));
			PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(srcfile,true)));
			while((value = in.readLine()) != null )
				out.println(value);
			  	out.close();
			  	
			}catch(JposException ex){
				ex.printStackTrace();
			}catch (FileNotFoundException ex){
				ex.printStackTrace();
			}catch(EOFException e) {
			  	System.err.println("End of stream");
			}catch(IOException ex){
				ex.printStackTrace();
			}	
				
	}

	/**
	 * @see com.royalstone.pos.hardware.IPrinter#feed(int)
	 */
	public void feed(int line) {
		try {
			if(isOpen){
				for (int i = 0; i < line; i++) {
					control.printNormal(POSPrinterConst.PTR_S_JOURNAL, "\n");
				}
			}
		} catch (JposException ex) {
			ex.printStackTrace();
		}
		
		try{
			File dir = new File("posprint/");
			if (!dir.exists()) {
				dir.mkdir();
			}

			String srcfile = "posprint" + File.separator + "Print.txt";						
			
			PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(srcfile,true)));
			for(int i = 0; i < line; i++)
				out.println("");
				
			out.close();
			  	
			}catch (FileNotFoundException ex){
				ex.printStackTrace();
			}catch(EOFException e) {
				System.err.println("End of stream");
			}catch(IOException ex){
				ex.printStackTrace();
			}

	}

	/**
	 * @see com.royalstone.pos.hardware.IPrinter#cut()
	 */
	public void cut() {
		try {
			if(isOpen){
				control.cutPaper(0);
			}
		} catch (JposException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * �Ե�ʵ���ķ�ʽȡ��СƱ��ӡ��
	 * @return
	 */
	public static POSPrinter1 getInstance() {
		if (instance == null) {
			instance = new POSPrinter1(new jpos.POSPrinter(), "POSPrinter1");
		}
		return instance;
	}


}

