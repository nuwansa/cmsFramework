/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import com.nsa.core.msg.MachineStatus;
import com.nsa.core.zoo.IMachineListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NuwanThilini
 */
public class LocalMachineInstance extends MicroService implements IMachineInstance{

    private final IMachineListener listener;
    private MachineStatus lastStatus = null;
    public LocalMachineInstance(Environment env, String group, String name, int cap) throws Exception {
        super(env, group, name, cap);
        listener =  env.getZooWatchHandler().watchMachine(name, this);
    }

    @Override
    public void onMessasge(Object msg) throws Exception {
        if(msg instanceof MachineStatus)
        {
            onMachineStatus((MachineStatus)msg);
        }
    }

    @Override
    public String getHost() {
        return getName();
    }

    @Override
    public int getPort() {
        if(lastStatus != null)
            return lastStatus.getPort();
        return 0;
    }

    @Override
    public String getIpAddr() {
        if(lastStatus != null)
            return lastStatus.getIp();
        return null;
    }

    private void onMachineStatus(MachineStatus machineStatus) {
        switch(machineStatus.getStatus())
        {
            case DOWN:
            {
                close();
                getEnv().shutdown();
                break;
            }
        }
    }

    @Override
    public void close() {
        try {
            getEnv().getZooHandler().removeMachineWatch(getName(), listener);
        } catch (Exception ex) {
            Logger.getLogger(LocalMachineInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getZone() {
        if(lastStatus != null)
            return lastStatus.getZone();
        return null;
    }
    
}
