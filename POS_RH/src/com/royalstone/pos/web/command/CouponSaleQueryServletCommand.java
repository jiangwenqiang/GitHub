package com.royalstone.pos.web.command;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.royalstone.pos.coupon.CouponSale;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ����˴��룬������Ӧ��Ejb��ɴ�ֵ���Ĳ�ѯ
 * @deprecated
 * @author liangxinbiao
 */
public class CouponSaleQueryServletCommand implements ICommand {

    public Object[] excute(Object[] values) {

        if (values.length == 4
                && (values[1] instanceof String)
                && (values[2] instanceof String)
				&& (values[3] instanceof String)) {
            try {

                String cardNo = (String) values[1];
                String secrety = (String) values[2];
                String storeid = (String) values[3];
                Object[] results = new Object[1];
                results[0] = query(cardNo, secrety,storeid);

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
     */
    private CouponSale query(String cardNo, String secrety,String storeid) {

        Connection conn = null;
        Statement state = null;
        ResultSet rsDiscount = null;
        Statement stateDiscount=null;
        ResultSet rs = null;
        ResultSet rsTime=null;
        conn = DBConnection.getConnection("java:comp/env/dbcard");

        CouponSale cardquery = new CouponSale();
        String mode = null;
        if (cardNo == null
                || cardNo.trim().equals("") || secrety == null || secrety.equals("")) {
            cardquery.setExceptionInfo("ȯ�Ż�����Ϊ��");
            return cardquery;
        }
        try {
            conn.setAutoCommit(true);
            state = conn.createStatement();
            stateDiscount =conn.createStatement();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
            String today=sdf.format(new Date());
                   
            String sqlRsTime= "select ValidValue from CouponType "
                    + "where TypeID = '"+cardNo.trim().substring(0,4)
                    + "' and convert(char(8),getdate(),112) between convert(char(8),Begindate,112) and convert(char(8),enddate,112)";
            rsTime=state.executeQuery(sqlRsTime);
            if(!rsTime.next()){
                 cardquery.setExceptionInfo("��ȯδ���ڻ���ڣ���������");
                 DBConnection.closeAll(rsTime, state, conn);
                 return cardquery;
            }
            
            cardquery.setValidValue(rsTime.getInt("ValidValue"));
            
            String sql= "select CouponID,vrowid,MInFlag,CPwd,Mode,CpnValue from Coupon "
                    + "where Coupon.CouponID = '"
                    + cardNo.trim()+ "' and Coupon.SalesID = '"
					+ storeid.trim()+"'";
            String sqlDiscount="select CpnSaleDisc.PromValue from CpnSaleDisc "
                    + "where  CpnSaleDisc.TypeID='"+cardNo.trim().substring(0,4)+"'" +
                    " and Begindate<='"+today+"' and enddate>='"+today+"' ";

            rsDiscount=stateDiscount.executeQuery(sqlDiscount);
            rs = state.executeQuery(sql);
            if(rsDiscount.next())
               cardquery.setDiscountValue(rsDiscount.getBigDecimal("PromValue"));

            if (rs.next()) {

                if (isPass(cardNo.trim(), rs.getString("vrowid").trim(), rs.getString("CPwd"), secrety.trim())) {
                    mode = (rs.getString("mode")).trim();
                    if (!mode.equals("4")) {
                        switch (mode.charAt(0)) {
                            case '1':
                                cardquery.setExceptionInfo("ȯ�ѳ���,�����ظ�����");
                                break;
                            case '3':
                                cardquery.setExceptionInfo("��ȯ������POS����");
                                break;
                            case '2':
                                cardquery.setExceptionInfo("δ����ȯ,��������");
                                break;
                            case 'f':
                                cardquery.setExceptionInfo("��ȯ�Ѷһ�,�����ٴ�����");
                                break;
                            case 'd':
                                cardquery.setExceptionInfo("��ȯ������,��������");
                                break;
                            default :
                                cardquery.setExceptionInfo("��������,��������");
                                break;
                        }
                        DBConnection.closeAll(rs, state, conn);
                        return cardquery;
                    }
                  cardquery.setCouponID(cardNo.trim());
                  cardquery.setMode(rs.getString("Mode"));
                  cardquery.setMinFlag(rs.getString("MInFlag"));
                  cardquery.setPrice(rs.getBigDecimal("CpnValue"));
                 // cardquery.setDiscountValue(rs.getBigDecimal("CpnValue").subtract(rs.getBigDecimal("DiscountValue")));
                  
                  return cardquery;
                  
               } else {
                   cardquery.setExceptionInfo("��ȯ�������󣬿����Ǽ�ȯ");
                  DBConnection.closeAll(rs, state, conn);
                   return cardquery;
           }
            } else {
               cardquery.setExceptionInfo("��ȯ������,�����������ŵ�δ����ȯ");
                DBConnection.closeAll(rs, null, null);
                return cardquery;
            }

        } catch (SQLException se) {
            se.printStackTrace();
            cardquery.setExceptionInfo("���ݿ��������");

            return cardquery;
        } finally {
            if(rsDiscount!=null)
                try {
                    rsDiscount.close();
                } catch (SQLException e) {}
            if(stateDiscount!=null)
                try {
                    stateDiscount.close();
                } catch (SQLException e) {}
            DBConnection.closeAll(rs, state, conn);
        }
    }

    private boolean isPass(String cardNO, String vrowID, String dbPass, String password) {

        String id = vrowID + cardNO;
        String s = "";
        long k = 123456789;

        for (int i = 0; i < id.length(); i++) {
            long a = ((int) id.charAt(i)) % 13 + 1;
            k = (k * a) % 9999999 + 1;
        }

        k = k % 98989898 + 99;
        for (int i = 0; i < password.length(); i++) {
            long a = ((int) password.charAt(i)) % 17 + 1;
            k = (k % 9876543 + 1) * a;
        }

        DecimalFormat df = new DecimalFormat("00000000");
        s = df.format(k % 100000000);

        return s.equals(dbPass.trim());
    }

}
