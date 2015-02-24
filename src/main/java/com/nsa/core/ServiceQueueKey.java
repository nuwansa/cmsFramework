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
public class ServiceQueueKey implements IQueueComparable {

    private final String group;
    private final String name;

    public ServiceQueueKey(String group, String name) {
        this.group = group;
        this.name = name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IQueueComparable)) {
            return false;
        }

        IQueueComparable comp = (IQueueComparable) other;
        return group.equals(comp.getGroup()) && name.equals(comp.getName());
    }

    @Override
    public int hashCode() {
        return group.hashCode() ^ name.hashCode();
    }
}
