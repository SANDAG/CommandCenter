package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.sandag.commandcenter.controller.util.HeaderSetter;

public class LogFileReaderControllerTest
{

    private LogFileReaderController controller;
    private File file;
    private String fileName;
    private String contentsStart = "1 Here is some data";
    private String contentsEnd = "browser in chunks...\r\n";
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;    
    private HeaderSetter mockHeaderSetter;
    
    @Before
    public void setup() throws URISyntaxException
    {
        controller = new LogFileReaderController();
        URL fileUrl = ClassLoader.getSystemResource("logFile.log");
        file = new File(fileUrl.toURI());
        fileName = file.getName();
        controller.dir = file.getParent();
        mockHeaderSetter = mock(HeaderSetter.class);
        controller.headerSetter = mockHeaderSetter;
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    public void missingFileReturnsNull()
    {
        file = new File("does not exist");
        assertNull(controller.read(file.getName(), 0, mockRequest, mockResponse));
        verify(mockHeaderSetter).setCrossOriginHeader(mockRequest, mockResponse);
    }

    @Test
    public void atOrPastEndReturnsNull()
    {
        int lastByte = (int) (file.length() - 1);
        String name = file.getName();
        
        assertNotNull(controller.read(name, lastByte, mockRequest, mockResponse));
        assertNull(controller.read(name, lastByte + 1, mockRequest, mockResponse));
        verify(mockHeaderSetter, times(2)).setCrossOriginHeader(mockRequest, mockResponse);
    }

    @Test 
    public void readsData()
    {
        String output = new String(controller.read(fileName, 0, mockRequest, mockResponse));
        assertTrue(output.startsWith(contentsStart));
        verify(mockHeaderSetter).setCrossOriginHeader(mockRequest, mockResponse);
    }
    
    @Test
    public void readsToEnd()
    {
        int length = contentsEnd.length();
        // assumption for this test
        assertTrue(controller.maxBytes >= length);
        String output = new String(controller.read(fileName, (int) (file.length() - length), null, null));
        assertEquals(contentsEnd, output);
    }
    
    @Test
    public void offsetWorks()
    {
        int offset = 3;
        String output = new String(controller.read(fileName, offset, null, null));
        assertTrue(output.startsWith(contentsStart.substring(3)));
    }
}
