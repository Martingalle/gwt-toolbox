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

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class SQuery2 implements IsSerializable, Serializable {

  private String kind_;
  private String ancestorKind_;
  private int ancestorId_;
  private ArrayList<Projection2> projections_ = new ArrayList<Projection2>();
  private ArrayList<Sort2> sorters_ = new ArrayList<Sort2>();
  private Clause2 clause_ = null;
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
    ancestorId_ = (int) ancestorId;
  }

  @Deprecated
  public static Filter2 idEqual(long propertyValue) {
    return Filter2.idEqual(propertyValue);
  }

  @Deprecated
  public static Filter2 idLessThan(long propertyValue) {
    return Filter2.idLessThan(propertyValue);
  }

  @Deprecated
  public static Filter2 idLessThanOrEqual(long propertyValue) {
    return Filter2.idLessThanOrEqual(propertyValue);
  }

  @Deprecated
  public static Filter2 idGreaterThan(long propertyValue) {
    return Filter2.idGreaterThan(propertyValue);
  }

  @Deprecated
  public static Filter2 idGreaterThanOrEqual(long propertyValue) {
    return Filter2.idGreaterThanOrEqual(propertyValue);
  }

  @Deprecated
  public static Filter2 idNotEqual(Object propertyValue) {
    return Filter2.idNotEqual(propertyValue);
  }

  @Deprecated
  public static Filter2 idIn(ArrayList<Long> propertyValues) {
    return Filter2.idIn(propertyValues);
  }

  @Deprecated
  public static Filter2 equal(String propertyName, Object propertyValue) {
    return Filter2.equal(propertyName, propertyValue);
  }

  @Deprecated
  public static Filter2 lessThan(String propertyName, Object propertyValue) {
    return Filter2.lessThan(propertyName, propertyValue);
  }

  @Deprecated
  public static Filter2 lessThanOrEqual(String propertyName, Object propertyValue) {
    return Filter2.lessThanOrEqual(propertyName, propertyValue);
  }

  @Deprecated
  public static Filter2 greaterThan(String propertyName, Object propertyValue) {
    return Filter2.greaterThan(propertyName, propertyValue);
  }

  @Deprecated
  public static Filter2 greaterThanOrEqual(String propertyName, Object propertyValue) {
    return Filter2.greaterThanOrEqual(propertyName, propertyValue);
  }

  @Deprecated
  public static Filter2 notEqual(String propertyName, Object propertyValue) {
    return Filter2.notEqual(propertyName, propertyValue);
  }

  @Deprecated
  public static Filter2 in(String propertyName, ArrayList<Long> propertyValues) {
    return Filter2.in(propertyName, propertyValues);
  }

  @Deprecated
  public static Clause2 and(Clause2 clauseLeft, Clause2 clauseRight) {
    return Clause2.and(clauseLeft, clauseRight);
  }

  @Deprecated
  public static Clause2 or(Clause2 clauseLeft, Clause2 clauseRight) {
    return Clause2.or(clauseLeft, clauseRight);
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
    return (ArrayList<SProjection2>) (ArrayList<?>) projections_;
  }

  public ArrayList<SSort2> getSorters() {
    return (ArrayList<SSort2>) (ArrayList<?>) sorters_;
  }

  public Clause2 getClause() {
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
      projections_.add(Projection2.of(propertyName, String.class));
    return this;
  }

  public SQuery2 addLongProjection(String propertyName) {
    if (isValidProjection(propertyName))
      projections_.add(Projection2.of(propertyName, Long.class));
    return this;
  }

  public SQuery2 addFloatProjection(String propertyName) {
    if (isValidProjection(propertyName))
      projections_.add(Projection2.of(propertyName, Float.class));
    return this;
  }

  public SQuery2 addBooleanProjection(String propertyName) {
    if (isValidProjection(propertyName))
      projections_.add(Projection2.of(propertyName, Boolean.class));
    return this;
  }

  public SQuery2 addDateProjection(String propertyName) {
    if (isValidProjection(propertyName))
      projections_.add(Projection2.of(propertyName, Date.class));
    return this;
  }

  public SQuery2 removeProjections() {
    projections_.clear();
    return this;
  }

  public SQuery2 addClause(Clause2 clause) {
    if (isValidClause(clause))
      clause_ = clause;
    return this;
  }

  public SQuery2 removeClause() {
    clause_ = null;
    return this;
  }

  public SQuery2 addIdAscendingSorter() {
    sorters_.add(Sort2.ascending("__key__"));
    return this;
  }

  public SQuery2 addIdDescendingSorter() {
    sorters_.add(Sort2.descending("__key__"));
    return this;
  }

  public SQuery2 addAscendingSorter(String propertyName) {
    sorters_.add(Sort2.ascending(propertyName));
    return this;
  }

  public SQuery2 addDescendingSorter(String propertyName) {
    sorters_.add(Sort2.descending(propertyName));
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
      for (Projection2 projection : projections_) {
        if (projection.getPropertyName().equals(propertyName))
          return false;
      }
    }

    // Properties referenced in an equality (EQUAL) or membership (IN) filter cannot be projected
    if (clause_ != null)
      return isValidProjection(propertyName, clause_);
    return true;
  }

  private boolean isValidProjection(String propertyName, Clause2 clause) {
    if (!clause.isLeaf())
      return isValidProjection(propertyName, clause.getLeftClause())
          && isValidProjection(propertyName, clause.getRightClause());

    Filter2 filter = (Filter2) clause;
    if (filter.getOperator() != FilterOperator2.IN && filter.getOperator() != FilterOperator2.EQUAL)
      return true;
    return !filter.getPropertyName().equals(propertyName);
  }

  private boolean isValidClause(Clause2 clause) {
    if (clause == null)
      return false;

    // Properties referenced in an equality (EQUAL) or membership (IN) filter cannot be projected
    if (projections_ != null) {
      for (Projection2 projection : projections_) {
        if (!isValidProjection(projection.getPropertyName(), clause))
          return false;
      }
    }

    // TODO :
    // - Inequality filters are limited to at most one property
    // - Properties used in inequality filters must be sorted first

    return clause.isLeaf()
        || (isValidClause(clause.getLeftClause()) && isValidClause(clause.getRightClause()));
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("kind_", kind_).add("ancestorKind_", ancestorKind_)
        .add("ancestorId_", ancestorId_).add("projections_", projections_)
        .add("sorters_", sorters_).add("clause_", clause_).add("isKeysOnly_", isKeysOnly_)
        .omitNullValues().toString();
  }

  @Deprecated
  public static class SFilterOperator2 extends FilterOperator2 {
  }

  @Deprecated
  public static class SSort2 extends Sort2 {
  }

  @Deprecated
  public static class SFilter2 extends Filter2 {
  }

  @Deprecated
  public static class SClause2 extends Clause2 {
  }

  @Deprecated
  public static class SProjection2 extends Projection2 {
  }
}
