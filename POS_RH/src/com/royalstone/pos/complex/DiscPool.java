/*
 * Created on 2004-6-1
 */
package com.royalstone.pos.complex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * DiscPool ����ʵ����ϴ����еĵ�Ч��Ʒ��.
 * ��ͬһ��DiscPool �е���Ʒ���пɻ�����. ����, ����Ʒa/b/c��10Ԫ��ѡ3ֻ,����Ʒa/b/c��ͬһ��DiscPool��.
 * @author Mengluoyi
 */
public class DiscPool implements Serializable {
	public static void main(String[] args) {
		DiscPool a, b, c, d, m;
		a = new DiscPool("666301", "666301", 3, 1000);
		b = new DiscPool("666301", "666302", 3, 1000);
		c = new DiscPool("666301", "666303", 3, 1000);

		m = DiscPool.merge(a, b);
		m = DiscPool.merge(m, c);

		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		System.out.println(m);

		a = new DiscPool("666701", "666701", 7, 2000);
		b = new DiscPool("666701", "666702", 7, 2000);
		c = new DiscPool("666701", "666703", 7, 2000);
		d = new DiscPool("666701", "666704", 7, 2000);
		m = DiscPool.merge(a, b);
		m = DiscPool.merge(m, c);
		m = DiscPool.merge(m, d);

		System.out.println(m);
	}

	/**
	 * @param leader	��Ч��Ʒ����鳤
	 */
	public DiscPool(String leader) {
		this.leader = leader;
	}

	/**	Constructor
	 * @param leader	��Ʒ�鳤
	 * @param member	��Ʒ���Ա
	 * @param qty		��Ʒ�����Ч����
	 * @param pool_price	��Ʒ��Ĵ����۸�
	 */
	public DiscPool(String leader, String member, int qty, int pool_price) {
		this.leader = leader;
		members = new String[1];
		members[0] = member;
		qty_per_group = qty;
		this.pool_price = pool_price;
	}

	/**	�ж���Ʒ�����Ƿ���DiscPool �ĳ�Ա����.
	 * @param vgno	��Ʒ����
	 * @return	true	�������Ʒ���������ƷС����;<br/>
	 * 			false	�������Ʒ���벻����ƷС����.
	 */
	public boolean hasVgno(String vgno) {
		for (int i = 0; i < members.length; i++)
			if (vgno.equals(members[i]))
				return true;

		return false;
	}

	/**	�ж�����DiscPool �Ƿ�����ͬ���鳤. ����鳤��ͬ,����Ϊ��ͬһ����Ч��,���Խ��кϲ�.
	 * @param b		DiscPool ����
	 * @return	true	���������this��������ͬ���鳤;<br/>
	 * 			false	�鳤��ͬ.
	 */
	public boolean isSamePool(DiscPool b) {
		return (this.leader.equals(b.leader));
	}

	/**	�жϵ�Ч��Ʒ���Ƿ���Ʒ.
	 * @return		true	��Ʒ;<br/>
	 * 				false	����Ʒ.
	 */
	public boolean isGift() {
		return (pool_price == 0);
	}

	/**	���������Ч��Ʒ����鳤��ͬ,���԰�������Ч��ϲ�. �ɸú�����ɺϲ�.
	 * @param a	DiscPool����
	 * @param b	DiscPool����
	 * @return	�µ�	DiscPool����, ���г�Ա��ƷΪa��b�Ĳ���.
	 */
	public static DiscPool merge(DiscPool a, DiscPool b) {
		// �������ͬһ����Ч��,�򷵻�Ϊnull.
		if (!a.isSamePool(b))
			return null;

		DiscPool new_item = new DiscPool(a.leader);
		new_item.qty_per_group =
			(a.qty_per_group > b.qty_per_group)
				? a.qty_per_group
				: b.qty_per_group;
		new_item.pool_price =
			(a.pool_price > b.pool_price) ? a.pool_price : b.pool_price;
		new_item.members = new String[a.members.length + b.members.length];

		int i;
		for (i = 0; i < a.members.length; i++)
			new_item.members[i] = a.members[i];
		for (int j = 0; j < b.members.length; i++, j++)
			new_item.members[i] = b.members[j];

		return new_item;
	}

	/**	ȡ��Ч����
	 * @return	Ϊʹ����������Ч,С������Ʒ������Ӧ�ﵽ����С����.
	 */
	public int getMinQty() {
		return qty_per_group;
	}

	/**	ȡ�����ۼ�
	 * @return	DiscPool �Ĵ����ۼ�(���ۼ۶�Ӧ����Ʒ����Ϊqty_per_group)
	 */
	public int getPromPrice() {
		return pool_price;
	}

	/**
	 * for debug use.
	 */
	public String toString() {
		String s = (members.length > 0) ? members[0] : "";
		for (int i = 1; i < members.length; i++)
			s += (", " + members[i]);

		return "{ "
			+ leader
			+ " * "
			+ qty_per_group
			+ " @"
			+ pool_price
			+ " : "
			+ " [ "
			+ s
			+ " ] "
			+ " }";
	}

	public DiscPool[] split() {

		ArrayList memberList = new ArrayList();

		if (qty_per_group < members.length) {

			if (members.length > 1 && qty_per_group > 1) {

				for (int i = 0; i < members.length; i++) {
					for (int j = 0; j < members.length; j++) {
						if (i != j) {
							ArrayList horizonList = new ArrayList();
							horizonList.add(members[i]);
							horizonList.add(members[j]);
							memberList.add(horizonList);
						}
					}
				}

				for (int k = 2; k < qty_per_group; k++) {

					ArrayList newMemberList = new ArrayList();
					for (int i = 0; i < members.length; i++) {
						for (int j = 0; j < memberList.size(); j++) {
							if (!((ArrayList) memberList.get(j))
								.contains(members[i])) {
								ArrayList horizonList = new ArrayList();
								horizonList.addAll(
									(ArrayList) memberList.get(j));
								horizonList.add(members[i]);
								newMemberList.add(horizonList);
							}
						}
					}
					memberList = newMemberList;

				}

			}

		}

		ArrayList allMemberList = new ArrayList();
		allMemberList.addAll(Arrays.asList(members));
		memberList.add(allMemberList);

		DiscPool[] result = new DiscPool[members.length + memberList.size()];

		for (int i = 0; i < members.length; i++) {
			result[i] =
				new DiscPool(members[i], members[i], qty_per_group, pool_price);
		}

		for (int i = 0; i < memberList.size(); i++) {

			ArrayList horizonList = (ArrayList) memberList.get(i);
			if (horizonList.size() > 0) {
				result[members.length + i] =
					new DiscPool(
						(String) horizonList.get(0),
						(String) horizonList.get(0),
						qty_per_group,
						pool_price);
			}

			String[] s = new String[horizonList.size()];
			for (int j = 0; j < horizonList.size(); j++) {
				s[j] = (String) horizonList.get(j);
			}

			result[members.length + i].setMembers(s);
		}

		return result;

	}

	public void setMembers(String[] members) {
		this.members = members;
	}

	/**
	 * <code>leader</code>		��Ч��Ʒ���"�鳤"
	 */
	private String leader = "";

	/**
	 * <code>members</code>		��Ч��Ʒ���������ĳ�Ա,ÿ����Ա��һ����Ʒ.
	 */
	private String[] members = null;

	/**
	 * <code>qty_per_group</code>	Ϊʹ����������Ч,С������Ʒ������Ӧ�ﵽ����С����.
	 */
	private int qty_per_group = 1;

	/**
	 * <code>pool_price</code>		�ڴ���������,��С����ۼ�(�Է�Ϊ��λ).
	 */
	private int pool_price = 0;
}
