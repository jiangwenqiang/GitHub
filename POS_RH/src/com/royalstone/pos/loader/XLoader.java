/*
 * Created on 2004-6-6
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.royalstone.pos.loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.invoke.FileDownload;
import com.royalstone.pos.util.UnZipFile;


/**
 * @author root
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XLoader {

	public static void main(String[] args) 
	{
		XLoader loader = new XLoader( "localhost", 9090 );
		try {
			loader.loadPrice( "price.NEW.xml" );
			loader.loadPosConfig( "pos.NEW.xml", "P001" );
		} catch (JDOMException e) {
			System.out.println( "load Failed." );
		} catch (IOException e) {
			System.out.println( "load Failed." );
		}
	}

	public XLoader( String host, int port )
	{
		this.host = host;
		this.port = port;
	}

	public void loadPrice( String file ) throws JDOMException, IOException
	{
		String url = "http://" + host + ":" + port + "/pos41/PriceTable";
		loadXMLDoc( file, url );
	}
	
	public void loadPriceComb( String file ) throws JDOMException, IOException
	{
		String url = "http://" + host + ":" + port + "/pos41/PriceTableComb";
		loadXMLDoc( file, url );
	}
	
	public void loadAccurateTable( String file ) throws JDOMException, IOException
	{
		String url = "http://" + host + ":" + port + "/pos41/AccurateTable";
		loadXMLDoc( file, url );
	}
	
	public void loadProdProperty(String file) throws JDOMException, IOException
	{
		String url = "http://" + host + ":" + port + "/pos41/PropertyTable";
		loadXMLDoc( file, url );
	}
	
	/**
	 * 从服务器下载商品价格数据
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void loadPrice() throws JDOMException, IOException
	{  //
		File downPrice=new File("download");
		if(!downPrice.exists())
			downPrice.mkdirs(); 
		FileDownload fd=new FileDownload(host,Integer.toString(port));
		if(!fd.download("pricetable.zip","download/pricetable.zip"))
			throw new IOException("下载价格文件失败");
		if(!fd.download("promotable.zip","download/promotable.zip"))
			throw new IOException("下载促销价格文件失败");
		UnZipFile unzip= new UnZipFile();
		unzip.unZip("download/pricetable.zip","price");
		unzip.unZip("download/promotable.zip","promo");
	}

	public void loadPriceExt( String file ) throws JDOMException, IOException
	{
		String url = "http://" + host + ":" + port + "/pos41/PriceTableExt";
		loadXMLDoc( file, url );
	}
	public void loadPriceCut( String file ) throws JDOMException, IOException
	{
		String url = "http://" + host + ":" + port + "/pos41/PriceTableCut";
		loadXMLDoc( file, url );
	}


	public void loadPosConfig( String file, String posid ) throws JDOMException, IOException
	{
		String url = "http://" + host + ":" + port + "/pos41/PosInit?posid=" + posid;
		loadXMLDoc( file, url );
	}

	public void loadCardType( String file ) throws JDOMException, IOException
	{
		String url = "http://" + host + ":" + port + "/pos41/CardType";
		loadXMLDoc( file, url );
	}

	public void loadXMLDoc( String file, String url ) throws JDOMException, IOException
	{
		FileOutputStream fs = new FileOutputStream(file);

		servlet = new URL( url );
		Document  doc    = (new SAXBuilder()).build( servlet );
		Element   root   = doc.getRootElement();
		XMLOutputter outputter = new XMLOutputter( "  ", true, "GB2312" );
		outputter.output( doc, fs );
		fs.close();
	}

	private URL servlet;
	private HttpURLConnection conn;
	private String host;
	private int port;
}


