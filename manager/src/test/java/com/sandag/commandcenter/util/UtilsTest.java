package com.sandag.commandcenter.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsTest
{
    
    @Test
    public void urlEncodes()
    {
        // checking that some characters get url encoded, not testing the underlying encoder
        assertEquals("%28%2C%2F%2B%21%29%3D%3F", Utils.urlEncode("(,/+!)=?"));
    }

    @Test
    public void urlEncodesSpacesWithPercent20()
    {
        // %20 works in both url path and query parts - some URLEncoders return '+' for spaces
        assertEquals("x%20x%20x", Utils.urlEncode("x x x"));
    }
    
}
