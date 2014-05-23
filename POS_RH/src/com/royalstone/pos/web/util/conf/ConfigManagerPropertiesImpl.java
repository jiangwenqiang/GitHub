package com.royalstone.pos.web.util.conf;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * User: fire
 * Date: 2005-3-21 || 
 */
public class ConfigManagerPropertiesImpl  implements ConfigManager {
	private String configFileName = null;
	private static Properties propertyCache=null;

    
    
    public ConfigManagerPropertiesImpl(String fileName){
		this.configFileName = fileName;
    }
	

    public synchronized String getPropertyValue(String name) throws IOException {
        if(propertyCache==null)
		    loadProperites();
        String property = (String) propertyCache.get(name);
        if(property==null)
            loadProperites();
        property = (String) propertyCache.get(name);
        if (property == null) {
         throw new IOException("�����������ļ����ҵ���Ӧ������"+name);
        } else {
            return property.trim();
        }
    }



    /**
     * �������ļ���������
     */
    private synchronized void loadProperites() throws IOException {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            properties .load(this.getClass().getResourceAsStream(configFileName));
            propertyCache = properties;
        }catch(IOException e){ 
            throw (e);
        }        
        finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
            }
        }
    }



}
