package fr.mncc.gwttoolbox.appengine.server;

import java.util.List;
import java.util.Map;

import fr.mncc.gwttoolbox.appengine.shared.SQuery2;
import fr.mncc.gwttoolbox.primitives.shared.Entity;

public class DatabaseConnector {

  private static DatabaseDriver driver;
  private static ThreadLocal<DatabaseDriver> threadLocal;

  public static void init(ThreadLocal<DatabaseDriver> databaseDriver) {
    threadLocal = databaseDriver;
    driver = databaseDriver.get();
  }

  public Entity fromAppEngineEntity(com.google.appengine.api.datastore.Entity appEngineEntity) {
    return driver.fromAppEngineEntity(appEngineEntity);
  }

  public Long put(Entity entity) {
    return driver.put(entity);
  }

  public Long put(Entity entity, String ancestorKind, long ancestorId) {
    return driver.put(entity, ancestorKind, ancestorId);
  }

  public List<Long> put(Iterable<Entity> entities) {
    return driver.putAll(entities);
  }

  public List<Long> put(Iterable<Entity> entities, String ancestorKind, long ancestorId) {
    return driver.putAll(entities, ancestorKind, ancestorId);
  }

  public Entity get(String kind, long id) {
    return driver.get(kind, id);
  }

  public Entity get(String kind, long id, String ancestorKind, long ancestorId) {
    return driver.get(kind, id, ancestorKind, ancestorId);
  }

  public Map<Long, Entity> get(String kind, Iterable<Long> ids) {
    return driver.getAll(kind, ids);
  }

  public Map<Long, Entity> get(String kind, Iterable<Long> ids, String ancestorKind, long ancestorId) {
    return driver.getAll(kind, ids, ancestorKind, ancestorId);
  }

  public boolean delete(String kind, long id) {
    return driver.delete(kind, id);
  }

  public boolean delete(String kind, long id, String ancestorKind, long ancestorId) {
    return driver.delete(kind, id, ancestorKind, ancestorId);
  }

  public boolean delete(String kind, Iterable<Long> ids) {
    return driver.deleteAll(kind, ids);
  }

  public boolean delete(String kind, Iterable<Long> ids, String ancestorKind, long ancestorId) {
    return driver.deleteAll(kind, ids, ancestorKind, ancestorId);
  }

  public long listSize(SQuery2 toolboxQuery) {
    return driver.listSize(toolboxQuery);
  }

  public Iterable<Entity> list(SQuery2 toolboxQuery) {
    return driver.list(toolboxQuery);
  }

  public Iterable<Entity> list(SQuery2 toolboxQuery, int startIndex, int amount) {
    return driver.list(toolboxQuery, startIndex, amount);
  }

  public Iterable<Long> listIds(SQuery2 toolboxQuery) {
    return driver.listIds(toolboxQuery);
  }

  public Iterable<Long> listIds(SQuery2 toolboxQuery, int startIndex, int amount) {
    return listIds(toolboxQuery, startIndex, amount);
  }

  public static DatabaseDriver getThreadLocal() {
    return threadLocal.get();
  }

}
