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

import java.util.HashMap;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;

public class RouteController implements ValueChangeHandler<String> {

  private static RouteController instance_;

  public static RouteController getInstance() {
    if (instance_ == null)
      instance_ = new RouteController();
    return instance_;
  }

  private final HashMap<String, RouteCallback> routes_ = new HashMap<String, RouteCallback>();
  private HandlerRegistration handlerRegistration_ = null;
  private RescueCallback rescueCallback_ = null;
  private String UA_ = "";
  private String domainName_ = "";

  protected RouteController() {

  }

  /**
   * Called when goTo() is called or the prev/next button of the browser is hit.
   * 
   * @param event
   */
  @Override
  public void onValueChange(ValueChangeEvent<String> event) {

    /*
     * token is of one of the following forms:
     * 
     * 1. routeName 2. routeName?xxx 3. !routeName 4. !routeName?xxx
     */
    String token = event.getValue();
    if (token == null) {
      if (rescueCallback_ != null)
        rescueCallback_.execute(token);
    } else {

      // Remove ! before route name if any
      if (token.indexOf("!") == 0)
        token = token.substring(1);

      // Update analytics if any
      if (!UA_.isEmpty() && !domainName_.isEmpty())
        recordAnalyticsHit(UA_, domainName_, token);

      // Extract the route name and arguments
      String routeName;
      String arguments;
      int questionMarkIndex = token.indexOf("?");
      if (questionMarkIndex < 0) {
        routeName = token;
        arguments = "";
      } else {
        routeName = token.substring(0, questionMarkIndex);
        arguments = token.substring(questionMarkIndex + 1);
      }

      // Call the right callback according to route name
      if (routes_.containsKey(routeName))
        routes_.get(routeName).execute(arguments);
      else {
        if (rescueCallback_ != null)
          rescueCallback_.execute(token);
      }
    }
  }

  /**
   * Activate the routing system.
   */
  public void listen() {
    if (handlerRegistration_ != null)
      GWT.log("History listener already set!");
    else
      handlerRegistration_ = History.addValueChangeHandler(this);
  }

  /**
   * Google Analytics
   * 
   * Do not forget to add the following code in your HTML file:
   * 
   * <script type="text/javascript"> var ga = document.createElement('script'); ga.type =
   * 'text/javascript'; ga.async = true; ga.src = ('https:' == document.location.protocol ?
   * 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js'; var s =
   * document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s); </script>
   */
  public void listen(String UA, String domainName) {
    if (UA != null && !UA.isEmpty() && domainName != null && !domainName.isEmpty()) {
      UA_ = UA;
      domainName_ = domainName;
      listen();
    } else
      GWT.log("Invalid UA and domain name!");
  }

  /**
   * Deactivate the routing system.
   */
  public void unlisten() {
    if (handlerRegistration_ != null)
      handlerRegistration_.removeHandler();
    handlerRegistration_ = null;
  }

  /**
   * Add a new route.
   * 
   * @param routeName
   * @param callback
   */
  public void addRoute(String routeName, RouteCallback callback) {
    if (routes_.containsKey(routeName)) {
      GWT.log("Route " + routeName + " already exist!");
      return;
    }
    if (routeName == null) {
      GWT.log("Route name must not be null!");
      return;
    }
    if (callback == null)
      removeRoute(routeName);
    else
      routes_.put(routeName, callback);
  }

  /**
   * Remove a route previously registered.
   * 
   * @param routeName
   */
  public void removeRoute(String routeName) {
    routes_.remove(routeName);
  }

  /**
   * Rescue callback will be called when we do not know how to process a route name.
   * 
   * @param rescueCallback
   */
  public void addRescueCallback(RescueCallback rescueCallback) {
    if (rescueCallback_ != null) {
      GWT.log("Rescue callback already set!");
      return;
    }
    if (rescueCallback == null)
      removeRescueCallback();
    else
      rescueCallback_ = rescueCallback;
  }

  /**
   * Remove rescue callback.
   */
  public void removeRescueCallback() {
    rescueCallback_ = null;
  }

  /**
   * Launch a new route.
   * 
   * @param routeName
   */
  public void goTo(String routeName) {
    if (routeName == null) {
      GWT.log("Route name must not be null!");
      return;
    }
    if (routeName.equals(History.getToken()))
      History.fireCurrentHistoryState();
    else
      History.newItem(routeName, true);
  }

  /**
   * Launch a new route with a given set of parameters.
   * 
   * @param routeName
   * @param arguments
   */
  public void goTo(String routeName, String arguments) {
    if (routeName == null) {
      GWT.log("Route name must not be null!");
      return;
    }
    if (arguments == null || arguments.isEmpty())
      goTo(routeName);
    else
      goTo(routeName + "?" + arguments);
  }

  /**
   * Reload the current route if any or a default one.
   * 
   * @param defaultRouteName
   */
  public void goToFromCurrentUrl(String defaultRouteName) {
    if (History.getToken().isEmpty())
      goTo(defaultRouteName);
    else
      History.fireCurrentHistoryState();
  }

  /**
   * Reload the current route if any or a default one with a given set of parameters.
   * 
   * @param defaultRouteName
   * @param defaultArguments
   */
  public void goToFromCurrentUrl(String defaultRouteName, String defaultArguments) {
    if (History.getToken().isEmpty())
      goTo(defaultRouteName, defaultArguments);
    else
      History.fireCurrentHistoryState();
  }

  /**
   * Push a new hashtag without triggering an onValueChange(). Mostly used to save parameters before
   * launching a new route.
   * 
   * @param routeName
   * @param arguments
   */
  public void saveRouteArguments(String routeName, String arguments) {
    if (routeName == null) {
      GWT.log("Route name must not be null!");
      return;
    }

    String token = routeName;
    if (arguments != null && !arguments.isEmpty())
      token += ("?" + arguments);

    if (!History.getToken().equals(token))
      History.newItem(token, false);
  }

  private native void recordAnalyticsHit(String UA, String domainName, String pageName) /*-{
    try {
      $wnd._gaq.push(['_setAccount', UA]);
      $wnd._gaq.push(['_setDomainName', domainName]);
      $wnd._gaq.push(['_setSiteSpeedSampleRate', 50]);
      $wnd._gaq.push(['_trackPageview', pageName]);
    } catch (err) {

    }
  }-*/;
}
