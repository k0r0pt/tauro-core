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

package org.koreops.tauro.core.process;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.koreops.tauro.core.exceptions.ProcessResumptionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for ProcessManager.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 * @since 17 Oct, 2017 7:35 PM
 */
public class ProcessManagerTest {

  private static final String[] args = new String[3];
  private static final List<String> doneHosts = new ArrayList<>();
  private static final String dumpFile = "src/test/resources/process.ps";

  @BeforeClass
  public static void init() {
    args[0] = "1";
    args[1] = "2";
    args[2] = "3";
    doneHosts.add("0.1.2.3");
    doneHosts.add("1.2.3.4");
    doneHosts.add("8.8.8.8");
    System.setProperty("tauro.core.processDumpFile", dumpFile);
  }

  @AfterClass
  public static void cleanup() {
    System.clearProperty("tauro.core.processDumpFile");
  }

  @Test
  public void testSaveArgs() throws Exception {
    ProcessManager.saveArgs(args);
    ProcessManager.finalizeProcessLogging(false);
    String[] readArgs = ProcessManager.getArgs();
    String[] expectedArgs = Arrays.copyOf(args, args.length + 1);
    expectedArgs[args.length] = "--resume";
    ProcessManager.finalizeProcessLogging(true);
    Assert.assertEquals(expectedArgs.length, readArgs.length);
    Assert.assertArrayEquals(expectedArgs, readArgs);
  }

  @Test
  public void testHostsWriteOff() throws Exception {
    ProcessManager.saveArgs(args);
    for (String host : doneHosts) {
      ProcessManager.writeOffHost(host);
    }
    ProcessManager.finalizeProcessLogging(false);
    List<String> actualDoneHosts = ProcessManager.getCoveredHosts();
    ProcessManager.finalizeProcessLogging(true);
    Assert.assertEquals(doneHosts.size(), actualDoneHosts.size());
    Assert.assertEquals(doneHosts, actualDoneHosts);
  }

  @Test
  public void testNonExistentDumpGetArgs() {
    try {
      ProcessManager.getArgs();
    } catch (Exception e) {
      Assert.assertTrue(e instanceof ProcessResumptionException);
      Assert.assertEquals("Failed to resume. Process status file has problems: " + dumpFile + " (No such file or directory)", e.getMessage());
    }
  }

  @Test
  public void testNonExistentDumpGetCoveredHosts() {
    try {
      ProcessManager.getCoveredHosts();
    } catch (Exception e) {
      Assert.assertTrue(e instanceof ProcessResumptionException);
      Assert.assertEquals("Failed to resume. Process status file has problems: " + dumpFile + " (No such file or directory)", e.getMessage());
    }
  }
}
