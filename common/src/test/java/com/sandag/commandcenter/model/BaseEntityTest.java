package com.sandag.commandcenter.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BaseEntityTest
{

    @Test
    public void onCreateSetsBoth() 
    {
        BaseEntity ent = new BaseEntity(){};
        assertNull(ent.getCreated());
        assertNull(ent.getUpdated());
        
        ent.onCreate();
        assertNotNull(ent.getCreated());
        assertNotNull(ent.getUpdated());
    }
    
    @Test
    public void onUpdateSetsUpdated() 
    {
        BaseEntity ent = new BaseEntity(){};
        assertNull(ent.getUpdated());
        
        ent.onUpdate();
        assertNotNull(ent.getUpdated());
    }
    
}
