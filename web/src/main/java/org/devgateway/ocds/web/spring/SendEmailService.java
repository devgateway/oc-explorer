/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.ocds.web.spring;

import org.apache.log4j.Logger;
import org.devgateway.toolkit.persistence.dao.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Service to send emails to users to validate email addresses or reset passwords
 *
 * @author mpostelnicu
 */
@Component
public class SendEmailService {

    private static final Logger LOGGER = Logger.getLogger(SendEmailService.class);

    @Autowired
    private JavaMailSender javaMailSenderImpl;

    private SimpleMailMessage templateMessage;

    /**
     * Send a reset password email. This is UNSAFE because passwords are sent in clear text.
     * Nevertheless some customers will ask for these emails to be sent, so ...
     *
     * @param person
     * @param newPassword
     */
    public void sendEmailResetPassword(final Person person, final String newPassword) {
        final SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(person.getEmail());
        msg.setFrom("support@developmentgateway.org");
        msg.setSubject("Recover your password");
        msg.setText("Dear " + person.getFirstName() + " " + person.getLastName() + ",\n\n"
                + "These are your new login credentials for DGToolkit.\n\n" + "Username: " + person.getUsername() + "\n"
                + "Password: " + newPassword + "\n\n"
                + "At login, you will be prompted to change your password to one of your choice.\n\n" + "Thank you,\n"
                + "DG Team");
        try {
            javaMailSenderImpl.send(msg);
        } catch (MailException e) {
            e.printStackTrace();
        }

    }
}
