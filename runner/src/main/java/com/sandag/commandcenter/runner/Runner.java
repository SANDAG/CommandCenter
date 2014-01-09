package com.sandag.commandcenter.runner;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job.Model;

@Service
public class Runner
{   
    private static final Logger LOGGER = Logger.getLogger(Runner.class.getName());
    private Model model;
    private ProcessBuilderWrapper processBuilder;
    private String workingDir;
    
    public void initialize() 
    {
        processBuilder.directory(new File(workingDir));
    }
    
    public Model supports()
    {
        return model;
    }
    
    public boolean run()
    {
        // success means not failure - exit value not set consistently (expect false positives)
        boolean success = false;
        LOGGER.debug(model.name() + " run started");
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
    
}
