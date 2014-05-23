package com.royalstone.pos.coupon;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.PosConfig;

/**
 * Created by IntelliJ IDEA. User: fire Date: 2005-6-24
 */
public class CouponMgrImpl implements CouponMgr {
	private URL servlet;

	private HttpURLConnection conn;

	public CouponMgrImpl() {
		try {
			servlet = new URL("http://"
					+ pos.core.getPosContext().getServerip() + ":"
					+ pos.core.getPosContext().getPort()
					+ "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String sale(CouponSale coupon) throws IOException, CouponException {
		String result = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] = "com.royalstone.pos.web.command.CouponSaleTransfer";
			params[1] = coupon;
			params[2] = PosConfig.getInstance().getString("CARDHOST_IP");

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (String) results[0];
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}

		return result;
	}

	/**
	 * 卷销售增加店号 区分卷的销售地点
	 * */
	public CouponSale getCouponSale(String couponID, String secrety, String storeid)
			throws IOException, CouponException {
		CouponSale result = null;
		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[5];

			params[0] = "com.royalstone.pos.web.command.CouponSaleQueryTransfer";
			params[1] = couponID;
			params[2] = secrety;
			params[3] = storeid;
			params[4] = PosConfig.getInstance().getString("CARDHOST_IP");

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {

				result = (CouponSale) results[0];

				if (result == null
						|| (result.getExceptionInfo() != null && !result
								.getExceptionInfo().equals(""))) {

					throw new CouponException(result.getExceptionInfo());

				}

			} else {

				throw new IOException("GetCouponSale Error");

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}

		return result;
	}

	public CouponEnCash query(String couponID, String secrety)
			throws IOException, CouponException {
		CouponEnCash result = null;
		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[4];

			params[0] = "com.royalstone.pos.web.command.CouponQueryTransfer";
			params[1] = couponID;
			params[2] = secrety;
			params[3] = PosConfig.getInstance().getString("CARDHOST_IP");

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {

				result = (CouponEnCash) results[0];

			} else {

				throw new IOException("CouponEnCash Error");

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}

		return result;
	}

	public String autoRever(CouponSaleList couponSales) throws IOException,
			CouponException {
		String result = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] = "com.royalstone.pos.web.command.CouponAutoReverTransfer";
			params[1] = couponSales;
			params[2] = PosConfig.getInstance().getString("CARDHOST_IP");

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (String) results[0];
			} else {
				throw new IOException("AutoRever Error");
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}

		return result;
	}

	public String saleAndEncashCoupons(CouponSaleList couponSales)
			throws IOException, CouponException {
		String result = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] = "com.royalstone.pos.web.command.CouponSaleTransfer";
			params[1] = couponSales;
			params[2] = PosConfig.getInstance().getString("CARDHOST_IP");

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {

				result = (String) results[0];

			} else {

				throw new IOException("SaleAndEncashCoupons Error");

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}

		return result;
	}

	public CouponLargess getCouponInfo(String coupon, String count,
			String sheepID, String largessType) throws IOException, CouponException {
		CouponLargess result = null;
		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[6];

			params[0] = "com.royalstone.pos.web.command.CouponInfoQuery";
			params[1] = coupon;
			params[2] = count;
			params[3] = sheepID;
			params[4] = largessType;
			params[5] = PosConfig.getInstance().getString("CARDHOST_IP");

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {

				result = (CouponLargess) results[0];

//				if (result == null
//						|| (result.getExceptionInfo() != null && !result
//								.getExceptionInfo().equals(""))) {
//					throw new CouponException(result.getExceptionInfo());
//				}

//			} else {
//
//				throw new IOException("券信息查询错误");

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}

		return result;
	}

	public void copyCouponInfo(String salesID) throws IOException {

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] = "com.royalstone.pos.web.command.CardCouponInfo";
			params[1] = PosConfig.getInstance().getString("CARDHOST_IP");
			params[2] = salesID;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {

				String result = (String) results[0];
				
				System.out.println("copyCouponInfo=" + result);

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
}

