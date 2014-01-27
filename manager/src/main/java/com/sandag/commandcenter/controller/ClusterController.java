package com.sandag.commandcenter.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sandag.commandcenter.model.Cluster;
import com.sandag.commandcenter.persistence.ClusterDao;

@Controller
@RequestMapping("/admin/cluster")
public class ClusterController
{

    @Autowired
    protected ClusterDao clusterDao;

    @RequestMapping(method = RequestMethod.GET)
    public String displayEmptyClusterForm(Model model)
    {
        model.addAttribute("message", "Add a cluster");
        model.addAttribute("cluster", new Cluster());
        return "cluster";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addCluster(@Valid Cluster cluster, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("message", "Please fix the error(s) below and resubmit");
        } else
        {
            clusterDao.create(cluster);
            return "redirect:clusters";
        }
        return "cluster";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/toggle/{cluster}")
    @ResponseBody
    public String toggleActive(@PathVariable Cluster cluster)
    {
        cluster.setActive(!cluster.getActive());
        clusterDao.update(cluster);
        return "Cluster deactivated";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{cluster}")
    @ResponseBody
    public String delete(@PathVariable Cluster cluster)
    {
        clusterDao.delete(cluster);
        return "Cluster deleted";
    }

}
