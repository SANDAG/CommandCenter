package com.sandag.commandcenter.testutils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

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
}
