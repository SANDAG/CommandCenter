package com.sandag.commandcenter.runner;

import com.sandag.commandcenter.model.Job;

public interface Runner
{
    Job.Model supports();
    void run();
}
