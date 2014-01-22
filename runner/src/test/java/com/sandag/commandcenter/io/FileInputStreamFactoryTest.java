package com.sandag.commandcenter.io;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class FileInputStreamFactoryTest
{

    private FileInputStreamFactory factory = new FileInputStreamFactory();

    @Test
    public void getForNonexistingFileThrowsFileNotFound() throws IOException
    {
        File nonexistingFile = new File("/missing/path/filename.ext");
        try (FileInputStream is = factory.getFor(nonexistingFile))
        {
            fail("FileNotFoundException expected");
        } catch (FileNotFoundException e)
        {
            assertTrue(true); // for checkstyle
        } 
    }

    @Test
    public void getsStream() throws FileNotFoundException, IOException, URISyntaxException
    {
        URL fileUrl = FileInputStreamFactoryTest.class.getResource("/development/logFile.log");
        File existingFile = new File(fileUrl.getPath());
        try (FileInputStream is = factory.getFor(existingFile))
        {
            is.available(); // no exception thrown passes
        }
    }

}
