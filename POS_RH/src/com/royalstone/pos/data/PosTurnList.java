package com.royalstone.pos.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.royalstone.pos.util.FileUtil;

/**
 * @author liangxinbiao
 */

public class PosTurnList implements Serializable {

	private static PosTurnList instance;
	public static final String file4posTurnList =
		"work" + File.separator + "PosTurnList.dat";
	private ArrayList turnList;

	private PosTurnList() {
		turnList = new ArrayList();
	}

	public static PosTurnList getInstance() {
		if (instance == null) {
			instance = load();
		}
		return instance;
	}

	public void deletePosTurnByState(int stat) {
		ArrayList newTurnList = new ArrayList();
		for (int i = 0; i < turnList.size(); i++) {
			PosTurn turn = (PosTurn) turnList.get(i);
			if (turn.getStat() != stat) {
				newTurnList.add(turnList.get(i));
			}
		}
		turnList = newTurnList;
	}

	/**
	 * ��PosTurn���뵽PosTurnList����
	 * @param turn
	 */
	public void addPosTurn(PosTurn turn) {
		turnList.add(turn);
	}

	/**
	 * ��PosTurnList�����PosTurn��Stat������������������PosTurn
	 * @param stat ���״̬
	 * @return �ɷ���������PosTurn��ɵ�ArrayList
	 */
	public ArrayList findByState(int stat) {
		ArrayList result = new ArrayList();
		for (int i = 0; i < turnList.size(); i++) {
			PosTurn turn = (PosTurn) turnList.get(i);
			if (turn.getStat() == stat) {
				result.add(turnList.get(i));
			}
		}
		return result;
	}

	/**
	 * ��PosTurnList�����PosTurn��Stat��isEndOffLine������������������PosTurn
	 * @param stat ���״̬
	 * @param isEndOffLine �Ƿ����ѻ�״̬����ɰ���
	 * @return �ɷ���������PosTurn��ɵ�ArrayList
	 */
	public ArrayList findByStateAndIsEndOffLine(
		int stat,
		boolean isEndOffLine) {
		ArrayList result = new ArrayList();
		for (int i = 0; i < turnList.size(); i++) {
			PosTurn turn = (PosTurn) turnList.get(i);
			if (turn.getStat() == stat
				&& turn.isEndOffLine() == isEndOffLine) {
				result.add(turnList.get(i));
			}
		}
		return result;
	}

	/**
	 * ��PosTurnListд���ļ��� 
	 */
	public void dump() {
		try {
			ObjectOutputStream out =
				new ObjectOutputStream(new FileOutputStream(file4posTurnList));
			out.writeObject(this);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static PosTurnList load() {
		PosTurnList result = null;
		FileInputStream fin = null;
		ObjectInputStream in = null;
		try {
			fin = new FileInputStream(file4posTurnList);
			in = new ObjectInputStream(fin);
			result = (PosTurnList) in.readObject();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			result = new PosTurnList();
			result.dump();
		} catch (Exception e) {
			e.printStackTrace();

			try {
				if (fin != null) {
					fin.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			FileUtil.fileError(file4posTurnList);
			result = new PosTurnList();
			result.dump();
		}
		return result;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("------------------\n");
		for (int i = 0; i < turnList.size(); i++) {
			result.append(turnList.get(i).toString() + "\n");
			result.append("------------------\n");
		}
		return result.toString();
	}

}
