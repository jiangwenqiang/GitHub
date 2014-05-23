------------------------------------------------------------------------------------------------------------------
-- $Author:: Xianshiyang         $  
-- $Date:: 04-08-02 11:49        $  
-- $Modtime:: 04-08-02 11:42     $  
-- $Revision:: 6                 $  
------------------------------------------------------------------------------------------------------------------

/*==============================================================================*/
/*  MCIS3.2�ŵ�POS���ݿ�ṹ(SQLServer��)					*/
/*  ˵��:									*/
/*	�ļ�����dbpos32_s��ʾdbpos��3.2�汾,_s��ʾ��SQLServer��			*/
/*	dbpos:POS�ÿ�(dbpos32_s.sql)						*/
/*  ����: ������ 2002.07.10							*/
/*  ����޸ģ������� 2002.08.27							*/
/*  ע��									*/
/*   1.���ű������ִ�Сд�������鰲װʱʹSQL SERVER�����ִ�Сд 		*/
/*   2.�������ݿ�ʱ���С���ļ�����·������ʵ������޸�         		*/
/*   3.��������������PRI_XXXXX��XXXXXΪ������ǰ����ַ�				*/
/*  �޸ļ�¼:                                                   		*/	
/*      ������ 2002.07.14 �޸��������ݣ�					*/
/*         1�������е�deptnoͳһΪchar(15)					*/
/*         2�����Ĵ洢����getaccurateΪSQL Server�﷨				*/
/*         3�����뽨���ݿ�ű�							*/
/*      ������ 2002.07.21 ����sale_jif,pay_jif 					*/
/*      ������ 2002.07.22 ���ڼ۸������ֶ�x��Ĭ��ֵ��Ϊ1��			*/
/*      ������ 2002.08.14 ����ȡ���ݺš�ȡ���кš�ȡ����������ģ��		*/
/*		�˵�ƽ 2002.08.20 ����rj_pos�洢����                      	*/
/*      ������ 2002.08.25 ����sale_jrep,pay_jrep���serialidΪ�Զ������ֶ�	*/
/*      ������ 2002.08.27 ����ṹ						*/
/*      ������ 2002.09.16 ����Pos_SumMoney���̣�����ͳ��ʵʱͳ��		*/
/*	�Խ�   2003.05.23 APPLE1.0��ֲ						*/
/*==============================================================================*/

--�޸� 2004.06.03 ���� ��ϴ�����
--1����ϴ����� (GoodsCombineProm,GoodsCombineItemProm) ��ҵ�����ٵ��
--ShopClock ����壬����POS��ʲô���
--POS_turn  POS��¼��¼��ȷ���Ƿ񿪻�
--��Ʒ����	OilDept
--���ݵ��붨���	DataImport
--�����޸ı��ʽ����	Expression

SET QUOTED_IDENTIFIER OFF
GO
SET ANSI_NULLS ON 
GO
SET ANSI_NULL_DFLT_ON ON
GO
--������ṹ

/*==============================================================*/
/* Table : clerk_lst ����Ա�б�      ��ǰ̨���ά����           */
/*==============================================================*/
create table dbo.clerk_lst (
clerk_id             char(4)              not null,	--����ԱID
name                 char(8)              not null,	--����Ա����
levelid              smallint             not null,	--����ԱȨ�޼���
auth                 int      default 0   not null,	--�Ƿ��ǿ�е�¼��1���ɣ�������������
max_disc             smallint default 0   not null,	--�����Ȩ�ۿ��ʣ�������Ա���ֹ��ۿ۵���ȨȨ��ʱ�ſ��ã�
passwd               char(8)              not null,	--��������
constraint pri_clerk primary key  (clerk_id)
);
go

/*==============================================================*/
/* Table : right_lst Ȩ�ޱ�                                           */
/*==============================================================*/
create table dbo.right_lst (
power_id             int                  not null,
name                 char(15)             not null,
constraint u160_309 primary key  (power_id)
)
go


/*==============================================================*/
/* Table : clerk_lst ����ԱȨ���嵥��  ��ǰ̨���ά����         */
/*==============================================================*/
create table dbo.power_lst (
levelID              smallint             not null,	--����ID
power_id             int                  not null,	--Ȩ��ID
name                 char(8)              not null,	--Ȩ�����ƣ�δ�ã���right_lst�������֣�
constraint pri_power primary key  (levelID, power_id),
foreign key(power_id) references right_lst(power_id)
);
go

/*==============================================================*/
/* Table : pos_lst POS���б� ��ǰ̨���ά����                   */
/*==============================================================*/
create table dbo.pos_lst (
pos_id               char(4)              not null,	--POS��̨��
logondate            datetime             null,		--��¼����
listno               int                  not null,	--��ǰ��ˮ��
current_op           char(4)              null,		--��ǰ����ԱID
keybd_type           char(3)              null,		--��������
authorizer_id        char(4)              null,		--��Ȩ��ID(δ�ã�
stat                 char(1)              null,		--��ǰ״̬��0�����С�1����ǩ����9��������
max_disc             smallint             not null,	--δ��
current_level        smallint             not null,	--δ��
desp                 char(20)             null,		--������
ip                   char(15)             null,		--IP��ַ
mac_addr             char(24)             null,		--MAC��ַ
placeno              char(6)    	  null,		--����ţ�NULL:�ǹ�������ǿգ����л�
posgroup             char(8)    default 'BASIC'  not null,	--POS�����
ShiftID 	     int	default 1 not null,   		--�����Ϣ
ShiftStatus 	     int	default 0 not null,   		--���״̬ 0=����ʼ,1�����
constraint pri_pos_l primary key  (pos_id)
);
go

/*==============================================================*/
/* Table : keybd_cfg:���̹���ӳ���    ��ǰ̨���ά����         */
/*==============================================================*/
create table dbo.keybd_cfg (
keybd_type           char(3)              not null,	--��������
key_value            smallint             not null,	--����ɨ����ֵ
keymap_value         smallint             not null,	--���ܺ�
constraint pri_keybd primary key  (keybd_type, key_value)
);
go

/*==============================================================*/
/* Table : keybd_lst�������Ͷ����  ��ǰ̨���ά����            */
/*==============================================================*/
create table dbo.keybd_lst (
keybd_type           char(3)      	  not null,	--��������
keybd_class          char(3)              not null,	--��������������
keybd_desp           char(20)             null,		--������
note                 char(80)             null,		--˵��
constraint u105_22 primary key  (keybd_type)
);
go

/*==============================================================*/
/* Table : function_lst��POS�����б� ��ʼ��ʱά��               */
/*==============================================================*/
create table dbo.function_lst (
keymap_value         smallint             not null,	--���̹��ܺţ�ASCII�ַ���
fun                  char(20)             not null,	--����˵��(Ӣ�ģ�
hzfun                char(20)             not null,	--����˵��(���ģ�
constraint pri_funct primary key  (keymap_value)
);
go


/*==============================================================*/
/* Table : rate_lst���ֻ��ʱ�ǰ̨���ά����                   */
/*==============================================================*/
create table dbo.rate_lst (
ffx                  char(3)              not null,	--�ұ���ʴ���ID��156/934�ȣ�
curren_code          char(3)              not null,	--�ұ���루RMB/USD/HKD�ȣ�
curren_name          char(10)             not null,	--�ұ����ƣ���Ԫ/�����/�۱ҵȣ�
unit_name            char(6)              not null,	--�ұ�λ��Ԫ��
rate                 decimal(10,7)        not null,	--������ҵĻ��ʣ����磺1.0700000)
constraint pri_rate_ primary key  (curren_code)
);
go

/*==============================================================*/
/* Table : hotkey_cfg   �����ȼ��� ��ǰ̨���ά����             */
/*==============================================================*/
create table dbo.hotkey_cfg (
keybd_type           char(3)              not null,	--��������
key_value            smallint             not null,	--����ɨ����ASCII�ַ�
plu                  char(13)             not null,	--��ݱ��루��λʱ���������������λ��������Ʒ���룻��������Ʒ���룩
flag 		    int 	not null default 0, 	--�ȼ����ͱ�־ 0=�������  1=��Ʒ����/����  2=�ֽ��ȼ�
constraint pri_hotkey primary key  (keybd_type, key_value)
);
go

/*==============================================================*/
/* Table : price_lst ��Ʒ������                                 */
/*==============================================================*/
create table dbo.price_lst (
vgno                 char(6)              not null,	--��Ʒ����
goodsno              char(13)             not null,	--��Ʒ����
gname                char(32)             not null,	--��Ʒ����
uname                char(8)              null,		--��Ʒ���۵�λ
spec                 char(8)             null,		--��Ʒ���
groupno              char(2)              not null,	--����ID��group='sm'ʱ����ʾ��ɫ����Ʒ������ʱ��������ɫ����Ϣ��
deptno               char(15)             not null,	--��Ʒ���
price                decimal(10,2)        not null,	--Ĭ���ۼ�
v_type               char(1)              not null,	--�������ͣ�0��������2�����أ�3����8�������룻9������������ֹ��4����ϣ���
p_type               char(1)              not null,	--�������ͣ�n:�ޣ�p:���ã�v:��Ʒ�ۿ�,d:С���ۿۣ�t:ʱ���ۿ�,g:�����ۿۣ�
qflag                char(1)              not null,
x                    int                  default 1 not null,
p_oldtype            char(1)              null,
up_date              datetime             null,
constraint u110_38 primary key  (vgno)
);
go


/*==============================================================*/
/* Table : promotion ������                                     */
/*==============================================================*/
create table dbo.promotion (
vgno                 char(6)              not null primary key,	--��Ʒ����
sheetno              char(16)             not null, --��������
promtype             char(1)              not null,	--��������(0:��ͨ 1:�ؼ� 2������)
promprice            decimal(10,2)        not null, --������
qty                  int                  not null, --��������
curqty               int                  not null, --��ǰ�ɴ�������(���Զ���������Ч)
qflag                char(1)              not null, --������־(1:Ϊ����,0Ϊ�Ƕ���)
startdate            datetime             not null, --��ʼ����
enddate              datetime             not null, --��������
starttime            DATETIME  not null,			--��ʼʱ��
endtime              DATETIME  not null,			--����ʱ��
)
go

/*==============================================================*/
/* Table : weight_code �����ʽ��                                     */
/*==============================================================*/
create table dbo.weight_code 
  (
    plu_format char(13),							--�����ʽ
    primary key (plu_format) 
  );

/*==============================================================*/
/* Table : vgnodiscount:ʱ�����ñ�                              */
/*==============================================================*/
create table dbo.vgnodiscount (
vgno                 char(6)              not null,
sheetno              char(16)             not null,
moneyrate            smallint             null,
startdate            datetime             null,
enddate              datetime             null,
time1                DATETIME  null,
time2                DATETIME  null,
time3                DATETIME  null,
time4                DATETIME  null,
time5                DATETIME  null,
time6                DATETIME  null,
distrate1            smallint             not null,
distrate2            smallint             not null,
distrate3            smallint             not null,
distrate4            smallint             not null,
distrate5            smallint             not null,
distrate6            smallint             not null,
constraint u125_101 primary key  (vgno)
)
go
/*==============================================================*/
/* Table : goodsplace   ��Ʒ�����Ӧ��                          */
/*==============================================================*/
create table dbo.goodsplace (
vgno                 char(6)              not null,
placeno              char(6)              not null,
flag                 int                  not null,
constraint u130_140 primary key  (vgno, placeno)
)
go

/*==============================================================*/
/* Table : cardlist                                             */
/*==============================================================*/
create table dbo.cardlist (
cardtype             char(4)              not null,
cardtag              char(8)              not null,
taglen               smallint             not null,
cardlen              smallint             not null,
bpos                 smallint             not null,
fmtcode              char(4)              not null,
constraint u133_148 primary key  (cardtype, cardtag)
)
go

/*==============================================================*/
/* Table : distitem_vgno  ��Ʒ���ñ�                            */
/*==============================================================*/
create table dbo.distitem_vgno (
vgno                 char(6)              not null,
sheetno              char(16)             not null,
promtype             char(1)              default 0 not null,
qty                  int                  default 0 not null,
curqty               int                  default 0 not null,
distrate1            smallint             default 0 not null,
min_amount           smallint             default 0 not null,
distrate2            smallint             default 0 not null,
med_amount           smallint             default 0 not null,
distrate3            smallint             default 0 not null,
max_amount           smallint             default 0 not null,
starttime            DATETIME  default getdate() not null,
endtime              DATETIME  default getdate() not null,
constraint u134_155 primary key  (vgno)
)
go

/*==============================================================*/
/* Table : distitem_dept  ������ñ�                            */
/*==============================================================*/
create table dbo.distitem_dept (
deptno               char(15)              not null,
sheetno              char(16)             not null,
promtype             char(1)              not null,
distrate1            smallint             not null,
min_amount           smallint             not null,
distrate2            smallint             not null,
med_amount           smallint             not null,
distrate3            smallint             not null,
max_amount           smallint             not null,
starttime            DATETIME             not null,
endtime              DATETIME             not null,
constraint u135_168 primary key  (deptno)
)
go

/*==============================================================*/
/* Table : distitem_place                                       */
/*==============================================================*/
create table dbo.distitem_place (
placeno              char(6)              not null,
sheetno              char(16)             not null,
promtype             char(1)              not null,
distrate1            smallint             not null,
min_amount           smallint             not null,
distrate2            smallint             not null,
med_amount           smallint             not null,
distrate3            smallint             not null,
max_amount           smallint             not null,
starttime            DATETIME  not null,
endtime              DATETIME  not null,
constraint u136_179 primary key  (placeno)
)
go

/*==============================================================*/
/* Table : answer                                               */
/*==============================================================*/
create table dbo.answer (
tran_id              int                  not null,
code                 int                  not null,
pos_seq              int                  not null,
old_seq              int                  null,
auth_seq             int                  null,
cardtype             char(4)              not null,
cardno               char(25)             not null,
old_cdno             char(25)             null,
cardname             char(16)             null,
reqdt                datetime             not null,
reqtime              char(8)              not null,
rec_time             DATETIME  not null,
rec_stat             char(1)              null,
cd_stat              char(1)              null,
shop_id              char(4)              not null,
pos_id               char(4)              not null,
cashier_id           char(4)              not null,
listno               int                  not null,
pay_value            decimal(7,2)         not null,
balance              decimal(7,2)         null,
auth_code            char(6)              null
)
go

/*==============================================================*/
/* Table : vgbkcard ���������Ѳ���                            */
/*==============================================================*/
create table dbo.vgbkcard (
cardtype             char(4)              not null,
cardtag              char(10)             not null,
taglen               int                  not null,
cdnolen              int                  not null,
cdnodisp             int                  not null,
cardname             char(16)             not null,
cardbank             char(16)             not null,
workfile             char(10)             not null,
constraint u139_205 primary key  (cardtype)
)
go


/*==============================================================*/
/* Table : cardnote  ���п���                                   */
/*==============================================================*/
create table dbo.cardnote (
bank_id              char(1)              not null,
cardtype             char(4)              not null,
bank                 char(10)             not null,
money_code           char(3)              null,
cardnote             char(8)              not null,
rate                 decimal(4,2)         null,
note1                char(80)             null,
note2                char(60)             null,
note3                char(60)             null,
constraint u142_241 primary key  (cardtype)
)
go

/*==============================================================*/
/* Table : edition_ctl                                          */
/*==============================================================*/
create table dbo.edition_ctl (
editdate             char(8)              not null,
edittime             char(6)              not null,
editbatchno          int                  not null,
editionno            smallint             not null,
curtimes             smallint             null,
flushdate            datetime             null
)
go

/*==============================================================*/
/* Table : pridel_lst                                           */
/*==============================================================*/
create table dbo.pridel_lst (
vgno                 char(6)              not null,
deldate              char(8)              not null,
constraint u146_253 primary key  (vgno)
)
go

/*==============================================================*/
/* Table : offline_ctl                                          */
/*==============================================================*/
create table dbo.offline_ctl (
shop_id              char(6)              null,
pos_id               char(4)              not null,
flag                 char(1)              null,
dloadtime            char(4)              null,
constraint u147_256 primary key  (pos_id)
)
go


/*==============================================================*/
/* Table : offglide_ctl                                         */
/*==============================================================*/
create table dbo.offglide_ctl (
shop_id              char(6)              null,
pos_id               char(4)              not null,
edate                char(8)              null,
etime                char(6)              null,
startlistno          int                  null,
stoplistno           int                  null,
constraint u148_258 primary key  (pos_id)
)
go


/*==============================================================*/
/* Table : cdanswer �������Ѳ���                              */
/*==============================================================*/
create table dbo.cdanswer (
tran_id              int                  not null,
code                 int                  not null,
cardno               char(25)             not null,
rec_stat             char(1)              null,
stat                 char(1)              null,
shop_id              char(4)              not null,
pos_id               char(4)              not null,
cashier_id           char(4)              not null,
date                 datetime             not null,
time                 char(8)              not null,
pay_value            decimal(10,2)        not null,
balance              decimal(10,2)        null,
cdseq                int                  not null
)
go


/*==============================================================*/
/* Table : pos_para  POS������                                  */
/*==============================================================*/
create table dbo.pos_para (
para                 char(16)             not null,
val                  char(64)             not null,
notes                char(64)             not null,
constraint u150_270 primary key  (para)
)
go


/*==============================================================*/
/* Table : pos_ini  POS����������ñ�                           */
/*==============================================================*/
create table dbo.pos_ini (
posgroup             char(8)              not null,
para                 char(16)             not null,
val                  char(64)             not null,
constraint u151_274 primary key  (posgroup, para)
)
go

/*==============================================================*/
/* Table : posgroup  POS������                                */
/*==============================================================*/
create table dbo.posgroup (
posgroup             char(8)              not null,
notes                char(64)             null,
constraint u152_278 primary key  (posgroup)
)
go

/*==============================================================*/
/* Table : keyname   ������                                     */
/*==============================================================*/
create table dbo.keyname (
keyvalue             smallint             not null,
keyname              char(16)             not null,
constraint u153_280 primary key  (keyvalue)
)
go

/*==============================================================*/
/* Table : keybd_class  ���̷����                              */
/*==============================================================*/
create table dbo.keybd_class (
classname            char(15)             not null,
note                 char(30)             null,
constraint u154_283 primary key  (classname)
)
go

/*==============================================================*/
/* Table : tableright                                           */
/*==============================================================*/
create table dbo.tableright (
tablename            varchar(32)          not null,
sourcetablename      varchar(32)          not null,
userproperty         varchar(32)          not null,
resultfield          varchar(128)         not null,
note                 varchar(128)         null,
constraint u155_285 primary key  (tablename)
)
go


/*==============================================================*/
/* Table : syslog   ϵͳ��־��                                  */
/*==============================================================*/
create table dbo.syslog (
eventdatetime        DATETIME  default getdate() not null,
loginid              smallint             not null,
workstationid        smallint             not null,
moduleid             int                  not null,
eventid              smallint             not null,
note                 varchar(50)          null
)
go


/*==============================================================*/
/* Table : keybd_layout  ���̲��ֱ�                             */
/*==============================================================*/
create table dbo.keybd_layout (
keybd_class          char(15)             not null,
x                    smallint             not null,
y                    smallint             not null,
key_value            smallint             not null,
constraint u157_295 primary key  (keybd_class, x, y)
)
go


/*==============================================================*/
/* Table : pay_log  ֧����־��                                  */
/*==============================================================*/
create table dbo.pay_log (
listno               int                  not null,
dt                   datetime             not null,
time                 char(10)             null,
pos_id               char(4)              not null,
flag3                char(1)              not null,
equiv_value          decimal(10,2)        null,
constraint u166_383 primary key  (pos_id, listno, dt)
)
go

/*==============================================================*/
/* Table : prog_lst   ����汾���Ʊ�                            */
/*==============================================================*/
create table dbo.prog_lst (
progname             char(12)             not null,
prognote             char(40)             null,
progver              char(8)              not null,
progoff              char(8)              not null,
progdate             datetime             not null,
ftpserver            char(40)             not null,
ftpuser              char(10)             not null,
ftppass              char(10)             not null,
constraint u167_388 primary key  (progname)
)
go


/*==============================================================*/
/* Table : distitem_type   �������ͱ�                           */
/*==============================================================*/
create table dbo.distitem_type (
fieldname            char(15)             not null,
type                 char(1)              not null,
desp                 char(15)             not null,
constraint u168_396 primary key  (fieldname, type)
)
go


/*==============================================================*/
/* Table : sale_jpick  �ͻ���                                   */
/*==============================================================*/
create table dbo.sale_jpick (
serialid             numeric(10)          identity,
dt                   datetime             not null,
listno               int                  not null,
posid                char(4)              not null,
cashierid            char(4)              not null,
vgno                 int                  not null,
placeid              char(6)              not null,
amount               int                  not null,
item_value           decimal(10,2)        not null,
x                    smallint             default 1 not null,
disc_value           decimal(10,2)        not null,
deliver_flag         char(1)              null,
flag1                char(1)              not null,
flag2                char(1)              not null,
flag3                char(1)              null,
constraint u170_409 primary key  (serialid)
)
go

/*==============================================================*/
/* Table : sale_jls ���Ǳ�                                      */
/*==============================================================*/
create table dbo.sale_jls (
serialid             numeric(10)          identity,
dt                   datetime             not null,
listno               int                  not null,
posid                char(4)              not null,
cashierid            char(4)              not null,
vgno                 int                  not null,
placeid              char(6)              not null,
amount               int                  not null,
x                    smallint             default 1 not null,
disc_value           decimal(10,2)        not null,
constraint u171_423 primary key  (serialid)
)
go

/*==============================================================*/
/* Table : rjcontrol      �ս���Ʊ�                            */
/*==============================================================*/
create table dbo.rjcontrol (
sdate                datetime             not null,
shopid               char(4)              not null,
stepname             char(30)             not null,
notes                char(60)             null,
statusflag           int                  not null,
constraint u173_439 primary key  (shopid, stepname)
)
go


/*==============================================================*/
/* Table : shop                                                 */
/*==============================================================*/
create table dbo.shop (
id                   char(4)              not null,
name                 char(16)             not null,
shoptype             int                  not null,
zoneid               int                  null,
headid               char(4)              null,
constraint u175_450 primary key  (id)
)
go

/*==============================================================*/
/* Table : goodscombine  �����Ʒ��                             */
/*==============================================================*/
create table dbo.goodscombine (
vgno                 char(6)              not null,
goodsno              char(13)             not null	primary key,
gname                char(32)             not null,
uname                char(8)              null,
spec                 char(12)             null,
price                decimal(10,2)        not null,
x                    int                  default 1 not null
)
go


/*==============================================================*/
/* Table : goodscut  ������Ʒ��                                 */
/*==============================================================*/
create table dbo.goodscut (
vgno                 char(6)              not null,
goodsno              char(13)             not null primary key,
gname                char(32)             not null,
uname                char(8)              null,
spec                 char(12)             null,
price                decimal(10,2)        not null,
x                    int                  default 1 not null,
qty                  int                  not null,
curqty               int                  not null
)
go

/*==============================================================*/
/* Table : log_lst   ��־��                                     */
/*==============================================================*/
create table dbo.log_lst (
shop_id              char(4)              null,
pos_id               char(4)              not null,
edate                char(8)              null,
etime                char(6)              null,
action               char(60)             null,
resultflag           char(1)              null,
cashier_id           char(4)              null
)
go

/*==============================================================*/
/* Table : fileproc                                             */
/*==============================================================*/
create table dbo.fileproc (
shop_id              char(4)              not null,
progname             char(30)             not null,
progdeclare          char(60)             null,
status               int                  not null,
rjdate               datetime             not null,
constraint u183_502 primary key  (shop_id, progname)
)
go


/*==============================================================*/
/* Table : waiter_id                                            */
/*==============================================================*/
create table dbo.waiter_id (
waiter_no            char(4)              not null,
waiter_name          char(10)             not null,
placeno              char(6)              null,
constraint u184_506 primary key  (waiter_no)
)
go


/*==============================================================*/
/* Table : casper                                               */
/*==============================================================*/
create table dbo.casper (
systemid             smallint             not null,
moduleid             int                  not null,
keyname              char(16)             not null,
constraint u186_513 primary key  (systemid, moduleid, keyname)
)
go


/*==============================================================*/
/* Table : myreportwhere                                        */
/*==============================================================*/
create table dbo.myreportwhere (
whereid              int                  not null,
languageid           int                  not null,
name                 char(64)             not null,
dllname              char(64)             not null,
modulename           char(64)             not null,
note                 char(128)            null,
constraint u187_517 primary key  (whereid)
)
go


/*==============================================================*/
/* Table : myreportwhereitem                                    */
/*==============================================================*/
create table dbo.myreportwhereitem (
moduleid             int                  not null,
orderindex           int                  not null,
prompt               char(64)             not null,
inputmaxsize         int                  not null,
displaysize          int                  not null,
whereid              int                  not null,
wheregen             char(255)            not null,
othergen             char(255)            null,
params               char(255)            null,
flag                 int                  not null
)
go

/*==============================================================*/
/* Table : myreportorderitem                                    */
/*==============================================================*/
create table dbo.myreportorderitem (
moduleid             int                  not null,
name                 char(64)             not null,
orderdesc            char(255)            not null,
note                 char(128)            null,
constraint u189_531 primary key  (moduleid, name)
)
go


/*==============================================================*/
/* Table : myreportgroup                                        */
/*==============================================================*/
create table dbo.myreportgroup (
groupid              numeric(10)          identity,
name                 char(64)             not null,
note                 char(128)            not null
)
go


/*==============================================================*/
/* Table : myreportgroupitem                                    */
/*==============================================================*/
create table dbo.myreportgroupitem (
groupid              int                  not null,
moduleid             int                  not null,
constraint u191_538 primary key  (groupid, moduleid)
)
go


/*==============================================================*/
/* Table : reportformat  �����ʽ��                             */
/*==============================================================*/
create table dbo.reportformat (
serialid             int                  not null,
languageid           int                  not null,
pageno               smallint             null,
pagewidth            int                  null,
pageheight           int                  null,
pageorient           smallint             not null,
pagetop              int                  null,
pagedown             int                  null,
pageleft             int                  null,
pageright            int                  null,
fieldcols            smallint             not null,
printgridline        smallint             not null,
rowspace             decimal(4,2)         not null,
pageheadexpr         varchar(255)         null,
pageheadfont         varchar(64)          null,
pagefootexpr         varchar(255)         null,
pagefootfont         varchar(64)          null,
pagetitle1           varchar(255)         null,
pagetitle1font       varchar(64)          null,
pagetitle2           varchar(255)         null,
pagetitle2font       varchar(64)          null,
gridheadexpr1        varchar(255)         null,
gridheadexpr2        varchar(255)         null,
gridheadexpr3        varchar(255)         null,
gridheadexpr4        varchar(255)         null,
gridheadexprfont     varchar(64)          null,
gridheadfont         varchar(64)          null,
gridfootexpr1        varchar(255)         null,
gridfootexpr2        varchar(255)         null,
gridfootexpr3        varchar(255)         null,
gridfootexpr4        varchar(255)         null,
gridfootexprfont     varchar(64)          null,
titlegroup1          varchar(255)         null,
titlegroup2          varchar(255)         null,
groupexpr            varchar(255)         null,
grouptitleexpr       varchar(255)         null,
groupsumexpr1        varchar(255)         null,
groupsumexpr2        varchar(255)         null,
groupsumexpr3        varchar(255)         null,
groupsumexpr4        varchar(255)         null,
groupfont            varchar(64)          null,
datafont             varchar(64)          null,
sumtotalcolumn1      varchar(255)         null,
sumtotalcolumn2      varchar(255)         null,
sumtotalcolumn3      varchar(255)         null,
sumpagecolumn1       varchar(255)         null,
sumpagecolumn2       varchar(255)         null,
sumpagecolumn3       varchar(255)         null,
sumfont              varchar(64)          null,
sumoneachpage        smallint             not null,
rowsperpage          smallint             not null,
txtcolsperrow        smallint             not null,
zoomtopagewidth      smallint             not null,
allowspanpage        smallint             not null,
autopageverttohorz   smallint             not null,
constraint u192_541 primary key  (languageid, serialid)
)
go


/*==============================================================*/
/* Table : reportformatitem                                     */
/*==============================================================*/
create table dbo.reportformatitem (
serialid             int                  not null,
fieldname            char(20)             not null,
fieldindex           smallint             not null,
visible              smallint             not null,
fieldwidth           smallint             not null,
caption              char(32)             null,
constraint u193_554 primary key  (serialid, fieldname)
)
go


/*==============================================================*/
/* Table : mymessages ��Ϣ��                                    */
/*==============================================================*/
create table dbo.mymessages (
messageid            int                  not null,
moduleid             int                  not null,
senddate             DATETIME  not null,
note                 varchar(255)         not null,
constraint u194_560 primary key  (messageid)
)
go


/*==============================================================*/
/* Table : mymessages0                                          */
/*==============================================================*/
create table dbo.mymessages0 (
messageid            numeric(10)          identity,
moduleid             int                  not null,
senddate             DATETIME  not null,
note                 varchar(255)         not null,
constraint u195_565 primary key  (messageid)
)
go

/*==============================================================*/
/* Table : eventlevel  �¼������                               */
/*==============================================================*/
create table dbo.eventlevel (
eventlevelid         smallint             not null,
note                 varchar(50)          null,
constraint u196_570 primary key  (eventlevelid)
)
go

/*==============================================================*/
/* Table : eventcode  �¼������                                */
/*==============================================================*/
create table dbo.eventcode (
eventid              smallint             not null,
eventlevelid         smallint             not null,
note                 varchar(50)          null,
constraint u197_572 primary key  (eventid)
)
go


/*==============================================================*/
/* Table : config  ϵͳ���ñ�                                   */
/*==============================================================*/
create table dbo.config (
systemid             smallint             not null,
name                 varchar(64)          not null,
value                varchar(64)          not null,
note                 varchar(128)         null,
constraint u198_575 primary key  (systemid, name)
)
go


/*==============================================================*/
/* Table : dictionary  �ֵ��                                   */
/*==============================================================*/
create table dbo.dictionary (
english              char(20)             not null,
LanguageID  int     default 936 not null,       -- ���Ա��
chinese              char(20)             not null,
formatdisplay        char(20)             null,
note                 varchar(64)          null,
constraint u199_579 primary key  (english)
)
go

/*==============================================================*/
/* Table : serialnumber ������ˮ���Ʊ�                          */
/*==============================================================*/
create table dbo.serialnumber (
serialid             int                  not null,
name                 char(16)             not null,
flag                 int                  not null,
serialnumber         int                  not null,
resetdate            datetime             not null,
dailyreset           smallint             not null,
servertranflag       smallint             not null,
sheetflag            smallint             not null,
constraint u200_582 primary key  (serialid)
)
go

/*==============================================================*/
/* Table : sheetflow0                                           */
/*==============================================================*/
create table dbo.sheetflow0 (
serialid             numeric(10)          identity,
sheettype            int                  not null,
sheetid              char(16)             not null,
beforeflag           smallint             not null,
afterflag            smallint             not null,
username             char(8)              not null,
flowdate             DATETIME  not null,
note                 char(32)             null,
constraint u201_591 primary key  (sheettype, sheetid, serialid)
)
go

/*==============================================================*/
/* Table : sheetflow                                            */
/*==============================================================*/
create table dbo.sheetflow (
serialid             int                  not null,
sheettype            int                  not null,
sheetid              char(16)             not null,
beforeflag           smallint             not null,
afterflag            smallint             not null,
username             char(8)              not null,
flowdate             DATETIME  not null,
note                 char(32)             null,
constraint u202_599 primary key  (sheettype, sheetid, serialid)
)
go

/*==============================================================*/
/* Table : goodsext6   �����۱�                               */
/*==============================================================*/
create table dbo.goodsext6 (
vgno                 char(6)              not null,
goodsno              char(13)             not null,
pknum                int                  not null,
spec                 char(16)             null,
price                decimal(10,2)        null,
gname                char(32)             not null,
uname                char(8)              null,
x                    int                  default 1 not null,
constraint u203_607 primary key  (goodsno)
)
go


/*==============================================================*/
/* Table : cashdiff                                             */
/*==============================================================*/
create table dbo.cashdiff (
sheetid              char(16)             not null,
flag                 smallint             not null,
editor               char(8)              not null,
editdate             datetime             not null,
operator             char(8)              not null,
checker              char(8)              null,
checkdate            datetime             null,
printcount           smallint             not null,
constraint u204_613 primary key  (sheetid)
)
go


/*==============================================================*/
/* Table : cashdiffitem                                         */
/*==============================================================*/
create table dbo.cashdiffitem (
sheetid              char(16)             not null,
cashierid            char(4)              not null,
name                 char(8)              not null,
posid                char(4)              not null,
sdate                datetime             not null,
pay_type             char(1)              not null,
cashvalue            decimal(10,2)        not null,
truecashvalue        decimal(10,2)        not null,
note                 char(32)             null
)
go


/*==============================================================*/
/* Table : cashdiffitem0                                        */
/*==============================================================*/
create table dbo.cashdiffitem0 (
sheetid              char(16)             not null,
cashierid            char(4)              not null,
name                 char(8)              not null,
posid                char(4)              not null,
sdate                datetime             not null,
pay_type             char(1)              not null,
cashvalue            decimal(10,2)        not null,
truecashvalue        decimal(10,2)        not null,
note                 char(32)             null
)
go


/*==============================================================*/
/* Table : diffcash ����Ա���̿��                              */
/*==============================================================*/
create table dbo.diffcash (
cashierid            char(4)              not null,
name                 char(8)              not null,
posid                char(4)              not null,
sdate                datetime             not null,
pay_type             char(1)              not null,
cashvalue            decimal(10,2)        not null,
truecashvalue        decimal(10,2)        not null,
note                 char(32)             null,
sdatetime            DATETIME  not null,
constraint u206_628 primary key  (sdate, cashierid, posid, pay_type)
)
go

/*==============================================================*/
/* Table : cashdiff0                                            */
/*==============================================================*/
create table dbo.cashdiff0 (
sheetid              char(16)             not null,
flag                 smallint             not null,
editor               char(8)              not null,
editdate             datetime             not null,
operator             char(8)              not null,
checker              char(8)              null,
checkdate            datetime             null,
printcount           smallint             not null,
constraint u207_637 primary key  (sheetid)
)
go

/*==============================================================*/
/* Table : myreportchartitem                                    */
/*==============================================================*/
create table dbo.myreportchartitem (
moduleid             int                  not null,
name                 char(32)             not null,
chartname            char(32)             not null,
chartid              smallint             not null,
labelfield           char(255)            null,
xfield               char(255)            not null,
yfield               char(255)            not null,
otherparam           char(255)            null,
showstyle            smallint             not null,
color                int                  null,
constraint u209_652 primary key  (moduleid, name, chartname)
)
go

/*==============================================================*/
/* Table : mysqlstr Sql�����                                   */
/*==============================================================*/
create table dbo.mysqlstr (
moduleid             int                  not null,
sqlserialid          int                  not null,
sql1                 varchar(255)         null,
sql2                 varchar(255)         null,
sql3                 varchar(255)         null,
sql4                 varchar(255)         null,
sql5                 varchar(255)         null,
constraint u210_660 primary key  (moduleid, sqlserialid)
)
go

--�����ʽ��
create table dbo.Reports
(
SerialID	int not null,			-- ��ˮ���
LanguageID	int default 936 not null,	-- ���Ա��
Data		image,				-- ��������
primary key(LanguageID,SerialID)
)
go


/*==============================================================*/
/* Table : myreport  �������                                 */
/*==============================================================*/
create table dbo.myreport (
moduleid             int                  not null,
languageid           int                  not null,
name                 char(64)             not null,
sql1                 char(255)            null,
sql2                 char(255)            null,
sql3                 char(255)            null,
sql4                 char(255)            null,
sql5                 char(255)            null,
sql6                 char(255)            null,
sql7                 char(255)            null,
sql8                 char(255)            null,
sql9                 char(255)            null,
sql10                char(255)            null,
fielddesc1           char(255)            null,
fielddesc2           char(255)            null,
fielddesc3           char(255)            null,
fielddesc4           char(255)            null,
fielddesc5           char(255)            null,
fielddesc6           char(255)            null,
fielddesc7           char(255)            null,
fielddesc8           char(255)            null,
fielddesc9           char(255)            null,
fielddesc10          char(255)            null,
note                 char(128)            null,
constraint u211_663 primary key  (languageid, moduleid)
)
go


/*==============================================================*/
/* Table : pos_logon                                            */
/*==============================================================*/
create table dbo.pos_logon (
pos_id               char(4)              not null,
clerk_id             char(4)              not null,
constraint u212_667 primary key  (pos_id)
)
go

/*==============================================================*/
/* Table : clerk_logon ����Ա��¼���Ʊ�                         */
/*==============================================================*/
create table dbo.clerk_logon (
clerkid              char(4)              not null,
posid                char(4)              not null,
constraint u213_670 primary key  (clerkid)
)
go

/*==============================================================*/
/* Table : promotion_vip  ��Ա�۱�                              */
/*==============================================================*/
create table dbo.promotion_vip (
vgno                 char(6)              not null,
sheetno              char(16)             not null,
promlevel            smallint             not null,
promprice            decimal(10,2)        not null,
startdate            datetime             not null,
enddate              datetime             not null,
starttime            DATETIME  not null,
endtime              DATETIME  not null,
promtype             char(1)              not null,
qflag                char(1)              not null,
qty                  int                  not null,
curqty               int                  not null
)
go

/*==============================================================*/
/* Table : sale_j ������ˮ��                                    */
/*==============================================================*/
create table dbo.sale_j (
dt                   datetime             not null,
time                 datetime             not null,
reqtime              char(10)             null,
listno               int                  not null,
sublistno            int                  not null,
pos_id               char(4)              not null,
cashier_id           char(4)              not null,
waiter_id            char(4)              null,
vgno                 char(6)              not null,
goodsno              char(13)             not null,
placeno              char(6)              not null,
groupno              char(2)              null,
deptno               char(15)              null,
amount               int                  not null,
colorsize            char(4)              null,
item_value           decimal(10,2)        not null,
disc_value           decimal(10,2)        not null,
item_type            char(1)              not null,
v_type               char(1)              not null,
disc_type            char(1)              not null,
authorizer_id        char(4)              null,
x                    int                  default 1 not null,
deliver_flag         char(1)              null,
flag1                char(1)              not null,
flag2                char(1)              not null,
flag3                char(1)              not null,
trainflag            char(1)              null,
price                decimal(10,2)        null,
use_goodsno          char(13)             null,
serialid             numeric(10)          identity,
ShiftID 	     int		  default 1 not null,   --�����Ϣ
WorkDate 	     Datetime		  default getdate() not null   --Ӫҵ����
)
go


/*==============================================================*/
/* Table : pay_j ֧����ˮ��                                     */
/*==============================================================*/
create table dbo.pay_j (
dt                   datetime             not null,
time                 datetime             not null,
reqtime              char(10)             null,
listno               int                  not null,
sublistno            int                  null,
pos_id               char(4)              not null,
cashier_id           char(4)              not null,
pay_seq              smallint             null,
pay_reason           char(1)              not null,
pay_type             char(1)              not null,
deliver_flag         char(1)              null,
curren_code          char(3)              not null,
pay_value            decimal(10,2)        not null,
equiv_value          decimal(10,2)        not null,
cardno               char(19)             null,
flag3                char(1)              not null,
trainflag            char(1)              null,
serialid             numeric(10)          identity,
ShiftID 	     int		  default 1 not null,   --�����Ϣ
WorkDate 	     Datetime		  default getdate() not null   --Ӫҵ����
)
go


/*==============================================================*/
/* Table : sale_jrep  �ظ�������ˮ��                            */
/*==============================================================*/
create table dbo.sale_jrep (
dt                   datetime             not null,
time                 datetime             not null,
reqtime              char(10)             null,
listno               int                  not null,
sublistno            int                  not null,
pos_id               char(4)              not null,
cashier_id           char(4)              not null,
waiter_id            char(4)              null,
vgno                 char(6)              not null,
goodsno              char(13)             not null,
placeno              char(6)              not null,
groupno              char(2)              null,
deptno               char(15)              null,
amount               int                  not null,
colorsize            char(4)              null,
item_value           decimal(10,2)        not null,
disc_value           decimal(10,2)        not null,
item_type            char(1)              not null,
v_type               char(1)              not null,
disc_type            char(1)              not null,
authorizer_id        char(4)              null,
x                    int                  default 1 not null,
deliver_flag         char(1)              null,
flag1                char(1)              not null,
flag2                char(1)              not null,
flag3                char(1)              not null,
trainflag            char(1)              null,
price                decimal(10,2)        null,
use_goodsno          char(13)             null,
serialid             numeric(10)          identity
)
go


/*==============================================================*/
/* Table : pay_jrep   �ظ�֧����ˮ��                            */
/*==============================================================*/
create table dbo.pay_jrep (
dt                   datetime             not null,
time                 datetime             not null,
reqtime              char(10)             null,
listno               int                  not null,
sublistno            int                  null,
pos_id               char(4)              not null,
cashier_id           char(4)              not null,
pay_seq              smallint             null,
pay_reason           char(1)              not null,
pay_type             char(1)              not null,
deliver_flag         char(1)              null,
curren_code          char(3)              not null,
pay_value            decimal(10,2)        not null,
equiv_value          decimal(10,2)        not null,
cardno               char(19)             null,
flag3                char(1)              not null,
trainflag            char(1)              null,
serialid             numeric(10)          identity
)
go



/*==============================================================*/
/* Table : sale_j_train  ��ѵ������ˮ��                         */
/*==============================================================*/
create table dbo.sale_j_train (
dt                   datetime             not null,
time                 datetime             not null,
reqtime              char(10)             null,
listno               int                  not null,
sublistno            int                  not null,
pos_id               char(4)              not null,
cashier_id           char(4)              not null,
waiter_id            char(4)              null,
vgno                 char(6)              not null,
goodsno              char(13)             not null,
placeno              char(6)              not null,
groupno              char(2)              null,
deptno               char(15)              null,
amount               int                  not null,
colorsize            char(4)              null,
item_value           decimal(10,2)        not null,
disc_value           decimal(10,2)        not null,
item_type            char(1)              not null,
v_type               char(1)              not null,
disc_type            char(1)              not null,
authorizer_id        char(4)              null,
x                    int                  default 1 not null,
deliver_flag         char(1)              null,
flag1                char(1)              not null,
flag2                char(1)              not null,
flag3                char(1)              not null,
trainflag            char(1)              null,
price                decimal(10,2)        null,
use_goodsno          char(13)             null,
serialid             int                  not null
)
go

/*==============================================================*/
/* Table : pay_j_train  ��ѵ֧����ˮ                            */
/*==============================================================*/
create table dbo.pay_j_train (
dt                   datetime             not null,
time                 datetime             not null,
reqtime              char(10)             null,
listno               int                  not null,
sublistno            int                  null,
pos_id               char(4)              not null,
cashier_id           char(4)              not null,
pay_seq              smallint             null,
pay_reason           char(1)              not null,
pay_type             char(1)              not null,
deliver_flag         char(1)              null,
curren_code          char(3)              not null,
pay_value            decimal(10,2)        not null,
equiv_value          decimal(10,2)        not null,
cardno               char(19)             null,
flag3                char(1)              not null,
trainflag            char(1)              null,
serialid             int                  not null
)
go

--������ˮ�ӿڱ�
create table dbo.sale_jif ( 
dt                   datetime             not null, 
time                 char(8)              not null,     --��Ϊ8λ�ַ� 
listno               int                  not null, 
pos_id               char(4)              not null, 
cashier_id           char(4)              not null, 
waiter_id            char(4)              null, 
vgno                 char(6)              not null, 
goodsno              char(13)             not null, 
amount               int                  not null, 
colorsize            char(4)              null, 
item_value           decimal(10,2)        not null, 
disc_value           decimal(10,2)        not null, 
item_type            char(1)              not null, 
v_type               char(1)              not null, 
disc_type            char(1)              not null, 
authorizer_id        char(4)              null, 
x                    int                  default 1 not null, 
deliver_flag         char(1)              null, 
trainflag            char(1)              null, 
price                decimal(10,2)        null, 
use_goodsno          char(13)             null 
) 
go 

--֧����ˮ�ӿڱ�
create table dbo.pay_jif ( 
dt                   datetime             not null, 
time                 char(8)              not null,     --��Ϊ8λ�ַ� 
listno               int                  not null, 
pos_id               char(4)              not null, 
cashier_id           char(4)              not null, 
pay_reason           char(1)              not null, 
pay_type             char(1)              not null, 
deliver_flag         char(1)              null, 
curren_code          char(3)              not null, 
pay_value            decimal(10,2)        not null, 
equiv_value          decimal(10,2)        not null, 
cardno               char(19)             null, 
trainflag            char(1)              null 
) 
go 




/*==============================================================*/
/* Table : accurate���ֱ�����                                    */
/*==============================================================*/

create table dbo.accurate
(
cardlevelid 	      smallint		 not null,   -- ��������
deptid		      int		 not null,   -- �����
accurate	      char(12)		 not null,   -- ����
constraint pri_accur primary key(CardLevelID,DeptID)
);
go


--��Ա����Ϣ��
create table dbo.Guest(
  CardNO                char(20)        not null primary key,   --���ֿ���
  CardType              char(1)         default '0' not null,   --����
  MemberID              char(20)        not null,               --��Ա��
  SheetID               char(16)        not null,               --��������
  Secrety               char(7)         not null,               --����
  ShopID                char(4)         not null,               --������
  InitPoint             integer         not null,               --��ʼ����
  Point                 integer         not null,               --�ֻ���
  LastUseDate           datetime 	null,                                   --���ʹ������
  LastShopID            char(4)		null,                                --������ѷֵ��
  LastPOSID             char(4)		null,                                --�������POS����
  LastCashierID         char(4)		null,                                --�����������Ա��
  PayMoney              dec(12,2)	default 0 not null,                  --������ �ۼ�
  Times                 smallint	default 0 not null,                  --�������
  Indate                datetime    default getdate() not null,              --��������
  EndDate               datetime    null,                                   --��Ч��
  SheetIDNew            char(16)        not null,               --���µ��� ����޸�״̬�ĵ��ݱ��
  Mode                  char(1)         default '1' not null,   --��״̬ 1-������r���ѻ��տ�l-��ʧ�� -����e-�ѻ���q-�˿�
  memberlevel           integer default 0 not null,              --��Ա����
  discount		smallint	default 0 not null      --�ۿ��ʣ�,98-- 98��, 0 --���ۿ�
)

go 
--���Ῠ������ˮ��
create table dbo.GuestPurch0(
  PurchSerial           integer IDENTITY       not null,   --֧����ˮ��
  CardNO                char(20)        not null,               --����
  MemberID              char(20)	null,                               --��Ա���
  PurchDateTime         datetime    default getdate() not null,         --��������
  ReqTime               char(10)        not null,               --����ʱ��
  PayMoney              dec(10,2)       not null,               --������
  Point                 integer         default 0 not null,               --���λ���
  ShopID                char(4)         not null,               --�̵��
  Branchno              char(4)         not null,               --̨��
  Cashierno             char(4)         not null,               --����Ա��
  ListNO                int		null,                                    --֧����ˮ��
  Stat                  char(1)         not null,               --����״̬ 0-����1-������2-��������3-����
  CheckFlag             smallint default 0 not null             --���ʱ�־ 0��ʾδ���ʣ�1��ʾ�Ѷ���
)

go


--�̵���ʱ��
create table dbo.pd_data0
(
SheetID     char(16)  not null,
vgno        integer   not null,
goodsno     char(13)  not null,
qty         dec(12,3) default 0.000 not null,
team        integer   null,
operator    char(8)   null,
plu         char(13)  null,
orderno     int IDENTITY(1,1) not null --˳���
)
go



alter table cashdiffitem
   add constraint r205_1050 foreign key (sheetid)
      references cashdiff (sheetid)
go


alter table cashdiffitem0
   add constraint r208_1051 foreign key (sheetid)
      references cashdiff0 (sheetid)
go


alter table eventcode
   add constraint r197_1049 foreign key (eventlevelid)
      references eventlevel (eventlevelid)
go

/*
alter table keybd_layout
   add constraint r157_1048 foreign key (keybd_class)
      references keybd_class (classname)

*/

if exists (select * from sysobjects
	    where id = object_id('dbo.GetAccuRate') and sysstat & 0xf = 4)
   drop procedure dbo.GetAccuRate
GO
-----------------------------------------------------------------
--˵����ȡ���ֱ���
--��������Ʒ���룬���ֿ�����
--���أ����ֱ���
--�� accrate,price_lst
--��������
--�㷨��
--������������ 2002.07.14
--�޸ģ�
--����޸ģ������� 2002.07.14
-----------------------------------------------------------------
create procedure dbo.GetAccuRate(@vgno char(6),@levelID int,@Rate char(12) output)
as

  select @Rate=a.accurate  from accurate a,price_lst b
   where a.deptid=b.deptno and b.vgno=@vgno and a.cardlevelid=@levelID;
  if @Rate is null select @Rate='000000000000'
  return 0;
go

if exists (select * from sysobjects
	    where id = object_id('dbo.GetNewSerial') and sysstat & 0xf = 4)
   drop procedure dbo.GetNewSerial
GO

-----------------------------------------------------------------
--˵����ȡ���к�
--��������������
--���أ����кţ�����
--�� serialnumber
--��������
--�㷨��
--������������ 2002.08.14
--�޸ģ�
--����޸ģ������� 2002.08.14
-----------------------------------------------------------------
create procedure dbo.GetNewSerial @in_SerialID int ,
     @nSerial int output ,@dtDate datetime output
AS
  
  DECLARE @NDate DateTime
  DECLARE @DReset int
  

  update SerialNumber set SerialNumber=SerialNumber+1 where SerialID=@in_SerialID

  select @nSerial = SerialNumber,
         @dtDate  = ResetDate
       from SerialNumber where SerialID=@in_SerialID;    

go       
       
if exists (select * from sysobjects
	    where id = object_id('dbo.GetNewSheetID') and sysstat & 0xf = 4)
   drop procedure dbo.GetNewSheetID
GO


create procedure dbo.GetNewSheetID @iSerialID int, 
       @iSheetID varchar(16) output
AS       
---------------------------------------------------------------
--��������ȡ���ݱ��			
--�� Config
--������GetNewSerial
--�㷨��
--������������ 2002.08.14
--�޸ģ�
--����޸ģ������� 2002.08.14
---------------------------------------------------------------
 
  declare @nSerial int,@k int,@dDate datetime
  declare @cShopID varchar(4)
  
  exec GetNewSerial @iSerialID,@nSerial out,@dDate out

  select @cShopID=Value from Config where Name='�����';
  
  select @k=(year(@dDate)*10000+month(@dDate)*100+day(@dDate))
  
  select @iSheetID=Convert(varchar(8),@k)+Str(@nSerial,4,0)
  
  select @iSheetID=Replace(@iSheetID,' ','0')
  
  select @iSheetID=@cShopID+@iSheetID
  

GO

if exists (select * from sysobjects
	    where id = object_id('dbo.GetServerDate') and sysstat & 0xf = 4)
   drop procedure dbo.GetServerDate
GO
-----------------------------------------------------------------
--˵����ȡ������ʱ��
--������
--���أ���������ʱ��ֵ
--��
--������GetDate()
--�㷨��
--������������ 2002.08.14
--�޸ģ�
-----------------------------------------------------------------
create procedure dbo.GetServerDate(@dDate datetime output)
as
  select @dDate=GetDate()
return 0
GO


if exists (select * from sysobjects
	    where id = object_id('dbo.TL_GetServerDate') and sysstat & 0xf = 4)
   drop procedure dbo.TL_GetServerDate
GO
-----------------------------------------------------------------
--˵����ȡ������ʱ�� TL_GetServerDate
--������
--���أ���������ʱ��ֵ
--��
--������GetDate()
--�㷨��
--������������ 2002.08.14
--�޸ģ��Խ� 2003.05.23 �޸�����
-----------------------------------------------------------------
create procedure dbo.TL_GetServerDate(@dDate datetime output)
as
  select @dDate=GetDate()
return 0
GO

-----------------------------------------------------------------
--Pos_SumMoney			ʵʱ�������۽��
--������
--���أ����۽��,�͵���,�ۿ۽�ʵ�����
--��  sale_j
--������
--�㷨��
--������������ 2002.09.14
--�޸ģ������� 2002.09.16 �����ۿ۽�ʵ�����ͳ��
-----------------------------------------------------------------
if exists (select * from sysobjects
	    where id = object_id('dbo.Pos_SumMoney') and sysstat & 0xf = 4)
   drop procedure dbo.Pos_SumMoney
GO
create procedure dbo.Pos_SumMoney @SDate datetime, @sumMoney dec(12,2) output ,@SumSheet int output,
	@sumdisc_value dec(12,2) output,@truevalue dec(12,2) output
WITH ENCRYPTION
-----------------------------------------------------------------
--Version Number:Apple1.0_20030515_01,Last Mender:DDP
-----------------------------------------------------------------
AS Begin 
  declare @Err          int; 
  declare @BreakPoint   int; 
  declare @StartWork    int; 
  declare @Msg          varchar(255); 

  select @StartWork=0

  create table #tempsheet
  (
    pos_id   char(4) null,
    listno   int null,
    salevalue     dec(12,2)  default 0 not null,
	discvalue     dec(12,2)  default 0 not null
   );

  select @Err = @@error,@BreakPoint=100010; 
  if @Err != 0 goto ErrHandle; 

  select @StartWork=1

  insert into #tempsheet(pos_id,listno,salevalue,discvalue)
    select pos_id,listno,sum(item_value),sum(disc_value) from sale_j where dt=@sdate
      and trainflag='0'
      group by pos_id,listno;
  select @Err = @@error,@BreakPoint=100020; 
  if @Err != 0 goto ErrHandle; 

  select @summoney=sum(salevalue),@sumsheet=count(*),@sumdisc_value=sum(discvalue),
	@truevalue=sum(salevalue-discvalue) 
    from #tempsheet
  select @Err = @@error,@BreakPoint=100040; 
  if @Err != 0 goto ErrHandle; 

  drop table #tempsheet;
  select @Err = @@error,@BreakPoint=100060; 
  if @Err != 0 goto ErrHandle; 

  select @StartWork=0

  return 0;

ErrHandle:
  if @StartWork = 1 rollback transaction;
  raiserror('[%s],�ϵ�=%d,Err=%d',16,1,@Msg,@BreakPoint,@Err);
  return -1;

End
Go


-----------------------------------------------------------------
-- Table : promdisc_vip  ��Ա�۱�                              
-- �� �� ��xiaoyong 2003.7.28
-----------------------------------------------------------------
create table dbo.promdisc_vip (
vgno                 char(6)              not null,      -----��Ʒ����
sheetno              char(16)             not null,      
promlevel            smallint             not null,
promdisc             decimal(5,2)         not null,       -----�ۿۣ�9�ۣ�90��8�ۣ�80 ....)
startdate            datetime             not null,      -----��ʼ����
enddate              datetime             not null,      -----��������
starttime            DATETIME             not null,          
endtime              DATETIME             not null,
promtype             char(1)              not null,
qflag                char(1)              not null,
qty                  int                  not null,
curqty               int                  not null
)
go

create table dbo.DeptLevel
(
DeptLevelID	int		not null primary key,	--��𼶱���
Name		char(16)	not null,		--��𼶱������
LevelValue	int		not null,		--Ȩֵ
LevelWidth	int		not null,		--���
Note		char(32)	not null		--��ע
)
go


--1����ϴ����� (GoodsCombineProm,GoodsCombinePromItem) ��ҵ�����ٵ��
create table dbo.GoodsCombineProm
(
SchemaNo	int		not null,		--��ϴ����������
Name		char(20)	not null,		--��ϴ�����������(������СƱ��ӡ)
Price		dec(10,2)	not null,		--��ϴ����ĵ�λ�ۼ�
StartDate	datetime	not null,		--��ʼ����(YYYYMMDD)
EndDate		datetime	not null,		--��������(YYYYMMDD)
Starttime	datetime	not null,		--��ʼʱ��(YYYYMMDD HH:MM:SS)
EndTime		datetime	not null		--����ʱ��(YYYYMMDD HH:MM:SS)
primary key (SchemaNo)
)
go

create table dbo.GoodsCombinePromItem
(
GoodsID		int		not null,		--��Ʒ����
Qty		dec(12,3)	default 1 not null,	--����Ǳ�ѡ��ƷΪ��
							--�����ѡ��Ʒ������С��ѡ����
Price		dec(10,2)	not null,		--�ۼ� (�����ֶ�)
PromValue	dec(10,2)	not null,		--�ۼ� (�����ۼ�)
SchemaNo	int		not null,		--��ϴ����������
Link_goodsid 	int		not null,		--������Ʒ����,ͬ�������Ʒ����
Gift_flag	int		not null,   		--0=����Ʒ��1=��Ʒ 	
primary key (GoodsID),	--һ����Ʒֻ����һ��������
foreign key (SchemaNo) references GoodsCombineProm (SchemaNo) 
)
go

--ShopClock ����壬����POS��ʲô���
if not exists (select id from sysobjects where id = object_id('dbo.ShopClock') and type = 'U')
create  table ShopClock
(
WorkDate      datetime  not null,
ShiftID       int    default 1 not null,
primary key(ShiftID,WorkDate)
)
go

--POS_turn  POS��¼��¼��ȷ���Ƿ񿪻�
if not exists (select id from sysobjects where id = object_id('dbo.pos_turn') and type = 'U')
create table pos_turn
(
  pos_id		char(4) NOT NULL, --POSID
  workdate	datetime NOT NULL,    --Ӫҵ����
  shiftid	int NOT NULL,         --Ӫҵ��� 
  start_time	datetime DEFAULT getdate() NOT NULL, --��ʼʱ��
  end_time		datetime DEFAULT NULL, --����ʱ��
  stat		int DEFAULT 0 NOT NULL,
  PRIMARY KEY (pos_id, workdate, shiftid)
);
go

--��Ʒ����

--�͸׶��������
if exists (select * from dbo.sysobjects 
	where id = object_id(N'dbo.OilDept') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table dbo.OilDept
GO

Create table dbo.OilDept
(
DeptID       int  not null primary key      --��Ʒ����𣬰������������𣬱�ʾ����Ʒ
)
go


--insert into oildept
--select id from myshopshstock..dept where id between 30101 and 30801;


delete from ShopClock

insert into ShopClock(workdate,shiftID) values(convert(char(8),getdate(),112),1);

--���ݵ��붨���
create table DataImport(
ModuleID	int		not null,		--ģ����
ImportName	char(50)	not null,		--��������
InputName	char(50)	not null,		--�����������ƣ����磺���š����ţ�
ImportFields    char(50)	not null,		--�������ݵ��ֶ�����,ʹ��','�ָ�
ImportSQL	char(255)	not null,		--����ԴSQL��䣬��ImportFields��Ӧ��ʹ��'%s'������������
primary key (ModuleID,ImportName)
);
go

--�����޸ı��ʽ����
create table Expression(
ModuleID	int		not null,		--ģ����
ExprName	char(50)	not null,		--��ʽ����
FieldName	char(25)	not null,		--�����ֶ�����
Expression	char(255)	not null,		--���ʽ
primary key(ModuleID,ExprName)
);
go

