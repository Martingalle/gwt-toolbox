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

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.java.contract.Ensures;
import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

@Invariant({"properties_ != null"})
public class Entity implements Comparable<Entity>, Serializable, IsSerializable, HasId,
    HasTimestamp {

  private List<String> properties_ = new ArrayList<String>(); // List of properties (KEY:TYPE:VALUE)

  protected Entity() {

  }

  @Requires("entity != null")
  @Ensures({"getKind().equals(entity.getKind())", "getId() == entity.getId()"})
  public Entity(Entity entity) {
    this(entity.getKind(), entity.getId(), entity.getProperties());
    setTimestamp();
  }

  @Requires("kind != null")
  @Ensures("getKind().equals(kind)")
  public Entity(String kind) {
    setKind(kind);
    setTimestamp();
  }

  @Requires({"kind != null", "id >= 0"})
  @Ensures({"getKind().equals(kind)", "getId() == id"})
  public Entity(String kind, long id) {
    setKind(kind);
    setId(id);
    setTimestamp();
  }

  @Requires({"kind != null", "id >= 0", "properties != null"})
  @Ensures({"getKind().equals(kind)", "getId() == id"})
  public Entity(String kind, long id, List<String> properties) {
    setId(id);
    setKind(kind);
    setProperties(new ArrayList<String>(properties));
    setTimestamp();
  }

  private static String createProperty(String key, Object value) {
    return key + ":" + ObjectUtils.toString(value);
  }

  private static String getKey(String property) {
    int index = property.indexOf(":");
    if (index < 0)
      return "";
    return property.substring(0, index);
  }

  private static String getValueAsString(String property) {
    int index = property.indexOf(":");
    if (index < 0)
      return "";
    return property.substring(index + 1);
  }

  private static Object getValueAsObject(String property) {
    return property == null ? null : ObjectUtils.fromString(getValueAsString(property));
  }

  private static boolean listEquals(List<String> l1, List<String> l2) {
    for (String s1 : l1) {
      boolean contains = false;
      for (String s2 : l2) {
        if (s1.equals(s2)) {
          contains = true;
          break;
        }
      }
      if (!contains)
        return false;
    }
    for (String s2 : l2) {
      boolean contains = false;
      for (String s1 : l1) {
        if (s2.equals(s1)) {
          contains = true;
          break;
        }
      }
      if (!contains)
        return false;
    }
    return true;
  }

  /**
   * Get the timestamp value from the list of properties of an entity
   * 
   * @param properties - list of properties of an entity
   * @return the timestamp value from the list of properties of an entity or <b>null</b> if the list
   *         of properties doesn't contain any timestamp value
   */
  private static Date getTimestampValue(List<String> properties) {

    for (int i = 0; i < properties.size(); i++) {
      String string = properties.get(i); // properties (KEY:TYPE:VALUE)
      String[] key = string.split(":", 3);
      if (key[0].equals("__timestamp__")) {
        DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate = null;
        // System.out.println(key[2].split("\\.")[0]);
        parsedDate = dateFormat.parse(key[2].split("\\.")[0]);
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        return parsedDate;
      }
    }

    return null;
  }

  /**
   * Removes the timestamp value from the list.
   * 
   * @param list - the list which may contain a timestamp value
   */
  private static List<String> removeTimestampValue(List<String> list) {
    for (int i = 0; i < list.size(); i++) {
      String str = list.get(i);
      if (str.startsWith("__timestamp__")) {
        list.remove(i);
      }
    }

    return list;
  }

  /**
   * Returns a list that contains the same property value from 2 entities<br/>
   * The properties have same type and value
   * 
   * @param entity1 - an entity
   * @param entity2 - an entity
   * @return
   */
  private static List<String> getSameTypeAndValueOfProperties(Entity entity1, Entity entity2) {
    List<String> list = new ArrayList<String>();

    int size1 = entity1.getProperties().size();
    int size2 = entity2.getProperties().size();
    for (int i = 0; i < size1; i++) {
      String propertyValue1 = entity1.getProperties().get(i);
      for (int j = 0; j < size2; j++) {
        String propertyValue2 = entity2.getProperties().get(j);
        if (propertyValue1.equals(propertyValue2) && !propertyValue1.startsWith("__timestamp__")) {
          list.add(propertyValue1);
        }
      }
    }

    return list;
  }

  /**
   * TODO: improve comment<br/>
   * Returns a {@code List<String>} that contains the difference between the properties of the first
   * and the second entities and vice-versa. <br/>
   * 
   * This method checks the with properties of the first entity that are not present in the second
   * entity and returns these entities. <br />
   * 
   * If the kind or id of the two entities are different, this method returns a an empty
   * {@code List<String>}
   * 
   * 
   * @param entity1 - an entity
   * @param entity2 - an entity
   * @return
   */
  private static List<String> diff(Entity entity1, Entity entity2) {
    if (!entity1.getKind().equals(entity2.getKind())) {
      return new ArrayList<String>();
    }

    if (entity1.getId() != entity2.getId()) {
      return new ArrayList<String>();
    }

    // it does not matter which properties of entity1 or entity is chosen I take because 2 diffs
    // have to be calculated which would then be used in the priority diff method
    // e.g diff(entity1, entity2) and diff(entity2, entity) have to be calculated
    List<String> properties = entity1.getProperties();

    List<String> diffArrayList = new ArrayList<String>();

    diffArrayList = getSameTypeAndValueOfProperties(entity1, entity2);
    ArrayList<String> arrayList = new ArrayList<String>(properties);
    arrayList.removeAll(diffArrayList);

    diffArrayList.clear();
    diffArrayList.addAll(arrayList);
    System.out.println("diff =" + diffArrayList);

    return diffArrayList;
  }

  /**
   * Finds the difference between the properties of two entities and chooses the one with the latest
   * current timestamp
   * 
   * @param list1 - list of properties of the first entity
   * @param list2 - list of properties of the second entity
   * @return
   */
  private static List<String> priorityDiff(List<String> list1, List<String> list2) {

    Date timestamp1 = getTimestampValue(list1);
    Date timestamp2 = getTimestampValue(list2);

    List<String> diff = new ArrayList<String>();
    int size1 = list1.size();
    int size2 = list2.size();

    String property1 = "", property2 = "";
    if (size1 == 0 && size2 == 0) {
      return new ArrayList<String>();
    }

    if (size1 >= size2) {
      System.out.println("size1 >= size2");
      for (int i = 0; i < size1; i++) {
        boolean isInserted = false;
        property1 = list1.get(i);
        int index = property1.lastIndexOf(":");
        String str1 = property1.substring(0, index);

        for (int j = 0; j < size2; j++) {
          property2 = list2.get(j);
          index = property2.lastIndexOf(":");

          String str2 = property2.substring(0, index);

          if (str1.equals(str2)) {
            if (!property1.equals(property2)) {
              // System.out.println(property1 + "\t\t" + property2);
              if (timestamp1.after(timestamp2)) {
                isInserted = diff.add(property1);
              } else {
                diff.add(property2);
              }
            }
          }
        }
        // System.out.println(bool);
        if (!isInserted && timestamp1.after(timestamp2)) {
          // System.out.println("entered here & " + property1);
          diff.add(property1);
        }
      }
    } else {
      System.out.println("size1 < size2");
      for (int i = 0; i < size2; i++) {
        boolean isInserted = false;
        property2 = list2.get(i);
        int index = property2.lastIndexOf(":");
        String str1 = property2.substring(0, index);
        for (int j = 0; j < size1; j++) {
          property1 = list2.get(j);
          index = property1.lastIndexOf(":");

          String str2 = property1.substring(0, index);
          if (str1.equals(str2)) {
            if (!property2.equals(property1)) {
              if (timestamp2.after(timestamp1)) {
                isInserted = diff.add(property2);
              } else {
                diff.add(property1);
              }
            }
          }
        }
        if (!isInserted && timestamp2.after(timestamp1)) {
          // System.out.println("entered here & " + property1);
          diff.add(property2);
        }
      }
    }

    System.out.println("pdiff =" + diff);
    return diff;
  }

  /**
   * Returns a list that contains the properties of 2 entities that has the same type and value
   * 
   * @param entity1 - an entity
   * @param entity2 - an entity
   * @return
   */
  private static Entity commonProperties(Entity entity1, Entity entity2) {
    if (!entity1.getKind().equals(entity2.getKind())) {
      return new Entity(entity1.getKind());
    }

    if (entity1.getId() != entity2.getId()) {
      return new Entity(entity1.getKind());
    }

    List<String> properties = getSameTypeAndValueOfProperties(entity1, entity2);
    // System.out.println("common properties =" + samePropertiesList);

    Entity entity = new Entity(entity1.getKind(), entity1.getId(), properties);

    return entity;
  }

  /**
   * Create an entity from the a diff property list and an entity
   * 
   * @param priorityDiff
   * @param commonProperties
   * @return
   */
  private static Entity createEntityOf(List<String> priorityDiff, Entity e) {

    if (e.getId() == 0) {
      return new Entity(e.getKind());
    }

    List<String> properties = new ArrayList<String>();

    priorityDiff = removeTimestampValue(priorityDiff);
    properties.addAll(priorityDiff);
    properties.addAll(e.getProperties());

    Entity entity = new Entity(e.getKind(), e.getId(), properties);

    return entity;
  }

  public static Entity createUpdatedEntityFrom(Entity newEntity, Entity oldEntity) {
    List<String> diff1 = Entity.diff(newEntity, oldEntity);
    List<String> diff2 = Entity.diff(oldEntity, newEntity);

    List<String> pDiff = Entity.priorityDiff(diff1, diff2);
    Entity commonEntity = Entity.commonProperties(newEntity, oldEntity);

    Entity updatedEntity = Entity.createEntityOf(pDiff, commonEntity);

    return updatedEntity;
  }

  @Ensures("result >= 0")
  @Override
  public long getId() {
    return getAsLong("__id__");
  }

  @Requires("id >= 0")
  @Ensures("getId() == id")
  public void setId(long id) {
    put("__id__", id);
  }

  @Override
  public Timestamp getTimestamp() {
    return getAsTimestamp("__timestamp__");
  }

  public void setTimestamp() {
    put("__timestamp__", new Timestamp(new Date().getTime()));
  }

  @Ensures("result != null")
  public List<String> getProperties() {
    return new ArrayList<String>(properties_);
  }

  @Requires("properties != null")
  @Ensures("getProperties() == properties")
  private void setProperties(List<String> properties) {
    properties_ = properties;
  }

  @Ensures("result != null")
  public Set<String> keySet() {
    Set<String> set = new HashSet<String>();
    for (String property : properties_)
      set.add(getKey(property));
    return set;
  }

  @Requires({
      "propertyName != null", "propertyValue != null",
      "!ObjectUtils.toString(propertyValue).equals(ObjectUtils.toString(null))"})
  @Ensures({
      "properties_.containsKey(propertyName)",
      "ObjectUtils.toString(getAsObject(propertyName)).equals(ObjectUtils.toString(propertyValue))"})
  public void put(String propertyName, Object propertyValue) {
    remove(propertyName);
    properties_.add(createProperty(propertyName, propertyValue));
  }

  @Requires("propertyName != null")
  @Ensures({"!properties_.containsKey(propertyName)", "getAsObject(propertyName) == null"})
  public void remove(String propertyName) {
    String property = get(propertyName);
    if (property != null)
      properties_.remove(property);
  }

  public Object getAsObject(String propertyName) {
    if (propertyName == null)
      return null;
    String property = get(propertyName);
    if (property == null)
      return null;
    return getValueAsObject(property);
  }

  @Requires("propertyName != null")
  @Ensures("result != null")
  public String getAsString(String propertyName) {
    if (propertyName == null)
      return "";
    String property = get(propertyName);
    if (property == null)
      return "";
    return ObjectUtils.asString(getValueAsString(property));
  }

  @Requires("propertyName != null")
  public int getAsInt(String propertyName) {
    Object object = getAsObject(propertyName);
    return object instanceof Integer ? (Integer) object : object instanceof Long ? ((Long) object)
        .intValue() : object instanceof Double ? ((Double) object).intValue()
        : object instanceof Float ? ((Float) object).intValue() : object instanceof String
            ? StringUtils.parseInt((String) object) : DefaultValues.intDefaultValue();
  }

  @Requires("propertyName != null")
  public long getAsLong(String propertyName) {
    Object object = getAsObject(propertyName);
    return object instanceof Long ? (Long) object : object instanceof Integer ? ((Integer) object)
        .longValue() : object instanceof Double ? ((Double) object).longValue()
        : object instanceof Float ? ((Float) object).longValue() : object instanceof String
            ? StringUtils.parseLong((String) object) : DefaultValues.longDefaultValue();
  }

  @Requires("propertyName != null")
  public double getAsDouble(String propertyName) {
    Object object = getAsObject(propertyName);
    return object instanceof Double ? (Double) object : object instanceof Float ? ((Float) object)
        .doubleValue() : object instanceof Long ? ((Long) object).doubleValue()
        : object instanceof Integer ? ((Integer) object).doubleValue() : object instanceof String
            ? StringUtils.parseDouble((String) object) : DefaultValues.doubleDefaultValue();
  }

  @Requires("propertyName != null")
  public float getAsFloat(String propertyName) {
    Object object = getAsObject(propertyName);
    return object instanceof Float ? (Float) object : object instanceof Double ? ((Double) object)
        .floatValue() : object instanceof Long ? ((Long) object).floatValue()
        : object instanceof Integer ? ((Integer) object).floatValue() : object instanceof String
            ? StringUtils.parseFloat((String) object) : DefaultValues.floatDefaultValue();
  }

  @Requires("propertyName != null")
  public boolean getAsBoolean(String propertyName) {
    Object object = getAsObject(propertyName);
    return object instanceof Boolean ? (Boolean) object : object instanceof String ? StringUtils
        .parseBoolean((String) object) : DefaultValues.boolDefaultValue();
  }

  @Requires("propertyName != null")
  public Date getAsDate(String propertyName) {
    Object object = getAsObject(propertyName);
    return object instanceof Timestamp ? new Date(((Timestamp) object).getTime())
        : object instanceof Time ? new Date(((Time) object).getTime()) : object instanceof Date
            ? (Date) object : DefaultValues.dateDefaultValue();

  }

  @Requires("propertyName != null")
  public Time getAsTime(String propertyName) {
    Object object = getAsObject(propertyName);
    return object instanceof Timestamp ? new Time(((Timestamp) object).getTime())
        : object instanceof Time ? (Time) object : object instanceof Date ? new Time(
            ((Date) object).getTime()) : DefaultValues.timeDefaultValue();
  }

  @Requires("propertyName != null")
  public Timestamp getAsTimestamp(String propertyName) {
    Object object = getAsObject(propertyName);
    return object instanceof Timestamp ? (Timestamp) object : object instanceof Time
        ? new Timestamp(((Time) object).getTime()) : object instanceof Date ? new Timestamp(
            ((Date) object).getTime()) : DefaultValues.timestampDefaultValue();
  }

  @Ensures("result != null")
  public String getKind() {
    return getAsString("__kind__");
  }

  @Requires("kind != null")
  @Ensures("getKind().equals(kind)")
  private void setKind(String kind) {
    put("__kind__", kind);
  }

  private String get(String propertyName) {
    propertyName += ":";
    for (String property : properties_) {
      if (property.startsWith(propertyName))
        return property;
    }
    return null;
  }

  private boolean containsKey(String propertyName) {
    propertyName += ":";
    for (String property : properties_) {
      if (property.startsWith(propertyName))
        return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId(), getKind(), properties_);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Entity))
      return false;

    Entity entity = (Entity) o;
    return Objects.equal(getId(), entity.getId()) && Objects.equal(getKind(), entity.getKind())
        && listEquals(properties_, entity.properties_);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("properties_", properties_).toString();
  }

  @Override
  public int compareTo(Entity entity) {
    return ComparisonChain.start().compare(getId(), entity.getId()).compare(getKind(),
        entity.getKind()).result();
  }
}
