/**
 * Copyright (c) 2013 MNCC
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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.GwtTransient;

public class EntityEx extends Entity {

  private static final int TYPE_STRING = 1;
  private static final int TYPE_INTEGER = 2;
  private static final int TYPE_FLOAT = 3;
  private static final int TYPE_DOUBLE = 4;
  private static final int TYPE_BOOLEAN = 5;
  private static final int TYPE_DATE = 6;
  private static final int TYPE_TIME = 7;
  private static final int TYPE_LONG = 8;
  private static final int TYPE_TIMESTAMP = 9;

  @GwtTransient
  private final HashMap<String, Object> propertiesAsObject_ = new HashMap<String, Object>();

  protected EntityEx() {
    super();
  }

  public EntityEx(EntityEx entity) {
    super(entity.getKind(), entity.getId(), entity.getProperties());
  }

  public EntityEx(String kind) {
    super(kind);
  }

  public EntityEx(String kind, long id) {
    super(kind, id);
  }

  public EntityEx(String kind, long id, List<String> properties) {
    super(kind, id, properties);
  }

  @Override
  public void put(String propertyName, Object propertyValue) {
    propertiesAsObject_.put(propertyName, propertyValue);
    super.put(propertyName, propertyValue);
  }

  @Override
  public void remove(String propertyName) {
    propertiesAsObject_.remove(propertyName);
    propertiesAsObject_.remove(propertyName + TYPE_BOOLEAN);
    propertiesAsObject_.remove(propertyName + TYPE_DATE);
    propertiesAsObject_.remove(propertyName + TYPE_DOUBLE);
    propertiesAsObject_.remove(propertyName + TYPE_TIME);
    propertiesAsObject_.remove(propertyName + TYPE_TIMESTAMP);
    propertiesAsObject_.remove(propertyName + TYPE_FLOAT);
    propertiesAsObject_.remove(propertyName + TYPE_INTEGER);
    propertiesAsObject_.remove(propertyName + TYPE_LONG);
    propertiesAsObject_.remove(propertyName + TYPE_STRING);
    super.remove(propertyName);
  }

  @Override
  public Object getAsObject(String propertyName) {
    if (propertyName == null)
      return null;

    if (propertiesAsObject_.containsKey(propertyName))
      return propertiesAsObject_.get(propertyName);

    Object object = super.getAsObject(propertyName);
    if (object != null)
      propertiesAsObject_.put(propertyName, object);
    return object;
  }

  @Override
  public String getAsString(String propertyName) {

    String propertyNameString = propertyName + TYPE_STRING;
    if (propertiesAsObject_.containsKey(propertyNameString))
      return (String) propertiesAsObject_.get(propertyNameString);

    String val = super.getAsString(propertyName);
    propertiesAsObject_.put(propertyNameString, val);
    return val;
  }

  @Override
  public int getAsInt(String propertyName) {

    String propertyNameInt = propertyName + TYPE_INTEGER;
    if (propertiesAsObject_.containsKey(propertyNameInt))
      return (Integer) propertiesAsObject_.get(propertyNameInt);

    int val = super.getAsInt(propertyName);
    propertiesAsObject_.put(propertyNameInt, val);
    return val;
  }

  @Override
  public long getAsLong(String propertyName) {

    String propertyNameLong = propertyName + TYPE_LONG;
    if (propertiesAsObject_.containsKey(propertyNameLong))
      return (Long) propertiesAsObject_.get(propertyNameLong);

    long val = super.getAsLong(propertyName);
    propertiesAsObject_.put(propertyNameLong, val);
    return val;
  }

  @Override
  public double getAsDouble(String propertyName) {

    String propertyNameDouble = propertyName + TYPE_DOUBLE;
    if (propertiesAsObject_.containsKey(propertyNameDouble))
      return (Double) propertiesAsObject_.get(propertyNameDouble);

    double val = super.getAsDouble(propertyName);
    propertiesAsObject_.put(propertyNameDouble, val);
    return val;
  }

  @Override
  public float getAsFloat(String propertyName) {

    String propertyNameFloat = propertyName + TYPE_FLOAT;
    if (propertiesAsObject_.containsKey(propertyNameFloat))
      return (Float) propertiesAsObject_.get(propertyNameFloat);

    float val = super.getAsFloat(propertyName);
    propertiesAsObject_.put(propertyNameFloat, val);
    return val;
  }

  @Override
  public boolean getAsBoolean(String propertyName) {

    String propertyNameBoolean = propertyName + TYPE_BOOLEAN;
    if (propertiesAsObject_.containsKey(propertyNameBoolean))
      return (Boolean) propertiesAsObject_.get(propertyNameBoolean);

    boolean val = super.getAsBoolean(propertyName);
    propertiesAsObject_.put(propertyNameBoolean, val);
    return val;
  }

  @Override
  public Date getAsDate(String propertyName) {

    String propertyNameDate = propertyName + TYPE_DATE;
    if (propertiesAsObject_.containsKey(propertyNameDate))
      return (Date) propertiesAsObject_.get(propertyNameDate);

    Date val = super.getAsDate(propertyName);
    propertiesAsObject_.put(propertyNameDate, val);
    return val;
  }

  @Override
  public Time getAsTime(String propertyName) {

    String propertyNameTime = propertyName + TYPE_TIME;
    if (propertiesAsObject_.containsKey(propertyNameTime))
      return (Time) propertiesAsObject_.get(propertyNameTime);

    Time val = super.getAsTime(propertyName);
    propertiesAsObject_.put(propertyNameTime, val);
    return val;
  }

  @Override
  public Timestamp getAsTimestamp(String propertyName) {

    String propertyNameTimestamp = propertyName + TYPE_TIMESTAMP;
    if (propertiesAsObject_.containsKey(propertyNameTimestamp))
      return (Timestamp) propertiesAsObject_.get(propertyNameTimestamp);

    Timestamp val = super.getAsTimestamp(propertyName);
    propertiesAsObject_.put(propertyNameTimestamp, val);
    return val;
  }
}
