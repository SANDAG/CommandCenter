package com.sandag.commandcenter.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.stereotype.Component;

@Component
public class EmailAddressConverter
{
    protected String unableToConvertFormat = "Unable to convert '%s' to an email address";
    
    @Value(value = "#{'${smtpToDomain}'}")
    protected String emailDomain;

    // expecting activeDirectoryUser values like 'I-RSG\craig.sabbey'
    public String fromActiveDirectoryName(String name)
    {
        String emailName;
        int lastBackslash = name.lastIndexOf('\\');
        if (lastBackslash < 0 || lastBackslash == name.length() - 1)
        {
            throw new MailPreparationException(String.format(unableToConvertFormat, name));
        }
        emailName = name.substring(lastBackslash + 1);
        
        return String.format("%s@%s", emailName, emailDomain);
    }
    
    
}
