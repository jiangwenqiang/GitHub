/*
 * �������� 2006-2-9
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.royalstone.pos.coupon;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author zhouzhou
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class LargessAutoCoupon {
	
	CouponLargessList largess;
	
	public  LargessAutoCoupon(){
		}
	
	public CouponLargessList Auto(){
		
		CouponLargessList largess = new CouponLargessList();

        File path = new File("largessinfo");
        File[] files = path.listFiles(new DirFilter("2006RH.xml"));
        if(files!=null){
            for (int i = 0; i < files.length; i++) {
                try {
                    Document doc = (new SAXBuilder()).build(files[i]);                	
                        List list;
                		try {
                			list = doc.getRootElement().getChildren("CouponLargess");

                			for (int a = 0; a < list.size(); a++)
                				largess.add(new CouponLargess((Element) list.get(i)));
                			String dd = String.valueOf((Element)list.get(i));

                		} catch (Exception e) {
                			e.printStackTrace();
                			return largess;
                		}
                } catch (JDOMException e) {
                    e.printStackTrace();
                }
            }
            return largess;
        }
        return largess;
    }
	
	
	
	private class DirFilter implements FilenameFilter {
		String afn;
		DirFilter(String afn) {
			this.afn = afn;
		}
		public boolean accept(File dir, String name) {
			return name.indexOf(afn) != -1;
		}
	}

}
