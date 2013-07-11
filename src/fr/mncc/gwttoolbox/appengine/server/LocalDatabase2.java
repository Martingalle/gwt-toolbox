package fr.mncc.gwttoolbox.appengine.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;

import fr.mncc.gwttoolbox.appengine.shared.Clause2;
import fr.mncc.gwttoolbox.appengine.shared.Filter2;
import fr.mncc.gwttoolbox.appengine.shared.FilterOperator2;
import fr.mncc.gwttoolbox.appengine.shared.SQuery2;
import fr.mncc.gwttoolbox.primitives.shared.Entity;
import fr.mncc.gwttoolbox.primitives.shared.ObjectUtils;

public class LocalDatabase2 {

  public final static Logger logger_ = Logger.getLogger(LocalDatabase2.class.getCanonicalName());

  private static List<fr.mncc.gwttoolbox.primitives.shared.Entity> entitiesDatabase_ =
      new ArrayList<fr.mncc.gwttoolbox.primitives.shared.Entity>();

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
      return put(entity, ancestorKind, ancestorId).get();
    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return 0L;
  }

  public static Future<Long> put(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
    return put(entity, null, 0);
  }

  public static Future<Long> put(final fr.mncc.gwttoolbox.primitives.shared.Entity entity,
      String ancestorKind, long ancestorId) {
    final Future<Long> id = new Future<Long>() {

      @Override
      public boolean isDone() {
        return false;
      }

      @Override
      public boolean isCancelled() {
        return false;
      }

      @Override
      public Long get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
          TimeoutException {
        return null;
      }

      @Override
      public Long get() throws InterruptedException, ExecutionException {
        long id = entity.getId();
        entitiesDatabase_.add(entity);

        return id;
      }

      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
      }
    };

    return id;
  }

  public static List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities) {
    return putSync(entities, null, 0);
  }

  public static List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities,
      String ancestorKind, long ancestorId) {
    try {
      return put(entities).get();
    } catch (InterruptedException e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    } catch (ExecutionException e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return null;
  }

  public static Future<List<Long>> put(
      Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities) {
    return put(entities, null, 0);

  }

  public static Future<List<Long>> put(
      final Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities,
      final String ancestorKind, final long ancestorId) {
    Future<List<Long>> ids = new Future<List<Long>>() {

      @Override
      public boolean isDone() {
        return false;
      }

      @Override
      public boolean isCancelled() {
        return false;
      }

      @Override
      public List<Long> get(long timeout, TimeUnit unit) throws InterruptedException,
          ExecutionException, TimeoutException {
        return null;
      }

      @Override
      public List<Long> get() throws InterruptedException, ExecutionException {
        List<Long> keys = new ArrayList<Long>();

        for (fr.mncc.gwttoolbox.primitives.shared.Entity entity : entities) {
          Long id = put(entity, ancestorKind, ancestorId).get();
          keys.add(id);
        }

        return keys;
      }

      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
      }
    };

    return ids;
  }

  public static fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id) {
    return getSync(kind, id, null, 0);
  }

  public static fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id,
      String ancestorKind, long ancestorId) {

    try {
      return get(kind, id, ancestorKind, ancestorId).get();
    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }
    return null;
  }

  public static Future<fr.mncc.gwttoolbox.primitives.shared.Entity> get(String kind, long id) {
    return get(kind, id, null, 0);
  }

  public static Future<fr.mncc.gwttoolbox.primitives.shared.Entity> get(final String kind,
      final long id, String ancestorKind, long ancestorId) {

    Future<fr.mncc.gwttoolbox.primitives.shared.Entity> idem = new Future<Entity>() {

      @Override
      public boolean isDone() {
        return false;
      }

      @Override
      public boolean isCancelled() {
        return false;
      }

      @Override
      public Entity get(long timeout, TimeUnit unit) throws InterruptedException,
          ExecutionException, TimeoutException {
        return null;
      }

      @Override
      public Entity get() throws InterruptedException, ExecutionException {
        int listId = getArrayIndexFromEntityId(id);
        if (listId != -1)
          return entitiesDatabase_.get(listId);
        return null;
      }

      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
      }
    };

    return idem;
  }

  public static Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids) {
    return getSync(kind, ids, null, 0);
  }

  public static Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids, String ancestorKind, long ancestorId) {
    try {
      return get(kind, ids, ancestorKind, ancestorId).get();
    } catch (InterruptedException e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    } catch (ExecutionException e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return null;
  }

  public static Future<Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity>> get(String kind,
      Iterable<Long> ids) {
    return get(kind, ids, null, 0);
  }

  public static Future<Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity>> get(
      final String kind, final Iterable<Long> ids, final String ancestorKind, final long ancestorId) {
    Future<Map<Long, Entity>> mapOfEntities = new Future<Map<Long, Entity>>() {

      @Override
      public boolean isDone() {
        return false;
      }

      @Override
      public boolean isCancelled() {
        return false;
      }

      @Override
      public Map<Long, Entity> get(long timeout, TimeUnit unit) throws InterruptedException,
          ExecutionException, TimeoutException {
        return null;
      }

      @Override
      public Map<Long, Entity> get() throws InterruptedException, ExecutionException {
        Map<Long, Entity> map = new HashMap<Long, Entity>();

        for (Long id : ids) {
          Entity entity = LocalDatabase2.get(kind, id, ancestorKind, ancestorId).get();
          map.put(id, entity);
        }

        return map;
      }

      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
      }
    };
    return mapOfEntities;
  }

  public static boolean deleteSync(String kind, long id) {
    return deleteSync(kind, id, null, 0);
  }

  public static boolean deleteSync(String kind, long id, String ancestorKind, long ancestorId) {
    return deleteEntity(kind, id);
  }

  public static Future<Void> delete(String kind, long id) {
    return delete(kind, id, null, 0);
  }

  public static Future<Void> delete(final String kind, final long id, String ancestorKind,
      long ancestorId) {

    Future<Void> nil = new Future<Void>() {

      @Override
      public boolean isDone() {
        return false;
      }

      @Override
      public boolean isCancelled() {
        return false;
      }

      @Override
      public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
          TimeoutException {
        return null;
      }

      @Override
      public Void get() throws InterruptedException, ExecutionException {
        deleteEntity(kind, id);

        return null;
      }

      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
      }
    };
    return nil;
  }

  public static boolean deleteSync(String kind, Iterable<Long> ids) {
    return deleteSync(kind, ids, null, 0);
  }

  public static boolean deleteSync(String kind, Iterable<Long> ids, String ancestorKind,
      long ancestorId) {

    try {
      delete(kind, ids, ancestorKind, ancestorId).get();
      return true;
    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.getMessage(), e);
    }

    return false;
  }

  public static Future<Void> delete(String kind, Iterable<Long> ids) {
    return delete(kind, ids, null, 0);
  }

  public static Future<Void> delete(final String kind, final Iterable<Long> ids,
      final String ancestorKind, final long ancestorId) {

    Future<Void> nil = new Future<Void>() {

      @Override
      public boolean isDone() {
        return false;
      }

      @Override
      public boolean isCancelled() {
        return false;
      }

      @Override
      public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
          TimeoutException {
        return null;
      }

      @Override
      public Void get() throws InterruptedException, ExecutionException {
        for (Long id : ids) {
          deleteEntity(kind, id);
        }
        return null;
      }

      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
      }
    };
    return nil;
  }

  public static long listSize(SQuery2 toolboxQuery) {
    Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicate = null;
    predicate = buildPredicates(toolboxQuery.getClause(), predicate);

    FluentIterable<Entity> fluentIterable =
        FluentIterable.from(entitiesDatabase_).filter(predicate);

    return fluentIterable.size();
  }

  public static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery) {
    Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicate = null;
    predicate = buildPredicates(toolboxQuery.getClause(), predicate);

    FluentIterable<Entity> fluentIterable =
        FluentIterable.from(entitiesDatabase_).filter(predicate);

    if (toolboxQuery.isKeysOnly()) {
      return removeIdFromEntityProperties(fluentIterable, toolboxQuery);
    }

    return fluentIterable;
  }

  public static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery,
      int startIndex, int amount) {
    Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicate = null;
    predicate = buildPredicates(toolboxQuery.getClause(), predicate);

    FluentIterable<fr.mncc.gwttoolbox.primitives.shared.Entity> fluentIterable =
        FluentIterable.from(entitiesDatabase_).filter(predicate);

    List<fr.mncc.gwttoolbox.primitives.shared.Entity> list =
        new ArrayList<fr.mncc.gwttoolbox.primitives.shared.Entity>();

    if (toolboxQuery.isKeysOnly()) {
      return removeIdFromEntityProperties(fluentIterable, toolboxQuery);
    }
    fluentIterable.copyInto(list);

    return subList(list, amount, startIndex);
  }

  public static Iterable<Long> listIds(SQuery2 toolboxQuery) {
    return getIds(toolboxQuery, 0, 0);

  }

  public static Iterable<Long> listIds(SQuery2 toolboxQuery, int startIndex, int amount) {
    return getIds(toolboxQuery, amount, startIndex);
  }

  public static List<fr.mncc.gwttoolbox.primitives.shared.Entity> getEntities() {
    return entitiesDatabase_;
  }

  public static void clear() {
    entitiesDatabase_.clear();
  }

  private static boolean deleteEntity(String kind, long id) {
    fr.mncc.gwttoolbox.primitives.shared.Entity entity = getSync(kind, id);
    return entitiesDatabase_.remove(entity);
  }

  private static Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicateEquals(
      final Filter2 sfilter) {
    final String propertyName = sfilter.getPropertyName();

    Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicate = new Predicate<Entity>() {
      @Override
      public boolean apply(@Nullable Entity entity) {
        Objects.requireNonNull(entity);

        Object entityPropertyValue = entity.getAsObject(propertyName);
        Object queryPropertyValue = sfilter.getPropertyValue();

        return com.google.common.base.Objects.equal(entityPropertyValue, queryPropertyValue);

      }
    };
    return predicate;
  }

  private static Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicateNotEquals(
      final Filter2 sfilter) {

    final String propertyName = sfilter.getPropertyName();

    Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicate = new Predicate<Entity>() {
      @Override
      public boolean apply(@Nullable Entity entity) {
        Objects.requireNonNull(entity);

        Object entityPropertyValue = entity.getAsObject(propertyName);
        Object queryPropertyValue = sfilter.getPropertyValue();

        return !com.google.common.base.Objects.equal(entityPropertyValue, queryPropertyValue);

      }
    };
    return predicate;
  }

  private static Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicateLessThan(
      final Filter2 sfilter) {

    final String propertyName = sfilter.getPropertyName();

    Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicate = new Predicate<Entity>() {
      @Override
      public boolean apply(@Nullable Entity entity) {
        Objects.requireNonNull(entity);
        Object entityPropertyValue = entity.getAsObject(propertyName);
        Object queryPropertyValue = sfilter.getPropertyValue();

        String x = ObjectUtils.toString(entityPropertyValue);
        String y = ObjectUtils.toString(queryPropertyValue);

        if (ObjectUtils.isInteger(x) && ObjectUtils.isInteger(y)) {
          return Integer.valueOf(entityPropertyValue.toString()) < Integer
              .valueOf(queryPropertyValue.toString());
        }
        if (ObjectUtils.isFloat(x) && ObjectUtils.isFloat(y)) {
          return Float.valueOf(entityPropertyValue.toString()) < Float.valueOf(queryPropertyValue
              .toString());
        }
        if (ObjectUtils.isDouble(x) && ObjectUtils.isDouble(y)) {
          return Double.valueOf(entityPropertyValue.toString()) < Double.valueOf(queryPropertyValue
              .toString());
        }
        if (ObjectUtils.isLong(x) && ObjectUtils.isLong(y)) {
          return Long.valueOf(entityPropertyValue.toString()) < Long.valueOf(queryPropertyValue
              .toString());
        }
        return false;
      }
    };
    return predicate;
  }

  private static Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicateGreaterThan(
      final Filter2 sfilter) {
    final String propertyName = sfilter.getPropertyName();

    Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicate = new Predicate<Entity>() {

      @Override
      public boolean apply(@Nullable Entity entity) {
        Objects.requireNonNull(entity);
        Object entityPropertyValue = entity.getAsObject(propertyName);
        Object queryPropertyValue = sfilter.getPropertyValue();

        String x = ObjectUtils.toString(entityPropertyValue);
        String y = ObjectUtils.toString(queryPropertyValue);

        if (ObjectUtils.isInteger(x) && ObjectUtils.isInteger(y)) {
          return Integer.valueOf(entityPropertyValue.toString()) > Integer
              .valueOf(queryPropertyValue.toString());
        }
        if (ObjectUtils.isFloat(x) && ObjectUtils.isFloat(y)) {
          return Float.valueOf(entityPropertyValue.toString()) > Float.valueOf(queryPropertyValue
              .toString());
        }
        if (ObjectUtils.isDouble(x) && ObjectUtils.isDouble(y)) {
          return Double.valueOf(entityPropertyValue.toString()) > Double.valueOf(queryPropertyValue
              .toString());
        }
        if (ObjectUtils.isLong(x) && ObjectUtils.isLong(y)) {
          return Long.valueOf(entityPropertyValue.toString()) > Long.valueOf(queryPropertyValue
              .toString());
        }
        return false;
      }
    };
    return predicate;
  }

  private static Predicate<Entity> buildPredicates(Clause2 clause,
      Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicate) {

    if (clause != null) {

      if (clause.isNode()) {
        if (clause.isAnd()) {
          predicate =
              Predicates.and(buildPredicates(clause.getLeftClause(), predicate), buildPredicates(
                  clause.getRightClause(), predicate));
        } else {
          predicate =
              Predicates.or(buildPredicates(clause.getLeftClause(), predicate), buildPredicates(
                  clause.getRightClause(), predicate));
        }

      }

      if (clause.getLeftClause() != null) {
        clause = clause.getLeftClause();
      }

      if (clause.isLeaf()) {
        Filter2 sfilter = (Filter2) clause;
        if (sfilter.getOperator() == FilterOperator2.EQUAL)
          predicate = predicateEquals(sfilter);
        else if (sfilter.getOperator() == FilterOperator2.NOT_EQUAL)
          predicate = predicateNotEquals(sfilter);
        else if (sfilter.getOperator() == FilterOperator2.LESS_THAN)
          predicate = predicateLessThan(sfilter);
        else if (sfilter.getOperator() == FilterOperator2.LESS_THAN_OR_EQUAL)
          predicate = Predicates.or(predicateLessThan(sfilter), predicateEquals(sfilter));
        else if (sfilter.getOperator() == FilterOperator2.GREATER_THAN)
          predicate = predicateGreaterThan(sfilter);
        else if (sfilter.getOperator() == FilterOperator2.GREATER_THAN_OR_EQUAL)
          predicate = Predicates.or(predicateGreaterThan(sfilter), predicateEquals(sfilter));

      }

      if (clause.getRightClause() != null) {
        clause = clause.getRightClause();
      }
    }

    return predicate;
  }

  private static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> removeIdFromEntityProperties(
      FluentIterable<fr.mncc.gwttoolbox.primitives.shared.Entity> fluentIterable,
      SQuery2 toolboxQuery) {
    List<fr.mncc.gwttoolbox.primitives.shared.Entity> entities =
        new ArrayList<fr.mncc.gwttoolbox.primitives.shared.Entity>();

    int fluentSize = fluentIterable.size();

    // Remove id from the list of properties
    for (int i = 0; i < fluentSize; i++) {
      fr.mncc.gwttoolbox.primitives.shared.Entity e =
          new Entity(fluentIterable.get(i).getKind(), fluentIterable.get(i).getId());
      List<String> properties = fluentIterable.get(i).getProperties();
      int tempLength = properties.size();
      for (int j = 1; j < tempLength; j++) {
        e.getProperties().add(properties.get(j));
      }

      entities.add(e);
    }

    return entities;
  }

  private static Iterable<Long> getIds(SQuery2 toolboxQuery, int startIndex, int amount) {
    Predicate<fr.mncc.gwttoolbox.primitives.shared.Entity> predicate = null;
    predicate = buildPredicates(toolboxQuery.getClause(), predicate);

    FluentIterable<Entity> fluentIterable =
        FluentIterable.from(entitiesDatabase_).filter(predicate);

    List<Long> ids = new ArrayList<Long>();

    int size = fluentIterable.size();
    for (int i = 0; i < size; i++) {
      ids.add(fluentIterable.get(i).getId());
    }

    if ((startIndex != 0 && amount != 0))
      return subList(ids, amount, startIndex);

    return ids;
  }

  /**
   * 
   * @param list - the list
   * @param offset - the offset
   * @param limit - the limit
   * @return a list that contains "limit" number of values starting from index offset
   */
  private static <T> List<T> subList(List<T> list, int offset, int limit) {
    if (offset < 0)
      throw new IllegalArgumentException("Offset must be >=0 but was " + offset + "!");
    if (limit < -1)
      throw new IllegalArgumentException("Limit must be >=-1 but was " + limit + "!");

    if (offset > 0) {
      if (offset >= list.size()) {
        return list.subList(0, 0); // return empty.
      }
      if (limit > -1) {
        // apply offset and limit
        return list.subList(offset, Math.min(offset + limit, list.size()));
      } else {
        // apply just offset
        return list.subList(offset, list.size());
      }
    } else if (limit > -1) {
      // apply just limit
      return list.subList(0, Math.min(limit, list.size()));
    } else {
      return list.subList(0, list.size());
    }
  }

  private static int getArrayIndexFromEntityId(long id) {

    int size = entitiesDatabase_.size();
    for (int index = 0; index < size; index++) {
      Entity entity = entitiesDatabase_.get(index);
      if (entity.getId() == id) {
        return index;
      }
    }
    return -1;
  }
}
