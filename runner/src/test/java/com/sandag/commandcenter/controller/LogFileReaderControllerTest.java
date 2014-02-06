package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
        controller.read("", 0, mockRequest, mockResponse);
        verify(mockHeaderSetter).setCrossOriginHeader(mockRequest, mockResponse);
    }
    
    @Test
    public void callsFileReader()
    {
        final String filePath = "another dir/filename.ext";
        int startByte = 0;
        controller.read(filePath, startByte, mockRequest, mockResponse);
        ArgumentCaptor<File> fileArg = ArgumentCaptor.forClass(File.class);
        verify(mockFileReader).read(fileArg.capture(), eq(startByte));
        assertEquals(filePath, fileArg.getValue().getPath().replace(File.separatorChar, '/'));
    }
    
}
