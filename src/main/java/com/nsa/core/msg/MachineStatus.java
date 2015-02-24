/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core.msg;

/**
 *
 * @author NuwanThilini
 */
public class MachineStatus{
    private final String host;
    private final String ip;
    private final int port;
    private final Status status;
    private final String zone;
    public MachineStatus(String zone,String host,String ip,int port,Status status)
    {
        this.zone = zone;
        this.host = host;
        this.ip = ip;
        this.port = port;
        this.status = status;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return the zone
     */
    public String getZone() {
        return zone;
    }
}
