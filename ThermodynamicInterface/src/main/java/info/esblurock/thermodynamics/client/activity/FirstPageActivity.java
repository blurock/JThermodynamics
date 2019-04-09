package info.esblurock.thermodynamics.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import info.esblurock.thermodynamics.client.place.FirstPagePlace;
import info.esblurock.thermodynamics.client.view.FirstPageView;


public class FirstPageActivity extends AbstractActivity implements FirstPageView.Presenter {
	private ClientFactory clientFactory;
	private String name;
	
	public FirstPageActivity() {
		
	}
	public FirstPageActivity(FirstPagePlace place, ClientFactory clientFactory) {
		this.name = place.getTitleName();
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		FirstPageView firstPageView = clientFactory.getFirstPageView();
		firstPageView.setName(name);
		firstPageView.setPresenter(this);
		containerWidget.setWidget(firstPageView.asWidget());
		firstPageView.setTopPanel(clientFactory.getThermodyanmicsBasePage());

		String inSystemS = Cookies.getCookie("hasAccount");
		if(Boolean.parseBoolean(inSystemS)) {
			firstPageView.asExistingUser();
		} else {
			firstPageView.asNewUser();
		}
	}
	   @Override
	    public String mayStop() {
			return null;
	    }

	@Override
	public void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}

}
