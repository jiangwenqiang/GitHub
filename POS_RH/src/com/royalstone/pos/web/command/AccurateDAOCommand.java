package com.royalstone.pos.web.command;


/**
 * Created by IntelliJ IDEA.
 * User: fire
 * Date: 2005-5-27
 */
public class AccurateDAOCommand implements ICommand {

	/**
	 * @see com.royalstone.pos.web.command.ICommand#excute(java.lang.Object[])
	 */
	public Object[] excute(Object[] values) {
		if (values.length == 3
			&& (values[1] instanceof String)) {
			Object[] results = new Object[1];
			//results[0] = //query((String) values[1],(String)values[2]);
			return results;
		}

		return null;
	}

//    private Accurate query(String cardLevel,String deptID ) {
//
//        Connection conn = null;
//        Statement state = null;
//        ResultSet rs = null;
//        conn = DBConnection.getConnection("java:comp/env/dbpos");
//
//        Accurate accurateQuery = new Accurate();
//        String precentage = null;
//        if (cardLevel == null
//            || cardLevel.trim().equals("")
//            ||deptID==null||deptID.trim().equals("")) {
//          accurateQuery.setExeptionInfo("��ѯ��������Ϊ��");
//          return accurateQuery;
//        }
//        try {
//            conn.setAutoCommit(true);
//            state = conn.createStatement();
//
//                rs =
//                    state.executeQuery(
//                        "select accurate from accurate where cardlevelid = '"
//                            + cardLevel.trim() + "' and deptid='"+deptID.trim()+"'");
//                //rs.next();
//
//                if (!rs.next()) {
//                    accurateQuery.setExeptionInfo("����Ʒ������Ϣ������,�밴���������");
//                    DBConnection.closeAll(rs, null, null);
//                    return accurateQuery;
//                } else { //����
//                  precentage = (rs.getString("accurate")).trim();
//                    if(precentage!=null&&!precentage.equals(""))
//                      accurateQuery.setAccurate(precentage);
//                }
//
//        } catch (SQLException se) {
//            se.printStackTrace();
//            accurateQuery.setExeptionInfo("���ݿ��������,�밴���������");
//            return accurateQuery;
//        } finally {
//            DBConnection.closeAll(rs, state, conn);
//        }
//        return accurateQuery;
//    }
//

}
