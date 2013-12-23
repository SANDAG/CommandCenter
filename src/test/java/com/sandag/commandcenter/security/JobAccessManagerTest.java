package com.sandag.commandcenter.security;

import java.security.Principal;

import org.junit.Test;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobAccessManagerTest
{

    private JobAccessManager manager = new JobAccessManager();

    @Test
    public void modelerCanDeleteOwnedJobs()
    {
        String username = "ASDFASDFASDF";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        User user = mock(User.class);
        when(user.getPrincipal()).thenReturn(username);
        Job job = new Job();
        job.setUser(user);
        
        assertTrue(manager.canDelete(job, principal));
    }

    @Test
    public void modelerCanNotDeleteUnownedJobs()
    {
        String jobUsername = "ASDFASDFASDF";
        String principalUsername = "Not ASDFASDFASDF";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(jobUsername);
        User user = mock(User.class);
        when(user.getPrincipal()).thenReturn(principalUsername);
        Job job = new Job();
        job.setUser(user);
        
        assertFalse(manager.canDelete(job, principal));
    }
}
