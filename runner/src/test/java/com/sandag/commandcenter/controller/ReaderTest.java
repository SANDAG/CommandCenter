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

public class ReaderTest
{

    Reader reader;
    File file;
    String fileName;
    String contentsStart = "1 Here is some data";
    String contentsEnd = "browser in chunks...\r\n";

    @Before
    public void setup() throws URISyntaxException
    {
        reader = new Reader();
        URL fileUrl = ClassLoader.getSystemResource("logFile.log");
        file = new File(fileUrl.toURI());
        fileName = file.getName();
        reader.dir = file.getParent();
    }

    @Test
    public void missingFileReturnsNull()
    {
        file = new File("does not exist");
        assertNull(reader.read(file.getName(), 0));
    }

    @Test
    public void atOrPastEndReturnsNull()
    {
        int lastByte = (int) (file.length() - 1);
        String fileName = file.getName();
        
        assertNotNull(reader.read(fileName, lastByte));
        assertNull(reader.read(fileName, lastByte + 1));
    }

    @Test 
    public void readsData()
    {
        String output = new String(reader.read(fileName, 0));
        assertTrue(output.startsWith(contentsStart));
    }
    
    @Test
    public void readsToEnd()
    {
        int length = contentsEnd.length();
        // assumption for this test
        assertTrue(reader.maxBytes >= length);
        String output = new String(reader.read(fileName, (int) (file.length() - length)));
        assertEquals(contentsEnd, output);
    }
    
    @Test
    public void offsetWorks()
    {
        int offset = 3;
        String output = new String(reader.read(fileName, offset));
        assertTrue(output.startsWith(contentsStart.substring(3)));
    }
}
