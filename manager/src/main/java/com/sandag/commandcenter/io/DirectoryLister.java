package com.sandag.commandcenter.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DirectoryLister
{
    private static final Logger LOGGER = Logger.getLogger(DirectoryLister.class.getName());

    public String[] getChildDirs(String path)
    {
        File parentDir = new File(path);
        if (!(parentDir.exists() && parentDir.isDirectory()))
        {
            LOGGER.debug(String.format("Unable to open '%s' ('%s'), exists: %s, is dir: %s", path, parentDir.getAbsolutePath(), parentDir.exists(),
                    parentDir.isDirectory()));
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
        // empty directory (instead of files and no subdirs)
        if (dirs == null)
        {
            dirs = new File[0];
        }
        String[] names = new String[dirs.length];
        for (int i = 0; i < dirs.length; i++)
        {
            names[i] = dirs[i].getName();
        }
        return names;
    }

}
