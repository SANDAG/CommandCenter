package com.sandag.commandcenter.controller;

import static com.sandag.commandcenter.model.Job.Model.ABM;
import static com.sandag.commandcenter.model.Job.Model.PECAS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Test;

import com.sandag.commandcenter.io.FileLister;
import com.sandag.commandcenter.io.FileListerFactory;
import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.testutils.TestUtils;

public class LogFilesControllerTest
{

    private LogFilesController controller = new LogFilesController();

    @Test
    public void initsOk() throws UnknownHostException
    {
        String pecasFiles = "file0,file1";
        String abmFiles = "file2,file3,file4";
        controller.runnerProperties = TestUtils.getProperties("PECAS_logFileNames", pecasFiles, "ABM_logFileNames", abmFiles);
        
        FileListerFactory fileListerFactory = mock(FileListerFactory.class); 
        controller.fileListerFactory = fileListerFactory;
        FileLister pecasFileLister = new FileLister();
        FileLister abmFileLister = new FileLister();
        when(fileListerFactory.getFileLister(PECAS, pecasFiles)).thenReturn(pecasFileLister);
        when(fileListerFactory.getFileLister(ABM, abmFiles)).thenReturn(abmFileLister);        

        controller.initialize();
        assertEquals(2, controller.listers.size());
        assertSame(pecasFileLister, controller.listers.get(PECAS));
        assertSame(abmFileLister, controller.listers.get(ABM));        
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