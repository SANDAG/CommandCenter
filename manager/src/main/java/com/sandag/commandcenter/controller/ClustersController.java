package com.sandag.commandcenter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sandag.commandcenter.model.Cluster;
import com.sandag.commandcenter.persistence.ClusterDao;

@Controller
@RequestMapping("/admin/clusters")
@SessionAttributes("navbarSelection")
public class ClustersController
{

    @Autowired
    protected ClusterDao clusterDao;

    @RequestMapping(method = RequestMethod.GET)
    public String display(Model model)
    {
        List<Cluster> clusters = clusterDao.readAll();
        model.addAttribute("clusters", clusters);
        model.addAttribute("navbarSelection", "clusters");
        return "clusters";
    }

}
