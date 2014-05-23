package com.royalstone.pos.invoke.realtime;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.royalstone.pos.common.Goods;
import com.royalstone.pos.common.GoodsCut;
import com.royalstone.pos.common.GoodsExt;
import com.royalstone.pos.common.GoodsProduct;
import com.royalstone.pos.complex.DiscComplexList;
import com.royalstone.pos.favor.BulkPrice;
import com.royalstone.pos.favor.DiscCriteria;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;
import com.royalstone.pos.shell.pos;

/**
 * ��װ��ʵʱ��۵Ŀͻ��˵���
 * @author liangxinbiao
 */
public class RealTime {

	private static RealTime instance;
	private URL servlet;
	private HttpURLConnection conn;

	private RealTime() {
		try {
			servlet =
				new URL(
					"http://"
						+ pos.core.getPosContext().getServerip()
						+ ":"
						+ pos.core.getPosContext().getPort()
						+ "/pos41/DispatchServlet");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static RealTime getInstance() {
		if (instance == null) {
			instance = new RealTime();
		}
		return instance;
	}

	public Goods findGoods(String code) throws RealTimeException {

		Goods result = null;
		String errorMsg1 = null;
		String errorMsg2 = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] =
				"com.royalstone.pos.web.command.realtime.LookupGoodsCommand";
			params[1] = code;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (Goods) results[0];
				errorMsg1 = (String) results[1];
				errorMsg2 = (String) results[2];
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RealTimeException("�������",true);
		}

		if (errorMsg1 != null) {
			System.out.println(errorMsg2);
			throw new RealTimeException(errorMsg1);
		}

		return result;
	}

	
	
	/**
	 * ʵʱ��ѯ��Ʒ��Ϣ
	 * @param code ��Ʒ���������
	 * @return ��Ʒ��Ϣ
	 * @throws RealTimeException
	 */
	public GoodsProduct findGoodsProduct(String code) throws RealTimeException {

		GoodsProduct result = null;
		String errorMsg1 = null;
		String errorMsg2 = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] =
				"com.royalstone.pos.web.command.realtime.LookupGoodsProductCommand";
			params[1] = code;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}
			
			if (results != null && results.length > 0) {
				result = (GoodsProduct) results[0];
				errorMsg1 = (String) results[1];
				errorMsg2 = (String) results[2];
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RealTimeException("�������",true);
		}

		if (errorMsg1 != null) {
			System.out.println(errorMsg2);
			throw new RealTimeException(errorMsg1);
		}
		return result;
	}

    public Goods findGoodsComb(String code) throws RealTimeException {

            Goods result = null;
            String errorMsg1 = null;
            String errorMsg2 = null;

            try {
                conn = (HttpURLConnection) servlet.openConnection();

                Object[] params = new Object[2];

                params[0] =
                    "com.royalstone.pos.web.command.realtime.LookupGoodsCombCommand";
                params[1] = code;

                MarshalledValue mvI = new MarshalledValue(params);
                MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

                Object[] results = null;

                if (mvO != null) {
                    results = mvO.getValues();
                }

                if (results != null && results.length > 0) {
                    result = (Goods) results[0];
                    errorMsg1 = (String) results[1];
                    errorMsg2 = (String) results[2];
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RealTimeException("�������",true);
            }

            if (errorMsg1 != null) {
                System.out.println(errorMsg2);
                throw new RealTimeException(errorMsg1);
            }

            return result;
        }

    public String findPrecentage(String cardLevelID,String deptID,String flag) throws RealTimeException {

             String result ="0";
             String errorMsg1 = null;
             String errorMsg2 = null;

             try {
                 conn = (HttpURLConnection) servlet.openConnection();

                 Object[] params = new Object[4];

                 params[0] =
                     "com.royalstone.pos.web.command.realtime.LookupPrecentageCommand";
                 params[1] = cardLevelID;
                 params[2] = deptID;
                 params[3] = flag;	// zhouzhou ��־λ 0 ȡ����ֵ 1ȡ����ֵ

                 MarshalledValue mvI = new MarshalledValue(params);
                 MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

                 Object[] results = null;

                 if (mvO != null) {
                     results = mvO.getValues();
                 }

                 if (results != null && results.length > 0) {
                     result = (String) results[0];
                     errorMsg1 = (String) results[1];
                     errorMsg2 = (String) results[2];
                 }
             } catch (IOException ex) {
                 ex.printStackTrace();
                 throw new RealTimeException("�������",true);
             }

             if (errorMsg1 != null) {
                 System.out.println(errorMsg2);
                 throw new RealTimeException(errorMsg1);
             }

             return result;
         }

    public GoodsCut findGoodsCut(String code) throws RealTimeException {

        GoodsCut result = null;
        String errorMsg1 = null;
        String errorMsg2 = null;

        try {
            conn = (HttpURLConnection) servlet.openConnection();

            Object[] params = new Object[2];

            params[0] =
                "com.royalstone.pos.web.command.realtime.LookupGoodsCutCommand";
            params[1] = code;

            MarshalledValue mvI = new MarshalledValue(params);
            MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

            Object[] results = null;

            if (mvO != null) {
                results = mvO.getValues();
            }

            if (results != null && results.length > 0) {
                result = (GoodsCut) results[0];
                errorMsg1 = (String) results[1];
                errorMsg2 = (String) results[2];
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RealTimeException("�������",true);
        }

        if (errorMsg1 != null) {
            System.out.println(errorMsg2);
            throw new RealTimeException(errorMsg1);
        }

        return result;
    }

	/**
	 * ��һƷ������ʵʱ��ѯ��Ʒ��Ϣ
	 * @param code ��Ʒ����
	 * @return ��Ʒ��չ��Ϣ
	 * @throws RealTimeException
	 */
	public GoodsExt findGoodsExt(String code) throws RealTimeException {

		GoodsExt result = null;
		String errorMsg1 = null;
		String errorMsg2 = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] =
				"com.royalstone.pos.web.command.realtime.LookupGoodsExtCommand";
			params[1] = code;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (GoodsExt) results[0];
				errorMsg1 = (String) results[1];
				errorMsg2 = (String) results[2];
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RealTimeException("�������",true);
		}

		if (errorMsg1 != null) {
			System.out.println(errorMsg2);
			throw new RealTimeException(errorMsg1);
		}
		return result;
	}

	/**
	 * ʵʱ��ѯ��Ʒ����ͨ������Ϣ
	 * @param code ��Ʒ����
	 * @param ptype ��������
	 * @return ������Ϣ
	 * @throws RealTimeException
	 */
	public DiscCriteria getDiscCriteria(String code, String ptype)
		throws RealTimeException {

		DiscCriteria result = null;
		String errorMsg1 = null;
		String errorMsg2 = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[3];

			params[0] =
				"com.royalstone.pos.web.command.realtime.GetDiscCriteriaCommand";
			params[1] = code;
			params[2] = ptype;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (DiscCriteria) results[0];
				errorMsg1 = (String) results[1];
				errorMsg2 = (String) results[2];
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RealTimeException("�������",true);
		}

		if (errorMsg1 != null) {
			System.out.println(errorMsg2);
			throw new RealTimeException(errorMsg1);
		}
		return result;
	}

	/**
	 * ʵʱ��ѯ��Ʒ������������Ϣ
	 * @param code ��Ʒ����
	 * @return ����������Ϣ
	 * @throws RealTimeException
	 */
	public BulkPrice getBulkPrice(String code) throws RealTimeException {

		BulkPrice result = null;
		String errorMsg1 = null;
		String errorMsg2 = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] =
				"com.royalstone.pos.web.command.realtime.GetBulkPriceCommand";
			params[1] = code;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (BulkPrice) results[0];
				errorMsg1 = (String) results[1];
				errorMsg2 = (String) results[2];
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RealTimeException("�������",true);
		}

		if (errorMsg1 != null) {
			System.out.println(errorMsg2);
			throw new RealTimeException(errorMsg1);
		}
		return result;
	}

	/**
	 * ʵʱ��ѯ���˿����Żݽ��
	 * @param cardno ���˿���
	 * @return �Żݽ��
	 * @throws RealTimeException
	 */
	public int getLoanCardDiscPrice(String cardno) throws RealTimeException {

		int result = 0;
		String errorMsg1 = null;
		String errorMsg2 = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] =
				"com.royalstone.pos.web.command.realtime.GetLoanCardDiscCountCommand";
			params[1] = cardno;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = ((Integer)results[0]).intValue();
				errorMsg1 = (String) results[1];
				errorMsg2 = (String) results[2];
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RealTimeException("�������",true);
		}

		if (errorMsg1 != null) {
			System.out.println(errorMsg2);
			throw new RealTimeException(errorMsg1);
		}
		return result;
	}
	
	/**
	 * ʵʱ��ѯ��Ʒ����ϴ�����Ϣ
	 * @param code ��Ʒ����
	 * @return ��ϴ�����Ϣ
	 * @throws RealTimeException
	 */
	public DiscComplexList getComplextList(String code)
		throws RealTimeException {

		DiscComplexList result = null;
		String errorMsg1 = null;
		String errorMsg2 = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] =
				"com.royalstone.pos.web.command.realtime.GetComplexListCommand";
			params[1] = code;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (DiscComplexList) results[0];
				errorMsg1 = (String) results[1];
				errorMsg2 = (String) results[2];
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RealTimeException("�������!",true);
		}

		if (errorMsg1 != null) {
			System.out.println(errorMsg2);
			throw new RealTimeException(errorMsg1);
		}

		return result;
	}


	/**
	 * ����ϵͳ�Ƿ�����
	 * @return �Ƿ�����
	 */
	public boolean testOnLine(){
		
		boolean result=false;	
		
		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[1];

			params[0] =
				"com.royalstone.pos.web.command.realtime.TestOnLineCommand";

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				if(((String) results[0]).equals("1")){
					result=true;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return result;
	}
	
	
	public ArrayList getGoodsUpdateList(ArrayList goodsNoList)
		throws RealTimeException {

		ArrayList result = null;
		String errorMsg1 = null;
		String errorMsg2 = null;

		try {
			conn = (HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] =
				"com.royalstone.pos.web.command.realtime.GetGoodsUpdateCommand";
			params[1] = goodsNoList;

			MarshalledValue mvI = new MarshalledValue(params);
			MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				result = (ArrayList) results[0];
				errorMsg1 = (String) results[1];
				errorMsg2 = (String) results[2];
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RealTimeException("�������!",true);
		}

		if (errorMsg1 != null) {
			System.out.println(errorMsg2);
			throw new RealTimeException(errorMsg1);
		}

		return result;
	}

}
