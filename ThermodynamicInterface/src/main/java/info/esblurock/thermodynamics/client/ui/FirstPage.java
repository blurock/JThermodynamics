package info.esblurock.thermodynamics.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialTitle;
import gwt.material.design.client.ui.MaterialTooltip;
import info.esblurock.thermodynamics.base.benson.SetOfBensonThermodynamicBaseElements;
import info.esblurock.thermodynamics.client.resources.ThermodynamicInterfaceResources;
import info.esblurock.thermodynamics.client.ui.benson.SetOfBensonThermodynamicElementsCollapsible;
import info.esblurock.thermodynamics.client.view.FirstPageView;
import info.esblurock.thermodynamics.async.CalculateThermodynamicsService;
import info.esblurock.thermodynamics.async.CalculateThermodynamicsServiceAsync;

public class FirstPage extends Composite implements FirstPageView, CalculateThermodynamicsInterface  {

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
	@UiField
	MaterialCollapsible results;
	@UiField
	MaterialTooltip smilestooltip;
	@UiField
	MaterialTooltip inchitooltip;
	
	String molecule;

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
				+ "SMILES or INCHI forms. " + 
				"The SMILES and INCHI formats are converted to molecular structures using the CDK libraries." +
				"At the moment, the server is quite slow (for this development version, I took the cheapest option)," +
				"so results take more time to appear (it is not due to the methodology itself).. sorry"
				);
		smilestooltip.setText("This uses the CDK conversion to molecular structure, not guarenteed results (radical representation does not seem to work)");
		inchitooltip.setText("This uses the CDK conversion to molecular structure, not guarenteed results");
	}
	
	@UiHandler("calculatenancy")
	public void onClickNancy(ClickEvent event) {
		CalculateThermodynamicsServiceAsync async = CalculateThermodynamicsService.Util.getInstance();
		CalculateThermodynamicsCallback callback = new CalculateThermodynamicsCallback(this);
		async.CalculateThermodynamics("nancy", nancyformtext.getText(), callback);
		molecule = nancyformtext.getText();
		MaterialLoader.loading(true);	
	}

	@UiHandler("calculatesmiles")
	public void onClickSmiles(ClickEvent event) {
		CalculateThermodynamicsServiceAsync async = CalculateThermodynamicsService.Util.getInstance();
		CalculateThermodynamicsCallback callback = new CalculateThermodynamicsCallback(this);
		async.CalculateThermodynamics("smiles", smilesformtext.getText(), callback);
		molecule = smilesformtext.getText();
		MaterialLoader.loading(true);	
	}

	@UiHandler("calculateinchi")
	public void onClickInchi(ClickEvent event) {
		CalculateThermodynamicsServiceAsync async = CalculateThermodynamicsService.Util.getInstance();
		CalculateThermodynamicsCallback callback = new CalculateThermodynamicsCallback(this);
		async.CalculateThermodynamics("inchi", inchiformtext.getText(), callback);
		molecule = inchiformtext.getText();
		MaterialLoader.loading(true);	
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

	@Override
	public void setInThermo(SetOfBensonThermodynamicBaseElements elements) {
		SetOfBensonThermodynamicElementsCollapsible collapsible 
		= new SetOfBensonThermodynamicElementsCollapsible(molecule, elements);
		results.add(collapsible);
	}
}
