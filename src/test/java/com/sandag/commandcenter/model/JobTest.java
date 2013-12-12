package com.sandag.commandcenter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
  "classpath:/db.xml",
})
public class JobTest {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Test
	public void configWorks() {
		// TODO transactions, setup/teardown (is db recreated for each test?)
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Job job = new Job();
			assertNull(job.getId());
			
			session.save(job);
			assertTrue(job.getId() > 0);
	
			//Job retrievedJob = (Job) session.createCriteria(Job.class).setMaxResults(1).list().get(0);
			Job retrievedJob = (Job) session.get(Job.class, job.getId());
			assertEquals(job.getId(), retrievedJob.getId());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	
	
}
