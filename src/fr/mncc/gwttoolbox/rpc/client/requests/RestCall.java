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
package fr.mncc.gwttoolbox.rpc.client.requests;

import fr.mncc.gwttoolbox.primitives.client.JsonParser;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RestCall {

  /**
   * Send a request using the JSONP mechanism
   * 
   * @param url
   * @param callback on success, returns a GWT javascript object
   */
  public static <T extends JavaScriptObject> void retry(final String url,
      final AsyncCallback<T> callback) {
    final JsonpRequestBuilder rb = new JsonpRequestBuilder();
    rb.requestObject(url, new AsyncCallback<T>() {
      @Override
      public void onFailure(final Throwable caught) {
        if (callback != null)
          callback.onFailure(caught);
      }

      @Override
      public void onSuccess(final T feed) {
        if (callback != null)
          callback.onSuccess(feed);
      }
    });
  }

  /**
   * Send a restful POST request to a given URL Does not work cross-domain
   * 
   * @param url
   * @param data
   * @param callback on success, returns a GWT JSON object
   */
  public static <T extends JavaScriptObject> void retryPost(final String url, final String data,
      final AsyncCallback<T> callback) {
    sendRequest(RequestBuilder.POST, url, data, callback);
  }

  /**
   * Send a restful GET request to a given URL Does not work cross-domain
   * 
   * @param url
   * @param data
   * @param callback on success, returns a GWT JSON object
   */
  public static <T extends JavaScriptObject> void retryGet(final String url, final String data,
      final AsyncCallback<T> callback) {
    sendRequest(RequestBuilder.GET, url, data, callback);
  }

  private static <T extends JavaScriptObject> void sendRequest(final RequestBuilder.Method method,
      final String url, final String data, final AsyncCallback<T> callback) {
    try {
      final RequestBuilder rb = new RequestBuilder(method, URL.encode(url));
      rb.setHeader("Accept", "application/json");
      rb.setHeader("Content-Type", "application/json");
      rb.sendRequest(data, new RequestCallback() {
        @Override
        public void onError(final com.google.gwt.http.client.Request request,
            final Throwable exception) {
          if (callback != null)
            callback.onFailure(exception);
        }

        @Override
        public void onResponseReceived(final com.google.gwt.http.client.Request request,
            final Response response) {
          if (response.getStatusCode() != Response.SC_OK) {
            if (callback != null)
              callback.onFailure(new Exception("response.getStatusCode() = "
                  + response.getStatusCode()));
          } else {
            final JsonParser<T> parser = new JsonParser<T>();
            if (callback != null)
              callback.onSuccess(parser.fromJson(response.getText()));
          }
        }
      });
    } catch (final Exception exception) {
      if (callback != null)
        callback.onFailure(exception);
    }
  }
}
