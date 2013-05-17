package fr.mncc.gwttoolbox.appengine.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class makes an instance of a JDBC connection to a PostgreSQL database
 * 
 * @author mkab
 * 
 */
public class JdbcPostgresConnection {

  private static final String DB_DRIVER = "org.postgresql.Driver";

  public static Connection getConnection(String host, int port, String databaseName,
      String userName, String password) {
    try {
      Class.forName(DB_DRIVER);
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }

    Connection connection = null;

    try {
      connection =
          DriverManager.getConnection("jdbc:postgresql://" + host + ":" + String.valueOf(port)
              + "/" + databaseName, userName, password);
    } catch (SQLException e) {
      System.out.println("Connection Failed");
      System.out.println(e.getMessage());
      System.exit(-1);
    }

    return connection;
  }

}
