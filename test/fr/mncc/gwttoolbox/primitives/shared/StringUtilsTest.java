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

import static junit.framework.Assert.*;

public class StringUtilsTest {

  @Test
  public void testParseInt() {
    assertEquals(StringUtils.parseInt(null), 0);
    assertEquals(StringUtils.parseInt(""), 0);
    assertEquals(StringUtils.parseInt("test"), 0);
    assertEquals(StringUtils.parseInt("8.8"), 0);
    assertEquals(StringUtils.parseInt("8"), 8);
    assertEquals(StringUtils.parseInt("08"), 8);
  }

  @Test
  public void testParseLong() {
    assertEquals(StringUtils.parseLong(null), 0);
    assertEquals(StringUtils.parseLong(""), 0);
    assertEquals(StringUtils.parseLong("test"), 0);
    assertEquals(StringUtils.parseLong("8.8"), 0);
    assertEquals(StringUtils.parseLong("8"), 8);
    assertEquals(StringUtils.parseLong("08"), 8);
  }

  @Test
  public void testParseFloat() {
    assertEquals(StringUtils.parseFloat(null), 0.0f);
    assertEquals(StringUtils.parseFloat(""), 0.0f);
    assertEquals(StringUtils.parseFloat("test"), 0.0f);
    assertEquals(StringUtils.parseFloat("8"), 8.0f);
    assertEquals(StringUtils.parseFloat("8.8"), 8.8f);
    assertEquals(StringUtils.parseFloat("08.8"), 8.8f);
  }

  @Test
  public void testParseDouble() {
    assertEquals(StringUtils.parseDouble(null), 0.0d);
    assertEquals(StringUtils.parseDouble(""), 0.0d);
    assertEquals(StringUtils.parseDouble("test"), 0.0d);
    assertEquals(StringUtils.parseDouble("8"), 8.0d);
    assertEquals(StringUtils.parseDouble("8.8"), 8.8d);
    assertEquals(StringUtils.parseDouble("08.8"), 8.8d);
  }

  @Test
  public void testParseBoolean() {
    assertFalse(StringUtils.parseBoolean(null));
    assertFalse(StringUtils.parseBoolean(""));
    assertFalse(StringUtils.parseBoolean("t"));
    assertFalse(StringUtils.parseBoolean("f"));
    assertTrue(StringUtils.parseBoolean("true"));
    assertTrue(StringUtils.parseBoolean("True"));
    assertTrue(StringUtils.parseBoolean("TRUE"));
    assertFalse(StringUtils.parseBoolean("false"));
    assertFalse(StringUtils.parseBoolean("False"));
    assertFalse(StringUtils.parseBoolean("FALSE"));
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testEscapeToLaTex() {
    assertEquals(StringUtils.escapeToLaTex(null), "");
    assertEquals(StringUtils.escapeToLaTex(""), "");
    assertEquals(StringUtils.escapeToLaTex("\\'éléphant'"), "\\textbackslash'\\'el\\'ephant'");
  }

  @Test
  public void testStringManipulations() {
    assertEquals(StringUtils.capitalizeWords(null), "");
    assertEquals(StringUtils.capitalizeWords(""), "");
    assertEquals(StringUtils.capitalizeWords("hey there"), "Hey There");
    assertEquals(StringUtils.capitalizeWords("heY therE"), "HeY TherE");
    assertEquals(StringUtils.uncapitalizeWords(null), "");
    assertEquals(StringUtils.uncapitalizeWords(""), "");
    assertEquals(StringUtils.uncapitalizeWords("Hey There"), "hey there");
    assertEquals(StringUtils.uncapitalizeWords("HeY TherE"), "heY therE");
  }
}
