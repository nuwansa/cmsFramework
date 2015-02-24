/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author NuwanThilini
 */
public class ServiceQueue  implements IQueueComparable{
    private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();
    private final String group;
    private final String name;
    private AtomicBoolean status = new AtomicBoolean(false);
    private MicroService service;
    private AtomicInteger size = new AtomicInteger(0);
    public ServiceQueue(String group,String name,MicroService serivce)
    {
        this.group = group;
        this.name = name;
        this.service = serivce;
    }
    
    public void add(Object message)
    {
        queue.add(message);
        size.incrementAndGet();
    }
    
    public boolean setBusy(boolean busy)
    {
        return status.compareAndSet(!status.get(), busy);
    }
    
    public Object poll()
    {
        Object data =  queue.poll();
        if(data != null)
            size.decrementAndGet();
        return data;
    }
    
    @Override
    public String getGroup()
    {
        return group;
    }
    
    public int getSize()
    {
        return size.get();
    }
    @Override
    public String getName()
    {
        return name;
    }
    
    public MicroService getService()
    {
        return service;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof IQueueComparable))
            return false;
            
        IQueueComparable  comp = (IQueueComparable)other;
        return group.equals(comp.getGroup()) && name.equals(comp.getName());
    }
    
    @Override
    public int hashCode()
    {
        return group.hashCode() ^ name.hashCode();
    }
    
    
}
