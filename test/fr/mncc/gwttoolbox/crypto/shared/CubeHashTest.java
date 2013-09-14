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

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class CubeHashTest {

  private final CubeHash cubeHash_ = new CubeHash();

  @Test
  public void testNull() {
    try {
      cubeHash_.hash(null);
      assertEquals(0, 1);
    } catch (Exception e) {
      assertEquals(0, 0);
    }
  }

  @Test
  public void testEmptyString() {
    String hash = cubeHash_.hash("");
    assertEquals(hash, "44c6de3ac6c73c391bf0906cb7482600ec06b216c7c54a2a8688a6a42676577d");
  }

  @Test
  public void testVector1() {
    String hash = cubeHash_.hash("Hello");
    assertEquals(hash, "bab9d5e37bc5869404d0d14d4fbb9439242770eb9ce240708c6eb249794a59a5");
  }

  @Test
  public void testVector2() {
    String hash = cubeHash_.hash("hello");
    assertEquals(hash, "a15c0247b4c8502c5ec2d866a4815a88c3e8780b0e7f7777c15489fc7c92d995");
  }

  @Test
  public void testVector3() {
    String hash = cubeHash_.hash("The quick brown fox jumps over the lazy dog");
    assertEquals(hash, "f81029811d549bc266706e6f238af61b8a463f87bedd67e710ecf72852b9bfc0");
  }
}
