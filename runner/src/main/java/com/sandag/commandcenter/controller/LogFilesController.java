package com.sandag.commandcenter.controller;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sandag.commandcenter.io.FileLister;
import com.sandag.commandcenter.model.Job;

@Controller
public class LogFilesController
{

    @Autowired
    protected ApplicationContext context;

    private static final Logger LOGGER = Logger.getLogger(FileLister.class.getName());
    protected Map<Job.Model, FileLister> listers = new HashMap<Job.Model, FileLister>();

    @PostConstruct
    public void initialize() throws UnknownHostException
    {
        Map<String, FileLister> listerBeans = context.getBeansOfType(FileLister.class);
        for (FileLister lister : listerBeans.values())
        {
            listers.put(lister.getModel(), lister);
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
