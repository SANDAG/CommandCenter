package com.sandag.commandcenter.controller;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sandag.commandcenter.model.Job;

@Controller
public class FileLister {

	List<String> fileNames;	
	String rootPath;
	URI root;
	
	@PostConstruct
	public void initialize()
	{
		root = new File(rootPath).toURI();
	}

	@RequestMapping(value="/logs/job/{job}")
	public String listFiles(Job job, Model model) 
	{
		List<String> files = new ArrayList<String>();
		search(rootPath + File.separatorChar + job.getScenario(), files);
		model.addAttribute("files", files);
		return "files";		
	}
	
	private void search(String path, List<String> files) {		
        File dir = new File(path);
        File[] list = dir.listFiles();

        if (list == null)
        {
        	return;
        }

        for (File f : list) {
        	if (fileNames.contains(f.getName())) {
                files.add(root.relativize(f.toURI()).getPath());
            } else  if (f.isDirectory()) {
            	search(f.getAbsolutePath(), files);
            }
        }
    }
	
}
