package com.royalstone.pos.web.command;

/**
 * ���������ӿ�,͸��DispatchServlet��ת�ķ������һ��Ҫʵ�ִ˽ӿ�
 * @author liangxinbiao
 */
public interface ICommand {
	
	/**
	 * ����˳����ҵ�񷽷�
	 * @param values
	 * @return
	 */
	public Object[] excute (Object[] values);

}
