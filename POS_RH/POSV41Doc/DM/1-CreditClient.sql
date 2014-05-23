/*==============================================================*/
/* Database name:  ���ʿͻ�                                     */
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
��Ʒ���ͱ�ָ��վ��Ӫ��������Ʒ����*/
create table dbo.OilType (
   OilTypeID            smallint             not null  /* ��Ʒ���ͱ��� */,
   OilTypeName          char(16)             not null  /* ��Ʒ�������� */,
   OilTypeSName         char(8)              null default ' '  /* ��Ʒ��������� */,
   OilGunCNums          int                  not null default 0  /* ��ǹ�� */,
   Note                 char(32)             null default ' '  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ����޸�ʱ�� */,
   LastModiOPID         char(8)              null default ' '  /* ����޸��� */,
   constraint PK_OILTYPE primary key  (OilTypeID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��Ʒ���ͱ�ָ��վ��Ӫ��������Ʒ����', N'user', N'dbo', N'table', N'OilType', NULL, NULL
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��Ʒ����ID�ţ�Ψһ�ؼ���', N'user', N'dbo', N'table', N'OilType', N'column', N'OilTypeID'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��Ʒ��������', N'user', N'dbo', N'table', N'OilType', N'column', N'OilTypeName'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��Ʒ���������', N'user', N'dbo', N'table', N'OilType', N'column', N'OilTypeSName'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��¼����Ʒ���͵���ǹ��', N'user', N'dbo', N'table', N'OilType', N'column', N'OilGunCNums'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��ע', N'user', N'dbo', N'table', N'OilType', N'column', N'Note'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'����޸�ʱ��', N'user', N'dbo', N'table', N'OilType', N'column', N'LastModiTime'
go


EXECUTE sp_addextendedproperty N'MS_Description', N'����޸���', N'user', N'dbo', N'table', N'OilType', N'column', N'LastModiOPID'
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
��Ӫ����*/
create table dbo.BusinessType (
   BusinessTypeID       int                  not null  /* ��Ӫ���ͱ��� */,
   BusinessTypeName     char(20)             not null  /* ��Ӫ�������� */,
   BusinessSName        char(8)              null default ' '  /* ��Ӫ��������� */,
   Note                 char(32)             null  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ����޸�ʱ�� */,
   LastModiOPID         char(8)              not null default ' '  /* ����޸��� */,
   constraint PK_BUSINESSTYPE primary key  (BusinessTypeID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��Ӫ����', N'user', N'dbo', N'table', N'BusinessType', NULL, NULL
go


/*==============================================================*/
/* Table: BusinessTypeLog                                       */
/*==============================================================*/
/*
��Ӫ������־*/
create table dbo.BusinessTypeLog (
   SerialID             int IDENTITY(1,1)    not null  /* ��� */,
   BusinessTypeID       int                  not null  /* ��Ӫ���ͱ��� */,
   BusinessTypeName     char(20)             not null  /* ��Ӫ�������� */,
   BusinessSName        char(8)              null default ' '  /* ��Ӫ��������� */,
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */,
   TranFlag 		int  		     default 0 not null,  --������־
   constraint PK_BUSINESSTYPELOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��Ӫ������־', N'user', N'dbo', N'table', N'BusinessTypeLog', NULL, NULL
go

/*
��Ӫ����*/
create table dbo.BusinessType9 (
   SheetID		char(16)	     not null,  --���ݱ��
   BusinessTypeID       int                  not null,  --��Ӫ���ͱ���
   BusinessTypeName     char(20)             not null,  --��Ӫ��������
   BusinessSName        char(8)              null default ' ',  --��Ӫ���������
   Note                 char(32)             null,  --��ע
   TranFlag             int                  null default 0,  --������־
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
���ͻ������ϱ�*/
create table dbo.CarType (
   CarTypeID            int                  not null  /* ���ͱ��� */,
   CarTypeName          char(32)             not null  /* �������� */,
   CarTypeSName         char(16)             null  /* ��������� */,
   OilTankCapacity      decimal(12,4)        null  /* �������� */,
   Note                 char(32)             null  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ����޸�ʱ�� */,
   LastModiOPID         char(8)              not null default ' '  /* ����޸��� */,
   constraint PK_CARTYPE primary key  (CarTypeID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ͻ������ϱ�', N'user', N'dbo', N'table', N'CarType', NULL, NULL
go


/*==============================================================*/
/* Table: CarTypeLog                                            */
/*==============================================================*/
/*
���ͻ������ϱ���־*/
create table dbo.CarTypeLog (
   SerialID             int IDENTITY(1,1)    not null  /* ��� */,
   CarTypeID            int                  not null  /* ���ͱ��� */,
   CarTypeName          char(32)             not null  /* �������� */,
   CarTypeSName         char(16)             null  /* ��������� */,
   OilTankCapacity      decimal(12,4)        null  /* �������� */,
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */,
   TranFlag 		int  		     default 0 not null,   --������־
   constraint PK_CARTYPELOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ͻ������ϱ���־', N'user', N'dbo', N'table', N'CarTypeLog', NULL, NULL
go

create table dbo.CarType9 (
   SheetID		char(16)	     not null,  --���ݱ��
   CarTypeID            int                  not null,   --���ͱ���
   CarTypeName          char(32)             not null,   --��������
   CarTypeSName         char(16)             null,       --���������
   OilTankCapacity      decimal(12,4)        null,       --��������
   Note                 char(32)             null,       --��ע
   TranFlag             int                  null default 0,  --������־
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
���ʿͻ���������ʽ���ɿ��ս�����0��д�����ʽ��*/
create table dbo.CardAcc (
   SerialID             int                  not null  /* ����������� */,
   MemberID             char(20)             not null  /* ��Ա�� */,
   CardNo               char(19)             not null  /* ���� */,
   CreditSubCardNo      char(19)             not null default ' '  /* ���ѵĹ��ʿ��� */,
   ShopID               char(4)              not null  /* ������� */,
   OccurDate            datetime             not null default getDate()  /* ����ʱ�� */,
   RecordDate           datetime             not null default getdate()  /* ����ʱ�� */,
   DirectFlag           int                  not null  /* ���˷��� */,
   Value                decimal(12,2)        null  /* ������ */,
   ResultValue          decimal(12,2)        null  /* ���� */,
   Point                dec(12,2)            null  /* �������� */,
   ResultPoint          dec(12,2)            null  /* ������ */,
   SheetID              char(16)             not null  /* ���ݱ�� */,
   SheetType            int                  not null  /* �������� */,
   Note                 char(64)             null  /* ��ע */,
   constraint PK_CARDACC primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ʿͻ���������ʽ���ɿ��ս�����0��д�����ʽ��', N'user', N'dbo', N'table', N'CardAcc', NULL, NULL
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
���ʿͻ������ʱ�*/
create table dbo.CardAcc0 (
   SerialID             int IDENTITY(1,1)    not null  /* ����������� */,
   MemberID             char(20)             not null  /* ��Ա�� */,
   CardNo               char(19)             not null  /* ���� */,
   CreditSubCardNo      char(19)             not null default ' '  /* ���ѵĹ��ʿ��� */,
   ShopID               char(4)              not null  /* ������� */,
   OccurDate            datetime             not null default getDate()  /* ����ʱ�� */,
   RecordDate           datetime             not null default getdate()  /* ����ʱ�� */,
   DirectFlag           int                  not null  /* ���˷��� */,
   Value                decimal(12,2)        null  /* ������ */,
   ResultValue          decimal(12,2)        null  /* ���� */,
   Point                dec(12,2)            null  /* �������� */,
   ResultPoint          dec(12,2)            null  /* ������ */,
   SheetID              char(16)             not null  /* ���ݱ�� */,
   SheetType            int                  not null  /* �������� */,
   Note                 char(64)             null  /* ��ע */,
   constraint PK_CARDACC0 primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ʿͻ������ʱ�', N'user', N'dbo', N'table', N'CardAcc0', NULL, NULL
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
���ʿͻ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�*/
create table dbo.CreditClient (
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ������� */,
   IDCard               char(18)             not null  /* ��ϵ�����֤�� */,
   MemberID             char(20)             not null  /* ��Ա���� */,
   BusinessTypeID       int                  not null  /* ��Ӫ���ͱ��� */,
   CardType             char(2)              not null  /* ������ */,
   Gue_PayMoney         decimal(12,2)        not null default 0.00  /* ������_�ۼ����ѽ�� */,
   CreditClientTypeID   int                  not null  /* ���ʿͻ����� */,
   CustomNo             char(16)             not null  /* ���ͱ���� */,
   Name                 char(50)             not null  /* ���� */,
   TeleNo               char(20)             null  /* �绰 */,
   Mobile               char(20)             null  /* ��ϵ���ֻ� */,
   PostalCode           char(6)              null default ' '  /* �������� */,
   Address              char(64)             not null default ' '  /* ��ַ */,
   InCharger            char(8)              not null default ' '  /* ������ */,
   LinkMan              char(8)              not null default ' '  /* ��ϵ�� */,
   FaxNo                char(20)             not null default ' '  /* ��˾���� */,
   FinanceNo		char(20)	     not null,		  --�������  
   PayMoney             decimal(12,2)        not null  /* �ۼ����ѽ�� */,
   Deposit              decimal(12,2)        not null default 0.00  /* Ѻ�� */,
   DiscCount 		decimal(12,2) not null default 0,  --���ʿͻ���Ʒ�Żݶ��
   StartDate            datetime             not null  /* �������� */,
   Status               char(1)              not null  /* ״̬ */,
   Note                 char(64)             null  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ���������� */,
   LastModiOPID         char(8)              not null default ' '  /* ����޸��� */,
   DiscCount 		decimal(12, 2),
   constraint PK_CREDITCLIENT primary key  (CardNo)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ʿͻ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�', N'user', N'dbo', N'table', N'CreditClient', NULL, NULL
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
���ʿͻ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�*/
create table dbo.CreditClientLog (
   SerialID             int IDENTITY(1,1)    not null  /* ��� */,
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ������� */,
   IDCard               char(18)             not null  /* ��ϵ�����֤�� */,
   MemberID             char(20)             not null  /* ��Ա���� */,
   BusinessTypeID       int                  not null  /* ��Ӫ���ͱ��� */,
   CardType             char(2)              not null  /* ������ */,
   Gue_PayMoney         decimal(12,2)        not null default 0.00  /* ������_�ۼ����ѽ�� */,
   CreditClientTypeID   int                  not null  /* ���ʿͻ����� */,
   CustomNo             char(16)             not null  /* ���ͱ���� */,
   Name                 char(50)             not null  /* ���� */,
   TeleNo               char(20)             null  /* �绰 */,
   Mobile               char(20)             null  /* ��ϵ���ֻ� */,
   PostalCode           char(6)              null default ' '  /* �������� */,
   Address              char(64)             not null default ' '  /* ��ַ */,
   InCharger            char(8)              not null default ' '  /* ������ */,
   LinkMan              char(8)              not null default ' '  /* ��ϵ�� */,
   FaxNo                char(20)             not null default ' '  /* ��˾���� */,
   PayMoney             decimal(12,2)        not null  /* �ۼ����ѽ�� */,
   Deposit              decimal(12,2)        not null default 0.00  /* Ѻ�� */,
   FinanceNo            char(20)             not null  /* ������� */,
   DiscCount 		decimal(12,2) not null default 0,  --���ʿͻ���Ʒ�Żݶ��
   StartDate            datetime             not null  /* �������� */,
   Status               char(1)              not null  /* ״̬ */,
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */,
   TranFlag 		int  		     default 0 not null,  --������־	
   constraint PK_CREDITCLIENTLOG primary key  (SerialID)
)
go



EXECUTE sp_addextendedproperty N'MS_Description', N'���ʿͻ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�', N'user', N'dbo', N'table', N'CreditClientLog', NULL, NULL
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
���ʿ���������������־��*/
create table dbo.CreditClient_CLog (
   SerialID             int IDENTITY(1,1)    not null  /* ��� */,
   CreditClientSubCardNo char(25)             not null  /* ���ѵĹ��ʿ��� */,
   DeptID               int                  not null  /* ������ */,
   IsAllowedUse         int                  null  /* �Ƿ��������� */,
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */,
   TranFlag 		int  		     default 0 not null,  --������־
   constraint PK_CREDITCLIENT_CLOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ʿ���������������־��', N'user', N'dbo', N'table', N'CreditClient_CLog', NULL, NULL
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
���ʿ�������������*/
create table dbo.CreditClient_C (
   CreditClientSubCardNo char(25)             not null  /* ���ѵĹ��ʿ��� */,
   DeptID               int                  not null  /* ������ */,
   IsAllowedUse         int                  null  /* �Ƿ��������� */,
   Note                 char(32)             null  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ����޸�ʱ�� */,
   LastModiOPID         char(8)              not null default ' '  /* ����޸��� */,
   constraint PK_CreditClient_C primary key  (CreditClientSubCardNo, DeptID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ʿ�������������', N'user', N'dbo', N'table', N'CreditClient_C', NULL, NULL
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
���ʿͻ���������վ���*/
create table dbo.CreditClient_Shop (
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ������� */,
   IsAllowedUse         int                  not null  /* �Ƿ��������� */,
   Note                 char(32)             null  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ����޸�ʱ�� */,
   LastModiOPID         char(8)              not null default ' '  /* ����޸��� */,
   constraint PK_CREDITCLIENT_SHOP primary key  (CardNo, LastModiOPID, ShopID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ʿͻ���������վ���', N'user', N'dbo', N'table', N'CreditClient_Shop', NULL, NULL
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
���ʿ����ϱ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�*/
create table dbo.CreditSubCard (
   CreditSubCardNo      char(25)             not null  /* ���ѵĹ��ʿ��� */,
   CarTypeID            int                  not null  /* ���ͱ��� */,
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ���ڵ�� */,
   CustomNo             char(16)             not null  /* С�ͱ���� */,
   CardVerifyCode       char(7)              null  /* ��У���� */,
   IsNeededVerify       int                  null  /* �Ƿ���ҪУ�鿨У���� */,
   IsEncryVerifyCode    int                  null  /* ��У�����Ƿ���� */,
   CarID                char(10)             not null  /* ���� */,
   DriverName           char(8)              null  /* ˾������ */,
   MaxOilQtyPerTrans    decimal(12,4)        not null  /* ÿ����������������Ʒ������ */,
   Credit               decimal(12,2)        not null  /* ���ʿͻ��Ŵ���� */,
   Balance              decimal(12,2)        not null  /* ���ʿ���� */,
   PayMoney             decimal(12,2)        not null  /* �ۼ����ѽ�� */,
   StartDate            datetime             not null  /* �������� */,
   OilTypeID	       smallint                not null,		-- ��Ʒ���ͱ��� 
   Status               char(1)              not null  /* ״̬ */,
   Note                 char(64)             null  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ���������� */,
   LastModiOPID         char(8)              not null default ' '  /* ����޸��� */,
   constraint PK_CREDITSUBCARD primary key  (CreditSubCardNo)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ʿ����ϱ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�', N'user', N'dbo', N'table', N'CreditSubCard', NULL, NULL
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
���ʿ����ϱ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�*/
create table dbo.CreditSubCardLog (
   SerialID             int IDENTITY(1,1)    not null  /* ��� */,
   CreditSubCardNo      char(25)             not null  /* ���ѵĹ��ʿ��� */,
   CarTypeID            int                  not null  /* ���ͱ��� */,
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ���ڵ�� */,
   CustomNo             char(16)             not null  /* С�ͱ���� */,
   CardVerifyCode       char(7)              null  /* ��У���� */,
   IsNeededVerify       int                  null  /* �Ƿ���ҪУ�鿨У���� */,
   IsEncryVerifyCode    int                  null  /* ��У�����Ƿ���� */,
   CarID                char(10)             not null  /* ���� */,
   DriverName           char(8)              null  /* ˾������ */,
   MaxOilQtyPerTrans    decimal(12,4)        not null  /* ÿ����������������Ʒ������ */,
   Credit               decimal(12,2)        not null  /* ���ʿͻ��Ŵ���� */,
   Balance              decimal(12,2)        not null  /* ���ʿ���� */,
   PayMoney             decimal(12,2)        not null  /* �ۼ����ѽ�� */,
   StartDate            datetime             not null  /* �������� */,
   Status               char(1)              not null  /* ״̬ */,
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */,
   TranFlag 		int  		     default 0 not null,  --������־		
   OilTypeID 		smallint  	     default 0 not null,  --�ο���Ʒ
   constraint PK_CREDITSUBCARDLOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'���ʿ����ϱ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�', N'user', N'dbo', N'table', N'CreditSubCardLog', NULL, NULL
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
��ϵͳ���õ���������ػ�����Ϣ���ֶ�����*/
create table dbo.TabFldDesc (
   TabName              char(30)             not null  /* ���� */,
   FldName              char(30)             not null  /* �ֶ��� */,
   FldSName             char(32)             not null  /* �ֶ���ʾ��Ϣ */,
   FldOrder             int                  null  /* �ֶ���ʾ��� */,
   DataType             smallint             not null default 1  /* �ֶ��������� */,
   DataLen              smallint             not null  /* �ֶ����ݳ��� */,
   isAllowNull          smallint             not null default 1  /* �Ƿ�����Ϊ�� */,
   Note                 char(32)             null default ' '  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ����޸�ʱ�� */,
   LastModiOPID         char(8)              null default ' '  /* ����޸��� */,
   constraint PK_TABFLDDESC primary key  (TabName, FldName)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��ϵͳ���õ���������ػ�����Ϣ���ֶ�����', N'user', N'dbo', N'table', N'TabFldDesc', NULL, NULL
go


/*==============================================================*/
/* Table: TabFldDescLog                                         */
/*==============================================================*/
/*
��ϵͳ���õ���������ػ�����Ϣ���ֶ�����*/
create table dbo.TabFldDescLog (
   SerialID             int IDENTITY(1,1)    not null  /* ���к� */,
   FldName              char(30)             not null  /* �ֶ��� */,
   FldSName             char(32)             not null  /* �ֶ���ʾ��Ϣ */,
   FldOrder             int                  null  /* �ֶ���ʾ��� */,
   DataType             smallint             not null default 1  /* �ֶ��������� */,
   DataLen              smallint             not null  /* �ֶ����ݳ��� */,
   TranFlag             int                  not null default 0  /* ������־ */,
   isAllowNull          smallint             not null default 1  /* �Ƿ�����Ϊ�� */,
   Note                 char(32)             null default ' '  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */,
   constraint PK_TABFLDDESCLOG primary key  (SerialID)
)
go


EXECUTE sp_addextendedproperty N'MS_Description', N'��ϵͳ���õ���������ػ�����Ϣ���ֶ�����', N'user', N'dbo', N'table', N'TabFldDescLog', NULL, NULL
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


 
--���ʿ��������ݱ�  OpenCard0,OpenCardItem0,OpenCardPayType0 ,OpenCard,OpenCardItem,OpenCardPayType

--���ʿ���ʧ���ݱ� LostClientCard0,LostClientCardItem0,LostClientCard,LostClientCardItem
--���ʿ����ʧ���ݱ� UnLostClientCard0,UnLostClientCardItem0,UnLostClientCard,UnLostClientCardItem

--���ʿ����ᵥ�ݱ� FreezeClientCard0,FreezeClientItem0,FreezeClientCard,FreezeClientCardItem
--���ʿ��ⶳ�ᵥ�ݱ� UnFreezeClientCard0,UnFreezeClientCardItem0,UnFreezeClientCard,UnFreezeClientCardItem

--���ʿ�����ֵ���ݱ� CreditMoneyAdd0,CreditMoneyAddItem0,CreditMoneyAdd,CreditMoneyAddItem
--���ʿ����ʵ��ݱ�	CreditSubCardClear0,CreditSubCardClearitem0,CreditSubCardClear,CreditSubCardClearitem

 
--���ʿ��������ݱ� OpenCard0,OpenItem0,OpenCardPayType0 ,OpenCard,OpenCardItem,OpenCardPayType

--���ʿ��廧���ݱ�	CreditCardCancel0,CreditCardCancelItem0,CreditCardCancel,CreditCardCancelitem
--���ʿ��������ѱ��ݱ�	GuestClear
--���ʿ������廧���ݱ�	ClearCreditClient		
--���ʿ��ӿ��廧���ݱ�	ClearCreditSubCard
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
  SheetID       char(16)        not null primary key,           --��������
  ShopID        char(4)         not null,                       --�������
  Flag          integer         default 0 not null,             --ȷ�ϱ�־ 0-�Ƶ�1-ȷ��  99=��ȡ�� 100=����,������������ʽ��ʱ�������ֱ�־,99��100
  PartFlag      smallint        default 0 not null,             --����ǰ�����־ 0--��ͨ����,1--��ǰ����
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����  
  BusinessTypeID       int      not null,			--��Ӫ���ͱ��
  Deposit       Money               null,			-- Ѻ�� 
  CreditClientTypeID   int      not null,  			--���ʿͻ�����  0=Ԥ����ͻ���1=�Ŵ��ͻ�
  Credit        Money           not null, 			-- ���ʿͻ��Ŵ����   
  TeleNo	char(20),					-- ��ϵ�绰
  Mobile	char(20),					-- �ֻ���
  WorkAddr	char(40),					-- ������λ
  Address	char(64),					-- ��ס��ַ
  InCharger	char(8),					--������
  IDCard	char(18)	not null,			-- ���������֤����
  PostalCode	char(6),					-- ��������
  LinkMan	char(8),					--��ϵ��
  FaxNo		char(20),					--����
  Email		char(32),					-- Email��ַ
  Memo 		char(255),       				--��ע  
  CardNo	char(25)	not null,			--����
  CardType      char(2)         not null,                       --������    
  CustomNo	char(16)	not null,			--���ͱ����  
  secrety 	char(7)  	not null ,            		--������  
  Money		dec(12,2)	default 0 not null,		--���ʿ����  
  DiscountType  smallint 	not null default(0),		--0�����ۿۣ�1������ۿۣ�2����ֵ�ۿ�
  Discountrate  int  		default 100 not null,	        --�����ۿ���
  Discountvalue decimal(8,2)  	default 0 not null ,		--�����ۿ۽��
  Totalvalue    decimal(10,2)   default 0 not null ,		--�����ۿ۽��  
  Paymoney 	decimal(10,2) 	not null ,			--���ܽ��
  FinanceNo	char(20)	not null,		  	--�������
  DiscCount 	decimal(12,2) not null default 0,  		--���ʿͻ���Ʒ�Żݶ��
  Indate 	datetime      	default getdate() not null ,  	--��������
  EndDate        datetime,  					--��Ч����ֹ����  
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null,                           --ȷ������
  auditer 	char(8),					--���������
  auditdate 	datetime, 					--��������� 
);
go

--OpenCardItem0
create table OpenCardItem0(
  SheetID       char(16)        not null,                       --��������
  CarTypeID            int      not null,			--  ���ͱ���
  CardNo               char(25)             not null,  		--���� 
  CustomNo             char(16)             not null,  		--���ͱ����
  CardVerifyCode       char(17)             null, 		--��У����
  IsNeededVerify       int                  null,  		-- �Ƿ���ҪУ�鿨У���� 
  IsEncryVerifyCode    int                  null,  		--��У�����Ƿ���� 
  CarID                char(10)             not null,		--����
  DriverName           char(8)              null,		--˾������
  MaxOilQtyPerTrans    dec(12,4)            not null,		--ÿ����������������Ʒ������
  OilTypeID	       smallint             not null,		-- ��Ʒ���ͱ��� 
  Credit               Money                not null,		--���ʿͻ��Ŵ����
  Memo 		char(255),       				--��ע     
  primary key(SheetID,CardNo),
  foreign key(SheetID) references OpenCard0(SheetID)
);
go

--OpenCardPayType0 
create table Opencardpaytype0(
    sheetid 	char(16)      not null ,                        --���ݱ��
    itemid 	int identity  not null ,                        --���к�
    paytype 	char(1)	      not null ,                        --֧�����ͱ��
    CurrentCode char(3)       not null ,                	--����
    checkno 	char(6),					--֧Ʊ��
    orgvalue 	dec(10,2),					--ԭ��
    value 	dec(10,2)     default 0.00 not null ,		--����(ԭ��*����)
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
  SheetID       char(16)        not null primary key,           --��������
  ShopID        char(4)         not null,                       --�������
  Flag          integer         default 0 not null,             --ȷ�ϱ�־ 0-�Ƶ�1-ȷ��  99=��ȡ�� 100=����,������������ʽ��ʱ�������ֱ�־,99��100
  PartFlag      smallint        default 0 not null,             --����ǰ�����־ 0--��ͨ����,1--��ǰ����
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����
  BusinessTypeID       int      not null,			--��Ӫ���ͱ��
  Deposit       Money               null,			-- Ѻ�� 
  CreditClientTypeID   int      not null,  			--���ʿͻ�����0=Ԥ����ͻ���1=�Ŵ��ͻ�
  Credit        Money           not null, 			-- ���ʿͻ��Ŵ����   
  TeleNo	char(20),					-- ��ϵ�绰
  Mobile	char(20),					-- �ֻ���
  WorkAddr	char(40),					-- ������λ
  Address	char(64),					-- ��ס��ַ
  InCharger	char(8),					--������
  IDCard	char(18)	not null,			-- ���������֤����
  PostalCode	char(6),					-- ��������
  LinkMan	char(8),					--��ϵ��
  FaxNo		char(20),					--����
  Email		char(32),					-- Email��ַ
  Memo 		char(255),       				--��ע  
  CardNo	char(25)	not null,			--����
  CardType      char(2)         not null,                       --������    
  CustomNo	char(16)	not null,			--���ͱ����  
  secrety 	char(7)  	not null ,            		--������  
  Money		dec(12,2)	default 0 not null,		--���ʿ����  
  DiscountType  smallint 	not null default(0),		--0�����ۿۣ�1������ۿۣ�2����ֵ�ۿ�
  Discountrate  int  		default 100 not null,	        --�����ۿ���
  Discountvalue decimal(8,2)  	default 0 not null ,		--�����ۿ۽��
  Totalvalue    decimal(10,2)   default 0 not null ,		--�����ۿ۽��  
  Paymoney 	decimal(10,2) 	not null ,			--���ܽ��
  FinanceNo	char(20)	not null,			--�������  
  DiscCount 	decimal(12,2) not null default 0,  		--���ʿͻ���Ʒ�Żݶ��
  Indate 	datetime      	default getdate() not null ,  	--��������
  EndDate        datetime,  					--��Ч����ֹ����  
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null,                           --ȷ������
  auditer 	char(8),					--���������
  auditdate 	datetime, 					--��������� 
);
go

--OpenCardItem
create table OpenCardItem(
  SheetID       char(16)        not null,                       --��������
  CarTypeID            int      not null,			--  ���ͱ���
  CardNo               char(25)             not null,  		--���� 
  CustomNo             char(16)            not null,  		--���ͱ����
  CardVerifyCode       char(7)             null, 		--��У����
  IsNeededVerify       int                  null,  		-- �Ƿ���ҪУ�鿨У���� 
  IsEncryVerifyCode    int                  null,  		--��У�����Ƿ���� 
  CarID                char(10)             not null,		--����
  DriverName           char(8)              null,		--˾������
  MaxOilQtyPerTrans    dec(12,4)            not null,		--ÿ����������������Ʒ������
  OilTypeID	       smallint             not null,		-- ��Ʒ���ͱ��� 
  Credit               Money                not null,		--���ʿͻ��Ŵ����
  Memo 		char(255),       				--��ע     
  primary key(SheetID,CardNo),
  foreign key(SheetID) references OpenCard(SheetID)
);
go

--OpenCardPayType 
create table Opencardpaytype(
    sheetid 	char(16)      not null ,                        --���ݱ��
    itemid 	int           not null ,                        --���к�
    paytype 	char(1)	      not null ,                        --֧�����ͱ��
    CurrentCode 	char(3)       not null ,                --����
    checkno 	char(6),					--֧Ʊ��
    orgvalue 	dec(10,2),					--ԭ��
    value 	dec(10,2)     default 0.00 not null ,		--����(ԭ��*����)
    primary key (sheetid,itemid), 
    foreign key(SheetID) references OpenCard(SheetID)    
);
go


--���ʿ���ʧ���ݱ� LostClientCard0,LostClientCardItem0,LostClientCard,LostClientCardItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.LostClientCardItem0') and   type = 'U')
   drop table LostClientCardItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.LostClientCard0') and   type = 'U')
   drop table LostClientCard0
go
create table LostClientCard0(
  SheetID       char(16)        not null primary key,           --��ʧ����
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽ��ʧ 100-ȷ�� 
  LostFlag      char(1)         default 'l' not null,           --��ʧ��־ l-��ʧ��
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����
  Reason        char(30)        null,                           --ԭ��
  OTReason      char(30)        null,                           --����ԭ��
  Owner         char(8)         null,                           --��ʧ������
  OwnerID       char(18)        null,                           --��ʧ�����֤����
  OwnerTele     varchar(20)     null,                           --��ʧ����ϵ�绰
  Police        Varchar(30)     null,				--�ɳ�������
  PoliceTele    Varchar(20)     null,				--�ɳ����绰
  Editor        char(8)         null,                           --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

--LostClientCardItem0
create table LostClientCardItem0(
  SheetID       char(16)        not null,                       --��ʧ����
  Flag		int	default 0 not null,			--���ʿ���־ 0=���ʿ� 1=���ʿ���־
  CardNO        char(19)        not null,                       --����
  CustomNo      char(16)        not null,  			--���ͱ����    
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
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
  SheetID       char(16)        not null primary key,           --��ʧ����
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽ��ʧ 100-ȷ�� 
  LostFlag      char(1)         default 'l' not null,           --��ʧ��־ l-��ʧ��
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����
  Reason        char(30)        null,                           --ԭ��
  OTReason      char(30)        null,                           --����ԭ��
  Owner         char(8)         null,                           --��ʧ������
  OwnerID       char(18)        null,                           --��ʧ�����֤����
  OwnerTele     varchar(20)     null,                           --��ʧ����ϵ�绰
  Police        Varchar(30)     null,				--�ɳ�������
  PoliceTele    Varchar(20)     null,				--�ɳ����绰
  Editor        char(8)         null,                           --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

--LostClientCardItem
create table LostClientCardItem(
  SheetID       char(16)        not null,                       --��ʧ����
  Flag		int	default 0 not null,			--���ʿ���־ 0=���ʿ� 1=���ʿ���־
  CardNO        char(19)        not null,                       --����
  CustomNo      char(16)        not null,  			--���ͱ����    
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  primary key(SheetID,CardNO),
  foreign key(SheetID) references LostClientCard(SheetID)
);
go

--���ʿ����ʧ���ݱ� UnLostClientCard0,UnLostClientCardItem0,UnLostClientCard,UnLostClientCardItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnLostClientCardItem0') and   type = 'U')
   drop table UnLostClientCardItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnLostClientCard0') and   type = 'U')
   drop table UnLostClientCard0
go
create table UnLostClientCard0(
  SheetID       char(16)        not null primary key,           --���ʧ����
  LostSheetID   char(16)        null,                           --��ʧ����
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����  
  Owner         char(8)         null,                           --��ʧ��
  OwnerID       char(18)        null,                           --��ʧ�����֤����
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table UnLostClientCardItem0(
  SheetID       char(16)        not null,                       --��ʧ����
  Flag		int	default 0 not null,			--���ʿ���־ 0=���ʿ� 1=���ʿ���־  
  CardNO        char(19)        not null,                       --����
  CustomNo      char(16)        not null,  			--���ͱ����      
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
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
  SheetID       char(16)        not null primary key,           --���ʧ����
  LostSheetID   char(16)        null,                           --��ʧ����
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����  
  Owner         char(8)         null,                           --��ʧ��
  OwnerID       char(18)        null,                           --��ʧ�����֤����
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table UnLostClientCardItem(
  SheetID       char(16)        not null,                       --��ʧ����
  Flag		int	default 0 not null,			--���ʿ���־ 0=���ʿ� 1=���ʿ���־  
  CardNO        char(19)        not null,                       --����
  CustomNo      char(16)        not null,  			--���ͱ����      
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  primary key(SheetID,CardNO),
  foreign key(SheetID) references UnLostClientCard(SheetID)
);
go

--���ʿ����ᵥ�ݱ� FreezeClientCard0,FreezeClientItem0,FreezeClientCard,FreezeClientCardItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.FreezeClientItem0') and   type = 'U')
   drop table FreezeClientItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.FreezeClientCard0') and   type = 'U')
   drop table FreezeClientCard0
go
create table FreezeClientCard0(
  SheetID       char(16)        not null primary key,           --���ᵥ��
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����    
  Reason        char(20)        not null,                       --ԭ��
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table FreezeClientItem0(
  SheetID       char(16)        not null,                       --��ʧ����
  Flag		int	default 0 not null,			--���ʿ���־ 0=���ʿ� 1=���ʿ���־  
  CardNO        char(19)        not null,                       --����
  CustomNo      char(16)        not null,  			--���ͱ����  
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
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
  SheetID       char(16)        not null primary key,           --���ᵥ��
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����    
  Reason        char(20)        not null,                       --ԭ��
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table FreezeClientItem(
  SheetID       char(16)        not null,                       --��ʧ����
  Flag		int	default 0 not null,			--���ʿ���־ 0=���ʿ� 1=���ʿ���־  
  CardNO        char(19)        not null,                       --����
  CustomNo      char(16)        not null,  			--���ͱ����    
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  primary key(SheetID,CardNO),
  foreign key(SheetID) references FreezeClientCard(SheetID)  
);
go

--���ʿ��ⶳ�ᵥ�ݱ� UnFreezeClientCard0,UnFreezeClientCardItem0,UnFreezeClientCard,UnFreezeClientCardItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnFreezeClientCardItem0') and   type = 'U')
   drop table UnFreezeClientCardItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.UnFreezeClientCard0') and   type = 'U')
   drop table UnFreezeClientCard0
go
create table UnFreezeClientCard0(
  SheetID       char(16)        not null primary key,           --�ⶳ�ᵥ��
  FreeSheetID   char(16)        null,                           --���ᵥ��
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 100-ȷ��
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����    
  Reason        char(20)        not null,                       --ԭ��
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table UnFreezeClientCardItem0(
  SheetID       char(16)        not null,                       --��ʧ����
  Flag		int	default 0 not null,			--���ʿ���־ 0=���ʿ� 1=���ʿ���־  
  CardNO        char(19)        not null,                       --����
  CustomNo      char(16)        not null,  			--���ͱ����    
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
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
  SheetID       char(16)        not null primary key,           --�ⶳ�ᵥ��
  FreeSheetID   char(16)        null,                           --���ᵥ��
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 100-ȷ��
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����    
  Reason        char(20)        not null,                       --ԭ��
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table UnFreezeClientCardItem(
  SheetID       char(16)        not null,                       --��ʧ����
  Flag		int	default 0 not null,			--���ʿ���־ 0=���ʿ� 1=���ʿ���־  
  CardNO        char(19)        not null,                       --����
  CustomNo      char(16)        not null,  			--���ͱ����    
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  primary key(SheetID,CardNO),
  foreign key(SheetID) references UnFreezeClientCard(SheetID)
);
go



--���ʿ�����ֵ���ݱ� CreditMoneyAdd0,CreditMoneyAddItem0,CreditMoneyAdd,CreditMoneyAddItem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMoneyAddItem0') and   type = 'U')
   drop table CreditMoneyAddItem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMoneyAdd0') and   type = 'U')
   drop table CreditMoneyAdd0
go
create table CreditMoneyAdd0(
  SheetID       char(16)        not null primary key,           --����
  ShopID        char(4)         not null,                       --��ֵ���
  ClearFlag 	smallint 	not null default 1,	--���ʿ��Ƿ�Ҫ���� 0 = ������,1 = ����
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null,                           --ȷ������
  Flag          int	default 0 not null           		--ȷ�ϱ�־ 0-�Ƶ� 100-ȷ��
);
go

create table CreditMoneyAddItem0(
  sheetid 	char(16) not null ,				--���ݱ��
  cardno	char(19) not null ,				--����
  MemberID      char(20)        not null,                       --�ͻ����
  Name          varchar(50)         null,                           --��˾����
  CustomNo      char(16)        not null,  			--���ͱ����    
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  DiscountRate  int default 100,				--�俨�ۿ���
  DiscountValue dec(10,2),					--�俨�ۿ�ֵ  
  newdetail 	dec(8,2) default 0.00 not null ,		--�俨���ֵ
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
  SheetID       char(16)        not null primary key,           --����
  ShopID        char(4)         not null,                       --��ֵ���
  ClearFlag 	smallint 	not null default 1,		--���ʿ��Ƿ�Ҫ���� 0 = ������,1 = ����
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null,                           --ȷ������
  Flag          int	default 0 not null           		--ȷ�ϱ�־ 0-�Ƶ� 100-ȷ��
);
go

create table CreditMoneyAddItem(
  sheetid 	char(16) not null ,				--���ݱ��
  cardno	char(19) not null ,				--����
  MemberID      char(20)        not null,                       --�ͻ����
  Name          varchar(50)         null,                           --��˾����
  CustomNo      char(16)        not null,  			--���ͱ����    
  SaleShopID    char(4)         null,                           --�������
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  DiscountRate  int default 100,				--�俨�ۿ���
  DiscountValue dec(10,2),					--�俨�ۿ�ֵ  
  newdetail 	dec(8,2) default 0.00 not null ,		--�俨���ֵ
  primary key (sheetid,cardno), 
  foreign key(SheetID) references CreditMoneyAdd(SheetID)
);
go


--������������� PrintCardType
if exists (select 1 from  sysobjects where  id = object_id('dbo.PrintCardType') and   type = 'U')
   drop table PrintCardType
go
create table dbo.PrintCardType 
( 
Name		char(16)	not null primary key,	--���˵�� 
Data		image		null			--��ǩ���ֶ��� 
)
go


--��ǩ�ɴ�ӡ����LabelFields
if exists (select 1 from  sysobjects where  id = object_id('dbo.LabelFields') and   type = 'U')
   drop table LabelFields
go
create table dbo.LabelFields(
labelchsname	char(20)	primary key,		--��ǩ��������
labelfieldname	char(20)	not null		--��ǩ�ֶ�����
);
go



--�����ӡ��������BarcodeConfig
if exists (select 1 from  sysobjects where  id = object_id('dbo.BarcodeConfig') and   type = 'U')
   drop table BarcodeConfig
go
create table dbo.BarcodeConfig(
id			integer		primary key,		--�����ӡ�����
name			char(32)	not null,		--�����ӡ������
CrToEmpty		integer		default 0 not null,	--<CR>Ϊ��(0-��,1-��)
IsBinaryCmd		integer		default 0 not null,	--���ı�������Ƕ���������(0-�ı�����,1-����������)
InitCmd			varchar(255)	,			--��ʼ������
LabelCmdLoopBefore	varchar(255)	,			--�ı���ͼƬ��ѭ��ǰ����
LabelCmdLoop		varchar(255)	,			--�ı���ͼƬ��ѭ������
LabelCmdLoopAfter	varchar(255)	,			--�ı���ͼƬ��ѭ��������
XorImage		integer		default 0 not null,	--ͼ���Ƿ�ҪXor(0-��,1-��)
EAN8BarcodeCmd		varchar(255)	,			--EAN8�����ӡ����
EAN13BarcodeCmd		varchar(255)	,			--EAN13�����ӡ����
Angle0			char(10)	,			--����0������
Angle90			char(10)	,			--����90������
Angle180		char(10)	,			--����180������
Angle270		char(10)	,			--����270������
EndBeginCmd		varchar(255)	,			--������ӡ��ʼ����
EndLoopCmd		varchar(255)	,			--����ѭ����ӡ����
EndAutoLoopCmd		varchar(255)	,			--�����Զ�ѭ����ӡ����(Zebra)
ContinueLoop		integer		default 0 not null	--�Ƿ�ѭ����ӡ,��֧��������ӡʱʹ��(0-��,1-��)
);

go



--���ʿ����ʵ��ݱ�	CreditSubCardClear0,CreditSubCardClearitem0,CreditSubCardClear,CreditSubCardClearitem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSubCardClearitem0') and   type = 'U')
   drop table CreditSubCardClearitem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSubCardClear0') and   type = 'U')
   drop table CreditSubCardClear0
go
create table CreditSubCardClear0(
  SheetID       char(16)        not null primary key,           --���ᵥ��
  refsheetid 	char(16) 	not null default '',  		--��ص��ݱ�� 
  refsheettype 	int 		not null default 0,  		--��ص�������     
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����    
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table CreditSubCardClearitem0(
  SheetID       char(16)        not null,                       --��ʧ����
  CardNO        char(19)        not null,                       --����
  SaleShopID    char(4)         null,                           --�������
  CustomNo      char(16)        not null,  			--���ͱ����  
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
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
  SheetID       char(16)        not null primary key,           --���ᵥ��
  refsheetid 	char(16) 	not null default '',  		--��ص��ݱ�� 
  refsheettype 	int 		not null default 0,  		--��ص�������       
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����    
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table CreditSubCardClearitem(
  SheetID       char(16)        not null,                       --��ʧ����
  CardNO        char(19)        not null,                       --����
  SaleShopID    char(4)         null,                           --�������
  CustomNo      char(16)        not null,  			--���ͱ����  
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditSubCardClear(SheetID)  
);
go


--�����Ŵ����޸ĵ��ݱ�	CreditMain0,CreditMainitem0,CreditMain,CreditMainitem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMainitem0') and   type = 'U')
   drop table CreditMainitem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditMain0') and   type = 'U')
   drop table CreditMain0
go
create table CreditMain0(
  SheetID       char(16)        not null primary key,           --���ᵥ��
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table CreditMainitem0(
  SheetID       char(16)        not null,                       --��ʧ����
  CardNO        char(19)        not null,                       --����
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(30)        not null,  			--��˾����    
  SaleShopID    char(4)         null,                           --�������
  CustomNo      char(16)        not null,  			--���ͱ����  
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
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
  SheetID       char(16)        not null primary key,           --���ᵥ��
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table CreditMainitem(
  SheetID       char(16)        not null,                       --��ʧ����
  CardNO        char(19)        not null,                       --����
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(30)        not null,  			--��˾����    
  SaleShopID    char(4)         null,                           --�������
  CustomNo      char(16)        not null,  			--���ͱ����  
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditMain(SheetID)  
);
go


--�ӿ��Ŵ����޸ĵ��ݱ�	CreditSub0,CreditSubitem0,CreditSub,CreditSubitem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSubitem0') and   type = 'U')
   drop table CreditSubitem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditSub0') and   type = 'U')
   drop table CreditSub0
go
create table CreditSub0(
  SheetID       char(16)        not null primary key,           --���ᵥ��
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����    
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table CreditSubitem0(
  SheetID       char(16)        not null,                       --��ʧ����
  CardNO        char(19)        not null,                       --����
  SaleShopID    char(4)         null,                           --�������
  CustomNo      char(16)        not null,  			--���ͱ����  
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
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
  SheetID       char(16)        not null primary key,           --���ᵥ��
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����    
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table CreditSubitem(
  SheetID       char(16)        not null,                       --��ʧ����
  CardNO        char(19)        not null,                       --����
  SaleShopID    char(4)         null,                           --�������
  CustomNo      char(16)        not null,  			--���ͱ����  
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditSub(SheetID)  
);
go

--���ǩ��� PrintLabelType
if exists (select 1 from  sysobjects where  id = object_id('dbo.PrintLabelType') and   type = 'U')
   drop table PrintLabelType
go
create table dbo.PrintLabelType 
( 
Name		char(16)	not null primary key,	--���˵�� 
Data		image		null			--��ǩ���ֶ��� 
)
go

--��ǩ�ɴ�ӡ����LabelFields
if exists (select 1 from  sysobjects where  id = object_id('dbo.LabelFields') and   type = 'U')
   drop table LabelFields
go
create table dbo.LabelFields(
labelchsname	char(20)	primary key,		--��ǩ��������
labelfieldname	char(20)	not null		--��ǩ�ֶ�����
);
go

--������� PrintBarcodeType
if exists (select 1 from  sysobjects where  id = object_id('dbo.PrintBarcodeType') and   type = 'U')
   drop table PrintBarcodeType
go
create table dbo.PrintBarcodeType 
( 
Name		char(16)	not null primary key,	--���˵�� 
Data		image		null			--��ǩ���ֶ��� 
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

--���ʿͻ���������վ��־�� 
create table dbo.CreditClient_ShopLog (
   SerialID             int IDENTITY(1,1)    not null  /* ��� */,
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ������� */,
   IsAllowedUse         int                  not null  /* �Ƿ��������� */,
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */,
   TranFlag             int                  null default 0  /* ������־ */,
   constraint PK_CREDITCLIENT_SHOPLOG primary key  (SerialID)
)
go

if exists (select * from dbo.sysobjects
	where id = object_id(N'dbo.CreditClient_Shop9') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table CreditClient_Shop9;
go

create table dbo.CreditClient_Shop9 (
   SheetID		char(16)	     not null,	--ͨѶ���ݱ��
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ������� */,
   IsAllowedUse         int                  not null  /* �Ƿ��������� */,
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */
)
go


if exists (select * from dbo.sysobjects
	where id = object_id(N'dbo.CreditClient_C9') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table CreditClient_C9;
go




--���ʿ���������������־��
create table dbo.CreditClient_C9 (
   SheetID		char(16)	     not null,	--ͨѶ���ݱ��
   CreditClientSubCardNo char(25)             not null  /* ���ѵĹ��ʿ��� */,
   DeptID               int                  not null  /* ������ */,
   IsAllowedUse         int                  null  /* �Ƿ��������� */,
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */
)
go

if exists (select * from dbo.sysobjects
	where id = object_id(N'dbo.CreditClient9') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table CreditClient9;
go



--���ʿͻ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�
create table dbo.CreditClient9 (
   SheetID		char(16)	     not null,	--ͨѶ���ݱ��
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ������� */,
   IDCard               char(18)             not null  /* ��ϵ�����֤�� */,
   MemberID             char(20)             not null  /* ��Ա���� */,
   BusinessTypeID       int                  not null  /* ��Ӫ���ͱ��� */,
   CardType             char(2)              not null  /* ������ */,
   Gue_PayMoney         decimal(12,2)        not null default 0.00  /* ������_�ۼ����ѽ�� */,
   CreditClientTypeID   int                  not null  /* ���ʿͻ����� */,
   CustomNo             char(16)             not null  /* ���ͱ���� */,
   Name                 char(30)             not null  /* ���� */,
   TeleNo               char(20)             null  /* �绰 */,
   Mobile               char(20)             null  /* ��ϵ���ֻ� */,
   PostalCode           char(6)              null default ' '  /* �������� */,
   Address              char(64)             not null default ' '  /* ��ַ */,
   InCharger            char(8)              not null default ' '  /* ������ */,
   LinkMan              char(8)              not null default ' '  /* ��ϵ�� */,
   FaxNo                char(20)             not null default ' '  /* ��˾���� */,
   PayMoney             decimal(12,2)        not null  /* �ۼ����ѽ�� */,
   Deposit              decimal(12,2)        not null default 0.00  /* Ѻ�� */,
   FinanceNo            char(20)             not null  /* ������� */,
   DiscCount 		decimal(12,2) not null default 0,  --���ʿͻ���Ʒ�Żݶ��
   StartDate            datetime             not null  /* �������� */,
   Status               char(1)              not null  /* ״̬ */,
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */
)
go

if exists (select * from dbo.sysobjects
	where id = object_id(N'dbo.CreditSubCard9') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table CreditSubCard9;
go


--���ʿ����ϱ���--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�
create table dbo.CreditSubCard9 (
   SheetID		char(16)	     not null,	--ͨѶ���ݱ��
   CreditSubCardNo      char(25)             not null  /* ���ѵĹ��ʿ��� */,
   CarTypeID            int                  not null  /* ���ͱ��� */,
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ���ڵ�� */,
   CustomNo             char(16)             not null  /* С�ͱ���� */,
   CardVerifyCode       char(7)              null  /* ��У���� */,
   IsNeededVerify       int                  null  /* �Ƿ���ҪУ�鿨У���� */,
   IsEncryVerifyCode    int                  null  /* ��У�����Ƿ���� */,
   CarID                char(10)             not null  /* ���� */,
   DriverName           char(8)              null  /* ˾������ */,
   MaxOilQtyPerTrans    decimal(12,4)        not null  /* ÿ����������������Ʒ������ */,
   Credit               decimal(12,2)        not null  /* ���ʿͻ��Ŵ���� */,
   Balance              decimal(12,2)        not null  /* ���ʿ���� */,
   PayMoney             decimal(12,2)        not null  /* �ۼ����ѽ�� */,
   StartDate            datetime             not null  /* �������� */,
   Status               char(1)              not null  /* ״̬ */,
   OilTypeID 		smallint  	     default 0 not null,  --�ο���Ʒ	
   Note                 char(32)             null  /* ��ע */,
   Remark               char(32)             null default ' '  /* �޸ı�ע */,
   ModiAttr             char(1)              not null  /* �޸�ģʽ */,
   ModiTime             datetime             not null default GetDate()  /* �޸�ʱ�� */,
   ModiOPID             char(8)              not null default ' '  /* �޸��� */
)
go


--���ʿ��廧���ݱ�	CreditCardCancel0,CreditCardCancelItem0,CreditCardCancel,CreditCardCancelitem
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditCardCancelitem0') and   type = 'U')
   drop table CreditCardCancelitem0
go
if exists (select 1 from  sysobjects where  id = object_id('dbo.CreditCardCancel0') and   type = 'U')
   drop table CreditCardCancel0
go
create table CreditCardCancel0(
  SheetID       char(16)        not null primary key,           --�廧����
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����  
  CreditClientTypeID   int      not null,			--���ʿͻ�����
  FinanceNo	char(20),		  			--�������      
  CardNo	char(19)	not null,			--���ʿ�
  CustomNo      char(16)        not null,  			--���ͱ����
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����  
  detail        dec(8,2),                                       --�����   
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��
  Deposit       decimal(12,2)   not null default 0.00, 		-- Ѻ��
  StartDate     datetime        not null,			--��������
  Status        char(1)         not null,			--״̬
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table CreditCardCancelitem0(
  SheetID       char(16)        not null,                       --�廧����
  CardNO        char(19)        not null,                       --����
  SaleShopID    char(4)         null,                           --�������
  CustomNo      char(16)        not null,  			--���ͱ����  
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
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
  SheetID       char(16)        not null primary key,           --�廧����
  ShopID        char(4)         not null,                       --�ۿ���
  Flag          int	default 0 not null,           		--ȷ�ϱ�־ 0-�Ƶ� 2-�ѽⶳ�� 100-ȷ�� 
  MemberID	char(20)        not null,			-- �ͻ����
  Name          char(50)        not null,  			--��˾����  
  CreditClientTypeID   int      not null,			--���ʿͻ�����
  FinanceNo	char(20),		  			--�������      
  CardNo	char(19)	not null,			--���ʿ�
  CustomNo      char(16)        not null,  			--���ͱ����
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����  
  detail        dec(8,2),                                       --�����   
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��
  Deposit       decimal(12,2)   not null default 0.00, 		-- Ѻ��
  StartDate     datetime        not null,			--��������
  Status        char(1)         not null,			--״̬
  Editor        char(8)         not null,                       --�Ƶ���
  EditDate      datetime default getdate() not null,            --�Ƶ�����
  Operator      char(8)         not null,                       --ҵ��Ա
  Checker       char(8)         null,                           --ȷ����
  CheckDate     datetime        null                            --ȷ������
);
go

create table CreditCardCancelitem(
  SheetID       char(16)        not null,                       --�廧����
  CardNO        char(19)        not null,                       --����
  SaleShopID    char(4)         null,                           --�������
  CustomNo      char(16)        not null,  			--���ͱ����  
  detail        dec(8,2),                                       --�����
  PayMoney      decimal(12,2)   not null,			--�ۼ����ѽ��  
  Credit        decimal(12,2)   not null,  			--���ʿͻ��Ŵ����
  primary key(SheetID,CardNO),
  foreign key(SheetID) references CreditCardCancel(SheetID)  
);
go	



/*==============================================================*/
/* Table: CreditClientClear                                          */
/*==============================================================*/
/*���ʿͻ������--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�*/
--��ṹͬCreditClient
if exists (select 1 from  sysobjects where  id = object_id('dbo.ClearCreditClient') and   type = 'U')
   drop table ClearCreditClient
go
create table dbo.ClearCreditClient (
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ������� */,
   IDCard               char(18)             not null  /* ��ϵ�����֤�� */,
   MemberID             char(20)             not null  /* ��Ա���� */,
   BusinessTypeID       int                  not null  /* ��Ӫ���ͱ��� */,
   CardType             char(2)              not null  /* ������ */,
   Gue_PayMoney         decimal(12,2)        not null default 0.00  /* ������_�ۼ����ѽ�� */,
   CreditClientTypeID   int                  not null  /* ���ʿͻ����� */,
   CustomNo             char(16)             not null  /* ���ͱ���� */,
   Name                 char(50)             not null  /* ���� */,
   TeleNo               char(20)             null  /* �绰 */,
   Mobile               char(20)             null  /* ��ϵ���ֻ� */,
   PostalCode           char(6)              null default ' '  /* �������� */,
   Address              char(64)             not null default ' '  /* ��ַ */,
   InCharger            char(8)              not null default ' '  /* ������ */,
   LinkMan              char(8)              not null default ' '  /* ��ϵ�� */,
   FaxNo                char(20)             not null default ' '  /* ��˾���� */,
   FinanceNo	char(20),		  			--�������  
   PayMoney             decimal(12,2)        not null  /* �ۼ����ѽ�� */,
   Deposit              decimal(12,2)        not null default 0.00  /* Ѻ�� */,
   StartDate            datetime             not null  /* �������� */,
   Status               char(1)              not null  /* ״̬ */,
   Note                 char(64)             null  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ���������� */,
   LastModiOPID         char(8)              not null default ' '  /* ����޸��� */,
   constraint PK_CLEARCREDITCLIENT primary key  (CardNo)
)
go	 


/*==============================================================*/
/* Table: ClearCreditSubCard                                         */
/*==============================================================*/
/*
���ʿ������������--status��״̬ 1-������,2-δ���ʿ�,r���ѻ��տ� m-һ���ʧ�� l-���ع�ʧ�� f-����e-�ѻ���q-�˿�*/
--��ṹͬCreditSubCard
if exists (select 1 from  sysobjects where  id = object_id('dbo.ClearCreditSubCard') and   type = 'U')
   drop table ClearCreditSubCard
go
create table dbo.ClearCreditSubCard (
   CreditSubCardNo      char(25)             not null  /* ���ѵĹ��ʿ��� */,
   CarTypeID            int                  not null  /* ���ͱ��� */,
   CardNo               char(19)             not null  /* ���� */,
   ShopID               char(4)              not null  /* ���ڵ�� */,
   CustomNo             char(16)             not null  /* С�ͱ���� */,
   CardVerifyCode       char(7)              null  /* ��У���� */,
   IsNeededVerify       int                  null  /* �Ƿ���ҪУ�鿨У���� */,
   IsEncryVerifyCode    int                  null  /* ��У�����Ƿ���� */,
   CarID                char(10)             not null  /* ���� */,
   DriverName           char(8)              null  /* ˾������ */,
   MaxOilQtyPerTrans    decimal(12,4)        not null  /* ÿ����������������Ʒ������ */,
   Credit               decimal(12,2)        not null  /* ���ʿͻ��Ŵ���� */,
   Balance              decimal(12,2)        not null  /* ���ʿ���� */,
   PayMoney             decimal(12,2)        not null  /* �ۼ����ѽ�� */,
   StartDate            datetime             not null  /* �������� */,
   OilTypeID	       smallint                not null,		-- ��Ʒ���ͱ��� 
   Status               char(1)              not null  /* ״̬ */,
   Note                 char(64)             null  /* ��ע */,
   LastModiTime         datetime             not null default GetDate()  /* ���������� */,
   LastModiOPID         char(8)              not null default ' '  /* ����޸��� */,
   constraint PK_CLEARCREDITSUBCARD primary key  (CreditSubCardNo)
)
go