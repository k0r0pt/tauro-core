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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class acts as the Database connection Engine.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 */
public class DbConnEngine {

  private static final String DB_LOCATION;
  private static final DbConnEngine dbConnEngine;

  static {
    String dbLoc = System.getProperty("tauro.core.db");
    String sep = File.separator;
    String uhome = System.getProperty("user.home");
    DB_LOCATION = dbLoc != null ? dbLoc : uhome + sep + ".h4X0r" + sep + "k0r0pt" + sep + "db" + sep + "WirelessStations.sqlite";
    dbConnEngine = new DbConnEngine();
  }

  /**
   * Gets a database connection.
   *
   * @return The database connection
   */
  public static Connection getConnection() throws DbDriverException {
    return dbConnEngine.getDbConnection();
  }

  Connection getDbConnection() throws DbDriverException {
    Connection connection;
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
