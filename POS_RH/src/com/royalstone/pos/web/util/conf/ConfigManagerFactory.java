package com.royalstone.pos.web.util.conf;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author yaopoqing
 * 
 * �������� 2005-3-22
 */
public class ConfigManagerFactory {
	
	
		private static Map configureCache = new Hashtable() ;


		public static ConfigManager getPropertiesConfigure(String fileName) {
			if (configureCache.get(fileName) == null) {
				ConfigManager configure = new ConfigManagerPropertiesImpl(fileName) ;
				configureCache.put(fileName, configure) ;
				return configure ;
			}
			else {
				return (ConfigManager)configureCache.get(fileName) ;
			}

		}
	   public static ConfigManager getPropertiesConfigure(){
	   	     String configPath="/pos.ini";
			 return getPropertiesConfigure(configPath);

			}

}
