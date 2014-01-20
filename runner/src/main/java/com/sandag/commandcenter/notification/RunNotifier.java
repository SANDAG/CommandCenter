package com.sandag.commandcenter.notification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;

@Service
public class RunNotifier
{
    protected static String startMessage = "Your job with scenario '%s' has started running.";
    
    // TODO temporarily hard-coding until we figure out where to get it
    protected String emailAddress = "craig.sabbey@rsginc.com";

    private static final Logger LOGGER = Logger.getLogger(RunNotifier.class.getName());

    @Autowired
    protected MailSender mailSender;
    
    @Autowired
    protected SimpleMailMessage runStartTemplate;

    public void sendStartedMessage(Job job)
    {
        SimpleMailMessage msg = new SimpleMailMessage(runStartTemplate);
        msg.setTo(emailAddress);
        msg.setText(String.format(startMessage, job.getScenario()));
        try
        {
            this.mailSender.send(msg);
        } catch (MailException e)
        {
            LOGGER.error("Run start email failed for job with id " + job.getId(), e);
        }
    }

}
