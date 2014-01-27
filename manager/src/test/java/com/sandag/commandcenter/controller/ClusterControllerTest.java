package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.sandag.commandcenter.model.Cluster;
import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.persistence.ClusterDao;
import com.sandag.commandcenter.persistence.JobDao;

public class ClusterControllerTest
{

    private ClusterController controller;

    private Model model;

    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
        controller = new ClusterController();
    }

    @Test
    public void emptyPageShows()
    {
        assertEquals("cluster", controller.displayEmptyClusterForm(model));
        assertTrue(model.containsAttribute("cluster"));
        assertTrue(model.containsAttribute("message"));
    }

    @Test
    public void invalidClusterGetsFailureMessage()
    {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        assertEquals("cluster", controller.addCluster(null, result, model));
        assertTrue(((String) model.asMap().get("message")).matches(".*fix.*error.*"));
    }

    @Test
    public void validClusterGetsSaved()
    {
        Cluster cluster = mock(Cluster.class);
        ClusterDao clusterDao = mock(ClusterDao.class);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        controller.clusterDao = clusterDao;
        assertEquals("redirect:clusters", controller.addCluster(cluster, result, model));
        verify(clusterDao).create(cluster);
    }

    @Test
    public void clusterActivates()
    {
        ClusterDao clusterDao = mock(ClusterDao.class);
        controller.clusterDao = clusterDao;
        Cluster cluster = new Cluster();
        cluster.setActive(true);
        controller.toggleActive(cluster);
        assertFalse(cluster.getActive());
        verify(clusterDao).update(cluster);
    }
    
    @Test
    public void clusterDectivates()
    {
        ClusterDao clusterDao = mock(ClusterDao.class);
        controller.clusterDao = clusterDao;
        Cluster cluster = new Cluster();
        cluster.setActive(false);
        controller.toggleActive(cluster);
        assertTrue(cluster.getActive());
        verify(clusterDao).update(cluster);
    }

    @Test
    public void jobGetsDeleted()
    {
        ClusterDao clusterDao = mock(ClusterDao.class);
        controller.clusterDao = clusterDao;
        Cluster cluster = new Cluster();
        assertEquals("Cluster deleted", controller.delete(cluster));
        verify(clusterDao).delete(cluster);
    }

}
