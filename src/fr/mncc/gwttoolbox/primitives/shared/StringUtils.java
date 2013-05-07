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
package fr.mncc.gwttoolbox.primitives.shared;

import com.google.java.contract.Ensures;

/**
 * String parsing with default value on error
 */
public class StringUtils {

  /**
   * Convert a string to an integer
   * 
   * @param str string to convert
   * @return an integer
   */
  public static int parseInt(String str) {
    if (str == null)
      return DefaultValues.intDefaultValue();

    str = str.trim();
    int val = DefaultValues.intDefaultValue();
    try {
      val = Integer.parseInt(str, 10);
    } catch (Exception e) {
    }
    return val;
  }

  /**
   * Convert a string to a long
   * 
   * @param str string to convert
   * @return a long
   */
  public static long parseLong(String str) {
    if (str == null)
      return DefaultValues.longDefaultValue();

    str = str.trim();
    long val = DefaultValues.longDefaultValue();
    try {
      val = Long.parseLong(str, 10);
    } catch (Exception e) {
    }
    return val;
  }

  /**
   * Convert a string to a float
   * 
   * @param str string to convert
   * @return a float
   */
  public static float parseFloat(String str) {
    if (str == null)
      return DefaultValues.floatDefaultValue();

    str = str.trim().replace(',', '.');
    float val = DefaultValues.floatDefaultValue();
    try {
      val = Float.parseFloat(str);
    } catch (Exception e) {
    }
    return val;
  }

  /**
   * Convert a string to a double
   * 
   * @param str string to convert
   * @return a double
   */
  public static double parseDouble(String str) {
    if (str == null)
      return DefaultValues.doubleDefaultValue();

    str = str.trim().replace(',', '.');
    double val = DefaultValues.doubleDefaultValue();
    try {
      val = Double.parseDouble(str);
    } catch (Exception e) {
    }
    return val;
  }

  /**
   * Convert a string to a boolean
   * 
   * @param str string to convert
   * @return a boolean
   */
  public static boolean parseBoolean(String str) {
    if (str == null)
      return DefaultValues.boolDefaultValue();

    str = str.trim();
    boolean val = DefaultValues.boolDefaultValue();
    try {
      val = Boolean.parseBoolean(str);
    } catch (Exception e) {
    }
    return val;
  }

  /**
   * Escape a string for LaTex
   * 
   * @param str string to escape
   * @return escaped string
   */
  @Ensures("result != null")
  public static String escapeToLaTex(String str) {
    return str == null ? "" : str.replaceAll("\\Q\\\\E", "\\\\textbackslash").replaceAll("\\Q{\\E",
        "\\\\{").replaceAll("\\Q_\\E", "\\\\_").replaceAll("\\Q^\\E", "\\\\textasciicircum")
        .replaceAll("\\Q}\\E", "\\\\}").replaceAll("\\Q#\\E", "\\\\#").replaceAll("\\Q&\\E",
            "\\\\&").replaceAll("\\Q$\\E", "\\\\textdollar").replaceAll("\\Q%\\E", "\\\\%")
        .replaceAll("\\Q~\\E", "\\\\textasciitilde").replaceAll("\\Qà\\E", "\\\\`a").replaceAll(
            "\\Qé\\E", "\\\\'e").replaceAll("\\Qè\\E", "\\\\`e");
  }

  /**
   * Replace the first letter of a word with an upper-case letter
   * 
   * @param str
   * @return
   */
  @Ensures("result != null")
  public static String capitalize(String str) {
    return str == null || str.isEmpty() ? "" : str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  /**
   * Replace the first letter of a word with a lower-case letter
   * 
   * @param str
   * @return
   */
  @Ensures("result != null")
  public static String uncapitalize(String str) {
    return str == null || str.isEmpty() ? "" : str.substring(0, 1).toLowerCase() + str.substring(1);
  }

  /**
   * Replace the first letter of each word with an upper-case letter
   * 
   * @param str
   * @return
   */
  @Ensures("result != null")
  public static String capitalizeWords(String str) {
    if (str == null || str.isEmpty())
      return "";

    StringBuilder stringBuilder = new StringBuilder();
    String[] words = str.trim().split(" ");
    for (int i = 0; i < words.length; i++)
      stringBuilder.append((i > 0 ? " " : "") + capitalize(words[i]));
    return stringBuilder.toString();
  }

  /**
   * Replace the first letter of each word with a lower-case letter
   * 
   * @param str
   * @return
   */
  @Ensures("result != null")
  public static String uncapitalizeWords(String str) {
    if (str == null || str.isEmpty())
      return "";

    StringBuilder stringBuilder = new StringBuilder();
    String[] words = str.trim().split(" ");
    for (int i = 0; i < words.length; i++)
      stringBuilder.append((i > 0 ? " " : "") + uncapitalize(words[i]));
    return stringBuilder.toString();
  }
}
