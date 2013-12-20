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
import com.sandag.commandcenter.persistence.JobService;
import com.sandag.commandcenter.persistence.UserService;

public class JobControllerTest
{

    private JobController controller;

    private Model model;

    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
        controller = new JobController();
    }

    @Test
    public void emptyPageShows()
    {
        assertEquals("job", controller.displayEmptyJobForm(model));
        assertTrue(model.containsAttribute("job"));
        assertTrue(model.containsAttribute("modelNameMappings"));
        assertTrue(model.containsAttribute("message"));
    }

    @Test
    public void invalidJobGetsFailureMessage()
    {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        assertEquals("job", controller.submitJob(null, result, model, null));
        assertTrue(model.containsAttribute("modelNameMappings"));
        assertTrue(((String) model.asMap().get("message")).matches(".*fix.*error.*"));
    }

    @Test
    public void validJobGetsSaved()
    {
        String principalName = "some identifier";
        User user = new User();
        user.setPrincipal(principalName);

        Job job = mock(Job.class);
        JobService jobService = mock(JobService.class);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(principalName);

        UserService userService = mock(UserService.class);
        when(userService.fetchOrCreate(principalName)).thenReturn(user);

        controller.jobService = jobService;
        controller.userService = userService;

        assertEquals("job", controller.submitJob(job, result, model, principal));

        verify(userService).fetchOrCreate(principalName);
        verify(job).setUser(user);
        verify(jobService).create(job);

        assertTrue(model.containsAttribute("modelNameMappings"));
        assertTrue(((String) model.asMap().get("message")).matches("Success.*"));
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

}
