package com.sandag.commandcenter.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;
import com.sandag.commandcenter.persistence.JobService;
import com.sandag.commandcenter.persistence.UserService;

@Controller
@RequestMapping("/job")
public class JobController
{

    @Autowired
    protected UserService userService;

    @Autowired
    protected JobService jobService;

    @RequestMapping(method = RequestMethod.GET)
    public String displayEmptyJobForm(Model model)
    {
        model.addAttribute("message", "Add a job to the queue");
        model.addAttribute("job", new Job());
        model.addAttribute("modelNameMappings", getModelNamesMap());
        return "job";
    }

    // WARNING does not handle editing
    //   for editing, user should not be set to the principal if already set (if admins can edit any job)
    @RequestMapping(method = RequestMethod.POST)
    public String submitJob(@Valid Job job, BindingResult result, Model model, Principal principal)
    {
        model.addAttribute("modelNameMappings", getModelNamesMap());
        
        if (result.hasErrors())
        {       
            model.addAttribute("message", "Please fix the error(s) below and resubmit your job");
        } else
        {
            User user = userService.fetchOrCreate(principal.getName());
            job.setUser(user);
            jobService.create(job);
            model.addAttribute("message", "Successfully queued job");
            // TODO return view "jobQueue" here instead (when that page is ready)?
        }
        return "job";
    }

    Map<String, String> getModelNamesMap()
    {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("", "Please select a model");
        for (Job.Model m : Job.Model.values())
        {
            map.put(m.toString(), m.toString());
        }
        return map;
    }

}
