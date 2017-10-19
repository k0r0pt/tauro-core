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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koreops.tauro.core.exceptions.DbDriverException;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Test class for {@link DbConnEngine}.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 * @since 17 Oct, 2017 11:38 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DbConnEngine.class)
public class DbConnEngineTest {

  static {
    System.setProperty("tauro.core.db", "nonExistent.db");
  }

  @Before
  public void init() {

  }

  @Test
  public void test() throws Exception {
    PowerMockito.mockStatic(DriverManager.class);
    Connection connection = PowerMockito.mock(Connection.class);
    BDDMockito.given(DriverManager.getConnection(Mockito.anyString())).willReturn(connection);
    Connection obtainedConn = DbConnEngine.getConnection();
    Assert.assertNotNull(obtainedConn);
    Assert.assertEquals(connection, obtainedConn);
  }

  @Test
  public void testForError() throws Exception {
    PowerMockito.mockStatic(DriverManager.class);
    BDDMockito.given(DriverManager.getConnection(Mockito.anyString())).willThrow(new SQLException(""));
    try {
      DbConnEngine.getConnection();
    } catch (Exception e) {
      Assert.assertTrue(e instanceof DbDriverException);
      Assert.assertEquals("", e.getMessage());
      return;
    }
    Assert.assertFalse(true);
  }
}
