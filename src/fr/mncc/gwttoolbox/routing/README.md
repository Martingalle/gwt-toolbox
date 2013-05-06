gwt-routing
===========

A lightweight web browsers routing framework for GWT.

Dependencies
============

None.

What is inside ?
================

Client :
* Easy acces to an application-wide event bus and routing API :    
    * [fr.mncc.gwttoolbox.routing.client.Environment](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/routing/client/Environment.java)
* Manage the routing for a whole web application :
    * [fr.mncc.gwttoolbox.routing.client.RouteController](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/routing/client/RouteController.java)
* What happens when a route is triggered :
    * [fr.mncc.gwttoolbox.routing.client.RouteCallback](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/routing/client/RouteCallback.java)
* What happens when routing fails :
    * [fr.mncc.gwttoolbox.routing.client.RescueCallback](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/routing/client/RescueCallback.java)

Example
=======

```java
private static final String ROUTE_HOME = "home";
private static final String ROUTE_LOGIN = "login";

@Override
public void onModuleLoad() {

    Window.addWindowClosingHandler(new Window.ClosingHandler() {
        @Override
        public void onWindowClosing(Window.ClosingEvent event) {
            Environment.uinitRouteController();
        }
    });

    Environment.initRouteController();
    Environment.addRoute(ROUTE_LOGIN, new RouteCallback() {
        @Override
        public void execute(String arguments) {
            // Implement what happens when #login or #!login is called.
        }
    });
    Environment.addRoute(ROUTE_HOME, new RouteCallback() {
        @Override
        public void execute(String arguments) {
            // Implement what happens when #home or #!home is called.
        }
    });
    Environment.goToFromCurrentUrl("!" /* because we want the default route to be AJAX crawlable */ + ROUTE_HOME);
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
