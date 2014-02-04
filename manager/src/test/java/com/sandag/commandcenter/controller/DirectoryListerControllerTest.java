package com.sandag.commandcenter.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import com.sandag.commandcenter.io.DirectoryLister;

public class DirectoryListerControllerTest
{
    @Test
    public void testListDirectories()
    {
        String path = "ASDFASDF";
        String[] dirNames = new String[] {"a", "c", "e" };
        DirectoryListerController controller = new DirectoryListerController();
        DirectoryLister lister = mock(DirectoryLister.class);
        controller.dirLister = lister;
        when(lister.getChildDirs(path)).thenReturn(dirNames);
        
        assertArrayEquals(dirNames, controller.listDirectories(path));
    }
}
