/**
 * Copyright (c) 2013 MNCC
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
package fr.mncc.gwttoolbox.appengine.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import fr.mncc.gwttoolbox.appengine.shared.Clause2;
import fr.mncc.gwttoolbox.appengine.shared.Filter2;
import fr.mncc.gwttoolbox.appengine.shared.SQuery2;
import fr.mncc.gwttoolbox.primitives.server.DateUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import static junit.framework.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppEngineVsPostgreSqlTest {

  private static Connection conn = JdbcPostgresConnection.getConnection("127.0.0.1", 5432, "mydb",
      "postgres", "zamani95");
  private static String createTable =
      "CREATE TABLE person (id serial, name varchar(100), age bigint, height double precision, birthdate date, salary real,status boolean)";
  private static String dropTable = "DROP TABLE IF EXISTS person";
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
  public void fromAppEngineEntity() {

  }

  @Test
  public void putSync() {
    createEntity();

    fr.mncc.gwttoolbox.primitives.shared.Entity e1 =
        new fr.mncc.gwttoolbox.primitives.shared.Entity("person", 739);

    e1.put("name", "cesc");
    e1.put("age", 11);
    e1.put("height", 183D);
    e1.put("birthdate", DateUtils.fromDateIso("1997-03-22"));
    e1.put("salary", 55536.775F);
    e1.put("status", true);

    fr.mncc.gwttoolbox.primitives.shared.Entity e2 =
        new fr.mncc.gwttoolbox.primitives.shared.Entity("person", 739);

    e2.put("name", "cesc");
    e2.put("age", 11);
    e2.put("height", 183D);
    e2.put("birthdate", "1997-03-22");
    e2.put("salary", 55536.775F);
    e2.put("status", "true");

    Long id1 = DataStore2.putSync(e1);
    Long id2 = PostgreSql2.putSync(e2);

    dropAndCreateTable();
    assertEquals(id1, id2);
  }

  @Test
  public void delete() {
    dropAndCreateTable();

    createEntity();

    // fr.mncc.gwttoolbox.primitives.shared.Entity entity1 =
    DataStore2.getSync("person", 321);

    // fr.mncc.gwttoolbox.primitives.shared.Entity entity2 =
    PostgreSql2.getSync("person", 321);

    boolean bool1 = DataStore2.deleteSync("person", 321);
    boolean bool2 = PostgreSql2.deleteSync("person", 321);

    assertEquals(bool1, bool2);

    dropAndCreateTable();
  }

  @Test
  public void listSize() {
    createEntity();
    Filter2 clauseLeft2 = Filter2.equal("name", "nedved");
    Filter2 clauseRight2 = Filter2.greaterThanOrEqual("age", 64);

    Clause2 clauseLeft = Filter2.and(clauseLeft2, clauseRight2);
    Filter2 clauseRight = Filter2.equal("age", 34);;

    Clause2 sc = Filter2.or(clauseLeft, clauseRight);

    SQuery2 squery = new SQuery2("person");
    squery.addClause(sc);

    squery.setKeysOnly(); // activation

    long size1 = DataStore2.listSize(squery);
    long size2 = PostgreSql2.listSize(squery);

    assertEquals(size1, size2);

    dropAndCreateTable();
  }

  @Test
  public void list() {
    createEntity();
    Filter2 clauseLeft2 = Filter2.equal("name", "seedorf");
    Filter2 clauseRight2 = Filter2.greaterThanOrEqual("age", 64);

    Clause2 clauseLeft = Clause2.and(clauseLeft2, clauseRight2);
    Filter2 clauseRight = Filter2.equal("age", 34);;

    Clause2 sc = Clause2.or(clauseLeft, clauseRight);

    SQuery2 squery = new SQuery2("person");
    squery.setKeysOnly();

    squery.addClause(sc);

    Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> e1 = DataStore2.list(squery);
    Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> e2 = PostgreSql2.list(squery);

    for (Iterator<fr.mncc.gwttoolbox.primitives.shared.Entity> iter1 = e1.iterator(), iter2 =
        e2.iterator(); iter1.hasNext() && iter2.hasNext();) {
      fr.mncc.gwttoolbox.primitives.shared.Entity entity1 =
          (fr.mncc.gwttoolbox.primitives.shared.Entity) iter1.next();
      fr.mncc.gwttoolbox.primitives.shared.Entity entity2 =
          (fr.mncc.gwttoolbox.primitives.shared.Entity) iter2.next();

      compareEntities(entity1, entity2);
    }
    dropAndCreateTable();
  }

  @Test
  public void list2() {
    dropAndCreateTable();
    createEntity();
    Filter2 clauseLeft2 = Filter2.equal("name", "seedorf");
    Filter2 clauseRight2 = Filter2.greaterThanOrEqual("age", 64);

    Clause2 clauseLeft = Clause2.and(clauseLeft2, clauseRight2);
    Filter2 clauseRight = Filter2.equal("age", 34);

    Clause2 sc = Clause2.or(clauseLeft, clauseRight);

    SQuery2 squery = new SQuery2("person");
    // squery.setKeysOnly();

    squery.addClause(sc);

    Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> e1 = DataStore2.list(squery, 1, 10);
    Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> e2 = PostgreSql2.list(squery, 1, 10);

    for (Iterator<fr.mncc.gwttoolbox.primitives.shared.Entity> iter1 = e1.iterator(), iter2 =
        e2.iterator(); iter1.hasNext() && iter2.hasNext();) {
      fr.mncc.gwttoolbox.primitives.shared.Entity entity1 =
          (fr.mncc.gwttoolbox.primitives.shared.Entity) iter1.next();
      fr.mncc.gwttoolbox.primitives.shared.Entity entity2 =
          (fr.mncc.gwttoolbox.primitives.shared.Entity) iter2.next();

      compareEntities(entity1, entity2);
    }
    dropAndCreateTable();
  }

  @Test
  public void listIDs() {
    createEntity();
    Filter2 clauseLeft2 = Filter2.equal("name", "cesc");
    Filter2 clauseRight2 = Filter2.greaterThanOrEqual("age", 64);

    Clause2 clauseLeft = Clause2.and(clauseLeft2, clauseRight2);
    Filter2 clauseRight = Filter2.equal("age", 34);;

    Clause2 sc = Clause2.or(clauseLeft, clauseRight);

    SQuery2 squery = new SQuery2("person");
    squery.setKeysOnly();

    squery.addClause(sc);

    Iterable<Long> e1 = DataStore2.listIds(squery);
    Iterable<Long> e2 = PostgreSql2.listIds(squery);

    for (Iterator<Long> iter1 = e1.iterator(), iter2 = e2.iterator(); iter1.hasNext()
        && iter2.hasNext();) {
      Long id1 = iter1.next();
      Long id2 = iter2.next();
      assertEquals(id1, id2);
    }

    dropAndCreateTable();
  }

  @Test
  public void listIDs2() {
    createEntity();
    Filter2 clauseLeft2 = Filter2.equal("name", "cesc");
    Filter2 clauseRight2 = Filter2.greaterThanOrEqual("age", 64);

    Clause2 clauseLeft = Clause2.and(clauseLeft2, clauseRight2);
    Filter2 clauseRight = Filter2.equal("age", 34);;

    Clause2 sc = Clause2.or(clauseLeft, clauseRight);

    SQuery2 squery = new SQuery2("person");

    squery.addClause(sc);

    // squery.setKeysOnly();
    Iterable<Long> e1 = DataStore2.listIds(squery, 1, 10);
    Iterable<Long> e2 = PostgreSql2.listIds(squery, 1, 10);

    for (Iterator<Long> iter1 = e1.iterator(), iter2 = e2.iterator(); iter1.hasNext()
        || iter2.hasNext();) {
      Long id1 = iter1.next();
      Long id2 = iter2.next();
      assertEquals(id1, id2);
    }

    dropAndCreateTable();

  }

  @Test
  public void get() {
    dropAndCreateTable();
    createEntity();

    fr.mncc.gwttoolbox.primitives.shared.Entity entity1 = DataStore2.getSync("person", 33);
    fr.mncc.gwttoolbox.primitives.shared.Entity entity2 = PostgreSql2.getSync("person", 33);

    // first check
    compareEntities(entity1, entity2);
    entity1 = DataStore2.getSync("person", 223);
    entity2 = PostgreSql2.getSync("person", 223);

    // 2nd check
    compareEntities(entity1, entity2);

    entity1 = DataStore2.getSync("person", 282);
    entity2 = PostgreSql2.getSync("person", 282);

    // 3rd check
    compareEntities(entity1, entity2);
    dropAndCreateTable();

  }

  @Test
  public void testEqualOperation() {
    createEntity();

    Filter2 sFilter = Filter2.equal("name", "nedved");

    SQuery2 squery = new SQuery2("person");
    squery.setKeysOnly();

    squery.addClause(sFilter);

    long size1 = DataStore2.listSize(squery);
    long size2 = PostgreSql2.listSize(squery);
    assertEquals(size1, size2);

    dropAndCreateTable();
  }

  @Test
  public void testNotEqualOperation() {
    createEntity();

    Filter2 sFilter = Filter2.notEqual("name", "nedved");

    SQuery2 squery = new SQuery2("person");
    squery.setKeysOnly();

    squery.addClause(sFilter);

    long size1 = DataStore2.listSize(squery);
    long size2 = PostgreSql2.listSize(squery);
    assertEquals(size1, size2);

    dropAndCreateTable();
  }

  @Test
  public void testLessThanOperation() {
    createEntity();

    Filter2 sFilter = Filter2.lessThan("age", 60);

    SQuery2 squery = new SQuery2("person");
    squery.setKeysOnly();

    squery.addClause(sFilter);

    long size1 = DataStore2.listSize(squery);
    long size2 = PostgreSql2.listSize(squery);
    assertEquals(size1, size2);

    dropAndCreateTable();
  }

  @Test
  public void testLessThanOrEqualOperation() {
    createEntity();

    Filter2 sFilter = Filter2.lessThanOrEqual("age", 60);

    SQuery2 squery = new SQuery2("person");
    squery.setKeysOnly();

    squery.addClause(sFilter);

    long size1 = DataStore2.listSize(squery);
    long size2 = PostgreSql2.listSize(squery);
    assertEquals(size1, size2);

    dropAndCreateTable();
  }

  @Test
  public void testGreaterThanOperation() {
    createEntity();

    Filter2 sFilter = Filter2.greaterThan("age", 60);

    SQuery2 squery = new SQuery2("person");
    squery.setKeysOnly();

    squery.addClause(sFilter);

    long size1 = DataStore2.listSize(squery);
    long size2 = PostgreSql2.listSize(squery);
    assertEquals(size1, size2);

    dropAndCreateTable();
  }

  @Test
  public void testGreaterThanOrEqualOperation() {
    createEntity();

    Filter2 sFilter = Filter2.greaterThanOrEqual("age", 60);

    SQuery2 squery = new SQuery2("person");
    squery.setKeysOnly();

    squery.addClause(sFilter);

    long size1 = DataStore2.listSize(squery);
    long size2 = PostgreSql2.listSize(squery);
    assertEquals(size1, size2);

    dropAndCreateTable();
  }

  private void createEntity() {

    for (int count = 1, j = 0; count <= 1000; count++) {

      float minX = 5000.0F;
      float maxX = 10000.0F;
      double min = 500.0D;
      double max = 1000.0D;

      String[] array = {"nedved", "shevchenko", "seedorf", "carzola", "podolski"};
      long[] ages = {20L, 40L, 60L, 80L, 100L};
      // int length = array.length;
      Random r = new Random();

      int month, year, day;
      Random call = new Random();
      month = call.nextInt(Calendar.DECEMBER - Calendar.JANUARY + 1) + Calendar.FEBRUARY;
      year = call.nextInt(40) + 1960;
      day = call.nextInt(27) + 1;

      String str = year + "-" + month + "-" + day;

      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date utilDate = null;
      try {
        utilDate = formatter.parse(str);
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      com.google.appengine.api.datastore.Entity entity = new Entity("person", count);

      if (count <= 800 && count % 200 == 0) {
        ++j;
      }
      String name = array[j];
      // long age = (long) (r.nextDouble() * (max - min) + min) / 20;
      long age = ages[j];
      double height = r.nextDouble() * (max - min) + min; // double
      boolean status = r.nextBoolean();
      float salary = r.nextFloat() * (maxX - minX) + minX; // float

      BigDecimal bd = new BigDecimal(Float.toString(salary));
      BigDecimal rounded = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
      salary = rounded.floatValue();

      entity.setProperty("name", name);
      entity.setProperty("age", age);
      entity.setProperty("height", Math.round(height * 100.0D) / 100.0D);
      entity.setProperty("birthdate", utilDate);
      entity.setProperty("salary", salary);
      entity.setProperty("status", status);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(entity);// put entity in datastore

      // Create PostgreSql2 query
      String query =
          "INSERT INTO person (name, age, height, birthdate, salary, status) VALUES('" + name
              + "', " + +age + ", " + Math.round(height * 100.0D) / 100.0D + ", '" + str + "', "
              + +salary + ", " + "'" + status + "')";

      try {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }

  }

  private void compareEntities(fr.mncc.gwttoolbox.primitives.shared.Entity entity1,
      fr.mncc.gwttoolbox.primitives.shared.Entity entity2) {

    assertEquals(entity1.getProperties().size(), entity2.getProperties().size());
    List<String> properties = entity1.getProperties();
    for (int i = 0; i < properties.size(); i++) {
      assertEquals(entity1.getAsObject(properties.get(i)), entity2.getAsObject(properties.get(i)));
    }

    properties.clear();

    properties = entity2.getProperties();
    for (int i = 0; i < properties.size(); i++) {
      assertEquals(entity2.getAsObject(properties.get(i)), entity1.getAsObject(properties.get(i)));
    }
  }

  private void dropAndCreateTable() {
    // drop and recreate table person
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(dropTable);
      stmt.executeUpdate(createTable);
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
