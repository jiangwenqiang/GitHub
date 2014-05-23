package com.royalstone.pos.web.command;

import com.royalstone.pos.card.SHCardQueryVO;
import com.royalstone.pos.ejb.ShoppingCard;
import com.royalstone.pos.ejb.ShoppingCardHome;
import com.royalstone.pos.web.util.ServiceLocator;

/**
 * 服务端代码，调用相应的Ejb完成储值卡的查询
 * @deprecated
 * @author liangxinbiao
 */
public class SHCardQueryCommand implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 4
			&& (values[1] instanceof String)
			&& (values[2] instanceof String)
			&& ((values[3] instanceof String) || (values[3]==null))) {
			try {

				String cardNo = (String) values[1];
				String secrety = (String) values[2];
				String host = (String) values[3];

				ShoppingCardHome homeobj =
					(ShoppingCardHome) ServiceLocator.getInstance(
						host).getRemoteHome(
						"pos41/ejb/ShoppingCard",
						ShoppingCardHome.class);
				ShoppingCard shoppingCard = homeobj.create();

				SHCardQueryVO query = shoppingCard.query(cardNo, secrety);

				Object[] results = new Object[1];
				results[0] = query;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

}
