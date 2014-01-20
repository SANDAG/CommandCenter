package com.sandag.commandcenter.notification;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.sandag.commandcenter.model.Job;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;

public class RunNotifierTest
{

    @Test
    public void sendsStartMessage()
    {
        MailSender sender = mock(MailSender.class);
        RunNotifier notifier = new RunNotifier();
        notifier.mailSender = sender;
        notifier.runStartTemplate = mock(SimpleMailMessage.class);
        notifier.sendStartedMessage(new Job());
        verify(sender).send((SimpleMailMessage) anyObject());
    }

    @Test
    public void usesTemplateParams()
    {
        final String subject = "Earn a living in your PJs";
        RunNotifier notifier = new RunNotifier();
        MailSender sender = mock(MailSender.class);
        notifier.mailSender = sender;
        SimpleMailMessage template = mock(SimpleMailMessage.class);
        when(template.getSubject()).thenReturn(subject);
        notifier.runStartTemplate = template;
        notifier.sendStartedMessage(new Job());
        verify(sender).send(argThat(new ArgumentMatcher<SimpleMailMessage>()
        {
            @Override
            public boolean matches(Object message)
            {
                return ((SimpleMailMessage) message).getSubject().equals(subject);
            }
        }));
    }
    
    @Test
    public void usesJob()
    {
        final String scenario = "234asldkjflwjq4";
        Job job = mock(Job.class);
        when(job.getScenario()).thenReturn(scenario);
        RunNotifier notifier = new RunNotifier();
        MailSender sender = mock(MailSender.class);
        notifier.mailSender = sender;
        notifier.runStartTemplate = mock(SimpleMailMessage.class);
        notifier.sendStartedMessage(job);
        verify(sender).send(argThat(new ArgumentMatcher<SimpleMailMessage>()
        {
            @Override
            public boolean matches(Object message)
            {
                return ((SimpleMailMessage) message).getText().equals(String.format(RunNotifier.startMessage, scenario));
            }
        }));
    }

    
    
    
}
