package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.Job.Model;
import com.sandag.commandcenter.model.User;

import static com.sandag.commandcenter.model.Job.Status.QUEUED;
import static com.sandag.commandcenter.model.Job.Status.RUNNING;
import static com.sandag.commandcenter.model.Job.Status.COMPLETE;
import static com.sandag.commandcenter.model.Job.Status.ARCHIVED;
import static com.sandag.commandcenter.model.Job.Status.DELETED;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/db.xml", "classpath:/autowire.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class JobDaoTest
{
    @Autowired
    private JobDao service;

    @Autowired
    private UserDao userDao;

    @After
    public void cleanUp()
    {
        service.getSession().createQuery("DELETE FROM Job").executeUpdate();
    }

    @Test
    public void queuedResultsReadInOrder()
    {
        int numResults = 5;
        User user = new User();
        userDao.create(user);
        for (int i = 0; i < numResults; i++)
        {
            createJobs(user, QUEUED);

        }
        createJobs(user, RUNNING, COMPLETE, ARCHIVED, DELETED); // do not get read
        List<Job> results = service.readQueued();
        assertEquals(numResults, results.size());
        int lastPosition = -1;
        for (Job job : results)
        {
            int position = job.getQueuePosition();
            assertTrue(position > lastPosition);
            assertEquals(QUEUED, job.getStatus());
            lastPosition = position;
        }
    }

    @Test
    public void resultsWrittenInOrder()
    {
        int numResults = 5;
        int last = -1;
        for (int i = 0; i < numResults; i++)
        {
            Job job = getJobWithAUser();
            service.create(job);
            int current = job.getQueuePosition();
            assertTrue(current > last);
            last = current;
        }
    }

    @Test
    public void readByStatus()
    {
        int numCopies = 2;
        Job.Status[] statuses = new Job.Status[] {RUNNING, DELETED };
        User user = new User();
        userDao.create(user);
        for (int i = 0; i < numCopies; i++)
        {
            createJobs(user, QUEUED, RUNNING, COMPLETE, ARCHIVED, DELETED);
        }
        List<Job> results = service.read(statuses);
        assertEquals(numCopies * statuses.length, results.size());
        Map<Job.Status, Integer> countByStatus = new HashMap<Job.Status, Integer>();
        for (Job job : results)
        {
            if (countByStatus.containsKey(job.getStatus()))
            {
                countByStatus.put(job.getStatus(), countByStatus.get(job.getStatus()) + 1);
            } else
            {
                countByStatus.put(job.getStatus(), 1);
            }
        }
        for (Job.Status status : statuses)
        {
            assertEquals(numCopies, countByStatus.get(status).intValue());
        }
    }

    @Test
    public void moveableBeforeWorks()
    {
        int numJobs = 5;
        List<Job> jobs = setupMoveableJobs(numJobs);

        // nothing before 1st for each user
        assertNull(service.getMoveableJobBefore(jobs.get(0)));
        assertNull(service.getMoveableJobBefore(jobs.get(1)));

        for (int i = 2; i < numJobs; i++)
        {
            // i - 2 for the even/odd user assignment
            assertEquals(jobs.get(i - 2).getId(), service.getMoveableJobBefore(jobs.get(i)).getId());
        }
    }

    @Test
    public void moveableAfterWorks()
    {
        int numJobs = 5;
        List<Job> jobs = setupMoveableJobs(numJobs);

        // nothing after last for each user
        assertNull(service.getMoveableJobAfter(jobs.get(numJobs - 1)));
        assertNull(service.getMoveableJobAfter(jobs.get(numJobs - 2)));

        for (int i = numJobs - 3; i >= 0; i--)
        {
            // i + 2 for the even/odd user assignment
            assertEquals(jobs.get(i + 2).getId(), service.getMoveableJobAfter(jobs.get(i)).getId());
        }
    }

    // for 2 users, 1 owns evens, other odds
    private List<Job> setupMoveableJobs(int numJobs)
    {
        List<Job> jobs = new ArrayList<Job>();
        User userEvens = new User();
        User userOdds = new User();
        userDao.create(userEvens);
        userDao.create(userOdds);

        for (int i = 0; i < numJobs; i++)
        {
            Job job = new Job();
            job.setUser(i % 2 == 0 ? userEvens : userOdds);
            service.create(job);
            jobs.add(job);
        }
        return jobs;
    }

    @Test
    public void findMoveablesChecksStatus()
    {
        User user = new User();
        userDao.create(user);

        List<Job> jobs = createJobs(user, QUEUED, RUNNING, COMPLETE, ARCHIVED, DELETED, QUEUED);

        // 1st job is the moveable before the last
        assertEquals(jobs.get(0).getId(), service.getMoveableJobBefore(jobs.get(jobs.size() - 1)).getId());

        // last job is the moveable before the 1st
        assertEquals(jobs.get(jobs.size() - 1).getId(), service.getMoveableJobAfter(jobs.get(0)).getId());
    }

    private List<Job> createJobs(User user, Job.Status... statuses)
    {
        List<Job> jobs = new ArrayList<Job>();
        for (Job.Status status : statuses)
        {
            Job job = new Job();
            job.setUser(user);
            job.setStatus(status);
            service.create(job);
            jobs.add(job);
        }
        return jobs;
    }

    @Test
    public void swapPositionsAndUpdateWorks()
    {
        Job jobA = getJobWithAUser();
        Job jobB = getJobWithAUser();
        service.create(jobA);
        service.create(jobB);

        int posA = jobA.getQueuePosition();
        int posB = jobB.getQueuePosition();

        service.updateWithSwappedQueuePositions(jobA, jobB);

        jobA = service.read(jobA.getId());
        jobB = service.read(jobB.getId());

        assertEquals(posA, jobB.getQueuePosition());
        assertEquals(posB, jobA.getQueuePosition());
    }

    @Test
    public void startsNextInQueue()
    {
        Model model = Job.Model.ABM;
        String runner = "ASLKJDF";

        createJobWith(model, COMPLETE); // not queued
        createJobWith(Model.PECAS, QUEUED); // wrong model
        Job j2 = createJobWith(model, QUEUED); // next
        Job j3 = createJobWith(model, QUEUED); // after next

        Job next = service.startNextInQueue(runner, model);
        assertEquals(j2.getId(), next.getId());
        assertEquals(runner, next.getRunner());
        assertEquals(RUNNING, next.getStatus());

        Job nextNext = service.startNextInQueue(runner, model);
        assertEquals(j3.getId(), nextNext.getId());
    }

    @Test
    public void noneQueuedReturnsNull()
    {
        createJobWith(Model.ABM, COMPLETE);
        assertNull(service.startNextInQueue("", Job.Model.ABM, Job.Model.PECAS));
    }

    // support
    private Job createJobWith(Model model, Job.Status status)
    {
        Job job = getJobWithAUser();
        job.setModel(model);
        job.setStatus(status);
        service.create(job);
        return job;
    }

    private Job getJobWithAUser()
    {
        Job job = new Job();
        User user = new User();
        userDao.create(user);
        job.setUser(user);
        return job;
    }

}
