Use MyshopPOS

delete from function_lst where keymap_value = 92 	--增加一个空单开钱箱的功能键
insert into function_lst values(92,'OPENCASHBOX','空单开钱箱')

delete from power_lst where power_id =92;	
delete from right_lst where power_id =92;	
insert into right_lst values(92,'空单开钱箱');	--增加一个空单开钱箱的权限对应的级别
insert into power_lst values(1,92,'-')	--增加一个空单开钱箱的对应的权限

delete from keybd_cfg  where key_value=34 and keybd_type='NCR'  --将键盘类型为NCR的合计功能必为为空单开钱箱的功能
insert into keybd_cfg  values('NCR',34,92)


delete  keybd_cfg where keybd_type='B01' and key_value=86	--将键盘类型为B01的合计功能必为为空单开钱箱的功能
insert into keybd_cfg values('B01',86,92)


delete power_lst where levelID=3
insert into power_lst values(3,1,'-')		--增加基本功能
insert into power_lst values(3,43,'-')		--重打上一单
insert into power_lst values(3,67,'-')		--更正
insert into power_lst values(3,73,'-')		--即更操作




















