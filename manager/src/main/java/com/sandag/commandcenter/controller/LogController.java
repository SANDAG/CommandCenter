package com.sandag.commandcenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.util.Utils;

@Controller
@RequestMapping(value = "/log")
public class LogController
{
    
    protected String urlFormat = "http://%s:8080/runner/log?filePath=%s";
    
    // path as a param instead of path var b/c periods get confused with file extensions, even when escaped
    @RequestMapping(value = "/job/{job}")
    public String displayLog(@PathVariable Job job, @RequestParam String logPath, Model model)
    {
        String url = String.format(urlFormat, job.getRunner(), Utils.urlEncode(logPath));
        model.addAttribute("url", url);
        model.addAttribute("path", logPath);
        model.addAttribute("job", job);
        return "log";
    }
    
}
