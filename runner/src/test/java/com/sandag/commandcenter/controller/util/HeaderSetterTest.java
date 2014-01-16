package com.sandag.commandcenter.controller.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

public class HeaderSetterTest
{

    private HeaderSetter setter = new HeaderSetter();
    private String allowedHost0 = "ah0";
    private String allowedHost1 = "ah1";
    private String allowedHost2 = "ah2";
    private String disallowedHost = "dh";
    private List<String> allowedHosts = Arrays.asList(new String[]{allowedHost0, allowedHost1, allowedHost2});
    
    @Before
    public void setup()
    {
        setter.allowedOrigins = allowedHosts;
    }
    
    @Test
    public void setsMatchingOrigin()
    {
        setsMatchingOrigin(allowedHost0, true);
        setsMatchingOrigin(allowedHost1, true);
        setsMatchingOrigin(allowedHost2, true);
        setsMatchingOrigin(disallowedHost, false);
    }

    // support
    public void setsMatchingOrigin(String host, boolean matches)
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Origin")).thenReturn(host);
        setter.setCrossOriginHeader(request, response);
        VerificationMode mode = matches ? times(1) : never();
        verify(response, mode).setHeader("Access-Control-Allow-Origin", host);
    }

}
