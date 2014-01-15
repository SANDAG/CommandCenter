package com.sandag.commandcenter.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogFileReaderController
{
    protected int maxBytes = 4096;
    private static final Logger LOGGER = Logger.getLogger(LogFileReaderController.class.getName());

    @Value(value = "#{'${baseDir}'}")
    protected String dir;

    @RequestMapping(value = "/log", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public byte[] read(@RequestParam String filePath, @RequestParam int startByte)
    {
        try
        {
            String absolutePath = dir + File.separatorChar + filePath;
            LOGGER.debug(String.format("Reading file '%s' starting at %d", absolutePath, startByte));
            File file = new File(absolutePath);
            int byteCount = (int) Math.min(maxBytes, file.length() - startByte);
            if (byteCount <= 0)
            {
                LOGGER.debug("No bytes to read (at/past end of file?)");
                return null;
            }
            ByteBuffer buffer = ByteBuffer.allocate(byteCount);
            // TODO test IOException (close on channel, read on channel, file not found (ioe child) for new stream)
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
