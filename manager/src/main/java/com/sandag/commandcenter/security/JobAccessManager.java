package com.sandag.commandcenter.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;

@Service
public class JobAccessManager
{
    @Autowired
    protected RoleChecker roleChecker;

    public boolean canUpdate(HttpServletRequest request, Job job, Principal principal)
    {
        if (job == null)
        {
            return false;
        }
        return roleChecker.isAdmin(request) || principal.getName().equals(job.getUser().getPrincipal());
    }
}
