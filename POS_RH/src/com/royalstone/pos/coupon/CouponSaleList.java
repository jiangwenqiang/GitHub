package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;



/**
   @version 1.0 2005.8.03
   @author  fire, Royalstone Co., Ltd.
 */

public class CouponSaleList implements Serializable
{
	/**
	 *
	 */
	public CouponSaleList()
	{
		lst = new Vector();
	}

	/**
	 * @param couponSale
	 */
	public boolean add( CouponSale couponSale )
	{   
		String couponID=couponSale.getCouponID();
        for(int i=0;i<lst.size();i++){
            //��������ͬȯ�Ų��ܲ���ȯ�б�
           if(((CouponSale)lst.get(i)).getCouponID().equals(couponID))
               return false;
       }
		lst.add(couponSale);
        return true;
	}
	
	
    public int getTotalValue(){
        int total=0;
        for(int i=0;i<lst.size();i++){
          int couponValue=(int)Math.rint(((CouponSale)lst.get(i)).getPrice().doubleValue()*100);
           total=total+couponValue;
       }
        return total;
    }

	/**
	 * @return
	 */
	public int size()
	{
		return lst.size();
	}
	
	
//	public void move()
//	{
//        for(int i=0;i<lst.size();i++){
//        	lst.remove(i);
//       }
//	}
	

	/**
	 * @param i
	 * @return
	 */
	public CouponSale get( int i )
	{
		return (CouponSale) lst.get(i);
	}

    public Element toElement(){
        Element elm_list = new Element("CouponSaleList");
		for (int i = 0; i < lst.size(); i++)
			elm_list.addContent(((CouponSale) lst.get(i)).toElement());
		return elm_list;
    }

      public static String getCouponNOReprint(String file){
       //todo
        return null;
    }
    /**
     * ��ȡ��ȯ�ĸ���״̬
     * @return
     */
    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }
    /**
     * ����coupon���� ɾ����Sheet�б���� ȯ��������
     * @param couponID
     */
     public void removeCouponSale(String couponID){
       for(int i=0;i<lst.size();i++){
          CouponSale couponSale= (CouponSale)lst.get(i);
           if(couponSale.getCouponID().equals(couponID))
               lst.remove(couponSale);
       }
    }

    public void fromElement(Element root){
        List list;
		try {
			//System.out.println("node name: " + root.getName());
			list = root.getChildren("CouponSale");

			for (int i = 0; i < list.size(); i++)
				this.add(new CouponSale((Element) list.get(i)));

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	/**
	 * Comment for <code>lst</code>
	 */
	private Vector lst;
    private String updateType="0";// //sale ״̬�� 4 ��Ϊ1 ��encash1,ȯ���ۺ��״̬  f ȯ���ֺ��״̬ ״̬�� 1 ��Ϊf
}
