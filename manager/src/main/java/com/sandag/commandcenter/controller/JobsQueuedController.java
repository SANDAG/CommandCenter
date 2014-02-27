package com.sandag.commandcenter.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;
import com.sandag.commandcenter.persistence.JobDao;
import com.sandag.commandcenter.persistence.UserDao;
import com.sandag.commandcenter.security.JobAccessManager;

@Controller
@RequestMapping({"/", "/jobs/queued" })
@SessionAttributes("navbarSelection")
public class JobsQueuedController
{

    @Autowired
    protected JobDao jobDao;

    @Autowired
    protected UserDao userDao;

    @Autowired
    private JobAccessManager manager;

    @RequestMapping(method = RequestMethod.GET)
    public String display(Model model, Principal principal)
    {
        List<Job> jobs = jobDao.readQueued();
        model.addAttribute("jobs", jobs);
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("jobAccessManager", manager);
        model.addAttribute("moveUpIds", getCanMoveUpJobIds(jobs, principal));
        model.addAttribute("moveDownIds", getCanMoveDownJobIds(jobs, principal));
        model.addAttribute("navbarSelection", "jobs");
        return "jobsQueued";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{job}/move", produces="text/plain")
    @ResponseBody // used for debugging only
    @PreAuthorize("@jobAccessManager.canUpdate(#request, #job, #principal)")
    public String move(@PathVariable Job job, @RequestParam(defaultValue = "true") boolean moveUp, Principal principal, HttpServletRequest request)
    {
        // no-op if either is null (someone else changed the queue order or deleted a job?)
        if (job == null)
        {
            return "not swapped - selected job was null";
        }
        Job other = moveUp ? jobDao.getMoveableJobBefore(job) : jobDao.getMoveableJobAfter(job);
        if (other == null)
        {
            return "not swapped - no job to swap with";
        }

        jobDao.updateWithSwappedQueuePositions(job, other);
        return "swapped";
    }

    protected List<Integer> getCanMoveUpJobIds(List<Job> jobs, Principal principal)
    {
        List<Integer> ids = getOwnedQueuedJobIds(jobs, principal);
        if (ids.size() > 0)
        {
            // first is at top and can't move up
            ids.remove(0);
        }
        return ids;
    }

    protected List<Integer> getCanMoveDownJobIds(List<Job> jobs, Principal principal)
    {
        List<Integer> ids = getOwnedQueuedJobIds(jobs, principal);
        if (ids.size() > 0)
        {
            // last is at bottom and can't move down
            ids.remove(ids.size() - 1);
        }
        return ids;
    }

    private List<Integer> getOwnedQueuedJobIds(List<Job> jobs, Principal principal)
    {
        List<Integer> ids = new ArrayList<Integer>();
        User user = userDao.fetchOrCreate(principal.getName());
        for (Job job : jobs)
        {
            if (user.isSame(job.getUser()) && Job.Status.QUEUED == job.getStatus())
            {
                ids.add(job.getId());
            }
        }
        return ids;
    }

}
