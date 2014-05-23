package com.royalstone.pos.ejb.test;

import com.royalstone.pos.card.SHCardPayVO;
import com.royalstone.pos.card.SHCardQueryVO;
import com.royalstone.pos.ejb.ShoppingCard;
import com.royalstone.pos.ejb.ShoppingCardHome;
import com.royalstone.pos.web.util.ServiceLocator;

/**
 * 储值卡的EJB的测试客户端
 * @deprecated
 * @author liangxinbiao
 */
public class ShoppingCardTest {

	private ShoppingCard shoppingCard;

	public ShoppingCardTest() throws Exception {
		ShoppingCardHome homeobj =
			(ShoppingCardHome) ServiceLocator.getInstance(null).getRemoteHome(
				"pos41/ejb/ShoppingCard",
				ShoppingCardHome.class);
		shoppingCard = homeobj.create();
	}

	public void testQuery() throws Exception {

		SHCardQueryVO query = shoppingCard.query("8800000003", "12345");
		System.out.println("ExceptionInfo=" + query.getExceptioninfo());
		System.out.println("Ifnewcard=" + query.getIfnewcard());
		System.out.println("Detail=" + query.getDetail());

	}
	
	public void testPay() throws Exception{
		
		SHCardPayVO pay=new SHCardPayVO();
		pay.setCardno("8800000003");
		pay.setCashierid("2323");
		pay.setCdseq("3");
		pay.setPassword("12345");
		pay.setPayvalue("340");
		pay.setPosid("a");
		pay.setShopid("a4");
		pay.setTime("2004/4/3");
		
		System.out.println("Pay="+shoppingCard.pay(pay));

		
	}
	
	public void testAutoRever() throws Exception{

		SHCardPayVO pay=new SHCardPayVO();
		pay.setCardno("8800000003");
		pay.setCashierid("1");
		pay.setCdseq("0");
		pay.setPassword("12345");
		pay.setPayvalue("83.60");
		pay.setPosid("1");
		pay.setShopid("1");
		pay.setTime("20040525");
		
		System.out.println("AutoRever="+shoppingCard.autoRever(pay));
		
	}

	public static void main(String[] args) throws Exception {
		ShoppingCardTest test = new ShoppingCardTest();
		//test.testQuery();
		//test.testPay();
		test.testAutoRever();
	}

}
