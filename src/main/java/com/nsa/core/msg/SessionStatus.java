/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core.msg;

import org.apache.mina.core.session.IoSession;

/**
 *
 * @author NuwanThilini
 */
public class SessionStatus {
    private final Action action ;
    private final IoSession session;
    public SessionStatus(IoSession session,Action action)
    {
        this.action = action;
        this.session = session;
    }
    
    public Action getAction()
    {
        return action;
    }
    
    public IoSession getSession()
    {
        return session;
    }
    
    
}
