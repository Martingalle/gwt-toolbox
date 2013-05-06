gwt-visualization
=================

Thin wrapper around the GWT Visualization API.

Dependencies
============

* [Google Visualization](https://developers.google.com/chart/)
* [gwt-rpc](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/rpc)

What is inside ?
================

Client :
* Area chart :
    * [fr.mncc.gwttoolbox.visualization.client.AreaChart](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/visualization/client/AreaChart.java)
* Bar chart :
    * [fr.mncc.gwttoolbox.visualization.client.BarChart](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/visualization/client/BarChart.java)
* Column chart :
    * [fr.mncc.gwttoolbox.visualization.client.ColumnChart](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/visualization/client/ColumnChart.java)
* Line chart :
    * [fr.mncc.gwttoolbox.visualization.client.LineChart](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/visualization/client/LineChart.java)
* Pie chart :
    * [fr.mncc.gwttoolbox.visualization.client.PieChart](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/visualization/client/PieChart.java)

Example
=======

```java
public class ChartNumberOfEventsPerMonth extends ColumnChart<Integer, Integer> {

    private String[] months = LocaleInfo.getCurrentLocale().getDateTimeFormatInfo().monthsFull();

    public ChartNumberOfEventsPerMonth(Panel panel) {
        super(panel);
    }

    @Override
    protected void fetch(AsyncCallback<LinkedHashMap<Integer, Integer>> innerCallback) {
        // Get the number of events for each month and then call innerCallback.onSuccess() with the result.
    }

    @Override
    protected AbstractDataTable createTable(LinkedHashMap<Integer, Integer> map) {

        DataTable table = DataTable.create();
        table.addColumn(DataTable.ColumnType.STRING, "Months");
        table.addColumn(DataTable.ColumnType.NUMBER, "Number of events");

        int i = 0;
        for (Integer month : map.keySet()) {
          table.addRow();
          table.setValue(i, 0, getMonth(month));
          table.setValue(i, 1, map.get(month));
          i++;
        }
        return table;
    }

    @Override
    protected Options createOptions() {
        Options options = Options.create();
        options.setWidth(900);
        options.setHeight(300);
        options.setTitle("Number of events per month"));
        return options;
    }

    private String getMonth(int month) {
        return month < 0 || month > 11 ? "Unknown" : months[month];
    }
}
```

License : MIT
=============

Copyright (c) 2011 [MNCC](http://www.mncc.fr/)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
