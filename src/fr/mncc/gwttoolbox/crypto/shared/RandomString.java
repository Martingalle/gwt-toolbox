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

import java.util.Random;


public class RandomString {

  private final int out_length = 32;

  /**
   * Return a random string made of upper-case/lower-case letters and digits only
   * 
   * @return random string
   */
  public String getLettersAndDigits() {
    final Random rng = new Random();
    final String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    char[] text = new char[out_length];
    for (int i = 0; i < out_length; i++)
      text[i] = characters.charAt(rng.nextInt(characters.length()));
    return new String(text);
  }

  /**
   * Return a random string made of upper-case/lower-case letters only
   * 
   * @return random string
   */
  public String getLettersOnly() {
    final Random rng = new Random();
    final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    char[] text = new char[out_length];
    for (int i = 0; i < out_length; i++)
      text[i] = characters.charAt(rng.nextInt(characters.length()));
    return new String(text);
  }

  /**
   * Return a random string made of digits only
   * 
   * @return random string
   */
  public String getDigitsOnly() {
    final Random rng = new Random();
    final String characters = "0123456789";
    char[] text = new char[out_length];
    for (int i = 0; i < out_length; i++)
      text[i] = characters.charAt(rng.nextInt(characters.length()));
    return new String(text);
  }
}
