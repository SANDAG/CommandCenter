package com.sandag.commandcenter.runner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sandag.commandcenter.model.Job;

public class CommandReplacerTest
{

    private CommandReplacer replacer = new CommandReplacer();
    
    @Test
    public void replacesLocation()
    {
        String prefix = "asSADFasdf";
        String suffix = "Asdfasdf";
        String command = prefix + CommandReplacer.SCENARIO_LOCATION + suffix;
        
        String location = "Belgium";
        Job job = new Job();
        job.setScenarioLocation(location);
        
        assertEquals(prefix + location + suffix, replacer.replace(command, job));
    }
    
}
