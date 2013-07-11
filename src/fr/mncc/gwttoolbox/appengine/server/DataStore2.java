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
package fr.mncc.gwttoolbox.appengine.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.appengine.api.datastore.Key;
import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import fr.mncc.gwttoolbox.appengine.shared.SQuery2;
import fr.mncc.gwttoolbox.primitives.shared.Entity;

public class DataStore2 {

  private final static ThreadLocal<DataStore3> dsThreadLocal = new ThreadLocal<DataStore3>();

  @Requires("entity != null")
  @Ensures("result != null")
  public static Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
    return dsThreadLocal.get().putSync(entity);
  }

  @Requires("entity != null")
  @Ensures("result != null")
  public static Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity,
      String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().putSync(entity, ancestorKind, ancestorId);
  }

  @Requires("entity != null")
  @Ensures("result != null")
  public static Future<Long> put(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
    return put(entity, null, 0);
  }

  @Requires("entity != null")
  @Ensures("result != null")
  public static Future<Long> put(fr.mncc.gwttoolbox.primitives.shared.Entity entity,
      String ancestorKind, long ancestorId) {
    final Future<Key> key =
        LowLevelDataStore2Async.put(dsThreadLocal.get().convertToAppEngineEntity(entity,
            ancestorKind, ancestorId));
    final Future<Long> id = new Future<Long>() {
      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        return key.cancel(mayInterruptIfRunning);
      }

      @Override
      public boolean isCancelled() {
        return key.isCancelled();
      }

      @Override
      public boolean isDone() {
        return key.isDone();
      }

      @Override
      public Long get() throws InterruptedException, ExecutionException {
        return key.get().getId();
      }

      @Override
      public Long get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
          TimeoutException {
        return key.get(timeout, unit).getId();
      }
    };
    return id;
  }

  @Requires("entities != null")
  @Ensures("result != null")
  public static List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities) {
    return dsThreadLocal.get().putSync(entities);
  }

  @Requires("entities != null")
  @Ensures("result != null")
  public static List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities,
      String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().putSync(entities, ancestorKind, ancestorId);
  }

  @Requires("entities != null")
  @Ensures("result != null")
  public static Future<List<Long>> put(
      Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities) {
    return put(entities, null, 0);
  }

  @Requires("entities != null")
  @Ensures("result != null")
  public static Future<List<Long>> put(
      Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities, String ancestorKind,
      long ancestorId) {
    final Future<List<Key>> keys =
        LowLevelDataStore2Async.put(dsThreadLocal.get().convertToAppEngineEntities(entities,
            ancestorKind, ancestorId));
    final Future<List<Long>> ids = new Future<List<Long>>() {
      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        return keys.cancel(mayInterruptIfRunning);
      }

      @Override
      public boolean isCancelled() {
        return keys.isCancelled();
      }

      @Override
      public boolean isDone() {
        return keys.isDone();
      }

      @Override
      public List<Long> get() throws InterruptedException, ExecutionException {
        List<Long> idsTmp = new ArrayList<Long>();
        List<Key> keysTmp = keys.get();
        for (Key key : keysTmp)
          idsTmp.add(key.getId());
        return idsTmp;
      }

      @Override
      public List<Long> get(long timeout, TimeUnit unit) throws InterruptedException,
          ExecutionException, TimeoutException {
        List<Long> idsTmp = new ArrayList<Long>();
        List<Key> keysTmp = keys.get(timeout, unit);
        for (Key key : keysTmp)
          idsTmp.add(key.getId());
        return idsTmp;
      }
    };
    return ids;
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id) {
    return dsThreadLocal.get().getSync(kind, id);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id,
      String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().getSync(kind, id, ancestorKind, ancestorId);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<fr.mncc.gwttoolbox.primitives.shared.Entity> get(String kind, long id) {
    return get(kind, id, null, 0);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<fr.mncc.gwttoolbox.primitives.shared.Entity> get(String kind, long id,
      String ancestorKind, long ancestorId) {
    final Key key =
        ancestorKind == null ? dsThreadLocal.get().createKey(kind, id) : dsThreadLocal.get()
            .createKey(kind, id, ancestorKind, ancestorId);
    final Future<com.google.appengine.api.datastore.Entity> appEngineEntity =
        LowLevelDataStore2Async.get(key);
    final Future<fr.mncc.gwttoolbox.primitives.shared.Entity> toolboxEntity =
        new Future<fr.mncc.gwttoolbox.primitives.shared.Entity>() {
          @Override
          public boolean cancel(boolean mayInterruptIfRunning) {
            return appEngineEntity.cancel(mayInterruptIfRunning);
          }

          @Override
          public boolean isCancelled() {
            return appEngineEntity.isCancelled();
          }

          @Override
          public boolean isDone() {
            return appEngineEntity.isDone();
          }

          @Override
          public fr.mncc.gwttoolbox.primitives.shared.Entity get() throws InterruptedException,
              ExecutionException {
            return dsThreadLocal.get().convertToToolboxEntity(appEngineEntity.get());
          }

          @Override
          public fr.mncc.gwttoolbox.primitives.shared.Entity get(long timeout, TimeUnit unit)
              throws InterruptedException, ExecutionException, TimeoutException {
            return dsThreadLocal.get().convertToToolboxEntity(appEngineEntity.get(timeout, unit));
          }
        };
    return toolboxEntity;
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids) {
    return dsThreadLocal.get().getSync(kind, ids);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids, String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().getSync(kind, ids, ancestorKind, ancestorId);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity>> get(String kind,
      Iterable<Long> ids) {
    return get(kind, ids, null, 0);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity>> get(String kind,
      Iterable<Long> ids, String ancestorKind, long ancestorId) {
    final Iterable<Key> keys =
        ancestorKind == null ? dsThreadLocal.get().createKeys(kind, ids) : dsThreadLocal.get()
            .createKeys(kind, ids, ancestorKind, ancestorId);
    final Future<Map<Key, com.google.appengine.api.datastore.Entity>> appEngineEntities =
        LowLevelDataStore2Async.get(keys);
    final Future<Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity>> toolboxEntities =
        new Future<Map<Long, Entity>>() {
          @Override
          public boolean cancel(boolean mayInterruptIfRunning) {
            return appEngineEntities.cancel(mayInterruptIfRunning);
          }

          @Override
          public boolean isCancelled() {
            return appEngineEntities.isCancelled();
          }

          @Override
          public boolean isDone() {
            return appEngineEntities.isDone();
          }

          @Override
          public Map<Long, Entity> get() throws InterruptedException, ExecutionException {
            Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> toolboxEntitiesTmp =
                new HashMap<Long, Entity>();
            Map<Key, com.google.appengine.api.datastore.Entity> appEngineEntitiesTmp =
                appEngineEntities.get();
            for (Key key : appEngineEntitiesTmp.keySet())
              toolboxEntitiesTmp.put(key.getId(), dsThreadLocal.get().convertToToolboxEntity(
                  appEngineEntitiesTmp.get(key)));
            return toolboxEntitiesTmp;
          }

          @Override
          public Map<Long, Entity> get(long timeout, TimeUnit unit) throws InterruptedException,
              ExecutionException, TimeoutException {
            Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> toolboxEntitiesTmp =
                new HashMap<Long, Entity>();
            Map<Key, com.google.appengine.api.datastore.Entity> appEngineEntitiesTmp =
                appEngineEntities.get(timeout, unit);
            for (Key key : appEngineEntitiesTmp.keySet())
              toolboxEntitiesTmp.put(key.getId(), dsThreadLocal.get().convertToToolboxEntity(
                  appEngineEntitiesTmp.get(key)));
            return toolboxEntitiesTmp;
          }
        };
    return toolboxEntities;
  }

  @Requires({"kind != null", "id > 0"})
  public static boolean deleteSync(String kind, long id) {
    return dsThreadLocal.get().deleteSync(kind, id);
  }

  @Requires({"kind != null", "id > 0"})
  public static boolean deleteSync(String kind, long id, String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().deleteSync(kind, id, ancestorKind, ancestorId);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<Void> delete(String kind, long id) {
    return delete(kind, id, null, 0);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<Void> delete(String kind, long id, String ancestorKind, long ancestorId) {
    if (ancestorKind == null)
      return LowLevelDataStore2Async.delete(dsThreadLocal.get().createKey(kind, id));
    return LowLevelDataStore2Async.delete(dsThreadLocal.get().createKey(kind, id, ancestorKind,
        ancestorId));
  }

  @Requires({"kind != null", "ids != null"})
  public static boolean deleteSync(String kind, Iterable<Long> ids) {
    return dsThreadLocal.get().deleteSync(kind, ids);
  }

  @Requires({"kind != null", "ids != null"})
  public static boolean deleteSync(String kind, Iterable<Long> ids, String ancestorKind,
      long ancestorId) {
    return dsThreadLocal.get().deleteSync(kind, ids, ancestorKind, ancestorId);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Void> delete(String kind, Iterable<Long> ids) {
    return delete(kind, ids, null, 0);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Void> delete(String kind, Iterable<Long> ids, String ancestorKind,
      long ancestorId) {
    if (ancestorKind.isEmpty())
      return LowLevelDataStore2Async.delete(dsThreadLocal.get().createKeys(kind, ids));
    return LowLevelDataStore2Async.delete(dsThreadLocal.get().createKeys(kind, ids, ancestorKind,
        ancestorId));
  }

  @Requires("toolboxQuery != null")
  @Ensures("result >= 0")
  public static long listSize(SQuery2 toolboxQuery) {
    return dsThreadLocal.get().listSize(toolboxQuery);
  }

  @Requires("toolboxQuery != null")
  @Ensures("result != null")
  public static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery) {
    return dsThreadLocal.get().list(toolboxQuery);
  }

  @Requires({"toolboxQuery != null", "startIndex >= 0", "amount > 0"})
  @Ensures("result != null")
  public static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery,
      int startIndex, int amount) {
    return dsThreadLocal.get().list(toolboxQuery, startIndex, amount);
  }

  @Requires("toolboxQuery != null")
  @Ensures("result != null")
  public static Iterable<Long> listIds(SQuery2 toolboxQuery) {
    return dsThreadLocal.get().listIds(toolboxQuery);
  }

  @Requires({"toolboxQuery != null", "startIndex >= 0", "amount > 0"})
  @Ensures("result != null")
  public static Iterable<Long> listIds(SQuery2 toolboxQuery, int startIndex, int amount) {
    return dsThreadLocal.get().listIds(toolboxQuery, startIndex, amount);
  }
}
