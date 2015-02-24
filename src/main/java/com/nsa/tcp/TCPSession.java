/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.tcp;

import com.nsa.core.ex.NetworkException;
import java.io.Serializable;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author NuwanThilini
 */
public class TCPSession {
    IoSession session;
    public TCPSession(IoSession session)
    {
        this.session = session;
    }
    
    public void write(Object msg) throws Exception
    {
        if(!(msg instanceof Serializable))
            throw new NetworkException("message {0} not serializable",msg.getClass().getName());
        
        if(session != null)
            session.write(msg);
        throw new NetworkException("session not found");
    }
}
