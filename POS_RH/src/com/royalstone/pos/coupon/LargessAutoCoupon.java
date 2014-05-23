/*
 * 创建日期 2006-2-9
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
