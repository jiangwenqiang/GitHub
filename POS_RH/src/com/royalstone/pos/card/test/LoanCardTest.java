package com.royalstone.pos.card.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import com.royalstone.pos.card.ILoanCard;
import com.royalstone.pos.card.LoanCardDirect;
import com.royalstone.pos.card.LoanCardFactory;
import com.royalstone.pos.card.LoanCardPayVO;
import com.royalstone.pos.card.LoanCardQueryVO;
import com.royalstone.pos.web.util.ConnectionFactoryDirect;
import com.royalstone.pos.web.util.DBConnection;

/**
 * ���˿��Ĳ�������
 * @author liangxinbiao
 */
public class LoanCardTest extends TestCase {

	private ILoanCard loanCard = null;

	public LoanCardTest(String arg0) {
		super(arg0);
	}

	/* ���� Javadoc��
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		DBConnection.setConnectionFactory(new ConnectionFactoryDirect());
		LoanCardFactory.setInstance(new LoanCardDirect());
		loanCard = LoanCardFactory.createInstance();
	}

	public void atestQuerySubCard() {

		LoanCardQueryVO query = loanCard.query("280001000000030002", "000000");
		assertNotNull(query);
		System.out.println(query.getExceptioninfo());
		assertNull(query.getExceptioninfo());
		assertEquals("280001000000030000", query.getCardNo());
		assertEquals("280001000000030002", query.getSubcardNo());
		//????Ӧ��ȡguest�е�����Detail��Credit,���ӿ�����creditSubCard�е�balance,credit?????
		assertEquals("47000", query.getDetail());
		//????��ô���㣬�͵�GoodsIDû��????
		assertEquals("300.0000", query.getMaxOilQtyPerTrans());
		assertEquals("441", query.getOilGoodsID());
		assertNotNull(query.getShopIDs());
		assertEquals(1, query.getShopIDs().size());
		assertNotNull(query.getDeptIDs());
		assertEquals(1, query.getDeptIDs().size());
		assertEquals("�й����⽨�����Ź��ݽ�����˾",query.getCustName());
		assertEquals("��Azhongi",query.getCarID());

	}

	public void atestQueryMainCard() {

		LoanCardQueryVO query = loanCard.query("280001000000030000", "0000000");
		assertNotNull(query);
		System.out.println(query.getExceptioninfo());
		assertNull(query.getExceptioninfo());
		assertEquals("280001000000030000", query.getCardNo());
		assertEquals("", query.getSubcardNo());
		assertEquals("1495000", query.getDetail());
		assertNull(query.getMaxOilQtyPerTrans());
		assertNull(query.getOilGoodsID());
		assertNotNull(query.getShopIDs());
		assertEquals(1, query.getShopIDs().size());
		assertNull(query.getDeptIDs());
		assertEquals("�й����⽨�����Ź��ݽ�����˾",query.getCustName());
		assertEquals("1000000",query.getCredit());
		assertNull(query.getCarID());
	}

	public void atestPayMainCardInLocalShop() {
		LoanCardPayVO cardpay = new LoanCardPayVO();
		cardpay.setCardno("280001000000030000");
		cardpay.setPassword("0000000");
		cardpay.setPayvalue("1000");
		cardpay.setShopid("C001");
		cardpay.setCashierid("0002");
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
		cardpay.setTime(sdf.format(new Date()));
		cardpay.setCdseq("0");
		
		String result=loanCard.pay(cardpay);
		assertNotNull(result);
		assertEquals("1",result);
	}
	
	public void atestAutoRecvMainCardInLocalShop() {
		LoanCardPayVO cardpay = new LoanCardPayVO();
		cardpay.setCardno("280001000000030000");
		cardpay.setPassword("0000000");
		cardpay.setPayvalue("1000");
		cardpay.setShopid("C001");
		cardpay.setCashierid("0002");
		cardpay.setTime("014920343");
		cardpay.setCdseq("0");
		
		String result=loanCard.autoRever(cardpay);
		assertNotNull(result);
		assertEquals("1",result);
	}


	public void atestPayMainCardNotInLocalShop() {
		LoanCardPayVO cardpay = new LoanCardPayVO();
		cardpay.setCardno("280001000000030000");
		cardpay.setPassword("0000000");
		cardpay.setPayvalue("1000");
		cardpay.setShopid("C002");
		cardpay.setCashierid("0002");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		cardpay.setTime(sdf.format(new Date()));
		cardpay.setCdseq("0");
		
		String result=loanCard.pay(cardpay);
		assertNotNull(result);
		assertEquals("1",result);
	}
	
	
	public void testPaySunCardInLocalShop() {
		LoanCardPayVO cardpay = new LoanCardPayVO();
		cardpay.setCardno("280001000000060000");
		cardpay.setSubcardno("280001000000060001");
		cardpay.setPassword("000000");
		cardpay.setPayvalue("1000");
		cardpay.setShopid("C001");
		cardpay.setCashierid("0002");
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
		cardpay.setTime(sdf.format(new Date()));
		cardpay.setCdseq("0");
		
		String result=loanCard.pay(cardpay);
		assertNotNull(result);
		assertEquals("1",result);
	}
	
	public void atestAutoRecvInLocalShop(){
		LoanCardPayVO cardpay = new LoanCardPayVO();
		cardpay.setCardno("280001000000060000");
		cardpay.setSubcardno("280001000000060001");
		cardpay.setPassword("000000");
		cardpay.setPayvalue("1000");
		cardpay.setShopid("C001");
		cardpay.setCashierid("0002");
		cardpay.setTime("011545796");
		cardpay.setCdseq("0");
		
		String result=loanCard.autoRever(cardpay);
		assertNotNull(result);
		assertEquals("1",result);
	}

	public void atestPaySunCardNotInLocalShop() {
		LoanCardPayVO cardpay = new LoanCardPayVO();
		cardpay.setCardno("280001000000030000");
		cardpay.setSubcardno("280001000000030002");
		cardpay.setPassword("000000");
		cardpay.setPayvalue("1000");
		cardpay.setShopid("C002");
		cardpay.setCashierid("0002");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		cardpay.setTime(sdf.format(new Date()));
		cardpay.setCdseq("0");
		
		String result=loanCard.pay(cardpay);
		assertNotNull(result);
		assertEquals("1",result);
	}


}
