package com.sandag.commandcenter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.JobDao;

@Controller
@RequestMapping("/jobs/running")
public class JobsRunningController
{

    @Autowired
    protected JobDao jobDao;

    @RequestMapping(method = RequestMethod.GET)
    public String display(Model model)
    {
        model.addAttribute("message", "");
        List<Job> jobs = jobDao.read(Job.Status.RUNNING);
        model.addAttribute("jobs", jobs);
        return "jobsRunning";
    }


}
