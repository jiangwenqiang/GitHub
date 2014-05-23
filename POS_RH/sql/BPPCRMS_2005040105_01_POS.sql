use myshopshcard
go

drop table CreditClientDisc
go
Create table CreditClientDisc
(
   CardNo               char(19)             not null,/* 卡号 */
   ItemType             int                  not null,/*编码类型 1=大类 2=中类 3=小类 4=商品*/ 
   ItemID               int                  not null,/*编号 类别编号或者商品编码*/
   DiscType             int        default 0 not null,/*0=优惠额度 1=百分比*/ 
   DiscCount dec(12,2)  default 0 not null,/*挂帐客户油品优惠额度 百分比或者优惠金额*/
   Note                 char(64)                 null,/*说明*/
   primary key  (CardNo,ItemType,ItemID)
);
