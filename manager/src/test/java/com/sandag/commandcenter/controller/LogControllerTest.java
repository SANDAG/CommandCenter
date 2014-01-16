package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.util.Utils;

public class LogControllerTest
{
    
    private LogController controller = new LogController();
        
    @Test
    public void controllerBasics()
    {
        String host = "host";
        String path = "/some path/file.log";
        String encodedPath = Utils.urlEncode(path);                             
        Job job = new Job();
        job.setRunner(host);
        Model model = new ExtendedModelMap();
    
        assertEquals("log", controller.displayLog(job, path, model));
        Map<String, ? extends Object> map = model.asMap();
        assertEquals(path, map.get("path"));
        assertEquals(job, map.get("job"));
        assertEquals(String.format(controller.urlFormat, host, encodedPath), map.get("url"));
    }
    
}
