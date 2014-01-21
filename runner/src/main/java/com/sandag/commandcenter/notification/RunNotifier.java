package com.sandag.commandcenter.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.Job;

@Service
public class RunNotifier
{
    protected static String startMessage = "Your job with scenario '%s' has started running.",
            finishMessage = "Your job with scenario '%s' has finished running.";

    @Autowired
    protected SimpleMailMessage runStartTemplate, runFinishTemplate;

    @Autowired
    protected EmailSender emailSender;

    public void sendStartedMessage(Job job)
    {
        emailSender.sendEmail(job, runStartTemplate, startMessage, job.getScenario());
    }

    public void sendFinishedMessage(Job job)
    {
        emailSender.sendEmail(job, runFinishTemplate, finishMessage, job.getScenario());
    }

}
