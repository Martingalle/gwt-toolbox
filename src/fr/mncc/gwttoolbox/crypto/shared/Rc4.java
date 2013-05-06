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
package fr.mncc.gwttoolbox.crypto.shared;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * RC4 Stream Cipher : http://en.wikipedia.org/wiki/RC4
 */
public class Rc4 {

  private final byte sbox[] = new byte[256];
  private int i;
  private int j;

  protected Rc4() {

  }

  public Rc4(String key) {
    reset(key);
  }

  /**
   * Code text after reseting S-box using key. Uses RC4 algorithm :
   * http://en.wikipedia.org/wiki/RC4
   * 
   * @param text to code
   * @param key to reset S-box
   * @return coded text
   */
  public String code(String text, String key) {
    reset(key);
    return byteStringToHexString(codeDecode(text));
  }

  /**
   * Decode text after reseting S-box using key. Uses RC4 algorithm :
   * http://en.wikipedia.org/wiki/RC4
   * 
   * @param text to decode
   * @param key to reset S-box
   * @return decoded text
   */
  public String decode(String text, String key) {
    reset(key);
    return codeDecode(hexStringToByteString(text));
  }

  /**
   * Code text using the current state of the S-box. Uses RC4 algorithm :
   * http://en.wikipedia.org/wiki/RC4
   * 
   * @param text to code
   * @return coded text
   */
  public String code(String text) {
    return byteStringToHexString(codeDecode(text));
  }

  /**
   * Decode text using the current state of the S-box. Uses RC4 algorithm :
   * http://en.wikipedia.org/wiki/RC4
   * 
   * @param text to decode
   * @return decoded text
   */
  public String decode(String text) {
    return codeDecode(hexStringToByteString(text));
  }

  private String byteStringToHexString(String s) {
    String r = "";
    for (int i = 0; i < s.length(); i++)
      r += byteToHexChars(s.charAt(i));
    return r;
  }

  private String byteToHexChars(int i) {
    final String s = "0" + Integer.toHexString(i);
    return s.substring(s.length() - 2);
  }

  private String hexStringToByteString(String s) {
    String r = "";
    for (int i = 0; i < s.length(); i += 2)
      r += (char) Integer.parseInt(s.substring(i, i + 2), 16);
    return r;
  }

  public void reset(String key) {

    checkNotNull(key, "key must not be null!");
    checkState(!key.isEmpty(), "key must not be an empty string!");

    for (i = 0; i < 256; i++)
      sbox[i] = (byte) i;

    int k;
    final int keyLength = key.length();

    for (i = 0, j = 0, k = 0; i < 256; i++) {
      j = j + sbox[i] + key.charAt(k) & 0xff;
      k = (k + 1) % keyLength;

      byte x = sbox[i];
      sbox[i] = sbox[j];
      sbox[j] = x;
    }

    i = 0;
    j = 0;
  }

  private String codeDecode(String text) {

    checkNotNull(text, "Message to code/decode must not be null!");

    String r = "";
    final int textLength = text.length();

    for (int k = 0; k < textLength; k++) {
      i = i + 1 & 0xff;
      j = j + sbox[i] & 0xff;

      byte x = sbox[i];
      sbox[i] = sbox[j];
      sbox[j] = x;

      r += (char) (text.charAt(k) ^ sbox[sbox[i] + sbox[j] & 0xff] & 0xff);
    }
    return r;
  }
}
