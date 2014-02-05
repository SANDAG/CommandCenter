package com.sandag.commandcenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sandag.commandcenter.io.DirectoryLister;

@Controller
public class DirectoryListerController
{
    @Autowired
    protected DirectoryLister dirLister;

    @ResponseBody
    @RequestMapping("/directories")
    public String[] listDirectories(@RequestParam String parentDir)
    {
        return dirLister.getChildDirs(parentDir);
    }

}
