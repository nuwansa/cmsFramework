/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core.zoo;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.cache.ChildData;
import com.netflix.curator.framework.recipes.cache.NodeCache;
import com.netflix.curator.framework.recipes.cache.NodeCacheListener;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.netflix.curator.utils.EnsurePath;
import com.netflix.curator.utils.ZKPaths;
import com.nsa.utils.CommonUtils;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.zookeeper.CreateMode;

/**
 *
 * @author NuwanThilini
 */
public class ZooHandler {
    private CuratorFramework client = null;
    private  String env;
    private Map<String,NodeCache> mapNodeCache =  new ConcurrentHashMap<>();
    private static final String STR_MACHINES_NODE = "MINode";
    public static ZooHandler create(String connectionString,String env) throws Exception
    {
        ZooHandler hnd = new ZooHandler();
        hnd.init(connectionString, env);
        return hnd;
    }
    
    private void init(String connString,String env) throws Exception
    {
        client = CuratorFrameworkFactory.builder().namespace(null).connectString(connString).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        client.start();
        this.env = "/"+env;
        check();
    }
    
    
    private void check()throws Exception
    {
        if(client.checkExists().forPath(env) == null)
            throw  new Exception("env "+env + "not found");
    }
    public void lockMachine(String zone,String hostname,String ipAddr,int port,String startupconfigs) throws Exception
    {
        ensure(STR_MACHINES_NODE);
        createNode(CreateMode.EPHEMERAL, STR_MACHINES_NODE,hostname , 
                ConfigHelper.create().put("hostname", hostname)
                                     .put("zone",zone)
                                     .put("ip",ipAddr)
                                     .put("port", port)
                                     .put("startConfig", startupconfigs).get()
        );       
    }
    
    
    public void watchMachine(final String hostName,final IMachineListener listener) throws Exception
    {
        if(listener.getAttached() != null)
            throw  new Exception("listener already registered");
        
        final NodeCache cache = getNodeCache(ZKPaths.makePath(createPath(STR_MACHINES_NODE),hostName));
        NodeCacheListener cacheListener = new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                ChildData data = cache.getCurrentData();
                if(data == null)
                    listener.onMachineDown(hostName);
                else
                {
                   String strData = new String(data.getData(),"UTF-8");
                    List<String> vals = getConfigs(strData, "zone","ip","port");
                    listener.onMachineUp(vals.get(0),hostName, vals.get(1), Integer.valueOf(vals.get(2)));
                }
            }
        };
        listener.attach(cacheListener);
        cache.getListenable().addListener(cacheListener);
    }
    
    
    public void removeMachineWatch(String hostName,IMachineListener listener) throws Exception
    {
        if(listener.getAttached() == null)
            return;
        
        final NodeCache cache = getNodeCache(ZKPaths.makePath(createPath(STR_MACHINES_NODE),hostName));
        cache.getListenable().removeListener(listener.getAttached());
        listener.attach(null);
        tryRemoveNode(ZKPaths.makePath(createPath(STR_MACHINES_NODE),hostName), cache);
    }
    
    
    public void lockService(String serviceGroup,String service,String hostname,String ipAddr,int port) throws Exception
    {
        ensure(serviceGroup);
        createNode(CreateMode.EPHEMERAL, serviceGroup,service , 
                ConfigHelper.create().put("hostname", hostname)
                                     .put("ip",ipAddr)
                                     .put("port", port).get()
        );       
    }
    
    public void watchService(final String group,final String name,final IServiceListener listener) throws Exception
    {
         if(listener.getAttached() != null)
            throw  new Exception("listener already registered");
         
        final NodeCache cache = getNodeCache(ZKPaths.makePath(createPath(group), name));
        NodeCacheListener cacheListener = new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                ChildData data = cache.getCurrentData();
                if(data == null)
                    listener.onServiceDown(group, name);
                else
                {
                    String strData = new String(data.getData(),"UTF-8");
                    List<String> vals = getConfigs(strData, "hostname","ip","port");
                    listener.onServiceUp(group, name, vals.get(0), vals.get(1), Integer.valueOf(vals.get(2)));
                }
            }
        };
        listener.attach(cacheListener);
        cache.getListenable().addListener(cacheListener);
    }
    
    public void removeServiceWatch(String group,String name,IServiceListener listener) throws Exception
    {
        if(listener.getAttached() == null)
            return;
        
         final NodeCache cache = getNodeCache(ZKPaths.makePath(createPath(group), name));
         cache.getListenable().removeListener(listener.getAttached());
         listener.attach(null);
         tryRemoveNode(ZKPaths.makePath(createPath(group), name),cache);
    }
    
    public  String getConfig(String group,String config,String defaultVal) throws Exception
    {
        if(!isPathExist(ZKPaths.makePath("configs",group), config))
            return defaultVal;
        String data = getNodeData(ZKPaths.makePath("configs",group), config);
        List<String> configs = getConfigs(data, "value");
        if(configs.get(0).equals(CommonUtils.nullString))
            return defaultVal;
        return configs.get(0);
    }
    
    public void setConfig(String group,String config,String value) throws Exception
    {
        ensure(ZKPaths.makePath("configs",group));
        createNode(CreateMode.PERSISTENT, ZKPaths.makePath("configs",group), config,
                                          ConfigHelper.create().put("value", value).get()
        );
    }
   
    private String createPath(String path)
    {
        return ZKPaths.makePath(env, path);
    }
    
    private String makePath(String path,String node)
    {
        return ZKPaths.makePath(createPath(path), node);
    }
    
    private void ensure(String path) throws Exception
    {
        EnsurePath ensure = new EnsurePath(createPath(path));
        ensure.ensure(client.getZookeeperClient());
    }
    
    public void createNode(CreateMode mode,String path,String node,Map<String,String> configs) throws Exception
    {
        client.create().withMode(mode).forPath(makePath(path, node),serializeMap(configs).getBytes());
    }
    
    private boolean isPathExist(String path,String node) throws Exception
    {
        return client.checkExists().forPath(ZKPaths.makePath(createPath(path),node)) != null;
    }
    private String serializeMap(Map<String,String> mapData)
    {
        if(mapData == null)
            return "";
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : mapData.entrySet())
        {
            builder.append(MessageFormat.format("{0}#{1}-", entry.getKey(),entry.getValue()));
        }
        return builder.toString();
    }
    
    public Map<String,String> getConfigs(String data)
    {
        StringTokenizer tokenizer = new StringTokenizer(data,"-");
        Map<String,String> map = new HashMap<>();
       while(tokenizer.hasMoreTokens())
       {
           String pair = tokenizer.nextToken();
           StringTokenizer subTokenizer = new StringTokenizer(pair,"#");
           map.put(subTokenizer.nextToken(), subTokenizer.nextToken());
       }
       return map;
    }
    
    private List<String> getConfigs(String data,String... configs)
    {
        List<String> array = new LinkedList<>();
        Map<String,String> map = getConfigs(data);
        for(String config :configs)
        {
            String val = map.get(config);
            if(val == null)
                array.add(CommonUtils.nullString);
            else
                array.add(val);
        }
        return array;
    }
    
    private String getNodeData(String path,String node) throws Exception
    {
        String data = new String(client.getData().forPath(ZKPaths.makePath(createPath(path), node)),"UTF-8");
        return data;
    }
    
    
    private synchronized NodeCache getNodeCache(String path) throws Exception
    {
        NodeCache cache = mapNodeCache.get(path);
        if(cache == null)
        {
                cache = mapNodeCache.get(path);
                if(cache == null)
                {
                    cache = new NodeCache(client, path);
                    cache.start();
                    mapNodeCache.put(path, cache);
                }
            
        }
        return cache;
    }
    
    private synchronized void tryRemoveNode(String path,NodeCache cache) throws IOException
    {
        if(cache.getListenable().size() != 0)
            return;
        cache.close();
        mapNodeCache.remove(path);
    }
}
