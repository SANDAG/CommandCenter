package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.JobDao;

public class JobArchiveControllerTest
{

    private JobArchiveController controller;
    private Model model;
    private Job job;

    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
        controller = new JobArchiveController();
        job = new Job();
    }

    @Test
    public void displayWorks()
    {
        assertEquals("jobArchive", controller.displayForm(job, model));
        assertEquals(job, model.asMap().get("job"));
        assertTrue(model.containsAttribute("message"));
    }
    
    @Test
    public void invalidJobGetsFailureMessage()
    {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        assertEquals("jobArchive", controller.archiveJob(job, result, model, null));
        assertTrue(((String) model.asMap().get("message")).matches(".*fix.*error.*"));
    }

    @Test
    public void validJobGetsArchived()
    {        
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        JobDao jobDao = mock(JobDao.class);        
        controller.jobDao = jobDao;
        
        assertEquals(Job.Status.QUEUED, job.getStatus());
        assertEquals("redirect:/jobs/finished", controller.archiveJob(job, result, model, null));
        verify(jobDao).update(job);
        assertEquals(Job.Status.ARCHIVED, job.getStatus());
    }    
    
    @Test
    public void unarchiveWorks()
    {        
        JobDao jobDao = mock(JobDao.class);        
        controller.jobDao = jobDao;
        job.setStatus(Job.Status.ARCHIVED);

        assertEquals("Job unarchived", controller.unarchiveJob(job, null));
        verify(jobDao).update(job);
        assertEquals(Job.Status.FINISHED, job.getStatus());
    }    

}
