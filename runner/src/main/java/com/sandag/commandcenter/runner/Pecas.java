package com.sandag.commandcenter.runner;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.Job.Model;

@Service
public class Pecas implements Runner
{   
    private static final Logger LOGGER = Logger.getLogger(Pecas.class.getName());

    @Override
    public Model supports()
    {
        return Model.PECAS;
    }
    
    // WARNING not yet tested - too many implementation questions
    @Override
    public void run(Job job)
    {
        LOGGER.debug("PECAS run started");
            
        ProcessBuilder builder = new ProcessBuilder("RunPECAS.py");
        // TODO put paths (base and model specific) in configuration
        builder.directory(new File("E:/Projects/Clients/SANDAG/PECAS Integration/Working Folder/" + job.getScenario()));
        try
        {
            Process process = builder.start();
            // TODO capture output, or parse logs afterwards?
            // TODO are there dependable exit values?
            /*int exitValue = */process.waitFor();
        } catch (IOException | InterruptedException e)
        {
            // TODO set failed status here? 
            LOGGER.warn("PECAS run failed", e);
        }
        LOGGER.debug("PECAS run finished");
    }
    
}
