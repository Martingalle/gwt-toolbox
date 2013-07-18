package fr.mncc.gwttoolbox.appengine.server;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.memcache.Expiration;

public class Memcache3 implements MemcacheImpl {

  private final Logger logger_ = Logger.getLogger(Memcache3.class.getCanonicalName());

  @Override
  public void put(String namespace, Object key, Object value) {
    put(namespace, key, value, -1);
  }

  public void putAsync(String namespace, Object key, Object value) {
    putAsync(namespace, key, value, -1);
  }

  @Override
  public void putAll(String namespace, Map<Object, Object> values) {
    putAll(namespace, values, -1);
  }

  public void putAllAsync(String namespace, Map<Object, Object> values) {
    putAllAsync(namespace, values, -1);
  }

  @Override
  public void put(String namespace, Object key, Object value, int expirationInSeconds) {
    String oldNamespace = NamespaceManager.get();
    NamespaceManager.set(namespace);
    try {
      if (expirationInSeconds < 0)
        LowLevelMemcache.getInstance().put(key, value);
      else
        LowLevelMemcache.getInstance().put(key, value,
            Expiration.byDeltaSeconds(expirationInSeconds));
    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.toString() + "\nnamespace = " + namespace + "\nkey = "
          + key.toString() + "\nvalue = " + value.toString() + "\nexpirationInSeconds = "
          + expirationInSeconds);
    } finally {
      NamespaceManager.set(oldNamespace);
    }
  }

  public void putAsync(String namespace, Object key, Object value, int expirationInSeconds) {
    String oldNamespace = NamespaceManager.get();
    NamespaceManager.set(namespace);
    try {
      if (expirationInSeconds < 0)
        LowLevelMemcacheAsync.getInstance().put(key, value);
      else
        LowLevelMemcacheAsync.getInstance().put(key, value,
            Expiration.byDeltaSeconds(expirationInSeconds));
    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.toString() + "\nnamespace = " + namespace + "\nkey = "
          + key.toString() + "\nvalue = " + value.toString() + "\nexpirationInSeconds = "
          + expirationInSeconds);
    } finally {
      NamespaceManager.set(oldNamespace);
    }
  }

  @Override
  public void putAll(String namespace, Map<Object, Object> values, int expirationInSeconds) {
    String oldNamespace = NamespaceManager.get();
    NamespaceManager.set(namespace);
    try {
      if (expirationInSeconds < 0)
        LowLevelMemcache.getInstance().putAll(values);
      else
        LowLevelMemcache.getInstance().putAll(values,
            Expiration.byDeltaSeconds(expirationInSeconds));
    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.toString() + "\nnamespace = " + namespace + "\nvalues = "
          + values.toString() + "\nexpirationInSeconds = " + expirationInSeconds);
    } finally {
      NamespaceManager.set(oldNamespace);
    }
  }

  public void putAllAsync(String namespace, Map<Object, Object> values, int expirationInSeconds) {
    String oldNamespace = NamespaceManager.get();
    NamespaceManager.set(namespace);
    try {
      if (expirationInSeconds < 0)
        LowLevelMemcacheAsync.getInstance().putAll(values);
      else
        LowLevelMemcacheAsync.getInstance().putAll(values,
            Expiration.byDeltaSeconds(expirationInSeconds));
    } catch (Exception e) {
      logger_.log(Level.SEVERE, e.toString() + "\nnamespace = " + namespace + "\nvalues = "
          + values.toString() + "\nexpirationInSeconds = " + expirationInSeconds);
    } finally {
      NamespaceManager.set(oldNamespace);
    }
  }

  @Override
  public Object get(String namespace, Object key) {
    return LowLevelMemcache.get(namespace, key);
  }

  public Future<Object> getAsync(String namespace, Object key) {
    return LowLevelMemcacheAsync.get(namespace, key);
  }

  @Override
  public Map<Object, Object> getAll(String namespace, List<Object> keys) {
    return LowLevelMemcache.getAll(namespace, keys);
  }

  public Future<Map<Object, Object>> getAllAsync(String namespace, List<Object> keys) {
    return LowLevelMemcacheAsync.getAll(namespace, keys);
  }

  @Override
  public boolean delete(String namespace, Object key) {
    return LowLevelMemcache.delete(namespace, key);
  }

  public Future<Boolean> deleteAsync(String namespace, Object key) {
    return LowLevelMemcacheAsync.delete(namespace, key);
  }

  @Override
  public Set<Object> deleteAll(String namespace, List<Object> keys) {
    return LowLevelMemcache.deleteAll(namespace, keys);
  }

  public Future<Set<Object>> deleteAllAsync(String namespace, List<Object> keys) {
    return LowLevelMemcacheAsync.deleteAll(namespace, keys);
  }

}
