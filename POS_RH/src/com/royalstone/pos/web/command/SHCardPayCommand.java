package com.royalstone.pos.web.command;

import com.royalstone.pos.card.SHCardPayVO;
import com.royalstone.pos.ejb.ShoppingCard;
import com.royalstone.pos.ejb.ShoppingCardHome;
import com.royalstone.pos.web.util.ServiceLocator;

/**
 * 服务端代码，调用相应的Ejb完成储值卡的支付
 * @deprecated
 * @author liangxinbiao
 */
public class SHCardPayCommand implements ICommand {

	public Object[] excute(Object[] values) {

		if (values.length == 3
			&& (values[1] instanceof SHCardPayVO)
			&& ((values[2] instanceof String) || (values[2] == null))) {
			try {
				SHCardPayVO payVO = (SHCardPayVO) values[1];
				String host = (String) values[2];

				ShoppingCardHome homeobj =
					(ShoppingCardHome) ServiceLocator.getInstance(
						host).getRemoteHome(
						"pos41/ejb/ShoppingCard",
						ShoppingCardHome.class);

				ShoppingCard shoppingCard = homeobj.create();

				String result = null;

				result = shoppingCard.pay(payVO);

				Object[] results = new Object[1];

				results[0] = result;

				return results;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}

}
