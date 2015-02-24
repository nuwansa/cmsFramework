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
public interface IServiceListener extends ZooListener{
    public void onServiceUp(String group,String name,String host,String ipAddr,int port);
    public void onServiceDown(String group,String name);
    
}
