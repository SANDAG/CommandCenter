package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sandag.commandcenter.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/db.xml", "classpath:/autowire.xml" })
public class UserDaoTest
{

    @Autowired
    private UserDao service;

    @Test
    public void createWithDupePrincipalReturnsUser()
    {
        String principal = "domain\\username";
        User userOrig = service.fetchOrCreate(principal);
        User userDupe = service.fetchOrCreate(principal);
        assertNotNull(userOrig.getId());
        assertEquals(userOrig.getId(), userDupe.getId());
    }

}
