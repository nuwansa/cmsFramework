/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import com.nsa.core.zoo.ZooHandler;

/**
 *
 * @author NuwanThilini
 */
public class ConfigHandler {
    private ZooHandler hnd =  null;
    public static final String GLOBAL = "global";
    public ConfigHandler(ZooHandler hnd)
    {
        this.hnd = hnd;
    }
    
    public String getConfig(String group,String config,String defaultVal) throws Exception
    {
        return hnd.getConfig(group, config, defaultVal);
    }
    
    public int getConfig(String group,String config,int defaultVal) throws Exception
    {
        return Integer.valueOf(hnd.getConfig(group, config, String.valueOf(defaultVal)));
    }
}
