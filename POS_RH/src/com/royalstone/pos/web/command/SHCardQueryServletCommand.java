package com.royalstone.pos.web.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.royalstone.pos.card.SHCardQueryVO;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ����˴��룬������Ӧ��Ejb��ɴ�ֵ���Ĳ�ѯ
 * @deprecated
 * @author liangxinbiao
 */
public class SHCardQueryServletCommand implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 3
			&& (values[1] instanceof String)
			&& (values[2] instanceof String)) {
			try {

				String cardNo = (String) values[1];
				String secrety = (String) values[2];
				Object[] results = new Object[1];
				results[0] = query(cardNo,secrety);

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

    /**
     * ���ݿ��ź�����(�ӿ���������)��ѯ���˿�����Ϣ
     * @param cardNo ���˿���
     * @return ���˿���ѯֵ����
     * GuestExt �������˻���
     */
    private SHCardQueryVO query(String cardNo,String secrety) {

        Connection conn = null;
        Statement state = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
        String today=sdf.format(new Date());//��������
        conn = DBConnection.getConnection("java:comp/env/dbcard");

        SHCardQueryVO cardquery = new SHCardQueryVO();
        String mode = null;
        if (cardNo == null
            || cardNo.trim().equals("")||secrety==null||secrety.equals("")) {
            cardquery.setExceptioninfo("���Ż�����Ϊ��");
            return cardquery;
        }
        try {
          conn.setAutoCommit(true);
			state = conn.createStatement();
//			rs =
//				state.executeQuery(
//					"select count(*) as counts from guest where cardno = '"
//						+ cardNo.trim()
//						+ "'");
//			rs.next();
//			if (rs.getInt("counts") == 0) {
//				cardquery.setExceptioninfo("�޴˿���");
//				DBConnection.closeAll(rs, null, null);
//				return cardquery;
//			}
//			rs =
//				state.executeQuery(
//					"select detail,mode,secrety,ifnewcard,memberid from guest "
//						+ "where cardno = '"
//						+ cardNo.trim()
//						+ "' and secrety = '"
//						+ secrety.trim()
//						+ "'");
            rs =
				state.executeQuery(
					"select detail,mode,secrety,ifnewcard,memberid from guest "
						+ "where cardno = '"
						+ cardNo.trim()
						+ "'");
		if (rs.next()) {

              if(isPass(cardNo.trim(),rs.getString("secrety"),rs.getInt("ifnewcard"),secrety.trim())){
				mode = (rs.getString("mode")).trim();
				if (!mode.equals("1")) {
					switch (mode.charAt(0)) {
						case '2' :
							cardquery.setExceptioninfo("δ���ʿ�");
							break;
						case 'r' :
							cardquery.setExceptioninfo("�ѻ��տ�");
							break;
						case 'm' :
							cardquery.setExceptioninfo("һ���ʧ��");
							break;
						case 'l' :
							cardquery.setExceptioninfo("���ع�ʧ��");
							break;
						case 'f' :
							cardquery.setExceptioninfo("����");
							break;
						case 'e' :
							cardquery.setExceptioninfo("�ѻ���");
							break;
						case 'q' :
							cardquery.setExceptioninfo("�˿�");
							break;
						default :
							cardquery.setExceptioninfo("��������");
							break;
					}
					DBConnection.closeAll(rs, state, conn);
					return cardquery;
				}
				cardquery.setDetail(rs.getString("detail"));
				cardquery.setIfnewcard(rs.getString("ifnewcard"));
				cardquery.setMemberid(rs.getString("memberid"));
				
				//����
				DBConnection.closeAll(rs, state, null);
				state = conn.createStatement();
				//��ѯ�ٻ���
	            rs =
					state.executeQuery(
						"select Detail from GuestExt "
							+ "where AccFlag=0 and cardno = '"
							+ cardNo.trim()
							+ "'and  EndDate >= '"
							+ today
							+ "'");

	            if (rs.next()) {
	            	cardquery.setRHBDetail(rs.getString("Detail"));
	            	cardquery.setAccflag("1");
	            	}

				DBConnection.closeAll(rs, state, conn);
			} else {
				cardquery.setExceptioninfo("��������");
				DBConnection.closeAll(rs, state, conn);
				return cardquery;
			}
        }else{
        	
         	cardquery.setExceptioninfo("�޴˿���");
		    DBConnection.closeAll(rs, null, null);
		    return cardquery;
        }

		} catch (SQLException se) {
            se.printStackTrace();
			cardquery = new SHCardQueryVO();
	//		cardquery.setExceptioninfo("���ݿ��������");
			cardquery.setExceptioninfo(se.getMessage());

			return cardquery;
		} finally {
			DBConnection.closeAll(rs, state, conn);
		}
		return cardquery;
    }
    private boolean isPass(String cardNO,String dbPass,int isNew,String password){
       if(isNew!=1){
         if(password.equals("0"))
            password="";
          return password.equals(dbPass.trim());
       }
       long k;
       int a;
       String s;
       k=123456789;
        for(int i=0;i<cardNO.length();i++){
            a=(int)cardNO.charAt(i)%13+1;
            k=(k * a)% 9999999 + 1;
        }
        k=k%98989898+99;

        for(int i=0;i<dbPass.length();i++){
           a=(int)dbPass.charAt(i)%17+1;
           k=(k%9876543+1)*a;
        }
       s=Long.toString(k+100000000);
       s=s.substring(s.length()-8,(s.length()-8)+6);
       s= s + verify(s);

        return s.equals(password);
    }
   private char verify(String s){
       int odd=0,env=0;
       int I;
        I=0;
       for(;I<s.length();){
        odd = odd +(int)s.charAt(I) - (int)'0';
        I = I + 2;
       }
        I=1;
       for(;I<s.length();){
           env = env + (int)s.charAt(I) -(int) '0';
        I = I + 2;
       }
       I = (env + odd * 3)%10;
       return (char)(I + (int)'0');
   }


}
