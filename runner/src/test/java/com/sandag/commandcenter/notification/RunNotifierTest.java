package com.sandag.commandcenter.notification;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;

import com.sandag.commandcenter.model.Job;

public class RunNotifierTest
{
    private RunNotifier notifier;
    private Job job;
    
    @Before
    public void setup()
    {
        notifier = new RunNotifier();
        job = new Job();
        job.setScenario("some believable scenario");
        EmailSender sender = mock(EmailSender.class);
        notifier.emailSender = sender;
    }

    @Test
    public void sendsStartMessage()
    {
        notifier.runStartTemplate = new SimpleMailMessage();
        notifier.sendStartedMessage(job);
        verify(notifier.emailSender).sendEmail(job, notifier.runStartTemplate, RunNotifier.startMessage, job.getScenario());
    }

    @Test
    public void sendsFinishedMessage()
    {
        notifier.runFinishTemplate = new SimpleMailMessage();
        notifier.sendFinishedMessage(job);
        verify(notifier.emailSender).sendEmail(job, notifier.runFinishTemplate, RunNotifier.finishMessage, job.getScenario());        
    }
}
