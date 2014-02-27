package com.sandag.commandcenter.io;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.sandag.commandcenter.model.Job.Model;

public class FileListerFactoryTest
{

    private FileListerFactory factory = new FileListerFactory();
    
    @Test 
    public void getsFactory()
    {
        String fileNames = "sdfasdfasdf,asdf,asdf";
        Model model = Model.ABM;        
        FileLister lister = factory.getFileLister(model, fileNames);
        assertEquals(Arrays.asList(fileNames.split(",")), lister.logFileNames);
        assertEquals(model, lister.model);
    }
}
