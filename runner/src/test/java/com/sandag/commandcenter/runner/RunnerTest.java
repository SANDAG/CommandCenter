package com.sandag.commandcenter.runner;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.sandag.commandcenter.model.Job.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RunnerTest
{

    @Test
    public void runsSetsDirectory() throws IOException
    {
        String dir = "."; // cross-platform compatible
        String scenarioDir = "scenario0";
        
        ProcessBuilderWrapper processBuilder = mock(ProcessBuilderWrapper.class);
        Process process = mock(Process.class);
        Runner runner = new Runner();
        runner.setModel(Model.PECAS);
        runner.setProcessBuilder(processBuilder);

        when(processBuilder.start()).thenReturn(process);
        runner.setWorkingDir(dir);
        runner.run(scenarioDir);

        verify(processBuilder).directory(new File(dir + File.separatorChar + scenarioDir));
    }

    @Test
    public void initSetsCommand()
    {
        String command = "Command!";
        Runner runner = new Runner();
        ProcessBuilderWrapper processBuilder = mock(ProcessBuilderWrapper.class);
        runner.processBuilder = processBuilder;
        runner.setCommandLine(command);
        runner.initialize();
        verify(processBuilder).command(command);
    }
    
    @Test
    public void supportsReturnsModel()
    {
        Model model = Model.PECAS;

        Runner runner = new Runner();
        runner.setModel(model);
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
        ProcessBuilderWrapper processBuilder = mock(ProcessBuilderWrapper.class);
        Runner runner = new Runner();
        runner.setProcessBuilder(processBuilder);
        runner.setModel(Model.PECAS);

        when(processBuilder.start()).thenThrow(new IOException());
        assertFalse(runner.run(""));
    }

    @Test
    public void interruptedExceptionHandled() throws InterruptedException, IOException
    {
        ProcessBuilderWrapper processBuilder = mock(ProcessBuilderWrapper.class);
        Process process = mock(Process.class);
        Runner runner = new Runner();
        runner.setModel(Model.PECAS);
        runner.setProcessBuilder(processBuilder);

        when(processBuilder.start()).thenReturn(process);
        when(process.waitFor()).thenThrow(new InterruptedException());
        assertFalse(runner.run(""));
    }

    // support
    private void checkExitValues(int processExitValue, boolean success) throws IOException, InterruptedException
    {
        ProcessBuilderWrapper processBuilder = mock(ProcessBuilderWrapper.class);
        Process process = mock(Process.class);
        when(processBuilder.start()).thenReturn(process);
        when(process.waitFor()).thenReturn(processExitValue);
        Runner runner = new Runner();
        runner.setModel(Model.PECAS);
        runner.setProcessBuilder(processBuilder);
        assertEquals(success, runner.run(""));
    }

}
