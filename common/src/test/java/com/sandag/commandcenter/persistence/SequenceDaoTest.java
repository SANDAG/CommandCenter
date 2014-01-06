package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/db.xml", "classpath:/autowire.xml" })
public class SequenceDaoTest
{
    @Autowired
    private SequenceDao service;

    @Test
    public void nextValueIncreases()
    {
        int last = -1;
        for (int i = 0; i < 5; i++) 
        {
            int current = service.next();
            assertTrue(current > last);
        }
    }

}
