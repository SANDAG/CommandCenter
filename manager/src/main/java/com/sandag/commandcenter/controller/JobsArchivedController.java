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
@RequestMapping("/jobs/archived")
public class JobsArchivedController
{

    @Autowired
    protected JobDao jobDao;

    @RequestMapping(method = RequestMethod.GET)
    public String display(Model model)
    {
        List<Job> jobs = jobDao.read(Job.Status.ARCHIVED);
        model.addAttribute("jobs", jobs);
        return "jobsArchived";
    }

}
