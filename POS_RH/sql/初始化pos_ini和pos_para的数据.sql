use MyShopPOS
--�����ӡ������
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
--����Ǯ�����
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
--������Բ���
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
--����ɨ���ǲ���
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
--�����ӡ������
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
--����Ǯ�����
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
--������Բ���
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
--����ɨ���ǲ���
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
--�����ӡ������
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
--����Ǯ�����
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
--������Բ���
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
--����ɨ���ǲ���
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
--�����ӡ������
go
insert into pos_para(para,val,notes) values('PRN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory','��ӡ��������')
go
insert into pos_para(para,val,notes) values('PRN_SERCLASS','com.royalstone.pos.services.POSPrinter','��ӡ��������')
go
insert into pos_para(para,val,notes) values('PRN_PORTNAME','COM1','��ӡ��COM��')
go
insert into pos_para(para,val,notes) values('PRN_BAUDRATE','9600','��ӡ��������')
go
insert into pos_para(para,val,notes) values('PRN_BYTESIZE','8','��ӡ��λ��')
go
insert into pos_para(para,val,notes) values('PRN_STOPBITS','0','��ӡ��ֹͣλ')
go
insert into pos_para(para,val,notes) values('PRN_PARITY','0','��ӡ����żУ����')
go
insert into pos_para(para,val,notes) values('PRN_DRIVERNAME','drv/RSPrinter41_TM_U210','��ӡ������')
go
--����Ǯ�����
insert into pos_para(para,val,notes) values('CASH_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory','Ǯ�乤����')
go
insert into pos_para(para,val,notes) values('CASH_SERCLASS','com.royalstone.pos.services.POSCashDrawer','Ǯ�������')
go
insert into pos_para(para,val,notes) values('CASH_PORTNAME','COM1','Ǯ��COM��')
go
insert into pos_para(para,val,notes) values('CASH_BAUDRATE','9600','Ǯ�䲨����')
go
insert into pos_para(para,val,notes) values('CASH_BYTESIZE','8','Ǯ��λ��')
go
insert into pos_para(para,val,notes) values('CASH_STOPBITS','0','Ǯ��ֹͣλ')
go
insert into pos_para(para,val,notes) values('CASH_PARITY','0','Ǯ����żУ����')
go
insert into pos_para(para,val,notes) values('CASH_DRIVERNAME','drv/RSCashDrawer_TM_U210','Ǯ������')
go
--������Բ���
insert into pos_para(para,val,notes) values('DISP_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory','������������')
go
insert into pos_para(para,val,notes) values('DISP_SERCLASS','com.royalstone.pos.services.CustDisplay','������������')
go
insert into pos_para(para,val,notes) values('DISP_PORTNAME','COM4','������COM��')
go
insert into pos_para(para,val,notes) values('DISP_BAUDRATE','9600','������������')
go
insert into pos_para(para,val,notes) values('DISP_BYTESIZE','8','������λ��')
go
insert into pos_para(para,val,notes) values('DISP_STOPBITS','0','������ֹͣλ')
go
insert into pos_para(para,val,notes) values('DISP_PARITY','1','��������żУ����')
go
insert into pos_para(para,val,notes) values('DISP_DRIVERNAME','drv/RSVFD41_BA63G','����������')
go
--����ɨ���ǲ���
insert into pos_para(para,val,notes) values('SCAN_FYCLASS','com.royalstone.pos.services.factories.RSServiceInstanceFactory"','ɨ���ǹ�����')
go
insert into pos_para(para,val,notes) values('SCAN_SERCLASS','com.royalstone.pos.services.RSScanner','ɨ���Ƿ�����')
go
insert into pos_para(para,val,notes) values('SCAN_QUEUINGMODE','on','ɨ����QUEUINGMODE')
go
insert into pos_para(para,val,notes) values('SCAN_SCANNERMODE','A','ɨ����SCANNERMODE')
go
insert into pos_para(para,val,notes) values('SCAN_PORT','COM2','ɨ����COM��')
go
insert into pos_para(para,val,notes) values('SCAN_BAUDRATE','9600','ɨ���ǲ�����')
go
insert into pos_para(para,val,notes) values('SCAN_BITS','8','ɨ����λ��')
go
insert into pos_para(para,val,notes) values('SCAN_STOPBITS','1','ɨ����ֹͣλ')
go
insert into pos_para(para,val,notes) values('SCAN_PARITY','none','ɨ������żУ����')
go
insert into pos_para(para,val,notes) values('SCAN_PROTOCOL','none','ɨ����PROTOCOL')
go
insert into pos_para(para,val,notes) values('SCAN_SETDTR','notused','ɨ����SETDTR')
go
insert into pos_para(para,val,notes) values('SCAN_SETRTS','notused','ɨ����SETRTS')
go
insert into pos_para(para,val,notes) values('SCAN_DSRCONTROL','notused','ɨ����DSRCONTROL')
go
insert into pos_para(para,val,notes) values('SCAN_READTIMEOUT','3000','ɨ����READTIMEOUT')