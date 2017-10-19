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

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.MessagingException;
import javax.mail.Transport;

/**
 * Test class for {@link org.koreops.tauro.core.process.status.reporting.Mailer}.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 * @since 17 Oct, 2017 9:14 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Transport.class })
public class MailerTest {
  @BeforeClass
  public static void initCls() {
    System.setProperty("tauro.core.overwatch", "no.one@no.wh.ere");
  }

  @Before
  public void init() {
    PowerMockito.mockStatic(Transport.class);
  }

  @Test
  public void testSendEmailDefaultSender() {
    PowerMockito.doNothing().when(Transport.class);
    Assert.assertTrue(Mailer.sendEmail("no.one@no.wh.ere", "Hello", "Message"));
  }

  @Test
  public void testSendEmail() {
    PowerMockito.doNothing().when(Transport.class);
    Assert.assertTrue(Mailer.sendEmail("superman@metropolis.city", "no.one@no.wh.ere", "Hello", "Message"));
  }

  @Test
  public void testSendError() throws Exception {
    PowerMockito.doThrow(new MessagingException("")).when(Transport.class, "send", Mockito.any());
    Assert.assertFalse(Mailer.sendEmail("no.one@no.wh.ere", "Hello", "Message"));
  }
}
