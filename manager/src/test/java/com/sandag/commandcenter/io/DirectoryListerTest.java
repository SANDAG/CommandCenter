package com.sandag.commandcenter.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class DirectoryListerTest
{
    private DirectoryLister lister = new DirectoryLister();

    @Test
    public void emptyForNotExists()
    {
        String emptyPath = "/no/file/here/or/at/least/I/hope/not";
        assertEquals(0, lister.getChildDirs(emptyPath).length);
    }

    @Test
    public void emptyForNonDirInput()
    {
        File file = getFile("OuterFile");
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        assertEquals(0, lister.getChildDirs(file.getAbsolutePath()).length);
    }

    @Test
    public void emptyForNoChildren()
    {
        File file = getFile("Dir0/Dir0_1");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
        assertEquals(0, file.list().length);
        assertEquals(0, lister.getChildDirs(file.getAbsolutePath()).length);
    }

    @Test
    public void getsChildDirs()
    {
        File file = getFile("Dir0");
        assertArrayEquals(new String[] {"Dir0_0", "Dir0_1"}, lister.getChildDirs(file.getAbsolutePath()));
    }

    // support
    public File getFile(String path)
    {
        URL url = ClassLoader.getSystemResource(path);
        try
        {
            return new File(url.toURI());
        } catch (URISyntaxException e)
        {
            throw new RuntimeException("Shouldn't happen, but we'll still fail if it does");
        }
    }

}
