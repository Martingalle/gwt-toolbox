package fr.mncc.gwttoolbox.appengine.server;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemcacheConnector {

  private static MemcacheImpl memcacheImpl;
  private static ThreadLocal<MemcacheImpl> memcacheThreadLocal;

  public static void init(ThreadLocal<MemcacheImpl> threadLocal) {
    memcacheThreadLocal = threadLocal;
    memcacheImpl = threadLocal.get();
  }

  public static void put(String namespace, Object key, Object value) {
    memcacheImpl.put(namespace, key, value);
  }

  public static void putAll(String namespace, Map<Object, Object> values) {
    memcacheImpl.putAll(namespace, values);
  }

  public static void put(String namespace, Object key, Object value, int expirationInSeconds) {
    memcacheImpl.put(namespace, key, value, expirationInSeconds);
  }

  public static void putAll(String namespace, Map<Object, Object> values, int expirationInSeconds) {
    memcacheImpl.putAll(namespace, values, expirationInSeconds);
  }

  public static Object get(String namespace, Object key) {
    return memcacheImpl.get(namespace, key);
  }

  public static Map<Object, Object> getAll(String namespace, List<Object> keys) {
    return memcacheImpl.getAll(namespace, keys);
  }

  public static boolean delete(String namespace, Object key) {
    return memcacheImpl.delete(namespace, key);
  }

  public static Set<Object> deleteAll(String namespace, List<Object> keys) {
    return memcacheImpl.deleteAll(namespace, keys);
  }

  public static MemcacheImpl getThreadLocal() {
    return memcacheThreadLocal.get();
  }
}
