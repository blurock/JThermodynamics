package info.esblurock.thermodynamics.async;

import com.google.gwt.user.client.rpc.AsyncCallback;

import info.esblurock.thermodynamics.base.benson.SetOfBensonThermodynamicBaseElements;

public interface CalculateThermodynamicsServiceAsync {

	void CalculateThermodynamics(String type, String parameter,
			AsyncCallback<SetOfBensonThermodynamicBaseElements> callback);

}
