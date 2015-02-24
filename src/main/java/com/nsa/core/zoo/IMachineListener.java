/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core.zoo;

/**
 *
 * @author NuwanThilini
 */
public interface IMachineListener extends ZooListener{
    public void onMachineUp(String zone,String host,String ipAddr,int port);
    public void onMachineDown(String host);
}
