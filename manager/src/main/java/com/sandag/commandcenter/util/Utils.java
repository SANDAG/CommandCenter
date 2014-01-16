package com.sandag.commandcenter.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class Utils
{

    private Utils()
    {
    }

    /*
     * Encodes spaces for use in path or query parts of URLs (%20 and not + in paths)
     */
    public static String urlEncode(String value)
    {
        try
        {
            return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

}
