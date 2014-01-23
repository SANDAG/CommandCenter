package com.sandag.commandcenter.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.JobDao;

public class ProcessKillerTest
{

    private ProcessKiller killer;
    private JobDao mockJobDao;
    private ProcessMap mockProcessMap;
    private Process mockProcess;
    private Job job0 = new Job();
    private Job job1 = new Job();
    private Job job2 = new Job();
    private List<Job> cancelledJobs = Arrays.asList(new Job[] {job0, job1, job2 });
    private String host = "host";

    @Before
    public void setup()
    {
        killer = new ProcessKiller();
        mockJobDao = mock(JobDao.class);
        mockProcessMap = mock(ProcessMap.class);
        mockProcess = mock(Process.class);
        killer.jobDao = mockJobDao;
        killer.processMap = mockProcessMap;
        killer.serviceName = host;
        when(mockJobDao.readCancelled(host)).thenReturn(cancelledJobs);
    }

    @Test
    public void killsCancelledJobs()
    {
        when(mockProcessMap.get((Job) anyObject())).thenReturn(mockProcess);
        killer.killCancelledJobProcesses();
        verify(mockProcess, times(cancelledJobs.size())).destroy();
        for (Job j : cancelledJobs)
        {
            verify(mockJobDao).delete(j);
            verify(mockProcessMap).remove(j);
        }
    }

    @Test
    public void noLongerInProcessMapContinuesKilling()
    {
        Job[] jobsStillInMap = new Job[] {job0, job2 };
        for (Job j : jobsStillInMap)
        {
            when(mockProcessMap.get(j)).thenReturn(mockProcess);
        }
        when(mockProcessMap.get(job1)).thenReturn(null);

        killer.killCancelledJobProcesses();
        for (Job j : jobsStillInMap)
        {
            verify(mockJobDao).delete(j);
            verify(mockProcessMap).remove(j);
        }
        verify(mockJobDao, never()).delete(job1);
        verify(mockProcessMap, never()).remove(job1);

        verify(mockProcess, times(jobsStillInMap.length)).destroy();
    }

    @Test
    public void noJobsToKillDoesNotThrow()
    {
        when(mockJobDao.readCancelled(host)).thenReturn(new ArrayList<Job>());
        killer.killCancelledJobProcesses();
        verifyZeroInteractions(mockProcessMap);
    }

    @Test
    public void initializes()
    {
        // service name gets set at startup so we don't rely on it being available later
        ServiceNameRetriever hostGetter = mock(ServiceNameRetriever.class);
        when(hostGetter.retrieve()).thenReturn(host);
        ProcessKiller pk = new ProcessKiller();
        pk.serviceNameRetriever = hostGetter;

        assertNull(pk.serviceName);
        pk.initialize();
        assertEquals(host, pk.serviceName);
    }
}
