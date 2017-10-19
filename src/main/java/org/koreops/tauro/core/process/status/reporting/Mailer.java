/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.koreops.tauro.core.process.status.reporting;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Utility class to send status messages about scan/scrape.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 */
public class Mailer {

  private static final String HOST = "localhost";
  private static final String FROM_HOST = "k0r0pt.org";
  private static final String NO_REPLY_FROM = "donotreply@k0r0pt.org";
  private static final String OVERWATCH = System.getProperty("tauro.core.overwatch");

  /**
   * Utility method to send a status email.
   *
   * @param fromEmail The address from which email is to be sent.
   * @param toEmail   The address to which email is to be sent.
   * @param subject   The subject of the email.
   * @param message   The content of the email.
   * @return          {@code true} if email was sent successfully. {@code false} otherwise.
   */
  public static boolean sendEmail(String fromEmail, String toEmail, String subject, String message) {
    Properties properties = System.getProperties();
    properties.setProperty(FROM_HOST, HOST);
    Session session = Session.getDefaultInstance(properties);

    try {
      MimeMessage mimeMessage = new MimeMessage(session);
      mimeMessage.setFrom(new InternetAddress(fromEmail));
      mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
      if (OVERWATCH != null) {
        mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(OVERWATCH));
      }
      mimeMessage.setSubject(subject);
      mimeMessage.setContent(message, "text/html");
      Transport.send(mimeMessage);
      return true;
    } catch (MessagingException ex) {
      Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }

  /**
   * Utility method to send a status email from default from address {@link #FROM_HOST}.
   *
   * @param toEmail   The address to which email is to be sent.
   * @param subject   The subject of the email.
   * @param message   The content of the email.
   * @return          {@code true} if email was sent successfully. {@code false} otherwise.
   */
  public static boolean sendEmail(String toEmail, String subject, String message) {
    return sendEmail(NO_REPLY_FROM, toEmail, subject, message);
  }
}
