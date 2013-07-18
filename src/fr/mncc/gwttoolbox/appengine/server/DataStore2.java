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

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class DataStore2 extends DatabaseConnector {

  private static ThreadLocal<DataStore3> dsThreadLocal;

  public static void init() {
    dsThreadLocal = new ThreadLocal<DataStore3>();

    DatabaseConnector.init(new ThreadLocal<DatabaseDriver>() {
      @Override
      protected DataStore3 initialValue() {
        return new DataStore3();
      }
    });
  }

  @Requires("entity != null")
  @Ensures("result != null")
  public static Future<Long> putAsync(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
    return dsThreadLocal.get().putAsync(entity);
  }

  @Requires("entity != null")
  @Ensures("result != null")
  public static Future<Long> putAsync(fr.mncc.gwttoolbox.primitives.shared.Entity entity,
      String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().putAsync(entity, ancestorKind, ancestorId);
  }

  @Requires("entities != null")
  @Ensures("result != null")
  public static Future<List<Long>> putAllAsync(
      Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities) {
    return dsThreadLocal.get().putAllAsync(entities);
  }

  @Requires("entities != null")
  @Ensures("result != null")
  public static Future<List<Long>> putAllAsync(
      Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities, String ancestorKind,
      long ancestorId) {
    return dsThreadLocal.get().putAllAsync(entities, ancestorKind, ancestorId);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<fr.mncc.gwttoolbox.primitives.shared.Entity> getAsync(String kind, long id) {
    return dsThreadLocal.get().getAsync(kind, id);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<fr.mncc.gwttoolbox.primitives.shared.Entity> getAsync(String kind, long id,
      String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().getAsync(kind, id, ancestorKind, ancestorId);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity>> getAllAsync(
      String kind, Iterable<Long> ids) {
    return dsThreadLocal.get().getAllAsync(kind, ids);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity>> getAllAsync(
      String kind, Iterable<Long> ids, String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().getAllAsync(kind, ids, ancestorKind, ancestorId);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<Void> deleteAsync(String kind, long id) {
    return dsThreadLocal.get().deleteAsync(kind, id);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<Void> deleteAsync(String kind, long id, String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().deleteAsync(kind, id, ancestorKind, ancestorId);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Void> deleteAllAsync(String kind, Iterable<Long> ids) {
    return dsThreadLocal.get().deleteAllAsync(kind, ids);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Void> deleteAllAsync(String kind, Iterable<Long> ids, String ancestorKind,
      long ancestorId) {
    return dsThreadLocal.get().deleteAllAsync(kind, ids, ancestorKind, ancestorId);
  }
}
