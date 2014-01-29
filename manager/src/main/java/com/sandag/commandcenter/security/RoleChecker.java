package com.sandag.commandcenter.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import waffle.spring.WindowsAuthenticationToken;

@Service
public class RoleChecker
{
    @Value(value = "#{'${adminRole}'}")
    protected String adminRole;

    private static final Logger LOGGER = Logger.getLogger(RoleChecker.class.getName());

    public boolean isAdmin(HttpServletRequest request)
    {
        boolean isAdmin = request.isUserInRole(adminRole);
        LOGGER.debug(String.format("isAdmin called, returning '%s' when matching against '%s'", isAdmin, adminRole));
        WindowsAuthenticationToken principal = (WindowsAuthenticationToken) request.getUserPrincipal();
        LOGGER.trace("ROLES: " + Arrays.toString(principal.getAuthorities().toArray()));

        return isAdmin;
    }

}
