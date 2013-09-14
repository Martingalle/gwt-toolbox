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

import static junit.framework.Assert.*;

import org.junit.Test;

public class EntityTest {

  @Test
  public void testComparators() {
    Entity entity1 = new Entity("test", 1);
    entity1.put("test", 1);

    Entity entity2 = new Entity("test", 1);
    entity2.put("test", 1);

    Entity entity3 = new Entity("test", 2);
    entity3.put("test", 1);

    assertEquals(entity1.toString(), entity2.toString());
    assertFalse(entity1.toString().equals(entity3.toString()));

    assertEquals(entity1.hashCode(), entity2.hashCode());
    assertFalse(entity1.hashCode() == entity3.hashCode());

    assertTrue(entity1.equals(entity1));
    assertTrue(entity1.equals(entity2));
    assertFalse(entity1.equals(entity3));

    assertTrue(entity1.compareTo(entity1) == 0);
    assertTrue(entity1.compareTo(entity2) == 0);
    assertFalse(entity1.compareTo(entity3) == 0);
    assertTrue(entity1.compareTo(entity3) < 0);
    assertTrue(entity3.compareTo(entity1) > 0);
  }

  @Test
  public void testNoProperty() {
    Entity entity = new Entity("test", 1);
    assertEquals(entity.getAsObject("test"), DefaultValues.objectDefaultValue());
    assertEquals(entity.getAsString("test"), DefaultValues.stringDefaultValue());
    assertEquals(entity.getAsInt("test"), DefaultValues.intDefaultValue().intValue());
    assertEquals(entity.getAsLong("test"), DefaultValues.longDefaultValue().longValue());
    assertEquals(entity.getAsDouble("test"), DefaultValues.doubleDefaultValue());
    assertEquals(entity.getAsFloat("test"), DefaultValues.floatDefaultValue());
    assertEquals(entity.getAsBoolean("test"), DefaultValues.boolDefaultValue().booleanValue());
    assertEquals(entity.getAsDate("test"), DefaultValues.dateDefaultValue());
    assertEquals(entity.getAsTime("test"), DefaultValues.timeDefaultValue());
    assertEquals(entity.getAsTimestamp("test"), DefaultValues.timestampDefaultValue());
  }

  @Test
  public void testLongInt() {
    Entity entity = new Entity("test", 1);
    entity.put("test", 1);
    assertEquals(entity.getAsObject("test"), 1);
    assertEquals(entity.getAsString("test"), "1");
    assertEquals(entity.getAsInt("test"), 1);
    assertEquals(entity.getAsLong("test"), 1L);
    assertEquals(entity.getAsDouble("test"), 1d);
    assertEquals(entity.getAsFloat("test"), 1f);
    assertEquals(entity.getAsBoolean("test"), DefaultValues.boolDefaultValue().booleanValue());
    assertEquals(entity.getAsDate("test"), DefaultValues.dateDefaultValue());
    assertEquals(entity.getAsTime("test"), DefaultValues.timeDefaultValue());
    assertEquals(entity.getAsTimestamp("test"), DefaultValues.timestampDefaultValue());
  }

  @Test
  public void testDoubleFloat() {
    Entity entity = new Entity("test", 1);
    entity.put("test", 1.1);
    assertEquals(entity.getAsObject("test"), 1.1);
    assertEquals(entity.getAsString("test"), "1.1");
    assertEquals(entity.getAsInt("test"), 1);
    assertEquals(entity.getAsLong("test"), 1L);
    assertEquals(entity.getAsDouble("test"), 1.1d);
    assertEquals(entity.getAsFloat("test"), 1.1f);
    assertEquals(entity.getAsBoolean("test"), DefaultValues.boolDefaultValue().booleanValue());
    assertEquals(entity.getAsDate("test"), DefaultValues.dateDefaultValue());
    assertEquals(entity.getAsTime("test"), DefaultValues.timeDefaultValue());
    assertEquals(entity.getAsTimestamp("test"), DefaultValues.timestampDefaultValue());
  }

  @Test
  public void testString() {
    Entity entity = new Entity("test", 1);
    entity.put("test", "1.1");
    assertEquals(entity.getAsObject("test"), "1.1");
    assertEquals(entity.getAsString("test"), "1.1");
    assertEquals(entity.getAsInt("test"), DefaultValues.intDefaultValue().intValue());
    assertEquals(entity.getAsLong("test"), DefaultValues.longDefaultValue().longValue());
    assertEquals(entity.getAsDouble("test"), 1.1d);
    assertEquals(entity.getAsFloat("test"), 1.1f);
    assertEquals(entity.getAsBoolean("test"), DefaultValues.boolDefaultValue().booleanValue());
    assertEquals(entity.getAsDate("test"), DefaultValues.dateDefaultValue());
    assertEquals(entity.getAsTime("test"), DefaultValues.timeDefaultValue());
    assertEquals(entity.getAsTimestamp("test"), DefaultValues.timestampDefaultValue());
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testDate() {
    Date dateExpected = new Date(12, 0, 16, 22, 22, 38);
    Entity entity = new Entity("test", 1);
    entity.put("test", dateExpected);

    Date dateReturned = (Date) entity.getAsObject("test");
    assertTrue(dateReturned.getYear() == dateExpected.getYear());
    assertTrue(dateReturned.getMonth() == dateExpected.getMonth());
    assertTrue(dateReturned.getDate() == dateExpected.getDate());
    assertTrue(dateReturned.getHours() == 1);
    assertTrue(dateReturned.getMinutes() == 0);
    assertTrue(dateReturned.getSeconds() == 0);
    assertTrue(dateReturned.getTimezoneOffset() == dateExpected.getTimezoneOffset());

    dateReturned = entity.getAsDate("test");
    assertTrue(dateReturned.getYear() == dateExpected.getYear());
    assertTrue(dateReturned.getMonth() == dateExpected.getMonth());
    assertTrue(dateReturned.getDate() == dateExpected.getDate());
    assertTrue(dateReturned.getHours() == 1);
    assertTrue(dateReturned.getMinutes() == 0);
    assertTrue(dateReturned.getSeconds() == 0);
    assertTrue(dateReturned.getTimezoneOffset() == dateExpected.getTimezoneOffset());

    assertEquals(entity.getAsString("test"), "16 Jan 1912 01:00:00 GMT");
    assertEquals(entity.getAsInt("test"), DefaultValues.intDefaultValue().intValue());
    assertEquals(entity.getAsLong("test"), DefaultValues.longDefaultValue().longValue());
    assertEquals(entity.getAsDouble("test"), DefaultValues.doubleDefaultValue());
    assertEquals(entity.getAsFloat("test"), DefaultValues.floatDefaultValue());
    assertEquals(entity.getAsBoolean("test"), DefaultValues.boolDefaultValue().booleanValue());

    dateReturned = entity.getAsTime("test");
    assertTrue(dateReturned.getHours() == 1);
    assertTrue(dateReturned.getMinutes() == 0);
    assertTrue(dateReturned.getSeconds() == 0);
    assertTrue(dateReturned.getTimezoneOffset() == dateExpected.getTimezoneOffset());

    dateReturned = entity.getAsTimestamp("test");
    assertTrue(dateReturned.getYear() == dateExpected.getYear());
    assertTrue(dateReturned.getMonth() == dateExpected.getMonth());
    assertTrue(dateReturned.getDate() == dateExpected.getDate());
    assertTrue(dateReturned.getHours() == 1);
    assertTrue(dateReturned.getMinutes() == 0);
    assertTrue(dateReturned.getSeconds() == 0);
    assertTrue(dateReturned.getTimezoneOffset() == dateExpected.getTimezoneOffset());
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testTime() {

    Time timeExpected = new Time(22, 22, 38);

    Entity entity = new Entity("test", 1);
    entity.put("test", timeExpected);

    Date timeReturned = (Time) entity.getAsObject("test");
    assertTrue(timeReturned.getHours() == timeExpected.getHours());
    assertTrue(timeReturned.getMinutes() == timeExpected.getMinutes());
    assertTrue(timeReturned.getSeconds() == timeExpected.getSeconds());
    assertTrue(timeReturned.getTimezoneOffset() == timeExpected.getTimezoneOffset());

    timeReturned = entity.getAsDate("test");
    assertTrue(timeReturned.getYear() == 70);
    assertTrue(timeReturned.getMonth() == 0);
    assertTrue(timeReturned.getDate() == 1);
    assertTrue(timeReturned.getHours() == timeExpected.getHours());
    assertTrue(timeReturned.getMinutes() == timeExpected.getMinutes());
    assertTrue(timeReturned.getSeconds() == timeExpected.getSeconds());
    assertTrue(timeReturned.getTimezoneOffset() == timeExpected.getTimezoneOffset());

    assertEquals(entity.getAsString("test"), "1 Jan 1970 21:22:38 GMT");
    assertEquals(entity.getAsInt("test"), DefaultValues.intDefaultValue().intValue());
    assertEquals(entity.getAsLong("test"), DefaultValues.longDefaultValue().longValue());
    assertEquals(entity.getAsDouble("test"), DefaultValues.doubleDefaultValue());
    assertEquals(entity.getAsFloat("test"), DefaultValues.floatDefaultValue());
    assertEquals(entity.getAsBoolean("test"), DefaultValues.boolDefaultValue().booleanValue());

    timeReturned = entity.getAsTime("test");
    assertTrue(timeReturned.getHours() == timeExpected.getHours());
    assertTrue(timeReturned.getMinutes() == timeExpected.getMinutes());
    assertTrue(timeReturned.getSeconds() == timeExpected.getSeconds());
    assertTrue(timeReturned.getTimezoneOffset() == timeExpected.getTimezoneOffset());

    timeReturned = entity.getAsTimestamp("test");
    assertTrue(timeReturned.getYear() == 70);
    assertTrue(timeReturned.getMonth() == 0);
    assertTrue(timeReturned.getDate() == 1);
    assertTrue(timeReturned.getHours() == timeExpected.getHours());
    assertTrue(timeReturned.getMinutes() == timeExpected.getMinutes());
    assertTrue(timeReturned.getSeconds() == timeExpected.getSeconds());
    assertTrue(timeReturned.getTimezoneOffset() == timeExpected.getTimezoneOffset());
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testTimestamp() {
    Timestamp timestampExpected = new Timestamp(12, 0, 16, 22, 22, 38, 0);
    Entity entity = new Entity("test", 1);
    entity.put("test", timestampExpected);

    Date timestampReturned = (Timestamp) entity.getAsObject("test");
    assertTrue(timestampReturned.getYear() == timestampExpected.getYear());
    assertTrue(timestampReturned.getMonth() == timestampExpected.getMonth());
    assertTrue(timestampReturned.getDate() == timestampExpected.getDate());
    assertTrue(timestampReturned.getHours() == timestampExpected.getHours());
    assertTrue(timestampReturned.getMinutes() == timestampExpected.getMinutes());
    assertTrue(timestampReturned.getSeconds() == timestampExpected.getSeconds());
    assertTrue(timestampReturned.getTimezoneOffset() == timestampExpected.getTimezoneOffset());

    timestampReturned = entity.getAsDate("test");
    assertTrue(timestampReturned.getYear() == timestampExpected.getYear());
    assertTrue(timestampReturned.getMonth() == timestampExpected.getMonth());
    assertTrue(timestampReturned.getDate() == timestampExpected.getDate());
    assertTrue(timestampReturned.getHours() == timestampExpected.getHours());
    assertTrue(timestampReturned.getMinutes() == timestampExpected.getMinutes());
    assertTrue(timestampReturned.getSeconds() == timestampExpected.getSeconds());
    assertTrue(timestampReturned.getTimezoneOffset() == timestampExpected.getTimezoneOffset());

    assertEquals(entity.getAsString("test"), "16 Jan 1912 22:22:38 GMT");
    assertEquals(entity.getAsInt("test"), DefaultValues.intDefaultValue().intValue());
    assertEquals(entity.getAsLong("test"), DefaultValues.longDefaultValue().longValue());
    assertEquals(entity.getAsDouble("test"), DefaultValues.doubleDefaultValue());
    assertEquals(entity.getAsFloat("test"), DefaultValues.floatDefaultValue());
    assertEquals(entity.getAsBoolean("test"), DefaultValues.boolDefaultValue().booleanValue());

    timestampReturned = entity.getAsTime("test");
    assertTrue(timestampReturned.getHours() == timestampExpected.getHours());
    assertTrue(timestampReturned.getMinutes() == timestampExpected.getMinutes());
    assertTrue(timestampReturned.getSeconds() == timestampExpected.getSeconds());
    assertTrue(timestampReturned.getTimezoneOffset() == timestampExpected.getTimezoneOffset());

    timestampReturned = entity.getAsTimestamp("test");
    assertTrue(timestampReturned.getYear() == timestampExpected.getYear());
    assertTrue(timestampReturned.getMonth() == timestampExpected.getMonth());
    assertTrue(timestampReturned.getDate() == timestampExpected.getDate());
    assertTrue(timestampReturned.getHours() == timestampExpected.getHours());
    assertTrue(timestampReturned.getMinutes() == timestampExpected.getMinutes());
    assertTrue(timestampReturned.getSeconds() == timestampExpected.getSeconds());
    assertTrue(timestampReturned.getTimezoneOffset() == timestampExpected.getTimezoneOffset());
  }
}
