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

/**
 * Base Logger class.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 * @since 17 Oct, 2017 12:47 PM
 */
public class BaseLogger extends AbstractLogger {

  public BaseLogger(String logLocation) {
    super(logLocation);
  }

  @Override
  public void debug(String debug, boolean logToOut) {
    if (logToOut) {
      this.printDebug(debug);
    } else {
      this.printFileDebug(debug);
    }
  }

  @Override
  public void info(String info, boolean logToOut) {
    if (logToOut) {
      this.printInfo(info);
    } else {
      this.printFileInfo(info);
    }
  }

  @Override
  public void error(String error, boolean logToOut) {
    if (logToOut) {
      this.printError(error);
    } else {
      this.printFileError(error);
    }
  }

  @Override
  public synchronized String finalizeFileLogging() {
    return super.finalizeFileLogging();
  }
}
