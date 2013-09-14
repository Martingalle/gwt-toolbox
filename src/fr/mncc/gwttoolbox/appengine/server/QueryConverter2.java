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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.mncc.gwttoolbox.appengine.shared.*;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;

public class QueryConverter2 {

  public static String getAsPostgreSqlQuery(SQuery2 squery) {

    // Add projections
    String query = "SELECT " + getProjectionQuery(squery) + " FROM " + squery.getKind() + " WHERE ";

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

  public static Query getAsAppEngineQuery(SQuery2 squery) {
    Query query =
        hasAncestor(squery) ? new Query(squery.getKind(), KeyFactory.createKey(squery
            .getAncestorKind(), squery.getAncestorId())) : new Query(squery.getKind());

    // Set keys only
    if (squery.isKeysOnly())
      query.setKeysOnly();

    // Add projections
    for (Projection2 projection : squery.getProjections())
      query.addProjection(new PropertyProjection(projection.getPropertyName(), projection
          .getClazz()));

    // Add sort order
    for (Sort2 sorter : squery.getSorters())
      query.addSort(sorter.getPropertyName(), sorter.isAscending() ? Query.SortDirection.ASCENDING
          : Query.SortDirection.DESCENDING);

    // Add clause
    if (squery.getClause() != null)
      query.setFilter(buildClause(squery, squery.getClause()));
    return query;
  }

  private static Query.Filter buildClause(SQuery2 squery, Clause2 clause) {
    if (clause.isLeaf()) {
      Filter2 sfilter = (Filter2) clause;
      if (sfilter.getOperator() != FilterOperator2.IN) {
        if (sfilter.getPropertyName().equals("__key__")
            && sfilter.getPropertyValue() instanceof Long) {
          if (hasAncestor(squery))
            return new Query.FilterPredicate(sfilter.getPropertyName(), getAsFilterOperator(sfilter
                .getOperator()), KeyFactory.createKey(KeyFactory.createKey(
                squery.getAncestorKind(), squery.getAncestorId()), squery.getKind(), (Long) sfilter
                .getPropertyValue()));
          return new Query.FilterPredicate(sfilter.getPropertyName(), getAsFilterOperator(sfilter
              .getOperator()), KeyFactory.createKey(squery.getKind(), (Long) sfilter
              .getPropertyValue()));
        }
        if (sfilter.getPropertyValue() instanceof Timestamp)
          return new Query.FilterPredicate(sfilter.getPropertyName(), getAsFilterOperator(sfilter
              .getOperator()), new Date(((Timestamp) sfilter.getPropertyValue()).getTime()));
        if (sfilter.getPropertyValue() instanceof Time)
          return new Query.FilterPredicate(sfilter.getPropertyName(), getAsFilterOperator(sfilter
              .getOperator()), new Date(((Time) sfilter.getPropertyValue()).getTime()));
        return new Query.FilterPredicate(sfilter.getPropertyName(), getAsFilterOperator(sfilter
            .getOperator()), sfilter.getPropertyValue());
      }

      List<Key> keys = new ArrayList<Key>();
      for (Long id : sfilter.getPropertyValues()) {
        if (hasAncestor(squery))
          keys.add(KeyFactory.createKey(KeyFactory.createKey(squery.getAncestorKind(), squery
              .getAncestorId()), squery.getKind(), id));
        else
          keys.add(KeyFactory.createKey(squery.getKind(), id));
      }
      return new Query.FilterPredicate("__key__", Query.FilterOperator.IN, keys);
    }
    if (clause.isAnd())
      return Query.CompositeFilterOperator.and(buildClause(squery, clause.getLeftClause()),
          buildClause(squery, clause.getRightClause()));
    return Query.CompositeFilterOperator.or(buildClause(squery, clause.getLeftClause()),
        buildClause(squery, clause.getRightClause()));
  }

  private static Query.FilterOperator getAsFilterOperator(int operator) {
    if (operator == FilterOperator2.EQUAL)
      return Query.FilterOperator.EQUAL;
    else if (operator == FilterOperator2.LESS_THAN)
      return Query.FilterOperator.LESS_THAN;
    else if (operator == FilterOperator2.LESS_THAN_OR_EQUAL)
      return Query.FilterOperator.LESS_THAN_OR_EQUAL;
    else if (operator == FilterOperator2.GREATER_THAN)
      return Query.FilterOperator.GREATER_THAN;
    else if (operator == FilterOperator2.GREATER_THAN_OR_EQUAL)
      return Query.FilterOperator.GREATER_THAN_OR_EQUAL;
    else if (operator == FilterOperator2.NOT_EQUAL)
      return Query.FilterOperator.NOT_EQUAL;
    else if (operator == FilterOperator2.IN)
      return Query.FilterOperator.IN;
    return null; // This case should never happen
  }

  private static boolean hasAncestor(SQuery2 squery) {
    return squery.getAncestorKind() != null && !squery.getAncestorKind().isEmpty()
        && squery.getAncestorId() > 0;
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
      sortQuery += sorter.getPropertyName() + " ";
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
          + " " + PostgreSql2.preparedQuery(sfilter.getPropertyValue()));
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
