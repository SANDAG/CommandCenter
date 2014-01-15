package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class LogFileReaderControllerTest
{

    private LogFileReaderController controller;
    private File file;
    private String fileName;
    private String contentsStart = "1 Here is some data";
    private String contentsEnd = "browser in chunks...\r\n";

    @Before
    public void setup() throws URISyntaxException
    {
        controller = new LogFileReaderController();
        URL fileUrl = ClassLoader.getSystemResource("logFile.log");
        file = new File(fileUrl.toURI());
        fileName = file.getName();
        controller.dir = file.getParent();
    }

    @Test
    public void missingFileReturnsNull()
    {
        file = new File("does not exist");
        assertNull(controller.read(file.getName(), 0));
    }

    @Test
    public void atOrPastEndReturnsNull()
    {
        int lastByte = (int) (file.length() - 1);
        String name = file.getName();
        
        assertNotNull(controller.read(name, lastByte));
        assertNull(controller.read(name, lastByte + 1));
    }

    @Test 
    public void readsData()
    {
        String output = new String(controller.read(fileName, 0));
        assertTrue(output.startsWith(contentsStart));
    }
    
    @Test
    public void readsToEnd()
    {
        int length = contentsEnd.length();
        // assumption for this test
        assertTrue(controller.maxBytes >= length);
        String output = new String(controller.read(fileName, (int) (file.length() - length)));
        assertEquals(contentsEnd, output);
    }
    
    @Test
    public void offsetWorks()
    {
        int offset = 3;
        String output = new String(controller.read(fileName, offset));
        assertTrue(output.startsWith(contentsStart.substring(3)));
    }
}
