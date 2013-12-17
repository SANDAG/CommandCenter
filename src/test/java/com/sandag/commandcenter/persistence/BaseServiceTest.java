package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;

import com.sandag.commandcenter.model.BaseServiceEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/db.xml", "classpath:/autowire.xml" })
public class BaseServiceTest
{

    @Autowired
    private BaseServiceImpl service;

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
    public void testDelete()
    {
        BaseServiceEntity created = createEntity();
        service.delete(created);
        assertNull(service.read(created.getId()));
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
