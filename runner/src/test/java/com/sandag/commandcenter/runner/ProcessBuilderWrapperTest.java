package com.sandag.commandcenter.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ProcessBuilderWrapperTest
{

    @Test
    public void constructsProcessBuilder() {
        String command = "scriptName.xyz";
        ProcessBuilderWrapper wrapper = new ProcessBuilderWrapper(command);
        ProcessBuilder pb = wrapper.processBuilder;
        assertEquals(1, pb.command().size());
        assertEquals(command, pb.command().get(0));
    }
    
    @Test
    public void wrapsDirectory() {
        File dir = new File(".");
        ProcessBuilderWrapper wrapper = new ProcessBuilderWrapper("command");
        wrapper.directory(dir);
        assertEquals(dir, wrapper.processBuilder.directory());
    }
    
    @Test
    public void wrapsStart() throws IOException {
        ProcessBuilderWrapper wrapper = new ProcessBuilderWrapper("python");
        Process process = wrapper.start();
        assertNotNull(process);
        process.destroy();
    }
}
