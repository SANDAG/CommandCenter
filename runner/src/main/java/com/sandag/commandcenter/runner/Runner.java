package com.sandag.commandcenter.runner;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;

import com.sandag.commandcenter.model.Job.Model;

public class Runner
{   
    private static final Logger LOGGER = Logger.getLogger(Runner.class.getName());
    
    private Model model;
    private String workingDir;
    private String commandLine;
    
    protected ProcessBuilderWrapper processBuilder = new ProcessBuilderWrapper();

    @PostConstruct
    public void initialize() 
    {
        processBuilder.command(commandLine);
    }
    
    public Model supports()
    {
        return model;
    }
    
    public boolean run(String scenarioFolder)
    {
        // success means not failure - exit value not set consistently (expect false positives)
        boolean success = false;
        LOGGER.debug(model.name() + " run started");
        processBuilder.directory(new File(workingDir + File.separatorChar + scenarioFolder));
        try
        {
            Process process = processBuilder.start();
            int exitValue = process.waitFor();
            success = exitValue == 0;
        } catch (IOException | InterruptedException e)
        {
            LOGGER.warn(model.name() + " run failed", e);
        }
        LOGGER.debug(model.name() + " run finished");
        return success;
    }
    
    public void setModel(Model model)
    {
        this.model = model;
    }

    public void setProcessBuilder(ProcessBuilderWrapper processBuilder)
    {
        this.processBuilder = processBuilder;
    }

    public void setWorkingDir(String workingDir)
    {
        this.workingDir = workingDir;
    }
    
    public void setCommandLine(String commandLine)
    {
        this.commandLine = commandLine;
    }
}
