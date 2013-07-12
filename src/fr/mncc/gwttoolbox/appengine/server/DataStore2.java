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

import fr.mncc.gwttoolbox.appengine.shared.SQuery2;

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
    return dsThreadLocal.get().put(entity);
  }

  @Requires("entity != null")
  @Ensures("result != null")
  public static Future<Long> put(fr.mncc.gwttoolbox.primitives.shared.Entity entity,
      String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().put(entity, ancestorKind, ancestorId);
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
    return dsThreadLocal.get().put(entities);
  }

  @Requires("entities != null")
  @Ensures("result != null")
  public static Future<List<Long>> put(
      Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities, String ancestorKind,
      long ancestorId) {
    return dsThreadLocal.get().put(entities, ancestorKind, ancestorId);
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
    return dsThreadLocal.get().get(kind, id);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<fr.mncc.gwttoolbox.primitives.shared.Entity> get(String kind, long id,
      String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().get(kind, id, ancestorKind, ancestorId);
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
    return dsThreadLocal.get().get(kind, ids);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity>> get(String kind,
      Iterable<Long> ids, String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().get(kind, ids, ancestorKind, ancestorId);
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
    return dsThreadLocal.get().delete(kind, id);
  }

  @Requires({"kind != null", "id > 0"})
  @Ensures("result != null")
  public static Future<Void> delete(String kind, long id, String ancestorKind, long ancestorId) {
    return dsThreadLocal.get().delete(kind, id, ancestorKind, ancestorId);
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
    return dsThreadLocal.get().delete(kind, ids);
  }

  @Requires({"kind != null", "ids != null"})
  @Ensures("result != null")
  public static Future<Void> delete(String kind, Iterable<Long> ids, String ancestorKind,
      long ancestorId) {
    return dsThreadLocal.get().delete(kind, ids, ancestorKind, ancestorId);
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
