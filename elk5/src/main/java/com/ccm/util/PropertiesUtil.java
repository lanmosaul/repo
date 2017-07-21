package com.ccm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author Jenny Liao
 * This allow to access other Java classes call {@link #getProperty(String)}  to get properties from spring load properties,
 * Need to replace org.springframework.beans.factory.config.PropertyPlaceholderConfigurer with com.ccm.api.util.PropertiesUtil
 * in spring xml.
 */
public class PropertiesUtil extends PropertyPlaceholderConfigurer {
    private static Map<String, String> propertiesMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory,
              Properties props) throws BeansException {
         super.processProperties(beanFactory, props);

         propertiesMap = new HashMap<String, String>();
         for (Object key : props.keySet()) {
             String keyStr = key.toString();
             propertiesMap.put(keyStr, resolvePlaceholder(keyStr,
                 props, SYSTEM_PROPERTIES_MODE_FALLBACK));
         }
     }

     public static String getProperty(String name) {
         return propertiesMap.get(name);
     }
     
     public static void setProperty(String name, String value) {
         propertiesMap.put(name, value);
     }
}
