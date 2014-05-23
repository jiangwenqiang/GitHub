Use MyshopPOS

delete from  pos_para  where para='BANK_CARD_TYPE'
delete from pos_ini where para='BANK_CARD_TYPE'

insert into pos_para (para,val,notes) values ('BANK_CARD_TYPE','OFF','是否输入银行卡类型')
insert into pos_ini (posgroup,para,val) 
  select posgroup,'BANK_CARD_TYPE','OFF'
    from posgroup
