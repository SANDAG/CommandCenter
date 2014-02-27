package com.sandag.commandcenter.runner;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.Job.Model;
import com.sandag.commandcenter.notification.RunNotifier;
import com.sandag.commandcenter.persistence.ClusterDao;
import com.sandag.commandcenter.persistence.JobDao;

@Service
public class Handler
{
    @Autowired
    protected JobDao jobDao;

    @Autowired
    protected ClusterDao clusterDao;

    @Autowired
    protected ServiceNameRetriever serviceNameRetriever;

    @Autowired
    protected RunNotifier runNotifier;

    @Autowired
    protected Properties runnerProperties;

    @Autowired
    protected RunnerFactory runnerFactory;
    
    private static final Logger LOGGER = Logger.getLogger(Handler.class.getName());

    protected Map<Job.Model, Runner> runners = new HashMap<Job.Model, Runner>();
    protected Job.Model[] supportedModels;
    protected String serviceName;

    protected boolean initialized = false;

    @PostConstruct
    public void initialize()
    {
        for (Model model : Job.Model.values())
        {
            String commandLine = runnerProperties.getProperty(model.toString() + "_cmdLine");
            if (commandLine != null)
            {
                runners.put(model, runnerFactory.getRunner(commandLine, model));
            } else
            {
                LOGGER.warn("No command line defined for model " + model);
            }
        }

        supportedModels = runners.keySet().toArray(new Job.Model[0]);
        serviceName = serviceNameRetriever.retrieve();
        initialized = true;
    }

    @Scheduled(fixedDelayString = "${runDelayMs}")
    public void runNext()
    {
        if (!initialized)
        {
            return;
        }
        if (!clusterDao.isActive(serviceName))
        {
            LOGGER.info(String.format("Not running - no active cluster with name '%s'.", serviceName));
            return;
        }

        LOGGER.debug("runNext started");
        Job next = jobDao.startNextInQueue(serviceName, supportedModels);
        if (next != null)
        {
            runNotifier.sendStartedMessage(next);
            Runner runner = runners.get(next.getModel());
            boolean success = runner.run(next);
            jobDao.updateAsFinished(next, success);
            runNotifier.sendFinishedMessage(next);
        }
        LOGGER.debug("runNext finished");
    }

}
