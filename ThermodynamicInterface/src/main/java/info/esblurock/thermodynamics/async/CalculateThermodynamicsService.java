package info.esblurock.thermodynamics.async;

import java.io.IOException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import info.esblurock.thermodynamics.base.benson.SetOfBensonThermodynamicBaseElements;

@RemoteServiceRelativePath("calculateThermodynamics")
public interface CalculateThermodynamicsService  extends RemoteService {
	   public static class Util
	   {
	       private static CalculateThermodynamicsServiceAsync instance;

	       public static CalculateThermodynamicsServiceAsync getInstance()
	       {
	           if (instance == null)
	           {
	               instance = GWT.create(CalculateThermodynamicsService.class);
	           }
	           return instance;
	       }
	   }

	   SetOfBensonThermodynamicBaseElements CalculateThermodynamics(String type, String parameter)
				throws IOException;
}
