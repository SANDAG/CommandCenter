package com.sandag.commandcenter.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

public class RoleCheckerTest
{

    @Test
    public void verify()
    {
        check(true);
        check(false);
    }
    
    // support
    public void check(boolean isAdmin)
    {
        // basically checking we proxy request.isUserInRole
        String adminRole = "ROLE_ADMIN";
        RoleChecker checker = new RoleChecker();
        checker.adminRole = adminRole;
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.isUserInRole(adminRole)).thenReturn(isAdmin);
        assertEquals(isAdmin, checker.isAdmin(request));
    }

}
