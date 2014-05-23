/*
 * �������� 2004-5-25
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.royalstone.pos.card.test;

import java.net.URL;

import com.royalstone.pos.card.MemberCardMgr;
import com.royalstone.pos.card.MemberCardMgrImpl;
import com.royalstone.pos.card.MemberCardUpdate;

/**
 * ��ֵ����������
 * @deprecated
 * @author liangxinbiao
 */
public class MemberCardTest {
	
	private MemberCardMgr shoppingCard;
	
	public MemberCardTest(){
        try {
            URL servlet=new URL(
					"http://localhost:9090/pos41/DispatchServlet");
            shoppingCard=new MemberCardMgrImpl(servlet);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
    }
	
//	public void testQuery() throws Exception {
//
//		SHCardQueryVO query = shoppingCard.query("8800000003", "12345");
//		System.out.println("ExceptionInfo=" + query.getExceptioninfo());
//		System.out.println("Ifnewcard=" + query.getIfnewcard());
//		System.out.println("Detail=" + query.getDetail());
//
//	}
	
	public void testPay() throws Exception{
		
		MemberCardUpdate pay=new MemberCardUpdate();
		pay.setCardno("8800000003");
		pay.setCashierid("2323");
		pay.setCdseq("3");
		pay.setPayvalue("340");
		pay.setPosid("a");
		pay.setShopid("a4");
		pay.setTime("2004/4/3");
        pay.setPoint("123.05");

		System.out.println("Pay="+shoppingCard.updateCardInfo(pay));

		
	}
	
	public static void main(String[] args) throws Exception{
		MemberCardTest test=new MemberCardTest();
	//	test.testQuery();
		test.testPay();
	}

}
