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

import java.util.ArrayList;
import java.util.List;

public abstract class TreeRootCallback {

  private int nbCallbacks_ = 0;
  private int nbFailures_ = 0;
  private int nbSuccesses_ = 0;
  private List<TreeLeafCallback<?>> results_ = new ArrayList<TreeLeafCallback<?>>();

  public TreeRootCallback(int nbCallbacks) {
    nbCallbacks_ = nbCallbacks;
  }

  public void onFailure(TreeLeafCallback<?> callback, Throwable caught) {
    results_.add(callback);
    nbFailures_++;
    if (hasEnded())
      onFailure(results_);
  }

  public void onSuccess(TreeLeafCallback<?> callback) {
    results_.add(callback);
    nbSuccesses_++;
    if (hasEndedWithSuccess())
      onSuccess(results_);
    if (hasEndedWithFailure())
      onFailure(results_);
  }

  private boolean hasEnded() {
    return (nbSuccesses_ + nbFailures_) == nbCallbacks_;
  }

  private boolean hasEndedWithSuccess() {
    return hasEnded() && nbFailures_ == 0;
  }

  private boolean hasEndedWithFailure() {
    return hasEnded() && nbFailures_ != 0;
  }

  protected abstract void onFailure(List<TreeLeafCallback<?>> results);
  protected abstract void onSuccess(List<TreeLeafCallback<?>> results);
}
