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
--APPLE1.0�汾����ʼ��APPLEPOS����
--������������ 2002.07.12
--����޸ģ������� 2002.08.27
--�޸ļ�¼��
--    ������ 2002.08.27 �Ѽ��̵���ز������ø�Ϊbcp���룬��ֱ������init.bat����.
--��ֲ��Apple 1.0 �Խ� 2003-5-23
-- �޸� : ʩ���� 20030714 ���ӳ�ʼ������
-- ע��: �޸� config��rjcontrol  ���������,Ĭ�� A00B
-----------------------------------------------------------
--1.ѡ����ȷ�����ݿ�


--2.���������ʼ���ı�
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


--3.���Ӵ������Ͷ����
-------distitem_type--------------------------------------
--3.1	��Ʒ��������
 insert into distitem_type(fieldname,type,desp) values('disc_type','c','�ֹ����');
 insert into distitem_type(fieldname,type,desp) values('disc_type','d','С���ۿ�');
 insert into distitem_type(fieldname,type,desp) values('disc_type','g','�����ۿ�');
 insert into distitem_type(fieldname,type,desp) values('disc_type','h','��Ա��');
 insert into distitem_type(fieldname,type,desp) values('disc_type','J','�ֹ�����ۿ�');
 insert into distitem_type(fieldname,type,desp) values('disc_type','k','��Ա�ۿ�');
 insert into distitem_type(fieldname,type,desp) values('disc_type','M','�ֹ��ܶ��ۿ�');
 insert into distitem_type(fieldname,type,desp) values('disc_type','n','���ۿ�');
 insert into distitem_type(fieldname,type,desp) values('disc_type','p','����');
 insert into distitem_type(fieldname,type,desp) values('disc_type','t','ʱ���ۿ�');
 insert into distitem_type(fieldname,type,desp) values('disc_type','v','��Ʒ�ۿ�');
 insert into distitem_type(fieldname,type,desp) values('disc_type','Z','�ֹ������ۿ�');
 insert into distitem_type(fieldname,type,desp) values('disc_type','B','��������');
 insert into distitem_type(fieldname,type,desp) values('disc_type','s','��ϴ���');
--3.2	����(֧��)��ʽ
 insert into distitem_type(fieldname,type,desp) values('paytype','C','�ֽ�');
 insert into distitem_type(fieldname,type,desp) values('paytype','D','����ȯ');
 insert into distitem_type(fieldname,type,desp) values('paytype','F','�ӳ�');
 insert into distitem_type(fieldname,type,desp) values('paytype','K','֧Ʊ');
 insert into distitem_type(fieldname,type,desp) values('paytype','L','������');
 insert into distitem_type(fieldname,type,desp) values('paytype','R','���п�');
 insert into distitem_type(fieldname,type,desp) values('paytype','S','�������');
 insert into distitem_type(fieldname,type,desp) values('paytype','T','�ͻ�����');
 insert into distitem_type(fieldname,type,desp) values('paytype','V','�����');
--3.3	��Ʒ����ʱ�Ĵ����ֶ�
 insert into distitem_type(fieldname,type,desp) values('promtype','0','��ͨ');
 insert into distitem_type(fieldname,type,desp) values('promtype','1','�ؼ�');
 insert into distitem_type(fieldname,type,desp) values('promtype','2','����');

--3.4	��Ʒ��������
 insert into distitem_type(fieldname,type,desp) values('v_type','0','��ͨ');
 insert into distitem_type(fieldname,type,desp) values('v_type','2','����');
 insert into distitem_type(fieldname,type,desp) values('v_type','3','���');
 insert into distitem_type(fieldname,type,desp) values('v_type','8','����');
 insert into distitem_type(fieldname,type,desp) values('v_type','9','������');


--4.����ǰ̨Ȩ�ޱ�
 insert into right_lst(power_id,name) values(1,'��������');
 insert into right_lst(power_id,name) values(38,'����ȡ��');
 insert into right_lst(power_id,name) values(41,'����ۿ�');
 insert into right_lst(power_id,name) values(43,'�ش���һ��');
 insert into right_lst(power_id,name) values(67,'����');
 insert into right_lst(power_id,name) values(70,'�˻�');
 insert into right_lst(power_id,name) values(73,'����');
 insert into right_lst(power_id,name) values(77,'���');
 insert into right_lst(power_id,name) values(95,'����');
 insert into right_lst(power_id,name) values(97,'�ӳ�');
 insert into right_lst(power_id,name) values(98,'�������');
 insert into right_lst(power_id,name) values(99,'�ͻ�����');
 insert into right_lst(power_id,name) values(123,'��Ʒ�ۿ�');
 insert into right_lst(power_id,name) values(124,'ǿ�е���');
 insert into right_lst(power_id,name) values(125,'�ܶ��ۿ�');
 insert into right_lst(power_id,name) values(126,'Ǯ��״̬');


--9.�Ի��ʱ���г�ʼ��
delete from rate_lst;
insert into rate_lst(ffx,curren_code,curren_name,unit_name,rate)
	values('156','RMB','�����','Ԫ',1.00000);
insert into rate_lst(ffx,curren_code,curren_name,unit_name,rate)
	values('344','HKD','�۱�','Ԫ',1.05000);
insert into rate_lst(ffx,curren_code,curren_name,unit_name,rate)
	values('998','USD','��Ԫ','Ԫ',8.27000);

--10.����ʵ�����������Ա�������
delete from weight_code;
--������
insert into weight_code
	values('2001vvvvvv00c');
--ɫ��	
insert into weight_code
	values('21vvvvvvddddc');
--������	
insert into weight_code
	values('22xxxxxwwwwwc');
--�����	
insert into weight_code
	values('23xxxxxmmmmmc');
--��ϣ�������������	
insert into weight_code
	values('26aaaaaa0000c');
--�ؼ���	
insert into weight_code
	values('29sssssss000c');
	

--11.�������п������ÿ�������
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


--12.�������п������ÿ���ע�ͱ�
------------------------modify by liyun 20020913-----------------------------
delete from cardnote where 1=1
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('A','0101','�й�����','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('A','0102','�й�����','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('B','0201','��������','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('B','0202','��������','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('C','0301','��������','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('C','0302','��������','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('D','0401','ũҵ����','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('D','0402','ũҵ����','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('E','0501','��������','RMB','���ÿ�',null,null,null,null);
--Modify by ZhengTianWei 20020905 ��������д���� -------
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('E','0502','��������','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('F','0601','��ͨ����','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('F','0602','��ͨ����','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('G','0701','��������','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('G','0702','��������','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('H','0801','�������','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('H','0802','�������','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('I','0901','������','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('I','0902','������','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('J','1001','��ҵ����','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('J','1002','��ҵ����','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('K','1101','�㷢����','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('K','1102','�㷢����','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('L','1201','��չ����','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('L','1202','��չ����','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('M','1301','��������','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('M','1302','��������','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('N','1401','��������','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('N','1402','��������','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('O','1501','�Ϻ��ַ�','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('O','1502','�Ϻ��ַ�','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('P','1601','��ҵ����','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('P','1602','��ҵ����','RMB','��ǿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('P','1701','������','RMB','���ÿ�',null,null,null,null);
insert into cardnote(bank_id,cardtype,bank,money_code,cardnote,rate,note1,note2,note3)
	values('P','1702','������','RMB','��ǿ�',null,null,null,null);
go


--13.����POS����չ���ò�����
delete from pos_para;
insert into pos_para(para,val,notes)
   values('PORT','4002','backpos���ػ��˿ں�');	--1
insert into pos_para(para,val,notes)
   values('PRICE_PORT','4001','BACKPRICE���ػ��˿ں�');--2
insert into pos_para( para,val,notes)
   values('MEM_PORT','4004','��Ա���˿ں�');--4
insert into pos_para( para,val,notes)
   values('TX_FLAG','OFF','������ʾ����');--5
insert into pos_para( para,val,notes)
   values('PRICE_FLAG','ON','��������Ʒ�Ƿ��������뵥��');--6
insert into pos_para( para,val,notes) 
   values('MONEYCODE_UNIT','100','������н�λ');--7
insert into pos_para( para,val,notes) 
   values('AUTH_VOIDITEM','ON','������Ҫ��Ȩ');   --9
insert into pos_para(para,val,notes)
   values('MAXCASH','1000000','ÿ������������۶�');--10
insert into pos_para(para,val,notes)
   values('MAXITEM','70','ÿ�����������Ŀ��');--11
insert into pos_para(para,val,notes)
   values('MAXAMOUNT','10000000','ÿ�����������Ʒ����');--12
insert into pos_para(para,val,notes)
   values('MAXVALUE','1000000','ÿ�����������Ʒ��ֵ');--13
insert into pos_para(para,val,notes)
   values('VGBOTTOM','10','��ֵ��ʹ�ý�����');--14
insert into pos_para(para,val,notes)
   values('POSTYPE','IBM','POS������');--16
insert into pos_para(para,val,notes)
   values('WEL_INFO','CRC SHOP','СƱ̧ͷ��ӭ��');--17
insert into pos_para(para,val,notes)
   values('HD1_INFO','��ͨ��˾��','СƱ��ӡͷ1:���Ȳ�����13�����ֻ�26����ĸ');--18
insert into pos_para(para,val,notes)
   values('HD2_INFO','WELCOME Royalstone SHOP','СƱ��ӡͷ2');--19
insert into pos_para(para,val,notes)
   values('TL1_INFO','��ӭ�ٴι���--����绰:8888888','СƱ��ӡβ1');--20
insert into pos_para(para,val,notes)
   values('TL2_INFO','�뱣������СƱ����Ϊ�˻���ƾ֤','СƱ��ӡβ2'); --21
insert into pos_para(para,val,notes)
   values('TL3_INFO',' ','СƱ��ӡβ3');--22
insert into pos_para(para,val,notes)
   values('HANGMAX','2','�������ҵ���');--23
insert into pos_para(para,val,notes)
   values('AMOUNT1','1','ÿ���������׺�����͵��ѻ���ˮ����');--24
insert into pos_para(para,val,notes)
   values('AMOUNT2','1','ÿ�ο���ʱ�ɷ����ѻ���ˮ�ĵ���');--25
insert into pos_para(para,val,notes)
   values('TIMES','2','�����������ʱ��ɷ����ѻ���ˮ��');--26
insert into   pos_para (para,val,notes) 
	values('VFD_TYPE','ORSVFD','POS���˿���ʾ������?');--29
insert into   pos_para (para,val,notes) 
	values('CD_TYPE','ORSDRAW','POS��Ǯ������?');--30
insert into   pos_para (para,val,notes)
	values('PRN_TYPE','ORSPR4','POS����ӡ������?');--31
insert into   pos_para (para,val,notes)
	values('VFD_COM','COM4','�˿���ʾ�����ں�');--32
insert into   pos_para (para,val,notes)
	values('CUTLINES','2','POS��ӡ������ֽλ��');--33
insert into   pos_para (para,val,notes)
	values('AUTH_LASTPRINT','OFF','�ش���һ���Ƿ���Ҫ��Ȩ');--34
insert into   pos_para (para,val,notes)
	values('AUTH_BLANKTRAN','ON','����ȡ���Ƿ���Ҫ��Ȩ');--35
insert into pos_para( para,val,notes) 
  	values('CLEAR_POS','ON','����Ա�Ƿ���Դ�ӡ�����');--36
insert into pos_para( para,val,notes)
   	values('INTERVAL','7','����������������');--38
insert into   pos_para (para,val,notes)
	values('MEMCARD_VERIFY','ON','��Ա���Ƿ���ҪУ��?');--53
insert into   pos_para (para,val,notes)
	values('LEN_MEMBER','13','��Ա������?');--54
insert into pos_para(para,val,notes)
   values('GOODSNO_VERIFY','ON','ANSI13���������Ƿ���ҪУ�飿');--57
insert into pos_para(para,val,notes)
   values('PRINT_ONLINE','ON','�Ƿ�ʵʱ��ӡ?');--57

insert into pos_para(para,val,notes)
   values('CARDHOST_IP','168.161.1.1','��ͨ������IP��ַ');
   
insert into pos_para(para,val,notes)
   values('CARDBAK_IP','168.161.1.1','��ͨ�ű���IP��ַ');
   
insert into pos_para(para,val,notes)
   values('SAVE_PORT','4006','��ֵ���ֵ��ػ��˿ں�');

insert into pos_para(para,val,notes)
   values('AUTH_SECONDNET','ON','���ñ��������Ƿ���Ҫ��Ȩ');

insert into pos_para(para,val,notes)
   values('PROMACCU_FLAG','ON','����������Ʒ�Ƿ����?');

insert into pos_para(para,val,notes)
   values('AUTODIAL','ON','������Ƿ�Ԥ�κ�?');

insert into pos_para (para,val,notes) 
   values('IF_USE_VIP','OFF','�Ƿ�ʹ�����ݿ�Ի�Ա������У�飿');

insert into pos_para (para,val,notes) 
   values( 'IF_CUT_ZERO','OFF','�Ƿ������ǰ׺����ȥ�������');

insert into pos_para (para,val,notes) 
	values('EMPTY_OPEN_DRAW','OFF','�Ƿ�����յ���Ǯ��?');

insert into  pos_para (para,val,notes)
  values ('CHK_CLOSE_DRAW','OFF','�Ƿ���Ǯ��ر�״̬[OFF]');

INSERT INTO pos_para (para,val,notes)
   values('ROUNDING_SCHEMA','OFF','������Ʒȡλ���ӷ�->��')

insert into  pos_para (para,val,notes)
  values ('DISCROUND_SCHEMA','ON','���ý���Ƿ����');

insert into pos_para (para,val,notes)
	values('RADIX_TYPE','ON','ǰ̨�Ƿ���Ҫ����С���㣿');

insert into  pos_para (para,val,notes)
  values ('TALROUND_SCHEMA','OFF','���������Ƿ�ֱ��ȥ��');

insert into  pos_para (para,val,notes)
  values ('POINT_MIN_MONEY','0','�������߽��');

insert into  pos_para (para,val,notes)
  values ('POINT_ROUNDING','OFF','�����Ƿ���������');

insert into  pos_para (para,val,notes)
  values ('PRINT_SUMPOINT','OFF','�Ƿ��ӡ�����ۼƻ���');

insert into  pos_para (para,val,notes)
  values ('PRINT_TALCOUNT','OFF','�Ƿ�Ҫ��ӡ��Ʒ�����ϼ�');


insert into  pos_para (para,val,notes)
  values ('CARD_NAME','�����','�������������');



--14.����汾���Ʊ�
insert into edition_ctl values('20000101','000000',0,0,0,getdate());

--15.����POS�����
delete from posgroup;
insert into posgroup values('BASIC','����������');
insert into posgroup values('OLIVT','OLIVTר����');
insert into posgroup values('TRAIN','��ѵ������');

--16.����POS��չ��
insert into pos_ini (posgroup,para,val) select 'BASIC',para,val from pos_para;
insert into pos_ini (posgroup,para,val) select 'OLIVT',para,val from pos_para;
insert into pos_ini (posgroup,para,val) select 'TRAIN',para,val from pos_para;


--18.ǰ̨����汾ά��
delete from prog_lst ;
insert into  prog_lst(progname, prognote,progver,progoff,
		      progdate,ftpserver,ftpuser,ftppass)
	     values('pos.exe','POSǰ̨��������','310001',0,
	     	     getdate(),'ftpserver','ftpuser','ftppass');

insert into  prog_lst(progname, prognote,progver,progoff,
		      progdate,ftpserver,ftpuser,ftppass)
	     values('loaddata.exe','POSǰ̨���س���','310001',0,
	     	     getdate(),'ftpserver','ftpuser','ftppass');
	     	     
insert into  prog_lst(progname, prognote,progver,progoff,
		      progdate,ftpserver,ftpuser,ftppass)
	     values('frdrv.tar','POSǰ̨�豸��������','310001',0,
	     	     getdate(),'ftpserver','ftpuser','ftppass');


--19.�ս���Ʊ�������ã�ע���޸Ķ�Ӧ����
delete from config ;
insert into config (systemid,name,value) Values (0,'�����','A00B');
insert into config (systemid,name,value,note)
	values(31,'��ϵͳ����','ǰ̨��ع�����ϵͳ','');			

delete from rjcontrol;
insert into rjcontrol(sdate,shopid,stepname,notes,statusflag)
	 values(getdate(),'A00B','rj_start','�սῪʼ',1);
insert into rjcontrol(sdate,shopid,stepname,notes,statusflag) 
	values(getdate(),'A00B','rj_working','�ս������',1);
insert into rjcontrol(sdate,shopid,stepname,notes,statusflag)
	values(getdate(),'A00B','rj_over','�ս����',1);
update rjcontrol set sdate=sdate-1,
	shopid=(select ltrim(value) from config where name='�����');

--������POS��̨�������桢��صȣ��ĳ�ʼ����Ϣ
--20.ǰ̨��ز������¼�����	

insert into EventLevel values(1,'ϵͳ����');
insert into EventLevel values(2,'ϵͳ����');
insert into EventLevel values(3,'ϵͳ��Ϣ');
insert into EventLevel values(4,'����');
insert into EventLevel values(5,'����');
insert into EventLevel values(6,'��Ϣ');

--21.ǰ̨��ز������¼����뼯��	

insert into EventCode(EventID,EventLevelID,Note) values(30001,3,'����Ա����ϵͳ');
insert into EventCode(EventID,EventLevelID,Note) values(30002,3,'����Ա�˳�ϵͳ');
insert into EventCode(EventID,EventLevelID,Note) values(30003,3,'�޸����ӱ�[Connect]');
insert into EventCode(EventID,EventLevelID,Note) values(30004,3,'�޸Ĳ���Աע���[Login]');
insert into EventCode(EventID,EventLevelID,Note) values(30005,3,'�޸Ľ�ɫ��[Part]');
insert into EventCode(EventID,EventLevelID,Note) values(30006,3,'�޸Ĺ������[WorkGroup]');
insert into EventCode(EventID,EventLevelID,Note) values(30007,3,'�޸Ľ�ɫϵͳ���ձ�[PartSystem]');
insert into EventCode(EventID,EventLevelID,Note) values(30008,3,'�޸Ĳ���Ա��ɫ���ձ�[PartMember]');
insert into EventCode(EventID,EventLevelID,Note) values(30009,3,'�޸Ľ�ɫ��������ձ�[PartGroup]');
insert into EventCode(EventID,EventLevelID,Note) values(30010,3,'�޸Ĺ�����Ȩ�޶��ձ�[GroupRight]');
insert into EventCode(EventID,EventLevelID,Note) values(30011,3,'�޸�ϵͳ���ñ�[Config]');
insert into EventCode(EventID,EventLevelID,Note) values(30012,3,'�޸Ŀ���');
insert into EventCode(EventID,EventLevelID,Note) values(30013,3,'����ϵͳ��־');
insert into EventCode(EventID,EventLevelID,Note) values(30014,3,'���ϵͳ��־');
insert into EventCode(EventID,EventLevelID,Note) values(30015,3,'�Ƿ�����վע��');
insert into EventCode(EventID,EventLevelID,Note) values(30016,3,'�������');
insert into EventCode(EventID,EventLevelID,Note) values(30017,3,'�޸Ĳ�����Ϣ[Office]');
insert into EventCode(EventID,EventLevelID,Note) values(30018,3,'�޸Ĺ���վ��Ϣ[Workstation]');
insert into EventCode(EventID,EventLevelID,Note) values(31001,3,'����POS��');
insert into EventCode(EventID,EventLevelID,Note) values(31002,3,'���POS����־');

--22.����������

insert into vgbkcard(cardtype,cardtag,taglen,cdnolen,cdnodisp,cardname,cardbank,workfile)
  values('0001','1234567890',1,1,1,'������','��������','workfile');


-------function_lst--------------------------------------
 insert into function_lst(keymap_value,fun,hzfun) values(8,'FunBks','�˸�');
 insert into function_lst(keymap_value,fun,hzfun) values(10,'FunEnter','ȷ��');
 insert into function_lst(keymap_value,fun,hzfun) values(34,'FunVipCard','��Ա��');
 insert into function_lst(keymap_value,fun,hzfun) values(36,'FunPrint','ʵʱ��ӡСƱ����');
 insert into function_lst(keymap_value,fun,hzfun) values(37,'FunVgBank','������');
 insert into function_lst(keymap_value,fun,hzfun) values(38,'FunBlankTran','����ȡ��');
 insert into function_lst(keymap_value,fun,hzfun) values(40,'Funwaiter','ӪҵԱ����');
 insert into function_lst(keymap_value,fun,hzfun) values(41,'FunDiscMoney','����ۿ�');
 insert into function_lst(keymap_value,fun,hzfun) values(43,'FunLastPrint','�ش���һ��');
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
 insert into function_lst(keymap_value,fun,hzfun) values(58,'FunOrder','Ԥ��');
 insert into function_lst(keymap_value,fun,hzfun) values(60,'FunIncrCash','���');
 insert into function_lst(keymap_value,fun,hzfun) values(62,'FunDecrCash','����');
 insert into function_lst(keymap_value,fun,hzfun) values(63,'FunRetPay','�˿�');
 insert into function_lst(keymap_value,fun,hzfun) values(66,'FunAmount','����');
 insert into function_lst(keymap_value,fun,hzfun) values(67,'FunVoid','����');
 insert into function_lst(keymap_value,fun,hzfun) values(68,'FunStock','����ȯ');
 insert into function_lst(keymap_value,fun,hzfun) values(69,'FunCheque','֧Ʊ');
 insert into function_lst(keymap_value,fun,hzfun) values(70,'FunReturn','�˻�');
 insert into function_lst(keymap_value,fun,hzfun) values(71,'FunCash','�ֽ����');
 insert into function_lst(keymap_value,fun,hzfun) values(72,'FunHold','�ҵ�');
 insert into function_lst(keymap_value,fun,hzfun) values(73,'FunQuickVoid','����');
 insert into function_lst(keymap_value,fun,hzfun) values(74,'FunVanguard','�����');
 insert into function_lst(keymap_value,fun,hzfun) values(75,'FunCredit','���п�');
 insert into function_lst(keymap_value,fun,hzfun) values(76,'FunDeli','�ͻ�');
 insert into function_lst(keymap_value,fun,hzfun) values(77,'FunAltPrice','���');
 insert into function_lst(keymap_value,fun,hzfun) values(78,'FunNewP','������');
 insert into function_lst(keymap_value,fun,hzfun) values(79,'FunTotal','�ϼ�');
 insert into function_lst(keymap_value,fun,hzfun) values(80,'FunSubtotal','С��');
 insert into function_lst(keymap_value,fun,hzfun) values(81,'FunStoG','���й���ת��');
 insert into function_lst(keymap_value,fun,hzfun) values(82,'FunEsc','�˳�');
 insert into function_lst(keymap_value,fun,hzfun) values(83,'FunLoanCard','���ʿ�');
 insert into function_lst(keymap_value,fun,hzfun) values(84,'FunLock','����/����');
 insert into function_lst(keymap_value,fun,hzfun) values(85,'FunHKD','�۱�');
 insert into function_lst(keymap_value,fun,hzfun) values(86,'FunRMB','�����');
 insert into function_lst(keymap_value,fun,hzfun) values(87,'FunUSD','��Բ');
 insert into function_lst(keymap_value,fun,hzfun) values(88,'FunClear','���');
 insert into function_lst(keymap_value,fun,hzfun) values(89,'FunCancel','����');
 insert into function_lst(keymap_value,fun,hzfun) values(90,'FunGroup','����');
 insert into function_lst(keymap_value,fun,hzfun) values(94,'FunGreenCard','�̿�����');
 insert into function_lst(keymap_value,fun,hzfun) values(95,'FunRoup','����');
 insert into function_lst(keymap_value,fun,hzfun) values(96,'FunShift','���');
 insert into function_lst(keymap_value,fun,hzfun) values(97,'FunFlee','�ӳ�');
 insert into function_lst(keymap_value,fun,hzfun) values(98,'FunSample','�������');
 insert into function_lst(keymap_value,fun,hzfun) values(99,'FunOilTest','�ͻ�����');
 insert into function_lst(keymap_value,fun,hzfun) values(123,'FunDiscount','�����ۿ�');
 insert into function_lst(keymap_value,fun,hzfun) values(124,'FunOffLine','��/�����л�');
 insert into function_lst(keymap_value,fun,hzfun) values(125,'FunDiscTotal','�ܶ��ۿ�');
 insert into function_lst(keymap_value,fun,hzfun) values(126,'Fun_SHOWCASHBOX','�տ���');
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
 insert into keyname(keyvalue,keyname) values(8,'����');
 insert into keyname(keyvalue,keyname) values(9,'TAB');
 insert into keyname(keyvalue,keyname) values(13,'ȷ��');
 insert into keyname(keyvalue,keyname) values(16,'Shift');
 insert into keyname(keyvalue,keyname) values(17,'Ctrl');
 insert into keyname(keyvalue,keyname) values(18,'Alt');
 insert into keyname(keyvalue,keyname) values(20,'Caps');
 insert into keyname(keyvalue,keyname) values(27,'ESC');
 insert into keyname(keyvalue,keyname) values(32,'�ո�');
 insert into keyname(keyvalue,keyname) values(33,'PgUp');
 insert into keyname(keyvalue,keyname) values(34,'PgDn');
 insert into keyname(keyvalue,keyname) values(35,'End');
 insert into keyname(keyvalue,keyname) values(36,'Home');
 insert into keyname(keyvalue,keyname) values(37,'��');
 insert into keyname(keyvalue,keyname) values(38,'��');
 insert into keyname(keyvalue,keyname) values(39,'��');
 insert into keyname(keyvalue,keyname) values(40,'��');
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
 insert into keybd_class(classname,note) values('TA','TA61����ͼ');
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

