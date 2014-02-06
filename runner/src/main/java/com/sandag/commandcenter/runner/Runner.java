package com.sandag.commandcenter.runner;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.Job.Model;

public class Runner
{
    @Autowired
    protected ProcessMap processMap;

    private static final Logger LOGGER = Logger.getLogger(Runner.class.getName());

    private Model model;
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

    public boolean run(Job job)
    {
        // success means not failure - exit value not set consistently (expect false positives)
        boolean success = false;
        File scenarioDir = new File(job.getScenarioLocation());
        LOGGER.debug(String.format("'%s' run started in '%s'", model.name(), scenarioDir.getPath()));
        processBuilder.directory(scenarioDir);
        try
        {
            Process process = processBuilder.start();
            processMap.put(job, process);
            int exitValue = process.waitFor();
            success = exitValue == 0;

        } catch (IOException | InterruptedException e)
        {
            LOGGER.warn(model.name() + " run failed", e);
        } finally
        {
            processMap.remove(job);
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

    public void setCommandLine(String commandLine)
    {
        this.commandLine = commandLine;
    }
}
