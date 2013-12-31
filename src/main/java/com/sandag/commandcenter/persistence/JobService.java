package com.sandag.commandcenter.persistence;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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

    public void updateWithSwappedQueuePositions(Job jobA, Job jobB)
    {
        int posA = jobA.getQueuePosition();
        jobA.setQueuePosition(jobB.getQueuePosition());
        jobB.setQueuePosition(posA);

        update(jobA);
        update(jobB);
    }
    
    public Job getMoveableJobAfter(Job job)
    {
        // lowest positioned job greater than job param for same user
        return (Job) startQuery().add(Restrictions.eq("user", job.getUser())).add(Restrictions.gt("queuePosition", job.getQueuePosition()))
                .addOrder(Order.asc("queuePosition")).setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    public Job getMoveableJobBefore(Job job)
    {
        // highest positioned job less than job param for same user
        return (Job) startQuery().add(Restrictions.eq("user", job.getUser())).add(Restrictions.lt("queuePosition", job.getQueuePosition()))
                .addOrder(Order.desc("queuePosition")).setFirstResult(0).setMaxResults(1).uniqueResult();
    }
}
