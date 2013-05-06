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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Ordered key-value map. Allows you to split the following string
 * 
 * "param1=value1&param2=value2&...&paramn=valuen"
 * 
 * into pairs
 * 
 * {(param1,value1),(param2,value2),...,(paramn,valuen)}
 */
public class KeyValueMap extends LinkedHashMap<String, String> {

  public KeyValueMap() {
    this("");
  }

  public KeyValueMap(String params) {
    initialize(params);
  }

  private void initialize(String params) {
    clear();

    if (params == null || params.isEmpty())
      return;

    String args[] = params.split("&");
    for (String element : args) {
      int equalIndex = element.indexOf("=");
      if (equalIndex < 0)
        put(element, "");
      else
        put(element.substring(0, equalIndex).trim(), element.substring(equalIndex + 1).trim());
    }
  }

  /**
   * Convert map to an ordered list of key-value pairs
   * 
   * @return a list of KeyValuePair objects
   */
  @Ensures("result != null")
  public List<KeyValuePair<String, String>> toPairs() {
    List<KeyValuePair<String, String>> list = new ArrayList<KeyValuePair<String, String>>();
    for (String key : keySet())
      list.add(new KeyValuePair<String, String>(key, get(key)));
    return list;
  }

  /**
   * Convert map to JSON string
   * 
   * @return map as JSON string
   */
  @Ensures("result != null")
  public String toJson() {
    String result = "{";
    String separator = "";
    for (String key : keySet()) {
      result += (separator + "\"" + key + "\":\"" + get(key) + "\"");
      separator = ",";
    }
    return result + "}";
  }

  @Override
  public String toString() {
    String result = "";
    String separator = "";
    for (String key : keySet()) {
      result += (separator + key + "=" + get(key));
      separator = "&";
    }
    return result;
  }
}
