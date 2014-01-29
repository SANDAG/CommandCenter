package com.sandag.commandcenter.node;

public class ServerImpl implements Server
{

    @Override
    public String sayHello(String name)
    {
        return String.format("Hello " + name);
    }

}
