package info.esblurock.thermodynamics.client.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import gwt.material.design.client.ui.MaterialLoader;
import info.esblurock.thermodynamics.base.benson.SetOfBensonThermodynamicBaseElements;

public class CalculateThermodynamicsCallback implements AsyncCallback<SetOfBensonThermodynamicBaseElements> {

	CalculateThermodynamicsInterface top;
	
	public CalculateThermodynamicsCallback(CalculateThermodynamicsInterface top) {
		this.top = top;
	}
	@Override
	public void onFailure(Throwable caught) {
		MaterialLoader.loading(false);	
		Window.alert(caught.toString());
	}

	@Override
	public void onSuccess(SetOfBensonThermodynamicBaseElements elements) {
		MaterialLoader.loading(false);	
		top.setInThermo(elements);
	}

}
