package com.sandag.commandcenter.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;

@Service
public class JobAccessManager
{

    @Value(value = "${adminRole}")
    protected String adminRole;

    public boolean canUpdate(HttpServletRequest request, Job job, Principal principal)
    {
        return request.isUserInRole(adminRole) || principal.getName().equals(job.getUser().getPrincipal());
    }
}
