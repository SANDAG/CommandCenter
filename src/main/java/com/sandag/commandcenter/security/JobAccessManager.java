package com.sandag.commandcenter.security;

import java.security.Principal;

import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;

@Service
public class JobAccessManager
{

    public boolean canDelete(Job job, Principal principal) 
    {
        return principal.getName().equals(job.getUser().getPrincipal());
    }
}
