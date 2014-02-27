package com.sandag.commandcenter.controller;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sandag.commandcenter.io.FileLister;
import com.sandag.commandcenter.io.FileListerFactory;
import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.Job.Model;

@Controller
public class LogFilesController
{

    @Autowired
    protected Properties runnerProperties;

    private static final Logger LOGGER = Logger.getLogger(FileLister.class.getName());
    protected Map<Job.Model, FileLister> listers = new HashMap<Job.Model, FileLister>();

    @Autowired
    protected FileListerFactory fileListerFactory;    
    
    @PostConstruct
    public void initialize() throws UnknownHostException
    {
        
        for (Model model : Job.Model.values())
        {
        
            String fileNames = runnerProperties.getProperty(model.toString() + "_logFileNames");
            if (fileNames != null)
            {
                listers.put(model, fileListerFactory.getFileLister(model, fileNames));
            } else
            {
                LOGGER.warn("No log file names defined for model " + model);
            }
        }
    }

    @RequestMapping(value = "/logs/job/{job}")
    @ResponseBody
    public List<String> listFiles(@PathVariable Job job)
    {
        FileLister lister = listers.get(job.getModel());
        if (lister == null)
        {
            LOGGER.warn(String.format("No file lister for '%s' in '%s'", job.getModel(), listers.keySet()));
            return null;
        }
        return lister.listFiles(job);
    }

}
