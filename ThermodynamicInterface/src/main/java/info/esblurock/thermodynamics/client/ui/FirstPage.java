package info.esblurock.thermodynamics.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import info.esblurock.thermodynamics.client.view.FirstPageView;


public class FirstPage extends Composite implements FirstPageView  {

	private static FirstPageUiBinder uiBinder = GWT.create(FirstPageUiBinder.class);

	interface FirstPageUiBinder extends UiBinder<Widget, FirstPage> {
	}
	
	Presenter listener;
	ThermodyanmicsBasePage toppanel;

	public FirstPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button button;

	public FirstPage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("Hello!");
	}

	@Override
	public void setName(String helloName) {
	}

	@Override
	public void setPresenter(Presenter listener) {
		this.listener = listener;
	}

	@Override
	public void asNewUser() {
	}

	@Override
	public void asExistingUser() {
	}

	@Override
	public void setTopPanel(ThermodyanmicsBasePage toppanel) {
		this.toppanel = toppanel;
	}
}
