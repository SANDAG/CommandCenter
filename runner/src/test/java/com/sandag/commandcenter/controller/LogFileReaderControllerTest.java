package com.sandag.commandcenter.controller;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.sandag.commandcenter.controller.util.HeaderSetter;
import com.sandag.commandcenter.io.FileReader;

public class LogFileReaderControllerTest
{

    private LogFileReaderController controller;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;    
    private HeaderSetter mockHeaderSetter;
    private FileReader mockFileReader;
    
    @Before
    public void setup()
    {
        controller = new LogFileReaderController();
        mockHeaderSetter = mock(HeaderSetter.class);
        mockFileReader = mock(FileReader.class);
        controller.headerSetter = mockHeaderSetter;
        controller.fileReader = mockFileReader;
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    public void setsCrossOriginHeader()
    {
        controller.read(null, 0, mockRequest, mockResponse);
        verify(mockHeaderSetter).setCrossOriginHeader(mockRequest, mockResponse);
    }
    
    @Test
    public void callsFileReader()
    {
        final String dir = "dir";
        final String filePath = "another dir/filename.ext";
        int startByte = 0;
        controller.dir = dir;
        controller.read(filePath, startByte, mockRequest, mockResponse);
        verify(mockFileReader).read(argThat(new ArgumentMatcher<File>()
        {
            @Override
            public boolean matches(Object file)
            {
                // new File and getPath to be os-independent
                String expectedPath = new File(String.format("%s/%s", dir, filePath)).getPath();
                return expectedPath.equals(((File) file).getPath());
            }
        }), eq(startByte));
    }
    
}
