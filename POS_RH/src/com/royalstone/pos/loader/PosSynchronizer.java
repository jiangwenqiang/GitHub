package com.royalstone.pos.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.jdom.JDOMException;

import com.royalstone.pos.gui.DialogConfirm;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.PosConfig;

/**
 * ��POS���ݽ���ͬ��
 * @author root
 */
public class PosSynchronizer {

	public static void main(String[] args) {
		try {
			PosSynchronizer s = new PosSynchronizer("pos.ini");
			s.synchronize();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public PosSynchronizer(String file)
		throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		posid = prop.getProperty("posid");
		host = prop.getProperty("server");
		port = Integer.parseInt(prop.getProperty("port"));
	}

	// ͬ������ ����POS����Ҫ���ص���Ϣ
	public void synchronize() throws IOException, JDOMException {
		try {
			//TODO ����ʱ�ӷ��������ز���Ա��Ϣ
			OperatorLoader oploader = new OperatorLoader(host, port);
			oploader.download("operator.NEW.lst");
			
			//TODO  ����pos������ʱ�ӷ�����ֱ�����ؼ۸���Ϣ  
			FavorLoader floader = new FavorLoader(host, port);
			floader.download("promo/favor.NEW.lst");
			
			XLoader xloader = new XLoader(host, port);
            xloader.loadPosConfig("pos.NEW.xml", posid);
             rename("pos.NEW.xml", "pos.xml", "pos.BAK.xml");
          PosConfig config=PosConfig.getInstance();
            String isFast=config.getString("ISFASTLOAD");
          if("OFF".equals(isFast)){
            if (confirm("�Ƿ�ӷ����������ѻ��۸����ݣ�")) {

				xloader.loadPrice();
			}
                //xloader.loadPriceCut("pricecut.NEW.xml");
				 File proFile=new File("price/pricemap.ini");
	                File barFile=new File("price/barcodemap.ini");
	                if(!proFile.exists())
	                    if(confirm("�ѻ��۸�����ӳ���ļ��ܵ��ƻ������������������ݣ��Ƿ����أ�"))
	                         xloader.loadPrice();
	                    else
	                         System.exit(2);
	               Properties proper=new Properties();
	               Properties properBarcode=new Properties();
	               proper.load(new FileInputStream(proFile));
	               properBarcode.load(new FileInputStream(barFile));
	               pos.core.priceListMap=new HashMap(proper);
	               pos.core.barcodeMap=new HashMap(properBarcode);
          }
          else{

                DiscountLoader discloader = new DiscountLoader(host, port);
    			discloader.download("promo/discount.NEW.lst");
    			xloader.loadPrice("price.NEW.xml");
    			xloader.loadPriceExt("promo/priceExt.NEW.xml");
    			xloader.loadCardType("promo/cardtype.NEW.xml");
                xloader.loadAccurateTable("promo/accurate.NEW.xml");
                xloader.loadPriceCut("promo/pricecut.NEW.xml");
                xloader.loadPriceComb("promo/pricecomb.NEW.xml");
                xloader.loadProdProperty("property.NEW.xml");
              // renameFiles();
          }

			//DiscountLoader discloader = new DiscountLoader(host, port);
    		//	discloader.download("discount.NEW.lst");

			//   xloader.loadAccurateTable("accurate.NEW.xml");
            //   xloader.loadPriceComb("pricecomb.NEW.xml");


			renameFiles();
		} catch (JDOMException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
//����ȷ��ѡ��Ի��� 	
   	private boolean confirm(String s) {
		DialogConfirm confirm = new DialogConfirm();
		confirm.setMessage(s);
		confirm.show();

		return (confirm.isConfirm());
	}
	

	private void renameFiles() {
        rename("promo/favor.NEW.xml", "promo/favor.xml", "promo/favor.BAK.xml");
        rename("promo/pricecut.NEW.xml", "promo/pricecut.xml", "promo/pricecut.BAK.xml");
        rename("promo/pricecomb.NEW.xml", "promo/pricecomb.xml", "promo/pricecomb.BAK.xml");
		rename("promo/accurate.NEW.xml", "promo/accurate.xml", "promo/accurate.BAK.xml");
		rename("operator.NEW.lst", "operator.lst", "operator.BAK.lst");
        rename("price.NEW.xml", "price.xml", "price.BAK.xml");
		rename("promo/discount.NEW.lst", "promo/discount.lst", "promo/discount.BAK.lst");
		//rename("promo/bulkprice.NEW.lst", "promo/bulkprice.lst", "promo/bulkprice.BAK.lst");
		rename("promo/favor.NEW.lst", "promo/favor.lst", "promo/favor.BAK.lst");

		rename("promo/priceExt.NEW.xml", "promo/priceExt.xml", "promo/priceExt.BAK.xml");
		rename("promo/cardtype.NEW.xml", "promo/cardtype.xml", "promo/cardtype.BAK.xml");
		rename("property.NEW.xml", "property.xml", "property.BAK.xml");
	}

	private void rename(String fnew, String fcurrent, String fbak) {
		if (new File(fnew).exists() && new File(fcurrent).exists()) {
			System.out.println("Del ... " + fbak);
			new File(fbak).delete();
		}

		if (new File(fnew).exists() && new File(fcurrent).exists()) {
			System.out.println("Ren ... " + fcurrent + " " + fbak);
			new File(fcurrent).renameTo(new File(fbak));
		}

		if (new File(fnew).exists()) {
			System.out.println("Ren ... " + fnew + " " + fcurrent);
			new File(fnew).renameTo(new File(fcurrent));
		}
	}

	private String host, posid;
	private int port;
}
