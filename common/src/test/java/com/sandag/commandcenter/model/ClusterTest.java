package com.sandag.commandcenter.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClusterTest
{

    @Test
    public void nameGetsSetWithLowercase()
    {
        String mixedCaseName = "ASDasdfASDFasdfASDF";
        Cluster cluster = new Cluster();
        cluster.setName(mixedCaseName);
        assertEquals(mixedCaseName.toLowerCase(), cluster.getName());
    }
}
