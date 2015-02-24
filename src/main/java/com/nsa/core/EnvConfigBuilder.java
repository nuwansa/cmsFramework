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
public class EnvConfigBuilder {
    private String hostName;
    private int port;
    private String ipAddr;
    private String zooConnString;
    private String envName;
    private String zone;
    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public EnvConfigBuilder setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     * @return 
     */
    public EnvConfigBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * @return the ipAddr
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * @param ipAddr the ipAddr to set
     */
    public EnvConfigBuilder setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    /**
     * @return the zooConnString
     */
    public String getZooConnString() {
        return zooConnString;
    }

    /**
     * @param zooConnString the zooConnString to set
     * @return 
     */
    public EnvConfigBuilder setZooConnString(String zooConnString) {
        this.zooConnString = zooConnString;
        return this;
    }

    /**
     * @return the envName
     */
    public String getEnvName() {
        return envName;
    }

    /**
     * @param envName the envName to set
     */
    public EnvConfigBuilder setEnvName(String envName) {
        this.envName = envName;
        return this;
    }
    
    public EnvConfig build()
    {
        return new EnvConfig(this);
    }

    public EnvConfigBuilder setZone(String zone)
    {
        this.zone = zone;
        return this;
    }
    
    String getZone() {
        return zone;
    }
}
