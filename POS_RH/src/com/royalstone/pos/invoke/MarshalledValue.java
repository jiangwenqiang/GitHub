package com.royalstone.pos.invoke;

import java.io.Serializable;

/**
 * ��װ��Ҫ��Զ�̵����д��ݵĲ�������
 * @author liangxinbiao
 */
public class MarshalledValue implements Serializable{
	
	Object[] values;
	
	public MarshalledValue(Object[] values){
		this.values=values;
	}
	
	public Object[] getValues(){
		return values;
	}

}
