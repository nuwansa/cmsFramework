/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import com.nsa.core.msg.MachineStatus;
import com.nsa.core.zoo.IMachineListener;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 *
 * @author NuwanThilini
 */
public class RemoteMachineInstance extends MicroService implements IMachineInstance{

    private IMachineListener miListener;
    private MachineStatus lastStatus = null;
    NioSocketConnector connector = null;
    private Timer reconnectTimer = null;
    RemoteMachineInstance(Environment env, String group, String name, int cap) throws Exception {
        super(env, group, name, cap);
        miListener = env.getZooWatchHandler().watchMachine(getName(), this);
    }

    @Override
    public String getHost() {
        return getName();
    }

    @Override
    public int getPort() {
        if(lastStatus != null)
            return lastStatus.getPort();
        return 0;
    }

    @Override
    public String getIpAddr() {
        if(lastStatus != null)
            return lastStatus.getIp();
        return null;
    }

    @Override
    public void close() {
        closeSessions();
        if(connector != null)
        {
            connector.dispose(false);
            connector = null;
        }
        getEnv().getMachineLocator().removeMachineInstance(getHost());
        getEnv().getQueueService().closeQueue(getGroup(), getName());
        try {
            getEnv().getZooHandler().removeMachineWatch(getHost(), miListener);
        } catch (Exception ex) {
            Logger.getLogger(RemoteMachineInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onMessasge(Object msg) throws Exception {
        if(msg instanceof MachineStatus)
        {
            onMachineStatus((MachineStatus)msg);
        }
        else if(msg instanceof Timer)
        {
            onTimer((Timer)msg);
        }
    }

    private void onMachineStatus(MachineStatus msg) {
        switch(msg.getStatus())
        {
            case UP:
            {
                lastStatus = msg;
                reconnectTimer = scheduleOnce(5000, TimeUnit.SECONDS);
                break;
            }
            case DOWN:
            {
                lastStatus = null;
                break;
            }
        }
    }
    
    private void closeSessions()
    {
        if(connector != null)
        {
            for(IoSession session : connector.getManagedSessions().values())
            {
                session.close(true);
            }
        }
    }
    private void createConnection()
    {
        if(connector != null)
        {
            closeSessions();
        }
        else
        {
            connector = new NioSocketConnector();
            connector.setConnectTimeoutMillis(5000);
            connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
            connector.setHandler(new IORemoteHandler(getEnv(), this));
        }
        final RemoteMachineInstance me = this;
        ConnectFuture future = connector.connect(new InetSocketAddress(getHost(), getPort()));
        future.addListener(new IoFutureListener<ConnectFuture>() {

            @Override
            public void operationComplete(ConnectFuture future) {
                if(!future.isConnected())
                {
                    getEnv().getSchedular().schedule(me, reconnectTimer);
                }
            }
        });
    }

    @Override
    public String getZone() {
        if(lastStatus != null)
            return lastStatus.getZone();
        return null;
    }

    private void onTimer(Timer timer) {
        if(timer == reconnectTimer)
        {
            createConnection();
        }
    }
}
