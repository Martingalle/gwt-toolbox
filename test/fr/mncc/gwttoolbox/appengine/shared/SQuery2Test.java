package fr.mncc.gwttoolbox.appengine.shared;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import fr.mncc.gwttoolbox.appengine.shared.Clause2;
import fr.mncc.gwttoolbox.appengine.shared.Filter2;
import fr.mncc.gwttoolbox.appengine.shared.SQuery2;

public class SQuery2Test {

  @Test
  public void testSQuery1() {
    String queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, " + "projections_=[], "
            + "sorters_=[], isKeysOnly_=true}";

    SQuery2 squery = new SQuery2("person");
    squery.setKeysOnly();

    assertEquals(queryString, squery.toString());

    squery = new SQuery2("friend", "person", 3);
    squery.removeKeysOnly();
    queryString =
        "SQuery2{kind_=friend, ancestorKind_=person, ancestorId_=3, " + "projections_=[], "
            + "sorters_=[], isKeysOnly_=false}";

    assertEquals(queryString, squery.toString());
  }

  @Test
  public void testSQuery2() {
    String queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, "
            + "projections_=[Projection2{propertyName_=act, clazz_=class java.lang.String}], "
            + "sorters_=[Sort2{propertyName_=age, isAscending_=true}], " + "isKeysOnly_=false}";

    SQuery2 squery = new SQuery2("person");

    squery.addStringProjection("act");
    squery.addAscendingSorter("age");
    assertEquals(queryString, squery.toString());
  }

  @Test
  public void testSQuery3() {
    String queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, "
            + "projections_=[Projection2{propertyName_=act, clazz_=class java.lang.String}, "
            + "Projection2{propertyName_=status, clazz_=class java.lang.Boolean}], "
            + "sorters_=[Sort2{propertyName_=age, isAscending_=true}, "
            + "Sort2{propertyName_=id, isAscending_=false}], isKeysOnly_=false}";

    SQuery2 squery = new SQuery2("person");

    squery.addStringProjection("act");
    squery.addBooleanProjection("status");

    squery.addAscendingSorter("age");
    squery.addDescendingSorter("id");

    assertEquals(queryString, squery.toString());
  }

  @Test
  public void testSQuery4() {
    String queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, "
            + "projections_=[Projection2{propertyName_=act, clazz_=class java.lang.String}, "
            + "Projection2{propertyName_=status, clazz_=class java.lang.Boolean}], "
            + "sorters_=[Sort2{propertyName_=age, isAscending_=true}, "
            + "Sort2{propertyName_=__key__, isAscending_=true}], isKeysOnly_=false}";

    SQuery2 squery = new SQuery2("person");

    squery.addStringProjection("act");
    squery.addBooleanProjection("status");

    squery.addAscendingSorter("age");
    squery.addIdAscendingSorter();

    assertEquals(queryString, squery.toString());

    queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, " + "projections_=[], "
            + "sorters_=[Sort2{propertyName_=age, isAscending_=true}, "
            + "Sort2{propertyName_=__key__, isAscending_=true}], isKeysOnly_=false}";

    // remove projections
    squery.removeProjections();
    assertEquals(queryString, squery.toString());

    queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, "
            + "projections_=[], sorters_=[], isKeysOnly_=false}";

    // remove sorters
    squery.removeSorters();
    assertEquals(queryString, squery.toString());
  }

  @Test
  public void testSQuery5() {

    String queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, projections_=[], sorters_=[], "
            + "clause_=Clause2{isAnd_=false, "
            + "clauseLeft_=Clause2{isAnd_=true, clauseLeft_=Filter2{operator_=0, propertyName_=name, propertyValue_=1:nedved, propertyValues_=[]}, "
            + "clauseRight_=Filter2{operator_=4, propertyName_=age, propertyValue_=2:20, propertyValues_=[]}}, "
            + "clauseRight_=Filter2{operator_=2, propertyName_=salary, propertyValue_=3:20000.0, propertyValues_=[]}}, "
            + "isKeysOnly_=false}";

    Filter2 clauseLeft2 = Filter2.equal("name", "nedved");
    Filter2 clauseRight2 = Filter2.greaterThanOrEqual("age", 20);

    Clause2 clauseLeft = Clause2.and(clauseLeft2, clauseRight2);
    Filter2 clauseRight = Filter2.lessThanOrEqual("salary", 20000F);

    Clause2 clause = Clause2.or(clauseLeft, clauseRight);
    SQuery2 squery = new SQuery2("person");
    squery.addClause(clause);
    assertEquals(queryString, squery.toString());

    queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, "
            + "projections_=[], sorters_=[], isKeysOnly_=false}";

    // remove clause
    squery.removeClause();
    assertEquals(queryString, squery.toString());
  }

  @Test
  public void testSQuery6() {
    String queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, projections_=[], sorters_=[], "
            + "clause_=Clause2{isAnd_=false, "
            + "clauseLeft_=Clause2{isAnd_=true, clauseLeft_=Filter2{operator_=0, propertyName_=name, propertyValue_=1:nedved, propertyValues_=[]}, "
            + "clauseRight_=Filter2{operator_=4, propertyName_=age, propertyValue_=2:20, propertyValues_=[]}}, "
            + "clauseRight_=Filter2{operator_=2, propertyName_=salary, propertyValue_=3:20000.0, propertyValues_=[]}}, "
            + "isKeysOnly_=false}";

    Filter2 clauseLeft2 = Filter2.equal("name", "nedved");
    Filter2 clauseRight2 = Filter2.greaterThanOrEqual("age", 20);

    Clause2 clauseLeft = Clause2.and(clauseLeft2, clauseRight2);
    Filter2 clauseRight = Filter2.lessThanOrEqual("salary", 20000F);

    Clause2 clause = Clause2.or(clauseLeft, clauseRight);
    SQuery2 squery = new SQuery2("person");
    squery.addClause(clause);
    assertEquals(queryString, squery.toString());

    ArrayList<Long> ar = new ArrayList<Long>();

    for (int i = 0; i < 10; i++)
      ar.add((long) i);

    Filter2 test = Filter2.idIn(ar);

    clause = Clause2.and(clause, test);

    queryString =
        "SQuery2{kind_=person, ancestorKind_=, ancestorId_=0, projections_=[], sorters_=[], "
            + "clause_=Clause2{isAnd_=true, "
            + "clauseLeft_=Clause2{isAnd_=false, "
            + "clauseLeft_=Clause2{isAnd_=true, clauseLeft_=Filter2{operator_=0, propertyName_=name, propertyValue_=1:nedved, propertyValues_=[]}, clauseRight_=Filter2{operator_=4, propertyName_=age, propertyValue_=2:20, propertyValues_=[]}}, "
            + "clauseRight_=Filter2{operator_=2, propertyName_=salary, propertyValue_=3:20000.0, propertyValues_=[]}}, "
            + "clauseRight_=Filter2{operator_=6, propertyName_=__key__, propertyValue_=, propertyValues_=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]}}, "
            + "isKeysOnly_=false}";

    // remove clause
    squery.removeClause();
    squery.addClause(clause);

    assertEquals(queryString, squery.toString());
  }
}
