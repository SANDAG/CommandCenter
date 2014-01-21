package com.sandag.commandcenter.notification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;

@Service
public class EmailSender
{
    private static final Logger LOGGER = Logger.getLogger(EmailSender.class.getName());
    
    // TODO temporarily hard-coding until we figure out where to get it
    protected String emailAddress = "craig.sabbey@rsginc.com";

    @Autowired
    protected MailSender mailSender;

    protected void sendEmail(Job job, SimpleMailMessage template, String message, Object... messageArgs)
    {
        SimpleMailMessage msg = new SimpleMailMessage(template);
        msg.setTo(emailAddress);
        msg.setText(String.format(message, messageArgs));
        try
        {
            this.mailSender.send(msg);
        } catch (MailException e)
        {
            LOGGER.error("Email failed for job with id " + job.getId(), e);
        }
    }

}
