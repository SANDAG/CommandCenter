package com.sandag.commandcenter.persistence;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;

@Service
public class JobService extends BaseService<Job, Integer>
{

    @Autowired
    private SequenceService sequenceService;
    
    
    public JobService()
    {
        super(Job.class);
    }

    @SuppressWarnings("unchecked")
    public List<Job> readAll()
    {
        return startQuery().addOrder(Order.asc("queuePosition")).list();
    }
    
    public Integer create(Job job)
    {
        job.setQueuePosition(sequenceService.next());
        return super.create(job);
    }

    
}
