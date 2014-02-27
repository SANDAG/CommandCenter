package com.sandag.commandcenter.testutils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public final class TestUtils
{

    private TestUtils()
    {
    }

    public static File getFile(String path)
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

    public static void removeHackFile(String path)
    {
        File file = getFile(path);
        File[] children = file.listFiles();
        for (File f : children)
        {
            if ("hack".equals(f.getName()))
            {
                f.delete();
            }
        }
    }

    public static Properties getProperties(String... nameValuePairs)
    {
        if (nameValuePairs.length % 2 != 0)
        {
            throw new RuntimeException("getProperties expecting name0, value0, name1, value1, ...");
        }
        Properties properties = new Properties();
        for (int i = 0; i < nameValuePairs.length; i += 2)
        {
            properties.setProperty(nameValuePairs[i], nameValuePairs[i + 1]);
        }
        return properties;
    }

}
