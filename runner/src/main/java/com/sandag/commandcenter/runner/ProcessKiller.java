package com.sandag.commandcenter.runner;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.JobDao;

@Service
public class ProcessKiller
{
    @Autowired
    protected JobDao jobDao;
    
    @Autowired
    protected ProcessMap processMap;
    
    @Autowired
    protected ServiceNameRetriever serviceNameRetriever;
    
    protected String serviceName;

    @PostConstruct
    public void initialize()
    {
        serviceName = serviceNameRetriever.retrieve();
    }
    
    @Scheduled(fixedDelayString = "${killDelayMs}")
    public void killCancelledJobProcesses()
    {
        List<Job> cancelledJobs = jobDao.readCancelled(serviceName);
        for (Job job : cancelledJobs)
        {
            Process process = processMap.get(job);
            if (process != null) // just finished and removed?
            {
                process.destroy();
            }
        }
    }
}
