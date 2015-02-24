/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsa.core;

import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author NuwanThilini
 */
public class Timer implements Runnable {

    private final Environment env;
    private String group;
    private String name;
    private ScheduledFuture<?> hnd;
    private boolean terminated = false;
    private boolean once = false;
    protected Timer(Environment env,String group,String name,boolean once) {
        this.env = env;
        this.group = group;
        this.name = name;
        this.once = once;
    }

    @Override
    public synchronized void run() {
        if(terminated)
            return;
        
        env.getSchedular().schedule(group, name,this);
        if(once)
        {
            terminated = true;
            if(hnd != null)
            {
                hnd.cancel(false);
                hnd = null;
            }
        }
    }

    synchronized void setHandler(ScheduledFuture<?> future) {
        if(terminated)
        {
            future.cancel(false);
            hnd = null;
        }
        else
            this.hnd = future;
    }
    
    public synchronized void cancel()
    {
        if(hnd != null)
            hnd.cancel(true);
        terminated = true;
        hnd = null;
    }

}
