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
package fr.mncc.gwttoolbox.appengine.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

import fr.mncc.gwttoolbox.primitives.shared.ObjectUtils;

public class SQuery2 implements IsSerializable, Serializable {

  private String kind_;
  private String ancestorKind_;
  private long ancestorId_;
  private ArrayList<SProjection2> projections_ = new ArrayList<SProjection2>();
  private ArrayList<SSort2> sorters_ = new ArrayList<SSort2>();
  private SClause2 clause_ = null;
  private boolean isKeysOnly_ = false;

  protected SQuery2() {
    this("", "", 0);
  }

  public SQuery2(String kind) {
    this(kind, "", 0);
  }

  public SQuery2(String kind, String ancestorKind, long ancestorId) {
    kind_ = kind;
    ancestorKind_ = ancestorKind;
    ancestorId_ = ancestorId;
  }

  public static SClause2 idEqual(long propertyValue) {
    return new SFilter2(SFilterOperator2.EQUAL, "__key__", propertyValue);
  }

  public static SClause2 idLessThan(long propertyValue) {
    return new SFilter2(SFilterOperator2.LESS_THAN, "__key__", propertyValue);
  }

  public static SClause2 idLessThanOrEqual(long propertyValue) {
    return new SFilter2(SFilterOperator2.LESS_THAN_OR_EQUAL, "__key__", propertyValue);
  }

  public static SClause2 idGreaterThan(long propertyValue) {
    return new SFilter2(SFilterOperator2.GREATER_THAN, "__key__", propertyValue);
  }

  public static SClause2 idGreaterThanOrEqual(long propertyValue) {
    return new SFilter2(SFilterOperator2.GREATER_THAN_OR_EQUAL, "__key__", propertyValue);
  }

  public static SClause2 idNotEqual(Object propertyValue) {
    return new SFilter2(SFilterOperator2.NOT_EQUAL, "__key__", propertyValue);
  }

  public static SClause2 idIn(ArrayList<Long> propertyValues) {
    return new SFilter2("__key__", propertyValues);
  }

  public static SClause2 equal(String propertyName, Object propertyValue) {
    return new SFilter2(SFilterOperator2.EQUAL, propertyName, propertyValue);
  }

  public static SClause2 lessThan(String propertyName, Object propertyValue) {
    return new SFilter2(SFilterOperator2.LESS_THAN, propertyName, propertyValue);
  }

  public static SClause2 lessThanOrEqual(String propertyName, Object propertyValue) {
    return new SFilter2(SFilterOperator2.LESS_THAN_OR_EQUAL, propertyName, propertyValue);
  }

  public static SClause2 greaterThan(String propertyName, Object propertyValue) {
    return new SFilter2(SFilterOperator2.GREATER_THAN, propertyName, propertyValue);
  }

  public static SClause2 greaterThanOrEqual(String propertyName, Object propertyValue) {
    return new SFilter2(SFilterOperator2.GREATER_THAN_OR_EQUAL, propertyName, propertyValue);
  }

  public static SClause2 notEqual(String propertyName, Object propertyValue) {
    return new SFilter2(SFilterOperator2.NOT_EQUAL, propertyName, propertyValue);
  }

  public static SClause2 in(String propertyName, ArrayList<Long> propertyValues) {
    return new SFilter2(propertyName, propertyValues);
  }

  public static SClause2 and(SClause2 clauseLeft, SClause2 clauseRight) {
    return new SClause2(true, clauseLeft, clauseRight);
  }

  public static SClause2 or(SClause2 clauseLeft, SClause2 clauseRight) {
    return new SClause2(false, clauseLeft, clauseRight);
  }

  /**
   * Transform a String query into a SQuery2 query
   * 
   * @param dataQuery
   * @return
   */
  final static public SQuery2 fromString2(String dataQuery) {

    SQuery2 squery = new SQuery2();

    // parameters
    int indexOpen = 0, indexClose = 0, start = 0;
    String state = "", parameters = "";

    if (dataQuery.indexOf("QUERY(") != 0) {
      System.out.println("This object is not a query");
    } else {
      // analyzing the code inside token Query( );
      start = 6;
      while (start != dataQuery.length() - 1) {
        indexOpen = dataQuery.indexOf("(", start);
        indexClose = dataQuery.indexOf(")", indexOpen);

        if (indexOpen == -1) {
          return null;
        }
        state = dataQuery.substring(start, indexOpen);

        if (!state.equals("WHERE") && !state.equals("SELECT")) {

          parameters = dataQuery.substring(indexOpen + 1, indexClose);
          squery.addParametersToQuery(state, parameters);
          start = indexClose + 1;

        } else if (state.equals("WHERE")) {

          indexClose = dataQuery.indexOf("ORDERBY") - 1;
          parameters = dataQuery.substring(indexOpen + 1, indexClose);
          squery.clause_ = addWhereToQuery(parameters);
          start = indexClose + 1;

        } else if (state.equals("SELECT")) {
          indexClose = dataQuery.indexOf("FROM") - 1;
          parameters = dataQuery.substring(indexOpen + 1, indexClose);
          squery.addSelectToQuery(parameters);
          start = indexClose + 1;
        }
      }
      return squery;
    }
    return null;
  }

  /**
   * Add clauses parameters to the query
   * 
   * @param parameters
   */
  private static final SClause2 addWhereToQuery(String parameters) {

    SClause2 clause = new SClause2();
    int nbParenthesis = 0, indexStart = 0, indexStop = 0, index = 0;
    String operator = "";
    if (!parameters.equals("")) {
      indexStart = parameters.indexOf("(", 0) + 1;
      operator = parameters.substring(0, indexStart - 1);

      // If our clause is a AND or an OR
      if (operator.equals("AND") || operator.equals("OR")) {
        int nbClause = 0;
        index = indexStart;

        if (operator.equals("AND")) {
          clause.isAnd_ = true;
        } else {
          clause.isAnd_ = false;
        }

        // Getting our 2 parameters and build new clause with it
        while (index < parameters.length() && nbClause < 2) {

          if (parameters.charAt(index) == '(') {
            nbParenthesis++;
          } else if (parameters.charAt(index) == ')') {
            nbParenthesis--;
          }

          if (nbParenthesis == 0 && parameters.charAt(index) == ','
              || index == parameters.length() - 1) {

            indexStop = index;
            if (nbClause == 0) {
              clause.clauseLeft_ = addWhereToQuery(parameters.substring(indexStart, indexStop));
              nbClause++;
            } else if (nbClause == 1) {
              clause.clauseRight_ = addWhereToQuery(parameters.substring(indexStart, indexStop));
              nbClause++;
            }

            indexStart = indexStop + 1;
          }
          index++;
        }
        indexStop = index;
        return clause;
      }
      // If it's not AND or OR, we build a filter
      else {
        if (!operator.equals("KEY_IN")) {
          return addFilter(parameters);
        } else {
          return addInFilter(parameters);
        }
      }
    }
    return null;
  }

  private static SFilter2 addInFilter(String parameters) {
    int indexStart = 0, indexStop = 0, index = 0;
    String propertyName = "", operator;
    ArrayList<Long> propertyValues = new ArrayList<Long>();
    Boolean openQuote = false, firstParam = true;

    indexStart = parameters.indexOf("(", 0) + 1;
    operator = parameters.substring(0, indexStart - 1);

    index = indexStart;

    while (index < parameters.length()) {
      if (parameters.charAt(index) == '"') {
        if (!openQuote) {
          openQuote = true;
          indexStart = index + 1;
        } else if (openQuote) {
          openQuote = false;
          indexStop = index;
        }
        // getting our propertyName then our propertyValue
        if (!openQuote && indexStop > indexStart) {
          if (firstParam) {
            propertyName = parameters.substring(indexStart, indexStop);
            firstParam = false;
          } else {
            propertyValues.add(Long.parseLong(parameters.substring(indexStart, indexStop)));
          }
        }
      }
      index++;
    }
    return new SFilter2(propertyName, propertyValues);
  }

  /**
   * Takes a string Filter (EQUAL("firstname","Alfred") ) and build an SFilter2 from it
   * 
   * @param parameters
   * @return
   */
  private static SFilter2 addFilter(String parameters) {

    int indexStart = 0, indexStop = 0, index = 0, nbParam = 0, intOperator = 0;
    String operator = "", propertyName = "", propertyValue = "";
    Boolean openQuote = false;

    indexStart = parameters.indexOf("(", 0) + 1;
    operator = parameters.substring(0, indexStart - 1);

    index = indexStart;
    // searching for our propertyName and our propertyValue
    while (index < parameters.length() && nbParam < 2) {
      if (parameters.charAt(index) == '"') {
        if (!openQuote) {
          openQuote = true;
          indexStart = index + 1;
        } else if (openQuote) {
          openQuote = false;
          indexStop = index;
        }
        // getting our propertyName then our propertyValue
        if (!openQuote && indexStop > indexStart) {
          if (nbParam == 0) {
            propertyName = parameters.substring(indexStart, indexStop);
            nbParam++;
          } else if (nbParam == 1) {
            propertyValue = parameters.substring(indexStart, indexStop);
            nbParam++;
          }
        }
      }
      index++;
    }
    // MANAGING DIFFERENT OPERATOR
    // EQUAL
    if (operator.equals("EQUAL")) {
      intOperator = 0;
      return new SFilter2(intOperator, propertyName, ObjectUtils.fromString(propertyValue));
    }

    // NOT EQUAL
    else if (operator.equals("NOT_EQUAL")) {
      intOperator = 5;
      return new SFilter2(intOperator, propertyName, ObjectUtils.fromString(propertyValue));
    }
    // GREATER THAN
    else if (operator.equals("GREATER_THAN")) {
      intOperator = 3;
      return new SFilter2(intOperator, propertyName, ObjectUtils.fromString(propertyValue));
    }
    // GREATER THAN OR EQUAL
    else if (operator.equals("GREATER_THAN_OR_EQUAL")) {
      intOperator = 4;
      return new SFilter2(intOperator, propertyName, ObjectUtils.fromString(propertyValue));
    }
    // LESS THAN
    else if (operator.equals("LESS_THAN")) {
      intOperator = 1;
      return new SFilter2(intOperator, propertyName, ObjectUtils.fromString(propertyValue));
    }
    // LESS THAN OR EQUAL
    else if (operator.equals("LESS_THAN_OR_EQUAL")) {
      intOperator = 2;
      return new SFilter2(intOperator, propertyName, ObjectUtils.fromString(propertyValue));
    }
    // never get here
    else {
      return null;
    }
  }

  public String getKind() {
    return kind_;
  }

  protected void setKind(String kind) {
    kind_ = kind;
  }

  public String getAncestorKind() {
    return ancestorKind_;
  }

  public long getAncestorId() {
    return ancestorId_;
  }

  public ArrayList<SProjection2> getProjections() {
    return projections_;
  }

  public ArrayList<SSort2> getSorters() {
    return sorters_;
  }

  public SClause2 getClause() {
    return clause_;
  }

  public boolean isKeysOnly() {
    return isKeysOnly_;
  }

  public SQuery2 setKeysOnly() {
    isKeysOnly_ = true;
    return this;
  }

  public SQuery2 removeKeysOnly() {
    isKeysOnly_ = false;
    return this;
  }

  public SQuery2 addStringProjection(String propertyName) {
    if (isValidProjection(propertyName))
      projections_.add(new SProjection2(propertyName, String.class));
    return this;
  }

  public SQuery2 addLongProjection(String propertyName) {
    if (isValidProjection(propertyName))
      projections_.add(new SProjection2(propertyName, Long.class));
    return this;
  }

  public SQuery2 addFloatProjection(String propertyName) {
    if (isValidProjection(propertyName))
      projections_.add(new SProjection2(propertyName, Float.class));
    return this;
  }

  public SQuery2 addBooleanProjection(String propertyName) {
    if (isValidProjection(propertyName))
      projections_.add(new SProjection2(propertyName, Boolean.class));
    return this;
  }

  public SQuery2 addDateProjection(String propertyName) {
    if (isValidProjection(propertyName))
      projections_.add(new SProjection2(propertyName, Date.class));
    return this;
  }

  public SQuery2 removeProjections() {
    projections_.clear();
    return this;
  }

  public SQuery2 addClause(SClause2 clause) {
    if (isValidClause(clause))
      clause_ = clause;
    return this;
  }

  public SQuery2 removeClause() {
    clause_ = null;
    return this;
  }

  public SQuery2 addIdAscendingSorter() {
    sorters_.add(new SSort2("__key__", true));
    return this;
  }

  public SQuery2 addIdDescendingSorter() {
    sorters_.add(new SSort2("__key__", false));
    return this;
  }

  public SQuery2 addAscendingSorter(String propertyName) {
    sorters_.add(new SSort2(propertyName, true));
    return this;
  }

  public SQuery2 addDescendingSorter(String propertyName) {
    sorters_.add(new SSort2(propertyName, false));
    return this;
  }

  public SQuery2 removeSorters() {
    sorters_.clear();
    return this;
  }

  private boolean isValidProjection(String propertyName) {
    if (propertyName == null || propertyName.isEmpty())
      return false;

    // The same property cannot be projected more than once
    if (projections_ != null) {
      for (SProjection2 projection : projections_) {
        if (projection.getPropertyName().equals(propertyName))
          return false;
      }
    }

    // Properties referenced in an equality (EQUAL) or membership (IN) filter cannot be projected
    if (clause_ != null)
      return isValidProjection(propertyName, clause_);
    return true;
  }

  private boolean isValidProjection(String propertyName, SClause2 clause) {
    if (!clause.isLeaf())
      return isValidProjection(propertyName, clause.getLeftClause())
          && isValidProjection(propertyName, clause.getRightClause());

    SFilter2 sfilter = (SFilter2) clause;
    if (sfilter.getOperator() != SFilterOperator2.IN
        && sfilter.getOperator() != SFilterOperator2.EQUAL)
      return true;
    return !sfilter.getPropertyName().equals(propertyName);
  }

  private boolean isValidClause(SClause2 clause) {
    if (clause == null)
      return false;

    // Properties referenced in an equality (EQUAL) or membership (IN) filter cannot be projected
    if (projections_ != null) {
      for (SProjection2 projection : projections_) {
        if (!isValidProjection(projection.getPropertyName(), clause))
          return false;
      }
    }

    // TODO :
    // - Inequality filters are limited to at most one property
    // - Properties used in inequality filters must be sorted first

    return clause.isLeaf() ? true : isValidClause(clause.getLeftClause())
        && isValidClause(clause.getRightClause());
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("kind_", kind_).add("ancestorKind_", ancestorKind_)
        .add("ancestorId_", ancestorId_).add("projections_", projections_)
        .add("sorters_", sorters_).add("clause_", clause_).add("isKeysOnly_", isKeysOnly_)
        .omitNullValues().toString();
  }

  private String toStringClause2(SClause2 clause) {
    if (clause == null) {
      return "";
    }

    if (clause instanceof SFilter2) {

      // EQUALS
      if (((SFilter2) clause).operator_ == SFilterOperator2.EQUAL) {
        return "EQUAL(\"" + ((SFilter2) clause).propertyName_ + "\",\""
            + ((SFilter2) clause).propertyValue_ + "\")";
      }

      // NOT EQUALS
      if (((SFilter2) clause).operator_ == SFilterOperator2.NOT_EQUAL) {
        return "NOT_EQUAL(\"" + ((SFilter2) clause).propertyName_ + "\",\""
            + ((SFilter2) clause).propertyValue_ + "\")";
      }
      // IN
      if (((SFilter2) clause).operator_ == SFilterOperator2.IN) {
        String list = "KEY_IN(\"";
        list += ((SFilter2) clause).propertyName_ + "\",\"";
        for (int i = 0; i < ((SFilter2) clause).propertyValues_.size(); i++) {
          if (i != ((SFilter2) clause).propertyValues_.size() - 1) {
            list += ((SFilter2) clause).propertyValues_.get(i) + "\",\"";
          } else {
            list += ((SFilter2) clause).propertyValues_.get(i);
          }
        }
        list += "\")";
        return list;
      }
      // GREATER THAN
      if (((SFilter2) clause).operator_ == SFilterOperator2.GREATER_THAN) {
        return "GREATER_THAN(\"" + ((SFilter2) clause).propertyName_ + "\",\""
            + ((SFilter2) clause).propertyValue_ + "\")";
      }

      // GREATER THAN OR EQUAL
      if (((SFilter2) clause).operator_ == SFilterOperator2.GREATER_THAN_OR_EQUAL) {
        return "GREATER_THAN_OR_EQUAL(\"" + ((SFilter2) clause).propertyName_ + "\",\""
            + ((SFilter2) clause).propertyValue_ + "\")";
      }

      // LESS THAN
      if (((SFilter2) clause).operator_ == SFilterOperator2.LESS_THAN) {
        return "LESS_THAN(\"" + ((SFilter2) clause).propertyName_ + "\",\""
            + ((SFilter2) clause).propertyValue_ + "\")";
      }
      // LESS THAN OR EQUAL
      if (((SFilter2) clause).operator_ == SFilterOperator2.LESS_THAN_OR_EQUAL) {
        return "LESS_THAN_OR_EQUAL(\"" + ((SFilter2) clause).propertyName_ + "\",\""
            + ((SFilter2) clause).propertyValue_ + "\")";
      }
    }

    if (clause.isAnd_)
      return "AND(" + toStringClause2(clause.clauseLeft_) + ","
          + toStringClause2(clause.clauseRight_) + ")";
    return "OR(" + toStringClause2(clause.clauseLeft_) + "," + toStringClause2(clause.clauseRight_)
        + ")";

  }

  /**
   * Return a String query
   * 
   * @return
   */
  final public String toString2() {

    String queryData = "";

    // TODO A changer si possible;
    String string = "";
    Date date = new Date();
    Long longClass = new Long(0);
    Float floatClass = new Float(0);
    Boolean bool = false;

    // SELECT(STRING_COLUMN("col"),BOOLEAN_COLUMN("bool"),...)
    queryData = "QUERY(SELECT(";
    if (projections_.size() != 0) {
      for (int projection = 0; projection < projections_.size(); projection++) {

        if (projections_.get(projection).getClazz().equals(string.getClass())) {
          queryData += "STRING_COLUMN(\"";
        } else if (projections_.get(projection).getClazz().equals(date.getClass())) {
          queryData += "DATE_COLUMN(\"";
        } else if (projections_.get(projection).getClazz().equals(longClass.getClass())) {
          queryData += "LONG_COLUMN(\"";
        } else if (projections_.get(projection).getClazz().equals(floatClass.getClass())) {
          queryData += "FLOAT_COLUMN(\"";
        } else if (projections_.get(projection).getClazz().equals(bool.getClass())) {
          queryData += "BOOLEAN_COLUMN(\"";
        }
        queryData += projections_.get(projection).getPropertyName();
        queryData += "\")";
        if (projection != projections_.size() - 1) {
          queryData += ",";
        }
      }
    }
    queryData += ")";

    // FROM(kind)
    queryData += "FROM(\"" + kind_ + "\")";

    // WHERE
    queryData += "WHERE(" + toStringClause2(clause_) + ")";

    // ORDERBY(column1,column2,...)
    queryData += "ORDERBY(\"";
    if (sorters_.size() != 0) {
      for (int sorters = 0; sorters < sorters_.size(); sorters++) {

        queryData += sorters_.get(sorters).getPropertyName();

        if (sorters != sorters_.size() - 1) {
          queryData += "\",\"";
        }
      }
    }
    queryData += "\")";

    // KEYSONLY(TRUE/FALSE)
    queryData += "KEYSONLY(\"" + isKeysOnly_ + "\"))";

    return queryData;
  }

  /**
   * Takes a string SELECT and add projections to the query
   * 
   * @param parameters
   */
  private void addSelectToQuery(String parameters) {
    // TODO Auto-generated method stub
    int indexStart = 0, indexStop = 0, start = 0;
    String propertyColumn = "", operator = "";

    if (parameters.length() != 2) {
      while (start != parameters.length()) {
        if (start != 0) {
          start++;
        }

        indexStart = parameters.indexOf("(", start) + 1;
        indexStop = parameters.indexOf(")", start);

        operator = parameters.substring(start, indexStart - 1);
        propertyColumn = parameters.substring(indexStart + 1, indexStop - 1);

        start = indexStop + 1;

        if (operator.equals("STRING_COLUMN")) {
          this.addStringProjection(propertyColumn);
        } else if (operator.equals("BOOLEAN_COLUMN")) {
          this.addBooleanProjection(propertyColumn);
        } else if (operator.equals("LONG_COLUMN")) {
          this.addLongProjection(propertyColumn);
        } else if (operator.equals("FLOAT_COLUMN")) {
          this.addFloatProjection(propertyColumn);
        } else if (operator.equals("DATE_COLUMN")) {
          this.addDateProjection(propertyColumn);
        }
      }
    }
  }

  /**
   * Add parameters(except clauses and select) to the query and return it
   * 
   * @param state
   * @param parameters
   * @param squery
   * @return
   */
  private void addParametersToQuery(String state, String parameters) {

    int indexStart = 0, indexStop = 0, start = 0;
    String singleParameter = "";

    if (parameters.length() != 2 && state.length() != 2) {
      while (start != parameters.length()) {

        indexStart = parameters.indexOf("\"", start) + 1;
        indexStop = parameters.indexOf("\"", indexStart + 1);

        singleParameter = parameters.substring(indexStart, indexStop);

        start = indexStop + 1;

        if (state.equals("FROM")) {
          this.setKind(singleParameter);
        } else if (state.equals("ORDERBY")) {
          this.addAscendingSorter(singleParameter);
        } else if (state.equals("KEYSONLY")) {
          if (singleParameter.equals(true))
            this.setKeysOnly();
        }
      }
    }
  }

  public static class SFilterOperator2 implements IsSerializable, Serializable {
    public static final int EQUAL = 0;
    public static final int LESS_THAN = 1;
    public static final int LESS_THAN_OR_EQUAL = 2;
    public static final int GREATER_THAN = 3;
    public static final int GREATER_THAN_OR_EQUAL = 4;
    public static final int NOT_EQUAL = 5;
    public static final int IN = 6;

    protected SFilterOperator2() {

    }
  }

  public static class SSort2 implements IsSerializable, Serializable {

    private String propertyName_ = "";
    private boolean isAscending_ = true;

    protected SSort2() {

    }

    public SSort2(String propertyName, boolean isAscending) {
      propertyName_ = propertyName;
      isAscending_ = isAscending;
    }

    public String getPropertyName() {
      return propertyName_;
    }

    public boolean isAscending() {
      return isAscending_;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this).add("propertyName_", propertyName_).add("isAscending_",
          isAscending_).omitNullValues().toString();
    }
  }

  public static class SFilter2 extends SClause2 implements IsSerializable, Serializable {

    private int operator_;
    private String propertyName_ = "";
    private String propertyValue_ = ""; // EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN,
                                        // GREATER_THAN_OR_EQUAL, NOT_EQUAL
    private ArrayList<Long> propertyValues_ = new ArrayList<Long>(); // IN

    protected SFilter2() {

    }

    public SFilter2(int operator, String propertyName, Object propertyValue) {
      if (operator != SFilterOperator2.IN) {
        operator_ = operator;
        propertyName_ = propertyName;
        propertyValue_ = ObjectUtils.toString(propertyValue);
      }
    }

    public SFilter2(String propertyName, ArrayList<Long> propertyValues) {
      operator_ = SFilterOperator2.IN;
      propertyName_ = propertyName;
      propertyValues_ = propertyValues;
    }

    public int getOperator() {
      return operator_;
    }

    public String getPropertyName() {
      return propertyName_;
    }

    public Object getPropertyValue() {
      return ObjectUtils.fromString(propertyValue_);
    }

    public ArrayList<Long> getPropertyValues() {
      return propertyValues_;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this).add("operator_", operator_).add("propertyName_",
          propertyName_).add("propertyValue_", propertyValue_).add("propertyValues_",
          propertyValues_).omitNullValues().toString();
    }
  }

  public static class SClause2 implements IsSerializable, Serializable {

    private boolean isAnd_ = true;
    private SClause2 clauseLeft_ = null;
    private SClause2 clauseRight_ = null;

    protected SClause2() {

    }

    public SClause2(boolean isAnd, SClause2 clauseLeft, SClause2 clauseRight) {
      isAnd_ = isAnd;
      clauseLeft_ = clauseLeft;
      clauseRight_ = clauseRight;
    }

    public boolean isAnd() {
      return isAnd_;
    }

    public boolean isOr() {
      return !isAnd();
    }

    public SClause2 getLeftClause() {
      return clauseLeft_;
    }

    public SClause2 getRightClause() {
      return clauseRight_;
    }

    public boolean isNode() {
      return !isLeaf();
    }

    public boolean isLeaf() {
      return clauseLeft_ == null && clauseRight_ == null;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this).add("isAnd_", isAnd_).add("clauseLeft_", clauseLeft_)
          .add("clauseRight_", clauseRight_).omitNullValues().toString();
    }
  }

  public static class SProjection2 implements IsSerializable, Serializable {

    private String propertyName_ = "";
    private Class<?> clazz_ = null;

    protected SProjection2() {

    }

    public SProjection2(String propertyName, Class<?> clazz) {
      propertyName_ = propertyName;
      clazz_ = clazz;
    }

    public String getPropertyName() {
      return propertyName_;
    }

    public Class<?> getClazz() {
      return clazz_;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this).add("propertyName_", propertyName_).add("clazz_", clazz_)
          .omitNullValues().toString();
    }
  }
}
