gwt-datagrid
============

Thin wrapper around the GWT DataGrid API in order to increase our productivity.

Dependencies
============

* [gwt-primitives](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives)

What is inside ?
================

Client :
* Custom cells :
    * [fr.mncc.gwttoolbox.datagrid.client.cells.*](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/datagrid/client/cells)
* Basic column types :
    * [fr.mncc.gwttoolbox.datagrid.client.columns.*](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/datagrid/client/columns)
* Basic grid implementation :
    * [fr.mncc.gwttoolbox.datagrid.client.Grid](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/datagrid/client/Grid.java)

Example
=======

```java
public class ContactDTO extends Entity {

    public ContactDTO() { super("ContactDTO"); }
    public String getName() { return getAsString("name"); }
    public void setName(String name) { put("name", name); }
    public int getAge() { return getAsInt("age"); }
    public void setAge(int age) { put("age", age); }
    public String getProfilePictureAsImageBase64() { return getAsString("imageBase64"); }
    public void setProfilePictureAsImageBase64(String imageBase64) { put("imageBase64", imageBase64); }
}
```

```java
public class ContactList extends Grid<ContactDTO> {

    class ProfilePictureCell extends ImageColumn<ContactDTO> {

        public ProfilePictureCell() { super(null /* constraint on height only */, "60px"); }
        @Override public String getValue(ContactDTO contact) { return "data:" + contact.getProfilePictureAsImageBase64(); }
    }

    class NameCell extends TextColumnReadOnly<ContactDTO> {

        public NameCell() {}
        @Override public String getColumnHeader() { return "Name"; }
        @Override public String getValue(ContactDTO contact) { return contact.getName(); }
    }

    class AgeCell extends TextColumnReadOnly<ContactDTO> {

        public AgeCell() {}
        @Override public String getColumnHeader() { return "Age"; }
        @Override public String getValue(ContactDTO contact) { return "" + contact.getAge(); }
    }

    public ContactList() {
        super(500 /* paging by group of 500 rows */);
        addColumn((fr.mncc.gwttoolbox.datagrid.client.columns.Column)(new ProfilePictureCell()));
        addColumn((fr.mncc.gwttoolbox.datagrid.client.columns.Column)(new NameCell()));
        addColumn((fr.mncc.gwttoolbox.datagrid.client.columns.Column)(new AgeCell()));
        addAsyncDataProvider(); // Tell the grid that the list of ContactDTO comes from a webservice
    }

    @Override
    protected void onRangeChanged(int start, int length) {
        // Here, call your webservice to get a list of ContactDTO.
        // Use start and length for paging the result list.
        // Then call Grid.update() to display the list of ContactDTO.
    }
}
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
