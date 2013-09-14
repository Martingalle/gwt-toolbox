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
package fr.mncc.gwttoolbox.primitives.shared;

import java.io.Serializable;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Key-value pair
 * 
 * @param <K> key_ type
 * @param <V> value_ type
 */
public class KeyValuePair<K, V> implements Serializable, IsSerializable {

  // @Ensures({"result != null", "result.getKey() == k", "result.getValue() == v"})
  public static <K, V> KeyValuePair<K, V> of(K k, V v) {
    return new KeyValuePair<K, V>(k, v);
  }

  private K key_;
  private V value_;

  public KeyValuePair() {
    this.key_ = null;
    this.value_ = null;
  }

  // @Ensures({"getKey() == key", "getValue() == value"})
  public KeyValuePair(K key, V value) {
    this.key_ = key;
    this.value_ = value;
  }

  public K getKey() {
    return key_;
  }

  public V getValue() {
    return value_;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("key_", key_).add("value_", value_).omitNullValues()
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key_, value_);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object)
      return true;
    if (!(object instanceof KeyValuePair))
      return false;

    KeyValuePair<?, ?> keyValue = (KeyValuePair<?, ?>) object;
    return Objects.equal(key_, keyValue.key_) && Objects.equal(value_, keyValue.value_);
  }
}
