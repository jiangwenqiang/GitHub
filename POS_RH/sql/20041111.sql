if exists (select * from sysobjects where id = object_id('dbo.TEMP_KEYBD_CHG ') and type = 'P')
  drop procedure dbo.TEMP_KEYBD_CHG 
GO

Create Procedure dbo.TEMP_KEYBD_CHG 
	@FunName varchar(50), 	--���ܼ���
	@KeyBdType varchar(10), --��������
	@PLU1 varchar(10),	--ԭ�ֽ��ȼ���PLU1
	@PLU2 varchar(10),	--ԭ�ֽ��ȼ���PLU2
	@NEWPLU varchar(10)	--��Ʒ�ȼ�PLU
AS BEGIN

declare @FunCode int		--���ܼ�ֵ
declare @OrgCashKeyValue int 	--ԭ�ֽ��ȼ��ļ�ֵ
declare @OrgFunKeyValue int 	--ԭ�ֹ��ܵļ�ֵ


SET NOCOUNT ON

select @FunCode=keymap_value from function_lst where fun=@FunName

if  not exists (select key_value from hotkey_cfg where keybd_type=@KeyBdType and flag=2 and (plu=@PLU1 or plu=@PLU2))
	return

Begin Transaction;

--��ԭ�����ܼ�������Ʒ�ȼ�
declare CurFunKeyValue cursor local for 
	select key_value from keybd_cfg where keybd_type=@KeyBdType and keymap_value=@FunCode

Open CurFunKeyValue

WHILE (1=1)
BEGIN
	if @@Error !=0 goto ErrHandle;
	FETCH NEXT FROM CurFunKeyValue INTO @OrgFunKeyValue
	if @@fetch_status !=0  break;
	insert into hotkey_cfg (keybd_type,key_value,plu,flag)
		values (@KeyBdType,@OrgFunKeyValue,@NEWPLU,1)
	if @@Error !=0 goto ErrHandle;	

	delete from keybd_cfg where keybd_type=@KeyBdType and key_value=@OrgFunKeyValue	
END


--��ԭ�ֽ��ȼ�����Ϊ���ܼ�
declare CurCashKeyValue cursor local for 
	Select key_value from hotkey_cfg where keybd_type=@KeyBdType and flag=2 and (plu=@PLU1 or plu=@PLU2)

open CurCashKeyValue;

WHILE (1=1)
BEGIN
	if @@Error != 0 goto ErrHandle;
	FETCH NEXT FROM CurCashKeyValue INTO @OrgCashKeyValue;
	if @@fetch_status !=0 break;
	insert Into keybd_cfg (keybd_type,key_value,keymap_value)
		Values (@KeyBdType,@OrgCashKeyValue,@FunCode)
END

--ɾ��ԭ�ֽ��ȼ�
Delete from hotkey_cfg where keybd_type=@KeyBdType and flag=2 and (plu=@PLU1 or plu=@PLU2)
if @@Error != 0 goto ErrHandle;


Commit Transaction;

Return 0;

ErrHandle:
	Rollback Transaction;
	return 1;
END;
GO


exec TEMP_KEYBD_CHG 'FunDecrCash','B01','10000','100','000004'
exec TEMP_KEYBD_CHG 'FunIncrCash','B01','5000',  '50','000006'
exec TEMP_KEYBD_CHG 'FunDecrCash','NCR','10000','100','000004'
exec TEMP_KEYBD_CHG 'FunIncrCash','NCR','5000' , '50','000006'

go

drop procedure dbo.TEMP_KEYBD_CHG 
GO













