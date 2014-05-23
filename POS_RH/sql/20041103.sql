--实时调价的相关表
use myshopPOS
go

if not exists (select id from sysobjects where id = object_id('dbo.notify') and type = 'U')
create table notify
(
	notifyID	int identity,				--通知信息序列号
	notifyType	int not null,				--通知信息类型（0表示实时调价）
	notifyTime	dateTime default getdate() not null,	--通知信息的产生时间
	descript	varchar(50),				--详细描述
	primary key 	(notifyID)
);
go

if not exists (select id from sysobjects where id = object_id('dbo.notifyitem') and type = 'U')
create table notifyitem
(
	notifyID	int,					--通知信息序列号
	itemID		int,					--通知信息明细序号
	value1		varchar(50),				--信息1
	value2		varchar(50),				--信息2
	value3		varchar(50),				--信息3
	primary key 	(notifyID,itemID)
);
go


if not exists (select id from sysobjects where id = object_id('dbo.notifiedPOS') and type = 'U')
create table notifiedPOS
(
	notifyID	int not null,				--通知信息序列号
	posID		char(4) not null			--POS机号
	primary key 	(notifyID,posID)
);
go


if not exists (select 1 from syscolumns where name='port' and id in 
	(select id from sysobjects where id = object_id('dbo.pos_lst') and type = 'U'))	
      alter table pos_lst add port integer default 9999
go
