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

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import fr.mncc.gwttoolbox.ui.client.dialogs.resources.DialogConstants;
import fr.mncc.gwttoolbox.ui.client.dialogs.resources.DialogResourceBundle;

@Deprecated
public class MessageBox extends PopupPanel {

  public static void showError(String text) {
    MessageBox dlg =
        new MessageBox(DialogConstants.INSTANCE.error(), text, DialogResourceBundle.INSTANCE
            .iconError(), null, "");
    dlg.show();
  }

  public static void showSuccess(String text) {
    MessageBox dlg =
        new MessageBox(DialogConstants.INSTANCE.success(), text, DialogResourceBundle.INSTANCE
            .iconSuccess(), null, "");
    dlg.show();
  }

  public static void showError(String text, String btnClassName) {
    MessageBox dlg =
        new MessageBox(DialogConstants.INSTANCE.error(), text, DialogResourceBundle.INSTANCE
            .iconError(), null, btnClassName);
    dlg.show();
  }

  public static void showSuccess(String text, String btnClassName) {
    MessageBox dlg =
        new MessageBox(DialogConstants.INSTANCE.success(), text, DialogResourceBundle.INSTANCE
            .iconSuccess(), null, btnClassName);
    dlg.show();
  }

  @Deprecated
  public static void showError(String text, DialogHideCallback hideCallback) {
    MessageBox dlg =
        new MessageBox(DialogConstants.INSTANCE.error(), text, DialogResourceBundle.INSTANCE
            .iconError(), hideCallback, "");
    dlg.show();
  }

  @Deprecated
  public static void showSuccess(String text, DialogHideCallback hideCallback) {
    MessageBox dlg =
        new MessageBox(DialogConstants.INSTANCE.success(), text, DialogResourceBundle.INSTANCE
            .iconSuccess(), hideCallback, "");
    dlg.show();
  }

  public MessageBox(String caption, String text, ImageResource resource,
      final DialogHideCallback hideCallback) {
    this(caption, text, resource, hideCallback, "");
  }

  private MessageBox(String caption, String text, ImageResource resource,
      final DialogHideCallback hideCallback, String btnClassName) {
    super();

    DialogResourceBundle.INSTANCE.style().ensureInjected();

    Image image = new Image(resource);
    image.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
    image.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
    image.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
    image.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

    Button btnClose = new Button(DialogConstants.INSTANCE.close());
    btnClose.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
        if (hideCallback != null)
          hideCallback.onHide();
      }
    });
    btnClose.getElement().getStyle().setMarginTop(0, Style.Unit.PX);
    btnClose.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
    btnClose.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
    btnClose.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

    if (btnClassName != null && !btnClassName.isEmpty())
      btnClose.addStyleName(btnClassName);

    Label title = new Label(caption);
    title.setStylePrimaryName(DialogResourceBundle.INSTANCE.style().dialogTitle());

    Label message = new Label(text);
    message.getElement().getStyle().setPadding(10, Style.Unit.PX);
    message.getElement().getStyle().setProperty("textAlign", "center");
    message.getElement().getStyle().setFontSize(14, Style.Unit.PX);

    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.setWidth("400px");
    verticalPanel.setStylePrimaryName(DialogResourceBundle.INSTANCE.style().dialogBkg());
    verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    verticalPanel.add(title);
    verticalPanel.add(image);
    verticalPanel.add(message);
    verticalPanel.add(btnClose);

    setWidget(verticalPanel);
    setStylePrimaryName(DialogResourceBundle.INSTANCE.style().dialog());
    setAnimationEnabled(false);
    setGlassEnabled(false);
    center();
  }
}
