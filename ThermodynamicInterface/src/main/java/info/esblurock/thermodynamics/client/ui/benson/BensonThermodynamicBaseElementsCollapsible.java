package info.esblurock.thermodynamics.client.ui.benson;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import info.esblurock.thermodynamics.base.benson.BensonThermodynamicBaseElements;
import info.esblurock.thermodynamics.base.benson.HeatCapacityTemperaturePair;

public class BensonThermodynamicBaseElementsCollapsible extends Composite {

	private static BensonThermodynamicBaseElementsCollapsibleUiBinder uiBinder = GWT
			.create(BensonThermodynamicBaseElementsCollapsibleUiBinder.class);

	interface BensonThermodynamicBaseElementsCollapsibleUiBinder
			extends UiBinder<Widget, BensonThermodynamicBaseElementsCollapsible> {
	}

	public BensonThermodynamicBaseElementsCollapsible() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	MaterialLink id;
	@UiField
	MaterialLink reference;
	@UiField
	MaterialLink enthalpy;
	@UiField
	MaterialLink entropy;
	@UiField
	MaterialDropDown dpHeatCap;
	@UiField
	MaterialLink heatcapbutton;

	public BensonThermodynamicBaseElementsCollapsible(String activator, BensonThermodynamicBaseElements element) {
		initWidget(uiBinder.createAndBindUi(this));
		init();
		boolean empty = false;
		if(element.getID() != null) {
			if(element.getID().length() == 0) {
				empty = true;
			}
		} else {
			empty = true;
		}
		if(empty) {
			id.setText("Correction");
		} else {
			id.setText(element.getID());
		}
		reference.setText(element.getReference());
		double enthalpyD = element.getStandardEnthalpy().doubleValue();
		String enthalpyS = NumberFormat.getFormat("#####.####").format(enthalpyD);
		double entropyD = element.getStandardEntropy().doubleValue();
		String entropyS = NumberFormat.getFormat("#####.####").format(entropyD);
		String H = "Enthalpy (298K) = " + enthalpyS;
		enthalpy.setText(H);
		String E = "Entropy (298K) = " + entropyS;
		entropy.setText(E);
		heatcapbutton.setActivates(activator);
		dpHeatCap.setActivator(activator);
		for(HeatCapacityTemperaturePair pair : element.getSetOfHeatCapacities()) {
			double temperatureD = pair.getTemperatureValue();
			String temperatureS = NumberFormat.getFormat("####.").format(temperatureD);
			double heatcapacityD = pair.getHeatCapacityValue();
			String heatcapacityS = NumberFormat.getFormat("####.####").format(heatcapacityD);
			String valueS = heatcapacityS + " (" + temperatureS + "K)";
			MaterialLink link = new MaterialLink(valueS);
			link.setTextColor(Color.BLACK);
			dpHeatCap.add(link);
		}
		
	}
	void init() {
		heatcapbutton.setText("Head Capacity (Temperature)");
	}
}
