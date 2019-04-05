package info.esblurock.thermodynamics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import info.esblurock.thermodynamics.client.ui.ThermodyanmicsBasePage;

public class ThermodynamicsTopPage implements EntryPoint {

	@Override
	public void onModuleLoad() {
		ThermodyanmicsBasePage basepage = new ThermodyanmicsBasePage();
		RootPanel.get().add(basepage);
	}

}
