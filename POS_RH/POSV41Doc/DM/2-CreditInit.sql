
--BP项目挂帐卡初始化脚本
--创建：谭鹏飞，2004.05
--修改：
--注意事项：
--1.在提交本脚本之前，应保证CreditClient.sql脚本已提交
--2.本脚本应根据CreditClient.sql提交到的数据库，修改数据库名MYSHOPCMStock或是MYSHOPCMConnect
--  或是MYSHOPCMCard
--3.挂帐卡条码格式为二进制数据，因不能采用SQL方式增加，因此只能系统运行后手工设计

use MYSHOPCMConnect
go

--
-- TABLE INSERT STATEMENTS
--
delete from Module where SystemID=55
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 550701, 936, 55, 191, 'P5501', 'M550701', '卡资料条码布局设计', '卡资料条码布局设计', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 550702, 936, 55, 191, 'P5501', 'M550702', '卡资料条码标签布局设计', '卡资料条码标签布局设计', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 550713, 936, 55, 191, 'P5501', 'M550713', '挂帐卡条码补打', '挂帐卡条码补打', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551101, 936, 55, 191, 'P5501', 'M551101', '卡分店维护单', '卡分店维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551102, 936, 55, 49, 'P5501', 'M551102', '卡分店维护单查询', '查询卡分店维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551103, 936, 55, 49, 'P5501', 'M551103', '卡分店查询', '查询卡分店', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551201, 936, 55, 191, 'P5501', 'M551201', '卡类型维护单', '卡类型维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551202, 936, 55, 49, 'P5501', 'M551202', '卡类型维护单查询', '查询卡类型维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551203, 936, 55, 49, 'P5501', 'M551203', '卡类型查询', '查询卡类型', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551401, 936, 55, 191, 'P5501', 'M551401', '币种维护单', '币种维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551402, 936, 55, 49, 'P5501', 'M551402', '币种维护单查询', '查询币种维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551403, 936, 55, 49, 'P5501', 'M551403', '币种资料查询', '查询币种资料', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551501, 936, 55, 191, 'P5501', 'M551501', '支付类型维护单', '支付类型维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551502, 936, 55, 49, 'P5501', 'M551502', '支付类型维护单查询', '查询支付类型维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 551503, 936, 55, 49, 'P5501', 'M551503', '支付类型查询', '查询支付类型', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 554105, 936, 55, 191, 'P5501', 'M554105', '挂帐卡开卡单', '挂帐卡开卡单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 554106, 936, 55, 177, 'P5501', 'M554106', '挂帐卡开卡入帐单', '挂帐卡开卡入帐单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 554107, 936, 55, 49, 'P5501', 'M554107', '挂帐卡开卡单查询', '挂帐卡开卡单查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 556305, 936, 55, 191, 'P5501', 'M556305', '挂帐卡挂失单', '挂帐卡挂失单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 556306, 936, 55, 49, 'P5501', 'M556306', '挂帐卡挂失单查询', '挂帐卡挂失单查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 556403, 936, 55, 191, 'P5501', 'M556403', '挂帐卡解挂失单', '挂帐卡解挂失单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 556404, 936, 55, 49, 'P5501', 'M556504', '挂帐卡解挂失单查询', '挂帐卡解挂失单查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 556505, 936, 55, 191, 'P5501', 'M556505', '挂帐卡冻结单', '挂帐卡冻结单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 556506, 936, 55, 49, 'P5501', 'M556506', '挂帐卡冻结单查询', '挂帐卡冻结单查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 556603, 936, 55, 191, 'P5501', 'M556603', '挂帐卡解冻结单', '挂帐卡解冻结单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 556604, 936, 55, 49, 'P5501', 'M556604', '挂帐卡解冻结单查询', '挂帐卡解冻结单查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 557103, 936, 55, 191, 'P5501', 'M557103', '帐户充值单', '帐户充值单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 557104, 936, 55, 49, 'P5501', 'M557104', '帐户充值单查询', '帐户充值单查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 557105, 936, 55, 191, 'P5501', 'M557105', '挂帐卡清帐单', '挂帐卡清帐单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 557106, 936, 55, 49, 'P5501', 'M557106', '挂帐卡清帐单查询', '挂帐卡清帐单查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558001, 936, 55, 127, 'P5501', 'M558001', '经营类型维护', '客户经营类型', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558101, 936, 55, 127, 'P5501', 'M558101', '车型维护', '车型维护', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558110, 936, 55, 115, 'P5501', 'M558110', '客户资料维护', '客户资料维护', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558111, 936, 55, 113, 'P5501', 'M558111', '客户资料查询', '客户资料查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558120, 936, 55, 115, 'P5501', 'M558120', '挂帐卡资料维护', '子卡资料维护', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558121, 936, 55, 113, 'P5501', 'M558121', '挂帐卡资料查询', '子卡资料查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558201, 936, 55, 127, 'P5501', 'M558201', '挂帐卡消费油站维护', '挂帐卡消费油站维护', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558210, 936, 55, 127, 'P5501', 'M558210', '消费类别维护', '挂帐卡消费类别维护', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558220, 936, 55, 255, 'P5501', 'M558220', '主帐户信贷额度维护单', '主帐户信贷额度维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558221, 936, 55, 113, 'P5501', 'M558221', '主帐户信贷额度维护单查询', '主帐户信贷额度维护单查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558230, 936, 55, 255, 'P5501', 'M558230', '挂帐卡信贷额度维护单', '挂帐卡信贷额度维护单', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558231, 936, 55, 113, 'P5501', 'M558231', '挂帐卡信贷额度维护单查询', '挂帐卡信贷额度维护单查询', -1, 1 ) 
go
INSERT INTO Module ( ModuleID, LanguageID, SystemID, EnabledRightBit, DLLName, Name, Caption, HINT, ResourceIndex, Enabled ) 
		 VALUES ( 558301, 936, 55, 113, 'P5501', 'M558301', '三级帐查询', '卡三级帐查询', -1, 1 ) 
go

--
-- TABLE INSERT MyMenu
--
delete from mymenu where systemid=55
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5500100, 936, 55, 0, '系统', '系统管理功能|保证系统正常运行', 10, 10, 0, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5500200, 936, 55, 0, '基本资料', '基本资料', 20, 20, 0, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5500300, 936, 55, 0, '挂帐卡功能维护', '卡功能维护|对挂帐卡消费功能进行维护', 30, 20, 0, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5500400, 936, 55, 0, '发售', '发售储值卡|储值卡发售管理', 40, 30, 0, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5500500, 936, 55, 0, '异动', '储值卡异动|储值卡异动管理', 50, 30, 0, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5500600, 936, 55, 0, '续卡与返利', '续卡与返利', 60, 30, 0, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5500700, 936, 55, 0, '查询', '查询', 70, 30, 0, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5500800, 936, 55, 111001, '', '', 10, 10, 5500100, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5500900, 936, 55, 559913, '', '', 20, 20, 5500100, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501000, 936, 55, 559918, '', '', 30, 20, 5500100, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501100, 936, 55, 559919, '', '', 40, 20, 5500100, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501200, 936, 55, 559914, '', '', 50, 30, 5500100, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501300, 936, 55, 111501, '', '', 60, 40, 5500100, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501400, 936, 55, 119901, '', '', 70, 50, 5500100, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501500, 936, 55, 551101, '', '', 10, 10, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501600, 936, 55, 551102, '', '', 20, 10, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501700, 936, 55, 551103, '', '', 30, 10, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501800, 936, 55, 551201, '', '', 40, 20, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5501900, 936, 55, 551202, '', '', 50, 20, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502000, 936, 55, 551203, '', '', 60, 20, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502100, 936, 55, 551401, '', '', 70, 30, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502200, 936, 55, 551402, '', '', 80, 30, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502300, 936, 55, 551403, '', '', 90, 30, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502400, 936, 55, 551501, '', '', 100, 40, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502500, 936, 55, 551502, '', '', 110, 40, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502600, 936, 55, 551503, '', '', 120, 40, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502700, 936, 55, 550701, '', '', 130, 50, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502800, 936, 55, 550702, '', '', 140, 50, 5500200, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5502900, 936, 55, 558001, '', '', 10, 10, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503000, 936, 55, 558101, '', '', 20, 10, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503100, 936, 55, 558110, '', '', 30, 20, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503200, 936, 55, 558120, '', '', 40, 20, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503300, 936, 55, 558201, '', '', 50, 30, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503400, 936, 55, 558210, '', '', 60, 30, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503500, 936, 55, 558111, '', '', 70, 40, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503600, 936, 55, 558121, '', '', 80, 40, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503700, 936, 55, 557105, '', '', 90, 50, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503800, 936, 55, 558220, '', '', 100, 50, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5503900, 936, 55, 558230, '', '', 110, 50, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504000, 936, 55, 557106, '', '', 120, 60, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504100, 936, 55, 558221, '', '', 130, 60, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504200, 936, 55, 558231, '', '', 140, 60, 5500300, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504300, 936, 55, 554105, '', '', 10, 10, 5500400, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504400, 936, 55, 554107, '', '', 20, 10, 5500400, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504500, 936, 55, 554106, '', '', 30, 10, 5500400, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504600, 936, 55, 550713, '', '', 40, 20, 5500400, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504700, 936, 55, 556305, '', '', 10, 10, 5500500, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504800, 936, 55, 556306, '', '', 20, 10, 5500500, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5504900, 936, 55, 556403, '', '', 30, 20, 5500500, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505000, 936, 55, 556404, '', '', 40, 20, 5500500, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505100, 936, 55, 556505, '', '', 50, 30, 5500500, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505200, 936, 55, 556506, '', '', 60, 30, 5500500, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505300, 936, 55, 556603, '', '', 70, 40, 5500500, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505400, 936, 55, 556604, '', '', 80, 40, 5500500, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505500, 936, 55, 557103, '', '', 10, 10, 5500600, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505600, 936, 55, 557104, '', '', 20, 10, 5500600, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505700, 936, 55, 558301, '', '', 10, 10, 5500700, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505800, 936, 55, 119601, '', '', 20, 20, 5500700, 0, '', 0 ) 
go
INSERT INTO MyMenu ( MenuID, LanguageID, SystemID, ModuleID, Caption, HINT, OrderNO, GroupNO, MasterMenuID, IfReConnect, ConnectAlias, EntryModuleID ) 
		 VALUES ( 5505900, 936, 55, 119603, '', '', 30, 20, 5500700, 0, '', 0 ) 
go

--
-- TABLE INSERT GroupRight
--
delete from GroupRight where moduleid between 500001 and 599999
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 550701, 63 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 550702, 63 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 550713, 63 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551101, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551102, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551103, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551201, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551202, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551203, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551401, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551402, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551403, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551501, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551502, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551503, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551601, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551602, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551603, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551801, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551802, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 551803, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552101, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552102, 177 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552103, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552201, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552401, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552402, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552501, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552502, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552601, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 552602, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 553201, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554101, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554102, 177 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554103, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554104, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554105, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554106, 177 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554107, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554201, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554301, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554501, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554601, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 554602, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 555101, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 555201, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 555202, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 555301, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 555302, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 555401, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 555501, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 555601, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 555602, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556101, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556102, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556103, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556104, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556201, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556202, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556301, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556302, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556304, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556305, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556306, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556401, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556402, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556403, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556404, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556501, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556502, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556504, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556505, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556506, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556601, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556602, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556603, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556604, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556701, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556702, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556801, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556802, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556901, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 556902, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557101, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557102, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557103, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557104, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557105, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557106, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557201, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557202, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557301, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 557302, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558001, 63 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558101, 63 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558110, 59 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558111, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558120, 59 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558121, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558201, 63 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558210, 63 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558220, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558221, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558230, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558231, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 558301, 49 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 559901, 191 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 559913, 63 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 559914, 3 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 559918, 15 ) 
go
INSERT INTO GroupRight ( GroupID, ModuleID, RightValues ) 
		 VALUES ( 1, 559919, 8 ) 
go

use MYSHOPCMCard
go

--建立业务数据库XXStock中Dept的视图到卡数据库中，以给表挂帐卡消费类别表使用
if exists (select 1
            from  sysobjects
           where  id = object_id('Dept')
            and   type = 'V')
   drop view Dept
go


/*==============================================================*/
/* View: Dept 小类视图表                                        */
/*==============================================================*/
CREATE VIEW Dept AS
  select * from MYSHOPCMStock.dbo.Dept
go

--在原卡客户资料表Guest中增加信贷额度字段
--原POS系统在判断是否可消费时需修改为信贷额度加上余额detail值是否为正进行
--预付款客户信贷额为0.00，信贷客户可设置
ALTER TABLE dbo.Guest ADD Credit dec(12,2) default 0.00
go



--
delete from TabFldDesc where tabName='BusinessType'
go

INSERT INTO TabFldDesc ( TabName, FldName, FldSName, FldOrder, DataType, DataLen, isAllowNull, Note, LastModiTime, LastModiOPID ) 
		 VALUES ( 'BusinessType', 'BusinessTypeID', '经营类型编码', 0, 2, 0, 0, '', '05/15/2004 12:00:00.000 AM', 'system' ) 
go
INSERT INTO TabFldDesc ( TabName, FldName, FldSName, FldOrder, DataType, DataLen, isAllowNull, Note, LastModiTime, LastModiOPID ) 
		 VALUES ( 'BusinessType', 'BusinessTypeName', '经营类型名称', 1, 1, 0, 0, '', '05/15/2004 12:00:00.000 AM', 'system' ) 
go
INSERT INTO TabFldDesc ( TabName, FldName, FldSName, FldOrder, DataType, DataLen, isAllowNull, Note, LastModiTime, LastModiOPID ) 
		 VALUES ( 'BusinessType', 'LastModiOPID', '最近修改人', 4, 1, 0, 1, '', '05/15/2004 12:00:00.000 AM', 'system' ) 
go
INSERT INTO TabFldDesc ( TabName, FldName, FldSName, FldOrder, DataType, DataLen, isAllowNull, Note, LastModiTime, LastModiOPID ) 
		 VALUES ( 'BusinessType', 'LastModiTime', '最近修改时间', 3, 3, 0, 1, '', '05/15/2004 12:00:00.000 AM', 'system' ) 
go
INSERT INTO TabFldDesc ( TabName, FldName, FldSName, FldOrder, DataType, DataLen, isAllowNull, Note, LastModiTime, LastModiOPID ) 
		 VALUES ( 'BusinessType', 'Note', '备注', 2, 1, 0, 1, '', '05/15/2004 12:00:00.000 AM', 'system' ) 
go

delete from businesstype where 1=1
insert into businesstype(BusinessTypeID,BusinessTypeName,BusinessSName,LastModiOPID)
values(1,'个人','个人','system');
insert into businesstype(BusinessTypeID,BusinessTypeName,BusinessSName,LastModiOPID)
values(2,'政府单位','政府单位','system');
insert into businesstype(BusinessTypeID,BusinessTypeName,BusinessSName,LastModiOPID)
values(3,'私营企业','私营企业','system');
insert into businesstype(BusinessTypeID,BusinessTypeName,BusinessSName,LastModiOPID)
values(4,'部队','部队','system');

delete from cartype where 1=1
insert into cartype(CarTypeID,CarTypeName,CarTypeSName,OilTankCapacity,LastModiOPID)
values(1,'大卡车','大卡车',100,'system');
insert into cartype(CarTypeID,CarTypeName,CarTypeSName,OilTankCapacity,LastModiOPID)
values(2,'小汽车','小汽车',100,'system');
insert into cartype(CarTypeID,CarTypeName,CarTypeSName,OilTankCapacity,LastModiOPID)
values(3,'货车','货车',100,'system');
insert into cartype(CarTypeID,CarTypeName,CarTypeSName,OilTankCapacity,LastModiOPID)
values(4,'大巴车','大巴车',100,'system');
insert into cartype(CarTypeID,CarTypeName,CarTypeSName,OilTankCapacity,LastModiOPID)
values(5,'小巴车','小巴车',100,'system');

--卡类型
delete from cardflag where flag = 3;
insert into cardflag(flag,value) values(3,'挂帐卡');

delete from config where name = '油站编号';
delete from config where name = '挂帐卡默认允许消费类别';
insert into config(SystemID,Name,Value,Note) values(55,'油站编号','0001','挂帐卡号规则包含4位编号');
insert into config(SystemID,Name,Value,Note) values(55,'挂帐卡默认允许消费类别','10101,10102,10103','设置默认允许消费类别，以逗号分隔，最多只能设置6组');

--自动生成单号初始化
delete from serialnumber where serialid in (5524,5558,5559,5560,5561,5562,5563,558001,558101);

insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (5524,'挂帐卡开单',0,0,Getdate(),1,1,0);
insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (5558,'挂帐卡冻结单',0,0,Getdate(),1,1,0);
insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (5559,'挂帐卡解冻结单',0,0,Getdate(),1,1,0);
insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (5560,'挂帐卡挂失单',0,0,Getdate(),1,1,0);
insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (5561,'挂帐卡解挂失单',0,0,Getdate(),1,1,0);
insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (5562,'挂帐客户充值单',0,0,Getdate(),1,1,0);
insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (5563,'挂帐卡清帐单',0,0,Getdate(),1,1,0);

insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (5582,'主卡信贷额度单',0,0,Getdate(),1,1,0);
insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (5583,'挂帐卡信贷额度单',0,0,Getdate(),1,1,0);

insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (558001,'客户经营类型',0,0,Getdate(),0,1,0);
insert into SerialNumber(SerialID,Name,Flag,SerialNumber,ResetDate,DailyReset,ServerTranFlag,SheetFlag)
Values (558101,'车型编码',0,0,Getdate(),0,1,0);

--商品类型参数初始化
delete from oiltype where 1=1
insert into oiltype(OilTypeID, OilTypeName,  OilTypeSName,  OilGunCNums,  Note, LastModiTime ,  LastModiOPID) 
values(1,'90#','90#',30,'',getdate(),'system');
insert into oiltype(OilTypeID, OilTypeName,  OilTypeSName,  OilGunCNums,  Note, LastModiTime ,  LastModiOPID) 
values(2,'91#','91#',30,'',getdate(),'system')
insert into oiltype(OilTypeID, OilTypeName,  OilTypeSName,  OilGunCNums,  Note, LastModiTime ,  LastModiOPID) 
values(3,'92#','92#',30,'',getdate(),'system')
insert into oiltype(OilTypeID, OilTypeName,  OilTypeSName,  OilGunCNums,  Note, LastModiTime ,  LastModiOPID) 
values(4,'93#','93#',30,'',getdate(),'system')



--价签可打印域定义
delete from LabelFields where 1=1

insert into LabelFields(labelchsname,labelfieldname) values ('[客户编号]','MemberID');
insert into LabelFields(labelchsname,labelfieldname) values ('[公司名称]','Name');
insert into LabelFields(labelchsname,labelfieldname) values ('[卡号]','CardNo');
insert into LabelFields(labelchsname,labelfieldname) values ('[油本编号]','CustomNo');
insert into LabelFields(labelchsname,labelfieldname) values ('[信贷额度]','Credit');
insert into LabelFields(labelchsname,labelfieldname) values ('[开卡日期]','StartDate');


--------------------条码价签打印---------------------------------
delete from BarcodeConfig where 1=1;

--Zebra打印机
insert into BarcodeConfig
    (id,name,CrToEmpty,IsBinaryCmd,
     InitCmd,
     LabelCmdLoopBefore,
     LabelCmdLoop,
     LabelCmdLoopAfter,
     XorImage,
     EAN8BarcodeCmd,
     EAN13BarcodeCmd,
     Angle0,Angle90,Angle180,Angle270,
     EndBeginCmd,
     EndLoopCmd,
     EndAutoLoopCmd,
     ContinueLoop)
values
     (1,'ZEBRA打印机',1,0,
      '''^XA<CR>^LH0,0<CR>''',
      '''~DGR:''+[FName]+'',''+str([ImageHeight]*[ImageWidthBytes])+'',''+str([ImageWidthBytes])+'',<CR>''+[ImageData]',
      '''^FO''+str([LabelLeft])+'',''+str([LabelTop])+''^XGR:''+[FName]+'',1,1^FS<CR>''',
      '',
      0,
      '''^FO''+str(BarLeft)+'',''+str([BarTop])+''^BY''+str([BarModul])+'',''+str([BarRatio])+''<CR>''+''^B8''+[BarAngle]+'',''+str([BarHeight])+''<CR>^FD''+[Barcode]+''^FS<CR>''',
      '''^FO''+str(BarLeft)+'',''+str([BarTop])+''^BY''+str([BarModul])+'',''+str([BarRatio])+''<CR>''+''^BE''+[BarAngle]+'',''+str([BarHeight])+''<CR>^FD''+[Barcode]+''^FS<CR>''',
      'N','R','I','B',
      'IF([dups]=1,''^XZ<CR>'','''')',
      '',
      '''^PQ''+str([dups])+''<CR>^XZ<CR>''',
      0);

--SATO打印机
insert into BarcodeConfig
    (id,name,CrToEmpty,IsBinaryCmd,
     InitCmd,
     LabelCmdLoopBefore,
     LabelCmdLoop,
     LabelCmdLoopAfter,
     XorImage,
     EAN8BarcodeCmd,
     EAN13BarcodeCmd,
     Angle0,Angle90,Angle180,Angle270,
     EndBeginCmd,
     EndLoopCmd,
     EndAutoLoopCmd,
     ContinueLoop)
values
     (2,'SATO打印机',1,0,
      '''<ESC>A<CR><ESC>H0000<ESC>V0000<CR>''',
      '',
      '''<ESC>H''+FormatFloat(''0000'',[LabelLeft])+''<ESC>V''+FormatFloat(''0000'',[LabelTop])+''<CR>''+''<ESC>GH''+FormatFloat(''000'',[ImageWidthBytes])+FormatFloat(''000'',[ImageHeightBytes])+''<CR>''+[ImageData]',
      '',
      0,
      '''<ESC>H''+FormatFloat(''0000'',[BarLeft])+''<ESC>V''+FormatFloat(''0000'',[BarTop])+''<CR>''+''<ESC>''+[BarAngle]+''<CR>''+''<ESC>BD402''+FormatFloat(''000'',[BarHeight])+[Barcode]+''<CR><ESC>%0''',
      '''<ESC>H''+FormatFloat(''0000'',[BarLeft])+''<ESC>V''+FormatFloat(''0000'',[BarTop])+''<CR>''+''<ESC>''+[BarAngle]+''<CR>''+''<ESC>BD302''+FormatFloat(''000'',[BarHeight])+[Barcode]+''<CR><ESC>%0''',
      '%0','%1','%2','%3',
      '''<ESC>Q1<ESC>Z<CR>''',
      '''<ESC>A<ESC>C<ESC>Z<CR>''',
      '',
      0);
      
--ARGOX打印机
insert into BarcodeConfig
    (id,name,CrToEmpty,IsBinaryCmd,
     InitCmd,
     LabelCmdLoopBefore,
     LabelCmdLoop,
     LabelCmdLoopAfter,
     XorImage,
     EAN8BarcodeCmd,
     EAN13BarcodeCmd,
     Angle0,Angle90,Angle180,Angle270,
     EndBeginCmd,
     EndLoopCmd,
     EndAutoLoopCmd,
     ContinueLoop)
values
     (3,'ARGOX打印机',0,1,
      '''<ESC>KX''+FormatFloat(''0000'',[PageHeight])+''<CR><ESC>KI#00#<CR>''',
      '',
      '''GW''+str([LabelLeft])+'',''+str([LabelTop])+'',''+str([ImageWidthBytes])+'',''+str([ImageHeight])+'',#''+[ImageData]+''#''',
      '',
      1,
      '''B''+str(BarLeft)+'',''+str([BarTop])+'',''+[BarAngle]+'',E80,''+str([BarModul])+'',''+str([BarModul])+'',''+str([BarHeight])+'',B,"''+[Barcode]+''"<CR>''',
      '''B''+str(BarLeft)+'',''+str([BarTop])+'',''+[BarAngle]+'',E30,''+str([BarModul])+'',''+str([BarModul])+'',''+str([BarHeight])+'',B,"''+[Barcode]+''"<CR>''',
      '0','1','2','3',
      '''P1,1''',
      '',
      '',
      1);
      
--
--
delete from SheetType
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 550000, '所有单据' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 554101, '售卡单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 554103, '挂帐卡开卡单审核' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 554105, '换卡开卡单重新编辑' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 554106, '挂帐卡开卡单审核取消' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 555201, '卡回收单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 555301, '卡回收取消单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 555601, '卡清理单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 556101, '退卡减少' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 556201, '换卡单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 556301, '挂失单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 556401, '解挂失单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 556501, '冻结单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 556601, '解冻结单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 556801, '金额修补单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 556901, '积分修补单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 557101, '金额充值单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 557201, '续卡单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 557301, '积分反利单' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 559001, '卡消费' ) 
go

INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 557105, '挂帐卡清帐' ) 
go

INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 558220, '主卡信贷额度调整' ) 
go
INSERT INTO SheetType ( SheetType, Name ) 
		 VALUES ( 558230, '挂帐卡信贷额度调整' ) 
go
INSERT INTO SheetType (SheetType,Name) values ('559002','卡冲正')
go

delete from LabelFields where 1=1
go
INSERT INTO LabelFields ( labelchsname, labelfieldname ) 
		 VALUES ( '[公司名称]', 'Name' ) 
go
INSERT INTO LabelFields ( labelchsname, labelfieldname ) 
		 VALUES ( '[挂帐卡号]', 'SubCardNo' ) 
go
INSERT INTO LabelFields ( labelchsname, labelfieldname ) 
		 VALUES ( '[开卡日期]', 'StartDate' ) 
go
INSERT INTO LabelFields ( labelchsname, labelfieldname ) 
		 VALUES ( '[客户编号]', 'MemberID' ) 
go
INSERT INTO LabelFields ( labelchsname, labelfieldname ) 
		 VALUES ( '[信贷额度]', 'CREDIT' ) 
go
INSERT INTO LabelFields ( labelchsname, labelfieldname ) 
		 VALUES ( '[油本编号]', 'CustomNo' ) 
go
      
INSERT INTO LabelFields ( labelchsname, labelfieldname ) 
		 VALUES ( '[主 帐 号]', 'CardNo' ) 
go


insert into LabelFields(labelchsname,labelfieldname) 
          values ('[车牌号码]','CarID');

insert into LabelFields(labelchsname,labelfieldname) 
          values ('[参考油品]','OilTypeID');

insert into LabelFields(labelchsname,labelfieldname) 
          values ('[消费升数]','MaxOilQtyPerTrans');
