Use MyshopPOS

delete from  pos_para  where para='AUTO_LOCK_TIME'
delete from pos_ini where para='AUTO_LOCK_TIME'

insert into pos_para (para,val,notes) values ('AUTO_LOCK_TIME','0','设定自动锁机等待时间(毫秒)')
insert into pos_ini (posgroup,para,val) 
  select posgroup,'AUTO_LOCK_TIME','0'
    from posgroup
