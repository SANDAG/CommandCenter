package com.sandag.commandcenter.runner;

import com.sandag.commandcenter.model.Job;

public interface Runner
{
    public Job.Model supports();
    public void run();
}
