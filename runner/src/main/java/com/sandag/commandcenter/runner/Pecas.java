package com.sandag.commandcenter.runner;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import com.sandag.commandcenter.model.Job.Model;

@Service
public class Pecas implements Runner
{
    private Model supported = Model.PECAS;
    
    private static final Logger LOGGER = Logger.getLogger(Pecas.class.getName());

    @Override
    public Model supports()
    {
        return supported;
    }

    @Override
    public void run()
    {
        LOGGER.debug("Running PECAS");
        // TODO Auto-generated method stub

    }
}
