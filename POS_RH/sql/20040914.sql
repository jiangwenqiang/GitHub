------------------------------------------------------------------
--�޸��ظ���ˮ�����������ֶ�
------------------------------------------------------------------
use myshoppos
go

if not exists (select 1 from syscolumns where name='shiftID' and id in 
	(select id from sysobjects where id = object_id('dbo.sale_jrep') and type = 'U'))	
      alter table sale_jrep add shiftID integer
go

if not exists (select 1 from syscolumns where name='workdate' and id in 
	(select id from sysobjects where id = object_id('dbo.sale_jrep') and type = 'U'))	
      alter table sale_jrep add workdate datetime
go

if not exists (select 1 from syscolumns where name='shiftID' and id in 
	(select id from sysobjects where id = object_id('dbo.pay_jrep') and type = 'U'))	
      alter table pay_jrep add shiftID integer
go

if not exists (select 1 from syscolumns where name='workdate' and id in 
	(select id from sysobjects where id = object_id('dbo.pay_jrep') and type = 'U'))	
      alter table pay_jrep add workdate datetime
go


------------------------------------------------------------------
--�¼�"������"�ĵ�������
------------------------------------------------------------------
use myshopshcard
go

delete from sheettype where sheettype='559002'
insert into sheettype (sheettype,name) values ('559002','������')

go

------------------------------------------------------------------
--���ӹ��˿��������Ż��ֶ�
------------------------------------------------------------------
if not exists (select 1 from syscolumns where name='DiscCount' and id in 
	(select id from sysobjects where id = object_id('dbo.creditclient') and type = 'U'))	
      alter table creditclient add DiscCount decimal(12, 2)
go



 
     