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

package org.koreops.tauro.core.constants;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for RouterAuthType.
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 * @since 17 Oct, 2017 6:16 PM
 */
public class RouterAuthTypeTest {

  @Test
  public void testBasic() {
    Assert.assertEquals("BASIC", RouterAuthType.BASIC.name());
  }

  @Test
  public void testForm() {
    Assert.assertEquals("FORM", RouterAuthType.FORM.name());
  }

  @Test
  public void testNotSupported() {
    Assert.assertEquals("NOT_SUPPORTED", RouterAuthType.NOT_SUPPORTED.name());
  }
}
