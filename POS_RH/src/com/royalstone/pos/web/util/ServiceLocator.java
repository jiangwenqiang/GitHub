package com.royalstone.pos.web.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

/**
 * �����������EJB�Ŀͻ���͸������������Դ
 * ���ڵ�һ�β��ҵ������Դ��Ὣ������������
 * �Ӷ��ӿ���һ�ε���Դ����
 * @author liangxinbiao
 */
public class ServiceLocator {
	private Context _context;
	private Map _cache;
	private static ServiceLocator _instance;
	private String url;
	private String host;

	/**
	 * ˽�й��췽��,��ʵ����Ҫ��
	 * @param host
	 * @throws Exception
	 */
	private ServiceLocator(String host) throws Exception {
		try {
			this.host = host;
			_context = this.getJBossInitialContext();
			_cache = Collections.synchronizedMap(new HashMap());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ȡ��ServiceLocator�ĵ�ʵ��
	 * @param host
	 * @return
	 * @throws Exception
	 */
	static public synchronized ServiceLocator getInstance(String host)
		throws Exception {
		if (_instance == null) {
			_instance = new ServiceLocator(host);
			return _instance;
		}
		return _instance;
	}

	/**
	 * ȡ��Զ�̵�Home�ӿ�
	 * @param jndiHomeName
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public EJBHome getRemoteHome(String jndiHomeName, Class className)
		throws Exception {
		EJBHome home = null;
		try {
			if (_cache.containsKey(jndiHomeName)) {
				home = (EJBHome) _cache.get(jndiHomeName);
			} else {
				Object objref = _context.lookup(jndiHomeName);
				Object obj = PortableRemoteObject.narrow(objref, className);
				home = (EJBHome) obj;
				_cache.put(jndiHomeName, home);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return home;
	}

	/**
	 * ȡ������Դ
	 * @param dataSourceName
	 * @return
	 * @throws Exception
	 */
	public DataSource getDataSource(String dataSourceName) throws Exception {
		DataSource dataSource = null;
		try {
			if (_cache.containsKey(dataSourceName)) {
				dataSource = (DataSource) _cache.get(dataSourceName);
			} else {
				dataSource =
					(DataSource) _context.lookup(
						"java:comp/env/" + dataSourceName);
				_cache.put(dataSourceName, dataSource);
			}
		} catch (NamingException ne) {
			throw ne;
		} catch (Exception e) {
			throw e;
		}
		return dataSource;
	}

	/**
	 * ȡ��JBoss�ĳ�ʼ������
	 * @return
	 * @throws NamingException
	 */
	private javax.naming.Context getJBossInitialContext()
		throws NamingException {
		if (host == null || host.equals("")) {
			host = "localhost";
		}

		java.util.Hashtable JNDIParm = new java.util.Hashtable();
		JNDIParm.put(Context.PROVIDER_URL, host);
		JNDIParm.put(
			Context.INITIAL_CONTEXT_FACTORY,
			"org.jnp.interfaces.NamingContextFactory");
		return new InitialContext(JNDIParm);
	}

}
