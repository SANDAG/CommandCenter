package com.sandag.commandcenter.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    // in case access checked after job deleted...
    @Test
    public void nullJobReturnsFalse()
    {
        JobAccessManager manager = new JobAccessManager();
        assertFalse(manager.canUpdate(null, null, null));
    }
    
    // support
    public void checkCanUpdate(String principalUsername, String jobUsername, boolean hasAdminRole, boolean accesses)
    {
        JobAccessManager manager = new JobAccessManager();
        HttpServletRequest request = mock(HttpServletRequest.class);
        
        RoleChecker roleChecker = mock(RoleChecker.class);
        when(roleChecker.isAdmin(request)).thenReturn(hasAdminRole);
        manager.roleChecker = roleChecker;
        
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(principalUsername);
        
        User user = mock(User.class);
        when(user.getPrincipal()).thenReturn(jobUsername);
        
        Job job = new Job();
        job.setUser(user);
        
        assertEquals(accesses, manager.canUpdate(request, job, principal));
    }
}
