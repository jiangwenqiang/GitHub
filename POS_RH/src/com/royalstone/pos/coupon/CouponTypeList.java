
package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.util.Vector;


public class CouponTypeList implements Serializable
{
	
	String  exception = "";
	
	public CouponTypeList()
	{
		lst = new Vector();
	}
	
	public void setException(String exception){
		this.exception = exception;
		}
	
	public String getException(){
		return exception;
		}
	
	public void add( String couponType)
	{ 
		lst.add(couponType);
	}
	
	
	public int size(){
		return lst.size();
		}
	
	
	public void rmove(){
		lst.removeAllElements();
		}
	
	public String getCouponType(int i){
		return (String)lst.get(i);
		}


	private Vector lst;
}
