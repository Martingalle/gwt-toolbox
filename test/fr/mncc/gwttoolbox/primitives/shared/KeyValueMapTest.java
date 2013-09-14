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

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class KeyValueMapTest {

  @Test
  public void testNoExceptionOnNull() {
    boolean thrown = false;
    try {
      KeyValueMap kvm = new KeyValueMap(null);
      assertEquals(kvm.toJson(), "{}");
      assertEquals(kvm.toString(), "");
      assertTrue(kvm.toPairs().isEmpty());
    } catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  public void testDefaultConstructor() {
    boolean thrown = false;
    try {
      KeyValueMap kvm = new KeyValueMap();
      assertEquals(kvm.toJson(), "{}");
      assertEquals(kvm.toString(), "");
      assertTrue(kvm.toPairs().isEmpty());
    } catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  public void testStringConstructor() {
    KeyValueMap kvm = new KeyValueMap("a=b&b=c&c=d");
    assertTrue(kvm.containsKey("a"));
    assertTrue(kvm.containsKey("b"));
    assertTrue(kvm.containsKey("c"));
    assertEquals(kvm.get("a"), "b");
    assertEquals(kvm.get("b"), "c");
    assertEquals(kvm.get("c"), "d");
  }

  @Test
  public void testToPair() {
    List<KeyValuePair<String, String>> keyValuePairs = createMap().toPairs();
    assertEquals(keyValuePairs.get(0).getKey(), "a");
    assertEquals(keyValuePairs.get(0).getValue(), "b");
    assertEquals(keyValuePairs.get(1).getKey(), "b");
    assertEquals(keyValuePairs.get(1).getValue(), "c");
    assertEquals(keyValuePairs.get(2).getKey(), "c");
    assertEquals(keyValuePairs.get(2).getValue(), "d");
  }

  @Test
  public void testToJson() {
    KeyValueMap kvm = createMap();
    assertEquals(kvm.toJson(), "{\"a\":\"b\",\"b\":\"c\",\"c\":\"d\"}");
  }

  @Test
  public void testToString() {
    KeyValueMap kvm = createMap();
    assertEquals(kvm.toString(), "a=b&b=c&c=d");
  }

  private KeyValueMap createMap() {
    KeyValueMap kvm = new KeyValueMap();
    kvm.put("a", "b");
    kvm.put("b", "c");
    kvm.put("c", "d");
    return kvm;
  }
}
