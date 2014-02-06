package com.sandag.commandcenter.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sandag.commandcenter.controller.util.HeaderSetter;
import com.sandag.commandcenter.io.FileReader;

@Controller
public class LogFileReaderController
{

    @Autowired
    protected HeaderSetter headerSetter;
    
    @Autowired
    protected FileReader fileReader;
    
    @RequestMapping(value = "/log", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public byte[] read(@RequestParam String filePath, @RequestParam int startByte, HttpServletRequest request, HttpServletResponse response)
    {
        headerSetter.setCrossOriginHeader(request, response);
        return fileReader.read(new File(filePath), startByte);
    }

}
