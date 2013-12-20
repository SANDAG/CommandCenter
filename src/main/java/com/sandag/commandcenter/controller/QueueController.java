package com.sandag.commandcenter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.JobService;

@Controller
@RequestMapping("/queue")
public class QueueController
{

    @Autowired
    protected JobService jobService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String display(Model model)
    {
        model.addAttribute("message", "View the queue");
        List<Job> jobs = jobService.readAll();
        model.addAttribute("jobs", jobs);
        return "queue";
    }

}
