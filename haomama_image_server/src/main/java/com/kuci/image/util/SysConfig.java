package com.kuci.image.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SysConfig {
	
    protected final Log logger = LogFactory.getLog(getClass());
    
    private static SysConfig me = null;
    
    private Map<String, String> cfg = new ConcurrentHashMap<String, String>();
    
    private SysConfig(){
        
    }
    
    public static SysConfig getInstance(){
        if(me == null){
            me = new SysConfig();
            me.readConfig();
        }
        return me;
    }
    
    private void readConfig(){
        Properties properties = new Properties();
        InputStream file = this.getClass().getResourceAsStream("/application.properties");
        try{
            properties.load(file);
        }catch(IOException ex){
            logger.error("没有找到配置文件!");
            return;
        }
        
        Set<Object> keys = properties.keySet();
        Iterator<Object> it = keys.iterator();
        while(it.hasNext()){
            String key = (String)it.next();
            String value = properties.getProperty(key);
           
            this.cfg.put(key, value);
        }
    }
    /**
     *  查找 classpath:config.properties
     * @param key .
     * @return
     */
    public String getConfig(String key){
        if(cfg == null)
            readConfig();
        
        
        if(! this.cfg.containsKey(key)){
            logger.error("没有找到,key(" + key + ")");
            return null;
        }
        
        return (String)this.cfg.get(key);
    }

}
