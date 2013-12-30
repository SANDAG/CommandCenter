package com.sandag.commandcenter.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UserTest
{

    @Test
    public void verifySameness()
    {
        checkMatch(1, 1, true);
        checkMatch(1, 2, false);
        checkMatch(2, 1, false);
        checkMatch(1, null, false);
        checkMatch(null, 1, false);
        checkMatch(null, null, false);
    }

    private void checkMatch(Integer id1, Integer id2, boolean expected)
    {
        User a1 = new User();
        User a2 = new User();
        a1.setId(id1);
        a2.setId(id2);
        assertEquals(expected, a1.isSame(a2));
    }

}
