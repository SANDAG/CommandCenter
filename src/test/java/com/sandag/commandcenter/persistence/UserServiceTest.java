package com.sandag.commandcenter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sandag.commandcenter.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/db.xml", "classpath:/autowire.xml" })
public class UserServiceTest
{

    @Autowired
    private UserService service;

    @Test
    public void catchMalformedEmailOnCreate()
    {
        boolean caught = false;
        try
        {
            service.create("blah");
        } catch (ConstraintViolationException e)
        {
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void createWithDupeEmailReturnsUser()
    {
        String email = "a@b.com";
        User userOrig = service.create(email);
        User userDupe = service.create(email);
        assertEquals(userOrig.getId(), userDupe.getId());
    }

}
