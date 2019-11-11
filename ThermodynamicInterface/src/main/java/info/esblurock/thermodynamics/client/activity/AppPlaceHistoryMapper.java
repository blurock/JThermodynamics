package info.esblurock.thermodynamics.client.activity;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

import info.esblurock.thermodynamics.client.place.FirstPagePlace;
import info.esblurock.thermodynamics.client.place.AboutSummaryPlace;

/**
 * PlaceHistoryMapper interface is used to attach all places which the
 * PlaceHistoryHandler should be aware of. This is done via the @WithTokenizers
 * annotation or by extending PlaceHistoryMapperWithFactory and creating a
 * separate TokenizerFactory.
 */
@WithTokenizers( {
	FirstPagePlace.Tokenizer.class,
	AboutSummaryPlace.Tokenizer.class
})

public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}
