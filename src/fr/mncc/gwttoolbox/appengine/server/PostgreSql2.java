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

import fr.mncc.gwttoolbox.appengine.shared.SQuery2;

public class PostgreSql2 {

  private static ThreadLocal<PostgreSql3> psqlThreadLocal = new ThreadLocal<PostgreSql3>();

  /**
   * 
   * @param obj
   * @return String a prepared string to be used in an INSERT or UPDATE query
   */
  public static String preparedQuery(Object obj) {
    return psqlThreadLocal.get().preparedQuery(obj);
  }

  public static fr.mncc.gwttoolbox.primitives.shared.Entity fromAppEngineEntity(
      com.google.appengine.api.datastore.Entity appEngineEntity) {
    return null;
  }

  public static Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity) {
    return psqlThreadLocal.get().putSync(entity);
  }

  public static Long putSync(fr.mncc.gwttoolbox.primitives.shared.Entity entity,
      String ancestorKind, long ancestorId) {
    return psqlThreadLocal.get().putSync(entity, ancestorKind, ancestorId);
  }

  public static List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities) {
    return psqlThreadLocal.get().putSync(entities);
  }

  public static List<Long> putSync(Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> entities,
      String ancestorKind, long ancestorId) {
    return psqlThreadLocal.get().putSync(entities, ancestorKind, ancestorId);
  }

  public static fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id) {
    return psqlThreadLocal.get().getSync(kind, id);
  }

  public static fr.mncc.gwttoolbox.primitives.shared.Entity getSync(String kind, long id,
      String ancestorKind, long ancestorId) {
    return psqlThreadLocal.get().getSync(kind, id, ancestorKind, ancestorId);
  }

  public static Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids) {
    return psqlThreadLocal.get().getSync(kind, ids);
  }

  public static Map<Long, fr.mncc.gwttoolbox.primitives.shared.Entity> getSync(String kind,
      Iterable<Long> ids, String ancestorKind, long ancestorId) {
    return psqlThreadLocal.get().getSync(kind, ids, ancestorKind, ancestorId);
  }

  public static boolean deleteSync(String kind, long id) {
    return psqlThreadLocal.get().deleteSync(kind, id);
  }

  public static boolean deleteSync(String kind, long id, String ancestorKind, long ancestorId) {
    return psqlThreadLocal.get().deleteSync(kind, id, ancestorKind, ancestorId);
  }

  public static boolean deleteSync(String kind, Iterable<Long> ids) {
    return psqlThreadLocal.get().deleteSync(kind, ids);
  }

  public static boolean deleteSync(String kind, Iterable<Long> ids, String ancestorKind,
      long ancestorId) {
    return psqlThreadLocal.get().deleteSync(kind, ids, ancestorKind, ancestorId);
  }

  public static long listSize(SQuery2 toolboxQuery) {
    return psqlThreadLocal.get().listSize(toolboxQuery);
  }

  public static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery) {
    return psqlThreadLocal.get().list(toolboxQuery);
  }

  public static Iterable<fr.mncc.gwttoolbox.primitives.shared.Entity> list(SQuery2 toolboxQuery,
      int startIndex, int amount) {
    return psqlThreadLocal.get().list(toolboxQuery, startIndex, amount);
  }

  public static Iterable<Long> listIds(SQuery2 toolboxQuery) {
    return psqlThreadLocal.get().listIds(toolboxQuery);
  }

  public static Iterable<Long> listIds(SQuery2 toolboxQuery, int startIndex, int amount) {
    return psqlThreadLocal.get().listIds(toolboxQuery, startIndex, amount);
  }
}
