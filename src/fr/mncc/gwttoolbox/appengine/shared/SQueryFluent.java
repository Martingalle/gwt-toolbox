package fr.mncc.gwttoolbox.appengine.shared;

import java.util.Date;

public class SQueryFluent {

  private SQuery2 squery_ = new SQuery2();

  SQueryFluent() {

  }

  public SQueryFluent stringColumn(String... columns) {
    for (int singleColumn = 0; singleColumn < columns.length; singleColumn++)
      squery_.addStringProjection(columns[singleColumn]);
    return this;
  }

  public SQueryFluent longColumn(String... columns) {
    for (int singleColumn = 0; singleColumn < columns.length; singleColumn++)
      squery_.addLongProjection(columns[singleColumn]);
    return this;
  }

  public SQueryFluent dateColumn(String... columns) {
    for (int singleColumn = 0; singleColumn < columns.length; singleColumn++)
      squery_.addDateProjection(columns[singleColumn]);
    return this;
  }

  public SQueryFluent floatColumn(String... columns) {
    for (int singleColumn = 0; singleColumn < columns.length; singleColumn++)
      squery_.addFloatProjection(columns[singleColumn]);
    return this;
  }
  
  public SQueryFluent booleanColumn(String... columns) {
    for (int singleColumn = 0; singleColumn < columns.length; singleColumn++)
      squery_.addBooleanProjection(columns[singleColumn]);
    return this;
  }
  
  public SQueryFluent fromTable(String table)
  {
     squery_.setKind(table);
     return this;
  }
  
  public SQueryFluent where(String...clause)
  {
    return null;
    
  }
}
