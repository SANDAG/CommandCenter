package com.sandag.commandcenter.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;
import com.sandag.commandcenter.persistence.JobDao;
import com.sandag.commandcenter.persistence.UserDao;
import com.sandag.commandcenter.security.JobAccessManager;

@Controller
@RequestMapping("/job")
public class JobController
{

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected JobDao jobDao;

    @Autowired
    protected JobAccessManager jobAccessManager;

    @RequestMapping(method = RequestMethod.GET)
    public String displayEmptyJobForm(Model model)
    {
        model.addAttribute("message", "Add a job to the queue");
        model.addAttribute("job", new Job());
        model.addAttribute("modelNameMappings", getModelNamesMap());
        return "job";
    }

    // WARNING does not handle editing
    // for editing, user should not be set to the principal if already set (if admins can edit any job)
    @RequestMapping(method = RequestMethod.POST)
    public String addJob(@Valid Job job, BindingResult result, Model model, Principal principal)
    {
        model.addAttribute("modelNameMappings", getModelNamesMap());

        if (result.hasErrors())
        {
            model.addAttribute("message", "Please fix the error(s) below and resubmit your job");
        } else
        {
            User user = userDao.fetchOrCreate(principal.getName());
            job.setUser(user);
            jobDao.create(job);
            return "redirect:jobs/queued?highlight=" + job.getId();
        }
        return "job";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{job}")
    @ResponseBody
    @PreAuthorize("@jobAccessManager.canUpdate(#request, @jobDao.read(#id), #principal)")
    public String deleteQueuedJob(@PathVariable Job job, Principal principal, HttpServletRequest request)
    {
        return jobDao.deleteIfQueued(job) ? "Job deleted" : "Job was no longer queued";
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cancel/{job}")
    @ResponseBody
    @PreAuthorize("@jobAccessManager.canUpdate(#request, @jobDao.read(#id), #principal)")
    public String cancelRunningJob(@PathVariable Job job, Principal principal, HttpServletRequest request)
    {
        return jobDao.cancelIfRunning(job) ? "Job cancelled" : "Job was no longer running";
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
