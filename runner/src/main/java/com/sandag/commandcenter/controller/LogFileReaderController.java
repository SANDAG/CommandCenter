package com.sandag.commandcenter.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sandag.commandcenter.controller.util.HeaderSetter;

@Controller
public class LogFileReaderController
{
    protected int maxBytes = 8192;
    private static final Logger LOGGER = Logger.getLogger(LogFileReaderController.class.getName());

    @Autowired
    protected HeaderSetter headerSetter;

    @Value(value = "#{'${baseDir}'}")
    protected String dir;

    @RequestMapping(value = "/log", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public byte[] read(@RequestParam String filePath, @RequestParam int startByte, HttpServletRequest request, HttpServletResponse response)
    {
        headerSetter.setCrossOriginHeader(request, response);
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
            LOGGER.debug(String.format("Returning bytes %d-%d of %s", startByte, startByte + byteCount, absolutePath));
            return buffer.array();
        } catch (IOException e)
        {
            // file may no longer exist
            return null;
        }
    }

}
