package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;
import com.sandag.commandcenter.persistence.JobService;
import com.sandag.commandcenter.persistence.UserService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

public class QueueControllerTest
{
    private QueueController controller;
    private Model model;

    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
        controller = new QueueController();
    }

    @Test
    public void canDisplayQueue()
    {
        setEmptyUserService();
        
        JobService service = mock(JobService.class);
        List<Job> jobs = new ArrayList<Job>();
        Job job = new Job();
        jobs.add(job);
        when(service.readAll()).thenReturn(jobs);
        controller.jobService = service;
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("principal name");

        assertEquals("queue", controller.display(model, principal));
        assertTrue(model.containsAttribute("message"));
        assertTrue(model.containsAttribute("principalName"));
        assertTrue(model.containsAttribute("jobAccessManager"));
        assertTrue(model.containsAttribute("moveUpIds"));
        assertTrue(model.containsAttribute("moveDownIds"));
        Object modelJobs = model.asMap().get("jobs");
        assertEquals(jobs, modelJobs);
    }

    @Test
    public void getMoveIds()
    {
        // empties
        testGetMoveIds(new Integer[] {}, new int[] {}, new int[] {}, true);
        testGetMoveIds(new Integer[] {}, new int[] {}, new int[] {}, false);

        // ones
        testGetMoveIds(new Integer[] {}, new int[] {7}, new int[] {0}, true);
        testGetMoveIds(new Integer[] {}, new int[] {7}, new int[] {0}, false);

        // more
        testGetMoveIds(new Integer[] {3}, new int[] {7, 3, 6, 12}, new int[] {0, 1}, true);
        testGetMoveIds(new Integer[] {7}, new int[] {7, 3, 6, 12}, new int[] {0, 1}, false);

        testGetMoveIds(new Integer[] {6, 12}, new int[] {7, 3, 6, 12}, new int[] {0, 2, 3}, true);
        testGetMoveIds(new Integer[] {7, 6}, new int[] {7, 3, 6, 12}, new int[] {0, 2, 3}, false);

        // unowned
        testGetMoveIds(new Integer[] {}, new int[] {7, 3, 6, 12}, new int[] {}, true);
        testGetMoveIds(new Integer[] {}, new int[] {7, 3, 6, 12}, new int[] {}, false);
    }

    private void testGetMoveIds(Integer[] expected, int[] jobIds, int[] ownedPositions, boolean up)
    {
        int userId = 34;
        int otherUserId = 234;
        String username = "ASDFASDF";

        User user = new User();
        user.setId(userId);
        user.setPrincipal(username);

        User otherUser = new User();
        otherUser.setId(otherUserId);

        UserService userService = mock(UserService.class);
        when(userService.fetchOrCreate(username)).thenReturn(user);
        controller.userService = userService;

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        List<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < jobIds.length; i++)
        {
            int id = jobIds[i];
            boolean owned = Arrays.binarySearch(ownedPositions, i) >= 0;
            Job job = new Job();
            job.setId(id);
            job.setUser(owned ? user : otherUser);
            jobs.add(job);
        }

        List<Integer> moveableIds = up ? controller.getCanMoveUpJobIds(jobs, principal) : controller.getCanMoveDownJobIds(jobs, principal);

        assertTrue(Arrays.asList(expected).equals(moveableIds));
    }

    private void setEmptyUserService()
    {
        User user = new User();
        UserService userService = mock(UserService.class);
        when(userService.fetchOrCreate(anyString())).thenReturn(user);
        controller.userService = userService;
    }

}