package com.sandag.commandcenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.sandag.commandcenter.model.Job;

@Controller
public class LogFileListerController
{

    @Autowired
    protected RestTemplate restTemplate;
    
    @RequestMapping(value = "/logs/job/{job}")
    public String listLogs(@PathVariable Job job, Model model)
    {
        model.addAttribute("job", job);
        String[] filePaths = restTemplate.getForObject("http://{host}:8080/runner/logs/job/{jobId}", String[].class, job.getRunner(), job.getId());
        model.addAttribute("logs", filePaths);
        return "logs";
    }
}
