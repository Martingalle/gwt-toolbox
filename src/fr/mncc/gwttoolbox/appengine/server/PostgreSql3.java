package fr.mncc.gwttoolbox.appengine.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.postgresql.PGResultSetMetaData;

import fr.mncc.gwttoolbox.appengine.shared.Clause2;
import fr.mncc.gwttoolbox.appengine.shared.Filter2;
import fr.mncc.gwttoolbox.appengine.shared.FilterOperator2;
import fr.mncc.gwttoolbox.appengine.shared.Projection2;
import fr.mncc.gwttoolbox.appengine.shared.SQuery2;
import fr.mncc.gwttoolbox.appengine.shared.Sort2;
import fr.mncc.gwttoolbox.primitives.shared.Entity;
import fr.mncc.gwttoolbox.primitives.shared.ObjectUtils;

public class PostgreSql3 implements DatabaseDriver {

  /**
   * This class makes an instance of a JDBC connection to a PostgreSQL database
   * 
   */
  private static class JdbcPostgresConnection {

    private final static Logger logger_ = Logger.getLogger(JdbcPostgresConnection.class
        .getCanonicalName());

    private final static String DB_DRIVER = "org.postgresql.Driver";

    public static Connection getConnection(String host, int port, String databaseName,
        String userName, String password) {
      try {
        Class.forName(DB_DRIVER);
      } catch (ClassNotFoundException e) {
        logger_.log(Level.SEVERE, e.getMessage(), e);
      }

      Connection connection = null;

      try {
        connection =
            DriverManager.getConnection("jdbc:postgresql://" + host + ":" + String.valueOf(port)
                + "/" + databaseName, userName, password);
      } catch (SQLException e) {
        logger_.log(Level.SEVERE, e.getMessage(), e);
      }

      return connection;
    }
  }

  private static class QueryConverter2 {

    public static String getAsPostgreSqlQuery(SQuery2 squery) {

      // Add projections
      String query =
          "SELECT " + getProjectionQuery(squery) + " FROM " + squery.getKind() + " WHERE ";

      // Add clauses
      List<String> clauses = new ArrayList<String>();
      buildClause(squery.getClause(), clauses);

      for (String clause : clauses) {
        query += clause;
      }

      // Add sort order
      if (!squery.getSorters().isEmpty())
        query += getSortQuery(squery);

      return query;

    }

    /**
     * Returns an int which signifies the operator to be used
     * 
     * @param operator
     * @return
     */
    private static String getAsPostgreSqlOperator(int operator) {
      if (operator == FilterOperator2.EQUAL)
        return "=";
      if (operator == FilterOperator2.LESS_THAN)
        return "<";
      if (operator == FilterOperator2.LESS_THAN_OR_EQUAL)
        return "<=";
      if (operator == FilterOperator2.GREATER_THAN)
        return ">";
      if (operator == FilterOperator2.GREATER_THAN_OR_EQUAL)
        return ">=";
      if (operator == FilterOperator2.NOT_EQUAL)
        return "<>";
      if (operator == FilterOperator2.IN)
        return "IN";
      return null; // This case should never happen
    }

    private static String getSortQuery(SQuery2 squery) {
      String sortQuery = " ORDER BY ";

      List<Sort2> listOfSorters = squery.getSorters();

      for (Sort2 sorter : listOfSorters) {
        String propertyName = sorter.getPropertyName();
        if (propertyName.equals("__key__"))
          sortQuery += "id" + " ";
        else
          sortQuery += propertyName + " ";
        sortQuery += sorter.isAscending() ? "ASC, " : "DESC, ";
      }

      sortQuery = sortQuery.substring(0, sortQuery.length() - 2);

      return sortQuery;
    }

    private static String getProjectionQuery(SQuery2 squery) {
      String projections = "";
      boolean containsIdColumn = false;

      List<Projection2> listOfProjections = squery.getProjections();

      if (squery.isKeysOnly())
        return "id";
      if (listOfProjections.isEmpty() || listOfProjections == null)
        return "*";

      // checks if the column id projection was created
      // set containsIdColumn to true if it exists as a projection, sets to false otherwise
      for (Projection2 sp : listOfProjections) {
        if (sp.getPropertyName().equalsIgnoreCase("id")) {
          containsIdColumn = true;
          break;
        }
      }

      // add coulumn id projection if column id projection doesn't exist
      if (!containsIdColumn)
        listOfProjections.add(0, Projection2.of("id", Long.class));

      for (Projection2 projection : listOfProjections) {
        projections += projection.getPropertyName() + ", ";
      }

      projections = projections.substring(0, projections.length() - 2);
      return projections;

    }

    private static void buildClause(Clause2 clause, List<String> result) {

      if (clause.getLeftClause() != null) {
        buildClause(clause.getLeftClause(), result);
      }

      if (clause.isLeaf()) {
        Filter2 sfilter = (Filter2) clause;

        result.add(sfilter.getPropertyName() + " " + getAsPostgreSqlOperator(sfilter.getOperator())
            + " " + preparedQuery(sfilter.getPropertyValue()));
      }

      if (clause.getRightClause() != null) {
        if (clause.isAnd())
          result.add(" AND ");
        else
          result.add(" OR ");
        buildClause(clause.getRightClause(), result);
      }
    }
  }

  private final static Logger logger_ = Logger.getLogger(PostgreSql3.class.getCanonicalName());
  private final static Connection conn_ = JdbcPostgresConnection.getConnection("127.0.0.1", 5432,
      "mydb", "postgres", "zamani95");
  private final static String ID = "id";

  /**
   * This method takes a {@link fr.mncc.gwttoolbox.primitives.shared.Entity} entity as a parameter
   * and creates an SQL INSERT query from the columns and values of the entity.
   * 
   * @param entity - the entity
   * @return a String representing the query created
   */
  private String createInsertQuery(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
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
  private String createUpdateQuery(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
    String separator = "";
    String query = "UPDATE " + entity.getKind() + " SET ";

    for (String propertyName : entity.keySet()) {
      query += (separator + propertyName + "=" + preparedQuery(entity.getAsObject(propertyName)));
      separator = ",";
    }

    return query + " WHERE id = " + entity.getId();
  }

  private boolean deletePostgreSQL(String kind, long id, String ancestorKind, long ancestorId) {
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
  private Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> createEntityFromResultSet(
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

  private Iterable<Long> getIds(SQuery2 toolboxQuery, int startIndex, int amount) {
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

  private Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list2(SQuery2 toolboxQuery,
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
  private static String preparedQuery(Object obj) {
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

  @Override
  public fr.mncc.gwttoolbox.primitives.shared.Entity fromAppEngineEntity(
      com.google.appengine.api.datastore.Entity appEngineEntity) {
    return null;
  }

  @Override
  public Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
    return putSync(entity, null, 0);
  }

  @Override
  public Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity, String ancestorKind,
      long ancestorId) {
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

  @Override
  public List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities) {
    return putSync(entities, null, 0);
  }

  @Override
  public List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities,
      String ancestorKind, long ancestorId) {
    List<Long> keys = new ArrayList<Long>();

    for (Entity entity : entities) {
      Long id = putSync(entity, ancestorKind, ancestorId);
      keys.add(id);
    }

    return keys;
  }

  @Override
  public fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id) {
    return getSync(kind, id, null, 0);
  }

  @Override
  public fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id,
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

  @Override
  public Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids) {
    return getSync(kind, ids, null, 0);
  }

  @Override
  public Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids, String ancestorKind, long ancestorId) {
    Map<Long, Entity> map = new HashMap<Long, Entity>();

    for (Long id : ids) {
      Entity entity = getSync(kind, id, ancestorKind, ancestorId);
      map.put(id, entity);
    }

    return map;
  }

  @Override
  public boolean deleteSync(String kind, long id) {
    return deleteSync(kind, id, null, 0);
  }

  @Override
  public boolean deleteSync(String kind, long id, String ancestorKind, long ancestorId) {
    return deletePostgreSQL(kind, id, ancestorKind, ancestorId);
  }

  @Override
  public boolean deleteSync(String kind, Iterable<Long> ids) {
    return deleteSync(kind, ids, null, 0);
  }

  @Override
  public boolean deleteSync(String kind, Iterable<Long> ids, String ancestorKind, long ancestorId) {
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

  @Override
  public long listSize(SQuery2 toolboxQuery) {
    int size = 0;

    String postGresQuery = QueryConverter2.getAsPostgreSqlQuery(toolboxQuery);
    System.out.println(postGresQuery);
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

  @Override
  public Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery) {
    return list2(toolboxQuery, 0, 0);
  }

  @Override
  public Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery,
      int startIndex, int amount) {
    return list2(toolboxQuery, startIndex, amount);
  }

  @Override
  public Iterable<Long> listIds(SQuery2 toolboxQuery) {
    return getIds(toolboxQuery, 0, 0);

  }

  @Override
  public Iterable<Long> listIds(SQuery2 toolboxQuery, int startIndex, int amount) {
    return getIds(toolboxQuery, startIndex, amount);
  }
}
