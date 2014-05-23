package com.royalstone.pos.card;

import java.io.File;
import java.io.FilenameFilter;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.royalstone.pos.shell.pos;

/**
 * ���˿��Զ�����,���Ժ�̨�̵߳���ʽ���У�ÿ�������Ӿͼ��autorever��Ŀ¼����û�г����ļ���
 * ����о�͸��LoanCard�������������������
 * @author liangxinbiao
 */
public class LoanCardAutoRever implements Runnable {

	ILoanCard loanCard = LoanCardFactory.createInstance();

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(10000);
				autoRever();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����
	 */
	private void autoRever() {
		synchronized (pos.Lock) {
			File path = new File("autorever");
			File[] files = path.listFiles(new DirFilter(".xml"));
			if(files!=null){
				for (int i = 0; i < files.length; i++) {
					try {
						Document doc = (new SAXBuilder()).build(files[i]);
						LoanCardPayVO cardpay =
							new LoanCardPayVO(doc.getRootElement());
						String result = loanCard.autoRever(cardpay);
						if (result != null && result.equals("1")) {
							files[i].delete();
						}
					} catch (JDOMException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Ŀ¼������
	 * @author liangxinbiao
	 */
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
