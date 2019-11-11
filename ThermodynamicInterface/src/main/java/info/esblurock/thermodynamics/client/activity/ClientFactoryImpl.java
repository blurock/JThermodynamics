package info.esblurock.thermodynamics.client.activity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;

import info.esblurock.thermodynamics.client.ui.FirstPage;
import info.esblurock.thermodynamics.client.ui.ThermodyanmicsBasePage;
import info.esblurock.thermodynamics.client.ui.about.AboutSummary;
import info.esblurock.thermodynamics.client.view.AboutSummaryView;
import info.esblurock.thermodynamics.client.view.FirstPageView;


public class ClientFactoryImpl implements ClientFactory {
	private final SimpleEventBus eventBus = new SimpleEventBus();
	private final PlaceController placeController = new PlaceController(eventBus);
	
	ThermodyanmicsBasePage basepage = new ThermodyanmicsBasePage();
	FirstPage firstpage = new FirstPage();
	AboutSummary summary = new AboutSummary();
	
	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public void setInUser() {
	}

	@Override
	public ThermodyanmicsBasePage getThermodyanmicsBasePage() {
		return basepage;
	}

	@Override
	public FirstPageView getFirstPageView() {
		return firstpage;
	}

	@Override
	public AboutSummaryView getAboutSummaryView() {
		return summary;
	}

}
