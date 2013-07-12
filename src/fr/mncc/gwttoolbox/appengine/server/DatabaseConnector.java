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

  public Long putSync(Entity entity) {
    return driver.putSync(entity);
  }

  public Long putSync(Entity entity, String ancestorKind, long ancestorId) {
    return driver.putSync(entity, ancestorKind, ancestorId);
  }

  public List<Long> putSync(Iterable<Entity> entities) {
    return driver.putSync(entities);
  }

  public List<Long> putSync(Iterable<Entity> entities, String ancestorKind, long ancestorId) {
    return driver.putSync(entities, ancestorKind, ancestorId);
  }

  public Entity getSync(String kind, long id) {
    return driver.getSync(kind, id);
  }

  public Entity getSync(String kind, long id, String ancestorKind, long ancestorId) {
    return driver.getSync(kind, id, ancestorKind, ancestorId);
  }

  public Map<Long, Entity> getSync(String kind, Iterable<Long> ids) {
    return driver.getSync(kind, ids);
  }

  public Map<Long, Entity> getSync(String kind, Iterable<Long> ids, String ancestorKind,
      long ancestorId) {
    return driver.getSync(kind, ids, ancestorKind, ancestorId);
  }

  public boolean deleteSync(String kind, long id) {
    return driver.deleteSync(kind, id);
  }

  public boolean deleteSync(String kind, long id, String ancestorKind, long ancestorId) {
    return driver.deleteSync(kind, id, ancestorKind, ancestorId);
  }

  public boolean deleteSync(String kind, Iterable<Long> ids) {
    return driver.deleteSync(kind, ids);
  }

  public boolean deleteSync(String kind, Iterable<Long> ids, String ancestorKind, long ancestorId) {
    return driver.deleteSync(kind, ids, ancestorKind, ancestorId);
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
