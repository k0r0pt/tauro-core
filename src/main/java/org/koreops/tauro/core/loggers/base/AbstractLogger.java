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

package org.koreops.tauro.core.loggers.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Abstract Logger class. This has raw implementations and abstract methods
 * to be implemented by child classes based on their use.
 * Also, the child Loggers will send the log file name based on their use.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 */
abstract class AbstractLogger {
  private static boolean initComplete = false;
  private static PrintStream out;
  private final String attackLogLocation;

  public abstract void debug(String debug, boolean logToOut);

  public abstract void info(String info, boolean logToOut);

  public abstract void error(String error, boolean logToOut);

  protected AbstractLogger(String logLocation) {
    this.attackLogLocation = logLocation;
  }

  /**
   * Prints a info log in STDOUT.
   *
   * @param info The info log message that needs to be logged.
   */
  protected synchronized void printInfo(String info) {
    System.out.println("[INFO] " + info);
  }

  /**
   * Prints an error log in STDOUT.
   *
   * @param error The error log message that needs to be logged.
   */
  protected synchronized void printError(String error) {
    System.err.println("[ERROR] " + error);
  }

  /**
   * Prints a debug log in STDOUT.
   *
   * @param debug The debug log message that needs to be logged.
   */
  protected synchronized void printDebug(String debug) {
    System.out.println("[DEBUG] " + debug);
  }

  /**
   * Prints an info log in the file.
   *
   * @param info The info log message that needs to be logged.
   */
  protected synchronized void printFileInfo(String info) {
    if (!initComplete) {
      initFileLogging();
    }
    out.println("[INFO] " + info);
  }

  /**
   * Prints a debug log in the file.
   *
   * @param debug The debug log message that needs to be logged.
   */
  protected synchronized void printFileDebug(String debug) {
    if (!initComplete) {
      initFileLogging();
    }
    out.println("[DEBUG] " + debug);
  }

  /**
   * Prints an error log in the file.
   *
   * @param error The error message to be logged.
   */
  protected synchronized void printFileError(String error) {
    if (!initComplete) {
      initFileLogging();
    }
    out.println("[ERROR] " + error);
  }

  /**
   * Finalizes file logging by performing file closing and other operations.
   */
  protected synchronized String finalizeFileLogging() {
    String newFileLocation = null;
    if (initComplete) {
      out.flush();
      out.close();
      File file = new File(attackLogLocation);
      DateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
      String ext = ".".concat(sdf.format(new Date()));
      File fileDest = new File(attackLogLocation + ext);
      file.renameTo(fileDest);
      System.out.println("[INFO] Log saved to: " + attackLogLocation + ext);
      newFileLocation = attackLogLocation + ext;
    }
    return newFileLocation;
  }

  private synchronized void initFileLogging() {
    String path = attackLogLocation.substring(0, attackLogLocation.lastIndexOf(File.separator));
    File dir = new File(path);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = new File(attackLogLocation);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException ex) {
        this.printError("Failed to initialize file logging to: " + attackLogLocation + ": " + ex.getMessage());
      }
    }
    try {
      out = new PrintStream(attackLogLocation);
      initComplete = true;
    } catch (FileNotFoundException ex) {
      this.printError("Failed to initialize file logging to: " + attackLogLocation + ": " + ex.getMessage());
    }
  }
}
