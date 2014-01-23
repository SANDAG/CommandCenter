package com.sandag.commandcenter.runner;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sandag.commandcenter.model.Job;

@Component
public class ProcessMap
{
    protected Map<Integer, Process> map = new HashMap<Integer, Process>();
    
    public void put(Job job, Process process)
    {
        map.put(job.getId(), process);
    }
 
    public Process get(Job job)
    {
        return map.get(job.getId());
    }
    
    public void remove(Job job)
    {
        map.remove(job.getId());
    }
    
}
