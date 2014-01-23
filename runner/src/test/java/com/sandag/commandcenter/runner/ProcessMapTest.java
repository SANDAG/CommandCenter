package com.sandag.commandcenter.runner;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sandag.commandcenter.model.Job;

public class ProcessMapTest
{

    private ProcessMap processMap;
    private HashMap<Integer, Process> ownedMap;
    private Job job = new Job();
    private Process process = mock(Process.class);
    
    @SuppressWarnings("unchecked")
    @Before
    public void setup()
    {
        processMap = new ProcessMap();
        ownedMap = mock(HashMap.class);
        processMap.map = ownedMap;
        job.setId(42);
    }
    
    @Test
    public void puts()
    {
        processMap.put(job, process);
        verify(ownedMap).put(job.getId(), process);
    }
    
    @Test
    public void removes()
    {
        processMap.remove(job);
        verify(ownedMap).remove(job.getId());
    }
    
    @Test
    public void gets()
    {
        processMap.put(job, process);
        when(ownedMap.get(job.getId())).thenReturn(process);
        assertEquals(process, processMap.get(job));
    }
}
