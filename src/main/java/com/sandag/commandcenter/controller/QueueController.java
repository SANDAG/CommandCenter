package com.sandag.commandcenter.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.JobService;
import com.sandag.commandcenter.security.JobAccessManager;

@Controller
@RequestMapping("/queue")
public class QueueController
{

    @Autowired
    protected JobService jobService;
    
    @Autowired
    private JobAccessManager manager;
    
    @RequestMapping(method = RequestMethod.GET)
    public String display(Model model, Principal principal)
    {
        model.addAttribute("message", "View the queue");
        List<Job> jobs = jobService.readAll();
        model.addAttribute("jobs", jobs);
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("jobAccessManager", manager);
        return "queue";
    }
    
}
