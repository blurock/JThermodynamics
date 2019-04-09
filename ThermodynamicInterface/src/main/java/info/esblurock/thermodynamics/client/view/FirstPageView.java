package info.esblurock.thermodynamics.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

import info.esblurock.thermodynamics.client.ui.ThermodyanmicsBasePage;

public interface FirstPageView extends IsWidget {
	void setName(String helloName);
	void setPresenter(Presenter listener);
	
	public void asNewUser();
	public void asExistingUser();

	public interface Presenter {
		void goTo(Place place);
	}

	void setTopPanel(ThermodyanmicsBasePage toppanel);

}
