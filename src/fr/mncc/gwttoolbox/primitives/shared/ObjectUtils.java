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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Easy serialization / de-serialization of a few basic types
 */
public class ObjectUtils {

  private static final int TYPE_STRING = 1;
  private static final int TYPE_INTEGER = 2;
  private static final int TYPE_FLOAT = 3;
  private static final int TYPE_DOUBLE = 4;
  private static final int TYPE_BOOLEAN = 5;
  private static final int TYPE_DATE = 6;
  private static final int TYPE_TIME = 7;
  private static final int TYPE_LONG = 8;
  private static final int TYPE_TIMESTAMP = 9;

  /*
   * Convert a string into an object
   * 
   * @param str string
   * 
   * @return Object representation of str, null if str cannot be de-serialized
   */
  public static Object fromString(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    return index < 0 ? null : getObject(StringUtils.parseInt(str.substring(0, index)), str
        .substring(index + 1, str.length()));
  }

  /**
   * Convert an object into a string without losing type
   * 
   * @param obj object
   * @return String representation of obj, empty string if obj cannot be serialized
   */
  @Ensures("result != null")
  public static String toString(Object obj) {
    return isValid(obj) ? getType(obj) + ":" + getString(obj) : "";
  }

  /**
   * Whatever the serialized object is, return it as a string
   * 
   * @param str serialized object
   * @return the object part of the serialized string, empty string if str is not a valid serialized
   *         string
   */
  @Ensures("result != null")
  public static String asString(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    return index < 0 ? "" : str.substring(index + 1);
  }

  /**
   * @param str serialized object
   * @return true if str is an Integer, false otherwise
   */
  public static boolean isInteger(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_INTEGER;
  }

  /**
   * @param str serialized object
   * @return true if str is a Long, false otherwise.
   */
  public static boolean isLong(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_LONG;
  }

  /**
   * @param str serialized object
   * @return true if str is a Double, false otherwise.
   */
  public static boolean isDouble(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_DOUBLE;
  }

  /**
   * @param str serialized object
   * @return true if str is a Float, false otherwise.
   */
  public static boolean isFloat(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_FLOAT;
  }

  /**
   * @param str serialized object
   * @return true if str is a Boolean, false otherwise.
   */
  public static boolean isBoolean(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_BOOLEAN;
  }

  /**
   * @param str serialized object
   * @return true if str is a String, false otherwise.
   */
  public static boolean isString(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_STRING;
  }

  /**
   * @param str serialized object
   * @return true if str is a timestamp, false otherwise.
   */
  public static boolean isTimeStamp(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_TIMESTAMP;
  }

  /**
   * @param str serialized object
   * @return true if str is a time, false otherwise.
   */
  public static boolean isTime(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_TIME;
  }

  /**
   * @param str serialized object
   * @return true if str is a date, false otherwise.
   */
  public static boolean isRealDate(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_DATE;
  }

  /**
   * @param str serialized object
   * @return true if str is a timestamp, a time or a date, false otherwise
   */
  public static boolean isDate(String str) {
    int index = str == null ? -1 : str.indexOf(':');
    int type = StringUtils.parseInt(str.substring(0, index));
    return type == TYPE_TIMESTAMP || type == TYPE_TIME || type == TYPE_DATE;
  }

  /**
   * Get an integer describing an object type
   * 
   * @param obj object
   * @return object type as an integer
   */
  private static int getType(Object obj) {
    if (obj instanceof Integer)
      return TYPE_INTEGER;
    else if (obj instanceof Long)
      return TYPE_LONG;
    else if (obj instanceof Double)
      return TYPE_DOUBLE;
    else if (obj instanceof Float)
      return TYPE_FLOAT;
    else if (obj instanceof Boolean)
      return TYPE_BOOLEAN;
    else if (obj instanceof String)
      return TYPE_STRING;
    else if (obj instanceof Timestamp) // Timestamp must be before Date because Timestamp extends
                                       // Date!
      return TYPE_TIMESTAMP;
    else if (obj instanceof Time) // Time must be before Date because Time extends Date!
      return TYPE_TIME;
    else if (obj instanceof Date)
      return TYPE_DATE;
    return 0;
  }

  /**
   * Serialize an object
   * 
   * @param obj object
   * @return serialized object as string
   */
  @SuppressWarnings("deprecation")
  private static String getString(Object obj) {
    if (obj instanceof Integer)
      return Integer.toString((Integer) obj, 10);
    else if (obj instanceof Long)
      return Long.toString((Long) obj, 10);
    else if (obj instanceof Double)
      return Double.toString((Double) obj);
    else if (obj instanceof Float)
      return Float.toString((Float) obj);
    else if (obj instanceof Boolean)
      return Boolean.toString((Boolean) obj);
    else if (obj instanceof String)
      return (String) obj;
    else if (obj instanceof Timestamp) // Timestamp must be before Date because Timestamp extends
                                       // Date!
      return dateToString((Timestamp) obj);
    else if (obj instanceof Time) { // Time must be before Date because Time extends Date!
      Time time = new Time(0);
      time.setHours(((Time) obj).getHours());
      time.setMinutes(((Time) obj).getMinutes());
      time.setSeconds(((Time) obj).getSeconds());
      return dateToString(time);
    } else if (obj instanceof Date) {
      Date date = new Date(0);
      date.setDate(((Date) obj).getDate());
      date.setMonth(((Date) obj).getMonth());
      date.setYear(((Date) obj).getYear());
      return dateToString(date);
    }
    return "";
  }

  /**
   * Deserialize an object
   * 
   * @param type object's type
   * @param value object
   * @return object
   */
  private static Object getObject(int type, String value) {
    if (type == TYPE_INTEGER)
      return StringUtils.parseInt(value);
    else if (type == TYPE_LONG)
      return StringUtils.parseLong(value);
    else if (type == TYPE_DOUBLE)
      return StringUtils.parseDouble(value);
    else if (type == TYPE_FLOAT)
      return StringUtils.parseFloat(value);
    else if (type == TYPE_BOOLEAN)
      return StringUtils.parseBoolean(value);
    else if (type == TYPE_STRING)
      return value;
    else if (type == TYPE_TIMESTAMP) // Timestamp must be before Date because Timestamp extends
                                     // Date!
      return new Timestamp(dateFromString(value).getTime());
    else if (type == TYPE_TIME) // Time must be before Date because Time extends Date!
      return new Time(dateFromString(value).getTime());
    else if (type == TYPE_DATE)
      return dateFromString(value);
    return null;
  }

  /**
   * Check if a given object is serializable
   * 
   * @param obj object
   * @return tru if object is serializable, false otherwise
   */
  private static boolean isValid(Object obj) {
    if (obj instanceof Integer)
      return true;
    else if (obj instanceof Long)
      return true;
    else if (obj instanceof Double)
      return true;
    else if (obj instanceof Float)
      return true;
    else if (obj instanceof Boolean)
      return true;
    else if (obj instanceof String)
      return true;
    else if (obj instanceof Timestamp) // Timestamp must be before Date because Timestamp extends
                                       // Date!
      return true;
    else if (obj instanceof Time) // Time must be before Date because Time extends Date!
      return true;
    else if (obj instanceof Date)
      return true;
    else if (obj instanceof List)
      return true;
    else if (obj instanceof Map)
      return true;
    return false;
  }

  @SuppressWarnings("deprecation")
  private static String dateToString(Date date) {
    if (date == null)
      return dateToString(DefaultValues.timestampDefaultValue());
    return new Timestamp(date.getTime()).toGMTString();
  }

  @SuppressWarnings("deprecation")
  private static Date dateFromString(String str) {
    if (str == null)
      return DefaultValues.timestampDefaultValue();
    return new Timestamp(Timestamp.parse(str));
  }
}
