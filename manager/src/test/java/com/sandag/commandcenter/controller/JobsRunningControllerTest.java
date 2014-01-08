package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.JobDao;

public class JobsRunningControllerTest
{
    private JobsRunningController controller;
    private Model model;

    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
        controller = new JobsRunningController();
    }

    @Test
    public void canDisplay()
    {
        JobDao service = mock(JobDao.class);
        List<Job> jobs = new ArrayList<Job>();
        Job job = new Job();
        jobs.add(job);
        when(service.read(Job.Status.RUNNING)).thenReturn(jobs);
        controller.jobDao = service;

        assertEquals("jobsRunning", controller.display(model));
        assertTrue(model.containsAttribute("message"));
        Object modelJobs = model.asMap().get("jobs");
        assertEquals(jobs, modelJobs);
    }

}