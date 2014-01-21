package com.sandag.commandcenter.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobAccessManagerTest
{
    @Test
    public void verifyCanUpdate()
    {
        String name0 = "ASDF";
        String name1 = "NOT ASDF";
        checkCanUpdate(name0, name0, false, true); // names match (user created job)
        checkCanUpdate(name0, name1, false, false); // names do not match
        checkCanUpdate(name0, name0, true, true); // admin w/ matching names
        checkCanUpdate(name0, name1, true, true); // admin w/o matching names
    }

    // support
    public void checkCanUpdate(String principalUsername, String jobUsername, boolean hasAdminRole, boolean accesses)
    {
        String adminRole = "ROLE_ADMIN";
        JobAccessManager manager = new JobAccessManager();
        manager.adminRole = adminRole;
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(principalUsername);
        User user = mock(User.class);
        when(user.getPrincipal()).thenReturn(jobUsername);
        Job job = new Job();
        job.setUser(user);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.isUserInRole(adminRole)).thenReturn(hasAdminRole);

        assertEquals(accesses, manager.canUpdate(request, job, principal));
    }
}
