use MyShopPOS
--插入打印机参数
insert into pos_ini(posgroup,para,val) values('BASIC','PRN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory')
go
insert into pos_ini(posgroup,para,val) values('BASIC','PRN_SERCLASS','com.royalstone.pos.services.POSPrinter')
go
insert into pos_ini(posgroup,para,val) values('BASIC','PRN_PORTNAME','COM1')
go
insert into pos_ini(posgroup,para,val) values('BASIC','PRN_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('BASIC','PRN_BYTESIZE','8')
go
insert into pos_ini(posgroup,para,val) values('BASIC','PRN_STOPBITS','0')
go
insert into pos_ini(posgroup,para,val) values('BASIC','PRN_PARITY','0')
go
insert into pos_ini(posgroup,para,val) values('BASIC','PRN_DRIVERNAME','drv/RSPrinter41_TM_U210')
go
--插入钱箱参数
insert into pos_ini(posgroup,para,val) values('BASIC','CASH_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory')
go
insert into pos_ini(posgroup,para,val) values('BASIC','CASH_SERCLASS','com.royalstone.pos.services.POSCashDrawer')
go
insert into pos_ini(posgroup,para,val) values('BASIC','CASH_PORTNAME','COM1')
go
insert into pos_ini(posgroup,para,val) values('BASIC','CASH_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('BASIC','CASH_BYTESIZE','8')
go
insert into pos_ini(posgroup,para,val) values('BASIC','CASH_STOPBITS','0')
go
insert into pos_ini(posgroup,para,val) values('BASIC','CASH_PARITY','0')
go
insert into pos_ini(posgroup,para,val) values('BASIC','CASH_DRIVERNAME','drv/RSCashDrawer_TM_U210')
go
--插入客显参数
insert into pos_ini(posgroup,para,val) values('BASIC','DISP_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory')
go
insert into pos_ini(posgroup,para,val) values('BASIC','DISP_SERCLASS','com.royalstone.pos.services.CustDisplay')
go
insert into pos_ini(posgroup,para,val) values('BASIC','DISP_PORTNAME','COM4')
go
insert into pos_ini(posgroup,para,val) values('BASIC','DISP_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('BASIC','DISP_BYTESIZE','8')
go
insert into pos_ini(posgroup,para,val) values('BASIC','DISP_STOPBITS','0')
go
insert into pos_ini(posgroup,para,val) values('BASIC','DISP_PARITY','1')
go
insert into pos_ini(posgroup,para,val) values('BASIC','DISP_DRIVERNAME','drv/RSVFD41_BA63G')
go
--插入扫描仪参数
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory"')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_SERCLASS','com.royalstone.pos.services.RSScanner')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_QUEUINGMODE','on')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_SCANNERMODE','A')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_PORT','COM2')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_BITS','8')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_STOPBITS','1')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_PARITY','none')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_PROTOCOL','none')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_SETDTR','notused')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_SETRTS','notused')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_DSRCONTROL','notused')
go
insert into pos_ini(posgroup,para,val) values('BASIC','SCAN_READTIMEOUT','3000')
-------------------OLIVT----------------------------------------------------------------------------------------------------------------------------
go
--插入打印机参数
insert into pos_ini(posgroup,para,val) values('OLIVT','PRN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','PRN_SERCLASS','com.royalstone.pos.services.POSPrinter')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','PRN_PORTNAME','COM1')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','PRN_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','PRN_BYTESIZE','8')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','PRN_STOPBITS','0')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','PRN_PARITY','0')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','PRN_DRIVERNAME','drv/RSPrinter41_TM_U210')
go
--插入钱箱参数
insert into pos_ini(posgroup,para,val) values('OLIVT','CASH_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','CASH_SERCLASS','com.royalstone.pos.services.POSCashDrawer')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','CASH_PORTNAME','COM1')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','CASH_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','CASH_BYTESIZE','8')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','CASH_STOPBITS','0')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','CASH_PARITY','0')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','CASH_DRIVERNAME','drv/RSCashDrawer_TM_U210')
go
--插入客显参数
insert into pos_ini(posgroup,para,val) values('OLIVT','DISP_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','DISP_SERCLASS','com.royalstone.pos.services.CustDisplay')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','DISP_PORTNAME','COM4')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','DISP_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','DISP_BYTESIZE','8')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','DISP_STOPBITS','0')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','DISP_PARITY','1')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','DISP_DRIVERNAME','drv/RSVFD41_BA63G')
go
--插入扫描仪参数
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory"')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_SERCLASS','com.royalstone.pos.services.RSScanner')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_QUEUINGMODE','on')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_SCANNERMODE','A')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_PORT','COM2')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_BITS','8')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_STOPBITS','1')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_PARITY','none')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_PROTOCOL','none')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_SETDTR','notused')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_SETRTS','notused')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_DSRCONTROL','notused')
go
insert into pos_ini(posgroup,para,val) values('OLIVT','SCAN_READTIMEOUT','3000')
go
---------------------------------------------------------------------
-------------------TRAIN----------------------------------------------------------------------------------------------------------------------------
--插入打印机参数
insert into pos_ini(posgroup,para,val) values('TRAIN','PRN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','PRN_SERCLASS','com.royalstone.pos.services.POSPrinter')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','PRN_PORTNAME','COM1')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','PRN_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','PRN_BYTESIZE','8')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','PRN_STOPBITS','0')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','PRN_PARITY','0')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','PRN_DRIVERNAME','drv/RSPrinter41_TM_U210')
go
--插入钱箱参数
insert into pos_ini(posgroup,para,val) values('TRAIN','CASH_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','CASH_SERCLASS','com.royalstone.pos.services.POSCashDrawer')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','CASH_PORTNAME','COM1')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','CASH_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','CASH_BYTESIZE','8')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','CASH_STOPBITS','0')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','CASH_PARITY','0')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','CASH_DRIVERNAME','drv/RSCashDrawer_TM_U210')
go
--插入客显参数
insert into pos_ini(posgroup,para,val) values('TRAIN','DISP_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','DISP_SERCLASS','com.royalstone.pos.services.CustDisplay')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','DISP_PORTNAME','COM4')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','DISP_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','DISP_BYTESIZE','8')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','DISP_STOPBITS','0')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','DISP_PARITY','1')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','DISP_DRIVERNAME','drv/RSVFD41_BA63G')
go
--插入扫描仪参数
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory"')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_SERCLASS','com.royalstone.pos.services.RSScanner')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_QUEUINGMODE','on')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_SCANNERMODE','A')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_PORT','COM2')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_BAUDRATE','9600')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_BITS','8')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_STOPBITS','1')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_PARITY','none')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_PROTOCOL','none')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_SETDTR','notused')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_SETRTS','notused')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_DSRCONTROL','notused')
go
insert into pos_ini(posgroup,para,val) values('TRAIN','SCAN_READTIMEOUT','3000')
-------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------
--插入打印机参数
go
insert into pos_para(para,val,notes) values('PRN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory','打印机工厂类')
go
insert into pos_para(para,val,notes) values('PRN_SERCLASS','com.royalstone.pos.services.POSPrinter','打印机服务类')
go
insert into pos_para(para,val,notes) values('PRN_PORTNAME','COM1','打印机COM口')
go
insert into pos_para(para,val,notes) values('PRN_BAUDRATE','9600','打印机波特率')
go
insert into pos_para(para,val,notes) values('PRN_BYTESIZE','8','打印机位数')
go
insert into pos_para(para,val,notes) values('PRN_STOPBITS','0','打印机停止位')
go
insert into pos_para(para,val,notes) values('PRN_PARITY','0','打印机奇偶校验码')
go
insert into pos_para(para,val,notes) values('PRN_DRIVERNAME','drv/RSPrinter41_TM_U210','打印机驱动')
go
--插入钱箱参数
insert into pos_para(para,val,notes) values('CASH_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory','钱箱工厂类')
go
insert into pos_para(para,val,notes) values('CASH_SERCLASS','com.royalstone.pos.services.POSCashDrawer','钱箱服务类')
go
insert into pos_para(para,val,notes) values('CASH_PORTNAME','COM1','钱箱COM口')
go
insert into pos_para(para,val,notes) values('CASH_BAUDRATE','9600','钱箱波特率')
go
insert into pos_para(para,val,notes) values('CASH_BYTESIZE','8','钱箱位数')
go
insert into pos_para(para,val,notes) values('CASH_STOPBITS','0','钱箱停止位')
go
insert into pos_para(para,val,notes) values('CASH_PARITY','0','钱箱奇偶校验码')
go
insert into pos_para(para,val,notes) values('CASH_DRIVERNAME','drv/RSCashDrawer_TM_U210','钱箱驱动')
go
--插入客显参数
insert into pos_para(para,val,notes) values('DISP_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory','客显屏工厂类')
go
insert into pos_para(para,val,notes) values('DISP_SERCLASS','com.royalstone.pos.services.CustDisplay','客显屏服务类')
go
insert into pos_para(para,val,notes) values('DISP_PORTNAME','COM4','客显屏COM口')
go
insert into pos_para(para,val,notes) values('DISP_BAUDRATE','9600','客显屏波特率')
go
insert into pos_para(para,val,notes) values('DISP_BYTESIZE','8','客显屏位数')
go
insert into pos_para(para,val,notes) values('DISP_STOPBITS','0','客显屏停止位')
go
insert into pos_para(para,val,notes) values('DISP_PARITY','1','客显屏奇偶校验码')
go
insert into pos_para(para,val,notes) values('DISP_DRIVERNAME','drv/RSVFD41_BA63G','客显屏驱动')
go
--插入扫描仪参数
insert into pos_para(para,val,notes) values('SCAN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory"','扫描仪工厂类')
go
insert into pos_para(para,val,notes) values('SCAN_SERCLASS','com.royalstone.pos.services.RSScanner','扫描仪服务类')
go
insert into pos_para(para,val,notes) values('SCAN_QUEUINGMODE','on','扫描仪QUEUINGMODE')
go
insert into pos_para(para,val,notes) values('SCAN_SCANNERMODE','A','扫描仪SCANNERMODE')
go
insert into pos_para(para,val,notes) values('SCAN_PORT','COM2','扫描仪COM口')
go
insert into pos_para(para,val,notes) values('SCAN_BAUDRATE','9600','扫描仪波特率')
go
insert into pos_para(para,val,notes) values('SCAN_BITS','8','扫描仪位数')
go
insert into pos_para(para,val,notes) values('SCAN_STOPBITS','1','扫描仪停止位')
go
insert into pos_para(para,val,notes) values('SCAN_PARITY','none','扫描仪奇偶校验码')
go
insert into pos_para(para,val,notes) values('SCAN_PROTOCOL','none','扫描仪PROTOCOL')
go
insert into pos_para(para,val,notes) values('SCAN_SETDTR','notused','扫描仪SETDTR')
go
insert into pos_para(para,val,notes) values('SCAN_SETRTS','notused','扫描仪SETRTS')
go
insert into pos_para(para,val,notes) values('SCAN_DSRCONTROL','notused','扫描仪DSRCONTROL')
go
insert into pos_para(para,val,notes) values('SCAN_READTIMEOUT','3000','扫描仪READTIMEOUT')