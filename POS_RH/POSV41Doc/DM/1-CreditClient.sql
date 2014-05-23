/*==============================================================*/
/* Database name:  挂帐客户                                     */
/* DBMS name:      Microsoft SQL Server 2000(Extended)          */
/* Created on:     2004-05-29 05:02:38                          */
/*==============================================================*/
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET ANSI_NULL_DFLT_ON ON
GO

if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.FK_OILTANK_REFERENCE_OILTYPE')
            and   type = 'F')
    alter table dbo.OilTank
        drop constraint FK_OILTANK_REFERENCE_OILTYPE
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.OilType')
            and   type = 'U')
   drop table dbo.OilType
go


/*==============================================================*/
/* Table: OilType                                               */
/*==============================================================*/
/*
油品类型表，指油站经营的所有油品类型*/
create table dbo.OilType (
   OilTypeID            smallint             not null  /* 油品类型编码 */,
   OilTypeName          char(16)             not null  /* 油品类型名称 */,
   OilTypeSName         char(8)              null default ' '  /* 油品类型名简称 */,
   OilGunCNums          int                  not null default 0  /* 油枪数 */,
   Note                 char(32)             null default ' '  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后修改时间 */,
   LastModiOPID         char(8)              null default ' '  /* 最后修改人 */,
   constraint PK_OILTYPE primary key  (OilTypeID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'油品类型表，指油站经营的所有油品类型', N'user', N'dbo', N'table', N'OilType', NULL, NULL
go


EXECUTE sp_addextendedproperty N'MS_Description', N'油品类型ID号，唯一关键字', N'user', N'dbo', N'table', N'OilType', N'column', N'OilTypeID'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'油品类型名称', N'user', N'dbo', N'table', N'OilType', N'column', N'OilTypeName'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'油品类型名简称', N'user', N'dbo', N'table', N'OilType', N'column', N'OilTypeSName'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'记录该油品类型的油枪数', N'user', N'dbo', N'table', N'OilType', N'column', N'OilGunCNums'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'备注', N'user', N'dbo', N'table', N'OilType', N'column', N'Note'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'最近修改时间', N'user', N'dbo', N'table', N'OilType', N'column', N'LastModiTime'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'最近修改人', N'user', N'dbo', N'table', N'OilType', N'column', N'LastModiOPID'
go



if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.FK_CREDITCL_INHERITAN_GUEST')
            and   type = 'F')
    alter table dbo.CreditClient
        drop constraint FK_CREDITCL_INHERITAN_GUEST
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.FK_CREDITCL_RELATIONS_BUSINESS')
            and   type = 'F')
    alter table dbo.CreditClient
        drop constraint FK_CREDITCL_RELATIONS_BUSINESS
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.FK_CREDITCL_SIGNSHOP_SHOP')
            and   type = 'F')
    alter table dbo.CreditClient
        drop constraint FK_CREDITCL_SIGNSHOP_SHOP
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.FK_CREDITCL_CREDITCLI_CREDITCL1')
            and   type = 'F')
    alter table dbo.CreditClient_C
        drop constraint FK_CREDITCL_CREDITCLI_CREDITCL1
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.FK_CREDITCL_CREDITCLI_CREDITCL')
            and   type = 'F')
    alter table dbo.CreditClient_Shop
        drop constraint FK_CREDITCL_CREDITCLI_CREDITCL
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.FK_CREDITCL_CREDITCLI_SHOP')
            and   type = 'F')
    alter table dbo.CreditClient_Shop
        drop constraint FK_CREDITCL_CREDITCLI_SHOP
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.FK_CREDITSU_RELATIONS_CREDITCL')
            and   type = 'F')
    alter table dbo.CreditSubCard
        drop constraint FK_CREDITSU_RELATIONS_CREDITCL
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.FK_CREDITSU_RELATIONS_CARTYPE')
            and   type = 'F')
    alter table dbo.CreditSubCard
        drop constraint FK_CREDITSU_RELATIONS_CARTYPE
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CardAcc')
            and   name  = 'Relationship_11_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CardAcc.Relationship_11_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CardAcc0')
            and   name  = 'Relationship_11_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CardAcc0.Relationship_11_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClient')
            and   name  = 'Relationship_7_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClient.Relationship_7_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClient')
            and   name  = 'SignShop_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClient.SignShop_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClientLog')
            and   name  = 'Relationship_7_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClientLog.Relationship_7_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClientLog')
            and   name  = 'SignShop_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClientLog.SignShop_FK
go




if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClient_CLog')
            and   name  = 'CreditClient_C2_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClient_CLog.CreditClient_C2_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClient_CLog')
            and   name  = 'CreditClient_C_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClient_CLog.CreditClient_C_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClient_C')
            and   name  = 'CreditClient_C2_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClient_C.CreditClient_C2_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClient_C')
            and   name  = 'CreditClient_C_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClient_C.CreditClient_C_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClient_Shop')
            and   name  = 'CreditClient_Shop2_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClient_Shop.CreditClient_Shop2_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditClient_Shop')
            and   name  = 'CreditClient_Shop_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditClient_Shop.CreditClient_Shop_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditSubCard')
            and   name  = 'Relationship_2_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditSubCard.Relationship_2_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditSubCard')
            and   name  = 'Relationship_8_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditSubCard.Relationship_8_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditSubCardLog')
            and   name  = 'Relationship_2_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditSubCardLog.Relationship_2_FK
go


if exists (select 1
            from  sysindexes
           where  id    = object_id('dbo.CreditSubCardLog')
            and   name  = 'Relationship_8_FK'
            and   indid > 0
            and   indid < 255)
   drop index dbo.CreditSubCardLog.Relationship_8_FK
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.BusinessType')
            and   type = 'U')
   drop table dbo.BusinessType
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.BusinessType9')
            and   type = 'U')
   drop table dbo.BusinessType9
go



if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.BusinessTypeLog')
            and   type = 'U')
   drop table dbo.BusinessTypeLog
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CarType')
            and   type = 'U')
   drop table dbo.CarType
go

if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CarType9')
            and   type = 'U')
   drop table dbo.CarType9
go



if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CarTypeLog')
            and   type = 'U')
   drop table dbo.CarTypeLog
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CardAcc')
            and   type = 'U')
   drop table dbo.CardAcc
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CardAcc0')
            and   type = 'U')
   drop table dbo.CardAcc0
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CreditClient')
            and   type = 'U')
   drop table dbo.CreditClient
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CreditClientLog')
            and   type = 'U')
   drop table dbo.CreditClientLog
go




if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CreditClient_CLog')
            and   type = 'U')
   drop table dbo.CreditClient_CLog
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CreditClient_C')
            and   type = 'U')
   drop table dbo.CreditClient_C
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CreditClient_Shop')
            and   type = 'U')
   drop table dbo.CreditClient_Shop
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CreditSubCard')
            and   type = 'U')
   drop table dbo.CreditSubCard
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.CreditSubCardLog')
            and   type = 'U')
   drop table dbo.CreditSubCardLog
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.TabFldDesc')
            and   type = 'U')
   drop table dbo.TabFldDesc
go


if exists (select 1
            from  sysobjects
           where  id = object_id('dbo.TabFldDescLog')
            and   type = 'U')
   drop table dbo.TabFldDescLog
go


/*==============================================================*/
/* Table: BusinessType                                          */
/*==============================================================*/
/*
经营类型*/
create table dbo.BusinessType (
   BusinessTypeID       int                  not null  /* 经营类型编码 */,
   BusinessTypeName     char(20)             not null  /* 经营类型名称 */,
   BusinessSName        char(8)              null default ' '  /* 经营类型名简称 */,
   Note                 char(32)             null  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后修改时间 */,
   LastModiOPID         char(8)              not null default ' '  /* 最后修改人 */,
   constraint PK_BUSINESSTYPE primary key  (BusinessTypeID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'经营类型', N'user', N'dbo', N'table', N'BusinessType', NULL, NULL
go


/*==============================================================*/
/* Table: BusinessTypeLog                                       */
/*==============================================================*/
/*
经营类型日志*/
create table dbo.BusinessTypeLog (
   SerialID             int IDENTITY(1,1)    not null  /* 序号 */,
   BusinessTypeID       int                  not null  /* 经营类型编码 */,
   BusinessTypeName     char(20)             not null  /* 经营类型名称 */,
   BusinessSName        char(8)              null default ' '  /* 经营类型名简称 */,
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */,
   TranFlag 		int  		     default 0 not null,  --传单标志
   constraint PK_BUSINESSTYPELOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'经营类型日志', N'user', N'dbo', N'table', N'BusinessTypeLog', NULL, NULL
go

/*
经营类型*/
create table dbo.BusinessType9 (
   SheetID		char(16)	     not null,  --单据编号
   BusinessTypeID       int                  not null,  --经营类型编码
   BusinessTypeName     char(20)             not null,  --经营类型名称
   BusinessSName        char(8)              null default ' ',  --经营类型名简称
   Note                 char(32)             null,  --备注
   TranFlag             int                  null default 0,  --传单标志
   Remark               char(32)              default ' ',
   ModiAttr             char(1)              not null,
   LastModiTime         datetime             default getdate()  not null ,
   LastModiOPID             char(8)              default ' '  not null 
)
go


/*==============================================================*/
/* Table: CarType                                               */
/*==============================================================*/
/*
车型基本资料表*/
create table dbo.CarType (
   CarTypeID            int                  not null  /* 车型编码 */,
   CarTypeName          char(32)             not null  /* 车型名称 */,
   CarTypeSName         char(16)             null  /* 车型名简称 */,
   OilTankCapacity      decimal(12,4)        null  /* 油箱容量 */,
   Note                 char(32)             null  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后修改时间 */,
   LastModiOPID         char(8)              not null default ' '  /* 最后修改人 */,
   constraint PK_CARTYPE primary key  (CarTypeID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'车型基本资料表', N'user', N'dbo', N'table', N'CarType', NULL, NULL
go


/*==============================================================*/
/* Table: CarTypeLog                                            */
/*==============================================================*/
/*
车型基本资料表日志*/
create table dbo.CarTypeLog (
   SerialID             int IDENTITY(1,1)    not null  /* 序号 */,
   CarTypeID            int                  not null  /* 车型编码 */,
   CarTypeName          char(32)             not null  /* 车型名称 */,
   CarTypeSName         char(16)             null  /* 车型名简称 */,
   OilTankCapacity      decimal(12,4)        null  /* 油箱容量 */,
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */,
   TranFlag 		int  		     default 0 not null,   --传单标志
   constraint PK_CARTYPELOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'车型基本资料表日志', N'user', N'dbo', N'table', N'CarTypeLog', NULL, NULL
go

create table dbo.CarType9 (
   SheetID		char(16)	     not null,  --单据编号
   CarTypeID            int                  not null,   --车型编码
   CarTypeName          char(32)             not null,   --车型名称
   CarTypeSName         char(16)             null,       --车型名简称
   OilTankCapacity      decimal(12,4)        null,       --油箱容量
   Note                 char(32)             null,       --备注
   TranFlag             int                  null default 0,  --传单标志
   Remark               char(32)              default ' '  ,
   ModiAttr             char(1)              not null  ,
   LastModiTime         datetime             default getdate()  not null ,
   LastModiOPID             char(8)              default ' '  not null 
)
go


/*==============================================================*/
/* Table: CardAcc                                               */
/*==============================================================*/
/*
挂帐客户三级帐正式表，由卡日结程序从0表写入此正式表*/
create table dbo.CardAcc (
   SerialID             int                  not null  /* 卡三级帐序号 */,
   MemberID             char(20)             not null  /* 会员号 */,
   CardNo               char(19)             not null  /* 卡号 */,
   CreditSubCardNo      char(19)             not null default ' '  /* 消费的挂帐卡号 */,
   ShopID               char(4)              not null  /* 发生店号 */,
   OccurDate            datetime             not null default getDate()  /* 发生时间 */,
   RecordDate           datetime             not null default getdate()  /* 记帐时间 */,
   DirectFlag           int                  not null  /* 记账方向 */,
   Value                decimal(12,2)        null  /* 发生额 */,
   ResultValue          decimal(12,2)        null  /* 结存额 */,
   Point                dec(12,2)            null  /* 发生积分 */,
   ResultPoint          dec(12,2)            null  /* 结存积分 */,
   SheetID              char(16)             not null  /* 单据编号 */,
   SheetType            int                  not null  /* 单据类型 */,
   Note                 char(64)             null  /* 备注 */,
   constraint PK_CARDACC primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'挂帐客户三级帐正式表，由卡日结程序从0表写入此正式表', N'user', N'dbo', N'table', N'CardAcc', NULL, NULL
go


/*==============================================================*/
/* Index: Relationship_11_FK                                    */
/*==============================================================*/
create   index Relationship_11_FK on dbo.CardAcc (
CardNo
)
go


/*==============================================================*/
/* Table: CardAcc0                                              */
/*==============================================================*/
/*
挂帐客户三级帐表*/
create table dbo.CardAcc0 (
   SerialID             int IDENTITY(1,1)    not null  /* 卡三级帐序号 */,
   MemberID             char(20)             not null  /* 会员号 */,
   CardNo               char(19)             not null  /* 卡号 */,
   CreditSubCardNo      char(19)             not null default ' '  /* 消费的挂帐卡号 */,
   ShopID               char(4)              not null  /* 发生店号 */,
   OccurDate            datetime             not null default getDate()  /* 发生时间 */,
   RecordDate           datetime             not null default getdate()  /* 记帐时间 */,
   DirectFlag           int                  not null  /* 记账方向 */,
   Value                decimal(12,2)        null  /* 发生额 */,
   ResultValue          decimal(12,2)        null  /* 结存额 */,
   Point                dec(12,2)            null  /* 发生积分 */,
   ResultPoint          dec(12,2)            null  /* 结存积分 */,
   SheetID              char(16)             not null  /* 单据编号 */,
   SheetType            int                  not null  /* 单据类型 */,
   Note                 char(64)             null  /* 备注 */,
   constraint PK_CARDACC0 primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'挂帐客户三级帐表', N'user', N'dbo', N'table', N'CardAcc0', NULL, NULL
go


/*==============================================================*/
/* Index: Relationship_11_FK                                    */
/*==============================================================*/
create   index Relationship_11_FK on dbo.CardAcc0 (
CardNo
)
go


/*==============================================================*/
/* Table: CreditClient                                          */
/*==============================================================*/
/*
挂帐客户表，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡*/
create table dbo.CreditClient (
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 发生店号 */,
   IDCard               char(18)             not null  /* 联系人身份证号 */,
   MemberID             char(20)             not null  /* 会员编码 */,
   BusinessTypeID       int                  not null  /* 经营类型编码 */,
   CardType             char(2)              not null  /* 卡类型 */,
   Gue_PayMoney         decimal(12,2)        not null default 0.00  /* 卡资料_累计消费金额 */,
   CreditClientTypeID   int                  not null  /* 挂帐客户类型 */,
   CustomNo             char(16)             not null  /* 大油本编号 */,
   Name                 char(50)             not null  /* 名称 */,
   TeleNo               char(20)             null  /* 电话 */,
   Mobile               char(20)             null  /* 联系人手机 */,
   PostalCode           char(6)              null default ' '  /* 邮政编码 */,
   Address              char(64)             not null default ' '  /* 地址 */,
   InCharger            char(8)              not null default ' '  /* 负责人 */,
   LinkMan              char(8)              not null default ' '  /* 联系人 */,
   FaxNo                char(20)             not null default ' '  /* 公司传真 */,
   FinanceNo		char(20)	     not null,		  --财务代码  
   PayMoney             decimal(12,2)        not null  /* 累计消费金额 */,
   Deposit              decimal(12,2)        not null default 0.00  /* 押金 */,
   DiscCount 		decimal(12,2) not null default 0,  --挂帐客户油品优惠额度
   StartDate            datetime             not null  /* 开户日期 */,
   Status               char(1)              not null  /* 状态 */,
   Note                 char(64)             null  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后更新日期 */,
   LastModiOPID         char(8)              not null default ' '  /* 最后修改人 */,
   DiscCount 		decimal(12, 2),
   constraint PK_CREDITCLIENT primary key  (CardNo)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'挂帐客户表，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡', N'user', N'dbo', N'table', N'CreditClient', NULL, NULL
go


/*==============================================================*/
/* Index: SignShop_FK                                           */
/*==============================================================*/
create   index SignShop_FK on dbo.CreditClient (
ShopID
)
go


/*==============================================================*/
/* Index: Relationship_7_FK                                     */
/*==============================================================*/
create   index Relationship_7_FK on dbo.CreditClient (
BusinessTypeID
)
go


/*==============================================================*/
/* Table: CreditClientLog                                       */
/*==============================================================*/
/*
挂帐客户表，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡*/
create table dbo.CreditClientLog (
   SerialID             int IDENTITY(1,1)    not null  /* 序号 */,
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 发生店号 */,
   IDCard               char(18)             not null  /* 联系人身份证号 */,
   MemberID             char(20)             not null  /* 会员编码 */,
   BusinessTypeID       int                  not null  /* 经营类型编码 */,
   CardType             char(2)              not null  /* 卡类型 */,
   Gue_PayMoney         decimal(12,2)        not null default 0.00  /* 卡资料_累计消费金额 */,
   CreditClientTypeID   int                  not null  /* 挂帐客户类型 */,
   CustomNo             char(16)             not null  /* 大油本编号 */,
   Name                 char(50)             not null  /* 名称 */,
   TeleNo               char(20)             null  /* 电话 */,
   Mobile               char(20)             null  /* 联系人手机 */,
   PostalCode           char(6)              null default ' '  /* 邮政编码 */,
   Address              char(64)             not null default ' '  /* 地址 */,
   InCharger            char(8)              not null default ' '  /* 负责人 */,
   LinkMan              char(8)              not null default ' '  /* 联系人 */,
   FaxNo                char(20)             not null default ' '  /* 公司传真 */,
   PayMoney             decimal(12,2)        not null  /* 累计消费金额 */,
   Deposit              decimal(12,2)        not null default 0.00  /* 押金 */,
   FinanceNo            char(20)             not null  /* 财务编码 */,
   DiscCount 		decimal(12,2) not null default 0,  --挂帐客户油品优惠额度
   StartDate            datetime             not null  /* 开户日期 */,
   Status               char(1)              not null  /* 状态 */,
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */,
   TranFlag 		int  		     default 0 not null,  --传单标志	
   constraint PK_CREDITCLIENTLOG primary key  (SerialID)
)
go



EXECUTE sp_addextendedproperty N'MS_Description', N'挂帐客户表，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡', N'user', N'dbo', N'table', N'CreditClientLog', NULL, NULL
go


/*==============================================================*/
/* Index: SignShop_FK                                           */
/*==============================================================*/
create   index SignShop_FK on dbo.CreditClientLog (
ShopID
)
go


/*==============================================================*/
/* Index: Relationship_7_FK                                     */
/*==============================================================*/
create   index Relationship_7_FK on dbo.CreditClientLog (
BusinessTypeID
)
go





/*==============================================================*/
/* Table: CreditClient_CLog                                     */
/*==============================================================*/
/*
挂帐卡－消费类别项表日志表*/
create table dbo.CreditClient_CLog (
   SerialID             int IDENTITY(1,1)    not null  /* 序号 */,
   CreditClientSubCardNo char(25)             not null  /* 消费的挂帐卡号 */,
   DeptID               int                  not null  /* 类别编码 */,
   IsAllowedUse         int                  null  /* 是否允许消费 */,
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */,
   TranFlag 		int  		     default 0 not null,  --传单标志
   constraint PK_CREDITCLIENT_CLOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'挂帐卡－消费类别项表日志表', N'user', N'dbo', N'table', N'CreditClient_CLog', NULL, NULL
go


/*==============================================================*/
/* Index: CreditClient_C_FK                              */
/*==============================================================*/
create   index CreditClient_C_FK on dbo.CreditClient_CLog (
CreditClientSubCardNo
)
go


/*==============================================================*/
/* Index: CreditClient_C2_FK                             */
/*==============================================================*/
create   index CreditClient_C2_FK on dbo.CreditClient_CLog (
DeptID
)
go


/*==============================================================*/
/* Table: CreditClient_C                                 */
/*==============================================================*/
/*
挂帐卡－消费类别项表*/
create table dbo.CreditClient_C (
   CreditClientSubCardNo char(25)             not null  /* 消费的挂帐卡号 */,
   DeptID               int                  not null  /* 类别编码 */,
   IsAllowedUse         int                  null  /* 是否允许消费 */,
   Note                 char(32)             null  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后修改时间 */,
   LastModiOPID         char(8)              not null default ' '  /* 最后修改人 */,
   constraint PK_CreditClient_C primary key  (CreditClientSubCardNo, DeptID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'挂帐卡－消费类别项表', N'user', N'dbo', N'table', N'CreditClient_C', NULL, NULL
go


/*==============================================================*/
/* Index: CreditClient_C_FK                              */
/*==============================================================*/
create   index CreditClient_C_FK on dbo.CreditClient_C (
CreditClientSubCardNo
)
go


/*==============================================================*/
/* Index: CreditClient_C2_FK                             */
/*==============================================================*/
create   index CreditClient_C2_FK on dbo.CreditClient_C (
DeptID
)
go


/*==============================================================*/
/* Table: CreditClient_Shop                                     */
/*==============================================================*/
/*
挂帐客户－消费油站项表*/
create table dbo.CreditClient_Shop (
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 发生店号 */,
   IsAllowedUse         int                  not null  /* 是否允许消费 */,
   Note                 char(32)             null  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后修改时间 */,
   LastModiOPID         char(8)              not null default ' '  /* 最后修改人 */,
   constraint PK_CREDITCLIENT_SHOP primary key  (CardNo, LastModiOPID, ShopID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'挂帐客户－消费油站项表', N'user', N'dbo', N'table', N'CreditClient_Shop', NULL, NULL
go


/*==============================================================*/
/* Index: CreditClient_Shop_FK                                  */
/*==============================================================*/
create   index CreditClient_Shop_FK on dbo.CreditClient_Shop (
CardNo
)
go


/*==============================================================*/
/* Index: CreditClient_Shop2_FK                                 */
/*==============================================================*/
create   index CreditClient_Shop2_FK on dbo.CreditClient_Shop (
ShopID
)
go


/*==============================================================*/
/* Table: CreditSubCard                                         */
/*==============================================================*/
/*
挂帐卡资料表，，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡*/
create table dbo.CreditSubCard (
   CreditSubCardNo      char(25)             not null  /* 消费的挂帐卡号 */,
   CarTypeID            int                  not null  /* 车型编码 */,
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 所在店号 */,
   CustomNo             char(16)             not null  /* 小油本编号 */,
   CardVerifyCode       char(7)              null  /* 卡校验码 */,
   IsNeededVerify       int                  null  /* 是否需要校验卡校验码 */,
   IsEncryVerifyCode    int                  null  /* 卡校验码是否加密 */,
   CarID                char(10)             not null  /* 车牌 */,
   DriverName           char(8)              null  /* 司机名称 */,
   MaxOilQtyPerTrans    decimal(12,4)        not null  /* 每单交易允许消费油品的总量 */,
   Credit               decimal(12,2)        not null  /* 挂帐客户信贷额度 */,
   Balance              decimal(12,2)        not null  /* 挂帐卡余额 */,
   PayMoney             decimal(12,2)        not null  /* 累计消费金额 */,
   StartDate            datetime             not null  /* 开户日期 */,
   OilTypeID	       smallint                not null,		-- 油品类型编码 
   Status               char(1)              not null  /* 状态 */,
   Note                 char(64)             null  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后更新日期 */,
   LastModiOPID         char(8)              not null default ' '  /* 最后修改人 */,
   constraint PK_CREDITSUBCARD primary key  (CreditSubCardNo)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'挂帐卡资料表，，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡', N'user', N'dbo', N'table', N'CreditSubCard', NULL, NULL
go


/*==============================================================*/
/* Index: Relationship_2_FK                                     */
/*==============================================================*/
create   index Relationship_2_FK on dbo.CreditSubCard (
CardNo
)
go


/*==============================================================*/
/* Index: Relationship_8_FK                                     */
/*==============================================================*/
create   index Relationship_8_FK on dbo.CreditSubCard (
CarTypeID
)
go


/*==============================================================*/
/* Table: CreditSubCardLog                                      */
/*==============================================================*/
/*
挂帐卡资料表，，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡*/
create table dbo.CreditSubCardLog (
   SerialID             int IDENTITY(1,1)    not null  /* 序号 */,
   CreditSubCardNo      char(25)             not null  /* 消费的挂帐卡号 */,
   CarTypeID            int                  not null  /* 车型编码 */,
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 所在店号 */,
   CustomNo             char(16)             not null  /* 小油本编号 */,
   CardVerifyCode       char(7)              null  /* 卡校验码 */,
   IsNeededVerify       int                  null  /* 是否需要校验卡校验码 */,
   IsEncryVerifyCode    int                  null  /* 卡校验码是否加密 */,
   CarID                char(10)             not null  /* 车牌 */,
   DriverName           char(8)              null  /* 司机名称 */,
   MaxOilQtyPerTrans    decimal(12,4)        not null  /* 每单交易允许消费油品的总量 */,
   Credit               decimal(12,2)        not null  /* 挂帐客户信贷额度 */,
   Balance              decimal(12,2)        not null  /* 挂帐卡余额 */,
   PayMoney             decimal(12,2)        not null  /* 累计消费金额 */,
   StartDate            datetime             not null  /* 开户日期 */,
   Status               char(1)              not null  /* 状态 */,
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */,
   TranFlag 		int  		     default 0 not null,  --传单标志		
   OilTypeID 		smallint  	     default 0 not null,  --参考油品
   constraint PK_CREDITSUBCARDLOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'挂帐卡资料表，，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡', N'user', N'dbo', N'table', N'CreditSubCardLog', NULL, NULL
go


/*==============================================================*/
/* Index: Relationship_2_FK                                     */
/*==============================================================*/
create   index Relationship_2_FK on dbo.CreditSubCardLog (
CardNo
)
go


/*==============================================================*/
/* Index: Relationship_8_FK                                     */
/*==============================================================*/
create   index Relationship_8_FK on dbo.CreditSubCardLog (
CarTypeID
)
go


/*==============================================================*/
/* Table: TabFldDesc                                            */
/*==============================================================*/
/*
本系统中用到的类型相关基本信息表字段描述*/
create table dbo.TabFldDesc (
   TabName              char(30)             not null  /* 表名 */,
   FldName              char(30)             not null  /* 字段名 */,
   FldSName             char(32)             not null  /* 字段显示信息 */,
   FldOrder             int                  null  /* 字段显示序号 */,
   DataType             smallint             not null default 1  /* 字段数据类型 */,
   DataLen              smallint             not null  /* 字段数据长度 */,
   isAllowNull          smallint             not null default 1  /* 是否允许为空 */,
   Note                 char(32)             null default ' '  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后修改时间 */,
   LastModiOPID         char(8)              null default ' '  /* 最后修改人 */,
   constraint PK_TABFLDDESC primary key  (TabName, FldName)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'本系统中用到的类型相关基本信息表字段描述', N'user', N'dbo', N'table', N'TabFldDesc', NULL, NULL
go


/*==============================================================*/
/* Table: TabFldDescLog                                         */
/*==============================================================*/
/*
本系统中用到的类型相关基本信息表字段描述*/
create table dbo.TabFldDescLog (
   SerialID             int IDENTITY(1,1)    not null  /* 序列号 */,
   FldName              char(30)             not null  /* 字段名 */,
   FldSName             char(32)             not null  /* 字段显示信息 */,
   FldOrder             int                  null  /* 字段显示序号 */,
   DataType             smallint             not null default 1  /* 字段数据类型 */,
   DataLen              smallint             not null  /* 字段数据长度 */,
   TranFlag             int                  not null default 0  /* 传单标志 */,
   isAllowNull          smallint             not null default 1  /* 是否允许为空 */,
   Note                 char(32)             null default ' '  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */,
   constraint PK_TABFLDDESCLOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'本系统中用到的类型相关基本信息表字段描述', N'user', N'dbo', N'table', N'TabFldDescLog', NULL, NULL
go


alter table dbo.CreditClient
   add constraint FK_CREDITCL_INHERITAN_GUEST foreign key (CardNo)
      references Guest (CardNo)
      on update cascade on delete cascade
go


alter table dbo.CreditClient
   add constraint FK_CREDITCL_RELATIONS_BUSINESS foreign key (BusinessTypeID)
      references dbo.BusinessType (BusinessTypeID)
      on update cascade on delete cascade
go


alter table dbo.CreditClient
   add constraint FK_CREDITCL_SIGNSHOP_SHOP foreign key (ShopID)
      references Shop (ShopID)
      on update cascade on delete cascade
go


alter table dbo.CreditClient_C
   add constraint FK_CREDITCL_CREDITCLI_CREDITCL1 foreign key (CreditClientSubCardNo)
      references dbo.CreditSubCard (CreditSubCardNo)
      on update cascade on delete cascade
go


alter table dbo.CreditClient_Shop
   add constraint FK_CREDITCL_CREDITCLI_CREDITCL foreign key (CardNo)
      references dbo.CreditClient (CardNo)
      on update cascade on delete cascade
go


alter table dbo.CreditClient_Shop
   add constraint FK_CREDITCL_CREDITCLI_SHOP foreign key (ShopID)
      references Shop (ShopID)
go


alter table dbo.CreditSubCard
   add constraint FK_CREDITSU_RELATIONS_CREDITCL foreign key (CardNo)
      references dbo.CreditClient (CardNo)
      on update cascade on delete cascade
go


alter table dbo.CreditSubCard
   add constraint FK_CREDITSU_RELATIONS_CARTYPE foreign key (CarTypeID)
      references dbo.CarType (CarTypeID)
      on update cascade on delete cascade
go


 
--挂帐卡开卡单据表：  OpenCard0,OpenCardItem0,OpenCardPayType0 ,OpenCard,OpenCardItem,OpenCardPayType

--挂帐卡挂失单据表： LostClientCard0,LostClientCardItem0,LostClientCard,LostClientCardItem
--挂帐卡解挂失单据表： UnLostClientCard0,UnLostClientCardItem0,UnLostClientCard,UnLostClientCardItem

--挂帐卡冻结单据表： FreezeClientCard0,FreezeClientItem0,FreezeClientCard,FreezeClientCardItem
--挂帐卡解冻结单据表： UnFreezeClientCard0,UnFreezeClientCardItem0,UnFreezeClientCard,UnFreezeClientCardItem

--挂帐卡金额充值单据表： CreditMoneyAdd0,CreditMoneyAddItem0,CreditMoneyAdd,CreditMoneyAddItem
--挂帐卡清帐单据表：	CreditSubCardClear0,CreditSubCardClearitem0,CreditSubCardClear,CreditSubCardClearitem

 
--挂帐卡开卡单据表： OpenCard0,OpenItem0,OpenCardPayType0 ,OpenCard,OpenCardItem,OpenCardPayType

--挂帐卡清户单据表：	CreditCardCancel0,CreditCardCancelItem0,CreditCardCancel,CreditCardCancelitem
--挂帐卡主户消费备份表	GuestClear
--挂帐卡主卡清户备份表	ClearCreditClient		
--挂帐卡子卡清户备份表	ClearCreditSubCard
--

if exists (select 1 from  sysobjects where  id = object_id('dbo.OpenCardItem0') and   type = 'U')
   drop table OpenCardItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.Opencardpaytype0') and   type = 'U')
   drop table Opencardpaytype0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.OpenCard0') and   type = 'U')
   drop table OpenCard0
go
create table OpenCard0(
  SheetID       char(16)        not null primary key,           --发卡单号
  ShopID        char(4)         not null,                       --发卡点号
  Flag          integer         default 0 not null,             --确认标志 0-制单1-确定  99=已取消 100=结束,表单结束备入正式表时仅有两种标志,99和100
  PartFlag      smallint        default 0 not null,             --发放前分配标志 0--普通分配,1--售前分配
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称  
  BusinessTypeID       int      not null,			--经营类型编号
  Deposit       Money               null,			-- 押金 
  CreditClientTypeID   int      not null,  			--挂帐客户类型  0=预付款客户，1=信贷客户
  Credit        Money           not null, 			-- 挂帐客户信贷额度   
  TeleNo	char(20),					-- 联系电话
  Mobile	char(20),					-- 手机号
  WorkAddr	char(40),					-- 工作单位
  Address	char(64),					-- 居住地址
  InCharger	char(8),					--负责人
  IDCard	char(18)	not null,			-- 负责人身份证号码
  PostalCode	char(6),					-- 邮政编码
  LinkMan	char(8),					--联系人
  FaxNo		char(20),					--传真
  Email		char(32),					-- Email地址
  Memo 		char(255),       				--备注  
  CardNo	char(25)	not null,			--卡号
  CardType      char(2)         not null,                       --卡类型    
  CustomNo	char(16)	not null,			--大油本编号  
  secrety 	char(7)  	not null ,            		--卡密码  
  Money		dec(12,2)	default 0 not null,		--主帐卡金额  
  DiscountType  smallint 	not null default(0),		--0：无折扣；1：金额折扣；2：面值折扣
  Discountrate  int  		default 100 not null,	        --购卡折扣率
  Discountvalue decimal(8,2)  	default 0 not null ,		--购卡折扣金额
  Totalvalue    decimal(10,2)   default 0 not null ,		--购卡折扣金额  
  Paymoney 	decimal(10,2) 	not null ,			--汇总金额
  FinanceNo	char(20)	not null,		  	--财务代码
  DiscCount 	decimal(12,2) not null default 0,  		--挂帐客户油品优惠额度
  Indate 	datetime      	default getdate() not null ,  	--购卡日期
  EndDate        datetime,  					--有效期终止日期  
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null,                           --确认日期
  auditer 	char(8),					--财务审核人
  auditdate 	datetime, 					--财审核日期 
);
go

--OpenCardItem0
create table OpenCardItem0(
  SheetID       char(16)        not null,                       --发卡单号
  CarTypeID            int      not null,			--  车型编码
  CardNo               char(25)             not null,  		--卡号 
  CustomNo             char(16)             not null,  		--大油本编号
  CardVerifyCode       char(17)             null, 		--卡校验码
  IsNeededVerify       int                  null,  		-- 是否需要校验卡校验码 
  IsEncryVerifyCode    int                  null,  		--卡校验码是否加密 
  CarID                char(10)             not null,		--车牌
  DriverName           char(8)              null,		--司机名称
  MaxOilQtyPerTrans    dec(12,4)            not null,		--每单交易允许消费油品的总量
  OilTypeID	       smallint             not null,		-- 油品类型编码 
  Credit               Money                not null,		--挂帐客户信贷额度
  Memo 		char(255),       				--备注     
  primary key(SheetID,CardNo),
  foreign key(SheetID) references OpenCard0(SheetID)
);
go

--OpenCardPayType0 
create table Opencardpaytype0(
    sheetid 	char(16)      not null ,                        --单据编号
    itemid 	int identity  not null ,                        --序列号
    paytype 	char(1)	      not null ,                        --支付类型编号
    CurrentCode char(3)       not null ,                	--币种
    checkno 	char(6),					--支票号
    orgvalue 	dec(10,2),					--原币
    value 	dec(10,2)     default 0.00 not null ,		--本币(原币*汇率)
    primary key (sheetid,itemid), 
    foreign key(SheetID) references OpenCard0(SheetID)    
);
go

if exists (select 1 from  sysobjects where  id = object_id('dbo.OpenCardItem') and   type = 'U')
   drop table OpenCardItem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.Opencardpaytype') and   type = 'U')
   drop table Opencardpaytype
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.OpenCard') and   type = 'U')
   drop table OpenCard
go
create table OpenCard(
  SheetID       char(16)        not null primary key,           --发卡单号
  ShopID        char(4)         not null,                       --发卡点号
  Flag          integer         default 0 not null,             --确认标志 0-制单1-确定  99=已取消 100=结束,表单结束备入正式表时仅有两种标志,99和100
  PartFlag      smallint        default 0 not null,             --发放前分配标志 0--普通分配,1--售前分配
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称
  BusinessTypeID       int      not null,			--经营类型编号
  Deposit       Money               null,			-- 押金 
  CreditClientTypeID   int      not null,  			--挂帐客户类型0=预付款客户，1=信贷客户
  Credit        Money           not null, 			-- 挂帐客户信贷额度   
  TeleNo	char(20),					-- 联系电话
  Mobile	char(20),					-- 手机号
  WorkAddr	char(40),					-- 工作单位
  Address	char(64),					-- 居住地址
  InCharger	char(8),					--负责人
  IDCard	char(18)	not null,			-- 负责人身份证号码
  PostalCode	char(6),					-- 邮政编码
  LinkMan	char(8),					--联系人
  FaxNo		char(20),					--传真
  Email		char(32),					-- Email地址
  Memo 		char(255),       				--备注  
  CardNo	char(25)	not null,			--卡号
  CardType      char(2)         not null,                       --卡类型    
  CustomNo	char(16)	not null,			--大油本编号  
  secrety 	char(7)  	not null ,            		--卡密码  
  Money		dec(12,2)	default 0 not null,		--主帐卡金额  
  DiscountType  smallint 	not null default(0),		--0：无折扣；1：金额折扣；2：面值折扣
  Discountrate  int  		default 100 not null,	        --购卡折扣率
  Discountvalue decimal(8,2)  	default 0 not null ,		--购卡折扣金额
  Totalvalue    decimal(10,2)   default 0 not null ,		--购卡折扣金额  
  Paymoney 	decimal(10,2) 	not null ,			--汇总金额
  FinanceNo	char(20)	not null,			--财务代码  
  DiscCount 	decimal(12,2) not null default 0,  		--挂帐客户油品优惠额度
  Indate 	datetime      	default getdate() not null ,  	--购卡日期
  EndDate        datetime,  					--有效期终止日期  
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null,                           --确认日期
  auditer 	char(8),					--财务审核人
  auditdate 	datetime, 					--财审核日期 
);
go

--OpenCardItem
create table OpenCardItem(
  SheetID       char(16)        not null,                       --发卡单号
  CarTypeID            int      not null,			--  车型编码
  CardNo               char(25)             not null,  		--卡号 
  CustomNo             char(16)            not null,  		--大油本编号
  CardVerifyCode       char(7)             null, 		--卡校验码
  IsNeededVerify       int                  null,  		-- 是否需要校验卡校验码 
  IsEncryVerifyCode    int                  null,  		--卡校验码是否加密 
  CarID                char(10)             not null,		--车牌
  DriverName           char(8)              null,		--司机名称
  MaxOilQtyPerTrans    dec(12,4)            not null,		--每单交易允许消费油品的总量
  OilTypeID	       smallint             not null,		-- 油品类型编码 
  Credit               Money                not null,		--挂帐客户信贷额度
  Memo 		char(255),       				--备注     
  primary key(SheetID,CardNo),
  foreign key(SheetID) references OpenCard(SheetID)
);
go

--OpenCardPayType 
create table Opencardpaytype(
    sheetid 	char(16)      not null ,                        --单据编号
    itemid 	int           not null ,                        --序列号
    paytype 	char(1)	      not null ,                        --支付类型编号
    CurrentCode 	char(3)       not null ,                --币种
    checkno 	char(6),					--支票号
    orgvalue 	dec(10,2),					--原币
    value 	dec(10,2)     default 0.00 not null ,		--本币(原币*汇率)
    primary key (sheetid,itemid), 
    foreign key(SheetID) references OpenCard(SheetID)    
);
go


--挂帐卡挂失单据表： LostClientCard0,LostClientCardItem0,LostClientCard,LostClientCardItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.LostClientCardItem0') and   type = 'U')
   drop table LostClientCardItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.LostClientCard0') and   type = 'U')
   drop table LostClientCard0
go
create table LostClientCard0(
  SheetID       char(16)        not null primary key,           --挂失单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解挂失 100-确认 
  LostFlag      char(1)         default 'l' not null,           --挂失标志 l-挂失卡
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称
  Reason        char(30)        null,                           --原因
  OTReason      char(30)        null,                           --其它原因
  Owner         char(8)         null,                           --挂失人姓名
  OwnerID       char(18)        null,                           --挂失人身份证号码
  OwnerTele     varchar(20)     null,                           --挂失人联系电话
  Police        Varchar(30)     null,				--派出所名称
  PoliceTele    Varchar(20)     null,				--派出所电话
  Editor        char(8)         null,                           --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

--LostClientCardItem0
create table LostClientCardItem0(
  SheetID       char(16)        not null,                       --挂失单号
  Flag		int	default 0 not null,			--主帐卡标志 0=主帐卡 1=挂帐卡标志
  CardNO        char(19)        not null,                       --卡号
  CustomNo      char(16)        not null,  			--大油本编号    
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references LostClientCard0(SheetID)
);
go


if exists (select 1 from  sysobjects where  id = object_id('dbo.LostClientCardItem') and   type = 'U')
   drop table LostClientCardItem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.LostClientCard') and   type = 'U')
   drop table LostClientCard
go
create table LostClientCard(
  SheetID       char(16)        not null primary key,           --挂失单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解挂失 100-确认 
  LostFlag      char(1)         default 'l' not null,           --挂失标志 l-挂失卡
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称
  Reason        char(30)        null,                           --原因
  OTReason      char(30)        null,                           --其它原因
  Owner         char(8)         null,                           --挂失人姓名
  OwnerID       char(18)        null,                           --挂失人身份证号码
  OwnerTele     varchar(20)     null,                           --挂失人联系电话
  Police        Varchar(30)     null,				--派出所名称
  PoliceTele    Varchar(20)     null,				--派出所电话
  Editor        char(8)         null,                           --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

--LostClientCardItem
create table LostClientCardItem(
  SheetID       char(16)        not null,                       --挂失单号
  Flag		int	default 0 not null,			--主帐卡标志 0=主帐卡 1=挂帐卡标志
  CardNO        char(19)        not null,                       --卡号
  CustomNo      char(16)        not null,  			--大油本编号    
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references LostClientCard(SheetID)
);
go

--挂帐卡解挂失单据表： UnLostClientCard0,UnLostClientCardItem0,UnLostClientCard,UnLostClientCardItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnLostClientCardItem0') and   type = 'U')
   drop table UnLostClientCardItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnLostClientCard0') and   type = 'U')
   drop table UnLostClientCard0
go
create table UnLostClientCard0(
  SheetID       char(16)        not null primary key,           --解挂失单号
  LostSheetID   char(16)        null,                           --挂失单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称  
  Owner         char(8)         null,                           --挂失人
  OwnerID       char(18)        null,                           --挂失人身份证号码
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table UnLostClientCardItem0(
  SheetID       char(16)        not null,                       --挂失单号
  Flag		int	default 0 not null,			--主帐卡标志 0=主帐卡 1=挂帐卡标志  
  CardNO        char(19)        not null,                       --卡号
  CustomNo      char(16)        not null,  			--大油本编号      
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references UnLostClientCard0(SheetID)
);
go

if exists (select 1 from  sysobjects where  id = object_id('dbo.UnLostClientCardItem') and   type = 'U')
   drop table UnLostClientCardItem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnLostClientCard') and   type = 'U')
   drop table UnLostClientCard
go
create table UnLostClientCard(
  SheetID       char(16)        not null primary key,           --解挂失单号
  LostSheetID   char(16)        null,                           --挂失单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称  
  Owner         char(8)         null,                           --挂失人
  OwnerID       char(18)        null,                           --挂失人身份证号码
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table UnLostClientCardItem(
  SheetID       char(16)        not null,                       --挂失单号
  Flag		int	default 0 not null,			--主帐卡标志 0=主帐卡 1=挂帐卡标志  
  CardNO        char(19)        not null,                       --卡号
  CustomNo      char(16)        not null,  			--大油本编号      
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references UnLostClientCard(SheetID)
);
go

--挂帐卡冻结单据表： FreezeClientCard0,FreezeClientItem0,FreezeClientCard,FreezeClientCardItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.FreezeClientItem0') and   type = 'U')
   drop table FreezeClientItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.FreezeClientCard0') and   type = 'U')
   drop table FreezeClientCard0
go
create table FreezeClientCard0(
  SheetID       char(16)        not null primary key,           --冻结单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称    
  Reason        char(20)        not null,                       --原因
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table FreezeClientItem0(
  SheetID       char(16)        not null,                       --挂失单号
  Flag		int	default 0 not null,			--主帐卡标志 0=主帐卡 1=挂帐卡标志  
  CardNO        char(19)        not null,                       --卡号
  CustomNo      char(16)        not null,  			--大油本编号  
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references FreezeClientCard0(SheetID)  
);
go


if exists (select 1 from  sysobjects where  id = object_id('dbo.FreezeClientItem') and   type = 'U')
   drop table FreezeClientItem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.FreezeClientCard') and   type = 'U')
   drop table FreezeClientCard
go
create table FreezeClientCard(
  SheetID       char(16)        not null primary key,           --冻结单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称    
  Reason        char(20)        not null,                       --原因
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table FreezeClientItem(
  SheetID       char(16)        not null,                       --挂失单号
  Flag		int	default 0 not null,			--主帐卡标志 0=主帐卡 1=挂帐卡标志  
  CardNO        char(19)        not null,                       --卡号
  CustomNo      char(16)        not null,  			--大油本编号    
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references FreezeClientCard(SheetID)  
);
go

--挂帐卡解冻结单据表： UnFreezeClientCard0,UnFreezeClientCardItem0,UnFreezeClientCard,UnFreezeClientCardItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnFreezeClientCardItem0') and   type = 'U')
   drop table UnFreezeClientCardItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnFreezeClientCard0') and   type = 'U')
   drop table UnFreezeClientCard0
go
create table UnFreezeClientCard0(
  SheetID       char(16)        not null primary key,           --解冻结单号
  FreeSheetID   char(16)        null,                           --冻结单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 100-确认
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称    
  Reason        char(20)        not null,                       --原因
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table UnFreezeClientCardItem0(
  SheetID       char(16)        not null,                       --挂失单号
  Flag		int	default 0 not null,			--主帐卡标志 0=主帐卡 1=挂帐卡标志  
  CardNO        char(19)        not null,                       --卡号
  CustomNo      char(16)        not null,  			--大油本编号    
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references UnFreezeClientCard0(SheetID)
);
go

if exists (select 1 from  sysobjects where  id = object_id('dbo.UnFreezeClientCardItem') and   type = 'U')
   drop table UnFreezeClientCardItem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnFreezeClientCard') and   type = 'U')
   drop table UnFreezeClientCard
go
create table UnFreezeClientCard(
  SheetID       char(16)        not null primary key,           --解冻结单号
  FreeSheetID   char(16)        null,                           --冻结单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 100-确认
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称    
  Reason        char(20)        not null,                       --原因
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table UnFreezeClientCardItem(
  SheetID       char(16)        not null,                       --挂失单号
  Flag		int	default 0 not null,			--主帐卡标志 0=主帐卡 1=挂帐卡标志  
  CardNO        char(19)        not null,                       --卡号
  CustomNo      char(16)        not null,  			--大油本编号    
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references UnFreezeClientCard(SheetID)
);
go



--挂帐卡金额充值单据表： CreditMoneyAdd0,CreditMoneyAddItem0,CreditMoneyAdd,CreditMoneyAddItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMoneyAddItem0') and   type = 'U')
   drop table CreditMoneyAddItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMoneyAdd0') and   type = 'U')
   drop table CreditMoneyAdd0
go
create table CreditMoneyAdd0(
  SheetID       char(16)        not null primary key,           --单号
  ShopID        char(4)         not null,                       --充值店号
  ClearFlag 	smallint 	not null default 1,	--子帐卡是否要清帐 0 = 不清帐,1 = 清帐
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null,                           --确认日期
  Flag          int	default 0 not null           		--确认标志 0-制单 100-确认
);
go

create table CreditMoneyAddItem0(
  sheetid 	char(16) not null ,				--单据编号
  cardno	char(19) not null ,				--卡号
  MemberID      char(20)        not null,                       --客户编号
  Name          varchar(50)         null,                           --公司名称
  CustomNo      char(16)        not null,  			--大油本编号    
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  DiscountRate  int default 100,				--充卡折扣率
  DiscountValue dec(10,2),					--充卡折扣值  
  newdetail 	dec(8,2) default 0.00 not null ,		--充卡金额值
  primary key (sheetid,cardno), 
  foreign key(SheetID) references CreditMoneyAdd0(SheetID)
);
go

if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMoneyAddItem') and   type = 'U')
   drop table CreditMoneyAddItem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMoneyAdd') and   type = 'U')
   drop table CreditMoneyAdd
go
create table CreditMoneyAdd(
  SheetID       char(16)        not null primary key,           --单号
  ShopID        char(4)         not null,                       --充值店号
  ClearFlag 	smallint 	not null default 1,		--子帐卡是否要清帐 0 = 不清帐,1 = 清帐
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null,                           --确认日期
  Flag          int	default 0 not null           		--确认标志 0-制单 100-确认
);
go

create table CreditMoneyAddItem(
  sheetid 	char(16) not null ,				--单据编号
  cardno	char(19) not null ,				--卡号
  MemberID      char(20)        not null,                       --客户编号
  Name          varchar(50)         null,                           --公司名称
  CustomNo      char(16)        not null,  			--大油本编号    
  SaleShopID    char(4)         null,                           --发卡店号
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  DiscountRate  int default 100,				--充卡折扣率
  DiscountValue dec(10,2),					--充卡折扣值  
  newdetail 	dec(8,2) default 0.00 not null ,		--充卡金额值
  primary key (sheetid,cardno), 
  foreign key(SheetID) references CreditMoneyAdd(SheetID)
);
go


--卡资料条码规格表 PrintCardType
if exists (select 1 from  sysobjects where  id = object_id('dbo.PrintCardType') and   type = 'U')
   drop table PrintCardType
go
create table dbo.PrintCardType 
( 
Name		char(16)	not null primary key,	--规格说明 
Data		image		null			--价签布局定义 
)
go


--价签可打印域定义LabelFields
if exists (select 1 from  sysobjects where  id = object_id('dbo.LabelFields') and   type = 'U')
   drop table LabelFields
go
create table dbo.LabelFields(
labelchsname	char(20)	primary key,		--标签中文名称
labelfieldname	char(20)	not null		--标签字段名称
);
go



--条码打印驱动定义BarcodeConfig
if exists (select 1 from  sysobjects where  id = object_id('dbo.BarcodeConfig') and   type = 'U')
   drop table BarcodeConfig
go
create table dbo.BarcodeConfig(
id			integer		primary key,		--条码打印机编号
name			char(32)	not null,		--条码打印机名称
CrToEmpty		integer		default 0 not null,	--<CR>为空(0-非,1-是)
IsBinaryCmd		integer		default 0 not null,	--是文本命令，还是二进制命令(0-文本命令,1-二进制命令)
InitCmd			varchar(255)	,			--初始化命令
LabelCmdLoopBefore	varchar(255)	,			--文本、图片列循环前命令
LabelCmdLoop		varchar(255)	,			--文本、图片列循环命令
LabelCmdLoopAfter	varchar(255)	,			--文本、图片列循环后命令
XorImage		integer		default 0 not null,	--图形是否要Xor(0-非,1-是)
EAN8BarcodeCmd		varchar(255)	,			--EAN8条码打印命令
EAN13BarcodeCmd		varchar(255)	,			--EAN13条码打印命令
Angle0			char(10)	,			--条码0度命令
Angle90			char(10)	,			--条码90度命令
Angle180		char(10)	,			--条码180度命令
Angle270		char(10)	,			--条码270度命令
EndBeginCmd		varchar(255)	,			--结束打印开始命令
EndLoopCmd		varchar(255)	,			--结束循环打印命令
EndAutoLoopCmd		varchar(255)	,			--结束自动循环打印命令(Zebra)
ContinueLoop		integer		default 0 not null	--是否循环打印,不支持连续打印时使用(0-非,1-是)
);

go



--挂帐卡清帐单据表：	CreditSubCardClear0,CreditSubCardClearitem0,CreditSubCardClear,CreditSubCardClearitem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSubCardClearitem0') and   type = 'U')
   drop table CreditSubCardClearitem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSubCardClear0') and   type = 'U')
   drop table CreditSubCardClear0
go
create table CreditSubCardClear0(
  SheetID       char(16)        not null primary key,           --冻结单号
  refsheetid 	char(16) 	not null default '',  		--相关单据编号 
  refsheettype 	int 		not null default 0,  		--相关单据类型     
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称    
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table CreditSubCardClearitem0(
  SheetID       char(16)        not null,                       --挂失单号
  CardNO        char(19)        not null,                       --卡号
  SaleShopID    char(4)         null,                           --发卡店号
  CustomNo      char(16)        not null,  			--大油本编号  
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditSubCardClear0(SheetID)  
);
go

if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSubCardClearitem') and   type = 'U')
   drop table CreditSubCardClearitem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSubCardClear') and   type = 'U')
   drop table CreditSubCardClear
go
create table CreditSubCardClear(
  SheetID       char(16)        not null primary key,           --冻结单号
  refsheetid 	char(16) 	not null default '',  		--相关单据编号 
  refsheettype 	int 		not null default 0,  		--相关单据类型       
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称    
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table CreditSubCardClearitem(
  SheetID       char(16)        not null,                       --挂失单号
  CardNO        char(19)        not null,                       --卡号
  SaleShopID    char(4)         null,                           --发卡店号
  CustomNo      char(16)        not null,  			--大油本编号  
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditSubCardClear(SheetID)  
);
go


--主卡信贷额修改单据表：	CreditMain0,CreditMainitem0,CreditMain,CreditMainitem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMainitem0') and   type = 'U')
   drop table CreditMainitem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMain0') and   type = 'U')
   drop table CreditMain0
go
create table CreditMain0(
  SheetID       char(16)        not null primary key,           --冻结单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table CreditMainitem0(
  SheetID       char(16)        not null,                       --挂失单号
  CardNO        char(19)        not null,                       --卡号
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(30)        not null,  			--公司名称    
  SaleShopID    char(4)         null,                           --发卡店号
  CustomNo      char(16)        not null,  			--大油本编号  
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditMain0(SheetID)  
);
go

if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMainitem') and   type = 'U')
   drop table CreditMainitem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMain') and   type = 'U')
   drop table CreditMain
go
create table CreditMain(
  SheetID       char(16)        not null primary key,           --冻结单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table CreditMainitem(
  SheetID       char(16)        not null,                       --挂失单号
  CardNO        char(19)        not null,                       --卡号
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(30)        not null,  			--公司名称    
  SaleShopID    char(4)         null,                           --发卡店号
  CustomNo      char(16)        not null,  			--大油本编号  
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditMain(SheetID)  
);
go


--子卡信贷额修改单据表：	CreditSub0,CreditSubitem0,CreditSub,CreditSubitem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSubitem0') and   type = 'U')
   drop table CreditSubitem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSub0') and   type = 'U')
   drop table CreditSub0
go
create table CreditSub0(
  SheetID       char(16)        not null primary key,           --冻结单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称    
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table CreditSubitem0(
  SheetID       char(16)        not null,                       --挂失单号
  CardNO        char(19)        not null,                       --卡号
  SaleShopID    char(4)         null,                           --发卡店号
  CustomNo      char(16)        not null,  			--大油本编号  
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditSub0(SheetID)  
);
go

if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSubitem') and   type = 'U')
   drop table CreditSubitem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSub') and   type = 'U')
   drop table CreditSub
go
create table CreditSub(
  SheetID       char(16)        not null primary key,           --冻结单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称    
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table CreditSubitem(
  SheetID       char(16)        not null,                       --挂失单号
  CardNO        char(19)        not null,                       --卡号
  SaleShopID    char(4)         null,                           --发卡店号
  CustomNo      char(16)        not null,  			--大油本编号  
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditSub(SheetID)  
);
go

--标价签规格 PrintLabelType
if exists (select 1 from  sysobjects where  id = object_id('dbo.PrintLabelType') and   type = 'U')
   drop table PrintLabelType
go
create table dbo.PrintLabelType 
( 
Name		char(16)	not null primary key,	--规格说明 
Data		image		null			--价签布局定义 
)
go

--价签可打印域定义LabelFields
if exists (select 1 from  sysobjects where  id = object_id('dbo.LabelFields') and   type = 'U')
   drop table LabelFields
go
create table dbo.LabelFields(
labelchsname	char(20)	primary key,		--标签中文名称
labelfieldname	char(20)	not null		--标签字段名称
);
go

--条码规格表 PrintBarcodeType
if exists (select 1 from  sysobjects where  id = object_id('dbo.PrintBarcodeType') and   type = 'U')
   drop table PrintBarcodeType
go
create table dbo.PrintBarcodeType 
( 
Name		char(16)	not null primary key,	--规格说明 
Data		image		null			--价签布局定义 
)
go

if exists (select 1
            from  sysobjects
           where  id = object_id('V_OpenCard')
            and   type = 'V')
   drop view V_OpenCard
go


/*==============================================================*/
/* View: V_OpenCard                                             */
/*==============================================================*/
create view V_OpenCard as 
select * from OpenCard
union 
select * from OpenCard0
go




if exists (select * from dbo.sysobjects
	where id = object_id(N'dbo.CreditClient_ShopLog') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table CreditClient_ShopLog;
go

--挂帐客户－消费油站日志表 
create table dbo.CreditClient_ShopLog (
   SerialID             int IDENTITY(1,1)    not null  /* 序号 */,
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 发生店号 */,
   IsAllowedUse         int                  not null  /* 是否允许消费 */,
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */,
   TranFlag             int                  null default 0  /* 传单标志 */,
   constraint PK_CREDITCLIENT_SHOPLOG primary key  (SerialID)
)
go

if exists (select * from dbo.sysobjects
	where id = object_id(N'dbo.CreditClient_Shop9') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table CreditClient_Shop9;
go

create table dbo.CreditClient_Shop9 (
   SheetID		char(16)	     not null,	--通讯单据编号
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 发生店号 */,
   IsAllowedUse         int                  not null  /* 是否允许消费 */,
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */
)
go


if exists (select * from dbo.sysobjects
	where id = object_id(N'dbo.CreditClient_C9') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table CreditClient_C9;
go




--挂帐卡－消费类别项表日志表
create table dbo.CreditClient_C9 (
   SheetID		char(16)	     not null,	--通讯单据编号
   CreditClientSubCardNo char(25)             not null  /* 消费的挂帐卡号 */,
   DeptID               int                  not null  /* 类别编码 */,
   IsAllowedUse         int                  null  /* 是否允许消费 */,
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */
)
go

if exists (select * from dbo.sysobjects
	where id = object_id(N'dbo.CreditClient9') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table CreditClient9;
go



--挂帐客户表，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡
create table dbo.CreditClient9 (
   SheetID		char(16)	     not null,	--通讯单据编号
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 发生店号 */,
   IDCard               char(18)             not null  /* 联系人身份证号 */,
   MemberID             char(20)             not null  /* 会员编码 */,
   BusinessTypeID       int                  not null  /* 经营类型编码 */,
   CardType             char(2)              not null  /* 卡类型 */,
   Gue_PayMoney         decimal(12,2)        not null default 0.00  /* 卡资料_累计消费金额 */,
   CreditClientTypeID   int                  not null  /* 挂帐客户类型 */,
   CustomNo             char(16)             not null  /* 大油本编号 */,
   Name                 char(30)             not null  /* 名称 */,
   TeleNo               char(20)             null  /* 电话 */,
   Mobile               char(20)             null  /* 联系人手机 */,
   PostalCode           char(6)              null default ' '  /* 邮政编码 */,
   Address              char(64)             not null default ' '  /* 地址 */,
   InCharger            char(8)              not null default ' '  /* 负责人 */,
   LinkMan              char(8)              not null default ' '  /* 联系人 */,
   FaxNo                char(20)             not null default ' '  /* 公司传真 */,
   PayMoney             decimal(12,2)        not null  /* 累计消费金额 */,
   Deposit              decimal(12,2)        not null default 0.00  /* 押金 */,
   FinanceNo            char(20)             not null  /* 财务编码 */,
   DiscCount 		decimal(12,2) not null default 0,  --挂帐客户油品优惠额度
   StartDate            datetime             not null  /* 开户日期 */,
   Status               char(1)              not null  /* 状态 */,
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */
)
go

if exists (select * from dbo.sysobjects
	where id = object_id(N'dbo.CreditSubCard9') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table CreditSubCard9;
go


--挂帐卡资料表，，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡
create table dbo.CreditSubCard9 (
   SheetID		char(16)	     not null,	--通讯单据编号
   CreditSubCardNo      char(25)             not null  /* 消费的挂帐卡号 */,
   CarTypeID            int                  not null  /* 车型编码 */,
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 所在店号 */,
   CustomNo             char(16)             not null  /* 小油本编号 */,
   CardVerifyCode       char(7)              null  /* 卡校验码 */,
   IsNeededVerify       int                  null  /* 是否需要校验卡校验码 */,
   IsEncryVerifyCode    int                  null  /* 卡校验码是否加密 */,
   CarID                char(10)             not null  /* 车牌 */,
   DriverName           char(8)              null  /* 司机名称 */,
   MaxOilQtyPerTrans    decimal(12,4)        not null  /* 每单交易允许消费油品的总量 */,
   Credit               decimal(12,2)        not null  /* 挂帐客户信贷额度 */,
   Balance              decimal(12,2)        not null  /* 挂帐卡余额 */,
   PayMoney             decimal(12,2)        not null  /* 累计消费金额 */,
   StartDate            datetime             not null  /* 开户日期 */,
   Status               char(1)              not null  /* 状态 */,
   OilTypeID 		smallint  	     default 0 not null,  --参考油品	
   Note                 char(32)             null  /* 备注 */,
   Remark               char(32)             null default ' '  /* 修改备注 */,
   ModiAttr             char(1)              not null  /* 修改模式 */,
   ModiTime             datetime             not null default GetDate()  /* 修改时间 */,
   ModiOPID             char(8)              not null default ' '  /* 修改人 */
)
go


--挂帐卡清户单据表：	CreditCardCancel0,CreditCardCancelItem0,CreditCardCancel,CreditCardCancelitem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditCardCancelitem0') and   type = 'U')
   drop table CreditCardCancelitem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditCardCancel0') and   type = 'U')
   drop table CreditCardCancel0
go
create table CreditCardCancel0(
  SheetID       char(16)        not null primary key,           --清户单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称  
  CreditClientTypeID   int      not null,			--挂帐客户类型
  FinanceNo	char(20),		  			--财务代码      
  CardNo	char(19)	not null,			--主帐卡
  CustomNo      char(16)        not null,  			--大油本编号
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度  
  detail        dec(8,2),                                       --卡余额   
  PayMoney      decimal(12,2)   not null,			--累计消费金额
  Deposit       decimal(12,2)   not null default 0.00, 		-- 押金
  StartDate     datetime        not null,			--开户日期
  Status        char(1)         not null,			--状态
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table CreditCardCancelitem0(
  SheetID       char(16)        not null,                       --清户单号
  CardNO        char(19)        not null,                       --卡号
  SaleShopID    char(4)         null,                           --发卡店号
  CustomNo      char(16)        not null,  			--大油本编号  
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditCardCancel0(SheetID)  
);
go		 


if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditCardCancelitem') and   type = 'U')
   drop table CreditCardCancelitem
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditCardCancel') and   type = 'U')
   drop table CreditCardCancel
go
create table CreditCardCancel(
  SheetID       char(16)        not null primary key,           --清户单号
  ShopID        char(4)         not null,                       --售卡点
  Flag          int	default 0 not null,           		--确认标志 0-制单 2-已解冻结 100-确认 
  MemberID	char(20)        not null,			-- 客户编号
  Name          char(50)        not null,  			--公司名称  
  CreditClientTypeID   int      not null,			--挂帐客户类型
  FinanceNo	char(20),		  			--财务代码      
  CardNo	char(19)	not null,			--主帐卡
  CustomNo      char(16)        not null,  			--大油本编号
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度  
  detail        dec(8,2),                                       --卡余额   
  PayMoney      decimal(12,2)   not null,			--累计消费金额
  Deposit       decimal(12,2)   not null default 0.00, 		-- 押金
  StartDate     datetime        not null,			--开户日期
  Status        char(1)         not null,			--状态
  Editor        char(8)         not null,                       --制单人
  EditDate      datetime default getdate() not null,            --制单日期
  Operator      char(8)         not null,                       --业务员
  Checker       char(8)         null,                           --确认人
  CheckDate     datetime        null                            --确认日期
);
go

create table CreditCardCancelitem(
  SheetID       char(16)        not null,                       --清户单号
  CardNO        char(19)        not null,                       --卡号
  SaleShopID    char(4)         null,                           --发卡店号
  CustomNo      char(16)        not null,  			--大油本编号  
  detail        dec(8,2),                                       --卡余额
  PayMoney      decimal(12,2)   not null,			--累计消费金额  
  Credit        decimal(12,2)   not null,  			--挂帐客户信贷额度
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditCardCancel(SheetID)  
);
go	



/*==============================================================*/
/* Table: CreditClientClear                                          */
/*==============================================================*/
/*挂帐客户清理表，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡*/
--表结构同CreditClient
if exists (select 1 from  sysobjects where  id = object_id('dbo.ClearCreditClient') and   type = 'U')
   drop table ClearCreditClient
go
create table dbo.ClearCreditClient (
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 发生店号 */,
   IDCard               char(18)             not null  /* 联系人身份证号 */,
   MemberID             char(20)             not null  /* 会员编码 */,
   BusinessTypeID       int                  not null  /* 经营类型编码 */,
   CardType             char(2)              not null  /* 卡类型 */,
   Gue_PayMoney         decimal(12,2)        not null default 0.00  /* 卡资料_累计消费金额 */,
   CreditClientTypeID   int                  not null  /* 挂帐客户类型 */,
   CustomNo             char(16)             not null  /* 大油本编号 */,
   Name                 char(50)             not null  /* 名称 */,
   TeleNo               char(20)             null  /* 电话 */,
   Mobile               char(20)             null  /* 联系人手机 */,
   PostalCode           char(6)              null default ' '  /* 邮政编码 */,
   Address              char(64)             not null default ' '  /* 地址 */,
   InCharger            char(8)              not null default ' '  /* 负责人 */,
   LinkMan              char(8)              not null default ' '  /* 联系人 */,
   FaxNo                char(20)             not null default ' '  /* 公司传真 */,
   FinanceNo	char(20),		  			--财务代码  
   PayMoney             decimal(12,2)        not null  /* 累计消费金额 */,
   Deposit              decimal(12,2)        not null default 0.00  /* 押金 */,
   StartDate            datetime             not null  /* 开户日期 */,
   Status               char(1)              not null  /* 状态 */,
   Note                 char(64)             null  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后更新日期 */,
   LastModiOPID         char(8)              not null default ' '  /* 最后修改人 */,
   constraint PK_CLEARCREDITCLIENT primary key  (CardNo)
)
go	 


/*==============================================================*/
/* Table: ClearCreditSubCard                                         */
/*==============================================================*/
/*
挂帐卡资料清理表，，--status卡状态 1-正常卡,2-未到帐卡,r—已回收卡 m-一般挂失卡 l-严重挂失卡 f-冻结e-已换卡q-退卡*/
--表结构同CreditSubCard
if exists (select 1 from  sysobjects where  id = object_id('dbo.ClearCreditSubCard') and   type = 'U')
   drop table ClearCreditSubCard
go
create table dbo.ClearCreditSubCard (
   CreditSubCardNo      char(25)             not null  /* 消费的挂帐卡号 */,
   CarTypeID            int                  not null  /* 车型编码 */,
   CardNo               char(19)             not null  /* 卡号 */,
   ShopID               char(4)              not null  /* 所在店号 */,
   CustomNo             char(16)             not null  /* 小油本编号 */,
   CardVerifyCode       char(7)              null  /* 卡校验码 */,
   IsNeededVerify       int                  null  /* 是否需要校验卡校验码 */,
   IsEncryVerifyCode    int                  null  /* 卡校验码是否加密 */,
   CarID                char(10)             not null  /* 车牌 */,
   DriverName           char(8)              null  /* 司机名称 */,
   MaxOilQtyPerTrans    decimal(12,4)        not null  /* 每单交易允许消费油品的总量 */,
   Credit               decimal(12,2)        not null  /* 挂帐客户信贷额度 */,
   Balance              decimal(12,2)        not null  /* 挂帐卡余额 */,
   PayMoney             decimal(12,2)        not null  /* 累计消费金额 */,
   StartDate            datetime             not null  /* 开户日期 */,
   OilTypeID	       smallint                not null,		-- 油品类型编码 
   Status               char(1)              not null  /* 状态 */,
   Note                 char(64)             null  /* 备注 */,
   LastModiTime         datetime             not null default GetDate()  /* 最后更新日期 */,
   LastModiOPID         char(8)              not null default ' '  /* 最后修改人 */,
   constraint PK_CLEARCREDITSUBCARD primary key  (CreditSubCardNo)
)
go