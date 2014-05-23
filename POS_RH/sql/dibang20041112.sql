Use MyshopPOS

delete from function_lst where keymap_value = 122
insert into function_lst values(122,'ALTVALUE','地磅修改')

delete from power_lst where power_id =122;
delete from right_lst where power_id =122;
insert into right_lst values(122,'地磅修改');
