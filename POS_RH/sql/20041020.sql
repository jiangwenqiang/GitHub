------------------------------------------------------------------
--修改PosTurn表，增加两个字段
------------------------------------------------------------------
use myshoppos
go

if not exists (select 1 from syscolumns where name='IsStartOffLine' and id in 
	(select id from sysobjects where id = object_id('dbo.pos_turn') and type = 'U'))	
      alter table pos_turn add IsStartOffLine integer
go

if not exists (select 1 from syscolumns where name='IsEndOffLine' and id in 
	(select id from sysobjects where id = object_id('dbo.pos_turn') and type = 'U'))	
      alter table pos_turn add IsEndOffLine integer
go





 
     