----------------------------------------------------------------------
--�����м��̵�"��ѯ�۸�"���ܼ���Ϊ93#�ͣ���Ʒ����Ϊ000003������Ʒ�ȼ�
----------------------------------------------------------------------
use myshoppos
go

insert into hotkey_cfg (keybd_type,key_value,plu,flag) 
select keybd_type,key_value,'000003',1 from keybd_cfg where keymap_value=91

delete from keybd_cfg where keymap_value=91

go