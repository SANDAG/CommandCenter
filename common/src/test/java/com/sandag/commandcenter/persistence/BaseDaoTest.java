package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.hibernate.Session;
import org.hibernate.cfg.NotYetImplementedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sandag.commandcenter.model.BaseServiceEntity;
import com.sandag.commandcenter.model.StringKeyEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/db.xml", "classpath:/autowire.xml" })
public class BaseDaoTest
{

    @Autowired
    private BaseDaoImpl service;

    @Autowired
    private ApplicationContext context;

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
    public void testRefresh()
    {
        BaseServiceEntity created = createEntity();
        assertNull(created.getValue());
        created.setValue("Not null");
        assertNotNull(created.getValue());
        service.refresh(created);
        assertNull(created.getValue());
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

    @Test
    public void converts()
    {
        BaseServiceEntity created = createEntity();
        BaseServiceEntity read = service.convert(String.valueOf(created.getId()));
        assertEquals(created.getId(), read.getId());
    }

    @Test
    public void supportingUnsupportedPkTypeFails()
    {
        StringKeyEntityDaoImpl dao = context.getBean(StringKeyEntityDaoImpl.class);
        StringKeyEntity ent = new StringKeyEntity();
        dao.create(ent);
        try
        {
            dao.convert(ent.getId());
            fail("NotYetImplementedException expected");
        } catch (NotYetImplementedException e)
        {
            // for checkstyle
            assertTrue(true);
        }
    }

    // support
    private BaseServiceEntity createEntity()
    {
        BaseServiceEntity ent = new BaseServiceEntity();
        assertNull(ent.getId());
        service.create(ent);
        return ent;
    }

    @Repository
    public static class StringKeyEntityDaoImpl extends BaseDao<StringKeyEntity, String>
    {
        public StringKeyEntityDaoImpl()
        {
            super(StringKeyEntity.class, String.class);
        }
    }

}
