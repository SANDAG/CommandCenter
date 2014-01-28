package com.sandag.commandcenter.filter;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ContextExposingHttpServletRequest;

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

public class SitemeshSpringFilterTest
{
    private SiteMeshFilter containedFilter;
    private SitemeshSpringFilter filter;

    @Before
    public void setup()
    {
        containedFilter = mock(SiteMeshFilter.class);
        filter = new SitemeshSpringFilter();
        filter.siteMeshFilter = containedFilter;
    }
    
    @Test
    public void testDoFilter() throws IOException, ServletException
    {
        ServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        final ApplicationContext context = mock(WebApplicationContext.class);
        filter.applicationContext = context;
        filter.doFilter(request, response, chain);

        verify(containedFilter).doFilter(argThat(new ArgumentMatcher<ServletRequest>()
        {
            @Override
            public boolean matches(Object argument)
            {
                return (((ContextExposingHttpServletRequest) argument).getWebApplicationContext().equals(context));
            }
        }), eq(response), eq(chain));
    }

    @Test
    public void callsInit() throws ServletException
    {
        FilterConfig config = mock(FilterConfig.class);
        filter.init(config);
        verify(containedFilter).init(config);
    }
    
    @Test
    public void callsDestroy()
    {
        filter.destroy();
        verify(containedFilter).destroy();
    }
    
}
