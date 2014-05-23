package com.royalstone.pos.common;


import java.util.Vector;






public class CouponListLargess // implements Serializable
{
	public CouponListLargess()
	{
		lst = new Vector();
	}

	public boolean add( Goods goods )
	{
		if (lst.size() < 1){
			lst.add(goods);
			return true;
		}
		return false;
	}
	
  /*  public int getTotalValue(){
        int total=0;
        for(int i=0;i<lst.size();i++){
          int couponValue=(int)Math.rint(((CouponLargess)lst.get(i)).getPrice().doubleValue()*100);
           total=total+couponValue;
       }
        return total;
    }
*/
	/**
	 * @return
	 */
	public int size()
	{
		return lst.size();
	}

	public Goods get( int i )
	{
		return (Goods) lst.get(i);
	}
	
	public void remove(int i)
	{
		lst.remove(i);
		}
	
    public void removeCouponSale(String barcode){
        for(int i=0;i<lst.size();i++){
        	Goods goods= (Goods)lst.get(i);
            if(goods.getBarcode().equals(barcode))
                lst.remove(goods);
        }
     }

 //   public Element toElement(){
 //       Element elm_list = new Element("CouponSaleList");
//		for (int i = 0; i < lst.size(); i++)
//			elm_list.addContent(((CouponLargess) lst.get(i)).toElement());
//		return elm_list;
//    }

//      public static String getCouponNOReprint(String file){
//       //todo
//        return null;
//    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }
    
/*     public void removeCouponSale(String couponID){
       for(int i=0;i<lst.size();i++){
          CouponSale couponSale= (CouponSale)lst.get(i);
           if(couponSale.getCouponID().equals(couponID))
               lst.remove(couponSale);
       }
    }
  */  
	/**
	 * Comment for <code>lst</code>
	 */
	private Vector lst;
    private String updateType="0";// //sale 状态由 4 变为1 ，encash1,券销售后的状态  f 券兑现后的状态 状态由 1 变为f
}
