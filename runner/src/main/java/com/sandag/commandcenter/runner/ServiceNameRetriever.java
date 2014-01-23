package com.sandag.commandcenter.runner;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

@Component
public class ServiceNameRetriever
{

    public String retrieve()
    {
        // TODO may want to explicitly configure this to prevent confusion (but adds setup)
        try
        {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e)
        {
            throw new RuntimeException(e);
        }
    }
}
