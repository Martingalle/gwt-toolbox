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
package fr.mncc.gwttoolbox.mvp.client.place;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import fr.mncc.gwttoolbox.mvp.client.WidgetEx;
import fr.mncc.gwttoolbox.mvp.client.presenter.AbstractPresenter;
import fr.mncc.gwttoolbox.mvp.client.view.AbstractView;
import fr.mncc.gwttoolbox.primitives.shared.KeyValueMap;

/**
 * Bind a Presenter with a View.
 * 
 * The main goal of this class is to layout the whole screen.
 */
public class Place {

  private final WidgetEx widgetEx_;

  public Place(AbstractPresenter presenter, AbstractView view) {
    widgetEx_ = new WidgetEx(presenter, view);
  }

  /**
   * Add the view to the RootLayoutPanel.
   * 
   * @param args
   */
  public void attachToRootLayoutPanel(KeyValueMap args) {
    attach(RootLayoutPanel.get(), args);
  }

  /**
   * Add the view to the RootPanel.
   * 
   * @param args
   */
  public void attachToRootPanel(KeyValueMap args) {
    attach(RootPanel.get(), args);
  }

  /**
   * Add the view to the DOM.
   * 
   * @param container
   */
  public void attach(Panel container, KeyValueMap args) {
    widgetEx_.setArgs(args);
    container.add(widgetEx_);
  }

  /**
   * Remove the view from the DOM.
   * 
   * @return args
   */
  public KeyValueMap detach() {
    widgetEx_.removeFromParent();
    return widgetEx_.getArgs();
  }

  /**
   * If the place is AJAX crawlable, the place name must start with a !
   * 
   * @return true if Place is AJAX crawlable, false otherwise
   */
  public boolean isAjaxCrawlable() {
    return false;
  }
}
