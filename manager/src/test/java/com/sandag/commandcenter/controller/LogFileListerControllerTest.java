package com.sandag.commandcenter.controller;

import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import com.sandag.commandcenter.model.Job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;


public class LogFileListerControllerTest
{

    private LogFileListerController controller = new LogFileListerController();
    
    @Test
    public void proxiesLogListing()
    {
        String[] paths = new String[]{"path/a", "path/b"};
        RestTemplate curl = mock(RestTemplate.class);
        when(curl.getForObject(anyString(), eq(String[].class), anyVararg())).thenReturn(paths);
        controller.restTemplate = curl;
        
        Model model = new ExtendedModelMap();    
        assertEquals("logs", controller.listLogs(new Job(), model));
        assertEquals(paths, model.asMap().get("logs"));
    }
    
}
