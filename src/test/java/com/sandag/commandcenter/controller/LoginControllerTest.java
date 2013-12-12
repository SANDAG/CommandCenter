package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.ui.ModelMap;

// TODO test configuration, too? 
//   http://www.petrikainulainen.net/programming/spring-framework/unit-testing-of-spring-mvc-controllers-normal-controllers/
public class LoginControllerTest
{

    @Test
    public void testStandInBehavior()
    {
        LoginController controller = new LoginController();
        ModelMap model = new ModelMap();

        String page = controller.printWelcome(model);
        assertEquals(page, "login");
        assertEquals(model.get("message"), "Spring 3 MVC Hello World");
    }

}
