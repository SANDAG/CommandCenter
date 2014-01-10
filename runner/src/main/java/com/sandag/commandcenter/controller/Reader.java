package com.sandag.commandcenter.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Reader
{
    int maxBytes = 4096;

    @Value("#{properties['baseDir'] + '/' + properties['logFileDir']}")
    String dir;

    @RequestMapping(value = "/log", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    byte[] read(@RequestParam String fileName, @RequestParam int startByte)
    {
        try
        {
            File file = new File(dir + File.separatorChar + fileName);
            int byteCount = (int) Math.min(maxBytes, file.length() - startByte);
            if (byteCount <= 0)
            {
                // at/past end of file
                return null;
            }
            ByteBuffer buffer = ByteBuffer.allocate(byteCount);
            try (FileInputStream in = new FileInputStream(file); FileChannel channel = in.getChannel())
            {
                channel.read(buffer, startByte);
            }
            return buffer.array();
        } catch (IOException e)
        {
            // file may no longer exist
            return null;
        }
    }

}
