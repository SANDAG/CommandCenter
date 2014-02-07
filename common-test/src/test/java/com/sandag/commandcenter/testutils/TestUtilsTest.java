package com.sandag.commandcenter.testutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class TestUtilsTest
{
    @Test
    public void getsFile()
    {
        File file = TestUtils.getFile("folder/otherFile");
        assertTrue(file.exists());
    }

    @Test
    public void removesHackFile()
    {
        String folderPath = "folder";
        String filePath = folderPath + "/hack";
        File file = TestUtils.getFile(filePath);
        assertTrue(file.exists());

        TestUtils.removeHackFile(folderPath);
        assertFalse(file.exists());
    }

}
