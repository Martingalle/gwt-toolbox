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

import org.junit.Test;

import fr.mncc.gwttoolbox.primitives.shared.Entity;
import fr.mncc.gwttoolbox.primitives.shared.KeyValuePair;
import fr.mncc.synchronization.LocalDatabase2;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EntityTest {

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

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

  @Test
  public void testDiff() {
    Entity entity1 = createNewEntity();
    Entity entity2 = new Entity(entity1);
    Entity entity3 = createNewEntity();

    long id1 = DataStore2.putSync(entity1);
    long id2 = DataStore2.putSync(entity2);
    long id3 = DataStore2.putSync(entity3);

    Entity datastoreEntity1 = DataStore2.getSync(entity1.getKind(), id1);
    Entity datastoreEntity2 = DataStore2.getSync(entity2.getKind(), id2);
    Entity datastoreEntity3 = DataStore2.getSync(entity3.getKind(), id3);

    Map<String, String> map1 =
        datastoreEntity1.diff(datastoreEntity2, Entity.TAKE_ID_INTO_ACCOUNT
            | Entity.TAKE_KIND_INTO_ACCOUNT | Entity.TAKE_TIMESTAMP_INTO_ACCOUNT);
    Map<String, String> map2 =
        datastoreEntity2.diff(datastoreEntity1, Entity.TAKE_ID_INTO_ACCOUNT
            | Entity.TAKE_KIND_INTO_ACCOUNT | Entity.TAKE_TIMESTAMP_INTO_ACCOUNT);
    Map<String, String> map3 =
        datastoreEntity1.diff(datastoreEntity3, Entity.TAKE_ID_INTO_ACCOUNT
            | Entity.TAKE_KIND_INTO_ACCOUNT | Entity.TAKE_TIMESTAMP_INTO_ACCOUNT);
    Map<String, String> map4 =
        datastoreEntity2.diff(datastoreEntity3, Entity.TAKE_ID_INTO_ACCOUNT
            | Entity.TAKE_KIND_INTO_ACCOUNT | Entity.TAKE_TIMESTAMP_INTO_ACCOUNT);

    assertNull(map1.get("__id__"));
    assertNull(map1.get("__kind__"));
    assertNull(map1.get("timestamp"));

    assertTrue(map1.isEmpty());
    assertTrue(map2.isEmpty());

    assertEquals(map1, map2);
    assertEquals(map3, map4);
  }

  @Test
  public void testGcd() {

    Entity entity1 = createNewEntity();
    Entity entity2 = createNewEntity();

    long id1 = DataStore2.putSync(entity1);
    long id2 = DataStore2.putSync(entity2);

    Entity datastoreEntity1 = DataStore2.getSync(entity1.getKind(), id1);
    Entity datastoreEntity2 = DataStore2.getSync(entity2.getKind(), id2);

    Map<String, String> map1 = datastoreEntity1.gcd(datastoreEntity2);
    Map<String, String> map2 = datastoreEntity2.gcd(datastoreEntity1);

    assertEquals(map1, map2);
    assertEquals(map2, map1);
  }

  @Test
  public void testMergeSuccess() {

    Entity entity1 = createNewEntity();
    pause(1000);
    Entity entity2 = createNewEntity();

    KeyValuePair<Integer, Entity> newEntity1 = entity1.merge(entity2);
    KeyValuePair<Integer, Entity> newEntity2 = entity2.merge(entity1);

    assertEquals(Entity.MERGE_SUCCESS, newEntity1.getKey().intValue());
    assertEquals(Entity.MERGE_SUCCESS, newEntity2.getKey().intValue());

    compareEntities(newEntity1.getValue(), newEntity2.getValue());
    compareEntities(newEntity2.getValue(), newEntity1.getValue());
  }

  @Test
  public void testMergeError() {

    Entity entity1 = createNewEntity();
    Entity entity2 = createNewEntity();

    KeyValuePair<Integer, Entity> newEntity1 = entity1.merge(entity2);
    KeyValuePair<Integer, Entity> newEntity2 = entity2.merge(entity1);

    assertEquals(Entity.MERGE_ERROR, newEntity1.getKey().intValue());
    assertEquals(Entity.MERGE_ERROR, newEntity2.getKey().intValue());

    compareEntities(entity1, newEntity1.getValue());
    compareEntities(entity2, newEntity2.getValue());
  }

  @Test
  public void testMerge() {

    Entity entity1 = createNewEntity();
    pause(1000);
    Entity entity2 = createNewEntity();

    long id1 = DataStore2.putSync(entity1);
    long id2 = DataStore2.putSync(entity2);

    Entity datastoreEntity1 = DataStore2.getSync(entity1.getKind(), id1);
    pause(1000);
    Entity datastoreEntity2 = DataStore2.getSync(entity2.getKind(), id2);

    datastoreEntity2.remove("status");
    KeyValuePair<Integer, Entity> newEntity1 = datastoreEntity1.merge(datastoreEntity2);
    KeyValuePair<Integer, Entity> newEntity2 = datastoreEntity2.merge(datastoreEntity1);

    assertEquals(Entity.MERGE_SUCCESS, newEntity1.getKey().intValue());
    assertEquals(Entity.MERGE_SUCCESS, newEntity2.getKey().intValue());
    assertTrue(newEntity1.getKey().equals(Integer.valueOf(Entity.MERGE_SUCCESS)));

    assertEquals(newEntity1.getKey(), newEntity2.getKey());
    compareEntities(newEntity1.getValue(), newEntity2.getValue());
    compareEntities(newEntity2.getValue(), newEntity1.getValue());
  }

  @Test
  public void testDiffLocal() {
    Entity entity1 = createNewLocalEntity();
    Entity entity2 = new Entity(entity1);
    Entity entity3 = createNewLocalEntity();

    long id1 = LocalDatabase2.putSync(entity1);
    long id2 = LocalDatabase2.putSync(entity2);
    long id3 = LocalDatabase2.putSync(entity3);

    Entity localEntity1 = LocalDatabase2.getSync(entity1.getKind(), id1);
    Entity localEntity2 = LocalDatabase2.getSync(entity2.getKind(), id2);
    Entity localEntity3 = LocalDatabase2.getSync(entity3.getKind(), id3);

    Map<String, String> map1 =
        localEntity1.diff(localEntity2, Entity.TAKE_ID_INTO_ACCOUNT | Entity.TAKE_KIND_INTO_ACCOUNT
            | Entity.TAKE_TIMESTAMP_INTO_ACCOUNT);
    Map<String, String> map2 =
        localEntity2.diff(localEntity1, Entity.TAKE_ID_INTO_ACCOUNT | Entity.TAKE_KIND_INTO_ACCOUNT
            | Entity.TAKE_TIMESTAMP_INTO_ACCOUNT);
    Map<String, String> map3 =
        localEntity1.diff(localEntity3, Entity.TAKE_ID_INTO_ACCOUNT | Entity.TAKE_KIND_INTO_ACCOUNT
            | Entity.TAKE_TIMESTAMP_INTO_ACCOUNT);
    Map<String, String> map4 =
        localEntity2.diff(localEntity3, Entity.TAKE_ID_INTO_ACCOUNT | Entity.TAKE_KIND_INTO_ACCOUNT
            | Entity.TAKE_TIMESTAMP_INTO_ACCOUNT);

    assertNull(map1.get("__id__"));
    assertNull(map1.get("__kind__"));
    assertNull(map1.get("timestamp"));

    assertTrue(map1.isEmpty());
    assertTrue(map2.isEmpty());

    assertEquals(map1, map2);
    assertEquals(map3, map4);

  }

  @Test
  public void testGcdLocal() {
    Entity entity1 = createNewLocalEntity();
    Entity entity2 = createNewLocalEntity();

    long id1 = LocalDatabase2.putSync(entity1);
    long id2 = LocalDatabase2.putSync(entity2);

    Entity localEntity1 = LocalDatabase2.getSync(entity1.getKind(), id1);
    Entity localEntity2 = LocalDatabase2.getSync(entity2.getKind(), id2);

    Map<String, String> map1 = localEntity1.gcd(localEntity2);
    Map<String, String> map2 = localEntity2.gcd(localEntity1);

    assertEquals(map1, map2);
    assertEquals(map2, map1);
  }

  @Test
  public void testMergeLocal() {
    Entity entity1 = createNewLocalEntity();
    pause(1000);
    Entity entity2 = createNewLocalEntity();

    long id1 = LocalDatabase2.putSync(entity1);
    long id2 = LocalDatabase2.putSync(entity2);

    Entity localEntity1 = LocalDatabase2.getSync(entity1.getKind(), id1);
    pause(1000);
    Entity localEntity2 = LocalDatabase2.getSync(entity2.getKind(), id2);

    localEntity1.put("height", 55D);

    KeyValuePair<Integer, Entity> newEntity1 = localEntity1.merge(localEntity2);
    KeyValuePair<Integer, Entity> newEntity2 = localEntity2.merge(localEntity1);

    assertEquals(Entity.MERGE_SUCCESS, newEntity1.getKey().intValue());
    assertEquals(Entity.MERGE_SUCCESS, newEntity2.getKey().intValue());
    assertTrue(newEntity1.getKey().equals(Integer.valueOf(Entity.MERGE_SUCCESS)));

    assertEquals(newEntity1.getKey(), newEntity2.getKey());
    compareEntities(newEntity1.getValue(), newEntity2.getValue());
    compareEntities(newEntity2.getValue(), newEntity1.getValue());
  }

  private Map<String, String> mapOf(List<String> list) {
    Map<String, String> map = new HashMap<String, String>();

    for (String property : list) {
      String str[] = property.split(":", 2);
      map.put(str[0], str[1]);
    };

    return map;
  }

  private void compareEntities(Entity entity1, Entity entity2) {
    assertEquals(entity1.getProperties().size(), entity2.getProperties().size());

    Map<String, String> map1 = mapOf(entity1.getProperties());
    Map<String, String> map2 = mapOf(entity2.getProperties());

    assertEquals(map1.size(), map2.size());

    Set<String> keys = map1.keySet();

    for (String key : keys) {
      assertEquals(map1.get(key), map2.get(key));
    }
  }

  private static Entity createNewEntity() {
    String[] array =
      {
        "giaccherini", "nedved", "shevchenko", "seedorf", "carzola", "podolski", "walcott",
        "rebrov", "robben", "silva", "luiz"};

    Random r = new Random();
    Entity entity = new Entity("person");

    String name = array[r.nextInt(array.length)];
    double height = 188D;
    float salary = r.nextFloat() * (10000.0F - 5000.0F) + 5000.0F;
    BigDecimal bd = new BigDecimal(Float.toString(salary));
    BigDecimal rounded = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
    salary = rounded.floatValue();

    entity.put("name", name);
    entity.put("age", 20L);
    entity.put("height", height);
    entity.put("salary", salary);
    entity.put("status", r.nextBoolean());

    return entity;
  }

  private void pause(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static Entity createNewLocalEntity() {
    String[] array =
        {
            "giaccherini", "nedved", "shevchenko", "seedorf", "carzola", "podolski", "walcott",
            "rebrov", "robben", "silva", "luiz"};

    Random r = new Random();
    long id = (long) Math.floor(Math.random() * 900L) + 100L;

    Entity entity = new Entity("person", id);

    String name = array[r.nextInt(array.length)];
    double height = 188D;
    float salary = r.nextFloat() * (10000.0F - 5000.0F) + 5000.0F;
    BigDecimal bd = new BigDecimal(Float.toString(salary));
    BigDecimal rounded = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
    salary = rounded.floatValue();

    entity.put("name", name);
    entity.put("age", 20L);
    entity.put("height", height);
    entity.put("salary", salary);
    entity.put("status", r.nextBoolean());
    entity.put("__timestamp__", "asdfasdFASDF");

    return entity;
  }

}
