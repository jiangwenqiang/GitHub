/*
 * �������� 2010-6-2
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.royalstone.pos.card;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.mhhid.MWRFJavaAPI;
import com.royalstone.pos.gui.ICInput;
import com.royalstone.pos.hardware.iccard.ConfigRCException;
import com.royalstone.pos.hardware.iccard.ICCardInfo;
import java.lang.Character;
/**
 * @author Thinkpad User
 * �����豸
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 * 0.rf_init(0,115200) //�򿪶�д��

1.rf_reset(icdev,10);  //��д����λ
2.rf_request(icdev,1,&TagType);	//Ѱ��
3.rf_anticoll(icdev,0,&_Snr);  	//����ͻ
4.rf_select(icdev,_Snr,&_Size);	//ѡ��
5.rf_load_key_hex();		//װ������
6.rf_authentication();		//��֤����
7.rf_read_hex(int icdev,usigned char _Adr,unsigned char *_Data);
8.rf_halt(int icdev)//�Ͽ��Կ�Ƭ����
9.rf_exit(icdev)//�رն�д��
 */
public class ICCardR330 {
	private String exceptionInfo;
	private String cardno="";
	private String pass="";

	public boolean process(){
		try {
		   int icdev =0;
		   short shortRet=-1;
		   char[] _NKey=  new char[12];
		   short _Mode=  0;
		   short _SecNr=  0;

		   Properties prop = new Properties();
		   prop.load(new FileInputStream("ICCardR330.properties"));
		   MWRFJavaAPI mwrfjavaapi = new MWRFJavaAPI();
		   
		   //��ʼ���豸
		   icdev =  mwrfjavaapi.rf_init((short)0, 115200);
		   shortRet = mwrfjavaapi.rf_reset(icdev,10);
		   
		   //��ʼ���豸ʧ��,����Ҫ�ر��豸
		   if(shortRet!=0)
		   {
			  exceptionInfo = "�豸��ʼ��ʧ��";
			  return false;
		   }
		   
		   //ѡȡģʽ 
		   shortRet = mwrfjavaapi.rf_card(icdev, _Mode);

		   //��һ������
		   shortRet = mwrfjavaapi.rf_beep(icdev, (short)10);
		   
		   //���أ���֤����
		   new String(prop.getProperty("Key")).getChars(0,12,_NKey,0);
		   shortRet = mwrfjavaapi.rf_load_key_hex(icdev,_Mode, _SecNr, _NKey);
		   //��֤����
		   shortRet = mwrfjavaapi.rf_authentication(icdev, _Mode, _SecNr);
		   
		   if(shortRet!=0)
		   {
			  exceptionInfo = "У�鿨��Կ����";
			  //�ر�
			  shortRet = mwrfjavaapi.rf_halt(icdev);
			  //�رն�д��
			  shortRet = mwrfjavaapi.rf_exit(icdev);
			  return false;
		   	}

			ICInput icInput=new ICInput();
			Thread.sleep(100);
			boolean wait=true;
			while(wait && !icInput.isFinish()){
				icInput.show();
				icInput.showStar();		
					   //��ȡ��1
				    shortRet = mwrfjavaapi.rf_read(icdev, (short)1);
					if(shortRet!=0)
					  {
						 exceptionInfo = "��ȡ���Ŵ���";
						  //�ر�
						 shortRet = mwrfjavaapi.rf_halt(icdev);
						  //�رն�д��
						 shortRet = mwrfjavaapi.rf_exit(icdev);
						 return false;
					   }
					   char[] _Data = mwrfjavaapi.getData();
					   //������
					   char[] _Data_new=new char[16];
						int len=0;
					    for(int i=0;i<_Data.length;i++)
					    {  
					    	char a = _Data[i];
					        
					        if(Character.isDigit(a))
					        {
					        	_Data_new[len]=a;
					        	len++;
					        	}
					    }

					   cardno = String.valueOf(_Data_new).trim();
					   
					   //��ȡ��2
					   shortRet = mwrfjavaapi.rf_read(icdev, (short)2);
					if(shortRet!=0)
					   {
						  exceptionInfo = "��ȡ���������";
						  //�ر�
						  shortRet = mwrfjavaapi.rf_halt(icdev);
						  //�رն�д��
						  shortRet = mwrfjavaapi.rf_exit(icdev);
						  return false;
					   	}
				 char[] _Data_1 = mwrfjavaapi.getData();
				 pass = String.valueOf(_Data_1).trim();
				 if(pass.length()>7)
				 	pass=pass.substring(0,7);
				 
				 wait=false;
				 icInput.dispose();
			}

			//�ڶ�������
		   shortRet = mwrfjavaapi.rf_beep(icdev, (short)10);
		    //�ر�
		   shortRet = mwrfjavaapi.rf_halt(icdev);
			//�رն�д��
		   shortRet = mwrfjavaapi.rf_exit(icdev);

           return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
		
	public String getExceptionInfo() {
		return exceptionInfo;
	}

	public String getCardNo() {
		return cardno;
	}

	public String getPass() {
		if(pass.length()<1)
			return "0";
		return pass;
	}
	
	 
		public static void main(String args[]) throws Exception{
			 MWRFJavaAPI mwrfjavaapi = new MWRFJavaAPI();
			 short _Adr=  2;
			 short shortRet;
			 long _Value= 2000;
			 short _Bcnt=  0;
			 long _Snr=0;
			 short _Mode=  0;
			 short _SecNr=  0;
			 char[] _NKey=  new char[12];
			 
			 //��ʼ���˿�
			 int icdev =  mwrfjavaapi.rf_init((short)0, 115200);
			 //��
//			 shortRet = mwrfjavaapi.rf_beep(icdev, (short)10);
			 //��λ
			 shortRet = mwrfjavaapi.rf_reset(icdev,10);
//			 //����ͻ
//			 shortRet = mwrfjavaapi.rf_anticoll(icdev,_Bcnt);
//			 if(shortRet == 0) 
//			    _Snr = mwrfjavaapi.getSnr();
			 
			 //ѡ��
//			 shortRet = mwrfjavaapi.rf_select(icdev,_Snr);
			 //ѡȡģʽ 
			 shortRet = mwrfjavaapi.rf_card(icdev, _Mode);
			 
			 //װ������  0       4-7 ��
//			 new String("RHDLMMMMMMMM").getChars(0,12,_NKey,0);
			 new String("FFFFFFFFFFFF").getChars(0,12,_NKey,0);
			 shortRet = mwrfjavaapi.rf_load_key_hex(icdev,_Mode, _SecNr, _NKey);
			 //��֤����
			 shortRet = mwrfjavaapi.rf_authentication(icdev, _Mode, _SecNr);
			 
			 //��ʼ��
			 //����2��ֵ��ʼ��Ϊ1000
	//		 shortRet = mwrfjavaapi.rf_initval(icdev, (short)2, 1000);  

			 
//			char[] _Data1 =  new char[16];
//			new String("1234567890123").getChars(0,13,_Data1,0);
//			shortRet = mwrfjavaapi.rf_write(icdev, (short)1, _Data1);
			
			 //д
//		    char[] _Data2=  new char[16];
//		    new String("AAAAAAAAAAAAAAAA").getChars(0,16,_Data2,0);
//		    shortRet = mwrfjavaapi.rf_write(icdev, (short)2, _Data2);
			 
			 //��
//			 shortRet = mwrfjavaapi.rf_read_hex(icdev, (short)2);
		    shortRet = mwrfjavaapi.rf_read(icdev, (short)1);
			char[] _Data = mwrfjavaapi.getData();
//			char[] _Data = mwrfjavaapi.getPtrDest();
			
			
	//	    System.out.println(d.indexOf(""));
			char[] _Data2=new char[16];
			int d2=0;
		    for(int i=0;i<_Data.length;i++)
		    {  
		    	char a = _Data[i];
		        
		        if(Character.isDigit(a))
		        {
		        	_Data2[d2]=a;
		        	d2++;
		        	}
		    	}
		    String d = String.valueOf(_Data2).trim();
		    
			
		    shortRet = mwrfjavaapi.rf_read(icdev, (short)2);
			char[] _Data1 = mwrfjavaapi.getData();
			String d1 = String.valueOf(_Data1).trim();

			 
//			 shortRet = mwrfjavaapi.rf_readval(icdev, (short)2);
//			 long dd = mwrfjavaapi.getValue();
			 
			 //�ر�
			 shortRet = mwrfjavaapi.rf_halt(icdev);
			 //�رն�д��
			 shortRet = mwrfjavaapi.rf_exit(icdev);
			 

		}
		
		public static void printHexString(byte[] b) { 
			for (int i = 0; i < b.length; i++) { 
			String hex = Integer.toHexString(b[i] & 0xFF); 
			if (hex.length() == 1) { 
			hex = '0' + hex; 
			} 
			System.out.print(hex.toUpperCase() + " "); 
			} 
			System.out.println(""); 
			} 

}
