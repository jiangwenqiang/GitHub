----------------------------------------------------------------------
--将所有键盘的"查询价格"功能键改为93#油（商品编码为000003）的商品热键
----------------------------------------------------------------------
use myshoppos
go

insert into hotkey_cfg (keybd_type,key_value,plu,flag) 
select keybd_type,key_value,'000003',1 from keybd_cfg where keymap_value=91

delete from keybd_cfg where keymap_value=91

go