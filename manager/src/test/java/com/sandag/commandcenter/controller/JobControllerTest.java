package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;
import com.sandag.commandcenter.persistence.JobDao;
import com.sandag.commandcenter.persistence.UserDao;

public class JobControllerTest
{

    private JobController controller;

    private Model model;

    private String scenarioLocationRoot = "C:/inside/schrodingers/box";
    
    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
        controller = new JobController();
        controller.scenarioLocationRoot = scenarioLocationRoot;
    }

    @Test
    public void emptyPageShows()
    {
        assertEquals("job", controller.displayEmptyJobForm(model));
        assertTrue(model.containsAttribute("job"));
        assertTrue(model.containsAttribute("modelNameMappings"));
        assertTrue(model.containsAttribute("message"));
        assertEquals(scenarioLocationRoot, model.asMap().get("dirPickerRoot"));
    }

    @Test
    public void invalidJobGetsFailureMessage()
    {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        assertEquals("job", controller.addJob(null, result, model, null));
        assertTrue(model.containsAttribute("modelNameMappings"));
        assertEquals(scenarioLocationRoot, model.asMap().get("dirPickerRoot"));
        assertTrue(((String) model.asMap().get("message")).matches(".*fix.*error.*"));
    }

    @Test
    public void validJobGetsSaved()
    {
        String principalName = "some identifier";
        User user = new User();
        user.setPrincipal(principalName);

        Job job = mock(Job.class);
        JobDao jobDao = mock(JobDao.class);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(principalName);

        UserDao userDao = mock(UserDao.class);
        when(userDao.fetchOrCreate(principalName)).thenReturn(user);

        controller.jobDao = jobDao;
        controller.userDao = userDao;

        String redirViewPrefix = "redirect:jobs/queued?highlight=";
        assertTrue(controller.addJob(job, result, model, principal).startsWith(redirViewPrefix));

        verify(userDao).fetchOrCreate(principalName);
        verify(job).setUser(user);
        verify(jobDao).create(job);

        assertTrue(model.containsAttribute("modelNameMappings"));
    }

    @Test
    public void modelNamesMapLooksGood()
    {
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) controller.getModelNamesMap();
        assertTrue(map.containsKey(""));
        for (Job.Model m : Job.Model.values())
        {
            assertTrue(map.containsKey(m.toString()));
        }
    }

    @Test
    public void jobGetsDeleted()
    {
        JobDao jobDao = mock(JobDao.class);
        controller.jobDao = jobDao;
        Job job = new Job();

        controller.deleteQueuedJob(job, null, null);

        verify(jobDao).deleteIfQueued(job);
    }

    @Test
    public void checkResponses()
    {
        JobDao jobDao = mock(JobDao.class);
        controller.jobDao = jobDao;
        Job job = new Job();
        
        when(jobDao.deleteIfQueued(job)).thenReturn(false);        
        assertEquals("Job was no longer queued", controller.deleteQueuedJob(job, null, null));

        when(jobDao.deleteIfQueued(job)).thenReturn(true);        
        assertEquals("Job deleted", controller.deleteQueuedJob(job, null, null));
}
    
    
    
}
