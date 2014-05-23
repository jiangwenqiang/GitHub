/*
 * 创建日期 2010-6-2
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
 * 明华设备
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 * 0.rf_init(0,115200) //打开读写器

1.rf_reset(icdev,10);  //读写器复位
2.rf_request(icdev,1,&TagType);	//寻卡
3.rf_anticoll(icdev,0,&_Snr);  	//防冲突
4.rf_select(icdev,_Snr,&_Size);	//选卡
5.rf_load_key_hex();		//装载密码
6.rf_authentication();		//认证密码
7.rf_read_hex(int icdev,usigned char _Adr,unsigned char *_Data);
8.rf_halt(int icdev)//断开对卡片操作
9.rf_exit(icdev)//关闭读写器
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
		   
		   //初始化设备
		   icdev =  mwrfjavaapi.rf_init((short)0, 115200);
		   shortRet = mwrfjavaapi.rf_reset(icdev,10);
		   
		   //初始化设备失败,不需要关闭设备
		   if(shortRet!=0)
		   {
			  exceptionInfo = "设备初始化失败";
			  return false;
		   }
		   
		   //选取模式 
		   shortRet = mwrfjavaapi.rf_card(icdev, _Mode);

		   //第一声操作
		   shortRet = mwrfjavaapi.rf_beep(icdev, (short)10);
		   
		   //加载，验证密码
		   new String(prop.getProperty("Key")).getChars(0,12,_NKey,0);
		   shortRet = mwrfjavaapi.rf_load_key_hex(icdev,_Mode, _SecNr, _NKey);
		   //验证密码
		   shortRet = mwrfjavaapi.rf_authentication(icdev, _Mode, _SecNr);
		   
		   if(shortRet!=0)
		   {
			  exceptionInfo = "校验卡密钥出错";
			  //关闭
			  shortRet = mwrfjavaapi.rf_halt(icdev);
			  //关闭读写器
			  shortRet = mwrfjavaapi.rf_exit(icdev);
			  return false;
		   	}

			ICInput icInput=new ICInput();
			Thread.sleep(100);
			boolean wait=true;
			while(wait && !icInput.isFinish()){
				icInput.show();
				icInput.showStar();		
					   //读取块1
				    shortRet = mwrfjavaapi.rf_read(icdev, (short)1);
					if(shortRet!=0)
					  {
						 exceptionInfo = "读取卡号错误";
						  //关闭
						 shortRet = mwrfjavaapi.rf_halt(icdev);
						  //关闭读写器
						 shortRet = mwrfjavaapi.rf_exit(icdev);
						 return false;
					   }
					   char[] _Data = mwrfjavaapi.getData();
					   //处理卡号
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
					   
					   //读取块2
					   shortRet = mwrfjavaapi.rf_read(icdev, (short)2);
					if(shortRet!=0)
					   {
						  exceptionInfo = "读取卡密码错误";
						  //关闭
						  shortRet = mwrfjavaapi.rf_halt(icdev);
						  //关闭读写器
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

			//第二声操作
		   shortRet = mwrfjavaapi.rf_beep(icdev, (short)10);
		    //关闭
		   shortRet = mwrfjavaapi.rf_halt(icdev);
			//关闭读写器
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
			 
			 //初始化端口
			 int icdev =  mwrfjavaapi.rf_init((short)0, 115200);
			 //叫
//			 shortRet = mwrfjavaapi.rf_beep(icdev, (short)10);
			 //复位
			 shortRet = mwrfjavaapi.rf_reset(icdev,10);
//			 //防冲突
//			 shortRet = mwrfjavaapi.rf_anticoll(icdev,_Bcnt);
//			 if(shortRet == 0) 
//			    _Snr = mwrfjavaapi.getSnr();
			 
			 //选卡
//			 shortRet = mwrfjavaapi.rf_select(icdev,_Snr);
			 //选取模式 
			 shortRet = mwrfjavaapi.rf_card(icdev, _Mode);
			 
			 //装载密码  0       4-7 区
//			 new String("RHDLMMMMMMMM").getChars(0,12,_NKey,0);
			 new String("FFFFFFFFFFFF").getChars(0,12,_NKey,0);
			 shortRet = mwrfjavaapi.rf_load_key_hex(icdev,_Mode, _SecNr, _NKey);
			 //验证密码
			 shortRet = mwrfjavaapi.rf_authentication(icdev, _Mode, _SecNr);
			 
			 //初始化
			 //将块2的值初始化为1000
	//		 shortRet = mwrfjavaapi.rf_initval(icdev, (short)2, 1000);  

			 
//			char[] _Data1 =  new char[16];
//			new String("1234567890123").getChars(0,13,_Data1,0);
//			shortRet = mwrfjavaapi.rf_write(icdev, (short)1, _Data1);
			
			 //写
//		    char[] _Data2=  new char[16];
//		    new String("AAAAAAAAAAAAAAAA").getChars(0,16,_Data2,0);
//		    shortRet = mwrfjavaapi.rf_write(icdev, (short)2, _Data2);
			 
			 //读
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
			 
			 //关闭
			 shortRet = mwrfjavaapi.rf_halt(icdev);
			 //关闭读写器
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
