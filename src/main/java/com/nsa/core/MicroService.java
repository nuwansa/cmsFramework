/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author NuwanThilini
 */
public abstract class  MicroService implements IMicroServiceDescriptor{
    private final Environment env;
    private final String group;
    private final String name;
    protected MicroService(Environment env,String group,String name,int cap) throws Exception
    {
        this.env = env;
        this.group = group;
        this.name = name;
        env.getQueueService().createQueue(group, name, cap, this);
    }
    
    public abstract void onMessasge(Object msg) throws Exception;
    
    @Override
    public final String getGroup()
    {
        return group;
    }
    
    @Override
    public final String getName()
    {
        return name;
    }
    
    public Timer scheduleOnce(long period,TimeUnit unit)
    {
        return env.getEventSchedular().scheduleOnce(group, name, period, unit);
    }
    
    public Timer schedule(long period,TimeUnit unit)
    {
        return env.getEventSchedular().schedule(group, name, period, unit);
    }
    
    public Environment getEnv()
    {
        return env;
    }
}
