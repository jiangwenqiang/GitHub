------------------------------------------------------------------------------------------------------------------
-- $Author:: Xindongchun         $  
-- $Date:: 04-08-10 9:50         $  
-- $Modtime:: 04-08-10 9:49      $  
-- $Revision:: 7                 $  
------------------------------------------------------------------------------------------------------------------

SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON
GO

SET ANSI_NULL_DFLT_ON ON
GO

-----------------------------------------------------------
--APPLE1.0版本，初始化APPLEPOS数据
--建立：唐天明 2002.07.12
--最后修改：辛东春 2002.08.27
--修改记录：
--    辛东春 2002.08.27 把键盘的相关参数设置改为bcp倒入，请直接运行init.bat即可.
--移植到Apple 1.0 赵剑 2003-5-23
-- 修改 : 施华钧 20030714 增加初始化数据
-- 注意: 修改 config和rjcontrol  表本店号配置,默认 A00B
-----------------------------------------------------------
--1.选择正确的数据库


--2.先清除欲初始化的表
delete from hotkey_cfg;
delete from keybd_layout;
delete from keyname ;
delete from keybd_cfg ;
delete from keybd_lst ;
delete from Keybd_Class ;
delete from function_lst;
delete from distitem_type;
delete from power_lst ;
delete from right_lst ;
delete from pos_para ;
delete from edition_ctl ;
delete from pos_ini ;
delete from posgroup;
delete from weight_code ;
delete from rate_lst ;
delete from pos_para ;
delete from pos_ini ;
delete from cardlist;
delete from cardnote;
delete from vgbkcard;
delete from EventCode ;
delete from EventLevel;
delete from mysqlstr;
delete from vgbkcard;
delete from OilDept where 1=1;


--3.增加促销类型定义表
-------distitem_type--------------------------------------
--3.1	商品促销类型
 insert into distitem_type(fieldname,type,desp) values('disc_type','c','手工变价');
 insert into distitem_type(fieldname,type,desp) values('disc_type','d','小类折扣');
 insert into distitem_type(fieldname,type,desp) values('disc_type','g','柜组折扣');
 insert into distitem_type(fieldname,type,desp) values('disc_type','h','会员价');
 insert into distitem_type(fieldname,type,desp) values('disc_type','J','手工金额折扣');
 insert into distitem_type(fieldname,type,desp) values('disc_type','k','会员折扣');
 insert into distitem_type(fieldname,type,desp) values('disc_type','M','手工总额折扣');
 insert into distitem_type(fieldname,type,desp) values('disc_type','n','无折扣');
 insert into distitem_type(fieldname,type,desp) values('disc_type','p','促销');
 insert into distitem_type(fieldname,type,desp) values('disc_type','t','时点折扣');
 insert into distitem_type(fieldname,type,desp) values('disc_type','v','单品折扣');
 insert into distitem_type(fieldname,type,desp) values('disc_type','Z','手工单项折扣');
 insert into distitem_type(fieldname,type,desp) values('disc_type','B','量贩促销');
 insert into distitem_type(fieldname,type,desp) values('disc_type','s','组合促销');
--3.2	收银(支付)方式
 insert into distitem_type(fieldname,type,desp) values('paytype','C','现金');
 insert into distitem_type(fieldname,type,desp) values('paytype','D','代币券');
 insert into distitem_type(fieldname,type,desp) values('paytype','F','逃车');
 insert into distitem_type(fieldname,type,desp) values('paytype','K','支票');
 insert into distitem_type(fieldname,type,desp) values('paytype','L','联名卡');
 insert into distitem_type(fieldname,type,desp) values('paytype','R','银行卡');
 insert into distitem_type(fieldname,type,desp) values('paytype','S','抽样检测');
 insert into distitem_type(fieldname,type,desp) values('paytype','T','油机测试');
 insert into distitem_type(fieldname,type,desp) values('paytype','V','提货卡');
--3.3	商品促销时的促销手段
 insert into distitem_type(fieldname,type,desp) values('promtype','0','普通');
 insert into distitem_type(fieldname,type,desp) values('promtype','1','特价');
 insert into distitem_type(fieldname,type,desp) values('promtype','2','惠赠');

--3.4	商品条码类型
 insert into distitem_type(fieldname,type,desp) values('v_type','0','普通');
 insert into distitem_type(fieldname,type,desp) values('v_type','2','称重');
 insert into distitem_type(fieldname,type,desp) values('v_type','3','金额');
 insert into distitem_type(fieldname,type,desp) values('v_type','8','基本');
 insert into distitem_type(fieldname,type,desp) values('v_type','9','代收银');


--4.增加前台权限表
 insert into right_lst(power_id,name) values(1,'基本功能');
 insert into right_lst(power_id,name) values(38,'整单取消');
 insert into right_lst(power_id,name) values(41,'金额折扣');
 insert into right_lst(power_id,name) values(43,'重打上一单');
 insert into right_lst(power_id,name) values(67,'更正');
 insert into right_lst(power_id,name) values(70,'退货');
 insert into right_lst(power_id,name) values(73,'即更');
 insert into right_lst(power_id,name) values(77,'变价');
 insert into right_lst(power_id,name) values(95,'拍卖');
 insert into right_lst(power_id,name) values(97,'逃车');
 insert into right_lst(power_id,name) values(98,'抽样检查');
 insert into right_lst(power_id,name) values(99,'油机测试');
 insert into right_lst(power_id,name) values(123,'单品折扣');
 insert into right_lst(power_id,name) values(124,'强行单机');
 insert into right_lst(power_id,name) values(125,'总额折扣');
 insert into right_lst(power_id,name) values(126,'钱箱状态');


--9.对汇率表进行初始化
delete from rate_lst;
insert into rate_lst(ffx,curren_code,curren_name,unit_name,rate)
	values('156','RMB','人民币','元',1.00000);
insert into rate_lst(ffx,curren_code,curren_name,unit_name,rate)
	values('344','HKD','港币','元',1.05000);
insert into rate_lst(ffx,curren_code,curren_name,unit_name,rate)
	values('998','USD','美元','元',8.27000);

--10.根据实际情况加入店自编码类型
delete from weight_code;
--店内码
insert into weight_code
	values('2001vvvvvv00c');
--色码	
insert into weight_code
	values('21vvvvvvddddc');
--称重码	
insert into weight_code
	values('22xxxxxwwwwwc');
--金额码	
insert into weight_code
	values('23xxxxxmmmmmc');
--组合（捆绑）销售条码	
insert into weight_code
	values('26aaaaaa0000c');
--特价码	
insert into weight_code
	values('29sssssss000c');
	

--11.加入银行卡（信用卡）类型
delete from cardlist ;
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0101','111111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0102','111222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0201','222111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0202','222222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0301','333111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0302','333222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0401','444111',6,18,3,'01');	
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0402','444222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0501','555111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0502','555222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0601','666111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0602','666222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0701','777111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0702','777222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0801','888111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0802','888222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0901','999111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('0902','999222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1001','100111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1002','100222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1101','110111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1102','110222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1201','120111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1202','120222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1301','130111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1302','130222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1401','140111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1402','140222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1501','150111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1502','150222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1601','160111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1602','160222',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1701','170111',6,18,3,'01');
insert into cardlist(cardtype,cardtag,taglen,cardlen,bpos,fmtcode)
	values('1702','170222',6,18,3,'01');	


--12.加入银行卡（信用卡）注释表
------------------------modify by liyun 20020913-----------------------------
delete from cardnote where 1=1
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('A','0101','中国银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('A','0102','中国银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('B','0201','建设银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('B','0202','建设银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('C','0301','工商银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('C','0302','工商银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('D','0401','农业银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('D','0402','农业银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('E','0501','招商银行','RMB','信用卡',null,null,null,null);
--Modify by ZhengTianWei 20020905 银行名字写错了 -------
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('E','0502','招商银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('F','0601','交通银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('F','0602','交通银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('G','0701','民生银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('G','0702','民生银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('H','0801','光大银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('H','0802','光大银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('I','0901','信用社','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('I','0902','信用社','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('J','1001','商业银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('J','1002','商业银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('K','1101','广发银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('K','1102','广发银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('L','1201','发展银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('L','1202','发展银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('M','1301','华厦银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('M','1302','华厦银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('N','1401','中信银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('N','1402','中信银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('O','1501','上海浦发','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('O','1502','上海浦发','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('P','1601','兴业银行','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('P','1602','兴业银行','RMB','借记卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('P','1701','邮政卡','RMB','信用卡',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('P','1702','邮政卡','RMB','借记卡',null,null,null,null);
go


--13.插入POS机扩展配置参数表
delete from pos_para;
insert into pos_para(para,val,notes)
   values('PORT','4002','backpos的守护端口号');	--1
insert into pos_para(para,val,notes)
   values('PRICE_PORT','4001','BACKPRICE的守护端口号');--2
insert into pos_para( para,val,notes)
   values('MEM_PORT','4004','会员卡端口号');--4
insert into pos_para( para,val,notes)
   values('TX_FLAG','OFF','特殊显示开关');--5
insert into pos_para( para,val,notes)
   values('PRICE_FLAG','ON','基本码商品是否允许输入单价');--6
insert into pos_para( para,val,notes) 
   values('MONEYCODE_UNIT','100','金额码中金额单位');--7
insert into pos_para( para,val,notes) 
   values('AUTH_VOIDITEM','ON','更正需要授权');   --9
insert into pos_para(para,val,notes)
   values('MAXCASH','1000000','每单最大允许销售额');--10
insert into pos_para(para,val,notes)
   values('MAXITEM','70','每单最大允许条目数');--11
insert into pos_para(para,val,notes)
   values('MAXAMOUNT','10000000','每笔最大允许商品数量');--12
insert into pos_para(para,val,notes)
   values('MAXVALUE','1000000','每笔最大允许商品价值');--13
insert into pos_para(para,val,notes)
   values('VGBOTTOM','10','储值卡使用金额底限');--14
insert into pos_para(para,val,notes)
   values('POSTYPE','IBM','POS机类型');--16
insert into pos_para(para,val,notes)
   values('WEL_INFO','CRC SHOP','小票抬头欢迎词');--17
insert into pos_para(para,val,notes)
   values('HD1_INFO','融通公司店','小票打印头1:长度不超过13个汉字或26个字母');--18
insert into pos_para(para,val,notes)
   values('HD2_INFO','WELCOME Royalstone SHOP','小票打印头2');--19
insert into pos_para(para,val,notes)
   values('TL1_INFO','欢迎再次光临--服务电话:8888888','小票打印尾1');--20
insert into pos_para(para,val,notes)
   values('TL2_INFO','请保留电脑小票，作为退换货凭证','小票打印尾2'); --21
insert into pos_para(para,val,notes)
   values('TL3_INFO',' ','小票打印尾3');--22
insert into pos_para(para,val,notes)
   values('HANGMAX','2','最大允许挂单数');--23
insert into pos_para(para,val,notes)
   values('AMOUNT1','1','每单联机交易后可上送的脱机流水单数');--24
insert into pos_para(para,val,notes)
   values('AMOUNT2','1','每次空闲时可发送脱机流水的单数');--25
insert into pos_para(para,val,notes)
   values('TIMES','2','隔多少秒空闲时间可发送脱机流水单');--26
insert into   pos_para (para,val,notes) 
	values('VFD_TYPE','ORSVFD','POS机顾客显示屏类型?');--29
insert into   pos_para (para,val,notes) 
	values('CD_TYPE','ORSDRAW','POS机钱箱类型?');--30
insert into   pos_para (para,val,notes)
	values('PRN_TYPE','ORSPR4','POS机打印机类型?');--31
insert into   pos_para (para,val,notes)
	values('VFD_COM','COM4','顾客显示屏串口号');--32
insert into   pos_para (para,val,notes)
	values('CUTLINES','2','POS打印机的切纸位置');--33
insert into   pos_para (para,val,notes)
	values('AUTH_LASTPRINT','OFF','重打上一单是否需要授权');--34
insert into   pos_para (para,val,notes)
	values('AUTH_BLANKTRAN','ON','整单取消是否需要授权');--35
insert into pos_para( para,val,notes) 
  	values('CLEAR_POS','ON','收银员是否可以打印清机单');--36
insert into pos_para( para,val,notes)
   	values('INTERVAL','7','单机资料下载周期');--38
insert into   pos_para (para,val,notes)
	values('MEMCARD_VERIFY','ON','会员卡是否需要校验?');--53
insert into   pos_para (para,val,notes)
	values('LEN_MEMBER','13','会员卡长度?');--54
insert into pos_para(para,val,notes)
   values('GOODSNO_VERIFY','ON','ANSI13销售条码是否需要校验？');--57
insert into pos_para(para,val,notes)
   values('PRINT_ONLINE','ON','是否实时打印?');--57

insert into pos_para(para,val,notes)
   values('CARDHOST_IP','168.161.1.1','卡通信主机IP地址');
   
insert into pos_para(para,val,notes)
   values('CARDBAK_IP','168.161.1.1','卡通信备份IP地址');
   
insert into pos_para(para,val,notes)
   values('SAVE_PORT','4006','储值卡分店守护端口号');

insert into pos_para(para,val,notes)
   values('AUTH_SECONDNET','ON','启用备份网络是否需要授权');

insert into pos_para(para,val,notes)
   values('PROMACCU_FLAG','ON','促销折让商品是否积分?');

insert into pos_para(para,val,notes)
   values('AUTODIAL','ON','提货卡是否预拔号?');

insert into pos_para (para,val,notes) 
   values('IF_USE_VIP','OFF','是否使用数据库对会员卡进行校验？');

insert into pos_para (para,val,notes) 
   values( 'IF_CUT_ZERO','OFF','是否对条码前缀进行去零操作？');

insert into pos_para (para,val,notes) 
	values('EMPTY_OPEN_DRAW','OFF','是否允许空单开钱箱?');

insert into  pos_para (para,val,notes)
  values ('CHK_CLOSE_DRAW','OFF','是否检查钱箱关闭状态[OFF]');

INSERT INTO pos_para (para,val,notes)
   values('ROUNDING_SCHEMA','OFF','称重商品取位：从分->角')

insert into  pos_para (para,val,notes)
  values ('DISCROUND_SCHEMA','ON','折让金额是否舍分');

insert into pos_para (para,val,notes)
	values('RADIX_TYPE','ON','前台是否需要输入小数点？');

insert into  pos_para (para,val,notes)
  values ('TALROUND_SCHEMA','OFF','总收银额是否直接去分');

insert into  pos_para (para,val,notes)
  values ('POINT_MIN_MONEY','0','积分下线金额');

insert into  pos_para (para,val,notes)
  values ('POINT_ROUNDING','OFF','积分是否四舍五入');

insert into  pos_para (para,val,notes)
  values ('PRINT_SUMPOINT','OFF','是否打印上日累计积分');

insert into  pos_para (para,val,notes)
  values ('PRINT_TALCOUNT','OFF','是否要打印商品数量合计');


insert into  pos_para (para,val,notes)
  values ('CARD_NAME','提货卡','提货卡名称设置');



--14.插入版本控制表
insert into edition_ctl values('20000101','000000',0,0,0,getdate());

--15.插入POS分组表
delete from posgroup;
insert into posgroup values('BASIC','基本销售组');
insert into posgroup values('OLIVT','OLIVT专用组');
insert into posgroup values('TRAIN','培训测试组');

--16.插入POS扩展表
insert into pos_ini (posgroup,para,val) select 'BASIC',para,val from pos_para;
insert into pos_ini (posgroup,para,val) select 'OLIVT',para,val from pos_para;
insert into pos_ini (posgroup,para,val) select 'TRAIN',para,val from pos_para;


--18.前台程序版本维护
delete from prog_lst ;
insert into  prog_lst(progname, prognote,progver,progoff,
		      progdate,ftpserver,ftpuser,ftppass)
	     values('pos.exe','POS前台收银程序','310001',0,
	     	     getdate(),'ftpserver','ftpuser','ftppass');

insert into  prog_lst(progname, prognote,progver,progoff,
		      progdate,ftpserver,ftpuser,ftppass)
	     values('loaddata.exe','POS前台下载程序','310001',0,
	     	     getdate(),'ftpserver','ftpuser','ftppass');
	     	     
insert into  prog_lst(progname, prognote,progver,progoff,
		      progdate,ftpserver,ftpuser,ftppass)
	     values('frdrv.tar','POS前台设备驱动程序','310001',0,
	     	     getdate(),'ftpserver','ftpuser','ftppass');


--19.日结控制表与店配置，注意修改对应参数
delete from config ;
insert into config (systemid,name,value) Values (0,'本店号','A00B');
insert into config (systemid,name,value,note)
	values(31,'子系统名称','前台监控管理子系统','');			

delete from rjcontrol;
insert into rjcontrol(sdate,shopid,stepname,notes,statusflag)
	 values(getdate(),'A00B','rj_start','日结开始',1);
insert into rjcontrol(sdate,shopid,stepname,notes,statusflag) 
	values(getdate(),'A00B','rj_working','日结进行中',1);
insert into rjcontrol(sdate,shopid,stepname,notes,statusflag)
	values(getdate(),'A00B','rj_over','日结完成',1);
update rjcontrol set sdate=sdate-1,
	shopid=(select ltrim(value) from config where name='本店号');

--下面是POS后台（进销存、监控等）的初始化信息
--20.前台监控参数：事件性质	

insert into EventLevel values(1,'系统错误');
insert into EventLevel values(2,'系统警告');
insert into EventLevel values(3,'系统信息');
insert into EventLevel values(4,'错误');
insert into EventLevel values(5,'警告');
insert into EventLevel values(6,'信息');

--21.前台监控参数：事件代码集合	

insert into EventCode(EventID,EventLevelID,Note) values(30001,3,'操作员连接系统');
insert into EventCode(EventID,EventLevelID,Note) values(30002,3,'操作员退出系统');
insert into EventCode(EventID,EventLevelID,Note) values(30003,3,'修改连接表[Connect]');
insert into EventCode(EventID,EventLevelID,Note) values(30004,3,'修改操作员注册表[Login]');
insert into EventCode(EventID,EventLevelID,Note) values(30005,3,'修改角色表[Part]');
insert into EventCode(EventID,EventLevelID,Note) values(30006,3,'修改工作组表[WorkGroup]');
insert into EventCode(EventID,EventLevelID,Note) values(30007,3,'修改角色系统对照表[PartSystem]');
insert into EventCode(EventID,EventLevelID,Note) values(30008,3,'修改操作员角色对照表[PartMember]');
insert into EventCode(EventID,EventLevelID,Note) values(30009,3,'修改角色工作组对照表[PartGroup]');
insert into EventCode(EventID,EventLevelID,Note) values(30010,3,'修改工作组权限对照表[GroupRight]');
insert into EventCode(EventID,EventLevelID,Note) values(30011,3,'修改系统配置表[Config]');
insert into EventCode(EventID,EventLevelID,Note) values(30012,3,'修改口令');
insert into EventCode(EventID,EventLevelID,Note) values(30013,3,'保存系统日志');
insert into EventCode(EventID,EventLevelID,Note) values(30014,3,'清空系统日志');
insert into EventCode(EventID,EventLevelID,Note) values(30015,3,'非法工作站注册');
insert into EventCode(EventID,EventLevelID,Note) values(30016,3,'程序更新');
insert into EventCode(EventID,EventLevelID,Note) values(30017,3,'修改部门信息[Office]');
insert into EventCode(EventID,EventLevelID,Note) values(30018,3,'修改工作站信息[Workstation]');
insert into EventCode(EventID,EventLevelID,Note) values(31001,3,'解锁POS机');
insert into EventCode(EventID,EventLevelID,Note) values(31002,3,'清空POS机日志');

--22.联名卡参数

insert into vgbkcard(cardtype,cardtag,taglen,cdnolen,cdnodisp,cardname,cardbank,workfile)
  values('0001','1234567890',1,1,1,'联名卡','联名银行','workfile');


-------function_lst--------------------------------------
 insert into function_lst(keymap_value,fun,hzfun) values(8,'FunBks','退格');
 insert into function_lst(keymap_value,fun,hzfun) values(10,'FunEnter','确认');
 insert into function_lst(keymap_value,fun,hzfun) values(34,'FunVipCard','会员卡');
 insert into function_lst(keymap_value,fun,hzfun) values(36,'FunPrint','实时打印小票开关');
 insert into function_lst(keymap_value,fun,hzfun) values(37,'FunVgBank','联名卡');
 insert into function_lst(keymap_value,fun,hzfun) values(38,'FunBlankTran','整单取消');
 insert into function_lst(keymap_value,fun,hzfun) values(40,'Funwaiter','营业员输入');
 insert into function_lst(keymap_value,fun,hzfun) values(41,'FunDiscMoney','金额折扣');
 insert into function_lst(keymap_value,fun,hzfun) values(43,'FunLastPrint','重打上一单');
 insert into function_lst(keymap_value,fun,hzfun) values(46,'Fun_PERIOD','.');
 insert into function_lst(keymap_value,fun,hzfun) values(48,'Fun_0','0');
 insert into function_lst(keymap_value,fun,hzfun) values(49,'Fun_1','1');
 insert into function_lst(keymap_value,fun,hzfun) values(50,'Fun_2','2');
 insert into function_lst(keymap_value,fun,hzfun) values(51,'Fun_3','3');
 insert into function_lst(keymap_value,fun,hzfun) values(52,'Fun_4','4');
 insert into function_lst(keymap_value,fun,hzfun) values(53,'Fun_5','5');
 insert into function_lst(keymap_value,fun,hzfun) values(54,'Fun_6','6');
 insert into function_lst(keymap_value,fun,hzfun) values(55,'Fun_7','7');
 insert into function_lst(keymap_value,fun,hzfun) values(56,'Fun_8','8');
 insert into function_lst(keymap_value,fun,hzfun) values(57,'Fun_9','9');
 insert into function_lst(keymap_value,fun,hzfun) values(58,'FunOrder','预订');
 insert into function_lst(keymap_value,fun,hzfun) values(60,'FunIncrCash','入款');
 insert into function_lst(keymap_value,fun,hzfun) values(62,'FunDecrCash','出款');
 insert into function_lst(keymap_value,fun,hzfun) values(63,'FunRetPay','退款');
 insert into function_lst(keymap_value,fun,hzfun) values(66,'FunAmount','数量');
 insert into function_lst(keymap_value,fun,hzfun) values(67,'FunVoid','更正');
 insert into function_lst(keymap_value,fun,hzfun) values(68,'FunStock','代币券');
 insert into function_lst(keymap_value,fun,hzfun) values(69,'FunCheque','支票');
 insert into function_lst(keymap_value,fun,hzfun) values(70,'FunReturn','退货');
 insert into function_lst(keymap_value,fun,hzfun) values(71,'FunCash','现金结算');
 insert into function_lst(keymap_value,fun,hzfun) values(72,'FunHold','挂单');
 insert into function_lst(keymap_value,fun,hzfun) values(73,'FunQuickVoid','即更');
 insert into function_lst(keymap_value,fun,hzfun) values(74,'FunVanguard','提货卡');
 insert into function_lst(keymap_value,fun,hzfun) values(75,'FunCredit','银行卡');
 insert into function_lst(keymap_value,fun,hzfun) values(76,'FunDeli','送货');
 insert into function_lst(keymap_value,fun,hzfun) values(77,'FunAltPrice','变价');
 insert into function_lst(keymap_value,fun,hzfun) values(78,'FunNewP','改密码');
 insert into function_lst(keymap_value,fun,hzfun) values(79,'FunTotal','合计');
 insert into function_lst(keymap_value,fun,hzfun) values(80,'FunSubtotal','小计');
 insert into function_lst(keymap_value,fun,hzfun) values(81,'FunStoG','超市柜组转换');
 insert into function_lst(keymap_value,fun,hzfun) values(82,'FunEsc','退出');
 insert into function_lst(keymap_value,fun,hzfun) values(83,'FunLoanCard','挂帐卡');
 insert into function_lst(keymap_value,fun,hzfun) values(84,'FunLock','加锁/解锁');
 insert into function_lst(keymap_value,fun,hzfun) values(85,'FunHKD','港币');
 insert into function_lst(keymap_value,fun,hzfun) values(86,'FunRMB','人民币');
 insert into function_lst(keymap_value,fun,hzfun) values(87,'FunUSD','美圆');
 insert into function_lst(keymap_value,fun,hzfun) values(88,'FunClear','清除');
 insert into function_lst(keymap_value,fun,hzfun) values(89,'FunCancel','放弃');
 insert into function_lst(keymap_value,fun,hzfun) values(90,'FunGroup','柜组');
 insert into function_lst(keymap_value,fun,hzfun) values(94,'FunGreenCard','绿卡处理');
 insert into function_lst(keymap_value,fun,hzfun) values(95,'FunRoup','拍卖');
 insert into function_lst(keymap_value,fun,hzfun) values(96,'FunShift','班结');
 insert into function_lst(keymap_value,fun,hzfun) values(97,'FunFlee','逃车');
 insert into function_lst(keymap_value,fun,hzfun) values(98,'FunSample','抽样检查');
 insert into function_lst(keymap_value,fun,hzfun) values(99,'FunOilTest','油机测试');
 insert into function_lst(keymap_value,fun,hzfun) values(123,'FunDiscount','单项折扣');
 insert into function_lst(keymap_value,fun,hzfun) values(124,'FunOffLine','联/单机切换');
 insert into function_lst(keymap_value,fun,hzfun) values(125,'FunDiscTotal','总额折扣');
 insert into function_lst(keymap_value,fun,hzfun) values(126,'Fun_SHOWCASHBOX','收款检查');
 insert into function_lst(keymap_value,fun,hzfun) values(200,'FunBiZero','00');
  
-------keybd_cfg--------------------------------------
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',27,82);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',33,78);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',34,84);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',35,67);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',36,73);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',44,60);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',45,96);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',65,69);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',66,85);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',67,71);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',70,88);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',71,38);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',72,89);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',73,43);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',74,70);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',75,34);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',76,200);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',77,96);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',78,99);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',79,84);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',80,77);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',81,68);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',82,66);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',83,126);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',84,72);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',85,73);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',86,74);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',87,75);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',89,67);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',90,83);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',91,98);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',92,78);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',93,97);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',96,48);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',97,49);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',98,50);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',99,51);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',100,52);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',101,53);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',102,54);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',103,55);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',104,56);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',105,57);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',109,96);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',112,78);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',113,84);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',114,67);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',115,73);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',116,72);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',117,38);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',118,89);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',119,70);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',120,43);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',155,38);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',214,84);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',219,84);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('B01',222,78);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',27,82);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',32,88);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',33,83);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',43,60);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',45,62);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',65,8);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',66,8);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',67,66);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',68,38);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',69,67);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',70,73);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',71,88);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',72,88);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',73,66);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',74,72);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',75,89);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',76,70);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',77,10);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',78,126);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',79,68);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',80,69);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',81,75);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',82,48);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',83,48);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',85,10);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',86,10);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',87,71);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',88,85);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',89,77);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',90,71);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',96,46);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',97,49);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',98,50);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',99,51);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',100,52);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',101,53);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',102,54);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',103,55);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',104,56);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',105,57);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',107,60);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',109,62);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',110,200);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',112,96);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',116,78);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',117,99);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',118,98);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',119,97);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',120,43);
 insert into keybd_cfg(keybd_type,key_value,keymap_value) values('T01',121,84);
  
-------keybd_layout--------------------------------------
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',1,1,27);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',1,2,36);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',1,3,37);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',1,4,35);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',1,5,107);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',2,1,8);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',2,2,38);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',2,3,32);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',2,4,40);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',2,5,109);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',3,1,112);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',3,2,33);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',3,3,39);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',3,4,34);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',3,5,96);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',4,1,113);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',4,2,103);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',4,3,100);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',4,4,97);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',4,5,82);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',5,1,114);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',5,2,104);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',5,3,101);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',5,4,98);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',5,5,83);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',6,1,115);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',6,2,105);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',6,3,102);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',6,4,99);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',6,5,110);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',7,1,116);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',7,2,65);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',7,3,71);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',7,4,13);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',7,5,85);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',8,1,117);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',8,2,66);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',8,3,72);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',8,4,77);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',8,5,86);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',9,1,118);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',9,2,67);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',9,3,73);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',9,4,90);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',9,5,87);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',10,1,119);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',10,2,68);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',10,3,74);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',10,4,79);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',10,5,88);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',11,1,120);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',11,2,69);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',11,3,75);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',11,4,80);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',11,5,89);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',12,1,121);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',12,2,70);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',12,3,76);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',12,4,81);
 insert into keybd_layout(keybd_class,x,y,key_value) values('TA',12,5,78);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',1,1,27);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',1,2,81);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',1,3,65);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',1,4,66);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',1,5,17);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',2,1,109);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',2,2,87);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',2,3,83);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',2,4,90);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',2,5,18);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',3,1,222);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',3,2,69);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',3,3,68);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',3,4,88);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',3,5,46);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',4,1,221);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',4,2,55);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',4,3,52);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',4,4,49);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',4,5,48);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',5,1,77);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',5,2,56);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',5,3,53);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',5,4,50);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',5,5,48);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',6,1,44);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',6,2,57);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',6,3,54);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',6,4,51);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',6,5,254);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',7,1,78);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',7,2,8);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',7,3,70);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',7,4,13);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',7,5,13);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',8,1,91);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',8,2,8);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',8,3,70);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',8,4,13);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',8,5,13);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',9,1,93);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',9,2,82);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',9,3,82);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',9,4,67);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',9,5,67);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',10,1,73);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',10,2,84);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',10,3,71);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',10,4,75);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',10,5,86);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',11,1,79);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',11,2,89);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',11,3,72);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',11,4,38);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',11,5,40);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',12,1,80);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',12,2,85);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',12,3,74);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',12,4,76);
 insert into keybd_layout(keybd_class,x,y,key_value) values('WIN',12,5,39);
  
-------keybd_lst--------------------------------------
 insert into keybd_lst(keybd_type,keybd_class,keybd_desp,note) values('B01','WIN','','');
 insert into keybd_lst(keybd_type,keybd_class,keybd_desp,note) values('T01','TA','','');
  
-------keyname--------------------------------------
 insert into keyname(keyvalue,keyname) values(8,'回退');
 insert into keyname(keyvalue,keyname) values(9,'TAB');
 insert into keyname(keyvalue,keyname) values(13,'确认');
 insert into keyname(keyvalue,keyname) values(16,'Shift');
 insert into keyname(keyvalue,keyname) values(17,'Ctrl');
 insert into keyname(keyvalue,keyname) values(18,'Alt');
 insert into keyname(keyvalue,keyname) values(20,'Caps');
 insert into keyname(keyvalue,keyname) values(27,'ESC');
 insert into keyname(keyvalue,keyname) values(32,'空格');
 insert into keyname(keyvalue,keyname) values(33,'PgUp');
 insert into keyname(keyvalue,keyname) values(34,'PgDn');
 insert into keyname(keyvalue,keyname) values(35,'End');
 insert into keyname(keyvalue,keyname) values(36,'Home');
 insert into keyname(keyvalue,keyname) values(37,'←');
 insert into keyname(keyvalue,keyname) values(38,'↑');
 insert into keyname(keyvalue,keyname) values(39,'→');
 insert into keyname(keyvalue,keyname) values(40,'↓');
 insert into keyname(keyvalue,keyname) values(41,')');
 insert into keyname(keyvalue,keyname) values(42,'*');
 insert into keyname(keyvalue,keyname) values(44,',');
 insert into keyname(keyvalue,keyname) values(46,'.');
 insert into keyname(keyvalue,keyname) values(47,'/');
 insert into keyname(keyvalue,keyname) values(48,'0');
 insert into keyname(keyvalue,keyname) values(49,'1');
 insert into keyname(keyvalue,keyname) values(50,'2');
 insert into keyname(keyvalue,keyname) values(51,'3');
 insert into keyname(keyvalue,keyname) values(52,'4');
 insert into keyname(keyvalue,keyname) values(53,'5');
 insert into keyname(keyvalue,keyname) values(54,'6');
 insert into keyname(keyvalue,keyname) values(55,'7');
 insert into keyname(keyvalue,keyname) values(56,'8');
 insert into keyname(keyvalue,keyname) values(57,'9');
 insert into keyname(keyvalue,keyname) values(58,':');
 insert into keyname(keyvalue,keyname) values(59,';');
 insert into keyname(keyvalue,keyname) values(60,'<');
 insert into keyname(keyvalue,keyname) values(61,'=');
 insert into keyname(keyvalue,keyname) values(62,'>');
 insert into keyname(keyvalue,keyname) values(63,'?');
 insert into keyname(keyvalue,keyname) values(64,'@');
 insert into keyname(keyvalue,keyname) values(65,'A');
 insert into keyname(keyvalue,keyname) values(66,'B');
 insert into keyname(keyvalue,keyname) values(67,'C');
 insert into keyname(keyvalue,keyname) values(68,'D');
 insert into keyname(keyvalue,keyname) values(69,'E');
 insert into keyname(keyvalue,keyname) values(70,'F');
 insert into keyname(keyvalue,keyname) values(71,'G');
 insert into keyname(keyvalue,keyname) values(72,'H');
 insert into keyname(keyvalue,keyname) values(73,'I');
 insert into keyname(keyvalue,keyname) values(74,'J');
 insert into keyname(keyvalue,keyname) values(75,'K');
 insert into keyname(keyvalue,keyname) values(76,'L');
 insert into keyname(keyvalue,keyname) values(77,'M');
 insert into keyname(keyvalue,keyname) values(78,'N');
 insert into keyname(keyvalue,keyname) values(79,'O');
 insert into keyname(keyvalue,keyname) values(80,'P');
 insert into keyname(keyvalue,keyname) values(81,'Q');
 insert into keyname(keyvalue,keyname) values(82,'R');
 insert into keyname(keyvalue,keyname) values(83,'S');
 insert into keyname(keyvalue,keyname) values(84,'T');
 insert into keyname(keyvalue,keyname) values(85,'U');
 insert into keyname(keyvalue,keyname) values(86,'V');
 insert into keyname(keyvalue,keyname) values(87,'W');
 insert into keyname(keyvalue,keyname) values(88,'X');
 insert into keyname(keyvalue,keyname) values(89,'Y');
 insert into keyname(keyvalue,keyname) values(90,'Z');
 insert into keyname(keyvalue,keyname) values(91,'[');
 insert into keyname(keyvalue,keyname) values(92,'\');
 insert into keyname(keyvalue,keyname) values(93,']');
 insert into keyname(keyvalue,keyname) values(94,'^');
 insert into keyname(keyvalue,keyname) values(95,'_');
 insert into keyname(keyvalue,keyname) values(96,'NUM0');
 insert into keyname(keyvalue,keyname) values(97,'NUM1');
 insert into keyname(keyvalue,keyname) values(98,'NUM2');
 insert into keyname(keyvalue,keyname) values(99,'NUM3');
 insert into keyname(keyvalue,keyname) values(100,'NUM4');
 insert into keyname(keyvalue,keyname) values(101,'NUM5');
 insert into keyname(keyvalue,keyname) values(102,'NUM6');
 insert into keyname(keyvalue,keyname) values(103,'NUM7');
 insert into keyname(keyvalue,keyname) values(104,'NUM8');
 insert into keyname(keyvalue,keyname) values(105,'NUM9');
 insert into keyname(keyvalue,keyname) values(107,'+');
 insert into keyname(keyvalue,keyname) values(109,'-');
 insert into keyname(keyvalue,keyname) values(110,'PERIOD');
 insert into keyname(keyvalue,keyname) values(112,'F1');
 insert into keyname(keyvalue,keyname) values(113,'F2');
 insert into keyname(keyvalue,keyname) values(114,'F3');
 insert into keyname(keyvalue,keyname) values(115,'F4');
 insert into keyname(keyvalue,keyname) values(116,'F5');
 insert into keyname(keyvalue,keyname) values(117,'F6');
 insert into keyname(keyvalue,keyname) values(118,'F7');
 insert into keyname(keyvalue,keyname) values(119,'F8');
 insert into keyname(keyvalue,keyname) values(120,'F9');
 insert into keyname(keyvalue,keyname) values(121,'F10');
 insert into keyname(keyvalue,keyname) values(122,'F11');
 insert into keyname(keyvalue,keyname) values(123,'F12');
 insert into keyname(keyvalue,keyname) values(124,'|');
 insert into keyname(keyvalue,keyname) values(125,'}');
 insert into keyname(keyvalue,keyname) values(127,'Del');
 insert into keyname(keyvalue,keyname) values(155,'Ins');
 insert into keyname(keyvalue,keyname) values(213,'!');
 insert into keyname(keyvalue,keyname) values(214,'"');
 insert into keyname(keyvalue,keyname) values(215,'#');
 insert into keyname(keyvalue,keyname) values(216,'$');
 insert into keyname(keyvalue,keyname) values(217,'(');
 insert into keyname(keyvalue,keyname) values(218,'%');
 insert into keyname(keyvalue,keyname) values(220,'&');
 insert into keyname(keyvalue,keyname) values(221,'~');
 insert into keyname(keyvalue,keyname) values(222,'''');
 insert into keyname(keyvalue,keyname) values(254,'00');
 insert into keyname(keyvalue,keyname) values(255,'Fn');
  
-------keybd_class--------------------------------------
 insert into keybd_class(classname,note) values('TA','TA61键盘图');
 insert into keybd_class(classname,note) values('WIN','WINCOR NIXDORF');
  
-------hotkey_cfg--------------------------------------
 insert into hotkey_cfg(keybd_type,key_value,plu) values('B01',68,'000005');
 insert into hotkey_cfg(keybd_type,key_value,plu) values('B01',69,'000002');
 insert into hotkey_cfg(keybd_type,key_value,plu) values('B01',80,'000007');
 insert into hotkey_cfg(keybd_type,key_value,plu) values('B01',88,'000001');

-------OilDept--------------------------------------
INSERT into OilDept ( DeptID ) VALUES ( 30101 );
INSERT into OilDept ( DeptID ) VALUES ( 30201 );
INSERT into OilDept ( DeptID ) VALUES ( 30301 );
INSERT into OilDept ( DeptID ) VALUES ( 30401 );
INSERT into OilDept ( DeptID ) VALUES ( 30501 );
INSERT into OilDept ( DeptID ) VALUES ( 30601 );
INSERT into OilDept ( DeptID ) VALUES ( 30701 );
INSERT into OilDept ( DeptID ) VALUES ( 30801 );


---------Power_lst--------------------------------------------
INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 1 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 38 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 41 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 43 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 67 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 70 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 73 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 77 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 97 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 98 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 99 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 123 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 124 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 1 , 125 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 1 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 38 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 41 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 43 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 67 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 70 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 73 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 77 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 97 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 98 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 99 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 123 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 124 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 2 , 125 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 3 , 1 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 3 , 41 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 3 , 43 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 3 , 67 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 3 , 73 , '-       ' );
 INSERT [power_lst] ( [levelID] , [power_id] , [name] ) VALUES ( 3 , 77 , '-       ' );

