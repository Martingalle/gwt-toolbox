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

/**
 * Extracted from http://code.google.com/p/gwt-examples/wiki/gwt_hmtl5
 */
//@Invariant({"ALPHABET != null", "toInt != null"})
public class Base64Utils {

  private final static char[] ALPHABET =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
  private final static int[] toInt = new int[128];

  static {
    for (int i = 0; i < ALPHABET.length; i++)
      toInt[ALPHABET[i]] = i;
  }

  /**
   * Translates the specified byte array into Base64 string.
   * 
   * @param buf the byte array (not null)
   * @return the translated Base64 string (not null)
   */
  ////@Requires("buf != null")
  //@Ensures("result != null")
  public static String encode(byte[] buf) {
    int size = buf.length;
    char[] ar = new char[((size + 2) / 3) * 4];
    int a = 0;
    int i = 0;
    while (i < size) {
      byte b0 = buf[i++];
      byte b1 = (i < size) ? buf[i++] : 0;
      byte b2 = (i < size) ? buf[i++] : 0;
      int mask = 0x3F;
      ar[a++] = ALPHABET[(b0 >> 2) & mask];
      ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
      ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
      ar[a++] = ALPHABET[b2 & mask];
    }
    switch (size % 3) {
      case 1:
        ar[--a] = '=';
      case 2:
        ar[--a] = '=';
    }
    return new String(ar);
  }

  /**
   * Translates the specified Base64 string into a byte array.
   * 
   * @param s the Base64 string (not null)
   * @return the byte array (not null)
   */
  ////@Requires("s != null")
  //@Ensures("result != null")
  public static byte[] decode(String s) {
    int delta = s.endsWith("==") ? 2 : s.endsWith("=") ? 1 : 0;
    byte[] buffer = new byte[s.length() * 3 / 4 - delta];
    int mask = 0xFF;
    int index = 0;
    for (int i = 0; i < s.length(); i += 4) {
      int c0 = toInt[s.charAt(i)];
      int c1 = toInt[s.charAt(i + 1)];
      buffer[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
      if (index >= buffer.length)
        return buffer;
      int c2 = toInt[s.charAt(i + 2)];
      buffer[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
      if (index >= buffer.length)
        return buffer;
      int c3 = toInt[s.charAt(i + 3)];
      buffer[index++] = (byte) (((c2 << 6) | c3) & mask);
    }
    return buffer;
  }
}
