/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsa.core.msg;

import com.nsa.core.IQueueComparable;
import java.io.Serializable;

/**
 *
 * @author NuwanThilini
 */
 public  class MICRouter implements IQueueComparable,Serializable {

        private final String group;
        private final String name;
        private final Object msg;

        public MICRouter(String group, String name, Object msg) {
            this.group = group;
            this.name = name;
            this.msg = msg;
        }

        /**
         * @return the group
         */
        @Override
        public String getGroup() {
            return group;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the msg
         */
        public Object getMsg() {
            return msg;
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