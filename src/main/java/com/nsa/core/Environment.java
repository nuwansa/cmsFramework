/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsa.core;

import com.netflix.curator.test.TestingServer;
import com.nsa.core.zoo.ZooHandler;
import org.apache.zookeeper.CreateMode;

/**
 *
 * @author NuwanThilini
 */
public class Environment {

    private ZooHandler zooHandler = null;
    
    private final EnvConfig config;
    private final ConfigHandler configHandler;
    private final QueueService queueService;
    private final MicroServiceSchedular micSched;
    private final EventSchedular eventSchedular;
    private final ZooWatchHandler zooWatchHandler;
    private final MachineLocator machineLocator;
    private Environment(EnvConfig envConfig) throws Exception {
        this.config = envConfig;
        zooHandler = ZooHandler.create(envConfig.getZooConnString(), envConfig.getEnvName());
        zooHandler.lockMachine(envConfig.getZone(),envConfig.getHostName(), envConfig.getIpAddr(), envConfig.getPort(), "");
        configHandler = new ConfigHandler(zooHandler);
        queueService= new QueueService();
        micSched = new MicroServiceSchedular(this);
        eventSchedular = new EventSchedular(this);
        zooWatchHandler = new ZooWatchHandler(this);
        machineLocator = new MachineLocator(this);
   }

    public static Environment attachToEnvironment(EnvConfig config) throws Exception {
            Environment env =  new Environment(config);
            env.run();
            return env;
    }
    
    public static void createEnvironment(EnvConfig config) throws Exception
    {
        TestingServer server = new TestingServer(config.getPort());
        ZooHandler handler = ZooHandler.create(server.getConnectString(), "");
        handler.createNode(CreateMode.PERSISTENT, "", config.getEnvName(),null);
        
    }
    
    public EnvConfig getConfig()
    {
        return config;
    }
    
    public ConfigHandler getConfigHandler()
    {
        return configHandler;
    }
    
    public QueueService getQueueService()
    {
        return queueService;
    }
    
    public MicroServiceSchedular getSchedular()
    {
        return micSched;
    }
 
    public EventSchedular getEventSchedular()
    {
        return eventSchedular;
    }
    
    
    public ZooHandler getZooHandler()
    {
        return zooHandler;
    }
    
    public ZooWatchHandler getZooWatchHandler()
    {
        return zooWatchHandler;
    }

    public MachineLocator getMachineLocator()
    {
        return machineLocator;
    }
    
    void shutdown() {
        //TODO publish shutdown event to schedular and wait
    }
    
    
    void run()
    {
        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                getSchedular().run();
            }
        };
        t.start();
    }
}
