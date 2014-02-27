package com.sandag.commandcenter.io;

import org.springframework.stereotype.Component;

import com.sandag.commandcenter.model.Job.Model;

@Component
public class FileListerFactory
{

    public FileLister getFileLister(Model model, String logFileNames)
    {
        FileLister lister = new FileLister();
        lister.setLogFileNames(logFileNames);
        lister.setModel(model);
        return lister;
    }

}
