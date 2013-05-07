/**
 * Copyright (c) 2011 MNCC
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
package fr.mncc.gwttoolbox.visualization.client;

import java.util.LinkedHashMap;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;

import fr.mncc.gwttoolbox.rpc.client.callbacks.SimpleCallback;

public abstract class Chart<T, U, V extends com.google.gwt.visualization.client.visualizations.corechart.CoreChart> {

  private AbstractDataTable dataCache = null; // Data used to render the current chart
  private Panel panel = null; // Where to insert the chart
  private int isLoading = 0; // != 0 while chart is being rendered
  private V chart = null; // chart object

  public Chart(Panel panel) {
    this.panel = panel;
  }

  /**
   * Display chart
   */
  final public void show() {
    fetch(new SimpleCallback<LinkedHashMap<T, U>>() {
      @Override
      public void onSuccess(final LinkedHashMap<T, U> data) {
        displayChart(data);
      }
    });
    Window.addResizeHandler(new ResizeHandler() {
      @Override
      public void onResize(final ResizeEvent event) {
        displayChart();
      }
    });
  }

  /**
   * Hide chart
   */
  final public void hide() {
    if (chart != null) {
      removeFromDOM(chart);
      chart = null;
    }
    dataCache = null;
  }

  /**
   * Transform data from user format to Google's format and render chart
   * 
   * @param data data to display
   */
  final private void displayChart(final LinkedHashMap<T, U> data) {

    // Create a callback to be called when the visualization API has been loaded
    final Runnable onLoadCallback = new Runnable() {
      @Override
      public void run() {
        if (isLoading != 0)
          return;

        isLoading++;

        if (chart != null) {
          removeFromDOM(chart);
          chart = null;
        }
        if (data != null && !data.isEmpty())
          dataCache = createTable(data);
        if (dataCache != null) {
          chart = newChart(dataCache, createOptions());
          addToDOM(chart);
        }

        isLoading--;
      }
    };

    // Load the visualization api, passing the onLoadCallback to be called when loading is done
    loadVisualizationAPI(onLoadCallback);
  }

  final private void displayChart() {
    displayChart(null);
  }

  /**
   * Create a new chart object
   * 
   * @param dataTable data in Google's format
   * @param options display options
   * @return chart in the right type
   */
  protected abstract V newChart(final AbstractDataTable dataTable,
      final com.google.gwt.visualization.client.visualizations.corechart.Options options);

  /**
   * Load visualization API
   * 
   * @param onLoadCallback called when API is loaded
   */
  protected abstract void loadVisualizationAPI(final Runnable onLoadCallback);

  /**
   * Insert chart in DOM
   * 
   * @param widget chart object
   */
  final private void addToDOM(final Widget widget) {
    if (panel != null && widget != null)
      panel.add(widget);
  }

  /**
   * Remove chart from DOM
   * 
   * @param widget chart object
   */
  final private void removeFromDOM(final Widget widget) {
    if (panel != null && widget != null)
      panel.remove(widget);
  }

  /**
   * Fetch data from server
   * 
   * @param callback called when data are availables
   */
  protected abstract void fetch(final AsyncCallback<LinkedHashMap<T, U>> callback);

  /**
   * Transform data from user's format to Google's format
   * 
   * @param list data in user's format
   * @return data in Google's format
   */
  protected abstract AbstractDataTable createTable(final LinkedHashMap<T, U> list);

  /**
   * Rendering options
   * 
   * @return options
   */
  protected abstract com.google.gwt.visualization.client.visualizations.corechart.Options createOptions();
}
