package com.sandag.commandcenter.node;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Host
{
    private Host()
    {
    }

    @SuppressWarnings("resource")
    // closing the resource will stop the service...
    public static void main(String[] args)
    {
        new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("Waiting for requests");
    }
}
