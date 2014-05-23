/*
 * Good Day;
 */
package com.royalstone.pos.hardware;

import jpos.JposException;
import jpos.LineDisplay;

import com.royalstone.pos.common.Sale;
import com.royalstone.pos.util.Value;

/**
 * @author wubingyan
 * Created by King_net  on 2005-7-20  11:04:25
 */
public class RHTouchCustDisplay implements ICustDisplay{

	private LineDisplay control;

	/**
	 * @see ICustDisplay#init()
	 */
	public void init(LineDisplay c) {
		this.control = c;
		try {
			control.clearText();
		} catch (JposException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see ICustDisplay#printText(String)
	 */
	public void printText(String value) {
		try {
			control.displayText(value, 0);
		} catch (JposException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see ICustDisplay#clear()
	 */
	public void clear() {
		try {
			control.clearText();
		} catch (JposException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see ICustDisplay#welcome()
	 */
	public void welcome() {
		
		//printText("ª∂”≠π‚¡Ÿ");
        try {
            control.displayTextAt(0,0,"Welcome to WINGWAH!",1);
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

	/**
	 * @see ICustDisplay#printSubTotal(String)
	 */
	public void printSubTotal(String value) {
		   try {
            control.displayTextAt(0,0,"Total: "+value,3);
        } catch (JposException e) {
            e.printStackTrace();
        }
	}

	/**
	 * @see ICustDisplay#printTotal(String)
	 */
	public void printTotal(String value) {
        try {
            control.displayTextAt(0,0,"Total: "+value,3);
        } catch (JposException e) {
            e.printStackTrace();
        }	}

	/**
	 * @see ICustDisplay#printReturn(String)
	 */
	public void printReturn(String value) {
		 try {
            control.displayTextAt(0,0,"Return: "+value,2);
        } catch (JposException e) {
            e.printStackTrace();
        }
		
	}

	/**
	 * @see ICustDisplay#printGoods(String)
	 */
	public void printGoods(Sale s) {
//		clear();
//
//		String text=s.getName().substring(0,5)+": "
//				+ (new Value(s.getStdPrice())).toString()
//				+ "x"
//				+ s.getQtyStr();
//		printText(text);
	}

	/**
	 * @see ICustDisplay#printAmtPr(String)
	 */
	public void printAmtPr(String value) {
//		printText("Amount: " + value);
	}

	/**
	 * @see ICustDisplay#printPayment(String)
	 */
	public void printPayment(String value) {
//		String text="÷ß∏∂: " + value;
//		printText(text);
	}

	/**
	 * @see ICustDisplay#printSubTotal(Value)
	 */
	public void printSubTotal(Value value) {
	}

	/**
	 * @see ICustDisplay#printTotal(Value)
	 */
	public void printTotal(Value value) {
	}

	/**
	 * @see ICustDisplay#printReturn(Value)
	 */
	public void printReturn(Value value) {
	}

	/**
	 * @see ICustDisplay#printPayment(Value)
	 */
	public void printPayment(Value value) {
	}
}
