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
public interface IMachineInstance {

    /**
     * @return the host
     */
    public String getHost();

    /**
     * @return the port
     */
    public int getPort();

    /**
     * @return the ipAddr
     */
    public String getIpAddr();

    public String getZone();
    public void close();
}
