
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
	
	// ����Ϸ����ݵľ����ݣ�����Ϸ�����ȥ��Ҫ����Ʒ����
	public int toMoveVngo(String largess){
		// ��¼����Ҫ�����Ʒ��
		int studio = 0;
		// ��¼��Ʒ��Ӧ�������
		int count =0;
		// ����Ʒ�ϵ����������ڼ���Ʒʱ
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
    private String updateType="0";// //sale ״̬�� 4 ��Ϊ1 ��encash1,ȯ���ۺ��״̬  f ȯ���ֺ��״̬ ״̬�� 1 ��Ϊf
}
