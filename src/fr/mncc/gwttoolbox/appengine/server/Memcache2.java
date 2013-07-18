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
import java.util.Set;
import java.util.concurrent.Future;

public class Memcache2 extends MemcacheConnector {

  private static ThreadLocal<Memcache3> memcacheThreadLocal;

  public static void init() {
    memcacheThreadLocal = new ThreadLocal<Memcache3>();

    MemcacheConnector.init(new ThreadLocal<MemcacheImpl>() {
      @Override
      protected Memcache3 initialValue() {
        return new Memcache3();
      }
    });
  }

  public void putAsync(String namespace, Object key, Object value) {
    memcacheThreadLocal.get().putAsync(namespace, key, value, 1);
  }

  public void putAllAsync(String namespace, Map<Object, Object> values) {
    memcacheThreadLocal.get().putAllAsync(namespace, values, -1);
  }

  public void putAsync(String namespace, Object key, Object value, int expirationInSeconds) {
    memcacheThreadLocal.get().putAsync(namespace, key, value, expirationInSeconds);
  }

  public void putAllAsync(String namespace, Map<Object, Object> values, int expirationInSeconds) {
    memcacheThreadLocal.get().putAllAsync(namespace, values, expirationInSeconds);
  }

  public Future<Object> getAsync(String namespace, Object key) {
    return memcacheThreadLocal.get().getAsync(namespace, key);
  }

  public Future<Map<Object, Object>> getAllAsync(String namespace, List<Object> keys) {
    return memcacheThreadLocal.get().getAllAsync(namespace, keys);
  }

  public Future<Boolean> deleteAsync(String namespace, Object key) {
    return memcacheThreadLocal.get().deleteAsync(namespace, key);
  }

  public Future<Set<Object>> deleteAllAsync(String namespace, List<Object> keys) {
    return memcacheThreadLocal.get().deleteAllAsync(namespace, keys);
  }
}
