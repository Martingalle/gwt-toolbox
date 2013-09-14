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

import fr.mncc.gwttoolbox.appengine.shared.SQuery2;
import fr.mncc.gwttoolbox.primitives.shared.Entity;
import fr.mncc.gwttoolbox.primitives.shared.ObjectUtils;
import org.postgresql.PGResultSetMetaData;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSql2 {

  private final static Logger logger_ = Logger.getLogger(PostgreSql2.class.getCanonicalName());
  private static Connection conn_ = JdbcPostgresConnection.getConnection("127.0.0.1", 5432, "mydb",
      "postgres", "zamani95");
  private final static String ID = "id";

  /**
   * This method takes a {@link fr.mncc.gwttoolbox.primitives.shared.Entity} entity as a parameter
   * and creates an SQL INSERT query from the columns and values of the entity.
   * 
   * @param entity - the entity
   * @return a String representing the query created
   */
  private static String createInsertQuery(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {

    String separator = "";
    String columns = "";
    String values = "";

    for (String propertyName : entity.keySet()) {
      columns += (separator + propertyName);
      values += (separator + preparedQuery(entity.getAsObject(propertyName)));
      separator = ",";
    }

    return "INSERT INTO " + entity.getKind() + "(" + columns + ") VALUES(" + values + ")";
  }

  /**
   * This method takes a {@link fr.mncc.gwttoolbox.primitives.shared.Entity} entity as a aparameter
   * and creates an SQL UPDATE query from the columns and values of the entity.
   * 
   * @param entity - the entity
   * @return a String representing the query created
   */
  private static String createUpdateQuery(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {

    String separator = "";
    String query = "UPDATE " + entity.getKind() + " SET ";

    for (String propertyName : entity.keySet()) {
      query += (separator + propertyName + "=" + preparedQuery(entity.getAsObject(propertyName)));
      separator = ",";
    }

    return query + " WHERE id = " + entity.getId();
  }

  private static boolean deletePostgreSQL(String kind, long id, String ancestorKind, long ancestorId) {
    String query = "DELETE FROM " + kind + " WHERE id = " + id;
    int delete = Integer.MIN_VALUE;
    Statement st = null;
    try {
      st = conn_.createStatement();
      delete = st.executeUpdate(query);
      st.close();
    } catch (SQLException e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return (delete == Integer.MIN_VALUE || delete != 1) ? false : true;

  }

  /**
   * Creates a list of iterable entities from a ResultSet
   * 
   * @param resultSet
   * @return
   * @throws SQLException
   */
  private static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> createEntityFromResultSet(
      ResultSet resultSet) throws SQLException {
    ResultSetMetaData rsMetaData = resultSet.getMetaData();
    PGResultSetMetaData data = (PGResultSetMetaData) rsMetaData; // used to get the table name
    int columnCount = rsMetaData.getColumnCount();

    List<fr.mncc.gwttoolbox.primitives.shared.Entity> entities =
        new ArrayList<fr.mncc.gwttoolbox.primitives.shared.Entity>();
    while (resultSet.next()) {

      if (columnCount != 0) {
        // table name gotten here. used to create an entity
        // used resultSet.getLong("id") to get the value of the column named "id" in a long format
        // this is because the id can be placed anywhere in the query by the user
        Entity entity = new Entity(data.getBaseTableName(1), resultSet.getLong(ID)); // creates new
                                                                                     // entity and
                                                                                     // sets id
        for (int i = 1; i <= columnCount; i++) {
          if (!rsMetaData.getColumnLabel(i).equals(ID)) {
            entity.put(rsMetaData.getColumnName(i), resultSet.getObject(i));
          }
        }
        entities.add(entity);

      }
    }

    return entities;

  }

  private static Iterable<Long> getIds(SQuery2 toolboxQuery, int startIndex, int amount) {
    toolboxQuery.setKeysOnly(); // Retrieve only the ids of the toolboxQuery
    String postgresQuery = QueryConverter2.getAsPostgreSqlQuery(toolboxQuery);
    if ((startIndex != 0 && amount != 0) || (startIndex == 0 && amount != 0))
      postgresQuery += " LIMIT " + amount + " OFFSET " + startIndex;

    List<Long> ids = new ArrayList<Long>();

    Statement stmt = null;

    try {
      stmt = conn_.createStatement();
      ResultSet resultSet = stmt.executeQuery(postgresQuery);

      while (resultSet.next()) {
        ids.add(resultSet.getLong("id")); // there is only one column which is the column "id"
      }
    } catch (SQLException e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return ids;
  }

  private static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list2(SQuery2 toolboxQuery,
      int startIndex, int amount) {
    String postgresQuery = QueryConverter2.getAsPostgreSqlQuery(toolboxQuery);
    if ((startIndex != 0 && amount != 0) || (startIndex == 0 && amount != 0))
      postgresQuery += " LIMIT " + amount + " OFFSET " + startIndex;

    Statement stmt = null;
    try {
      stmt = conn_.createStatement();
      ResultSet resultSet = stmt.executeQuery(postgresQuery);
      return createEntityFromResultSet(resultSet);
    } catch (SQLException e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return null;

  }

  /**
   * 
   * @param obj
   * @return String a prepared string to be used in an INSERT or UPDATE query
   */
  public static String preparedQuery(Object obj) {
    String value = ObjectUtils.toString(obj);
    if (ObjectUtils.isTimeStamp(value))
      return "to_timestamp('" + obj + "', 'YYYY-MM-DD HH24:MI:SS')";
    if (ObjectUtils.isTime(value))
      return "'" + ObjectUtils.asString(value) + "'";
    if (ObjectUtils.isRealDate(value))
      return "'" + ObjectUtils.asString(value) + "'";
    if (ObjectUtils.isDate(value))
      return "'" + ObjectUtils.asString(value) + "'";
    if (ObjectUtils.isString(value))
      return "'" + ObjectUtils.asString(value) + "'";
    if (ObjectUtils.isBoolean(value))
      return ObjectUtils.asString(value);
    if (ObjectUtils.isInteger(value))
      return ObjectUtils.asString(value);
    if (ObjectUtils.isDouble(value))
      return ObjectUtils.asString(value);
    if (ObjectUtils.isFloat(value))
      return ObjectUtils.asString(value);
    if (ObjectUtils.isLong(value))
      return ObjectUtils.asString(value);
    return "";
  }

  public static fr.mncc.gwttoolbox.primitives.shared.Entity fromAppEngineEntity(
      com.google.appengine.api.datastore.Entity appEngineEntity) {
    return null;
  }

  public static Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
    return putSync(entity, null, 0);
  }

  public static Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity,
      String ancestorKind, long ancestorId) {

    try {
      String query = "";
      Long id = null;
      Statement st = null;

      if (entity.getId() <= 0) {
        query = createInsertQuery(entity);
      } else {
        query = createUpdateQuery(entity);
      }

      try {
        st = conn_.createStatement();

        st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = st.getGeneratedKeys();

        if (rs.next()) {
          id = rs.getLong("id");
        }

        rs.close();
        st.close();
      } catch (SQLException e) {
        logger_.log(Level.SEVERE, e.getMessage(), e);
      }

      return id;
    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return 0L;

  }

  public static List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities) {
    return putSync(entities, null, 0);
  }

  public static List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities,
      String ancestorKind, long ancestorId) {
    List<Long> keys = new ArrayList<Long>();

    for (Entity entity : entities) {
      Long id = putSync(entity, ancestorKind, ancestorId);
      keys.add(id);
    }

    return keys;
  }

  public static fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id) {
    return getSync(kind, id, null, 0);
  }

  public static fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id,
      String ancestorKind, long ancestorId) {

    try {
      fr.mncc.gwttoolbox.primitives.shared.Entity entity = new Entity(kind, id);

      Statement st = null;
      try {
        st = conn_.createStatement();
        ResultSet resultSet = st.executeQuery("SELECT * FROM " + kind + " WHERE id = " + id);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        while (resultSet.next()) {
          for (int j = 2; j <= columnsNumber; j++) {
            if (!rsmd.getColumnName(j).equals(ID))
              entity.put(rsmd.getColumnName(j), resultSet.getObject(j));
          }
        }
        st.close();
        resultSet.close();
      } catch (SQLException e) {
        logger_.log(Level.SEVERE, e.getMessage(), e);
      }

      return entity;
    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }
    return null;
  }

  public static Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids) {
    return getSync(kind, ids, null, 0);
  }

  public static Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids, String ancestorKind, long ancestorId) {
    Map<Long, Entity> map = new HashMap<Long, Entity>();

    for (Long id : ids) {
      Entity entity = getSync(kind, id, ancestorKind, ancestorId);
      map.put(id, entity);
    }

    return map;
  }

  public static boolean deleteSync(String kind, long id) {
    return deleteSync(kind, id, null, 0);

  }

  public static boolean deleteSync(String kind, long id, String ancestorKind, long ancestorId) {
    return deletePostgreSQL(kind, id, ancestorKind, ancestorId);
  }

  public static boolean deleteSync(String kind, Iterable<Long> ids) {
    return deleteSync(kind, ids, null, 0);
  }

  public static boolean deleteSync(String kind, Iterable<Long> ids, String ancestorKind,
      long ancestorId) {

    boolean status = false;
    try {

      for (Long id : ids) {
        status = deletePostgreSQL(kind, id, ancestorKind, ancestorId);
      }

    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return status;

  }

  public static long listSize(SQuery2 toolboxQuery) {
    int size = 0;

    String postGresQuery = QueryConverter2.getAsPostgreSqlQuery(toolboxQuery);
    Statement stmt = null;

    try {
      stmt = conn_.createStatement();
      ResultSet rs = stmt.executeQuery(postGresQuery);

      while (rs.next()) {
        size++; // increment the size
      }
      stmt.close();
      rs.close();
    } catch (SQLException e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return size;

  }

  public static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery) {
    return list2(toolboxQuery, 0, 0);

  }

  public static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery,
      int startIndex, int amount) {
    return list2(toolboxQuery, startIndex, amount);
  }

  public static Iterable<Long> listIds(SQuery2 toolboxQuery) {
    return getIds(toolboxQuery, 0, 0);

  }

  public static Iterable<Long> listIds(SQuery2 toolboxQuery, int startIndex, int amount) {
    return getIds(toolboxQuery, startIndex, amount);
  }
}
