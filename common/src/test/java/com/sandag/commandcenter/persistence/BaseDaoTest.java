package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sandag.commandcenter.model.BaseServiceEntity;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/db.xml", "classpath:/autowire.xml" })
public class BaseDaoTest
{

    @Autowired
    private BaseDaoImpl service;

    @Test
    public void testCreate()
    {
        BaseServiceEntity ent = new BaseServiceEntity();
        assertNull(ent.getId());
        service.create(ent);
        assertNotNull(ent.getId());
    }

    @Test
    public void testRead()
    {
        BaseServiceEntity created = createEntity();
        BaseServiceEntity retrieved = service.read(created.getId());
        assertNotNull(retrieved.getId());
        assertEquals(created.getId(), retrieved.getId());
    }

    @Test
    public void testUpdate()
    {
        String expected = "lsdfkjal;sdjkfl;";
        BaseServiceEntity created = createEntity();
        assertNull(created.getValue());
        created.setValue(expected);
        service.update(created);
        BaseServiceEntity retrieved = service.read(created.getId());
        assertEquals(expected, retrieved.getValue());
    }

    @Test
    public void testDeleteByEntity()
    {
        BaseServiceEntity created = createEntity();
        service.delete(created);
        assertNull(service.read(created.getId()));
    }
    
    @Test
    public void testDeleteById()
    {
        BaseServiceEntity created = createEntity();
        service.delete(created.getId());
        assertNull(service.read(created.getId()));
    }
    

    @Test
    public void queryStartsWithRightType()
    {
        BaseDao<BaseServiceEntity, Integer> localService = spy(new BaseDaoImpl());
        Session session = mock(Session.class);
        doReturn(session).when(localService).getSession();
        localService.startQuery();
        verify(session).createCriteria(BaseServiceEntity.class);
    }

    // support
    private BaseServiceEntity createEntity()
    {
        BaseServiceEntity ent = new BaseServiceEntity();
        assertNull(ent.getId());
        service.create(ent);
        return ent;
    }

}
