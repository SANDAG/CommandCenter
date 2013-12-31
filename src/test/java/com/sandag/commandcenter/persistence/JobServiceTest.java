package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/db.xml", "classpath:/autowire.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class JobServiceTest
{
    @Autowired
    private JobService service;

    @Autowired
    private UserService userService;

    @After
    public void cleanUp()
    {
        service.getSession().createQuery("DELETE FROM Job").executeUpdate();
    }

    @Test
    public void resultsReadInOrder()
    {
        int numResults = 5;
        for (int i = 0; i < numResults; i++)
        {
            service.create(getJobWithAUser());

        }
        List<Job> results = service.readAll();
        assertEquals(numResults, results.size());
        int lastPosition = -1;
        for (Job job : results)
        {
            int position = job.getQueuePosition();
            assertTrue(position > lastPosition);
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
        userService.create(userEvens);
        userService.create(userOdds);

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
    
    
    
    
    
    
    
    
    
    
    
    
    // support
    private Job getJobWithAUser()
    {
        Job job = new Job();
        User user = new User();
        userService.create(user);
        job.setUser(user);
        return job;
    }

}
