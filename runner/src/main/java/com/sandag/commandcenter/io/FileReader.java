package com.sandag.commandcenter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileReader
{
    protected int maxBytes = 8192;
    private static final Logger LOGGER = Logger.getLogger(FileReader.class.getName());

    @Autowired
    protected FileInputStreamFactory fileInputStreamFactory;

    public byte[] read(File file, int startByte)
    {
        LOGGER.debug(String.format("Reading file '%s' starting at %d", file.getAbsolutePath(), startByte));
        
        int byteCount = getByteCount(file.length(), startByte);
        if (byteCount == -1)
        {
            return null;
        }
        return read(file, startByte, byteCount);
    }
    
    private int getByteCount(long fileLength, int startByte)
    {
        int byteCount = (int) Math.min(maxBytes, fileLength - startByte);
        if (byteCount <= 0)
        {
            LOGGER.debug("No bytes to read (at/past end of file?)");
            return -1;
        }
        return byteCount;
    }

    private byte[] read(File file, int startByte, int byteCount)
    {   
        ByteBuffer buffer = ByteBuffer.allocate(byteCount);
        
        try (FileInputStream in = fileInputStreamFactory.getFor(file); FileChannel channel = in.getChannel())
        {
            channel.read(buffer, startByte);
        }
        catch (IOException e)
        {
            LOGGER.debug("File removed?", e);
            return null;
        }
        LOGGER.debug(String.format("Returning bytes %d-%d of %s", startByte, startByte + byteCount, file.getAbsolutePath()));
        return buffer.array();
    }

}
