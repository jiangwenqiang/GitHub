Use MyshopPOS

delete from  pos_para  where para='PRICE_FLAG'
delete from pos_ini where para='PRICE_FLAG'

insert into pos_para (para,val,notes) values ('PRICE_FLAG','ON','基本码是否可以输单价')
insert into pos_ini (posgroup,para,val) 
  select posgroup,'PRICE_FLAG','ON'
    from posgroup