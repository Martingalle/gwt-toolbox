gwt-rpc
=======

Miscellaneous stuff related to GWT REST/RPC calls.

Dependencies
============

* [Google Guava](http://code.google.com/p/guava-libraries/) 13.0 or above
* [Cofoja](https://code.google.com/p/cofoja/) 1.0-r139
* [HtmlUnit](http://htmlunit.sourceforge.net/) 2.10
* [gwt-primitives](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/primitives)
* [gwt-ui](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/ui)

What is inside ?
================

Client :
* Callback with a default onFailure() implementation :
    * [fr.mncc.gwttoolbox.rpc.client.callbacks.SimpleCallback](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc/client/callbacks/SimpleCallback.java)
* Another callback with a default onFailure() implementation :
    * [fr.mncc.gwttoolbox.rpc.client.callbacks.VoidCallback](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc/client/callbacks/VoidCallback.java)
* RunAsync callback with a default onFailure() implementation :
    * [fr.mncc.gwttoolbox.rpc.client.callbacks.SimpleRunAsyncCallback](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc/client/callbacks/SimpleRunAsyncCallback.java)
* Callbacks for when you need to start many RPC calls but must wait for them all before being able to process the results :
    * [fr.mncc.gwttoolbox.rpc.client.callbacks.TreeRootCallback](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc/client/callbacks/TreeRootCallback.java)
    * [fr.mncc.gwttoolbox.rpc.client.callbacks.TreeLeafCallback](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc/client/callbacks/TreeLeafCallback.java)
* Wrapper for RPC calls with a built-in busy indicator :
    * [fr.mncc.gwttoolbox.rpc.client.requests.RpcCall](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc/client/requests/RpcCall.java)
* Wrapper for REST calls using JSONP protocol :
    * [fr.mncc.gwttoolbox.rpc.client.requests.RestCall](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc/client/requests/RestCall.java)

Server :
* Add cache control header to GWT generated files :
    * [fr.mncc.gwttoolbox.rpc.server.filters.CacheControlFilter](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc/server/filters/CacheControlFilter.java)
* Make some of your GWT pages AJAX crawlable using the ! symbol in your hashtags :
    * [fr.mncc.gwttoolbox.rpc.server.filters.CrawlFilter](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc/server/filters/CrawlFilter.java)

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
public class ContactList<ContactDTO> extends JavaScriptObject {

  protected ContactList() { }
  public final native String getErrorText() /*-{ return this.error_text; }-*/;
  public final native JsArray<T> getContacts() /*-{ return this.contact_list; }-*/;
}
```

```java
RestCall.retry("http://www.mywebsite.com/private/contacts", new SimpleCallback<FeedAsString>() {
    @Override
    public void onSuccess(ContactList contactList) {
        if (contactList.getErrorText() != null && !contactList.getErrorText().isEmpty())
            // Display error message.
        else
            // Do whatever you need to do with the contact list.
    }
});
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
