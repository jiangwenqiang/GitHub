if exists (select * from sysobjects
        where id = object_id('dbo.generate') and sysstat & 0xf = 4)
   drop procedure dbo.generate
GO
create     proc generate
     @n  int ,
     @sdate   datetime
as 
begin
     declare     @maxvgno int
     declare     @type    char(1) 
     declare     @dt              datetime     
     declare	 @time            datetime     
     declare	 @reqtime         char(10)   
     declare     @listno          int     
     declare	 @sublistno       int     
     declare	 @pos_id          char  (4) 
     declare	 @cashier_id      char  (4)  
     declare	 @waiter_id       char  (4)   
     declare	 @vgno            char  (6)    
     declare	 @goodsno         char  (13) 
     declare	 @placeno         char  (6)    
     declare	 @groupno         char  (2)   
     declare	 @deptno          char  (15)   
     declare	 @amount          int     
     declare	 @colorsize       char  (4)   
     declare	 @item_value      decimal (10 , 2)    
     declare	 @disc_value      decimal (10 , 2)    
     declare	 @item_type       char  (1)   
     declare	 @v_type          char  (1)    
     declare	 @disc_type       char  (1)    
     declare	 @authorizer_id   char  (4)   
     declare	 @x   int   
     declare	 @deliver_flag   char  (1)   
     declare	 @flag1          char  (1)     
     declare	 @flag2         char  (1)     
     declare	 @flag3         char  (1)   
     declare	 @trainflag      char  (1)   
     declare	 @price         decimal (10,  2) 
     declare	 @use_goodsno   char  (13)   
     declare     @maxNo int
     declare     @i int
     declare     @j int
     declare     @rnd dec(10,7)
     declare     @subitem int
     declare     @total dec(10,2);
     declare     @p_type  char(1)
     declare     @queryvgno int;
     declare     @tmpprice dec(7,2);
     declare     @nTotal   int;
     declare     @toChange dec(7,2);
     declare     @maxposid int;
     declare     @k int;
     declare     @pos int;
     declare     @moneycode int;
     declare     @tmp   char(13);
     declare     @c     char(1);
     declare     @sum  int;
     select      @i=0
     select      @j=0
   
      
     select @flag1='0'
     select @flag2='0'
     select @flag3='0'
     select @trainflag='0'
     select @deliver_flag='0'
     create table #_sale
     (
         number int,
         vgno int
     );
     if @n>500 begin
      declare cur_vgno cursor for
          select vgno  from price_lst order by vgno
     open  cur_vgno
     if @@error!=0 goto error; 
        select @k=1
        while 1=1
        begin 
          fetch next from cur_vgno into @vgno
          if @@fetch_status!=0 break
          insert into #_sale values(@k,@vgno)
          select @k=@k+1
        end  
      close cur_vgno;
     deallocate cur_vgno;
     end
    
     error:
     
     select @maxposid=count(*) from pos_lst ;
     if @maxposid is null select @maxposid=1;
     select @maxvgno=max(vgno) from price_lst;
     if @maxvgno is null select @maxvgno=50000
     if @n>500 select @maxvgno=@k
     select @moneycode=convert(int,val)from pos_ini where para='MONEYCODE_UNIT'and posgroup='BASIC'
     if @moneycode is null select @moneycode=100
     while @i<@n  begin
      select @subitem=rand()*10+1
      select @pos=rand()*@maxposid+1;
      if @pos<10 select @pos_id='P00'+char(48+@pos)
      else    select  @pos_id='P0'+convert(char(2),@pos)
     
      select @cashier_id='0001'
      
      select @placeno='A00000'
      select @total=0
      select  @maxNo=max(listno) from sale_j where dt=@sdate and pos_id=@pos_id
      if @maxno is null select @maxno=0
      select @listno=@maxNo+1
      select @j=0
      while @j<@subitem 
      begin
         select @authorizer_id=''     
         select @disc_value=0
         select @dt=@sdate
         select @time=@sdate+convert(char(8),getdate(),108)
         select @reqtime=convert(char(8),getdate(),108)
         select @sublistno=0
         select  @queryvgno=convert(int,rand()*@maxvgno)
         if @n>500 begin
             select @queryvgno=vgno from #_sale where number=@queryvgno
         end
         select @vgno=vgno,@use_goodsno=goodsno,@deptno=deptno,
                @v_type=v_type,@p_type=p_type,@groupno=groupno,@price=price,
                @x=x,@goodsno=goodsno from price_lst where vgno=@queryvgno 
         if @@rowcount=0 continue
         select @amount=1
         
         if @v_type!='2'
         begin
             select @rnd=rand()
             select @amount=10*(0.1*rand()+0.9*rand())+1
             if @rnd<=0.7 select @amount=1
         end
         if @v_type='3' begin --金额码
             select @amount=1;
             select @use_goodsno=substring(@goodsno,1,7)
             select @pos=convert(int,10*rand()+1)*@price*@moneycode;
             select @k=0
             select @tmp='';
             select @tmp=convert(char(13),@pos);

               if len(rtrim(@tmp))=4
                    select @tmp='0'+rtrim(@tmp) 
                else
               if len(rtrim(@tmp))=3
                    select @tmp='00'+rtrim(@tmp)
                else
               if len(rtrim(@tmp))=2
                    select @tmp='000'+rtrim(@tmp) 
                else
               if len(rtrim(@tmp))=1
                    select @tmp='0000'+rtrim(@tmp) 

            
             select @use_goodsno=convert(char(7),@use_goodsno)+convert(char(5),@tmp);
             select @k=1
             select @sum=0
             while  @k<=12 begin
                select @c=substring(@use_goodsno,@k,1);

                if @k %2=0  select @sum=@sum+convert(int,@c)*3;
                else      select @sum=@sum+convert(int,@c);
                select @k=@k+1            
             end
            
            select @c=convert(char(1),10-@sum%10)
             if @c='*' or @c is null select @c='0'

             select @use_goodsno=ltrim(rtrim(@use_goodsno))+ltrim(@c);
             select @item_value=@pos/convert(dec(10,2),@moneycode)
             select @amount=@amount*(@item_value/@price)*@x
           end
         if @v_type='2' begin   --称重码
              select @amount=1;
             select @use_goodsno=substring(@goodsno,1,7)
             select @pos=convert(int,150*rand()+1)*@x;
             select @k=0
             select @tmp='';
             select @tmp=convert(char(13),@pos);
               if len(rtrim(@tmp))=4
                    select @tmp='0'+rtrim(@tmp) 
                  else
               if len(rtrim(@tmp))=3
                    select @tmp='00'+rtrim(@tmp)
                  else
               if len(rtrim(@tmp))=2
                    select @tmp='000'+rtrim(@tmp) 
                  else
               if len(rtrim(@tmp))=1
                    select @tmp='0000'+rtrim(@tmp) 
             select @use_goodsno=convert(char(7),@use_goodsno)+convert(char(5),@tmp);
             select @k=1
             select @sum=0
             while  @k<=12 begin
                select @c=substring(@use_goodsno,@k,1);
                if @k %2=0  select @sum=@sum+convert(int,@c)*3;
                else      select @sum=@sum+convert(int,@c);
                select @k=@k+1            
             end
            select @c=convert(char(1),10-@sum%10)
             if @c='*' or @c is null select @c='0'
             select @use_goodsno=ltrim(rtrim(@use_goodsno))+ltrim(@c);
             select @item_value=@price*@pos/@x;
             select @amount=@pos
         end
          if @v_type!='3' and @v_type!='2'
            select @item_value=@amount*@price

         
         select @type=char(rand()*26+97)
         select @disc_value=0
         select @item_type='a'
         select @disc_type='n'
         --促销
         if @p_type='p' and @v_type!='2' and @v_type!='3' begin
             select @tmpprice=promprice from promotion where vgno=@vgno
             
             if @@rowcount!=0 begin
                select @disc_type='p'
                select @disc_value=(@price-@tmpprice)*@amount;
             end
          end
         --变价
         if @type='c' begin
         select @disc_value=round(@item_value*0.01*(100-90*rand()),1);
         select @disc_type='c'
         select @authorizer_id='0001'
         end   else
         if @type='r' begin  --退货
           select @item_type='r'
           select @item_value=-@item_value
           select @amount=-@amount
         end
         insert into sale_j (
               dt,
               time         ,
               reqtime      ,
               listno       ,
               sublistno    ,
               pos_id       ,
               cashier_id   ,
               waiter_id    ,
               vgno         ,
               goodsno      ,
               placeno      ,
               groupno      ,
               deptno       ,
               amount       ,
               colorsize    ,
               item_value   ,
               disc_value   ,
               item_type    ,
               v_type       ,
               disc_type    ,
               authorizer_id,
               x            ,
               deliver_flag ,
               flag1        ,
               flag2        ,
               flag3        ,
               trainflag    ,
               price        ,
               use_goodsno) values 
              (@dt           ,
               @time         ,
               @reqtime      ,
               @listno       ,
               @sublistno    ,
               @pos_id       ,
               @cashier_id   ,
               @waiter_id    ,
               @vgno         ,
               @goodsno      ,
               @placeno      ,
               @groupno      ,
               @deptno       ,
               @amount       ,
               @colorsize    ,
               @item_value   ,
               @disc_value   ,
               @item_type    ,
               @v_type       ,
               @disc_type    ,
               @authorizer_id,
               @x            ,
               @deliver_flag ,
               @flag1        ,
               @flag2        ,
               @flag3        ,
               @trainflag    ,
               @price        ,
               @use_goodsno)                          
         select @total=@total+@item_value-@disc_value
         select  @j=@j+1             
      end    
        if @total>0 begin
          
         select @nTotal=convert(int,@total)+1;
        end
        else select @nTotal=0
        select @toChange=@total-@nTotal;
        insert into pay_j (  
                             dt ,          
                             time,         
                             reqtime,      
                             listno,       
                             sublistno,    
                             pos_id,       
                             cashier_id,   
                             pay_seq,      
                             pay_reason,   
                             pay_type,     
                             deliver_flag, 
                             curren_code,  
                             pay_value,    
                             equiv_value,  
                             cardno,       
                             flag3,        
                             trainflag )
                    values (
                            @dt ,          
                            @time,         
                            @reqtime,       
                            @listno,        
                            null,     
                            @pos_id,        
                            @cashier_id,    
                            0,       
                            'p',    
                            'C',      
                            '0',          
                            'RMB',           
                            @nTotal,             
                            @nTotal,           
                            '',                
                            '0',              
                             @trainflag 
                             )
        if @toChange!=0 begin
               insert into pay_j (  
                             dt ,          
                             time,         
                             reqtime,      
                             listno,       
                             sublistno,    
                             pos_id,       
                             cashier_id,   
                             pay_seq,      
                             pay_reason,   
                             pay_type,     
                             deliver_flag, 
                             curren_code,  
                             pay_value,    
                             equiv_value,  
                             cardno,       
                             flag3,        
                             trainflag )
                    values (
                            @dt ,          
                            @time,         
                            @reqtime,       
                            @listno,        
                            null,     
                            @pos_id,        
                            @cashier_id,    
                            0,       
                            'r',    
                            'C',      
                            '0',          
                            'RMB',           
                            @toChange,             
                            @toChange,           
                            '',                
                            '0',              
                             @trainflag 
                             )
     end
     select @i=@i+1
     select @maxNo=null
  end                                           
  drop table #_sale
end                                     
                             
                             
                             
                             