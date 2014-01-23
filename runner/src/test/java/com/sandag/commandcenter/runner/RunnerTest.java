package com.sandag.commandcenter.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.Job.Model;

public class RunnerTest
{
    private Runner runner;
    private ProcessMap mockProcessMap;
    private ProcessBuilderWrapper mockProcessBuilder;
    private Process mockProcess;
    private Job.Model model = Model.PECAS;
    private String scenarioDir = "scenario0";
    private Job job = new Job();
    {
        job.setScenario(scenarioDir);
    }

    @Before
    public void setup() throws IOException
    {
        runner = new Runner();
        mockProcessMap = mock(ProcessMap.class);
        runner.processMap = mockProcessMap;
        mockProcessBuilder = mock(ProcessBuilderWrapper.class);
        runner.processBuilder = mockProcessBuilder;
        mockProcess = mock(Process.class);
        when(mockProcessBuilder.start()).thenReturn(mockProcess);
        runner.setModel(model);
    }
    
    @Test
    public void runsSetsDirectory() throws IOException
    {
        String baseDir = ".";  // cross-platform compatible
        String dir = ".";
                
        runner.baseDir = baseDir;
        runner.setWorkingDir(dir);
        runner.run(job);

        verify(mockProcessBuilder).directory(new File(String.format("%s/%s/%s", baseDir, dir, scenarioDir)));
    }

    @Test
    public void initSetsCommand()
    {
        String command = "Command!";
        runner.setCommandLine(command);
        runner.initialize();
        verify(mockProcessBuilder).command(command);
    }
    
    @Test
    public void supportsReturnsModel()
    {
        assertEquals(model, runner.supports());
    }

    @Test
    public void checkExitValues() throws InterruptedException, IOException
    {
        checkExitValues(-56345624, false);
        checkExitValues(-1, false);
        checkExitValues(0, true);
        checkExitValues(1, false);
        checkExitValues(42342, false);
    }

    @Test
    public void ioExceptionHandled() throws IOException
    {
        runner.setModel(Model.PECAS);
        when(mockProcessBuilder.start()).thenThrow(new IOException());
        assertFalse(runner.run(job));
        verify(mockProcessMap, never()).put(job, mockProcess);
    }

    @Test
    public void interruptedExceptionHandled() throws InterruptedException, IOException
    {
        when(mockProcess.waitFor()).thenThrow(new InterruptedException());
        assertFalse(runner.run(job));
        verify(mockProcessMap).put(job, mockProcess);
        verify(mockProcessMap).remove(job);
    }

    // support
    private void checkExitValues(int processExitValue, boolean success) throws IOException, InterruptedException
    {
        when(mockProcess.waitFor()).thenReturn(processExitValue);
        runner.setModel(Model.PECAS);
        assertEquals(success, runner.run(job));
        verify(mockProcessMap).put(job, mockProcess);
        verify(mockProcessMap).remove(job);
        reset(mockProcessMap);
    }

}
