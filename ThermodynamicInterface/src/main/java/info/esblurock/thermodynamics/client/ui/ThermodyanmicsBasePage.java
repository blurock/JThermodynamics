package info.esblurock.thermodynamics.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import info.esblurock.thermodynamics.client.activity.ClientFactory;
import info.esblurock.thermodynamics.client.place.AboutSummaryPlace;
import info.esblurock.thermodynamics.client.place.FirstPagePlace;

public class ThermodyanmicsBasePage extends Composite  {

	private static ThermodyanmicsBasePageUiBinder uiBinder = GWT.create(ThermodyanmicsBasePageUiBinder.class);

	interface ThermodyanmicsBasePageUiBinder extends UiBinder<Widget, ThermodyanmicsBasePage> {
	}
	@UiField
	MaterialLink about;
	@UiField
	MaterialLink home;
	@UiField
	MaterialLink calculate;
	@UiField
	MaterialLabel subtitle;
	ClientFactory clientFactory;
	
	@UiField
	SimplePanel contentPanel;
	
	public ThermodyanmicsBasePage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public ThermodyanmicsBasePage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	void init() {
		setSubTitle("JTherGas (pre release version)");
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
		setSubTitle("About JTherGas");
		goTo(new AboutSummaryPlace("About JTherGas"));
	}
	@UiHandler("home")
	public void onHomeClick(ClickEvent event) {
		setSubTitle("JTherGas (pre release version)");
		goTo(new FirstPagePlace("Home"));
	}
	private void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}
	
	public void setClientFactory(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
}
