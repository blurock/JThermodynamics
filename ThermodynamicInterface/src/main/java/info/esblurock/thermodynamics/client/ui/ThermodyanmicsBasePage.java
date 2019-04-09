package info.esblurock.thermodynamics.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;

public class ThermodyanmicsBasePage extends Composite  {

	private static ThermodyanmicsBasePageUiBinder uiBinder = GWT.create(ThermodyanmicsBasePageUiBinder.class);

	interface ThermodyanmicsBasePageUiBinder extends UiBinder<Widget, ThermodyanmicsBasePage> {
	}
	@UiField
	MaterialLink about;
	@UiField
	MaterialLink calculate;
	@UiField
	MaterialLabel subtitle;

	
	@UiField
	SimplePanel contentPanel;
	
	public ThermodyanmicsBasePage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public ThermodyanmicsBasePage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	void init() {
		calculate.setText("Calculate");
	}
	
	public AcceptsOneWidget getContentPanel() {
		return contentPanel;
	}
	
	public void setSubTitle(String subtitletext) {
		subtitle.setText(subtitletext);
	}

	
	@UiHandler("about")
	public void onAboutClick(ClickEvent event) {
		setSubTitle("About ChemConnect");
		//goTo(new AboutSummaryPlace("About ChemConnect"));
	}


}
