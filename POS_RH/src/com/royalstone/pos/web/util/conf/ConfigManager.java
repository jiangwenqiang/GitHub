package com.royalstone.pos.web.util.conf;
import java.io.IOException;

/**
 * User: fire
 * Date: 2005-3-21 || 
 */
/**
 * �ṩ���������ļ��Ľӿ�
 * �����ļ������� XML ��ʽ���� Properties �ļ���ʽ
 * �����ļ����������������¸�ʽ��root.child.property
 * �� XML ��Ϊ��<root><child><property>value</property></child></root>
 * �� Propetties ��Ϊ root.child.property=value
 */
public interface ConfigManager {

        /**
     * ����ָ�����Ƶ�����ֵ
     * �������Ƹ�ʽΪ��root.child.property
     * @param name - ��������
     * @return String ����ֵ
     */
    public String getPropertyValue(String name) throws IOException;


}
