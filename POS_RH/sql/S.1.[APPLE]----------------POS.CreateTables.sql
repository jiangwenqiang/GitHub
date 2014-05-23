------------------------------------------------------------------------------------------------------------------
-- $Author:: Xianshiyang         $  
-- $Date:: 04-08-02 11:49        $  
-- $Modtime:: 04-08-02 11:42     $  
-- $Revision:: 6                 $  
------------------------------------------------------------------------------------------------------------------

/*==============================================================================*/
/*  MCIS3.2门店POS数据库结构(SQLServer版)					*/
/*  说明:									*/
/*	文件名叫dbpos32_s表示dbpos库3.2版本,_s表示是SQLServer版			*/
/*	dbpos:POS用库(dbpos32_s.sql)						*/
/*  建立: 唐天明 2002.07.10							*/
/*  最后修改：辛东春 2002.08.27							*/
/*  注意									*/
/*   1.本脚本不区分大小写，即建议安装时使SQL SERVER不区分大小写 		*/
/*   2.创建数据库时其大小和文件名及路径根据实际情况修改         		*/
/*   3.创建的主键均以PRI_XXXXX，XXXXX为表名的前五个字符				*/
/*  修改记录:                                                   		*/	
/*      辛东春 2002.07.14 修改以下内容：					*/
/*         1、将所有的deptno统一为char(15)					*/
/*         2、更改存储过程getaccurate为SQL Server语法				*/
/*         3、加入建数据库脚本							*/
/*      辛东春 2002.07.21 加入sale_jif,pay_jif 					*/
/*      辛东春 2002.07.22 对于价格因子字段x的默认值改为1。			*/
/*      辛东春 2002.08.14 加入取单据号、取序列号、取服务器日期模块		*/
/*		邓德平 2002.08.20 加入rj_pos存储过程                      	*/
/*      辛东春 2002.08.25 更改sale_jrep,pay_jrep表的serialid为自动增长字段	*/
/*      辛东春 2002.08.27 检查表结构						*/
/*      辛东春 2002.09.16 加入Pos_SumMoney过程，用于统计实时统计		*/
/*	赵剑   2003.05.23 APPLE1.0移植						*/
/*==============================================================================*/

--修改 2004.06.03 增加 组合促销表
--1、组合促销表 (GoodsCombineProm,GoodsCombineItemProm) 比业务库表少店号
--ShopClock 公告板，告诉POS是什么班次
--POS_turn  POS登录记录表，确认是否开机
--油品类别表	OilDept
--数据导入定义表	DataImport
--批量修改表达式定义	Expression

SET QUOTED_IDENTIFIER OFF
GO
SET ANSI_NULLS ON 
GO
SET ANSI_NULL_DFLT_ON ON
GO
--创建表结构

/*==============================================================*/
/* Table : clerk_lst 收银员列表      （前台监控维护）           */
/*==============================================================*/
create table dbo.clerk_lst (
clerk_id             char(4)              not null,	--收银员ID
name                 char(8)              not null,	--收银员姓名
levelid              smallint             not null,	--收银员权限级别
auth                 int      default 0   not null,	--是否可强行登录（1：可，其他：不允许）
max_disc             smallint default 0   not null,	--最大授权折扣率（若收银员有手工折扣的授权权限时才可用）
passwd               char(8)              not null,	--加密密码
constraint pri_clerk primary key  (clerk_id)
);
go

/*==============================================================*/
/* Table : right_lst 权限表                                           */
/*==============================================================*/
create table dbo.right_lst (
power_id             int                  not null,
name                 char(15)             not null,
constraint u160_309 primary key  (power_id)
)
go


/*==============================================================*/
/* Table : clerk_lst 收银员权限清单表  （前台监控维护）         */
/*==============================================================*/
create table dbo.power_lst (
levelID              smallint             not null,	--级别ID
power_id             int                  not null,	--权限ID
name                 char(8)              not null,	--权限名称（未用，在right_lst表中体现）
constraint pri_power primary key  (levelID, power_id),
foreign key(power_id) references right_lst(power_id)
);
go

/*==============================================================*/
/* Table : pos_lst POS机列表 （前台监控维护）                   */
/*==============================================================*/
create table dbo.pos_lst (
pos_id               char(4)              not null,	--POS机台号
logondate            datetime             null,		--登录日期
listno               int                  not null,	--当前流水号
current_op           char(4)              null,		--当前收银员ID
keybd_type           char(3)              null,		--键盘类型
authorizer_id        char(4)              null,		--授权人ID(未用）
stat                 char(1)              null,		--当前状态（0：空闲、1：已签到、9：锁定）
max_disc             smallint             not null,	--未用
current_level        smallint             not null,	--未用
desp                 char(20)             null,		--简单描述
ip                   char(15)             null,		--IP地址
mac_addr             char(24)             null,		--MAC地址
placeno              char(6)    	  null,		--柜组号，NULL:是柜组机，非空：超市机
posgroup             char(8)    default 'BASIC'  not null,	--POS机组别
ShiftID 	     int	default 1 not null,   		--班次信息
ShiftStatus 	     int	default 0 not null,   		--班结状态 0=待开始,1已完成
constraint pri_pos_l primary key  (pos_id)
);
go

/*==============================================================*/
/* Table : keybd_cfg:键盘功能映射表    （前台监控维护）         */
/*==============================================================*/
create table dbo.keybd_cfg (
keybd_type           char(3)              not null,	--键盘类型
key_value            smallint             not null,	--键盘扫描码值
keymap_value         smallint             not null,	--功能号
constraint pri_keybd primary key  (keybd_type, key_value)
);
go

/*==============================================================*/
/* Table : keybd_lst键盘类型定义表  （前台监控维护）            */
/*==============================================================*/
create table dbo.keybd_lst (
keybd_type           char(3)      	  not null,	--键盘类型
keybd_class          char(3)              not null,	--键盘物理类名称
keybd_desp           char(20)             null,		--简单描述
note                 char(80)             null,		--说明
constraint u105_22 primary key  (keybd_type)
);
go

/*==============================================================*/
/* Table : function_lst：POS功能列表 初始化时维护               */
/*==============================================================*/
create table dbo.function_lst (
keymap_value         smallint             not null,	--键盘功能号（ASCII字符）
fun                  char(20)             not null,	--功能说明(英文）
hzfun                char(20)             not null,	--功能说明(中文）
constraint pri_funct primary key  (keymap_value)
);
go


/*==============================================================*/
/* Table : rate_lst币种汇率表（前台监控维护）                   */
/*==============================================================*/
create table dbo.rate_lst (
ffx                  char(3)              not null,	--币别国际代码ID（156/934等）
curren_code          char(3)              not null,	--币别代码（RMB/USD/HKD等）
curren_name          char(10)             not null,	--币别名称（美元/人民币/港币等）
unit_name            char(6)              not null,	--币别单位（元）
rate                 decimal(10,7)        not null,	--与人民币的汇率（例如：1.0700000)
constraint pri_rate_ primary key  (curren_code)
);
go

/*==============================================================*/
/* Table : hotkey_cfg   键盘热键表 （前台监控维护）             */
/*==============================================================*/
create table dbo.hotkey_cfg (
keybd_type           char(3)              not null,	--键盘类型
key_value            smallint             not null,	--键盘扫描码ASCII字符
plu                  char(13)             not null,	--快捷编码（两位时：代表银行类别；六位：代表商品编码；其他：商品条码）
flag 		    int 	not null default 0, 	--热键类型标志 0=银行类别  1=商品编码/条码  2=现金热键
constraint pri_hotkey primary key  (keybd_type, key_value)
);
go

/*==============================================================*/
/* Table : price_lst 商品主档表                                 */
/*==============================================================*/
create table dbo.price_lst (
vgno                 char(6)              not null,	--商品编码
goodsno              char(13)             not null,	--商品条码
gname                char(32)             not null,	--商品名称
uname                char(8)              null,		--商品销售单位
spec                 char(8)             null,		--商品规格
groupno              char(2)              not null,	--大类ID（group='sm'时，表示是色码商品且销售时必须输入色码信息）
deptno               char(15)             not null,	--商品类别
price                decimal(10,2)        not null,	--默认售价
v_type               char(1)              not null,	--条码类型（0：正常，2：称重，3：金额，8：基本码；9：代收银，禁止（4：组合））
p_type               char(1)              not null,	--促销类型（n:无，p:折让，v:单品折扣,d:小类折扣，t:时点折扣,g:柜组折扣，
qflag                char(1)              not null,
x                    int                  default 1 not null,
p_oldtype            char(1)              null,
up_date              datetime             null,
constraint u110_38 primary key  (vgno)
);
go


/*==============================================================*/
/* Table : promotion 促销表                                     */
/*==============================================================*/
create table dbo.promotion (
vgno                 char(6)              not null primary key,	--商品编码
sheetno              char(16)             not null, --促销单号
promtype             char(1)              not null,	--促销类型(0:普通 1:特价 2：惠赠)
promprice            decimal(10,2)        not null, --促销价
qty                  int                  not null, --促销数量
curqty               int                  not null, --当前可促销数量(仅对定量促销有效)
qflag                char(1)              not null, --定量标志(1:为定量,0为非定量)
startdate            datetime             not null, --开始日期
enddate              datetime             not null, --结束日期
starttime            DATETIME  not null,			--开始时间
endtime              DATETIME  not null,			--结束时间
)
go

/*==============================================================*/
/* Table : weight_code 条码格式表                                     */
/*==============================================================*/
create table dbo.weight_code 
  (
    plu_format char(13),							--条码格式
    primary key (plu_format) 
  );

/*==============================================================*/
/* Table : vgnodiscount:时点折让表                              */
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
/* Table : goodsplace   商品柜组对应表                          */
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
/* Table : distitem_vgno  单品折让表                            */
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
/* Table : distitem_dept  类别折让表                            */
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
/* Table : vgbkcard 联名卡表，已不用                            */
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
/* Table : cardnote  银行卡表                                   */
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
/* Table : cdanswer 冲正表，已不用                              */
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
/* Table : pos_para  POS参数表                                  */
/*==============================================================*/
create table dbo.pos_para (
para                 char(16)             not null,
val                  char(64)             not null,
notes                char(64)             not null,
constraint u150_270 primary key  (para)
)
go


/*==============================================================*/
/* Table : pos_ini  POS机组参数设置表                           */
/*==============================================================*/
create table dbo.pos_ini (
posgroup             char(8)              not null,
para                 char(16)             not null,
val                  char(64)             not null,
constraint u151_274 primary key  (posgroup, para)
)
go

/*==============================================================*/
/* Table : posgroup  POS机组别表                                */
/*==============================================================*/
create table dbo.posgroup (
posgroup             char(8)              not null,
notes                char(64)             null,
constraint u152_278 primary key  (posgroup)
)
go

/*==============================================================*/
/* Table : keyname   键名表                                     */
/*==============================================================*/
create table dbo.keyname (
keyvalue             smallint             not null,
keyname              char(16)             not null,
constraint u153_280 primary key  (keyvalue)
)
go

/*==============================================================*/
/* Table : keybd_class  键盘分类表                              */
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
/* Table : syslog   系统日志表                                  */
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
/* Table : keybd_layout  键盘布局表                             */
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
/* Table : pay_log  支付日志表                                  */
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
/* Table : prog_lst   程序版本控制表                            */
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
/* Table : distitem_type   促销类型表                           */
/*==============================================================*/
create table dbo.distitem_type (
fieldname            char(15)             not null,
type                 char(1)              not null,
desp                 char(15)             not null,
constraint u168_396 primary key  (fieldname, type)
)
go


/*==============================================================*/
/* Table : sale_jpick  送货表                                   */
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
/* Table : sale_jls 利是表                                      */
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
/* Table : rjcontrol      日结控制表                            */
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
/* Table : goodscombine  组合商品表                             */
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
/* Table : goodscut  削价商品表                                 */
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
/* Table : log_lst   日志表                                     */
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
/* Table : reportformat  报表格式表                             */
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
/* Table : mymessages 消息表                                    */
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
/* Table : eventlevel  事件级别表                               */
/*==============================================================*/
create table dbo.eventlevel (
eventlevelid         smallint             not null,
note                 varchar(50)          null,
constraint u196_570 primary key  (eventlevelid)
)
go

/*==============================================================*/
/* Table : eventcode  事件代码表                                */
/*==============================================================*/
create table dbo.eventcode (
eventid              smallint             not null,
eventlevelid         smallint             not null,
note                 varchar(50)          null,
constraint u197_572 primary key  (eventid)
)
go


/*==============================================================*/
/* Table : config  系统配置表                                   */
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
/* Table : dictionary  字典表                                   */
/*==============================================================*/
create table dbo.dictionary (
english              char(20)             not null,
LanguageID  int     default 936 not null,       -- 语言编号
chinese              char(20)             not null,
formatdisplay        char(20)             null,
note                 varchar(64)          null,
constraint u199_579 primary key  (english)
)
go

/*==============================================================*/
/* Table : serialnumber 单据流水控制表                          */
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
/* Table : goodsext6   多码多价表                               */
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
/* Table : diffcash 收银员长短款表                              */
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
/* Table : mysqlstr Sql精灵表                                   */
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

--报表格式表
create table dbo.Reports
(
SerialID	int not null,			-- 流水编号
LanguageID	int default 936 not null,	-- 语言编号
Data		image,				-- 报表数据
primary key(LanguageID,SerialID)
)
go


/*==============================================================*/
/* Table : myreport  报表精灵表                                 */
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
/* Table : clerk_logon 收银员登录控制表                         */
/*==============================================================*/
create table dbo.clerk_logon (
clerkid              char(4)              not null,
posid                char(4)              not null,
constraint u213_670 primary key  (clerkid)
)
go

/*==============================================================*/
/* Table : promotion_vip  会员价表                              */
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
/* Table : sale_j 销售流水表                                    */
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
ShiftID 	     int		  default 1 not null,   --班次信息
WorkDate 	     Datetime		  default getdate() not null   --营业日期
)
go


/*==============================================================*/
/* Table : pay_j 支付流水表                                     */
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
ShiftID 	     int		  default 1 not null,   --班次信息
WorkDate 	     Datetime		  default getdate() not null   --营业日期
)
go


/*==============================================================*/
/* Table : sale_jrep  重复销售流水表                            */
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
/* Table : pay_jrep   重复支付流水表                            */
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
/* Table : sale_j_train  培训销售流水表                         */
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
/* Table : pay_j_train  培训支付流水                            */
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

--销售流水接口表
create table dbo.sale_jif ( 
dt                   datetime             not null, 
time                 char(8)              not null,     --改为8位字符 
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

--支付流水接口表
create table dbo.pay_jif ( 
dt                   datetime             not null, 
time                 char(8)              not null,     --改为8位字符 
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
/* Table : accurate积分比例表                                    */
/*==============================================================*/

create table dbo.accurate
(
cardlevelid 	      smallint		 not null,   -- 卡级别编号
deptid		      int		 not null,   -- 类别编号
accurate	      char(12)		 not null,   -- 比例
constraint pri_accur primary key(CardLevelID,DeptID)
);
go


--会员卡信息表
create table dbo.Guest(
  CardNO                char(20)        not null primary key,   --积分卡号
  CardType              char(1)         default '0' not null,   --卡别
  MemberID              char(20)        not null,               --会员号
  SheetID               char(16)        not null,               --发卡单号
  Secrety               char(7)         not null,               --密码
  ShopID                char(4)         not null,               --发卡点
  InitPoint             integer         not null,               --初始积分
  Point                 integer         not null,               --现积分
  LastUseDate           datetime 	null,                                   --最后使用日期
  LastShopID            char(4)		null,                                --最后消费分店号
  LastPOSID             char(4)		null,                                --最后消费POS机号
  LastCashierID         char(4)		null,                                --最后消费收银员号
  PayMoney              dec(12,2)	default 0 not null,                  --购物金额 累计
  Times                 smallint	default 0 not null,                  --购物次数
  Indate                datetime    default getdate() not null,              --发卡日期
  EndDate               datetime    null,                                   --有效期
  SheetIDNew            char(16)        not null,               --最新单号 最近修改状态的单据编号
  Mode                  char(1)         default '1' not null,   --卡状态 1-正常卡r—已回收卡l-挂失卡 -冻结e-已换卡q-退卡
  memberlevel           integer default 0 not null,              --会员级别
  discount		smallint	default 0 not null      --折扣率％,98-- 98折, 0 --无折扣
)

go 
--积会卡消费流水表
create table dbo.GuestPurch0(
  PurchSerial           integer IDENTITY       not null,   --支付流水号
  CardNO                char(20)        not null,               --卡号
  MemberID              char(20)	null,                               --会员编号
  PurchDateTime         datetime    default getdate() not null,         --购物日期
  ReqTime               char(10)        not null,               --请求时间
  PayMoney              dec(10,2)       not null,               --购物金额
  Point                 integer         default 0 not null,               --本次积分
  ShopID                char(4)         not null,               --商店号
  Branchno              char(4)         not null,               --台号
  Cashierno             char(4)         not null,               --收银员号
  ListNO                int		null,                                    --支付流水号
  Stat                  char(1)         not null,               --购物状态 0-正常1-被撤消2-撤消请求3-冲正
  CheckFlag             smallint default 0 not null             --对帐标志 0表示未对帐，1表示已对帐
)

go


--盘点临时表
create table dbo.pd_data0
(
SheetID     char(16)  not null,
vgno        integer   not null,
goodsno     char(13)  not null,
qty         dec(12,3) default 0.000 not null,
team        integer   null,
operator    char(8)   null,
plu         char(13)  null,
orderno     int IDENTITY(1,1) not null --顺序号
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
--说明：取积分比例
--参数：商品编码，积分卡级别
--返回：积分比例
--表： accrate,price_lst
--函数：无
--算法：
--建立：辛东春 2002.07.14
--修改：
--最后修改：辛东春 2002.07.14
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
--说明：取序列号
--参数：单据类型
--返回：序列号，日期
--表： serialnumber
--函数：无
--算法：
--建立：辛东春 2002.08.14
--修改：
--最后修改：辛东春 2002.08.14
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
--过程名：取单据编号			
--表： Config
--函数：GetNewSerial
--算法：
--建立：辛东春 2002.08.14
--修改：
--最后修改：辛东春 2002.08.14
---------------------------------------------------------------
 
  declare @nSerial int,@k int,@dDate datetime
  declare @cShopID varchar(4)
  
  exec GetNewSerial @iSerialID,@nSerial out,@dDate out

  select @cShopID=Value from Config where Name='本店号';
  
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
--说明：取服务器时间
--参数：
--返回：服务器的时间值
--表：
--函数：GetDate()
--算法：
--建立：辛东春 2002.08.14
--修改：
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
--说明：取服务器时间 TL_GetServerDate
--参数：
--返回：服务器的时间值
--表：
--函数：GetDate()
--算法：
--建立：辛东春 2002.08.14
--修改：赵剑 2003.05.23 修改名称
-----------------------------------------------------------------
create procedure dbo.TL_GetServerDate(@dDate datetime output)
as
  select @dDate=GetDate()
return 0
GO

-----------------------------------------------------------------
--Pos_SumMoney			实时计算销售金额
--参数：
--返回：销售金额,客单量,折扣金额，实销金额
--表：  sale_j
--函数：
--算法：
--建立：辛东春 2002.09.14
--修改：辛东春 2002.09.16 加入折扣金额，实销金额统计
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
  raiserror('[%s],断点=%d,Err=%d',16,1,@Msg,@BreakPoint,@Err);
  return -1;

End
Go


-----------------------------------------------------------------
-- Table : promdisc_vip  会员价表                              
-- 建 立 ：xiaoyong 2003.7.28
-----------------------------------------------------------------
create table dbo.promdisc_vip (
vgno                 char(6)              not null,      -----商品编码
sheetno              char(16)             not null,      
promlevel            smallint             not null,
promdisc             decimal(5,2)         not null,       -----折扣（9折：90，8折：80 ....)
startdate            datetime             not null,      -----开始日期
enddate              datetime             not null,      -----结束日期
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
DeptLevelID	int		not null primary key,	--类别级别编号
Name		char(16)	not null,		--类别级别的名称
LevelValue	int		not null,		--权值
LevelWidth	int		not null,		--宽度
Note		char(32)	not null		--备注
)
go


--1、组合促销表 (GoodsCombineProm,GoodsCombinePromItem) 比业务库表少店号
create table dbo.GoodsCombineProm
(
SchemaNo	int		not null,		--组合促销方案编号
Name		char(20)	not null,		--组合促销方案名称(可用于小票打印)
Price		dec(10,2)	not null,		--组合促销的单位售价
StartDate	datetime	not null,		--开始日期(YYYYMMDD)
EndDate		datetime	not null,		--结束日期(YYYYMMDD)
Starttime	datetime	not null,		--开始时间(YYYYMMDD HH:MM:SS)
EndTime		datetime	not null		--结束时间(YYYYMMDD HH:MM:SS)
primary key (SchemaNo)
)
go

create table dbo.GoodsCombinePromItem
(
GoodsID		int		not null,		--商品编码
Qty		dec(12,3)	default 1 not null,	--如果非必选商品为零
							--如果必选商品输入最小必选数量
Price		dec(10,2)	not null,		--售价 (冗余字段)
PromValue	dec(10,2)	not null,		--售价 (本组售价)
SchemaNo	int		not null,		--组合促销方案编号
Link_goodsid 	int		not null,		--链接商品编码,同组根的商品编码
Gift_flag	int		not null,   		--0=非赠品，1=赠品 	
primary key (GoodsID),	--一个商品只能在一个方案中
foreign key (SchemaNo) references GoodsCombineProm (SchemaNo) 
)
go

--ShopClock 公告板，告诉POS是什么班次
if not exists (select id from sysobjects where id = object_id('dbo.ShopClock') and type = 'U')
create  table ShopClock
(
WorkDate      datetime  not null,
ShiftID       int    default 1 not null,
primary key(ShiftID,WorkDate)
)
go

--POS_turn  POS登录记录表，确认是否开机
if not exists (select id from sysobjects where id = object_id('dbo.pos_turn') and type = 'U')
create table pos_turn
(
  pos_id		char(4) NOT NULL, --POSID
  workdate	datetime NOT NULL,    --营业日期
  shiftid	int NOT NULL,         --营业班次 
  start_time	datetime DEFAULT getdate() NOT NULL, --开始时间
  end_time		datetime DEFAULT NULL, --结束时间
  stat		int DEFAULT 0 NOT NULL,
  PRIMARY KEY (pos_id, workdate, shiftid)
);
go

--油品类别表

--油缸读数情况表
if exists (select * from dbo.sysobjects 
	where id = object_id(N'dbo.OilDept') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
	drop table dbo.OilDept
GO

Create table dbo.OilDept
(
DeptID       int  not null primary key      --油品的类别，包含在里面的类别，表示是油品
)
go


--insert into oildept
--select id from myshopshstock..dept where id between 30101 and 30801;


delete from ShopClock

insert into ShopClock(workdate,shiftID) values(convert(char(8),getdate(),112),1);

--数据导入定义表
create table DataImport(
ModuleID	int		not null,		--模块编号
ImportName	char(50)	not null,		--导入名称
InputName	char(50)	not null,		--输入条件名称（比如：单号、类别号）
ImportFields    char(50)	not null,		--导入数据的字段名称,使用','分隔
ImportSQL	char(255)	not null,		--数据源SQL语句，与ImportFields对应，使用'%s'代替输入条件
primary key (ModuleID,ImportName)
);
go

--批量修改表达式定义
create table Expression(
ModuleID	int		not null,		--模块编号
ExprName	char(50)	not null,		--公式名称
FieldName	char(25)	not null,		--调整字段名称
Expression	char(255)	not null,		--表达式
primary key(ModuleID,ExprName)
);
go

