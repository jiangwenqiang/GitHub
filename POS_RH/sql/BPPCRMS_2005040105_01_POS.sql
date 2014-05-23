use myshopshcard
go

drop table CreditClientDisc
go
Create table CreditClientDisc
(
   CardNo               char(19)             not null,/* ���� */
   ItemType             int                  not null,/*�������� 1=���� 2=���� 3=С�� 4=��Ʒ*/ 
   ItemID               int                  not null,/*��� ����Ż�����Ʒ����*/
   DiscType             int        default 0 not null,/*0=�Żݶ�� 1=�ٷֱ�*/ 
   DiscCount dec(12,2)  default 0 not null,/*���ʿͻ���Ʒ�Żݶ�� �ٷֱȻ����Żݽ��*/
   Note                 char(64)                 null,/*˵��*/
   primary key  (CardNo,ItemType,ItemID)
);
