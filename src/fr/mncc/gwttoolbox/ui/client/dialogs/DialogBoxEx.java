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
package fr.mncc.gwttoolbox.ui.client.dialogs;

import com.google.gwt.user.client.ui.DialogBox;

/**
 * NEVER set dialog.setAnimationEnabled(true) otherwise the dialog might be cut for some unknown
 * reasons :-(
 * 
 * http://code.google.com/p/google-web-toolkit/issues/detail?id=4597
 * http://code.google.com/p/google-web-toolkit/issues/detail?id=2595
 * http://code.google.com/p/google-web-toolkit/issues/detail?id=1424
 * 
 * There is a workaround which suggest to set the width on the gwt-DialogBox style
 */
@Deprecated
public class DialogBoxEx extends DialogBox {

  public DialogBoxEx() {
    super();
    setAnimationEnabled(false);
  }

  public DialogBoxEx(final Caption captionWidget) {
    super(captionWidget);
    setAnimationEnabled(false);
  }

  @Override
  final public void setAnimationEnabled(boolean enable) {
    super.setAnimationEnabled(false);
  }
}
