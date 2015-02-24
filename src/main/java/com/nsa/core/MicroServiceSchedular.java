/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsa.core;

import com.nsa.core.msg.MICRouter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NuwanThilini
 */
public class MicroServiceSchedular {

    private final ExecutorService service;
    private final CompletionService<ServiceQueue> completion;
    public static final String SCHED_THRD_COUNT = "schedThrdCount";
    private final Environment env;
    private final BlockingQueue<MICRouter> schedQueue;

    public MicroServiceSchedular(Environment env) throws Exception {
        this.env = env;
        service = Executors.newFixedThreadPool(env.getConfigHandler().getConfig(ConfigHandler.GLOBAL, SCHED_THRD_COUNT, 10));
        schedQueue = new LinkedBlockingQueue<>();
        completion = new ExecutorCompletionService<>(service);
    }

    public void schedule(String group, String name, Object msg) {
        schedule(new MICRouter(group, name, msg));
    }

    public void schedule(MICRouter router)
    {
        schedQueue.add(router);
    }
    
    public void schedule(IMicroServiceDescriptor descriptor,Object msg)
    {
        schedule(descriptor.getGroup(), descriptor.getName(), msg);
    }
    
    public void run() {
        while (true) {
            try {
                MICRouter router = schedQueue.take();
                ServiceQueue queue = env.getQueueService().getQueue(router);
                if (queue != null) {
                    queue.add(router.getMsg());
                    if(queue.setBusy(true))
                    {
                        completion.submit(new QueueRunner(queue),queue);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MicroServiceSchedular.class.getName()).log(Level.SEVERE, null, ex);
            }
            checkCompletion();
        }
    }

    private void checkCompletion()
    {
        Future<ServiceQueue> future ;
        while((future = completion.poll()) != null)
        {
            try {
                ServiceQueue queue = future.get();
                queue.setBusy(false);
                if(queue.getSize() > 0)
                {
                    queue.setBusy(true);
                    completion.submit(new QueueRunner(queue),queue);
                }
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(MicroServiceSchedular.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
   
    
    public class QueueRunner implements Runnable
    {
        private ServiceQueue queue;
        public QueueRunner(ServiceQueue queue)
        {
            this.queue = queue;
        }
        @Override
        public void run() {
            int i = 0;
            while(i < 10)
            {
                Object msg = queue.poll();
                if(msg != null)
                    try {
                        queue.getService().onMessasge(msg);
                } catch (Exception ex) {
                    Logger.getLogger(MicroServiceSchedular.class.getName()).log(Level.SEVERE, null, ex);
                }
                else
                    break;
                i++;
            }
        }
        
    }
}
