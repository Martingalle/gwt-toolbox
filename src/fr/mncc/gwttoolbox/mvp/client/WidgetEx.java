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
package fr.mncc.gwttoolbox.mvp.client;

import fr.mncc.gwttoolbox.mvp.client.presenter.AbstractPresenter;
import fr.mncc.gwttoolbox.mvp.client.view.AbstractView;
import fr.mncc.gwttoolbox.primitives.shared.KeyValueMap;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * Bind a Presenter with a View.
 */
public class WidgetEx<P extends AbstractPresenter, V extends AbstractView> extends Composite
    implements ProvidesResize, RequiresResize {

  private final AbstractPresenter presenter_;
  private final AbstractView view_;

  public WidgetEx(P presenter, V view) {

    assert presenter != null;
    assert view != null;

    presenter_ = presenter;
    view_ = view;

    presenter_.setView(view_);
    view_.setPresenter(presenter_);

    initWidget(view_.asWidget());
  }

  @Override
  public void onResize() {
    Widget child = getWidget();
    if (child instanceof RequiresResize)
      ((RequiresResize) child).onResize();
  }

  public KeyValueMap getArgs() {
    return presenter_.getArgs();
  }

  public void setArgs(KeyValueMap args) {
    presenter_.setArgs(args);
  }
}
