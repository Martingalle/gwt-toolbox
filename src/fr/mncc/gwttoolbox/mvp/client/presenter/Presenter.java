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
package fr.mncc.gwttoolbox.mvp.client.presenter;

import fr.mncc.gwttoolbox.mvp.client.view.AbstractView;
import fr.mncc.gwttoolbox.primitives.shared.KeyValueMap;

/**
 * Implements AbstractPresenter interface.
 * 
 * @param <V> View interface
 */
public class Presenter<V extends AbstractView> {

  private KeyValueMap args_ = new KeyValueMap();
  private V view_ = null;

  /**
   * Implements this method to set the UI default values after the View has been added to the DOM.
   */
  public void afterViewLoad() {

  }

  /**
   * Implements this method to save the UI current values before the View is removed from the DOM.
   */
  public void beforeViewUnload() {

  }

  public void setArgs(KeyValueMap args) {
    args_ = args != null ? args : new KeyValueMap();
  }

  public KeyValueMap getArgs() {
    return args_;
  }

  public void setView(AbstractView view) {
    view_ = (V) view;
  }

  protected V getView() {
    return view_;
  }
}
