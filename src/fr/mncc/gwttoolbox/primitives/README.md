gwt-primitives
==============

Miscellaneous stuff related to data types.

Dependencies
============

* [Google Guava](http://code.google.com/p/guava-libraries/) 13.0 or above
* [Cofoja](https://code.google.com/p/cofoja/) 1.0-r139

What is inside ?
================

Shared :
* Convert a byte array to a base 64 string and vice versa : 
 * [fr.mncc.gwttoolbox.primitives.shared.Base64Utils](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/Base64Utils.java)
* Default values for Java data types in order to ensure consistency across a whole project : 
 * [fr.mncc.gwttoolbox.primitives.shared.DefaultValues](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/DefaultValues.java)
* Generic HashMap with built-in serialization/deserialization to/from string with type preservation : 
 * [fr.mncc.gwttoolbox.primitives.shared.Entity](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/Entity.java)
* Wrapper around the [Entity](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/Entity.java) object in order to avoid a deserialization each time a getAsXxx() method is called :
 * [fr.mncc.gwttoolbox.primitives.shared.EntityEx](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/EntityEx.java)
* Ordered key-value map for easy parsing of strings of type "param1=value1&param2=value2&...&paramn=valuen" :
 * [fr.mncc.gwttoolbox.primitives.shared.KeyValueMap](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/KeyValueMap.java)
* Simple class to manage key-value pairs : 
 * [fr.mncc.gwttoolbox.primitives.shared.KeyValuePair](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/KeyValuePair.java)
* Easy serialization/deserialization of objects to/from strings of a few basic Java data types : 
 * [fr.mncc.gwttoolbox.primitives.shared.ObjectUtils](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/ObjectUtils.java)
* Simple class to manage table paging : 
 * [fr.mncc.gwttoolbox.primitives.shared.Page](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/Page.java)
* In an async environment, add id to requests in order to be able to process results in the right order : 
 * [fr.mncc.gwttoolbox.primitives.shared.SerializableObject](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/SerializableObject.java)
* Miscellaneous utils for processing strings : 
 * [fr.mncc.gwttoolbox.primitives.shared.StringUtils](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/shared/StringUtils.java)

Client :
* Miscellaneous utils for serialization/deserialization of dates to/from strings : 
 * [fr.mncc.gwttoolbox.primitives.client.DateUtils](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/client/DateUtils.java)
* Simple JSON parser/encoder : 
 * [fr.mncc.gwttoolbox.primitives.client.JsonParser](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/client/JsonParser.java)

Server :
* Miscellaneous utils for serialization/deserialization of dates to/from strings : 
 * [fr.mncc.gwttoolbox.primitives.server.DateUtils](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives/server/DateUtils.java)

Example
=======

```java
public class ContactDTO extends JavaScriptObject {

    public static ContactDTO create() { return (ContactDTO) JavaScriptObject.createObject().cast(); }

    protected ContactDTO() { }
    public final native long getId() /*-{ return this.id; }-*/;
    public final native void setId(long id) /*-{ this.id = id; }-*/;
    public final native String getName() /*-{ return this.name; }-*/;
    public final native void setName(String name) /*-{ this.name = name; }-*/;
    public final native int getAge() /*-{ return this.age; }-*/;
    public final native void setAge(int age) /*-{ this.age = age; }-*/;
}
```

```java
JsonParser<ContactDTO> parser = new JsonParser<ContactDTO>();
ContactDTO contact = parser.fromJson("{\"id\":666,\"name\":\"John Doe\",\"age\":28}");
```

License : MIT
=============

Copyright (c) 2011 [MNCC](http://www.mncc.fr/)
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.
 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

