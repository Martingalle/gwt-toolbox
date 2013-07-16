package fr.mncc.gwttoolbox.appengine.server;

import java.util.List;
import java.util.Map;

import fr.mncc.gwttoolbox.appengine.shared.SQuery2;
import fr.mncc.gwttoolbox.primitives.shared.Entity;

public interface DatabaseDriver {

  Entity fromAppEngineEntity(com.google.appengine.api.datastore.Entity appEngineEntity);

  Long put(Entity entity);

  Long put(Entity entity, String ancestorKind, long ancestorId);

  List<Long> put(Iterable<Entity> entities);

  List<Long> put(Iterable<Entity> entities, String ancestorKind, long ancestorId);

  Entity get(String kind, long id);

  Entity get(String kind, long id, String ancestorKind, long ancestorId);

  Map<Long, Entity> get(String kind, Iterable<Long> ids);

  Map<Long, Entity> get(String kind, Iterable<Long> ids, String ancestorKind, long ancestorId);

  boolean delete(String kind, long id);

  boolean delete(String kind, long id, String ancestorKind, long ancestorId);

  boolean delete(String kind, Iterable<Long> ids);

  boolean delete(String kind, Iterable<Long> ids, String ancestorKind, long ancestorId);

  long listSize(SQuery2 toolboxQuery);

  Iterable<Entity> list(SQuery2 toolboxQuery);

  Iterable<Entity> list(SQuery2 toolboxQuery, int startIndex, int amount);

  Iterable<Long> listIds(SQuery2 toolboxQuery);

  Iterable<Long> listIds(SQuery2 toolboxQuery, int startIndex, int amount);
}
