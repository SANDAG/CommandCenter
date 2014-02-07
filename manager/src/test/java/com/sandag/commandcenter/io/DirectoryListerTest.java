package com.sandag.commandcenter.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sandag.commandcenter.testutils.TestUtils;

public class DirectoryListerTest
{
    private DirectoryLister lister = new DirectoryLister();

    @BeforeClass
    public static void removeHackFiles()
    {
        TestUtils.removeHackFile("Dir0/Dir0_0");
        TestUtils.removeHackFile("Dir0/Dir0_1");
    }

    @Test
    public void emptyForNotExists()
    {
        String emptyPath = "/no/file/here/or/at/least/I/hope/not";
        assertEquals(0, lister.getChildDirs(emptyPath).length);
    }

    @Test
    public void emptyForNonDirInput()
    {
        File file = TestUtils.getFile("OuterFile");
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        assertEquals(0, lister.getChildDirs(file.getAbsolutePath()).length);
    }

    @Test
    public void emptyForNoChildren()
    {
        File file = TestUtils.getFile("Dir0/Dir0_1");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
        assertEquals(0, file.list().length);
        assertEquals(0, lister.getChildDirs(file.getAbsolutePath()).length);
    }

    @Test
    public void getsChildDirs()
    {
        File file = TestUtils.getFile("Dir0");
        assertArrayEquals(new String[] {"Dir0_0", "Dir0_1" }, lister.getChildDirs(file.getAbsolutePath()));
    }

}
