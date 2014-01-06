package com.sandag.commandcenter.runner;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

@Component
public class ServiceNameRetriever
{

    public String retrieve() throws UnknownHostException 
    {
        // TODO may want to explicitly configure this to prevent confusion (but adds setup)
        return InetAddress.getLocalHost().getHostName();
    }
}
