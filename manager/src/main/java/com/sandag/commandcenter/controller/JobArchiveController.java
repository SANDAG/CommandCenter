package com.sandag.commandcenter.controller;

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
import com.sandag.commandcenter.persistence.JobDao;
import com.sandag.commandcenter.security.RoleChecker;

import static com.sandag.commandcenter.model.Job.Status.ARCHIVED;
import static com.sandag.commandcenter.model.Job.Status.FINISHED;

@Controller
@RequestMapping("/admin/job")
public class JobArchiveController
{
    @Autowired
    protected JobDao jobDao;

    @Autowired
    protected RoleChecker roleChecker;

    @RequestMapping(method = RequestMethod.GET, value = "/archive/{job}")
    public String displayForm(@PathVariable Job job, Model model)
    {
        model.addAttribute("message", "Archive this job");
        model.addAttribute("job", job);
        return "jobArchive";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/archive/{job}")
    @PreAuthorize("@roleChecker.isAdmin(#request)")
    public String archiveJob(@Valid Job job, BindingResult result, Model model, HttpServletRequest request)
    {
        if (result.hasErrors())
        {
            model.addAttribute("message", "Please fix the error(s) below and resubmit");
            return "jobArchive";
        } else
        {
            job.setStatus(ARCHIVED);
            jobDao.update(job);
            return "redirect:/jobs/finished";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/unarchive/{job}")
    @ResponseBody    
    @PreAuthorize("@roleChecker.isAdmin(#request)")
    public String unarchiveJob(@Valid Job job, HttpServletRequest request)
    {
        job.setStatus(FINISHED);
        jobDao.update(job);
        return "Job unarchived";
    }
}
