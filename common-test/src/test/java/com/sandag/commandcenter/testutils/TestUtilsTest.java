package com.sandag.commandcenter.testutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Properties;

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

    @Test
    public void getPropertiesFailsWithOddNvps()
    {
        try
        {
            TestUtils.getProperties("name0", "value0", "name1");
            fail("RuntimeException expected");
        } catch (RuntimeException e)
        {
            assertTrue(e.getMessage().startsWith("getProperties expecting"));
        }
    }
    
    @Test
    public void getPropertiesWorks()
    {
        Properties properties = TestUtils.getProperties("n0", "v0", "n1", "v1");
        assertEquals(2, properties.size());
        assertEquals("v0", properties.get("n0"));
        assertEquals("v1", properties.get("n1"));        
    }
    
}
