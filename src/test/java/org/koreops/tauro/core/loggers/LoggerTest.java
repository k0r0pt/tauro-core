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

package org.koreops.tauro.core.loggers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.koreops.tauro.core.loggers.base.BaseLogger;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Test class for {@link Logger}.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 * @since 17 Oct, 2017 6:24 PM
 */
public class LoggerTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private static final String infoMsg = "info";
  private static final String debugMsg = "debug";
  private static final String errorMsg = "error";
  private static final String logFile = "src/test/resources/testLog.log";
  private boolean fileOp;
  private static PrintStream out;
  private static PrintStream err;

  @BeforeClass
  public static void init() {
    System.setProperty("tauro.core.logFile", logFile);
  }

  @Before
  public void setUpStreams() {
    out = System.out;
    err = System.err;
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanup() throws Exception {
    System.setOut(out);
    System.setErr(err);
    Field initStatusField = BaseLogger.class.getSuperclass().getDeclaredField("initComplete");
    initStatusField.setAccessible(true);
    initStatusField.set(null, false);
    initStatusField.setAccessible(false);
    if (fileOp && !(new File(logFile)).delete()) {
      throw new Exception("Failed to delete log file");
    }
    fileOp = false;
  }

  @Test
  public void testInfoStdout() {
    Logger.info(infoMsg, true);
    Assert.assertEquals("[INFO] " + infoMsg + "\n", outContent.toString());
  }

  @Test
  public void testErrorStdout() {
    Logger.error(errorMsg, true);
    Assert.assertEquals("[ERROR] " + errorMsg + "\n", errContent.toString());
  }

  @Test
  public void testDebugStdout() {
    Logger.debug(debugMsg, true);
    Assert.assertEquals("[DEBUG] " + debugMsg + "\n", outContent.toString());
  }

  @Test
  public void testError() throws Exception {
    Logger.error(errorMsg);
    BufferedReader reader = new BufferedReader(new FileReader(logFile));
    String loggedMsg = reader.readLine();
    reader.close();
    fileOp = true;
    Assert.assertEquals("[ERROR] " + errorMsg, loggedMsg);
  }

  @Test
  public void testInfo() throws Exception {
    Logger.info(infoMsg);
    BufferedReader reader = new BufferedReader(new FileReader(logFile));
    String loggedMsg = reader.readLine();
    reader.close();
    fileOp = true;
    Assert.assertEquals("[INFO] " + infoMsg, loggedMsg);
  }

  @Test
  public void testDebug() throws Exception {
    Logger.debug(debugMsg);
    BufferedReader reader = new BufferedReader(new FileReader(logFile));
    String loggedMsg = reader.readLine();
    reader.close();
    fileOp = true;
    Assert.assertEquals("[DEBUG] " + debugMsg, loggedMsg);
  }

  @Test
  public void testLogFinalizing() throws Exception {
    Logger.debug(debugMsg);
    String newFileLocation = Logger.finalizeLogging();
    if (!(new File(newFileLocation)).delete()) {
      throw new Exception("Failed to delete renamed log file. Manual cleanup needed.");
    }
    DateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
    String ext = ".".concat(sdf.format(new Date()));
    String expectedFileLocation = logFile + ext;
    Assert.assertEquals(expectedFileLocation, newFileLocation);
  }
}
