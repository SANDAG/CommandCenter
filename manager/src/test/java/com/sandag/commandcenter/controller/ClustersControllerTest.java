package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sandag.commandcenter.model.Cluster;
import com.sandag.commandcenter.persistence.ClusterDao;

public class ClustersControllerTest
{

    private ClustersController controller;
    private Model model;
    private ClusterDao clusterDao;

    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
        controller = new ClustersController();
        clusterDao = mock(ClusterDao.class);
        controller.clusterDao = clusterDao;
    }

    @Test
    public void viewWorks()
    {
        List<Cluster> clusters = Arrays.asList(new Cluster[] {new Cluster(), new Cluster() });
        when(clusterDao.readAll()).thenReturn(clusters);

        assertEquals("clusters", controller.display(model));
        assertTrue(model.containsAttribute("navbarSelection"));
        assertEquals(clusters, model.asMap().get("clusters"));
    }
}
