package info.esblurock.thermodynamics.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class FirstPagePlace extends Place {
	private String titleName;

	public FirstPagePlace(String token) {
		this.titleName = token;
	}
	public String getTitleName() {
		return titleName;
	}

	public static class Tokenizer implements PlaceTokenizer<FirstPagePlace> {
		@Override
		public String getToken(FirstPagePlace place) {
			return place.getTitleName();
		}
		@Override
		public FirstPagePlace  getPlace(String token)  {
			return new FirstPagePlace(token);
		}
	}

}
