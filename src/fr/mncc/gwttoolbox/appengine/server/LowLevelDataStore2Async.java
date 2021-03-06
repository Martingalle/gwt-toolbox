/**
 * Copyright (c) 2012 MNCC
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

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class LowLevelDataStore2Async {

  private final static AsyncDatastoreService dataStore_ = DatastoreServiceFactory
      .getAsyncDatastoreService();

  // @Ensures("dataStore_ != null")
  public static AsyncDatastoreService getInstance() {
    return dataStore_;
  }

  // //@Requires("entity != null")
  public static Future<Key> put(Entity entity) {
    return dataStore_.put(entity);
  }

  // //@Requires("entities != null")
  public static Future<List<Key>> put(Iterable<Entity> entities) {
    return dataStore_.put(entities);
  }

  // //@Requires("key != null")
  public static Future<Entity> get(Key key) {
    return dataStore_.get(key);
  }

  // //@Requires("keys != null")
  public static Future<Map<Key, Entity>> get(Iterable<Key> keys) {
    return dataStore_.get(keys);
  }

  // //@Requires("key != null")
  public static Future<Void> delete(Key key) {
    return dataStore_.delete(key);
  }

  // //@Requires("keys != null")
  public static Future<Void> delete(Iterable<Key> keys) {
    return dataStore_.delete(keys);
  }
}
