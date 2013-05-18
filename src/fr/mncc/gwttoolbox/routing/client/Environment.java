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
package fr.mncc.gwttoolbox.routing.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {

  // Application-wide event bus
  private static final SimpleEventBus eventBus_ = new SimpleEventBus();
  private static final RouteController routeController_ = RouteController.getInstance();
  private static final Map<String, List<HandlerRegistration>> eventHandlers_ =
      new HashMap<String, List<HandlerRegistration>>();

  protected Environment() {

  }

  public static void fireEvent(GwtEvent<?> event) {
    eventBus_.fireEvent(event);
  }

  public static <H extends EventHandler> HandlerRegistration registerEventHandler(
      GwtEvent.Type<H> type, H handler) {
    return eventBus_.addHandler(type, handler);
  }

  public static <H extends EventHandler> void registerEventHandler(GwtEvent.Type<H> type,
      H handler, String uuid) {
    if (!eventHandlers_.containsKey(uuid)) {
      eventHandlers_.put(uuid, new ArrayList<HandlerRegistration>());
    }
  eventHandlers_.get(uuid).add(eventBus_.addHandler(type, handler));
}

  public static void unregisterEventHandlers(String uuid) {
    if (eventHandlers_.containsKey(uuid)) {
      for (HandlerRegistration handlerRegistration : eventHandlers_.get(uuid)) {
        handlerRegistration.removeHandler();
      }
      eventHandlers_.remove(uuid);
    }
  }

  public static void initRouteController() {
    routeController_.listen();
  }

  public static void initRouteController(String UA, String domainName) {
    routeController_.listen(UA, domainName);
  }

  public static void uinitRouteController() {
    routeController_.unlisten();
  }

  public static void addRoute(String name, RouteCallback routeCallback) {
    routeController_.addRoute(name, routeCallback);
  }

  public static void goTo(String routeName, String arguments) {
    if (arguments == null || arguments.isEmpty())
      routeController_.goTo(routeName);
    else
      routeController_.goTo(routeName, arguments);
  }

  public static void goToFromCurrentUrl(String defaultRouteName, String defaultArguments) {
    if (defaultArguments == null || defaultArguments.isEmpty())
      routeController_.goToFromCurrentUrl(defaultRouteName);
    else
      routeController_.goToFromCurrentUrl(defaultRouteName, defaultArguments);
  }

  public static void saveRouteArguments(String routeName, String arguments) {
    routeController_.saveRouteArguments(routeName, arguments);
  }
}
