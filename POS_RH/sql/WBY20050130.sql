Use MyshopPOS

delete from function_lst where keymap_value = 92 	--����һ���յ���Ǯ��Ĺ��ܼ�
insert into function_lst values(92,'OPENCASHBOX','�յ���Ǯ��')

delete from power_lst where power_id =92;	
delete from right_lst where power_id =92;	
insert into right_lst values(92,'�յ���Ǯ��');	--����һ���յ���Ǯ���Ȩ�޶�Ӧ�ļ���
insert into power_lst values(1,92,'-')	--����һ���յ���Ǯ��Ķ�Ӧ��Ȩ��

delete from keybd_cfg  where key_value=34 and keybd_type='NCR'  --����������ΪNCR�ĺϼƹ��ܱ�ΪΪ�յ���Ǯ��Ĺ���
insert into keybd_cfg  values('NCR',34,92)


delete  keybd_cfg where keybd_type='B01' and key_value=86	--����������ΪB01�ĺϼƹ��ܱ�ΪΪ�յ���Ǯ��Ĺ���
insert into keybd_cfg values('B01',86,92)


delete power_lst where levelID=3
insert into power_lst values(3,1,'-')		--���ӻ�������
insert into power_lst values(3,43,'-')		--�ش���һ��
insert into power_lst values(3,67,'-')		--����
insert into power_lst values(3,73,'-')		--��������




















