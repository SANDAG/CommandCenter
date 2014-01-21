package com.sandag.commandcenter.persistence;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sandag.commandcenter.model.Job;

@Repository
public class JobDao extends BaseDao<Job, Integer>
{

    @Autowired
    private SequenceDao sequenceDao;

    public JobDao()
    {
        super(Job.class, Integer.class);
    }

    @SuppressWarnings("unchecked")
    public List<Job> readQueued()
    {
        return startQuery().add(Restrictions.eq("status", Job.Status.QUEUED)).addOrder(Order.asc("queuePosition")).list();
    }

    public boolean deleteIfQueued(Job job)
    {
        // refresh from db here to keep within transaction
        refresh(job);
        if (job.getStatus() != Job.Status.QUEUED)
        {
            return false;
        }
        delete(job);
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public List<Job> read(Job.Status... statuses)
    {
        return startQuery().add(Restrictions.in("status", statuses)).list();
    }
    
    public Integer create(Job job)
    {
        job.setQueuePosition(sequenceDao.next());
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
        return (Job) startQuery().add(Restrictions.eq("user", job.getUser())).add(Restrictions.eq("status", Job.Status.QUEUED))
                .add(Restrictions.gt("queuePosition", job.getQueuePosition())).addOrder(Order.asc("queuePosition")).setFirstResult(0)
                .setMaxResults(1).uniqueResult();
    }

    public Job getMoveableJobBefore(Job job)
    {
        // highest positioned job less than job param for same user
        return (Job) startQuery().add(Restrictions.eq("user", job.getUser())).add(Restrictions.eq("status", Job.Status.QUEUED))
                .add(Restrictions.lt("queuePosition", job.getQueuePosition())).addOrder(Order.desc("queuePosition")).setFirstResult(0)
                .setMaxResults(1).uniqueResult();
    }

    public Job startNextInQueue(String runnerName, Job.Model... model)
    {
        Job next = (Job) startQuery().add(Restrictions.in("model", model)).add(Restrictions.eq("status", Job.Status.QUEUED))
                .addOrder(Order.asc("queuePosition")).setFirstResult(0).setMaxResults(1).uniqueResult();
        if (next != null)
        {
            next.setStatus(Job.Status.RUNNING);
            next.setStarted(new Date());
            next.setRunner(runnerName);
            update(next);
        }
        return next;
    }

    public void updateAsFinished(Job job, boolean success)
    {
        job.setStatus(success ? Job.Status.FINISHED : Job.Status.FAILED);
        job.setFinished(new Date());
        update(job);
    }
}
