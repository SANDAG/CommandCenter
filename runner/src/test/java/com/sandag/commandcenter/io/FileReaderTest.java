package com.sandag.commandcenter.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Before;
import org.junit.Test;

public class FileReaderTest
{

    private FileReader reader;
    private URL fileUrl = ClassLoader.getSystemResource("logFile.log");
    private String contentsStart = "1 Here is some data";
    private String contentsEnd = "browser in chunks...\r\n";
    private File file;
    
    @Before
    public void setup() throws URISyntaxException
    {
        file = new File(fileUrl.toURI());
        reader = new FileReader();
        reader.fileInputStreamFactory = new FileInputStreamFactory();
    }
    
    @Test
    public void atOrPastEndReturnsNull()
    {
        int lastByte = (int) (file.length() - 1);
        assertNotNull(reader.read(file, lastByte));
        assertNull(reader.read(file, lastByte + 1));
    }
    
    @Test
    public void missingFileReturnsNull()
    {
        file = new File("does not exist");
        assertNull(reader.read(file, 0));
    }

    @Test 
    public void readsData()
    {
        String output = new String(reader.read(file, 0));
        assertTrue(output.startsWith(contentsStart));
    }
    
    @Test
    public void readsToEnd()
    {
        int length = contentsEnd.length();
        // assumption for this test
        assertTrue(reader.maxBytes >= length);
        String output = new String(reader.read(file, (int) (file.length() - length)));
        assertEquals(contentsEnd, output);
    }

    @Test
    public void offsetWorks()
    {
        int offset = 3;
        String output = new String(reader.read(file, offset));
        assertTrue(output.startsWith(contentsStart.substring(3)));
    }

    @Test
    public void ioExceptionReturnsNull() throws IOException
    {
        FileInputStreamFactory factory = mock(FileInputStreamFactory.class);
        reader.fileInputStreamFactory = factory;
        
        FileInputStream is = mock(FileInputStream.class);
        when(factory.getFor((File) anyObject())).thenReturn(is);
    
        FileChannel channel = mock(FileChannel.class);
        when(is.getChannel()).thenReturn(channel);
        when(channel.read((ByteBuffer) anyObject(), anyLong())).thenThrow(new IOException());
        
        assertNull(reader.read(file, 0));
    }

}
