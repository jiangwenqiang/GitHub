package com.royalstone.pos.web.command;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.royalstone.pos.common.Goods;
import com.royalstone.pos.coupon.CouponEnCash;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ����˴��룬������Ӧ��Ejb��ɴ�ֵ���Ĳ�ѯ
 * @deprecated
 * @author liangxinbiao
 */
public class  CouponQueryServletCommand implements ICommand {

    public Object[] excute(Object[] values) {

        if (values.length == 3
                && (values[1] instanceof String)
                && (values[2] instanceof String)) {
            try {

                String cardNo = (String) values[1];
                String secrety = (String) values[2];
                Object[] results = new Object[1];
                results[0] = query(cardNo, secrety);

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
    private CouponEnCash query(String cardNo, String secrety) {

        Connection conn = null;
        Statement state = null;
        ResultSet rs = null;
        ResultSet rsGoods=null;
        ResultSet rsTime=null;
        conn = DBConnection.getConnection("java:comp/env/dbcard");

        CouponEnCash cardquery = new CouponEnCash();
        String mode = null;
        if (cardNo == null
                || cardNo.trim().equals("") || secrety == null || secrety.equals("")) {
            cardquery.setExceptionInfo("ȯ�Ż�����Ϊ��");
            return cardquery;
        }
        try {
           // conn.setAutoCommit(true);
            state = conn.createStatement();
            String sqlRsTime= "select CouponID  from Coupon "
                    + "where CouponID = '"+cardNo.trim()
                    + "' and convert(char(8),getdate(),112) between convert(char(8),getdate(),112) and convert(char(8),EndDate,112)";
            rsTime=state.executeQuery(sqlRsTime);
            if(!rsTime.next()){
            	rsTime.close();
                cardquery.setExceptionInfo("��ȯ�ѹ���");
                return cardquery;
            } 
            
            String sqlRsTimeA= "select typeID,GoodFlag,CpnValue from CouponType "
                + "where TypeID = '"+cardNo.trim().substring(0,4)
                + "'";
            rsTime=state.executeQuery(sqlRsTimeA);
            if(!rsTime.next()){
            	rsTime.close();
            	cardquery.setExceptionInfo("��ȯ�಻����");
            	return cardquery;
            } 
           BigDecimal couponPrice= rsTime.getBigDecimal("CpnValue");
           String couponType= rsTime.getString("GoodFlag");
           rsTime.close();
           cardquery.setPrice(couponPrice);
           cardquery.setType(couponType);
            rs = state.executeQuery(
                    "select CouponID,vrowid,CPwd,CpnValue,Mode,MInFlag from Coupon "
                    + "where CouponID = '"
                    + cardNo.trim()
                    + "'");
            if (rs.next()) {

              if (isPass(cardNo.trim(), rs.getString("vrowid").trim(), rs.getString("CPwd"), secrety.trim())) {
                    mode = (rs.getString("mode")).trim();
                    if (!mode.equals("1")) {
                        switch (mode.charAt(0)) {
                            case '2':
                                cardquery.setExceptionInfo("��ȯδ����,���ܶһ�");
                                break;
                            case '3':
                                cardquery.setExceptionInfo("�ѷ���ȯ,���ܶһ�");
                                break;
                            case 'f':
                                cardquery.setExceptionInfo("�Ѷһ�ȯ,�����ظ��һ�");
                                break;
                            case 'd':
                                cardquery.setExceptionInfo("������ȯ,���ܶһ�");
                                break;
                            case '4':
                                cardquery.setExceptionInfo("δ����ȯ,���ܶһ�");
                                break;
                            default :
                                cardquery.setExceptionInfo("��������,���ܶһ�");
                                break;
                        }
                        DBConnection.closeAll(rs, state, conn);
                        return cardquery;
                    }
                  cardquery.setCouponID(cardNo.trim());
                  cardquery.setCouponPass(secrety);
                  cardquery.setMode(rs.getString("Mode"));
                  cardquery.setMinFlag(rs.getString("MInFlag"));

                  DecimalFormat df = new DecimalFormat("000000");

                        state = conn.createStatement();
                        String sql = null;
                        sql =
                                "select CouponGoodsItem0.GoodsID,CouponGoodsItem0.Qty FROM  CouponGoodsItem0" +
                                " where  CouponGoodsItem0.TypeID='" + cardNo.trim().substring(0, 4) + "'";
                        rsGoods=state.executeQuery(sql);
                       List goodsList=new ArrayList();
                       List goodsQty=new ArrayList();
                       while (rsGoods.next()) {
                            int vgno = rsGoods.getInt(1);
                         Goods result =
                                    new Goods(
                                            df.format(vgno),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            0,
                                            0,
                                            1,
                                            "");

                           goodsList.add(result);
                           goodsQty.add(rsGoods.getBigDecimal(2));

                        }

                        cardquery.setGoodsList(goodsList);
                        cardquery.setGoodsQty(goodsQty);
                        return cardquery;

                } else {
                    cardquery.setExceptionInfo("��ȯ�������󣬿����Ǽ�ȯ");
                    DBConnection.closeAll(rs, state, conn);
                    return cardquery;
                }
            } else {
               cardquery.setExceptionInfo("��ȯ�����ڣ������Ǽ�ȯ");
                DBConnection.closeAll(rs, null, null);
                return cardquery;
            }

        } catch (SQLException se) {
            se.printStackTrace();
            cardquery.setExceptionInfo("���ݿ��������");

            return cardquery;
        } finally {
            if(rsTime!=null)
                try {
                    rsTime.close();
                } catch (SQLException e) {}
            if(rsGoods!=null)
                try {
                    rsGoods.close();
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
