package info.esblurock.thermodynamics.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class ThermodyanmicsBasePage extends Composite  {

	private static ThermodyanmicsBasePageUiBinder uiBinder = GWT.create(ThermodyanmicsBasePageUiBinder.class);

	interface ThermodyanmicsBasePageUiBinder extends UiBinder<Widget, ThermodyanmicsBasePage> {
	}

	public ThermodyanmicsBasePage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public ThermodyanmicsBasePage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
