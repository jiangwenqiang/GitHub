
delete function_lst where keymap_value=67 and fun='FunVoid';  --ɾ����������
update function_lst set hzfun='����' where keymap_value=73 ;  --���������ܸ�Ϊ��������

delete from power_lst where power_id =67;	 --��Ȩ����ɾ����������
delete from right_lst where power_id =67;	 --������Ȩ�޼�����ɾ����������
update right_lst set name='����' where power_id =73; --�ڼ���Ȩ�޸�Ϊ����Ȩ��
