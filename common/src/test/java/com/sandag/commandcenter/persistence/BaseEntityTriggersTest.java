package com.sandag.commandcenter.persistence;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sandag.commandcenter.model.BaseEntity;

public class BaseEntityTriggersTest
{

    @Test
    public void updateWorks()
    {
        BaseEntity entity = new BaseEntity()
        {
        };
        BaseEntityTriggers triggers = new BaseEntityTriggers();

        Object[] currentState = new Object[1];
        assertTrue(triggers.onFlushDirty(entity, null, currentState, null, new String[] {"updated" }, null));
        assertNotNull(entity.getUpdated());
        assertEquals(entity.getUpdated(), currentState[0]);
    }

    @Test
    public void createWorks()
    {
        BaseEntity entity = new BaseEntity()
        {
        };
        BaseEntityTriggers triggers = new BaseEntityTriggers();

        Object[] currentState = new Object[2];
        assertTrue(triggers.onSave(entity, null, currentState, new String[] {"updated", "created" }, null));
        assertNotNull(entity.getUpdated());
        assertNotNull(entity.getCreated());
        assertEquals(entity.getUpdated(), currentState[0]);
        assertEquals(entity.getCreated(), currentState[1]);
    }

    @Test
    public void updateNonBaseEntityOk()
    {
        BaseEntityTriggers triggers = new BaseEntityTriggers();
        assertFalse(triggers.onFlushDirty(new Object(), null, null, null, null, null));
    }

    @Test
    public void createNonBaseEntityOk()
    {
        BaseEntityTriggers triggers = new BaseEntityTriggers();
        assertFalse(triggers.onSave(new Object(), null, null, null, null));
    }
}
