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
package fr.mncc.gwttoolbox.datagrid.client.columns;

import fr.mncc.gwttoolbox.datagrid.client.cells.ClickableImageCell;

import com.google.gwt.cell.client.Cell;

/**
 * Image column.
 * 
 * @param <T> the row type
 */
public abstract class ImageColumn<T> extends ColumnReadOnly<T, String> {

  public ImageColumn(String width, String height) {
    this(true, width, height);
  }

  public ImageColumn(boolean trustUri, String width, String height) {
    this(new ClickableImageCell(trustUri, width, height));
  }

  public ImageColumn(Cell<String> cell) {
    super(cell);
  }

  @Override
  public String getColumnHeader() {
    return "";
  }

  /**
   * Image to display as a base64 string or an url.
   * 
   * @param media
   * @return image as a base64 string or an url.
   */
  @Override
  public abstract String getValue(T media);
}
