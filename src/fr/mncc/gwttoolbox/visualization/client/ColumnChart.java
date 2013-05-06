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

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.VisualizationUtils;

public abstract class ColumnChart<T, U> extends
    Chart<T, U, com.google.gwt.visualization.client.visualizations.corechart.ColumnChart> {

  public ColumnChart(Panel panel) {
    super(panel);
  }

  @Override
  final protected com.google.gwt.visualization.client.visualizations.corechart.ColumnChart newChart(
      final AbstractDataTable dataTable,
      final com.google.gwt.visualization.client.visualizations.corechart.Options options) {
    return new com.google.gwt.visualization.client.visualizations.corechart.ColumnChart(dataTable,
        options);
  }

  @Override
  final protected void loadVisualizationAPI(final Runnable onLoadCallback) {
    VisualizationUtils.loadVisualizationApi(onLoadCallback,
        com.google.gwt.visualization.client.visualizations.corechart.ColumnChart.PACKAGE);
  }
}
