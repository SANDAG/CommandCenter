package com.sandag.commandcenter.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import waffle.spring.WindowsAuthenticationToken;

public class RoleCheckerTest
{

    @Test
    public void verify()
    {
        check(true);
        check(false);
    }

    // support
    private void check(boolean isAdmin)
    {
        // basically checking we proxy request.isUserInRole
        String adminRole = "ROLE_ADMIN";
        RoleChecker checker = new RoleChecker();
        checker.adminRole = adminRole;
        HttpServletRequest request = mock(HttpServletRequest.class);
        setupMockRequestForLogging(request);
        when(request.isUserInRole(adminRole)).thenReturn(isAdmin);
        assertEquals(isAdmin, checker.isAdmin(request));
    }

    private void setupMockRequestForLogging(HttpServletRequest request)
    {
        WindowsAuthenticationToken token = mock(WindowsAuthenticationToken.class);
        when(request.getUserPrincipal()).thenReturn(token);
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        when(token.getAuthorities()).thenReturn(roles);
    }

}
