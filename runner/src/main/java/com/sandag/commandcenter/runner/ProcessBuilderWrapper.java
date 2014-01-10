package com.sandag.commandcenter.runner;

import java.io.File;
import java.io.IOException;

// ProcessBuilder is final, wrapping that for easier testing (can not mock final classes)
public class ProcessBuilderWrapper
{
    protected ProcessBuilder processBuilder;

    public void command(String command)
    {
        processBuilder = new ProcessBuilder(command.split(" "));
    }

    public void directory(File file)
    {
        processBuilder.directory(file);
    }

    public Process start() throws IOException
    {
        return processBuilder.start();
    }

}
