--ʵʱ���۵���ر�
use myshopPOS
go

if not exists (select id from sysobjects where id = object_id('dbo.notify') and type = 'U')
create table notify
(
	notifyID	int identity,				--֪ͨ��Ϣ���к�
	notifyType	int not null,				--֪ͨ��Ϣ���ͣ�0��ʾʵʱ���ۣ�
	notifyTime	dateTime default getdate() not null,	--֪ͨ��Ϣ�Ĳ���ʱ��
	descript	varchar(50),				--��ϸ����
	primary key 	(notifyID)
);
go

if not exists (select id from sysobjects where id = object_id('dbo.notifyitem') and type = 'U')
create table notifyitem
(
	notifyID	int,					--֪ͨ��Ϣ���к�
	itemID		int,					--֪ͨ��Ϣ��ϸ���
	value1		varchar(50),				--��Ϣ1
	value2		varchar(50),				--��Ϣ2
	value3		varchar(50),				--��Ϣ3
	primary key 	(notifyID,itemID)
);
go


if not exists (select id from sysobjects where id = object_id('dbo.notifiedPOS') and type = 'U')
create table notifiedPOS
(
	notifyID	int not null,				--֪ͨ��Ϣ���к�
	posID		char(4) not null			--POS����
	primary key 	(notifyID,posID)
);
go


if not exists (select 1 from syscolumns where name='port' and id in 
	(select id from sysobjects where id = object_id('dbo.pos_lst') and type = 'U'))	
      alter table pos_lst add port integer default 9999
go
