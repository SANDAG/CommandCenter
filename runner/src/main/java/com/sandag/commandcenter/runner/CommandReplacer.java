package com.sandag.commandcenter.runner;

import org.springframework.stereotype.Component;

import com.sandag.commandcenter.model.Job;

@Component
public class CommandReplacer
{

    public static final String SCENARIO_LOCATION = "SCENARIO_LOCATION";

    public String replace(String command, Job job)
    {
        return command.replace(SCENARIO_LOCATION, job.getScenarioLocation());
    }

}
