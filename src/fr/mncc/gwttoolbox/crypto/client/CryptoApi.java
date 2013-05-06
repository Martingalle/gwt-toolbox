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
package fr.mncc.gwttoolbox.crypto.client;

import fr.mncc.gwttoolbox.crypto.shared.CubeHash;
import fr.mncc.gwttoolbox.crypto.shared.RandomString;
import fr.mncc.gwttoolbox.crypto.shared.Rc4;

/**
 * Wrapper for common cryptographic methods
 */
public class CryptoApi {

  private final static CubeHash cubeHash_ = new CubeHash();
  private final static Rc4 rc4_ = new Rc4("mncc");
  private final static RandomString rndString_ = new RandomString();

  /**
   * Hash string using CubeHash algorithm http://en.wikipedia.org/wiki/CubeHash
   * 
   * @param text string to hash
   * @return hashed string
   */
  public static String hash(String text) {
    return cubeHash_.hash(text);
  }

  /**
   * Code text after resetting S-box using key. Uses RC4 algorithm :
   * http://en.wikipedia.org/wiki/RC4
   * 
   * @param text to code
   * @param key to reset S-box
   * @return coded text
   */
  public static String code(String text, String key) {
    return rc4_.code(text, key);
  }

  /**
   * Code text using the current S-box
   * 
   * @param text to code
   * @return coded text
   */
  public static String code(String text) {
    return rc4_.code(text);
  }

  /**
   * Decode text after resetting S-box using key. Uses RC4 algorithm :
   * http://en.wikipedia.org/wiki/RC4
   * 
   * @param text to decode
   * @param key to reset S-box
   * @return decoded text
   */
  public static String decode(String text, String key) {
    return rc4_.decode(text, key);
  }

  /**
   * Decode text using the current S-box
   * 
   * @param text to decode
   * @return decoded text
   */
  public static String decode(String text) {
    return rc4_.decode(text);
  }

  /**
   * Return a random string made of upper-case/lower-case letters and digits only
   * 
   * @return random string
   */
  public static String getRandomLettersAndDigits() {
    return rndString_.getLettersAndDigits();
  }

  /**
   * Return a random string made of upper-case/lower-case letters only
   * 
   * @return random string
   */
  public static String getRandomLettersOnly() {
    return rndString_.getLettersOnly();
  }

  /**
   * Return a random string made of digits only
   * 
   * @return random string
   */
  public static String getRandomDigitsOnly() {
    return rndString_.getDigitsOnly();
  }
}
