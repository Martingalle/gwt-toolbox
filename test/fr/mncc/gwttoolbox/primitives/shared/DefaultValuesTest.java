/**
 * Copyright (c) 2012 MNCC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author http://www.mncc.fr
 */
package fr.mncc.gwttoolbox.primitives.shared;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class DefaultValuesTest {

  @Test
  @SuppressWarnings("deprecation")
  public void testDefaultValues() {
    assertEquals(DefaultValues.objectDefaultValue(), null);
    assertEquals(DefaultValues.stringDefaultValue(), "");
    assertEquals(DefaultValues.intDefaultValue(), new Integer(0));
    assertEquals(DefaultValues.longDefaultValue(), new Long(0));
    assertEquals(DefaultValues.boolDefaultValue(), new Boolean(false));
    assertEquals(DefaultValues.doubleDefaultValue(), new Double(0));
    assertEquals(DefaultValues.floatDefaultValue(), new Float(0));

    Date date = DefaultValues.dateDefaultValue();
    assertEquals(date.getYear(), 70);
    assertEquals(date.getMonth(), 0);
    assertEquals(date.getDate(), 1);
    assertEquals(date.getHours(), 1);
    assertEquals(date.getMinutes(), 0);
    assertEquals(date.getSeconds(), 0);

    Time time = DefaultValues.timeDefaultValue();
    assertEquals(time.getHours(), 1);
    assertEquals(time.getMinutes(), 0);
    assertEquals(time.getSeconds(), 0);

    Timestamp timestamp = DefaultValues.timestampDefaultValue();
    assertEquals(timestamp.getYear(), 70);
    assertEquals(timestamp.getMonth(), 0);
    assertEquals(timestamp.getDate(), 1);
    assertEquals(timestamp.getHours(), 1);
    assertEquals(timestamp.getMinutes(), 0);
    assertEquals(timestamp.getSeconds(), 0);
  }
}
