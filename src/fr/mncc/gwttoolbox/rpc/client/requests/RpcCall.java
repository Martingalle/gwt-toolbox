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

import fr.mncc.gwttoolbox.ui.client.popups.Indicator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.SerializationException;

/**
 * Based on a code by Daniel Guermeur and Amy Unruh
 * https://bitbucket.org/bbtran/connectr/src/717afa323f4e
 * /src/com/metadot/book/connectr/client/helper/RPCCall.java
 * 
 * @param <T> result type
 */
public abstract class RpcCall<T> {

  private static final RpcConstants constants_ = GWT.create(RpcConstants.class);

  private final AsyncCallback<T> userCallback_;
  private Indicator indicator_ = null;
  private Timer timer_ = null;

  /**
   * Provided for your convenience if you do not expect a return value.
   */
  public RpcCall() {
    userCallback_ = null;
  }

  /**
   * Specify a callback to be called on success or failure.
   * 
   * @param callback user's provided callback
   */
  public RpcCall(AsyncCallback<T> callback) {
    userCallback_ = callback;
  }

  /**
   * Async call to server. Throw an error on failure.
   */
  public void retry() {
    retry(0);
  }

  /**
   * Async call to server. Throw an error after nbRetries failures.
   * 
   * @param nbRetries
   */
  public void retry(int nbRetries) {
    call(nbRetries);
  }

  /**
   * Implement async call to server. Use the provided callback as your webservice callback.
   * 
   * @param innerCallback callback to call on success or failure
   */
  protected abstract void callWebservice(AsyncCallback<T> innerCallback);

  private void call(final int nbRetries) {
    callWebservice(new AsyncCallback<T>() {
      @Override
      public void onFailure(Throwable caught) {
        RpcCall.this.onFailure(nbRetries, caught);
      }

      @Override
      public void onSuccess(T result) {
        RpcCall.this.onSuccess(result);
      }
    });
    showIndicator();
  }

  private void showIndicator() {

    timer_ = new Timer() {
      @Override
      public void run() {

        // If a trip to the server takes more than 500ms, display a busy indicator
        indicator_ = Indicator.showInfoForever(constants_.workingHard());
      }
    };
    timer_.schedule(500);
  }

  private void hideIndicator() {

    if (timer_ != null)
      timer_.cancel();
    timer_ = null;

    if (indicator_ == null)
      return;

    indicator_.hide();
    indicator_.removeFromParent();
    indicator_ = null;
  }

  private void onSuccess(T result) {
    hideIndicator();
    if (userCallback_ != null)
      userCallback_.onSuccess(result);
  }

  private void onFailure(int nbRetries, Throwable caught) {
    hideIndicator();
    if (nbRetries > 0)
      call(nbRetries - 1);
    else {
      try {
        throw caught;
      } catch (InvocationException e) {
        Indicator.showError(constants_.serverNotAvailable());
      } catch (IncompatibleRemoteServiceException e) {
        Indicator.showError(constants_.appOutOfDate());
      } catch (SerializationException e) {
        Indicator.showError(constants_.serializationError());
      } catch (RequestTimeoutException e) {
        Indicator.showError(constants_.timeout());
      } catch (Throwable e) {
        // Indicator.showError(e.toString());
      } finally {
        if (userCallback_ != null)
          userCallback_.onFailure(caught);
      }
    }
  }
}
