package net.xdclass.xdclassshiro.config;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 *  自定义sessionId
 */
public class CustomSessionIdGenerator implements SessionIdGenerator {
    @Override
    public Serializable generateId(Session session) {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
