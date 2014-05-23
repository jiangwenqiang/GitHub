Use MyshopPOS


delete from power_lst where power_id =93;	
delete from right_lst where power_id =93;	
insert into right_lst values(93,'脱机班结');	--增加一个脱机班结的权限
insert into power_lst values(1,93,'-');	        --给权限级别是 1 的级别增加一个脱机班结的权限
