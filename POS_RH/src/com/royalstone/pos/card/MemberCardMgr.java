
package com.royalstone.pos.card;

import java.io.IOException;


/**
 * @author yaopoqing
 */
public interface MemberCardMgr {
	
	public  MemberCard query(String cardNo)throws IOException;
	public String updateCardInfo(MemberCardUpdate memberCard)throws IOException;
//	public String updateCardChange(String cardno,String money,String plan,String shopid,String posid,String listno,String flag) throws IOException;
	public CardChange updateCardChange(CardChange cardChange) throws IOException;
}
