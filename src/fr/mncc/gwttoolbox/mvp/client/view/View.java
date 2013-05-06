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
package fr.mncc.gwttoolbox.mvp.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import fr.mncc.gwttoolbox.mvp.client.presenter.AbstractPresenter;

/**
 * Implements AbstractView interface.
 * Unlike ResizeComposite, do not force the internal widget to inherits from RequiresResize.
 *
 * @param <P> Presenter interface
 */
public class View<P extends AbstractPresenter> extends Composite implements ProvidesResize, RequiresResize {

  private P presenter_ = null;

  @Override
  public void onResize() {
    Widget child = getWidget();
    if (child instanceof RequiresResize)
      ((RequiresResize) child).onResize();
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    afterViewLoad();
    if (presenter_ != null)
      presenter_.afterViewLoad();
  }

  @Override
  protected void onUnload() {
    if (presenter_ != null)
      presenter_.beforeViewUnload();
    beforeViewUnload();
    super.onUnload();
  }

  public void setPresenter(AbstractPresenter presenter) {
    presenter_ = (P) presenter;
  }

  protected P getPresenter() {
    return presenter_;
  }

  /**
   * Called after the View has been added to the DOM.
   */
  protected void afterViewLoad() {

  }

  /**
   * Called before the View is removed from the DOM.
   */
  protected void beforeViewUnload() {

  }
}
