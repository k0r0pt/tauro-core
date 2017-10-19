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

package org.koreops.tauro.core.db;

import org.koreops.tauro.core.exceptions.DbDriverException;
import org.koreops.tauro.core.loggers.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class acts as the Database connection Engine.
 *
 * @author Xtreme (k0r0pt) (sudipto.sarkar@visioplanet.org)
 */
public class DbConnEngine {

  private static final String DB_LOCATION;

  static {
    String dbLoc = System.getProperty("tauro.core.db");
    DB_LOCATION = dbLoc != null ? dbLoc : System.getProperty("user.home") + "/h4X0r/db/WirelessStations.sqlite";
  }

  /**
   * Gets a database connection.
   *
   * @return The database connection
   */
  public static Connection getConnection() throws DbDriverException {
    Connection connection = null;
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + DB_LOCATION);
    } catch (ClassNotFoundException | SQLException e) {
      Logger.error("Database Connection failed! Fuuuuuuuuuuuuuu!!!!!" + e.getMessage(), true);
      throw new DbDriverException(e.getMessage());
    }
    Logger.info("Opened database successfully", true);
    return connection;
  }
}
