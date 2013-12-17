package com.sandag.commandcenter.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/db.xml", })
public class JobTest
{
    @Test
    public void ignoreStatusEnumForCodeCoverage()
    {
        Job.Status.valueOf(Job.Status.ARCHIVED.toString());
    }

}
