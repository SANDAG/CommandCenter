package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyObject;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.sandag.commandcenter.io.FileLister;
import com.sandag.commandcenter.model.Job;

public class LogFilesControllerTest
{

    private LogFilesController controller = new LogFilesController();

    @Test
    public void initsOk() throws UnknownHostException
    {
        Map<String, FileLister> listers = new HashMap<String, FileLister>();
        FileLister abm = mock(FileLister.class);
        FileLister pecas = mock(FileLister.class);
        when(abm.getModel()).thenReturn(Job.Model.ABM);
        when(pecas.getModel()).thenReturn(Job.Model.PECAS);
        listers.put("ignored", abm);
        listers.put("ignored too", pecas);

        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBeansOfType(FileLister.class)).thenReturn(listers);

        controller.context = context;
        controller.initialize();

        assertEquals(2, controller.listers.size());
        assertEquals(abm, controller.listers.get(Job.Model.ABM));
        assertEquals(pecas, controller.listers.get(Job.Model.PECAS));
    }

    @Test
    public void callsLister()
    {
        Job job = new Job();
        FileLister lister = mock(FileLister.class);
        @SuppressWarnings("unchecked")
        Map<Job.Model, FileLister> listers = mock(Map.class);
        when(listers.get(anyObject())).thenReturn(lister);
        controller.listers = listers;
        controller.listFiles(job);
        verify(lister).listFiles(job);
    }

    @Test
    public void returnsNullWithNoLister()
    {
        Job job = new Job();
        @SuppressWarnings("unchecked")
        Map<Job.Model, FileLister> listers = mock(Map.class);
        when(listers.get(anyObject())).thenReturn(null);
        assertNull(controller.listFiles(job));
    }

}