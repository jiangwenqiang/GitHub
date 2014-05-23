package com.royalstone.pos.hardware;

import jpos.config.JposEntry;

import com.royalstone.pos.services.RSPrinter;

public class POSPrinterEx implements IPrinter {
	
	String resetEntryName = null;
	
	private String driverName;
	private String portName;
	private String baudRate;
	private String byteSize;
	private String parity;
	private String stopBits;
	private String value1;
	private String value2;
	private String value3;
	private String value4;
	private String value5;

	public void init(JposEntry entry) {
		driverName = (String) entry.getPropertyValue("driverName");
		portName = (String) entry.getPropertyValue("portName");
		baudRate = (String) entry.getPropertyValue("baudRate");
		byteSize = (String) entry.getPropertyValue("byteSize");
		parity = (String) entry.getPropertyValue("parity");
		stopBits = (String) entry.getPropertyValue("stopBits");
		value1 = (String) entry.getPropertyValue("value1");
		value2 = (String) entry.getPropertyValue("value2");
		value3 = (String) entry.getPropertyValue("value3");
		value4 = (String) entry.getPropertyValue("value4");
		value5 = (String) entry.getPropertyValue("value5");
		System.out.println(driverName);
		
		//loadLibrary(driverName);
		initPrinter();
	}
	
	protected void loadLibrary(String driverName)
	{
		RSPrinter.driverInit(driverName);
	}
	
	public void initPrinter()
	{
		RSPrinter.paramInit(
				portName,
				baudRate,
				byteSize,
				parity,
				stopBits,
				value1,
				value2,
				value3,
				value4,
				value5);	
	}

	public void resetPrinter()
	{
		JposEntry entry = JposXmlReader.getInstance().getEntry("POSPrinter");
    	init(entry);
	}
	
	public void setPrinter(String entryName)
	{
		JposEntry entry = JposXmlReader.getInstance().getEntry(entryName);
		init(entry);
	}
	
    public POSPrinterEx() {
    	//resetPrinter();
    }

	public void cut() {
		RSPrinter.printCut();
	}
	
	public void feed(int line) {
		while(line>0)
		{
			println("");
			line--;
		}
			
	}

	public void println(String data) {
		// TODO 自动生成方法存根
		String value = data + "\n";
		try{
			RSPrinter.printStr(value.getBytes("GB2312"));
    	}catch(Exception ex){
			System.out.println(ex.toString());
			ex.printStackTrace();
    	}
	}

	public static POSPrinterEx getInstance() {
		if (instance == null) {
			instance = new POSPrinterEx();
		}
		return instance;
	}
	private static POSPrinterEx instance;
}
