package com.sandag.commandcenter.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ContextExposingHttpServletRequest;

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

@Component
public class SitemeshSpringFilter implements Filter, ApplicationContextAware
{
    protected ApplicationContext applicationContext;
    protected SiteMeshFilter siteMeshFilter = new SiteMeshFilter();

    // makes spring context available to sitemesh decorators
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        WebApplicationContext context = (WebApplicationContext) this.applicationContext;
        ServletRequest wrappedRequest = new ContextExposingHttpServletRequest(req, context);
        siteMeshFilter.doFilter(wrappedRequest, response, chain);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        siteMeshFilter.init(filterConfig);
    }

    @Override
    public void destroy()
    {
        siteMeshFilter.destroy();
    }
}
