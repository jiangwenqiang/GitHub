Use MyshopPOS

delete from  pos_para  where para='CASH_MAXLIMIT'
delete from pos_ini where para='CASH_MAXLIMIT'

insert into pos_para (para,val,notes) values ('CASH_MAXLIMIT','10000','Ǯ������ֽ���ƶ�')
insert into pos_ini (posgroup,para,val) 
  select posgroup,'CASH_MAXLIMIT','10000'
    from posgroup
