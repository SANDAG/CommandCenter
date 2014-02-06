package com.sandag.commandcenter.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;

import com.sandag.commandcenter.model.Job;

public class FileLister
{

    protected Job.Model model;
    protected List<String> logFileNames;

    private static final Logger LOGGER = Logger.getLogger(FileLister.class.getName());

    public List<String> listFiles(@PathVariable Job job)
    {
        File scenarioDir = new File(job.getScenarioLocation());
        LOGGER.debug(String.format("Listing files for: '%s'", scenarioDir.getPath()));
        return search(scenarioDir);
    }

    private List<String> search(File dir)
    {
        List<String> files = new ArrayList<String>();
        search(dir, files);
        return files;
    }

    private void search(File dir, List<String> files)
    {
        File[] list = dir.listFiles();

        if (list == null)
        {
            return;
        }

        for (File f : list)
        {
            if (logFileNames.contains(f.getName()))
            {
                files.add(f.getAbsolutePath());
            } else if (f.isDirectory())
            {
                search(f, files);
            }
        }
    }

    public Job.Model getModel()
    {
        return model;
    }

    public void setModel(Job.Model model)
    {
        this.model = model;
    }

    public void setLogFileNames(List<String> logFileNames)
    {
        this.logFileNames = logFileNames;
    }

}
