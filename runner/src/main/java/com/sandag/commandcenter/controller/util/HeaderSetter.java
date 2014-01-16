package com.sandag.commandcenter.controller.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HeaderSetter
{    
    private static final Logger LOGGER = Logger.getLogger(HeaderSetter.class.getName());

    @Value(value = "#{'${crossOriginHosts}'.split(',')}")
    protected List<String> allowedOrigins;
    
    // values are a single host, null, or *, so we match against a list to set the header
    public void setCrossOriginHeader(HttpServletRequest request, HttpServletResponse response)
    {
        String origin = request.getHeader("Origin");
        if (allowedOrigins.contains(origin))
        {
            LOGGER.debug(String.format("Allowing origin '%s'", origin));
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else
        {
            LOGGER.debug(String.format("Origin '%s' not in crossOriginHosts '%s'", origin, allowedOrigins));
        }
    }
}
