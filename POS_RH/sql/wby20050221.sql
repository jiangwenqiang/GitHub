
delete function_lst where keymap_value=67 and fun='FunVoid';  --删除更正功能
update function_lst set hzfun='更正' where keymap_value=73 ;  --将即更功能改为更正功能

delete from power_lst where power_id =67;	 --在权限中删除更正功能
delete from right_lst where power_id =67;	 --在所有权限级别中删除更正功能
update right_lst set name='更正' where power_id =73; --在即更权限改为更正权限
