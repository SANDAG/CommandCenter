package com.sandag.commandcenter.notification;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.sandag.commandcenter.model.Job;

public class EmailSenderTest
{
    private EmailSender emailSender;
    
    @Before
    public void setup()
    {
        emailSender = new EmailSender();
        MailSender sender = mock(MailSender.class);
        emailSender.mailSender = sender;
    }
    
    @Test
    public void callsSend()
    {
        emailSender.sendEmail(new Job(), new SimpleMailMessage(), "");
        verify(emailSender.mailSender).send((SimpleMailMessage) anyObject());
    }

    @Test
    public void usesTemplateParams()
    {
        final String subject = "Earn a living in your PJs";
        SimpleMailMessage template = mock(SimpleMailMessage.class);
        when(template.getSubject()).thenReturn(subject);
        emailSender.sendEmail(new Job(), template, "");
        verify(emailSender.mailSender).send(argThat(new ArgumentMatcher<SimpleMailMessage>()
        {
            @Override
            public boolean matches(Object message)
            {
                return ((SimpleMailMessage) message).getSubject().equals(subject);
            }
        }));
    }

    @Test
    public void usesMessageArgs()
    {
        final String format = "%s, %s ¸.·´¯`·.´¯`·.¸¸.·´¯`·.¸><(((º>";
        final Object[] messageArgs = new String[] {"Just keep swimming", "just keep swimming... " };
        emailSender.sendEmail(new Job(), new SimpleMailMessage(), format, messageArgs);
        verify(emailSender.mailSender).send(argThat(new ArgumentMatcher<SimpleMailMessage>()
        {
            @Override
            public boolean matches(Object message)
            {
                return ((SimpleMailMessage) message).getText().equals(String.format(format, messageArgs));
            }
        }));
    }
}
