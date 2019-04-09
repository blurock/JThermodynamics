package info.esblurock.thermodynamics.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

import info.esblurock.thermodynamics.client.place.FirstPagePlace;

public class AppActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	public AppActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	
	@Override
	public Activity getActivity(Place place) {
		if (place instanceof FirstPagePlace) {
			return new FirstPageActivity((FirstPagePlace) place, clientFactory);
		}
		return null;
	}

}
