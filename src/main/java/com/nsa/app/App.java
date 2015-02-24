/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.app;

import com.nsa.core.Environment;
import com.nsa.core.EnvConfigBuilder;

/**
 *
 * @author NuwanThilini
 */
public class App {
    public static void main(String[] args) throws Exception
    {
        Environment.createEnvironment(new EnvConfigBuilder()
                                                            .setEnvName("TestEnv")
                                                            .setPort(7777).build()
        );
        
        Environment env = Environment.attachToEnvironment(new EnvConfigBuilder()
                                            .setEnvName("TestEnv")
                                            .setHostName("TestHost")
                                            .setIpAddr("127.0.0.1")
                                            .setZooConnString("localhost:7777")
                                            .setPort(12345).build());
    }
}
