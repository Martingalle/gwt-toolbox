gwt-mvp
=======

A lightweight mvp framework for GWT.

Dependencies
============

* [gwt-primitives](https://github.com/csavelief/gwt-primitives) 1.0 or above

What is inside ?
================

Client :
* Presenters must inherit from Presenter and implements AsbtractPresenter :
    * [fr.mncc.gwttoolbox.mvp.client.presenter.Presenter](https://github.com/csavelief/gwt-mvp/blob/master/src/fr/mncc/gwttoolbox/mvp/client/presenter/Presenter.java)
    * [fr.mncc.gwttoolbox.mvp.client.presenter.AbstractPresenter](https://github.com/csavelief/gwt-mvp/blob/master/src/fr/mncc/gwttoolbox/mvp/client/presenter/AbstractPresenter.java)
* Views must inherit from View and implements AbstractView :
    * [fr.mncc.gwttoolbox.mvp.client.view.View](https://github.com/csavelief/gwt-mvp/blob/master/src/fr/mncc/gwttoolbox/mvp/client/view/View.java)
    * [fr.mncc.gwttoolbox.mvp.client.view.AbtractView](https://github.com/csavelief/gwt-mvp/blob/master/src/fr/mncc/gwttoolbox/mvp/client/view/AbstractView.java)
* Use the WidgetEx class to bind a view with a presenter :
    * [fr.mncc.gwttoolbox.mvp.client.WidgetEx](https://github.com/csavelief/gwt-mvp/blob/master/src/fr/mncc/gwttoolbox/mvp/client/WidgetEx.java)
* Use the Place classes to layout the whole screen :
    * [fr.mncc.gwttoolbox.mvp.client.place.Place](https://github.com/csavelief/gwt-mvp/blob/master/src/fr/mncc/gwttoolbox/mvp/client/place/Place.java)
    * [fr.mncc.gwttoolbox.mvp.client.place.PlaceCrawlable](https://github.com/csavelief/gwt-mvp/blob/master/src/fr/mncc/gwttoolbox/mvp/client/place/PlaceCrawlable.java)

How to get started ?
====================

Download gwt-mvp.jar (built against the latest tag) and add it to your Java/GWT project classpath.

Example
=======

```html
<!DOCTYPE ui:UiBinder SYSTEM "http://dl.google.com/gwt/DTD/xhtml.ent">
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
             xmlns:g="urn:import:com.google.gwt.user.client.ui">
    <ui:style>
        .floatRight {
            float: right;
        }
    </ui:style>
    <g:DockLayoutPanel unit="PX">
        <g:north size="40">
            <g:HorizontalPanel width="100%" height="100%">
                <g:Button ui:field="btnLogin" text="Login" addStyleNames="{style.floatRight}" />
                <g:PasswordTextBox ui:field="edPassword" addStyleNames="{style.floatRight}" />
                <g:TextBox ui:field="edUsername" addStyleNames="{style.floatRight}" />
            </g:HorizontalPanel>
        </g:north>

        <!-- Add more widgets here -->

    </g:DockLayoutPanel>
</ui:UiBinder>
```

```java
public class MyView extends View<MyAbstractPresenter> implements AbstractView {

    @UiTemplate("MyView.ui.xml")
    interface MyViewUiBinder extends UiBinder<Widget, MyView> {}
    private final static MyViewUiBinder uiBinder_ = GWT.create(MyViewUiBinder.class);

    @UiField TextBox edUsername;
    @UiField PasswordTextBox edPassword;

    public MyView() {
        initWidget(uiBinder_.createAndBindUi(this));
    }

    @UiHandler("btnLogin")
    protected void onLoginClick(ClickEvent event) {
        if (getPresenter() != null)
            getPresenter().login(edUsername.getValue(), edPassword.getValue());
    }

    @UiHandler("edPassword")
    protected void onPasswordEnter(KeyUpEvent event) {
        if (event.getNativeKeyCode() == 13 /* ENTER */ && getPresenter() != null)
            getPresenter().login(edUsername.getValue(), edPassword.getValue());
    }
}
```

```java
public interface MyAbstractPresenter extends AbstractPresenter {

    void login(String username, String password);
}
```

```java
public MyPresenter extends Presenter<AbstractView> implements MyAbstractPresenter {

    @Override
    public void login(String username, String password) {
        // Implements all your login stuff here.
    }
}
```

```java
public MyPlace extends Place {

    public MyPlace() {
        super(new MyPresenter(), new MyView());
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
