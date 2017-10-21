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

import org.koreops.tauro.core.loggers.base.BaseLogger;

import java.io.File;

/**
 * General Logging class.
 * This class has methods for printing both to stdout and log file: ~/h4X0r/logs/routerLog.log.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 */
public class Logger {

  private static final BaseLogger logger;

  static {
    String logLoc = System.getProperty("tauro.core.logFile");
    String sep = File.separator;
    String uhome = System.getProperty("user.home");
    logLoc = logLoc != null ? logLoc : uhome + sep + ".h4X0r" + sep + "k0r0pt" + sep + "logs" + sep + "tauroAttackLog.log";
    logger = new BaseLogger(logLoc);
  }

  public static synchronized void info(String info) {
    info(info, false);
  }

  public static synchronized void info(String info, boolean logToOut) {
    logger.info(info, logToOut);
  }

  public static synchronized void debug(String debug) {
    debug(debug, false);
  }

  public static synchronized void debug(String debug, boolean logToOut) {
    logger.debug(debug, logToOut);
  }

  public static synchronized void error(String error) {
    error(error, false);
  }

  public static synchronized void error(String error, boolean logToOut) {
    logger.error(error, logToOut);
  }

  public static synchronized String finalizeLogging() {
    return logger.finalizeFileLogging();
  }
}
