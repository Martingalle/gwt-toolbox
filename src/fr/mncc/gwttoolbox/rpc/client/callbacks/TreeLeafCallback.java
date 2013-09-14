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
package fr.mncc.gwttoolbox.rpc.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

import static com.google.common.base.Preconditions.checkNotNull;

public class TreeLeafCallback<T> implements AsyncCallback<T> {

  private TreeRootCallback treeRoot_ = null;
  private boolean hasFailed_ = false;
  private T result_ = null;

  protected TreeLeafCallback() {

  }

  public TreeLeafCallback(TreeRootCallback treeRoot) {
    treeRoot_ = checkNotNull(treeRoot);
  }

  public boolean hasFailed() {
    return hasFailed_;
  }

  public T getResult() {
    return result_;
  }

  @Override
  public void onFailure(Throwable caught) {
    caught.printStackTrace();
    hasFailed_ = true;
    treeRoot_.onFailure(this, caught);
  }

  @Override
  public void onSuccess(T result) {
    result_ = result;
    hasFailed_ = false;
    treeRoot_.onSuccess(this);
  }
}
