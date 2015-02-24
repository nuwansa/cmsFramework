/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import com.netflix.curator.framework.recipes.cache.NodeCacheListener;
import com.nsa.core.msg.MachineStatus;
import com.nsa.core.msg.Status;
import com.nsa.core.zoo.IMachineListener;

/**
 *
 * @author NuwanThilini
 */
public class ZooWatchHandler {
    private final Environment env;
    
    public ZooWatchHandler(Environment env)
    {
        this.env = env;
    }
    
    public IMachineListener watchMachine(final String hostName,final MicroService service) throws Exception
    {
        IMachineListener miListener = new IMachineListener() {
            NodeCacheListener listener = null;
            @Override
            public void onMachineUp(String zone,String host, String ipAddr, int port) {
               env.getSchedular().schedule(service,new MachineStatus(zone,host, ipAddr, port, Status.UP));
            }

            @Override
            public void onMachineDown(String host) {
                     env.getSchedular().schedule(service,new MachineStatus(null,host, null, 0, Status.DOWN));
            }

            @Override
            public void attach(NodeCacheListener listener) {
                this.listener = listener;
            }

            @Override
            public NodeCacheListener getAttached() {
                return listener;
            }
        };
        env.getZooHandler().watchMachine(hostName, miListener);
        return miListener;
    }
}
