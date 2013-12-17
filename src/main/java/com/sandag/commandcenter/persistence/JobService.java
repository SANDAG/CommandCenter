package com.sandag.commandcenter.persistence;

import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;

@Service
public class JobService extends BaseService<Job, Integer>
{
    public JobService()
    {
        super(Job.class);
    }
    
    
    
}
