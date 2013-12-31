package com.sandag.commandcenter.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;
import com.sandag.commandcenter.persistence.JobService;
import com.sandag.commandcenter.persistence.UserService;
import com.sandag.commandcenter.security.JobAccessManager;

@Controller
@RequestMapping("/queue")
public class QueueController
{

    @Autowired
    protected JobService jobService;

    @Autowired
    protected UserService userService;

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
        model.addAttribute("moveUpIds", getCanMoveUpJobIds(jobs, principal));
        model.addAttribute("moveDownIds", getCanMoveDownJobIds(jobs, principal));
        return "queue";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/move")
    @PreAuthorize("@jobAccessManager.canUpdate(@jobService.read(#id), #principal)")
    public void move(@PathVariable Integer id, @RequestParam(defaultValue = "true") boolean moveUp, Principal principal)
    {
        // no-op if either is null (someone else changed the queue order or deleted a job?)
        Job chosen = jobService.read(id);
        if (chosen == null)
        {
            return;
        }
        Job other = moveUp ? jobService.getMoveableJobBefore(chosen) : jobService.getMoveableJobAfter(chosen);
        if (other == null)
        {
            return;
        }

        jobService.updateWithSwappedQueuePositions(chosen, other);
    }

    protected List<Integer> getCanMoveUpJobIds(List<Job> jobs, Principal principal)
    {
        List<Integer> ids = getOwnedJobIds(jobs, principal);
        if (ids.size() > 0)
        {
            // first is at top and can't move up
            ids.remove(0);
        }
        return ids;
    }

    protected List<Integer> getCanMoveDownJobIds(List<Job> jobs, Principal principal)
    {
        List<Integer> ids = getOwnedJobIds(jobs, principal);
        if (ids.size() > 0)
        {
            // last is at bottom and can't move down
            ids.remove(ids.size() - 1);
        }
        return ids;
    }

    private List<Integer> getOwnedJobIds(List<Job> jobs, Principal principal)
    {
        List<Integer> ids = new ArrayList<Integer>();
        User user = userService.fetchOrCreate(principal.getName());
        for (Job job : jobs)
        {
            if (user.isSame(job.getUser()))
            {
                ids.add(job.getId());
            }
        }
        return ids;
    }

}
