package com.sandag.commandcenter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.stereotype.Service;

// allows us to swap implementations of the stream for unit-testing
@Service
public class FileInputStreamFactory
{
    
    public FileInputStream getFor(File file) throws FileNotFoundException
    {
        return new FileInputStream(file);
    }
    
}
