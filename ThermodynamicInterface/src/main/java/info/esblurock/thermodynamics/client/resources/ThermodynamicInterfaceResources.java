package info.esblurock.thermodynamics.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.resources.client.ImageResource;

public interface ThermodynamicInterfaceResources extends ClientBundle {
	@Source("topintro.html")
	  public TextResource topIntro();
	@Source("GeneralPrinciple.html")
	  public TextResource generalPrinciple();
	@Source("ExternalSymmetry.jpg")
	  public ImageResource externalSymmetry();
	@Source("2DMolecules.png")
	  public ImageResource twoDMolecular();

}
