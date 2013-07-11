package fr.mncc.gwttoolbox.appengine.server;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Projection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Text;
import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import fr.mncc.gwttoolbox.appengine.shared.SQuery2;

public class DataStore3 implements DatabaseDriver {

  public static class LowLevelDataStore2 {

    private final static int OFFSET_LIMIT = 1000;
    private final static Logger logger_ = Logger.getLogger(LowLevelDataStore2.class
        .getCanonicalName());
    private final static DatastoreService dataStore_ = DatastoreServiceFactory
        .getDatastoreService();

    @Ensures("dataStore_ != null")
    public static DatastoreService getInstance() {
      return dataStore_;
    }

    @Requires("entity != null")
    public static Key put(com.google.appengine.api.datastore.Entity entity) {
      return dataStore_.put(entity);
    }

    @Requires("entities != null")
    public static List<Key> put(Iterable<com.google.appengine.api.datastore.Entity> entities) {
      return dataStore_.put(entities);
    }

    @Requires("key != null")
    public static com.google.appengine.api.datastore.Entity get(Key key) {
      Entity entity = null;
      try {
        entity = dataStore_.get(key);
      } catch (Exception e) {
        logger_.log(Level.SEVERE, e.getMessage());
      }
      return entity;
    }

    @Requires("keys != null")
    public static Map<Key, com.google.appengine.api.datastore.Entity> get(Iterable<Key> keys) {
      return dataStore_.get(keys);
    }

    @Requires("key != null")
    public static void delete(Key key) {
      dataStore_.delete(key);
    }

    @Requires("keys != null")
    public static void delete(Iterable<Key> keys) {
      dataStore_.delete(keys);
    }

    @Requires("query != null")
    public static long listSize(Query query) {
      List<Query.SortPredicate> sortPredicates =
          new ArrayList<Query.SortPredicate>(query.getSortPredicates());
      query.getSortPredicates().clear();

      List<Projection> projections = new ArrayList<Projection>(query.getProjections());
      query.getProjections().clear();

      boolean isKeysOnly = query.isKeysOnly();
      query.setKeysOnly();

      PreparedQuery preparedQuery = dataStore_.prepare(query);
      FetchOptions fetchOptions = FetchOptions.Builder.withOffset(0);
      long size = preparedQuery.asList(fetchOptions).size();

      if (!isKeysOnly)
        query.clearKeysOnly();

      for (Projection projection : projections)
        query.addProjection(projection);

      for (Query.SortPredicate sortPredicate : sortPredicates)
        query.addSort(sortPredicate.getPropertyName(), sortPredicate.getDirection());
      return size;
    }

    @Requires("query != null")
    public static Iterable<com.google.appengine.api.datastore.Entity> list(Query query) {
      return list(query, 0, OFFSET_LIMIT);
    }

    @Requires({"query != null", "startIndex >= 0", "amount > 0"})
    @SuppressWarnings("deprecation")
    public static Iterable<com.google.appengine.api.datastore.Entity> list(Query query,
        int startIndex, int amount) {

      PreparedQuery preparedQuery = dataStore_.prepare(query);
      int offset = startIndex;

      if (offset < OFFSET_LIMIT) {
        if ((offset + amount) >= OFFSET_LIMIT)
          amount = amount - (offset + amount - OFFSET_LIMIT);
        return preparedQuery.asList(FetchOptions.Builder.withLimit(amount).offset(offset)
            .chunkSize(amount));
      }

      int steps = startIndex / OFFSET_LIMIT;
      offset = startIndex - steps * OFFSET_LIMIT;
      Cursor cursor = getCursor(steps, preparedQuery);

      if (cursor != null)
        return preparedQuery.asList(FetchOptions.Builder.withLimit(amount).offset(offset)
            .chunkSize(amount).cursor(cursor));
      return null;
    }

    @Requires("query != null")
    @Ensures("result != null")
    public static Iterable<Key> listKeys(Query query) {
      return listKeys(query, 0, OFFSET_LIMIT);
    }

    @Requires({"query != null", "startIndex >= 0", "amount > 0"})
    @Ensures("result != null")
    public static Iterable<Key> listKeys(Query query, int startIndex, int amount) {
      boolean isKeysOnly = query.isKeysOnly();
      query.setKeysOnly();

      List<Key> ids = new ArrayList<Key>();
      for (com.google.appengine.api.datastore.Entity entity : LowLevelDataStore2.list(query,
          startIndex, amount))
        ids.add(entity.getKey());

      if (!isKeysOnly)
        query.clearKeysOnly();
      return ids;
    }

    @SuppressWarnings("deprecation")
    private static Cursor getCursor(int steps, PreparedQuery preparedQuery) {
      QueryResultList<com.google.appengine.api.datastore.Entity> queryResultList =
          preparedQuery.asQueryResultList(FetchOptions.Builder.withLimit(1).chunkSize(1).offset(
              OFFSET_LIMIT - 1));
      Cursor cursor = queryResultList.getCursor();
      while (--steps > 0 && cursor != null) {
        queryResultList =
            preparedQuery.asQueryResultList(FetchOptions.Builder.withLimit(1).chunkSize(1).offset(
                OFFSET_LIMIT - 1).cursor(cursor));
        cursor = queryResultList.getCursor();
      }
      return cursor;
    }
  }

  private final static Logger logger_ = Logger.getLogger(DataStore3.class.getCanonicalName());

  @Requires({"kind != null", "id >= 0"})
  @Ensures("result != null")
  public final Key createKey(String kind, long id) {
    return KeyFactory.createKey(kind, id);
  }

  @Requires({"kind != null", "id >= 0", "ancestorKind != null", "ancestorId >= 0"})
  @Ensures("result != null")
  public final Key createKey(String kind, long id, String ancestorKind, long ancestorId) {
    return ancestorKind == null ? createKey(kind, id) : KeyFactory.createKey(createKey(
        ancestorKind, ancestorId), kind, id);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public final Iterable<Key> createKeys(String kind, Iterable<Long> ids) {
    List<Key> keys = new ArrayList<Key>();
    for (Long id : ids)
      keys.add(createKey(kind, id));
    return keys;
  }

  @Requires({"kind != null", "ids != null", "ancestorKind != null", "ancestorId >= 0"})
  @Ensures("result != null")
  public final Iterable<Key> createKeys(String kind, Iterable<Long> ids, String ancestorKind,
      long ancestorId) {
    if (ancestorKind == null)
      return createKeys(kind, ids);

    List<Key> keys = new ArrayList<Key>();
    for (Long id : ids)
      keys.add(createKey(kind, id, ancestorKind, ancestorId));
    return keys;
  }

  @Deprecated
  public fr.mncc.gwttoolbox.primitives.shared.Entity fromAppEngineEntity(
      com.google.appengine.api.datastore.Entity appEngineEntity) {
    return convertToToolboxEntity(appEngineEntity);
  }

  @Deprecated
  public final com.google.appengine.api.datastore.Entity toAppEngineEntity(
      fr.mncc.gwttoolbox.primitives.shared.Entity toolboxEntity, String ancestorKind,
      long ancestorId) {
    return convertToAppEngineEntity(toolboxEntity, ancestorKind, ancestorId);
  }

  @Requires({"toolboxEntity != null", "ancestorKind != null", "ancestorId >= 0"})
  @Ensures("result != null")
  public final com.google.appengine.api.datastore.Entity convertToAppEngineEntity(
      fr.mncc.gwttoolbox.primitives.shared.Entity toolboxEntity, String ancestorKind,
      long ancestorId) {

    // Create a new AppEngine entity
    com.google.appengine.api.datastore.Entity appEngineEntity = null;
    if (ancestorKind == null) {
      if (toolboxEntity.getId() != 0)
        appEngineEntity =
            new com.google.appengine.api.datastore.Entity(toolboxEntity.getKind(), toolboxEntity
                .getId());
      else
        appEngineEntity = new com.google.appengine.api.datastore.Entity(toolboxEntity.getKind());
    } else {
      if (toolboxEntity.getId() != 0)
        appEngineEntity =
            new com.google.appengine.api.datastore.Entity(toolboxEntity.getKind(), toolboxEntity
                .getId(), createKey(ancestorKind, ancestorId));
      else
        appEngineEntity =
            new com.google.appengine.api.datastore.Entity(toolboxEntity.getKind(), createKey(
                ancestorKind, ancestorId));
    }

    // Fill the AppEngine entity with the proper values, taking care of a few AppEngine limitations
    for (String propertyName : toolboxEntity.keySet()) {

      if (propertyName.startsWith("__") || propertyName.endsWith("__")) {
        continue;
      }

      Object propertyValue = toolboxEntity.getAsObject(propertyName);

      if (propertyValue == null)
        continue;

      // DataStore limits String objects to 500 characters
      if (propertyValue instanceof String && ((String) propertyValue).length() >= 500)
        propertyValue = new Text((String) propertyValue);
      else if (propertyValue instanceof Timestamp) // DataStore is not able to store Timestamp
        // objects
        propertyValue = new Date(((Timestamp) propertyValue).getTime());
      else if (propertyValue instanceof Time) // DataStore is not able to store Time objects
        propertyValue = new Date(((Time) propertyValue).getTime());

      appEngineEntity.setProperty(propertyName, propertyValue);
    }

    return appEngineEntity;
  }

  @Requires("appEngineEntity != null")
  @Ensures("result != null")
  public final fr.mncc.gwttoolbox.primitives.shared.Entity convertToToolboxEntity(
      com.google.appengine.api.datastore.Entity appEngineEntity) {
    // Create a new Toolbox entity
    fr.mncc.gwttoolbox.primitives.shared.Entity toolboxEntity =
        new fr.mncc.gwttoolbox.primitives.shared.Entity(appEngineEntity.getKind(), appEngineEntity
            .getKey().getId());

    // Fill the Toolbox entity with the proper values, removing any AppEngine specific type
    for (String propertyName : appEngineEntity.getProperties().keySet()) {

      Object propertyValue = appEngineEntity.getProperty(propertyName);
      if (propertyValue instanceof Text)
        toolboxEntity.put(propertyName, ((Text) propertyValue).getValue());
      else if (propertyValue instanceof Date)
        toolboxEntity.put(propertyName, new Timestamp(((Date) propertyValue).getTime()));
      else
        toolboxEntity.put(propertyName, propertyValue);
    }
    return toolboxEntity;
  }

  @Requires({"toolboxEntities != null", "ancestorKind != null", "ancestorId >= 0"})
  @Ensures("result != null")
  public final Iterable<com.google.appengine.api.datastore.Entity> convertToAppEngineEntities(
      Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> toolboxEntities, String ancestorKind,
      long ancestorId) {
    List<com.google.appengine.api.datastore.Entity> appEngineEntities =
        new ArrayList<com.google.appengine.api.datastore.Entity>();
    for (fr.mncc.gwttoolbox.primitives.shared.Entity toolboxEntity : toolboxEntities)
      appEngineEntities.add(convertToAppEngineEntity(toolboxEntity, ancestorKind, ancestorId));
    return appEngineEntities;
  }

  @Requires("appEngineEntities != null")
  @Ensures("result != null")
  public final Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> convertToToolboxEntities(
      Iterable<com.google.appengine.api.datastore.Entity> appEngineEntities) {
    List<fr.mncc.gwttoolbox.primitives.shared.Entity> toolboxEntities =
        new ArrayList<fr.mncc.gwttoolbox.primitives.shared.Entity>();
    for (com.google.appengine.api.datastore.Entity appEngineEntity : appEngineEntities)
      toolboxEntities.add(convertToToolboxEntity(appEngineEntity));
    return toolboxEntities;
  }

  @Override
  @Requires("entity != null")
  @Ensures("result != null")
  public Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
    return putSync(entity, null, 0);
  }

  @Override
  @Requires("entity != null")
  @Ensures("result != null")
  public Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity, String ancestorKind,
      long ancestorId) {
    return LowLevelDataStore2.put(convertToAppEngineEntity(entity, ancestorKind, ancestorId))
        .getId();
  }

  @Override
  @Requires("entities != null")
  @Ensures("result != null")
  public List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities) {
    return putSync(entities, null, 0);
  }

  @Override
  @Requires("entities != null")
  @Ensures("result != null")
  public List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities,
      String ancestorKind, long ancestorId) {
    List<Long> idsTmp = new ArrayList<Long>();
    for (Key key : LowLevelDataStore2.put(convertToAppEngineEntities(entities, ancestorKind,
        ancestorId)))
      idsTmp.add(key.getId());
    return idsTmp;
  }

  @Override
  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id) {
    return getSync(kind, id, null, 0);
  }

  @Override
  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id,
      String ancestorKind, long ancestorId) {
    final Key key =
        ancestorKind == null ? createKey(kind, id) : createKey(kind, id, ancestorKind, ancestorId);
    return convertToToolboxEntity(LowLevelDataStore2.get(key));
  }

  @Override
  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids) {
    return getSync(kind, ids, null, 0);
  }

  @Override
  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids, String ancestorKind, long ancestorId) {

    final Iterable<Key> keys =
        ancestorKind == null ? createKeys(kind, ids) : createKeys(kind, ids, ancestorKind,
            ancestorId);
    Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> toolboxEntitiesTmp =
        new HashMap<Long, fr.mncc.gwttoolbox.primitives.shared.Entity>();
    Map<Key, com.google.appengine.api.datastore.Entity> appEngineEntitiesTmp =
        LowLevelDataStore2.get(keys);
    for (Key key : appEngineEntitiesTmp.keySet())
      toolboxEntitiesTmp.put(key.getId(), convertToToolboxEntity(appEngineEntitiesTmp.get(key)));
    return toolboxEntitiesTmp;
  }

  @Override
  @Requires({"kind != null", "id > 0"})
  public boolean deleteSync(String kind, long id) {
    return deleteSync(kind, id, null, 0);
  }

  @Override
  @Requires({"kind != null", "id > 0"})
  public boolean deleteSync(String kind, long id, String ancestorKind, long ancestorId) {
    if (ancestorKind == null)
      LowLevelDataStore2.delete(createKey(kind, id));
    else
      LowLevelDataStore2.delete(createKey(kind, id, ancestorKind, ancestorId));
    return true;
  }

  @Override
  @Requires({"kind != null", "ids != null"})
  public boolean deleteSync(String kind, Iterable<Long> ids) {
    return deleteSync(kind, ids, null, 0);
  }

  @Override
  @Requires({"kind != null", "ids != null"})
  public boolean deleteSync(String kind, Iterable<Long> ids, String ancestorKind, long ancestorId) {
    if (ancestorKind.isEmpty())
      LowLevelDataStore2.delete(createKeys(kind, ids));
    else
      LowLevelDataStore2.delete(createKeys(kind, ids, ancestorKind, ancestorId));
    return true;
  }

  @Override
  @Requires("toolboxQuery != null")
  @Ensures("result >= 0")
  public long listSize(SQuery2 toolboxQuery) {
    // logger_.log(Level.INFO, toolboxQuery.toString());
    Query appEngineQuery = QueryConverter2.getAsAppEngineQuery(toolboxQuery);
    return LowLevelDataStore2.listSize(appEngineQuery);
  }

  @Override
  @Requires("toolboxQuery != null")
  @Ensures("result != null")
  public Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery) {
    // logger_.log(Level.INFO, toolboxQuery.toString());
    Query appEngineQuery = QueryConverter2.getAsAppEngineQuery(toolboxQuery);
    return convertToToolboxEntities(LowLevelDataStore2.list(appEngineQuery));
  }

  @Override
  @Requires({"toolboxQuery != null", "startIndex >= 0", "amount > 0"})
  @Ensures("result != null")
  public Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery,
      int startIndex, int amount) {
    // logger_.log(Level.INFO, toolboxQuery.toString());
    Query appEngineQuery = QueryConverter2.getAsAppEngineQuery(toolboxQuery);
    return convertToToolboxEntities(LowLevelDataStore2.list(appEngineQuery, startIndex, amount));
  }

  @Override
  @Requires("toolboxQuery != null")
  @Ensures("result != null")
  public Iterable<Long> listIds(SQuery2 toolboxQuery) {
    // logger_.log(Level.INFO, toolboxQuery.toString());
    Query appEngineQuery = QueryConverter2.getAsAppEngineQuery(toolboxQuery);
    List<Long> ids = new ArrayList<Long>();
    for (Key key : LowLevelDataStore2.listKeys(appEngineQuery))
      ids.add(key.getId());
    return ids;
  }

  @Override
  @Requires({"toolboxQuery != null", "startIndex >= 0", "amount > 0"})
  @Ensures("result != null")
  public Iterable<Long> listIds(SQuery2 toolboxQuery, int startIndex, int amount) {
    // logger_.log(Level.INFO, toolboxQuery.toString());
    Query appEngineQuery = QueryConverter2.getAsAppEngineQuery(toolboxQuery);
    List<Long> ids = new ArrayList<Long>();
    for (Key key : LowLevelDataStore2.listKeys(appEngineQuery, startIndex, amount))
      ids.add(key.getId());
    return ids;
  }

}
