package com.sandag.commandcenter.runner;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

public class ServiceNameRetrieverTest
{

    @Test
    public void retrieveWorks() throws UnknownHostException
    {
        ServiceNameRetriever retriever = new ServiceNameRetriever();
        assertEquals(InetAddress.getLocalHost().getHostName(), retriever.retrieve());
    }
}
