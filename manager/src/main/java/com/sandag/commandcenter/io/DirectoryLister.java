package com.sandag.commandcenter.io;

import java.io.File;
import java.io.FileFilter;

public class DirectoryLister
{
    public File[] getChildDirs(String path)
    {
        File parentDir = new File(path);
        if (!(parentDir.exists() && parentDir.isDirectory()))
        {
            return new File[0];
        }
        return parentDir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File child)
            {
                return child.isDirectory();
            }

        });

    }

}
