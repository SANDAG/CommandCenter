package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.Job.Status;
import com.sandag.commandcenter.persistence.JobDao;

public class JobsFinishedControllerTest
{
    private JobsFinishedController controller;
    private Model model;

    @Before
    public void setUp()
    {
        model = new ExtendedModelMap();
        controller = new JobsFinishedController();
    }

    @Test
    public void canDisplay()
    {
        JobDao dao = mock(JobDao.class);
        List<Job> jobs = new ArrayList<Job>();
        Job job = new Job();
        jobs.add(job);
        when(dao.read((Status[]) anyVararg())).thenReturn(jobs);
        controller.jobDao = dao;

        assertEquals("jobsFinished", controller.display(model));
        Object modelJobs = model.asMap().get("jobs");
        assertEquals(jobs, modelJobs);
    }

}