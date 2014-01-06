package com.sandag.commandcenter.runner;

import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job.Model;

@Service
public class Pecas implements Runner
{
    Model supported = Model.PECAS;
    
    @Override
    public Model supports()
    {
        return supported;
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        
    }   
}
