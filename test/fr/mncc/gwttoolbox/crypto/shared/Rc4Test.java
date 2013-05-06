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
package fr.mncc.gwttoolbox.crypto.shared;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Rc4Test {

  private final Rc4 rc4_ = new Rc4();

  @Test
  public void testConstructor() {
    try {
      Rc4 rc4 = new Rc4(null);
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }

    try {
      Rc4 rc4 = new Rc4("");
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }

    try {
      Rc4 rc4 = new Rc4("key");
      assertEquals(0, 0);
    }
    catch (Exception e) {
      assertEquals(0, 1);
    }

    try {
      Rc4 rc4 = new Rc4();
      assertEquals(0, 0);
    }
    catch (Exception e) {
      assertEquals(0, 1);
    }
  }

  @Test
  public void testCodeNullTextAndNullKey() {
    try {
      rc4_.code(null, null);
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }
  }

  @Test
  public void testCodeNullTextOrNullKey() {
    try {
      rc4_.code(null, "key");
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }

    try {
      rc4_.code(null);
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }

    try {
      rc4_.code("text", null);
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }
  }

  @Test
  public void testDecodeNullTextAndNullKey() {
    try {
      rc4_.decode(null, null);
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }
  }

  @Test
  public void testDecodeNullTextOrNullKey() {
    try {
      rc4_.decode(null, "key");
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }

    try {
      rc4_.decode(null);
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }

    try {
      rc4_.decode("text", null);
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }
  }

  @Test
  public void testCodeEmptyText() {
    String coded = rc4_.code("", "key");
    assertEquals(coded, "");

    coded = rc4_.code("");
    assertEquals(coded, "");
  }

  @Test
  public void testDecodeEmptyText() {
    String coded = rc4_.decode("", "key");
    assertEquals(coded, "");

    coded = rc4_.decode("");
    assertEquals(coded, "");
  }

  @Test
  public void testCodeEmptyKey() {
    try {
      rc4_.code("Hello", "");
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }
  }

  @Test
  public void testDecodeEmptyKey() {
    try {
      rc4_.decode("Hello", "");
      assertEquals(0, 1);
    }
    catch (Exception e) {
      assertEquals(0, 0);
    }
  }

  @Test
  public void testCodeVector1() {
    String coded1 = rc4_.code("Hello", "key");
    assertEquals(coded1, "430958814b");

    String coded2 = rc4_.code("Hello");
    assertEquals(coded2, "c71e262424");

    String coded = rc4_.code("HelloHello", "key");
    assertEquals(coded, "430958814bc71e262424");
  }

  @Test
  public void testDecodeVector1() {
    String coded1 = rc4_.decode("430958814b", "key");
    assertEquals(coded1, "Hello");

    String coded2 = rc4_.decode("c71e262424");
    assertEquals(coded2, "Hello");

    String coded = rc4_.decode("430958814bc71e262424", "key");
    assertEquals(coded, "HelloHello");
  }

  @Test
  public void testCodeVector2() {
    String coded1 = rc4_.code("The quick brown fox", "key");
    assertEquals(coded1, "5f0451cd55fa1229236b6a09792a7cdde91b95");

    String coded2 = rc4_.code(" jumps over the lazy dog");
    assertEquals(coded2, "46c1948e8f45d3c7cb5c9e5bea7c5896e2c8f5c39c57b898");

    String coded = rc4_.code("The quick brown fox jumps over the lazy dog", "key");
    assertEquals(coded, "5f0451cd55fa1229236b6a09792a7cdde91b9546c1948e8f45d3c7cb5c9e5bea7c5896e2c8f5c39c57b898");
  }

  @Test
  public void testDecodeVector2() {
    String coded1 = rc4_.decode("5f0451cd55fa1229236b6a09792a7cdde91b95", "key");
    assertEquals(coded1, "The quick brown fox");

    String coded2 = rc4_.decode("46c1948e8f45d3c7cb5c9e5bea7c5896e2c8f5c39c57b898");
    assertEquals(coded2, " jumps over the lazy dog");

    String coded = rc4_.decode("5f0451cd55fa1229236b6a09792a7cdde91b9546c1948e8f45d3c7cb5c9e5bea7c5896e2c8f5c39c57b898", "key");
    assertEquals(coded, "The quick brown fox jumps over the lazy dog");
  }
}