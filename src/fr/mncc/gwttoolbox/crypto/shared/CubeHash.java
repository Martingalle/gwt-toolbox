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

import java.util.Stack;

/**
 * Cryptographic Hash Function : http://en.wikipedia.org/wiki/CubeHash
 */
public class CubeHash {

  private final int out_length = 256;

  private void plusRotate(final int[] state, final int r, final int s) {
    for (int i = 0; i < 16; i += 1) {
      state[16 + i] += state[i];
      state[i] = (state[i] << r) ^ (state[i] >>> s);
    }
  }

  private void swapXorSwap(final int[] state, final int mask1, final int mask2) {
    for (int i = 0; i < 16; i += 1) {
      if ((i & mask1) != 0) {
        int j = i ^ mask1;
        int tmp = state[i] ^ state[j + 16];
        state[i] = state[j] ^ state[i + 16];
        state[j] = tmp;
      }
    }
    for (int i = 16; i < 32; i += 1) {
      if ((i & mask2) != 0) {
        int j = i ^ mask2;
        int tmp = state[i];
        state[i] = state[j];
        state[j] = tmp;
      }
    }
  }

  private void round(final int[] state, int n) {
    n *= 16;
    for (int r = 0; r < n; r += 1) {
      plusRotate(state, 7, 25);
      swapXorSwap(state, 8, 2);
      plusRotate(state, 11, 21);
      swapXorSwap(state, 4, 1);
    }
  }

  /**
   * Output formatting function, giving the little-endian hex display of a number
   * 
   * @param n number to format
   * @return formatted number
   */
  private String hex(final Integer n) {
    String hex = "00" + Integer.toHexString(n);
    return hex.substring(hex.length() - 2, hex.length());
  }

  private String formatOutput(final Integer n) {
    return hex(n & 255) + hex(n >>> 8) + hex(n >>> 16) + hex(n >>> 24);
  }

  /**
   * Hash string using CubeHash algorithm http://en.wikipedia.org/wiki/CubeHash
   * 
   * @param str string to hash
   * @return hashed string
   */
  public String hash(String str) {

    checkNotNull(str, "String to hash must not be null!");

    int state[] =
        new int[] {
            out_length / 8, 32, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0};
    round(state, 10);

    str += "\u0080";
    while (str.length() % 16 > 0)
      str += "\u0000";

    final Stack<Integer> input = new Stack<Integer>();
    for (int i = 0; i < str.length(); i += 2)
      input.push(str.charAt(i) + str.charAt(i + 1) * 0x10000);

    for (int block = 0; block < input.size(); block += 8) {
      for (int i = 0; i < 8; i++)
        state[i] ^= input.get(block + i);
      round(state, 1);
    }

    state[31] ^= 1;
    round(state, 10);

    String hash = "";
    for (int i = 0; i < state.length; i++)
      hash = hash + formatOutput(state[i]);

    return hash.substring(0, out_length / 4);
  }
}
