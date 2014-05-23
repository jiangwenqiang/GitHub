package com.royalstone.pos.hardware;

import java.util.Hashtable;

import jpos.config.JposEntry;
import jpos.config.simple.xml.XercesRegPopulator;

public class JposXmlReader extends XercesRegPopulator{

	private static JposXmlReader instance = null;
	private Hashtable table = null;
	private JposEntry entry = null;
	
	public JposXmlReader() {
		super();
		load(DEFAULT_XML_FILE_NAME);
	}

	public JposXmlReader(String s) {
		super(s);
	}
		
	public static JposXmlReader getInstance() {
		if (instance == null) {
			instance = new JposXmlReader();
		}
		return instance;
	}
	
	public String getPropValue(String entryName,String propName)
	{
		table = getJposEntries();
		if(table != null)
			entry = (JposEntry)table.get(entryName);
		
		String value = null;
		if(entry != null)
			value = (String)entry.getPropertyValue(propName);	
		return value;
	}
	
	public void setEntry(String entryName)
	{
		table = getJposEntries();
		if(table != null)
			entry = (JposEntry)table.get(entryName);
	}
	
	public String getPropValue(String propName)
	{
		String value = null;
		if(entry != null)
			value = (String)entry.getPropertyValue(propName);	
		return value;
	}
	
	public JposEntry getEntry(String entryName)
	{
		table = getJposEntries();
		if(table != null)
			entry = (JposEntry)table.get(entryName);
		return entry;
	}
	
	public static void main(String[] args) 
	{
		/*
		JposXmlReader reader = new JposXmlReader();
		reader.load(DEFAULT_XML_FILE_NAME);
		Hashtable table = reader.getJposEntries();
		JposEntry entry = (JposEntry)table.get("POSPrinterEx");
		String driverName = (String)entry.getPropertyValue("driverName");
		*/
		JposXmlReader.getInstance().setEntry("POSPrinterEx"); 
		String driverName = JposXmlReader.getInstance().getPropValue("driverName");
		System.out.println(driverName);
	}
}
