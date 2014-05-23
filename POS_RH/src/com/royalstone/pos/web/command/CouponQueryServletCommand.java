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
 * 服务端代码，调用相应的Ejb完成储值卡的查询
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
     * 根据卡号和密码(子卡不需密码)查询挂账卡的信息
     * @param cardNo 挂账卡号
     * @return 挂账卡查询值对象
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
            cardquery.setExceptionInfo("券号或密码为空");
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
                cardquery.setExceptionInfo("此券已过期");
                return cardquery;
            } 
            
            String sqlRsTimeA= "select typeID,GoodFlag,CpnValue from CouponType "
                + "where TypeID = '"+cardNo.trim().substring(0,4)
                + "'";
            rsTime=state.executeQuery(sqlRsTimeA);
            if(!rsTime.next()){
            	rsTime.close();
            	cardquery.setExceptionInfo("此券类不存在");
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
                                cardquery.setExceptionInfo("此券未到帐,不能兑换");
                                break;
                            case '3':
                                cardquery.setExceptionInfo("已分配券,不能兑换");
                                break;
                            case 'f':
                                cardquery.setExceptionInfo("已兑换券,不能重复兑换");
                                break;
                            case 'd':
                                cardquery.setExceptionInfo("已作废券,不能兑换");
                                break;
                            case '4':
                                cardquery.setExceptionInfo("未出售券,不能兑换");
                                break;
                            default :
                                cardquery.setExceptionInfo("其他错误,不能兑换");
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
                    cardquery.setExceptionInfo("此券密码有误，可能是假券");
                    DBConnection.closeAll(rs, state, conn);
                    return cardquery;
                }
            } else {
               cardquery.setExceptionInfo("此券不存在，可能是假券");
                DBConnection.closeAll(rs, null, null);
                return cardquery;
            }

        } catch (SQLException se) {
            se.printStackTrace();
            cardquery.setExceptionInfo("数据库操作有误");

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
