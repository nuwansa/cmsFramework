/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core.ex;

import java.text.MessageFormat;

/**
 *
 * @author NuwanThilini
 */
public class NetworkException extends Exception{
    public NetworkException(String format,Object... args)
    {
        super(MessageFormat.format(format, args));
    }
}
