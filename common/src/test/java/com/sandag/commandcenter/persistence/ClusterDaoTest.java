package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sandag.commandcenter.model.Cluster;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/db.xml", "classpath:/autowire.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class ClusterDaoTest
{

    @Autowired
    private ClusterDao dao;

    @Test
    public void testReadAll()
    {
        int num = 9;
        List<Cluster> createds = new ArrayList<Cluster>();
        for (int i = 0; i < num; i++)
        {
            Cluster cluster = new Cluster();
            dao.create(cluster);
            createds.add(cluster);
        }

        List<Cluster> retrieveds = dao.readAll();
        assertEquals(createds.size(), retrieveds.size());
        assertTrue(createds.containsAll(retrieveds));
    }

    @Test
    public void testReadAllInDescUpdatedOrder()
    {
        ClusterDao clusterDao = new ClusterDao();
        SessionFactory sessionFactory = mock(SessionFactory.class);
        clusterDao.sessionFactory = sessionFactory;
        Session session = mock(Session.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        Criteria criteria = mock(Criteria.class);
        when(session.createCriteria(Cluster.class)).thenReturn(criteria);
        when(criteria.addOrder((Order) anyObject())).thenReturn(criteria);
        clusterDao.readAll();
        verify(criteria).addOrder(argThat(new ArgumentMatcher<Order>() {
            @Override
            public boolean matches(Object argument)
            {
                return "updated desc".equals(argument.toString());
            }
        }));
        verify(criteria).list();
    }
}
