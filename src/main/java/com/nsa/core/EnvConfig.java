/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

/**
 *
 * @author NuwanThilini
 */
public class EnvConfig {
    private final String hostName;
    private final int port;
    private final String ipAddr;
    private final String zooConnString;
    private final String envName;
    private final String zone;
    EnvConfig(EnvConfigBuilder builder)
    {
        this.envName = builder.getEnvName();
        this.hostName = builder.getHostName();
        this.port = builder.getPort();
        this.ipAddr = builder.getIpAddr();
        this.zooConnString = builder.getZooConnString();
        this.zone = builder.getZone();
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the ipAddr
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * @return the zooConnString
     */
    public String getZooConnString() {
        return zooConnString;
    }

    /**
     * @return the envName
     */
    public String getEnvName() {
        return envName;
    }
    
    public String getZone()
    {
        if(zone == null)
            return "Default";
        return zone;
    }
}
