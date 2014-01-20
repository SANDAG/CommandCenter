package com.sandag.commandcenter.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.Job.Model;
import com.sandag.commandcenter.notification.RunNotifier;
import com.sandag.commandcenter.persistence.JobDao;

public class HandlerTest
{

    @Test
    public void initsOk() throws UnknownHostException
    {
        String serviceName = "Server host";

        Map<String, Runner> runners = new HashMap<String, Runner>();
        Runner abm = mock(Runner.class);
        Runner pecas = mock(Runner.class);
        when(abm.supports()).thenReturn(Job.Model.ABM);
        when(pecas.supports()).thenReturn(Job.Model.PECAS);
        runners.put("ignored", abm);
        runners.put("ignored too", pecas);

        ServiceNameRetriever retriever = mock(ServiceNameRetriever.class);
        when(retriever.retrieve()).thenReturn(serviceName);

        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBeansOfType(Runner.class)).thenReturn(runners);

        Handler handler = new Handler();
        handler.serviceNameRetriever = retriever;
        handler.context = context;

        handler.initialize();

        assertEquals(2, handler.runners.size());
        assertEquals(abm, handler.runners.get(Job.Model.ABM));
        assertEquals(pecas, handler.runners.get(Job.Model.PECAS));

        assertEquals(2, handler.supportedModels.length);
        assertContains(handler.supportedModels, Job.Model.ABM);
        assertContains(handler.supportedModels, Job.Model.PECAS);

        assertEquals(serviceName, handler.serviceName);
    }

    @Test
    public void runsNext()
    {
        boolean runSuccessful = true;
        JobDao dao = mock(JobDao.class);
        Handler handler = new Handler();
        handler.serviceName = "name";
        handler.supportedModels = new Job.Model[] {Job.Model.ABM, Job.Model.PECAS };

        Job.Model jobModel = Job.Model.ABM;
        Job job = mock(Job.class);
        when(job.getModel()).thenReturn(jobModel);

        Runner runner = mock(Runner.class);
        when(runner.run(null)).thenReturn(runSuccessful);

        @SuppressWarnings("unchecked")
        Map<Job.Model, Runner> runners = mock(Map.class);
        when(runners.get(jobModel)).thenReturn(runner);

        when(dao.startNextInQueue(handler.serviceName, handler.supportedModels)).thenReturn(job);

        handler.jobDao = dao;
        handler.runners = runners;
        handler.initialized = true;
        handler.runNotifier = mock(RunNotifier.class);
        handler.runNext();
        verify(runner).run(null);
        verify(dao).updateAsFinished(job, runSuccessful);
    }

    @Test
    public void noNextDoesNothing()
    {
        JobDao dao = mock(JobDao.class);
        when(dao.startNextInQueue(anyString(), (Job.Model) anyObject())).thenReturn(null);
        Handler handler = new Handler();
        @SuppressWarnings("unchecked")
        Map<Job.Model, Runner> runners = mock(Map.class);
        when(runners.get(any(Job.Model.class))).thenReturn(new Runner());
        RunNotifier runNotifier = mock(RunNotifier.class);
        handler.runNotifier = runNotifier;
        handler.jobDao = dao;
        handler.runners = runners;
        handler.runNext();
        verify(runNotifier, never()).sendStartedMessage(null);
        verify(runners, never()).get(any(Job.Model.class));
    }

    @Test
    public void noOpWhenUninitialized()
    {
        Handler handler = new Handler();
        JobDao dao = mock(JobDao.class);
        handler.jobDao = dao;

        handler.runNext();
        verify(dao, never()).startNextInQueue(anyString(), (Model[]) anyObject());
    }

    @Test
    public void sendsStartedMessage()
    {
        Job job = new Job();
        JobDao dao = mock(JobDao.class);
        when(dao.startNextInQueue(anyString(), (Job.Model) anyObject())).thenReturn(job);
        Handler handler = new Handler();
        RunNotifier runNotifier = mock(RunNotifier.class);
        @SuppressWarnings("unchecked")
        Map<Job.Model, Runner> runners = mock(Map.class);
        when(runners.get(any(Job.Model.class))).thenReturn(mock(Runner.class));
        handler.initialized = true;
        handler.runNotifier = runNotifier;
        handler.jobDao = dao;
        handler.runners = runners;
        handler.runNext();
        verify(runNotifier).sendStartedMessage(job);
    }
    
    // support
    private void assertContains(Object[] array, Object value)
    {
        for (Object o : array)
        {
            if (o.equals(value))
            {
                return;
            }
        }
        fail(String.format("'%s' is not in '%s'", value, array));
    }

}
