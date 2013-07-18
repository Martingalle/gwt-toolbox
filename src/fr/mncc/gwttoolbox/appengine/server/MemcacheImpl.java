package fr.mncc.gwttoolbox.appengine.server;

import java.util.List;
import java.util.Map;
import java.util.Set;

interface MemcacheImpl {

  void put(String namespace, Object key, Object value);

  void putAll(String namespace, Map<Object, Object> values);

  void put(String namespace, Object key, Object value, int expirationInSeconds);

  void putAll(String namespace, Map<Object, Object> values, int expirationInSeconds);

  Object get(String namespace, Object key);

  Map<Object, Object> getAll(String namespace, List<Object> keys);

  boolean delete(String namespace, Object key);

  Set<Object> deleteAll(String namespace, List<Object> keys);
}
