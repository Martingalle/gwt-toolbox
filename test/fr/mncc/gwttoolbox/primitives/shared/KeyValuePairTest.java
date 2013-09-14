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

import static org.junit.Assert.*;

public class KeyValuePairTest {

  @Test
  public void testNoExceptionOnNull() {
    boolean thrown = false;
    try {
      KeyValuePair kvp = new KeyValuePair(null, null);
      assertNull(kvp.getKey());
      assertNull(kvp.getValue());
      assertEquals(kvp.toString(), "KeyValuePair{}");
      assertEquals(kvp.hashCode(), 961);
      assertEquals(kvp, new KeyValuePair(null, null));
      assertEquals(kvp, kvp);
      assertFalse(kvp.equals("test"));
    } catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  public void testDefaultConstructor() {
    boolean thrown = false;
    try {
      KeyValuePair kvp = new KeyValuePair();
      assertNull(kvp.getKey());
      assertNull(kvp.getValue());
      assertEquals(kvp.toString(), "KeyValuePair{}");
      assertEquals(kvp.hashCode(), 961);
      assertEquals(kvp, new KeyValuePair());
      assertEquals(kvp, new KeyValuePair(null, null));
      assertEquals(kvp, kvp);
      assertFalse(kvp.equals("test"));
    } catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  public void testStaticInitializer() {
    boolean thrown = false;
    try {
      KeyValuePair kvp = KeyValuePair.of(null, null);
      assertNull(kvp.getKey());
      assertNull(kvp.getValue());
      assertEquals(kvp.toString(), "KeyValuePair{}");
      assertEquals(kvp.hashCode(), 961);
      assertEquals(kvp, new KeyValuePair());
      assertEquals(kvp, new KeyValuePair(null, null));
      assertEquals(kvp, kvp);
      assertFalse(kvp.equals("test"));
    } catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  public void testForStrings() {
    KeyValuePair kvp = new KeyValuePair<String, String>("a", "b");
    assertEquals(kvp.getKey(), "a");
    assertEquals(kvp.getValue(), "b");
    assertEquals(kvp.toString(), "KeyValuePair{key_=a, value_=b}");
    assertEquals(kvp.hashCode(), 4066);
    assertTrue(kvp.equals(new KeyValuePair("a", "b")));
    assertTrue(kvp.equals(kvp));
    assertFalse(kvp.equals(new KeyValuePair("b", "a")));
    assertFalse(kvp.equals("a"));
  }
}
