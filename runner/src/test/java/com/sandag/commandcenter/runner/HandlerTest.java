package com.sandag.commandcenter.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Test;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.Job.Model;

import static com.sandag.commandcenter.model.Job.Model.ABM;
import static com.sandag.commandcenter.model.Job.Model.PECAS;

import com.sandag.commandcenter.notification.RunNotifier;
import com.sandag.commandcenter.persistence.ClusterDao;
import com.sandag.commandcenter.persistence.JobDao;
import com.sandag.commandcenter.testutils.TestUtils;

public class HandlerTest
{

    @Test
    public void initsOk() throws UnknownHostException
    {
        String serviceName = "Server host";
        ServiceNameRetriever retriever = mock(ServiceNameRetriever.class);
        when(retriever.retrieve()).thenReturn(serviceName);

        Handler handler = new Handler();
        handler.serviceNameRetriever = retriever;
        
        String pecasCmd = "PECAS command line";
        String abmCmd = "ABM command line";
        handler.runnerProperties = TestUtils.getProperties("PECAS_cmdLine", pecasCmd, "ABM_cmdLine", abmCmd);

        RunnerFactory runnerFactory = mock(RunnerFactory.class); 
        handler.runnerFactory = runnerFactory;
        Runner pecasRunner = new Runner();
        Runner abmRunner = new Runner();
        when(runnerFactory.getRunner(pecasCmd, PECAS)).thenReturn(pecasRunner);
        when(runnerFactory.getRunner(abmCmd, ABM)).thenReturn(abmRunner);        
        
        handler.initialize();
        assertEquals(2, handler.runners.size());
        assertSame(pecasRunner, handler.runners.get(PECAS));
        assertSame(abmRunner, handler.runners.get(ABM));
        
        assertEquals(2, handler.supportedModels.length);
        assertContains(handler.supportedModels, ABM);
        assertContains(handler.supportedModels, PECAS);

        assertEquals(serviceName, handler.serviceName);
    }

    @Test
    public void runsNext()
    {
        boolean runSuccessful = true;
        JobDao dao = mock(JobDao.class);
        Handler handler = new Handler();
        handler.serviceName = "name";
        handler.supportedModels = new Job.Model[] {ABM, PECAS };
        addMockClusterDao(handler, true);
        
        Job.Model jobModel = ABM;
        Job job = mock(Job.class);
        when(job.getModel()).thenReturn(jobModel);

        Runner runner = mock(Runner.class);
        when(runner.run(job)).thenReturn(runSuccessful);

        @SuppressWarnings("unchecked")
        Map<Job.Model, Runner> runners = mock(Map.class);
        when(runners.get(jobModel)).thenReturn(runner);

        when(dao.startNextInQueue(handler.serviceName, handler.supportedModels)).thenReturn(job);

        handler.jobDao = dao;
        handler.runners = runners;
        handler.initialized = true;
        handler.runNotifier = mock(RunNotifier.class);
        handler.runNext();
        verify(runner).run(job);
        verify(dao).updateAsFinished(job, runSuccessful);
    }

    @Test
    public void noNextDoesNothing()
    {
        Handler handler = new Handler();
        addMockJobDao(handler, null);
        @SuppressWarnings("unchecked")
        Map<Job.Model, Runner> runners = mock(Map.class);
        when(runners.get(any(Job.Model.class))).thenReturn(new Runner());
        RunNotifier runNotifier = mock(RunNotifier.class);
        handler.runNotifier = runNotifier;
        handler.runners = runners;
        handler.runNext();
        verify(runNotifier, never()).sendStartedMessage(null);
        verify(runNotifier, never()).sendFinishedMessage(null);
        verify(runners, never()).get(any(Job.Model.class));
    }

    @Test
    public void noOpWhenUninitialized()
    {
        Handler handler = new Handler();
        addMockJobDao(handler, new Job());
        addMockClusterDao(handler, true);
        handler.runNext();
        verify(handler.jobDao, never()).startNextInQueue(anyString(), (Model[]) anyObject());
    }

    @Test
    public void noOpWhenClusterInactive()
    {
        Handler handler = new Handler();
        handler.initialized = true;
        addMockClusterDao(handler, false);
        addMockJobDao(handler, new Job());
        handler.runNext();
        verify(handler.jobDao, never()).startNextInQueue(anyString(), (Model[]) anyObject());
    }

    @Test
    public void sendsMessages()
    {
        Handler handler = new Handler();
        Job job = new Job();
        addMockJobDao(handler, job);
        addMockClusterDao(handler, true);
        RunNotifier runNotifier = mock(RunNotifier.class);
        @SuppressWarnings("unchecked")
        Map<Job.Model, Runner> runners = mock(Map.class);
        when(runners.get(any(Job.Model.class))).thenReturn(mock(Runner.class));
        handler.initialized = true;
        handler.runNotifier = runNotifier;
        handler.runners = runners;
        handler.runNext();
        verify(runNotifier).sendStartedMessage(job);
        verify(runNotifier).sendFinishedMessage(job);
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

    private void addMockClusterDao(Handler handler, boolean clusterActive)
    {
        ClusterDao clusterDao = mock(ClusterDao.class);
        when(clusterDao.isActive(anyString())).thenReturn(clusterActive);
        handler.clusterDao = clusterDao;
    }
    
    private void addMockJobDao(Handler handler, Job startNextReturn)
    {
        JobDao jobDao = mock(JobDao.class);
        when(jobDao.startNextInQueue(anyString(), (Job.Model) anyObject())).thenReturn(startNextReturn);
        handler.jobDao = jobDao;
    }
    
}
