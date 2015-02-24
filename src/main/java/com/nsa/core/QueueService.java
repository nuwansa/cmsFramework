/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import com.nsa.core.ex.QueueException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author NuwanThilini
 */
public class QueueService {
    private Map<Object,ServiceQueue> map = new ConcurrentHashMap<>();
    
    public synchronized ServiceQueue createQueue(String group,String name,int capacity,MicroService service)throws Exception
    {
        ServiceQueueKey key = new ServiceQueueKey(group, name);
        ServiceQueue queue = map.get(key);
        if(queue != null)
            throw new QueueException("queue {0}-{1} exist",group,name);
        queue = new ServiceQueue(group, name,service);
        map.put(key, queue);
        return queue;
    }
    
    public synchronized void closeQueue(String group,String name)
    {
        ServiceQueueKey key = new ServiceQueueKey(group, name);
        map.remove(key);
    }
    
    public ServiceQueue getQueue(String group,String name)
    {
        ServiceQueueKey key = new ServiceQueueKey(group, name);
        ServiceQueue queue = map.get(key);
        return queue;
    }
    
    public ServiceQueue getQueue(Object key)
    {
        ServiceQueue queue = map.get(key);
        return queue;
    }
   
}
