package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/db.xml", "classpath:/autowire.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class JobServiceTest
{
    @Autowired
    private JobService service;

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
            service.create(new Job());
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
            Job job = new Job();
            service.create(job);
            int current = job.getQueuePosition();
            assertTrue(current > last);
            last = current;
        }
    }

}
