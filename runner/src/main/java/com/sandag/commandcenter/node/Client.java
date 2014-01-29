package com.sandag.commandcenter.node;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Client
{
    @Autowired
    protected Server nodeServer;

    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    @PostConstruct
    public void init()
    {
        LOGGER.warn(String.format("Server says '%s'", nodeServer.sayHello("Client")));
    }

}
