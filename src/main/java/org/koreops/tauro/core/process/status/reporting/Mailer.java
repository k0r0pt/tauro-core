/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
