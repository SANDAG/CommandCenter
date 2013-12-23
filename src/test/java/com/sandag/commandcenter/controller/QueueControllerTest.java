package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.JobService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueueControllerTest
{
    private QueueController controller = new QueueController();
    private Model model;
        
    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
    }

    @Test
    public void canDisplayQueue()
    {
        JobService service = mock(JobService.class);
        List<Job> jobs = new ArrayList<Job>();
        Job job = new Job();
        jobs.add(job);
        when(service.readAll()).thenReturn(jobs);
        controller.jobService = service;
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("principal name");
        
        assertEquals("queue", controller.display(model, principal));
        assertTrue(model.containsAttribute("message"));
        assertTrue(model.containsAttribute("principalName"));
        assertTrue(model.containsAttribute("jobAccessManager"));
        Object modelJobs = model.asMap().get("jobs");
        assertEquals(jobs, modelJobs);
    }


}