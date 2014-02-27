package com.sandag.commandcenter.runner;

import org.springframework.stereotype.Component;

import com.sandag.commandcenter.model.Job.Model;

@Component
public class RunnerFactory
{

    public Runner getRunner(String commandLine, Model model)
    {
        Runner runner = new Runner();
        runner.setCommandLine(commandLine);
        runner.setModel(model);
        return runner;
    }

}
