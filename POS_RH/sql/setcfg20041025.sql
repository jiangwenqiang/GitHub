Use MyshopPOS

delete from  pos_para  where para='REMARK'
delete from pos_ini where para='REMARK'

insert into pos_para (para,val,notes) values ('REMARK','remark here','为打印销售清单设置的remark值')
insert into pos_ini (posgroup,para,val) 
  select posgroup,'REMARK','remark here'
    from posgroup
    

delete from  pos_para  where para='ITEMS'
delete from pos_ini where para='ITEMS'

insert into pos_para (para,val,notes) values ('ITEMS','5','为打印销售清单设置的每页打印行数')
insert into pos_ini (posgroup,para,val) 
  select posgroup,'ITEMS','5'
    from posgroup
    

delete from  pos_para  where para='HEADLINES'
delete from pos_ini where para='HEADLINES'

insert into pos_para (para,val,notes) values ('HEADLINES','5','为打印销售清单设置的清单头走纸行数')
insert into pos_ini (posgroup,para,val) 
  select posgroup,'HEADLINES','5'
    from posgroup
  
    
delete from  pos_para  where para='TAILLINES'
delete from pos_ini where para='TAILLINES'

insert into pos_para (para,val,notes) values ('TAILLINES','5','为打印销售清单设置的清单尾走纸行数')
insert into pos_ini (posgroup,para,val) 
  select posgroup,'TAILLINES','5'
    from posgroup
    

delete from  pos_para  where para='PRINTTYPE'
delete from pos_ini where para='PRINTTYPE'

insert into pos_para (para,val,notes) values ('PRINTTYPE','0','打印机类型,0为小票打印,其他为清单打印')
insert into pos_ini (posgroup,para,val) 
  select posgroup,'PRINTTYPE','5'
    from posgroup