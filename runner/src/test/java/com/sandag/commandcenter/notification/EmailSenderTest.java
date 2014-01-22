package com.sandag.commandcenter.notification;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.model.User;

public class EmailSenderTest
{
    private EmailSender emailSender;
    private MailSender sender;
    private Job job;
    private EmailAddressConverter emailConverter;
    private String emailTo = "first.last@domain.tld";
    private String activeDirectoryName = "DOMAIN\\first.last";

    @Before
    public void setup()
    {
        emailSender = new EmailSender();
        sender = mock(MailSender.class);
        emailSender.mailSender = sender;
        emailConverter = mock(EmailAddressConverter.class);
        emailSender.emailAddressConverter = emailConverter;
        when(emailConverter.fromActiveDirectoryName(anyString())).thenReturn(emailTo);
        job = new Job();
        User user = new User();
        user.setPrincipal(activeDirectoryName);
        job.setUser(user);

    }

    @Test
    public void callsSend()
    {
        emailSender.sendEmail(job, new SimpleMailMessage(), "");
        verify(emailSender.mailSender).send((SimpleMailMessage) anyObject());
    }

    @Test
    public void usesTemplateParams()
    {
        final String subject = "Earn a living in your PJs";
        SimpleMailMessage template = mock(SimpleMailMessage.class);
        when(template.getSubject()).thenReturn(subject);
        emailSender.sendEmail(job, template, "");
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
        emailSender.sendEmail(job, new SimpleMailMessage(), format, messageArgs);
        verify(emailSender.mailSender).send(argThat(new ArgumentMatcher<SimpleMailMessage>()
        {
            @Override
            public boolean matches(Object message)
            {
                return ((SimpleMailMessage) message).getText().equals(String.format(format, messageArgs));
            }
        }));
    }

    @Test
    public void usesEmailAddressConverterForTo()
    {
        emailSender.sendEmail(job, new SimpleMailMessage(), "");
        verify(emailConverter).fromActiveDirectoryName(activeDirectoryName);
        verify(emailSender.mailSender).send(argThat(new ArgumentMatcher<SimpleMailMessage>()
        {
            @Override
            public boolean matches(Object message)
            {
                return Arrays.equals(((SimpleMailMessage) message).getTo(), new String[] {emailTo });
            }
        }));
    }

    @Test
    public void handlesMailExceptionForSender()
    {
        Mockito.doThrow(mock(MailException.class)).when(sender).send((SimpleMailMessage) anyObject());
        emailSender.sendEmail(job, new SimpleMailMessage(), "");
        // passes if thrown exception handled
    }

    @Test
    public void handlesMailExceptionForConverter()
    {
        Mockito.doThrow(mock(MailException.class)).when(emailConverter).fromActiveDirectoryName(anyString());
        emailSender.sendEmail(job, new SimpleMailMessage(), "");
        // passes if thrown exception handled
    }
}
