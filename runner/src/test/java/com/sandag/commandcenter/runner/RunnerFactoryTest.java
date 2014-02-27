package com.sandag.commandcenter.runner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sandag.commandcenter.model.Job.Model;

public class RunnerFactoryTest
{

    private RunnerFactory factory = new RunnerFactory();
    
    @Test 
    public void getsFactory()
    {
        String commandLine = "sdfasdfasdf";
        Model model = Model.ABM;        
        Runner runner = factory.getRunner(commandLine, model);
        assertEquals(commandLine, runner.commandLine);
        assertEquals(model, runner.model);
    }
}
