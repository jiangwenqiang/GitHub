/*
 * Created on 2004-10-14
 */
package com.royalstone.pos.journal;

import java.io.FileInputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**Ϊ�ش���һ���������ӵļ�¼���ʿ����ź�����װ��
 * ��XML�ļ����ɿ��źͿ������Ϣ
 * @author huangxuean
 */
public class LoanCard4Reprint {
	
	private String cardno;
	private String resultValue;
	//public static String filename = "reprint/reprintsheet.xml";
	
	public LoanCard4Reprint(String file){
		createInstace(file);
	}
	
	public void setResultValue(String value){
		resultValue = value;
	}
	
	public String getCardno(){
		return cardno;
	}
		
	public String getResultValue(){
		return resultValue;
	}
	/**���ļ������ɿ����Ϳ�����Ϣ
	 * @param file ��һ���ı����ļ���
	 * */
	private void createInstace(String file){
		
		try{			
			Document doc = (new SAXBuilder()).build( new FileInputStream(file) );
			Element root = doc.getRootElement();
			Element sheet = root.getChild("sheet");
			Element paymentlist = sheet.getChild("paymentlist");	
			List payments = paymentlist.getChildren("payment");		
		
			for (int i=0 ; i < payments.size() ; i++){
				Element type = ((Element)(payments.get(i))).getChild("type");
				Element card = ((Element)(payments.get(i))).getChild("cardno");
				Element cardResult = ((Element)(payments.get(i))).getChild("cardResult");	
							
				if(type.getTextTrim().charAt(0)=='V'){
					cardno = card.getTextTrim();
					resultValue = cardResult.getTextTrim();
				}
			   
			}
			return ;
		
			} catch ( Exception e ){
				e.printStackTrace();
			}
			return ;
		}

	public static void main(String[] args) {
		LoanCard4Reprint test = new LoanCard4Reprint("reprint/reprintsheet.xml");
		System.out.println("test.getCardno():  "+test.getCardno());
		System.out.println("test.getResultValue():  "+test.getResultValue());
	}
}
