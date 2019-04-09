package info.esblurock.thermodynamics.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootPanel;

import info.esblurock.thermodynamics.client.activity.AppActivityMapper;
import info.esblurock.thermodynamics.client.activity.AppPlaceHistoryMapper;
import info.esblurock.thermodynamics.client.activity.ClientFactory;
import info.esblurock.thermodynamics.client.place.FirstPagePlace;
import info.esblurock.thermodynamics.client.ui.ThermodyanmicsBasePage;

public class ThermodynamicsTopPage implements EntryPoint {
	
	private Place defaultPlace = new FirstPagePlace("Top");

	@Override
	public void onModuleLoad() {
		String redirect = Cookies.getCookie("redirect");
		String account_name = Cookies.getCookie("account_name");
		Cookies.removeCookie("redirect");
		boolean firsttime = true;
		if(redirect == null || account_name == null) {
			firsttime = true;
		}  else if(redirect.compareTo(account_name) == 0) {
			firsttime = false;
		}
		ClientFactory clientFactory = GWT.create(ClientFactory.class);
		setUpInterface(clientFactory);
	}
	@SuppressWarnings("deprecation")
	public void setUpInterface(ClientFactory clientFactory) {
		EventBus eventBus = clientFactory.getEventBus();
		PlaceController placeController = clientFactory.getPlaceController();

		// Start ActivityManager for the main widget with our ActivityMapper
		ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
		ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);

		ThermodyanmicsBasePage basepage = clientFactory.getThermodyanmicsBasePage();
		activityManager.setDisplay(basepage.getContentPanel());
		AppPlaceHistoryMapper historyMapper= GWT.create(AppPlaceHistoryMapper.class);
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);

		RootPanel.get().add(basepage);
		historyHandler.handleCurrentHistory();
		
	}
}
