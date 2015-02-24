/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core;

import com.nsa.core.msg.Action;
import com.nsa.core.msg.MICRouter;
import com.nsa.core.msg.SessionStatus;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author NuwanThilini
 */
public class IORemoteHandler  implements IoHandler{

    private RemoteMachineInstance rmi;
    private Environment env;
    
    public IORemoteHandler(Environment env,RemoteMachineInstance rmi)
    {
        this.env = env;
        this.rmi = rmi;
    }
    
    @Override
    public void sessionCreated(IoSession session) throws Exception {
 
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        env.getSchedular().schedule(rmi, new SessionStatus(session,Action.CONNECTED));
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        env.getSchedular().schedule(rmi, new SessionStatus(session,Action.DISCONNECTED));
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
  
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        env.getSchedular().schedule(rmi, new SessionStatus(session,Action.DISCONNECTED));
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if(message  instanceof MICRouter)
        {
            env.getSchedular().schedule((MICRouter)message);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
 
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
  
    }
    
}
