package com.sandag.commandcenter.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RoleChecker
{
    @Value(value = "#{'${adminRole}'}")
    protected String adminRole;

    public boolean isAdmin(HttpServletRequest request)
    {
        return request.isUserInRole(adminRole);
    }

}
