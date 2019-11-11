package info.esblurock.thermodynamics.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;

import info.esblurock.thermodynamics.client.ui.ThermodyanmicsBasePage;
import info.esblurock.thermodynamics.client.view.AboutSummaryView;
import info.esblurock.thermodynamics.client.view.FirstPageView;


public interface ClientFactory {
	EventBus getEventBus();
	PlaceController getPlaceController();
	
	void setInUser();

	public FirstPageView getFirstPageView();
	public AboutSummaryView getAboutSummaryView();
	public ThermodyanmicsBasePage getThermodyanmicsBasePage();
}
