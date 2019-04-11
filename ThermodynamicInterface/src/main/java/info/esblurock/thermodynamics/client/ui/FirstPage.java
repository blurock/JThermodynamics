package info.esblurock.thermodynamics.client.ui;

import java.io.IOException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialParallax;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialTitle;
import info.esblurock.thermodynamics.client.resources.ThermodynamicInterfaceResources;
import info.esblurock.thermodynamics.client.view.FirstPageView;
import nu.xom.ParsingException;
import thermo.api.CalculateThermodynamicsAPI;
import thermo.data.benson.SetOfBensonThermodynamicBase;


public class FirstPage extends Composite implements FirstPageView  {

	private static FirstPageUiBinder uiBinder = GWT.create(FirstPageUiBinder.class);

	interface FirstPageUiBinder extends UiBinder<Widget, FirstPage> {
	}
	
	Presenter listener;
	ThermodyanmicsBasePage toppanel;
	ThermodynamicInterfaceResources resources = GWT.create(ThermodynamicInterfaceResources.class);


	@UiField
	MaterialPanel headerpanel;
	@UiField
	HTML introduction;
	@UiField
	MaterialImage parallax1;
	@UiField
	HTML introduction2;
	@UiField
	MaterialImage parallax2;
	@UiField
	MaterialTitle calculationtitle;
	@UiField
	MaterialLink calculatenancy;
	@UiField
	MaterialTextBox nancyformtext;
	@UiField
	MaterialLink calculatesmiles;
	@UiField
	MaterialTextBox smilesformtext;
	@UiField
	MaterialLink calculateinchi;
	@UiField
	MaterialTextBox inchiformtext;


	public FirstPage() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	public FirstPage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	void init() {
		String topintro = resources.topIntro().getText();
		introduction.setHTML(topintro);
		String principle = resources.generalPrinciple().getText();
		introduction2.setHTML(principle);
		ImageResource externalSymmetryR = resources.twoDMolecular();
		//Image externalSymmetryI = new Image(externalSymmetryR.getSafeUri());
		parallax2.setResource(externalSymmetryR);
		
		calculationtitle.setTitle("Calculate Thermodynamics from 2D Structure");
		calculationtitle.setDescription("Calculate the thermodynamics from either the nancy, "
				+ "smiles or INCHI forms. " + 
				"");
	}
	
	@UiHandler("calculatenancy")
	public void onClickNancy(ClickEvent event) {
		try {
			MaterialLoader.loading(true);	
			SetOfBensonThermodynamicBase set = 
					CalculateThermodynamicsAPI.calculate(CalculateThermodynamicsAPI.nancyParameterS, 
					nancyformtext.getText());
			Window.alert(set.toString());
			MaterialLoader.loading(false);	
		} catch (IOException | ParsingException e) {
			MaterialLoader.loading(false);	
			e.printStackTrace();
		}
		
	}

	@Override
	public void setPresenter(Presenter listener) {
		this.listener = listener;
	}

	@Override
	public void asNewUser() {
	}

	@Override
	public void asExistingUser() {
	}

	@Override
	public void setTopPanel(ThermodyanmicsBasePage toppanel) {
		this.toppanel = toppanel;
	}

	@Override
	public void setName(String helloName) {
	}
}
