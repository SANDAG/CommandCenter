package com.sandag.commandcenter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.JobDao;
import com.sandag.commandcenter.security.JobAccessManager;

@Controller
@RequestMapping("/jobs/finished")
public class JobsFinishedController
{

    @Autowired
    protected JobDao jobDao;

    @Autowired
    private JobAccessManager manager;

    @RequestMapping(method = RequestMethod.GET)
    public String display(Model model)
    {
        model.addAttribute("jobAccessManager", manager);
        List<Job> jobs = jobDao.read(Job.Status.FINISHED, Job.Status.FAILED);
        model.addAttribute("jobs", jobs);
        return "jobsFinished";
    }

}
