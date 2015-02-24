/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core.zoo;

import com.netflix.curator.framework.recipes.cache.NodeCacheListener;

/**
 *
 * @author NuwanThilini
 */
public interface ZooListener {
    public void attach(NodeCacheListener listener);
    public NodeCacheListener getAttached();
}
