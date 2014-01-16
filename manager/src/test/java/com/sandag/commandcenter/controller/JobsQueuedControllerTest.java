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
import com.sandag.commandcenter.persistence.JobDao;
import com.sandag.commandcenter.persistence.UserDao;

import static com.sandag.commandcenter.model.Job.Status.QUEUED;
import static com.sandag.commandcenter.model.Job.Status.RUNNING;
import static com.sandag.commandcenter.model.Job.Status.COMPLETE;
import static com.sandag.commandcenter.model.Job.Status.ARCHIVED;
import static com.sandag.commandcenter.model.Job.Status.DELETED;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;

public class JobsQueuedControllerTest
{
    private JobsQueuedController controller;
    private Model model;

    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
        controller = new JobsQueuedController();
    }

    @Test
    public void canDisplayQueue()
    {
        setEmptyUserService();

        JobDao dao = mock(JobDao.class);
        List<Job> jobs = new ArrayList<Job>();
        Job job = new Job();
        jobs.add(job);
        when(dao.readQueued()).thenReturn(jobs);
        controller.jobDao = dao;
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("principal name");

        assertEquals("jobsQueued", controller.display(model, principal));
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
        testGetMoveIds(new Integer[] {}, new int[] {7 }, new int[] {0 }, true);
        testGetMoveIds(new Integer[] {}, new int[] {7 }, new int[] {0 }, false);

        // more
        testGetMoveIds(new Integer[] {3 }, new int[] {7, 3, 6, 12 }, new int[] {0, 1 }, true);
        testGetMoveIds(new Integer[] {7 }, new int[] {7, 3, 6, 12 }, new int[] {0, 1 }, false);

        testGetMoveIds(new Integer[] {6, 12 }, new int[] {7, 3, 6, 12 }, new int[] {0, 2, 3 }, true);
        testGetMoveIds(new Integer[] {7, 6 }, new int[] {7, 3, 6, 12 }, new int[] {0, 2, 3 }, false);

        // unowned
        testGetMoveIds(new Integer[] {}, new int[] {7, 3, 6, 12 }, new int[] {}, true);
        testGetMoveIds(new Integer[] {}, new int[] {7, 3, 6, 12 }, new int[] {}, false);
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

        UserDao userDao = mock(UserDao.class);
        when(userDao.fetchOrCreate(username)).thenReturn(user);
        controller.userDao = userDao;

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
            job.setStatus(QUEUED);
            jobs.add(job);
        }

        List<Integer> moveableIds = up ? controller.getCanMoveUpJobIds(jobs, principal) : controller.getCanMoveDownJobIds(jobs, principal);

        assertTrue(Arrays.asList(expected).equals(moveableIds));
    }

    @Test
    public void checkGetMoveIdLimitedByStatus()
    {
        String username = "Craig";
        int userId = 23;
        User user = new User();
        user.setId(userId);
        user.setPrincipal(username);

        UserDao userDao = mock(UserDao.class);
        when(userDao.fetchOrCreate(username)).thenReturn(user);
        controller.userDao = userDao;

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        List<Job> jobs = getJobs(user, QUEUED, RUNNING, COMPLETE, ARCHIVED, DELETED, QUEUED);

        List<Integer> upMoveableIds = controller.getCanMoveUpJobIds(jobs, principal);
        List<Integer> downMoveableIds = controller.getCanMoveDownJobIds(jobs, principal);

        assertEquals(1, upMoveableIds.size());
        assertEquals(1, downMoveableIds.size());

        assertEquals(jobs.get(0).getId(), downMoveableIds.get(0));
        assertEquals(jobs.get(jobs.size() - 1).getId(), upMoveableIds.get(0));
    }

    private List<Job> getJobs(User user, Job.Status... statuses)
    {
        List<Job> jobs = new ArrayList<Job>();
        int mockId = 0;
        for (Job.Status status : statuses)
        {
            Job job = new Job();
            job.setUser(user);
            job.setStatus(status);
            job.setId(mockId++);
            jobs.add(job);
        }
        return jobs;
    }

    @Test
    public void moveUpForDeletedJob()
    {
        int deletedJobId = 1234134;
        JobDao dao = mock(JobDao.class);
        when(dao.read(deletedJobId)).thenReturn(null);
        controller.jobDao = dao;

        controller.move(deletedJobId, true, null);
        verify(dao, never()).getMoveableJobBefore((Job) anyObject());
        verify(dao, never()).updateWithSwappedQueuePositions((Job) anyObject(), (Job) anyObject());
    }

    @Test
    public void moveUpForUnswappableJob()
    {
        // in case someone else changed queue

        int jobId = 1234134;
        Job job = new Job();
        JobDao dao = mock(JobDao.class);
        when(dao.read(jobId)).thenReturn(job);
        when(dao.getMoveableJobBefore(job)).thenReturn(null);
        controller.jobDao = dao;

        controller.move(jobId, true, null);
        verify(dao).getMoveableJobBefore(job);
        verify(dao, never()).updateWithSwappedQueuePositions((Job) anyObject(), (Job) anyObject());
    }

    @Test
    public void moveUpWorks()
    {
        int jobId = 1234134;
        Job jobA = new Job();
        Job jobB = new Job();
        JobDao dao = mock(JobDao.class);
        when(dao.read(jobId)).thenReturn(jobA);
        when(dao.getMoveableJobBefore(jobA)).thenReturn(jobB);
        controller.jobDao = dao;

        controller.move(jobId, true, null);
        verify(dao).getMoveableJobBefore(jobA);
        verify(dao).updateWithSwappedQueuePositions(jobA, jobB);
    }

    @Test
    public void moveDownForDeletedJob()
    {
        int deletedJobId = 1234134;
        JobDao dao = mock(JobDao.class);
        when(dao.read(deletedJobId)).thenReturn(null);
        controller.jobDao = dao;

        controller.move(deletedJobId, false, null);
        verify(dao, never()).getMoveableJobAfter((Job) anyObject());
        verify(dao, never()).updateWithSwappedQueuePositions((Job) anyObject(), (Job) anyObject());
    }

    @Test
    public void moveDownForUnswappableJob()
    {
        // in case someone else changed queue

        int jobId = 1234134;
        Job job = new Job();
        JobDao dao = mock(JobDao.class);
        when(dao.read(jobId)).thenReturn(job);
        when(dao.getMoveableJobAfter(job)).thenReturn(null);
        controller.jobDao = dao;

        controller.move(jobId, false, null);
        verify(dao).getMoveableJobAfter(job);
        verify(dao, never()).updateWithSwappedQueuePositions((Job) anyObject(), (Job) anyObject());
    }

    @Test
    public void moveDownWorks()
    {
        int jobId = 1234134;
        Job jobA = new Job();
        Job jobB = new Job();
        JobDao dao = mock(JobDao.class);
        when(dao.read(jobId)).thenReturn(jobA);
        when(dao.getMoveableJobAfter(jobA)).thenReturn(jobB);
        controller.jobDao = dao;

        controller.move(jobId, false, null);
        verify(dao).getMoveableJobAfter(jobA);
        verify(dao).updateWithSwappedQueuePositions(jobA, jobB);
    }

    private void setEmptyUserService()
    {
        User user = new User();
        UserDao userDao = mock(UserDao.class);
        when(userDao.fetchOrCreate(anyString())).thenReturn(user);
        controller.userDao = userDao;
    }

}