/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.concurrent.ScheduledService;

/**
 *
 * @author NuwanThilini
 */
public class EventSchedular {
    private final ScheduledExecutorService service;
    private Environment env;
    public static final String EVT_SCHED_THRD_COUNT = "evtSchedThrdCount";
    public EventSchedular(Environment env) throws Exception
    {
        this.env = env;
        service = Executors.newScheduledThreadPool(env.getConfigHandler().getConfig(ConfigHandler.GLOBAL, EVT_SCHED_THRD_COUNT, 5));
    }
    
    public Timer schedule(String group,String name,long period,TimeUnit unit)
    {
        return _schedule(group, name, period, unit, false);
    }
    
    public Timer scheduleOnce(String group,String name,long period,TimeUnit unit)
    {
        return _schedule(group, name, period, unit, true);
    }
    
    private Timer _schedule(String group,String name,long period,TimeUnit unit,boolean once)
    {
        Timer timer = new Timer(env, group, name,once);
        ScheduledFuture<?> future = service.scheduleAtFixedRate(timer, 0,period,unit);
        timer.setHandler(future);
        return timer;
    }
    
}
