
package com.royalstone.pos.coupon;

import java.io.Serializable;
import java.util.Vector;

import com.royalstone.pos.common.Operator;

public class CouponVgnoCountList implements Serializable
{
	
	public CouponVgnoCountList()
	{
		lst = new Vector();
	}
	
	
	public void add( Operator operator)
	{ 
		lst.add(operator);
	}
	
	
	public int size(){
		return lst.size();
		}
	
	
	public void rmove(int i){
		lst.remove(i);
		}
	
	public Operator get( int i )
	{
		return (Operator) lst.get(i);
	}
	
	// 算出合符内容的卷内容，如果合符，减去必要的商品数量
	public int toMoveVngo(String largess){
		// 记录符合要求的商品数
		int studio = 0;
		// 记录商品对应卷的数量
		int count =0;
		// 记商品合的数量，是在减商品时
		int Abour = 0;
		int A = 0;
		for (int i = 0; i < lst.size(); i++){
		Operator op = (Operator)lst.get(i);
		if (op.gettypeid().equals(largess)){
			op.getCount();
			studio ++;
			}
			}
		if (studio == 0){
			return A;
			}
		double de = studio/count;
		de =Math.rint(de);
		String at = String.valueOf(de);
		 A = Integer.parseInt(at);
		
		if (A > 0){
		int in = studio - A*count;
			for (int i = 0; i < lst.size(); i++){
				Operator op = (Operator)lst.get(i);
				if (op.gettypeid().equals(largess)){
					Abour++;
					}
				if ((in - Abour) < 0){
					return A;
					}
				lst.remove(i);
				}
			return A;
			}
		else{
			return A;
			}
		}


	private Vector lst;
    private String updateType="0";// //sale 状态由 4 变为1 ，encash1,券销售后的状态  f 券兑现后的状态 状态由 1 变为f
}
