package com.royalstone.pos.ejb.util;

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
 * EJB的服务查找器
 * @deprecated
 * @author liangxinbiao
 */
public class ServiceLocator {
	private Context _context;
	private Map _cache;
	private static ServiceLocator _instance;
	private String url;

	private ServiceLocator() throws Exception {
		try {
			_context = this.getJBossInitialContext();
			_cache = Collections.synchronizedMap(new HashMap());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static public synchronized ServiceLocator getInstance() throws Exception {
		if (_instance == null) {
			_instance = new ServiceLocator();
			return _instance;
		}
		return _instance;
	}

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

	private javax.naming.Context getJBossInitialContext()
		throws NamingException {
		return new InitialContext();
	}

}
