/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author NuwanThilini
 */
public class MachineLocator {
    private Map<String,IMachineInstance> mapInstances = new HashMap<>();
    private Environment env;
    
    public MachineLocator(Environment env)
    {
        this.env = env;
    }
    
    public synchronized  IMachineInstance getMachineInstance(String hostname) throws Exception
    {
        IMachineInstance instance = mapInstances.get(hostname);
        if(instance == null)
        {
            if(env.getConfig().getHostName().equals(hostname))
                instance = new LocalMachineInstance(env, "mi", hostname, 100);
            else
                instance = new RemoteMachineInstance(env,"mi",hostname,100);
            
            mapInstances.put(hostname, instance);
        }
        return instance;
    }

    synchronized void removeMachineInstance(String hostname) {
        mapInstances.remove(hostname);
      
    }
}
