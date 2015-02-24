/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core.zoo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author NuwanThilini
 */
public class ConfigHelper {
    private Map<String,String> map = new HashMap<>();
    private ConfigHelper()
    {
        
    }
    public static ConfigHelper create()
    {
        return new ConfigHelper();
    }
    
    public ConfigHelper put(String config,String value)
    {
        map.put(config, value);
        return this;
    }
    
    public ConfigHelper put(String config,int value)
    {
        map.put(config, String.valueOf(value));
        return this;
    }
    
    public Map<String,String> get()
    {
        return map;
    }
}
