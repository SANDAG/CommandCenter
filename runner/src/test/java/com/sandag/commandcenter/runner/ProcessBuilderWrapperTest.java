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
        String command0 = "interpreter";
        String command1 = "scriptName.xyz";
        ProcessBuilderWrapper wrapper = new ProcessBuilderWrapper();
        wrapper.command(command0 + " " + command1);
        ProcessBuilder pb = wrapper.processBuilder;
        assertEquals(2, pb.command().size());
        assertEquals(command0, pb.command().get(0));
        assertEquals(command1, pb.command().get(1));
    }
    
    @Test
    public void wrapsDirectory() {
        File dir = new File(".");
        ProcessBuilderWrapper wrapper = new ProcessBuilderWrapper();
        wrapper.command("[some command here]");
        wrapper.directory(dir);
        assertEquals(dir, wrapper.processBuilder.directory());
    }
    
    @Test
    public void wrapsStart() throws IOException {
        ProcessBuilderWrapper wrapper = new ProcessBuilderWrapper();
        wrapper.command("java");
        Process process = wrapper.start();
        assertNotNull(process);
        process.destroy();
    }
}
