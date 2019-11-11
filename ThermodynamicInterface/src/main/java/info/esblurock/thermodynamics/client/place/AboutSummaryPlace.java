package info.esblurock.thermodynamics.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class AboutSummaryPlace extends Place {
	private String titleName;

	public AboutSummaryPlace(String token) {
		this.titleName = token;
	}
	public String getTitleName() {
		return titleName;
	}

	public static class Tokenizer implements PlaceTokenizer<AboutSummaryPlace> {
		@Override
		public String getToken(AboutSummaryPlace place) {
			return place.getTitleName();
		}
		@Override
		public AboutSummaryPlace getPlace(String token)  {
			return new AboutSummaryPlace(token);
		}
	}

}
