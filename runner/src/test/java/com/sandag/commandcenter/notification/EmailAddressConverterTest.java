package com.sandag.commandcenter.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.mail.MailPreparationException;

public class EmailAddressConverterTest
{
    private EmailAddressConverter converter = new EmailAddressConverter();
    private String adDomain = "DOMAIN";
    private String adName = "first.last";
    private String adCompleteName = String.format("%s\\%s", adDomain, adName);
    {
        converter.emailDomain = "domain.tld";
    }

    @Test
    public void fromActiveDirectoryWorks()
    {
        String expectedEmail = String.format("%s@%s", adName, converter.emailDomain);
        String actualEmail = converter.fromActiveDirectoryName(adCompleteName);
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    public void noDomainPartThrows()
    {
        try
        {
            converter.fromActiveDirectoryName(adName);
            fail("MailPreparationException expected");
        } catch (MailPreparationException e)
        {
            assertTrue(true); // for checkstyle
        }
    }

    @Test
    public void endsInBackSlashThrows()
    {
        try
        {
            converter.fromActiveDirectoryName(adName + '\\');
            fail("MailPreparationException expected");
        } catch (MailPreparationException e)
        {
            assertTrue(true); // for checkstyle
        }
    }

}
