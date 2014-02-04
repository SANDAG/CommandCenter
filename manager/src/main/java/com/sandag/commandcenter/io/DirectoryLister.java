package com.sandag.commandcenter.io;

import java.io.File;
import java.io.FileFilter;

import org.springframework.stereotype.Component;

@Component
public class DirectoryLister
{
    public String[] getChildDirs(String path)
    {
        File parentDir = new File(path);
        if (!(parentDir.exists() && parentDir.isDirectory()))
        {
            return new String[0];
        }
        File[] dirs = parentDir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File child)
            {
                return child.isDirectory();
            }

        });
        String[] names = new String[dirs.length];
        for (int i = 0; i < dirs.length; i++)
        {
            names[i] = dirs[i].getName();
        }
        return names;
    }

}
