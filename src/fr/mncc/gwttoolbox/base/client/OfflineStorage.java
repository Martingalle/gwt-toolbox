/**
 * Copyright (c) 2011 MNCC
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
package fr.mncc.gwttoolbox.base.client;

import com.google.gwt.storage.client.Storage;

public class OfflineStorage {

  /**
   * Save a 2-uplet (key, value) to local storage
   * 
   * @param key unique identifier
   * @param data value
   */
  public static void ToLocalStorage(final String key, final String data) {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null)
      storage.setItem(key, data);
  }

  /**
   * Get value associated with key from local storage
   * 
   * @param key unique identifier
   * @return value
   */
  public static String FromLocalStorage(final String key) {
    Storage storage = Storage.getLocalStorageIfSupported();
    return (storage != null && storage.getItem(key) != null) ? storage.getItem(key) : "";
  }

  /**
   * Remove item whose key is key from local storage
   * 
   * @param key unique identifier
   */
  public static void RemoveFromLocalStorage(final String key) {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null)
      storage.removeItem(key);
  }

  /**
   * Save a 2-uplet (key, value) to session storage
   * 
   * @param key unique identifier
   * @param data value
   */
  public static void ToSessionStorage(final String key, final String data) {
    Storage storage = Storage.getSessionStorageIfSupported();
    if (storage != null)
      storage.setItem(key, data);
  }

  /**
   * Get value associated with key from session storage
   * 
   * @param key unique identifier
   * @return value
   */
  public static String FromSessionStorage(final String key) {
    Storage storage = Storage.getSessionStorageIfSupported();
    return storage != null ? storage.getItem(key) : "";
  }

  /**
   * Remove item whose key is key from session storage
   * 
   * @param key unique identifier
   */
  public static void RemoveFromSessionStorage(final String key) {
    Storage storage = Storage.getSessionStorageIfSupported();
    if (storage != null)
      storage.removeItem(key);
  }
}
